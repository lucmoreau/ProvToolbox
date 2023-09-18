package org.openprovenance.prov.scala.nlgspec_transformer

import java.io.{File, FileInputStream, InputStream}
import org.openprovenance.prov.scala.primitive.Keywords
import org.openprovenance.prov.model.Namespace
import org.openprovenance.prov.scala.immutable.{ProvFactory, QualifiedName}
import org.openprovenance.prov.scala.nlgspec_transformer.defs.{Dictionary, Template}
import org.openprovenance.prov.scala.nlgspec_transformer.specTypes.Phrase

import scala.io.{BufferedSource, Source}


/*
case class Template (var name: String,
                     sentence:  Map[String,Object],
                     context: Map[String,String],
                     query: Object,
                     where: Seq[Map[String,String]],
                     select: Map[String,Map[String,String]])


 */
object Environment {
  def apply(context: Map[String,String],
            dictionaries:Seq[Dictionary],
            profiles:Map[String,Object],
            theprofile:String,
            active_profiles:List[List[String]]=List()): Environment = {
    Environment(context,dictionaries,profiles,if (theprofile==null) new Array[String](0) else theprofile.split(","),active_profiles)
  }
}
case class Environment (context: Map[String,String],
                        dictionaries:Seq[Dictionary],
                        profiles:Map[String,Object],
                        theprofile:Array[String],
                        active_profiles:List[List[String]]) {
  lazy val dictionary:Map[QualifiedName,Phrase]=dictionaries.map(dict => {
    val dico: Map[String, specTypes.Phrase] =dict.dictionary
    val con: Map[String, String] =dict.context
    val ns=toNamespace(con)
    dico.map{case (src,dst) => (ns.stringToQualifiedName(src,ProvFactory.pf).asInstanceOf[QualifiedName],dst)}
  }).head
  lazy val snippets: Map[String, Phrase] =dictionaries.map(dict => {
    val snippets=dict.snippets
    snippets}).head

  def toNamespace(context: Map[String,String]): Namespace = {
    val ns=new Namespace
    ns.addKnownNamespaces()
    context.foreach{case (p,n) => ns.register(p,n)}
    ns
  }
}




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
    val lang = SpecLoader.mapper.readValue(resource.bufferedReader, classOf[Language])
    lang
  }

  def readDescriptor(f: File): Language = {
    val lang = SpecLoader.mapper.readValue(f, classOf[Language])
    lang
  }

  def read(s:String): (String,Array[Template], Array[Dictionary]) = {
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

  def read(ss:Seq[String], filep:Boolean): (Seq[Template],Seq[Dictionary], Map[String, Object]) = {

    val result: (Seq[Template], Seq[Dictionary], Map[String, Object]) = (
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

object ConfigurationLoader {


  def readTemplate(f: File): Template = {
    val template = SpecLoader.mapper.readValue(f, classOf[Template])
    template
  }
  def readTemplate(f: InputStream): Template = {
    val template = SpecLoader.mapper.readValue(f, classOf[Template])
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
  def readTemplate(resourcePath: String): Template = {
    println("Reading resource from " + resourcePath)
    val x: BufferedSource = Source.fromResource(resourcePath)
    val template = SpecLoader.mapper.readValue(x.bufferedReader(), classOf[Template])
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
    m.map(value =>
      value match {
        case s:String => if (s.startsWith("{")) fromJson(s) else s
        case _ => value
      })
  }

  def extractPotentialJSon(m: Map[String, Object]): Map[String, Object] = {
    val value = m(Keywords.TMP)
    value match {
      case s:String => fromJson(s)
      case _ => value.asInstanceOf[Map[String, Object]]
    }
  }

}
