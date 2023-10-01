package org.openprovenance.prov.scala.interop
import com.fasterxml.jackson.databind.SerializationFeature
import org.openprovenance.prov.model.Namespace
import org.openprovenance.prov.nlg.ValidationObjectMaker
import org.openprovenance.prov.scala.iface.{Explainer, Narrator, XFactory}
import org.openprovenance.prov.scala.immutable._
import org.openprovenance.prov.scala.narrator.{EventsDescription, NarratorFunctionality}
import org.openprovenance.prov.scala.nf.CommandLine.parseDocument
import org.openprovenance.prov.scala.nf.xml.{XmlNfBean, XmlSignature}
import org.openprovenance.prov.scala.nf.{DocumentProxyFromStatements, Normalizer}
import org.openprovenance.prov.scala.nlgspec_transformer.Environment
import org.openprovenance.prov.scala.query.{Processor, StatementAccessor, SummaryQueryGenerator}
import org.openprovenance.prov.scala.streaming._
import org.openprovenance.prov.scala.summary._
import org.openprovenance.prov.scala.summary.types.{FlatType, ProvType}
import org.openprovenance.prov.scala.template.{BindingKind, Expander}
import org.openprovenance.prov.scala.viz.{Graphics, SummaryGraphics}
import org.openprovenance.prov.scala.xplain.{Narrative, RealiserFactory}
import org.openprovenance.prov.template.{Bindings, BindingsJson}
import org.openprovenance.prov.validation.report.ValidationReport
import org.openprovenance.prov.validation.{EventMatrix, Validate}
import scopt.OptionParser

import java.io._
import java.security.KeyStore
import java.security.cert.X509Certificate
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.xml.namespace.QName
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.collection.parallel.CollectionConverters._





object CommandLine {
  import org.openprovenance.prov.scala.immutable.ProvFactory.pf


  val parser: OptionParser[Config] = ProvmanOptionParser.parser

  def exportToJson(outw: java.io.Writer, any: AnyRef): Unit = {
    TypePropagator.om.enable(SerializationFeature.INDENT_OUTPUT);
    TypePropagator.om.writeValue(outw, any)
  }

  def narrativeExport(config: Config,
                      text: Map[String,Narrative],
                      doc2: Document,
                      mat: EventMatrix,
                      descriptor: EventsDescription): Unit = {
    if (config.description != null) {
      val fileName = config.description.toString
      val bw = new BufferedWriter(new FileWriter(fileName))
      exportToJson(bw, descriptor)
    }


    if (config.outfiles.nonEmpty) {
      outputer(doc2, config)
    }

    if (config.snlg!=null) {
      val m=NarratorFunctionality.getSnlgOnly(text)
      textOutputer(m, config.snlg,jsonSerialized = true)
    }

    if (config.matrix != null) {
      val mat_text = mat.displayMatrix2()
      output(mat_text, config.matrix)
    }

    textOutputer(narrator.getTextOnly(text),config.text)

  }




  def explanationExport(config: Config,
                        text: Map[String,Narrative],
                        doc2: Document): Unit = {


    if (config.outfiles.nonEmpty) {
      outputer(doc2, config)
    }

    if (config.snlg!=null) {
      val m=NarratorFunctionality.getSnlgOnly(text)
      textOutputer(m, config.snlg, jsonSerialized = true)
    }

    textOutputer(narrator.getTextOnly(text),config.text)

  }

  val xFactory: XFactory =new XFactory()
  val narrator:  Narrator  = xFactory.makeNarrator
  val explainer: Explainer = xFactory.makeExplainer

  def main(args: Array[String]): Unit = {
      parser.parse(args, Config()) match {
      case Some(config) =>

         config.command match {
           case "summary"   => summarize(config)
           case "kernelize"   => summarize(config.copy(kernel=true))
           case "summaryDraw"   => summaryDraw(config)
           case "summary.compare"   => summary_compare(config.infile, config.summaryDescriptionFile,
                                                       config.withSummaryFile, config.withSummaryDescriptionFile, config)
           case "normalize.old" => normalizeOLDSTUFF(config)
           case "normalize" => normalize(config)
           case "sign"      => sign(config)
           case "signature" => signature(config)
           case "translate" => translate(config.infile, config)
           case "expand" => if (config.time==null) { expandExport(config.infile, config) } else {expandTime(config.infile, config,config.time)}
           case "validate" => validate(config.infile, config)
           case "narrate"  =>
             val in: Input = config.infile
             val docIn = parseDocument(in)
             val (text, doc, mat, descriptor)=narrator.narrate(docIn,config)
             narrativeExport(config,text,doc,mat,descriptor)

           case "explain"  =>
             val in: Input = config.infile
             val doc = parseDocument(in)
             val text=explainer.explain(doc,config)
             explanationExport(config,text,doc)

           case "bindings"  => bindings(config)
           case "bindings.v2"  => bindings_v2(config)
           case "bindings.v3"  => bindings_v3(config)
           case "batch"        => batch_processing(config)
           case "compare"      => compare(config.infile, config.withfile, config.nf, config)
           case "config"   => configuration()
           case "blockly"      => toBlockly(config.withfile, config.blockly, config.xplan)


         }

         
      case None =>      // arguments are bad, error message will have been displayed
      }
  }
  
  def configuration(): Unit = {
	  val classpath = System.getProperty("java.class.path")
	  println("provman.classpath=" + classpath)
	  println("provman.main=" + getClass.getName)
  }

  import org.openprovenance.prov.scala.nf.CommandLine.{parseDocument, parseDocumentToNormalForm, toBufferedSource}

  private def normalizeOLDSTUFF(config:Config): Unit = {
	  val in:Input=config.infile
	  val doc=parseDocumentToNormalForm(in)
    Namespace.withThreadNamespace(doc.namespace)
  
    println(doc.toString())
  }
  
  def exportToXMLFile(out: Output, doc: DocumentProxyFromStatements, id: String): Unit = {
    out match {
      case FileOutput(f) => XmlNfBean.toXMLFile(f.toString(),doc,id)
      case StandardOutput() => XmlNfBean.toXMLOutputStream(doc,id)
      case _  => new UnsupportedOperationException
    }
  }

  def exportToOutput(out: Output, doc: ByteArrayOutputStream): Unit = {
    out match {
      case FileOutput(f) =>
        val os=new PrintWriter(f)
        os.print(doc)
        os.close()
      case StreamOutput(os) =>
        new PrintWriter(os).print(doc)
        os.close()
      case StandardOutput() => System.out.print(doc)
    }
  }
  def exportToOutput(out: Output, doc: String): Unit = {
    out match {
      case FileOutput(f) =>
        val os=new PrintWriter(f)
        os.print(doc)
        os.close()
      case StandardOutput() => System.out.print(doc)
      case StreamOutput(_) => throw new UnsupportedOperationException
    }
  }


  def normalize(config:Config): Unit = {
	  val in:Input=config.infile
		val doc=parseDocument(in)
		val nfDoc=Normalizer.fusion(doc)

		val out:Output=config.outfiles.head // select one of them!
		// use config.pretty?
		exportToXMLFile(out,nfDoc,null)


  }

  def sign1(config:Config): (X509Certificate, ByteArrayOutputStream) ={
	  val in:Input=config.infile
		val doc=parseDocument(in)
		val nfDoc=Normalizer.fusion(doc)


		val baos=new ByteArrayOutputStream


	  val store=config.store
	  val storepass=config.storepass
	  val key=config.key
	  val keypass=config.keypass


		val keyStore = KeyStore.getInstance("jks")
	  keyStore.load(new FileInputStream(store), storepass.toCharArray)
    val cryptoKey = keyStore.getKey(key, keypass.toCharArray)
    val cert = keyStore.getCertificate(key).asInstanceOf[X509Certificate]


	  val pipeIn=XmlNfBean.serializeToPipe(nfDoc,config.id)
	  XmlSignature.doSign( XmlSignature.toStreamReader(pipeIn), baos, cryptoKey, cert)
	  (cert,baos)
  }

  def sign(config:Config): Unit = {
    val (_,baos)=sign1(config)

	  val out:Output=config.outfiles.head // select one of them!

	  exportToOutput(out,baos)

  }

  def signature(config:Config): Unit = {
	  val (cert,baos)=sign1(config)


    val arr=baos.toByteArray

    val namesToSign = List(new QName("document"))


    val listener=XmlSignature.getEventListenerUsingStAX(new ByteArrayInputStream(arr),namesToSign,cert)
    val (sign,token,algos,signedElementEvents)=XmlSignature.extractSignature(listener,namesToSign)


    val out:Output=config.outfiles.head // select one of them!

	  exportToOutput(out,sign)
	  algos.foreach(algo => {
		  println(algo.getAlgorithmURI)
		  println(algo.getAlgorithmUsage)
		  println(algo.getKeyLength)
		  println(algo.getCorrelationID)
		  println(algo.getSecurityEventType)
	  })  
	  
	  signedElementEvents.foreach(signedElementEvent => {
	                                println("id " + signedElementEvent.getCorrelationID)
	                                println("path " + signedElementEvent.getElementPath)
	                                println("token " + signedElementEvent.getSecurityToken)
	                                println("order " + signedElementEvent.getProtectionOrder)
	  })
	  
	  println("token id " + token.getId)
	  println("token id " + token.getElementPath)
	  println("token id " + token.getSha1Identifier)

	                                
  }
  


  def summaryDraw(config:Config): Unit = {
    val counts=config.from to config.to
    val (ind, s)=sum(config.infile,config)
    val params = nowParam()
    counts.foreach(level => summaryDraw(config,s,ind,level,params))
  }

  def summarize(config:Config): Unit = {
    val counts=config.from to config.to
    val allDescriptions: mutable.Map[Int,SummaryDescriptionJson] = mutable.Map()

    val (ind, s)=sum(config.infile,config)
    val params = nowParam()
    counts.foreach(level => {
      summarize(config, s, ind, level, params, allDescriptions)
    })
  }

      
  def summarize(config:Config,
                s:TypePropagator,
                ind:Indexer,
                level: Int,
                params: Map[String,String],
                allDescriptions:mutable.Map[Int,SummaryDescriptionJson]): Unit = {
      val summaryIndex: SummaryIndex =SummaryAPI.makeSummaryIndex(projectConfiguration(config),s,ind,level,params,allDescriptions.get(0))
      
      val theformats=config.theOutputFormats()

      val params2=params ++ Map(Format.levelVar -> level.toString)

      if (!config.kernel) {
        config.outfiles.zip(theformats).par foreach { case (o: Output, format: Format.Format) => Format2.outputers(format).output(summaryIndex, o, params2) }

        if (config.types != null) {
          exportToJsonDescription(summaryIndex, config, params2, level, allDescriptions)
        }
      }

      if (config.features!=null) {
    	  exportToJsonFeatures(summaryIndex,config.features,params2)
      }
  }

  // Share some code with original

  def summaryDraw(config:Config, s:TypePropagator, ind:Indexer, level: Int, params: Map[String,String]): Unit = {

    val summaryIndex=SummaryAPI.makeSummaryIndex(projectConfiguration(config),s,ind,level,params, None)

    val theformats=config.theOutputFormats()

    val params2=params ++ Map(Format.levelVar -> level.toString, Format.summarypVar -> "")
    val params3=params ++ Map(Format.levelVar -> level.toString, Format.summarypVar -> "-summary")

    val annotatedDocument: Document =annotateWithProvenanceTypes(ind, summaryIndex, summaryp = false)
    val annotatedSummary: Document =annotateWithProvenanceTypes(summaryIndex, summaryIndex, summaryp = true)

    config.outfiles.zip(theformats).par foreach { case (o:Output,format: Format.Format) =>  Format2.outputers(format).output(annotatedDocument, o, params2)  }

    config.outfiles.zip(theformats).par foreach { case (o:Output,format: Format.Format) =>  Format2.outputers(format).output(annotatedSummary, o, params3)  }

    if (config.types!=null) {
      exportToJsonDescription(summaryIndex,config,params2, level, mutable.Map())  //LUC: note that here I do not include a persistent map to accumulate all descriptionns
    }
    if (config.features!=null) {
      exportToJsonFeatures(summaryIndex,config.features,params2)
    }
  }
  val summaryNamespaceURI = "http://openprovenance.org/summary/ns#"
  val summaryPrefix = "sum"

  def annotateWithProvenanceTypes(ind: Indexing, summaryIndex: SummaryIndex, summaryp: Boolean): Document = {
    val base: Map[String, Int] = summaryIndex.mapToBaseUri.flatMap { case (id, urls) => urls.map(url => (url, id)) }

    val idsVec: Iterable[QualifiedName] = ind.idsVec

    val amap: Map[QualifiedName, Int] = summaryIndex.amap

    val toType: Map[Int, Set[ProvType]] = summaryIndex.provTypeIndex.map { case (x, y) => (y, x) }

    val ns=new Namespace(ind.document().namespace)
    ns.register(summaryPrefix,summaryNamespaceURI)
    ns.register("dot",Graphics.DOT_NS)
    Namespace.withThreadNamespace(ns)


    val newStatements: Iterable[Statement] =addProvenanceTypeToNodes(idsVec,base,toType,ind,summaryp)  ++ ind.document().statements().collect{case rel:Relation => rel}

    new Document(newStatements,ns)
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


  def exportQueriesForType(desc: SummaryDescriptionJson,
                           config:Config,
                           params: Map[String,String],
                           level:Int,
                           allDescriptions:mutable.Map[Int,SummaryDescriptionJson]): Unit = {

    if (config.summary_queries!=null) {

      val previousLevels: collection.Set[Int] =allDescriptions.keySet - level


      val previousFlatTypes: Map[Int, Map[Int, Set[FlatType]]] = previousLevels.map(l => l -> allDescriptions(l).getFlatTypes).toMap
      val allTypeMaps: Map[Int, Map[Int, Set[FlatType]]] =previousFlatTypes + (level -> allDescriptions(level).getFlatTypes(level,allDescriptions))
      val allTypeStrings:  Map[Int, Map[String, Int]] =allDescriptions.map{case (level,v) => (level,v.typeStrings.map { case (k, v) => (v, k) })}.toMap
      val allTypeStringsR: Map[Int, Map[Int, String]] =allDescriptions.map{case (level,v) => (level,v.typeStrings)}.toMap
      val allTypeCounts:  Map[Int, Map[Int, Int]] =allDescriptions.map{case (level,v) => (level,v.getFeatures.map { case (k, v) => (allTypeStrings(level)(k), v) })}.toMap

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
        queryExporter(desc,config,params,level,allTypeMaps,allTypeStringsR,typeCount,engine, selectedType)
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
                    selectedType: Int): Unit  =  {
    val params2=params ++ Map(Format.typeVar -> selectedType.toString)
    val fileName=Format.substParams(config.summary_queries.toString,params2)
    val fw=new FileWriter(fileName)

    fw.write("// query for type " + selectedType + ":\n")
    fw.write("//  type " + selectedType + " is " + desc.typeStrings(selectedType) + "\n")
    val queryString: ArrayBuffer[String] = engine.flatType2Query(selectedType, level, allTypeMaps, allTypeStringsR, typeCount, engine.QueryDirectiveTop())
    fw.write(queryString.mkString)
    fw.write("\n")
    fw.write("//end\n\n")
    fw.close()
  }

  def exportToJsonDescription(desc: SummaryDescriptionJson, file: File, params: Map[String,String]): Unit = {
     val fileName=Format.substParams(file.toString,params)
     val bw=new BufferedWriter(new FileWriter(fileName))
     SummaryIndex.exporToJsonDescription(bw,desc)
     bw.close()

  }


   def exportToJsonFeatures(index: SummaryIndex, file: File, params: Map[String,String]): Unit = {
     val fileName=Format.substParams(file.toString,params)
     val bw=new BufferedWriter(new FileWriter(fileName))
     index.exportToFeatures(bw)
     bw.close()
  }

  def compare (in:Input, withfile: Input, nf: Int, config:Config): Unit = {
	  val doc1=parseDocumentToNormalForm(in)
	  val doc2=parseDocumentToNormalForm(withfile)

	  val same=doc1.equals(doc2)

	  println("comparing doc1 doc2 " + same)

	  if (!same) {
	    println("doc1 - doc2")
	    println(doc1.toDocument.statementOrBundle.toSet -- doc2.toDocument.statementOrBundle.toSet)
	    println("doc2 - doc2")
	    println(doc2.toDocument.statementOrBundle.toSet -- doc1.toDocument.statementOrBundle.toSet)
	  }

  }

  def nowParam(): Map[String, String] = {
     val now = Calendar.getInstance().getTime
     val formatter:SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-hh.mm.ss")
     val params = Map(Format.dateVar -> formatter.format(now))
     params
  }

  def mapToSortedVector (names: Map[String,Int]): Vector[String] = {
    names.toSeq.sortWith((p1,p2)=> p1._2 < p2._2).map(_._1).toVector
  }

  def summary_compare(inSummary: Input, descriptionFile:Input, withSummary: Input,  withSummaryDescription: Input, config:Config): Unit = {
    println("summary_compare => ...")

    val params=nowParam()


    val source1= toBufferedSource(descriptionFile).bufferedReader()
    val source2= toBufferedSource(withSummaryDescription).bufferedReader()

    val desca=TypePropagator.om.readValue(source1, classOf[SummaryDescriptionJson])
    val descb=TypePropagator.om.readValue(source2, classOf[SummaryDescriptionJson])

    val nsa=NamespaceHelper.toNamespace(desca.prefixes)
    val nsb=NamespaceHelper.toNamespace(descb.prefixes)

    val veca=mapToSortedVector(desca.names).map(s => nsa.stringToQualifiedName(s, pf).asInstanceOf[QualifiedName])
    val vecb=mapToSortedVector(descb.names).map(s => nsb.stringToQualifiedName(s, pf).asInstanceOf[QualifiedName])

    val doca = new Indexer(parseDocument(inSummary), veca)
    val docb = new Indexer(parseDocument(withSummary), vecb)


    println(desca)
    println(doca.document())

    println(descb)
    println(docb.document())

    val index1=new SummaryIndex(desca,doca)
    val index2=new SummaryIndex(descb,docb)

    val (desc1,desc2,desc3,desc4)=index1.diff(index2)


    val oDoca=new OrderedDocument(doca.document())
    val oDocb=new OrderedDocument(docb.document())

    val oDoc1=new OrderedDocument(index1.document())
    val oDoc2=new OrderedDocument(index2.document())


    val doc1=TypePropagator.highlight(oDoc1,desc1)
    val doc2=TypePropagator.highlight(oDoc1,desc2)
    val doc3=TypePropagator.highlight(oDoc2,desc3)
    val doc4=TypePropagator.highlight(oDoc2,desc4)




    val theformats=config.theOutputFormats()

    List((oDoca,0,null) , (doc1,1,desc1), (doc2,2,desc2), (doc3,3,desc3), (doc4,4,desc4), (oDocb,5,null)) foreach {
                                 case (doc,count,desc) =>
                                    val params2=params ++ Map("%kind" -> count.toString)
                                    config.outfiles.zip(theformats).par foreach { case (o:Output,format: Format.Format) =>
                                                                                     Format2.outputers(format).output(doc, o, params2)  }
                                    if (desc!=null)
                                    exportToJsonDescription(desc, config.description, params2)

                                    }
   
    
    println("... => summary_compare end.")

  }
  
  def translate (in:Input, config:Config): Unit = {
    if (config.streaming) {
      translate_streaming(in,config)
    } else {
      translate_non_streaming(in,config)
    }
  }

  def translate_non_streaming (in:Input, config:Config):Unit = {
      val doc=parseDocument(in)
      Namespace.withThreadNamespace(doc.namespace)
      if (config.query==null) {
        outputer(doc, config)
      } else {
        processQueryAndOutput(doc, config)
      }
  }

  def processQueryAndOutput(doc: Document, config: Config): Unit = {
    import scala.jdk.CollectionConverters._
    val query: Input = config.query
    val context: Map[String, String] = doc.namespace.getPrefixes.asScala.toMap
    val environment=Environment(context,null,null,new Array[String](0),List())

    val rf=new RealiserFactory(config)

    val accessor: StatementAccessor =rf.makeStatementAccessor(doc.statements().toSeq)

    //val alternate_files: Map[String, String] =if (config.infiles!=null) SpecLoader.mapper.readValue(config.infiles,classOf[Map[String,String]]) else Map[String,String]()

    val accessors: Map[String,StatementAccessor]=rf.accessors.map{case (s,ss) => (s,rf.makeStatementAccessor(ss))} //alternate_files.map{case (s,f) => (s, rf.makeStatementAccessor(parseDocument(new FileInput(new File(f))).statements().toSeq))}



    val statementAccessorForDocument:(Option[String]=>StatementAccessor) = {
      case None => accessor
      case Some(s) => accessors(s)
    }


    val source = scala.io.Source.fromFile(query.asInstanceOf[FileInput].f)
    val queryContents: String = try source.mkString finally source.close()


    val engine=new Processor(statementAccessorForDocument, environment)
    val set: mutable.Set[engine.Record] =engine.newRecords()
    engine.evalAccumulate(queryContents, set)
    val result: Set[engine.RField] = engine.toFields(set)
    println("found " + set.size + " records (and " + result.size + " fields)")
    val statementOrBundle: Iterable[StatementOrBundle]=null
    val doc2 = new Document(statementOrBundle, doc.namespace)
    outputer(doc2, config)
  }

  def outputer(doc:Document, config:Config): Unit = {
    val theformats=config.theOutputFormats()
             
    config.outfiles.zip(theformats).par foreach { case (o:Output,format: Format.Format) =>  Format2.outputers(format).output(doc, o, Map[String,String]())  }
     
  }
  
  def inputer(config:Config):Document = {
    val format=config.theInputFormat()           
    val d=Format.inputers(format).input(config.infile, Map[String,String]())           
    d
  }

  def textOutputer(text: Map[String,List[String]], config_text: Output, jsonSerialized:Boolean=false): Unit = {
    if (config_text != null) {
      val format = config_text match {
        case StandardOutput() => "txt"
        case FileOutput(f) => f.getName.substring(f.getName.lastIndexOf('.') + 1)
      }

      format match {
        case "json" =>
          if (!jsonSerialized) {
            TypePropagator.om.writeValue(config_text.asInstanceOf[FileOutput].f, text.map{case (k,vv) => (k, vv.mkString("  "))})
          } else {
            output(text.map{case (k,vv) => "\"" + k + "\": " + vv.mkString("  ")}.mkString("{", ", ", "}"), config_text)
          }
        case _ => output(text.map { case (k, v) => (k, v.mkString("  ")) }.mkString("", "\n", ""), config_text)

      }
    }
  }



  
  def translate_streaming (in:Input, config:Config): Unit = {

      val theformats=config.theOutputFormats()
      
      val format=theformats.head // TODO: generalize to n outputs
      val output=config.outfiles.head
      
      val fs:FileStreamer=output match { case StandardOutput() => new FileStreamer(new PrintWriter(System.out),true)
                                         case FileOutput(f:File) => new FileStreamer(f,true)
                                         case StreamOutput(_) => throw new UnsupportedOperationException}
      
      if (config.stats==null) {
          parseDocument(in,fs)
      } else {
          val statspw:PrintWriter=config.stats match {case StandardOutput() => new PrintWriter(System.out)
                                                      case FileOutput(f:File) => new PrintWriter(f)
                                                      case StreamOutput(_) => throw new UnsupportedOperationException}
            
          val str=new Tee(fs,new SimpleStreamStatsPrint(10000,statspw))
          parseDocument(in,str)        
      }
        
      
  }

  def projectConfiguration(config: Config): SummaryConfig = {
    // create an instance of SummaryConfig by copying the relevant fields from the config
     SummaryConfig(config.kernel,
        config.aggregatep,
        config.level0,
        config.triangle,
        config.always_with_type_0,
        config.primitivep,
        config.prov_constraints_inference,
        config.filter,
        config.maxlevel,
        config.nsBase,
        config.prettyMethod,
        config.aggregatep,
        config.aggregate0p,
        config.withLevel0Description,
        config.to,
        config.outfiles,
        config.outformats,
        config.defaultFormat)
  }

  
  
  def sum(in: Input, config: Config): (Indexer,TypePropagator) = {
    val doc=parseDocument(in)  // to check why this is in nf module?
    SummaryAPI.sum(doc,projectConfiguration(config))
  }
  
  def sum(doc: Document, config: Config): (Indexer, TypePropagator) = {
    SummaryAPI.sum(doc,projectConfiguration(config))
  }

  
  def prepareExpansion (in:Input, config:Config):(Document,Bindings) = {
    
      val doc=parseDocument(in)

      val version=config.bindingsVersion

      

      val bindings =
        if (version==3) {
              
            val bb2=config.bindings match {
              case FileInput(f) => BindingsJson.importBean(f)
              case StandardInput() => BindingsJson.importBean(System.in)
            }         
            
            val bindings=BindingsJson.fromBean(bb2,pf)
          
            //val expansion=exp.expander(doc, "ignore", bindings)
            
            bindings
                
        } else {
      
        	  val bindingDoc=parseDocument(config.bindings)
        	  
        	  val bindings=
        	      (if (config.bindingsVersion==1) BindingKind.V1 else {BindingKind.V2}) match {
        	      case BindingKind.V1 => Bindings.fromDocument(bindingDoc, ProvFactory.pf)
        	      case BindingKind.V2 => Bindings.fromDocument_v2(bindingDoc, ProvFactory.pf)       
        	  }    

            //val expansion=exp.expander(doc, "ignore", bindingDoc, if (config.bindingsVersion==1) BindingKind.V1 else {BindingKind.V2})
            
            bindings
          
        }
      
      (doc,bindings)

  }
  
  def expand (in:Input, config:Config):Document = {
      val (doc, bindings)=prepareExpansion(in,config)
      val exp=new Expander(config.allexpanded,config.genorder)
      val expansion=exp.expander(doc, "ignore", bindings)

      expansion
    }
       
      
  def expandExport (in:Input, config:Config):Unit = {
	    val theformats=config.theOutputFormats()         
	    val expansion=expand(in,config)
      config.outfiles.zip(theformats).par foreach { case (o:Output,format: Format.Format) =>  Format2.outputers(format).output(expansion, o, Map[String,String]())  }

  }
  
  def expandTime (in:Input, config:Config, out:Output):Unit = {
      val (doc, bindings)=prepareExpansion(in,config)
      var i=0
      for (i <- 0 to config.jitWait) {
    	  val exp=new Expander(config.allexpanded,config.genorder)
    	  val expansion=exp.expander(doc, "ignore", bindings)
      }
      var count=0
      val result=
        for (count <-0 to config.averageRepeat) yield {
      	  val before=System.nanoTime
    		  for (i <-1 to config.averageNum) {
        	  val exp=new Expander(config.allexpanded,config.genorder)
      	    val expansion=exp.expander(doc, "ignore", bindings)
          }
	        val after=System.nanoTime
	        val duration:Float=(after-before)/config.averageNum
	        duration
        }
      
      val sb= new StringBuilder
      //System.out.println("Time is " + result.map(_/1000000.0))
      val avg=result.sum / result.length / 1000000.0
      sb.append(avg)
      sb.append(", ")
      result.map(_/1000000.0).addString(sb, ",")
      //sb.append("\n")
      output(sb.toString(),out)
  }
  
  def bindings (config:Config): Unit = {
    if (config.bindingsVersion==2) {
      bindings_v2(config)
    } else {
      bindings_v3(config)
    }
  }
  
  def bindings_v2 (config:Config): Unit = {
      System.out.println("converting bindings to v2")

	    val bindings=config.infile
      val bindingsDoc=parseDocument(bindings)
      val bindings_v1=Bindings.fromDocument(bindingsDoc,pf)
      
      System.out.println("bindings v1 " ++ bindings_v1.toString())

      
      val doc_v2=bindings_v1.toDocument_v2.asInstanceOf[org.openprovenance.prov.scala.immutable.Document]
      
      val theformats=config.theOutputFormats()         
             
      config.outfiles.zip(theformats).par foreach { case (o:Output,format: Format.Format) =>  Format2.outputers(format).output(doc_v2, o, Map[String,String]())  }


  }
  
    def bindings_v3 (config:Config): Unit = {
      
      
     // System.out.println("converting bindings to v3"); 
	    val bindings=config.infile
      val bindingsDoc=parseDocument(bindings)
      val bindings_v1=Bindings.fromDocument(bindingsDoc,pf)
      
      bindings_v1.addVariableBindingsAsAttributeBindings()
      
     // System.out.println("bindings v1 " ++ bindings_v1.toString())

      val doc_v2=bindings_v1.toDocument_v2.asInstanceOf[org.openprovenance.prov.scala.immutable.Document]
      
      val bb=BindingsJson.toBean(bindings_v1)
      
      val name=config.outfiles.head match {
        case FileOutput(f) => f
        case _ => ???
      }

      BindingsJson.exportBean(name.toString,bb,config.pretty)

  }
  
  val mpf=new org.openprovenance.prov.scala.mutable.ProvFactory()
  
   def output(s:String, out: Output): Unit = {
      out match {
        case StandardOutput() =>
          println(s)
        case FileOutput(f:File) =>
          val bw=new BufferedWriter(new FileWriter(f))
          bw.append(s)
          bw.close()
        case StreamOutput(_) => throw new UnsupportedOperationException
      }
    }
  
    def validateDocument(doc: Document, validator: Validate, mpf: org.openprovenance.prov.model.ProvFactory): ValidationReport = {
      val mutableDoc:org.openprovenance.prov.model.Document = mpf.newDocument(doc)
     
      val report=validator.validate(mutableDoc)
      
      report
  }



  def validate (in:Input, config:Config):Unit = {
      val doc=parseDocument(in)
      
    	val validator: Validate = new Validate(org.openprovenance.prov.validation.Config.newYesToAllConfig(mpf, new ValidationObjectMaker))
      
      val report=validateDocument(doc,validator, mpf)
      
      if (config.matrix!=null) {
        val mat=validator.constraints.getMatrix.displayMatrix2()
        output(mat,config.matrix)
      }     
      
      if (config.image!=null) {
        config.image match {
          case FileOutput(f) => validator.constraints.getMatrix.generateImage1(f.toString)
          case _ => ???
        }
      }
      
      config.outfiles.headOption match {
        case Some(f: FileOutput)    =>   output(report.toString(),f)
        case Some(StandardOutput()) =>   println(report.toString())
        case Some (StreamOutput(_)) => throw new UnsupportedOperationException
        case None =>
      }
  }

  def batch_processing (config:Config):Unit = {
     val bufferedSource=toBufferedSource(config.infile)
      val lines: Iterator[String] =bufferedSource.getLines()
      if (config.parallel) {
        println("batch parallel processing")
        lines.toSeq.par.foreach(process_item)
      } else {
        println("batch sequential processing")
        lines.foreach(process_item)
      }
  }

  def process_item(line: String): Unit ={
    val args=line.split(" ")
    // println("processing: " + args.mkString("[", ",", "]"))
    CommandLine.main(args.drop(1))
  }


  def toBlockly(withfile: Input, blockly: Output, xplan: String): Unit = {

  }


  object statements
}


class FileNameWithoutExtensionException extends Exception


  
