


package nlg.wrapper

import org.openprovenance.prov.scala.wrapper.{IO, defs}
import org.openprovenance.prov.scala.wrapper.defs.{AdjectivePhrase, Clause, CoordinatedPhrase, EnumeratedList, NounPhrase, Paragraph, Phrase, PrepositionPhrase, StringPhrase, VerbPhrase}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import simplenlg.framework.{NLGElement, PhraseElement}

import scala.Console.in

object Formatter extends Enumeration {
  val normal, html, markup = Value
}

object Tester {
  def roundtrip(phrase: Phrase, file: String, formatter: Formatter.Value = Formatter.normal): (Phrase, NLGElement, String) = {

    IO.phraseExport(file, phrase)

    val phrase2: Phrase = IO.phraseImport(file)

    val spec: NLGElement = phrase2.expand().asInstanceOf[NLGElement]


    formatter match {
      case Formatter.html =>
        val myRealiser = defs.withHTMLFormatter()
        val result = defs.realise(spec, myRealiser)
        (phrase2, spec, result)
      case Formatter.normal =>
        val result = defs.realise(spec)
        (phrase2, spec, result)
      case Formatter.markup =>
        val myRealiser = defs.withMarkupFormatter()
        val result = defs.realise(spec, myRealiser)
        (phrase2, spec, result)

    }
  }
}


class BPhraseSpec extends AnyFlatSpec with Matchers {

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


  "adj phrase " should "be redadable" in {
    val template= IO.phraseImport("src/test/resources/examples/prep.json")

    println(template)

    val spec =template.expand().asInstanceOf[PhraseElement]

    val result=defs.realise(spec)

    println(spec)
    println(result)

    result should be ("By you.")

  }


  "data.sources template" should "be redadable" in {
    val template= IO.phraseImport("src/test/resources/examples/data.sources1.json")

    println(template)

    val spec =template.expand().asInstanceOf[PhraseElement]

    val result=defs.realise(spec)

    println(spec)
    println(result)

    result should be ("The data sources were the loan application provided by you, a credit reference provided by the creditAgency at 16:00 and a fico score provided by the credit agency at 18:00.")

  }

  "attribution sentence" should "be redadable" in {
    val template= IO.phraseImport("src/test/resources/examples/attribution.json")

    println(template)

    val spec =template.expand().asInstanceOf[PhraseElement]

    val result=defs.realise(spec)

    println(spec)
    println(result)

    result should be ("E1 was attributed to ag1.")

  }

  "loan 15 sentence" should "be redadable" in {
    val template= IO.phraseImport("src/test/resources/examples/loan15.json")

    println(template)

    val spec =template.expand().asInstanceOf[PhraseElement]

    val result=defs.realise(spec)

    println(spec)
    println(result)

    result should be ("The loan application was automatically approved based on a combination of the borrower's loan application and third party data: the borrower's FICO score and the borrower's credit reference.")

  }

  "human decision sentence" should "be redadable" in {
    val template= IO.phraseImport("src/test/resources/examples/human.decision1.json")

    println(template)

    val spec =template.expand().asInstanceOf[PhraseElement]

    val result=defs.realise(spec)

    println(spec)
    println(result)

    result should be ("The automated recommendation was reviewed by a credit officer (staff/112) whose decision was based on it application (applications/128350251), the automated recommendation (recommendation/128350251) itself, a credit reference (credit_history/128350251) and a fico score (fico_score/128350251).")

  }

  "provided  2 by sentence" should "be redadable" in {
    val template= IO.phraseImport("src/test/resources/examples/application-provided2.json")

    println(template)

    val spec =template.expand().asInstanceOf[PhraseElement]

    val result=defs.realise(spec)

    println(spec)
    println(result)

    result should be ("The loan application provided by the credit agency.")

  }

  "provided by sentence" should "be redadable" in {
    val template= IO.phraseImport("src/test/resources/examples/application-provided.json")

    println(template)

    val spec =template.expand().asInstanceOf[PhraseElement]

    val result=defs.realise(spec)

    println(spec)
    println(result)

    result should be ("The loan application which were provided by you.")

  }

  "sentences" should "be readable  " in {
    val templates: Array[String] = Array(
    "src/test/resources/examples/application-provided.json",
      "src/test/resources/examples/house-built.json",
      "src/test/resources/examples/houses-built.json"
    )
    val expanded: Array[String] = Array(
      "The loan application which were provided by you.",
      "The house which was quickly built.",
      "The houses which were quickly built."
    )

    templates.zip(expanded).foreach { case (t, v) =>
      val template = IO.phraseImport(t)
      val spec = template.expand().asInstanceOf[PhraseElement]
      val result = defs.realise(spec)
      println(t)
      println(result)
      result should be(v)
    }
  }

  "A Phrase with an coordinated object" should "be serializable" in {


    val phrase= Clause(NounPhrase(null, "Luc"),
      VerbPhrase("eats"),
      Some(CoordinatedPhrase(null,Seq(NounPhrase(null,"cabbage"),NounPhrase(null,"lettuce")),"and")),
      None)

    val (phrase2, spec, result)=Tester.roundtrip(phrase, "target/coordination.json")


    //phrase should be (phrase2)


    result should be ("Luc eats cabbage and lettuce.")
  }

  "A Paragraph" should "be serializable" in {


    val phrase1= Clause(NounPhrase(null, "Luc"),
      VerbPhrase("eats"),
      Some(CoordinatedPhrase(null,Seq(NounPhrase(null,"cabbage"),NounPhrase(null,"lettuce")),"and")),
      None)
    val phrase2= Clause(NounPhrase(null, "Alice"),
      VerbPhrase("eats"),
      Some(CoordinatedPhrase(null,Seq(NounPhrase(null,"apples"),NounPhrase(null,"pears")),"and")),
      None)

    val paragraph=Paragraph(Seq(phrase1,phrase2), Map())

    val (paragraph2, spec, result)=Tester.roundtrip(paragraph, "target/paragraph.json")

    println("|"+result+"|")

    //phrase should be (phrase2)


    "|"+result+"|" should be ("|"+"Luc eats cabbage and lettuce. Alice eats apple and pear.\n\n"+"|")
  }




  "A Phrase with an enumeration 1" should "be serializable" in {


    val phrase: EnumeratedList = EnumeratedList(Seq(NounPhrase(null,"cabbage"),NounPhrase(null,"lettuce")),null)

    val (phrase2, spec, result)=Tester.roundtrip(phrase, "target/enumeration1.json",Formatter.normal)


    //phrase should be (phrase2)


    result should be ("1 - cabbage\n2 - lettuce\n")
  }




  "A Phrase with an enumeration 2" should "be serializable" in {


    val phrase: EnumeratedList = EnumeratedList(Seq(NounPhrase(null,"cabbage"),NounPhrase(null,"lettuce")),null)

    val (phrase2, spec, result)=Tester.roundtrip(phrase, "target/enumeration2.json", Formatter.html)


    //phrase should be (phrase2)


    result should be ("<ol><li>cabbage</li><li>lettuce</li></ol>")
  }

/*
  "A Phrase with an enumeration 3" should "be serializable" in {


    val phrase: EnumeratedList = EnumeratedList(Seq(NounPhrase(null,"cabbage"),NounPhrase(null,"lettuce")),null)

    val (phrase2, spec, result)=Tester.roundtrip(phrase, "target/enumeration1.json", true)


    //phrase should be (phrase2)


    result should be ("<ol><li>cabbage</li><li>lettuce</li></ol>")
  }


 */



  "A Phrase 2 with an coordinated object" should "be serializable" in {


    val phrase= Clause(NounPhrase(null, "Luc"),
      VerbPhrase("eats"),
      Some(CoordinatedPhrase(null,Seq(NounPhrase(null,"cabbage"),NounPhrase(null,"lettuce")),"and")),
      None)

    val (phrase2, spec, result)=Tester.roundtrip(phrase, "target/coordination2.json", Formatter.markup)


    //phrase should be (phrase2)


    result should be ("Luc eats cabbage and lettuce.")
  }



  "A Phrase 3 with an coordinated object" should "be serializable" in {


    val phrase= Clause(NounPhrase(null, "Luc"),
      VerbPhrase("eats"),
      Some(CoordinatedPhrase(null,
                             Seq(NounPhrase(null,"cabbage"),
                                 NounPhrase(null,"lettuce")),
                             "and",
                              Seq(PrepositionPhrase("with",Some(NounPhrase(null,"appetite")))),
                             defs.olFeature)),
      None,Seq(),Seq(), Seq(), Seq(), Seq(),null,defs.spanFeature)

    val (phrase2, spec, result)=Tester.roundtrip(phrase, "target/coordination3.json", Formatter.markup)


    println(spec.printTree("  "))
    //phrase should be (phrase2)


    result should be ("<span>Luc eats <ol><li>cabbage and</li> <li>lettuce</li> with appetite</ol></span>.")
  }

  val htmlExplanationClass=Map(Constants.MARKUP_ATTRIBUTES ->"class=\"explanation\"")
  "A Phrase 4 with an coordinated object" should "be serializable" in {


    val phrase= Clause(NounPhrase(null, "Luc"),
      VerbPhrase("eats"),
      Some(CoordinatedPhrase(null,
        Seq(NounPhrase(null,"cabbage"),
          NounPhrase(null,"lettuce")),
        "and",
        Seq(),
        defs.olFeature)),
      None,Seq(),Seq(),Seq(), Seq(),Seq(),null,defs.spanFeature ++ htmlExplanationClass)

    val (phrase2, spec, result)=Tester.roundtrip(phrase, "target/coordination4.json", Formatter.markup)


    println(spec.printTree("  "))
    //phrase should be (phrase2)


    result should be ("<span class=\"explanation\">Luc eats <ol><li>cabbage and</li> <li>lettuce</li></ol></span>.")
  }

  "coord 1 " should "be readable" in {
    val phrase=IO.phraseImport("src/test/resources/examples/coordination_toread.json")
  }

  "coord 2 " should "be readable" in {
    val phrase=IO.phraseImport("src/test/resources/examples/coordination2_toread.json")
  }

  "coord 3 " should "be readable" in {
    val phrase=IO.phraseImport("src/test/resources/examples/coordination3_toread.json")
  }

  "cra " should "be readable" in {
    val phrase=IO.phraseImport("src/test/resources/examples/cra.json")
    val spec=phrase.expand().asInstanceOf[NLGElement]

    println(spec.printTree(" "))

  }

  "to eat " should "be readable" in {
    val phrase=IO.phraseImport("src/test/resources/examples/toeat.json")
    val spec=phrase.expand().asInstanceOf[NLGElement]


    val (phrase2, spec2, result)=Tester.roundtrip(phrase, "target/toeat.json", Formatter.markup)


    //phrase should be (phrase2)

  //  println(spec.printTree(" "))

    result should be ("Thus I somehow like to eat greens.")

  }

  "this " should "be readable" in {
    val phrase=IO.phraseImport("src/test/resources/examples/this.json")
    val spec=phrase.expand().asInstanceOf[NLGElement]


    val (phrase2, spec2, result)=Tester.roundtrip(phrase, "target/this.json", Formatter.markup)


    //phrase should be (phrase2)

    println(spec.printTree(" "))

    result should be ("That is because of negative credit history information on your file.")

  }

  "markup example " should "be readable" in {
    val phrase=IO.phraseImport("src/test/resources/examples/example-with-markup.json")
    val spec=phrase.expand().asInstanceOf[NLGElement]

    println(spec.printTree(" "))

    val real1=defs.theRealiser
    val real2=defs.withMarkupFormatter()

    val sentence1=real1.realiseSentence(spec)
    val sentence2=real2.realiseSentence(spec)


    println("sentence is: " + sentence1)
    println("sentence is: " + sentence2)

  }


  "closing lexicon " should "work" in {
    defs.lexicon.close()
  }

}

