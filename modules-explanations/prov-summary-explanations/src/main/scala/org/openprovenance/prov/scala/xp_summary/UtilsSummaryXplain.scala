package org.openprovenance.prov.scala.xp_summary

import org.openprovenance.prov.scala.immutable.Format
import org.openprovenance.prov.scala.query.{QueryDirectiveTop, SummaryQueryGenerator}
import org.openprovenance.prov.scala.summary.types.FlatType
import org.openprovenance.prov.scala.summary.{SConfig, SummaryDescriptionJson, SummaryIndex}

import java.io.{BufferedWriter, FileWriter}
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

class UtilsSummaryXplain {

  def exportQueriesForType(desc: SummaryDescriptionJson,
                           config: SConfig,
                           params: Map[String, String],
                           level: Int,
                           allDescriptions: mutable.Map[Int, SummaryDescriptionJson]): Unit = {

    if (config.summary_queries != null) {

      val previousLevels: collection.Set[Int] = allDescriptions.keySet - level


      val previousFlatTypes: Map[Int, Map[Int, Set[FlatType]]] = previousLevels.map(l => l -> allDescriptions(l).getFlatTypes).toMap
      val allTypeMaps: Map[Int, Map[Int, Set[FlatType]]] = previousFlatTypes + (level -> allDescriptions(level).getFlatTypes(level, allDescriptions))
      val allTypeStrings: Map[Int, Map[String, Int]] = allDescriptions.map { case (level, v) => (level, v.typeStrings.map { case (k, v) => (v, k) }) }.toMap
      val allTypeStringsR: Map[Int, Map[Int, String]] = allDescriptions.map { case (level, v) => (level, v.typeStrings) }.toMap
      val allTypeCounts: Map[Int, Map[Int, Int]] = allDescriptions.map { case (level, v) => (level, v.getFeatures.map { case (k, v) => (allTypeStrings(level)(k), v) }) }.toMap

      val typeMap: Map[Int, Set[FlatType]] = allTypeMaps(level)
      val typeStrings: Map[String, Int] = allTypeStrings(level)
      val typeCount: Map[Int, Int] = allTypeCounts(level)


      val selectedTypes: Set[Int] = typeMap.keySet

      println("exportQueriesForType " + level)
      //println(allDescriptions)
      //println(allTypeMaps)
      selectedTypes.foreach(selectedType => {
        queryExporter(desc, config, params, level, allTypeMaps, allTypeStringsR, typeCount, selectedType)
      })

    }
  }

  def queryExporter(desc: SummaryDescriptionJson,
                    config: SConfig,
                    params: Map[String, String],
                    level: Int,
                    allTypeMaps: Map[Int, Map[Int, Set[FlatType]]],
                    allTypeStringsR: Map[Int, Map[Int, String]],
                    typeCount: Map[Int, Int],
                    selectedType: Int): Unit = {
    val params2 = params ++ Map(Format.typeVar -> selectedType.toString)
    val fileName = Format.substParams(config.summary_queries.toString, params2)
    val fw = new FileWriter(fileName)

    fw.write("// query for type " + selectedType + ":\n")
    fw.write("//  type " + selectedType + " is " + desc.typeStrings(selectedType) + "\n")
    val queryString: ArrayBuffer[String] = SummaryQueryGenerator.flatType2Query(selectedType, level, allTypeMaps, allTypeStringsR, typeCount, QueryDirectiveTop())
    fw.write(queryString.mkString)
    fw.write("\n")
    fw.write("//end\n\n")
    fw.close()
  }

  def exportToJsonDescription(index: SummaryIndex,
                              config: SConfig,
                              params: Map[String, String],
                              level: Int,
                              allDescriptions: mutable.Map[Int, SummaryDescriptionJson]): Unit = {
    val file = config.types
    val fileName = Format.substParams(file.toString, params)
    val bw = new BufferedWriter(new FileWriter(fileName))
    val summaryDescriptionJson: SummaryDescriptionJson = index.exportToJsonDescription(bw)
    bw.close()
    allDescriptions += (level -> summaryDescriptionJson)

    // and now optionally export queries for all types
    exportQueriesForType(summaryDescriptionJson, config, params, level, allDescriptions)
  }


}