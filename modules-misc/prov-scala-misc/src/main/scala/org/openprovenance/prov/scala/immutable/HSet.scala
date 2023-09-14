package org.openprovenance.prov.scala.immutable

import scala.collection.GenTraversableOnce
import scala.collection.generic.CanBuildFrom

object HSet {
  def apply[A](elems: A*) = new HSet(Set(elems: _*))
  def empty[A] = new HSet(Set.empty[A])
}

class HSet [A](set: Set[A]) extends Set[A] {
  def +(elem: A) = new HSet(set + elem)
  def -(elem: A) = new HSet(set - elem)
  def contains(elem: A) = set.contains(elem)
  def iterator = set.iterator
  override lazy val hashCode = set.hashCode
  
  
  override def ++(elems: GenTraversableOnce[A]): HSet[A] = {
    new HSet(set ++ elems)
  }
  
  override def flatMap[B, That](f: (A) ⇒ GenTraversableOnce[B])(implicit bf: CanBuildFrom[Set[A], B, That]): That = {
    val res:That=set.flatMap(f)
    res
  }
  
  def flatMap2[B](f: (A) ⇒ GenTraversableOnce[B]): HSet[B] = {
    new HSet(set.flatMap(f))
  }
  
  def getSet() = set
  
 
  //def canEqual(a: Any) = a.isInstanceOf[HSet[A]]

  override def equals(that: Any): Boolean =
    that match {
      case that: HSet[A] => that.canEqual(this) &&
        this.hashCode == that.hashCode &&
        this.set == that.getSet
      case _ => false
    }

}
