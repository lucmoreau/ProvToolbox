


package org.openprovenance.prov.scala.nlgspec_transformer

import org.openprovenance.prov.model.Namespace
import org.openprovenance.prov.scala.immutable.{Document, MyActions, MyActions2, MyParser, Statement}
import org.openprovenance.prov.scala.nlgspec_transformer.defs.{Dictionary, Plan}
import org.openprovenance.prov.scala.nlgspec_transformer.specTypes.{Phrase, TransformEnvironment}
import org.openprovenance.prov.scala.streaming.{DocBuilder, DocBuilderFunctions}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.util.Success


class TransformSpecsSpec extends AnyFlatSpec with Matchers {

  def parse(d: String):Document = {

    val actions=new MyActions()
    val actions2=new MyActions2()
    val funs=new DocBuilderFunctions()

    funs.reset()
    val db: DocBuilder =new DocBuilder(funs)
    val ns=new Namespace
    actions2.docns=ns
    actions2.bun_ns=None
    actions2.next=db

    ns.addKnownNamespaces()
    val p=new MyParser(d,actions2,actions)
    val doc =p.document.run() match {
      case Success(_) => db.document()
      case x => println(x)
        throw new UnsupportedOperationException()
    }
    doc
  }

  val path = "src/test/resources/nlg/templates/loan/"


  val (templates,dictionaries,profiles): (Seq[Plan], Seq[Dictionary], Map[String, Object]) = Language.read(Seq(path + "template-library.json"),filep = true)

 // println(dictionaries)
  //println(profiles)


  def transformerCall(template: String, m: Map[String, Statement], ms: Map[String, Seq[Statement]]=null, profile:String="ln:borrower-noun"): Phrase = {

    val tmpl: defs.Plan = SpecLoader.templateImport (template)
    val phraseSpec = tmpl.sentence
    val context=tmpl.context

    //println("dico " + dico)



    val te=new TransformEnvironment {
      override val environment: Environment = Environment(context,dictionaries,profiles,profile)
      override val statements: Map[String, Statement] = m
      override val seqStatements: Map[String, Seq[Statement]] = ms
    }

    val phraseSpec2 = phraseSpec.transform[Phrase](te)


    phraseSpec2.get

  }

  def transformerCallString(template: String, m: Map[String, Statement], ms: Map[String, Seq[Statement]]=null, profile:String="ln:borrower-noun" ): String = {
    val phraseSpec: Phrase =transformerCall(template,m,ms,profile)
    val result: String =defs.realise(phraseSpec)
    result
  }



  "Template human.decision1" should "be processable" in {

    val template_file = "human.decision1.json"

    val template=path + template_file



    val doc = parse(
      """
            document
             prefix ex <http://example.org/>
             prefix foaf <http://xmlns.com/foaf/0.1/>
             prefix ln <https://plead-project.org/ns/loan#>
             prefix pl <https://plead-project.org/ns/plead#>
             prefix nlg <https://openprovenance.org/nlg#>

             entity(ex:applications/128350251,[prov:type = 'ln:LoanApplication',ln:attr_term = " 36 months" %% xsd:string,ln:attr_purpose = "credit_card" %% xsd:string,ln:attr_loan_amnt = "12025" %% xsd:float,ln:attr_emp_title = "Lead Vet Tech" %% xsd:string,ln:attr_addr_state = "CA" %% xsd:string,ln:attr_title = "Credit card refinancing" %% xsd:string,ln:attr_application_type = "Joint App" %% xsd:string,ln:attr_emp_length = "4 years" %% xsd:string,ln:attr_zip_code = "950xx" %% xsd:string,ln:attr_annual_inc = "50000" %% xsd:float,ln:attr_home_ownership = "RENT" %% xsd:string])

             agent(ex:staff/112,[prov:type = 'ln:CreditOfficer',prov:type = 'prov:Person'])

             wasDerivedFrom(nlg:evt15;ex:decision/128350251,ex:credit_history/128350251)

             entity(ex:credit_history/128350251,[prov:type = 'ln:CreditReference',ln:attr_pub_rec_bankruptcies = "0" %% xsd:float,ln:attr_revol_bal = "4140" %% xsd:float,ln:created_at = "2019-01-10T14:10:16" %% xsd:dateTime,ln:attr_pub_rec = "0" %% xsd:float,ln:attr_earliest_cr_line = "Sep-2010" %% xsd:string,ln:attr_open_acc = "5" %% xsd:float,ln:attr_revol_util = "18.6" %% xsd:float,ln:attr_mort_acc = "0" %% xsd:float])
             activity(ex:review_recommendation/128350251,-,-,[prov:type = 'pl:HumanLedActivity',prov:type = 'ln:LoanAssessment'])
             entity(ex:decision/128350251,[prov:type = 'ln:HumanDecision',ln:result = 'ln:approved'])
             wasDerivedFrom(nlg:evt4;ex:decision/128350251,ex:recommendation/128350251)
             wasDerivedFrom(nlg:evt5;ex:decision/128350251,ex:applications/128350251)
             entity(ex:fico_score/128350251,[prov:type = 'ln:FICOScore',ln:fico_range_low = "710" %% xsd:float,ln:fico_range_high = "714" %% xsd:float,ln:created_at = "2019-03-10T11:00:00" %% xsd:dateTime])
             entity(ex:recommendation/128350251,[prov:type = 'ln:AutomatedLoanRecommendation',ln:probability_chargeoff = "0.211044" %% xsd:float,ln:recommendation = 'ln:approved'])
             wasDerivedFrom(nlg:evt22;ex:decision/128350251,ex:fico_score/128350251)
             wasAssociatedWith(nlg:evt24;ex:review_recommendation/128350251,ex:staff/112,ex:policy/loan_review/2019)

            endDocument
            """")

    def idx(key: String) = {
      doc.statements().find(s => s.getId().toString == key).get
    }

    val m: Map[String, Statement] =
      Map("application" -> idx("ex:applications/128350251"),
        "officer" -> idx("ex:staff/112"),
        "der4" -> idx("nlg:evt15"),
        "credit" -> idx("ex:credit_history/128350251"),
        "reviewing" -> idx("ex:review_recommendation/128350251"),
        "decision" -> idx("ex:decision/128350251"),
        "der3" -> idx("nlg:evt4"),
        "der1" -> idx("nlg:evt5"),
        "fico" -> idx("ex:fico_score/128350251"),
        "reco" -> idx("ex:recommendation/128350251"),
        "der2" -> idx("nlg:evt22"),
        "waw" -> idx("nlg:evt24"))

    Namespace.withThreadNamespace(doc.namespace)  // added to allow for debug printing



    val result=transformerCallString(template,m)


    result should be ("The automated recommendation was reviewed by a credit officer (staff/112) whose decision was based on your application (applications/128350251), the automated recommendation (recommendation/128350251) itself, a credit reference (credit_history/128350251) and a fico score (fico_score/128350251).")


    println(template_file + ": " + result)

  }


  "Template automatic.decision3" should "be processable" in {

    val template_file = "automatic.decision3.json"

    val template=path + template_file



    val doc = parse(
      """
            document
             prefix ex <http://example.org/>
             prefix foaf <http://xmlns.com/foaf/0.1/>
             prefix ln <https://plead-project.org/ns/loan#>
             prefix pl <https://plead-project.org/ns/plead#>
             prefix nlg <https://openprovenance.org/nlg#>

              entity(ex:applications/127567056,[prov:type = 'ln:LoanApplication',prov:type = 'pl:Controlled',ln:attr_term = " 36 months" %% xsd:string,ln:attr_purpose = "home_improvement" %% xsd:string,ln:attr_loan_amnt = "10000" %% xsd:float,ln:attr_emp_title = "Business analyst " %% xsd:string,ln:attr_addr_state = "PA" %% xsd:string,ln:attr_title = "Home improvement" %% xsd:string,ln:attr_application_type = "Joint App" %% xsd:string,ln:attr_emp_length = "1 year" %% xsd:string,ln:attr_zip_code = "190xx" %% xsd:string,ln:attr_annual_inc = "82000" %% xsd:float,ln:attr_home_ownership = "MORTGAGE" %% xsd:string])
              entity(ex:fico_score/127567056,[prov:type = 'ln:FICOScore',ln:fico_range_low = "730" %% xsd:float,ln:fico_range_high = "734" %% xsd:float])
              entity(ex:decision/127567056,[prov:type = 'ln:AutomatedDecision',ln:result = 'ln:approved'])

            endDocument
            """")

    def idx(key: String) = {
      doc.statements().find(s => s.getId().toString == key).get
    }

    val m: Map[String, Statement] =
      Map(
        "application" -> idx("ex:applications/127567056"),
        "decision" -> idx("ex:decision/127567056"),
        "decision_" -> idx("ex:decision/127567056")
      )

    val ms: Map[String, Seq[Statement]] =
      Map(
        "ourdata" -> Seq(idx("ex:applications/127567056")),
        "theirdata" -> Seq(idx("ex:fico_score/127567056"))
      )



    Namespace.withThreadNamespace(doc.namespace)  // added to allow for debug printing


    val result=transformerCallString(template,m,ms)




    result should be ("The loan application was automatically approved based on a combination of the borrower's loan application and third party data: the borrower's FICO score.")


    println(template_file + ": " + result)

  }



  "Template data.sources2" should "be processable" in {

    val template_file = "data.sources2.json"

    val template=path + template_file



    val doc = parse(
      """
            document
             prefix ex <http://example.org/>
             prefix foaf <http://xmlns.com/foaf/0.1/>
             prefix ln <https://plead-project.org/ns/loan#>
             prefix pl <https://plead-project.org/ns/plead#>
             prefix nlg <https://openprovenance.org/nlg#>

              entity(ex:decision/128350251,[prov:type = 'ln:HumanDecision',ln:result = 'ln:approved'])
              entity(ex:applications/128350251,[prov:type = 'ln:LoanApplication',ln:attr_term = " 36 months" %% xsd:string,ln:attr_purpose = "credit_card" %% xsd:string,ln:attr_loan_amnt = "12025" %% xsd:float,ln:attr_emp_title = "Lead Vet Tech" %% xsd:string,ln:attr_addr_state = "CA" %% xsd:string,ln:attr_title = "Credit card refinancing" %% xsd:string,ln:attr_application_type = "Joint App" %% xsd:string,ln:attr_emp_length = "4 years" %% xsd:string,ln:attr_zip_code = "950xx" %% xsd:string,ln:attr_annual_inc = "50000" %% xsd:float,ln:attr_home_ownership = "RENT" %% xsd:string])
              entity(ex:fico_score/128350251,[prov:type = 'ln:FICOScore',ln:fico_range_low = "710" %% xsd:float,ln:fico_range_high = "714" %% xsd:float,ln:created_at = "2019-03-10T11:00:00" %% xsd:dateTime])
              entity(ex:credit_history/128350251,[prov:type = 'ln:CreditReference',ln:attr_pub_rec_bankruptcies = "0" %% xsd:float,ln:attr_revol_bal = "4140" %% xsd:float,ln:created_at = "2019-01-10T14:10:16" %% xsd:dateTime,ln:attr_pub_rec = "0" %% xsd:float,ln:attr_earliest_cr_line = "Sep-2010" %% xsd:string,ln:attr_open_acc = "5" %% xsd:float,ln:attr_revol_util = "18.6" %% xsd:float,ln:attr_mort_acc = "0" %% xsd:float])
              agent(ex:applicants/128350251,[prov:type = 'ln:LoanApplicant',prov:type = 'prov:Person'])
              agent(ex:fico,[prov:type = 'ln:CreditReferencingAgency',prov:type = 'prov:Organization'])
              agent(ex:credit_agency,[prov:type = 'ln:CreditReferencingAgency',prov:type = 'prov:Organization'])
            endDocument
            """")

    def idx(key: String) = {
      doc.statements().find(s => s.getId().toString == key).get
    }

    val m: Map[String, Statement] =
      Map(
        "decision" -> idx("ex:decision/128350251")
      )

    val ms: Map[String, Seq[Statement]] =
      Map(
        "data" -> Seq(idx("ex:applications/128350251"), idx("ex:fico_score/128350251"), idx("ex:credit_history/128350251")),
        "supplierAgency" -> Seq(idx("ex:applicants/128350251"), idx("ex:fico"), idx("ex:credit_agency"))
      )



    Namespace.withThreadNamespace(doc.namespace)  // added to allow for debug printing


    val result=transformerCallString(template,m,ms)


    result should be ("The data sources were the borrower's loan application (applications/128350251) provided by the loan applicant (applicants/128350251), the borrower's FICO score (fico_score/128350251) provided by credit referencing agency (fico) at 2019-03-10T11:00:00 and the borrower's credit reference (credit_history/128350251) provided by credit referencing agency (credit_agency) at 2019-01-10T14:10:16.")


    println(template_file + ": " + result)

  }




  "Template loan.application1" should "be processable" in {

    val template_file = "loan.application1.json"

    val template=path + template_file



    val doc = parse(
      """
            document
             prefix ex <http://example.org/>
             prefix foaf <http://xmlns.com/foaf/0.1/>
             prefix ln <https://plead-project.org/ns/loan#>
             prefix pl <https://plead-project.org/ns/plead#>
             prefix nlg <https://openprovenance.org/nlg#>
                wasAttributedTo(nlg:evt26;ex:applications/128350251,ex:applicants/128350251)
                entity(ex:applications/128350251,[prov:type = 'ln:LoanApplication',ln:attr_term = " 36 months" %% xsd:string,ln:attr_purpose = "credit_card" %% xsd:string,ln:attr_loan_amnt = "12025" %% xsd:float,ln:attr_emp_title = "Lead Vet Tech" %% xsd:string,ln:attr_addr_state = "CA" %% xsd:string,ln:attr_title = "Credit card refinancing" %% xsd:string,ln:attr_application_type = "Joint App" %% xsd:string,ln:attr_emp_length = "4 years" %% xsd:string,ln:attr_zip_code = "950xx" %% xsd:string,ln:attr_annual_inc = "50000" %% xsd:float,ln:attr_home_ownership = "RENT" %% xsd:string])
                agent(ex:applicants/128350251,[prov:type = 'ln:LoanApplicant',prov:type = 'prov:Person'])
            endDocument
            """")

    def idx(key: String) = {
      doc.statements().find(s => s.getId().toString == key).get
    }

    val m: Map[String, Statement] =
      Map(

        "wat" -> idx("nlg:evt26"),

        "appl" -> idx("ex:applications/128350251"),

        "cust" -> idx("ex:applicants/128350251")

      )

    val ms: Map[String, Seq[Statement]] =
      Map()



    Namespace.withThreadNamespace(doc.namespace)  // added to allow for debug printing


    val result=transformerCallString(template,m,ms)


    result should be ("Customer (applicants/128350251) applied for a loan (applications/128350251) in support of a credit_card." )
    println(template_file + ": " + result)

  }




  "Template relevancy2" should "be processable" in {

    val template_file = "relevancy2.json"

    val template=path + template_file



    val doc = parse(
      """
            document
             prefix ex <http://example.org/>
             prefix foaf <http://xmlns.com/foaf/0.1/>
             prefix ln <https://plead-project.org/ns/loan#>
             prefix pl <https://plead-project.org/ns/plead#>
             prefix nlg <https://openprovenance.org/nlg#>

              entity(ex:applications/128350251,[prov:type = 'ln:LoanApplication',ln:attr_term = " 36 months" %% xsd:string,ln:attr_purpose = "credit_card" %% xsd:string,ln:attr_loan_amnt = "12025" %% xsd:float,ln:attr_emp_title = "Lead Vet Tech" %% xsd:string,ln:attr_addr_state = "CA" %% xsd:string,ln:attr_title = "Credit card refinancing" %% xsd:string,ln:attr_application_type = "Joint App" %% xsd:string,ln:attr_emp_length = "4 years" %% xsd:string,ln:attr_zip_code = "950xx" %% xsd:string,ln:attr_annual_inc = "50000" %% xsd:float,ln:attr_home_ownership = "RENT" %% xsd:string])
              entity(ex:fico_score/128350251,[prov:type = 'ln:FICOScore',ln:fico_range_low = "710" %% xsd:float,ln:fico_range_high = "714" %% xsd:float,ln:created_at = "2019-03-10T11:00:00" %% xsd:dateTime])
              entity(ex:credit_history/128350251,[prov:type = 'ln:CreditReference',ln:attr_pub_rec_bankruptcies = "0" %% xsd:float,ln:attr_revol_bal = "4140" %% xsd:float,ln:created_at = "2019-01-10T14:10:16" %% xsd:dateTime,ln:attr_pub_rec = "0" %% xsd:float,ln:attr_earliest_cr_line = "Sep-2010" %% xsd:string,ln:attr_open_acc = "5" %% xsd:float,ln:attr_revol_util = "18.6" %% xsd:float,ln:attr_mort_acc = "0" %% xsd:float])
              activity(ex:review_recommendation/128350251,-,-,[prov:type = 'pl:HumanLedActivity',prov:type = 'ln:LoanAssessment'])
              entity(ex:decision/128350251,[prov:type = 'ln:HumanDecision',ln:result = 'ln:approved'])
              agent(ex:fico,[prov:type = 'ln:CreditReferencingAgency',prov:type = 'prov:Organization'])
              agent(ex:credit_agency,[prov:type = 'ln:CreditReferencingAgency',prov:type = 'prov:Organization'])

             endDocument
            """")

    def idx(key: String) = {
      doc.statements().find(s => s.getId().toString == key).get
    }

    val m: Map[String, Statement] =
      Map(

        "application" -> idx("ex:applications/128350251"),

        "decision" -> idx("ex:decision/128350251"),

        "reviewing" -> idx("ex:review_recommendation/128350251")


      )

    val ms: Map[String, Seq[Statement]] =
      Map(
        "credit" -> Seq(idx("ex:fico_score/128350251"), idx("ex:credit_history/128350251")),
        "creditAgency" -> Seq( idx("ex:fico"), idx("ex:credit_agency"))

      )



    Namespace.withThreadNamespace(doc.namespace)  // added to allow for debug printing


    val result=transformerCallString(template,m,ms)


    result should be ("The external data sources were the borrower's FICO score (fico_score/128350251) provided by credit referencing agency (fico) at 2019-03-10T11:00:00 and the borrower's credit reference (credit_history/128350251) provided by credit referencing agency (credit_agency) at 2019-01-10T14:10:16.")

    println(template_file + ": " + result)

    val result2=transformerCallString(template,m,ms,"ln:borrower-person2")


    result2 should be ("The external data sources were your FICO score (fico_score/128350251) provided by credit referencing agency (fico) at 2019-03-10T11:00:00 and your credit reference (credit_history/128350251) provided by credit referencing agency (credit_agency) at 2019-01-10T14:10:16.")
    println(template_file + ": " + result2)


  }



  "Template relevancy3" should "be processable" in {

    val template_file = "relevancy3.json"

    val template=path + template_file



    val doc = parse(
      """
            document
             prefix ex <http://example.org/>
             prefix foaf <http://xmlns.com/foaf/0.1/>
             prefix ln <https://plead-project.org/ns/loan#>
             prefix pl <https://plead-project.org/ns/plead#>
             prefix nlg <https://openprovenance.org/nlg#>

              entity(ex:applications/128350251,[prov:type = 'ln:LoanApplication',ln:attr_term = " 36 months" %% xsd:string,ln:attr_purpose = "credit_card" %% xsd:string,ln:attr_loan_amnt = "12025" %% xsd:float,ln:attr_emp_title = "Lead Vet Tech" %% xsd:string,ln:attr_addr_state = "CA" %% xsd:string,ln:attr_title = "Credit card refinancing" %% xsd:string,ln:attr_application_type = "Joint App" %% xsd:string,ln:attr_emp_length = "4 years" %% xsd:string,ln:attr_zip_code = "950xx" %% xsd:string,ln:attr_annual_inc = "50000" %% xsd:float,ln:attr_home_ownership = "RENT" %% xsd:string])
              entity(ex:fico_score/128350251,[prov:type = 'ln:FICOScore',ln:fico_range_low = "710" %% xsd:float,ln:fico_range_high = "714" %% xsd:float,ln:created_at = "2019-03-10T11:00:00" %% xsd:dateTime])
              entity(ex:credit_history/128350251,[prov:type = 'ln:CreditReference',ln:attr_pub_rec_bankruptcies = "0" %% xsd:float,ln:attr_revol_bal = "4140" %% xsd:float,ln:created_at = "2019-01-10T14:10:16" %% xsd:dateTime,ln:attr_pub_rec = "0" %% xsd:float,ln:attr_earliest_cr_line = "Sep-2010" %% xsd:string,ln:attr_open_acc = "5" %% xsd:float,ln:attr_revol_util = "18.6" %% xsd:float,ln:attr_mort_acc = "0" %% xsd:float])
              activity(ex:review_recommendation/128350251,-,-,[prov:type = 'pl:HumanLedActivity',prov:type = 'ln:LoanAssessment'])
              entity(ex:decision/128350251,[prov:type = 'ln:HumanDecision',ln:result = 'ln:approved'])
              agent(ex:fico,[prov:type = 'ln:CreditReferencingAgency',prov:type = 'prov:Organization'])
              agent(ex:credit_agency,[prov:type = 'ln:CreditReferencingAgency',prov:type = 'prov:Organization'])

             endDocument
            """")

    def idx(key: String) = {
      doc.statements().find(s => s.getId().toString == key).get
    }

    val m: Map[String, Statement] =
      Map(

        "application" -> idx("ex:applications/128350251"),

        "decision" -> idx("ex:decision/128350251"),

        "reviewing" -> idx("ex:review_recommendation/128350251")


      )

    val ms: Map[String, Seq[Statement]] =
      Map(
        "credit" -> Seq(idx("ex:fico_score/128350251"), idx("ex:credit_history/128350251")),
        "creditAgency" -> Seq( idx("ex:fico"), idx("ex:credit_agency"))

      )



    Namespace.withThreadNamespace(doc.namespace)  // added to allow for debug printing


    val result=transformerCallString(template,m,ms)


    result should be ("The external data sources were the borrower's FICO score (fico_score/128350251) provided by credit referencing agency (fico) at 2019-03-10T11:00:00 and the borrower's credit reference (credit_history/128350251) provided by credit referencing agency (credit_agency) at 2019-01-10T14:10:16.")

    println(template_file + ": " + result)

    val result2=transformerCallString(template,m,ms,"ln:borrower-person2")


    result2 should be ("The external data sources were your FICO score (fico_score/128350251) provided by credit referencing agency (fico) at 2019-03-10T11:00:00 and your credit reference (credit_history/128350251) provided by credit referencing agency (credit_agency) at 2019-01-10T14:10:16.")
    println(template_file + ": " + result2)


  }




  "Template performance4" should "be processable" in {

    val template_file = "performance4.json"

    val template=path + template_file



    val doc = parse(
      """
            document
             prefix ex <http://example.org/>
             prefix foaf <http://xmlns.com/foaf/0.1/>
             prefix ln <https://plead-project.org/ns/loan#>
             prefix pl <https://plead-project.org/ns/plead#>
             prefix nlg <https://openprovenance.org/nlg#>
          	 prefix py <urn:python:var:>
             prefix sk <https://scikit-learn.org/stable/modules/generated/sklearn.>

                wasGeneratedBy(nlg:evt8;py:1558649326/4890465504,ex:ml/validating/1558649326,-)
                wasDerivedFrom(nlg:evt16;py:1558649326/4890465504,py:1558649326/5011959424)
                entity(py:1558649326/4890465504,[prov:type = 'pl:AccuracyScore',prov:value = "0.795794" %% xsd:float])
                agent(ex:staff/259,[prov:type = 'prov:Person',prov:type = 'ln:DataEngineer'])
                activity(ex:ml/validating/1558649326,2019-05-23T23:34:13.861+01:00,2019-05-23T23:34:14.117+01:00,[prov:type = 'pl:AssessingPerformance'])
                entity(py:1558649326/5011959424,[prov:type = 'sk:pipeline.Pipeline'])
                wasAssociatedWith(nlg:evt34;ex:ml/validating/1558649326,ex:staff/259,-)
                used(nlg:evt37;ex:ml/validating/1558649326,py:1558649326/5011959424,-)

             endDocument
            """")

    def idx(key: String) = {
      doc.statements().find(s => s.getId().toString == key).get
    }

    val m: Map[String, Statement] =
      Map(

        "wgb" -> idx("nlg:evt8"),

        "wdf" -> idx("nlg:evt16"),

        "score" -> idx("py:1558649326/4890465504"),

        "agent" -> idx("ex:staff/259"),
        "assessing" -> idx("ex:ml/validating/1558649326"),
        "pipeline" -> idx("py:1558649326/5011959424"),
        "waw" -> idx("nlg:evt34"),
        "usd" -> idx("nlg:evt37")

      )

    val ms: Map[String, Seq[Statement]] =
      Map(
      )



    Namespace.withThreadNamespace(doc.namespace)  // added to allow for debug printing


    val result=transformerCallString(template,m,ms,"ln:company-noun")

   // val phrase: Phrase =transformerCall(template,m,ms,"ln:company-noun")
   // SpecLoader.phraseExport(System.out,phrase)

    println(result)

    val phrase=transformerCall(template,m,ms)

    println(phrase)



    result should be ("The company's pipeline was assessed by a data engineer (staff/259) and has the level of accuracy of 79.58%.")

    println(template_file + ": " + result)


  }




  "Template responsibility5" should "be processable" in {

    val template_file = "responsibility5.json"

    val template=path + template_file



    val doc = parse(
      """
            document
             prefix ex <http://example.org/>
             prefix foaf <http://xmlns.com/foaf/0.1/>
             prefix ln <https://plead-project.org/ns/loan#>
             prefix pl <https://plead-project.org/ns/plead#>
             prefix nlg <https://openprovenance.org/nlg#>
          	 prefix py <urn:python:var:>
          	 prefix file <urn:file:var:>
             prefix loan <https://plead-project.org/ns/loan#>
             prefix sk <https://scikit-learn.org/stable/modules/generated/sklearn.>

                entity(ex:recommendation/254,[prov:type = 'ln:AutomatedLoanRecommendation',prov:type = 'ex:AutomatedLoanRecommendation',ln:probability_chargeoff = "0.325" %% xsd:double,ln:recommendation = 'ln:deferred',ex:recommendation = 'ex:deferred'])
                entity(py:pipeline_new,[prov:type = 'sk:pipeline.Pipeline'])
                entity(py:pipeline_trained,[prov:type = 'sk:pipeline.Pipeline'])
                activity(ex:create_pipeline,-,-,[prov:type = 'ex:Creating'])
                activity(ex:train_pipeline,-,-,[prov:type = 'ex:DataFitting'])
                agent(ex:engineer0,[prov:type = 'ln:DataEngineer',prov:type = 'ex:DataEngineer',prov:type = 'prov:Person'])
                agent(ex:engineer,[prov:type = 'ln:DataEngineer',prov:type = 'ex:DataEngineer',prov:type = 'prov:Person'])
             endDocument
            """")

    def idx(key: String) = {
      doc.statements().find(s => s.getId().toString == key).get
    }

    val m: Map[String, Statement] =
      Map(

        "recommendation" -> idx("ex:recommendation/254")
      )

    val ms: Map[String, Seq[Statement]] =
      Map(

        "pipeline2" -> Seq(idx("py:pipeline_new"),idx("py:pipeline_trained")),

        "do2" -> Seq (idx("ex:create_pipeline"), idx("ex:train_pipeline")),

        "actor2" -> Seq (idx("ex:engineer0"), idx("ex:engineer"))

        )



    Namespace.withThreadNamespace(doc.namespace)  // added to allow for debug printing


    val result=transformerCallString(template,m,ms)


    result should be ("The recommendation (recommendation/254) was influenced (create_pipeline) by the agent (engineer0) and influenced (train_pipeline) by the agent (engineer).")

    println(template_file + ": " + result)


  }




  "Template responsibility6" should "be processable" in {

    val template_file = "responsibility6.json"

    val template=path + template_file



    val doc = parse(
      """
            document
             prefix ex <http://example.org/>
             prefix foaf <http://xmlns.com/foaf/0.1/>
             prefix ln <https://plead-project.org/ns/loan#>
             prefix pl <https://plead-project.org/ns/plead#>
             prefix nlg <https://openprovenance.org/nlg#>
          	 prefix py <urn:python:var:>
          	 prefix file <urn:file:var:>
             prefix loan <https://plead-project.org/ns/loan#>
             prefix sk <https://scikit-learn.org/stable/modules/generated/sklearn.>

                entity(loan:pipeline/1,[prov:type = 'ln:ApprovedPipeline',prov:type = 'sk:pipeline.Pipeline',ln:created_at = "2019-05-08T17:45:11" %% xsd:dateTime])

                entity(loan:pipeline/1,[prov:type = 'ln:ApprovedPipeline',prov:type = 'sk:pipeline.Pipeline',ln:created_at = "2019-05-08T17:45:11" %% xsd:dateTime])
                entity(file:loans_train.xz,[prov:type = 'ln:File',ln:filesize = "25471436" %% xsd:int,ln:created_at = "2019-05-08T22:16:12" %% xsd:dateTime,ln:n_cols = "123" %% xsd:int,ln:n_rows = "1195824" %% xsd:int,ln:sha256 = "414cbd79d8fd93d316a97521a5903bd4f5ee8b9e024e3116bb1fb7acdb591b57" %% xsd:string])
                entity(py:1558649326/5011959424,[prov:type = 'sk:pipeline.Pipeline'])
                entity(file:loans_filtered.xz,[prov:type = 'ln:File',ln:filesize = "36506212" %% xsd:int,ln:created_at = "2019-05-10T18:29:14.694384" %% xsd:dateTime,ln:n_cols = "31" %% xsd:int,ln:n_rows = "1345310" %% xsd:int,ln:sha256 = "b7d1e66bba8479bed23b75e84c1551b5a2322d194c3a4c2f6a5b40697b7e16f1" %% xsd:string])

                activity(ex:ml/approving/1558649326,2019-05-24T00:34:14.118+01:00,2019-05-24T00:34:14.814+01:00,[prov:type = 'pl:ApprovingPipeline'])
                activity(ex:ml/splitting/1558649326,2019-05-24T00:31:46.564+01:00,2019-05-24T00:33:58.062+01:00,[prov:type = 'pl:SplittingTestData'])
                activity(ex:ml/training/1558649326,2019-05-24T00:33:58.146+01:00,2019-05-24T00:34:13.861+01:00,[prov:type = 'pl:FittingData'])
                activity(ex:ml/filtering/1558649326,2019-05-24T00:26:10.789+01:00,2019-05-24T00:28:41.553+01:00,[prov:type = 'pl:SelectingData'])

                agent(ex:staff/37,[prov:type = 'ln:Manager',prov:type = 'prov:Person'])
                agent(ex:staff/259,[prov:type = 'ln:DataEngineer',prov:type = 'prov:Person'])
                agent(ex:staff/259,[prov:type = 'ln:DataEngineer',prov:type = 'prov:Person'])
                agent(ex:staff/259,[prov:type = 'ln:DataEngineer',prov:type = 'prov:Person'])
             endDocument
            """")

    def idx(key: String) = {
      doc.statements().find(s => s.getId().toString == key).get
    }

    val m: Map[String, Statement] =
      Map(

        "pipeline" -> idx("loan:pipeline/1")
      )

    val ms: Map[String, Seq[Statement]] =
      Map(
        "ancestor" -> Seq(idx("loan:pipeline/1"), idx("file:loans_train.xz"), idx("py:1558649326/5011959424"), idx("file:loans_filtered.xz")),
        "do" -> Seq( idx("ex:ml/approving/1558649326"), idx("ex:ml/splitting/1558649326"), idx("ex:ml/training/1558649326"), idx("ex:ml/filtering/1558649326")),
        "actor" -> Seq( idx("ex:staff/37"), idx("ex:staff/259"), idx("ex:staff/259"), idx("ex:staff/259"))
      )



    Namespace.withThreadNamespace(doc.namespace)  // added to allow for debug printing


    val result=transformerCallString(template,m,ms)


    result should be ("Responsibilities for the AI pipeline were " +
      "that manager (staff/37) approved the company pipeline (pipeline/1), that a data engineer (staff/259) split file (loans_train.xz), " +
      "that a data engineer (staff/259) fitted data for the company pipeline (1558649326/5011959424) and " +
      "that a data engineer (staff/259) selected file (loans_filtered.xz).")

    println(template_file + ": " + result)


  }




  "Template counterfactual1a" should "be processable" in {

    val template_file = "counterfactual1a.json"

    val template=path + template_file



    val doc = parse(
      """
            document
             prefix ex <http://example.org/>
             prefix foaf <http://xmlns.com/foaf/0.1/>
             prefix ln <https://plead-project.org/ns/loan#>
             prefix pl <https://plead-project.org/ns/plead#>
             prefix nlg <https://openprovenance.org/nlg#>
          	 prefix py <urn:python:var:>
          	 prefix file <urn:file:var:>
             prefix loan <https://plead-project.org/ns/loan#>
             prefix sk <https://scikit-learn.org/stable/modules/generated/sklearn.>

                entity(loan:applications/28,[prov:type = 'ln:LoanApplication',prov:type = 'pl:Controlled',ln:attr_term = " 36 months" %% xsd:string,ln:attr_purpose = "debt_consolidation" %% xsd:string,ln:attr_loan_amnt = "3000" %% xsd:float,ln:attr_emp_title = "Operations Manager" %% xsd:string,ln:attr_addr_state = "PA" %% xsd:string,ln:attr_title = "Debt consolidation" %% xsd:string,ln:attr_application_type = "Individual" %% xsd:string,ln:attr_emp_length = "10+ years" %% xsd:string,ln:attr_zip_code = "170xx" %% xsd:string,ln:attr_annual_inc = "125000" %% xsd:float,ln:attr_home_ownership = "MORTGAGE" %% xsd:string])

                wasDerivedFrom(nlg:evt26;loan:applications/28/cf/home_ownership/own,loan:applications/28,[prov:type = 'pl:CounterFactualDerivation',pl:cf_field = "home_ownership" %% xsd:string,pl:cf_value = "own" %% xsd:string])
                wasDerivedFrom(nlg:evt14;loan:applications/28/cf/home_ownership/rent,loan:applications/28,[prov:type = 'pl:CounterFactualDerivation',pl:cf_field = "home_ownership" %% xsd:string,pl:cf_value = "rent" %% xsd:string])
                wasDerivedFrom(nlg:evt43;loan:applications/28/cf/home_ownership/other,loan:applications/28,[prov:type = 'pl:CounterFactualDerivation',pl:cf_field = "home_ownership" %% xsd:string,pl:cf_value = "other" %% xsd:string])
                 entity(loan:applications/28/cf/home_ownership/own,[prov:type = 'pl:CounterFactualInput',prov:type = 'ln:LoanApplication',ln:attr_home_ownership = "own" %% xsd:string])
                 entity(loan:applications/28/cf/home_ownership/rent,[prov:type = 'pl:CounterFactualInput',prov:type = 'ln:LoanApplication',ln:attr_home_ownership = "rent" %% xsd:string])
                 entity(loan:applications/28/cf/home_ownership/other,[prov:type = 'pl:CounterFactualInput',prov:type = 'ln:LoanApplication',ln:attr_home_ownership = "other" %% xsd:string])
                entity(loan:applications/28/decision/cf/home_ownership/own,[prov:type = 'pl:CounterFactualOutput',prov:type = 'ln:AutomatedDecision',ln:result = 'ln:approved'])
                entity(loan:applications/28/decision/cf/home_ownership/rent,[prov:type = 'pl:CounterFactualOutput',prov:type = 'ln:AutomatedDecision',ln:result = 'ln:approved'])
                entity(loan:applications/28/decision/cf/home_ownership/other,[prov:type = 'pl:CounterFactualOutput',prov:type = 'ln:AutomatedDecision',ln:result = 'ln:approved'])

             endDocument
            """")

    def idx(key: String) = {
      doc.statements().find(s => s.getId().toString == key).get
    }

    val m: Map[String, Statement] =
      Map(

        "application" -> idx("loan:applications/28")
      )

    val ms: Map[String, Seq[Statement]] =
      Map(
        "wdf" -> Seq( idx("nlg:evt26"), idx("nlg:evt14"), idx("nlg:evt43") ),
        "cfapp" -> Seq( idx("loan:applications/28/cf/home_ownership/own"),idx("loan:applications/28/cf/home_ownership/rent"),idx("loan:applications/28/cf/home_ownership/other")),
        "decision" -> Seq(  idx("loan:applications/28/decision/cf/home_ownership/own"), idx("loan:applications/28/decision/cf/home_ownership/rent"), idx("loan:applications/28/decision/cf/home_ownership/other"))
      )



    Namespace.withThreadNamespace(doc.namespace)  // added to allow for debug printing


    val result=transformerCallString(template,m,ms)


    result should be ("We simulated alternative loan applications for all possible values of home_ownership, i.e., own, rent and other.")

    println(template_file + ": " + result)


  }


  "Template counterfactual1b" should "be processable" in {

    val template_file = "counterfactual1b.json"

    val template=path + template_file



    val doc = parse(
      """
            document
             prefix ex <http://example.org/>
             prefix foaf <http://xmlns.com/foaf/0.1/>
             prefix ln <https://plead-project.org/ns/loan#>
             prefix pl <https://plead-project.org/ns/plead#>
             prefix nlg <https://openprovenance.org/nlg#>
          	 prefix py <urn:python:var:>
          	 prefix file <urn:file:var:>
             prefix loan <https://plead-project.org/ns/loan#>
             prefix sk <https://scikit-learn.org/stable/modules/generated/sklearn.>

                entity(loan:applications/28,[prov:type = 'ln:LoanApplication',prov:type = 'pl:Controlled',ln:attr_term = " 36 months" %% xsd:string,ln:attr_purpose = "debt_consolidation" %% xsd:string,ln:attr_loan_amnt = "3000" %% xsd:float,ln:attr_emp_title = "Operations Manager" %% xsd:string,ln:attr_addr_state = "PA" %% xsd:string,ln:attr_title = "Debt consolidation" %% xsd:string,ln:attr_application_type = "Individual" %% xsd:string,ln:attr_emp_length = "10+ years" %% xsd:string,ln:attr_zip_code = "170xx" %% xsd:string,ln:attr_annual_inc = "125000" %% xsd:float,ln:attr_home_ownership = "MORTGAGE" %% xsd:string])

                wasDerivedFrom(nlg:evt26;loan:applications/28/cf/home_ownership/OWN,loan:applications/28,[prov:type = 'pl:CounterFactualDerivation',pl:cf_field = "home_ownership" %% xsd:string,pl:cf_value = "own" %% xsd:string])
                wasDerivedFrom(nlg:evt14;loan:applications/28/cf/home_ownership/RENT,loan:applications/28,[prov:type = 'pl:CounterFactualDerivation',pl:cf_field = "home_ownership" %% xsd:string,pl:cf_value = "rent" %% xsd:string])
                wasDerivedFrom(nlg:evt43;loan:applications/28/cf/home_ownership/OTHER,loan:applications/28,[prov:type = 'pl:CounterFactualDerivation',pl:cf_field = "home_ownership" %% xsd:string,pl:cf_value = "other" %% xsd:string])
                 entity(loan:applications/28/cf/home_ownership/OWN,[prov:type = 'pl:CounterFactualInput',prov:type = 'ln:LoanApplication',ln:attr_home_ownership = "own" %% xsd:string])
                 entity(loan:applications/28/cf/home_ownership/RENT,[prov:type = 'pl:CounterFactualInput',prov:type = 'ln:LoanApplication',ln:attr_home_ownership = "rent" %% xsd:string])
                 entity(loan:applications/28/cf/home_ownership/OTHER,[prov:type = 'pl:CounterFactualInput',prov:type = 'ln:LoanApplication',ln:attr_home_ownership = "other" %% xsd:string])
                entity(loan:applications/28/decision/cf/home_ownership/OWN,[prov:type = 'pl:CounterFactualOutput',prov:type = 'ln:AutomatedDecision',ln:result = 'ln:approved'])
                entity(loan:applications/28/decision/cf/home_ownership/RENT,[prov:type = 'pl:CounterFactualOutput',prov:type = 'ln:AutomatedDecision',ln:result = 'ln:approved'])
                entity(loan:applications/28/decision/cf/home_ownership/OTHER,[prov:type = 'pl:CounterFactualOutput',prov:type = 'ln:AutomatedDecision',ln:result = 'ln:approved'])

             endDocument
            """")

    def idx(key: String) = {
      doc.statements().find(s => s.getId().toString == key).get
    }

    val m: Map[String, Statement] =
      Map(

        "application" -> idx("loan:applications/28")
      )

    val ms: Map[String, Seq[Statement]] =
      Map(
        "wdf" -> Seq( idx("nlg:evt26"), idx("nlg:evt14"), idx("nlg:evt43") ),
        "cfapp" -> Seq( idx("loan:applications/28/cf/home_ownership/OWN"),idx("loan:applications/28/cf/home_ownership/RENT"),idx("loan:applications/28/cf/home_ownership/OTHER")),
        "decision" -> Seq(  idx("loan:applications/28/decision/cf/home_ownership/OWN"), idx("loan:applications/28/decision/cf/home_ownership/RENT"), idx("loan:applications/28/decision/cf/home_ownership/OTHER"))
      )



    Namespace.withThreadNamespace(doc.namespace)  // added to allow for debug printing


    val result=transformerCallString(template,m,ms)


    result should be ("In these simulations, the alternate applications will result in the following decisions: 'approval', 'approval' and 'approval' for values own, rent and other, respectively.")

    println(template_file + ": " + result)


  }



  "Template inclusion3" should "be processable" in {

    val template_file = "inclusion3.json"

    val template=path + template_file



    val doc = parse(
      """
            document
             prefix ex <http://example.org/>
             prefix foaf <http://xmlns.com/foaf/0.1/>
             prefix ln <https://plead-project.org/ns/loan#>
             prefix pl <https://plead-project.org/ns/plead#>
             prefix nlg <https://openprovenance.org/nlg#>
          	 prefix py <urn:python:var:>
          	 prefix file <urn:file:var:>
             prefix loan <https://plead-project.org/ns/loan#>
             prefix sk <https://scikit-learn.org/stable/modules/generated/sklearn.>


               entity(ex:recommendation/128350251,[prov:type = 'ln:AutomatedLoanRecommendation',ln:probability_chargeoff = "0.211044" %% xsd:float,ln:recommendation = 'ln:approved'])

               entity(ex:credit_history/128350251,[prov:type = 'ln:CreditReference',ln:attr_pub_rec_bankruptcies = "0" %% xsd:float,ln:attr_revol_bal = "4140" %% xsd:float,ln:created_at = "2019-01-10T14:10:16" %% xsd:dateTime,ln:attr_pub_rec = "0" %% xsd:float,ln:attr_earliest_cr_line = "Sep-2010" %% xsd:string,ln:attr_open_acc = "5" %% xsd:float,ln:attr_revol_util = "18.6" %% xsd:float,ln:attr_mort_acc = "0" %% xsd:float])
               entity(ex:fico_score/128350251,[prov:type = 'ln:FICOScore',ln:fico_range_low = "710" %% xsd:float,ln:fico_range_high = "714" %% xsd:float,ln:created_at = "2019-03-10T11:00:00" %% xsd:dateTime])

               entity(ex:applications/128350251,[prov:type = 'ln:LoanApplication',ln:attr_term = " 36 months" %% xsd:string,ln:attr_purpose = "credit_card" %% xsd:string,ln:attr_loan_amnt = "12025" %% xsd:float,ln:attr_emp_title = "Lead Vet Tech" %% xsd:string,ln:attr_addr_state = "CA" %% xsd:string,ln:attr_title = "Credit card refinancing" %% xsd:string,ln:attr_application_type = "Joint App" %% xsd:string,ln:attr_emp_length = "4 years" %% xsd:string,ln:attr_zip_code = "950xx" %% xsd:string,ln:attr_annual_inc = "50000" %% xsd:float,ln:attr_home_ownership = "RENT" %% xsd:string])

             endDocument
            """")

    def idx(key: String) = {
      doc.statements().find(s => s.getId().toString == key).get
    }

    val m: Map[String, Statement] =
      Map(

        "recommendation" -> idx("ex:recommendation/128350251")
      )

    val ms: Map[String, Seq[Statement]] =
      Map(
        "data" -> Seq(idx("ex:credit_history/128350251"), idx("ex:fico_score/128350251"), idx("ex:applications/128350251"))
      )



    Namespace.withThreadNamespace(doc.namespace)  // added to allow for debug printing


    val result=transformerCallString(template,m,ms)


    result should be ("The data Company Co considered for the borrower's loan application is " +
      "the month the borrower's earliest reported credit line was opened, " +
      "the number of mortgage accounts, " +
      "the number of open credit lines in the borrower's credit file, " +
      "the number of derogatory public records, " +
      "the number of public record bankruptcies, " +
      "total credit revolving balance, " +
      "revolving line utilization rate, " +
      "the borrower's higher FICO score, " +
      "the borrower's lower FICO score, " +
      "the state, " +
      "the self-reported annual income provided by the borrower during registration, " +
      "type of application, " +
      "employment length in years, " +
      "the self-reported job title, " +
      "the home ownership status provided by the borrower during registration, " +
      "the listed amount of the loan applied for by the borrower, " +
      "the purpose of the loan, " +
      "the number of payments on the loan, " +
      "the loan title provided by the borrower and " +
      "the address."

      )

    println(template_file + ": " + result)


  }




  "Template exclusion1" should "be processable" in {

    val template_file = "exclusion1.json"

    val template=path + template_file



    val doc = parse(
      """
            document
             prefix ex <http://example.org/>
             prefix foaf <http://xmlns.com/foaf/0.1/>
             prefix ln <https://plead-project.org/ns/loan#>
             prefix pl <https://plead-project.org/ns/plead#>
             prefix nlg <https://openprovenance.org/nlg#>
          	 prefix py <urn:python:var:>
          	 prefix file <urn:file:var:>
             prefix loan <https://plead-project.org/ns/loan#>
             prefix sk <https://scikit-learn.org/stable/modules/generated/sklearn.>
             prefix pd <https://pandas.pydata.org/#>


              entity(py:loan_features/128350251,[prov:type = 'pd:Series',ln:attr_sub_grade_C3 = "1" %% xsd:int,ln:attr_term = "36" %% xsd:int,ln:attr_total_acc = "9" %% xsd:float,ln:attr_loan_amnt = "12025" %% xsd:float,ln:attr_home_ownership_RENT = "1" %% xsd:int,ln:attr_int_rate = "14.07" %% xsd:float,ln:attr_initial_list_status_w = "1" %% xsd:int,ln:attr_emp_length = "4" %% xsd:float,ln:attr_dti = "4.6" %% xsd:float,ln:attr_purpose_credit_card = "1" %% xsd:int,ln:attr_earliest_cr_line = "2010" %% xsd:int,ln:attr_application_type_Joint_App = "1" %% xsd:int,ln:attr_fico_score = "712" %% xsd:float,ln:attr_addr_state_CA = "1" %% xsd:int,ln:attr_open_acc = "5" %% xsd:float,ln:attr_revol_util = "18.6" %% xsd:float,ln:attr_installment = "411.4" %% xsd:float,ln:attr_log_annual_inc = "4.69898" %% xsd:float,ln:attr_log_revol_bal = "3.61711" %% xsd:float,ln:attr_verification_status_Source_Verified = "1" %% xsd:int])

               entity(ex:recommendation/128350251,[prov:type = 'ln:AutomatedLoanRecommendation',ln:probability_chargeoff = "0.211044" %% xsd:float,ln:recommendation = 'ln:approved'])

               entity(ex:credit_history/128350251,[prov:type = 'ln:CreditReference',ln:attr_pub_rec_bankruptcies = "0" %% xsd:float,ln:attr_revol_bal = "4140" %% xsd:float,ln:created_at = "2019-01-10T14:10:16" %% xsd:dateTime,ln:attr_pub_rec = "0" %% xsd:float,ln:attr_earliest_cr_line = "Sep-2010" %% xsd:string,ln:attr_open_acc = "5" %% xsd:float,ln:attr_revol_util = "18.6" %% xsd:float,ln:attr_mort_acc = "0" %% xsd:float])
               entity(ex:fico_score/128350251,[prov:type = 'ln:FICOScore',ln:fico_range_low = "710" %% xsd:float,ln:fico_range_high = "714" %% xsd:float,ln:created_at = "2019-03-10T11:00:00" %% xsd:dateTime])

               entity(ex:applications/128350251,[prov:type = 'ln:LoanApplication',ln:attr_term = " 36 months" %% xsd:string,ln:attr_purpose = "credit_card" %% xsd:string,ln:attr_loan_amnt = "12025" %% xsd:float,ln:attr_emp_title = "Lead Vet Tech" %% xsd:string,ln:attr_addr_state = "CA" %% xsd:string,ln:attr_title = "Credit card refinancing" %% xsd:string,ln:attr_application_type = "Joint App" %% xsd:string,ln:attr_emp_length = "4 years" %% xsd:string,ln:attr_zip_code = "950xx" %% xsd:string,ln:attr_annual_inc = "50000" %% xsd:float,ln:attr_home_ownership = "RENT" %% xsd:string])

             endDocument
            """")

    def idx(key: String) = {
      doc.statements().find(s => s.getId().toString == key).get
    }

    val m: Map[String, Statement] =
      Map(

        "features"  -> idx("py:loan_features/128350251"),
        "recommendation" -> idx("ex:recommendation/128350251")
      )

    val ms: Map[String, Seq[Statement]] =
      Map(
        "data" -> Seq(idx("ex:credit_history/128350251"), idx("ex:fico_score/128350251"), idx("ex:applications/128350251"))
      )



    Namespace.withThreadNamespace(doc.namespace)  // added to allow for debug printing


    val result=transformerCallString(template,m,ms)


    result should be ("The data that Company Co excluded for the processing of the borrower's loan application are " +
      "the number of derogatory public records" + ", " +
      "the loan title provided by the borrower" + ", " +
      "the self-reported job title" + ", " +
      "the address" + ", " +
      "the number of mortgage accounts" + " and " +
      "the number of public record bankruptcies" +
      "."
    )


    println(template_file + ": " + result)


  }


}

