package org.openprovenance.prov.scala.iface

import org.openprovenance.prov.scala.immutable.Document
import org.openprovenance.prov.scala.narrator.{EventsDescription, XConfig}
import org.openprovenance.prov.validation.EventMatrix

class XFactory {

  def makeNarrator: Narrator = {
    new Narrator {
      @Override
      def narrate(doc:Document, config:XConfig): (Map[String,Narrative], Document, EventMatrix, EventsDescription) = {
        org.openprovenance.prov.scala.narrator.NarratorFunctionality.narrate(doc, config)
      }

      @Override
      def narrate1(doc: Document, config: XConfig): Map[String, Narrative] = {
        val (text, _, _, _) = narrate(doc, config)
        text
      }

      @Override
      def narrate2(doc: Document, config: XConfig): Map[String, List[String]] = {
        getTextOnly(narrate1(doc, config))
      }

      @Override
      def narrate2string(doc: Document, config: XConfig): Map[String, String] = {
        narrate2(doc, config).map { case (k, v) => (k, v.mkString("\n")) }
      }

      @Override
      def getTextOnly(text: Map[String, Narrative]): Map[String, List[String]] = {
        text.map { case (f, n) => (f, n.sentences) }
      }

      @Override
      def getSnlgOnly(text: Map[String, Narrative]): Map[String, List[String]] = {
        text.map { case (f, n) => (f, n.snlgs) }
      }

    }
  }

  def makeExplainer: Explainer = {
    new Explainer {
      @Override
      def explain(doc: Document, config: XConfig): Map[String, Narrative] = {
        org.openprovenance.prov.scala.xplain.ExplainerFunctionality.explain(doc, config)  // move functionality to Xplain package.
      }
    }
  }

}
