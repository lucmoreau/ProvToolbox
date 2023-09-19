package org.openprovenance.prov.scala.nlg

import java.util

import org.apache.logging.log4j.{LogManager, Logger}
import org.openprovenance.prov.model.Namespace
import org.openprovenance.prov.scala.immutable.{Document, QualifiedName, Statement, StatementOrBundle}
import org.openprovenance.prov.scala.interop.{Input, Output}
import org.openprovenance.prov.scala.nf.CommandLine
import org.openprovenance.prov.scala.nlg.EventOrganiser.{NLG_PREFIX, NLG_URI, findOrder, gensym}
import org.openprovenance.prov.validation.EventMatrix

import scala.collection.mutable
import scala.util.matching.Regex


case class EventsDescription(idx: Map[Integer, String],
                             evt: Map[String, Statement],
                             order: LinearOrder,
                             timeline: Array[ActivityInfo],
                             activityInfos: Map[String, ActivityInfo],
                             theOrderedStatements: List[Statement]) {
  val theOrderStatementIds: Seq[String] =theOrderedStatements.map(_.id.getUri())
}

trait Config {
  def snlg: Output


  def languageAsFilep: Boolean

  def selected_templates: Seq[String]

  def profile: String

  def batch_templates: Option[String]

  def language: Seq[String]

  def linear: Boolean

  def infile: Input

  def format_option: Int

  def infiles: String

}

case class AConfig(snlg: Output=null,
                   languageAsFilep: Boolean=true,
                   selected_templates: Seq[String]=Seq(),
                   profile: String=null,
                   batch_templates: Option[String]=None,
                   language: Seq[String]=Seq(),
                   linear:Boolean=false,
                   infile:Input=null,
                   infiles:String=null,
                   format_option:Int=0) extends Config {
  def this(c: Config) {
    this(c.snlg, c.languageAsFilep, c.selected_templates, c.profile, c.batch_templates, c.language, c.linear, c.infile, c.infiles, c.format_option)
  }

}



object Narrator {

  def linearConfig(): Config = AConfig(linear = true)
  def randomConfig(): Config = AConfig()


  val logger: Logger = LogManager.getLogger("Narrator")




  def explainFromConfig(config: Config): (Map[String,Narrative],Document) = {

    val in: Input = config.infile
    val doc = CommandLine.parseDocument(in)

    (explain(doc,config), doc)

  }

  def explain(doc: Document, config: Config): Map[String, Narrative] = {

    val doc1 = Document(doc, gensym, NLG_PREFIX, NLG_URI)
    val newEntities = EventOrganiser.addEntitiesToAgents(doc1.statements())

    val newStatements: Iterable[StatementOrBundle] = doc1.statements ++ newEntities

    val namespace: Namespace = doc1.namespace
    val doc2: Document = new Document(newStatements, namespace)


    val theStatements: Seq[Statement] = doc2.statements().toSeq


    realise(config, theStatements,allp = false)

  }

  def getTextOnly(text: Map[String, (List[String], List[String], List[() => String])]): Map[String, List[String]] = {
      text.map { case (f, (v1, v2, v3)) => (f, v1) }
  }

  def getSnlgOnly(text: Map[String, (List[String], List[String], List[() => String])]): Map[String, List[String]] = {
      text.map { case (f, (v1, v2, v3)) => (f, v2) }
  }

  def getTextOnly2(text: Map[String, Narrative]): Map[String, List[String]] = {
    text.map { case (f, n) => (f, n.sentences) }
  }

  def getSnlgOnly2(text: Map[String, Narrative]): Map[String, List[String]] = {
    text.map { case (f, n) => (f, n.snlgs) }
  }


  def narrateFromConfig(config: Config): (Map[String, Narrative], Document, EventMatrix, EventsDescription) = {

    val in: Input = config.infile
    val doc = CommandLine.parseDocument(in)

    narrate(doc,config)

  }

  def narrate(doc:Document, config:Config): (Map[String,Narrative] , Document, EventMatrix, EventsDescription) = {

    import scala.jdk.CollectionConverters._
    val doc1=Document(doc,gensym,NLG_PREFIX,NLG_URI)
    val newEntities=EventOrganiser.addEntitiesToAgents(doc1.statements())

    val newStatements: Iterable[StatementOrBundle] =doc1.statements ++ newEntities

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

  def realise(config: Config, theOrderedStatements: Seq[Statement], allp: Boolean): Map[String, Narrative]  = {
    if (config.language.isEmpty) {
      val config1=new AConfig(config)
      val config2: Config =config1.copy(language = Seq("nlg/templates/provbasic/provbasic.json"), languageAsFilep = false)
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

  def realise_aux(config: Config, theOrderedStatements: Seq[Statement], format_option: Int, allp: Boolean): Map[String, Narrative] = {
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



  def narrate1(doc:Document, config: Config): Map[String, Narrative] = {
    val (text,_,_,_)=narrate(doc,config)

    text
  }

  def narrate2(doc:Document, config: Config): Map[String, List[String]] = {
    getTextOnly2(narrate1(doc,config))
  }

  def narrate2string(doc:Document, config: Config): Map[String, String] = {
    narrate2(doc,config).map{case (k,v) => (k, v.mkString("\n"))}
  }

}
