


package nlg.wrapper

import org.openprovenance.prov.scala.wrapper.defs
import org.openprovenance.prov.scala.wrapper.defs.{Clause, NounPhrase, VerbPhrase}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import simplenlg.framework.NLGElement




class APhraseSpec extends AnyFlatSpec with Matchers {

  "One  " should "be able to realise a sentence " in {

    val spec: NLGElement = new Clause(
      subject = NounPhrase(null, "Luc" ),
      verb = VerbPhrase("eat"),
      direct_object = Some(NounPhrase(null, "cabbage")),
      None
    ).expand()

    val result = defs.realise(spec)

    System.out.println("result: " + result)

    try {
      result should be("Luc eats cabbage.")
    } catch {
      case e: Exception => {
        System.out.println("Exception: " + e)
        System.out.println("result: " + result)
        System.out.println("spec: " + spec)
        throw e
      }
    }
  }


}

