
package org.openprovenance.prov.scala

import org.scalatest._
import org.scalatest.matchers.should.Matchers
import org.openprovenance.prov.scala.mutable.ProvFactory
import org.openprovenance.prov.scala.mutable.QualifiedName
import org.openprovenance.prov.scala.immutable.Indexer
import org.openprovenance.prov.scala.immutable.Document
import org.scalatest.flatspec.AnyFlatSpec


abstract class IndexerSpec extends AnyFlatSpec with Matchers {
  val EX_NS="http://example.org/"
  val pf=new ProvFactory
  val ipf=new org.openprovenance.prov.scala.immutable.ProvFactory

  def q(local: String): QualifiedName = {
    val res=new QualifiedName
    res.localPart=local
    res.namespaceURI=EX_NS
    res.prefix="ex"
    res
  }


  
  def loadDoc(file: String):Indexer = {
    val u = new org.openprovenance.prov.notation.Utility();
    val doc = u.readDocument(file, ipf);
    val ind=new Indexer(doc.asInstanceOf[Document])   
    ind
  }
  
   "pc1" should "sort nodes" in {
     val ind=loadDoc("src/test/resources/validate/pc1-full.provn")
     val order1=ind.sortQualifiedNames()
     val order2=ind.sortQualifiedNames(ind.pred)

     println(order1)
     //order1 should be (order2.reverse)

     //order1.head should be (ind.sinks)
     //order1.last should be (ind.sources)
     //order1  should be (Seq())
     
     //order2.head should be (ind.sources)
     //order2.last should be (ind.sinks)

   }


  "risk1" should "sort nodes" in {
     val ind=loadDoc("src/test/resources/factorgraph/Risk_Prov_FG1.provn")
     val order1=ind.sortQualifiedNames()
     val order2=ind.sortQualifiedNames(ind.pred)

     println(order1)
     println(order2)

   }


}
