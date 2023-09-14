
package org.openprovenance.prov.scala

import collection.mutable.Stack
import org.scalatest._
import org.openprovenance.prov.scala.mutable.ProvFactory
import org.openprovenance.prov.scala.mutable.QualifiedName
import org.openprovenance.prov.model.StatementOrBundle.Kind
import org.openprovenance.prov.model.DocumentEquality
import java.util.LinkedList

import org.openprovenance.prov.model.Statement
import org.openprovenance.prov.scala.immutable.Indexer
import org.openprovenance.prov.scala.immutable.Document
//import org.openprovenance.prov.scala.summary.{CommonTypePropagator, DefaultLevel0}


class ModelSpec extends FlatSpec with Matchers {
  val EX_NS="http://example.org/"
  val pf=new ProvFactory
  val ipf=new org.openprovenance.prov.scala.immutable.ProvFactory

  def q(local: String) = {
    val res=new QualifiedName
    res.localPart=local
    res.namespaceURI=EX_NS
    res.prefix="ex"
    res
  }

  "An entity" should "have an identifier" in {



    val entity1=pf.newEntity(q("e1"))
    
    q("e2").getUri() should  be (EX_NS+"e2")
    
    entity1.getKind should be (Kind.PROV_ENTITY)
    entity1.getId() should not be (null)
    entity1.getId.getNamespaceURI() should  be (EX_NS)
    entity1.getId.getUri should  be (EX_NS+"e1")
    
    println(entity1)
  }
  
  "A generation" should "be equal to itself" in {
     
    val wgb1=pf.newWasGeneratedBy(q("id"), q("e1"),q("a1"))
    val wgb2=pf.newWasGeneratedBy(q("id"), q("e1"),q("a1"))
    
    
    
    wgb1 should be (wgb2)
    
    
    
  }
   
  "An entity" should "be equal to itself" in {
     
    val entity1=pf.newEntity(q("e1"))
    
    val entity2=pf.newEntity(q("e1"))

    val entity3=pf.newEntity(q("e3"))
    
    entity1 should be (entity1)
    entity2 should be (entity1)
    entity2 should not be (entity3)
    
    val ll1: java.util.List[Statement] = new LinkedList()
    ll1.add(entity1)
    
    val ll2: java.util.List[Statement] = new LinkedList()
    ll2.add(entity2)
    
    ll1 should be (ll2)
        
    
   }
  

    

  "A file"  should "be loadable twice" in {
    loadFromProvnTwice("src/test/resources/prov/ex0.provn",true) should be (true)
    loadFromProvnTwice("src/test/resources/prov/ex1.provn",true) should be (true)
  }
    
  "A simple file"  should "be loadable, saveable, and reloadable" in {
      loadFromProvnSaveAndReload("src/test/resources/prov/ex1.provn",true) should be (true)
  }
  
   
  "And again a simple file"  should "be loadable, saveable, and reloadable" in {
    loadFromProvnSaveAndReload("src/test/resources/prov/primer.provn",true) should be (true)
  }


  def loadFromProvnSaveAndReload(file: String, compare: Boolean):Boolean = {
    val pf=org.openprovenance.prov.scala.immutable.ProvFactory.pf //TODO: the test fails with prov/ex1.provn and the mutable factory, on the test with bundles.
    println("-------------- File: " + file)
    val u = new org.openprovenance.prov.notation.Utility()
    val de = new DocumentEquality(true, System.out)

    val doc1 = u.readDocument(file, pf)
    val file2 = file.replace('/', '_')
    u.writeDocument(doc1, "target/" + file2, pf)
    val doc2 = u.readDocument("target/" + file2, pf)

    println("read file ")
    val result = de.check(doc1, doc2)

    if (!result && compare) {
      System.out.println(doc1)
      System.out.println("------------------")
      System.out.println(doc2)
    }

    println("result is " + result)



    doc1 should be (doc2)

    
    result

  }
  
  def loadFromProvnTwice(file: String, compare: Boolean):Boolean = {
    println("----(2) ---------- File: " + file)
    val u = new org.openprovenance.prov.notation.Utility()

    val doc1 = u.readDocument( file, pf)
    val doc2 = u.readDocument( file, pf)
    
    val result=doc1==doc2
    
    if (!result && compare) {
      System.out.println(doc1)
      System.out.println("------------------")
      System.out.println(doc2)
    }

    println("result (2) is " + result)
    
    doc1 should be (doc2)

    
    result

  }

    

}
