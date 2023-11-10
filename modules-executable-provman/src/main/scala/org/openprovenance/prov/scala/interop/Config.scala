package org.openprovenance.prov.scala.interop

import org.openprovenance.prov.scala.immutable.Format
import org.openprovenance.prov.scala.immutable.Format.Format
import org.openprovenance.prov.scala.narrator.XConfig
import org.openprovenance.prov.scala.summary.{PrettyMethod, SConfig}

import java.io.File

case class Config(outfiles: Seq[Output] = Seq(),
                  outformats: Seq[Format.Format] = Seq(),
                  defaultFormat: Format.Format=null,
                  defaultInputFormat: Format.Format=null,
                  selected_templates: Seq[String]=Seq(),
                  batch_templates:Option[String]=None,
                  infile: Input = null,
                  infiles: String=null,
                  informat: Format.Format=null,
                  query: Input = null,
                  queryResult:Output=null,
                  withfile: Input = null,
                  profile:String=null,
                  nf: Int = -1,
                  bindings: Input =null,
                  bindingsVersion: Int=1,
                  inputFormat: Format.Format=null,
                  xplan: String=null,
                  stats: Output=null,
                  matrix: Output=null,
                  time: Output=null,
                  id: String=null,
                  jitWait: Int=1000, // let the jit happen
                  averageNum: Int=40, // average over n measures
                  averageRepeat: Int=20, // repeat this r times
                  image: Output=null,
                  debug: Boolean = false,
                  primitivep: Boolean=true,
                  parallel: Boolean = false,
                  genorder: Boolean = false,
                  allexpanded: Boolean = true,
                  streaming: Boolean=false,
                  triangle: Boolean = false,
                  pretty: Boolean = false,
                  prettyMethod: PrettyMethod.Pretty=PrettyMethod.Name,
                  command: String = "",
                  store: String = null,
                  nsBase: String="http://openprovenance.org/summary/xyz/types#",
                  storepass: String = null,
                  key: String = null,
                  keypass: String = null,
                  text: Output = null,
                  blockly: Output = null,
                  snlg: Output = null,
                  kernel: Boolean=false,
                  linear: Boolean = false,
                  hierarchical: Boolean = false,
                  summaryFile: Input=null,
                  summaryDescriptionFile: Input=null,
                  withSummaryFile: Input=null,
                  withSummaryDescriptionFile: Input=null,
                  from: Int = -1,
                  to: Int = -1,
                  maxlevel: Int = -1,
                  format_option:Int=0,
                  level0: File = null,
                  filter: Seq[String] = Seq(),
                  types: File=null,
                  features: File=null,
                  description: File=null,
                  prov_constraints_inference:Boolean=true,
                  aggregatep:Boolean=true,
                  aggregate0p:Boolean=true,
                  always_with_type_0:Boolean=false,
                  language: Seq[String]=Seq(),
                  summary_queries: File = null,
                  withLevel0Description:Boolean=false,
                  languageAsFilep: Boolean=true)  extends XConfig with OutConfig with SConfig {


  def this (os: java.io.OutputStream, mediaType: String) = {
    this(outfiles=Seq(new StreamOutput(os)), defaultFormat=Format.fromMediatype(mediaType))
  }
  def this (is: java.io.InputStream, mediaType: String) = {
    this(infile=new StreamInput(is), defaultInputFormat=Format.fromMediatype(mediaType))
  }
  def this (value: Int) = {
    this(to=value)
  }
  def this (value: Int, kernel: Boolean, aggregatep: Boolean) = {
    this(to=value,kernel=kernel, aggregatep=aggregatep)
  }

  def this (value: Int, uri: String) = {
    this(to=value,nsBase=uri)
  }
  def this (value: Int, uri: String, pretty: String) = {
    this(to=value,nsBase=uri, prettyMethod=PrettyMethod.withName(pretty))
  }


  def theOutputFormats (): Seq[Format] = {
    if (outformats.isEmpty) {
      outfiles.map {
        case StandardOutput() => defaultFormat
        case StreamOutput(s) => defaultFormat
        case FileOutput(f: File) => Format.withName(extension(f.getPath))
      }
    } else {
      outformats
    }
  }

  def theInputFormat (): Format = {
    if (informat==null) {
      infile match { case StandardInput() => defaultInputFormat
      case StreamInput(s)  => defaultInputFormat
      case FileInput(f:File) => Format.withName(extension(f.getPath)) }
    } else {
      informat
    }
  }


  def extension(filename :String): String = {
    val count = filename.lastIndexOf(".");
    if (count == -1) throw new FileNameWithoutExtensionException
    filename.substring(count + 1);
  }

}

trait OutConfig {
  def outfiles: Seq[Output]
  def theOutputFormats (): Seq[Format]
  def queryResult ():Output
}

