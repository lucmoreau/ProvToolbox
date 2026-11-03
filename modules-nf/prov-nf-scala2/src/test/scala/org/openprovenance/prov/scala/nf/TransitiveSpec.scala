


package org.openprovenance.prov.scala.nf

import org.openprovenance.prov.scala.immutable.QualifiedName
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers


class TransitiveSpec extends AnyFlatSpec with Matchers {

  def q(s: String) = {
    new QualifiedName("ex", s, "http://example.org/")
  }
  "tc1" should "check" in {
    val tc1=new TransitiveClosure2[QualifiedName]()
    tc1.add2(Set(q("a")))
    tc1.add2(Set(q("b")))
    tc1.add2(Set(q("c")))
    tc1.add2(Set(q("d")))
    
    val tc2=tc1.duplicate()
    
    val map1=tc1.transitiveClosure(false)
    val map2=tc2.transitiveClosure(true)
    
    map1.forall{case (q,s) => s==Set(q)} should be (true)
    map1 should be (map2)
    
  }
  
  "tc2" should "check" in {
    val tc1=new TransitiveClosure2[QualifiedName]()
    tc1.add2(Set(q("a")))
    tc1.add2(Set(q("b"),q("a")))
    tc1.add2(Set(q("c")))
    tc1.add2(Set(q("d"),q("c")))
    
    val tc2=tc1.duplicate()
    
    val map1=tc1.transitiveClosure(false)
    val map2=tc2.transitiveClosure(true)
    
    map1 should be (map2)
    
  }

  
   "tc3" should "check" in {
    val tc1=new TransitiveClosure2[QualifiedName]()
    tc1.add2(Set(q("a")))
    tc1.add2(Set(q("b"),q("a")))
    tc1.add2(Set(q("c"),q("a")))
    tc1.add2(Set(q("d"),q("c")))
    
    val tc2=tc1.duplicate()

    
    val map1=tc1.transitiveClosure(false)
    val map2=tc2.transitiveClosure(true)
    
    println(map1)    
    println(map2)
    //println("tc1")
    //tc1.showds(tc1.ds)
    //println("tc2")
    //tc2.showds(tc2.ds)
    
    map1 should be (map2)

  }
  
   "tc4" should "check" in {
    val tc1=new TransitiveClosure2[QualifiedName]()
    tc1.add2(Set(q("a")))
    tc1.add2(Set(q("b")))
    tc1.add2(Set(q("c"),q("a")))
    tc1.add2(Set(q("d"),q("c"),q("e")))
    
    val tc2=tc1.duplicate()

    
    val map1=tc1.transitiveClosure(false)
    val map2=tc2.transitiveClosure(true)
    
    println(map1)    
    println(map2)
    println("tc1")
    tc1.showds(tc1.ds)
    println("tc2")
    tc2.showds(tc2.ds)
    
    map1 should be (map2)

  }

  
   "tc5" should "check" in {
    val tc1=new TransitiveClosure2[QualifiedName]()
    tc1.add2(Set(q("a")))
    tc1.add2(Set(q("b")))
    tc1.add2(Set(q("c"),q("a")))
    tc1.add2(Set(q("d"),q("c"),q("e")))
    tc1.add2(Set(q("b"),q("e")))

    val tc2=tc1.duplicate()

    
    val map1=tc1.transitiveClosure(false)
    val map2=tc2.transitiveClosure(true)
    
    println(map1)    
    println(map2)
    println("tc1")
    tc1.showds(tc1.ds)
    println("tc2")
    tc2.showds(tc2.ds)
    
    map1 should be (map2)

  }

   "tc6" should "check" in {
    val tc1=new TransitiveClosure2[QualifiedName]()
    tc1.add2(Set(q("a")))
    tc1.add2(Set(q("b")))
    tc1.add2(Set(q("c"),q("a")))
    tc1.add2(Set(q("d"),q("c"),q("e")))
    tc1.add2(Set(q("e"),q("b")))

    val tc2=tc1.duplicate()

    
    val map1=tc1.transitiveClosure(false)
    val map2=tc2.transitiveClosure(true)
    
    println(map1)    
    println(map2)
    println("tc1")
    tc1.showds(tc1.ds)
    println("tc2")
    tc2.showds(tc2.ds)
    
    map1 should be (map2)

  }


}