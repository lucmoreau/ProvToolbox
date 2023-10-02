package org.openprovenance.prov.scala.query

import org.openprovenance.prov.scala.summary.types.{Act, Ag, BasicFlatType, CompositeFlatType, Ent, FlatType, NumberedFlatType, Prim, SummaryTypesNames}

import scala.collection.mutable

trait SummaryQueryGenerator extends QueryInterpreter {

  sealed abstract class QueryDirective(val name: Option[String])

  case class QueryDirectiveTop() extends QueryDirective(None)

  case class QueryDirectiveContinue(s: String) extends QueryDirective(Some(s))

  /* Class used to append a number at the end of an identifier, to ensure its uniqueness */
  class GensymGenerator {
    var table: Map[String, Set[String]] =Map()
    def newId(s:String):String = {
      table.get(s) match {
        case None =>
          table=table + (s -> Set(s))
          s
        case Some(set) =>
          val s2=s+"_"+set.size
          table=table + (s ->(set + s2))
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
                     gensym: GensymGenerator=new GensymGenerator()): mutable.ArrayBuffer[String] = {

    val result: mutable.ArrayBuffer[String] = mutable.ArrayBuffer()
    if (selectedType == (-1)) {
      result += ("// found -1 for level " + level + "\n")
      return result
    }

    var top = directive.isInstanceOf[QueryDirectiveTop]

    val flatTypes: Set[FlatType] = flatTypeMap(level)(selectedType)


    val tabulation = " " * nesting

    if (top) {
      result += "\n\n"
      result += "prefix provext <http://openprovenance.org/prov/extension#>\n"
      result += "prefix t <http://openprovenance.org/summary/xyz/types#>\n"
      result += "prefix sum <http://openprovenance.org/summary/ns#>\n\n"
    }


    var latest_directive=directive




    val sortedFlatTypes=flatTypes.toSeq.sortWith(lessThan)
    for (ft <- sortedFlatTypes) {
      latest_directive=flatType2Query2(ft,
                                      result,
                                      level,
                                      selectedType,
                                      flatTypeMap,
                                      allTypeStringsR,
                                      typeCount,
                                      latest_directive,
                                      nesting,
                                      where,
                                      comments,
                                      top,
                                      tabulation,
                                      gensym)
      top=false
    }


    if (directive.isInstanceOf[QueryDirectiveTop]) {
      result += "\n\nwhere "
      var first = true
      where.foreach(l => {
        if (first) {
          first = false
        } else {
          result += "and   "
        }
        result += (l + "\n")
      })

      result += "\n"
      comments.foreach(l => {
        result += (l + "\n")
      })

      result += "\n"
    }

    result

  }

  def lessThan(t1: FlatType, t2: FlatType):Boolean = {
    (t1, t2) match {
      case (BasicFlatType(l1, b1), BasicFlatType(l2, b2)) => l1 <= l2
      case (BasicFlatType(l1, b1), _) => true
      case (_, BasicFlatType(l1, b1)) => false
      case (CompositeFlatType(l1, NumberedFlatType(lnext1, next1)), CompositeFlatType(l2, NumberedFlatType(lnext2, next2))) =>
        if (l1 == l2)  lnext1 <= lnext2  else l1 < l2
    }
  }


  def flatType2Query2(ft: FlatType,
                      result: mutable.ArrayBuffer[String],
                      level: Int,
                      selectedType: Int,
                      flatTypeMap: Map[Int, Map[Int, Set[FlatType]]],
                      allTypeStringsR: Map[Int, Map[Int, String]],
                      typeCount: Map[Int, Int],
                      directive: QueryDirective,
                      nesting: Int = 0,
                      where: mutable.ArrayBuffer[String],
                      comments: mutable.ArrayBuffer[String],
                      top: Boolean,
                      tabulation: String,
                      gensym: GensymGenerator): QueryDirective = {
    ft match {
      case BasicFlatType(l, b) =>
        if (top) {
          result += (tabulation + "select * ")
        }
        b match {

          case Prim(_) =>  directive

          case Ent() =>
            val newEntityId = "ent_%d_%d".format(level, selectedType)
            directive match {
              case QueryDirectiveTop() => result += (tabulation + "from %s a prov:Entity\n".format(newEntityId))
              case QueryDirectiveContinue(s) =>
                result += (tabulation + "from %s a prov:Entity\n".format(newEntityId))
                result += (tabulation + " join %s=%s.id\n".format(s, newEntityId))
            }
            if (directive.isInstanceOf[QueryDirectiveTop]) where += "%s[sum:nbr] = %d".format(newEntityId, selectedType)
            comments += "//%s has type %s".format(newEntityId, allTypeStringsR(level)(selectedType))
            QueryDirectiveContinue(newEntityId)

          case Act() =>
            val newActivityId = "act_%d_%d".format(level, selectedType)
            directive match {
              case QueryDirectiveTop() => result += (tabulation + "from %s a prov:Activity\n".format(newActivityId))
              case QueryDirectiveContinue(s) =>
                result += (tabulation + "from %s a prov:Activity\n".format(newActivityId))
                result += (tabulation + " join %s=%s.id\n".format(s, newActivityId))
            }
            if (directive.isInstanceOf[QueryDirectiveTop]) where += "%s[sum:nbr] = %d".format(newActivityId, selectedType)
            comments += "//%s has type %s".format(newActivityId, allTypeStringsR(level)(selectedType))
            QueryDirectiveContinue(newActivityId)

          case Ag() =>
            val newAgentId = "ag_%d_%d".format(level, selectedType)
            directive match {
              case QueryDirectiveTop() => result += (tabulation + "from %s a prov:Agent\n".format(newAgentId))
              case QueryDirectiveContinue(s) =>
                result += (tabulation + "from %s a prov:Agent\n".format(newAgentId))
                result += (tabulation + " join %s=%s.id\n".format(s, newAgentId))
            }
            if (directive.isInstanceOf[QueryDirectiveTop]) where += "%s[sum:nbr] = %d".format(newAgentId, selectedType)
            comments += "//%s has type %s".format(newAgentId, allTypeStringsR(level)(selectedType))
            QueryDirectiveContinue(newAgentId)

        }

      case CompositeFlatType(l, NumberedFlatType(lnext, next)) => l match {
        case MEM =>
          val newMemId = gensym.newId("mem_%d_%d".format(level, selectedType))
          result += (tabulation + "from %s a provext:HadMember\n".format(newMemId))
          result += (tabulation + " join %s.id=%s.collection\n".format(directive.asInstanceOf[QueryDirectiveContinue].s, newMemId))
          result += tabulation
          result ++= flatType2Query(next, lnext, flatTypeMap, allTypeStringsR, typeCount, QueryDirectiveContinue("%s.entity".format(newMemId)), nesting + 1, where, comments, gensym)

        case USD =>
          val usedId = gensym.newId("mem_%d_%d".format(level, selectedType))
          result += (tabulation + "from %s a prov:Used\n".format(usedId))
          result += (tabulation + " join %s.id=%s.activity\n".format(directive.asInstanceOf[QueryDirectiveContinue].s, usedId))
          result += tabulation
          result ++= flatType2Query(next, lnext, flatTypeMap, allTypeStringsR, typeCount, QueryDirectiveContinue("%s.entity".format(usedId)), nesting + 1, where, comments, gensym)

        case WGB =>
          val wgbId = gensym.newId("wgb%d_%d".format(level, selectedType))
          result += (tabulation + "from %s a prov:WasGeneratedBy\n".format(wgbId))
          result += (tabulation + " join %s.id=%s.activity\n".format(directive.asInstanceOf[QueryDirectiveContinue].s, wgbId))
          result += tabulation
          result ++= flatType2Query(next, lnext, flatTypeMap, allTypeStringsR, typeCount, QueryDirectiveContinue("%s.entity".format(wgbId)), nesting + 1, where, comments, gensym)

        case WAT =>
          val newWatId = gensym.newId("wat_%d_%d".format(level, selectedType))
          result += (tabulation + "from %s a prov:WasAttributedTo\n".format(newWatId))
          result += (tabulation + " join %s.id=%s.entity\n".format(directive.asInstanceOf[QueryDirectiveContinue].s, newWatId))
          result += tabulation
          result ++= flatType2Query(next, lnext, flatTypeMap, allTypeStringsR, typeCount, QueryDirectiveContinue("%s.agent".format(newWatId)), nesting + 1, where, comments, gensym)

        case SPEC =>
          val newSpecId = gensym.newId("spec_%d_%d".format(level, selectedType))
          result += (tabulation + "from %s a provext:SpecializationOf\n".format(newSpecId))
          result += (tabulation + " join %s.id=%s.specificEntity\n".format(directive.asInstanceOf[QueryDirectiveContinue].s, newSpecId))
          result += tabulation
          result ++= flatType2Query(next, lnext, flatTypeMap, allTypeStringsR, typeCount, QueryDirectiveContinue("%s.generalEntity".format(newSpecId)), nesting + 1, where, comments, gensym)

        case WDF =>
          val newWdfId = gensym.newId("wdf_%d_%d".format(level, selectedType))
          result += (tabulation + "from %s a prov:WasDerivedFrom\n".format(newWdfId))
          result += (tabulation + " join %s.id=%s.generatedEntity\n".format(directive.asInstanceOf[QueryDirectiveContinue].s, newWdfId))
          result += tabulation
          result ++= flatType2Query(next, lnext, flatTypeMap, allTypeStringsR, typeCount, QueryDirectiveContinue("%s.usedEntity".format(newWdfId)), nesting + 1, where, comments, gensym)
      }
      directive
    }
  }



}
