


package org.openprovenance.prov.scala

import org.openprovenance.prov.scala.viz.Animator
import org.scalatest._


class AnimatorSpec extends FlatSpec with Matchers {

  "Animator" should "run" in {
    Animator.main(Array("src/test/resources/ipaw16/history-noagent.provn", "target/history.svg"))
  }

}