package org.openprovenance.prov.scala.nlgspec_transformer

import org.openprovenance.prov.model.Namespace
import org.openprovenance.prov.scala.immutable.{ProvFactory, QualifiedName}
import org.openprovenance.prov.scala.nlgspec_transformer.defs.Dictionary
import org.openprovenance.prov.scala.nlgspec_transformer.specTypes.Phrase


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


