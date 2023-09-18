
package org.openprovenance.prov.scala.nf

import org.openprovenance.prov.model.Namespace
import org.openprovenance.prov.model.StatementOrBundle.Kind
import org.openprovenance.prov.scala.immutable._
import org.openprovenance.prov.scala.interop.{FileInput, Input}
import org.openprovenance.prov.scala.nf.xml.XmlNfBean
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.io.File

class UnificationSpec extends AnyFlatSpec with Matchers {
	val EX_NS="http://example/"

	def q(local: String) = {
   new QualifiedName("ex",local,EX_NS)
  }



  
  "TransitiveClosure" should "be computable" in {
    val tc=new TransitiveClosure[QualifiedName]
    tc.add2(q("a"), q("b"))
    tc.add2(q("c"), q("a"))
    
    println(tc.transitiveClosure())
    
    tc.transitiveClosure().contains((q("b"),q("c"))) should be (true)
    
    println(tc.partition())
    
    tc.add2(q("x"), q("y"))
        
    println(tc.partition())

    tc.add2(Set(q("i"), q("j"), q("k")))
    
    println(tc.partition())
    
    tc.add2(Set(q("i"), q("x")))
        
    println(tc.partition())
    
    //println(List(q("k"), q("x"),q("y"), q("i"),q("j")).sorted)
    //println(List(q("k"), q("x"),q("y"), q("i"),q("j")).sorted.toSet)



  }

  "Entity ent1" should "have a normal form" in {
    val doc=nf("src/test/resources/unify/ent1.provn")
    doc.indexer.entity.size should be (1)
    println(doc.indexer.entity)
    doc.indexer.entity(q("e1")).value.size should be (1)

  }

  
  def nf(f: String) = {
    val in:Input=new FileInput(new File(f))
	  val doc=CommandLine.parseDocumentToNormalForm(in)
	  doc
  }


    
  "Entity ent1" should "src/test/resources/unify" in {
    val doc=nf("src/test/resources/unify/ent1.provn")
    val u=doc.unify()
    val d=u.toDocument()

    u.indexer.entity.size should be (2)
    u.indexer.activity.size should be (0)
    u.indexer.entity(q("e1")).getKind() should be (Kind.PROV_ENTITY)
    d.statements().count(_.isInstanceOf[Activity]) should be (0)
    d.statements().count(_.isInstanceOf[Entity]) should be (2)
    d.statements().count(_.isInstanceOf[WasGeneratedBy]) should be (2)
        
  }
  
       
  "Activity act1" should "src/test/resources/unify" in {
    val doc=nf("src/test/resources/unify/act1.provn")
    val u=doc.unify()
    val d=u.toDocument()
    
    u.indexer.entity.size should be (0)
    u.indexer.activity.size should be (2)
    u.indexer.activity(q("a1")).getKind() should be (Kind.PROV_ACTIVITY)
    d.statements().count(_.isInstanceOf[Activity]) should be (2)
    d.statements().count(_.isInstanceOf[Entity]) should be (0)
    d.statements().count(_.isInstanceOf[WasInvalidatedBy]) should be (2)
        
  }   

  "Activity act2" should "src/test/resources/unify" in {
    val doc=nf("src/test/resources/unify/act2.provn")
    val u=doc.unify()
    val d=u.toDocument()
    
    u.indexer.entity.size should be (0)
    u.indexer.activity.size should be (2)
    u.indexer.activity(q("a1")).getKind() should be (Kind.PROV_ACTIVITY)
    d.statements().count(_.isInstanceOf[Activity]) should be (2)
    d.statements().count(_.isInstanceOf[Entity]) should be (0)
    d.statements().count(_.isInstanceOf[Used]) should be (2)
        
  }   
  
  
      
  "Entity ent10" should "src/test/resources/unify" in {
    val doc=nf("src/test/resources/unify/ent10.provn")
    val u=doc.unify()
    val d=u.toDocument()

    u.indexer.entity.size should be (2)
    u.indexer.activity.size should be (0)
    u.indexer.entity(q("e10")).getKind() should be (Kind.PROV_ENTITY)
    
    d.statements().count(_.isInstanceOf[Activity]) should be (0)
    d.statements().count(_.isInstanceOf[Entity]) should be (2)
    d.statements().count(_.isInstanceOf[WasGeneratedBy]) should be (4)
        
  }
  
        
  "Entity ent10b" should "unify" in {
    val doc=nf("src/test/resources/unify/ent10b.provn")
    val u=doc.unify()
    val d=u.toDocument()

    u.indexer.entity.size should be (2)
    u.indexer.activity.size should be (0)
    u.indexer.entity(q("e10")).getKind() should be (Kind.PROV_ENTITY)
    
    
    d.statements().count(_.isInstanceOf[Activity]) should be (0)
    d.statements().count(_.isInstanceOf[Entity]) should be (2)
    d.statements().count(_.isInstanceOf[WasGeneratedBy]) should be (6)
  
        
  }
  
    "Activity act20b" should "src/test/resources/unify" in {
    val doc=nf("src/test/resources/unify/act20b.provn")
    val u=doc.unify()
    val d=u.toDocument()

    u.indexer.entity.size should be (0)
    u.indexer.activity.size should be (2)
    u.indexer.activity(q("a1")).getKind() should be (Kind.PROV_ACTIVITY)
    
    
    d.statements().count(_.isInstanceOf[Activity]) should be (2)
    d.statements().count(_.isInstanceOf[Entity]) should be (0)
    d.statements().count(_.isInstanceOf[Used]) should be (6)
  
        
  }
    

  def checkfile (name: String, test1: Boolean=true): Unit = {
	  val doc1=nf("src/test/resources/unify/" + name + ".provn")
  
    // linearize to document, and check it has same normal form, as well as same normal form of reference solution
    val doc2=doc1.unify().keyConstraints().toDocument().toNormalForm(new DocumentProxy(new Namespace()))
    val sol=nf("src/test/resources/unify/" + name + "-sol.provn")
    
    if (test1) {
      doc1 should not be (sol)
      }

    doc2 should be (sol)
  
  
    // create proper normal form, save, read it, and compare with reference normal form
    val doc3=doc1.unify().keyConstraints().toNormalForm().unify()
    XmlNfBean.toXMLFile("target/" + name + ".xml",doc3,null)
    

    val doc4=XmlNfBean.fromXMLWithFileStream("target/" + name + ".xml")
    val doc5=XmlNfBean.fromXMLWithFileStream("src/test/resources/unify/" + name + "-nf.xml")

    
    doc4 should be (doc3)
    doc4 should be (doc5)
   
            
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

  /*  
  "File unify-usd0" should "unify with key constraints" in {
     checkfile("unify-usd0")
   }

  "File unify-usd1" should "unify with key constraints" in {
     checkfile("unify-usd1")
  }
 
  
  "File unify-usd2" should "unify with key constraints" in {
	  checkfile("unify-usd2")
  }
 
  "File unify-usd3" should "unify with key constraints" in {
	  checkfile("unify-usd3")
  }
*/
      
  "File unify-wdf0" should "unify with key constraints" in {
     checkfile("unify-wdf0", false)
   }

  
}
