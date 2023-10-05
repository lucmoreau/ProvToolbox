package org.openprovenance.prov.scala.interop

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.scala.{ClassTagExtensions, DefaultScalaModule}
import org.openprovenance.prov.scala.iface.{Explainer, Narrator, QueryEngine}
import org.openprovenance.prov.scala.immutable.{Document, Statement}
import org.openprovenance.prov.scala.interop.CommandLine.{output, outputer}
import org.openprovenance.prov.scala.narrator.{EventsDescription, XConfig}
import org.openprovenance.prov.scala.query.QueryInterpreter.RField
import org.openprovenance.prov.scala.summary.TypePropagator
import org.openprovenance.prov.scala.xplain.Narrative
import org.openprovenance.prov.validation.EventMatrix

import java.io.{BufferedWriter, FileWriter}

class UtilsXplain(narrator: Narrator, explainer: Explainer, queryEngine: QueryEngine[Statement, RField]) {
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

    val source = scala.io.Source.fromFile(query.asInstanceOf[FileInput].f)
    val queryContents: String = try source.mkString finally source.close()


    val result: _root_.org.openprovenance.prov.scala.iface.QueryResult[_root_.org.openprovenance.prov.scala.query.QueryInterpreter.RField] = queryEngine.processQuery(queryContents, doc)
    println("found " + result.getRecords.size + " records (and " + result.getRecords.headOption.getOrElse(Map()).keySet.size + " fields)")

    if (outConfig.queryResult()!=null) exportToJson(outConfig.queryResult(),result.getRecords.asJava)

    val doc2 = result.getDocument
    outputer(doc2, outConfig)
  }




  private def exportToJson(outw: java.io.Writer, any: AnyRef): Unit = {
    TypePropagator.om.enable(SerializationFeature.INDENT_OUTPUT);
    TypePropagator.om.writeValue(outw, any)
  }

  private def exportToJson(out: Output, records: AnyRef): Unit = {
    val om=JsonMapper.builder().addModule(DefaultScalaModule).build() :: ClassTagExtensions
    new org.openprovenance.prov.core.jsonld11.serialization.ProvSerialiser(om,false).getMapper.writeValue(out.asInstanceOf[FileOutput].f, records)
    //new ProvSerialiser(om,false).customize2(om).writeValue(out.asInstanceOf[FileOutput].f, records)
    // FIXME: which serializtion to use? None seems suitable.
    //om.writeValue(out.asInstanceOf[FileOutput].f, records)
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
