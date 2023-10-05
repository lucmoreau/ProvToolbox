package org.openprovenance.prov.scala.xplain


import com.fasterxml.jackson.databind.util.StdConverter
import org.apache.logging.log4j.{LogManager, Logger}
import org.openprovenance.prov.scala.iface.Narrative
import org.openprovenance.prov.scala.immutable
import org.openprovenance.prov.scala.immutable.Kind.Kind
import org.openprovenance.prov.scala.immutable.{Document, Kind, Statement}
import org.openprovenance.prov.scala.interop.FileInput
import org.openprovenance.prov.scala.narrator.{XConfig, XplainConfig}
import org.openprovenance.prov.scala.nf.CommandLine.parseDocument
import org.openprovenance.prov.scala.nlg.SentenceMaker
import org.openprovenance.prov.scala.nlgspec_transformer.defs.{Dictionary, Plan}
import org.openprovenance.prov.scala.nlgspec_transformer.{Environment, Language, SpecLoader, specTypes}
import org.openprovenance.prov.scala.primitive.{Keywords, Triple}
import org.openprovenance.prov.scala.query.QueryInterpreter.RField
import org.openprovenance.prov.scala.query.QuerySetup
import org.openprovenance.prov.scala.xplain.RealiserFactory.logger

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

class RealiserFactory(plans:Seq[Plan],
                      dictionaries:Seq[Dictionary],
                      profiles:Map[String, Object],
                      infiles:String=null) {



  def this(plan:Plan, dictionary: Dictionary, profiles:Map[String,Object]) = {
    this(Seq(plan), Seq(dictionary), profiles, null)
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

  val names: Seq[(Kind.Value, Plan)] = plans.map(plan => (QuerySetup.nameMapper(plan.select(plan.select.keys.head)(Keywords.TYPE)),plan)).toSeq

  val index: Map[Kind.Value, Set[Plan]] =names.groupBy(_._1).view.mapValues(x => x.map(_._2).toSet).toMap

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


  private val alternate_files: Map[String, String] =if (infiles!=null) SpecLoader.mapper.readValue(infiles,classOf[Map[String,String]]) else Map[String,String]()

  private val accessors: Map[String,Seq[Statement]]=alternate_files.map{case (s,f) => (s, parseDocument(FileInput(new File(f))).statements().toSeq)}




  def make(doc: Document): Realiser = {
    make(doc.statements().toSeq)
  }

  def make(statements:Seq[immutable.Statement]) : Realiser = {
    new Realiser(statements, accessors)
  }


  class Realiser(statements:Seq[immutable.Statement],
                         documents: Map[String, Seq[immutable.Statement]])  {

    def realise(the_profile: String, templates: Seq[String] = Seq(), format_option: Int=0, allp: Boolean=true): Narrative = {
      if (allp) {
        val narratives: Set[Narrative] = realise_all(statements, Set(Narrative()), the_profile, templates, format_option)
        val narrative = selectBestNarrative(narratives)
        optimiseNarrative(narrative)
      } else {
        realise_one(statements, the_profile, templates, format_option)
      }
    }

    val planQuery: PlanQuery =new PlanQuery(statements, documents)



    final def realisePlan(plan: Plan, the_profile: String, format_option: Int): Seq[((String, String, () => String), Plan, mutable.Set[Triple])] = {
      val context: Map[String, String] = provContext(plan.context)
      val (environment: Environment, all_matching_objects: Seq[Map[String, RField]]) = planQuery.processQuery(plan, dictionaries, profiles, the_profile, context)
      realise(all_matching_objects, plan, environment, format_option)
    }


    final def realise_one(statements: Seq[Statement], the_profile: String, selected_templates: Seq[String], format_option: Int): Narrative = {

      val kinds: Set[Kind] = statements.map(_.enumType).toSet
      val plans: Set[Plan] = kinds.flatMap(kind => index.getOrElse(kind, Set[Plan]())).filter(template => if (selected_templates.isEmpty) true else selected_templates.contains(template.name))

      val new_sentences: Set[((String, String, () => String), Plan, mutable.Set[Triple])] = plans.flatMap(plan => {

        val context: Map[String, String] = provContext(plan.context)
        val (environment: Environment, all_matching_objects: Seq[Map[String, RField]]) = planQuery.processQuery(plan, dictionaries, profiles, the_profile, context)

        logger.debug("realise_one: found objects_ok " + all_matching_objects.size + "  for " + plan.name)

        realise(all_matching_objects, plan, environment, format_option)
      })

      val sentences: List[((String, String, () => String), Plan, mutable.Set[Triple])] = new_sentences.toList
      Narrative(sentences.map { case ((v1, v2, v3), _, _) => v1 }, sentences.map { case ((v1, v2, v3), _, _) => v2 }, sentences.map { case ((v1, v2, v3), _, _) => v3 }, sentences.map { case (_, t, _) => t })
    }


    @tailrec
    final def realise_all(statements: Seq[Statement],
                          accumulator: Set[Narrative],
                          the_profile: String,
                          select_plans: Seq[String],
                          format_option: Int): Set[Narrative] = {

      if (statements.isEmpty) {
        accumulator
      } else {
        val currentStatement = statements.head
        val tail = statements.tail
        val kind: Kind = currentStatement.enumType
        val plans: Set[Plan] = index.getOrElse(kind, Set()).filter(plan => if (select_plans.isEmpty) true else select_plans.contains(plan.name))

        val accumulator2: Set[Narrative] = accumulator.flatMap((narrative_so_far: Narrative) => {

          val new_sentences: Set[((String, String, () => String), Plan, mutable.Set[Triple])] = plans.flatMap(plan => {

            val context: Map[String, String] = provContext(plan.context)
            val environment = Environment(context, dictionaries, profiles, the_profile)
            val all_matching_objects: Seq[Map[String, RField]] = planQuery.processQuery(plan, currentStatement, dictionaries, profiles, the_profile, context)

            logger.debug("realise_all: found objects_ok " + all_matching_objects.size + "  for " + plan.name)

            realise(all_matching_objects, plan, environment, format_option)
          })

          if (new_sentences.isEmpty) { // if cannot translate current statement, then just ignore, and move on
            Set(narrative_so_far)
          } else {
            new_sentences.map(a_sentence => narrative_so_far.add(a_sentence))
          }
        }
        )

        realise_all(tail, accumulator2, the_profile, select_plans, format_option)
      }
    }


    private def realise(all_matching_objects: Seq[Map[String, RField]],
                        plan: Plan,
                        environment: Environment,
                        format_option: Int): Seq[((String, String, () => String), Plan, mutable.Set[Triple])] = {
      all_matching_objects.flatMap(selected_objects => {
        val triples = scala.collection.mutable.Set[Triple]()
        val maker = new SentenceMaker()
        val result: Option[specTypes.Phrase] = maker.transform(selected_objects, plan.sentence, environment, triples, "IGNORE")
        val text: (String, String, () => String) = maker.realisation(result, format_option)
        Some((text, plan, triples))
      })
    }
  }

}

