

package org.openprovenance.prov.scala


import org.openprovenance.prov.scala.interop.CommandLine
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.collection.mutable
import org.scalactic.Explicitly._
import org.scalactic.TypeCheckedTripleEquals._
class FactorGraphSpec extends AnyFlatSpec with Matchers {

  "A Stack" should "pop values in last-in-first-out order" in {
    val stack = new mutable.Stack[Int]
    stack.push(1)
    stack.push(2)
    stack.pop() should be (2)
    stack.pop() should be (1)
  }
    


  "FactorGraph" should "be exportable" in {
    val args= Array("translate", 
                    "--infile", "src/test/resources/factorgraph/Risk_Prov_FG1.provn", 
                    "--outfiles", "target/risk.m,target/risk.svg")
    CommandLine.main(args)
    
  }
   
}