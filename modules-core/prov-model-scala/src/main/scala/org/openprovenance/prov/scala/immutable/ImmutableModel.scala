package org.openprovenance.prov.scala.immutable


import java.util
import javax.xml.datatype.XMLGregorianCalendar
import org.openprovenance.prov.model.Attribute.AttributeKind
import org.openprovenance.prov.model.StatementOrBundle.Kind._
import org.openprovenance.prov.model._
import org.openprovenance.prov.model.exception.QualifiedNameException
import org.openprovenance.prov.model.extension.QualifiedHadMember
import org.openprovenance.prov.scala.immutable
import org.openprovenance.prov.scala.immutable.Attribute.split
import org.openprovenance.prov.scala.immutable.Kind.Kind
import org.openprovenance.prov.{model, vanilla}

import scala.annotation.unused
import scala.beans.BeanProperty
import scala.jdk.CollectionConverters._
import scala.collection.immutable.HashMap


trait HasLocation {
    def getLocation: java.util.List[org.openprovenance.prov.model.Location] = location.toSeq.asInstanceOf[Seq[org.openprovenance.prov.model.Location]].asJava
    val location: Set[Location]
}

trait HasTime {
    val time: Option[javax.xml.datatype.XMLGregorianCalendar]
}


trait HasRole {
    def getRole: java.util.List[org.openprovenance.prov.model.Role] = role.toSeq.asInstanceOf[Seq[org.openprovenance.prov.model.Role]].asJava
    val role: Set[Role]        
}

trait HasLabel {
    def getLabel: java.util.List[org.openprovenance.prov.model.LangString] = label.toSeq.asInstanceOf[Seq[org.openprovenance.prov.model.LangString]].asJava
    
    val label: Set[LangString]       
}


trait HasOther {
    //val other: Set[Other] 
	  val other: Map[QualifiedName,Set[Other]]

    def getOther: java.util.List[org.openprovenance.prov.model.Other] = {
	    val setnil: Set[Other]=Set()
	    def op (s:Set[Other], entry: (QualifiedName,Set[Other])): Set[Other]= {s++entry._2}
	    val set: Set[Other]=other.foldLeft(setnil)(op)
	    set.toSeq.asInstanceOf[Seq[org.openprovenance.prov.model.Other]].asJava
	  }

}

trait HasValue {
    val value: Option[Value]
    
    def getValue:org.openprovenance.prov.model.Value={
      value match {
        case Some(v) => v
        case None => null
      }
    }
    
    def setValue(x: org.openprovenance.prov.model.Value): Unit = {
      throw new UnsupportedOperationException
    }
}

trait HasType {
    val typex: Set[Type]
    
    def getType: java.util.List[org.openprovenance.prov.model.Type] = typex.toSeq.asInstanceOf[Seq[org.openprovenance.prov.model.Type]].asJava
}


trait Identifiable extends org.openprovenance.prov.model.Identifiable {
    val id: QualifiedName
    
    def getId: org.openprovenance.prov.model.QualifiedName=id
    def setId(x: org.openprovenance.prov.model.QualifiedName): Unit = {
      throw new UnsupportedOperationException
    }
    
    def optionalId(id: QualifiedName,sb: StringBuilder): Unit={
      if (id!=null) {
        sb++=id.toString()
        sb+=';'
      }
    }
    def idOrMarker(id: QualifiedName,sb: StringBuilder): Unit ={
      if (id!=null) {sb++=id.toString()} else {sb+='-'}
    }
    
    def timeOrMarker(time: XMLGregorianCalendar,sb: StringBuilder): Unit = {
        if (time == null) { sb+='-' } else { sb++= time.toString};
    }
    def timeOrMarker(time: Option[XMLGregorianCalendar],sb: StringBuilder):  Unit = {
       time match { case None => sb+='-'; case Some(time) => sb++= time.toString }
    }

}

trait HasAttributes {
  def getAttributes :Set[Attribute]
  def addAttributes (attr: Set[Attribute]): Statement


  val PLS: QualifiedName = ProvFactory.pf.getName.XSD_STRING.asInstanceOf[QualifiedName]

  def convertAttributes(label: Set[LangString],
                        typex: Set[Type],
                        vs: Option[Value],
                        location: Set[Location],
                        role: Set[Role],
                        other: Map[QualifiedName, Set[Other]]): util.Map[model.QualifiedName, util.Set[model.Attribute]] = {
    val other2=other.asInstanceOf[Map[QualifiedName,Set[Attribute]]] +
      (ProvFactory.pf.prov_label -> label.map(l => new Label(PLS,l).asInstanceOf[Attribute]))  +
      (ProvFactory.pf.prov_type -> typex.asInstanceOf[Set[Attribute]]) +
      (ProvFactory.pf.prov_role -> role.asInstanceOf[Set[Attribute]]) +
      (ProvFactory.pf.prov_location -> location.asInstanceOf[Set[Attribute]])
    val other3=if (vs.isDefined)  other2 + (ProvFactory.pf.prov_value -> Set(vs.get.asInstanceOf[Attribute])) else other2
    other3.map{case (k,v) => (k.asInstanceOf[model.QualifiedName],
      v.map(_.asInstanceOf[model.Attribute]).asJava)}.asJava
  }

}

trait Hashable {
      
      @inline final def h(x: AnyRef): Int = {
          if (x==null) 0 else {
            x match {
              case xs:Iterable[_] =>  {
                val set: Set[_] =xs.toSet
                set.hashCode
              }
              case _ => x.hashCode
            }
          }
      }
      
      @inline final def pr(v0: Int,v1:Int): Int = {
          prime*v0+v1
      }
    
      final private val prime=37
}


trait ImmutableEntity extends org.openprovenance.prov.model.HasLabel 
                         with org.openprovenance.prov.model.HasType 
                         with org.openprovenance.prov.model.HasValue 
                         with org.openprovenance.prov.model.HasLocation 
                         with org.openprovenance.prov.model.HasOther
                         with org.openprovenance.prov.model.Entity
                         with Identifiable 
                         with HasLocation 
                         with HasValue 
                         with HasLabel 
                         with HasType 
                         with HasOther 
                         with HasAttributes
                         with Hashable {
    @BeanProperty
    val kind=PROV_ENTITY
      
    val enumType: immutable.Kind.Value =Kind.ent
  
    def canEqual(a: Any): Boolean = a.isInstanceOf[ImmutableEntity]
      
      
    override def equals(that: Any): Boolean =
    that match {
      case that: ImmutableEntity => that.canEqual(this) && 
                                    this.id == that.id && 
                                    this.value == that.value && 
                                    this.location == that.location &&  
                                    this.label == that.label && 
                                    this.typex == that.typex && 
                                    this.other == that.other                   
      case _ => false
    }
   
    override lazy val hashCode:Int = {
      pr(pr(pr(pr(pr(h(id),h(value)),h(location)),h(label)),h(typex)),h(other))
    }
    
    def valueSet():Set[Value]=value match {case Some(v) => Set(v); case None => Set()} 

    
    def getAttributes: Set[Attribute]= {
      label.map(l =>new Label(ProvFactory.pf.prov_label,l).asInstanceOf[Attribute])++typex++location++valueSet++other.values.flatten
    }
    
    
    
    def toNotation (sb:StringBuilder): Unit = {
      sb++="entity("
      sb++=id.toString()
      val vs:Set[Value]=value match {case Some(v) => Set(v); case None => Set()} 
      Attribute.toNotation(sb, label, typex, vs, location, Set(), other)
      sb+=')'      
    }
    
}


trait ImmutableAgent extends org.openprovenance.prov.model.HasLabel 
                         with org.openprovenance.prov.model.HasType 
                         with org.openprovenance.prov.model.HasValue 
                         with org.openprovenance.prov.model.HasLocation 
                         with org.openprovenance.prov.model.HasOther
                         with org.openprovenance.prov.model.Agent
                         with Identifiable 
                         with HasLocation 
                         with HasValue 
                         with HasLabel 
                         with HasType 
                         with HasOther 
                         with HasAttributes
                         with Hashable {
  
    @BeanProperty
    val kind=PROV_AGENT
      
    val enumType: immutable.Kind.Value =Kind.ag
  
  
    def canEqual(a: Any): Boolean = a.isInstanceOf[ImmutableAgent]
      
      
    override def equals(that: Any): Boolean =
    that match {
      case that: ImmutableAgent => that.canEqual(this) && 
                                    this.id == that.id && 
                                    this.value == that.value && 
                                    this.location == that.location &&  
                                    this.label == that.label && 
                                    this.typex == that.typex && 
                                    this.other == that.other                   
      case _ => false
    }
   
    override lazy val  hashCode:Int = {
      pr(pr(pr(pr(pr(h(id),h(value)),h(location)),h(label)),h(typex)),h(other))
    }
    
    def valueSet():Set[Value]=value match {case Some(v) => Set(v); case None => Set()} 

    
    def getAttributes :Set[Attribute]= {
      label.map(l =>new Label(ProvFactory.pf.prov_label,l).asInstanceOf[Attribute])++typex++location++valueSet++other.values.flatten
    }
        
    def toNotation (sb:StringBuilder): Unit = {
      sb++="agent("
      sb++=id.toString()
      val vs:Set[Value]=valueSet()
      Attribute.toNotation(sb, label, typex, vs, location, Set(), other)
      sb+=')'      
    }
  
}

trait ImmutableActivity extends org.openprovenance.prov.model.HasLabel 
                         with org.openprovenance.prov.model.HasType 
                         with org.openprovenance.prov.model.HasLocation 
                         with org.openprovenance.prov.model.HasOther
                         with org.openprovenance.prov.model.Activity
                         with Identifiable 
                         with HasLocation 
                         with HasLabel 
                         with HasType 
                         with HasOther 
                         with HasAttributes
                         with Hashable {
  
    
    
    val startTime: Option[XMLGregorianCalendar]
    override def getStartTime:XMLGregorianCalendar={
      startTime match {
        case Some(v) => v
        case None => null
      }
    }
    
    val endTime: Option[XMLGregorianCalendar]

    override def getEndTime:XMLGregorianCalendar={
      endTime match {
        case Some(v) => v
        case None => null
      }
    }
    
    def setEndTime(x$1: javax.xml.datatype.XMLGregorianCalendar): Unit = throw new UnsupportedOperationException
    
    def setStartTime(x$1: javax.xml.datatype.XMLGregorianCalendar): Unit = throw new UnsupportedOperationException
    
    def canEqual(a: Any): Boolean = a.isInstanceOf[ImmutableActivity]
      
      
    override def equals(that: Any): Boolean =
    that match {
      case that: ImmutableActivity => that.canEqual(this) && 
                                    this.id == that.id && 
                                    this.startTime == that.startTime &&
                                    this.endTime == that.endTime &&
                                    this.location == that.location &&  
                                    this.label == that.label && 
                                    this.typex == that.typex && 
                                    this.other == that.other                   
      case _ => false
    }
   
    override lazy val hashCode:Int = {
      pr(pr(pr(pr(pr(pr(h(id),h(startTime)),h(endTime)),h(location)),h(label)),h(typex)),h(other))

    }

    
    def getAttributes :Set[Attribute]= {
      label.map(l =>new Label(ProvFactory.pf.prov_label,l).asInstanceOf[Attribute])++typex++location++other.values.flatten
    }
    
    
    def toNotation (sb:StringBuilder): Unit = {
      sb++="activity("
      sb++=id.toString()
      sb+=','
      timeOrMarker(startTime, sb)
      sb+=','
      timeOrMarker(endTime, sb)
      Attribute.toNotation(sb, label, typex, Set(), location, Set(), other)
      sb+=')'      
    }

}


trait ImmutableWasDerivedFrom extends Relation with org.openprovenance.prov.model.WasDerivedFrom with Identifiable  with HasLabel with HasType with HasOther with HasAttributes with Hashable {
  
    val generatedEntity: QualifiedName
    
    val usedEntity: QualifiedName
    
    val activity: QualifiedName
    
    val generation: QualifiedName
    
    val usage: QualifiedName

    @BeanProperty
    val kind =PROV_DERIVATION
    val enumType: Kind =Kind.wdf

    
    def getCause: QualifiedName = usedEntity
    def getEffect: QualifiedName = generatedEntity
    
    
    def getActivity: org.openprovenance.prov.model.QualifiedName = activity
    def getGeneratedEntity: org.openprovenance.prov.model.QualifiedName = generatedEntity
    def getGeneration: org.openprovenance.prov.model.QualifiedName = generation
    def getUsage: org.openprovenance.prov.model.QualifiedName = usage
    def getUsedEntity: org.openprovenance.prov.model.QualifiedName = usedEntity
    
    def setActivity(x$1: org.openprovenance.prov.model.QualifiedName): Unit = throw new UnsupportedOperationException
    def setGeneratedEntity(x$1: org.openprovenance.prov.model.QualifiedName): Unit = throw new UnsupportedOperationException
    def setGeneration(x$1: org.openprovenance.prov.model.QualifiedName): Unit = throw new UnsupportedOperationException
    def setUsage(x$1: org.openprovenance.prov.model.QualifiedName): Unit = throw new UnsupportedOperationException
    def setUsedEntity(x$1: org.openprovenance.prov.model.QualifiedName): Unit = throw new UnsupportedOperationException


    def canEqual(a: Any): Boolean = a.isInstanceOf[ImmutableWasDerivedFrom]

    override def equals(that: Any): Boolean =
    that match {
      case that:ImmutableWasDerivedFrom => that.canEqual(this) && 
                                           this.id == that.id && 
                                           this.generatedEntity == that.generatedEntity && 
                                           this.usedEntity == that.usedEntity && 
                                           this.activity == that.activity &&  
                                           this.generation == that.generation &&  
                                           this.usage == that.usage &&  
                                           this.label == that.label && 
                                           this.typex == that.typex && 
                                           this.other == that.other                   
      case _ => false
    }
    
    override lazy val hashCode:Int = {
        pr(pr(pr(pr(pr(pr(pr(pr(h(id),h(generatedEntity)),h(usedEntity)),h(activity)),h(usage)),h(generation)),h(label)),h(typex)),h(other))

    }
    
    def getAttributes :Set[Attribute]= {
      label.map(l =>new Label(ProvFactory.pf.prov_label,l).asInstanceOf[Attribute])++typex++other.values.flatten
    }
    
    def toNotation (sb:StringBuilder): Unit = {
      sb++="wasDerivedFrom("
      optionalId(id,sb)
      idOrMarker(generatedEntity, sb)
      sb+=','
      idOrMarker(usedEntity, sb)
      Attribute.toNotation(sb, label, typex, Set(), Set(), Set(), other)
      sb+=')'      
    }

}




trait ImmutableWasGeneratedBy extends Relation with org.openprovenance.prov.model.WasGeneratedBy with Identifiable  with HasLabel with HasType with HasOther with HasLocation with HasRole with HasAttributes with HasTime with Hashable {
  
    val entity: QualifiedName
    
    val activity: QualifiedName
    
    
    val time: Option[javax.xml.datatype.XMLGregorianCalendar]
    override def getTime:XMLGregorianCalendar={
      time match {
        case Some(v) => v
        case None => null
      }
    }  
    
    @BeanProperty
    val kind=PROV_GENERATION
    val enumType: Kind =Kind.wgb

    
    def getCause: QualifiedName = activity
    def getEffect: QualifiedName = entity
    
    
    def getActivity: org.openprovenance.prov.model.QualifiedName = activity
    def getEntity: org.openprovenance.prov.model.QualifiedName = entity
    

    def setActivity(x$1: org.openprovenance.prov.model.QualifiedName): Unit = throw new UnsupportedOperationException
    def setEntity(x$1: org.openprovenance.prov.model.QualifiedName): Unit = throw new UnsupportedOperationException
    def setTime(x$1: javax.xml.datatype.XMLGregorianCalendar): Unit = throw new UnsupportedOperationException


    def canEqual(a: Any): Boolean = a.isInstanceOf[ImmutableWasGeneratedBy]

    override def equals(that: Any): Boolean =
    that match {
      case that:ImmutableWasGeneratedBy => that.canEqual(this) && 
                                           this.id == that.id && 
                                           this.entity == that.entity && 
                                           this.activity == that.activity &&  
                                           this.time == that.time &&  
                                           this.location == that.location &&  
                                           this.label == that.label && 
                                           this.typex == that.typex && 
                                           this.role == that.role && 
                                           this.other == that.other                   
      case _ => false
    }
    
    override lazy val hashCode:Int = {
        pr(pr(pr(pr(pr(pr(pr(h(id),h(activity)),h(entity)),h(location)),h(label)),h(typex)),h(role)),h(other))

    }
    
    def getAttributes: Set[Attribute]= {
      label.map(l =>new Label(ProvFactory.pf.prov_label,l).asInstanceOf[Attribute])++location++typex++role++other.values.flatten
    }

    def toNotation (sb:StringBuilder): Unit = {
      sb++="wasGeneratedBy("
      optionalId(id,sb)
      idOrMarker(entity, sb)
      sb+=','
      idOrMarker(activity, sb)
      sb+=','
      timeOrMarker(time, sb)
      Attribute.toNotation(sb, label, typex, Set(), location, role, other)
      sb+=')'      
    }

 
}

trait ImmutableUsed extends Relation with org.openprovenance.prov.model.Used with Identifiable  with HasLabel with HasType with HasOther with HasLocation with HasRole with HasAttributes with HasTime with Hashable {
  
    val entity: QualifiedName
    
    val activity: QualifiedName
    
    
    val time: Option[javax.xml.datatype.XMLGregorianCalendar]
    override def getTime:XMLGregorianCalendar={
      time match {
        case Some(v) => v
        case None => null
      }
    }  
    
    @BeanProperty
    val kind=PROV_USAGE
    val enumType: Kind =Kind.usd

    
    def getCause: QualifiedName = entity
    def getEffect: QualifiedName = activity
    
    
    def getActivity: org.openprovenance.prov.model.QualifiedName = activity
    def getEntity: org.openprovenance.prov.model.QualifiedName = entity
    

    def setActivity(x$1: org.openprovenance.prov.model.QualifiedName): Unit = throw new UnsupportedOperationException
    def setEntity(x$1: org.openprovenance.prov.model.QualifiedName): Unit = throw new UnsupportedOperationException
    def setTime(x$1: javax.xml.datatype.XMLGregorianCalendar): Unit = throw new UnsupportedOperationException


    def canEqual(a: Any): Boolean = a.isInstanceOf[ImmutableUsed]

    override def equals(that: Any): Boolean =
    that match {
      case that:ImmutableUsed => that.canEqual(this) && 
                                           this.id == that.id && 
                                           this.entity == that.entity && 
                                           this.activity == that.activity &&  
                                           this.time == that.time &&  
                                           this.location == that.location &&  
                                           this.label == that.label && 
                                           this.typex == that.typex && 
                                           this.role == that.role && 
                                           this.other == that.other                   
      case _ => false
    }
    
    override lazy val hashCode:Int = {
        pr(pr(pr(pr(pr(pr(pr(h(id),h(activity)),h(entity)),h(location)),h(label)),h(typex)),h(role)),h(other))

    }
    
    def getAttributes :Set[Attribute]= {
      label.map(l =>new Label(ProvFactory.pf.prov_label,l).asInstanceOf[Attribute])++location++typex++role++other.values.flatten
    }
    
    def toNotation (sb:StringBuilder): Unit = {
      sb++="used("
      optionalId(id,sb)
      idOrMarker(activity, sb)
      sb+=','
      idOrMarker(entity, sb)
      sb+=','
      timeOrMarker(time, sb)
      Attribute.toNotation(sb, label, typex, Set(), location, role, other)
      sb+=')'      
    }
}

trait ImmutableWasInvalidatedBy extends Relation with org.openprovenance.prov.model.WasInvalidatedBy with Identifiable  with HasLabel with HasType with HasOther with HasLocation with HasRole with HasAttributes with HasTime with Hashable {
  
    val entity: QualifiedName
    
    val activity: QualifiedName
    
    
    val time: Option[javax.xml.datatype.XMLGregorianCalendar]
    override def getTime:XMLGregorianCalendar={
      time match {
        case Some(v) => v
        case None => null
      }
    }  
    
    @BeanProperty
    val kind=PROV_INVALIDATION
    val enumType: Kind =Kind.wib

    
    def getCause: QualifiedName = activity
    def getEffect: QualifiedName = entity
    
    
    def getActivity: org.openprovenance.prov.model.QualifiedName = activity
    def getEntity: org.openprovenance.prov.model.QualifiedName = entity
    

    def setActivity(x$1: org.openprovenance.prov.model.QualifiedName): Unit = throw new UnsupportedOperationException
    def setEntity(x$1: org.openprovenance.prov.model.QualifiedName): Unit = throw new UnsupportedOperationException
    def setTime(x$1: javax.xml.datatype.XMLGregorianCalendar): Unit = throw new UnsupportedOperationException


    def canEqual(a: Any): Boolean = a.isInstanceOf[ImmutableWasInvalidatedBy]

    override def equals(that: Any): Boolean =
    that match {
      case that:ImmutableWasInvalidatedBy => that.canEqual(this) && 
                                           this.id == that.id && 
                                           this.entity == that.entity && 
                                           this.activity == that.activity &&  
                                           this.time == that.time &&  
                                           this.location == that.location &&  
                                           this.label == that.label && 
                                           this.typex == that.typex && 
                                           this.role == that.role && 
                                           this.other == that.other                   
      case _ => false
    }
    
    override lazy val hashCode:Int = {
        pr(pr(pr(pr(pr(pr(pr(h(id),h(activity)),h(entity)),h(location)),h(label)),h(typex)),h(role)),h(other))

    }

     
    def getAttributes :Set[Attribute]= {
      label.map(l =>new Label(ProvFactory.pf.prov_label,l).asInstanceOf[Attribute])++location++typex++role++other.values.flatten
    }
    
    def toNotation (sb:StringBuilder): Unit = {
      sb++="wasInvalidatedBy("
      optionalId(id,sb)
      idOrMarker(entity, sb)
      sb+=','
      idOrMarker(activity, sb)
      sb+=','
      timeOrMarker(time, sb)
      Attribute.toNotation(sb, label, typex, Set(), location, role, other)
      sb+=')'      
    }

 
}



trait ImmutableWasStartedBy extends Relation with org.openprovenance.prov.model.WasStartedBy with Identifiable with HasLocation with HasRole with HasLabel with HasType with HasOther with HasAttributes with HasTime with Hashable {
  
    val activity: QualifiedName
    
    val trigger: QualifiedName
    
    val starter: QualifiedName
    
    val time: Option[javax.xml.datatype.XMLGregorianCalendar]
    override def getTime:XMLGregorianCalendar={
      time match {
        case Some(v) => v
        case None => null
      }
    }    
    
    @BeanProperty
    val kind= PROV_START
    val enumType: Kind =Kind.wsb

    
    def getCause: QualifiedName = trigger
    def getEffect: QualifiedName = activity
    
    
    
   
    def getActivity: org.openprovenance.prov.model.QualifiedName = activity
    def getTrigger: org.openprovenance.prov.model.QualifiedName = trigger
    def getStarter: org.openprovenance.prov.model.QualifiedName = starter


    def setActivity(x$1: org.openprovenance.prov.model.QualifiedName): Unit = throw new UnsupportedOperationException
    def setTrigger(x$1: org.openprovenance.prov.model.QualifiedName): Unit = throw new UnsupportedOperationException
    def setStarter(x$1: org.openprovenance.prov.model.QualifiedName): Unit = throw new UnsupportedOperationException
    def setTime(x$1: javax.xml.datatype.XMLGregorianCalendar): Unit = throw new UnsupportedOperationException
     
    
    def canEqual(a: Any): Boolean = a.isInstanceOf[ImmutableWasStartedBy]

    override def equals(that: Any): Boolean =
    that match {
      case that: ImmutableWasStartedBy => that.canEqual(this) && 
                           this.id == that.id && 
                           this.activity == that.activity && 
                           this.trigger == that.trigger && 
                           this.starter == that.starter &&  
                           this.time == that.time &&  
                           this.location == that.location &&  
                           this.label == that.label && 
                           this.role == that.role && 
                           this.typex == that.typex && 
                           this.other == that.other                   
      case _ => false
    }
   
    override lazy val hashCode:Int = {
        pr(pr(pr(pr(pr(pr(pr(pr(pr(h(id),h(activity)),h(trigger)),h(starter)),h(time)),h(location)), h(label)),h(typex)),h(role)),h(other))

    }
    
    def getAttributes :Set[Attribute]= {
      label.map(l =>new Label(ProvFactory.pf.prov_label,l).asInstanceOf[Attribute])++location++typex++role++other.values.flatten
    }
   
    def toNotation (sb:StringBuilder): Unit = {
      sb++="wasStartedBy("
      optionalId(id,sb)
      idOrMarker(activity, sb)
      sb+=','
      idOrMarker(trigger, sb)
      sb+=','
      idOrMarker(starter, sb)
      sb+=','
      timeOrMarker(time, sb)
      Attribute.toNotation(sb, label, typex, Set(), location, role, other)
      sb+=')'      
    }

}

trait ImmutableWasEndedBy extends Relation with org.openprovenance.prov.model.WasEndedBy with Identifiable with HasLocation with HasRole with HasLabel with HasType with HasOther with HasAttributes with HasTime with Hashable {
  
    val activity: QualifiedName
    
    val trigger: QualifiedName
    
    val ender: QualifiedName
    
    val time: Option[javax.xml.datatype.XMLGregorianCalendar]
    override def getTime:XMLGregorianCalendar={
      time match {
        case Some(v) => v
        case None => null
      }
    }    
    
    @BeanProperty
    val kind= PROV_END
    val enumType: Kind =Kind.web

    
    def getCause: QualifiedName = trigger
    def getEffect: QualifiedName = activity
    
    
    
   
    def getActivity: org.openprovenance.prov.model.QualifiedName = activity
    def getTrigger: org.openprovenance.prov.model.QualifiedName = trigger
    def getEnder: org.openprovenance.prov.model.QualifiedName = ender


    def setActivity(x$1: org.openprovenance.prov.model.QualifiedName): Unit = throw new UnsupportedOperationException
    def setTrigger(x$1: org.openprovenance.prov.model.QualifiedName): Unit = throw new UnsupportedOperationException
    def setEnder(x$1: org.openprovenance.prov.model.QualifiedName): Unit = throw new UnsupportedOperationException
    def setTime(x$1: javax.xml.datatype.XMLGregorianCalendar): Unit = throw new UnsupportedOperationException
     
    
    def canEqual(a: Any): Boolean = a.isInstanceOf[ImmutableWasEndedBy]

    override def equals(that: Any): Boolean =
    that match {
      case that: ImmutableWasEndedBy => that.canEqual(this) && 
                           this.id == that.id && 
                           this.activity == that.activity && 
                           this.trigger == that.trigger && 
                           this.ender == that.ender &&  
                           this.time == that.time &&  
                           this.location == that.location &&  
                           this.label == that.label && 
                           this.typex == that.typex && 
                           this.role == that.role && 
                           this.other == that.other                   
      case _ => false
    }
   
    override lazy val hashCode:Int = {
        pr(pr(pr(pr(pr(pr(pr(pr(pr(h(id),h(activity)),h(trigger)),h(ender)),h(time)),h(location)), h(label)),h(typex)),h(role)),h(other))

    }
    
    def getAttributes :Set[Attribute]= {
      label.map(l =>new Label(ProvFactory.pf.prov_label,l).asInstanceOf[Attribute])++location++typex++role++other.values.flatten
    }
    def toNotation (sb:StringBuilder): Unit = {
      sb++="wasEndedBy("
      optionalId(id,sb)
      idOrMarker(activity, sb)
      sb+=','
      idOrMarker(trigger, sb)
      sb+=','
      idOrMarker(ender, sb)
      sb+=','
      timeOrMarker(time, sb)
      Attribute.toNotation(sb, label, typex, Set(), location, role, other)
      sb+=')'      
    }

}


trait ImmutableActedOnBehalfOf extends Relation with org.openprovenance.prov.model.ActedOnBehalfOf with Identifiable with HasLabel with HasType with HasOther with HasAttributes with Hashable {

  val delegate: QualifiedName

  val responsible: QualifiedName

  val activity: QualifiedName


  @BeanProperty
  val kind= PROV_DELEGATION
  val enumType: Kind =Kind.aobo


  def getCause: QualifiedName = responsible
  def getEffect: QualifiedName = delegate




  def getActivity: org.openprovenance.prov.model.QualifiedName = activity
  def getResponsible: org.openprovenance.prov.model.QualifiedName = responsible
  def getDelegate: org.openprovenance.prov.model.QualifiedName = delegate


  def setActivity(x$1: org.openprovenance.prov.model.QualifiedName): Unit = throw new UnsupportedOperationException
  def setResponsible(x$1: org.openprovenance.prov.model.QualifiedName): Unit = throw new UnsupportedOperationException
  def setDelegate(x$1: org.openprovenance.prov.model.QualifiedName): Unit = throw new UnsupportedOperationException


  def canEqual(a: Any): Boolean = a.isInstanceOf[ImmutableActedOnBehalfOf]

  override def equals(that: Any): Boolean =
    that match {
      case that: ImmutableActedOnBehalfOf => that.canEqual(this) &&
        this.id == that.id &&
        this.delegate == that.delegate &&
        this.responsible == that.responsible &&
        this.activity == that.activity &&
        this.label == that.label &&
        this.typex == that.typex &&
        this.other == that.other
      case _ => false
    }

  override lazy val hashCode:Int = {
    pr(pr(pr(pr(pr(pr(h(id),h(delegate)),h(responsible)),h(activity)),h(label)),h(typex)),h(other))
  }

  def getAttributes :Set[Attribute]= {
    label.map(l =>new Label(ProvFactory.pf.prov_label,l).asInstanceOf[Attribute])++typex++other.values.flatten
  }


  def toNotation (sb:StringBuilder): Unit = {
    sb++="actedOnBehalfOf("
    optionalId(id,sb)
    idOrMarker(delegate, sb)
    sb+=','
    idOrMarker(responsible, sb)
    sb+=','
    idOrMarker(activity, sb)
    Attribute.toNotation(sb, label, typex, Set(), Set(), Set(), other)
    sb+=')'
  }

}



trait ImmutableWasAssociatedWith extends Relation with org.openprovenance.prov.model.WasAssociatedWith with Identifiable  with HasLabel with HasType with HasOther with HasRole with HasAttributes with Hashable {
  
    val activity: QualifiedName
    
    val agent: QualifiedName

    val plan: QualifiedName
    
    @BeanProperty
    val kind=PROV_ASSOCIATION
    val enumType: Kind =Kind.waw

    
    def getCause: QualifiedName = agent
    def getEffect: QualifiedName = activity
    def getOtherCause: Set[_ <: QualifiedName] = if (plan==null) Set() else Set(plan)
    
    
    def getActivity: org.openprovenance.prov.model.QualifiedName = activity
    def getAgent: org.openprovenance.prov.model.QualifiedName = agent
    def getPlan: org.openprovenance.prov.model.QualifiedName = plan
    

    def setActivity(x$1: org.openprovenance.prov.model.QualifiedName): Unit = throw new UnsupportedOperationException
    def setAgent(x$1: org.openprovenance.prov.model.QualifiedName): Unit = throw new UnsupportedOperationException
    def setPlan(x$1: org.openprovenance.prov.model.QualifiedName): Unit = throw new UnsupportedOperationException

       
    def canEqual(a: Any): Boolean = a.isInstanceOf[ImmutableWasAssociatedWith]

    override def equals(that: Any): Boolean =
    that match {
      case that: ImmutableWasAssociatedWith => that.canEqual(this) && 
                                     this.id == that.id && 
                                     this.activity == that.activity && 
                                     this.agent == that.agent &&
                                     this.plan == that.plan &&
                                     this.label == that.label && 
                                     this.typex == that.typex && 
                                     this.role == that.role && 
                                     this.other == that.other                   
      case _ => false
    }
   
    override lazy val hashCode:Int = {
        pr(pr(pr(pr(pr(pr(pr(h(id),h(activity)),h(agent)),h(plan)),h(label)),h(typex)),h(role)),h(other))

    }
    
    def getAttributes :Set[Attribute]= {
      label.map(l =>new Label(ProvFactory.pf.prov_label,l).asInstanceOf[Attribute])++typex++role++other.values.flatten
    }

 
  def toNotation (sb:StringBuilder): Unit = {
      sb++="wasAssociatedWith("
      optionalId(id,sb)
      idOrMarker(activity, sb)
      sb+=','
      idOrMarker(agent, sb)
      sb+=','
      idOrMarker(plan, sb)
      Attribute.toNotation(sb, label, typex, Set(), Set(), role, other)
      sb+=')'      
    }
}



trait ImmutableWasAttributedTo extends Relation with org.openprovenance.prov.model.WasAttributedTo with Identifiable  with HasLabel with HasType with HasOther with HasAttributes with Hashable {
  
    val entity: QualifiedName
    
    val agent: QualifiedName
    
    @BeanProperty
    val kind=PROV_ATTRIBUTION
    val enumType: Kind =Kind.wat

    
    def getCause: QualifiedName = agent
    def getEffect: QualifiedName = entity
    
    
    def getEntity: org.openprovenance.prov.model.QualifiedName = entity
    def getAgent: org.openprovenance.prov.model.QualifiedName = agent
    

    def setEntity(x$1: org.openprovenance.prov.model.QualifiedName): Unit = throw new UnsupportedOperationException
    def setAgent(x$1: org.openprovenance.prov.model.QualifiedName): Unit = throw new UnsupportedOperationException

       
    def canEqual(a: Any): Boolean = a.isInstanceOf[ImmutableWasAttributedTo]

    override def equals(that: Any): Boolean =
    that match {
      case that: ImmutableWasAttributedTo => that.canEqual(this) && 
                                             this.id == that.id && 
                                             this.entity == that.entity && 
                                             this.agent == that.agent &&
                                             this.label == that.label && 
                                             this.typex == that.typex && 
                                             this.other == that.other                   
      case _ => false
    }
   
    override lazy val hashCode:Int = {
        pr(pr(pr(pr(pr(h(id),h(entity)),h(agent)),h(label)),h(typex)),h(other))

    }
    
    def getAttributes :Set[Attribute]= {
      label.map(l =>new Label(ProvFactory.pf.prov_label,l).asInstanceOf[Attribute])++typex++other.values.flatten
    }

 
   def toNotation (sb:StringBuilder): Unit = {
      sb++="wasAttributedTo("
      optionalId(id,sb)
      idOrMarker(entity, sb)
      sb+=','
      idOrMarker(agent, sb)
      Attribute.toNotation(sb, label, typex, Set(), Set(), Set(), other)
      sb+=')'      
    }
}



trait ImmutableSpecializationOf extends Relation with org.openprovenance.prov.model.SpecializationOf with org.openprovenance.prov.model.extension.QualifiedSpecializationOf with Identifiable  with HasLabel with HasType with HasOther with HasAttributes with Hashable {
  
    val generalEntity: QualifiedName
    
    val specificEntity: QualifiedName

    override def isUnqualified: Boolean = id == null && other.isEmpty && label.isEmpty && typex.isEmpty
    
    @BeanProperty
    val kind=PROV_SPECIALIZATION
    val enumType: Kind =Kind.spec

    
    def getCause: QualifiedName = generalEntity
    def getEffect: QualifiedName = specificEntity
    
    
    def getGeneralEntity: org.openprovenance.prov.model.QualifiedName = generalEntity
    def getSpecificEntity: org.openprovenance.prov.model.QualifiedName = specificEntity
    

    def setGeneralEntity(x$1: org.openprovenance.prov.model.QualifiedName): Unit = throw new UnsupportedOperationException
    def setSpecificEntity(x$1: org.openprovenance.prov.model.QualifiedName): Unit = throw new UnsupportedOperationException

       
    def canEqual(a: Any): Boolean = a.isInstanceOf[ImmutableSpecializationOf]

    override def equals(that: Any): Boolean =
    that match {
      case that: ImmutableSpecializationOf => that.canEqual(this) && 
                                             this.id == that.id && 
                                             this.generalEntity == that.generalEntity && 
                                             this.specificEntity == that.specificEntity &&
                                             this.label == that.label && 
                                             this.typex == that.typex && 
                                             this.other == that.other                   
      case _ => false
    }
   
    override lazy val hashCode:Int = {
              pr(pr(pr(pr(pr(h(id),h(generalEntity)),h(specificEntity)),h(label)),h(typex)),h(other))
    }
    
    def getAttributes :Set[Attribute]= {
      label.map(l =>new Label(ProvFactory.pf.prov_label,l).asInstanceOf[Attribute])++typex++other.values.flatten
    }

   def toNotation (sb:StringBuilder): Unit = {
      val isExt=(id!=null) || label.nonEmpty || typex.nonEmpty || other.nonEmpty
      
      sb++= (if (isExt) "provext:specializationOf(" else  "specializationOf(")
      optionalId(id,sb)
      idOrMarker(specificEntity, sb)
      sb+=','
      idOrMarker(generalEntity, sb)
      Attribute.toNotation(sb, label, typex, Set(), Set(), Set(), other)
      sb+=')'      
    }
 
}



trait ImmutableAlternateOf extends Relation with org.openprovenance.prov.model.AlternateOf with org.openprovenance.prov.model.extension.QualifiedAlternateOf with Identifiable  with HasLabel with HasType with HasOther with HasAttributes with Hashable {
  
    val alternate1: QualifiedName
    
    val alternate2: QualifiedName

  override def isUnqualified: Boolean = id == null && other.isEmpty && label.isEmpty && typex.isEmpty


  @BeanProperty
    val kind=PROV_ALTERNATE
    val enumType: Kind =Kind.alt

    
    def getCause: QualifiedName = alternate2
    def getEffect: QualifiedName = alternate1
    
    
    def getAlternate1: org.openprovenance.prov.model.QualifiedName = alternate1
    def getAlternate2: org.openprovenance.prov.model.QualifiedName = alternate2
    

    def setAlternate1(x$1: org.openprovenance.prov.model.QualifiedName): Unit = throw new UnsupportedOperationException
    def setAlternate2(x$1: org.openprovenance.prov.model.QualifiedName): Unit = throw new UnsupportedOperationException

       
    def canEqual(a: Any): Boolean = a.isInstanceOf[ImmutableAlternateOf]

    override def equals(that: Any): Boolean =
    that match {
      case that: ImmutableAlternateOf => that.canEqual(this) && 
                                             this.id == that.id && 
                                             this.alternate1 == that.alternate1 && 
                                             this.alternate2 == that.alternate2 && 
                                             this.label == that.label && 
                                             this.typex == that.typex && 
                                             this.other == that.other                   
      case _ => false
    }
   
    override lazy val hashCode:Int = {
              pr(pr(pr(pr(pr(h(id),h(alternate1)),h(alternate2)),h(label)),h(typex)),h(other))
    }
    
    def getAttributes :Set[Attribute]= {
      label.map(l =>new Label(ProvFactory.pf.prov_label,l).asInstanceOf[Attribute])++typex++other.values.flatten
    }

   def toNotation (sb:StringBuilder): Unit = {
      val isExt=(id!=null) || label.nonEmpty || typex.nonEmpty || other.nonEmpty
      
      sb++= (if (isExt) "provext:alternateOf(" else  "alternateOf(")
      optionalId(id,sb)
      idOrMarker(alternate1, sb)
      sb+=','
      idOrMarker(alternate2, sb)
      Attribute.toNotation(sb, label, typex, Set(), Set(), Set(), other)
      sb+=')'      
    }
}


trait ImmutableMentionOf extends Relation with org.openprovenance.prov.model.MentionOf with org.openprovenance.prov.model.extension.QualifiedSpecializationOf with Identifiable  with HasLabel with HasType with HasOther with HasAttributes with Hashable {
  
    val generalEntity: QualifiedName
    
    val specificEntity: QualifiedName
    
    val bundle: QualifiedName

  override def isUnqualified: Boolean = id == null && other.isEmpty && label.isEmpty && typex.isEmpty


  @BeanProperty
    val kind=PROV_MENTION
    val enumType: Kind =Kind.men

    
    def getCause: QualifiedName = generalEntity
    def getEffect: QualifiedName = specificEntity
    def getBundle: QualifiedName = bundle
    
    
    def getGeneralEntity: org.openprovenance.prov.model.QualifiedName = generalEntity
    def getSpecificEntity: org.openprovenance.prov.model.QualifiedName = specificEntity
    

    def setGeneralEntity(x$1: org.openprovenance.prov.model.QualifiedName): Unit = throw new UnsupportedOperationException
    def setSpecificEntity(x$1: org.openprovenance.prov.model.QualifiedName): Unit = throw new UnsupportedOperationException
    def setBundle(x$1: org.openprovenance.prov.model.QualifiedName): Unit = throw new UnsupportedOperationException

       
    def canEqual(a: Any): Boolean = a.isInstanceOf[ImmutableMentionOf]

    override def equals(that: Any): Boolean =
    that match {
      case that: MentionOf => that.canEqual(this) && 
    		                      this.id == that.id && 
    		                      this.generalEntity == that.generalEntity && 
    		                      this.specificEntity == that.specificEntity &&
    		                      this.bundle == that.bundle &&
    		                      this.label == that.label && 
    		                      this.typex == that.typex && 
    		                      this.other == that.other                   
      case _ => false
    }
   
    override lazy val hashCode:Int = {
              pr(pr(pr(pr(pr(pr(h(id),h(generalEntity)),h(specificEntity)),h(bundle)),h(label)),h(typex)),h(other))
    }
    
    def getAttributes :Set[Attribute]= {
      label.map(l =>new Label(ProvFactory.pf.prov_label,l).asInstanceOf[Attribute])++typex++other.values.flatten
    }

   def toNotation (sb:StringBuilder): Unit = {
      val isExt=(id!=null) || label.nonEmpty || typex.nonEmpty || other.nonEmpty
      
      sb++= (if (isExt) "provext:mentionOf(" else  "mentionOf(")
      optionalId(id,sb)
      idOrMarker(specificEntity, sb)
      sb+=','
      idOrMarker(generalEntity, sb)
      sb+=','
      idOrMarker(bundle, sb)
      Attribute.toNotation(sb, label, typex, Set(), Set(), Set(), other)
      sb+=')'      
    }
 
}


trait ImmutableWasInformedBy extends Relation with org.openprovenance.prov.model.WasInformedBy with Identifiable  with HasLabel with HasType with HasOther with HasAttributes with Hashable {
  
    val informed: QualifiedName
    
    val informant: QualifiedName
    
    @BeanProperty
    val kind=PROV_COMMUNICATION
    val enumType: Kind =Kind.winfob

    
    def getCause: QualifiedName = informant
    def getEffect: QualifiedName = informed
    
    
    def getInformant: org.openprovenance.prov.model.QualifiedName = informant
    def getInformed: org.openprovenance.prov.model.QualifiedName = informed
    

    def setInformed(x$1: org.openprovenance.prov.model.QualifiedName): Unit = throw new UnsupportedOperationException
    def setInformant(x$1: org.openprovenance.prov.model.QualifiedName): Unit = throw new UnsupportedOperationException

       
    def canEqual(a: Any): Boolean = a.isInstanceOf[ImmutableWasInformedBy]

    override def equals(that: Any): Boolean =
    that match {
      case that: ImmutableWasInformedBy => that.canEqual(this) && 
                                             this.id == that.id && 
                                             this.informed == that.informed && 
                                             this.informant == that.informant && 
                                             this.label == that.label && 
                                             this.typex == that.typex && 
                                             this.other == that.other                   
      case _ => false
    }
   
    override lazy val hashCode:Int = {
              pr(pr(pr(pr(pr(h(id),h(informed)),h(informant)),h(label)),h(typex)),h(other))

    }
    
    def getAttributes :Set[Attribute]= {
      label.map(l =>new Label(ProvFactory.pf.prov_label,l).asInstanceOf[Attribute])++typex++other.values.flatten
    }
    
    def toNotation (sb:StringBuilder): Unit = {
      sb++="wasInformedBy("
      optionalId(id,sb)
      idOrMarker(informed, sb)
      sb+=','
      idOrMarker(informant, sb)
      Attribute.toNotation(sb, label, typex, Set(), Set(), Set(), other)
      sb+=')'      
    }

}


trait ImmutableWasInfluencedBy extends Relation with org.openprovenance.prov.model.WasInfluencedBy with Identifiable  with HasLabel with HasType with HasOther with HasAttributes with Hashable {
  
    val influencee: QualifiedName
    
    val influencer: QualifiedName
    
    @BeanProperty
    val kind=PROV_INFLUENCE
    val enumType: Kind =Kind.winfl

    
    def getCause: QualifiedName = influencer
    def getEffect: QualifiedName = influencee
    
    
    def getInfluencer: org.openprovenance.prov.model.QualifiedName = influencer
    def getInfluencee: org.openprovenance.prov.model.QualifiedName = influencee
    

    def setInfluencee(x$1: org.openprovenance.prov.model.QualifiedName): Unit = throw new UnsupportedOperationException
    def setInfluencer(x$1: org.openprovenance.prov.model.QualifiedName): Unit = throw new UnsupportedOperationException

       
    def canEqual(a: Any): Boolean = a.isInstanceOf[ImmutableWasInfluencedBy]

    override def equals(that: Any): Boolean =
    that match {
      case that: ImmutableWasInfluencedBy => that.canEqual(this) && 
                                             this.id == that.id && 
                                             this.influencee == that.influencee && 
                                             this.influencer == that.influencer && 
                                             this.label == that.label && 
                                             this.typex == that.typex && 
                                             this.other == that.other                   
      case _ => false
    }
   
    override lazy val hashCode:Int = {
              pr(pr(pr(pr(pr(h(id),h(influencee)),h(influencer)),h(label)),h(typex)),h(other))

    }

    def getAttributes :Set[Attribute]= {
      label.map(l =>new Label(ProvFactory.pf.prov_label,l).asInstanceOf[Attribute])++typex++other.values.flatten
    }
    
    
    def toNotation (sb:StringBuilder): Unit = {
      sb++="wasInfluencedBy("
      optionalId(id,sb)
      idOrMarker(influencee, sb)
      sb+=','
      idOrMarker(influencer, sb)
      Attribute.toNotation(sb, label, typex, Set(), Set(), Set(), other)
      sb+=')'      
    }
}



trait ImmutableHadMember extends Relation with org.openprovenance.prov.model.HadMember with QualifiedHadMember with Identifiable  with HasLabel with HasType with HasOther with HasAttributes with Hashable {
  

    val collection: QualifiedName
    
    val entity: Set[QualifiedName]

   override def isUnqualified: Boolean = id == null && other.isEmpty && label.isEmpty && typex.isEmpty


  @BeanProperty
    val kind=PROV_MEMBERSHIP
    val enumType: Kind =Kind.mem

    
    def getCause: QualifiedName = entity.head
    def getEffect: QualifiedName = collection

    def getCollection: org.openprovenance.prov.model.QualifiedName = collection
    def setCollection(collection: org.openprovenance.prov.model.QualifiedName): Unit = throw new UnsupportedOperationException
    def getEntity: java.util.List[org.openprovenance.prov.model.QualifiedName] = {
      entity.toSeq.asInstanceOf[Seq[org.openprovenance.prov.model.QualifiedName]].asJava
    }
    def setEntity(entity: java.util.List[org.openprovenance.prov.model.QualifiedName]) = throw new UnsupportedOperationException




    
    def canEqual(a: Any): Boolean = a.isInstanceOf[ImmutableHadMember]

    override def equals(that: Any): Boolean =
    that match {
      case that: ImmutableHadMember => that.canEqual(this) && 
                                       this.id == that.id &&
                                       this.collection == that.collection &&
                                       this.entity == that.entity &&
                                       this.label == that.label && 
                                       this.typex == that.typex && 
                                       this.other == that.other                   
      case _ => false
    }
    
    override lazy val hashCode:Int = {
        pr(pr(pr(pr(pr(h(id),h(collection)),h(entity)),h(label)),h(typex)),h(other))
    }
    
    def getAttributes :Set[Attribute]= {
      label.map(l =>new Label(ProvFactory.pf.prov_label,l).asInstanceOf[Attribute])++typex++other.values.flatten
    }
    
    def toNotation (sb:StringBuilder): Unit = {
    	val isExt=(id!=null) || label.nonEmpty || typex.nonEmpty || other.nonEmpty
      
      sb++= (if (isExt) "provext:hadMember(" else  "hadMember(")
      optionalId(id,sb)
      idOrMarker(collection, sb)
      sb+=','
      idOrMarker(entity.head, sb)
      Attribute.toNotation(sb, label, typex, Set(), Set(), Set(), other)
      sb+=')'      
    }

}




object Statement {
	def apply(statement: org.openprovenance.prov.model.Statement): Statement = {
			statement match {
			case s: Statement => s
			case e: org.openprovenance.prov.model.Entity => Entity(e)
			case a: org.openprovenance.prov.model.Activity => Activity(a)
			case ag: org.openprovenance.prov.model.Agent => Agent(ag)
			case u: org.openprovenance.prov.model.Used =>   Used(u)
			case w: org.openprovenance.prov.model.WasDerivedFrom => WasDerivedFrom(w)
			case w: org.openprovenance.prov.model.WasGeneratedBy => WasGeneratedBy(w)
			case e: org.openprovenance.prov.model.WasInvalidatedBy => WasInvalidatedBy(e)
			case e: org.openprovenance.prov.model.ActedOnBehalfOf => ActedOnBehalfOf(e)
			case e: org.openprovenance.prov.model.WasAssociatedWith => WasAssociatedWith(e)
			case e: org.openprovenance.prov.model.WasAttributedTo => WasAttributedTo(e)
      case e: org.openprovenance.prov.model.WasStartedBy => WasStartedBy(e)
      case e: org.openprovenance.prov.model.WasEndedBy => WasEndedBy(e)
      case e: org.openprovenance.prov.model.extension.QualifiedSpecializationOf => SpecializationOf(e)
      case e: org.openprovenance.prov.model.extension.QualifiedAlternateOf => AlternateOf(e)
      case e: org.openprovenance.prov.model.extension.QualifiedHadMember => HadMember(e)
      case e: org.openprovenance.prov.model.SpecializationOf => SpecializationOf(e)
      case e: org.openprovenance.prov.model.AlternateOf => AlternateOf(e)
      case e: org.openprovenance.prov.model.WasInformedBy => WasInformedBy(e)
      case e: org.openprovenance.prov.model.WasInfluencedBy => WasInfluencedBy(e)

      }
	}

  def apply(statement: org.openprovenance.prov.model.Statement, gensym: () => org.openprovenance.prov.model.QualifiedName): Statement = {
    statement match {
      case e: org.openprovenance.prov.model.Entity => Entity(e,gensym)
      case a: org.openprovenance.prov.model.Activity => Activity(a,gensym)
      case ag: org.openprovenance.prov.model.Agent => Agent(ag,gensym)
      case u: org.openprovenance.prov.model.Used =>   Used(u,gensym)
      case w: org.openprovenance.prov.model.WasDerivedFrom => WasDerivedFrom(w,gensym)
      case w: org.openprovenance.prov.model.WasGeneratedBy => WasGeneratedBy(w,gensym)
      case e: org.openprovenance.prov.model.WasInvalidatedBy => WasInvalidatedBy(e,gensym)
      case e: org.openprovenance.prov.model.ActedOnBehalfOf => ActedOnBehalfOf(e,gensym)
      case e: org.openprovenance.prov.model.WasAssociatedWith => WasAssociatedWith(e,gensym)
      case e: org.openprovenance.prov.model.WasAttributedTo => WasAttributedTo(e,gensym)
      case e: org.openprovenance.prov.model.WasStartedBy => WasStartedBy(e,gensym)
      case e: org.openprovenance.prov.model.WasEndedBy => WasEndedBy(e,gensym)
      case e: org.openprovenance.prov.model.SpecializationOf => SpecializationOf(e,gensym)
      case e: org.openprovenance.prov.model.AlternateOf => AlternateOf(e,gensym)
      case m: org.openprovenance.prov.model.HadMember => HadMember(m,gensym)
    }
  }


  def apply(statements: java.util.Collection[org.openprovenance.prov.model.Statement]): Set[Statement] = {
			val statements2=statements.asScala.toSet
					statements2.map(s => Statement(s))
	}
}

object StatementOrBundle {
    def apply(statement: org.openprovenance.prov.model.StatementOrBundle): StatementOrBundle = {
          statement match {
            case s: StatementOrBundle => s
            case e: org.openprovenance.prov.model.Entity => Entity(e)
            case a: org.openprovenance.prov.model.Activity => Activity(a)
            case a: org.openprovenance.prov.model.Agent => Agent(a)
            case u: org.openprovenance.prov.model.Used =>   Used(u)
            case w: org.openprovenance.prov.model.WasDerivedFrom => WasDerivedFrom(w)
            case w: org.openprovenance.prov.model.WasGeneratedBy => WasGeneratedBy(w)
            case e: org.openprovenance.prov.model.WasInvalidatedBy => WasInvalidatedBy(e)
            case e: org.openprovenance.prov.model.ActedOnBehalfOf => ActedOnBehalfOf(e)
            case e: org.openprovenance.prov.model.WasAssociatedWith => WasAssociatedWith(e)
            case e: org.openprovenance.prov.model.WasAttributedTo => WasAttributedTo(e)
            case e: org.openprovenance.prov.model.WasStartedBy => WasStartedBy(e)
            case e: org.openprovenance.prov.model.WasEndedBy => WasEndedBy(e)
            case e: org.openprovenance.prov.model.WasInformedBy => WasInformedBy(e)
            case e: org.openprovenance.prov.model.WasInfluencedBy => WasInfluencedBy(e)
            case e: org.openprovenance.prov.model.SpecializationOf => SpecializationOf(e)
            case e: org.openprovenance.prov.model.AlternateOf => AlternateOf(e)
            case e: org.openprovenance.prov.model.HadMember => HadMember(e)
            case e: org.openprovenance.prov.model.Bundle => Bundle(e)

          }
    }

    def apply(statements: java.util.Collection[org.openprovenance.prov.model.Statement]): Set[StatementOrBundle] = {
         val statements2=statements.asScala.toSeq
         statements2.map(s => Statement(s)).toSet
    }
      
    def forBundle(statements: java.util.Collection[org.openprovenance.prov.model.Bundle]): Set[StatementOrBundle] = {
         val statements2=statements.asScala.toSeq
         statements2.map(s => StatementOrBundle(s)).toSet
    }
    def forStatementOrBundle(statements: java.util.Collection[org.openprovenance.prov.model.StatementOrBundle]): Set[StatementOrBundle] = {
         val statements2=statements.asScala.toSeq
         statements2.map(s => StatementOrBundle(s)).toSet
    }
}

trait StatementOrBundle extends org.openprovenance.prov.model.StatementOrBundle with Identifiable {
        val enumType : Kind.Kind
        def toNotation(sb: StringBuilder): Unit

}

abstract class Statement extends org.openprovenance.prov.model.Statement with Identifiable with StatementOrBundle {
    override def toString:String = {
       val sb=new StringBuilder
       toNotation(sb)
       sb.toString()
    }
    def toNotation(sb: StringBuilder): Unit
    
    def getAttributes :Set[Attribute]
    def addAttributes (attr: Set[Attribute]): Statement

}

abstract class Node extends Statement with HasType with HasOther {
        
      @inline final def ll(m: Map[String,String], s: Set[LangString]): Set[LangString] = {
          if (s==null) null else s.map(l => l.rename(m))
      }
      
  
  def rename(m: Map[QualifiedName,QualifiedName], lab: Map[String,String]): Node with Element with model.HasType with model.HasLocation with model.HasOther with HasLocation with HasLabel with HasAttributes with Hashable = {
    this match {
      case e:Entity  => new Entity(m(e.id), ll(lab,e.label), e.typex, e.value,e.location,e.other)      
      case e:Activity => new Activity(m(e.id), e.startTime,e.endTime, ll(lab,e.label), e.typex, e.location,e.other)
      case e:Agent    => new Agent(m(e.id), ll(lab,e.label), e.typex, e.value,e.location,e.other)
    }
  }
  
}


trait Relation extends StatementOrBundle with HasAttributes with HasOther {
  def getCause: QualifiedName
  def getEffect: QualifiedName
//  def getOtherCauses(): Set[QualifiedName]
  def rename(m: Map[QualifiedName,QualifiedName]): Relation = { this }

}

object Entity {
  def apply(e: org.openprovenance.prov.model.Entity):Entity = {
    e match {
      case e:Entity => e
      case _ =>
            new Entity(QualifiedName(e.getId),
                       LangString.fromJavaCollection(e.getLabel),
                       Type(e.getType),
                       if (e.getValue==null) None else Some(Value(e.getValue)),
                       Location(e.getLocation),
                       Other(e.getOther))
    }
  }
  def apply(e: org.openprovenance.prov.model.Entity, gensym: () => org.openprovenance.prov.model.QualifiedName):Entity = {
    new Entity(if (e.getId==null) QualifiedName(gensym()) else QualifiedName(e.getId),
      LangString.fromJavaCollection(e.getLabel),
      Type(e.getType),
      if (e.getValue==null) None else Some(Value(e.getValue)),
      Location(e.getLocation),
      Other(e.getOther))
  }
}


class Entity(val id: QualifiedName,
             val label: Set[LangString],
             val typex: Set[Type],
             val value: Option[Value],
             val location: Set[Location],
             val other: Map[QualifiedName,Set[Other]]) extends Node with ImmutableEntity {

	def addAttributes (attr: Set[Attribute]): Entity = {
      ProvFactory.pf.newEntity(id,getAttributes ++ attr)
	}

  //override def setIndexedAttributes(qn: Any, attributes: util.Set[model.Attribute]): Unit = ???


  @unused
  def getIndexedAttributes: util.Map[model.QualifiedName, util.Set[model.Attribute]] = {
    convertAttributes(label, typex, value, location, Set(), other)
  }



}

object Agent {
  def apply(e: org.openprovenance.prov.model.Agent):Agent = {
    e match {
      case e:Agent => e
      case _ =>
            new Agent(QualifiedName(e.getId),
                       LangString.fromJavaCollection(e.getLabel),
                       Type(e.getType),
                       None,
                       Location(e.getLocation),
                       Other(e.getOther))
    }
  }

  def apply(e: org.openprovenance.prov.model.Agent, gensym: () => org.openprovenance.prov.model.QualifiedName):Agent = {
    new Agent(if (e.getId==null) QualifiedName(gensym()) else QualifiedName(e.getId),
      LangString.fromJavaCollection(e.getLabel),
      Type(e.getType),
      None,
      Location(e.getLocation),
      Other(e.getOther))
  }
}
class Agent(val id: QualifiedName,
            val label: Set[LangString],
            val typex: Set[Type],
            val value: Option[Value],
            val location: Set[Location],
            val other: Map[QualifiedName,Set[Other]]) extends Node with ImmutableAgent  {


	def addAttributes (attr: Set[Attribute]): Agent = {
      ProvFactory.pf.newAgent(id,getAttributes ++ attr)
	}


  @unused
  def getIndexedAttributes: util.Map[model.QualifiedName, util.Set[model.Attribute]] = {
    convertAttributes(label, typex, value, location, Set(), other)
  }




}


object Activity {
  def apply(a: org.openprovenance.prov.model.Activity):Activity = {
    a match {
      case a:Activity => a
      case _ =>
            new Activity(QualifiedName(a.getId),
                         Option(a.getStartTime),
                         Option(a.getEndTime),
                         LangString.fromJavaCollection(a.getLabel),
                         Type(a.getType),
                         Location(a.getLocation),
                         Other(a.getOther))
    }
  }

  def apply(a: org.openprovenance.prov.model.Activity, gensym: () => org.openprovenance.prov.model.QualifiedName):Activity = {
    new Activity(if (a.getId==null) QualifiedName(gensym()) else QualifiedName(a.getId),
      Option(a.getStartTime),
      Option(a.getEndTime),
      LangString.fromJavaCollection(a.getLabel),
      Type(a.getType),
      Location(a.getLocation),
      Other(a.getOther))
  }
}

class Activity(val id: QualifiedName,
               val startTime: Option[XMLGregorianCalendar],
               val endTime: Option[XMLGregorianCalendar],
               val label: Set[LangString],
               val typex: Set[Type],
               val location: Set[Location],
               val other: Map[QualifiedName,Set[Other]]) extends Node with ImmutableActivity  {

      @BeanProperty
      val kind=PROV_ACTIVITY
      val enumType: Kind =Kind.act

      def addAttributes (attr: Set[Attribute]): Activity = {
    		  ProvFactory.pf.newActivity(id, startTime, endTime, getAttributes ++ attr)
      }


  @unused
  def getIndexedAttributes: util.Map[model.QualifiedName, util.Set[model.Attribute]] = {
    convertAttributes(label, typex, None, location, Set(), other)
  }




}

object WasDerivedFrom {
  def apply(e: org.openprovenance.prov.model.WasDerivedFrom):WasDerivedFrom = {
    e match {
      case e:WasDerivedFrom => e
      case _ =>
            new WasDerivedFrom(QualifiedName(e.getId),
                               QualifiedName(e.getGeneratedEntity),
                               QualifiedName(e.getUsedEntity),
                               QualifiedName(e.getActivity),
                               QualifiedName(e.getGeneration),
                               QualifiedName(e.getUsage),
                               LangString.fromJavaCollection(e.getLabel),
                               Type(e.getType),
                               Other(e.getOther))
    }
  }

  def apply(e: org.openprovenance.prov.model.WasDerivedFrom, gensym: () => org.openprovenance.prov.model.QualifiedName):WasDerivedFrom = {
    new WasDerivedFrom(if (e.getId==null) QualifiedName(gensym()) else QualifiedName(e.getId),
      QualifiedName(e.getGeneratedEntity),
      QualifiedName(e.getUsedEntity),
      QualifiedName(e.getActivity),
      QualifiedName(e.getGeneration),
      QualifiedName(e.getUsage),
      LangString.fromJavaCollection(e.getLabel),
      Type(e.getType),
      Other(e.getOther))
  }
}

class WasDerivedFrom(val id: QualifiedName,
                     val generatedEntity: QualifiedName,
                     val usedEntity: QualifiedName,
                     val activity: QualifiedName,
                     val generation: QualifiedName,
                     val usage: QualifiedName,
                     val label: Set[LangString],
                     val typex: Set[Type],
                     val other: Map[QualifiedName,Set[Other]]) extends Statement with ImmutableWasDerivedFrom  {


      def addAttributes (attr: Set[Attribute]): WasDerivedFrom = {
    		  ProvFactory.pf.newWasDerivedFrom(id,
    		                                   generatedEntity, usedEntity,
    		                                   activity,
    		                                   generation, usage,
    		                                   getAttributes ++ attr)
      }

      override def rename(m: Map[QualifiedName,QualifiedName]): WasDerivedFrom ={
	     new WasDerivedFrom(m.getOrElse(id, id),
	                        m(generatedEntity),
	                        m(usedEntity),
	                        if (activity==null) null else m(activity),
	                        if (generation==null) null else m.getOrElse(generation,generation),
	                        if (usage==null) null else m.getOrElse(usage,usage),
	                        label,
	                        typex,
	                        this.other)
	   }


  @unused
  def getIndexedAttributes: util.Map[model.QualifiedName, util.Set[model.Attribute]] = {
    convertAttributes(label, typex, None, Set(), Set(), other)
  }


}

object WasGeneratedBy {
  def apply(e: org.openprovenance.prov.model.WasGeneratedBy):WasGeneratedBy = {
    e match {
      case e:WasGeneratedBy => e
      case _ =>
            new WasGeneratedBy(QualifiedName(e.getId),
                               QualifiedName(e.getEntity),
                               QualifiedName(e.getActivity),
                               Option(e.getTime),
                               LangString.fromJavaCollection(e.getLabel),
                               Type(e.getType),
                               Location(e.getLocation),
                               Role(e.getRole),
                               Other(e.getOther))
    }
  }

  def apply(e: org.openprovenance.prov.model.WasGeneratedBy, gensym: () => org.openprovenance.prov.model.QualifiedName):WasGeneratedBy = {
    new WasGeneratedBy(if (e.getId==null) QualifiedName(gensym()) else QualifiedName(e.getId),
      QualifiedName(e.getEntity),
      QualifiedName(e.getActivity),
      Option(e.getTime),
      LangString.fromJavaCollection(e.getLabel),
      Type(e.getType),
      Location(e.getLocation),
      Role(e.getRole),
      Other(e.getOther))
  }
}

class WasGeneratedBy(val id: QualifiedName,
                     val entity: QualifiedName,
                     val activity: QualifiedName,
                     val time:Option[XMLGregorianCalendar],
                     val label: Set[LangString],
                     val typex: Set[Type],
                     val location: Set[Location],
                     val role: Set[Role],
                     val other: Map[QualifiedName,Set[Other]]) extends Statement with ImmutableWasGeneratedBy  {

	   def addAttributes (attr: Set[Attribute]): WasGeneratedBy = {
    		  ProvFactory.pf.newWasGeneratedBy(id,
    		                                   entity, activity, time,
    		                                   getAttributes ++ attr)
	   }

	   override def rename(m: Map[QualifiedName,QualifiedName]): WasGeneratedBy ={
	     new WasGeneratedBy(m.getOrElse(id, id),
	                        m(entity),
	                        m(activity),
	                        time,
	                        label,
	                        typex,
	                        location,
	                        role,
	                        other)
	   }


  @unused
  def getIndexedAttributes: util.Map[model.QualifiedName, util.Set[model.Attribute]] = {
    convertAttributes(label, typex, None, location, role, other)
  }


}

object Used {
  def apply(e: org.openprovenance.prov.model.Used):Used = {
    e match {
      case e:Used => e
      case _ =>
            new Used(QualifiedName(e.getId),
            		     QualifiedName(e.getActivity),
            		     QualifiedName(e.getEntity),
            		     Option(e.getTime),
            		     LangString.fromJavaCollection(e.getLabel),
            		     Type(e.getType),
            		     Location(e.getLocation),
            		     Role(e.getRole),
            		     Other(e.getOther))
    }
  }

  def apply(e: org.openprovenance.prov.model.Used, gensym: () => org.openprovenance.prov.model.QualifiedName):Used = {
    new Used(if (e.getId==null) QualifiedName(gensym()) else QualifiedName(e.getId),
      QualifiedName(e.getActivity),
      QualifiedName(e.getEntity),
      Option(e.getTime),
      LangString.fromJavaCollection(e.getLabel),
      Type(e.getType),
      Location(e.getLocation),
      Role(e.getRole),
      Other(e.getOther))
  }
}

class Used(val id: QualifiedName,
                     val activity: QualifiedName,
                     val entity: QualifiedName,
                     val time:Option[XMLGregorianCalendar],
                     val label: Set[LangString],
                     val typex: Set[Type],
                     val location: Set[Location],
                     val role: Set[Role],
                     val other: Map[QualifiedName,Set[Other]]) extends Statement with ImmutableUsed  {

	   def addAttributes (attr: Set[Attribute]): Used = {
    		  ProvFactory.pf.newUsed(id,
    				                     activity, entity, time,
    				                     getAttributes ++ attr)
	   }

	   override def rename(m: Map[QualifiedName,QualifiedName]): Used ={
	     new Used(m.getOrElse(id, id),
	    		      m(activity),
	    		      m(entity),
	    		      time,
	    		      label,
	    		      typex,
	    		      location,
	    		      role,
	    		      other)
	   }

  @unused
  def getIndexedAttributes: util.Map[model.QualifiedName, util.Set[model.Attribute]] = {
    convertAttributes(label, typex, None, location, role, other)
  }
}

object WasInvalidatedBy {
  def apply(e: org.openprovenance.prov.model.WasInvalidatedBy):WasInvalidatedBy = {
    e match {
      case e:WasInvalidatedBy => e
      case _ =>
            new WasInvalidatedBy(QualifiedName(e.getId),
                                QualifiedName(e.getEntity),
                                QualifiedName(e.getActivity),
                                Option(e.getTime),
                                LangString.fromJavaCollection(e.getLabel),
                                Type(e.getType),
                                Location(e.getLocation),
                                Role(e.getRole),
                                Other(e.getOther))
    }
  }

  def apply(e: org.openprovenance.prov.model.WasInvalidatedBy, gensym: () => org.openprovenance.prov.model.QualifiedName):WasInvalidatedBy = {
    new WasInvalidatedBy(if (e.getId==null) QualifiedName(gensym()) else QualifiedName(e.getId),
      QualifiedName(e.getEntity),
      QualifiedName(e.getActivity),
      Option(e.getTime),
      LangString.fromJavaCollection(e.getLabel),
      Type(e.getType),
      Location(e.getLocation),
      Role(e.getRole),
      Other(e.getOther))
  }
}

class WasInvalidatedBy(val id: QualifiedName,
                     val entity: QualifiedName,
                     val activity: QualifiedName,
                     val time:Option[XMLGregorianCalendar],
                     val label: Set[LangString],
                     val typex: Set[Type],
                     val location: Set[Location],
                     val role: Set[Role],
                     val other: Map[QualifiedName,Set[Other]]) extends Statement with ImmutableWasInvalidatedBy  {
	   def addAttributes (attr: Set[Attribute]): WasInvalidatedBy = {
    		  ProvFactory.pf.newWasInvalidatedBy(id,
    		                                   entity, activity, time,
    		                                   getAttributes ++ attr)
	   }

	   override def rename(m: Map[QualifiedName,QualifiedName]): WasInvalidatedBy ={
	     new WasInvalidatedBy(m.getOrElse(id, id),
	                        m(entity),
	                        m(activity),
	                        time,
	                        label,
	                        typex,
	                        location,
	                        role,
	                        other)
	   }

  @unused
  def getIndexedAttributes: util.Map[model.QualifiedName, util.Set[model.Attribute]] = {
    convertAttributes(label, typex, None, location, role, other)
  }

}

object WasStartedBy {
  def apply(e: org.openprovenance.prov.model.WasStartedBy): WasStartedBy = {
    e match {
      case e: WasStartedBy => e
      case _ =>
        new WasStartedBy( QualifiedName(e.getId),
                          QualifiedName(e.getActivity),
                          QualifiedName(e.getTrigger),
                          QualifiedName(e.getStarter),
                          Option(e.getTime),
                          LangString.fromJavaCollection(e.getLabel),
                          Type(e.getType),
                          Location(e.getLocation),
                          Role(e.getRole),
                          Other(e.getOther))
    }
  }

  def apply(e: org.openprovenance.prov.model.WasStartedBy, gensym: () => org.openprovenance.prov.model.QualifiedName):WasStartedBy = {
    new WasStartedBy(if (e.getId==null) QualifiedName(gensym()) else QualifiedName(e.getId),
      QualifiedName(e.getActivity),
      QualifiedName(e.getTrigger),
      QualifiedName(e.getStarter),
      Option(e.getTime),
      LangString.fromJavaCollection(e.getLabel),
      Type(e.getType),
      Location(e.getLocation),
      Role(e.getRole),
      Other(e.getOther))
  }
}

class WasStartedBy(val id: QualifiedName,
                   val activity: QualifiedName,
                   val trigger: QualifiedName,
                   val starter: QualifiedName,
                   val time:Option[XMLGregorianCalendar],
                   val label: Set[LangString],
                   val typex: Set[Type],
                   val location: Set[Location],
                   val role: Set[Role],
                   val other: Map[QualifiedName,Set[Other]]) extends Statement with ImmutableWasStartedBy  {


     def addAttributes (attr: Set[Attribute]): WasStartedBy = {
    		  ProvFactory.pf.newWasStartedBy(id,
    				                             activity, trigger, starter, time,
    				                             getAttributes ++ attr)
	   }


  @unused
  def getIndexedAttributes: util.Map[model.QualifiedName, util.Set[model.Attribute]] = {
    convertAttributes(label, typex, None, location, role, other)
  }

}

object WasEndedBy {
  def apply(e: org.openprovenance.prov.model.WasEndedBy): WasEndedBy = {
    e match {
      case e: WasEndedBy => e
      case _ =>
        new WasEndedBy(QualifiedName(e.getId),
          QualifiedName(e.getActivity),
          QualifiedName(e.getTrigger),
          QualifiedName(e.getEnder),
          Option(e.getTime),
          LangString.fromJavaCollection(e.getLabel),
          Type(e.getType),
          Location(e.getLocation),
          Role(e.getRole),
          Other(e.getOther))
    }
  }

  def apply(e: org.openprovenance.prov.model.WasEndedBy, gensym: () => org.openprovenance.prov.model.QualifiedName):WasEndedBy = {
    new WasEndedBy(if (e.getId==null) QualifiedName(gensym()) else QualifiedName(e.getId),
      QualifiedName(e.getActivity),
      QualifiedName(e.getTrigger),
      QualifiedName(e.getEnder),
      Option(e.getTime),
      LangString.fromJavaCollection(e.getLabel),
      Type(e.getType),
      Location(e.getLocation),
      Role(e.getRole),
      Other(e.getOther))
  }
}


class WasEndedBy(val id: QualifiedName,
                 val activity: QualifiedName,
                 val trigger: QualifiedName,
                 val ender: QualifiedName,
                 val time:Option[XMLGregorianCalendar],
                 val label: Set[LangString],
                 val typex: Set[Type],
                 val location: Set[Location],
                 val role: Set[Role],
                 val other: Map[QualifiedName,Set[Other]]) extends Statement with ImmutableWasEndedBy  {

  def addAttributes (attr: Set[Attribute]): WasEndedBy = {
    		  ProvFactory.pf.newWasEndedBy(id,
    				                           activity, trigger, ender, time,
    				                           getAttributes ++ attr)
	   }


  @unused
  def getIndexedAttributes: util.Map[model.QualifiedName, util.Set[model.Attribute]] = {
    convertAttributes(label, typex, None, location, role, other)
  }
}

object ActedOnBehalfOf {
  def apply(e: org.openprovenance.prov.model.ActedOnBehalfOf):ActedOnBehalfOf = {
    e match {
      case e:ActedOnBehalfOf => e
      case _ =>
            new ActedOnBehalfOf(QualifiedName(e.getId),
                                QualifiedName(e.getDelegate),
                                QualifiedName(e.getResponsible),
                                QualifiedName(e.getActivity),
                                LangString.fromJavaCollection(e.getLabel),
                                Type(e.getType),
                                Other(e.getOther))
    }
  }

  def apply(e: org.openprovenance.prov.model.ActedOnBehalfOf, gensym: () => org.openprovenance.prov.model.QualifiedName):ActedOnBehalfOf = {
    new ActedOnBehalfOf(if (e.getId==null) QualifiedName(gensym()) else QualifiedName(e.getId),
      QualifiedName(e.getDelegate),
      QualifiedName(e.getResponsible),
      QualifiedName(e.getActivity),
      LangString.fromJavaCollection(e.getLabel),
      Type(e.getType),
      Other(e.getOther))

  }
}

class ActedOnBehalfOf(val id: QualifiedName,
                      val delegate: QualifiedName,
                      val responsible: QualifiedName,
                      val activity: QualifiedName,
                      val label: Set[LangString],
                      val typex: Set[Type],
                      val other: Map[QualifiedName,Set[Other]]) extends Statement with ImmutableActedOnBehalfOf {

  def addAttributes (attr: Set[Attribute]): ActedOnBehalfOf = {
    ProvFactory.pf.newActedOnBehalfOf(id,
      delegate, responsible, activity,
      getAttributes ++ attr)
  }


  @unused
  def getIndexedAttributes: util.Map[model.QualifiedName, util.Set[model.Attribute]] = {
    convertAttributes(label, typex, None, Set(), Set(), other)
  }

}

object WasAssociatedWith {
  def apply(e: org.openprovenance.prov.model.WasAssociatedWith):WasAssociatedWith = {
    e match {
      case e:WasAssociatedWith => e
      case _ =>
            new WasAssociatedWith(QualifiedName(e.getId),
                                  QualifiedName(e.getActivity),
                                  QualifiedName(e.getAgent),
                                  QualifiedName(e.getPlan),
                                  LangString.fromJavaCollection(e.getLabel),
                                  Type(e.getType),
                                  Role(e.getRole),
                                  Other(e.getOther))
    }
  }


  def apply(e: org.openprovenance.prov.model.WasAssociatedWith, gensym: () => org.openprovenance.prov.model.QualifiedName):WasAssociatedWith = {
    new WasAssociatedWith(if (e.getId==null) QualifiedName(gensym()) else QualifiedName(e.getId),
      QualifiedName(e.getActivity),
      QualifiedName(e.getAgent),
      QualifiedName(e.getPlan),
      LangString.fromJavaCollection(e.getLabel),
      Type(e.getType),
      Role(e.getRole),
      Other(e.getOther))

  }
}
class WasAssociatedWith(val id: QualifiedName,
                        val activity: QualifiedName,
                        val agent: QualifiedName,
                        val plan: QualifiedName,
                        val label: Set[LangString],
                        val typex: Set[Type],
                        val role: Set[Role],
                        val other: Map[QualifiedName,Set[Other]]) extends Statement with ImmutableWasAssociatedWith {
     def addAttributes (attr: Set[Attribute]): WasAssociatedWith = {
    		  ProvFactory.pf.newWasAssociatedWith(id,
    				                                  activity, agent, plan,
    				                                  getAttributes ++ attr)
	   }


  def getIndexedAttributes: util.Map[model.QualifiedName, util.Set[model.Attribute]] = {
    convertAttributes(label, typex, None, Set(), role, other)
  }



}

object WasAttributedTo {
  def apply(e: org.openprovenance.prov.model.WasAttributedTo):WasAttributedTo = {
    e match {
      case e:WasAttributedTo => e
      case _ =>
            new WasAttributedTo(QualifiedName(e.getId),
            		                QualifiedName(e.getEntity),
            		                QualifiedName(e.getAgent),
            		                LangString.fromJavaCollection(e.getLabel),
            		                Type(e.getType),
            		                Other(e.getOther))
    }
  }

  def apply(e: org.openprovenance.prov.model.WasAttributedTo, gensym: () => org.openprovenance.prov.model.QualifiedName):WasAttributedTo = {
        new WasAttributedTo(if (e.getId==null) QualifiedName(gensym()) else QualifiedName(e.getId),
          QualifiedName(e.getEntity),
          QualifiedName(e.getAgent),
          LangString.fromJavaCollection(e.getLabel),
          Type(e.getType),
          Other(e.getOther))
  }
}

class WasAttributedTo(val id: QualifiedName,
                        val entity: QualifiedName,
                        val agent: QualifiedName,
                        val label: Set[LangString],
                        val typex: Set[Type],
                        val other: Map[QualifiedName,Set[Other]]) extends Statement with ImmutableWasAttributedTo  {

     def addAttributes (attr: Set[Attribute]): WasAttributedTo = {
    		  ProvFactory.pf.newWasAttributedTo(id,
    				                                entity, agent,
    				                                getAttributes ++ attr)
	   }

     override def rename(m: Map[QualifiedName,QualifiedName]): WasAttributedTo ={
	     new WasAttributedTo(m.getOrElse(id, id),
	                         m(entity),
	                         m(agent),
	                         label,
	                         typex,
	                         other)
	   }


  @unused
  def getIndexedAttributes: util.Map[model.QualifiedName, util.Set[model.Attribute]] = {
    convertAttributes(label, typex, None, Set(), Set(), other)
  }

}

object SpecializationOf {
  def apply(e: org.openprovenance.prov.model.SpecializationOf):SpecializationOf = {
    e match {
      case e:SpecializationOf => e
      case e:org.openprovenance.prov.model.extension.QualifiedSpecializationOf =>
        new SpecializationOf(QualifiedName(e.getId),
          QualifiedName(e.getSpecificEntity),
          QualifiedName(e.getGeneralEntity),
          LangString.fromJavaCollection(e.getLabel),
          Type(e.getType),
          Other(e.getOther))
      case _ =>
            new SpecializationOf(null,
            		                 QualifiedName(e.getSpecificEntity),
            		                 QualifiedName(e.getGeneralEntity),
            		                 Set(),
            		                 Set(),
            		                 Map())
    }
  }

  def apply(e: org.openprovenance.prov.model.SpecializationOf, gensym: () => org.openprovenance.prov.model.QualifiedName):SpecializationOf = {
        new SpecializationOf(QualifiedName(gensym()),
          QualifiedName(e.getSpecificEntity),
          QualifiedName(e.getGeneralEntity),
          Set(),
          Set(),
          Map())
  }
}

class SpecializationOf(val id: QualifiedName,
                        val specificEntity: QualifiedName,
                        val generalEntity: QualifiedName,
                        val label: Set[LangString],
                        val typex: Set[Type],
                        val other: Map[QualifiedName,Set[Other]]) extends Statement with ImmutableSpecializationOf {
     def addAttributes (attr: Set[Attribute]): SpecializationOf = {
    		  ProvFactory.pf.newSpecializationOf(id,
    				                                 specificEntity, generalEntity,
    				                                 getAttributes ++ attr)
	   }

}

object AlternateOf {
  def apply(e: org.openprovenance.prov.model.AlternateOf):AlternateOf = {
    e match {
      case e:AlternateOf => e
      case e:org.openprovenance.prov.model.extension.QualifiedAlternateOf =>
        new AlternateOf(QualifiedName(e.getId),
          QualifiedName(e.getAlternate1),
          QualifiedName(e.getAlternate2),
          LangString.fromJavaCollection(e.getLabel),
          Type(e.getType),
          Other(e.getOther))
      case _ =>
            new AlternateOf(null,
            		                 QualifiedName(e.getAlternate1),
            		                 QualifiedName(e.getAlternate2),
            		                 Set(),
            		                 Set(),
            		                 Map())
    }
  }

  def apply(e: org.openprovenance.prov.model.AlternateOf, gensym: () => org.openprovenance.prov.model.QualifiedName):AlternateOf = {
    new AlternateOf(QualifiedName(gensym()),
                    QualifiedName(e.getAlternate1),
                    QualifiedName(e.getAlternate2),
                    Set(),
                    Set(),
                    Map())
  }
}

class AlternateOf(val id: QualifiedName,
                        val alternate1: QualifiedName,
                        val alternate2: QualifiedName,
                        val label: Set[LangString],
                        val typex: Set[Type],
                        val other: Map[QualifiedName,Set[Other]]) extends Statement with ImmutableAlternateOf {

     def addAttributes (attr: Set[Attribute]): AlternateOf = {
    		  ProvFactory.pf.newAlternateOf(id,
    				                            alternate1, alternate2,
    				                            getAttributes ++ attr)
	   }
}

class MentionOf(val id: QualifiedName,
                        val specificEntity: QualifiedName,
                        val generalEntity: QualifiedName,
                        val bundle: QualifiedName,
                        val label: Set[LangString],
                        val typex: Set[Type],
                        val other: Map[QualifiedName,Set[Other]]) extends Statement with ImmutableMentionOf {

     def addAttributes (attr: Set[Attribute]): MentionOf = {
    		  ProvFactory.pf.newMentionOf(id,
    				                          specificEntity, generalEntity, bundle,
    				                          getAttributes ++ attr)
	   }
}



object WasInformedBy {
  def apply(e: org.openprovenance.prov.model.WasInformedBy):WasInformedBy = {
    e match {
      case e:WasInformedBy => e
      case _ =>
        new WasInformedBy(QualifiedName(e.getId),
                          QualifiedName(e.getInformed),
                          QualifiedName(e.getInformant),
                          LangString.fromJavaCollection(e.getLabel),
                          Type(e.getType),
                          Other(e.getOther))
    }
  }

  def apply(e: org.openprovenance.prov.model.WasInformedBy, gensym: () => org.openprovenance.prov.model.QualifiedName):WasInformedBy = {
    new WasInformedBy(if (e.getId==null) QualifiedName(gensym()) else QualifiedName(e.getId),
                      QualifiedName(e.getInformed),
                      QualifiedName(e.getInformant),
                      LangString.fromJavaCollection(e.getLabel),
                      Type(e.getType),
                      Other(e.getOther))
  }
}


class WasInformedBy(val id: QualifiedName,
                    val informed: QualifiedName,
                    val informant: QualifiedName,
                    val label: Set[LangString],
                    val typex: Set[Type],
                    val other: Map[QualifiedName,Set[Other]]) extends Statement with ImmutableWasInformedBy {
     def addAttributes (attr: Set[Attribute]): WasInformedBy = {
    		  ProvFactory.pf.newWasInformedBy(id,
    				                              informed, informant,
    				                              getAttributes ++ attr)
	   }

  @unused
  def getIndexedAttributes: util.Map[model.QualifiedName, util.Set[model.Attribute]] = {
    convertAttributes(label, typex, None, Set(), Set(), other)
  }

}

object WasInfluencedBy {
  def apply(e: org.openprovenance.prov.model.WasInfluencedBy):WasInfluencedBy = {
    e match {
      case e:WasInfluencedBy => e
      case _ =>
        new WasInfluencedBy(QualifiedName(e.getId),
          QualifiedName(e.getInfluencee),
          QualifiedName(e.getInfluencer),
          LangString.fromJavaCollection(e.getLabel),
          Type(e.getType),
          Other(e.getOther))
    }
  }

  def apply(e: org.openprovenance.prov.model.WasInfluencedBy, gensym: () => org.openprovenance.prov.model.QualifiedName):WasInfluencedBy = {
    new WasInfluencedBy(if (e.getId==null) QualifiedName(gensym()) else QualifiedName(e.getId),
                        QualifiedName(e.getInfluencee),
                        QualifiedName(e.getInfluencer),
                        LangString.fromJavaCollection(e.getLabel),
                        Type(e.getType),
                        Other(e.getOther))
  }
}


class WasInfluencedBy(val id: QualifiedName,
                      val influencee: QualifiedName,
                      val influencer: QualifiedName,
                      val label: Set[LangString],
                      val typex: Set[Type],
                      val other: Map[QualifiedName,Set[Other]]) extends Statement with ImmutableWasInfluencedBy {

     def addAttributes (attr: Set[Attribute]): WasInfluencedBy = {
    		  ProvFactory.pf.newWasInfluencedBy(id,
    				                                influencee, influencer,
    				                                getAttributes ++ attr)
	   }


  @unused
  def getIndexedAttributes: util.Map[model.QualifiedName, util.Set[model.Attribute]] = {
    convertAttributes(label, typex, None, Set(), Set(), other)
  }

}

object HadMember {
  def apply(e: org.openprovenance.prov.model.HadMember):HadMember = {
    e match {
      case e:HadMember => e
      case e: org.openprovenance.prov.model.extension.QualifiedHadMember =>
        new HadMember(QualifiedName(e.getId),
          QualifiedName(e.getCollection),
          e.getEntity.asScala.map(QualifiedName(_)).toSet,
          LangString.fromJavaCollection(e.getLabel),
          Type(e.getType),
          Other(e.getOther))
      case _ =>
            new HadMember(null,
            		                 QualifiedName(e.getCollection),
            		                 e.getEntity.asScala.map(QualifiedName(_)).toSet,
            		                 Set(),
            		                 Set(),
            		                 Map())
    }
  }

  def apply(e: org.openprovenance.prov.model.HadMember, gensym: () => org.openprovenance.prov.model.QualifiedName):HadMember = {
    new HadMember(QualifiedName(gensym()),
                  QualifiedName(e.getCollection),
                  e.getEntity.asScala.map(QualifiedName(_)).toSet,
                  Set(),
                  Set(),
                  Map())
  }
}

class HadMember(val id: QualifiedName,
                val collection: QualifiedName,
                val entity: Set[QualifiedName],
                val label: Set[LangString],
                val typex: Set[Type],
                val other: Map[QualifiedName,Set[Other]]) extends Statement with ImmutableHadMember {
     def addAttributes (attr: Set[Attribute]): HadMember = {
    		  ProvFactory.pf.newHadMember(id,
    				                          collection, entity,
    				                          getAttributes ++ attr)
	   }

}

trait ExtensionStatement {
  def addIds (set: Set[QualifiedName]):Set[QualifiedName]
}

trait HasStatements {
    val statement: Iterable[Statement]
    def getStatement: java.util.List[org.openprovenance.prov.model.Statement] = {
            statement.toSeq.asInstanceOf[Seq[org.openprovenance.prov.model.Statement]].asJava
    }
}

trait HasStatementsOrBundle {
    val statementOrBundle: Iterable[StatementOrBundle]
    def getStatementOrBundle: java.util.List[org.openprovenance.prov.model.StatementOrBundle] = {
            statementOrBundle.toSeq.asInstanceOf[Seq[org.openprovenance.prov.model.StatementOrBundle]].asJava
    }
}

object Bundle {
  def apply(bun: org.openprovenance.prov.model.Bundle): Bundle = {
    bun match {
      case b: Bundle => b
      case b: org.openprovenance.prov.model.Bundle =>
        val ss: Iterable[Statement] =b.getStatement.asScala.map((s: model.Statement) => Statement(s))
        new Bundle(QualifiedName(b.getId),ss,b.getNamespace)
    }
  }
}

class Bundle(val id: QualifiedName,
             val statement: Iterable[Statement],
             @BeanProperty val namespace: Namespace) extends StatementOrBundle with org.openprovenance.prov.model.Bundle with Identifiable with HasStatements with Hashable with HasNamespace {


    @BeanProperty
    val kind=PROV_BUNDLE
    val enumType: Kind = Kind.bun



    def canEqual(a: Any): Boolean = a.isInstanceOf[Bundle]

    override def equals(that: Any): Boolean =
    that match {
      case that: Bundle => that.canEqual(this) &&
                           this.id == that.id  &&
                           this.statement.toSet == that.statement.toSet
      case _ => false
    }

    override lazy val hashCode:Int = {
        pr(h(id),h(statement))
    }

    def setNamespace(x: org.openprovenance.prov.model.Namespace): Unit = {
      throw new UnsupportedOperationException
    }

    def toNotation (sb:StringBuilder): Unit = {
      sb++="bundle "
      sb++=id.toString()
      sb+='\n'
      printNamespace(sb)
      //statement.addString(sb, "    ", "\n    ", "\n")
      val currentNS=new Namespace(Namespace.getThreadNamespace)
      Namespace.withThreadNamespace(namespace)
      addString(sb,statement, "    ", "\n    ", "\n")
      Namespace.withThreadNamespace(currentNS);

      sb++="  endBundle"
    }
    override def toString:String = {
       val sb=new StringBuilder
       toNotation(sb)
       sb.toString()
    }


}

trait HasNamespace {
  val namespace: Namespace
  def printNamespace(sb:StringBuilder): Unit = {
    if (namespace.getDefaultNamespace!=null) {
        sb++="  default <" ++ namespace.getDefaultNamespace ++ ">\n"   //
    }
    namespace.getPrefixes.asScala.map(p => {
                               if (! ((p._1=="xsd" && p._2==org.openprovenance.prov.model.NamespacePrefixMapper.XSD_NS)
                                     || (p._1=="prov" && p._2==org.openprovenance.prov.model.NamespacePrefixMapper.PROV_NS))) {
                               sb++="  prefix " ++ p._1 ++ " <" ++ p._2 ++ ">\n" }})
  }

      // See https://github.com/scala/scala/blob/v2.10.2/src/library/scala/collection/TraversableOnce.scala#L316
    // extended to support a stringbuilder
    def addString[T <: StatementOrBundle](b: StringBuilder, set: IterableOnce[T], start: String, sep: String, end: String): StringBuilder = {
       var first = true

       b append start
       for (x <- set) {
           if (first) {
               x.toNotation(b)
               first = false
           }
           else {
               b append sep
               x.toNotation(b)
           }
       }
       b append end
       b
    }

}

object Document {
  def apply(doc: Document, gensym: () => QualifiedName, prefix: String, uri: String): Document = {
    val ss=doc.statements().map(Statement(_,gensym))
    val bb: Iterable[Bundle] =doc.bundles()
    val ns=new Namespace(doc.namespace)
    ns.register(prefix,uri)
    new Document(ss++bb,ns)
  }
}

class Document(val statementOrBundle: Iterable[StatementOrBundle],
               @BeanProperty val namespace: Namespace) extends org.openprovenance.prov.model.Document with HasStatementsOrBundle with Hashable  with HasNamespace {

	  def this() {
	    this(List(),new Namespace)
	  }

    def statements (): Iterable[Statement] = statementOrBundle.collect{case s:Statement => s}
    def bundles (): Iterable[Bundle] = statementOrBundle.collect{case b:Bundle    => b}

    def canEqual(a: Any): Boolean = a.isInstanceOf[Document]

    override def equals(that: Any): Boolean =
    that match {
      case that: Document => that.canEqual(this) &&
                                     this.statementOrBundle.toSet == that.statementOrBundle.toSet
      case _ => false
    }

    override lazy val hashCode:Int = {
        h(statementOrBundle)
    }

    def setNamespace(x: org.openprovenance.prov.model.Namespace): Unit = {
      throw new UnsupportedOperationException
    }




    def toNotation (sb:StringBuilder): Unit = {
        Namespace.withThreadNamespace(namespace)
        sb++="document\n"
        printNamespace(sb)
        //statementOrBundle.addString(sb, "  ", "\n  ", "\n")
        addString(sb,statementOrBundle, "  ", "\n  ", "\n")
        sb++="endDocument\n"
    }
    override def toString:String = {
       val sb=new StringBuilder
       toNotation(sb)
       sb.toString()
    }

    def toNormalForm (in: org.openprovenance.prov.scala.nf.DocumentProxyInterface) : org.openprovenance.prov.scala.nf.DocumentProxyInterface = {
      var accumulator=in
      statementOrBundle.foreach{ x => accumulator=accumulator.add(x.asInstanceOf[Statement]) }
      accumulator
    }

    def map(f: StatementOrBundle=>StatementOrBundle): Document = {
      new Document(statementOrBundle.map(f),namespace)
    }
    def union(doc2: Document, flatten:Boolean=false, setp: Boolean=false): Document = {
      val ns=new Namespace(namespace)
      doc2.getNamespace.getNamespaces.asScala.foreach{case (namespaceuri:String, prefix:String) => ns.register(prefix, namespaceuri)}
      val statements2=doc2.statementOrBundle
      val statements3=if (flatten) {
    	  statements2.flatMap(sb => sb match {
    	                              case b:Bundle => {  b.namespace.getNamespaces.asScala.foreach{case (namespaceuri:String, prefix:String) => ns.register(prefix, namespaceuri)}
    	                                                  b.statement
    	                                                }
    	                              case s:Statement => Seq(s)
    	                       })
      } else {
        statements2
      }
      val statements=statementOrBundle ++ statements3

      new Document(if (setp) statements.toSet else statements, ns)

    }


}

class OrderedDocument( val orderedStatementOrBundle: Seq[StatementOrBundle],
		                   @BeanProperty override val namespace: Namespace) extends Document(orderedStatementOrBundle.toSet,namespace) {
  def this (doc: Document) {
    this(doc.statementOrBundle.toSeq,doc.namespace)
  }

  //val orderedStatementOrBundle=statementOrBundle.toSeq
  def orderedStatements (): Seq[Statement] = orderedStatementOrBundle.filter(_.isInstanceOf[Statement]).map(_.asInstanceOf[Statement])
  def orderedBundles (): Seq[Bundle] = orderedStatementOrBundle.filter(_.isInstanceOf[Bundle]).map(_.asInstanceOf[Bundle])

  override def map(f: StatementOrBundle=>StatementOrBundle): OrderedDocument = {
      new OrderedDocument(orderedStatementOrBundle.map(f),namespace)
  }
}


class TypedValue protected (val `type`:  QualifiedName,
                            val value: Object) extends org.openprovenance.prov.model.TypedValue {
	def convertValueToObject(x$1: org.openprovenance.prov.model.ValueConverter): Object = ???
  def getConvertedValue: Object = getValue()

  def getType: org.openprovenance.prov.model.QualifiedName = { `type` }
  def getValue: Object = value


  def this(t: QualifiedName,
           v: String) {
        this (t,{val o:Object=v;o})
  }

  def this(t: QualifiedName,
           v: QualifiedName) {
        this (t,{val o:Object=v;o})
  }

  def this(t: QualifiedName,
           v: LangString) {
        this (t,{val o:Object=v;o})
  }

  override def setType(t: org.openprovenance.prov.model.QualifiedName): Unit = { throw new UnsupportedOperationException }
  override def setValue(s: String): Unit = { throw new UnsupportedOperationException }
  override def setValue(qn: org.openprovenance.prov.model.QualifiedName): Unit = { throw new UnsupportedOperationException }
  override def setValue(s: org.openprovenance.prov.model.LangString): Unit = { throw new UnsupportedOperationException }
  override def setValueFromObject(value: Any): Unit = { throw new UnsupportedOperationException }

}

trait ToNotationString {
    def getElementName: org.openprovenance.prov.model.QualifiedName
    def getValue: Object
    def getType: org.openprovenance.prov.model.QualifiedName
    def valueToNotationString(): String = {
      org.openprovenance.prov.model.ProvUtilities.valueToNotationString(getValue, getType)
    }
    def toNotationString: String = {
            val s1=org.openprovenance.prov.model.DOMProcessing.qualifiedNameToString(getElementName)
            s1 ++ " = " ++ valueToNotationString()
    }
    override def toString: String = { toNotationString }

}


abstract sealed class Attribute  protected (override val `type`: QualifiedName,
                                            override val value: Object)
                                            extends TypedValue(`type`,value) with org.openprovenance.prov.model.Attribute {

    val elementName : QualifiedName
    def getElementName:org.openprovenance.prov.model.QualifiedName = elementName

}

object Attribute {
  import ProvFactory.pf


  def ifQualifiedNameOrLangString(value: AnyRef): Object = {
    value match {
      case qn:org.openprovenance.prov.model.QualifiedName => QualifiedName(qn)
      case lg:org.openprovenance.prov.model.LangString => LangString(lg)
      case _ => value
    }
  }

  def apply(attr: org.openprovenance.prov.model.Attribute): Attribute = {
      pf.newAttribute(attr.getElementName,
                      ifQualifiedNameOrLangString(attr.getValue),
                      attr.getType) match {
      case a:Attribute => a
    }
  }

  def apply(attributes: java.util.Collection[org.openprovenance.prov.model.Attribute]): Set[Attribute] = {
    val attributes2 = attributes.asScala.toSet
    attributes2.map(s => Attribute(s))
  }

  def apply2(attributes: Seq[org.openprovenance.prov.model.Attribute]): Set[Attribute] = {
    val attributes2 = attributes.toSet
    attributes2.map(s => Attribute(s))
  }


  def split2(attributes: java.util.Collection[org.openprovenance.prov.model.Attribute]): (Set[Label],Set[Type],Set[Value],Set[Location],Set[Role],Map[QualifiedName,Set[Other]]) = {
    splitrec(Attribute(attributes).toList,
             Set(),
             Set(),
             Set(),
             Set(),
             Set(),
             new HashMap())
  }

  def split3(attributes: Seq[org.openprovenance.prov.model.Attribute]): (Set[Label], Set[Type], Set[Value], Set[Location], Set[Role], Map[QualifiedName, Set[Other]]) = {
    splitrec(Attribute.apply2(attributes).toList,
      Set(),
      Set(),
      Set(),
      Set(),
      Set(),
      new HashMap())
  }
 // def split(attributes: Set[Attribute]): (Set[Label],Set[Type],Set[Value],Set[Location],Set[Role],Map[QualifiedName,Set[Other]])  = {
 //   split(attributes.toList)
 // }

  def split(attributes: Iterable[Attribute]): (Set[Label],Set[Type],Set[Value],Set[Location],Set[Role],Map[QualifiedName,Set[Other]]) = {
    splitrec(attributes.toList,
             Set(),
             Set(),
             Set(),
             Set(),
             Set(),
             new HashMap())
  }


  @scala.annotation.tailrec
  def splitrec(attributes: List[Attribute],
               ls: Set[Label],
               ts: Set[Type],
               vs: Set[Value],
               locs: Set[Location],
               rs: Set[Role],
               os: Map[QualifiedName,Set[Other]])
               : (Set[Label],Set[Type],Set[Value],Set[Location],Set[Role],Map[QualifiedName,Set[Other]]) = {
      attributes match {
         case List() => (ls,ts,vs,locs,rs,os)
         case attr :: rest =>
            attr match {
              case l:Label    => splitrec(rest,ls + l,ts,   vs,   locs,   rs,   os)
              case t:Type     => splitrec(rest,ls,    ts+t, vs,   locs,   rs,   os)
              case l:Location => splitrec(rest,ls,    ts,   vs,   locs+l, rs,   os)
              case v:Value    => splitrec(rest,ls,    ts,   vs+v, locs,   rs,   os)
              case r:Role     => splitrec(rest,ls,    ts,   vs,   locs,   rs+r, os)
              case o:Other    => splitrec(rest,ls,    ts,   vs,   locs,   rs,   Other.extend(os,o))
            }
      }
  }

  def toNotation (sb: StringBuilder, ls: Set[LangString],ts: Set[Type],vs: Set[Value],locs: Set[Location],rs: Set[Role],os: Map[QualifiedName,Set[Other]]):StringBuilder = {
      if (ls.isEmpty && ts.isEmpty & vs.isEmpty && locs.isEmpty && os.isEmpty) sb else {
          val all:List[AnyRef]=List()++ls.map(Label(null,_))++ts++vs++locs++rs++os.values.flatMap(x=>x)

          all.addString(sb, ",[", ",", "]")
      }
  }


}

object Type {
      val myName: QualifiedName =QualifiedName(ProvFactory.pf.getName.PROV_TYPE)
      private[immutable] def apply (`type`: QualifiedName,
                                    value: Object) = {
          new Type(`type`,value)
      }


      private[immutable] def apply (typex: org.openprovenance.prov.model.Type):Type = {
          typex match {
            case v:Type => v
            case _ =>Type(QualifiedName(typex.getType),Attribute.ifQualifiedNameOrLangString(typex.getValue))
          }
      }

      def apply(types: java.util.Collection[org.openprovenance.prov.model.Type]): Set[Type] = {
          val types2=types.asScala.toSet
          types2.map(s => Type(s))
      }
}

class Type protected (override val `type`: QualifiedName,
                      override val value: Object)
           extends Attribute(`type`,value) with org.openprovenance.prov.model.Type  with org.openprovenance.prov.model.Attribute with ToNotationString with Hashable {
    import Type.myName


    val elementName : QualifiedName = myName


    def this(t: QualifiedName,
             v: String) {
        this (t,{val o:Object=v;o})
    }

    def this(t: QualifiedName,
             v: QualifiedName) {
        this (t,{val o:Object=v;o})
    }

    def this(t: QualifiedName,
             v: LangString) {
        this (t,{val o:Object=v;o})
    }

    def getAttributeKind(x$1: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Attribute.AttributeKind = ???

    def getKind: org.openprovenance.prov.model.Attribute.AttributeKind = org.openprovenance.prov.model.Attribute.AttributeKind.PROV_TYPE

    def getQualifiedName(x$1: org.openprovenance.prov.model.Attribute.AttributeKind): org.openprovenance.prov.model.QualifiedName = ???

    def canEqual(a: Any): Boolean = a.isInstanceOf[Type]


    override def equals(that: Any): Boolean =
    that match {
      case that: Type => that.canEqual(this) &&
                           this.elementName == that.elementName &&
                           this.`type` == that.`type` &&
                           this.value == that.value
      case _ => false
    }

    override val hashCode:Int = pr(pr(h(elementName),h(`type`)),h(value))


}

object Role {
      val myName: QualifiedName =QualifiedName(ProvFactory.pf.getName.PROV_ROLE)

      private[immutable] def apply (`type`: QualifiedName,
    		                            value: Object) = {
        new Role(`type`,value)
      }


      private[immutable] def apply (loc: org.openprovenance.prov.model.Role):Role = {
          loc match {
            case v:Role => v
            case _ =>Role(QualifiedName(loc.getType),Attribute.ifQualifiedNameOrLangString(loc.getValue))
          }
      }

      def apply(types: java.util.Collection[org.openprovenance.prov.model.Role]): Set[Role] = {
          val types2=types.asScala.toSet
          types2.map(s => Role(s))
      }


}
class Role protected (override val `type`: QualifiedName,
                      override val value: Object)
                      extends Attribute(`type`,value) with org.openprovenance.prov.model.Role  with org.openprovenance.prov.model.Attribute with ToNotationString with Hashable {
    import Role.myName
    val elementName: QualifiedName = myName



    def this(t: QualifiedName,
             v: String) {
        this (t,{val o:Object=v;o})
    }

    def this(t: QualifiedName,
             v: QualifiedName) {
        this (t,{val o:Object=v;o})
    }

    def this(t: QualifiedName,
             v: LangString) {
        this (t,{val o:Object=v;o})
    }

    def getAttributeKind(x$1: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Attribute.AttributeKind = ???

    def getKind: org.openprovenance.prov.model.Attribute.AttributeKind = org.openprovenance.prov.model.Attribute.AttributeKind.PROV_ROLE

    def getQualifiedName(x$1: org.openprovenance.prov.model.Attribute.AttributeKind): org.openprovenance.prov.model.QualifiedName = ???

    def canEqual(a: Any): Boolean = a.isInstanceOf[Role]

    override def equals(that: Any): Boolean =
    that match {
      case that: Role => that.canEqual(this) &&
                           this.elementName == that.elementName &&
                           this.`type` == that.`type` &&
                           this.value == that.value
      case _ => false
    }

    override val hashCode:Int = pr(pr(h(elementName),h(`type`)),h(value))

}
object Location {
      val myName: QualifiedName =QualifiedName(ProvFactory.pf.getName.PROV_LOCATION)
      private[immutable] def apply (`type`: QualifiedName,
                                    value: Object) = {
        new Location(`type`,value)
      }

      private[immutable] def apply (loc: org.openprovenance.prov.model.Location):Location = {
          loc match {
            case v:Location => v
            case _ =>Location(QualifiedName(loc.getType),Attribute.ifQualifiedNameOrLangString(loc.getValue))
          }
      }


      def apply(locs: java.util.Collection[org.openprovenance.prov.model.Location]): Set[Location] = {
          val locs2=locs.asScala.toSet
          locs2.map(s => Location(s))
      }


}

class Location protected (override val `type`: QualifiedName,
                          override val value: Object)
                          extends Attribute(`type`,value) with org.openprovenance.prov.model.Location  with org.openprovenance.prov.model.Attribute with ToNotationString with Hashable {
    import Location.myName

    val elementName: QualifiedName = myName


    def this(t: QualifiedName,
             v: String) {
        this (t, {val o:Object=v; o})
    }

    def this(t: QualifiedName,
             v: QualifiedName) {
        this (t, {val o:Object=v;o})
    }

    def this(t: QualifiedName,
             v: LangString) {
        this (t, {val o:Object=v;o})
    }


    def getAttributeKind(x$1: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Attribute.AttributeKind = ???

    def getKind: org.openprovenance.prov.model.Attribute.AttributeKind = org.openprovenance.prov.model.Attribute.AttributeKind.PROV_LOCATION

    def getQualifiedName(x$1: org.openprovenance.prov.model.Attribute.AttributeKind): org.openprovenance.prov.model.QualifiedName = ???

    def canEqual(a: Any): Boolean = a.isInstanceOf[Location]

    override def equals(that: Any): Boolean =
    that match {
      case that: Location => that.canEqual(this) &&
                           this.elementName == that.elementName &&
                           this.`type` == that.`type` &&
                           this.value == that.value
      case _ => false
    }
    override lazy val hashCode:Int = {
      pr(pr(h(elementName),h(`type`)),h(value))
    }
}

object Value {
      val myName: QualifiedName =QualifiedName(ProvFactory.pf.getName.PROV_VALUE)

      private[immutable] def apply (`type`: QualifiedName,
    		                            value: Object): Value = {
        new Value(`type`,value)
      }

      def apply (value: org.openprovenance.prov.model.Value):Value = {  // To CHECK: used to be private[immutable]. Was changed to public for NormalForm.
          value match {
            case v:Value => v
            case _ =>Value(QualifiedName(value.getType),Attribute.ifQualifiedNameOrLangString(value.getValue))
          }
      }

}

class Value protected (override val `type`:QualifiedName,
                       override val value: Object)
                       extends Attribute(`type`,value) with org.openprovenance.prov.model.Value  with org.openprovenance.prov.model.Attribute with ToNotationString with Hashable {
    import Value.myName

    val elementName: QualifiedName = myName


    def this(t: QualifiedName,
             v: String) {
        this (t, {val o:Object=v; o})
    }

    def this(t: QualifiedName,
             v: QualifiedName) {
        this (t, {val o:Object=v; o})
    }

    def this(t: QualifiedName,
             v: LangString) {
        this (t, {val o:Object=v; o})
    }

    def getAttributeKind(x$1: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Attribute.AttributeKind = ???

    def getKind: org.openprovenance.prov.model.Attribute.AttributeKind = org.openprovenance.prov.model.Attribute.AttributeKind.PROV_VALUE

    def getQualifiedName(x$1: org.openprovenance.prov.model.Attribute.AttributeKind): org.openprovenance.prov.model.QualifiedName = ???

    def canEqual(a: Any): Boolean = a.isInstanceOf[Value]

    override def equals(that: Any): Boolean =
    that match {
      case that: Value => that.canEqual(this) &&
                           this.elementName == that.elementName &&
                           this.`type` == that.`type` &&
                           this.value == that.value
      case _ => false
    }
    override val hashCode:Int = pr(pr(h(elementName),h(`type`)),h(value))

}

object LangString {

      private[immutable] def apply (str: org.openprovenance.prov.model.LangString):LangString = {
          str match {
            case v:LangString => v
            case _ =>
              val lang = str.getLang
              new LangString(str.getValue, Option(lang))
          }
      }


      def fromJavaCollection(labels: java.util.Collection[org.openprovenance.prov.model.LangString]): Set[LangString] = {
          val labels2: Iterable[model.LangString] =labels.asScala
          labels2.map(LangString(_)).toSet
      }
  
  
      def apply(labels: Set[org.openprovenance.prov.model.LangString]): Set[LangString] = {
          labels.map(LangString(_))
      }  
}

class LangString (@BeanProperty val value: String,
                  val lang: Option[String]) extends org.openprovenance.prov.model.LangString with Hashable{

  override def getLang: String = {
    lang match {
      case Some(l) => l
      case None => null
    }
  }

  def this(value: String,lang: String) = {
    this(value, Option(lang))
  }

  def this(lg: org.openprovenance.prov.model.LangString) = {
    this(lg.getValue, lg.getLang)
  }

  override def setValue(x: String): Unit = {
    throw new UnsupportedOperationException
  }

  override def setLang(x: String): Unit = {
    throw new UnsupportedOperationException
  }

  def canEqual(a: Any): Boolean = a.isInstanceOf[LangString]

  override def equals(that: Any): Boolean =
    that match {
      case that: LangString => that.canEqual(this) &&
        this.lang == that.lang &&
        this.value == that.value
      case _ => false
    }


    override val hashCode:Int = pr(h(lang),h(value))
    
    override def toString: String = {
      lang match { case Some(l) => '"' + value + '"' + "@" + l; case None => '"' + value + '"' } 
    }
    
    def toLabel: Label = {
      new Label(null,this)
    }
    
     def rename(m: Map[String,String]): LangString = {
       m.get(value) match {
         case None => this
         case Some(v) => new LangString(v,lang)
       }
     }
  
}


object Label {
      val myName: QualifiedName =QualifiedName(ProvFactory.pf.getName.PROV_LABEL)

      private[immutable] def apply (`type`: QualifiedName,
    		                            value: Object) = {
        new Label(`type`,value)
      }

      private[immutable] def apply (label: org.openprovenance.prov.model.Label):Label = {
          label match {
            case v:Label => v
            case _ =>Label(QualifiedName(label.getType),label.getValue)
          }
      }
      
  
      def apply(labels: java.util.Collection[org.openprovenance.prov.model.Label]): Set[Label] = {
          val labels2=labels.asScala.toSet
          labels2.map(s => Label(s))
      }
  
  

}

class Label protected (override val `type`: QualifiedName,
                       override val value: Object) 
                       extends Attribute(`type`,value) with org.openprovenance.prov.model.Label  with org.openprovenance.prov.model.Attribute with ToNotationString with Hashable {
    import Label.myName

    val elementName: QualifiedName = myName
    
       
    
    def this(t: QualifiedName,
             v: LangString) {
        this (t,{val o:Object=v;o})
    }
    
    def getLangString: model.LangString = {
      value match {
        case l:LangString => l
        case l:String => ProvFactory.pf.newInternationalizedString(l)
      }
    }
    
    def getAttributeKind(x$1: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Attribute.AttributeKind = ???
  
    def getKind: org.openprovenance.prov.model.Attribute.AttributeKind = org.openprovenance.prov.model.Attribute.AttributeKind.PROV_LABEL
	
    def getQualifiedName(x$1: org.openprovenance.prov.model.Attribute.AttributeKind): org.openprovenance.prov.model.QualifiedName = ???
    
    def canEqual(a: Any): Boolean = a.isInstanceOf[Label]

    override def equals(that: Any): Boolean =
    that match {
      case that: Label => that.canEqual(this) && 
                           this.elementName == that.elementName && 
                           this.`type` == that.`type` && 
                           this.value == that.value                  
      case _ => false
    }
    override val hashCode:Int = pr(pr(h(elementName),h(`type`)),h(value))
        
}

object Other {
       private[immutable] def apply (elementName : org.openprovenance.prov.model.QualifiedName,
                                     `type`: QualifiedName,
                                     value: Object) = {
        new Other(QualifiedName(elementName),`type`,value)
       }


       private[immutable] def apply (other: org.openprovenance.prov.model.Other):Other = {
          other match {
            case v:Other => v
            case _ =>Other(QualifiedName(other.getElementName),QualifiedName(other.getType),other.getValue)
          }
      }
      
  
   
      def apply(others: java.util.Collection[org.openprovenance.prov.model.Other]): Map[QualifiedName,Set[Other]] = {
          val others2=others.asScala.toSet
          val group=    others2.map(s => {
                       val o=Other(s);
                       o.elementName -> o
                      }).groupBy(_._1)
          val group2=group.view.mapValues(_.map(_._2)).toMap
          group2
      }
      
      def extend(others: Map[QualifiedName,Set[Other]], o:Other):Map[QualifiedName,Set[Other]] = {
        val name=o.elementName
        others + (others.get(name) match {
                                     case Some(set) => name -> (set + o)
                                     case None => name -> Set(o)  
                                   })
      }
  

 }

class Other protected (val elementName : QualifiedName,
                       override val `type`: QualifiedName,
                       override val value: Object)
                       extends Attribute(`type`,value) with org.openprovenance.prov.model.Other  with org.openprovenance.prov.model.Attribute with ToNotationString with Hashable {
  
  
    def this(el: QualifiedName,
             t:  QualifiedName,
             v:  String) {
        this (el,t,{val o:Object=v;o})
    }
    
    def this(el: QualifiedName,
             t:  QualifiedName,
             v:  QualifiedName) {
        this (el,t,{val o:Object=v;o})
    }
    
    def this(el: QualifiedName,
             t:  QualifiedName,
             v:  LangString) {
        this (el,t,{val o:Object=v;o})
    }
     
    def setElementName (x$1: org.openprovenance.prov.model.QualifiedName): Unit = { throw new UnsupportedOperationException }
   
    def getAttributeKind(x$1: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Attribute.AttributeKind = ???
  
    def getKind: org.openprovenance.prov.model.Attribute.AttributeKind = org.openprovenance.prov.model.Attribute.AttributeKind.OTHER
	
    def getQualifiedName(x$1: org.openprovenance.prov.model.Attribute.AttributeKind): org.openprovenance.prov.model.QualifiedName = ???
 
    def canEqual(a: Any): Boolean = a.isInstanceOf[Other]

  
    override def equals(that: Any): Boolean =
    that match {
      case that: Other => that.canEqual(this) && 
                           this.elementName == that.elementName && 
                           this.`type` == that.`type` && 
                           this.value == that.value                  
      case _ => false
    }

    override lazy val hashCode:Int = {
      pr(pr(h(elementName),h(`type`)),h(value))
    }      
    
}

object QualifiedName {
    val qnU=new QualifiedNameUtils;
    
    def apply(name: org.openprovenance.prov.model.QualifiedName): QualifiedName = {
        if (name==null) return null
    		name match {
          case qn:QualifiedName => qn
          case _ => new QualifiedName(name.getPrefix,name.getLocalPart,name.getNamespaceURI)
    		}  
    }
  
}


class QualifiedName (val prefix: String,
                     val localPart: String,
                     val namespaceURI: String) extends org.openprovenance.prov.model.QualifiedName with Ordered[QualifiedName] {    
    import QualifiedName.qnU
		     
    private val uri: String=namespaceURI+localPart

    override val hashCode: Int = this.namespaceURI.hashCode ^ this.localPart.hashCode()

    def getUri: String = uri

    def getLocalPart: String = localPart
    
    def getNamespaceURI: String = namespaceURI
    
    def getPrefix: String = prefix

    def setLocalPart(x$1: String): Unit = { throw new UnsupportedOperationException }
    
    def setNamespaceURI(x$1: String): Unit =  { throw new UnsupportedOperationException }
    
    def setPrefix(x$1: String): Unit = { throw new UnsupportedOperationException }
    
    override def setUri(discard: String): Unit = { throw new UnsupportedOperationException  }

    def getUnescapedLocalPart: String = { qnU.unescapeProvLocalName(localPart)}
   
    override def toString: String ={
      if (prefix==null) localPart else prefix + ":" + localPart
    }
    
    override def toQName: javax.xml.namespace.QName= {
            val escapedLocal=qnU.escapeToXsdLocalName(getUnescapedLocalPart)
            if (qnU.is_NC_Name(escapedLocal)) {
                if (prefix==null) {
                    new javax.xml.namespace.QName(namespaceURI,escapedLocal)
                } else {
                    new javax.xml.namespace.QName(namespaceURI,escapedLocal,prefix)
                }
            } else {
                throw new QualifiedNameException("PROV-SCALA QName: localPart not valid " + localPart)
            }
    }
    
    def canEqual(a: Any): Boolean = a.isInstanceOf[QualifiedName]

    override def equals (other: Any) : Boolean = other match { 
        case other: QualifiedName => other.canEqual(this) && 
                                     this.hashCode == other.hashCode   // cheap 
                                     this.uri == other.uri             // more expensive
        case _ => false
    }
    
    def compare(that: QualifiedName): Int = (this.getUri()) compare (that.getUri())


}


class ObjectFactory extends org.openprovenance.prov.model.ObjectFactory {
  override def createEntity () = throw new UnsupportedOperationException
  override def createActivity () = throw new UnsupportedOperationException
  override def createAgent () = throw new UnsupportedOperationException
  override def createDocument () = throw new UnsupportedOperationException
  override def createNamedBundle () = throw new UnsupportedOperationException
  override def createWasGeneratedBy () = throw new UnsupportedOperationException
  override def createWasInvalidatedBy () = throw new UnsupportedOperationException
  override def createUsed() = throw new UnsupportedOperationException
  override def createWasAssociatedWith() = throw new UnsupportedOperationException
  override def createWasAttributedTo() = throw new UnsupportedOperationException
  override def createWasDerivedFrom() = throw new UnsupportedOperationException
  override def createWasEndedBy() = throw new UnsupportedOperationException
  override def createWasInfluencedBy() = throw new UnsupportedOperationException
  override def createWasInformedBy() = throw new UnsupportedOperationException
  override def createWasStartedBy() =throw new UnsupportedOperationException
  override def createSpecializationOf() = throw new UnsupportedOperationException
  override def createActedOnBehalfOf() = throw new UnsupportedOperationException
  override def createHadMember() =    throw new UnsupportedOperationException
  override def createAlternateOf() = throw new UnsupportedOperationException

  override def createQualifiedSpecializationOf() = throw new UnsupportedOperationException
  override def createQualifiedHadMember() =    throw new UnsupportedOperationException
  override def createQualifiedAlternateOf() = throw new UnsupportedOperationException

  override def createLocation() = throw new UnsupportedOperationException
  override def createOther() = throw new UnsupportedOperationException
  override def createRole() = throw new UnsupportedOperationException
  override def createType() = throw new UnsupportedOperationException
  
  override def createTypedValue() = throw new UnsupportedOperationException
  override def createValue() = throw new UnsupportedOperationException
  
  
  override def createDerivedByInsertionFrom() = throw new UnsupportedOperationException
  override def createDerivedByRemovalFrom() = throw new UnsupportedOperationException
  override def createDictionaryMembership() = throw new UnsupportedOperationException
  override def createEntry() = throw new UnsupportedOperationException
  override def createKey() = throw new UnsupportedOperationException
  override def createMentionOf() = throw new UnsupportedOperationException
  override def createInternationalizedString() = throw new UnsupportedOperationException


}

class FooException extends Exception

object ProvFactory {
  val pf: ProvFactory =new ProvFactory
  val getFactory: ProvFactory =pf
}

trait ImmutableConstructorDelegator  extends ImmutableConstructorInterface {
  def delegate: ImmutableConstructorInterface

  override def newEntity(id: model.QualifiedName, attributes: Set[Attribute]): Entity = {
    delegate.newEntity(id,attributes)
  }

  override def newAgent(id: QualifiedName, attributes: Set[Attribute]): Agent = {
    delegate.newAgent(id,attributes)
  }

  override def newActivity(id: QualifiedName, startTime: XMLGregorianCalendar, endTime: XMLGregorianCalendar, attributes: Set[Attribute]): Activity = {
    delegate.newActivity(id,startTime,endTime,attributes)
  }

  override def newActivity(id: QualifiedName, startTime: Option[XMLGregorianCalendar], endTime: Option[XMLGregorianCalendar], attributes: Set[Attribute]): Activity = {
    delegate.newActivity(id,startTime,endTime,attributes)
  }

  override def newWasDerivedFrom(id: QualifiedName,
                                 generatedEntity: QualifiedName,
                                 usedEntity: QualifiedName,
                                 activity: QualifiedName,
                                 generation: QualifiedName, usage: QualifiedName, attributes: Set[Attribute]): WasDerivedFrom = {
    delegate.newWasDerivedFrom(id,generatedEntity,usedEntity,activity,generation,usage,attributes)
  }

  override def newWasGeneratedBy(id: QualifiedName, entity: QualifiedName, activity: QualifiedName, time: Option[XMLGregorianCalendar], attributes: Set[Attribute]): WasGeneratedBy = {
    delegate.newWasGeneratedBy(id,entity,activity,time,attributes)
  }

  override def newUsed(id: QualifiedName, activity: QualifiedName, entity: QualifiedName, time: Option[XMLGregorianCalendar], attributes: Set[Attribute]): Used = {
    delegate.newUsed(id,activity,entity, time,attributes)
  }

  override def newWasInvalidatedBy(id: QualifiedName, entity: QualifiedName, activity: QualifiedName, time: Option[XMLGregorianCalendar], attributes: Set[Attribute]): WasInvalidatedBy = {
    delegate.newWasInvalidatedBy(id, entity, activity, time, attributes)
  }

  override def newWasStartedBy(id: QualifiedName, activity: QualifiedName, trigger: QualifiedName, starter: QualifiedName, time: Option[XMLGregorianCalendar], attributes: Set[Attribute]): WasStartedBy = {
    delegate.newWasStartedBy(id, activity, trigger, starter, time, attributes)
  }

  override def newWasEndedBy(id: QualifiedName, activity: QualifiedName, trigger: QualifiedName, ender: QualifiedName, time: Option[XMLGregorianCalendar], attributes: Set[Attribute]): WasEndedBy = {
    delegate.newWasEndedBy(id, activity, trigger, ender, time, attributes)
  }

  override def newWasAssociatedWith(id: QualifiedName, activity: QualifiedName, agent: QualifiedName, plan: QualifiedName, attributes: Set[Attribute]): WasAssociatedWith = {
    delegate.newWasAssociatedWith(id, activity, agent, plan, attributes)
  }

  override def newActedOnBehalfOf(id: QualifiedName, del: QualifiedName, responsible: QualifiedName, activity: QualifiedName, attributes: Set[Attribute]): ActedOnBehalfOf = {
    delegate.newActedOnBehalfOf(id,del, responsible, activity, attributes)
  }

  override def newWasAttributedTo(id: QualifiedName, entity: QualifiedName, agent: QualifiedName, attributes: Set[Attribute]): WasAttributedTo = {
    delegate.newWasAttributedTo(id, entity, agent, attributes)
  }

  override def newSpecializationOf(id: QualifiedName, entity2: QualifiedName, entity1: QualifiedName, attributes: Set[Attribute]): SpecializationOf = {
    delegate.newSpecializationOf(id, entity2, entity1, attributes)
  }

  override def newMentionOf(specializedEntity: QualifiedName, generalEntity: QualifiedName, bundle: QualifiedName): MentionOf = ???

  override def newMentionOf(specializedEntity: QualifiedName, generalEntity: QualifiedName, bundle: QualifiedName, attributes: Set[Attribute]): MentionOf = ???

  override def newMentionOf(id: QualifiedName, specializedEntity: QualifiedName, generalEntity: QualifiedName, bundle: QualifiedName, attributes: Set[Attribute]): MentionOf = ???

  override def newAlternateOf(id: QualifiedName, entity2: QualifiedName, entity1: QualifiedName, attributes: Set[Attribute]): AlternateOf = {
    delegate.newAlternateOf(id, entity2, entity1, attributes)
  }

  override def newWasInformedBy(id: QualifiedName, informed: QualifiedName, informant: QualifiedName, attributes: Set[Attribute]): WasInformedBy = {
    delegate.newWasInformedBy(id, informed, informant, attributes)
  }

  override def newWasInfluencedBy(id: QualifiedName, influencee: QualifiedName, influencer: QualifiedName, attributes: Set[Attribute]): WasInfluencedBy = {
    delegate.newWasInfluencedBy(id, influencee, influencer, attributes)
  }

  override def newHadMember(collection: QualifiedName, entity: Set[QualifiedName]): HadMember = {
    delegate.newHadMember(collection,entity)
  }

  override def newHadMember(id: QualifiedName, collection: QualifiedName, entity: Set[QualifiedName], attributes: Set[Attribute]): HadMember = {
    delegate.newHadMember(id, collection, entity, attributes)
  }
}
trait ImmutableConstructor extends ImmutableConstructorInterface {

  override def newEntity(id: org.openprovenance.prov.model.QualifiedName,
                         attributes: Set[Attribute]): Entity= {
    split(attributes) match {
      case (ls,ts,vs,locs,rs,os) =>
        val size=vs.size
        if (size>1) throw new MultipleValuedEntityException
        val langStrings=LangString(ls.map(l=>l.getLangString))
        vs.headOption match {
          case None => new Entity(QualifiedName(id),langStrings,ts,None,locs,os)
          case Some(v) => new Entity(QualifiedName(id),langStrings,ts,Some(v),locs,os)
        }

    }
  }

  override def newAgent(id: QualifiedName,
                        attributes: Set[Attribute]): Agent = {
    split(attributes) match {
      case (ls,ts,vs,locs,rs,os) =>
        val size=vs.size
        if (size>1) throw new MultipleValuedEntityException
        val langStrings=LangString(ls.map(l=>l.getLangString))
        vs.headOption match {
          case None => new Agent(id,langStrings,ts,None,locs,os)
          case Some(v) => new Agent(id,langStrings,ts,Some(v),locs,os)
        }

    }
  }

  override def newActivity (id: QualifiedName,
                            startTime: XMLGregorianCalendar,
                            endTime: XMLGregorianCalendar,
                            attributes: Set[Attribute]): Activity = {
    newActivity(id,
      Option(startTime),
      Option(endTime),
      attributes)
  }

  override def newActivity (id: QualifiedName,
                            startTime: Option[XMLGregorianCalendar],
                            endTime: Option[XMLGregorianCalendar],
                            attributes: Set[Attribute]): Activity = {
    split(attributes) match {
      case (ls,ts,vs,locs,rs,os) =>
        val size=vs.size
        if (size>0) throw new ValueExistException
        val langStrings=LangString(ls.map(l=>l.getLangString))
        new Activity(id,
          startTime,
          endTime,
          langStrings,ts,locs,os)

    }

  }

  override def newWasDerivedFrom(id: QualifiedName,
                                 generatedEntity: QualifiedName,
                                 usedEntity: QualifiedName,
                                 activity: QualifiedName,
                                 generation: QualifiedName,
                                 usage: QualifiedName,
                                 attributes: Set[Attribute]): WasDerivedFrom= {
    split(attributes) match {
      case (ls,ts,vs,locs,rs,os) =>
        val size=vs.size
        if (size>0) throw new ValueExistException
        val langStrings=LangString(ls.map(l=>l.getLangString))
        new WasDerivedFrom(id,
          generatedEntity,
          usedEntity,
          activity,
          generation,
          usage,
          langStrings,
          ts,
          os)

    }
  }

  override def newWasGeneratedBy(id: QualifiedName,
                                 entity: QualifiedName,
                                 activity: QualifiedName,
                                 time: Option[XMLGregorianCalendar],
                                 attributes: Set[Attribute]): WasGeneratedBy= {
    split(attributes) match {
      case (ls,ts,vs,locs,rs,os) =>
        val size=vs.size
        if (size>0) throw new ValueExistException
        val langStrings=LangString(ls.map(l=>l.getLangString))
        new WasGeneratedBy( id,
                            entity,
                            activity,
                            time,
                            langStrings,
                            ts,
                            locs,
                            rs,
                            os)

    }
  }

  override def newUsed(id: QualifiedName,
                       activity: QualifiedName,
                       entity: QualifiedName,
                       time: Option[XMLGregorianCalendar],
                       attributes: Set[Attribute]): Used= {
    split(attributes) match {
      case (ls,ts,vs,locs,rs,os) =>
        val size=vs.size
        if (size>0) throw new ValueExistException
        val langStrings=LangString(ls.map(l=>l.getLangString))
        new Used(id,
          activity,
          entity,
          time,
          langStrings,
          ts,
          locs,
          rs,
          os)

    }
  }

  override def newWasInvalidatedBy(id: QualifiedName,
                                   entity: QualifiedName,
                                   activity: QualifiedName,
                                   time: Option[XMLGregorianCalendar],
                                   attributes: Set[Attribute]): WasInvalidatedBy= {
    split(attributes) match {
      case (ls,ts,vs,locs,rs,os) =>
        val size=vs.size
        if (size>0) throw new ValueExistException
        val langStrings=LangString(ls.map(l=>l.getLangString))
        new WasInvalidatedBy(id,
          entity,
          activity,
          time,
          langStrings,
          ts,
          locs,
          rs,
          os)

    }
  }


  override def newWasStartedBy(id: QualifiedName,
                               activity: QualifiedName,
                               trigger: QualifiedName,
                               starter: QualifiedName,
                               time: Option[XMLGregorianCalendar],
                               attributes: Set[Attribute]): WasStartedBy = {
    split(attributes) match {
      case (ls,ts,vs,locs,rs,os) =>
        val size=vs.size
        if (size>0) throw new ValueExistException
        val langStrings=LangString(ls.map(l=>l.getLangString))
        new WasStartedBy( id,
                          activity,
                          trigger,
                          starter,
                          time,
                          langStrings,
                          ts,
                          locs,
                          rs,
                          os)

    }
  }

  override def newWasEndedBy(id: QualifiedName,
                             activity: QualifiedName,
                             trigger: QualifiedName,
                             ender: QualifiedName,
                             time: Option[XMLGregorianCalendar],
                             attributes: Set[Attribute]): WasEndedBy = {
    split(attributes) match {
      case (ls,ts,vs,locs,rs,os) =>
        val size=vs.size
        if (size>0) throw new ValueExistException
        val langStrings=LangString(ls.map(l=>l.getLangString))
        new WasEndedBy(id,
          activity,
          trigger,
          ender,
          time,
          langStrings,
          ts,
          locs,
          rs,
          os)

    }
  }

  override def newWasAssociatedWith (id: QualifiedName,
                                     activity: QualifiedName,
                                     agent: QualifiedName,
                                     plan: QualifiedName,
                                     attributes: Set[Attribute]): WasAssociatedWith= {
    split(attributes) match {
      case (ls,ts,vs,locs,rs,os) =>
        val size=vs.size
        if (size>0) throw new ValueExistException
        val langStrings: Set[LangString]=LangString(ls.map(l=>l.getLangString))
        new WasAssociatedWith(id,
                              activity,
                              agent,
                              plan,
                              langStrings,
                              ts,
                              rs,
                              os)

    }
  }

  override def newActedOnBehalfOf (id: QualifiedName,
                                   delegate: QualifiedName,
                                   responsible: QualifiedName,
                                   activity: QualifiedName,
                                   attributes: Set[Attribute]): ActedOnBehalfOf = {
    split(attributes) match {
      case (ls,ts,vs,locs,rs,os) =>
        val size=vs.size
        if (size>0) throw new ValueExistException
        val langStrings=LangString(ls.map(l=>l.getLangString))
        new ActedOnBehalfOf(id,
          delegate,
          responsible,
          activity,
          langStrings,
          ts,
          os)
    }
  }

  override def newWasAttributedTo (id: QualifiedName,
                                   entity: QualifiedName,
                                   agent: QualifiedName,
                                   attributes: Set[Attribute]): WasAttributedTo = {
    split(attributes) match {
      case (ls,ts,vs,locs,rs,os) =>
        val size=vs.size
        if (size>0) throw new ValueExistException
        if (rs.nonEmpty) throw new RoleExistException
        val langStrings=LangString(ls.map(l=>l.getLangString))
        new WasAttributedTo(id,
          entity,
          agent,
          langStrings,
          ts,
          os)

    }
  }





  override def newSpecializationOf (id: QualifiedName,
                                    entity2: QualifiedName,
                                    entity1: QualifiedName,
                                    attributes: Set[Attribute]): SpecializationOf = {
    split(attributes) match {
      case (ls,ts,vs,locs,rs,os) =>
        val size=vs.size
        if (size>0) throw new ValueExistException
        if (rs.nonEmpty) throw new RoleExistException
        val langStrings=LangString(ls.map(l=>l.getLangString))
        new SpecializationOf(id,
          entity2,
          entity1,
          langStrings,
          ts,
          os)

    }
  }

  override def newMentionOf (specializedEntity: QualifiedName,
                             generalEntity: QualifiedName,
                             bundle: QualifiedName): MentionOf= {
    new MentionOf(null,
      specializedEntity,
      generalEntity,
      bundle,
      Set(),
      Set(),
      Map())

  }

  override def newMentionOf (specializedEntity: QualifiedName,
                             generalEntity: QualifiedName,
                             bundle: QualifiedName,
                             attributes: Set[Attribute]): MentionOf= {
    split(attributes) match {
      case (ls,ts,vs,locs,rs,os) =>
        val size=vs.size
        if (size>0) throw new ValueExistException
        if (rs.nonEmpty) throw new RoleExistException
        val langStrings=LangString(ls.map(l=>l.getLangString))
        new MentionOf(null,
          specializedEntity,
          generalEntity,
          bundle,
          langStrings,
          ts,
          os)
    }

  }

  override def newMentionOf (id: QualifiedName,
                             specializedEntity: QualifiedName,
                             generalEntity: QualifiedName,
                             bundle: QualifiedName,
                             attributes: Set[Attribute]): MentionOf= {
    split(attributes) match {
      case (ls,ts,vs,locs,rs,os) =>
        val size=vs.size
        if (size>0) throw new ValueExistException
        if (rs.nonEmpty) throw new RoleExistException
        val langStrings=LangString(ls.map(l=>l.getLangString))
        new MentionOf(id,
          specializedEntity,
          generalEntity,
          bundle,
          langStrings,
          ts,
          os)
    }

  }



  override def newAlternateOf (id: QualifiedName,
                               entity2: QualifiedName,
                               entity1: QualifiedName,
                               attributes: Set[Attribute]): AlternateOf = {
    split(attributes) match {
      case (ls,ts,vs,locs,rs,os) =>
        val size=vs.size
        if (size>0) throw new ValueExistException
        if (rs.nonEmpty) throw new RoleExistException
        val langStrings=LangString(ls.map(l=>l.getLangString))
        new AlternateOf(id,
          entity2,
          entity1,
          langStrings,
          ts,
          os)

    }
  }

  override def newWasInformedBy (id: QualifiedName,
                                 informed: QualifiedName,
                                 informant: QualifiedName,
                                 attributes: Set[Attribute]): WasInformedBy= {
    split(attributes) match {
      case (ls,ts,vs,locs,rs,os) =>
        val size=vs.size
        if (size>0) throw new ValueExistException
        if (rs.nonEmpty) throw new RoleExistException
        if (locs.nonEmpty) throw new LocationExistException
        val langStrings=LangString(ls.map(l=>l.getLangString))
        new WasInformedBy(id,
          informed,
          informant,
          langStrings,
          ts,
          os)

    }
  }

  override def newWasInfluencedBy (id: QualifiedName,
                                   influencee: QualifiedName,
                                   influencer: QualifiedName,
                                   attributes: Set[Attribute]): WasInfluencedBy = {
    split(attributes) match {
      case (ls, ts, vs, locs, rs, os) =>
        val size = vs.size
        if (size > 0) throw new ValueExistException
        if (rs.nonEmpty) throw new RoleExistException
        if (locs.nonEmpty) throw new LocationExistException
        val langStrings = LangString(ls.map(l => l.getLangString))
        new WasInfluencedBy(id,
          influencee,
          influencer,
          langStrings,
          ts,
          os)

    }
  }

  override def newHadMember (collection: QualifiedName,
                             entity: Set[QualifiedName]): HadMember = {
    new HadMember(null,
      QualifiedName(collection),
      entity,
      Set(),
      Set(),
      HashMap())

  }

  override def newHadMember (id: QualifiedName,
                             collection: QualifiedName,
                             entity: Set[QualifiedName],
                             attributes: Set[Attribute]): HadMember = {
    split(attributes) match {
      case (ls,ts,vs,locs,rs,os) =>
        val size=vs.size
        if (size>0) throw new ValueExistException
        if (rs.size>0) throw new RoleExistException
        val langStrings=LangString(ls.map(l=>l.getLangString))
        new HadMember(id,
          collection,
          entity,
          langStrings,
          ts,
          os)

    }
  }

}



class ProvConstructor ( adelegate: ImmutableConstructorInterface) extends org.openprovenance.prov.model.AtomConstructor with org.openprovenance.prov.model.ModelConstructor with ImmutableConstructorDelegator  {

  override def delegate = adelegate

  override def newQualifiedName(x1: String, x2:String, x3: String, x4:org.openprovenance.prov.model.ProvUtilities.BuildFlag) = throw new UnsupportedOperationException



  override def newInternationalizedString(s: String): org.openprovenance.prov.model.LangString = {
    new LangString(s,None)
  }

  override def newInternationalizedString(s: String, lang: String): org.openprovenance.prov.model.LangString = {
    new LangString(s,Option(lang))
  }





  override def newQualifiedName(namespace: String, local: String, prefix: String): org.openprovenance.prov.model.QualifiedName = {
    new QualifiedName(prefix,local,namespace)
  }

  override def newEntity(id: org.openprovenance.prov.model.QualifiedName,
                         attributes: java.util.Collection[org.openprovenance.prov.model.Attribute]): org.openprovenance.prov.model.Entity= {
    newEntity(id,Attribute(attributes))
  }

  override def newAgent(id: org.openprovenance.prov.model.QualifiedName,
                        attributes: java.util.Collection[org.openprovenance.prov.model.Attribute]): org.openprovenance.prov.model.Agent = {

    newAgent(QualifiedName(id),Attribute(attributes))
  }

  override def newDocument(namespace: Namespace,
                           statements: java.util.Collection[org.openprovenance.prov.model.Statement],
                           bundles: java.util.Collection[org.openprovenance.prov.model.Bundle]): org.openprovenance.prov.model.Document = {
    new Document(StatementOrBundle(statements)++StatementOrBundle.forBundle(bundles), namespace)
  }

  override def newNamedBundle(id: org.openprovenance.prov.model.QualifiedName,
                              namespace: Namespace,
                              statements: java.util.Collection[org.openprovenance.prov.model.Statement]): org.openprovenance.prov.model.Bundle = {
    new Bundle(QualifiedName(id),Statement(statements),namespace)
  }

  override def newActivity (id: org.openprovenance.prov.model.QualifiedName,
                            startTime: XMLGregorianCalendar,
                            endTime: XMLGregorianCalendar,
                            attributes: java.util.Collection[org.openprovenance.prov.model.Attribute]): org.openprovenance.prov.model.Activity = {
    newActivity(QualifiedName(id),
      startTime,
      endTime,
      Attribute(attributes))
  }

  override def newWasDerivedFrom(id: org.openprovenance.prov.model.QualifiedName,
                                 generatedEntity: org.openprovenance.prov.model.QualifiedName,
                                 usedEntity: org.openprovenance.prov.model.QualifiedName,
                                 activity: org.openprovenance.prov.model.QualifiedName,
                                 generation: org.openprovenance.prov.model.QualifiedName,
                                 usage: org.openprovenance.prov.model.QualifiedName,
                                 attributes: java.util.Collection[org.openprovenance.prov.model.Attribute]): org.openprovenance.prov.model.WasDerivedFrom = {
    newWasDerivedFrom(QualifiedName(id),
                      QualifiedName(generatedEntity),
                      QualifiedName(usedEntity),
                      QualifiedName(activity),
                      QualifiedName(generation),
                      QualifiedName(usage),
                      Attribute(attributes))
  }

  override def newWasGeneratedBy(id: org.openprovenance.prov.model.QualifiedName,
                                 entity: org.openprovenance.prov.model.QualifiedName,
                                 activity: org.openprovenance.prov.model.QualifiedName,
                                 time: XMLGregorianCalendar,
                                 attributes: java.util.Collection[org.openprovenance.prov.model.Attribute]): WasGeneratedBy= {
    newWasGeneratedBy(QualifiedName(id),
                      QualifiedName(entity),
                      QualifiedName(activity),
                      Option(time),
                      Attribute(attributes))
  }

  override def newUsed(id: org.openprovenance.prov.model.QualifiedName,
                       activity: org.openprovenance.prov.model.QualifiedName,
                       entity: org.openprovenance.prov.model.QualifiedName,
                       time: XMLGregorianCalendar,
                       attributes: java.util.Collection[org.openprovenance.prov.model.Attribute]): org.openprovenance.prov.model.Used= {
    newUsed(QualifiedName(id),
            QualifiedName(activity),
            QualifiedName(entity),
            Option(time),
            Attribute(attributes))

  }

  override def newWasInvalidatedBy(id: org.openprovenance.prov.model.QualifiedName,
                                   entity: org.openprovenance.prov.model.QualifiedName,
                                   activity: org.openprovenance.prov.model.QualifiedName,
                                   time: XMLGregorianCalendar,
                                   attributes: java.util.Collection[org.openprovenance.prov.model.Attribute]): org.openprovenance.prov.model.WasInvalidatedBy= {
    newWasInvalidatedBy(QualifiedName(id),
      QualifiedName(entity),
      QualifiedName(activity),
      Option(time),
      Attribute(attributes))

  }

  override def newWasStartedBy(id: org.openprovenance.prov.model.QualifiedName,
                               activity: org.openprovenance.prov.model.QualifiedName,
                               trigger: org.openprovenance.prov.model.QualifiedName,
                               starter: org.openprovenance.prov.model.QualifiedName,
                               time: XMLGregorianCalendar,
                               attributes: java.util.Collection[org.openprovenance.prov.model.Attribute]): org.openprovenance.prov.model.WasStartedBy = {
    newWasStartedBy(QualifiedName(id),
                    QualifiedName(activity),
                    QualifiedName(trigger),
                    QualifiedName(starter),
                    Option(time),
                    Attribute(attributes))
  }



  override def newWasEndedBy(id: org.openprovenance.prov.model.QualifiedName,
                             activity: org.openprovenance.prov.model.QualifiedName,
                             trigger: org.openprovenance.prov.model.QualifiedName,
                             ender: org.openprovenance.prov.model.QualifiedName,
                             time: XMLGregorianCalendar,
                             attributes: java.util.Collection[org.openprovenance.prov.model.Attribute]): org.openprovenance.prov.model.WasEndedBy = {
    newWasEndedBy(QualifiedName(id),
                  QualifiedName(activity),
                  QualifiedName(trigger),
                  QualifiedName(ender),
                  Option(time),
                  Attribute(attributes))
  }


  override def newWasAssociatedWith (id: org.openprovenance.prov.model.QualifiedName,
                                     activity: org.openprovenance.prov.model.QualifiedName,
                                     agent: org.openprovenance.prov.model.QualifiedName,
                                     plan: org.openprovenance.prov.model.QualifiedName,
                                     attributes: java.util.Collection[org.openprovenance.prov.model.Attribute]): org.openprovenance.prov.model.WasAssociatedWith= {
    newWasAssociatedWith(QualifiedName(id),
                          QualifiedName(activity),
                          QualifiedName(agent),
                          QualifiedName(plan),
                          Attribute(attributes))
  }


  override def newActedOnBehalfOf (id: org.openprovenance.prov.model.QualifiedName,
                                   delegate: org.openprovenance.prov.model.QualifiedName,
                                   responsible: org.openprovenance.prov.model.QualifiedName,
                                   activity: org.openprovenance.prov.model.QualifiedName,
                                   attributes: java.util.Collection[org.openprovenance.prov.model.Attribute]): org.openprovenance.prov.model.ActedOnBehalfOf= {
    newActedOnBehalfOf(QualifiedName(id),
                      QualifiedName(delegate),
                      QualifiedName(responsible),
                      QualifiedName(activity),
                      Attribute(attributes))
  }


  override def newWasAttributedTo (id: org.openprovenance.prov.model.QualifiedName,
                                   entity: org.openprovenance.prov.model.QualifiedName,
                                   agent: org.openprovenance.prov.model.QualifiedName,
                                   attributes: java.util.Collection[org.openprovenance.prov.model.Attribute]): org.openprovenance.prov.model.WasAttributedTo = {
    newWasAttributedTo(QualifiedName(id),
      QualifiedName(entity),
      QualifiedName(agent),
      Attribute(attributes))
  }

  override def newSpecializationOf (specializedEntity: org.openprovenance.prov.model.QualifiedName,
                                    generalEntity: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.SpecializationOf= {

    newSpecializationOf(QualifiedName(specializedEntity),
      QualifiedName(generalEntity))

  }

  def newSpecializationOf (specializedEntity: QualifiedName,
                           generalEntity: QualifiedName): SpecializationOf= {
    new SpecializationOf(null,
                          specializedEntity,
                          generalEntity,
                          Set(),
                          Set(),
                          Map())

  }


  override def newAlternateOf (alternate1: org.openprovenance.prov.model.QualifiedName,
                               alternate2: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.AlternateOf = {
    newAlternateOf(QualifiedName(alternate1),
      QualifiedName(alternate2))

  }

  def newAlternateOf (alternate1: QualifiedName,
                      alternate2: QualifiedName): AlternateOf = {

    new AlternateOf(null,
                    alternate1,
                    alternate2,
                    Set(),
                    Set(),
                    Map())

  }


  override def newWasInformedBy (id: org.openprovenance.prov.model.QualifiedName,
                                 informed: org.openprovenance.prov.model.QualifiedName,
                                 informant: org.openprovenance.prov.model.QualifiedName,
                                 attributes: java.util.Collection[org.openprovenance.prov.model.Attribute]): org.openprovenance.prov.model.WasInformedBy= {
    newWasInformedBy(QualifiedName(id),
      QualifiedName(informed),
      QualifiedName(informant),
      Attribute(attributes))
  }



  override def newWasInfluencedBy (id: org.openprovenance.prov.model.QualifiedName,
                                   influencee: org.openprovenance.prov.model.QualifiedName,
                                   influencer: org.openprovenance.prov.model.QualifiedName,
                                   attributes: java.util.Collection[org.openprovenance.prov.model.Attribute]): org.openprovenance.prov.model.WasInfluencedBy = {
    newWasInfluencedBy(QualifiedName(id),
                      QualifiedName(influencee),
                      QualifiedName(influencer),
                      Attribute(attributes))
  }


  override def newHadMember (collection: org.openprovenance.prov.model.QualifiedName,
                             entity: java.util.Collection[org.openprovenance.prov.model.QualifiedName]): org.openprovenance.prov.model.HadMember = {
    val set=entity.asScala.toSet
    newHadMember(QualifiedName(collection),
      set.map(n => QualifiedName(n)))

  }


  override def newLabel(value: Object, `type`: model.QualifiedName): model.Label = {
    val type1=QualifiedName(`type`)
    Label(type1,value)
  }

  override def newRole(value: Object, `type`: model.QualifiedName): model.Role = {
    val type1=QualifiedName(`type`)
    Role(type1,value)
  }

  override def newLocation(value: Object, `type`: model.QualifiedName): model.Location = {
    val type1=QualifiedName(`type`)
    value match {
      case s:String => new Location(type1, s)
      case q:org.openprovenance.prov.model.QualifiedName => new Location(type1, QualifiedName(q))
      case l:org.openprovenance.prov.model.LangString => new Location(type1, new LangString(l))
      case _ => throw new UnsupportedOperationException
    }
  }

  override def newType(value: Object, `type`: model.QualifiedName): model.Type = {
    val type1=QualifiedName(`type`)
    value match {
      case s:String => new Type(type1, s)
      case q:org.openprovenance.prov.model.QualifiedName => new Type(type1, QualifiedName(q))
      case l:org.openprovenance.prov.model.LangString => new Type(type1, new LangString(l))
      case _ => throw new UnsupportedOperationException
    }
  }

  override def newValue(o: Object, `type`: model.QualifiedName): model.Value = {
    val type1=QualifiedName(`type`)
    o match {
      case s:String => new Value(type1, s)
      case q:org.openprovenance.prov.model.QualifiedName => new Value(type1, QualifiedName(q))
      case l:org.openprovenance.prov.model.LangString => new Value(type1, new LangString(l))
      case _ => throw new UnsupportedOperationException
    }
  }

  override def newOther(elementName: model.QualifiedName, value: Any, `type`: model.QualifiedName): model.Other = {
    val type1=QualifiedName(`type`)
    val elementName1=QualifiedName(elementName)
    value match {
      case s:String => new Other(elementName1,type1, s)
      case q:org.openprovenance.prov.model.QualifiedName => new Other(elementName1,type1, QualifiedName(q))
      case l:org.openprovenance.prov.model.LangString => new Other(elementName1,type1, new LangString(l))
      case _ => throw new UnsupportedOperationException
    }


  }

  override def newDerivedByInsertionFrom(qualifiedName: model.QualifiedName, qualifiedName1: model.QualifiedName, qualifiedName2: model.QualifiedName, list: util.List[Entry], collection: util.Collection[model.Attribute]): DerivedByInsertionFrom = ???

  override def newDerivedByRemovalFrom(qualifiedName: model.QualifiedName, qualifiedName1: model.QualifiedName, qualifiedName2: model.QualifiedName, list: util.List[Key], collection: util.Collection[model.Attribute]): DerivedByRemovalFrom = ???

  override def newDictionaryMembership(qualifiedName: model.QualifiedName, list: util.List[Entry]): DictionaryMembership = ???

  override def newMentionOf(qualifiedName: model.QualifiedName, qualifiedName1: model.QualifiedName, qualifiedName2: model.QualifiedName): model.MentionOf = ???

  override def startBundle(qualifiedName: model.QualifiedName, namespace: Namespace): Unit = ???

  override def startDocument(namespace: Namespace): Unit = ???
}

class ICons extends ImmutableConstructor


class ProvFactory1 extends vanilla.ProvFactory(new ObjectFactory, new ProvConstructor(new ICons)) {}


//TODO: code duplication between ProvConstructor (Delegated) and ProvFactory. Requires refactoring
//class ProvFactory extends org.openprovenance.prov.model.ProvFactory (new ObjectFactory) with org.openprovenance.prov.model.AtomConstructor {
class ProvFactory extends ProvFactory1  {



  override def getSerializer() = throw new UnsupportedOperationException
  override def newQualifiedName(x1: String, x2:String, x3: String, x4:org.openprovenance.prov.model.ProvUtilities.BuildFlag) = throw new UnsupportedOperationException
  override def newAttribute(kind: org.openprovenance.prov.model.Attribute.AttributeKind,
                            value: Object,
                            `type`: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Attribute = {
    val type1=QualifiedName(`type`)
    kind match {
      case AttributeKind.PROV_TYPE => Type(type1,value)
      case AttributeKind.PROV_ROLE => Role(type1,value)
      case AttributeKind.PROV_LABEL =>  Label(type1,value)
      case AttributeKind.PROV_LOCATION => Location(type1,value)
      case AttributeKind.PROV_VALUE => Value(type1,value)
      case AttributeKind.PROV_KEY => throw new FooException
      case AttributeKind.OTHER => throw new UnsupportedOperationException // this function does have an elementName, so, we can't deal with OTHER
    }
  }
  val prov_type: QualifiedName           =getName.PROV_TYPE.asInstanceOf[QualifiedName]
  val prov_role: QualifiedName           =getName.PROV_ROLE.asInstanceOf[QualifiedName]
  val prov_location: QualifiedName       =getName.PROV_LOCATION.asInstanceOf[QualifiedName]
  val prov_value: QualifiedName          =getName.PROV_VALUE.asInstanceOf[QualifiedName]
  val prov_key: QualifiedName            =getName.PROV_KEY.asInstanceOf[QualifiedName]
  val prov_label: QualifiedName          =getName.PROV_LABEL.asInstanceOf[QualifiedName]
  val prov_qualified_name: QualifiedName =getName.PROV_QUALIFIED_NAME.asInstanceOf[QualifiedName]
  val xsd_string: QualifiedName          =getName.XSD_STRING.asInstanceOf[QualifiedName]
  val xsd_int: QualifiedName             =getName.XSD_INT.asInstanceOf[QualifiedName]

  val attributeKindMap: Map[QualifiedName, AttributeKind] = Map(prov_type -> AttributeKind.PROV_TYPE, prov_role -> AttributeKind.PROV_ROLE, prov_value -> AttributeKind.PROV_VALUE, prov_location -> AttributeKind.PROV_LOCATION, prov_key -> AttributeKind.PROV_KEY, prov_label -> AttributeKind.PROV_LABEL)
 // val attributeKindCons: Map[AttributeKind, ((QualifiedName,Object) -> org.openprovenance.prov.model.Attribute)] = Map(AttributeKind.PROV_TYPE -> {case (t:QualifiedName,v:Object) -> Type(t, v)})

  def attributeKindOld(qualifiedName: org.openprovenance.prov.model.QualifiedName): AttributeKind = {
    qualifiedName match {
      case  `prov_type` =>  AttributeKind.PROV_TYPE
      case  `prov_role` =>  AttributeKind.PROV_ROLE
      case  `prov_value` =>  AttributeKind.PROV_VALUE
      case  `prov_location` =>  AttributeKind.PROV_LOCATION
      case  `prov_key` =>  AttributeKind.PROV_KEY
      case  `prov_label` =>  AttributeKind.PROV_LABEL
      case _ => AttributeKind.OTHER
      }
  }

  def attributeKind(qualifiedName: org.openprovenance.prov.model.QualifiedName): AttributeKind = {
    attributeKindMap.getOrElse(qualifiedName.asInstanceOf[QualifiedName], AttributeKind.OTHER)
  }


  override def newAttribute(qualifiedName: org.openprovenance.prov.model.QualifiedName,
                            value: Object,
                            typ: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Attribute = {
    val kind=attributeKind(qualifiedName)
    kind match {
      case AttributeKind.OTHER => Other(qualifiedName,QualifiedName(typ),value)
      case _ => newAttribute(kind,value,typ)
    }

  }

  override def newInternationalizedString(s: String): org.openprovenance.prov.model.LangString = {
    new LangString(s,None)
  }

  override def newInternationalizedString(s: String, lang: String): org.openprovenance.prov.model.LangString = {
    new LangString(s,if (lang==null) None else Some(lang))
  }


  override def newKey(x$1: Any,x$2: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Key = throw new UnsupportedOperationException

  override def newQualifiedName(namespace: String, local: String, prefix: String): org.openprovenance.prov.model.QualifiedName = {
    new QualifiedName(prefix,local,namespace)
  }

  import Attribute.split
  
  override def newEntity(id: org.openprovenance.prov.model.QualifiedName,   
                         attributes: java.util.Collection[org.openprovenance.prov.model.Attribute]): org.openprovenance.prov.model.Entity= {
    newEntity(id,Attribute(attributes))
  }

  def newEntity(id: org.openprovenance.prov.model.QualifiedName,
                         attributes: Iterable[Attribute]): Entity= {
    split(attributes) match {
      case (ls,ts,vs,locs,rs,os) => 
        val size=vs.size
        if (size>1) throw new MultipleValuedEntityException
        val langStrings=LangString(ls.map(l=>l.getLangString))
        vs.headOption match {
          case None => new Entity(QualifiedName(id),langStrings,ts,None,locs,os)
          case Some(v) => new Entity(QualifiedName(id),langStrings,ts,Some(v),locs,os)
        }
        
    }
  }
  
  
  override def newAgent(id: org.openprovenance.prov.model.QualifiedName,   
                         attributes: java.util.Collection[org.openprovenance.prov.model.Attribute]): org.openprovenance.prov.model.Agent = {

    newAgent(QualifiedName(id),Attribute(attributes))
  }

  def newAgent(id: QualifiedName,
               attributes: Iterable[Attribute]): Agent = {
    split(attributes) match {
      case (ls,ts,vs,locs,rs,os) => 
        val size=vs.size
        if (size>1) throw new MultipleValuedEntityException
        val langStrings=LangString(ls.map(l=>l.getLangString))
        vs.headOption match {
          case None => new Agent(id,langStrings,ts,None,locs,os)
          case Some(v) => new Agent(id,langStrings,ts,Some(v),locs,os)
        }
        
    }
  }
  
  override def newDocument(document: org.openprovenance.prov.model.Document): Document = {
    val statements=document.getStatementOrBundle
    new Document(StatementOrBundle.forStatementOrBundle(statements), document.getNamespace)
  }
  

  override def newDocument(namespace: Namespace,
                           statements: java.util.Collection[org.openprovenance.prov.model.Statement],
                           bundles: java.util.Collection[org.openprovenance.prov.model.Bundle]): org.openprovenance.prov.model.Document = {
      new Document(StatementOrBundle(statements)++StatementOrBundle.forBundle(bundles), namespace)
  }
  
  override def newNamedBundle(id: org.openprovenance.prov.model.QualifiedName,
                              namespace: Namespace,
                              statements: java.util.Collection[org.openprovenance.prov.model.Statement]): org.openprovenance.prov.model.Bundle = {
      new Bundle(QualifiedName(id),Statement(statements),namespace)
  }
  
  override def newActivity (id: org.openprovenance.prov.model.QualifiedName, 
                            startTime: XMLGregorianCalendar,
                            endTime: XMLGregorianCalendar,
                            attributes: java.util.Collection[org.openprovenance.prov.model.Attribute]): org.openprovenance.prov.model.Activity = {
     newActivity(QualifiedName(id),
                 startTime,
                 endTime,
                 Attribute(attributes))
  }
  
  def newActivity (id: QualifiedName, 
                   startTime: XMLGregorianCalendar,
                   endTime: XMLGregorianCalendar,
                   attributes: Iterable[Attribute]): Activity = {
      newActivity(id,
                  Option(startTime),
                  Option(endTime),
                  attributes)
  }
    
    def newActivity (id: QualifiedName, 
                   startTime: Option[XMLGregorianCalendar],
                   endTime: Option[XMLGregorianCalendar],
                   attributes: Iterable[Attribute]): Activity = {
    split(attributes) match {
      case (ls,ts,vs,locs,rs,os) => 
        val size=vs.size
        if (size>0) throw new ValueExistException
        val langStrings=LangString(ls.map(l=>l.getLangString))
        new Activity(id,
                     startTime,
                     endTime,
                     langStrings,ts,locs,os)
        
    }
      
  }
    
  override def newWasDerivedFrom(id: org.openprovenance.prov.model.QualifiedName,   
                                 generatedEntity: org.openprovenance.prov.model.QualifiedName,   
                                 usedEntity: org.openprovenance.prov.model.QualifiedName,   
                                 activity: org.openprovenance.prov.model.QualifiedName,   
                                 generation: org.openprovenance.prov.model.QualifiedName,   
                                 usage: org.openprovenance.prov.model.QualifiedName,   
                                 attributes: java.util.Collection[org.openprovenance.prov.model.Attribute]): org.openprovenance.prov.model.WasDerivedFrom = {
    newWasDerivedFrom(QualifiedName(id),
                      QualifiedName(generatedEntity),
                      QualifiedName(usedEntity),
                      if (activity==null) null else QualifiedName(activity),
                      if (generation==null) null else QualifiedName(generation),
                      if (usage==null) null else QualifiedName(usage),
                      Attribute(attributes))
  }

  def newWasDerivedFrom(id: QualifiedName,
                        generatedEntity: QualifiedName,   
                        usedEntity: QualifiedName,   
                        activity: QualifiedName,   
                        generation: QualifiedName,   
                        usage: QualifiedName,   
                        attributes: Iterable[Attribute]): WasDerivedFrom= {
    split(attributes) match {
      case (ls,ts,vs,locs,rs,os) => 
        val size=vs.size
        if (size>0) throw new ValueExistException
        val langStrings=LangString(ls.map(l=>l.getLangString))
        new WasDerivedFrom(id,
                           generatedEntity,
                           usedEntity,
                           activity,
                           generation,
                           usage,
                           langStrings,
                           ts,
                           os)
        
    }
  }
  
  
    override def newWasGeneratedBy(id: org.openprovenance.prov.model.QualifiedName,   
                                 entity: org.openprovenance.prov.model.QualifiedName,   
                                 activity: org.openprovenance.prov.model.QualifiedName,   
                                 time: XMLGregorianCalendar,   
                                 attributes: java.util.Collection[org.openprovenance.prov.model.Attribute]): WasGeneratedBy= {
      newWasGeneratedBy(QualifiedName(id),
                       QualifiedName(entity),
                       QualifiedName(activity),
                       Option(time),
                       Attribute(attributes))
    }
    def newWasGeneratedBy(id: QualifiedName,
                                 entity: QualifiedName,   
                                 activity: QualifiedName,   
                                 time: Option[XMLGregorianCalendar],   
                                 attributes: Iterable[Attribute]): WasGeneratedBy= {
    split(attributes) match {
      case (ls,ts,vs,locs,rs,os) => 
        val size=vs.size
        if (size>0) throw new ValueExistException
        val langStrings=LangString(ls.map(l=>l.getLangString))
        new WasGeneratedBy(id,
                           entity,
                           activity,
                           time,
                           langStrings,
                           ts,
                           locs,
                           rs,
                           os)
        
    }
  }


  override def newUsed(id: org.openprovenance.prov.model.QualifiedName,   
                       activity: org.openprovenance.prov.model.QualifiedName,   
                       entity: org.openprovenance.prov.model.QualifiedName,   
                       time: XMLGregorianCalendar,   
                       attributes: java.util.Collection[org.openprovenance.prov.model.Attribute]): org.openprovenance.prov.model.Used= {
    newUsed(QualifiedName(id),
            QualifiedName(activity),
            QualifiedName(entity),
            Option(time),
            Attribute(attributes))
    
  }
   def newUsed(id: QualifiedName,   
                       activity: QualifiedName,   
                       entity: QualifiedName,   
                       time: Option[XMLGregorianCalendar],   
                       attributes: Iterable[Attribute]): Used= {
    split(attributes) match {
      case (ls,ts,vs,locs,rs,os) => 
        val size=vs.size
        if (size>0) throw new ValueExistException
        val langStrings=LangString(ls.map(l=>l.getLangString))
        new Used(id,
                 activity,
                 entity,
                 time,
                 langStrings,
                 ts,
                 locs,
                 rs,
                 os)
        
    }
  }

  
  override def newWasInvalidatedBy(id: org.openprovenance.prov.model.QualifiedName,   
                                   entity: org.openprovenance.prov.model.QualifiedName,   
                                   activity: org.openprovenance.prov.model.QualifiedName,   
                                   time: XMLGregorianCalendar,   
                                   attributes: java.util.Collection[org.openprovenance.prov.model.Attribute]): org.openprovenance.prov.model.WasInvalidatedBy= {
    newWasInvalidatedBy(QualifiedName(id),
                        QualifiedName(entity),
                        QualifiedName(activity),
                        Option(time),
                        Attribute(attributes))
                     
  }
  
  def newWasInvalidatedBy(id: QualifiedName,   
                                   entity: QualifiedName,   
                                   activity: QualifiedName,   
                                   time: Option[XMLGregorianCalendar],   
                                   attributes: Iterable[Attribute]): WasInvalidatedBy= {
    split(attributes) match {
      case (ls,ts,vs,locs,rs,os) => 
        val size=vs.size
        if (size>0) throw new ValueExistException
        val langStrings=LangString(ls.map(l=>l.getLangString))
        new WasInvalidatedBy(id,
                             entity,
                             activity,
                             time,
                             langStrings,
                             ts,
                             locs,
                             rs,
                             os)
        
    }
  }


  override def newWasStartedBy(id: org.openprovenance.prov.model.QualifiedName,   
                               activity: org.openprovenance.prov.model.QualifiedName,   
                               trigger: org.openprovenance.prov.model.QualifiedName,   
                               starter: org.openprovenance.prov.model.QualifiedName,   
                               time: XMLGregorianCalendar,
                               attributes: java.util.Collection[org.openprovenance.prov.model.Attribute]): org.openprovenance.prov.model.WasStartedBy = {
      newWasStartedBy(QualifiedName(id),
                      QualifiedName(activity),
                      QualifiedName(trigger),
                      QualifiedName(starter),
                      Option(time),
                      Attribute(attributes))
  }
  
  def newWasStartedBy(id: QualifiedName,   
                      activity: QualifiedName,   
                      trigger: QualifiedName,   
                      starter: QualifiedName,   
                      time: Option[XMLGregorianCalendar],
                      attributes: Iterable[Attribute]): WasStartedBy = {
    split(attributes) match {
      case (ls,ts,vs,locs,rs,os) => 
        val size=vs.size
        if (size>0) throw new ValueExistException
        val langStrings=LangString(ls.map(l=>l.getLangString))
        new WasStartedBy(id,
                         activity,
                         trigger,
                         starter,
                         time,
                         langStrings,
                         ts,
                         locs,
                         rs,
                         os)
        
    }
  }

  override def newWasEndedBy(id: org.openprovenance.prov.model.QualifiedName,   
                               activity: org.openprovenance.prov.model.QualifiedName,   
                               trigger: org.openprovenance.prov.model.QualifiedName,   
                               ender: org.openprovenance.prov.model.QualifiedName,   
                               time: XMLGregorianCalendar,
                               attributes: java.util.Collection[org.openprovenance.prov.model.Attribute]): org.openprovenance.prov.model.WasEndedBy = {
      newWasEndedBy(QualifiedName(id),
                      QualifiedName(activity),
                      QualifiedName(trigger),
                      QualifiedName(ender),
                      Option(time),
                      Attribute(attributes))
  }
  
  def newWasEndedBy(id: QualifiedName,   
                      activity: QualifiedName,   
                      trigger: QualifiedName,   
                      ender: QualifiedName,   
                      time: Option[XMLGregorianCalendar],
                      attributes: Iterable[Attribute]): WasEndedBy = {
    split(attributes) match {
      case (ls,ts,vs,locs,rs,os) => 
        val size=vs.size
        if (size>0) throw new ValueExistException
        val langStrings=LangString(ls.map(l=>l.getLangString))
        new WasEndedBy(id,
                         activity,
                         trigger,
                         ender,
                         time,
                         langStrings,
                         ts,
                         locs,
                         rs,
                         os)
        
    }
  }

   
  override def newWasAssociatedWith (id: org.openprovenance.prov.model.QualifiedName,   
                                     activity: org.openprovenance.prov.model.QualifiedName,   
                                     agent: org.openprovenance.prov.model.QualifiedName,   
                                     plan: org.openprovenance.prov.model.QualifiedName,   
                                     attributes: java.util.Collection[org.openprovenance.prov.model.Attribute]): org.openprovenance.prov.model.WasAssociatedWith= {
      newWasAssociatedWith(QualifiedName(id),
                           QualifiedName(activity),
                           QualifiedName(agent),
                           if (plan==null) null else QualifiedName(plan),
                           Attribute(attributes))
  }
 
  def newWasAssociatedWith (id: QualifiedName,   
                            activity: QualifiedName,   
                            agent: QualifiedName,   
                            plan: QualifiedName,   
                            attributes: Iterable[Attribute]): WasAssociatedWith= {
    split(attributes) match {
      case (ls,ts,vs,locs,rs,os) => 
        if (vs.nonEmpty) throw new ValueExistException
        val langStrings=LangString(ls.map(l=>l.getLangString))
        new WasAssociatedWith(id,
                              activity,
                              agent,
                              plan,
                              langStrings,
                              ts,
                              rs,
                              os)
        
    }
  }
  

  override def newActedOnBehalfOf (id: org.openprovenance.prov.model.QualifiedName,   
                                   delegate: org.openprovenance.prov.model.QualifiedName,   
                                   responsible: org.openprovenance.prov.model.QualifiedName,   
                                   activity: org.openprovenance.prov.model.QualifiedName,   
                                   attributes: java.util.Collection[org.openprovenance.prov.model.Attribute]): org.openprovenance.prov.model.ActedOnBehalfOf= {
    newActedOnBehalfOf(QualifiedName(id),
                       QualifiedName(delegate),
                       QualifiedName(responsible),
                       QualifiedName(activity),
                       Attribute(attributes))
  }

  def newActedOnBehalfOf (id: QualifiedName,
                          delegate: QualifiedName,
                          responsible: QualifiedName,
                          activity: QualifiedName,
                          attributes: Iterable[Attribute]): ActedOnBehalfOf = {
    split(attributes) match {
      case (ls,ts,vs,locs,rs,os) => 
        if (vs.nonEmpty) throw new ValueExistException
        val langStrings=LangString(ls.map(l=>l.getLangString))
        new ActedOnBehalfOf(id,
                            delegate,
                            responsible,
                            activity,
                            langStrings,
                            ts,
                            os)
    }
  }
  



  override def newWasAttributedTo (id: org.openprovenance.prov.model.QualifiedName,   
                                   entity: org.openprovenance.prov.model.QualifiedName,   
                                   agent: org.openprovenance.prov.model.QualifiedName,   
                                   attributes: java.util.Collection[org.openprovenance.prov.model.Attribute]): org.openprovenance.prov.model.WasAttributedTo = {
    newWasAttributedTo(QualifiedName(id),
                       QualifiedName(entity),
                       QualifiedName(agent),
                       Attribute(attributes))
  }
  def newWasAttributedTo (id: QualifiedName,
                          entity: QualifiedName,   
                          agent: QualifiedName,   
                          attributes: Iterable[Attribute]): WasAttributedTo = {
    split(attributes) match {
      case (ls,ts,vs,locs,rs,os) => 
        if (vs.nonEmpty) throw new ValueExistException
        if (rs.nonEmpty) throw new RoleExistException
        val langStrings=LangString(ls.map(l=>l.getLangString))
        new WasAttributedTo(id,
                            entity,
                            agent,
                            langStrings,
                            ts,
                            os)
        
    }
  }
  


  override def newSpecializationOf (specializedEntity: org.openprovenance.prov.model.QualifiedName,   
                                    generalEntity: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.SpecializationOf= {

      newSpecializationOf(QualifiedName(specializedEntity),
                          QualifiedName(generalEntity))

  }

  def newSpecializationOf (specializedEntity: QualifiedName,   
                           generalEntity: QualifiedName): SpecializationOf= {      
      new SpecializationOf(null,
                           specializedEntity,
                           generalEntity,
                           Set(),
                           Set(),
                           Map())

  }
  
  def newSpecializationOf (id: QualifiedName,
                           entity2: QualifiedName,   
                           entity1: QualifiedName,   
                           attributes: Iterable[Attribute]): SpecializationOf = {
    split(attributes) match {
      case (ls,ts,vs,locs,rs,os) => 
        if (vs.nonEmpty) throw new ValueExistException
        if (rs.nonEmpty) throw new RoleExistException
        val langStrings=LangString(ls.map(l=>l.getLangString))
        new SpecializationOf(id,
                            entity2,
                            entity1,
                            langStrings,
                            ts,
                            os)
        
    }
  }
  
  def newMentionOf (specializedEntity: QualifiedName,   
		                generalEntity: QualifiedName,
		                bundle: QualifiedName): MentionOf= {      
      new MentionOf(null,
    		            specializedEntity,
    		            generalEntity,
    		            bundle,
    		            Set(),
    		            Set(),
    		            Map())

  }
  
  def newMentionOf (specializedEntity: QualifiedName,   
		                 generalEntity: QualifiedName,
		                 bundle: QualifiedName,
		                 attributes: Iterable[Attribute]): MentionOf= {
       split(attributes) match {
         case (ls,ts,vs,locs,rs,os) => 
           if (vs.nonEmpty) throw new ValueExistException
           if (rs.nonEmpty) throw new RoleExistException
           val langStrings=LangString(ls.map(l=>l.getLangString))
           new MentionOf(null,
        		             specializedEntity,
        		             generalEntity,
        		             bundle,
        		             langStrings,
        		             ts,
        		             os)
       }

  }

   def newMentionOf (id: QualifiedName,
                     specializedEntity: QualifiedName,   
		                 generalEntity: QualifiedName,
		                 bundle: QualifiedName,
		                 attributes: Iterable[Attribute]): MentionOf= {
       split(attributes) match {
         case (ls,ts,vs,locs,rs,os) => 
           if (vs.nonEmpty) throw new ValueExistException
           if (rs.nonEmpty) throw new RoleExistException
           val langStrings=LangString(ls.map(l=>l.getLangString))
           new MentionOf(id,
        		             specializedEntity,
        		             generalEntity,
        		             bundle,
        		             langStrings,
        		             ts,
        		             os)
       }

  }
  override def newAlternateOf (alternate1: org.openprovenance.prov.model.QualifiedName,   
                               alternate2: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.AlternateOf = {
      newAlternateOf(QualifiedName(alternate1),
                     QualifiedName(alternate2))

  }

  def newAlternateOf (alternate1: QualifiedName,
                      alternate2: QualifiedName): AlternateOf = {

      new AlternateOf(null,
                      alternate1,
                      alternate2,
                      Set(),
                      Set(),
                      Map())

  }


  def newAlternateOf (id: QualifiedName,
                      entity2: QualifiedName,   
                      entity1: QualifiedName,   
                      attributes: Iterable[Attribute]): AlternateOf = {
    split(attributes) match {
      case (ls,ts,vs,locs,rs,os) => 
        if (vs.nonEmpty) throw new ValueExistException
        if (rs.nonEmpty) throw new RoleExistException
        val langStrings=LangString(ls.map(l=>l.getLangString))
        new AlternateOf(id,
                            entity2,
                            entity1,
                            langStrings,
                            ts,
                            os)
        
    }
  }

  override def newWasInformedBy (id: org.openprovenance.prov.model.QualifiedName,   
                                 informed: org.openprovenance.prov.model.QualifiedName,   
                                 informant: org.openprovenance.prov.model.QualifiedName,   
                                 attributes: java.util.Collection[org.openprovenance.prov.model.Attribute]): org.openprovenance.prov.model.WasInformedBy= {
	   newWasInformedBy(QualifiedName(id),
			                QualifiedName(informed),
			                QualifiedName(informant),
			                Attribute(attributes))
  }
  
  def newWasInformedBy (id: QualifiedName,   
		                    informed: QualifiedName,   
		                    informant: QualifiedName,   
		                    attributes: Iterable[Attribute]): WasInformedBy= {
    split(attributes) match {
      case (ls,ts,vs,locs,rs,os) => 
        if (vs.nonEmpty) throw new ValueExistException
        if (rs.nonEmpty) throw new RoleExistException
        if (locs.nonEmpty) throw new LocationExistException
        val langStrings=LangString(ls.map(l=>l.getLangString))
        new WasInformedBy(id,
                          informed,
                          informant,
                          langStrings,
                          ts,
                          os)
        
    }
  }
  
  
  
  override def newWasInfluencedBy (id: org.openprovenance.prov.model.QualifiedName,   
                                   influencee: org.openprovenance.prov.model.QualifiedName,   
                                   influencer: org.openprovenance.prov.model.QualifiedName,   
                                   attributes: java.util.Collection[org.openprovenance.prov.model.Attribute]): org.openprovenance.prov.model.WasInfluencedBy = {
    newWasInfluencedBy(QualifiedName(id),
                       QualifiedName(influencee),
                       QualifiedName(influencer),
                       Attribute(attributes))
  }
  def newWasInfluencedBy (id: QualifiedName,
                          influencee: QualifiedName,   
                          influencer: QualifiedName,   
                          attributes: Iterable[Attribute]): WasInfluencedBy = {
    split(attributes) match {
      case (ls,ts,vs,locs,rs,os) => 
        val size=vs.size
        if (vs.nonEmpty) throw new ValueExistException
        if (rs.nonEmpty) throw new RoleExistException
        if (locs.nonEmpty) throw new LocationExistException
        val langStrings=LangString(ls.map(l=>l.getLangString))
        new WasInfluencedBy(id,
                            influencee,
                            influencer,
                            langStrings,
                            ts,
                            os)
        
    }
  }

  override def  newHadMember (collection: org.openprovenance.prov.model.QualifiedName,
                             entity: java.util.Collection[org.openprovenance.prov.model.QualifiedName]): org.openprovenance.prov.model.HadMember = {
      val set=entity.asScala.toSet
              newHadMember(QualifiedName(collection),
                           set.map(n => QualifiedName(n)))

  }

  def newHadMember (collection: QualifiedName,
                    entity: Set[QualifiedName]): HadMember = {
      new HadMember(null,
                    QualifiedName(collection),
                    entity,
                    Set(),
                    Set(),
                    HashMap())

  }
  
  def newHadMember (id: QualifiedName,
                    collection: QualifiedName,   
                    entity: Set[QualifiedName],
                    attributes: Iterable[Attribute]): HadMember = {
    split(attributes) match {
      case (ls,ts,vs,locs,rs,os) => 
        if (vs.nonEmpty) throw new ValueExistException
        if (rs.nonEmpty) throw new RoleExistException
        val langStrings=LangString(ls.map(l=>l.getLangString))
        new HadMember(id,
                      collection,
                      entity,
                      langStrings,
                      ts,
                      os)
        
    }
  }

  override def newLabel(value: Object, `type`: model.QualifiedName): model.Label = {
    val type1=QualifiedName(`type`)
    Label(type1,value)
  }
}

object Kind extends Enumeration {
    type Kind = this.Value
    val ent, act, ag, wdf, wgb, usd, wib, waw, wat, wsb, web, aobo, winfob, winfl, alt, spec, mem, men, bun = this.Value

    def toKind(k: org.openprovenance.prov.model.StatementOrBundle.Kind): this.Value = {
       k match {
         case PROV_ENTITY => ent
         case PROV_ACTIVITY => act
         case PROV_USAGE => usd
         case PROV_AGENT => ag
         case PROV_ALTERNATE => alt
         case PROV_ASSOCIATION => waw
         case PROV_ATTRIBUTION => wat
         case PROV_COMMUNICATION => winfob
         case PROV_DELEGATION => aobo
         case PROV_DERIVATION => wdf
         case PROV_END => web
         case PROV_GENERATION => wgb
         case PROV_INFLUENCE => winfl
         case PROV_INVALIDATION => wib
         case PROV_MEMBERSHIP => mem
         case PROV_MENTION => men
         case PROV_SPECIALIZATION => spec
         case PROV_START => wsb
         case _ => throw new UnsupportedOperationException
       }
    }
}


class LocationExistException extends Exception
class ValueExistException extends Exception
class RoleExistException extends Exception
class MultipleValuedEntityException extends Exception




