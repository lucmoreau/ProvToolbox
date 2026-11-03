package org.openprovenance.prov.scala.nf.xml

import java.io.PipedInputStream
import java.io.PipedOutputStream
import java.io.StringWriter
import org.openprovenance.prov.scala.immutable.ActedOnBehalfOf
import org.openprovenance.prov.scala.immutable.Activity
import org.openprovenance.prov.scala.immutable.Agent
import org.openprovenance.prov.scala.immutable.AlternateOf
import org.openprovenance.prov.scala.immutable.Attribute
import org.openprovenance.prov.scala.immutable.Document
import org.openprovenance.prov.scala.immutable.Entity
import org.openprovenance.prov.scala.immutable.HadMember
import org.openprovenance.prov.scala.immutable.QualifiedName
import org.openprovenance.prov.scala.immutable.SpecializationOf
import org.openprovenance.prov.scala.immutable.Statement
import org.openprovenance.prov.scala.immutable.Used
import org.openprovenance.prov.scala.immutable.WasAssociatedWith
import org.openprovenance.prov.scala.immutable.WasAttributedTo
import org.openprovenance.prov.scala.immutable.WasDerivedFrom
import org.openprovenance.prov.scala.immutable.WasEndedBy
import org.openprovenance.prov.scala.immutable.WasGeneratedBy
import org.openprovenance.prov.scala.immutable.WasInfluencedBy
import org.openprovenance.prov.scala.immutable.WasInformedBy
import org.openprovenance.prov.scala.immutable.WasInvalidatedBy
import org.openprovenance.prov.scala.immutable.WasStartedBy
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule
import com.fasterxml.jackson.dataformat.xml.XmlMapper

import javax.xml.datatype.XMLGregorianCalendar
import javax.xml.stream.XMLOutputFactory
import javax.xml.stream.XMLStreamReader
import javax.xml.stream.XMLStreamWriter
import javax.xml.transform.TransformerFactory
import javax.xml.transform.stax.StAXResult
import javax.xml.transform.stax.StAXSource
import org.openprovenance.prov.bean.xml
import org.openprovenance.prov.bean.xml.Attr

import scala.jdk.CollectionConverters._

object XmlBean {
  
  
  def createAttr(attr: Set[Attribute]): Seq[Attr] ={
      val ll=attr.map(x => { val res=new org.openprovenance.prov.bean.xml.Attr
                      res.element=x.elementName.getUri()
                      res.`type`=x.`type`.getUri()
                      res.value=x.value.toString()
                      res }).toList

    ll
  }

  //FIXME: this is not sorted!!!!
  
  def toBean(s:org.openprovenance.prov.scala.immutable.Statement): org.openprovenance.prov.bean.xml.Statement = {
    s match {
      case ent:Entity             => convert(ent).asInstanceOf[org.openprovenance.prov.bean.xml.Statement]
      case act:Activity           => convert(act).asInstanceOf[org.openprovenance.prov.bean.xml.Statement]
      case ag:Agent               => convert(ag).asInstanceOf[org.openprovenance.prov.bean.xml.Statement]

      case waw:WasAssociatedWith  => convert(waw).asInstanceOf[org.openprovenance.prov.bean.xml.Statement]
      case wat:WasAttributedTo    => convert(wat).asInstanceOf[org.openprovenance.prov.bean.xml.Statement]
      case aob:ActedOnBehalfOf    => convert(aob).asInstanceOf[org.openprovenance.prov.bean.xml.Statement]
      case wgb:WasGeneratedBy     => convert(wgb).asInstanceOf[org.openprovenance.prov.bean.xml.Statement]
      case wib:WasInvalidatedBy   => convert(wib).asInstanceOf[org.openprovenance.prov.bean.xml.Statement]
      case usd:Used               => convert(usd).asInstanceOf[org.openprovenance.prov.bean.xml.Statement]
      case wsb:WasStartedBy       => convert(wsb).asInstanceOf[org.openprovenance.prov.bean.xml.Statement]
      case web:WasEndedBy         => convert(web).asInstanceOf[org.openprovenance.prov.bean.xml.Statement]
      case wdf:WasDerivedFrom     => convert(wdf).asInstanceOf[org.openprovenance.prov.bean.xml.Statement]
      case winfb:WasInformedBy    => convert(winfb).asInstanceOf[org.openprovenance.prov.bean.xml.Statement]
      case winflb:WasInfluencedBy => convert(winflb).asInstanceOf[org.openprovenance.prov.bean.xml.Statement]
      case spe:SpecializationOf   => convert(spe).asInstanceOf[org.openprovenance.prov.bean.xml.Statement]
      case alt:AlternateOf        => convert(alt).asInstanceOf[org.openprovenance.prov.bean.xml.Statement]
      case mem:HadMember          => convert(mem).asInstanceOf[org.openprovenance.prov.bean.xml.Statement]

    }
  }
  
  def uriIfNotNull(q: QualifiedName) = {
    if (q==null) null else q.getUri()
  }
  def timeOrNullOld(time: Option[XMLGregorianCalendar]): XMLGregorianCalendar = {
    time match { 
      case Some(t) => t 
      case None => null }
  }
  def timeOrNull(time: Option[XMLGregorianCalendar]): String = {
    time match { 
      case Some(t) => t.toString() 
      case None => null }
  }

      
  def convert(ent: Entity):org.openprovenance.prov.bean.xml.Entity  ={
    val attr=ent.getAttributes
    val res=new org.openprovenance.prov.bean.xml.Entity
    res.id=uriIfNotNull(ent.id)
    res.attr=createAttr(attr)  .asJava
    res   
  }  
  
  
        
  def convert(ag: Agent):org.openprovenance.prov.bean.xml.Agent  ={
    val attr=ag.getAttributes
    val res=new org.openprovenance.prov.bean.xml.Agent
    res.id=uriIfNotNull(ag.id)
    res.attr=createAttr(attr).asJava
    res   
  }  
  
  
        
  def convert(act: Activity):org.openprovenance.prov.bean.xml.Activity  ={
    val attr=act.getAttributes
    val res=new org.openprovenance.prov.bean.xml.Activity
    res.startTime=timeOrNull(act.startTime)
    res.endTime=timeOrNull(act.endTime)
    res.id=uriIfNotNull(act.id)
    res.attr=createAttr(attr) .asJava
    res   
  }  
  
  
  def convert(wdf: WasDerivedFrom):org.openprovenance.prov.bean.xml.WasDerivedFrom  ={
    val attr=wdf.getAttributes
    val res=new org.openprovenance.prov.bean.xml.WasDerivedFrom
    res.id=uriIfNotNull(wdf.id)
    res.generatedEntity=uriIfNotNull(wdf.generatedEntity)
    res.usedEntity=uriIfNotNull(wdf.usedEntity)
    res.activity=uriIfNotNull(wdf.activity)
    res.generation=uriIfNotNull(wdf.generation)
    res.usage=uriIfNotNull(wdf.usage)
    res.attr=createAttr(attr) .asJava 
    res   
  }
      
  def convert(wgb: WasGeneratedBy):org.openprovenance.prov.bean.xml.WasGeneratedBy  ={
    val attr=wgb.getAttributes
    val res=new org.openprovenance.prov.bean.xml.WasGeneratedBy
    res.id=uriIfNotNull(wgb.id)
    res.entity=uriIfNotNull(wgb.entity)
    res.activity=uriIfNotNull(wgb.activity)
    res.time=timeOrNull(wgb.time)
    res.attr=createAttr(attr) .asJava 
    res   
  }
  
      
  def convert(wgb: WasInvalidatedBy):org.openprovenance.prov.bean.xml.WasInvalidatedBy  ={
    val attr=wgb.getAttributes
    val res=new org.openprovenance.prov.bean.xml.WasInvalidatedBy
    res.id=uriIfNotNull(wgb.id)
    res.entity=uriIfNotNull(wgb.entity)
    res.activity=uriIfNotNull(wgb.activity)
    res.time=timeOrNull(wgb.time)
    res.attr=createAttr(attr)  .asJava
    res   
  }
  
  def convert(usd: Used):org.openprovenance.prov.bean.xml.Used  ={
    val attr=usd.getAttributes
    val res=new org.openprovenance.prov.bean.xml.Used
    res.id=uriIfNotNull(usd.id)
    res.activity=uriIfNotNull(usd.activity)
    res.entity=uriIfNotNull(usd.entity)
    res.time=timeOrNull(usd.time)
    res.attr=createAttr(attr)  .asJava
    res   
  }
  
  def convert(wsb: WasStartedBy):org.openprovenance.prov.bean.xml.WasStartedBy  ={
    val attr=wsb.getAttributes
    val res=new org.openprovenance.prov.bean.xml.WasStartedBy
    res.id=uriIfNotNull(wsb.id)
    res.activity=uriIfNotNull(wsb.activity)
    res.trigger=uriIfNotNull(wsb.trigger)
    res.starter=uriIfNotNull(wsb.starter)
    res.time=timeOrNull(wsb.time)
    res.attr=createAttr(attr)  .asJava
    res   
  }
  
  def convert(wgb: WasEndedBy):org.openprovenance.prov.bean.xml.WasEndedBy  ={
    val attr=wgb.getAttributes
    val res=new org.openprovenance.prov.bean.xml.WasEndedBy
    res.id=uriIfNotNull(wgb.id)
    res.activity=uriIfNotNull(wgb.activity)
    res.trigger=uriIfNotNull(wgb.trigger)
    res.ender=uriIfNotNull(wgb.ender)
    res.time=timeOrNull(wgb.time)
    res.attr=createAttr(attr)  .asJava
    res   
  }
    
  def convert(waw: WasAssociatedWith):org.openprovenance.prov.bean.xml.WasAssociatedWith  ={
    val attr=waw.getAttributes
    val res=new org.openprovenance.prov.bean.xml.WasAssociatedWith
    res.id=uriIfNotNull(waw.id)
    res.activity=uriIfNotNull(waw.activity)
    res.agent=uriIfNotNull(waw.agent)
    res.plan=uriIfNotNull(waw.plan)
    res.attr=createAttr(attr)  .asJava
    res   
  }
  
  def convert(aob: ActedOnBehalfOf):org.openprovenance.prov.bean.xml .ActedOnBehalfOf  ={
    val attr=aob.getAttributes
    val res=new org.openprovenance.prov.bean.xml .ActedOnBehalfOf
    res.id=uriIfNotNull(aob.id)
    res.activity=uriIfNotNull(aob.delegate)
    res.activity=uriIfNotNull(aob.responsible)
    res.activity=uriIfNotNull(aob.activity)
    res.attr=createAttr(attr)  .asJava
    res   
  }

  def convert(wat: WasAttributedTo):org.openprovenance.prov.bean.xml .WasAttributedTo  ={
    val attr=wat.getAttributes
    val res=new org.openprovenance.prov.bean.xml .WasAttributedTo
    res.id=uriIfNotNull(wat.id)
    res.entity=uriIfNotNull(wat.entity)
    res.agent=uriIfNotNull(wat.agent)
    res.attr=createAttr(attr)  .asJava
    res   
  }

  def convert(wat: WasInformedBy):org.openprovenance.prov.bean.xml.WasInformedBy  ={
    val attr=wat.getAttributes
    val res=new org.openprovenance.prov.bean.xml.WasInformedBy
    res.id=uriIfNotNull(wat.id)
    res.informed=uriIfNotNull(wat.informed)
    res.informant=uriIfNotNull(wat.informant)
    res.attr=createAttr(attr)  .asJava
    res   
  }

  def convert(winflb: WasInfluencedBy):org.openprovenance.prov.bean.xml.WasInfluencedBy  ={
    val attr=winflb.getAttributes
    val res=new org.openprovenance.prov.bean.xml.WasInfluencedBy
    res.id=uriIfNotNull(winflb.id)
    res.influencee=uriIfNotNull(winflb.influencee)
    res.influencer=uriIfNotNull(winflb.influencer)
    res.attr=createAttr(attr)  .asJava
    res   
  }

  def convert(spe: SpecializationOf):org.openprovenance.prov.bean.xml.SpecializationOf  ={
    val attr=spe.getAttributes
    val res=new org.openprovenance.prov.bean.xml.SpecializationOf
    res.id=uriIfNotNull(spe.id)
    res.specificEntity=uriIfNotNull(spe.specificEntity)
    res.generalEntity=uriIfNotNull(spe.generalEntity)
    res.attr=createAttr(attr)  .asJava
    res   
  }
  
  def convert(spe: AlternateOf):org.openprovenance.prov.bean.xml.AlternateOf  ={
    val attr=spe.getAttributes
    val res=new org.openprovenance.prov.bean.xml.AlternateOf
    res.id=uriIfNotNull(spe.id)
    res.alternate1=uriIfNotNull(spe.alternate1)
    res.alternate2=uriIfNotNull(spe.alternate2)
    res.attr=createAttr(attr)  .asJava
    res   
  }
  
  def convert(spe: HadMember):org.openprovenance.prov.bean.xml.HadMember  ={
    val attr=spe.getAttributes
    val res=new org.openprovenance.prov.bean.xml.HadMember
    res.id=uriIfNotNull(spe.id)
    res.collection=uriIfNotNull(spe.collection)
    res.entity=spe.entity.map(x=>uriIfNotNull(x)).toList.asJava
    res.attr=createAttr(attr)  .asJava
    res   
  }
  
  def toXMLWithStream(sw: XMLStreamWriter, statements: Set[Statement]): Unit = {
    val beans: Set[xml.Statement] =statements.map(toBean)
    val module=new JacksonXmlModule()
    // and then configure, for example:
    module.setDefaultUseWrapper(false)

    val xmlMapper = new XmlMapper(module)
    beans.foreach(xmlMapper.writeValue(sw, _))
  }

  def toXML2(statements: Set[Statement]):String = {
	  val out = new StringWriter();
	  toXML2(out,statements)
	  out.toString
  }
  
  def toXML(doc: Document, id: String=null):String = {
	  val out = new StringWriter();
	  toXMLStringWriter(out,doc, id)
	  out.toString()
  }
  
  def toXMLStringWriter(out: StringWriter, doc: Document, id: String): Unit = {
    val xmlOutputFactory = XMLOutputFactory.newFactory();
    val sw = xmlOutputFactory.createXMLStreamWriter(out);
    toXML(sw,doc, id)
  }
   
  def toXML(sw: XMLStreamWriter, doc: Document, id: String): Unit = {
    sw.writeStartDocument();
    sw.writeStartElement("root")
    sw.writeStartElement("document")
    if (id!=null) sw.writeAttribute("Id", id)
    toXMLWithStream(sw,doc.statementOrBundle.map(_.asInstanceOf[org.openprovenance.prov.scala.immutable.Statement]).toSet) // TODO: what about bundles
    sw.writeEndElement();
    sw.writeEndElement();
    sw.writeEndDocument();
  }

  def toXML2(out: StringWriter, statements: Set[Statement]): Unit = {
    val xmlOutputFactory = XMLOutputFactory.newFactory();
    val sw = xmlOutputFactory.createXMLStreamWriter(out);
    toXML(sw,statements)
  }
  
  def toXML(sw: XMLStreamWriter, statements: Set[Statement]): Unit = {
    println("toXML (1)")
    sw.writeStartDocument();
    println("toXML (2)")
    sw.writeStartElement("document");
    println("toXML (3)")
    toXMLWithStream(sw,statements) 
    println("toXML (4)")
        sw.writeComment("Done");
    sw.writeEndElement();
    sw.writeEndDocument(); 
  }
  
  // TODO
  
  def fromXML(sr: XMLStreamReader): Document = {
    val event=sr.getEventType
    
    null
  }
  def fromXMLWithStream(sr: XMLStreamReader): Set[Statement] = {
    val module=new JacksonXmlModule()
    // and then configure, for example:
    module.setDefaultUseWrapper(false)

    val xmlMapper = new XmlMapper(module)
    val expr=xmlMapper.readValue(sr, Statement.getClass)
    
    null
  }
  

  
  def pipe (): (PipedInputStream, PipedOutputStream) = {
    val out=new PipedOutputStream
    val in= new PipedInputStream
    out.connect(in)
    (in,out)
  }
  
  def compose(xmlStreamReader: XMLStreamReader,
              xmlStreamWriter: XMLStreamWriter): Unit = {
    val tf = TransformerFactory.newInstance();
    val t:javax.xml.transform.Transformer = tf.newTransformer();
    val source = new StAXSource(xmlStreamReader);
    val result = new StAXResult(xmlStreamWriter);
    t.transform(source, result);
  }

  
}