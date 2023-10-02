package org.openprovenance.prov.scala.iface
import org.openprovenance.prov.scala.nlgspec_transformer.Environment
import org.openprovenance.prov.scala.query.{Processor, QueryInterpreter, Record, StatementAccessor}

import scala.collection.mutable

class QFactory {
  def makeQueryEnfine: QueryEngine =
    new QueryEngine {
      override def processQuery(theQuery: String, environment: Environment, statementAccessorForDocument: Option[String] => StatementAccessor): Set[QueryInterpreter.RField] = {
        val queryProcessor = new Processor(statementAccessorForDocument, environment)
        val records: mutable.Set[Record] = queryProcessor.newRecords()
        queryProcessor.evalAccumulate(theQuery, records)
        val result: Set[QueryInterpreter.RField] = queryProcessor.toFields(records)
        println("found " + records.size + " records (and " + result.size + " fields)")
        result
      }
    }
}
