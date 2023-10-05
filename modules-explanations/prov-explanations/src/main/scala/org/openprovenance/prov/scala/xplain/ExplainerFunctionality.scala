package org.openprovenance.prov.scala.xplain

import org.openprovenance.prov.model.Namespace
import org.openprovenance.prov.scala.immutable.{Document, Statement, StatementOrBundle}
import org.openprovenance.prov.scala.narrator.{EventOrganiser, XConfig}
import EventOrganiser.{NLG_PREFIX, NLG_URI, gensym}
import org.openprovenance.prov.scala.iface.Narrative
import org.openprovenance.prov.scala.narrator.NarratorFunctionality.realise

object ExplainerFunctionality {

  def explain(doc: Document, config: XConfig): Map[String, Narrative] = {

    val doc1 = Document(doc, gensym, NLG_PREFIX, NLG_URI)
    val newEntities = EventOrganiser.addEntitiesToAgents(doc1.statements())

    val newStatements: Iterable[StatementOrBundle] = doc1.statements() ++ newEntities

    val namespace: Namespace = doc1.namespace
    val doc2: Document = new Document(newStatements, namespace)


    val theStatements: Seq[Statement] = doc2.statements().toSeq


    realise(config, theStatements, allp = false)

  }


}
