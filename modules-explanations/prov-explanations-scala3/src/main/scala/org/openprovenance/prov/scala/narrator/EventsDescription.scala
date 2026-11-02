package org.openprovenance.prov.scala.narrator

import org.openprovenance.prov.scala.immutable.Statement
import org.openprovenance.prov.scala.nlg.ActivityInfo

case class EventsDescription(idx: Map[Integer, String],
                             evt: Map[String, Statement],
                             order: LinearOrder,
                             timeline: Array[ActivityInfo],
                             activityInfos: Map[String, ActivityInfo],
                             theOrderedStatements: List[Statement]) {
  val theOrderStatementIds: Seq[String] =theOrderedStatements.map(_.id.getUri())
}

