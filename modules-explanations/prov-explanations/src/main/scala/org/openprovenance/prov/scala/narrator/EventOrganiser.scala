package org.openprovenance.prov.scala.narrator

import org.apache.logging.log4j.{LogManager, Logger}
import org.openprovenance.prov.model.BeanTraversal
import org.openprovenance.prov.nlg.ValidationObjectMaker
import org.openprovenance.prov.scala.immutable._
import org.openprovenance.prov.validation.report.ValidationReport
import org.openprovenance.prov.validation.{EventMatrix, Gensym, Validate}
import org.openprovenance.prov.vanilla.ProvFactory

import java.io.{File, PrintWriter}
import java.util
import scala.collection.JavaConverters._
import scala.collection.{JavaConverters, mutable}

// Tests require london timezone:
//export TZ=Europe/London


object EventOrganiser {
  import org.openprovenance.prov.scala.immutable.Kind

  val logger: Logger = LogManager.getLogger("EventOrganiser")


  val  mpf: ProvFactory =ProvFactory.getFactory

  var count = 0
  val NLG_URI = "https://openprovenance.org/nlg#"
  val NLG_PREFIX = "nlg"

  def validateDocument(doc: Document, validator: Validate, mpf: org.openprovenance.prov.model.ProvFactory): ValidationReport = {

    val bc = new BeanTraversal(mpf, mpf)
    val mutableDoc = bc.doAction(doc)
    //val mutableDoc:org.openprovenance.prov.model.Document = mpf.newDocument(doc)/// this does not do a deep copy!

    val report=validator.validate(mutableDoc)

    report
  }

  def findOrder(doc: Document): (EventMatrix, util.Map[String, Integer], Map[String, Statement]) = {

    val validator: Validate = new Validate(org.openprovenance.prov.validation.Config.newYesToAllConfig(mpf, new ValidationObjectMaker))
    val report = validateDocument(doc, validator, mpf)
    val mat = validator.constraints.getMatrix
    val indexer = validator.getEventIndexer
    val idx = indexer.eventIndex
    val evts: util.Map[String, org.openprovenance.prov.model.Statement] = indexer.eventTable

    val evts4: mutable.Map[String,org.openprovenance.prov.model.Statement]=JavaConverters.mapAsScalaMapConverter(evts).asScala
    val evts5: Map[String,Statement]=evts4.map{ case (id,s) => (id,Statement(s))}.toMap

    //val matString =mat.displayMatrix2()
    (mat, idx, evts5)
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


  def gensym(): QualifiedName = {
    count = count + 1
    new QualifiedName(NLG_PREFIX, "evt" + count, NLG_URI)
  }

  def findEventsFromSentence(amap: util.Map[String, Integer]): Map[String, Integer] = {
    amap.asScala.toMap.filterNot { case (s, _) => s.startsWith(Gensym.VAL_URI) }
  }

  def combine(s1: String,s2: String): String = {
    s2 + "\\\\ " + s1
  }

  def doLatex(file:String, body1: () => String, body2: () => String): Unit = {
    val pw=new PrintWriter(new File(file))
    pw.write("\\documentclass{article}\n\n\\begin{document}")
    pw.write(body1())
    pw.write("\\begin{verbatim}")
    pw.write(body2())
    pw.write("\\end{verbatim}")
    pw.write("\\end{document}")
    pw.close()
  }

  def orderToTeX(order: EventOrder): String = {
    order match {
      case NoEvent() => " "
      case Follows(l, p) => "\\fbox{\\begin{tabular}{c}" + combine(l.mkString(" $\\mid$ "),orderToTeX(p)) + "\\end{tabular}}"
      case Parallel(s,p) => "\\fbox{\\begin{tabular}{c}"  + combine(s.map(orderToTeX).mkString(" "), orderToTeX(p)) + "\\end{tabular}}"
      case Join(s,p) => "\\fbox{\\begin{tabular}{c}" + combine(s.mkString(" $\\mid$ "), orderToTeX(p)) + "\\end{tabular}}"
      case Synchronized(s,p) => "\\fbox{\\begin{tabular}{c}" + combine(s.mkString(" $\\bowtie$ "), orderToTeX(p)) + "\\end{tabular}}"
    }
  }

  def addEntitiesToAgents (statements: Iterable[Statement]): Iterable[Entity] = {
    val entities: Set[QualifiedName]    = statements.collect{case e:Entity => e.id}.toSet
    val activities: Set[QualifiedName]  = statements.collect{case a:Activity => a.id}.toSet
    val agents: Iterable[QualifiedName] = statements.collect{case ag:Agent => ag.id}.filterNot(q => entities.contains(q)).filterNot(q => activities.contains(q))
    val newEntities=agents.map(id => new Entity(id, Set(), Set(), None, Set(), Map()))
    logger.debug(newEntities)
    newEntities
  }
}

trait EventPrecedence {
  def precedes(x: Integer): Integer => Boolean
}

class EventOrganiser(mat: EventMatrix) extends EventPrecedence {


  def precedes(x: Integer): Integer => Boolean = {
    evt =>
      evt != x && {
        val p = mat.m.g(evt, x)
        (p != null) && (p.getValue > 0)
      }
  }

  def orderPrecedes(x: Integer): EventOrder => Boolean = {
    case NoEvent() => false
    case Synchronized(seq, past) => seq.exists(precedes(x)) || orderPrecedes(x)(past)
    case Follows(seq, past) => seq.exists(precedes(x)) || orderPrecedes(x)(past)
    case Parallel(seq, past) => seq.exists(orderPrecedes(x)) || orderPrecedes(x)(past)
  }

  def findEventsWithoutPredecessors(events: Set[Integer]): Set[Integer] = {
    events.filterNot(evt => events.exists(precedes(evt)))
  }



  def findParallelEventsWithoutPredecessors(events: Set[Integer]): Set[Integer] = {

    val (cycles, _) = splitEvents(events.map(evt => ToProcess(Seq(evt))), events)

    //println(cycles)

    //println(paths)

    // ensure that any predecessor evt of an element i of the cycle is also part of the cycle
    // sort to select the smallest set
    val found: Option[ACycle] = cycles.toVector.sortWith { case (c1, c2) => c1.set.size < c2.set.size }.find(cycle => cycle.set.forall(i => events.forall(evt => if (precedes(i)(evt)) cycle.set.contains(evt) else true)))
    // println("   => " + found)
    found.get.set
  }


  /**
    * // start with a sequence for each element in the set
    * // find all the predecessors p of the last element of each sequence s
    * // create a new sequence p s if it does not create cycle
    * // or else accumulate the cycle
    * // terminates when no more predecessor, or all of them are cycles.
    * // So, need to use an accumulator for cycles and for acyclic paths
    * // Recursion parameter the seq of sequences still to be processed
    *
    */
  def splitEvents(sequenceSet: Set[ToProcess], all: Set[Integer]): (Set[ACycle], Set[Path]) = {


    if (sequenceSet.isEmpty) {
      (Set(), Set())
    } else {

      val findPredecessors: Set[(Seq[Integer], Set[Integer])] = sequenceSet.map(s => (s.seq, all.filter(precedes(s.seq.head))))

      val sequences: Set[EventsSeq] = findPredecessors.flatMap(x => {
        val (seq: Seq[Integer], set: Set[Integer]) = x
        if (set.isEmpty) Set(Path(seq).asInstanceOf[EventsSeq]) else set.map(i => if (seq.contains(i)) ACycle(seq.toSet).asInstanceOf[EventsSeq] else ToProcess(seq.+:(i)))
      }
      )

      val todo: Set[ToProcess] = sequences.collect { case x: ToProcess => x }
      val cycles: Set[ACycle] = sequences.collect { case x: ACycle => x }
      val paths: Set[Path] = sequences.collect { case x: Path => x }


      val (cycles2: Set[ACycle], paths2: Set[Path]) = splitEvents(todo, all)

      (cycles ++ cycles2, paths ++ paths2)
    }
  }


  def linearizeEvents(events: Set[Integer], order: LinearOrder): LinearOrder = {
    if (events.isEmpty) {
      order
    } else {
      val nextEvents: Set[Integer] = findEventsWithoutPredecessors(events)
      if (nextEvents.isEmpty) {
        val nextParallelEvents: Set[Integer] = findParallelEventsWithoutPredecessors(events)
        val nextOrder = Synchronized(nextParallelEvents, order) //  use another tactic to identity the next set of events


        linearizeEvents(events -- nextParallelEvents, nextOrder)
      } else {
        val nextOrder: LinearOrder = Follows(nextEvents, order)
        linearizeEvents(events -- nextEvents, nextOrder)
      }
    }
  }

  def add_element_to_subsets(p: Set[Set[Integer]], element: Set[Integer]): Set[Set[Set[Integer]]] = {
    val pairing: Set[(Set[Integer], Set[Integer])] = p.map(s => (s, s ++ element))
    pairing.map { case (older, newer) => p - older + newer }
  }


  def partitions(s: Set[Integer]): Set[Set[Set[Integer]]] = s.size match {
    case 0 => throw new UnsupportedOperationException
    case 1 => Set(Set(s))
    case _ =>
      val elem = s.head
      val rest = s - elem
      val pl: Set[Set[Set[Integer]]] = partitions(rest)

      val partList1: Set[Set[Set[Integer]]] = pl.map((p: Set[Set[Integer]]) => p + Set(elem))

      val partList2: Set[Set[Set[Integer]]] = pl.flatMap((p: Set[Set[Integer]]) => add_element_to_subsets(p, Set(elem)))

      // println(" [[ " + elem + " + " + rest)
      // println(" [[[[ " + partList1)
      //  println(" [[[[ " + partList2)
      partList1 ++ partList2
  }


  def reorganize(order: LinearOrder): EventOrder = {
    order match {
      case NoEvent() => NoEvent()
      case Synchronized(events, past) => Synchronized(events, reorganize(past.asInstanceOf[LinearOrder]))
      case Follows(events, past) =>
        val past2 = reorganize(past.asInstanceOf[LinearOrder])
        past2 match {
          case NoEvent() => if (events.size == 1) {
            Follows(events, past2)
          } else {
            Parallel(events.map(e => Follows(Set(e), NoEvent())).toSeq, NoEvent())
          }
          case Synchronized(_: Set[Integer], _) => Follows(events, past2)
          case Parallel(orders, p) =>
            println("  ====> " + past2)
            println("  =====> " + events)

            if (events.size == 1) {
              val yes = orders.filter(o => orderPrecedes(events.head)(o))
              val no = orders.filterNot(o => orderPrecedes(events.head)(o))
              if (yes.size == 1) {
                Parallel(no.+:(Follows(events, yes.head)), p)
              } else if (yes.size > 1) {
                Parallel(no.+:(Follows(events, Parallel(yes, NoEvent()))), p)
              } else { //yes.size=0
                Parallel(orders.+:(Follows(events, NoEvent())), p)
              }
            } else { // events.size>1

              println("--> " + events)

              val yesOld: Set[(Integer, Seq[EventOrder])] = events.map(e => (e, orders.filter(o => orderPrecedes(e)(o)))) // all the events, with order preceding them

              val yes: Map[Integer, Set[EventOrder]] = events.map(e => (e, orders.filter(o => orderPrecedes(e)(o)).toSet)).toMap // all the events, with order preceding them


              val yesEvents: Set[Integer] = yesOld.map(_._1)
              val yesOrders: Set[EventOrder] = yesOld.flatMap(_._2)

              // TODO: partition the yes Events,
              println("--> " + yesEvents)
              yes.foreach(x => println("--> " + x))
              println("--->>" + partitions(yesEvents))

              val noEvents: Set[Integer] = events -- yesEvents
              val noOrders: Set[EventOrder] = orders.toSet -- yesOrders

              val allPar: Set[EventOrder] = noOrders ++ noEvents.map(e => Parallel(Seq(Follows(Set(e), NoEvent())), NoEvent())) + Follows(yesEvents, Parallel(yesOrders.toSeq, p))
              Parallel(allPar.toSeq, p)
            }
        }
    }
  }

}



