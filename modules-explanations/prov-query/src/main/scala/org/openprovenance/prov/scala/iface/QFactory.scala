package org.openprovenance.prov.scala.iface
import org.openprovenance.prov.scala.nlgspec_transformer.Environment
import org.openprovenance.prov.scala.query.{Processor, QueryInterpreter, Record, StatementAccessor}

import scala.collection.mutable

class QFactory {
  def makeQueryEnfine: QueryEngine =
    new QueryEngine {
      override def processQuery(queryContents: String, environment: Environment, statementAccessorForDocument: Option[String] => StatementAccessor): Set[QueryInterpreter.RField] = {
        val engine = new Processor(statementAccessorForDocument, environment)
        val set: mutable.Set[Record] = engine.newRecords()
        engine.evalAccumulate(queryContents, set)
        val result: Set[QueryInterpreter.RField] = engine.toFields(set)
        println("found " + set.size + " records (and " + result.size + " fields)")
        result
      }
    }

}
