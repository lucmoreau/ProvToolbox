package org.openprovenance.prov.scala

import org.openprovenance.prov.scala.iface.{Narrative, Narrator, XFactory}

import java.io.File
import org.openprovenance.prov.scala.immutable.{Document, ProvFactory, QualifiedName}
import org.openprovenance.prov.scala.interop.{FileInput, Input, Output}
import org.openprovenance.prov.scala.nf.CommandLine
import org.openprovenance.prov.scala.narrator.XConfig
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers


class ExplanationSpec extends AnyFlatSpec with Matchers {
  val EX_NS="http://example.org/"
  val pf=new ProvFactory
  val ipf=new org.openprovenance.prov.scala.immutable.ProvFactory

  def q(local: String): QualifiedName = {
    new QualifiedName("ex",local,EX_NS)
  }


  def readDoc(f: String): Document = {
    val in:Input=FileInput(new File(f))
    val doc=CommandLine.parseDocument(in)
    doc
  }

  def makeConfig(template: String, profile0: String, arg_format_option: Int): XConfig = {
    val config: XConfig = new XConfig {
      override def snlg: Output = null

      override def languageAsFilep: Boolean = true

      override def selected_templates: Seq[String] = Seq(template)

      override def profile: String = profile0

      override def batch_templates: Option[String] = None

      override def language: Seq[String] = Seq("src/main/resources/nlg/templates/loan/template-library.json")

      override def linear: Boolean = false

      override def infile: Input = ???

      override def infiles: String=null

      override def format_option: Int = arg_format_option
    }
    config
  }

  val xFactory=new XFactory
  val narrator: Narrator =xFactory.makeNarrator


  def explain(f:String, template:String, profile:String, format_option:Int=0): Map[String, List[String]] = {
    val doc=readDoc(f)
    val (text:Map[String, Narrative], _, _, _) = narrator.narrate(doc,makeConfig(template,profile,format_option))
    narrator.getTextOnly(text)
  }

  var count=0

  def theTest(file:String, template:String, result:String, testp:Boolean=true,profile:String="ln:borrower-noun"): Unit = {
    count = count + 1
    "Template " + template + "(" + (count) + ")" should "be processable" in {

      val text = explain("src/test/resources/prov/loan/" + file, template, profile)
      print(text)
      if (testp) {
        text.getOrElse(template, "") should be {
          List(result)
        }
      }
      text
    }
  }

  theTest(
    "16.provn",
    "automatic.decision3",
    "The loan application was automatically approved based on a combination of the borrower's loan application and third party data: the borrower's credit reference and the borrower's FICO score.")

  theTest(
    "16.provn",
    "data.sources2",
    "The data sources were the borrower's loan application (applications/127567056) provided by the loan applicant (applicants/127567056)."
  )


  theTest(
    "128350251.provn",
    "data.sources2",
    "The data sources were the borrower's FICO score (fico_score/128350251) provided by credit referencing agency (fico) at <2019-03-10T11:00:00>, the borrower's loan application (applications/128350251) provided by the loan applicant (applicants/128350251) and the borrower's credit reference (credit_history/128350251) provided by credit referencing agency (credit_agency) at <2019-01-10T14:10:16>.",
    testp = false
  )



  theTest(
    "training-1.provn",
    "responsibility6",
    "Responsibilities for the AI pipeline were that a data engineer (staff/259) fit data for the company pipeline (1558649326/5011959424), that a data engineer (staff/259) split file (loans_train.xz), that manager (staff/37) approved pipeline (pipeline/1) and that a data engineer (staff/259) selected file (loans_filtered.xz).",
    testp = false) //non determinism



  "Plan with count " should "be processable" in {
    val template="aggregate-count"
    val doc=readDoc("src/test/resources/prov/loan/" + "16.provn")
    val (text:Map[String, Narrative], _, _, _) =narrator.narrate(doc,makeConfig(template,"ln:borrower-noun",0))
    text.getOrElse(template, "<<not found>>")
  }



}
