package org.openprovenance.prov.scala.nlg

import org.openprovenance.prov.scala.narrator.{EventPrecedence, Follows, LinearOrder, NoEvent, Synchronized}

object ActivityInfo {
  def oldestEvent(order: LinearOrder): Set[Integer] = {
    order match {
      case NoEvent() => Set()
      case Synchronized(events, past) =>
        val events2 = oldestEvent(past.asInstanceOf[LinearOrder])
        if (events2.isEmpty) {
          events
        } else {
          events2
        }
      case Follows(events, past) =>
        val events2 = oldestEvent(past.asInstanceOf[LinearOrder])
        if (events2.isEmpty) {
          events
        } else {
          events2
        }
    }
  }

  def mostRecentEvent(order: LinearOrder): Set[Integer] = {
    order match {
      case NoEvent() => Set()
      case Synchronized(events, _) => events
      case Follows(events, _) => events
    }
  }
}

case class ActivityInfo(preamble: Set[Integer], body: LinearOrder, postamble: Set[Integer], ep: EventPrecedence) extends Ordered[ActivityInfo] {
  override def compare(that: ActivityInfo): Int = {
    val p1 = precedes(this, that)
    val p2 = precedes(that, this)
    if (p1) {
      if (p2) 0 else -1
    } else if (p2) 1 else 0
  }


  def precedes(activity1: ActivityInfo, activity2: ActivityInfo): Boolean = {
    val old1: Set[Integer] = ActivityInfo.oldestEvent(activity1.body)
    val recent2: Set[Integer] = ActivityInfo.mostRecentEvent(activity2.body)
    old1.exists((i1:Integer) => recent2.exists((i2:Integer) => ep.precedes(i1)(i2)))
  }

}

