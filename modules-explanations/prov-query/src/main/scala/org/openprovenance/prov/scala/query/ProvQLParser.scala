package org.openprovenance.prov.scala.query

import org.openprovenance.prov.model.Namespace
import org.openprovenance.prov.scala.immutable.ProvFactory.pf
import org.openprovenance.prov.scala.immutable.{ProvnCore, ProvnNamespaces, QualifiedName}
import org.parboiled2._
import shapeless.{::, HNil}



trait pqlParser   extends Parser with ProvnCore with ProvnNamespaces  {

  override def namespaceDeclarations: Rule[HNil, HNil] = rule  {	 zeroOrMore (namespaceDeclaration) ~ WS }


  def query: Rule[HNil, proc.Operator :: HNil]  =
    rule { WS ~ namespaceDeclarations ~ WS ~ selectClause }

  def selectClause: Rule[HNil, proc.Operator :: HNil]  =
    rule { "select" ~ WS ~ ( "*" ~ WS ~> makeEmptyList | fieldList) ~ WS ~ fromClause ~ whereClause ~ groupClause ~> makeSelect }

  def whereClause: Rule[proc.Operator :: HNil, proc.Operator :: HNil]  = rule { optional( "where" ~ WS ~ oneOrMore (pred).separatedBy(WS ~ "and" ~ WS) ~ WS) ~> makeWhere  }


  def fromClause: Rule[HNil, proc.Operator :: HNil] = rule { "from" ~ WS ~ tableClause ~ WS ~ optional(joinClause1)}

  def joinClause1: Rule[proc.Operator :: HNil, proc.Operator :: HNil] = rule { "from" ~ WS ~ oneOrMore (tableJoinClause).separatedBy(WS ~ "from" ~ WS) }

  def tableClause: Rule[HNil, proc.Operator :: HNil] = rule {
    ( fieldIdent ~ WS ~ "a" ~ WS ~ identifier ~ WS ~ optional ("in" ~ WS ~ "document" ~ WS ~ fieldIdent ~ WS ) ~> makeScan |
      "(" ~ WS ~ selectClause ~ WS ~ ")" )}


  def groupClause: Rule[proc.Operator :: HNil, proc.Operator :: HNil] = rule {
    optional("group" ~ WS ~ "by" ~ WS ~ fieldIdList ~ WS ~ "aggregate" ~ WS ~ fieldIdList
      ~ WS ~ "with" ~ WS ~ (capture("Seq")  | capture("Count") )~ WS ~ optional("order" ~ WS ~ "by" ~ WS ~ ref ~ WS ) ~> makeGroup)
  }

//  def orderClause: Rule[proc.Operator :: HNil, proc.Operator :: HNil] = rule {
 //   optional("order" ~ WS ~ "by" ~ WS ~ ref ~ WS ~> makeOrder)
 // }

  def tableJoinClause: Rule[proc.Operator :: HNil, proc.Operator :: HNil] =
    rule { tableClause ~ WS ~ ("join" ~ WS ~ joinClause2 ~> makeTableJoin | "ljoin" ~ WS ~ joinClause2 ~> makeTableLJoin)}

  def pred: Rule[HNil, proc.Predicate :: HNil] = rule {
      ref  ~ ( ">=" ~ WS ~ ref ~> makeIncludes ~ WS |
               "!>=" ~ WS ~ ref ~> makeIncludesNot ~ WS |
               "="  ~ WS ~ ( ref  ~> makeEq | int_literal  ~> makeEqL)  ~ WS |
               "exists" ~ WS ~> makeExists ~ WS ) ~ optional ( "or" ~ WS ~ pred ~> makeOr )}


  def joinClause2: Rule[HNil, (proc.Ref, proc.Ref) :: HNil] = rule {
    ref  ~ WS ~ "=" ~ WS ~ ref ~> makeJoinPair ~ WS }


  def identifier: Rule[HNil, QualifiedName :: HNil] = qualified_name

  def fieldIdent: Rule[HNil, String :: HNil] = rule { capture (pn_local_no_dot) ~> makeText }

  def fieldList: Rule[HNil, Seq[(String,Option[String])] :: HNil] = rule { oneOrMore(fieldIdent ~ optional(WS ~ "as" ~ WS ~ fieldIdent) ~> makeFieldIdentPair).separatedBy(WS ~ "," ~ WS)}

  def fieldIdList: Rule[HNil, Seq[String] :: HNil] = rule { oneOrMore(fieldIdent).separatedBy(WS ~ "," ~ WS)}

  def ref: Rule[HNil, pqlParser.this.proc.Ref :: HNil] = rule {
    (fieldIdent ~ WS ~ ( "[" ~ WS ~ identifier ~> makeProperty ~ WS ~ "]" ~ WS   |
                        "." ~ WS ~ fieldIdent ~> makeField ~ WS ) ) |
    '\'' ~ qualified_name ~ '\'' ~> makeValue ~ WS
    /*|
      """'[^']*'""".r ^^ (s => Value(s.drop(1).dropRight(1))) */ /* |
      """[0-9]+""".r ^^ (s => Value(s.toInt))*/
  }

  def int_literal = rule { capture ( optional ( marker ) ~ oneOrMore ( CharPredicate.Digit ) ) ~> makeText ~ WS }

  private val marker: CharPredicate = CharPredicate('-')


  //def pn_local_no_dot = rule { ( pn_chars_u | CharPredicate.Digit | pn_chars_others ) ~
  //  zeroOrMore ( pn_chars |  pn_chars_others )     }

  def makeFieldIdentPair: (String, Option[String]) => (String, Option[String]) = (s1,s2) => (s1,s2)
  def makeEmptyList: () => Seq[(String,Option[String])] = () => Seq()

  val proc: Processor

  def makeSelect: (Seq[(String,Option[String])],proc.Operator) => proc.Operator
  def makeScan1: (String, QualifiedName ) => proc.Operator = (s,q) => makeScan(s,q,None)
  def makeScan2: (String, QualifiedName, String ) => proc.Operator = (s,q,d) => makeScan(s,q,Some(d))
  def makeScan: (String, QualifiedName,Option[String] ) => proc.Operator
  def makeProperty: (String, QualifiedName)  => proc.Ref
  def makeField: (String ,String) => proc.Ref
  def makeValue: (QualifiedName)  => proc.Ref

  def makeWhere:  (proc.Operator, Option[Seq[proc.Predicate]]) => proc.Operator

  def makeEq: (proc.Ref, proc.Ref) => proc.Predicate
  def makeEqL: (proc.Ref, String) => proc.Predicate

  def makeIncludes: (proc.Ref, proc.Ref) => proc.Predicate
  def makeIncludesNot: (proc.Ref, proc.Ref) => proc.Predicate

  def makeExists: proc.Ref  => proc.Predicate

  def makeJoinPair: (proc.Ref,proc.Ref) => (proc.Ref,proc.Ref)

  def makeTableJoin: (proc.Operator, proc.Operator,(proc.Ref,proc.Ref)) => proc.Operator

  def makeTableLJoin: (proc.Operator, proc.Operator,(proc.Ref,proc.Ref)) => proc.Operator

  def makeJoin1:(proc.Operator, Seq[proc.Operator]) => proc.Operator

  def makeGroup:(proc.Operator, Seq[String], Seq[String], String, Option[proc.Ref]) => proc.Operator

  //def makeOrder:(proc.Operator, proc.Ref) => proc.Operator

  def makeOr: (proc.Predicate,proc.Predicate) => proc.Predicate = (p,q) => proc.OrPred(p,q)


}

class ProvQLParser (override val proc: Processor, override val input: ParserInput, val ns: Namespace) extends  pqlParser {
  override def theNamespace: () => Namespace = () => ns

  override val makeText: String => String =  (text: String) => text

  override val makeQualifiedName: String => QualifiedName = (s:String) => QualifiedName {
    theNamespace().stringToQualifiedName(s,pf).asInstanceOf[QualifiedName]
  }

  override def makeSelect: (Seq[(String,Option[String])],proc.Operator) => proc.Operator =
    (ss,op) =>  if (ss.isEmpty) op else {
      val (fs,fs1) = ss.map { case (a,b) => (b.getOrElse(a),a) }.unzip
      proc.Project(proc.Schema(fs:_*),proc.Schema(fs1:_*),op)
    }

  override def makeScan: (String, QualifiedName, Option[String]) => proc.Operator = {
    (f,q,d) => proc.Scan(q.toString(), proc.Schema(f),d): proc.Operator
  }


  override def makeProperty: (String, QualifiedName) => proc.Ref = (s,q) => proc.Property(s,q.toString())

  override def makeField: (String, String) => proc.Ref = (s1,s2) => proc.Field(s1,s2)

  override def makeValue: (QualifiedName) => proc.Ref = q => proc.Value(q.toString())

  override def makeIncludes: (proc.Ref, proc.Ref) => proc.Predicate = (ref1,ref2) => proc.Eq("includesQualifiedName",ref1,ref2)

  override def makeIncludesNot: (proc.Ref, proc.Ref) => proc.Predicate = (ref1,ref2) => proc.Eq("includesQualifiedNameNot",ref1,ref2)

  override def makeEq: (proc.Ref, proc.Ref) => proc.Predicate = (ref1,ref2) => proc.Eq("equals",ref1,ref2)
  override def makeEqL: (proc.Ref, String) => proc.Predicate = (ref1,ref2) => proc.EqL("equals",ref1,ref2)

  override def makeExists: proc.Ref => proc.Predicate = ref => proc.Eq("exists",ref, null)

  override def makeWhere: (proc.Operator, Option[Seq[proc.Predicate]]) => proc.Operator = (op, x) =>  x match {
    case None => op
    case Some(pred) => pred.foldLeft(op)((o,p) => proc.Filter(p, o))
  }

  def makeJoinPair_old: (proc.Ref,proc.Ref) => (proc.Field,proc.Field) = (a, b)=>(a.asInstanceOf[proc.Field],b.asInstanceOf[proc.Field])
  override def makeJoinPair: (proc.Ref,proc.Ref) => (proc.Ref,proc.Ref) = (a, b)=>(a,b)

  override def makeTableJoin: (proc.Operator, proc.Operator,(proc.Ref,proc.Ref)) => proc.Operator =
    (op1,op2,pair) => pair match {
      case (field1:proc.Field, field2:proc.Field) => proc.Join(op1,field1.name,field1.field, op2,  field2.name,field2.field)
      case (field1:proc.Property, field2:proc.Field) => proc.Join(op1,field1.name,field1.property, op2,  field2.name,field2.field)
      case (field1:proc.Field, field2:proc.Property) => proc.Join(op1,field1.name,field1.field, op2,  field2.name,field2.property)
      case (field1:proc.Property, field2:proc.Property) => proc.Join(op1,field1.name,field1.property, op2,  field2.name,field2.property)
    }


  override def makeTableLJoin: (proc.Operator, proc.Operator,(proc.Ref,proc.Ref)) => proc.Operator =
    (op1,op2,pair) => pair match {
      case (field1:proc.Field, field2:proc.Field) => proc.LeftJoin(op1,field1.name,field1.field, op2,  field2.name,field2.field)
    }

  override def makeJoin1:(proc.Operator, Seq[proc.Operator]) => proc.Operator =
    (op, ops) => op


  override def makeGroup:(proc.Operator, Seq[String], Seq[String], String, Option[proc.Ref]) => proc.Operator = (op,keys,agg,kind,ref) => proc.Group(proc.Schema(keys:_*),proc.Schema(agg:_*),op, kind, ref)



}
