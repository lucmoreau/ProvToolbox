package org.openprovenance.prov.scala.iface
import org.openprovenance.prov.scala.nlgspec_transformer.Environment
import org.openprovenance.prov.scala.query.{Processor, StatementAccessor}

import scala.collection.mutable

class QFactory {
  def makeQueryEnfine: QueryEngine =
    new QueryEngine {
      override def processQuery(queryContents: String, environment: Environment, statementAccessorForDocument: Option[String] => StatementAccessor) = {
        val engine = new Processor(statementAccessorForDocument, environment)
        val set: mutable.Set[engine.Record] = engine.newRecords()
        engine.evalAccumulate(queryContents, set)
        val result: Set[engine.RField] = engine.toFields(set)
        println("found " + set.size + " records (and " + result.size + " fields)")
        result
      }
    }

}
