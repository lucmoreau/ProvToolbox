package org.openprovenance.prov.scala.viz

import org.openprovenance.prov.scala.summary.types._


object SummaryGraphics {
  def toColors(provenance_type: Set[ProvType]): (String,String) = {
    myColorMap.getOrElse(provenance_type,("peru","black"))
  }

  def toFillColor(provenance_type: Set[ProvType]): String = {
    toColors(provenance_type)._1
  }

  def toFontColor(provenance_type: Set[ProvType]): String = {
    toColors(provenance_type)._2
  }

  val myColorMap: Map[Set[_ <: ProvType], (String,String)]
  = Map(Set(Act()) -> (Graphics.activityFillColor,"black"),
        Set(Act(), Usd(Set(Ent()))) -> ("limegreen","black"),
        Set(Act(), Usd(Set(Ent())), Waw(Set(Ag(), Prim("prov:Person")),Set()))-> ("rosybrown","black"),
        Set(Act(), Winfob(Set(Act()))) -> ("dodgerblue4","white"),
        Set(Act(), Waw(Set(Ag()),Set())) -> ("purple2","black"),

        Set(Ag())-> (Graphics.agentFillColor,"black"),
        Set(Ag(), Prim("prov:Organization"))-> ("darkorange","black"),
        Set(Ag(), Prim("prov:Person"))-> ("coral","black"),
        Set(Ag(), Aobo(Set(Ag()),Set())) -> ("red","white"),
        Set(Aobo(Set(Ag(), Prim("prov:Organization")),Set(Act())), Ag(), Prim("prov:Person"))-> ("orangered","black"),

        Set(Ent())-> (Graphics.entityFillColor,"black"),
        Set(Wdf(Set(Ent()),Set()), Alt(Set(Ent())), Spec(Set(Ent())), Ent())-> ("navajowhite","black"),
        Set(Wdf(Set(Ent()),Set()), Ent())-> ("gold","black"),
        Set(Wdf(Set(Ent()),Set()), Spec(Set(Ent())), Ent())-> ("navajowhite","black"),
        Set(Wdf(Set(Ent()),Set()), Wgb(Set(Act())), Ent())-> ("khaki","black"),
        Set(Wdf(Set(Ent()),Set()), Mem(Set(Ent())), Ent()) -> ("olivedrab2","black"),
        Set(Wdf(Set(Ent()),Set()), Wgb(Set(Act())), Mem(Set(Ent())), Ent()) -> ("olivedrab","black"),

        Set(Wgb(Set(Act())), Ent())-> ("forestgreen","black"),
        Set(Wgb(Set(Act())), Wat(Set(Ag(), Prim("prov:Person"))), Ent())-> ("violet","black"),
        Set(Wat(Set(Ag())), Ent())-> ("deeppink","black")



  )


}
