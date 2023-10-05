package org.openprovenance.prov.scala.iface

import org.openprovenance.prov.scala.immutable.Document
import org.openprovenance.prov.scala.narrator.{EventsDescription, XConfig}
import org.openprovenance.prov.validation.EventMatrix

trait Narrator {
  def narrate(doc:Document, config:XConfig): (Map[String,Narrative], Document, EventMatrix, EventsDescription)

  def narrate1(doc: Document, config: XConfig): Map[String, Narrative]

  def narrate2(doc: Document, config: XConfig): Map[String, List[String]]

  def narrate2string(doc: Document, config: XConfig): Map[String, String]

  def getTextOnly(text: Map[String, Narrative]): Map[String, List[String]]

  def getSnlgOnly(text: Map[String, Narrative]): Map[String, List[String]]

}
