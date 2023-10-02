package org.openprovenance.prov.scala.iface

import org.openprovenance.prov.scala.immutable.Statement
import org.openprovenance.prov.scala.nlgspec_transformer.Environment
import org.openprovenance.prov.scala.query.QueryInterpreter.RField
import org.openprovenance.prov.scala.query.StatementAccessor

trait QueryEngine [Statement,RField] {
  def processQuery(queryContents: String, environment: Environment, statementAccessorForDocument: Option[String] => StatementAccessor[Statement]): QueryResult[RField]

}

trait QueryResult[+RField] {
  def get: Seq[Map[String, RField]]
}

