
package org.openprovenance.prov.scala

import org.scalatest._
import org.openprovenance.prov.scala.immutable.ProvFactory
import org.openprovenance.prov.model.StatementOrBundle.Kind
import java.util.LinkedList

import org.openprovenance.prov.model.Statement


class ModelSpec extends FlatSpec with Matchers {
  val EX_NS="http://example.org/"
  val pf=new ProvFactory
  val ipf=new org.openprovenance.prov.scala.immutable.ProvFactory

  def q(local: String) = {
    val res=pf.newQualifiedName(EX_NS,local,"ex")
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
  


  

    

}
