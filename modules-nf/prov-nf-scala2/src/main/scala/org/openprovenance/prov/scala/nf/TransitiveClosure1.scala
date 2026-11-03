package org.openprovenance.prov.scala.nf

import scala.annotation.tailrec

object TransitiveClosure {
    //one transitive step
	def addTransitive[A](s: Set[(A,A)]): Set[(A, A)] = {
		s ++ (for ((x1, y1) <- s; (x2, y2) <- s if y1 == x2) yield (x1, y2))
  }

  //	repeat until we don't get a bigger set
  @tailrec
  def transitiveClosure[A <: Ordered[A]](s:Set[(A,A)]):Set[(A,A)] = {
		val t = addTransitive(s)
		if (t.size == s.size) s else transitiveClosure(t)
  }
}

object SetExtractor {
  def unapplySeq[T](s: Set[T]): Option[Seq[T]] = Some(s.toSeq)
}
class TransitiveClosure[A <: Ordered[A]] {
  var set:Set[(A,A)]=Set()
  var computed=false
  var symmetric=true;

  def add2(x1:A,x2:A): Unit = {
    if (symmetric) {
      set += ((x2, x1))
    }
	  set += ((x1,x2))
    computed=false
  }
  
  def add2(ss: Set[A]): Unit = {
    if (ss.nonEmpty) {
    	ss match {
    	  case SetExtractor(x, xs @ _*) => {
    		  xs match {
    		    case Seq() => add2(x,x)
    		    case _ => xs.map((x,_)).foreach(p => add2(p._1,p._2)) 
    		  }
    	  }
    	}
    }
  }

  var closure:Set[(A,A)]=Set()
  
  def transitiveClosure(): Set[(A, A)] = {
    if (!computed) {
      closure=TransitiveClosure.transitiveClosure(set)
      computed=true
    }
    closure
  }
  
  def partition():Map[A,Set[A]] = {
    val s=transitiveClosure()
    val grp1=s.groupBy({ case (x1,x2) => x1 } ).view.mapValues(s => s.map(_._2) ).toMap
    //val grp2=s.groupBy({ case (x1,x2) => x1 } ).mapValues(s => s.map(_._2).toSeq.sortBy { x => x }.toSet )  // Set elements are ordered, ezier to read
    //val grp2=s.groupBy({ case (x1,x2) => x1 } ).mapValues(s => s.map(_._2).toSeq.sorted.toSet )  // Set elements are ordered, ezier to read
    
 //   { case (x1:A,x2:A) => x1 compare x2 })
    
    grp1
  }
  
  
}
	
