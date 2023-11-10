package org.openprovenance.prov.scala.nlgspec_transformer

import org.openprovenance.prov.scala.nlgspec_transformer.defs.{Dictionary, Plan}

import java.io.File
import scala.io.{BufferedSource, Source}


case class Language(name: String,
                    templates: Array[String],
                    dictionaries: Array[String],
                    profiles: Array[String],
                    active_profiles: List[List[String]])

/*
case class Triple (subject: QualifiedName,
                   predicate: String,
                   obj: Object) {
  override val hashCode: Int = scala.runtime.ScalaRunTime._hashCode(Triple.this)
}
*/


object Language {
  def readDescriptor(s: String, filep:Boolean=true): Language = {
    if (filep) {
      readDescriptor(new File(s))
    } else {
      readDescriptorFromResource(s)
    }
  }

  def readDescriptorFromResource(resourcePath:String): Language = {
    val resource: BufferedSource =Source.fromResource(resourcePath)
    val lang = SpecLoader.mapper.readValue(resource.bufferedReader(), classOf[Language])
    lang
  }

  def readDescriptor(f: File): Language = {
    val lang = SpecLoader.mapper.readValue(f, classOf[Language])
    lang
  }

  def read(s:String): (String,Array[Plan], Array[Dictionary]) = {
    val desc: Language =readDescriptor(s)
    val prefix=s.substring(0,s.lastIndexOf('/')+1)
    val templates=desc.templates.map(templ => {
      val template=ConfigurationLoader.readTemplate(new File(prefix+templ))
      template.name=templ
      template})
    val dictionaries=if (desc.dictionaries==null)
      Array[Dictionary]()
    else
      desc.dictionaries.map(templ => {
        val dictionary=ConfigurationLoader.readDictionary(new File(prefix+templ))
        dictionary})
    (desc.name, templates,dictionaries)
  }

  def read(ss:Seq[String], filep:Boolean): (Seq[Plan],Seq[Dictionary], Map[String, Object]) = {

    val result: (Seq[Plan], Seq[Dictionary], Map[String, Object]) = (
      ss.flatMap(s => {
        val desc: Language = readDescriptor(s, filep)
        val prefix = s.substring(0, s.lastIndexOf('/') + 1)
        val templates = desc.templates.map(templ => {
          if (filep) {
            val template = ConfigurationLoader.readTemplate(new File(prefix + templ))
            template.name = templ.substring(0,templ.lastIndexOf("."))
            template
          } else {
            val template = ConfigurationLoader.readTemplate(prefix + templ)
            template.name = templ.substring(0,templ.lastIndexOf("."))
            template
          }
        })
        templates
      }),
      ss.flatMap(s => {
        val desc: Language = readDescriptor(s, filep)
        val prefix = s.substring(0, s.lastIndexOf('/') + 1)
        if (desc.dictionaries == null) {
          Array[Dictionary]()
        }
        else {
          val dictionaries = desc.dictionaries.map(templ => {
            if (filep) {
              val dictionary: Dictionary = ConfigurationLoader.readDictionary(new File(prefix + templ))
              dictionary
            } else {
              val dictionary = ConfigurationLoader.readDictionary(prefix + templ)
              dictionary
            }
          })
          dictionaries
        }
      }),
      ss.flatMap(s => {
        val desc: Language = readDescriptor(s, filep)
        val prefix = s.substring(0, s.lastIndexOf('/') + 1)
        if (desc.profiles == null) Map[String, Object]() else {
          val profiles = desc.profiles.map(templ => {
            if (filep) {
              val profile = ConfigurationLoader.readProfile(new File(prefix + templ))
              profile
            } else {
              val profile = ConfigurationLoader.readProfileFromResource(prefix + templ)
              profile
            }
          }).flatMap(_.toSet)
          profiles
        }
      }).toMap
    )



    result
  }
}

