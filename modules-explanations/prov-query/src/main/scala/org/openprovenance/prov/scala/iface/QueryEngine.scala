package org.openprovenance.prov.scala.iface

import org.openprovenance.prov.scala.nlgspec_transformer.Environment
import org.openprovenance.prov.scala.query.{QueryInterpreter, StatementAccessor}

trait QueryEngine {
  def processQuery(queryContents: String, environment: Environment, statementAccessorForDocument: Option[String] => StatementAccessor): Set[QueryInterpreter.RField]

}
