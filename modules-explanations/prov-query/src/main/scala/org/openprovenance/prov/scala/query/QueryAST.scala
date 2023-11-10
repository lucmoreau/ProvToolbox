
package org.openprovenance.prov.scala.query

import org.openprovenance.prov.scala.query.QueryAST.Schema


object QueryAST {
  type Table
  type Schema = Vector[String]
  def toSchema(schema: String*): Schema = schema.toVector
}


// relational algebra ops
sealed abstract class Operator

case class Scan(statementType: String, schema: Schema, document: Option[String]) extends Operator
case class Print(parent: Operator) extends Operator
case class Project(outSchema: Schema, inSchema: Schema, parent: Operator) extends Operator
case class Filter(pred: Predicate, parent: Operator) extends Operator
case class Join(parent1: Operator, field1: String, property1: String, parent2: Operator, field2: String, property2: String) extends Operator
case class Group(keys: Schema, agg: Schema, parent: Operator, kind: String, ref: Option[Ref]) extends Operator
case class HashJoin(parent1: Operator, parent2: Operator) extends Operator
case class LeftJoin(parent1: Operator, field1: String, property1: String, parent2: Operator, field2: String, property2: String) extends Operator
// case class Order(field: Ref, parent: Operator, kind: String) extends Operator


// filter predicates
sealed abstract class Predicate
case class Eq(pred: String, a: Ref, b: Ref) extends Predicate
case class EqL(pred: String, a: Ref, b: String) extends Predicate
case class OrPred(pred1: Predicate, pred2: Predicate) extends Predicate

sealed abstract class Ref
case class Field(name: String, field: String) extends Ref
case class Property(name: String, property: String) extends Ref
case class Value(x: Object) extends Ref

