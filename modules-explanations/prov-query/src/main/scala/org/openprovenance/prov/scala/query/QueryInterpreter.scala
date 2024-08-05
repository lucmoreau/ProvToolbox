package org.openprovenance.prov.scala.query

import org.openprovenance.prov.scala.immutable.{Other, ProvFactory, QualifiedName, Statement, TypedValue}
import org.openprovenance.prov.scala.nlgspec_transformer.Environment
import org.openprovenance.prov.scala.primitive.{Primitive, Result, Triple}
import org.openprovenance.prov.scala.query.QueryAST.{Schema, toSchema}
import org.openprovenance.prov.scala.query.QueryInterpreter.{RField, RFields, StatementOrNull}
import org.openprovenance.prov.scala.query.QuerySetup.qother
import org.openprovenance.prov.scala.summary.types._
import org.openprovenance.prov.scala.utilities.OrType
import org.openprovenance.prov.scala.utilities.OrType._

import scala.collection.mutable
import scala.util.Try

object QueryInterpreter {
  type StatementOrNull = Option[Statement]
  type RField = StatementOrNull or Seq[Statement] or Seq[TypedValue]
  type RFields = Vector[RField]

  def getStatementOrNull(f: RField): Option[StatementOrNull] = {
    f.a match {
      case None => None
      case Some(a) => a.a
    }
  }

  def getStatement(f: RField): Option[Statement] = {

    f.a match {
      case None => None
      case Some(a) => a.a match {
        case None => None
        case Some(s) => s
      }
    }
  }

  def getSeqStatement(f: RField): Option[Seq[Statement]] = f.a match {
    case None => None
    case Some(a) => a.b
  }

}

case class Record(fields: RFields, schema: Schema) {
  def apply(key: String): RField = Try(fields(schema indexOf key)).getOrElse[RField](throw new ArrayIndexOutOfBoundsException("Record.apply(key): failed to find " + key + " in " + schema))
  def apply(keys: Schema): RFields = keys.map(key => apply(key))
}

trait QueryInterpreter extends SummaryTypesNames {
  val defaultFieldDelimiter = ','

  val environment: Environment

  /**
   * Low-Level Processing Logic
   * --------------------------
   */


  def toStatement(f: RField): Statement = Try(f.a.get.a.get.get).getOrElse[Statement](throw new UnsupportedOperationException("toStatement for " + f))

  def toSeqStatement(f: RField): Seq[Statement] = f.a.get.b.get

  def toValues(f: RField): Seq[TypedValue] = f.b.get

  def getValues(f: RField): Option[Seq[TypedValue]] = f.b

/*
  def convertToSeqStatement(f: RField): Seq[Statement] = {
    f.a match {
      case None => Seq()
      case Some(f2: or[Statement, Seq[Statement]]) => f2.a match {
        case None => f2.b.get
        case Some(a) => Seq(a)
      }
    }
  }

 */

  def processDocument(document: List[Statement], schema: Schema)(yld: Record => Unit): Unit = {
    val s: Iterator[Statement] = document.iterator

    def nextRecord: Record = Record(schema.map { x => val f: RField = Some(s.next()); f }, schema)

    while (s.hasNext) yld(nextRecord)
  }

  def isNumericCol(s: String): Boolean = s.startsWith("#")

  def printSchema(schema: Schema): Unit = println(schema.mkString(defaultFieldDelimiter.toString))

  def printFields(fields: RFields): Unit = printf(fields.map { _ => "%s" }.mkString(" - ", defaultFieldDelimiter.toString, "\n"), fields: _*)

  /**
   * Query Interpretation
   * --------------------
   */
  def evalPred(p: Predicate)(rec: Record): Boolean = p match {
    case Eq("exists", a1, _) =>
      val result = Primitive.applyFun("exists", (evalRef(a1)(rec), Set()), optionp = false)
      result match {
        case (Some(Result(Some(v), _, _, _)), _triples) => "true" == v
        case (Some(v), _triples) => throw new UnsupportedOperationException("not supporting not string Result")
        case _ => false
      }

    case Eq(pred, a1, a2) =>
      val result = Primitive.applyFun1(pred, (evalRef(a1)(rec), Set()), optionp = false, (evalRef(a2)(rec), Set()), environment)
      result match {
        case (Some(Result(Some(v), _, _, _)), _triples) => "true" == v
        case (Some(v), _triples) => throw new UnsupportedOperationException("not supporting not string Result")
        case _ => false
      }

    case EqL(pred, a1, a2) =>
      val result = Primitive.applyFun1(pred, (evalRef(a1)(rec), Set()), optionp = false, (Seq[Object](a2), Set()), environment)
      result match {
        case (Some(Result(Some(v), _, _, _)), _triples) => "true" == v
        case (Some(v), _triples) => throw new UnsupportedOperationException("not supporting not string Result")
        case _ => false
      }

    case OrPred(pred1, pred2) =>
      if (evalPred(pred1)(rec)) true else {
        evalPred(pred2)(rec)
      }

  }

  def evalRef(r: Ref)(rec: Record): Seq[Object] = r match {
    case Property(name, property) => Primitive.applyProperty(property, None, toStatement(rec(name)), environment)._1
    case Field(name, property) => Primitive.applyField(property, toStatement(rec(name)))._1
    case Value(x) => Seq(x)
  }

  import scala.collection.mutable.ArrayBuffer

  def resultSchema(o: Operator): Schema = o match {
    case Scan(_, schema, _) => schema
    case Filter(_, parent) => resultSchema(parent)
    case Project(schema, _, _) => schema
    case Join(left, _, _, right, _, _) => resultSchema(left) ++ resultSchema(right)
    case LeftJoin(left, _, _, right, _, _) => resultSchema(left) ++ resultSchema(right)
    case Group(keys, agg, _, _, _) => keys ++ agg
    case HashJoin(left, right) => resultSchema(left) ++ resultSchema(right)
    case Print(parent) => toSchema()
    //case Order(f,parent,_)        => Schema()
  }

  val statementFinder: Option[String] => StatementAccessor[Statement]


  def execOpFollowedbyNull(o: Operator)(yld: Record => Unit): Unit = {
    execOp(o)(yld)
    //println("execOpFollowedbyNull  null")
    yld(null)
  }

  def execOp(o: Operator)(yld: Record => Unit): Unit = {
    o match {

      case Scan(statementType, schema, document) =>
        processDocument(statementFinder(document).findStatement(statementType), schema)(yld)

      case Filter(pred, parent) =>
        execOp(parent) { rec => if (evalPred(pred)(rec)) yld(rec) }

      case Project(newSchema, parentSchema, parent) =>
        execOp(parent) { rec => yld(Record(rec(parentSchema), newSchema)) }

      case Join(left, key1, property1, right, key2, property2) =>
        execOp(left) { rec1 =>
          execOp(right) { rec2 =>
            val v1: (Seq[Object], Set[Triple]) = Primitive.applyField(property1, toStatement(rec1(key1)))
            val v2: (Seq[Object], Set[Triple]) = Primitive.applyField(property2, toStatement(rec2(key2)))
            if (v1._1 == v2._1) {
              //  println("*** success")
              yld(Record(rec1.fields ++ rec2.fields, rec1.schema ++ rec2.schema))
            }
          }
        }

      case LeftJoin(left, key1, property1, right, key2, property2) =>
        //println(left)
        //println(right)
        execOp(left) { rec1 =>
          var an_example_of_rec2: Option[Record] = None
          var asuccess: Boolean = false
          execOpFollowedbyNull(right) { rec2 =>
            if (rec2 != null) {
              val v1: (Seq[Object], Set[Triple]) = Primitive.applyField(property1, toStatement(rec1(key1)))
              val v2: (Seq[Object], Set[Triple]) = Primitive.applyField(property2, toStatement(rec2(key2)))

              //println("v1: " + v1)
              //println("v2: " + v2)

              if (v1._1 == v2._1) {
                an_example_of_rec2 = Some(rec2)
                asuccess = true
                val record = Record(rec1.fields ++ rec2.fields, rec1.schema ++ rec2.schema)
                //println("Supplying record (1) " + record)
                yld(record)
              }
            } else {
              //println("==> End of Stream right " + an_example_of_rec2 + " " + asuccess)
              if (!asuccess) {
                val noStatement:StatementOrNull=None;
                val record = Record(rec1.fields ++ Vector(noStatement), rec1.schema ++ Vector(key2))
                //println("Supplying record (2) " + record)
                yld(record)
              }
            }
          }
        }


      case Group(keys, agg, parent, kind, ref) =>
        type Aggregate = Seq[Statement]

        def fieldUnit0(s: String): Aggregate = {
          Seq[Statement]()
        }

        def aggregateFun0(a: Aggregate, b: Seq[RField]): Aggregate = {
          val c: Seq[Statement] = a ++ b.map(f => toStatement(f))
          c
        }

        def aggregateFun1(a: Seq[Object], b: Seq[RField]): Seq[Object] = {
          val c: Seq[Object] = a ++ b.map(f => toValues(f))
          c
        }

        def toField0(a: Aggregate): RField = a

        kind match {
          case "Seq" => ref match {
            case None => doAggregate[Seq[Statement]](keys, agg, parent, yld, fieldUnit0, aggregateFun0, toField0)
            case Some(ref) => doAggregateSort[Seq[Statement]](keys, agg, parent, yld, fieldUnit0, aggregateFun0, toField0, ref)
          }
          case "Count" =>
            doCount[Seq[Statement]](keys, agg, parent, yld, fieldUnit0, aggregateFun0, toField0)

        }



      case HashJoin(left, right) =>
        val keys = resultSchema(left) intersect resultSchema(right)
        val hm = new mutable.HashMap[RFields, ArrayBuffer[Record]]
        execOp(left) { rec1 =>
          val buf = hm.getOrElseUpdate(rec1(keys), new ArrayBuffer[Record])
          buf += rec1
        }
        execOp(right) { rec2 =>
          hm.get(rec2(keys)) foreach {
            _.foreach { rec1 =>
              yld(Record(rec1.fields ++ rec2.fields, rec1.schema ++ rec2.schema))
            }
          }
        }

      case Print(parent) =>
        val schema = resultSchema(parent)
        printSchema(schema)
        execOp(parent) { rec => printFields(rec.fields) }
    }
  }


  def doAggregate[AGGREGATE](keys: Schema, agg: Schema, parent: Operator, yld: Record => Unit, fieldUnit: String => AGGREGATE, aggregateFun: (AGGREGATE, Seq[RField]) => AGGREGATE, toField: AGGREGATE => RField): Unit = {
    val table = new mutable.HashMap[RFields, Seq[AGGREGATE]]
    execOp(parent) { rec =>
      val kvs: RFields = rec(keys)
      val aggregate: Seq[AGGREGATE] = table.getOrElseUpdate(kvs, agg.map(fieldUnit))
      table(kvs) = aggregate.lazyZip(rec(agg).map(f => Seq(f))).map(aggregateFun)
    }
    table foreach { case (k: RFields, a: Seq[AGGREGATE]) =>
      yld(Record(k ++ a.map(toField), keys ++ agg))
    }
  }


  def doCount[AGGREGATE](keys: Schema, agg: Schema, parent: Operator, yld: Record => Unit, fieldUnit: String => AGGREGATE, aggregateFun: (AGGREGATE, Seq[RField]) => AGGREGATE, toField: AGGREGATE => RField): Unit = {
    val table = new mutable.HashMap[RFields, Seq[AGGREGATE]]
    execOp(parent) { rec =>
      val kvs: RFields = rec(keys)
      val aggregate: Seq[AGGREGATE] = table.getOrElseUpdate(kvs, agg.map(fieldUnit))
      table(kvs) = aggregate.lazyZip(rec(agg).map(f => Seq(f))).map(aggregateFun)
    }
    table foreach { case (k: RFields, a: Seq[AGGREGATE]) =>
      val record = Record(k ++ a.map(x => OrType.bToOr2(Seq(new Other(qother, ProvFactory.pf.getName.XSD_INT.asInstanceOf[QualifiedName], x.asInstanceOf[Seq[AnyRef]].size.toString)))), keys ++ agg)
      println(record)
      yld(record)
    }
  }

  def doAggregateSort[AGGREGATE](keys: Schema, agg: Schema, parent: Operator, yld: Record => Unit, fieldUnit: String => AGGREGATE, aggregateFun: (AGGREGATE, Seq[RField]) => AGGREGATE, toField: AGGREGATE => RField, sorter: Ref): Unit = {
    val table = new mutable.HashMap[RFields, Seq[AGGREGATE]]
    execOp(parent) { rec =>
      val kvs: RFields = rec(keys)
      val aggregate: Seq[AGGREGATE] = table.getOrElseUpdate(kvs, agg.map(fieldUnit))
      table(kvs) = (aggregate, rec(agg).map(f => Seq(f))).zipped.map(aggregateFun)
    }

    def valueToSort(statement: Statement): Object = {
      sorter match {
        case Field(_, field) => throw new UnsupportedOperationException("sorter with field " + field)
        case Property(_, property) => Primitive.applyProperty1(property, statement, environment).head
        case Value(x) => throw new UnsupportedOperationException("sorter with value " + x)
      }
    }

    def sortFunction(v: AGGREGATE): AGGREGATE = {
      v match {
        case v: Seq[Statement] => v.sortWith { case (a1, a2) => valueToSort(a1).toString < valueToSort(a2).toString }.asInstanceOf[AGGREGATE]
      }
    }

    val selected = "category"
    table foreach { case (k: RFields, a: Seq[AGGREGATE]) =>
      //   println(keys)
      //  println(k)
      //  println(agg)
      //  println(a)
      val aSorted: Seq[AGGREGATE] = agg.zip(a).map { case (s: String, a: AGGREGATE) => if (s != selected) a else sortFunction(a) }
      yld(Record(k ++ aSorted.map(toField), keys ++ agg))
    }
  }


  def execQuery(q: Operator): Unit = execOp(q) { _ => }
}



