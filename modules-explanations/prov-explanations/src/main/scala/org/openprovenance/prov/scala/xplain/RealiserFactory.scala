package org.openprovenance.prov.scala.xplain


import com.fasterxml.jackson.databind.util.StdConverter
import org.apache.logging.log4j.{LogManager, Logger}
import org.openprovenance.prov.scala.immutable
import org.openprovenance.prov.scala.immutable.Kind.Kind
import org.openprovenance.prov.scala.immutable._
import org.openprovenance.prov.scala.interop.FileInput
import org.openprovenance.prov.scala.narrator.{XConfig, XplainConfig}
import org.openprovenance.prov.scala.nf.CommandLine.parseDocument
import org.openprovenance.prov.scala.nlg.SentenceMaker
import org.openprovenance.prov.scala.nlgspec_transformer.defs.{Dictionary, Plan}
import org.openprovenance.prov.scala.nlgspec_transformer.{Environment, Language, SpecLoader, specTypes}
import org.openprovenance.prov.scala.primitive.{Keywords, Triple}
import org.openprovenance.prov.scala.query.{Processor, QuerySetup, StatementAccessor, StatementIndexer}
import org.openprovenance.prov.scala.utilities.{WasDerivedFromPlus, WasDerivedFromStar}

import java.io.File
import scala.annotation.tailrec
import scala.collection.mutable

class TreeConverter extends StdConverter[() => String, String] {
  override def convert(in: () => String): String = {
    in()
  }
}


object RealiserFactory {
  val logger: Logger = LogManager.getLogger(classOf[RealiserFactory])

}

class RealiserFactory(templates:Seq[Plan], dictionaries:Seq[Dictionary], profiles:Map[String, Object], infiles:String=null) {

  import RealiserFactory._


  def this(template:Plan, dictionary: Dictionary, profiles:Map[String,Object]) = {
    this(Seq(template), Seq(dictionary), profiles, null)
  }

  def this(triple: (Seq[Plan],Seq[Dictionary], Map[String, Object]), infiles:String) ={
    this(triple._1,triple._2,triple._3, infiles)
  }

  def this(config:XConfig) = {
    this(Language.read(config.language,config.languageAsFilep), config.infiles)
  }

  def this(s:String, filep:Boolean) = {
    this(XplainConfig(language=Seq(s),languageAsFilep = filep))
  }

  def this(seq:Seq[String], filep:Boolean) = {
    this(XplainConfig(language=seq,languageAsFilep = filep))
  }

  val names: Seq[(immutable.Kind.Value, Plan)] =templates.map(template => (QuerySetup.nameMapper(template.select(template.select.keys.head)(Keywords.TYPE)),template)).toSeq

  val index: Map[immutable.Kind.Value, Set[Plan]] =names.groupBy(_._1).view.mapValues(x => x.map(_._2).toSet).toMap

  def selectBestNarrative(narratives: Set[Narrative]): Narrative = {
    var count=1

    val sorted: Array[Narrative] =scala.util.Sorting.stableSort(narratives.toSeq)
    sorted.foreach(narrative => {
      println(count.toString + narrative.sentences.reverse)
      println("---")
      val tr: Set[Triple] =narrative.coverage.map(_.toSet).fold(Set())((a1, a2)=> a1 ++ a2)
      println(tr)
      println("------------------------")
      count=count+1
    })
    sorted.last
  }


  def optimiseNarrative(narrative: Narrative): Narrative = {
    narrative
  }

  private def provContext(context: Map[String, String]): Map[String, String] = {
    val context2 = context + ("prov" -> "http://www.w3.org/ns/prov#")
    context2
  }

  /*
  val TOKEN_TYPE="@type"
  val TOKEN_OBJECT="@object"
  val TOKEN_OBJECT1="@object1"
  */


  private def processQuery(template: Plan, s: Statement, engine: Processor): List[Map[String, engine.RField]] = {

    val headStatementId=template.select.keys.head
    val headStatementType=template.select(headStatementId)(Keywords.TYPE)
    val headStatementKind:   Kind.Value = QuerySetup.nameMapper(headStatementType)
    val actualStatementKind: Kind.Value = Kind.toKind(s.getKind)

    if (actualStatementKind==headStatementKind) {

      val query: String = template.query match {
        case s: String => s
        case a: Array[String] => a.mkString(" \n")
        case a: Seq[String] @unchecked => a.mkString(" \n")
        case _ => println(template.name); println(template); throw new UnsupportedOperationException("incorrect query for " + template.name)
      }


      val set: mutable.Set[engine.Record] = engine.newRecords()

      if (query=="None") {
        List(Map())
      } else {
        engine.evalAccumulate(query, set)
        val statements: List[Map[String, engine.RField]] = set.toSeq.map(engine.toMap2).filter(m => engine.toStatement(m(headStatementId)) == s).toList
        statements
      }
    } else {
      List()
    }
  }


  private def processQuery(template: Plan, engine: Processor): List[Map[String, engine.RField]] = {

    val query: String = template.query match {
      case s: String => s
      case a: Array[String] => a.mkString(" \n")
      case a: Seq[String] @unchecked => a.mkString(" \n")
      case _ => println(template.name); println(template); throw new UnsupportedOperationException("incorrect query for " + template.name)
    }

    val set: mutable.Set[engine.Record] = engine.newRecords()

    engine.evalAccumulate(query, set)
    val statements: List[Map[String, engine.RField]] = set.toSeq.map(engine.toMap2(_)).toList

    statements

  }


  val alternate_files: Map[String, String] =if (infiles!=null) SpecLoader.mapper.readValue(infiles,classOf[Map[String,String]]) else Map[String,String]()

  val accessors: Map[String,Seq[Statement]]=alternate_files.map{case (s,f) => (s, parseDocument(FileInput(new File(f))).statements().toSeq)}




  def make(doc: Document): Realiser = {
    make(doc.statements().toSeq)
  }

  def make(statements:Seq[immutable.Statement]) : Realiser = {
    new Realiser(statements, accessors)
  }


  def makeStatementAccessor(statements:Seq[immutable.Statement]): StatementAccessor = {

    val idx: Map[immutable.Kind.Value, List[Statement]] = StatementIndexer.splitByStatementType(statements)

    val (allDerivationsPlus, allDerivationsStar): (List[WasDerivedFromPlus], List[WasDerivedFromStar]) = StatementIndexer.computeDerivationClosure(idx)


    new StatementAccessor {
      override def findStatement(type_string: String): List[Statement] = {
        val kind = QuerySetup.nameMapper(type_string)
        if (kind == Kind.winfl) {
          type_string match {
            case "provext:WasDerivedFromPlus" => allDerivationsPlus
            case "provext:WasDerivedFromStar" => allDerivationsStar
          }

        } else {
          idx(kind)
        }
      }
    }

  }

  class Realiser(statements:Seq[immutable.Statement], documents: Map[String, Seq[immutable.Statement]])  {
    val accessor: StatementAccessor = makeStatementAccessor(statements)
    val accessors: Map[String, StatementAccessor] = documents.map{case (s:String, seq:Seq[Statement]) => (s,makeStatementAccessor(seq))}

    def realise(the_profile: String, templates: Seq[String] = Seq(), format_option: Int=0, allp: Boolean=true): Narrative = {
      if (allp) {
        val narratives: Set[Narrative] = realise_all(statements, Set(Narrative()), the_profile, templates, format_option)
        val narrative = selectBestNarrative(narratives)
        val compactNarrative = optimiseNarrative(narrative)
        compactNarrative
      } else {
        val narratives: Narrative = realise_one(statements, the_profile, templates, format_option)
        narratives
      }
    }

    val statementAccessorForDocument:(Option[String]=>StatementAccessor) = {
      case None => accessor
      case Some(s) => accessors(s)
    }

    final def realisePlan(template: Plan, the_profile: String, format_option: Int): Seq[(String, String, () => String)] = {
      val context: Map[String, String] = provContext(template.context)
      val environment = Environment(context, dictionaries, profiles, the_profile)
      val engine = new Processor(statementAccessorForDocument, environment)
      val all_matching_objects: Seq[Map[String, engine.RField]] = processQuery(template, engine)
      all_matching_objects.flatMap((selected_objects: Map[String, engine.RField]) => {

        val triples = scala.collection.mutable.Set[Triple]()

        val maker = new SentenceMaker(engine)

        val result: Option[specTypes.Phrase] = maker.transform(selected_objects, template.sentence, environment, triples, "IGNORE")

        val text: (String, String, () => String) = maker.realisation(result, format_option)


        logger.debug("plan realisation: " + text)
        Some(text)
      })

    }


    final def realise_one(statements: Seq[Statement],  the_profile: String, selected_templates: Seq[String], format_option: Int): Narrative = {

      val kinds: Set[Kind] =statements.map(_.enumType).toSet
      val templates: Set[Plan] =kinds.flatMap(kind => index.getOrElse(kind, Set[Plan]())).filter(template => if (selected_templates.isEmpty) true else selected_templates.contains(template.name))

      val new_sentences: Set[((String, String, () => String), Plan, mutable.Set[Triple])] = templates.flatMap(template => {


        val context: Map[String, String] = provContext(template.context)
        val environment = Environment(context, dictionaries, profiles, the_profile)

//        println("environment " + environment)

        val engine = new Processor(statementAccessorForDocument, environment)

        val all_matching_objects: Seq[Map[String, engine.RField]] = processQuery(template, engine)


        logger.debug("realise_one: found objects_ok " + all_matching_objects.size + "  for " + template.name)

        all_matching_objects.flatMap(selected_objects => {

         // logger.debug("==> " + selected_objects)
          val triples = scala.collection.mutable.Set[Triple]()

          val maker = new SentenceMaker(engine)

          val result: Option[specTypes.Phrase] = maker.transform(selected_objects, template.sentence, environment, triples, "IGNORE")

          val text: (String, String, () => String) = maker.realisation(result, format_option)

          //println(triples)

          Some((text, template, triples))
              })


      })

      val sentences: List[((String, String, () => String), Plan, mutable.Set[Triple])] = new_sentences.toList
      Narrative(sentences.map { case ((v1, v2, v3), _, _) => v1 }, sentences.map { case ((v1, v2, v3), _, _) => v2 }, sentences.map { case ((v1, v2, v3), _, _) => v3 }, sentences.map { case (_, t, _) => t })
    }


    @tailrec
    final def realise_all(statements: Seq[Statement], accumulator: Set[Narrative],  the_profile: String, selected_templates: Seq[String], format_option: Int): Set[Narrative] = {

      if (statements.isEmpty) {

        accumulator

      } else {

        val currentStatement = statements.head
        val tail = statements.tail

        val kind: Kind = currentStatement.enumType

        val templates: Set[Plan] = index.getOrElse(kind, Set()).filter(template => if (selected_templates.isEmpty) true else selected_templates.contains(template.name))

        val accumulator2: Set[Narrative] =
          accumulator.flatMap((narrative_so_far: Narrative) => {

            val new_sentences: Set[((String, String, () => String), Plan, mutable.Set[Triple])] = templates.flatMap(template => {


              val context: Map[String, String] = provContext(template.context)
              val environment = Environment(context, dictionaries, profiles, the_profile)


              val engine = new Processor(statementAccessorForDocument, environment)

              val all_matching_objects: Seq[Map[String, engine.RField]] = processQuery(template, currentStatement, engine)


              logger.debug("realise_all: found objects_ok " + all_matching_objects.size + "  for " + template.name)

              all_matching_objects.flatMap(selected_objects => {

                val triples = scala.collection.mutable.Set[Triple]()

                val maker = new SentenceMaker(engine)

                val result: Option[specTypes.Phrase] = maker.transform(selected_objects, template.sentence, environment, triples, "IGNORE")

                val text = maker.realisation(result, format_option)

                //println(triples)

                Some((text, template, triples))
              })


            })

            if (new_sentences.isEmpty) { // if cannot translate current statement, then just ignore, and move on
              Set(narrative_so_far)
            } else {
              new_sentences.map(a_sentence => narrative_so_far.add(a_sentence))
            }
          }
          )

        realise_all(tail, accumulator2, the_profile, selected_templates, format_option)
      }
    }

  }

}

