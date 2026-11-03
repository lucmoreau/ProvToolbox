package org.openprovenance.prov.scala.nlg

import org.openprovenance.prov.scala.immutable._
import org.openprovenance.prov.scala.narrator._
import org.openprovenance.prov.validation.{EventMatrix, Gensym}

import scala.collection.mutable


class ActivityProcessor(mat: EventMatrix, idx: Map[Integer,String], evts: Map[String, Statement]) extends EventOrganiser (mat) {
  

  def getActivity(s: org.openprovenance.prov.model.Statement): Option[QualifiedName] = {
    s match {
      case wgb: WasGeneratedBy => Some(wgb.activity)
      case usd: Used => Some(usd.activity)
      case wsb: WasStartedBy => Some(wsb.activity)
      case web: WasEndedBy => Some(web.activity)
      case _ => None
    }
  }

  def filterPerActivity(order: LinearOrder, theMap: scala.collection.mutable.Map[String, LinearOrder]): Unit = {
    order match {
      case NoEvent() =>
      case Synchronized(events, past) =>
        filterPerActivity(past.asInstanceOf[LinearOrder], theMap)
        events.foreach(i => {
          val id = idx(i)
          val s = evts(id)
          getActivity(s) match {
            case Some(qn) =>
              val str = getUriIfNotNull(qn)
              val order = theMap.getOrElse(str, NoEvent())
              theMap += ((str, Synchronized(Set(i), order)))
            case None =>
          }
        })

      case Follows(events, past) =>
        filterPerActivity(past.asInstanceOf[LinearOrder], theMap)
        events.foreach(i => {
          val id = idx(i)
          val s: Statement = evts(id)

          getActivity(s) match {
            case Some(qn) =>
              val str = getUriIfNotNull(qn)
              val order = theMap.getOrElse(str, NoEvent())
              theMap += ((str, Follows(Set(i), order)))
            case None =>
          }
        })
    }
  }


  def structureActivityEvents2(order: LinearOrder, preamble: scala.collection.mutable.Set[Integer], postamble: scala.collection.mutable.Set[Integer]): LinearOrder = {
    order match {
      case NoEvent() => NoEvent()
      case Synchronized(events, past) =>
        var bodyEvents = List[Integer]()
        events.foreach(i => {
          val id = idx(i)
          val s = evts(id)
          s match {
            case _: WasStartedBy => preamble += i
            case _: WasEndedBy => postamble += i
            case _ => bodyEvents = i :: bodyEvents
          }
        })

        if (bodyEvents.nonEmpty) {
          Synchronized(bodyEvents.toSet, structureActivityEvents2(past.asInstanceOf[LinearOrder], preamble, postamble))
        } else {
          structureActivityEvents2(past.asInstanceOf[LinearOrder], preamble, postamble)
        }

      case Follows(events, past) =>
        var bodyEvents = List[Integer]()
        events.foreach(i => {
          val id = idx(i)
          val s = evts(id)
          s match {
            case _: WasStartedBy => preamble += i
            case _: WasEndedBy => postamble += i
            case _ => bodyEvents = i :: bodyEvents
          }
        })

        if (bodyEvents.nonEmpty) {
          Follows(bodyEvents.toSet, structureActivityEvents2(past.asInstanceOf[LinearOrder], preamble, postamble))
        } else {
          structureActivityEvents2(past.asInstanceOf[LinearOrder], preamble, postamble)
        }
    }
  }

  def structureActivityEvents(order: LinearOrder): ActivityInfo = {
    val preamble: scala.collection.mutable.Set[Integer] = scala.collection.mutable.Set()
    val postamble: scala.collection.mutable.Set[Integer] = scala.collection.mutable.Set()
    val newOrder = structureActivityEvents2(order, preamble, postamble)
    ActivityInfo(preamble.toSet, newOrder, postamble.toSet, this)
  }

  val unknown_URI = "http://openprovenance.org/unknown"

  def getUriIfNotNull(q: QualifiedName): String = {
    if (q == null) unknown_URI else q.getUri
  }

  def toActivityInfo(activityOrder: scala.collection.mutable.Map[String, LinearOrder]): Map[String, ActivityInfo] = {
    activityOrder.map { case (s, o) => (s, structureActivityEvents(o)) }.toMap
  }

  def convertToSequence(order: LinearOrder, statements: Set[Statement], seen: scala.collection.mutable.Set[QualifiedName]): List[Statement] = {
    order match {
      case NoEvent() => List()
      case Follows(events, past) =>
        processOrder(events, past.asInstanceOf[LinearOrder], statements, seen)
      case Synchronized(events, past) =>
        processOrder(events, past.asInstanceOf[LinearOrder], statements, seen)
    }
  }

  def originalStatement(s: Statement): Boolean = {
    val id = s.asInstanceOf[Identifiable].getId
    //println(id)
    id.getNamespaceURI != Gensym.VAL_URI
  }

  // TODO: contains relations, which predicate is being used?
  def searchForWgb(wgb: WasGeneratedBy, statements: Set[Statement], seen: scala.collection.mutable.Set[QualifiedName]): List[Statement] = {
    var ll: List[Statement] = List()
    val entity = wgb.entity
    val activity: QualifiedName = wgb.activity
    val allAgents: Set[QualifiedName] = statements.collect { case ag: Agent => ag.id }

    if ((activity != null) && !activity.isInstanceOf[org.openprovenance.prov.validation.VarQName] && !seen.contains(activity)) {
      ll = ll ++ statements.collect { case a: Activity => a }.filter(a => a.id.equals(activity) && !seen.contains(activity))
      seen.+=(activity)
    }

    if ((entity != null) && !seen.contains(entity)) {
      val entities = statements.collect { case e: Entity => e }.filter(e => e.id.equals(entity) && !seen.contains(entity) && !allAgents.contains(entity))
      ll = ll ++ entities
      if (allAgents.contains(entity)) {
        ll = ll ++ statements.collect { case ag: Agent => ag }.filter(ag => ag.getId().equals(entity))
      }
      seen.+=(entity)
    }

    ll = ll ++ statements.collect { case s: WasGeneratedBy => s }.filter(s => wgb.getId.equals(s.getId))


    val ll2 = statements.collect { case wdf: WasDerivedFrom => wdf }.filter(wdf => wdf.generatedEntity.equals(entity) && (activity == null || wdf.activity == null || wdf.activity.equals(activity)))

    ll = ll ++ ll2

    val ll3 = statements.collect { case wat: WasAttributedTo => wat }.filter(wat => wat.entity.equals(entity) && !seen.contains(wat.id))

    val agents: Set[QualifiedName] = ll3.map(wat => wat.agent).diff(seen)

    seen.++=(agents)
    seen.++=(ll3.map(_.id))

    val ll4 = statements.collect { case ag: Agent => ag }.filter(ag => agents.contains(ag.id))

    val ll6: Set[ActedOnBehalfOf] = statements.collect { case aobo: ActedOnBehalfOf => aobo}.filter(aobo => agents.contains(aobo.delegate))  // TODO, needs to ensure that we have seen the responsible agents

    val agents2: Set[QualifiedName] = ll6.map(aobo => aobo.responsible).diff(seen)

    val ll5 = statements.collect { case ag: Agent => ag }.filter(ag => agents2.contains(ag.id))


    seen.++=(agents2)

    seen.++=(ll6.map(_.id))



    val ll7 = statements.collect { case spe: SpecializationOf => spe }.filter(spe => spe.getSpecificEntity != null && spe.getSpecificEntity.equals(entity))
    val ll8 = statements.collect { case alt: AlternateOf => alt }.filter(alt => alt.getAlternate2 != null && alt.getAlternate2.equals(entity))

    ll = ll ++ ll5 ++ ll4 ++ ll6 ++ ll3 ++ ll7 ++ ll8


    ll
  }

  def searchForUsd(usd: Used, statements: Set[Statement], seen: mutable.Set[QualifiedName]): List[Statement] = {
    var ll: List[Statement] = List()
    val entity = usd.entity
    val activity = usd.activity


    if ((activity != null) && !seen.contains(activity)) {
      ll = ll ++ statements.collect { case a: Activity => a }.filter(a => a.getId == activity && !seen.contains(activity))
      seen.+=(activity)
    }


    if ((entity != null) && !seen.contains(entity)) {
      ll = ll ++ statements.collect { case e: Entity => e }.filter(e => e.getId == entity && !seen.contains(entity))
      seen.+=(entity)
    }

    ll = ll ++ statements.collect { case s: Used => s }.filter(s => usd.getId == s.getId)

    ll
  }

  def searchForWib(wib: WasInvalidatedBy, statements: Set[Statement], seen: mutable.Set[QualifiedName]): List[Statement] = {
    var ll: List[Statement] = List()
    val entity = wib.entity
    val activity = wib.activity


    if ((activity != null) && !seen.contains(activity)) {
      ll = ll ++ statements.collect { case a: Activity => a }.filter(a => a.getId == activity && !seen.contains(activity))
      seen.+=(activity)
    }


    if ((entity != null) && !seen.contains(entity)) {
      ll = ll ++ statements.collect { case e: Entity => e }.filter(e => e.getId == entity && !seen.contains(entity))
      seen.+=(entity)
    }

    ll = ll ++ statements.collect { case s: WasInvalidatedBy => s }.filter(s => wib.getId == s.getId)

    ll
  }

  def searchForWeb(web: WasEndedBy, statements: Set[Statement], seen: mutable.Set[QualifiedName]): List[Statement] = {
    var ll: List[Statement] = List()
    val ender = web.ender
    val activity = web.activity


    if ((activity != null) && !seen.contains(activity)) {
      ll = ll ++ statements.collect { case a: Activity => a }.filter(a => a.getId == activity && !seen.contains(activity))
      seen.+=(activity)
    }


    if ((ender != null) && !seen.contains(ender)) {
      ll = ll ++ statements.collect { case e: Entity => e }.filter(e => e.getId == ender && !seen.contains(ender))
      seen.+=(ender)
    }

    ll = ll ++ statements.collect { case s: WasEndedBy => s }.filter(s => web.id == s.id)

    ll
  }

  def searchForWsb(wsb: WasStartedBy, statements: Set[Statement], seen: mutable.Set[QualifiedName]): List[Statement] = {
    var ll: List[Statement] = List()
    val starter = wsb.starter
    val activity = wsb.activity


    if ((starter != null) && !seen.contains(starter)) {
      ll = ll ++ statements.collect { case e: Entity => e }.filter(e => e.getId.equals(starter) && !seen.contains(starter))
      seen += starter
    }


    if ((activity != null) && !seen.contains(activity)) {
      ll = ll ++ statements.collect { case a: Activity => a }.filter(a => a.getId.equals(activity) && !seen.contains(activity))
      seen.+=(activity)
    }


    ll = ll ++ statements.collect { case s: WasStartedBy => s }.filter(s => wsb.getId.equals(s.getId))

    val ll2 = statements.collect { case waw: WasAssociatedWith => waw }.filter(waw => waw.getActivity != null && waw.getActivity.equals(activity))


    val agents: Set[QualifiedName] = ll2.map(waw => waw.agent).diff(seen)

    seen.++=(agents)
    seen.++=(ll2.map(_.id))

    val ll3= statements.collect { case aobo: ActedOnBehalfOf => aobo}.filter(aobo => agents.contains(aobo.delegate))  // TODO, needs to ensure that we have seen the responsible agents

    val agents2: Set[QualifiedName] = ll3.map(aobo => aobo.responsible).diff(seen)

    val ll1= statements.collect { case ag: Agent => ag }.filter(ag => agents.contains(ag.id))
    val ll4 = statements.collect { case ag: Agent => ag }.filter(ag => agents2.contains(ag.id))

    seen.++=(agents2)

    seen.++=(ll3.map(_.id))

    ll = ll ++ ll4 ++ ll1 ++ ll2 ++ ll3

    ll
  }


  private def processOrder(events: Set[Integer], past: LinearOrder, statements: Set[Statement], seen: scala.collection.mutable.Set[QualifiedName]): List[Statement] = {
    val seq2 = convertToSequence(past, statements, seen)
    var ll: List[Statement] = List()


    events.foreach(i => {
      val id: String = idx(i)
      val s: Statement = evts(id)


      s match {
        case wsb: WasStartedBy => if (originalStatement(wsb)) {
          val lll = searchForWsb(wsb, statements, seen)
          ll = ll ++ lll
        }
        case web: WasEndedBy => if (originalStatement(web)) {
          val lll = searchForWeb(web, statements, seen)
          ll = ll ++ lll
        }
        case wib: WasInvalidatedBy => if (originalStatement(wib)) {
          val lll = searchForWib(wib, statements, seen)
          ll = ll ++ lll
        }
        case wgb: WasGeneratedBy => if (originalStatement(wgb)) {
          val lll = searchForWgb(wgb, statements, seen)
          ll = ll ++ lll
        }
        case usd: Used => if (originalStatement(usd)) {
          val lll = searchForUsd(usd, statements, seen)
          ll = ll ++ lll
        }
      }
    })
    seq2 ++ ll
  }

}

