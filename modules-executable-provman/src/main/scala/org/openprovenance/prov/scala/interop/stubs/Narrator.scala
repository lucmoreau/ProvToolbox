package org.openprovenance.prov.scala.interop.stubs

import org.openprovenance.prov.scala.immutable.Document
import org.openprovenance.prov.scala.interop.Config
import org.openprovenance.prov.scala.nlg.{EventsDescription, Narrative}
import org.openprovenance.prov.validation.EventMatrix

object Narrator {
  def narrate1(doc: Document, config: Config): Map[String, Narrative] = ???

  def getSnlgOnly2(text: Map[String, Narrative]): Map[String, List[String]] = ???

  def getTextOnly2(text: Map[String, Narrative]): Map[String, List[String]] = ???

  def narrateFromConfig(config: Config): (Map[String, Narrative], Document, EventMatrix, EventsDescription) = ???

  def explainFromConfig(config: Config): (Map[String, Narrative], Document) = ???

}





