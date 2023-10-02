package org.openprovenance.prov.scala.query

import org.openprovenance.prov.model.Namespace
import org.openprovenance.prov.scala.immutable.ProvFactory.pf
import org.openprovenance.prov.scala.immutable.{ProvnCore, ProvnNamespaces, QualifiedName}
import org.openprovenance.prov.scala.query.QueryAST.toSchema
import org.parboiled2._
import org.parboiled2.support.hlist._


trait PQLParser   extends Parser with ProvnCore with ProvnNamespaces  {

  override def namespaceDeclarations: Rule[HNil, HNil] = rule  {	 zeroOrMore (namespaceDeclaration) ~ WS }


  def query: Rule[HNil, Operator :: HNil]  =
    rule { WS ~ namespaceDeclarations ~ WS ~ selectClause }

  def selectClause: Rule[HNil, Operator :: HNil]  =
    rule { "select" ~ WS ~ ( "*" ~ WS ~> makeEmptyList | fieldList) ~ WS ~ fromClause ~ whereClause ~ groupClause ~> makeSelect }

  def whereClause: Rule[Operator :: HNil, Operator :: HNil]  = rule { optional( "where" ~ WS ~ oneOrMore (pred).separatedBy(WS ~ "and" ~ WS) ~ WS) ~> makeWhere  }


  def fromClause: Rule[HNil, Operator :: HNil] = rule { "from" ~ WS ~ tableClause ~ WS ~ optional(joinClause1)}

  def joinClause1: Rule[Operator :: HNil, Operator :: HNil] = rule { "from" ~ WS ~ oneOrMore (tableJoinClause).separatedBy(WS ~ "from" ~ WS) }

  def tableClause: Rule[HNil, Operator :: HNil] = rule {
    fieldIdent ~ WS ~ "a" ~ WS ~ identifier ~ WS ~ optional ("in" ~ WS ~ "document" ~ WS ~ fieldIdent ~ WS ) ~> makeScan |
      "(" ~ WS ~ selectClause ~ WS ~ ")"
  }


  def groupClause: Rule[Operator :: HNil, Operator :: HNil] = rule {
    optional("group" ~ WS ~ "by" ~ WS ~ fieldIdList ~ WS ~ "aggregate" ~ WS ~ fieldIdList
      ~ WS ~ "with" ~ WS ~ (capture("Seq")  | capture("Count") )~ WS ~ optional("order" ~ WS ~ "by" ~ WS ~ ref ~ WS ) ~> makeGroup)
  }

//  def orderClause: Rule[Operator :: HNil, Operator :: HNil] = rule {
 //   optional("order" ~ WS ~ "by" ~ WS ~ ref ~ WS ~> makeOrder)
 // }

  def tableJoinClause: Rule[Operator :: HNil, Operator :: HNil] =
    rule { tableClause ~ WS ~ ("join" ~ WS ~ joinClause2 ~> makeTableJoin | "ljoin" ~ WS ~ joinClause2 ~> makeTableLJoin)}

  def pred: Rule[HNil, Predicate :: HNil] = rule {
      ref  ~ ( ">=" ~ WS ~ ref ~> makeIncludes ~ WS |
               "!>=" ~ WS ~ ref ~> makeIncludesNot ~ WS |
               "="  ~ WS ~ ( ref  ~> makeEq | int_literal  ~> makeEqL)  ~ WS |
               "exists" ~ WS ~> makeExists ~ WS ) ~ optional ( "or" ~ WS ~ pred ~> makeOr )}


  def joinClause2: Rule[HNil, (Ref, Ref) :: HNil] = rule {
    ref  ~ WS ~ "=" ~ WS ~ ref ~> makeJoinPair ~ WS }


  def identifier: Rule[HNil, QualifiedName :: HNil] = qualified_name

  def fieldIdent: Rule[HNil, String :: HNil] = rule { capture (pn_local_no_dot) ~> makeText }

  def fieldList: Rule[HNil, Seq[(String,Option[String])] :: HNil] = rule { oneOrMore(fieldIdent ~ optional(WS ~ "as" ~ WS ~ fieldIdent) ~> makeFieldIdentPair).separatedBy(WS ~ "," ~ WS)}

  def fieldIdList: Rule[HNil, Seq[String] :: HNil] = rule { oneOrMore(fieldIdent).separatedBy(WS ~ "," ~ WS)}

  def ref: Rule[HNil, Ref :: HNil] = rule {
    (fieldIdent ~ WS ~ ( "[" ~ WS ~ identifier ~> makeProperty ~ WS ~ "]" ~ WS   |
                        "." ~ WS ~ fieldIdent ~> makeField ~ WS ) ) |
    '\'' ~ qualified_name ~ '\'' ~> makeValue ~ WS
    /*|
      """'[^']*'""".r ^^ (s => Value(s.drop(1).dropRight(1))) */ /* |
      """[0-9]+""".r ^^ (s => Value(s.toInt))*/
  }

  def int_literal: Rule[HNil, String :: HNil] = rule { capture ( optional ( marker ) ~ oneOrMore ( CharPredicate.Digit ) ) ~> makeText ~ WS }

  private val marker: CharPredicate = CharPredicate('-')


  //def pn_local_no_dot = rule { ( pn_chars_u | CharPredicate.Digit | pn_chars_others ) ~
  //  zeroOrMore ( pn_chars |  pn_chars_others )     }

  def makeFieldIdentPair: (String, Option[String]) => (String, Option[String]) = (s1,s2) => (s1,s2)
  def makeEmptyList: () => Seq[(String,Option[String])] = () => Seq()

  val proc: Processor

  def makeSelect: (Seq[(String,Option[String])],Operator) => Operator

  def makeScan: (String, QualifiedName,Option[String] ) => Operator
  def makeProperty: (String, QualifiedName)  => Ref
  def makeField: (String ,String) => Ref
  def makeValue: QualifiedName  => Ref

  def makeWhere:  (Operator, Option[Seq[Predicate]]) => Operator

  def makeEq: (Ref, Ref) => Predicate
  def makeEqL: (Ref, String) => Predicate

  def makeIncludes: (Ref, Ref) => Predicate
  def makeIncludesNot: (Ref, Ref) => Predicate

  def makeExists: Ref  => Predicate

  def makeJoinPair: (Ref,Ref) => (Ref,Ref)

  def makeTableJoin: (Operator, Operator,(Ref,Ref)) => Operator

  def makeTableLJoin: (Operator, Operator,(Ref,Ref)) => Operator

  def makeJoin1:(Operator, Seq[Operator]) => Operator

  def makeGroup:(Operator, Seq[String], Seq[String], String, Option[Ref]) => Operator

  //def makeOrder:(Operator, Ref) => Operator

  def makeOr: (Predicate,Predicate) => Predicate = (p,q) => OrPred(p,q)


}

class ProvQLParser (override val proc: Processor, override val input: ParserInput, val ns: Namespace) extends  PQLParser {
  override def theNamespace: () => Namespace = () => ns

  override val makeText: String => String =  (text: String) => text

  override val makeQualifiedName: String => QualifiedName = (s:String) => QualifiedName {
    theNamespace().stringToQualifiedName(s,pf).asInstanceOf[QualifiedName]
  }

  override def makeSelect: (Seq[(String,Option[String])],Operator) => Operator =
    (ss,op) =>  if (ss.isEmpty) op else {
      val (fs,fs1) = ss.map { case (a,b) => (b.getOrElse(a),a) }.unzip
      Project(toSchema(fs:_*), toSchema(fs1:_*),op)
    }

  override def makeScan: (String, QualifiedName, Option[String]) => Operator = {
    (f,q,d) => Scan(q.toString(), toSchema(f),d): Operator
  }


  override def makeProperty: (String, QualifiedName) => Ref = (s,q) => Property(s,q.toString())

  override def makeField: (String, String) => Ref = (s1,s2) => Field(s1,s2)

  override def makeValue: QualifiedName => Ref = q => Value(q.toString())

  override def makeIncludes: (Ref, Ref) => Predicate = (ref1,ref2) => Eq("includesQualifiedName",ref1,ref2)

  override def makeIncludesNot: (Ref, Ref) => Predicate = (ref1,ref2) => Eq("includesQualifiedNameNot",ref1,ref2)

  override def makeEq: (Ref, Ref) => Predicate = (ref1,ref2) => Eq("equals",ref1,ref2)
  override def makeEqL: (Ref, String) => Predicate = (ref1,ref2) => EqL("equals",ref1,ref2)

  override def makeExists: Ref => Predicate = ref => Eq("exists",ref, null)

  override def makeWhere: (Operator, Option[Seq[Predicate]]) => Operator = (op, x) =>  x match {
    case None => op
    case Some(pred) => pred.foldLeft(op)((o,p) => Filter(p, o))
  }

  override def makeJoinPair: (Ref,Ref) => (Ref,Ref) = (a, b)=>(a,b)

  override def makeTableJoin: (Operator, Operator,(Ref,Ref)) => Operator =
    (op1,op2,pair) => pair match {
      case (field1:Field, field2:Field) => Join(op1,field1.name,field1.field, op2,  field2.name,field2.field)
      case (field1:Property, field2:Field) => Join(op1,field1.name,field1.property, op2,  field2.name,field2.field)
      case (field1:Field, field2:Property) => Join(op1,field1.name,field1.field, op2,  field2.name,field2.property)
      case (field1:Property, field2:Property) => Join(op1,field1.name,field1.property, op2,  field2.name,field2.property)
      case _ => throw new IllegalStateException("illegal case")
    }


  override def makeTableLJoin: (Operator, Operator,(Ref,Ref)) => Operator =
    (op1,op2,pair) => pair match {
      case (field1:Field, field2:Field) => LeftJoin(op1,field1.name,field1.field, op2,  field2.name,field2.field)
      case _ => throw new IllegalStateException("illegal case")
    }

  override def makeJoin1:(Operator, Seq[Operator]) => Operator =
    (op, _) => op


  override def makeGroup:(Operator, Seq[String], Seq[String], String, Option[Ref]) => Operator = (op,keys,agg,kind,ref) => Group(toSchema(keys:_*),toSchema(agg:_*),op, kind, ref)



}
