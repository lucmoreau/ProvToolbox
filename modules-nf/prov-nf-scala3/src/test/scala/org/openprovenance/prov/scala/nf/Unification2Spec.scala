
package org.openprovenance.prov.scala.nf

import org.openprovenance.prov.scala.immutable.{Document, ProvNInputer, QualifiedName}
import org.openprovenance.prov.scala.interop.{FileInput, Input}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.io.File

class Unification2Spec extends AnyFlatSpec with Matchers {
	val EX_NS="http://example/"

	def q(local: String): QualifiedName = {
   new QualifiedName("ex",local,EX_NS)
  }




  def readDoc(f: String): Document = {
    val in:Input=FileInput(new File(f))
    val doc=new ProvNInputer().input(in,Map())
    doc
  }

    

  def checkfile (name: String, test1: Boolean=true): Unit = {
	  val doc1=readDoc("src/test/resources/unify/" + name + ".provn")
  
	  
	  
	  
	  
    val doc2=readDoc("src/test/resources/unify/" + name + "-sol.provn")
    
    if (test1) {
      doc1 should not be (doc2)
    }
	  
	  println("=============")
	  val doc3=Normalizer.fusion(doc1)
	  println("=============")
	  val doc4=Normalizer.fusion(doc2)
	  println("=============")
	  println(doc3)
	  println(doc4)
    doc3 should be (doc4)
 
            
  }
  "File unify-wgb0" should "unify with key constraints" in {
     checkfile("unify-wgb0")
   }


  "File unify-wgb1" should "unify with key constraints" in {
     checkfile("unify-wgb1")
  }
 
  "File unify-wgb2" should "unify with key constraints" in {
	  checkfile("unify-wgb2")
  }
  
  "File unify-wgb3" should "unify with key constraints" in {
	  checkfile("unify-wgb3")
  }

  
  
  "File unify-wib0" should "unify with key constraints" in {
     checkfile("unify-wib0")
   }

  "File unify-wib1" should "unify with key constraints" in {
     checkfile("unify-wib1")
  }
 
  "File unify-wib2" should "unify with key constraints" in {
	  checkfile("unify-wib2")
  }
 
  "File unify-wib3" should "unify with key constraints" in {
	  checkfile("unify-wib3")
  }
  
  "File unify-bun-wgb1" should "unify with key constraints" in {
     checkfile("unify-bun-wgb1")
  }

    
  "File unify-usd0" should "unify with key constraints" in {
     checkfile("unify-usd0",test1 = false)
   }

 
   "File unify-usd1" should "unify with key constraints" in {
     checkfile("unify-usd1")
  }
  /* ERROR IN TEST: Used shouldn't have a complex key!

  "File unify-usd2" should "unify with key constraints" in {
	  checkfile("unify-usd2")
  }
 
  "File unify-usd3" should "unify with key constraints" in {
	  checkfile("unify-usd3")
  }
 */
      
  "File unify-wdf0" should "unify with key constraints" in {
     checkfile("unify-wdf0", test1 = false)
   }

  "File unify-wdf1" should "unify with key constraints" in {
     checkfile("unify-wdf1", test1 = false)
  }

}
