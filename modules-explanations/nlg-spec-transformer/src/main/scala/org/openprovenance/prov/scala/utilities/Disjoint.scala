package org.openprovenance.prov.scala.utilities

case class OrType[A,B](a: Option[A], b: Option[B]) {
}

object OrType {
  import scala.language.implicitConversions

  // namespace pollution?  Maybe use "||" or "OrType"
  type or[A,B] = OrType[A,B]

  // convenience of definition functions
  private def da[A,B](a: A): or[A,B] = { new OrType(Some(a),None) }
  private def db[A,B](b: B): or[A,B] = { new OrType(None,Some(b)) }
  private def na[A,B](n: Nothing): or[A,B] = { new OrType(None, None) }

  // implicit defs - stuttering-or
  implicit def noneToOr2[A,B](n: Nothing): or[A,B] =
  { na(n) }
  implicit def aToOr2[A,B](a: A): or[A,B] =
  { da(a) }
  implicit def bToOr2[A,B](b: B): or[A,B] =
  { db(b) }
  implicit def aToOr3[A,B,C](a: A): or[or[A,B],C] =
  { da(da(a)) }
  implicit def bToOr3[A,B,C](b: B): or[or[A,B],C] =
  { da(db(b)) }
  implicit def aToOr4[A,B,C,D](a: A): or[or[or[A,B],C],D] =
  { da(da(da(a))) }
  implicit def bToOr4[A,B,C,D](b: B): or[or[or[A,B],C],D] =
  { da(da(db(b))) }
  implicit def aToOr5[A,B,C,D,E](a: A): or[or[or[or[A,B],C],D],E] =
  { da(da(da(da(a)))) }
  implicit def bToOr5[A,B,C,D,E](b: B): or[or[or[or[A,B],C],D],E] =
  { da(da(da(db(b)))) }
  // more? ...

}

import org.openprovenance.prov.scala.utilities.OrType.or


class Foo {

  def bar[T <% Int or String or Double](t: Option[T]): Unit = {
    t match {
      case Some(x: Int) => println("processing Int: " + x)
      case Some(x: String) => println("processing String: " + x)
      case Some(x: Double) => println("processing Double: " + x)
      case None => println("empty and I don't care the type")
      case Some(other) => throw new UnsupportedOperationException
    }
  }

  def foo[T <% Int or String or Double](t: T): Unit = {
    t match {
      case x: Int => println("processing Int: " + x)
      case x: String => println("processing String: " + x)
      case x: Double => println("processing Double: " + x)
      case  _ => println("empty and I don't care the type")
    }
  }

  def baz[T <% String or Int](t: List[T]): Unit = {
    for (x <- t) x match {
      case x: String => println("String list item: " + x)
      case x: Int => println("Int list item: " + x)
    }
  }
}

object Foo {

  val f = new Foo

  f.bar(None)
  f.bar(Some(1))
  f.bar(Some("blah"))
  f.bar(Some(3.45))
  // f.bar(Some(Some(2))) // compiler error
  // f.bar(Some(Set("x", "y"))) // compiler error

  f.baz(List(1,1,2,3,5,8,13))
  f.baz(List("boogie", "woogie"))
  // f.baz(List(3.4, 3.14)) // compiler error
  // f.baz(List(1,"one")) // compiler error
  // f.baz(Some(1)) // compiler error
}

/*


The only difference is that we now don't require clarifying the type for a None, and the class name more accurately reflects what it does.

UPDATE: Per Ittay's suggestion below, I've updated the implementation to use Either as the backing class (shown below). This simplifies the code and allows extraction of values from the composite type via pattern matching. Also, it can no longer accurately be described as "stuttering-or".


package org.okcjug.imports

object DisjointBoundedView {
  // namespace pollution?  Maybe use "||" or "OrType"
  type or[A,B] = Either[A,B]

  // implicit defs
  implicit def l[T](t: T) = Left(t)
  implicit def r[T](t: T) = Right(t)
  implicit def ll[T](t: T) = Left(Left(t))
  implicit def lr[T](t: T) = Left(Right(t))
  implicit def lll[T](t: T) = Left(Left(Left(t)))
  implicit def llr[T](t: T) = Left(Left(Right(t)))
  implicit def llll[T](t: T) = Left(Left(Left(Left(t))))
  implicit def lllr[T](t: T) = Left(Left(Left(Right(t))))
  // more? ...

}

*/