


package org.openprovenance.prov.scala

import org.scalatest._
import org.openprovenance.prov.scala.viz.Animator
import org.scalatest.flatspec.AnyFlatSpec


abstract class AnimatorSpec extends AnyFlatSpec {

  "Animator" should "run" in {
    Animator.main(Array("src/test/resources/ipaw16/history-noagent.provn", "target/history.svg"))
  }

}