package org.openprovenance.prov.scala.xplain

import org.openprovenance.prov.scala.iface.QFactory.makeDocumentAccessor
import org.openprovenance.prov.scala.immutable
import org.openprovenance.prov.scala.immutable.{Kind, Statement}
import org.openprovenance.prov.scala.nlgspec_transformer.Environment
import org.openprovenance.prov.scala.nlgspec_transformer.defs.{Dictionary, Plan}
import org.openprovenance.prov.scala.primitive.Keywords
import org.openprovenance.prov.scala.query.QueryInterpreter.RField
import org.openprovenance.prov.scala.query.{Processor, QuerySetup, Record}

import scala.collection.mutable

class PlanQuery(statements:Seq[immutable.Statement], documents: Map[String, Seq[immutable.Statement]]) {


  def processQuery(plan: Plan,
                   dictionaries: Seq[Dictionary],
                   profiles: Map[String, Object],
                   the_profile: String,
                   context: Map[String, String]): (Environment, Seq[Map[String, RField]]) = {
    val environment = Environment(context, dictionaries, profiles, the_profile)
    val engine = new Processor(statementAccessorForDocument, environment)

    val query: String = plan.query match {
      case s: String => s
      case a: Array[String] => a.mkString(" \n")
      case a: Seq[String]@unchecked => a.mkString(" \n")
      case _ => println(plan.name); println(plan); throw new UnsupportedOperationException("incorrect query for " + plan.name)
    }

    val records: mutable.Set[Record] = engine.newRecords()

    engine.evalAccumulate(query, records)
    val all_matching_objects: Seq[Map[String, RField]] = records.toSeq.map(engine.toMap2).toList
    (environment, all_matching_objects)
  }

  def processQuery(plan: Plan,
                   s: Statement,
                   dictionaries: Seq[Dictionary],
                   profiles: Map[String, Object],
                   the_profile: String,
                   context: Map[String, String]): List[Map[String, RField]] = {


    val environment = Environment(context, dictionaries, profiles, the_profile)
    val engine = new Processor(statementAccessorForDocument, environment)

    val headStatementId = plan.select.keys.head
    val headStatementType = plan.select(headStatementId)(Keywords.TYPE)
    val headStatementKind: Kind.Value = QuerySetup.nameMapper(headStatementType)
    val actualStatementKind: Kind.Value = Kind.toKind(s.getKind)

    if (actualStatementKind == headStatementKind) {

      val query: String = plan.query match {
        case s: String => s
        case a: Array[String] => a.mkString(" \n")
        case a: Seq[String]@unchecked => a.mkString(" \n")
        case _ => println(plan.name); println(plan); throw new UnsupportedOperationException("incorrect query for " + plan.name)
      }


      val set: mutable.Set[Record] = engine.newRecords()

      if (query == "None") {
        List(Map())
      } else {
        engine.evalAccumulate(query, set)
        val statements: List[Map[String, RField]] = set.toSeq.map(engine.toMap2).filter(m => engine.toStatement(m(headStatementId)) == s).toList
        statements
      }
    } else {
      List()
    }
  }

  /*
  private val accessor: StatementAccessor[Statement] = makeStatementAccessor(statements)
  private val accessors: Map[String, StatementAccessor[Statement]] = documents.map { case (s: String, seq: Seq[Statement]) => (s, makeStatementAccessor(seq)) }

  val statementAccessorForDocument: (Option[String] => StatementAccessor[Statement]) = {
    case None => accessor
    case Some(s) => accessors(s)
  }
  *
   */

  private val statementAccessorForDocument=makeDocumentAccessor(statements,documents)



}
