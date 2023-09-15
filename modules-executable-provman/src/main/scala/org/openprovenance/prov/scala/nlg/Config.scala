package org.openprovenance.prov.scala.nlg

import org.openprovenance.prov.scala.immutable.{QualifiedName, Statement}
import org.openprovenance.prov.scala.interop.Config

import scala.collection.mutable

case class Triple (subject: QualifiedName,
                   predicate: String,
                   obj: Object) {
  override val hashCode: Int = scala.runtime.ScalaRunTime._hashCode(Triple.this)
}
case class Narrative (sentences: List[String] =List(),
                      snlgs: List[String] =List(),
                      trees: List[() => String] =List(),
                      templates: List[Object]=List(),
                      coverage: List[mutable.Set[Triple]]=List())

case class EventsDescription()

case class Environment (context: Map[String,String],
                        dictionaries:Seq[Object],
                        profiles:Map[String,Object],
                        theprofile:Array[String],
                        active_profiles:List[List[String]])


class RealiserFactory(templates:Seq[Object], dictionaries:Seq[Object], profiles:Map[String, Object], infiles:String=null) {
  def makeStatementAccessor(toSeq: Seq[Statement]): _root_.org.openprovenance.prov.scala.query.StatementAccessor = ???

  def this(config: Config) {
    this(Seq(),Seq(),Map(),null)
  }

  val accessors: Map[String,Seq[Statement]] = ???
}