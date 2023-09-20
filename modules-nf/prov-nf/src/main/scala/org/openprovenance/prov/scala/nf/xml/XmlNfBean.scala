package org.openprovenance.prov.scala.nf.xml

import com.fasterxml.jackson.databind.`type`.TypeFactory

import java.io._
import java.util
import java.util.{Collections, LinkedList}
import com.fasterxml.jackson.databind.introspect.{AnnotationIntrospectorPair, JacksonAnnotationIntrospector}
import com.fasterxml.jackson.dataformat.xml.{JacksonXmlModule, XmlMapper}
import com.fasterxml.jackson.module.jakarta.xmlbind.JakartaXmlBindAnnotationIntrospector
import jakarta.xml.bind.DatatypeConverter

import javax.xml.datatype.XMLGregorianCalendar
import javax.xml.stream.{XMLInputFactory, XMLOutputFactory, XMLStreamReader, XMLStreamWriter}
import javax.xml.transform.stax.{StAXResult, StAXSource}
import javax.xml.transform.stream.{StreamResult, StreamSource}
import javax.xml.transform.{Transformer, TransformerConfigurationException, TransformerFactory}
import org.apache.logging.log4j.{LogManager, Logger}
import org.openprovenance.apache.commons.lang.StringEscapeUtils
import org.openprovenance.prov.model
import org.openprovenance.prov.model.Namespace
import org.openprovenance.prov.nf.xml
import org.openprovenance.prov.nf.xml.Attr
import org.openprovenance.prov.scala.immutable.Attribute.{split2, split3}
import org.openprovenance.prov.scala.immutable.{Attribute, LangString, Location, Other, QualifiedName, Role, Type, Value}
import org.openprovenance.prov.scala.nf._

import scala.jdk.CollectionConverters._
import scala.collection.immutable

object Transformer {
  val factory: TransformerFactory = TransformerFactory.newInstance()
  val xmlOutputFactory: XMLOutputFactory = XMLOutputFactory.newFactory()
  val xmlInputFactory: XMLInputFactory = XMLInputFactory.newInstance()
  
  def getTransformer(filename: String): javax.xml.transform.Transformer =  {
        val xslStream:StreamSource = new StreamSource(this.getClass.getClassLoader.getResourceAsStream(filename))
        var transformer:javax.xml.transform.Transformer=null
        try {
            transformer = factory.newTransformer(xslStream)
            System.out.println("***** Loading xsl file")
        } catch {
          case e: TransformerConfigurationException => e.printStackTrace()
        }
        return transformer
  }
  
  val transformer: Transformer =getTransformer("removewrapper.xslt")
    
}


object XmlNfBean {
  val logger: Logger = LogManager.getLogger("XmlNfBean")

  val DOCUMENT_TAG="document"
  val ROOT_TAG="root"
  val ID_TAG="Id"


  def createAttr(attr: Set[Attribute]): util.LinkedList[Attr] = {
		  val l: immutable.Seq[Attr] =attr.map(x => {   val res=new Attr
                                             res.element=x.elementName.getUri()
                                             res.`type`=x.`type`.getUri()
                                             res.value= x.value match {
                                                           case qn:QualifiedName => qn.getUri()
                                                           case d:XMLGregorianCalendar => d.toXMLFormat()
                                                           case qn:model.QualifiedName => qn.getUri()
                                                           case ls:org.openprovenance.prov.model.LangString => ls.getValue + "@" + ls.getLang
                                                           case o => StringEscapeUtils.escapeXml(o.toString())  }
                                             res }).toList
    sortAttributes(l)
  }


  def sortAttributes(l: immutable.Seq[Attr]): util.LinkedList[Attr] = {
    logger.info("sort attributes list")
    val ll = l.asJava
    val ll2 = new util.LinkedList[Attr]()
    ll2.addAll(ll)
    Collections.sort(ll2)
    ll2
  }

  def toBean(s:org.openprovenance.prov.scala.nf.Statement): org.openprovenance.prov.nf.xml.Statement = {
    s match {
      case ent:Entity             => convert(ent).asInstanceOf[org.openprovenance.prov.nf.xml.Statement]
      case act:Activity           => convert(act).asInstanceOf[org.openprovenance.prov.nf.xml.Statement]
      case ag:Agent               => convert(ag).asInstanceOf[org.openprovenance.prov.nf.xml.Statement]

      case waw:WasAssociatedWith  => convert(waw).asInstanceOf[org.openprovenance.prov.nf.xml.Statement]
      case wat:WasAttributedTo    => convert(wat).asInstanceOf[org.openprovenance.prov.nf.xml.Statement]
      case aob:ActedOnBehalfOf    => convert(aob).asInstanceOf[org.openprovenance.prov.nf.xml.Statement]
      case wgb:WasGeneratedBy     => convert(wgb).asInstanceOf[org.openprovenance.prov.nf.xml.Statement]
      case wib:WasInvalidatedBy   => convert(wib).asInstanceOf[org.openprovenance.prov.nf.xml.Statement]
      case usd:Used               => convert(usd).asInstanceOf[org.openprovenance.prov.nf.xml.Statement]
      case wsb:WasStartedBy       => convert(wsb).asInstanceOf[org.openprovenance.prov.nf.xml.Statement]
      case web:WasEndedBy         => convert(web).asInstanceOf[org.openprovenance.prov.nf.xml.Statement]
      case wdf:WasDerivedFrom     => convert(wdf).asInstanceOf[org.openprovenance.prov.nf.xml.Statement]
      case winfb:WasInformedBy    => convert(winfb).asInstanceOf[org.openprovenance.prov.nf.xml.Statement]
      case winflb:WasInfluencedBy => convert(winflb).asInstanceOf[org.openprovenance.prov.nf.xml.Statement]
      case spe:SpecializationOf   => convert(spe).asInstanceOf[org.openprovenance.prov.nf.xml.Statement]
      case alt:AlternateOf        => convert(alt).asInstanceOf[org.openprovenance.prov.nf.xml.Statement]
      case mem:HadMember          => convert(mem).asInstanceOf[org.openprovenance.prov.nf.xml.Statement]

    }
  }
    def fromBean(s:org.openprovenance.prov.nf.xml.Statement, ns: Namespace): org.openprovenance.prov.scala.nf.Statement = { //TODO FIX THEM ALL
    s match {
      case ent:org.openprovenance.prov.nf.xml.Entity             => convert(ent,ns).asInstanceOf[org.openprovenance.prov.scala.nf.Statement]
      case act:org.openprovenance.prov.nf.xml.Activity           => convert(act,ns).asInstanceOf[org.openprovenance.prov.scala.nf.Statement]
      case ag:org.openprovenance.prov.nf.xml.Agent               => convert(ag,ns).asInstanceOf[org.openprovenance.prov.scala.nf.Statement]

      case waw:org.openprovenance.prov.nf.xml.WasAssociatedWith  => convert(waw,ns).asInstanceOf[org.openprovenance.prov.scala.nf.Statement]
      case wat:org.openprovenance.prov.nf.xml.WasAttributedTo    => convert(wat,ns).asInstanceOf[org.openprovenance.prov.scala.nf.Statement]
      case aob:org.openprovenance.prov.nf.xml.ActedOnBehalfOf    => convert(aob,ns).asInstanceOf[org.openprovenance.prov.scala.nf.Statement]
      case wgb:org.openprovenance.prov.nf.xml.WasGeneratedBy     => convert(wgb,ns).asInstanceOf[org.openprovenance.prov.scala.nf.Statement]
      case wib:org.openprovenance.prov.nf.xml.WasInvalidatedBy   => convert(wib,ns).asInstanceOf[org.openprovenance.prov.scala.nf.Statement]
      case usd:org.openprovenance.prov.nf.xml.Used               => convert(usd,ns).asInstanceOf[org.openprovenance.prov.scala.nf.Statement]
      case wsb:org.openprovenance.prov.nf.xml.WasStartedBy       => convert(wsb,ns).asInstanceOf[org.openprovenance.prov.scala.nf.Statement]
      case web:org.openprovenance.prov.nf.xml.WasEndedBy         => convert(web,ns).asInstanceOf[org.openprovenance.prov.scala.nf.Statement]
      case wdf:org.openprovenance.prov.nf.xml.WasDerivedFrom     => convert(wdf,ns).asInstanceOf[org.openprovenance.prov.scala.nf.Statement]
      case winfb:org.openprovenance.prov.nf.xml.WasInformedBy    => convert(winfb,ns).asInstanceOf[org.openprovenance.prov.scala.nf.Statement]
      case winflb:org.openprovenance.prov.nf.xml.WasInfluencedBy => convert(winflb,ns).asInstanceOf[org.openprovenance.prov.scala.nf.Statement]
      case spe:org.openprovenance.prov.nf.xml.SpecializationOf   => convert(spe,ns).asInstanceOf[org.openprovenance.prov.scala.nf.Statement]
      case alt:org.openprovenance.prov.nf.xml.AlternateOf        => convert(alt,ns).asInstanceOf[org.openprovenance.prov.scala.nf.Statement]
      case mem:org.openprovenance.prov.nf.xml.HadMember          => convert(mem,ns).asInstanceOf[org.openprovenance.prov.scala.nf.Statement]

    }
  }
  
  def ifNull[T](q: Seq[T]): Seq[T] = {
    if (q==null) Seq[T]() else q
  } 

  def uriIfNotNull(q: QualifiedName) = {
    if (q==null) null else q.getUri()
  }
   def uriIfNotNull(q: Set[QualifiedName]) = {
    if (q==null) Set[String]() else q.map(_.getUri())
  } 
   
  def timeOrNullOld(time: Option[XMLGregorianCalendar]): XMLGregorianCalendar = {
    time match { 
      case Some(t) => t 
      case None => null }
  }
  def timeOrNull(time: Option[XMLGregorianCalendar]): String = {
    time match { 
      case Some(t) => t.toXMLFormat() 
      case None => null }
  }
  
  def timeOrNull(time: Set[XMLGregorianCalendar]): Set[String] = {
    if (time==null) Set[String]() else time.map(_.toXMLFormat())
  }
  
  def orderUris(s: Set[String]): util.List[String] = {
    s.toSeq.sortWith((s1,s2) => s1<s2).asJava
  }
  
  def orderTimes(s: Set[String]): util.List[String] = {
    s.toSeq.sortWith((s1,s2) => s1<s2).asJava
  }
      
  def convert(ent: Entity):org.openprovenance.prov.nf.xml.Entity  ={
    val attr=ent.getAttributes
    val res=new org.openprovenance.prov.nf.xml.Entity
    res.id=orderUris(uriIfNotNull(ent.id))
    res.attr=createAttr(attr)  
    res   
  }  
  
  
        
  def convert(ag: Agent):org.openprovenance.prov.nf.xml.Agent  ={
    val attr=ag.getAttributes
    val res=new org.openprovenance.prov.nf.xml.Agent
    res.id=orderUris(uriIfNotNull(ag.id))
    res.attr=createAttr(attr)  
    res   
  }  
  
  
        
  def convert(act: Activity):org.openprovenance.prov.nf.xml.Activity  ={
    val attr=act.getAttributes
    val res=new org.openprovenance.prov.nf.xml.Activity
    res.startTime=orderTimes(timeOrNull(act.startTime))
    res.endTime=orderTimes(timeOrNull(act.endTime))
    res.id=orderUris(uriIfNotNull(act.id))
    res.attr=createAttr(attr)  
    res   
  }  
  
  
  def convert(wdf: WasDerivedFrom):org.openprovenance.prov.nf.xml.WasDerivedFrom  ={
    val attr=wdf.getAttributes
    val res=new org.openprovenance.prov.nf.xml.WasDerivedFrom
    res.id=orderUris(uriIfNotNull(wdf.id))
    res.generatedEntity=orderUris(uriIfNotNull(wdf.generatedEntity))
    res.usedEntity=orderUris(uriIfNotNull(wdf.usedEntity))
    res.activity=orderUris(uriIfNotNull(wdf.activity))
    res.generation=orderUris(uriIfNotNull(wdf.generation))
    res.usage=orderUris(uriIfNotNull(wdf.usage))
    res.attr=createAttr(attr)  
    res   
  }
      
  def convert(wgb: WasGeneratedBy):org.openprovenance.prov.nf.xml.WasGeneratedBy  ={
    val attr=wgb.getAttributes
    val res=new org.openprovenance.prov.nf.xml.WasGeneratedBy
    res.id=orderUris(uriIfNotNull(wgb.id))
    res.entity=orderUris(uriIfNotNull(wgb.entity))
    res.activity=orderUris(uriIfNotNull(wgb.activity))
    res.time=orderTimes(timeOrNull(wgb.time))
    res.attr=createAttr(attr)  
    res   
  }
  
      
  def convert(wgb: WasInvalidatedBy):org.openprovenance.prov.nf.xml.WasInvalidatedBy  ={
    val attr=wgb.getAttributes
    val res=new org.openprovenance.prov.nf.xml.WasInvalidatedBy
    res.id=orderUris(uriIfNotNull(wgb.id))
    res.entity=orderUris(uriIfNotNull(wgb.entity))
    res.activity=orderUris(uriIfNotNull(wgb.activity))
    res.time=orderTimes(timeOrNull(wgb.time))
    res.attr=createAttr(attr)  
    res   
  }
  
  def convert(usd: Used):org.openprovenance.prov.nf.xml.Used  ={
    val attr=usd.getAttributes
    val res=new org.openprovenance.prov.nf.xml.Used
    res.id=orderUris(uriIfNotNull(usd.id))
    res.activity=orderUris(uriIfNotNull(usd.activity))
    res.entity=orderUris(uriIfNotNull(usd.entity))
    res.time=orderTimes(timeOrNull(usd.time))
    res.attr=createAttr(attr)  
    res   
  }
  
  def convert(wsb: WasStartedBy):org.openprovenance.prov.nf.xml.WasStartedBy  ={
    val attr=wsb.getAttributes
    val res=new org.openprovenance.prov.nf.xml.WasStartedBy
    res.id=orderUris(uriIfNotNull(wsb.id))
    res.activity=orderUris(uriIfNotNull(wsb.activity))
    res.trigger=orderUris(uriIfNotNull(wsb.trigger))
    res.starter=orderUris(uriIfNotNull(wsb.starter))
    res.time=orderTimes(timeOrNull(wsb.time))
    res.attr=createAttr(attr)  
    res   
  }
  
  def convert(wgb: WasEndedBy):org.openprovenance.prov.nf.xml.WasEndedBy  ={
    val attr=wgb.getAttributes
    val res=new org.openprovenance.prov.nf.xml.WasEndedBy
    res.id=orderUris(uriIfNotNull(wgb.id))
    res.activity=orderUris(uriIfNotNull(wgb.activity))
    res.trigger=orderUris(uriIfNotNull(wgb.trigger))
    res.ender=orderUris(uriIfNotNull(wgb.ender))
    res.time=orderTimes(timeOrNull(wgb.time))
    res.attr=createAttr(attr)  
    res   
  }
    
  def convert(waw: WasAssociatedWith):org.openprovenance.prov.nf.xml.WasAssociatedWith  ={
    val attr=waw.getAttributes
    val res=new org.openprovenance.prov.nf.xml.WasAssociatedWith
    res.id=orderUris(uriIfNotNull(waw.id))
    res.activity=orderUris(uriIfNotNull(waw.activity))
    res.agent=orderUris(uriIfNotNull(waw.agent))
    res.plan=orderUris(uriIfNotNull(waw.plan))
    res.attr=createAttr(attr)  
    res   
  }
  
  def convert(aob: ActedOnBehalfOf):org.openprovenance.prov.nf.xml.ActedOnBehalfOf  ={
    val attr=aob.getAttributes
    val res=new org.openprovenance.prov.nf.xml.ActedOnBehalfOf
    res.id=orderUris(uriIfNotNull(aob.id))
    res.delegate=orderUris(uriIfNotNull(aob.delegate))
    res.responsible=orderUris(uriIfNotNull(aob.responsible))
    res.activity=orderUris(uriIfNotNull(aob.activity))
    res.attr=createAttr(attr)  
    res   
  }

  def convert(wat: WasAttributedTo):org.openprovenance.prov.nf.xml.WasAttributedTo  ={
    val attr=wat.getAttributes
    val res=new org.openprovenance.prov.nf.xml.WasAttributedTo
    res.id=orderUris(uriIfNotNull(wat.id))
    res.entity=orderUris(uriIfNotNull(wat.entity))
    res.agent=orderUris(uriIfNotNull(wat.agent))
    res.attr=createAttr(attr)  
    res   
  }

  def convert(wat: WasInformedBy):org.openprovenance.prov.nf.xml.WasInformedBy  ={
    val attr=wat.getAttributes
    val res=new org.openprovenance.prov.nf.xml.WasInformedBy
    res.id=orderUris(uriIfNotNull(wat.id))
    res.informed=orderUris(uriIfNotNull(wat.informed))
    res.informant=orderUris(uriIfNotNull(wat.informant))
    res.attr=createAttr(attr)  
    res   
  }

  def convert(winflb: WasInfluencedBy):org.openprovenance.prov.nf.xml.WasInfluencedBy  ={
    val attr=winflb.getAttributes
    val res=new org.openprovenance.prov.nf.xml.WasInfluencedBy
    res.id=orderUris(uriIfNotNull(winflb.id))
    res.influencee=orderUris(uriIfNotNull(winflb.influencee))
    res.influencer=orderUris(uriIfNotNull(winflb.influencer))
    res.attr=createAttr(attr)  
    res   
  }

  def convert(spe: SpecializationOf):org.openprovenance.prov.nf.xml.SpecializationOf  ={
    val attr=spe.getAttributes
    val res=new org.openprovenance.prov.nf.xml.SpecializationOf
    res.id=orderUris(uriIfNotNull(spe.id))
    res.specificEntity=orderUris(uriIfNotNull(spe.specificEntity))
    res.generalEntity=orderUris(uriIfNotNull(spe.generalEntity))
    res.attr=createAttr(attr)  
    res   
  }
  
  def convert(spe: AlternateOf):org.openprovenance.prov.nf.xml.AlternateOf  ={
    val attr=spe.getAttributes
    val res=new org.openprovenance.prov.nf.xml.AlternateOf
    res.id=orderUris(uriIfNotNull(spe.id))
    res.alternate1=orderUris(uriIfNotNull(spe.alternate1))
    res.alternate2=orderUris(uriIfNotNull(spe.alternate2))
    res.attr=createAttr(attr)  
    res   
  }
  
  def convert(spe: HadMember):org.openprovenance.prov.nf.xml.HadMember  ={
    val attr=spe.getAttributes
    val res=new org.openprovenance.prov.nf.xml.HadMember
    res.id=orderUris(uriIfNotNull(spe.id))
    res.collection=orderUris(uriIfNotNull(spe.collection))
    res.entity=orderUris(uriIfNotNull(spe.entity))
    res.attr=createAttr(attr)  
    res   
  }
  
  
  /////////////
  import org.openprovenance.prov.scala.immutable.Attribute.split
  import org.openprovenance.prov.scala.immutable.ProvFactory.pf

  
  var count=0
  
  def q(uri: String,ns: Namespace) = {
    val map=ns.getPrefixes.asScala
    val x=map.map{case (p,u) => if (uri.startsWith(u)) Some(p,u,uri.substring(u.length())) else None}.flatten
    x.headOption match {
      case Some((p,u,local)) => new QualifiedName(p,local,u)
      case None => {
    	  val mycount=count
    			  count=count+1
    			  val pre="_pre"+count
    			  ns.register(pre, uri)
    			  new QualifiedName(pre,"",uri)
      }
    }
  }

  
  def time(time: String) : XMLGregorianCalendar = {
    pf.newTime(DatatypeConverter.parseDateTime(time).getTime)
  }
  
  def time(times: java.util.List[String]) : Set[XMLGregorianCalendar] = {
    if (times==null) return Set()
    val seq=times.asScala.toSet
    seq.map(time)
  }    
  
  def q(uris: java.util.List[String], ns: Namespace): Set[QualifiedName] = {
    if (uris==null) return Set()
    val seq=uris.asScala.toSet
    seq.map(q(_,ns))
  }


  
  val prov_qualified_name: QualifiedName = pf.prov_qualified_name
  def makeAttribute(element: String, value: String, `type`: String, ns: Namespace): org.openprovenance.prov.model.Attribute = {
    val qElement=q(element,ns)
    val qType=q(`type`,ns)
    
    if (prov_qualified_name.equals(qType)) {
      // val qValue=ns.stringToQualifiedName(value, pf)
      // val qValue=ns.uriToQualifiedName(value,pf,true)
      val qValue=q(value,ns)
    	pf.newAttribute(qElement,qValue,qType)
    } else {
      pf.newAttribute(qElement,value,qType)
    }
    
  }
  
  def convertAttrs(attrList: java.util.List[org.openprovenance.prov.nf.xml.Attr], ns: Namespace): (Set[LangString], Set[Type], Set[Value], Set[Location], Set[Role], Map[QualifiedName, Set[Other]]) = {
    val attrs=if (attrList==null) Seq() else { attrList.asScala.toSeq }
    val attrSet=ifNull(attrs).map{attr => makeAttribute(attr.element,attr.value,attr.`type`,ns)}
    val (l,t,v,loc,r,m)=split3(attrSet)  //(Set[Label],Set[Type],Set[Value],Set[Location],Set[Role],Map[QualifiedName,Set[Other]])
    val langStrings=LangString(l.map(_.getLangString))
    (langStrings,t,v,loc,r,m)
  }
  
  def convert(ent: org.openprovenance.prov.nf.xml.Entity, ns: Namespace): Entity = {
    val id=q(ent.id,ns)
    val (l,t,v,loc,r,m)=convertAttrs(ent.attr,ns)
    val res=new Entity(id,l,t,v,loc,m) // ignoring role!
    res   
  }  
 
  def convert(ag: org.openprovenance.prov.nf.xml.Agent, ns: Namespace): Agent = {
    val id=q(ag.id,ns)
    val (l,t,v,loc,r,m)=convertAttrs(ag.attr,ns)
    val res=new Agent(id,l,t,v,loc,m) // ignoring role!
    res   
  }  
    
  def convert(act: org.openprovenance.prov.nf.xml.Activity, ns: Namespace): Activity = {
    val id=q(act.id,ns)
    val (l,t,v,loc,r,m)=convertAttrs(act.attr,ns)
    val res=new Activity(id,time(act.startTime), time(act.endTime), l,t,loc,m) // ignoring role!
    res   
  }  
      
      
  def convert(wdf: org.openprovenance.prov.nf.xml.WasDerivedFrom, ns:Namespace): WasDerivedFrom = {
    val ids=q(wdf.id,ns)
    val (l,t,v,loc,r,m)=convertAttrs(wdf.attr,ns)
    val res=new WasDerivedFrom(ids,q(wdf.generatedEntity,ns), q(wdf.usedEntity,ns), q(wdf.activity,ns), q(wdf.generation,ns), q(wdf.usage,ns), l,t, m) 
    res   
  }   
  
    
  def convert(wgb: org.openprovenance.prov.nf.xml.WasGeneratedBy, ns:Namespace): WasGeneratedBy = {
    val ids=q(wgb.id,ns)
    val (l,t,v,loc,r,m)=convertAttrs(wgb.attr,ns)
    val res=new WasGeneratedBy(ids,q(wgb.entity,ns), q(wgb.activity,ns), time(wgb.time), l,t,loc,r, m) 
    res   
  }   
  
  def convert(wib: org.openprovenance.prov.nf.xml.WasInvalidatedBy, ns:Namespace): WasInvalidatedBy = {
    val ids=q(wib.id,ns)
    val (l,t,v,loc,r,m)=convertAttrs(wib.attr,ns)
    val res=new WasInvalidatedBy(ids,q(wib.entity,ns), q(wib.activity,ns), time(wib.time), l,t,loc,r, m)
    res   
  } 
  
  def convert(usd: org.openprovenance.prov.nf.xml.Used, ns:Namespace): Used = {
    val ids=q(usd.id,ns)
    val (l,t,v,loc,r,m)=convertAttrs(usd.attr,ns)
    val res=new Used(ids,q(usd.activity,ns), q(usd.entity,ns), time(usd.time), l,t,loc,r, m)
    res   
  }
  
  def convert(wsb: org.openprovenance.prov.nf.xml.WasStartedBy, ns:Namespace): WasStartedBy = {
    val ids=q(wsb.id,ns)
    val (l,t,v,loc,r,m)=convertAttrs(wsb.attr,ns)
    val res=new WasStartedBy(ids,q(wsb.activity,ns), q(wsb.trigger,ns), q(wsb.starter,ns), time(wsb.time), l,t,loc,r, m)
    res   
  }
  
  def convert(web: org.openprovenance.prov.nf.xml.WasEndedBy, ns:Namespace): WasEndedBy = {
    val ids=q(web.id,ns)
    val (l,t,v,loc,r,m)=convertAttrs(web.attr,ns)
    val res=new WasEndedBy(ids,q(web.activity,ns), q(web.trigger,ns), q(web.ender,ns), time(web.time), l,t,loc,r, m)
    res   
  }
  
  def convert(waw: org.openprovenance.prov.nf.xml.WasAssociatedWith, ns:Namespace): WasAssociatedWith = {
    val ids=q(waw.id,ns)
    val (l,t,v,loc,r,m)=convertAttrs(waw.attr,ns)
    val res=new WasAssociatedWith(ids,q(waw.activity,ns), q(waw.agent,ns), q(waw.plan,ns), l,t,r, m)
    res   
  } 
  
  def convert(wat: org.openprovenance.prov.nf.xml.WasAttributedTo, ns:Namespace): WasAttributedTo = {
    val ids=q(wat.id,ns)
    val (l,t,v,loc,r,m)=convertAttrs(wat.attr,ns)
    val res=new WasAttributedTo(ids,q(wat.entity,ns), q(wat.agent,ns), l,t, m)
    res   
  } 
  
  def convert(aob: org.openprovenance.prov.nf.xml.ActedOnBehalfOf, ns:Namespace): ActedOnBehalfOf = {
    val ids=q(aob.id,ns)
    val (l,t,v,loc,r,m)=convertAttrs(aob.attr,ns)
    val res=new ActedOnBehalfOf(ids,q(aob.delegate,ns), q(aob.responsible,ns), q(aob.activity,ns), l,t, m)
    res   
  } 
  
  def convert(winfb: org.openprovenance.prov.nf.xml.WasInformedBy, ns:Namespace): WasInformedBy = {
    val ids=q(winfb.id,ns)
    val (l,t,v,loc,r,m)=convertAttrs(winfb.attr,ns)
    val res=new WasInformedBy(ids,q(winfb.informed,ns), q(winfb.informant,ns), l,t, m)
    res   
  } 
    
  def convert(winflb: org.openprovenance.prov.nf.xml.WasInfluencedBy, ns:Namespace): WasInfluencedBy = {
    val ids=q(winflb.id,ns)
    val (l,t,v,loc,r,m)=convertAttrs(winflb.attr,ns)
    val res=new WasInfluencedBy(ids,q(winflb.influencee,ns), q(winflb.influencer,ns), l,t, m)
    res   
  } 
  
  def convert(spe: org.openprovenance.prov.nf.xml.SpecializationOf, ns:Namespace): SpecializationOf = {
    val ids=q(spe.id,ns)
    val (l,t,v,loc,r,m)=convertAttrs(spe.attr,ns)
    val res=new SpecializationOf(ids,q(spe.specificEntity,ns), q(spe.generalEntity,ns), l,t, m)
    res   
  } 
    
  def convert(alt: org.openprovenance.prov.nf.xml.AlternateOf, ns:Namespace): AlternateOf = {
    val ids=q(alt.id,ns)
    val (l,t,v,loc,r,m)=convertAttrs(alt.attr,ns)
    val res=new AlternateOf(ids,q(alt.alternate1,ns), q(alt.alternate2,ns), l,t, m)
    res   
  } 
      
  def convert(mem: org.openprovenance.prov.nf.xml.HadMember, ns:Namespace): HadMember = {
    val ids=q(mem.id,ns)
    val (l,t,v,loc,r,m)=convertAttrs(mem.attr,ns)
    val res=new HadMember(ids,q(mem.collection,ns), q(mem.entity,ns), l,t, m)
    res   
  } 
  
  def convert(doc: org.openprovenance.prov.nf.xml.Document): DocumentProxy = {
    val ss = {val x=doc.statements; if (x==null) Set() else x.asScala.toSet } 
    val prefixes=doc.prefixes.asScala
    val ns=new Namespace
    prefixes.foldLeft(ns)((ns,p) => {ns.register(p._1,p._2); ns})
    val ss2=ss.map(x => fromBean(x,ns))

    

    val doc2=ss2.foldLeft(new DocumentProxy(ns))((doc,s)=>doc.add(s)) 
   
    doc2  
  }   
  
  
  ////////////


  def sortBeans(l: immutable.Seq[org.openprovenance.prov.nf.xml.Statement]): util.LinkedList[org.openprovenance.prov.nf.xml.Statement] = {
    logger.info("sorting statements list (1)")
    val ll = l.asJava
    val ll2 = new util.LinkedList[org.openprovenance.prov.nf.xml.Statement]()
    ll2.addAll(ll)
    Collections.sort(ll2)
    ll2
  }
  
  def toXMLWithStream(sw: XMLStreamWriter, statements: Set[org.openprovenance.prov.scala.nf.Statement]): Unit = {
    val beans=statements.map(toBean).toList
    val module=new JacksonXmlModule()
    // and then configure, for example:
    module.setDefaultUseWrapper(false)

    val ll: util.LinkedList[xml.Statement] =sortBeans(beans)

    val xmlMapper = new XmlMapper(module)
    ll.iterator().asScala.foreach(xmlMapper.writeValue(sw, _))
  }
  
  def toXMLWithStream(sw: XMLStreamWriter, doc:  org.openprovenance.prov.nf.xml.Document): Unit = {
    val module=new JacksonXmlModule()
    // and then configure, for example:
    module.setDefaultUseWrapper(false)

    
       
    val primary = new JacksonAnnotationIntrospector()
    val secondary = new JakartaXmlBindAnnotationIntrospector(TypeFactory.defaultInstance())
    val pair = new AnnotationIntrospectorPair(primary, secondary)


    val xmlMapper = new XmlMapper(module)

    logger.info("sorting statements list (2)")
    val ll=new util.LinkedList[org.openprovenance.prov.nf.xml.Statement]
    ll.addAll(doc.statements)
    Collections.sort(ll)
    doc.statements=ll

    xmlMapper.setAnnotationIntrospector(pair)
    //val xmlMapper = new XmlMapper(module)
    xmlMapper.writeValue(sw, doc)
  }

  def toXML2(statements: Set[org.openprovenance.prov.scala.nf.Statement]):String = {
	  val out = new StringWriter()
	  toXML2(out,statements)
	  out.toString()
  }
  
  def toXML(doc: org.openprovenance.prov.scala.nf.DocumentProxy, id: String=null):String = {
	  val out = new StringWriter()
	  toXMLStringWriter(out,doc, id)
	  out.toString()
  }
  
  def toXMLStringWriter(out: StringWriter, doc: org.openprovenance.prov.scala.nf.DocumentProxy, id: String) {
    val xmlOutputFactory = XMLOutputFactory.newFactory()
    val sw = xmlOutputFactory.createXMLStreamWriter(out)
    toXMLWithRoot(sw,doc, id)
  }
  
  
   
  def toXMLWithRoot(sw: XMLStreamWriter, doc: org.openprovenance.prov.scala.nf.DocumentProxy, id: String) {
    sw.writeStartDocument()
    sw.writeStartElement(ROOT_TAG)
    sw.writeStartElement(DOCUMENT_TAG)
    if (id!=null) sw.writeAttribute(ID_TAG, id)
    toXMLWithStream(sw,doc.statement.statements ++ doc.indexer.values()) // TODO: what about bundles
    sw.writeEndElement()
    sw.writeEndElement()
    sw.writeEndDocument()
  }
  
  
  def toXMLFile(out: String, doc: org.openprovenance.prov.scala.nf.DocumentProxy, id: String) {
        
    val fos=new FileOutputStream(out)
        
    val xmlOutputFactory = XMLOutputFactory.newFactory()
    val sw = xmlOutputFactory.createXMLStreamWriter(fos)
    
    toXML(sw,doc,id)
    fos.close()

    
  }
  
  def toXMLOutputStream(doc: org.openprovenance.prov.scala.nf.DocumentProxyFromStatements, id: String) {
                
    val xmlOutputFactory = XMLOutputFactory.newFactory()
    val sw = xmlOutputFactory.createXMLStreamWriter(System.out)
    
    toXML(sw,doc,id)
    sw.close()
    
  }

    
  def toXMLFile(out: String, doc: org.openprovenance.prov.scala.nf.DocumentProxyFromStatements, id: String) {
        
    val fos=new FileOutputStream(out)
        
    val xmlOutputFactory = XMLOutputFactory.newFactory()
    val sw = xmlOutputFactory.createXMLStreamWriter(fos)
    toXML(sw,doc,id)
    fos.close()

    
  }
    
  def toXML(sw: XMLStreamWriter, doc: org.openprovenance.prov.scala.nf.ProxyWithStatements, id: String) {  // why with nf.Document and below with nf.Document2
	  sw.writeStartDocument()
	  if (id!=null) sw.writeAttribute(ID_TAG, id)
	  
	  serializeIntoReadyStream(sw,doc,true)
  
    sw.writeEndDocument()
  }
  
  def toXMLOutputStream(doc: org.openprovenance.prov.scala.nf.DocumentProxyFromStatements, out:java.io.OutputStream, id: String) {
    documentProxySerialiser(out,doc,true,id,true,wrapInDocument)
  }
  def toXMLOutputStreamForSignature(doc: org.openprovenance.prov.scala.nf.DocumentProxyFromStatements, out:java.io.OutputStream, id: String) {
    documentProxySerialiser(out,doc,false,id,true,wrapInDocumentForSignature) // note prefixes not include in serialization
  }  
  def documentProxySerialiser(out: java.io.OutputStream, 
                              nfDoc: org.openprovenance.prov.scala.nf.ProxyWithStatements,  
                              prefixes: Boolean, 
                              id: String,
                              flatten: Boolean=true,
                              toXMLWithStream: ((XMLStreamWriter,org.openprovenance.prov.scala.nf.ProxyWithStatements,Boolean, String) => Unit) ){
    if (flatten) {
    	val (pipe_in,pipe_out)=pipe()
      val sw_out = Transformer.xmlOutputFactory.createXMLStreamWriter(pipe_out)
      val stream_in=new StreamSource(pipe_in)
      val stream_out=new StreamResult(out)    
      var sourceException:Option[Throwable]=None
      val thread = new Thread {
    	  override def run() {
    		  try {
    			  System.err.println("documentProxySerialiser.Thread: start")
    			  toXMLWithStream(sw_out,nfDoc,prefixes,id)
    			  System.err.println("documentProxySerialiser.Thread: end")
    			  pipe_out.close()
    		  } catch {
    		  case e: Throwable => {
    			  e.printStackTrace()
    			  sourceException=Some(e)
    			  pipe_out.close()
    		  }
    		  }
    	  }
      }
      thread.start()
      System.err.println("documentProxySerialiser: starting transform")
      try {
    	  Transformer.transformer.transform(stream_in,stream_out)
      } catch {
      case e: Throwable => {
        sourceException match {
          case Some(e0) => {
            e0.printStackTrace()
            //e0.printStrackTrace(new java.io.PrintStream(out))
            //out.close()
            throw e0
          }
          case None => {
            e.printStackTrace()
            out.close()
          }
        }
    	  
      }
      }
      System.err.println("documentProxySerialiser: ending transform")

    } else {
    	val sw_out = Transformer.xmlOutputFactory.createXMLStreamWriter(out)
    	toXMLWithStream(sw_out,nfDoc,prefixes,id) 
    }
  }
  
  
  
  def wrapInDocument(sw: XMLStreamWriter, doc:  org.openprovenance.prov.scala.nf.ProxyWithStatements, prefixes: Boolean, id: String) = {
  	  sw.writeStartDocument()
  	  sw.writeStartElement(DOCUMENT_TAG)
  	  if (id!=null) sw.writeAttribute(ID_TAG, id)
  	  serializeIntoReadyStream(sw,doc,prefixes)
  	  sw.writeEndElement()
  	  sw.writeEndDocument()
  }
  
  def wrapInDocumentForSignature(sw: XMLStreamWriter, doc:  org.openprovenance.prov.scala.nf.ProxyWithStatements, prefixes: Boolean, id: String) = {
  	  sw.writeStartDocument()
  	  sw.writeStartElement(ROOT_TAG)
  	  sw.writeStartElement(DOCUMENT_TAG)
  	  if (id!=null) sw.writeAttribute(ID_TAG, id)
  	  serializeIntoReadyStream(sw,doc,prefixes)
  	  sw.writeEndElement()
  	  sw.writeEndElement()
      sw.writeEndDocument()
  }
  
  def serializeIntoReadyStream(sw: XMLStreamWriter, doc: org.openprovenance.prov.scala.nf.ProxyWithStatements,  prefixes: Boolean) {
    val nfDoc=new org.openprovenance.prov.nf.xml.Document()    
    val statements=doc.getStatements // TODO: what about bundles
    val beanList: Set[xml.Statement] =statements.map(toBean)
    nfDoc.statements=beanList.toSeq.sorted.asJava

    if (prefixes) {
    	val prefixes=doc.namespace.getPrefixes
    	nfDoc.prefixes=prefixes
    }
    System.err.println("calling toXMLWIthstream")
    toXMLWithStream(sw,nfDoc)
    System.err.println("ending toXMLWIthstream")

  } 

 
  def toXML(sw: XMLStreamWriter, doc: org.openprovenance.prov.scala.nf.DocumentProxyFromStatements, id: String) {
    sw.writeStartDocument()
    serializeIntoReadyStream(sw,doc,true) // note: prefixes include
    sw.writeEndDocument()
  }
  
  def serializeToPipe (d: org.openprovenance.prov.scala.nf.DocumentProxyFromStatements, id: String) = {
	  val (in,out)=pipe()
    val thread = new Thread {
        override def run {
            println("serializeToPipe: in thread")
            toXMLOutputStreamForSignature(d,out,id)
            println("serializeToPipe: finished thread")
            out.close()
        }
    }
    thread.start()
    in
  }


    

  def toXML2(out: StringWriter, statements: Set[org.openprovenance.prov.scala.nf.Statement]) {
    val xmlOutputFactory = XMLOutputFactory.newFactory()
    val sw = xmlOutputFactory.createXMLStreamWriter(out)
    toXML(sw,statements)
  }
  
  def toXML(sw: XMLStreamWriter, statements: Set[org.openprovenance.prov.scala.nf.Statement]) {
    println("toXML (1)")
    sw.writeStartDocument()
    println("toXML (2)")
    sw.writeStartElement("document")
    println("toXML (3)")
    toXMLWithStream(sw,statements) 
    println("toXML (4)")
        sw.writeComment("Done")
    sw.writeEndElement()
    sw.writeEndDocument()
  }
  
  // TODO
  
  def fromXML(sr: XMLStreamReader): DocumentProxy = {
    val event=sr.getEventType
    
    null
  }
  def fromXMLWithFileStream(in: String): DocumentProxy = {
     val fis=new FileInputStream(in)
     val sr = XMLInputFactory.newInstance().createXMLStreamReader(fis)
     val doc=fromXMLWithStream(sr)
     fis.close()
     doc
  }

  def fromXMLWithStream(sr: XMLStreamReader): DocumentProxy = {
    
    val module=new JacksonXmlModule()
    // and then configure, for example:
    module.setDefaultUseWrapper(false)
    
    val primary = new JacksonAnnotationIntrospector()
    val secondary =  new JakartaXmlBindAnnotationIntrospector()
    val pair = new AnnotationIntrospectorPair(primary, secondary)


    val xmlMapper = new XmlMapper(module)
    xmlMapper.setAnnotationIntrospector(pair)


    val docClass=new org.openprovenance.prov.nf.xml.Document().getClass
    val expr=xmlMapper.readValue(sr, docClass)
    convert(expr)
  }
  

  
  def pipe (): (PipedInputStream, PipedOutputStream) = {
    val out=new PipedOutputStream
    val in= new PipedInputStream
    out.connect(in)
    (in,out)
  }
  
  def compose(xmlStreamReader: XMLStreamReader,
              xmlStreamWriter: XMLStreamWriter) = {
    val tf = TransformerFactory.newInstance()
    val t:javax.xml.transform.Transformer = tf.newTransformer()
    val source = new StAXSource(xmlStreamReader)
    val result = new StAXResult(xmlStreamWriter)
    t.transform(source, result)
  }
  
  
  
  //////////////  TODELETE

    
  def toXMLOutputStream_DELETE(doc: org.openprovenance.prov.scala.nf.DocumentProxyFromStatements, out:java.io.OutputStream, id: String) {
                
    val xmlOutputFactory = XMLOutputFactory.newFactory()
    val sw = xmlOutputFactory.createXMLStreamWriter(out)
    
    toXML(sw,doc,id)
    sw.close()
    
  }



  def toXMLForSignature_DELETE(sw: XMLStreamWriter, doc: org.openprovenance.prov.scala.nf.DocumentProxyFromStatements, id: String) {
    sw.writeStartDocument()
    sw.writeStartElement(ROOT_TAG)
    sw.writeStartElement(DOCUMENT_TAG)
    if (id!=null) sw.writeAttribute(ID_TAG, id)
    
    serializeIntoReadyStream(sw,doc,false) // note no prefix

    sw.writeEndElement()
    sw.writeEndElement()
    sw.writeEndDocument()
  }
  
}