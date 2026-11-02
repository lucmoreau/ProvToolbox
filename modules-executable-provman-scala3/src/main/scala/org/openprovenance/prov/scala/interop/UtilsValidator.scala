package org.openprovenance.prov.scala.interop
import org.openprovenance.prov.nlg.ValidationObjectMaker
import org.openprovenance.prov.scala.immutable._
import org.openprovenance.prov.scala.interop.CommandLine.output
import org.openprovenance.prov.scala.nf.CommandLine.parseDocument
import org.openprovenance.prov.validation.Validate
import org.openprovenance.prov.validation.report.ValidationReport

class UtilsValidator {

  val mpf=new org.openprovenance.prov.scala.mutable.ProvFactory()

  def validateDocument(doc: Document, validator: Validate, mpf: org.openprovenance.prov.model.ProvFactory): ValidationReport = {
    val mutableDoc: org.openprovenance.prov.model.Document = mpf.newDocument(doc)

    val report = validator.validate(mutableDoc)

    report
  }


  def validate(in: Input, config: Config): Unit = {
    val doc = parseDocument(in)

    val validator: Validate = new Validate(org.openprovenance.prov.validation.Config.newYesToAllConfig(mpf, new ValidationObjectMaker))

    val report = validateDocument(doc, validator, mpf)

    if (config.matrix != null) {
      val mat = validator.constraints.getMatrix.displayMatrix2()
      output(mat, config.matrix)
    }

    if (config.image != null) {
      config.image match {
        case FileOutput(f) => validator.constraints.getMatrix.generateImage1(f.toString)
        case _ => ???
      }
    }

    config.outfiles.headOption match {
      case Some(f: FileOutput) => output(report.toString(), f)
      case Some(StandardOutput()) => println(report.toString())
      case Some(StreamOutput(_)) => throw new UnsupportedOperationException
      case None =>
    }
  }

}
