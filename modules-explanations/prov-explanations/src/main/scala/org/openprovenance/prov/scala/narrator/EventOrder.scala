package org.openprovenance.prov.scala.narrator


trait EventOrder {}
trait LinearOrder extends EventOrder{}

case class NoEvent () extends LinearOrder
case class Synchronized(seq: Set[Integer], past: EventOrder) extends LinearOrder
case class Follows (last: Set[Integer], past: EventOrder)  extends LinearOrder
case class Parallel (seq: Seq[EventOrder], past: EventOrder) extends EventOrder
case class Join (seq: Seq[Integer], past: Parallel) extends EventOrder


trait EventsSeq{}
case class ACycle (set: Set[Integer])     extends EventsSeq
case class Path (seq: Seq[Integer])      extends EventsSeq
case class ToProcess (seq: Seq[Integer]) extends EventsSeq
