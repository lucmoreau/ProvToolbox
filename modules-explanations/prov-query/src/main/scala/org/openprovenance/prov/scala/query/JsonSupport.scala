package org.openprovenance.prov.scala.query

import com.fasterxml.jackson.databind.ObjectMapper


object Json {
  val om: ObjectMapper =new ObjectMapper()

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
