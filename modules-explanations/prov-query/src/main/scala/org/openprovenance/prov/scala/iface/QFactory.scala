package org.openprovenance.prov.scala.iface
import org.openprovenance.prov.scala.immutable.{Document, Statement, TypedValue}
import org.openprovenance.prov.scala.nlgspec_transformer.Environment
import org.openprovenance.prov.scala.query.QueryInterpreter.RField
import org.openprovenance.prov.scala.query.{Processor, Record, StatementAccessor}
import org.openprovenance.prov.scala.utilities.OrType.or

import scala.collection.mutable

class QFactory {
  def makeQueryEnfine: QueryEngine[Statement, RField] =
    new QueryEngine[Statement, RField] {
      override def processQuery(theQuery: String, doc: Document, environment: Environment, statementAccessorForDocument: Option[String] => StatementAccessor[Statement]): QueryResult[RField] = {
        val queryProcessor = new Processor(statementAccessorForDocument, environment)
        val records: mutable.Set[Record] = queryProcessor.newRecords()
        queryProcessor.evalAccumulate(theQuery, records)

        val result1: Set[RField] = queryProcessor.toFields(records)
        println("found " + records.size + " records (and " + result1.size + " fields)")

        def convert[T <% Statement or Seq[Statement] or Seq[TypedValue]](t: T): Seq[Statement] = {
          t.a match {
            case None => t.b match {
              case None => throw new UnsupportedOperationException
              case Some(s: Seq[TypedValue]) => Seq()
            }
            case Some(t1) => t1.a match {
              case None => t1.b.get
              case Some(s) => Seq(s)
            }
          }
        }


        val statements = result1.flatMap(f => convert[RField](f))
        val doc2 = new Document(statements, doc.namespace)


        val result: Seq[Map[String, RField]] =records.toSeq.map(queryProcessor.toMap2)
        new QueryResult[RField] {
          override def getRecords: Seq[Map[String, RField]] = result
          override def getDocument: Document=doc2
        }
      }
    }
}
