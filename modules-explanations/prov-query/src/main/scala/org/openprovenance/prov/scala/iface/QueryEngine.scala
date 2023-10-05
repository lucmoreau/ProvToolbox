package org.openprovenance.prov.scala.iface

import org.openprovenance.prov.scala.immutable.{Document, Statement}
import org.openprovenance.prov.scala.nlgspec_transformer.Environment
import org.openprovenance.prov.scala.query.QueryInterpreter.RField
import org.openprovenance.prov.scala.query.StatementAccessor

trait QueryEngine [Statement,RField] {
  def processQuery(queryContents: String, doc: Document, environment: Environment, statementAccessorForDocument: Option[String] => StatementAccessor[Statement]): QueryResult[RField]
  def processQuery(queryContents: String, doc: Document, environment: Environment): QueryResult[RField]
  def processQuery(queryContents: String, doc: Document): QueryResult[RField]
  

}

trait QueryResult[+RField] {
  def getRecords: Seq[Map[String, RField]]
  def getDocument: Document
}

