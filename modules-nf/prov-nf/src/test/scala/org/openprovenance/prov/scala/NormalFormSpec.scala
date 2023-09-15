
package org.openprovenance.prov.scala

import java.io.File

import org.openprovenance.prov.scala.immutable.{ProvNInputer, QualifiedName}
import org.openprovenance.prov.scala.interop.{FileInput, Input}
import org.openprovenance.prov.scala.nf.xml.XmlBean
import org.openprovenance.prov.scala.nf.{CommandLine, TransitiveClosure}
import org.scalatest._

class NormalFormSpec extends FlatSpec with Matchers {
	val EX_NS="http://example/"

	def q(local: String) = {
   new QualifiedName("ex",local,EX_NS)
  }
  "Entity ent1" should "have a normal form" in {
    val doc=nf("src/test/resources/nf/ent1.provn")
    doc.indexer.entity.size should be (1)
    doc.indexer.entity(q("e1")).value.size should be (2)

  }
  "Agent ag1" should "have a normal form" in {
    val doc=nf("src/test/resources/nf/ag1.provn")
    doc.indexer.agent.size should be (1)
    doc.indexer.agent(q("ag1")).value.size should be (2)
  }
  
  "Activity act1" should "have a normal form" in {
    val doc=nf("src/test/resources/nf/act1.provn")
    doc.indexer.activity.size should be (1)
    doc.indexer.activity(q("act1")).startTime.size should be (2)
    doc.indexer.activity(q("act1")).endTime.size should be (2)
  }
    
  "Derivation wdf1" should "have a normal form" in {
    val doc=nf("src/test/resources/nf/wdf1.provn")
    doc.indexer.wasDerivedFrom.size should be (1)
    doc.indexer.wasDerivedFrom(q("wdf1")).generatedEntity.size should be (2)
    doc.indexer.wasDerivedFrom(q("wdf1")).usedEntity.size should be (2)
    doc.indexer.wasDerivedFrom(q("wdf1")).activity.size should be (2)
    doc.indexer.wasDerivedFrom(q("wdf1")).generation.size should be (2)
    doc.indexer.wasDerivedFrom(q("wdf1")).usage.size should be (2)
  }
  
  "Generation wgb1" should "have a normal form" in {
    val doc=nf("src/test/resources/nf/wgb1.provn")
    doc.indexer.wasGeneratedBy.size should be (1)
    doc.indexer.wasGeneratedBy(q("wgb1")).entity.size should be (2)
    doc.indexer.wasGeneratedBy(q("wgb1")).activity.size should be (2)
    doc.indexer.wasGeneratedBy(q("wgb1")).time.size should be (2)
  }

  "Usage usd1" should "have a normal form" in {
    val doc=nf("src/test/resources/nf/usd1.provn")
    doc.indexer.used.size should be (1)
    doc.indexer.used(q("used1")).entity.size should be (2)
    doc.indexer.used(q("used1")).activity.size should be (2)
    doc.indexer.used(q("used1")).time.size should be (2)
  }
     
  "Invalidation wib1" should "have a normal form" in {
    val doc=nf("src/test/resources/nf/wib1.provn")
    doc.indexer.wasInvalidatedBy.size should be (1)
    doc.indexer.wasInvalidatedBy(q("wib1")).entity.size should be (2)
    doc.indexer.wasInvalidatedBy(q("wib1")).activity.size should be (2)
    doc.indexer.wasInvalidatedBy(q("wib1")).time.size should be (2)
  }

       
  "Start wsb1" should "have a normal form" in {
    val doc=nf("src/test/resources/nf/wsb1.provn")
    doc.indexer.wasStartedBy.size should be (1)
    doc.indexer.wasStartedBy(q("wsb1")).activity.size should be (2)
    doc.indexer.wasStartedBy(q("wsb1")).starter.size should be (2)
    doc.indexer.wasStartedBy(q("wsb1")).trigger.size should be (2)
    doc.indexer.wasStartedBy(q("wsb1")).time.size should be (2)
  }
  
  "Start web1" should "have a normal form" in {
    val doc=nf("src/test/resources/nf/web1.provn")
    doc.indexer.wasEndedBy.size should be (1)
    doc.indexer.wasEndedBy(q("web1")).activity.size should be (2)
    doc.indexer.wasEndedBy(q("web1")).ender.size should be (2)
    doc.indexer.wasEndedBy(q("web1")).trigger.size should be (2)
    doc.indexer.wasEndedBy(q("web1")).time.size should be (2)
  }


  "Specialization spe1" should "have a normal form" in {
    val doc=nf("src/test/resources/nf/spe1.provn")
    doc.indexer.specializationOf.size should be (1)
    doc.indexer.specializationOf(q("spe1")).specificEntity.size should be (2)
    doc.indexer.specializationOf(q("spe1")).generalEntity.size should be (2)
  }
  
  "Alternate alt1" should "have a normal form" in {
    val doc=nf("src/test/resources/nf/alt1.provn")
    doc.indexer.alternateOf.size should be (1)
    doc.indexer.alternateOf(q("alt1")).alternate1.size should be (2)
    doc.indexer.alternateOf(q("alt1")).alternate2.size should be (2)
  }
  
  "Membership mem1" should "have a normal form" in {
    val doc=nf("src/test/resources/nf/mem1.provn")
    doc.indexer.hadMember.size should be (1)
    doc.indexer.hadMember(q("mem1")).entity.size should be (2)
    doc.indexer.hadMember(q("mem1")).collection.size should be (2)
  }

  "Influence winflb1" should "have a normal form" in {
    val doc=nf("src/test/resources/nf/winflb1.provn")
    doc.indexer.wasInfluencedBy.size should be (1)
    doc.indexer.wasInfluencedBy(q("winflb1")).influencee.size should be (2)
    doc.indexer.wasInfluencedBy(q("winflb1")).influencer.size should be (2)
  }
  
  "Communication winfb1" should "have a normal form" in {
    val doc=nf("src/test/resources/nf/winfb1.provn")
    doc.indexer.wasInformedBy.size should be (1)
    doc.indexer.wasInformedBy(q("winfb1")).informant.size should be (2)
    doc.indexer.wasInformedBy(q("winfb1")).informed.size should be (2)
  }

  
  "Attribution wat1" should "have a normal form" in {
    val doc=nf("src/test/resources/nf/wat1.provn")
    doc.indexer.wasAttributedTo.size should be (1)
    doc.indexer.wasAttributedTo(q("wat1")).entity.size should be (2)
    doc.indexer.wasAttributedTo(q("wat1")).agent.size should be (2)
  }

  "Association waw1" should "have a normal form" in {
    val doc=nf("src/test/resources/nf/waw1.provn")
    doc.indexer.wasAssociatedWith.size should be (1)
    doc.indexer.wasAssociatedWith(q("waw1")).activity.size should be (2)
    doc.indexer.wasAssociatedWith(q("waw1")).agent.size should be (2)
    doc.indexer.wasAssociatedWith(q("waw1")).plan.size should be (2)
  }

  "Delegation aob1" should "have a normal form" in {
    val doc=nf("src/test/resources/nf/aob1.provn")
    doc.indexer.actedOnBehalfOf.size should be (1)
    doc.indexer.actedOnBehalfOf(q("aob1")).delegate.size should be (2)
    doc.indexer.actedOnBehalfOf(q("aob1")).responsible.size should be (2)
    doc.indexer.actedOnBehalfOf(q("aob1")).activity.size should be (2)
  }

  
  def nf(f: String) = {
    val in:Input=new FileInput(new File(f))
	  val doc=CommandLine.parseDocumentToNormalForm(in)
	  doc
  }
  def readDoc(f: String) = {
    val in:Input=new FileInput(new File(f))
    val doc=new ProvNInputer().input(in,Map())
    doc
  }

    
  "Entity ent1" should "have a normal form in XML" in {
    val doc=nf("src/test/resources/nf/ent1.provn")
    val d=doc.toDocument()
    val x=XmlBean.toXML(d)
    println(x)
        x
  }
  
      
  "Activity act1" should "have a normal form in XML" in {
    val doc=nf("src/test/resources/nf/act1.provn")
    val d=doc.toDocument()
    val x=XmlBean.toXML(d)
    println(x)
        x
  }
  
  
      
  "Agent ag1" should "have a normal form in XML" in {
    val doc=nf("src/test/resources/nf/ag1.provn")
    val d=doc.toDocument()
    val x=XmlBean.toXML(d)
    println(x)
        x
  }
  
  
  "Association waw1" should "have a normal form in XML" in {
    val doc=nf("src/test/resources/nf/waw1.provn")
    val d=doc.toDocument()
    val x=XmlBean.toXML(d)
    println(x)
        x
  }
  
    
  "Attribution wat1" should "have a normal form in XML" in {
    val doc=nf("src/test/resources/nf/wat1.provn")
    val d=doc.toDocument()
    val x=XmlBean.toXML(d)
    println(x)
        x    
  }

  
  "Delegation aobo1" should "have a normal form in XML" in {
    val doc=nf("src/test/resources/nf/aob1.provn")
    val d=doc.toDocument()
    val x=XmlBean.toXML(d)
    println(x)
        x    
  }

   "Derivation wdf1" should "have a normal form in XML" in {
    val doc=nf("src/test/resources/nf/wdf1.provn")
    val d=doc.toDocument()
    val x=XmlBean.toXML(d)
    println(x)
    x    
  }
    
  "Generation wgb1" should "have a normal form in XML" in {
    val doc=nf("src/test/resources/nf/wgb1.provn")
    val d=doc.toDocument()
    val x=XmlBean.toXML(d)
    println(x)
    x    
  }

  "Usage usd1" should "have a normal form in XML" in {
    val doc=nf("src/test/resources/nf/usd1.provn")
    val d=doc.toDocument()
    val x=XmlBean.toXML(d)
    println(x)
    x    
  }
  
  
  "Invalidation wib1" should "have a normal form in XML" in {
    val doc=nf("src/test/resources/nf/wib1.provn")
    val d=doc.toDocument()
    val x=XmlBean.toXML(d)
    println(x)
    x    
  }

  
  "Start wsb1" should "have a normal form in XML" in {
    val doc=nf("src/test/resources/nf/wsb1.provn")
    val d=doc.toDocument()
    val x=XmlBean.toXML(d)
    println(x)
    x    
  }

    
  "End web1" should "have a normal form in XML" in {
    val doc=nf("src/test/resources/nf/web1.provn")
    val d=doc.toDocument()
    val x=XmlBean.toXML(d)
    println(x)
    x    
  }

    
  "Influence winflb1" should "have a normal form in XML" in {
    val doc=nf("src/test/resources/nf/winflb1.provn")
    val d=doc.toDocument()
    val x=XmlBean.toXML(d)
    println(x)
    x    
  }

      
  "Communication winfb1" should "have a normal form in XML" in {
    val doc=nf("src/test/resources/nf/winfb1.provn")
    val d=doc.toDocument()
    val x=XmlBean.toXML(d)
    println(x)
    x    
  }
  
        
  "Specialization spe1" should "have a normal form in XML" in {
    val doc=nf("src/test/resources/nf/spe1.provn")
    val d=doc.toDocument()
    val x=XmlBean.toXML(d)
    println(x)
    x    
  }
  
        
  "Alternate alt1" should "have a normal form in XML" in {
    val doc=nf("src/test/resources/nf/alt1.provn")
    val d=doc.toDocument()
    val x=XmlBean.toXML(d)
    println(x)
    x    
  }
  
        
  "Membership mem1" should "have a normal form in XML" in {
    val doc=nf("src/test/resources/nf/mem1.provn")
    val d=doc.toDocument()
    val x=XmlBean.toXML(d)
    println(x)
    x    
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


}
