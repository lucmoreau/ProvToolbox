package org.openprovenance.prov.scala.query

import org.openprovenance.prov.scala.immutable
import org.openprovenance.prov.scala.immutable.{ActedOnBehalfOf, Activity, Agent, AlternateOf, Document, Entity, HadMember, Kind, SpecializationOf, Statement, Used, WasAssociatedWith, WasAttributedTo, WasDerivedFrom, WasEndedBy, WasGeneratedBy, WasInvalidatedBy, WasStartedBy}
import org.openprovenance.prov.scala.nf.TransitiveClosure
import org.openprovenance.prov.scala.utilities.{WasDerivedFromPlus, WasDerivedFromStar}

class StatementIndexer (doc:Document) extends StatementAccessor {

  val idx: Map[Kind.Value, List[Statement]] = StatementIndexer.splitByStatementType(doc.statements().toSeq)


  def findStatement(type_string: String): List[Statement] = {
    val kind = QuerySetup.nameMapper(type_string)
    if (kind == Kind.winfl) {
      type_string match {
        case "provext:WasDerivedFromPlus" => allDerivationsPlus
        case "provext:WasDerivedFromStar" => allDerivationsStar
      }

    } else {
      idx(kind)
    }
  }


  val (allDerivationsPlus, allDerivationsStar): (List[WasDerivedFromPlus], List[WasDerivedFromStar]) = StatementIndexer.computeDerivationClosure(idx)

}

object StatementIndexer {

  def enumerateAllCombinations[A](statements: List[(String, List[A])]): List[Map[String, A]] = {
    statements match {
      case List() => List()
      case List((str,seq)) => seq.map(s => Map(str -> s))
      case (str,seq) :: tail  => enumerateAllCombinations(tail).flatMap(amap => seq.map(s => amap + (str -> s)))
    }
  }

  def splitByStatementType (d: Document): Map[Kind.Value, List[Statement]] = {
    splitByStatementType(d.statements().toList)
  }

  def splitByStatementType (ss: Seq[Statement]): Map[Kind.Value, List[Statement]] = {
    Map(
      Kind.act  -> ss.toArray.collect{case s:Activity          => s.asInstanceOf[Statement]}.toList,
      Kind.ent  -> ss.toArray.collect{case s:Entity            => s.asInstanceOf[Statement]}.toList,
      Kind.ag   -> ss.toArray.collect{case s:Agent             => s.asInstanceOf[Statement]}.toList,
      Kind.wgb  -> ss.toArray.collect{case s:WasGeneratedBy    => s.asInstanceOf[Statement]}.toList,
      Kind.wib  -> ss.toArray.collect{case s:WasInvalidatedBy  => s.asInstanceOf[Statement]}.toList,
      Kind.usd  -> ss.toArray.collect{case s:Used              => s.asInstanceOf[Statement]}.toList,
      Kind.wdf  -> ss.toArray.collect{case s:WasDerivedFrom    => s.asInstanceOf[Statement]}.toList,
      Kind.waw  -> ss.toArray.collect{case s:WasAssociatedWith => s.asInstanceOf[Statement]}.toList,
      Kind.wat  -> ss.toArray.collect{case s:WasAttributedTo   => s.asInstanceOf[Statement]}.toList,
      Kind.aobo -> ss.toArray.collect{case s:ActedOnBehalfOf   => s.asInstanceOf[Statement]}.toList,
      Kind.spec -> ss.toArray.collect{case s:SpecializationOf  => s.asInstanceOf[Statement]}.toList,
      Kind.alt  -> ss.toArray.collect{case s:AlternateOf       => s.asInstanceOf[Statement]}.toList,
      Kind.wsb  -> ss.toArray.collect{case s:WasStartedBy      => s.asInstanceOf[Statement]}.toList,
      Kind.web  -> ss.toArray.collect{case s:WasEndedBy        => s.asInstanceOf[Statement]}.toList,
      Kind.mem  -> ss.toArray.collect{case s:HadMember         => s.asInstanceOf[Statement]}.toList
    )
  }


  def computeDerivationClosure(idx: Map[immutable.Kind.Value, List[org.openprovenance.prov.model.Statement]]): (List[WasDerivedFromPlus],List[WasDerivedFromStar]) = {
    val allDerivations: List[WasDerivedFrom] = idx(Kind.wdf).collect { case (wdf: WasDerivedFrom) => wdf }


    val transitiveClosure = new TransitiveClosure[immutable.QualifiedName]

    transitiveClosure.symmetric = false
    allDerivations.foreach(der => transitiveClosure.add2(der.generatedEntity, der.usedEntity))


    val allDerivationsPlus: List[WasDerivedFromPlus] = transitiveClosure.transitiveClosure().map { case (q1, q2) => new WasDerivedFromPlus(null, q1, q2, null, null, null, Set(), Set(), Map()) }.toList



    val transitiveClosure2 = new TransitiveClosure[immutable.QualifiedName]
    transitiveClosure2.symmetric = false
    allDerivations.foreach(der => {
      transitiveClosure2.add2(der.generatedEntity, der.usedEntity)
      transitiveClosure2.add2(der.generatedEntity, der.generatedEntity)
    })

    val allDerivationsStar: List[WasDerivedFromStar] = transitiveClosure2.transitiveClosure().map { case (q1, q2) => new WasDerivedFromStar(null, q1, q2, null, null, null, Set(), Set(), Map()) }.toList



    (allDerivationsPlus, allDerivationsStar)
  }


}
