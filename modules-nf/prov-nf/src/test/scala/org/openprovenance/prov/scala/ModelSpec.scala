
package org.openprovenance.prov.scala


import org.openprovenance.prov.model.DocumentEquality
import org.openprovenance.prov.scala.immutable.{Document, Indexer, ProvFactory, QualifiedName}
import org.scalatest._


class ModelSpec extends FlatSpec with Matchers {
  val EX_NS="http://example.org/"
  val pf= ProvFactory.pf
  val ipf=ProvFactory.pf

  def q(local: String) = {
    val res=new QualifiedName("ex", local, EX_NS)

    res
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
  
  "A file"  should "be loadable with the immutable factory" in {
    //loadFromProvnTwice3("prov/ex0.provn",true) should be (true)
    loadFromProvnTwice3("src/test/resources/prov/primer.provn",true) should be (true)
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

    
  

  def loadFromProvnTwice3(file: String, compare: Boolean):Boolean = {
    println("++++++++++++++++  File: " + file)
    val u = new org.openprovenance.prov.notation.Utility()

    val doc1 = u.readDocument( file, ipf)
    val doc2 = u.readDocument(file, ipf)
    
    val result=doc1==doc2
    
    if (!result && compare) {
      System.out.println(doc1)
      System.out.println("------------------")
      System.out.println(doc2)
    }

    println("result (2) is " + result)
    
    doc1 should be (doc2)
    

    val file2 = file.replace('/', '_');
    u.writeDocument(doc1, "target/immutable_" + file2, ipf);
    
    println(doc1.toString())
    
    val ind=new Indexer(doc1.asInstanceOf[Document])
    
    //new CommonTypePropagator(doc1.asInstanceOf[Document], new DefaultLevel0, true).provtypeZero(ind)
    
    result

  }


}
