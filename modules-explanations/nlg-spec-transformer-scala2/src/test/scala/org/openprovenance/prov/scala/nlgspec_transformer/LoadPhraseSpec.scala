


package org.openprovenance.prov.scala.nlgspec_transformer

import org.openprovenance.prov.scala.nlgspec_transformer.specTypes.Phrase
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import simplenlg.framework.{NLGElement, PhraseElement}


object Tester {
  def roundtrip (phrase: Phrase, file: String): (Phrase, NLGElement, String) = {

    SpecLoader.phraseExport(file, phrase)

    val phrase2: Phrase = SpecLoader.phraseImport(file)

    val spec =phrase2.expand().asInstanceOf[NLGElement]

    val result=defs.realise(spec)

    (phrase2, spec, result)

  }
}


class LoadPhraseSpec extends AnyFlatSpec with Matchers {
  import defs._

  "A Phrase with an object" should "be serializable" in {


    val phrase= Clause(NounPhrase(null, "Luc"),
                       VerbPhrase("eats"),
                       NounPhrase(null, "cabbage"),
      None)

    val (phrase2, spec, result)=Tester.roundtrip(phrase, "target/phrase1.json")


    //phrase should be (phrase2)


    result should be ("Luc eats cabbage.")
  }

  "phrase 2" should "be serializable" in {


    val phrase= Clause(NounPhrase(null, "Luc"),
      VerbPhrase("has"),
       NounPhrase("a", "day", Seq(), Seq(AdjectivePhrase("bad"))),
                 None)

    val (phrase2, spec, result)=Tester.roundtrip(phrase, "target/phrase2.json")

    //println(" spec is " + spec)

    //println("result is " + result)

    result should be ("Luc has a bad day.")
  }


  "phrase 3" should "be serializable" in {


    val phrase= Clause(StringPhrase("Luc"),
      VerbPhrase("has"),
      NounPhrase("a", "day", Seq(), Seq(AdjectivePhrase("bad"))),
      None)

    val (phrase2, spec, result)=Tester.roundtrip(phrase, "target/phrase3.json")

    //println(" spec is " + spec)

    //println("result is " + result)

    result should be ("Luc has a bad day.")
  }

  "data.sources template" should "be redadable" in {
    val template= SpecLoader.phraseImport("src/test/resources/examples/data.sources1.json")

    //println(template)

    val spec =template.expand().asInstanceOf[PhraseElement]

    val result=defs.realise(spec)

  //  println(spec)
 //   println(result)

    result should be ("The data sources were the loan application provided by you, a credit reference provided by the creditAgency at 16:00 and a fico score provided by the credit agency at 18:00.")

  }

  "attribution sentence" should "be redadable" in {
    val template= SpecLoader.phraseImport("src/test/resources/examples/attribution.json")

   // println(template)

    val spec =template.expand().asInstanceOf[PhraseElement]

    val result=defs.realise(spec)

  //  println(spec)
   // println(result)

    result should be ("E1 was attributed to ag1.")

  }

  "loan 15 sentence" should "be redadable" in {
    val template= SpecLoader.phraseImport("src/test/resources/examples/loan15.json")

   // println(template)

    val spec =template.expand().asInstanceOf[PhraseElement]

    val result=defs.realise(spec)

  //  println(spec)
   // println(result)

    result should be ("The loan application was automatically approved based on a combination of the borrower's loan application and third party data: the borrower's FICO score and the borrower's credit reference.")

  }

  "human decisionsentence" should "be redadable" in {
    val template= SpecLoader.phraseImport("src/test/resources/examples/human.decision1.json")

  //  println(template)

    val spec =template.expand().asInstanceOf[PhraseElement]

    val result=defs.realise(spec)

   // println(spec)
   // println(result)

    result should be ("The automated recommendation was reviewed by a credit officer (staff/112) whose decision was based on your application (applications/128350251), the automated recommendation (recommendation/128350251) itself, a credit reference (credit_history/128350251) and a fico score (fico_score/128350251).")

  }

  "paragraph" should "be serializable" in {


    val phrase1= Clause(NounPhrase(null, "Luc"),
      VerbPhrase("has"),
      NounPhrase("a", "day", Seq(), Seq(AdjectivePhrase("bad"))),
      None)

    val phrase2= Clause(NounPhrase(null, "Alice"),
      VerbPhrase("has"),
      NounPhrase("a", "day", Seq(), Seq(AdjectivePhrase("fantastic"))),
      None)

    val paragraph=Paragraph(Seq(phrase1,phrase2), Map())

    val (phrase_, spec, result)=Tester.roundtrip(paragraph, "target/paragraph.json")

    //println(" spec is " + spec)

    //println("result is " + result)

    result should be ("Luc has a bad day. Alice has a fantastic day.\n\n")
  }

}

