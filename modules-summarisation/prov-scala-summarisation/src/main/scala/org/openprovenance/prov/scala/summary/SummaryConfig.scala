package org.openprovenance.prov.scala.summary

import org.openprovenance.prov.scala.immutable.Format
import org.openprovenance.prov.scala.immutable.Format.Format
import org.openprovenance.prov.scala.interop.{FileOutput, Output, StandardOutput, StreamOutput}
import org.openprovenance.prov.scala.summary.PrettyMethod.Pretty
import org.openprovenance.prov.summary.FileNameWithoutExtensionException

import java.io.File

case class SummaryConfig(kernel: Boolean=false,
                         aggregated: Boolean=false,
                         level0: File = null,
                         triangle: Boolean = false,
                         always_with_type_0: Boolean = false,
                         primitivep: Boolean = false,
                         prov_constraints_inference: Boolean = false,
                         filter: List[String] = List(),
                         maxlevel: Int = 0,
                         nsBase: String = "",
                         prettyMethod: Pretty = PrettyMethod.Name,
                         aggregatep: Boolean = false,
                         aggregate0p: Boolean = false,
                         withLevel0Description: Boolean = false,
                         to: Int = -1,
                         outfiles: Seq[Output] = Seq(),
                         outformats: Seq[Format.Format] = Seq(),
                         defaultFormat: Format.Format=null)
{

  def this(os: java.io.OutputStream, mediaType: String) {
    this(outfiles = Seq(StreamOutput(os)), defaultFormat = Format.fromMediatype(mediaType))
  }
  def this(value: Int) =
    this(to = value)

  def this(value: Int, kernel: Boolean, aggregatep: Boolean) {
    this(to = value, kernel = kernel, aggregatep = aggregatep)
  }

  def theOutputFormats(): Seq[Format] = {
    if (outformats.isEmpty) {
      outfiles.map { o: Output =>
        o match {
          case StandardOutput() => defaultFormat
          case StreamOutput(_) => defaultFormat
          case FileOutput(f: File) => Format.withName(extension(f.getPath))
        }
      }
    } else {
      outformats
    }
  }

  private def extension(filename: String): String = {
    val count = filename.lastIndexOf(".")
    if (count == -1) throw new FileNameWithoutExtensionException(filename)
    filename.substring(count + 1)
  }

}