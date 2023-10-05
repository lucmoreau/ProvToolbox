package org.openprovenance.prov.scala.nlgspec_transformer

import org.openprovenance.prov.scala.nlgspec_transformer.defs.{Dictionary, Plan}
import org.openprovenance.prov.scala.primitive.Keywords

import java.io.{File, FileInputStream, InputStream}
import scala.io.{BufferedSource, Source}

object ConfigurationLoader {


  def readTemplate(f: File): Plan = {
    val template = SpecLoader.mapper.readValue(f, classOf[Plan])
    template
  }
  def readTemplate(f: InputStream): Plan = {
    val template = SpecLoader.mapper.readValue(f, classOf[Plan])
    template
  }
  def readProfile(f: File): Map[String, Object] = {
    val profile: Map[String, Object] = SpecLoader.mapper.readValue(f, classOf[Map[String,Object]])
    profile
  }
  def readProfileFromResource(path: String): Map[String, Object] = {
    val x: BufferedSource = Source.fromResource(path)
    val profile: Map[String, Object] = SpecLoader.mapper.readValue(x.bufferedReader(), classOf[Map[String,Object]])
    profile
  }
  def readProfile(f: String): Map[String, Object] = {
    val profile: Map[String, Object] = SpecLoader.mapper.readValue(new File(f), classOf[Map[String,Object]])
    profile
  }
  def readTemplate(resourcePath: String): Plan = {
    println("Reading resource from " + resourcePath)
    val x: BufferedSource = Source.fromResource(resourcePath)
    val template = SpecLoader.mapper.readValue(x.bufferedReader(), classOf[Plan])
    template
  }

  def readDictionary(f: File): defs.Dictionary = {
    val dictionary = //SpecLoader.mapper.readValue(f, classOf[Map[String,Object]])
      SpecLoader.dictionaryImport(new FileInputStream(f))
    dictionary
  }
  def readDictionary(resourcePath: String): Dictionary = {
    val x: BufferedSource = Source.fromResource(resourcePath)
    val dictionary = SpecLoader.dictionaryImport(x.reader())
    dictionary
  }

  def toJsonSentence(s: Map[String,Object]): String = {
    //TypePropagator.om.writeValueAsString(Template(query = null, sentence = s, context = null, select = null, where = null, name = null))
    SpecLoader.mapper.writeValueAsString(s)
  }

  def toJsonSentence(s: Array[Object]): String = {
    SpecLoader.mapper.writeValueAsString(s)
  }
  def fromJson(s:String): Map[String,Object] = {
    val m = SpecLoader.mapper.readValue(s, classOf[Map[String,Object]])
    m
  }

  def extractPotentialJSon(s: String): Object = {
    if (s.startsWith("{")) fromJson(s) else s
  }
  def extractPotentialJSon(m: Array[Object]): Array[Object] = {
    m.map {
      case s: String => if (s.startsWith("{")) fromJson(s) else s
      case value => value
    }
  }

  def extractPotentialJSon(m: Map[String, Object]): Map[String, Object] = {
    val value = m(Keywords.TMP)
    value match {
      case s:String => fromJson(s)
      case _ => value.asInstanceOf[Map[String, Object]]
    }
  }

}
