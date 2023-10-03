package org.openprovenance.prov.scala.query

import org.apache.logging.log4j.{LogManager, Logger}
import org.openprovenance.prov.model.Namespace
import org.openprovenance.prov.scala.immutable.Statement
import org.openprovenance.prov.scala.interop.{FileOutput, Output}
import org.openprovenance.prov.scala.nlgspec_transformer.Environment
import org.openprovenance.prov.scala.query.QueryAST.Schema
import org.openprovenance.prov.scala.query.QueryInterpreter.{RField, RFields}
import org.parboiled2.ParseError

import scala.annotation.unused
import scala.collection.mutable

trait StatementAccessor[Statement] {
  def findStatement(type_string: String): List[Statement]
}



class Processor (finder:Option[String]=>StatementAccessor[Statement], env: Environment) extends QueryInterpreter  {

  val logger: Logger = LogManager.getLogger(classOf[Processor])

  override val statementFinder: Option[String]=>StatementAccessor[Statement] = finder
  override val environment: Environment = env

  @unused
  def evalPrint(q: String): Unit = {
    val op: Operator = parsePQL(q).get
    execQuery(Print(op), None)
  }

  def newRecords(): mutable.Set[Record] = {
    mutable.Set[Record]()
  }

  def accumulateInto(where: Option[mutable.Set[Record]]): Record => Unit = {
    where match {
      case None => _: Record =>
      case Some(set) => x: Record => set += x
    }
  }

  def execQuery(q: Operator, recs: Option[mutable.Set[Record]]): Unit = {
    execOp(q) (accumulateInto (recs))
  }

  def evalAccumulate(queryString: String, set: mutable.Set[Record]): Processor = {
    val op: Operator =parsePQL(queryString).get
    logger.debug(op)
    execQuery(op, Some(set))
    this
  }

  @unused
  def toStatements(set: mutable.Set[Record]): Set[Statement] = {
    set.map(rec => rec.apply(rec.schema).map(toStatement).toSet).toSet.flatten
  }

  def toFields (records: mutable.Set[Record]): Set[RField] = {
    records.map(record => record.apply(record.schema).toSet).toSet.flatten
  }

  def toMap(rec: Record): Map[String, Statement] = {
    val schema: Schema =rec.schema
    val values: Vector[Statement] =rec(schema).map(toStatement)
    schema.zip(values).toMap
  }

  def toMap2(record: Record): Map[String, RField] = {
    val schema: Schema  = record.schema
    val values: RFields = record(schema)
    schema.zip(values).toMap
  }

  def parsePQL(s: String): Option[Operator] = {
    import scala.util.{Failure, Success}

    val ns=new Namespace
    ns.addKnownNamespaces()
    ns.register("provext", "http://openprovenance.org/prov/extension#");

    val p=new ProvQLParser( s, ns)
    p.query.run() match {
      case Success(ast) => Some(ast)
      case Failure(e: ParseError) => println(p.formatError(e)); None
      case Failure(e) =>e.printStackTrace(); None
    }
  }



}
