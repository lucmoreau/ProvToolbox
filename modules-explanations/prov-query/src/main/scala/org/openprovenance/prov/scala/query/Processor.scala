package org.openprovenance.prov.scala.query

import org.apache.logging.log4j.{LogManager, Logger}
import org.openprovenance.prov.model.Namespace
import org.openprovenance.prov.scala.immutable.Statement
import org.openprovenance.prov.scala.interop.{FileOutput, Output}
import org.openprovenance.prov.scala.nlgspec_transformer.Environment
import org.openprovenance.prov.scala.query.Run.MainEngine
import org.openprovenance.prov.scala.query.query_unstaged.QueryInterpreter
import org.parboiled2.ParseError

import scala.collection.mutable

trait StatementAccessor {
  def findStatement(type_string: String): List[Statement]
}

/*
trait QueryAble extends QueryInterpreter {
  def evalPrint(q: String): Unit
  def evalAccumulate(q: String, set: mutable.Set[Record]): QueryAble
  def toStatements (set: mutable.Set[Record]): Set[Statement]
}*/


class Processor (finder:Option[String]=>StatementAccessor, env: Environment) extends Engine with MainEngine with QueryInterpreter  {

  val logger: Logger = LogManager.getLogger(classOf[Processor])


  override def liftTable(n: Table): String = n

  override def eval: Unit = run

  override val statementFinder: Option[String]=>StatementAccessor = finder

  override val environment: Environment = env

  val primitive = new EngineProcessFunction(this)


  def evalPrint(q: String): Unit = {
    //val op: Operator =parseSql(q)
    val op: Operator =parseSql2(q).get
    //println(op)
    execQuery(Print(op), None)
  }

  def newRecords(): mutable.Set[Record] = {
    mutable.Set[Record]()
  }

  def accumulateInto(where: Option[mutable.Set[Record]]): Record => Unit = {
    where match {
      case None => { _: Record => }
      case Some(set) => { x: Record => set += x }
    }
  }

  def execQuery(q: Operator, recs: Option[mutable.Set[Record]]): Unit = {
    execOp(q) (accumulateInto (recs))

//    println("Processor " + recs)
  }



  def evalAccumulate(q: String, set: mutable.Set[Record]): Processor = {
    //val op: Operator =parseSql(q)
    val op: Operator =parseSql2(q).get
    logger.debug(op)
    //println(op)
    execQuery(op, Some(set))
    this
  }

  def toStatements (set: mutable.Set[Record]): Set[Statement] = {
    val res1 =set.map(rec => {
      val f: Set[Statement] =rec.apply(rec.schema).map(toStatement(_)).toSet
      f
    }).toSet.flatten

    res1
  }

  def toFields (set: mutable.Set[Record]): Set[RField] = {
    val res1 =set.map(rec => {
      val f: Set[RField] =rec.apply(rec.schema).toSet
      f
    }).toSet.flatten

    res1
  }


  def toMap(rec: Record): Map[String, Statement] = {
    val schema: Schema =rec.schema
    val values: Vector[Statement] =rec(schema).map(toStatement(_))
    schema.zip(values).toMap
  }

  def toMap2(rec: Record): Map[String, RField] = {
    val schema: Schema =rec.schema
    val values: RFields =rec(schema)
    schema.zip(values).toMap
  }


  def parseSql2 (s: String): Option[Operator] = {
    import scala.util.{Failure, Success}

    val ns=new Namespace
    ns.addKnownNamespaces()
    ns.register("provext", "http://openprovenance.org/prov/extension#");



    val p=new ProvQLParser(this, s, ns)
    p.query.run() match {
      case Success(ast) => Some(ast.asInstanceOf[Operator])
      case Failure(e: ParseError) => println(p.formatError(e)); None
      case Failure(e) =>e.printStackTrace(); None
    }
  }

  def exportToJson(out: Output, set: mutable.Set[Record]): Unit = {
    Json.om.writeValue(out.asInstanceOf[FileOutput].f,set)
  }


}