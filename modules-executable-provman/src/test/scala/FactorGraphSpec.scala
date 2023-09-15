


package org.openprovenance.prov.scala

import org.scalatest._
import org.openprovenance.prov.scala.interop.CommandLine
import collection.mutable.Stack

class FactorGraphSpec extends FlatSpec with Matchers {
  
    "A Stack" should "pop values in last-in-first-out order" in {
    val stack = new Stack[Int]
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