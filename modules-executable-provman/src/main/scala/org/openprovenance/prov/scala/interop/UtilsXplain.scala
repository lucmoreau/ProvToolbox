package org.openprovenance.prov.scala.interop

import com.fasterxml.jackson.databind.SerializationFeature
import org.openprovenance.prov.scala.iface.{Explainer, Narrator, QueryEngine}
import org.openprovenance.prov.scala.immutable.{Document, StatementOrBundle}
import org.openprovenance.prov.scala.interop.CommandLine.{output, outputer}
import org.openprovenance.prov.scala.narrator.{EventsDescription, XConfig}
import org.openprovenance.prov.scala.nlgspec_transformer.Environment
import org.openprovenance.prov.scala.query.StatementAccessor
import org.openprovenance.prov.scala.summary.TypePropagator
import org.openprovenance.prov.scala.xplain.{Narrative, RealiserFactory}
import org.openprovenance.prov.validation.EventMatrix

import java.io.{BufferedWriter, FileWriter}

class UtilsXplain(narrator: Narrator, explainer: Explainer, queryEngine: QueryEngine) {
  def narrativeExport(config: Config,
                      text: Map[String, Narrative],
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

    if (config.snlg != null) {
      val m = narrator.getSnlgOnly(text)
      textOutputer(m, config.snlg, jsonSerialized = true)
    }

    if (config.matrix != null) {
      val mat_text = mat.displayMatrix2()
      output(mat_text, config.matrix)
    }

    textOutputer(narrator.getTextOnly(text), config.text)

  }


  def explanationExport(config: Config,
                        text: Map[String, Narrative],
                        doc2: Document): Unit = {


    if (config.outfiles.nonEmpty) {
      outputer(doc2, config)
    }

    if (config.snlg != null) {
      val m = narrator.getSnlgOnly(text)
      textOutputer(m, config.snlg, jsonSerialized = true)
    }

    textOutputer(narrator.getTextOnly(text), config.text)

  }


  def processQueryAndOutput(doc: Document, query: Input, config: XConfig, outConfig: OutConfig): Unit = {
    import scala.jdk.CollectionConverters._
    val context: Map[String, String] = doc.namespace.getPrefixes.asScala.toMap
    val environment = Environment(context, null, null, new Array[String](0), List())

    val rf = new RealiserFactory(config)

    val accessor: StatementAccessor = rf.makeStatementAccessor(doc.statements().toSeq)
    val accessors: Map[String, StatementAccessor] = rf.accessors.map { case (s, ss) => (s, rf.makeStatementAccessor(ss)) } //alternate_files.map{case (s,f) => (s, rf.makeStatementAccessor(parseDocument(new FileInput(new File(f))).statements().toSeq))}

    val statementAccessorForDocument: (Option[String] => StatementAccessor) = {
      case None => accessor
      case Some(s) => accessors(s)
    }


    val source = scala.io.Source.fromFile(query.asInstanceOf[FileInput].f)
    val queryContents: String = try source.mkString finally source.close()


    processQuery(queryContents, environment, statementAccessorForDocument)

    val statementOrBundle: Iterable[StatementOrBundle] = null
    val doc2 = new Document(statementOrBundle, doc.namespace)
    outputer(doc2, outConfig)
  }

  def processQuery(queryContents: String, environment: Environment, statementAccessorForDocument: Option[String] => StatementAccessor) = {
    queryEngine.processQuery(queryContents, environment, statementAccessorForDocument)
  }

  def exportToJson(outw: java.io.Writer, any: AnyRef): Unit = {
    TypePropagator.om.enable(SerializationFeature.INDENT_OUTPUT);
    TypePropagator.om.writeValue(outw, any)
  }


  def textOutputer(text: Map[String, List[String]], config_text: Output, jsonSerialized: Boolean = false): Unit = {
    if (config_text != null) {
      val format = config_text match {
        case StandardOutput() => "txt"
        case FileOutput(f) => f.getName.substring(f.getName.lastIndexOf('.') + 1)
      }

      format match {
        case "json" =>
          if (!jsonSerialized) {
            TypePropagator.om.writeValue(config_text.asInstanceOf[FileOutput].f, text.map { case (k, vv) => (k, vv.mkString("  ")) })
          } else {
            output(text.map { case (k, vv) => "\"" + k + "\": " + vv.mkString("  ") }.mkString("{", ", ", "}"), config_text)
          }
        case _ => output(text.map { case (k, v) => (k, v.mkString("  ")) }.mkString("", "\n", ""), config_text)

      }
    }
  }


}
