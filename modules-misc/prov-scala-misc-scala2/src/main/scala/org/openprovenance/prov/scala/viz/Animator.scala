package org.openprovenance.prov.scala.viz

import java.io.File

import org.openprovenance.prov.scala.interop.FileOutput
import org.openprovenance.prov.scala.immutable.{Activity, Agent, Document, Entity,  HasType, Indexer, LangString, OrderedDocument, Other, ProvFactory, QualifiedName, Relation, StatementOrBundle, Type, Used, WasAssociatedWith, WasDerivedFrom, WasGeneratedBy, WasInformedBy, WasInvalidatedBy}
import org.openprovenance.prov.scala.viz.Graphics.colorQN
import org.openprovenance.prov.scala.viz.Graphics.fillcolorQN
import org.openprovenance.prov.scala.viz.Graphics.fontcolorQN

object Animator {
  val pf: ProvFactory =ProvFactory.pf
  
  def main (args: Array[String]): Unit = {
    val filename = args(0)
    val out = args(1)

  }

  def process(doc:Document, out: String): Unit = {

    val odoc=new OrderedDocument(doc)
    
    
    val ipawType=new QualifiedName("talk","IPAW","http://www.ipaw.info/2016/keynote/")
    val tappType=new QualifiedName("talk","TAPP","http://www.ipaw.info/2016/keynote/")
    val pcType=new QualifiedName("talk","PC","http://www.ipaw.info/2016/keynote/")
    val pwType=new QualifiedName("talk","PW","http://www.ipaw.info/2016/keynote/")
    val w3cType=new QualifiedName("talk","W3C","http://www.ipaw.info/2016/keynote/")
    val otherType=new QualifiedName("talk","OTHER","http://www.ipaw.info/2016/keynote/")
    val tutType=new QualifiedName("talk","TUT","http://www.ipaw.info/2016/keynote/")
     
    
    val docs=transformer(odoc,List(ipawType,tappType,pwType,pcType,w3cType,tutType,otherType))
    
    var count=0
    val doc_count_list=docs.map((_,{count=count+1; count}))
    
    doc_count_list.foreach{case (doc,n) => new SVGOutputer().output(doc,
                                                               new FileOutput(new File(out.replace(".", n.toString()++"."))),
                        		                                   Map[String,String]())}
    
  }
  
  def addLabel(ss:Set[LangString], s:String): Set[LangString] = {
    ss+new LangString(s,None)    
  }
  
  val color_transparent=new Other(colorQN,pf.xsd_string,"transparent")
  val fillcolor_transparent=new Other(fillcolorQN,pf.xsd_string,"transparent")
  val fontcolor_transparent=new Other(fontcolorQN,pf.xsd_string,"transparent")

  val transparentColorMap: Map[QualifiedName, Set[Other]] =
    Map(colorQN -> Set(color_transparent),
      fillcolorQN -> Set(fillcolor_transparent),
      fontcolorQN -> Set(fontcolor_transparent) )

  def hasType(s:StatementOrBundle, typ: Type): Boolean ={
    s match {
      case st: HasType => st.typex.contains(typ)
      case _ => false
    }
  }
  
  def hasTypeIn(s:StatementOrBundle, typ: Set[Type]): Boolean ={
    s match {
      case st: HasType => st.typex.intersect(typ).nonEmpty
      case _ => false
    }
  }
  
  def keepRelation(s: StatementOrBundle, pred: QualifiedName=> Boolean): Boolean = {
    s match {
       case r: Relation => pred(r.getCause) && pred(r.getEffect)
       case _ => false
    }
  }
  
  def keepStatement(s: StatementOrBundle, nodeKeep: scala.collection.mutable.Map[org.openprovenance.prov.model.QualifiedName,Boolean]): Boolean = {
    nodeKeep.getOrElse(s.getId(), false) ||
    keepRelation(s, q=>nodeKeep.getOrElse(q,false))
  }
  
  def transformer(doc: Document, types: List[QualifiedName]): List[Document] = {

    val doc1=transformerId(doc)
    val ind=new Indexer(doc1)
    
    var sofar:Set[Type]=Set()
    
    List(doc1)++
    types.map(typx => {
    	val myType=new Type(pf.prov_qualified_name,typx)
    	sofar=sofar+myType
    	val nodeKeep= scala.collection.mutable.Map[org.openprovenance.prov.model.QualifiedName,Boolean]()
    	doc1.statements().foreach(x => if (hasTypeIn(x,sofar)) nodeKeep.update(x.getId(), true) ) 
      val doc2=transformerOther(doc1,
    		                        s => keepStatement(s,nodeKeep),
    		                        transparentColorMap)
      doc2    		                        
    })
    
                     
  }
  
  def transformerId(doc: Document): Document = {
    doc.map {
      case e: Entity => new Entity(e.id, addLabel(e.label, e.id.getLocalPart), e.typex, e.value, e.location, e.other)
      case e: Activity => new Activity(e.id, e.startTime, e.endTime, addLabel(e.label, e.id.getLocalPart), e.typex, e.location, e.other)
      case e: Agent => new Agent(e.id, addLabel(e.label, e.id.getLocalPart), e.typex, e.value, e.location, e.other)
      case s => s
    }
  }
  def merge(other1: Map[QualifiedName,Set[Other]],other2: Map[QualifiedName,Set[Other]]): Map[QualifiedName, Set[Other]] = {
    val set=other1.toSet ++ other2.toSet
    val x=set.groupBy{case(q,s) => q }.view.mapValues(x => x.flatMap{case (q,s) => s})
    x.toMap
  }
  def transformerOther(doc: Document,  pred: StatementOrBundle=>Boolean, other: Map[QualifiedName,Set[Other]]): Document = {
    doc.map(s => 
      if (pred(s)) s else {
    	  s match {
    	  case e: Entity => new Entity(e.id,e.label,e.typex,e.value,e.location,merge(e.other,other))
    	  case e: Activity => new Activity(e.id,e.startTime, e.endTime, e.label,e.typex,e.location,merge(e.other,other))
    	  case e: Agent => new Agent(e.id,e.label,e.typex,e.value,e.location,merge(e.other,other))
    	  case e: WasInformedBy => new WasInformedBy(e.id,e.informed,e.informant,e.label,e.typex,merge(e.other,other))
    	  case e: WasAssociatedWith => new WasAssociatedWith(e.id,e.activity,e.agent,e.plan,e.label,e.typex,e.role,merge(e.other,other))
    	  case e: Used => new Used(e.id,e.activity,e.entity,e.time,e.label,e.typex,e.location,e.role,merge(e.other,other))
    	  case e: WasGeneratedBy => new Used(e.id,e.entity,e.activity,e.time,e.label,e.typex,e.location,e.role,merge(e.other,other))
    	  case e: WasInvalidatedBy => new Used(e.id,e.entity,e.activity,e.time,e.label,e.typex,e.location,e.role,merge(e.other,other))
    	  case e: WasDerivedFrom => new WasDerivedFrom(e.id,e.generatedEntity,e.usedEntity,e.activity,e.generation,e.usage,e.label,e.typex,merge(e.other,other))
    	  case _ => s
    	  }})
      }
  
}