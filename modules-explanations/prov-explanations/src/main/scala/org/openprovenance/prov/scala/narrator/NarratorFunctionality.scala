package org.openprovenance.prov.scala.narrator

import org.apache.logging.log4j.{LogManager, Logger}
import org.openprovenance.prov.model.Namespace
import org.openprovenance.prov.scala.immutable.{Document, QualifiedName, Statement, StatementOrBundle}

import EventOrganiser.{NLG_PREFIX, NLG_URI, findOrder, gensym}
import org.openprovenance.prov.scala.nlg._
import org.openprovenance.prov.scala.xplain.{Narrative, RealiserFactory}
import org.openprovenance.prov.validation.EventMatrix

import java.util
import scala.collection.mutable
import scala.util.matching.Regex


object NarratorFunctionality {

  def linearConfig(): XConfig = XplainConfig(linear = true)
  def randomConfig(): XConfig = XplainConfig()


  val logger: Logger = LogManager.getLogger("Narrator")

/*
  def getTextOnly(text: Map[String, Narrative]): Map[String, List[String]] = {
    text.map { case (f, n) => (f, n.sentences) }
  }

  def getSnlgOnly(text: Map[String, Narrative]): Map[String, List[String]] = {
    text.map { case (f, n) => (f, n.snlgs) }
  }

 */

  def narrate(doc:Document, config:XConfig): (Map[String,Narrative], Document, EventMatrix, EventsDescription) = {

    import scala.jdk.CollectionConverters._
    val doc1=Document(doc,gensym,NLG_PREFIX,NLG_URI)
    val newEntities=EventOrganiser.addEntitiesToAgents(doc1.statements())

    val newStatements: Iterable[StatementOrBundle] =doc1.statements() ++ newEntities

    val namespace: Namespace = doc1.namespace
    val doc2:Document=new Document(newStatements,namespace)


    val (mat,idx: util.Hashtable[String, Integer],evts: Map[String, Statement])=findOrder(doc2)

    val activityOrder: mutable.Map[String,LinearOrder] =scala.collection.mutable.Map()
    val amap:util.Map[String,Integer]=idx
    val idx2: Set[(String, Integer)] =amap.asScala.toSet
    val idx3: Map[Integer, String] =idx2.map{ case (s,i) => (i,s)}.toMap
    val allEvents=amap.values()

    val documentProcessor = new EventOrganiser(mat)
    val dp = new ActivityProcessor(mat,idx3, evts)
    val order3: LinearOrder =documentProcessor.linearizeEvents(allEvents.asScala.toSet,NoEvent())
    dp.filterPerActivity(order3,activityOrder)
    //(activityOrder)

    val activityInfos: Map[String, ActivityInfo] = dp.toActivityInfo(activityOrder)
    //println(activityInfos)

    val sorted: Array[ActivityInfo] =scala.util.Sorting.stableSort(activityInfos.values.toSeq)

    val seen:mutable.Set[QualifiedName]=scala.collection.mutable.Set()
    val theStatements: Set[Statement] =doc2.statements().toSet

    val theOrderedStatements: List[Statement] =
      if (config.linear) {
        dp.convertToSequence(order3,theStatements,seen)

      } else {
        List()
      }
    val remaining: Iterable[Statement] = if (config.linear) {
      theStatements -- theOrderedStatements -- newEntities
    } else {
      List()
    }

    val descriptor=EventsDescription(idx3, evts, order3, sorted, activityInfos, theOrderedStatements)


    val text: Map[String, Narrative] =if (config.linear) {
      val text1: Map[String, Narrative] =realise(config, theOrderedStatements,allp = true)
      val remaining2: Seq[Statement] =remaining.toSeq
      if (remaining2.isEmpty) {
        text1
      } else {
        println("Unordered statements: " + remaining2.mkString("|",",","|"))
        val text2: Map[String, Narrative] = realise(config, remaining2, allp = true).map { case (k, v) => ("+" + k, v) }
        val result: Map[String, Narrative] =text1  ++ text2
        result
      }
    } else {
      realise(config,doc2.statements().toSeq,allp = true)
    }


    (text,doc2,mat,descriptor)
  }

  def realise(config: XConfig, theOrderedStatements: Seq[Statement], allp: Boolean): Map[String, Narrative]  = {
    if (config.language.isEmpty) {
      val config1=new XplainConfig(config)
      val config2: XConfig =config1.copy(language = Seq("nlg/templates/provbasic/provbasic.json"), languageAsFilep = false)
      logger.debug("config2 is " + config2)
      realise_aux(config2, theOrderedStatements, config.format_option, allp)
    } else {
      logger.debug("With vocabulary " + config.language.toList)
      realise_aux(config, theOrderedStatements,config.format_option, allp)
    }
  }

  val Bracketed: Regex = """\[.*?\]""".r
  def extractBracketContents(s: String): Seq[String] =
    (Bracketed findAllIn s).map(s => s.substring(1,s.length-1)).toSeq

  private def realise_aux(config: XConfig, theOrderedStatements: Seq[Statement], format_option: Int, allp: Boolean): Map[String, Narrative] = {
    val factory=new RealiserFactory(config)
    val realiser: factory.Realiser = factory.make(theOrderedStatements)
    if (!allp && config.batch_templates.isDefined) {
      config.batch_templates match {
        case Some(s:String) =>
          val batch=extractBracketContents(s)
          batch.map(b => {
            val compactNarrative: Narrative = realiser.realise(config.profile, b.split(","), format_option, allp)
            b -> compactNarrative.copy(sentences=compactNarrative.sentences.reverse)
          }).toMap
        case _ => throw new UnsupportedOperationException // never here
      }
    } else {

      val compactNarrative: Narrative = realiser.realise(config.profile, config.selected_templates, format_option, allp)
      Map(config.selected_templates.mkString(",") ->  compactNarrative.copy(sentences=compactNarrative.sentences.reverse))
    }
  }






}
