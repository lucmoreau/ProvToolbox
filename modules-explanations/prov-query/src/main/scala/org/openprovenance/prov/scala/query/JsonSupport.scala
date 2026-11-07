package org.openprovenance.prov.scala.query

import tools.jackson.databind.ObjectMapper


object JsonSupport {
  val om: ObjectMapper =tools.jackson.databind.json.JsonMapper.builderWithJackson2Defaults().build() // D2

  def toJsonSentence(s: Map[String,Object]): String = {
    om.writeValueAsString(s)
  }

  def fromJson(s:String): Map[String,Object] = {
    val m = om.readValue(s, classOf[Map[String,Object]])
    m
  }

  def extractPotentialJSon(s: String): Object = {
    if (s.startsWith("{")) fromJson(s) else s
  }
}
