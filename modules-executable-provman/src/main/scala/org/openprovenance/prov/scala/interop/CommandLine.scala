package org.openprovenance.prov.scala.interop
import org.openprovenance.prov.model.Namespace
import org.openprovenance.prov.scala.iface.{Explainer, Narrator, QFactory, QueryEngine, XFactory}
import org.openprovenance.prov.scala.immutable.ProvFactory.pf
import org.openprovenance.prov.scala.immutable.{Document, Format, Statement}
import org.openprovenance.prov.scala.interop.CommandLine.uExpand.{bindings, bindings_v2, bindings_v3, expandExport, expandTime}
import org.openprovenance.prov.scala.interop.CommandLine.uSignature.{normalize, sign, signature}
import org.openprovenance.prov.scala.interop.CommandLine.uSummary.{normalizeOLDSTUFF, summarize, summaryDraw, summary_compare}
import org.openprovenance.prov.scala.interop.CommandLine.uValidate.validate
import org.openprovenance.prov.scala.nf.CommandLine.{parseDocument, toBufferedSource}
import org.openprovenance.prov.scala.query.QueryInterpreter.RField
import org.openprovenance.prov.scala.streaming.{FileStreamer, SimpleStreamStatsPrint, Tee}
import scopt.OptionParser

import java.io._
import scala.collection.parallel.CollectionConverters._





object CommandLine extends Constants {


  val parser: OptionParser[Config] = new CommandLineOptionParser().parser


  val xFactory: XFactory = new XFactory()
  val narrator: Narrator = xFactory.makeNarrator
  val explainer: Explainer = xFactory.makeExplainer

  val qFactory: QFactory = new QFactory()
   val uSignature: UtilsSignature = new UtilsSignature()
  val uSummary: UtilsSummary = new UtilsSummary(pf)
  val uExpand: UtilsExpand = new UtilsExpand()
  val uValidate: UtilsValidator = new UtilsValidator()


  def main(args: Array[String]): Unit = {
    // first, let's disable hsqldb logging
    System.setProperty("hsqldb.reconfig_logging", "false")
    System.setProperty("java.util.logging.config.file", "src/main/resources/config/logging.properties")

    parser.parse(args, Config()) match {
      case Some(config) =>

        val queryEngine: QueryEngine[Statement, RField] = qFactory.makeQueryEnfine(config)

        val uExplain: UtilsXplain = new UtilsXplain(narrator, explainer, queryEngine)


        config.command match {
          case "summary" => summarize(config)
          case "kernelize" => summarize(config.copy(kernel = true))
          case "summaryDraw" => summaryDraw(config)
          case "summary.compare" => summary_compare(config.infile, config.summaryDescriptionFile,
            config.withSummaryFile, config.withSummaryDescriptionFile, config)
          case "normalize.old" => normalizeOLDSTUFF(config)
          case NORMALIZE => normalize(config)
          case "sign" => sign(config)
          case "signature" => signature(config)
          case TRANSLATE => translate(config.infile, config, uExplain)
          case EXPAND => if (config.time == null) {
            expandExport(config.infile, config)
          } else {
            expandTime(config.infile, config, config.time)
          }
          case "validate" => validate(config.infile, config)
          case "narrate" =>
            val in: Input = config.infile
            val docIn = parseDocument(in)
            val (text, doc, mat, descriptor) = narrator.narrate(docIn, config)
            uExplain.narrativeExport(config, text, doc, mat, descriptor)

          case "explain" =>
            val in: Input = config.infile
            val doc = parseDocument(in)
            val text = explainer.explain(doc, config)
            uExplain.explanationExport(config, text, doc)

          case "bindings" => bindings(config)
          case "bindings.v2" => bindings_v2(config)
          case "bindings.v3" => bindings_v3(config)
          case "batch" => batch_processing(config)
          case "compare" => uSummary.compare(config.infile, config.withfile, config.nf, config)
          case "config" => configuration()
          case "blockly" => toBlockly(config.withfile, config.blockly, config.xplan)
        }


      case None => // arguments are bad, error message will have been displayed
    }
  }

  def configuration(): Unit = {
    val classpath = System.getProperty("java.class.path")
    println("provman.classpath=" + classpath)
    println("provman.main=" + getClass.getName)
  }




  def translate(in: Input, config: Config, uExplain: UtilsXplain): Unit = {
    if (config.streaming) {
      translate_streaming(in, config)
    } else {
      translate_non_streaming(in, config, uExplain)
    }
  }

  def translate_non_streaming(in: Input, config: Config, uExplain: UtilsXplain): Unit = {
    val doc = parseDocument(in)
    Namespace.withThreadNamespace(doc.namespace)
    if (config.query == null) {
      outputer(doc, config)
    } else {
      uExplain.processQueryAndOutput(doc, config.query, config, config)
    }
  }

  def outputer(doc: Document, config: OutConfig): Unit = {
    val theformats = config.theOutputFormats()

    config.outfiles.zip(theformats).par foreach { case (o: Output, format: Format.Format) => Format2.outputers(format).output(doc, o, Map[String, String]()) }
  }

  def inputer(config: Config): Document = {
    val format = config.theInputFormat()
    val d = Format.inputers(format).input(config.infile, Map[String, String]())
    d
  }

  def translate_streaming(in: Input, config: Config): Unit = {

    val theformats = config.theOutputFormats()

    val format = theformats.head // TODO: generalize to n outputs
    val output = config.outfiles.head

    val fs: FileStreamer = output match {
      case StandardOutput() => new FileStreamer(new PrintWriter(System.out), true)
      case FileOutput(f: File) => new FileStreamer(f, true)
      case StreamOutput(_) => throw new UnsupportedOperationException
    }

    if (config.stats == null) {
      parseDocument(in, fs)
    } else {
      val statspw: PrintWriter = config.stats match {
        case StandardOutput() => new PrintWriter(System.out)
        case FileOutput(f: File) => new PrintWriter(f)
        case StreamOutput(_) => throw new UnsupportedOperationException
      }

      val str = new Tee(fs, new SimpleStreamStatsPrint(10000, statspw))
      parseDocument(in, str)
    }


  }


  def output(s: String, out: Output): Unit = {
    out match {
      case StandardOutput() =>
        println(s)
      case FileOutput(f: File) =>
        val bw = new BufferedWriter(new FileWriter(f))
        bw.append(s)
        bw.close()
      case StreamOutput(_) => throw new UnsupportedOperationException
    }
  }


  def batch_processing(config: Config): Unit = {
    val bufferedSource = toBufferedSource(config.infile)
    val lines: Iterator[String] = bufferedSource.getLines()
    if (config.parallel) {
      println("batch parallel processing")
      lines.toSeq.par.foreach(process_item)
    } else {
      println("batch sequential processing")
      lines.foreach(process_item)
    }
  }

  private def process_item(line: String): Unit = {
    val args = line.split("\\s+")
    //println("processing: " + args.mkString("[", ",", "]"))
    CommandLine.main(args.drop(1))
  }


  def toBlockly(withfile: Input, blockly: Output, xplan: String): Unit = {

  }

}



class FileNameWithoutExtensionException extends Exception


  
