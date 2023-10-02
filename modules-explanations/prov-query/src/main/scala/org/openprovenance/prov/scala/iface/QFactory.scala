package org.openprovenance.prov.scala.iface
import org.openprovenance.prov.scala.immutable.Statement
import org.openprovenance.prov.scala.nlgspec_transformer.Environment
import org.openprovenance.prov.scala.query.QueryInterpreter.RField
import org.openprovenance.prov.scala.query.{Processor, Record, StatementAccessor}

import scala.collection.mutable

class QFactory {
  def makeQueryEnfine: QueryEngine[Statement, RField] =
    new QueryEngine[Statement, RField] {
      override def processQuery(theQuery: String, environment: Environment, statementAccessorForDocument: Option[String] => StatementAccessor[Statement]): QueryResult[RField] = {
        val queryProcessor = new Processor(statementAccessorForDocument, environment)
        val records: mutable.Set[Record] = queryProcessor.newRecords()
        queryProcessor.evalAccumulate(theQuery, records)

        val result: Seq[Map[String, RField]] =records.toSeq.map(queryProcessor.toMap2)
        new QueryResult[RField] {
          override def get: Seq[Map[String, RField]] = result
        }
      }
    }
}
