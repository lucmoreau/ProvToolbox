package org.openprovenance.prov.scala.iface

import org.openprovenance.prov.scala.immutable.Document
import org.openprovenance.prov.scala.narrator.XConfig
import org.openprovenance.prov.scala.xplain.Narrative

trait Explainer {
  def explain(doc: Document, config: XConfig): Map[String, Narrative]

}
