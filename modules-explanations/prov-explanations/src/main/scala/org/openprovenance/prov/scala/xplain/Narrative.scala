package org.openprovenance.prov.scala.xplain

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.openprovenance.prov.scala.nlgspec_transformer.defs.Plan
import org.openprovenance.prov.scala.primitive.Triple

import scala.collection.mutable

case class Narrative (sentences: List[String] =List(),
                      @JsonSerialize(contentUsing = classOf[SNLGSerializer])
                      snlgs: List[String] =List(),
                      @JsonSerialize(contentConverter = classOf[TreeConverter])
                      trees: List[() => String] =List(),
                      templates: List[Plan]=List(),
                      coverage: List[mutable.Set[Triple]]=List()) extends Ordered[Narrative] {
  def add(sentence_material: ((String, String, () => String), Plan, mutable.Set[Triple])): Narrative = {
    val ((sentence,snlg,tree), template, triples) = sentence_material
    Narrative(sentence :: sentences, snlg::snlgs, tree:: trees, template :: templates, triples :: coverage)
  }

  lazy val tr: Set[Triple] = coverage.map(_.toSet).fold(Set())((a1, a2) => a1 ++ a2)

  override def compare(that: Narrative): Int = { //TODO: when coverage is equal, then prioritise according to number of statemetns.
    val p1 = this.tr.subsetOf(that.tr)
    val p2 = that.tr.subsetOf(this.tr)
    if (p1) {
      if (p2) 0 else -1
    } else if (p2) 1 else 0
  }
}
