package org.openprovenance.prov.scala.query

import org.openprovenance.prov.scala.immutable.{Kind, QualifiedName}

object QuerySetup {


  val nameMapper: Map[String, Kind.Value] = Map(
    "prov:ActedOnBehalfOf" -> Kind.aobo,
    "prov:Activity" -> Kind.act,
    "prov:Agent" -> Kind.ag,
    "prov:Entity" -> Kind.ent,
    "prov:Used" -> Kind.usd,
    "prov:WasAssociatedWith" -> Kind.waw,
    "prov:WasAttributedTo" -> Kind.wat,
    "prov:WasDerivedFrom" -> Kind.wdf,
    "prov:WasGeneratedBy" -> Kind.wgb,
    "prov:WasInvalidatedBy" -> Kind.wib,
    "prov:WasStartedBy" -> Kind.wsb,
    "prov:WasEndedBy" -> Kind.web,
    "provext:AlternateOf" -> Kind.alt,
    "provext:SpecializationOf" -> Kind.spec,
    "provext:HadMember" -> Kind.mem,
    "provext:WasDerivedFromPlus" -> Kind.winfl,
    "provext:WasDerivedFromStar" -> Kind.winfl)


  val NLG_URI = "https://openprovenance.org/nlg#"
  val NLG_PREFIX = "nlg"

  var count:Int=0

  def gensym(): QualifiedName = {
    count = count + 1
    new QualifiedName(NLG_PREFIX, "evt" + count, NLG_URI)
  }
}