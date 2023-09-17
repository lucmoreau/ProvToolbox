package org.openprovenance.prov.scala.summary

import org.openprovenance.prov.model.Namespace
import org.openprovenance.prov.scala.immutable.{Document, Format, Indexer}
import org.openprovenance.prov.scala.interop.{Format2, Input, Output}
import scala.collection.parallel.CollectionConverters._

object SummaryAPI {

  def sum(doc: Document, config: SummaryConfig): (Indexer, TypePropagator) = {
    Namespace.withThreadNamespace(doc.namespace)
    val ind: Indexer = new Indexer(doc, Vector(), true) // TODO LUC Weak Inference flag, make it a cmd line flag

    if (config.level0 != null) {
      val level0 = Level0Mapper(config.level0)
      val s = new TypePropagator(doc, config.triangle, config.always_with_type_0, !config.prov_constraints_inference, config.primitivep, level0)
      (ind, s)
    } else {
      val s = new TypePropagator(doc, config.triangle, config.always_with_type_0, config.primitivep)
      (ind, s)
    }
  }

  def sum(doc: Document, config: SummaryConfig, level0: Level0Mapper): (Indexer, TypePropagator) = {
    Namespace.withThreadNamespace(doc.namespace)
    val ind = new Indexer(doc)
    if (level0 != null) {
      val s = new TypePropagator(doc, config.triangle, config.always_with_type_0, !config.prov_constraints_inference, config.primitivep, level0)
      (ind, s)
    } else {
      val s = new TypePropagator(doc, config.triangle, config.always_with_type_0, config.primitivep)
      (ind, s)
    }
  }

  def makeSummaryIndex(config: SummaryConfig,
                       s: TypePropagator,
                       ind: Indexer,
                       level: Int,
                       params: Map[String, String],
                       summaryDescriptionJson0: Option[SummaryDescriptionJson]): SummaryIndex = {
    //val agg_provtypes_from_to=s.aggregateProvTypesToLevel(level)
    //val summary=new SummaryIndexes(s, ind.succ, agg_provtypes_from_to)

    val summaryConstructor = if (config.filter.isEmpty) s.getConstructorForLevel(level, config.nsBase, config.prettyMethod, config.aggregatep, config.aggregate0p, config.withLevel0Description)
    else s.getConstructorForLevel(level, config.filter.toSet, config.maxlevel, config.nsBase, config.prettyMethod, config.withLevel0Description)

    val summaryIndex = summaryConstructor.makeIndex()

    summaryIndex

  }

  def outputer(doc: Document, config: SummaryConfig): Unit = {
    val theformats = config.theOutputFormats()

    config.outfiles.zip(theformats).par foreach { case (o: Output, format: Format.Format) => Format2.outputers(format).output(doc, o, Map[String, String]()) }

  }
}
