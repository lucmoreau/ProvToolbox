package org.openprovenance.prov.scala.query

import org.openprovenance.prov.scala.immutable.Statement
import org.openprovenance.prov.scala.interop.Output
import org.openprovenance.prov.scala.nlg.Environment
import org.openprovenance.prov.scala.summary.types.FlatType

import scala.collection.mutable

trait StatementAccessor {
  def findStatement(type_string: String): List[Statement]
}

class Processor (finder:Option[String]=>StatementAccessor, env: Environment) {
  def exportToJson(queryResult: Output, set: mutable.Set[Record]) = ???

  def toFields(set: mutable.Set[Record]): Set[RField] = ???

  def evalAccumulate(queryContents: String, set: mutable.Set[Record]) = ???

  def newRecords(): mutable.Set[Record] = ???

  case class Record(fields: RField, schema: Object)

  case class RField()
}


trait SummaryQueryGenerator {

  sealed abstract class QueryDirective(val name: Option[String])

  case class QueryDirectiveTop() extends QueryDirective(None)

  class GensymGenerator {
    var table: Map[String, Set[String]] = Map()

    def newId(s: String): String = {
      table.get(s) match {
        case None =>
          table = table + (s -> Set(s))
          s
        case Some(set) =>
          val s2 = s + "_" + set.size
          table = table + (s -> (set + s2))
          s2
      }
    }
  }


  def flatType2Query(selectedType: Int,
                     level: Int,
                     flatTypeMap: Map[Int, Map[Int, Set[FlatType]]],
                     allTypeStringsR: Map[Int, Map[Int, String]],
                     typeCount: Map[Int, Int],
                     directive: QueryDirective,
                     nesting: Int = 0,
                     where: mutable.ArrayBuffer[String] = mutable.ArrayBuffer(),
                     comments: mutable.ArrayBuffer[String] = mutable.ArrayBuffer(),
                     gensym: GensymGenerator = new GensymGenerator()): mutable.ArrayBuffer[String] =  ???

}