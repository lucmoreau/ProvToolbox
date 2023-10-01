package org.openprovenance.prov.scala.interop
import org.openprovenance.prov.model.Namespace
import org.openprovenance.prov.scala.immutable._
import org.openprovenance.prov.scala.nf.CommandLine.{parseDocument, parseDocumentToNormalForm, toBufferedSource}
import org.openprovenance.prov.scala.nlgspec_transformer.Environment
import org.openprovenance.prov.scala.query.{Processor, StatementAccessor, SummaryQueryGenerator}
import org.openprovenance.prov.scala.summary._
import org.openprovenance.prov.scala.summary.types.{FlatType, ProvType}
import org.openprovenance.prov.scala.viz.{Graphics, SummaryGraphics}

import java.io.{BufferedWriter, File, FileWriter}
import java.text.SimpleDateFormat
import java.util.Calendar
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.collection.parallel.CollectionConverters._

class UtilsSummary (pf:ProvFactory) {


  def summaryDraw(config: Config): Unit = {
    val counts = config.from to config.to
    val (ind, s) = sum(config.infile, config)
    val params = nowParam()
    counts.foreach(level => summaryDraw(config, s, ind, level, params))
  }

  def summarize(config: Config): Unit = {
    val counts = config.from to config.to
    val allDescriptions: mutable.Map[Int, SummaryDescriptionJson] = mutable.Map()

    val (ind, s) = sum(config.infile, config)
    val params = nowParam()
    counts.foreach(level => {
      summarize(config, s, ind, level, params, allDescriptions)
    })
  }


  def summarize(config: Config,
                s: TypePropagator,
                ind: Indexer,
                level: Int,
                params: Map[String, String],
                allDescriptions: mutable.Map[Int, SummaryDescriptionJson]): Unit = {
    val summaryIndex: SummaryIndex = SummaryAPI.makeSummaryIndex(config, s, ind, level, params, allDescriptions.get(0))

    val theformats = config.theOutputFormats()

    val params2 = params ++ Map(Format.levelVar -> level.toString)

    if (!config.kernel) {
      config.outfiles.zip(theformats).par foreach { case (o: Output, format: Format.Format) => Format2.outputers(format).output(summaryIndex, o, params2) }

      if (config.types != null) {
        exportToJsonDescription(summaryIndex, config, params2, level, allDescriptions)
      }
    }

    if (config.features != null) {
      exportToJsonFeatures(summaryIndex, config.features, params2)
    }
  }

  // Share some code with original

  def summaryDraw(config: Config, s: TypePropagator, ind: Indexer, level: Int, params: Map[String, String]): Unit = {

    val summaryIndex = SummaryAPI.makeSummaryIndex(config, s, ind, level, params, None)

    val theformats = config.theOutputFormats()

    val params2 = params ++ Map(Format.levelVar -> level.toString, Format.summarypVar -> "")
    val params3 = params ++ Map(Format.levelVar -> level.toString, Format.summarypVar -> "-summary")

    val annotatedDocument: Document = annotateWithProvenanceTypes(ind, summaryIndex, summaryp = false)
    val annotatedSummary: Document = annotateWithProvenanceTypes(summaryIndex, summaryIndex, summaryp = true)

    config.outfiles.zip(theformats).par foreach { case (o: Output, format: Format.Format) => Format2.outputers(format).output(annotatedDocument, o, params2) }

    config.outfiles.zip(theformats).par foreach { case (o: Output, format: Format.Format) => Format2.outputers(format).output(annotatedSummary, o, params3) }

    if (config.types != null) {
      exportToJsonDescription(summaryIndex, config, params2, level, mutable.Map()) //LUC: note that here I do not include a persistent map to accumulate all descriptionns
    }
    if (config.features != null) {
      exportToJsonFeatures(summaryIndex, config.features, params2)
    }
  }

  val summaryNamespaceURI = "http://openprovenance.org/summary/ns#"
  val summaryPrefix = "sum"

  def annotateWithProvenanceTypes(ind: Indexing, summaryIndex: SummaryIndex, summaryp: Boolean): Document = {
    val base: Map[String, Int] = summaryIndex.mapToBaseUri.flatMap { case (id, urls) => urls.map(url => (url, id)) }

    val idsVec: Iterable[QualifiedName] = ind.idsVec

    val amap: Map[QualifiedName, Int] = summaryIndex.amap

    val toType: Map[Int, Set[ProvType]] = summaryIndex.provTypeIndex.map { case (x, y) => (y, x) }

    val ns = new Namespace(ind.document().namespace)
    ns.register(summaryPrefix, summaryNamespaceURI)
    ns.register("dot", Graphics.DOT_NS)
    Namespace.withThreadNamespace(ns)


    val newStatements: Iterable[Statement] = addProvenanceTypeToNodes(idsVec, base, toType, ind, summaryp) ++ ind.document().statements().collect { case rel: Relation => rel }

    new Document(newStatements, ns)
  }

  def addProvenanceTypeToNodes(idsVec: Iterable[QualifiedName],
                               base: Map[String, Int],
                               toType: Map[Int, Set[ProvType]],
                               ind: Indexing,
                               summaryp: Boolean): Iterable[Statement] = {
    idsVec.zipWithIndex.toMap.map { case (q, i) =>
      val summary_id = if (summaryp) i else base(q.getUri())
      val provenance_type = toType(summary_id)
      val node: Node = ind.nodes(i)

      val node2: Statement = node.addAttributes(Set(newTypeAttribute(provenance_type),
        newFillColorAttribute(provenance_type),
        newFontColorAttribute(provenance_type)))

      //(q, provenance_type,node)
      node2
    }
  }

  def newTypeAttribute(provenance_type: Set[ProvType]): Attribute = {
    Attribute(pf.newAttribute(pf.newQualifiedName(summaryNamespaceURI, "ptype", summaryPrefix), provenance_type.toString(), ProvFactory.pf.xsd_string))
  }

  def newFillColorAttribute(provenance_type: Set[ProvType]): Attribute = {
    Attribute(pf.newAttribute(Graphics.fillcolorQN, SummaryGraphics.toFillColor(provenance_type), ProvFactory.pf.xsd_string))
  }

  def newFontColorAttribute(provenance_type: Set[ProvType]): Attribute = {
    Attribute(pf.newAttribute(Graphics.fontcolorQN, SummaryGraphics.toFontColor(provenance_type), ProvFactory.pf.xsd_string))
  }

  def compare(in: Input, withfile: Input, nf: Int, config: Config): Unit = {
    val doc1 = parseDocumentToNormalForm(in)
    val doc2 = parseDocumentToNormalForm(withfile)

    val same = doc1.equals(doc2)

    println("comparing doc1 doc2 " + same)

    if (!same) {
      println("doc1 - doc2")
      println(doc1.toDocument.statementOrBundle.toSet -- doc2.toDocument.statementOrBundle.toSet)
      println("doc2 - doc2")
      println(doc2.toDocument.statementOrBundle.toSet -- doc1.toDocument.statementOrBundle.toSet)
    }

  }


  def summary_compare(inSummary: Input, descriptionFile: Input, withSummary: Input, withSummaryDescription: Input, config: Config): Unit = {
    println("summary_compare => ...")

    val params = nowParam()


    val source1 = toBufferedSource(descriptionFile).bufferedReader()
    val source2 = toBufferedSource(withSummaryDescription).bufferedReader()

    val desca = TypePropagator.om.readValue(source1, classOf[SummaryDescriptionJson])
    val descb = TypePropagator.om.readValue(source2, classOf[SummaryDescriptionJson])

    val nsa = NamespaceHelper.toNamespace(desca.prefixes)
    val nsb = NamespaceHelper.toNamespace(descb.prefixes)

    val veca = mapToSortedVector(desca.names).map(s => nsa.stringToQualifiedName(s, pf).asInstanceOf[QualifiedName])
    val vecb = mapToSortedVector(descb.names).map(s => nsb.stringToQualifiedName(s, pf).asInstanceOf[QualifiedName])

    val doca = new Indexer(parseDocument(inSummary), veca)
    val docb = new Indexer(parseDocument(withSummary), vecb)


    println(desca)
    println(doca.document())

    println(descb)
    println(docb.document())

    val index1 = new SummaryIndex(desca, doca)
    val index2 = new SummaryIndex(descb, docb)

    val (desc1, desc2, desc3, desc4) = index1.diff(index2)


    val oDoca = new OrderedDocument(doca.document())
    val oDocb = new OrderedDocument(docb.document())

    val oDoc1 = new OrderedDocument(index1.document())
    val oDoc2 = new OrderedDocument(index2.document())


    val doc1 = TypePropagator.highlight(oDoc1, desc1)
    val doc2 = TypePropagator.highlight(oDoc1, desc2)
    val doc3 = TypePropagator.highlight(oDoc2, desc3)
    val doc4 = TypePropagator.highlight(oDoc2, desc4)


    val theformats = config.theOutputFormats()

    List((oDoca, 0, null), (doc1, 1, desc1), (doc2, 2, desc2), (doc3, 3, desc3), (doc4, 4, desc4), (oDocb, 5, null)) foreach {
      case (doc, count, desc) =>
        val params2 = params ++ Map("%kind" -> count.toString)
        config.outfiles.zip(theformats).par foreach { case (o: Output, format: Format.Format) =>
          Format2.outputers(format).output(doc, o, params2)
        }
        if (desc != null)
          exportToJsonDescription(desc, config.description, params2)

    }


    println("... => summary_compare end.")

  }

  def normalizeOLDSTUFF(config: Config): Unit = {
    val in: Input = config.infile
    val doc = parseDocumentToNormalForm(in)
    Namespace.withThreadNamespace(doc.namespace)

    println(doc.toString())
  }

  def exportQueriesForType(desc: SummaryDescriptionJson,
                           config: Config,
                           params: Map[String, String],
                           level: Int,
                           allDescriptions: mutable.Map[Int, SummaryDescriptionJson]): Unit = {

    if (config.summary_queries != null) {

      val previousLevels: collection.Set[Int] = allDescriptions.keySet - level


      val previousFlatTypes: Map[Int, Map[Int, Set[FlatType]]] = previousLevels.map(l => l -> allDescriptions(l).getFlatTypes).toMap
      val allTypeMaps: Map[Int, Map[Int, Set[FlatType]]] = previousFlatTypes + (level -> allDescriptions(level).getFlatTypes(level, allDescriptions))
      val allTypeStrings: Map[Int, Map[String, Int]] = allDescriptions.map { case (level, v) => (level, v.typeStrings.map { case (k, v) => (v, k) }) }.toMap
      val allTypeStringsR: Map[Int, Map[Int, String]] = allDescriptions.map { case (level, v) => (level, v.typeStrings) }.toMap
      val allTypeCounts: Map[Int, Map[Int, Int]] = allDescriptions.map { case (level, v) => (level, v.getFeatures.map { case (k, v) => (allTypeStrings(level)(k), v) }) }.toMap

      val typeMap: Map[Int, Set[FlatType]] = allTypeMaps(level)
      val typeStrings: Map[String, Int] = allTypeStrings(level)
      val typeCount: Map[Int, Int] = allTypeCounts(level)


      val context: Map[String, String] = Map()
      val environment = Environment(context, null, null, new Array[String](0), List())

      val statementAccessorForDocument: (Option[String] => StatementAccessor) = (s: Option[String]) => {
        null
      }
      val engine = new Processor(statementAccessorForDocument, environment) with SummaryQueryGenerator

      val selectedTypes: Set[Int] = typeMap.keySet

      println("exportQueriesForType " + level)
      //println(allDescriptions)
      //println(allTypeMaps)
      selectedTypes.foreach(selectedType => {
        queryExporter(desc, config, params, level, allTypeMaps, allTypeStringsR, typeCount, engine, selectedType)
      })

    }
  }

  def queryExporter(desc: SummaryDescriptionJson,
                    config: Config,
                    params: Map[String, String],
                    level: Int,
                    allTypeMaps: Map[Int, Map[Int, Set[FlatType]]],
                    allTypeStringsR: Map[Int, Map[Int, String]],
                    typeCount: Map[Int, Int],
                    engine: Processor with SummaryQueryGenerator,
                    selectedType: Int): Unit = {
    val params2 = params ++ Map(Format.typeVar -> selectedType.toString)
    val fileName = Format.substParams(config.summary_queries.toString, params2)
    val fw = new FileWriter(fileName)

    fw.write("// query for type " + selectedType + ":\n")
    fw.write("//  type " + selectedType + " is " + desc.typeStrings(selectedType) + "\n")
    val queryString: ArrayBuffer[String] = engine.flatType2Query(selectedType, level, allTypeMaps, allTypeStringsR, typeCount, engine.QueryDirectiveTop())
    fw.write(queryString.mkString)
    fw.write("\n")
    fw.write("//end\n\n")
    fw.close()
  }

  def exportToJsonDescription(desc: SummaryDescriptionJson, file: File, params: Map[String, String]): Unit = {
    val fileName = Format.substParams(file.toString, params)
    val bw = new BufferedWriter(new FileWriter(fileName))
    SummaryIndex.exporToJsonDescription(bw, desc)
    bw.close()

  }


  def exportToJsonFeatures(index: SummaryIndex, file: File, params: Map[String, String]): Unit = {
    val fileName = Format.substParams(file.toString, params)
    val bw = new BufferedWriter(new FileWriter(fileName))
    index.exportToFeatures(bw)
    bw.close()
  }



  def exportToJsonDescription(index: SummaryIndex,
                              config:Config,
                              params: Map[String,String],
                              level:Int,
                              allDescriptions:mutable.Map[Int,SummaryDescriptionJson]): Unit = {
    val file=config.types
    val fileName=Format.substParams(file.toString,params)
    val bw=new BufferedWriter(new FileWriter(fileName))
    val summaryDescriptionJson: SummaryDescriptionJson =index.exportToJsonDescription(bw)
    bw.close()
    allDescriptions += (level -> summaryDescriptionJson)

    // and now optionally export queries for all types
    exportQueriesForType(summaryDescriptionJson, config, params, level, allDescriptions)
  }



  def sum(in: Input, config: Config): (Indexer,TypePropagator) = {
    val doc=parseDocument(in)  // to check why this is in nf module?
    SummaryAPI.sum(doc,config)
  }

  def sum(doc: Document, config: Config): (Indexer, TypePropagator) = {
    SummaryAPI.sum(doc,config)
  }

  def nowParam(): Map[String, String] = {
    val now = Calendar.getInstance().getTime
    val formatter: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-hh.mm.ss")
    val params = Map(Format.dateVar -> formatter.format(now))
    params
  }

  private def mapToSortedVector(names: Map[String, Int]): Vector[String] = {
    names.toSeq.sortWith((p1, p2) => p1._2 < p2._2).map(_._1).toVector
  }


}
