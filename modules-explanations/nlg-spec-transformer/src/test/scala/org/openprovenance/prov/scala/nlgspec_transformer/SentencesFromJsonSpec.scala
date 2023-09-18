


package org.openprovenance.prov.scala.nlgspec_transformer

import org.openprovenance.prov.scala.nlgspec_transformer.specTypes.Phrase
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import simplenlg.framework.NLGElement

object Tester2 {
  def doLoad (file: String): (Phrase, NLGElement, String) = {

    val phrase2: Phrase = SpecLoader.phraseImport("src/test/resources/sentences/" + file)

    val spec =phrase2.expand().asInstanceOf[NLGElement]

    val result=defs.realise(spec)

    (phrase2, spec, result)

  }
}


class SentencesFromJsonSpec extends AnyFlatSpec with Matchers {

  "Sentences" should "expand" in {



    val (phrase2, spec, result1)=Tester2.doLoad("regret1.json")
    result1 should be ("We regret that we inform you that we declined your application because of your low credit score.")


    val (_, _, result2)=Tester2.doLoad("regret2.json")
    result2 should be ("We regret to inform you that we declined your application because of your low credit score.")
  }


}

