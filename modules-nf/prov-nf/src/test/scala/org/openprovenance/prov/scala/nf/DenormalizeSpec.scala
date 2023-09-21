
package org.openprovenance.prov.scala.nf

import org.openprovenance.prov.model.Namespace
import org.openprovenance.prov.scala.immutable.{Document, MyActions, MyActions2, MyParser, QualifiedName}
import org.openprovenance.prov.scala.interop.{FileInput, Input}
import org.openprovenance.prov.scala.streaming.{DocBuilder, DocBuilderFunctions}
import org.parboiled2.ParseError
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.io.File
import scala.util.{Failure, Success}

class DenormalizeSpec extends AnyFlatSpec with Matchers {
	val EX_NS="http://example/"

	def q(local: String): QualifiedName = {
   new QualifiedName("ex",local,EX_NS)
  }
	

	"Entity ent1" should "round trip" in {
    val doc=nf("src/test/resources/nf/ent1.provn")
    val doc2=roundTrip(doc)
    println(doc2.toDocument)
    doc2 should be (doc)
  }
		
	"Activity act1" should "round trip" in {
    val doc=nf("src/test/resources/nf/act1.provn")
    val doc2=roundTrip(doc)
    doc2 should be (doc)
  }	
	
	"Agent ag1" should "round trip" in {
    val doc=nf("src/test/resources/nf/ag1.provn")
    val doc2=roundTrip(doc)
    doc2 should be (doc)
  }

  "Derivation wdf1" should "round trip" in {
    val doc=nf("src/test/resources/nf/wdf1.provn")
    val doc2=roundTrip(doc)
    doc2 should be (doc)
  }
  
  "Generation wgb1" should "round trip" in {
    val doc=nf("src/test/resources/nf/wgb1.provn")
    val doc2=roundTrip(doc)
    doc2 should be (doc)
  }
  
  "Usage usd1" should "round trip" in {
    val doc=nf("src/test/resources/nf/usd1.provn")
    val doc2=roundTrip(doc)
    doc2 should be (doc)
  }
    
  "Invalidation wib1" should "round trip" in {
    val doc=nf("src/test/resources/nf/wib1.provn")
    val doc2=roundTrip(doc)
    doc2 should be (doc)
  }
      
  "Start wsb1" should "round trip" in {
    val doc=nf("src/test/resources/nf/wsb1.provn")
    val doc2=roundTrip(doc)
    doc2 should be (doc)
  }
      
  "End web1" should "round trip" in {
    val doc=nf("src/test/resources/nf/web1.provn")
    val doc2=roundTrip(doc)
    doc2 should be (doc)
  }
  
  "Specialization spe1" should "round trip" in {
    val doc=nf("src/test/resources/nf/spe1.provn")
    val doc2=roundTrip(doc)
    doc2 should be (doc)
  }
   
  "Alternate alt11" should "round trip" in {
    val doc=nf("src/test/resources/nf/alt1.provn")
    val doc2=roundTrip(doc)
    doc2 should be (doc)
  }   
   
  "Membership mem1" should "round trip" in {
    val doc=nf("src/test/resources/nf/mem1.provn")
    val doc2=roundTrip(doc)
    doc2 should be (doc)
  }   
  
  "Influence winflb1" should "round trip" in {
    val doc=nf("src/test/resources/nf/winflb1.provn")
    val doc2=roundTrip(doc)
    doc2 should be (doc)
  }   
   
  "Communication winflb1" should "round trip" in {
    val doc=nf("src/test/resources/nf/winfb1.provn")
    val doc2=roundTrip(doc)
    doc2 should be (doc)
  }   
   
      
  "Attribution wat1" should "round trip" in {
    val doc=nf("src/test/resources/nf/wat1.provn")
    val doc2=roundTrip(doc)
    doc2 should be (doc)
  }   
     
   "Association waw1" should "round trip" in {
    val doc=nf("src/test/resources/nf/waw1.provn")
    val doc2=roundTrip(doc)
    doc2 should be (doc)
  }   
   
  "Delegation aob1" should "round trip" in {
    val doc=nf("src/test/resources/nf/aob1.provn")
    val doc2=roundTrip(doc)
    doc2 should be (doc)
  }   
  
  
  
  def nf(f: String): DocumentProxy = {
    val in:Input=new FileInput(new File(f))
	  val doc=CommandLine.parseDocumentToNormalForm(in)
	  doc
  }
  
  def roundTrip (d: org.openprovenance.prov.scala.nf.DocumentProxy): DocumentProxy = {
    d.toDocument.toNormalForm(d).asInstanceOf[DocumentProxy]
  }

  def parse(f: String): Document = {
    val in:Input=new FileInput(new File(f))
	  val doc=CommandLine.parseDocument(in)
	  doc
  }

  
  
  
  

}


class RoundTripFromStatement extends AnyFlatSpec with Matchers {
	val EX_NS="http://example.org/"
  val EX_PRE="ex"
 
	def doCheckStatement (s: String, ns: Namespace): Boolean = {

    val actions=new MyActions()
    val actions2=new MyActions2()
    val funs=new DocBuilderFunctions()

    val docBuilder: DocBuilder =new DocBuilder(funs)
    actions2.bun_ns=None
    actions2.next=docBuilder
    actions2.docns=ns

    val p=new MyParser(s,actions2,actions)

      p.statement.run() match {
        case Success(result) =>
          val d1=new org.openprovenance.prov.scala.immutable.Document(Set(result),ns)
          val nf1=d1.toNormalForm(new DocumentProxy(new Namespace())).asInstanceOf[DocumentProxy]
          nf1.toDocument.statementOrBundle.head == result
        case Failure(e: ParseError) => println("Error " + p.formatError(e)); false
        case Failure(e) =>println("Error " + e); false
      }
  }
	
	
  

  "A statement" should "roundtrip" in {
    val ns=new Namespace
    ns.addKnownNamespaces()
    ns.register("ex", EX_NS)

    doCheckStatement("entity(ex:e1)", ns) should be (true)
    doCheckStatement("entity(ex:e1, [prov:value=\"a\"])", ns) should be (true)
    
    doCheckStatement("agent(ex:eg1)", ns) should be (true)
    doCheckStatement("agent(ex:eg1, [prov:value=\"a\"])", ns) should be (true)
    
    doCheckStatement("activity(ex:a1,2012-03-31T09:21:00, 2012-03-31T09:22:00)", ns) should be (true)
    doCheckStatement("activity(ex:a1,-, 2012-03-31T09:22:00)", ns) should be (true)
    doCheckStatement("activity(ex:a1,2012-03-31T09:21:00, -)", ns) should be (true)
    doCheckStatement("activity(ex:a1)", ns) should be (true) 
    
    
    doCheckStatement("wasDerivedFrom(ex:wdf1;ex:e2, ex:e1)", ns) should be (true) 
    doCheckStatement("wasDerivedFrom(ex:wdf1;ex:e2, ex:e1, ex:a, -, -)", ns) should be (true) 
    doCheckStatement("wasDerivedFrom(ex:wdf1;ex:e2, ex:e1, ex:a, ex:g1, -)", ns) should be (true) 
    doCheckStatement("wasDerivedFrom(ex:wdf1;ex:e2, ex:e1, ex:a, ex:g1, ex:u1)", ns) should be (true) 
    doCheckStatement("wasDerivedFrom(ex:wdf1;ex:e2, ex:e1, ex:a, -, ex:u1)", ns) should be (true) 
    doCheckStatement("wasDerivedFrom(ex:wdf1;ex:e2, ex:e1, -, -, ex:u1)", ns) should be (true) 
    doCheckStatement("wasDerivedFrom(ex:wdf1;ex:e2, ex:e1, -, ex:g1, -)", ns) should be (true) 
    
    
    doCheckStatement("wasDerivedFrom(ex:e2, ex:e1)", ns) should be (true) 
    doCheckStatement("wasDerivedFrom(ex:e2, ex:e1, ex:a, -, -)", ns) should be (true) 
    doCheckStatement("wasDerivedFrom(ex:e2, ex:e1, ex:a, ex:g1, -)", ns) should be (true) 
    doCheckStatement("wasDerivedFrom(ex:e2, ex:e1, ex:a, ex:g1, ex:u1)", ns) should be (true) 
    doCheckStatement("wasDerivedFrom(ex:e2, ex:e1, ex:a, -, ex:u1)", ns) should be (true) 
    doCheckStatement("wasDerivedFrom(ex:e2, ex:e1, -, -, ex:u1)", ns) should be (true) 
    doCheckStatement("wasDerivedFrom(ex:e2, ex:e1, -, ex:g1, -)", ns) should be (true) 
    
   
    doCheckStatement("wasGeneratedBy(ex:wgb1;ex:e1, ex:a1, - )", ns) should be (true) 
    doCheckStatement("wasGeneratedBy(ex:wgb1;ex:e1, -, - )", ns) should be (true) 
    doCheckStatement("wasGeneratedBy(ex:e1, ex:a1, - )", ns) should be (true) 
    doCheckStatement("wasGeneratedBy(ex:e1, -, - )", ns) should be (true) 
    
    
    
    doCheckStatement("used(ex:usd1;ex:e1, ex:a1, - )", ns) should be (true) 
    doCheckStatement("used(ex:usd1;ex:e1, -, - )", ns) should be (true) 
    doCheckStatement("used(ex:e1, ex:a1, - )", ns) should be (true) 
    doCheckStatement("used(ex:e1, -, - )", ns) should be (true) 
    
    
    doCheckStatement("wasInvalidatedBy(ex:wib1;ex:e1, ex:a1, - )", ns) should be (true) 
    doCheckStatement("wasInvalidatedBy(ex:wib1;ex:e1, -, - )", ns) should be (true) 
    doCheckStatement("wasInvalidatedBy(ex:e1, ex:a1, - )", ns) should be (true) 
    doCheckStatement("wasInvalidatedBy(ex:e1, -, - )", ns) should be (true) 

    doCheckStatement("wasAttributedTo(ex:wat1; ex:e1, ex:ag1)", ns) should be (true) 

    doCheckStatement("wasAssociatedWith(ex:waw1; ex:a1, ex:ag1, -)", ns) should be (true) 
    doCheckStatement("wasAssociatedWith(ex:waw1; ex:a1, ex:ag1, ex:plan)", ns) should be (true) 
    doCheckStatement("wasAssociatedWith(ex:a1, ex:ag1, -)", ns) should be (true) 
    doCheckStatement("wasAssociatedWith(ex:a1, ex:ag1, ex:plan)", ns) should be (true) 
    
    doCheckStatement("actedOnBehalfOf(ex:aob1; ex:a1, ex:ag1, -)", ns) should be (true) 
    doCheckStatement("actedOnBehalfOf(ex:aob1; ex:a1, ex:ag1, ex:plan)", ns) should be (true) 
    doCheckStatement("actedOnBehalfOf(ex:a1, ex:ag1, -)", ns) should be (true) 
    doCheckStatement("actedOnBehalfOf(ex:a1, ex:ag1, ex:plan)", ns) should be (true) 

    doCheckStatement("wasStartedBy(ex:wsb1;ex:a1, ex:e1, ex:a2, - )", ns) should be (true) 
    doCheckStatement("wasStartedBy(ex:wsb1;ex:a1, ex:e1, -, - )", ns) should be (true) 
    doCheckStatement("wasStartedBy(ex:wsb1;ex:a1, -, ex:a2, - )", ns) should be (true) 
    doCheckStatement("wasStartedBy(ex:wsb1;ex:a1, ex:e1, ex:a2, 2012-03-31T09:21:00)", ns) should be (true) 
    doCheckStatement("wasStartedBy(ex:wsb1;ex:a1, -, ex:a2, 2012-03-31T09:21:00)", ns) should be (true) 
    doCheckStatement("wasStartedBy(ex:wsb1;ex:a1, ex:e1, -, 2012-03-31T09:21:00)", ns) should be (true) 
    doCheckStatement("wasStartedBy(ex:a1, ex:e1, ex:a2, - )", ns) should be (true) 
    doCheckStatement("wasStartedBy(ex:a1, ex:e1, -, - )", ns) should be (true) 
    doCheckStatement("wasStartedBy(ex:a1, -, ex:a2, - )", ns) should be (true) 
    doCheckStatement("wasStartedBy(ex:a1, ex:e1, ex:a2, 2012-03-31T09:21:00)", ns) should be (true) 
    doCheckStatement("wasStartedBy(ex:a1, -, ex:a2, 2012-03-31T09:21:00)", ns) should be (true) 
    doCheckStatement("wasStartedBy(ex:a1, ex:e1, -, 2012-03-31T09:21:00)", ns) should be (true) 

    
    doCheckStatement("wasEndedBy(ex:web1;ex:a1, ex:e1, ex:a2, - )", ns) should be (true) 
    doCheckStatement("wasEndedBy(ex:web1;ex:a1, ex:e1, -, - )", ns) should be (true) 
    doCheckStatement("wasEndedBy(ex:web1;ex:a1, -, ex:a2, - )", ns) should be (true) 
    doCheckStatement("wasEndedBy(ex:web1;ex:a1, ex:e1, ex:a2, 2012-03-31T09:21:00)", ns) should be (true) 
    doCheckStatement("wasEndedBy(ex:web1;ex:a1, -, ex:a2, 2012-03-31T09:21:00)", ns) should be (true) 
    doCheckStatement("wasEndedBy(ex:web1;ex:a1, ex:e1, -, 2012-03-31T09:21:00)", ns) should be (true) 
    doCheckStatement("wasEndedBy(ex:a1, ex:e1, ex:a2, - )", ns) should be (true) 
    doCheckStatement("wasEndedBy(ex:a1, ex:e1, -, - )", ns) should be (true) 
    doCheckStatement("wasEndedBy(ex:a1, -, ex:a2, - )", ns) should be (true) 
    doCheckStatement("wasEndedBy(ex:a1, ex:e1, ex:a2, 2012-03-31T09:21:00)", ns) should be (true) 
    doCheckStatement("wasEndedBy(ex:a1, -, ex:a2, 2012-03-31T09:21:00)", ns) should be (true) 
    doCheckStatement("wasEndedBy(ex:a1, ex:e1, -, 2012-03-31T09:21:00)", ns) should be (true) 
    
    doCheckStatement("specializationOf(ex:a1, ex:e1)", ns) should be (true) 
    doCheckStatement("provext:specializationOf(ex:a1, ex:e1)", ns) should be (true) 
    doCheckStatement("provext:specializationOf(ex:spe1; ex:a1, ex:e1)", ns) should be (true) 
        
    
    doCheckStatement("alternateOf(ex:a1, ex:e1)", ns) should be (true) 
    doCheckStatement("provext:alternateOf(ex:a1, ex:e1)", ns) should be (true) 
    doCheckStatement("provext:alternateOf(ex:spe1; ex:a1, ex:e1)", ns) should be (true) 
    
    
    doCheckStatement("hadMember(ex:a1, ex:e1)", ns) should be (true) 
    doCheckStatement("provext:hadMember(ex:a1, ex:e1)", ns) should be (true) 
    doCheckStatement("provext:hadMember(ex:spe1; ex:a1, ex:e1)", ns) should be (true) 
        
    doCheckStatement("wasInfluencedBy(ex:winfl1; ex:a1, ex:e1)", ns) should be (true) 
    doCheckStatement("wasInfluencedBy(ex:a1, ex:e1)", ns) should be (true) 

    
    doCheckStatement("wasInformedBy(ex:winfob; ex:a2, ex:ea11)", ns) should be (true) 
    doCheckStatement("wasInformedBy(ex:a2, ex:ea11)", ns) should be (true) 


}

}