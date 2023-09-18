
package org.openprovenance.prov.scala

import java.io.{BufferedWriter, File, FileWriter}
import org.openprovenance.prov.model.Namespace
import org.openprovenance.prov.scala.immutable._
import org.openprovenance.prov.scala.interop.FileOutput
import org.openprovenance.prov.scala.streaming.{DocBuilder, DocBuilderFunctions}
import org.openprovenance.prov.scala.summary.TypePropagator.SUM_NS
import org.openprovenance.prov.scala.summary._
import org.openprovenance.prov.scala.summary.types._
import org.openprovenance.prov.scala.viz.{Graphics, SVGOutputer}
import org.parboiled2.ParseError
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers.be
import org.scalatest.matchers.should.Matchers

import scala.util.{Failure, Success}

class SummarySpec extends AnyFlatSpec with Matchers {
  val EX_NS="http://example.org/"
  val ipf=new org.openprovenance.prov.scala.immutable.ProvFactory
  val xsd_string: QualifiedName =QualifiedName(ipf.xsd_string)
  val prov_qualified_name: QualifiedName =QualifiedName(ipf.prov_qualified_name)
  
  val nsBase="http://example.org/summary#"

  def q(local: String): QualifiedName = {
   new QualifiedName("ex",local,EX_NS)
  }
  
  def doCheckDocument (s: String, doc: Document): Boolean = {
      val p=new MyParser(s,null)
      p.document.run() match {
        case Success(result) => p.getNext().asInstanceOf[DocBuilder].document==doc
        case Failure(e: ParseError) => false
        case Failure(e) =>false
      }
  }

  def doCheckEntity (s: String, ns: Namespace, ent: Entity): Boolean = {
    val actions=new MyActions()
    val actions2=new MyActions2()
    val funs=new DocBuilderFunctions()


    val db=new DocBuilder(funs)
    actions2.docns=ns
    actions2.bun_ns=None
    actions2.next=db
    val p=new MyParser(s, actions2, actions)
    p.entity.run() match {
      case Success(result) => Namespace.withThreadNamespace(ns); println("Success " + result); result==ent
      case Failure(e: ParseError) => println("Error " + p.formatError(e)); false
      case Failure(e) =>println("Error " + e); false
    }
  }
  
  def sum(d: String,triangle:Boolean=false, level0:Level0=new DefaultLevel0, always_with_type_0:Boolean=false): (Indexer, TypePropagator) = {
    val actions=new MyActions()
    val actions2=new MyActions2()
    val funs=new DocBuilderFunctions()


    val db=new DocBuilder(funs)
    val ns=new Namespace
    ns.addKnownNamespaces()
    actions2.docns=ns
    actions2.bun_ns=None
    actions2.next=db


    val p=new MyParser(d,actions2,actions)
    val doc: Document =p.document.run() match {
          case Success(result) => db.document
          case _ => ???
    }
    val ind=new Indexer(doc)
    val s=new TypePropagator(doc,triangle, always_with_type_0, false, true,level0)
    (ind,s)
  }

  

  "A document with generation" should "have correct provenance types" in {
    val ns=new Namespace
    ns.addKnownNamespaces()
    ns.register("ex", EX_NS)
    
    val (ind,s)=sum("""
                    document
                    prefix ex <http://example.org/>
                    entity(ex:e)
                    activity(ex:a)
                    wasGeneratedBy(ex:e,ex:a,-)
                    endDocument
                    """)
                    
      
    s.type0(ind.amap(q("e"))) should be (Set(Ent()))
    s.type0(ind.amap(q("a"))) should be (Set(Act()))
    s.type1(ind.amap(q("e"))) should be (Set(Wgb(Set(Act()))))
    s.type1.get(ind.amap(q("a"))) should be (None)     
    s.type_back1.get(ind.amap(q("e"))) should be (None)     
    s.type_back1(ind.amap(q("a"))) should be (Set(Wgb_1(Set(Ent())))) 
    s.type0.size should be (2)
    s.type1.size should be (1)
    s.type_back1.size should be (1)
    
  }
  
  
  
  "A document with invalidation" should "have correct provenance types" in {
    val ns=new Namespace
    ns.addKnownNamespaces()
    ns.register("ex", EX_NS)
    
    val (ind,s)=sum("""
                    document
                    prefix ex <http://example.org/>
                    entity(ex:e)
                    activity(ex:a)
                    wasInvalidatedBy(ex:e,ex:a,-)
                    endDocument
                    """)
                    
      
    s.type0(ind.amap(q("e"))) should be (Set(Ent()))
    s.type0(ind.amap(q("a"))) should be (Set(Act()))
    s.type1(ind.amap(q("e"))) should be (Set(Winvb(Set(Act()))))
    s.type1.get(ind.amap(q("a"))) should be (None)       
    s.type_back1.get(ind.amap(q("e"))) should be (None)     
    s.type_back1(ind.amap(q("a"))) should be (Set(Winvb_1(Set(Ent())))) 
    s.type0.size should be (2)
    s.type1.size should be (1)
    s.type_back1.size should be (1)
    
  }
  

    

  "A document with usage" should "have correct provenance types" in {
    val ns=new Namespace
    ns.addKnownNamespaces()
    ns.register("ex", EX_NS)
    
    val (ind,s)=sum("""
                    document
                    prefix ex <http://example.org/>
                    entity(ex:e)
                    activity(ex:a)
                    used(ex:a,ex:e,-)
                    endDocument
                    """)
                    
      
    s.type0(ind.amap(q("e"))) should be (Set(Ent()))
    s.type0(ind.amap(q("a"))) should be (Set(Act()))
    s.type1(ind.amap(q("a"))) should be (Set(Usd(Set(Ent()))))
    s.type1.get(ind.amap(q("e"))) should be (None)                            
    s.type_back1.get(ind.amap(q("a"))) should be (None)     
    s.type_back1(ind.amap(q("e"))) should be (Set(Usd_1(Set(Act())))) 
    s.type0.size should be (2)
    s.type1.size should be (1)
    s.type_back1.size should be (1)
    
  }
  
  
  
  "A document with derivation" should "have correct provenance types" in {
    val ns=new Namespace
    ns.addKnownNamespaces()
    ns.register("ex", EX_NS)
    
    val (ind,s)=sum("""
                    document
                    prefix ex <http://example.org/>
                    entity(ex:e2)
                    entity(ex:e1)
                    wasDerivedFrom(ex:e2,ex:e1)
                    endDocument
                    """)
                    
      
    s.type0(ind.amap(q("e1"))) should be (Set(Ent()))
    s.type0(ind.amap(q("e2"))) should be (Set(Ent()))
    s.type1(ind.amap(q("e2"))) should be (Set(Wdf(Set(Ent()))))
    s.type1.get(ind.amap(q("e1"))) should be (None)           
    s.type_back1.get(ind.amap(q("e2"))) should be (None)     
    s.type_back1(ind.amap(q("e1"))) should be (Set(Wdf_1(Set(Ent())))) 
    s.type0.size should be (2)
    s.type1.size should be (1)
    s.type_back1.size should be (1)

    
  }
  
   
  
  "A document with derivation with an activity" should "have correct provenance types" in {
    val ns=new Namespace
    ns.addKnownNamespaces()
    ns.register("ex", EX_NS)
    
    val (ind,s)=sum("""
                    document
                    prefix ex <http://example.org/>
                    entity(ex:e2)
                    entity(ex:e1)
                    activity(ex:a)
                    wasDerivedFrom(ex:e2,ex:e1,ex:a,-,-)
                    endDocument
                    """)
                    
      
    s.type0(ind.amap(q("e1"))) should be (Set(Ent()))
    s.type0(ind.amap(q("e2"))) should be (Set(Ent()))
    s.type0(ind.amap(q("a"))) should be (Set(Act()))
    s.type1(ind.amap(q("e2"))) should be (Set(Wdf(Set(Ent()),Set(Act()))))
    s.type1.get(ind.amap(q("e1"))) should be (None)                            
    s.type1.get(ind.amap(q("a"))) should be (None)                            
    s.type_back1.get(ind.amap(q("e2"))) should be (None)     
    s.type_back1(ind.amap(q("e1"))) should be (Set(Wdf_1(Set(Ent()),Set(Act())))) 
    s.type_back1.get(ind.amap(q("a"))) should be (None)     
    s.type0.size should be (3)
    s.type1.size should be (1)
    s.type_back1.size should be (1)
    
  }
 
    
  "A document with attribution" should "have correct provenance types" in {
    val ns=new Namespace
    ns.addKnownNamespaces()
    ns.register("ex", EX_NS)
    
    val (ind,s)=sum("""
                    document
                    prefix ex <http://example.org/>
                    entity(ex:e)
                    agent(ex:ag)
                    wasAttributedTo(ex:e,ex:ag)
                    endDocument
                    """)
                    
      
    s.type0(ind.amap(q("e"))) should be (Set(Ent()))
    s.type0(ind.amap(q("ag"))) should be (Set(Ag()))
    s.type1(ind.amap(q("e"))) should be (Set(Wat(Set(Ag()))))
    s.type1.get(ind.amap(q("ag"))) should be (None)           
    s.type_back1.get(ind.amap(q("e"))) should be (None)     
    s.type_back1(ind.amap(q("ag"))) should be (Set(Wat_1(Set(Ent())))) 
    s.type0.size should be (2)
    s.type1.size should be (1)
    s.type_back1.size should be (1)

    
  }
  
  "A document with association" should "have correct provenance types" in {
    val ns=new Namespace
    ns.addKnownNamespaces()
    ns.register("ex", EX_NS)
    
    val (ind,s)=sum("""
                    document
                    prefix ex <http://example.org/>
                    activity(ex:a)
                    agent(ex:ag)
                    wasAssociatedWith(ex:a,ex:ag,-)
                    endDocument
                    """)
                    
      
    s.type0(ind.amap(q("a"))) should be (Set(Act()))
    s.type0(ind.amap(q("ag"))) should be (Set(Ag()))
    s.type1(ind.amap(q("a"))) should be (Set(Waw(Set(Ag()), Set()))) //TO CHECK: waw
    s.type1.get(ind.amap(q("ag"))) should be (None)           
    s.type_back1.get(ind.amap(q("a"))) should be (None)     
    s.type_back1(ind.amap(q("ag"))) should be (Set(Waw_1(Set(Act()), Set()))) //TO CHECK: waw
    s.type0.size should be (2)
    s.type1.size should be (1)
    s.type_back1.size should be (1)

    
  }
  
  "A document with association and plan" should "have correct provenance types" in {
    val ns=new Namespace
    ns.addKnownNamespaces()
    ns.register("ex", EX_NS)
    
    val (ind,s)=sum("""
                    document
                    prefix ex <http://example.org/>
                    activity(ex:a)
                    agent(ex:ag)
                    entity(ex:pl)
                    wasAssociatedWith(ex:a,ex:ag,ex:pl)
                    endDocument
                    """)
                    
      
    s.type0(ind.amap(q("a"))) should be (Set(Act()))
    s.type0(ind.amap(q("ag"))) should be (Set(Ag()))
    s.type0(ind.amap(q("pl"))) should be (Set(Ent()))
    s.type1(ind.amap(q("a"))) should be (Set(Waw(Set(Ag()), Set(Ent()))))
    s.type1.get(ind.amap(q("ag"))) should be (None)           
    s.type1.get(ind.amap(q("pl"))) should be (None)           
    s.type_back1.get(ind.amap(q("a"))) should be (None)     
    s.type_back1(ind.amap(q("ag"))) should be (Set(Waw_1(Set(Act()), Set(Ent())))) //TO CHECK: waw
    s.type_back1.get(ind.amap(q("pl"))) should be (None)     
    s.type0.size should be (3)
    s.type1.size should be (1)
    s.type_back1.size should be (1)

    
  }
  
   
  "A document with delegation" should "have correct provenance types" in {
    val ns=new Namespace
    ns.addKnownNamespaces()
    ns.register("ex", EX_NS)
    
    val (ind,s)=sum("""
                    document
                    prefix ex <http://example.org/>
                    agent(ex:ag1)
                    agent(ex:ag2)
                    actedOnBehalfOf(ex:ag2,ex:ag1,-)
                    endDocument
                    """)
                    
      
    s.type0(ind.amap(q("ag1"))) should be (Set(Ag()))
    s.type0(ind.amap(q("ag2"))) should be (Set(Ag()))
    s.type1(ind.amap(q("ag2"))) should be (Set(Aobo(Set(Ag()))))
    s.type1.get(ind.amap(q("ag1"))) should be (None)           
    s.type_back1.get(ind.amap(q("ag2"))) should be (None)     
    s.type_back1(ind.amap(q("ag1"))) should be (Set(Aobo_1(Set(Ag())))) 
    s.type0.size should be (2)
    s.type1.size should be (1)
    s.type_back1.size should be (1)

    
  }
  
  
   
  "A document with delegation with activity" should "have correct provenance types" in {
    val ns=new Namespace
    ns.addKnownNamespaces()
    ns.register("ex", EX_NS)
    
    val (ind,s)=sum("""
                    document
                    prefix ex <http://example.org/>
                    agent(ex:ag1)
                    agent(ex:ag2)
                    activity(ex:a)
                    actedOnBehalfOf(ex:ag2,ex:ag1,ex:a)
                    endDocument
                    """)
                    
      
    s.type0(ind.amap(q("ag1"))) should be (Set(Ag()))
    s.type0(ind.amap(q("ag2"))) should be (Set(Ag()))
    s.type0(ind.amap(q("a"))) should be (Set(Act()))
    s.type1(ind.amap(q("ag2"))) should be (Set(Aobo(Set(Ag()),Set(Act()))))
    s.type1.get(ind.amap(q("ag1"))) should be (None)           
    s.type1.get(ind.amap(q("a"))) should be (None)           
    s.type_back1.get(ind.amap(q("ag2"))) should be (None)     
    s.type_back1(ind.amap(q("ag1"))) should be (Set(Aobo_1(Set(Ag()),Set(Act())))) 
    s.type_back1.get(ind.amap(q("a"))) should be (None)     
    s.type0.size should be (3)
    s.type1.size should be (1)
    s.type_back1.size should be (1)

    
  }
  
  
    
  "A document with specialization" should "have correct provenance types" in {
    val ns=new Namespace
    ns.addKnownNamespaces()
    ns.register("ex", EX_NS)
    
    val (ind,s)=sum("""
                    document
                    prefix ex <http://example.org/>
                    entity(ex:e2)
                    entity(ex:e1)
                    specializationOf(ex:e2,ex:e1)
                    endDocument
                    """)
                    
      
    s.type0(ind.amap(q("e2"))) should be (Set(Ent()))
    s.type0(ind.amap(q("e1"))) should be (Set(Ent()))
    s.type1(ind.amap(q("e2"))) should be (Set(Spec(Set(Ent()))))
    s.type1.get(ind.amap(q("e1"))) should be (None)           
    s.type_back1.get(ind.amap(q("e2"))) should be (None)     
    s.type_back1(ind.amap(q("e1"))) should be (Set(Spec_1(Set(Ent())))) 
    s.type0.size should be (2)
    s.type1.size should be (1)
    s.type_back1.size should be (1)

    
  }

    
  "A document with alternate" should "have correct provenance types" in {
    val ns=new Namespace
    ns.addKnownNamespaces()
    ns.register("ex", EX_NS)
    
    val (ind,s)=sum("""
                    document
                    prefix ex <http://example.org/>
                    entity(ex:e2)
                    entity(ex:e1)
                    alternateOf(ex:e2,ex:e1)
                    endDocument
                    """)
                    
      
    s.type0(ind.amap(q("e2"))) should be (Set(Ent()))
    s.type0(ind.amap(q("e1"))) should be (Set(Ent()))
    s.type1(ind.amap(q("e2"))) should be (Set(Alt(Set(Ent()))))
    s.type1.get(ind.amap(q("e1"))) should be (None)           
    s.type_back1.get(ind.amap(q("e2"))) should be (None)     
    s.type_back1(ind.amap(q("e1"))) should be (Set(Alt_1(Set(Ent())))) 
    s.type0.size should be (2)
    s.type1.size should be (1)
    s.type_back1.size should be (1)
    
  }

  
  
    
  "A document with influence" should "have correct provenance types" in {
    val ns=new Namespace
    ns.addKnownNamespaces()
    ns.register("ex", EX_NS)
    
    val (ind,s)=sum("""
                    document
                    prefix ex <http://example.org/>
                    entity(ex:e2)
                    entity(ex:e1)
                    wasInfluencedBy(ex:e2,ex:e1)  //Note, test is about entities here
                    endDocument
                    """)
                    
      
    s.type0(ind.amap(q("e2"))) should be (Set(Ent()))
    s.type0(ind.amap(q("e1"))) should be (Set(Ent()))
    s.type1(ind.amap(q("e2"))) should be (Set(Winflb(Set(Ent()))))
    s.type1.get(ind.amap(q("e1"))) should be (None)           
    s.type_back1.get(ind.amap(q("e2"))) should be (None)     
    s.type_back1(ind.amap(q("e1"))) should be (Set(Winflb_1(Set(Ent())))) 
    s.type0.size should be (2)
    s.type1.size should be (1)
    s.type_back1.size should be (1)

    
  }

  
  "A document with Communication" should "have correct provenance types" in {
    val ns=new Namespace
    ns.addKnownNamespaces()
    ns.register("ex", EX_NS)
    
    val (ind,s)=sum("""
                    document
                    prefix ex <http://example.org/>
                    activity(ex:a2)
                    activity(ex:a1)
                    wasInformedBy(ex:a2,ex:a1)  //Note, test is about entities here
                    endDocument
                    """)
                    
      
    s.type0(ind.amap(q("a2"))) should be (Set(Act()))
    s.type0(ind.amap(q("a1"))) should be (Set(Act()))
    s.type1(ind.amap(q("a2"))) should be (Set(Winfob(Set(Act()))))
    s.type1.get(ind.amap(q("a1"))) should be (None)           
    s.type_back1.get(ind.amap(q("a2"))) should be (None)     
    s.type_back1(ind.amap(q("a1"))) should be (Set(Winfob_1(Set(Act())))) 
    s.type0.size should be (2)
    s.type1.size should be (1)
    s.type_back1.size should be (1)

    
  }
  
  
  "A document with generation and derivation" should "have correct provenance types" in {
    val ns=new Namespace
    ns.addKnownNamespaces()
    ns.register("ex", EX_NS)
    
    val (ind,s)=sum("""
                    document
                    prefix ex <http://example.org/>
                    entity(ex:e2)
                    entity(ex:e1)
                    activity(ex:a)
                    wasGeneratedBy(ex:e2,ex:a,-)
                    wasDerivedFrom(ex:e2,ex:e1)
                    endDocument
                    """)
                    
      
    s.type0(ind.amap(q("e2"))) should be (Set(Ent()))
    s.type0(ind.amap(q("e1"))) should be (Set(Ent()))
    s.type0(ind.amap(q("a"))) should be (Set(Act()))
    s.type1(ind.amap(q("e2"))) should be (Set(Wdf(Set(Ent())),Wgb(Set(Act()))))
    s.type1.get(ind.amap(q("a"))) should be (None)     
    s.type1.get(ind.amap(q("e1"))) should be (None)     
    s.type_back1.get(ind.amap(q("e2"))) should be (None)     
    s.type_back1(ind.amap(q("a"))) should be (Set(Wgb_1(Set(Ent())))) 
    s.type_back1(ind.amap(q("e1"))) should be (Set(Wdf_1(Set(Ent())))) 
    s.type0.size should be (3)
    s.type1.size should be (1)
    s.type_back1.size should be (2)
    
  }
  "A document with triangle generation/usage/derivation" should "have correct provenance types" in {
    val ns=new Namespace
    ns.addKnownNamespaces()
    ns.register("ex", EX_NS)

    val (ind,s)=sum("""
                    document
                    prefix ex <http://example.org/>

                    entity(ex:e1)
                    entity(ex:e0)
                    activity(ex:a)
                    activity(ex:a2)
                    wasGeneratedBy(ex:e1,ex:a,-)
                    wasDerivedFrom(ex:e1,ex:e0)
                    used(ex:a,ex:e0,-)
                    used(ex:a2,ex:e0,-)
                    endDocument
                    """)

    var withTriangles = false;

    println(ind.amap)
    s.type0(ind.amap(q("e0"))) should be (Set(Ent()))
    s.type0(ind.amap(q("e1"))) should be (Set(Ent()))
    s.type0(ind.amap(q("a2"))) should be (Set(Act()))
    s.type0(ind.amap(q("a"))) should be (Set(Act()))
    s.type1(ind.amap(q("e1"))) should be (if (withTriangles) Set(Wdf(Set(Ent()))) else Set(Wdf(Set(Ent())),Wgb(Set(Act()))))  // FIXME
    s.type1.get(ind.amap(q("a"))) should be (if (withTriangles) None else Some(Set(Usd(Set(Ent())))))
    s.type1(ind.amap(q("a2"))) should be (Set(Usd(Set(Ent()))))
    s.type1.get(ind.amap(q("e0"))) should be (None)

    s.type2.get(ind.amap(q("e1"))) should be (if (withTriangles) None else Some(Set(Wgb(Set(Usd(Set(Ent())))))))
    s.type2.get(ind.amap(q("a"))) should be (None)
    s.type2.get(ind.amap(q("e0"))) should be (None)

    s.type0.size should be (4)
    s.type1.size should be (if (withTriangles) 2 else 3)
    s.type2.size should be (if (withTriangles) 0 else 1)
   // s.type_back1.size should be (if (TypePropagator.withTriangles) 2 else 3)

    val agg_provtypes_0_2=s.aggregateProvTypesToLevel(2)

    val index2=new SummaryConstructor(ind,agg_provtypes_0_2, nsBase).makeIndex()

    val doc2=index2.document()

    println(doc2)

  }

  "A document with triangle generation/usage/derivation with triangle detection" should "have correct provenance types" in {
    val ns=new Namespace
    ns.addKnownNamespaces()
    ns.register("ex", EX_NS)

    val (ind,s)=sum("""
                    document
                    prefix ex <http://example.org/>

                    entity(ex:e1)
                    entity(ex:e0)
                    activity(ex:a)
                    activity(ex:a2)
                    wasGeneratedBy(ex:e1,ex:a,-)
                    wasDerivedFrom(ex:e1,ex:e0)
                    used(ex:a,ex:e0,-)
                    used(ex:a2,ex:e0,-)
                    endDocument
                    """,
      true)  // triangle true!

    var withTriangles = true;

    println(ind.amap)
    s.type0(ind.amap(q("e0"))) should be (Set(Ent()))
    s.type0(ind.amap(q("e1"))) should be (Set(Ent()))
    s.type0(ind.amap(q("a2"))) should be (Set(Act()))
    s.type0(ind.amap(q("a"))) should be (Set(Act()))
    s.type1(ind.amap(q("e1"))) should be (if (withTriangles) Set(Wdf_triangle(Set(Ent()))) else Set(Wdf(Set(Ent())),Wgb(Set(Act()))))
    s.type1.get(ind.amap(q("a"))) should be (if (withTriangles) None else Some(Set(Usd(Set(Ent())))))
    s.type1(ind.amap(q("a2"))) should be (Set(Usd(Set(Ent()))))
    s.type1.get(ind.amap(q("e0"))) should be (None)

    s.type2.get(ind.amap(q("e1"))) should be (if (withTriangles) None else Some(Set(Wgb(Set(Usd(Set(Ent())))))))
    s.type2.get(ind.amap(q("a"))) should be (None)
    s.type2.get(ind.amap(q("e0"))) should be (None)

    s.type0.size should be (4)
    s.type1.size should be (if (withTriangles) 2 else 3)
    s.type2.size should be (if (withTriangles) 0 else 1)
    // s.type_back1.size should be (if (TypePropagator.withTriangles) 2 else 3)

    val agg_provtypes_0_2=s.aggregateProvTypesToLevel(2)

    val index2=new SummaryConstructor(ind,agg_provtypes_0_2, nsBase).makeIndex()

    val doc2=index2.document()

    println(doc2)


  }

  "A document with generation/usage/derivation" should "have correct provenance types" in {
    val ns=new Namespace
    ns.addKnownNamespaces()
    ns.register("ex", EX_NS)
    
    val (ind,s)=sum("""
                    document
                    prefix ex <http://example.org/>
                    entity(ex:e2)
                    entity(ex:e1)
                    entity(ex:e0)
                    activity(ex:a)
                    wasGeneratedBy(ex:e2,ex:a,-)
                    wasDerivedFrom(ex:e2,ex:e1)
                    wasDerivedFrom(ex:e1,ex:e0)
                    used(ex:a,ex:e0,-)
                    endDocument
                    """)
                    

    s.type0(ind.amap(q("e0"))) should be (Set(Ent()))
    s.type0(ind.amap(q("e2"))) should be (Set(Ent()))
    s.type0(ind.amap(q("e1"))) should be (Set(Ent()))
    s.type0(ind.amap(q("a"))) should be (Set(Act()))
    s.type1(ind.amap(q("e2"))) should be (Set(Wdf(Set(Ent())),Wgb(Set(Act()))))
    s.type1(ind.amap(q("a"))) should be (Set(Usd(Set(Ent()))))
    s.type1(ind.amap(q("e1"))) should be (Set(Wdf(Set(Ent()))))
    s.type1.get(ind.amap(q("e0"))) should be (None)     

    s.type2(ind.amap(q("e2"))) should be (Set(Wdf(Set(Wdf(Set(Ent())))),Wgb(Set(Usd(Set(Ent()))))))
    s.type2.get(ind.amap(q("e1"))) should be (None)
    s.type2.get(ind.amap(q("a"))) should be (None)
    s.type2.get(ind.amap(q("e0"))) should be (None)
   
    s.type0.size should be (4)
    s.type1.size should be (3)
    s.type2.size should be (1)
    s.type_back1.size should be (3)
    
    val agg_provtypes_0_2=s.aggregateProvTypesToLevel(2)

    val index2=new SummaryConstructor(ind,agg_provtypes_0_2, nsBase).makeIndex()
 
    val doc2=index2.document()
    
    println(doc2)
        
  }
 
  "A document with many derivations" should "have correct provenance types" in {
    val ns=new Namespace
    ns.addKnownNamespaces()
    ns.register("ex", EX_NS)
    
    val (ind,s)=sum("""
                    document
                    prefix ex <http://example.org/>
                    entity(ex:e2)
                    entity(ex:e1)
                    entity(ex:e0a)
                    entity(ex:e0b)
                    entity(ex:e0c)
                    entity(ex:e0d)
                    entity(ex:e0e)
                    wasDerivedFrom(ex:e2,ex:e1)
                    wasDerivedFrom(ex:e1,ex:e0a)
                    wasDerivedFrom(ex:e1,ex:e0b)
                    wasDerivedFrom(ex:e1,ex:e0c)
                    wasDerivedFrom(ex:e1,ex:e0d)
                    wasDerivedFrom(ex:e1,ex:e0d)
                    endDocument
                    """)
                    
    val agg_provtypes_0_1=s.aggregateProvTypesToLevel(1)
    val agg_provtypes_0_2=s.aggregateProvTypesToLevel(2)
   
      
    val index2=new SummaryConstructor(ind,agg_provtypes_0_2,nsBase).makeIndex()

    val doc2=index2.document()
    
   
    println(doc2)
    
    println(ind.nodes)
    
  }


  "A document with association and plan and types" should "have correct provenance types" in {
    val ns=new Namespace
    ns.addKnownNamespaces()
    ns.register("ex", EX_NS)

    val (ind,s)=sum("""
                    document
                    prefix ex <http://example.org/>
                    activity(ex:a, [prov:type='ex:Act'])
                    agent(ex:ag, [prov:type='ex:Ag'])
                    entity(ex:pl, [prov:type='ex:Pl'])
                    wasAssociatedWith(ex:a,ex:ag,ex:pl, [prov:type='ex:Assoc'])
                    endDocument
                    """)


    s.type0(ind.amap(q("a"))) should be (Set(Act(), Prim("ex:Act")))
    s.type0(ind.amap(q("ag"))) should be (Set(Ag(), Prim("ex:Ag")))
    s.type0(ind.amap(q("pl"))) should be (Set(Ent(), Prim("ex:Pl")))
    s.type1(ind.amap(q("a"))) should be (Set(Waw(Set(Ag(), Prim("ex:Ag")), Set(Ent(), Prim("ex:Pl")))))
    s.type1.get(ind.amap(q("ag"))) should be (None)
    s.type1.get(ind.amap(q("pl"))) should be (None)
    s.type_back1.get(ind.amap(q("a"))) should be (None)
    s.type_back1(ind.amap(q("ag"))) should be (Set(Waw_1(Set(Act(), Prim("ex:Act")), Set(Ent(), Prim("ex:Pl"))))) //TO CHECK: waw
    s.type_back1.get(ind.amap(q("pl"))) should be (None)
    s.type0.size should be (3)
    s.type1.size should be (1)
    s.type_back1.size should be (1)


  }


  "A document with association and plan and types and filter" should "have correct provenance types" in {
    val ns=new Namespace
    ns.addKnownNamespaces()
    ns.register("ex", EX_NS)

    val level0=Level0Mapper(""" {"mapper":{},"ignore":["http://example.org/Ag"]} """)


    val (ind,s)=sum("""
                    document
                    prefix ex <http://example.org/>
                    activity(ex:a, [prov:type='ex:Act'])
                    agent(ex:ag, [prov:type='ex:Ag'])
                    entity(ex:pl, [prov:type='ex:Pl'])
                    wasAssociatedWith(ex:a,ex:ag,ex:pl, [prov:type='ex:Assoc'])
                    endDocument
                    """,false, level0)


    s.type0(ind.amap(q("a"))) should be (Set(Act(), Prim("ex:Act")))
    s.type0(ind.amap(q("ag"))) should be (Set(Ag()))
    s.type0(ind.amap(q("pl"))) should be (Set(Ent(), Prim("ex:Pl")))
    s.type1(ind.amap(q("a"))) should be (Set(Waw(Set(Ag()), Set(Ent(), Prim("ex:Pl")))))
    s.type1.get(ind.amap(q("ag"))) should be (None)
    s.type1.get(ind.amap(q("pl"))) should be (None)
    s.type_back1.get(ind.amap(q("a"))) should be (None)
    s.type_back1(ind.amap(q("ag"))) should be (Set(Waw_1(Set(Act(), Prim("ex:Act")), Set(Ent(), Prim("ex:Pl"))))) //TO CHECK: waw
    s.type_back1.get(ind.amap(q("pl"))) should be (None)
    s.type0.size should be (3)
    s.type1.size should be (1)
    s.type_back1.size should be (1)


  }



  "A document with association and plan and types and filter2" should "have correct provenance types" in {
    val ns=new Namespace
    ns.addKnownNamespaces()
    ns.register("ex", EX_NS)

    val level0=Level0Mapper(""" {"mapper":{},"ignore":["http://example.org/Pl", "http://example.org/Ag"]} """)


    val (ind,s)=sum("""
                    document
                    prefix ex <http://example.org/>
                    activity(ex:a, [prov:type='ex:Act'])
                    agent(ex:ag, [prov:type='ex:Ag'])
                    entity(ex:pl, [prov:type='ex:Pl'])
                    wasAssociatedWith(ex:a,ex:ag,ex:pl, [prov:type='ex:Assoc'])
                    endDocument
                    """,false, level0)


    s.type0(ind.amap(q("a"))) should be (Set(Act(), Prim("ex:Act")))
    s.type0(ind.amap(q("ag"))) should be (Set(Ag()))
    s.type0(ind.amap(q("pl"))) should be (Set(Ent()))
    s.type1(ind.amap(q("a"))) should be (Set(Waw(Set(Ag()), Set(Ent()))))  //TODO: association type is not explicit here
    s.type1.get(ind.amap(q("ag"))) should be (None)
    s.type1.get(ind.amap(q("pl"))) should be (None)
    s.type_back1.get(ind.amap(q("a"))) should be (None)
    s.type_back1(ind.amap(q("ag"))) should be (Set(Waw_1(Set(Act(), Prim("ex:Act")), Set(Ent())))) //TO CHECK: waw
    s.type_back1.get(ind.amap(q("pl"))) should be (None)
    s.type0.size should be (3)
    s.type1.size should be (1)
    s.type_back1.size should be (1)


  }




  "Provenance challenge " should "have correct summary " in {
    val ns=new Namespace
    ns.addKnownNamespaces()
    ns.register("ex", EX_NS)
    
    val level0b=new Level0Mapper(Map("http://openprovenance.org/primitives#File" -> "File"), Set(), Set())
    
    
    val level0=Level0Mapper(""" {"mapper":{"http://openprovenance.org/primitives#File":"File"},"ignore":[]} """)

    println(level0.exportToJSon())
    
    val (ind,s)=sum("""
                    document
  prefix prim <http://openprovenance.org/primitives#>
  prefix pc1 <http://www.ipaw.info/pc1/>
  activity(pc1:a1,-,-,[prov:type = 'prim:align_warp', prov:label = "align_warp 1"])
  activity(pc1:a2,-,-,[prov:type = 'prim:align_warp', prov:label = "align_warp 2"])
  activity(pc1:a3,-,-,[prov:type = 'prim:align_warp', prov:label = "align_warp 3"])
  activity(pc1:a4,-,-,[prov:type = 'prim:align_warp', prov:label = "align_warp 4"])
  activity(pc1:a5,-,-,[prov:type = 'prim:reslice', prov:label = "Reslice 1"])
  activity(pc1:a6,-,-,[prov:type = 'prim:reslice', prov:label = "Reslice 2"])
  activity(pc1:a7,-,-,[prov:type = 'prim:reslice', prov:label = "Reslice 3"])
  activity(pc1:a8,-,-,[prov:type = 'prim:reslice', prov:label = "Reslice 4"])
  activity(pc1:a9,-,-,[prov:type = 'prim:softmean', prov:label = "Softmean"])
  activity(pc1:a10,-,-,[prov:type = 'prim:slicer', prov:label = "Slicer 1"])
  activity(pc1:a11,-,-,[prov:type = 'prim:slicer', prov:label = "Slicer 2"])
  activity(pc1:a12,-,-,[prov:type = 'prim:slicer', prov:label = "Slicer 3"])
  activity(pc1:a13,-,-,[prov:type = 'prim:convert', prov:label = "Convert 1"])
  activity(pc1:a14,-,-,[prov:type = 'prim:convert', prov:label = "Convert 2"])
  activity(pc1:a15,-,-,[prov:type = 'prim:convert', prov:label = "Convert 3"])
  entity(pc1:e1,[prov:type = 'prim:File', prov:label = "Reference Image", pc1:url = "http://www.ipaw.info/challenge/reference.img" %% xsd:anyURI])
  entity(pc1:e2,[prov:type = 'prim:File', prov:label = "Reference Header", pc1:url = "http://www.ipaw.info/challenge/reference.hdr" %% xsd:anyURI])
  entity(pc1:e5,[prov:type = 'prim:File', prov:label = "Anatomy I2", pc1:url = "http://www.ipaw.info/challenge/anatomy2.img" %% xsd:anyURI])
  entity(pc1:e6,[prov:type = 'prim:File', prov:label = "Anatomy H2", pc1:url = "http://www.ipaw.info/challenge/anatomy2.hdr" %% xsd:anyURI])
  entity(pc1:e3,[prov:type = 'prim:File', prov:label = "Anatomy I1", pc1:url = "http://www.ipaw.info/challenge/anatomy1.img" %% xsd:anyURI])
  entity(pc1:e4,[prov:type = 'prim:File', prov:label = "Anatomy H1", pc1:url = "http://www.ipaw.info/challenge/anatomy1.hdr" %% xsd:anyURI])
  entity(pc1:e7,[prov:type = 'prim:File', prov:label = "Anatomy I3", pc1:url = "http://www.ipaw.info/challenge/anatomy3.img" %% xsd:anyURI])
  entity(pc1:e8,[prov:type = 'prim:File', prov:label = "Anatomy H3", pc1:url = "http://www.ipaw.info/challenge/anatomy3.hdr" %% xsd:anyURI])
  entity(pc1:e9,[prov:type = 'prim:File', prov:label = "Anatomy I4", pc1:url = "http://www.ipaw.info/challenge/anatomy4.img" %% xsd:anyURI])
  entity(pc1:e10,[prov:type = 'prim:File', prov:label = "Anatomy H4", pc1:url = "http://www.ipaw.info/challenge/anatomy4.hdr" %% xsd:anyURI])
  entity(pc1:e11,[prov:type = 'prim:File', prov:label = "Warp Params1", pc1:url = "http://www.ipaw.info/challenge/warp1.warp" %% xsd:anyURI])
  entity(pc1:e12,[prov:type = 'prim:File', prov:label = "Warp Params2", pc1:url = "http://www.ipaw.info/challenge/warp2.warp" %% xsd:anyURI])
  entity(pc1:e13,[prov:type = 'prim:File', prov:label = "Warp Params3", pc1:url = "http://www.ipaw.info/challenge/warp3.warp" %% xsd:anyURI])
  entity(pc1:e14,[prov:type = 'prim:File', prov:label = "Warp Params4", pc1:url = "http://www.ipaw.info/challenge/warp4.warp" %% xsd:anyURI])
  entity(pc1:e15,[prov:type = 'prim:File', prov:label = "Resliced I1", pc1:url = "http://www.ipaw.info/challenge/resliced1.img" %% xsd:anyURI])
  entity(pc1:e16,[prov:type = 'prim:File', prov:label = "Resliced H1", pc1:url = "http://www.ipaw.info/challenge/resliced1.hdr" %% xsd:anyURI])
  entity(pc1:e17,[prov:type = 'prim:File', prov:label = "Resliced I2", pc1:url = "http://www.ipaw.info/challenge/resliced2.img" %% xsd:anyURI])
  entity(pc1:e18,[prov:type = 'prim:File', prov:label = "Resliced H2", pc1:url = "http://www.ipaw.info/challenge/resliced2.hdr" %% xsd:anyURI])
  entity(pc1:e19,[prov:type = 'prim:File', prov:label = "Resliced I3", pc1:url = "http://www.ipaw.info/challenge/resliced3.img" %% xsd:anyURI])
  entity(pc1:e20,[prov:type = 'prim:File', prov:label = "Resliced H3", pc1:url = "http://www.ipaw.info/challenge/resliced3.hdr" %% xsd:anyURI])
  entity(pc1:e21,[prov:type = 'prim:File', prov:label = "Resliced I4", pc1:url = "http://www.ipaw.info/challenge/resliced4.img" %% xsd:anyURI])
  entity(pc1:e22,[prov:type = 'prim:File', prov:label = "Resliced H4", pc1:url = "http://www.ipaw.info/challenge/resliced4.hdr" %% xsd:anyURI])
  entity(pc1:e23,[prov:type = 'prim:File', prov:label = "Atlas Image", pc1:url = "http://www.ipaw.info/challenge/atlas.img" %% xsd:anyURI])
  entity(pc1:e24,[prov:type = 'prim:File', prov:label = "Atlas Header", pc1:url = "http://www.ipaw.info/challenge/atlas.hdr" %% xsd:anyURI])
  entity(pc1:e25,[prov:type = 'prim:File', prov:label = "Atlas X Slice", pc1:url = "http://www.ipaw.info/challenge/atlas-x.pgm" %% xsd:anyURI])
  entity(pc1:e25p,[prov:type = 'prim:String', prov:label = "slicer param 1", pc1:value = "-x .5" %% xsd:string])
  entity(pc1:e26,[prov:type = 'prim:File', prov:label = "Atlas Y Slice", pc1:url = "http://www.ipaw.info/challenge/atlas-y.pgm" %% xsd:anyURI])
  entity(pc1:e26p,[prov:type = 'prim:String', prov:label = "slicer param 2", pc1:value = "-y .5" %% xsd:string])
  entity(pc1:e27,[prov:type = 'prim:File', prov:label = "Atlas Z Slice", pc1:url = "http://www.ipaw.info/challenge/atlas-z.pgm" %% xsd:anyURI])
  entity(pc1:e27p,[prov:type = 'prim:String', prov:label = "slicer param 3", pc1:value = "-z .5" %% xsd:string])
  entity(pc1:e28,[prov:type = 'prim:File', prov:label = "Atlas X Graphic", pc1:url = "http://www.ipaw.info/challenge/atlas-x.gif" %% xsd:anyURI])
  entity(pc1:e29,[prov:type = 'prim:File', prov:label = "Atlas Y Graphic", pc1:url = "http://www.ipaw.info/challenge/atlas-y.gif" %% xsd:anyURI])
  entity(pc1:e30,[prov:type = 'prim:File', prov:label = "Atlas Z Graphic", pc1:url = "http://www.ipaw.info/challenge/atlas-z.gif" %% xsd:anyURI])
  agent(pc1:ag1,[prov:label = "John Doe"])
  used(pc1:a1,pc1:e3,-,[prov:role = 'prim:img'])
  used(pc1:a1,pc1:e4,-,[prov:role = 'prim:hdr'])
  used(pc1:u3;pc1:a1,pc1:e1,-,[prov:role = 'prim:imgRef'])
  used(pc1:a1,pc1:e2,-,[prov:role = 'prim:hdrRef'])
  used(pc1:a2,pc1:e5,-,[prov:role = 'prim:img'])
  used(pc1:a2,pc1:e6,-,[prov:role = 'prim:hdr'])
  used(pc1:a2,pc1:e1,-,[prov:role = 'prim:imgRef'])
  used(pc1:a2,pc1:e2,-,[prov:role = 'prim:hdrRef'])
  used(pc1:a3,pc1:e7,-,[prov:role = 'prim:img'])
  used(pc1:a3,pc1:e8,-,[prov:role = 'prim:hdr'])
  used(pc1:a3,pc1:e1,-,[prov:role = 'prim:imgRef'])
  used(pc1:a3,pc1:e2,-,[prov:role = 'prim:hdrRef'])
  used(pc1:a4,pc1:e9,-,[prov:role = 'prim:img'])
  used(pc1:a4,pc1:e10,-,[prov:role = 'prim:hdr'])
  used(pc1:a4,pc1:e1,-,[prov:role = 'prim:imgRef'])
  used(pc1:a4,pc1:e2,-,[prov:role = 'prim:hdrRef'])
  used(pc1:a5,pc1:e11,-,[prov:role = 'prim:in'])
  used(pc1:a6,pc1:e12,-,[prov:role = 'prim:in'])
  used(pc1:a7,pc1:e13,-,[prov:role = 'prim:in'])
  used(pc1:a8,pc1:e14,-,[prov:role = 'prim:in'])
  used(pc1:a9,pc1:e15,-,[prov:role = 'prim:i1'])
  used(pc1:a9,pc1:e16,-,[prov:role = 'prim:h1'])
  used(pc1:a9,pc1:e17,-,[prov:role = 'prim:i2'])
  used(pc1:a9,pc1:e18,-,[prov:role = 'prim:h2'])
  used(pc1:a9,pc1:e19,-,[prov:role = 'prim:i3'])
  used(pc1:a9,pc1:e20,-,[prov:role = 'prim:h3'])
  used(pc1:a9,pc1:e21,-,[prov:role = 'prim:i4'])
  used(pc1:a9,pc1:e22,-,[prov:role = 'prim:h4'])
  used(pc1:a10,pc1:e23,-,[prov:role = 'prim:img'])
  used(pc1:a10,pc1:e24,-,[prov:role = 'prim:hdr'])
  used(pc1:a11,pc1:e23,-,[prov:role = 'prim:img'])
  used(pc1:a11,pc1:e24,-,[prov:role = 'prim:hdr'])
  used(pc1:a12,pc1:e23,-,[prov:role = 'prim:img'])
  used(pc1:a12,pc1:e24,-,[prov:role = 'prim:hdr'])
  used(pc1:a10,pc1:e25p,-,[prov:role = 'prim:param'])
  used(pc1:a11,pc1:e26p,-,[prov:role = 'prim:param'])
  used(pc1:a12,pc1:e27p,-,[prov:role = 'prim:param'])
  used(pc1:a13,pc1:e25,-,[prov:role = 'prim:in'])
  used(pc1:a14,pc1:e26,-,[prov:role = 'prim:in'])
  used(pc1:a15,pc1:e27,-,[prov:role = 'prim:in'])
  wasGeneratedBy(pc1:wgb1;pc1:e11,pc1:a1,-,[prov:role = 'prim:out'])
  wasGeneratedBy(pc1:e12,pc1:a2,-,[prov:role = 'prim:out'])
  wasGeneratedBy(pc1:e13,pc1:a3,-,[prov:role = 'prim:out'])
  wasGeneratedBy(pc1:e14,pc1:a4,-,[prov:role = 'prim:out'])
  wasGeneratedBy(pc1:e15,pc1:a5,-,[prov:role = 'prim:img'])
  wasGeneratedBy(pc1:e16,pc1:a5,-,[prov:role = 'prim:hdr'])
  wasGeneratedBy(pc1:e17,pc1:a6,-,[prov:role = 'prim:img'])
  wasGeneratedBy(pc1:e18,pc1:a6,-,[prov:role = 'prim:hdr'])
  wasGeneratedBy(pc1:e19,pc1:a7,-,[prov:role = 'prim:img'])
  wasGeneratedBy(pc1:e20,pc1:a7,-,[prov:role = 'prim:hdr'])
  wasGeneratedBy(pc1:e21,pc1:a8,-,[prov:role = 'prim:img'])
  wasGeneratedBy(pc1:e22,pc1:a8,-,[prov:role = 'prim:hdr'])
  wasGeneratedBy(pc1:e23,pc1:a9,-,[prov:role = 'prim:img'])
  wasGeneratedBy(pc1:e24,pc1:a9,-,[prov:role = 'prim:hdr'])
  wasGeneratedBy(pc1:e25,pc1:a10,-,[prov:role = 'prim:out'])
  wasGeneratedBy(pc1:e26,pc1:a11,-,[prov:role = 'prim:out'])
  wasGeneratedBy(pc1:e27,pc1:a12,-,[prov:role = 'prim:out'])
  wasGeneratedBy(pc1:e28,pc1:a13,2012-10-26T09:58:08.407+01:00,[prov:role = 'prim:out'])
  wasGeneratedBy(pc1:e29,pc1:a14,2012-10-26T09:58:08.407+01:00,[prov:role = 'prim:out'])
  wasGeneratedBy(pc1:e30,pc1:a15,2012-10-26T09:58:08.407+01:00,[prov:role = 'prim:out'])
  wasDerivedFrom(pc1:e11, pc1:e1, pc1:a1, pc1:wgb1, pc1:u3)
  wasDerivedFrom(pc1:e11, pc1:e2)
  wasDerivedFrom(pc1:e11, pc1:e3)
  wasDerivedFrom(pc1:e11, pc1:e4)
  wasDerivedFrom(pc1:e12, pc1:e1)
  wasDerivedFrom(pc1:e12, pc1:e2)
  wasDerivedFrom(pc1:e12, pc1:e5)
  wasDerivedFrom(pc1:e12, pc1:e6)
  wasDerivedFrom(pc1:e13, pc1:e1)
  wasDerivedFrom(pc1:e13, pc1:e2)
  wasDerivedFrom(pc1:e13, pc1:e7)
  wasDerivedFrom(pc1:e13, pc1:e8)
  wasDerivedFrom(pc1:e14, pc1:e1)
  wasDerivedFrom(pc1:e14, pc1:e2)
  wasDerivedFrom(pc1:e14, pc1:e9)
  wasDerivedFrom(pc1:e14, pc1:e10)
  wasDerivedFrom(pc1:e15, pc1:e11)
  wasDerivedFrom(pc1:e16, pc1:e11)
  wasDerivedFrom(pc1:e17, pc1:e12)
  wasDerivedFrom(pc1:e18, pc1:e12)
  wasDerivedFrom(pc1:e19, pc1:e13)
  wasDerivedFrom(pc1:e20, pc1:e13)
  wasDerivedFrom(pc1:e21, pc1:e14)
  wasDerivedFrom(pc1:e22, pc1:e14)
  wasDerivedFrom(pc1:e23, pc1:e15)
  wasDerivedFrom(pc1:e23, pc1:e16)
  wasDerivedFrom(pc1:e23, pc1:e17)
  wasDerivedFrom(pc1:e23, pc1:e18)
  wasDerivedFrom(pc1:e23, pc1:e19)
  wasDerivedFrom(pc1:e23, pc1:e20)
  wasDerivedFrom(pc1:e23, pc1:e21)
  wasDerivedFrom(pc1:e23, pc1:e22)
  wasDerivedFrom(pc1:e24, pc1:e15)
  wasDerivedFrom(pc1:e24, pc1:e16)
  wasDerivedFrom(pc1:e24, pc1:e17)
  wasDerivedFrom(pc1:e24, pc1:e18)
  wasDerivedFrom(pc1:e24, pc1:e19)
  wasDerivedFrom(pc1:e24, pc1:e20)
  wasDerivedFrom(pc1:e24, pc1:e21)
  wasDerivedFrom(pc1:e24, pc1:e22)
  wasDerivedFrom(pc1:e25, pc1:e23)
  wasDerivedFrom(pc1:e25, pc1:e24)
  wasDerivedFrom(pc1:e26, pc1:e23)
  wasDerivedFrom(pc1:e26, pc1:e24)
  wasDerivedFrom(pc1:e27, pc1:e23)
  wasDerivedFrom(pc1:e27, pc1:e24)
  wasDerivedFrom(pc1:e28, pc1:e25)
  wasDerivedFrom(pc1:e29, pc1:e26)
  wasDerivedFrom(pc1:e30, pc1:e27)
  wasAssociatedWith(pc1:waw1;pc1:a1,pc1:ag1,-)
endDocument
                    """,
      false, level0)
                    
    def prim(s: String) = {
      new QualifiedName("prim",s,"http://openprovenance.org/primitives#")
    }
    def pc1(s: String) = {
      new QualifiedName("pc1",s,"http://www.ipaw.info/pc1/")
    }
                    
    val agg_provtypes_0_1=s.aggregateProvTypesToLevel(1)
    //val (v1,m1,qn1)= s.enumerateProvTypes(agg_provtypes_0_1)
    
    //println ("types map (m0) " + s.type0)
    //println ("types map (m1) " + m1)
    
    ind.absentNodes shouldBe empty
    
    val summary1=new SummaryConstructor(ind,agg_provtypes_0_1, nsBase).makeIndex()
    println("Summary 1 appears to be " + summary1.provTypeIndex + " " + summary1.amap)
     
    val doc2=summary1.document()
    
    agg_provtypes_0_1(ind.amap(pc1("e1"))) should be (Set(Ent(), Prim("File"))) // See how we refer to the customised provtype
    agg_provtypes_0_1(ind.amap(pc1("e25p"))) should be (Set(Ent(), Prim("prim:String")))  // here default provtype for this domain specific type
   
    val idx=summary1.provTypeIndex(Set(Ent(), Prim("File")))
    summary1.nodes(idx) shouldBe a [Entity]
    println(summary1.nodes(idx).asInstanceOf[Entity])
    summary1.nodes(idx).asInstanceOf[Entity].other(new QualifiedName("sum","size",SUM_NS)).head.value should be ("10")
    
    val idx1=summary1.provTypeIndex( agg_provtypes_0_1(ind.amap(pc1("a4"))) )       // align_warp    
    val idx1_a1=summary1.provTypeIndex( agg_provtypes_0_1(ind.amap(pc1("a1"))) )       // align_warp   , has an agent 
    summary1.nodes(idx1) shouldBe a [Activity]
    summary1.nodes(idx1).asInstanceOf[Activity].other(new QualifiedName("sum","size",SUM_NS)).head.value should be ("3")
    summary1.nodes(idx1_a1).asInstanceOf[Activity].other(new QualifiedName("sum","size",SUM_NS)).head.value should be ("1")
  
    agg_provtypes_0_1(ind.amap(pc1("a11"))) should be (Set(Act(), 
                                                           Prim("prim:slicer"),
                                                           Usd(Set(Ent(), Prim("File"))),
                                                           Usd(Set(Ent(), Prim("prim:String")))))
    val idx2=summary1.provTypeIndex( agg_provtypes_0_1(ind.amap(pc1("a11"))) )
    summary1.nodes(idx2) shouldBe a [Activity]
    summary1.nodes(idx2).asInstanceOf[Activity].other(new QualifiedName("sum","size",SUM_NS)).head.value should be ("3")
    
    Graphics.toDot(doc2, {q:QualifiedName => summary1.amap(q).toString }, summary1.sizeRange, new File("target/pc1-sum.dot"))
    Graphics.dotConvert("target/pc1-sum.dot","target/pc1-sum.svg","svg")
    
    println(" quantities " + summary1.sizeRange)
  
  }
 
  def ex(s: String) = {
      new QualifiedName("ex",s,EX_NS)
  }

  "A document with generation (but no entity/activity)" should "have correct summary" in {
    val ns=new Namespace
    ns.addKnownNamespaces()
    ns.register("ex", EX_NS)
    
 
    val (ind,s)=sum("""
                    document
                    prefix ex <http://example.org/>
                    wasGeneratedBy(ex:e,ex:a,-)
                    endDocument
                    """)
                    
    
    s.type0(ind.amap(q("e"))) should be (Set(Ent()))
    s.type0(ind.amap(q("a"))) should be (Set(Act()))
    s.type1(ind.amap(q("e"))) should be (Set(Wgb(Set(Act()))))
    s.type1.get(ind.amap(q("a"))) should be (None)     
    s.type_back1.get(ind.amap(q("e"))) should be (None)     
    s.type_back1(ind.amap(q("a"))) should be (Set(Wgb_1(Set(Ent())))) 
    s.type0.size should be (2)
    s.type1.size should be (1)
    s.type_back1.size should be (1)
    
    ind.absentNodes should not be empty
    
    val agg_provtypes_0_1=s.aggregateProvTypesToLevel(1)
    val summary1=new SummaryConstructor(ind,agg_provtypes_0_1,nsBase).makeIndex()
    println("Summary 1 appears to be " + summary1.provTypeIndex + " " + summary1.amap)
     
    val doc2=summary1.document()
    println("Summary is " + doc2)
        
    
    val idx1=summary1.provTypeIndex( agg_provtypes_0_1(ind.amap(ex("e"))) )                      
    summary1.nodes(idx1) shouldBe a [Entity]
    summary1.nodes(idx1).asInstanceOf[Entity].other(new QualifiedName("sum","size",SUM_NS)).head.value should be ("0")
  
    val idx2=summary1.provTypeIndex( agg_provtypes_0_1(ind.amap(ex("a"))) )                      
    summary1.nodes(idx2) shouldBe a [Activity]
    summary1.nodes(idx2).asInstanceOf[Activity].other(new QualifiedName("sum","size",SUM_NS)).head.value should be ("0")
    
    summary1.absentNodes.size should be (0)

    
  }
  
   "A document with influence (but no influencer/influencee)" should "have correct summary" in {
    val ns=new Namespace
    ns.addKnownNamespaces()
    ns.register("ex", EX_NS)
    
 
    val (ind,s)=sum("""
                    document
                    prefix ex <http://example.org/>
                    wasInfluencedBy(ex:e,ex:a)
                    endDocument
                    """)
                    
    ind.absentNodes.size should be (2)

    s.type0(ind.amap(q("e"))) should be (Set(Resource()))
    s.type0(ind.amap(q("a"))) should be (Set(Resource()))
    s.type1(ind.amap(q("e"))) should be (Set(Winflb(Set(Resource()))))  
    s.type1.get(ind.amap(q("a"))) should be (None)     
    s.type_back1.get(ind.amap(q("e"))) should be (None)     
    s.type_back1(ind.amap(q("a"))) should be (Set(Winflb_1(Set(Resource()))))  
    s.type0.size should be (2)
    s.type1.size should be (1)
    s.type_back1.size should be (1)

   
    val agg_provtypes_0_1=s.aggregateProvTypesToLevel(1)
    
           
    val summary1=new SummaryConstructor(ind,agg_provtypes_0_1,nsBase).makeIndex()
     
    val doc2=summary1.document()
    println("Summary is " + doc2)
        
    
    val idx1=summary1.provTypeIndex( agg_provtypes_0_1(ind.amap(ex("e"))) )  
    
    summary1.nodes.get(idx1) shouldBe (None)
  
    val idx2=summary1.provTypeIndex( agg_provtypes_0_1(ind.amap(ex("a"))) )                      
    summary1.nodes.get(idx2) shouldBe (None)
    
    summary1.absentNodes.size should be (0)

  }
  
   "A document with influence (but no influencer)" should "have correct summary" in {
    val ns=new Namespace
    ns.addKnownNamespaces()
    ns.register("ex", EX_NS)
    
 
    val (ind,s)=sum("""
                    document
                    prefix ex <http://example.org/>
                    entity(ex:e)
                    wasInfluencedBy(ex:e,ex:a)
                    endDocument
                    """)
                    
    ind.absentNodes.size should be (1)

    s.type0(ind.amap(q("e"))) should be (Set(Ent()))
    s.type0(ind.amap(q("a"))) should be (Set(Resource()))
    s.type1(ind.amap(q("e"))) should be (Set(Winflb(Set(Resource()))))  
    s.type1.get(ind.amap(q("a"))) should be (None)     
    s.type_back1.get(ind.amap(q("e"))) should be (None)     
    s.type_back1(ind.amap(q("a"))) should be (Set(Winflb_1(Set(Ent()))))  
    s.type0.size should be (2)
    s.type1.size should be (1)
    s.type_back1.size should be (1)
    
   
    val agg_provtypes_0_1=s.aggregateProvTypesToLevel(1)
    
           
    val summary1=new SummaryConstructor(ind,agg_provtypes_0_1, nsBase).makeIndex()
     
    val doc2=summary1.document()
    println("Summary is " + doc2)
        
    
    val idx1=summary1.provTypeIndex( agg_provtypes_0_1(ind.amap(ex("e"))) )  
    summary1.nodes(idx1) shouldBe a [Entity]
    summary1.nodes(idx1).asInstanceOf[Entity].other(new QualifiedName("sum","size",SUM_NS)).head.value should be ("1")
  
    val idx2=summary1.provTypeIndex( agg_provtypes_0_1(ind.amap(ex("a"))) )                      
    summary1.nodes.get(idx2) shouldBe (None)
    
    summary1.absentNodes.size should be (0)

  }
  
  "A document with generatedAtTime (but no entity/activity)" should "have correct summary" in {
    val ns=new Namespace
    ns.addKnownNamespaces()
    ns.register("ex", EX_NS)
    
 
    val (ind,s)=sum("""
                    document
                    prefix ex <http://example.org/>
                    wasGeneratedBy(ex:e,-,2014-05-19T10:30:00.000+01:00)
                    endDocument
                    """)
                    
    
    s.type0(ind.amap(q("e"))) should be (Set(Ent()))
    /*
    s.type1(ind.amap(q("e"))) should be (Set(Wgb(Set(Act()))))
    s.type_back1.get(ind.amap(q("e"))) should be (None)     
    s.type0.size should be (2)
    s.type1.size should be (1)
    s.type_back1.size should be (1)
    
    ind.absentNodes should not be empty
    */
    val agg_provtypes_0_1=s.aggregateProvTypesToLevel(1)
    val summary1=new SummaryConstructor(ind,agg_provtypes_0_1,nsBase).makeIndex()
    println("Summary 1 appears to be " + summary1.provTypeIndex + " " + summary1.amap)
     
    val doc2=summary1.document()
    println("Summary is " + doc2)
        
    
    val idx1=summary1.provTypeIndex( agg_provtypes_0_1(ind.amap(ex("e"))) )                      
    summary1.nodes(idx1) shouldBe a [Entity]
    summary1.nodes(idx1).asInstanceOf[Entity].other(new QualifiedName("sum","size",SUM_NS)).head.value should be ("0")
  
 
    summary1.absentNodes.size should be (0)

    
  }
  
   "A document with mentionOf " should "have correct summary" in {
    val ns=new Namespace
    ns.addKnownNamespaces()
    ns.register("ex", EX_NS)
    
 
    val (ind,s)=sum("""
                    document
                    prefix ex <http://example.org/>
                    
                    mentionOf(ex:e1,ex:e2, ex:e3)
                    endDocument
                    """)
                    
    
    s.type0(ind.amap(q("e1"))) should be (Set(Ent()))
    s.type0(ind.amap(q("e2"))) should be (Set(Ent()))
    s.type0(ind.amap(q("e3"))) should be (Set(Ent()))
  
    val agg_provtypes_0_1=s.aggregateProvTypesToLevel(1)

    val summary1=new SummaryConstructor(ind,agg_provtypes_0_1,nsBase).makeIndex()
    println("Summary 1 appears to be " + summary1.provTypeIndex + " " + summary1.amap)
     
    val doc2=summary1.document()
    println("Summary is " + doc2)
        
    
    val idx1=summary1.provTypeIndex( agg_provtypes_0_1(ind.amap(ex("e1"))) )                      
    summary1.nodes(idx1) shouldBe a [Entity]
    summary1.nodes(idx1).asInstanceOf[Entity].other(new QualifiedName("sum","size",SUM_NS)).head.value should be ("0")
  
 
    summary1.absentNodes.size should be (0)

    
  }
  
  
   "Two documents with generation/usage/derivation" should "have differences" in {
    val ns=new Namespace
    ns.addKnownNamespaces()
    ns.register("ex", EX_NS)
    
    val (ind1,s1)=sum("""
                    document
                    prefix ex <http://example.org/>
                    entity(ex:e2)
                    entity(ex:e1)
                    entity(ex:e0)
                    activity(ex:a)
                    wasGeneratedBy(ex:e2,ex:a,-)
                    wasDerivedFrom(ex:e2,ex:e1)
                    wasDerivedFrom(ex:e1,ex:e0)
                    used(ex:a,ex:e0,-)
                    endDocument
                    """)
                    
      

    
    //val agg_provtypes_0_2=s1.aggregateProvTypesToLevel(2)
    //val index1=new SummaryIndexes(s1, ind1.succ,agg_provtypes_0_2).makeIndex()
    val index1=s1.getConstructorForLevel(2,nsBase,PrettyMethod.Name,true,true,false).makeIndex()

       
    val (ind2,s2)=sum("""
                    document
                    prefix ex <http://example.org/>
                    entity(ex:e2)
                    activity(ex:a)
                    wasGeneratedBy(ex:e2,ex:a,-)
                    wasDerivedFrom(ex:e2,ex:e1)
                    wasDerivedFrom(ex:e1,ex:e0)
                    used(ex:a,ex:e0,-)
                    agent(ex:ag)
                    wasAttributedTo(ex:e2,ex:ag)
                    endDocument
                    """)
                    
      

    
    //val agg_provtypes_0_2_b=s2.aggregateProvTypesToLevel(2)
    //val index2=new SummaryIndexes(s2, ind2.succ,agg_provtypes_0_2_b).makeIndex()
 
    val index2=s2.getConstructorForLevel(2,nsBase,PrettyMethod.Name,true,true, false).makeIndex()

    val (meOnly,meCommon,otherOnly,otherCommon) = index1.diff(index2)  //TODO: typing of SummaryIndexes as a subclass of instance doesn't allow idx1.diff(idx2)

    val outwa=new BufferedWriter(new FileWriter("target/diffa.json"))
    val outwb=new BufferedWriter(new FileWriter("target/diffb.json"))
    val outw1=new BufferedWriter(new FileWriter("target/diff1.json"))
    val outw2=new BufferedWriter(new FileWriter("target/diff2.json"))
    val outw3=new BufferedWriter(new FileWriter("target/diff3.json"))
    val outw4=new BufferedWriter(new FileWriter("target/diff4.json"))
    index1.exportToJsonDescription(outwa)
    index2.exportToJsonDescription(outwb)

    SummaryIndex.exporToJsonDescription(outw1, meOnly)
    SummaryIndex.exporToJsonDescription(outw2, meCommon)
    SummaryIndex.exporToJsonDescription(outw3, otherOnly)
    SummaryIndex.exporToJsonDescription(outw4, otherCommon)
    
    val descriptions=Seq(meOnly, meCommon, otherOnly, otherCommon)
    
    //val unionDoc=ind1.document().union(ind2.document())
    val oDoc1=new OrderedDocument(index1.document)
    val oDoc2=new OrderedDocument(index2.document)
    val doc1=TypePropagator.highlight(oDoc1,meOnly)
    val doc2=TypePropagator.highlight(oDoc1,meCommon)
    val doc3=TypePropagator.highlight(oDoc2,otherOnly)
    val doc4=TypePropagator.highlight(oDoc2,otherCommon)
    
    new SVGOutputer().output(index1, FileOutput(new File("target/index1.svg")),Map[String,String]())
    new SVGOutputer().output(index2, FileOutput(new File("target/index2.svg")),Map[String,String]())

    new ProvNOutputer().output(index1, FileOutput(new File("target/index1.provn")),Map[String,String]())
    new ProvNOutputer().output(index2, FileOutput(new File("target/index2.provn")),Map[String,String]())
    
    new SVGOutputer().output(doc1, FileOutput(new File("target/doc1.svg")),Map[String,String]())
    new SVGOutputer().output(doc2, FileOutput(new File("target/doc2.svg")),Map[String,String]())
    new SVGOutputer().output(doc3, FileOutput(new File("target/doc3.svg")),Map[String,String]())
    new SVGOutputer().output(doc4, FileOutput(new File("target/doc4.svg")),Map[String,String]())
    
        
    new ProvNOutputer().output(doc1, FileOutput(new File("target/doc1.provn")),Map[String,String]())
    new ProvNOutputer().output(doc2, FileOutput(new File("target/doc2.provn")),Map[String,String]())
    new ProvNOutputer().output(doc3, FileOutput(new File("target/doc3.provn")),Map[String,String]())
    new ProvNOutputer().output(doc4, FileOutput(new File("target/doc4.provn")),Map[String,String]())
        
  }
   
     
   "Renaming of summmary" should "work" in {
    val ns=new Namespace
    ns.addKnownNamespaces()
    ns.register("ex", EX_NS)
    
    val (ind1,s1)=sum("""
                    document
                    prefix ex <http://example.org/>
                    entity(ex:e2)
                    entity(ex:e1)
                    entity(ex:e0)
                    activity(ex:a)
                    wasGeneratedBy(ex:e2,ex:a,-)
                    wasDerivedFrom(ex:e2,ex:e1)
                    wasDerivedFrom(ex:e1,ex:e0)
                    used(ex:a,ex:e0,-)
                    endDocument
                    """)
                    
      
    val index1=s1.getConstructorForLevel(2,nsBase, PrettyMethod.Name,true,true,false).makeIndex()

       
    val (ind2,s2)=sum("""
                    document
                    prefix ex <http://example.org/>
                    entity(ex:e2)
                    activity(ex:a)
                    wasGeneratedBy(ex:e2,ex:a,-)
                    wasDerivedFrom(ex:e2,ex:e1)
                    wasDerivedFrom(ex:e1,ex:e0)
                    used(ex:a,ex:e0,-)
                    agent(ex:ag)
                    wasAttributedTo(ex:e2,ex:ag)
                    endDocument
                    """)



     val index2=s2.getConstructorForLevel(2,nsBase, PrettyMethod.Name,true,true,false).makeIndex()

     val index3=Merge.renameOther(index1,index2, PrettyMethod.Name)

     new ProvNOutputer().output(index3,new FileOutput(new File("target/index3.provn")),Map[String,String]())

     new SVGOutputer().output(index3,new FileOutput(new File("target/index3.svg")),Map[String,String]())

     val outw=new BufferedWriter(new FileWriter("target/index3.json"))

     index3.exportToJsonDescription(outw)

    
   }

  "summaryTypesNames" should "exist" in {
    new SummaryTypesNames{} .name_order_map should not be null
  }

}
