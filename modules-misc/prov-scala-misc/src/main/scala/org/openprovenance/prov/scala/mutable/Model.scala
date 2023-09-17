package org.openprovenance.prov.scala.mutable


import org.openprovenance.prov.model.StatementOrBundle.Kind
import org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_AGENT
import org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_ENTITY
import org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_ACTIVITY
import org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_USAGE
import org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_GENERATION
import org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_DERIVATION
import org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_SPECIALIZATION
import org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_ALTERNATE
import org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_ATTRIBUTION
import org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_ASSOCIATION
import org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_DELEGATION
import org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_INVALIDATION
import org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_START
import org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_END
import org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_MEMBERSHIP
import org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_COMMUNICATION
import org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_INFLUENCE
import org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_BUNDLE

import scala.beans.BeanProperty
import scala.jdk.CollectionConverters._
import org.openprovenance.prov.model.Namespace
import org.openprovenance.prov.model.Statement
import org.openprovenance.prov.model.StatementOrBundle
import org.openprovenance.prov.model.extension.QualifiedSpecializationOf
import org.openprovenance.prov.model.extension.QualifiedAlternateOf
import org.openprovenance.prov.model.extension.QualifiedHadMember
import org.openprovenance.prov.model.QualifiedNameUtils
import org.openprovenance.prov.model.exception.QualifiedNameException
import javax.xml.datatype.XMLGregorianCalendar
import org.openprovenance.prov.model
import org.openprovenance.prov.model.Attribute.AttributeKind


trait HasLocation {
    @BeanProperty
    var location: java.util.List[org.openprovenance.prov.model.Location]=new java.util.LinkedList() 
}

trait HasRole {
    @BeanProperty
    var role: java.util.List[org.openprovenance.prov.model.Role]=new java.util.LinkedList()        
}

trait HasLabel {
    @BeanProperty
    var label: java.util.List[org.openprovenance.prov.model.LangString]=new java.util.LinkedList()        
}


trait HasOther {
    def setOther(others: java.util.List[org.openprovenance.prov.model.Other]) {
      other=others
    }
    def getOther() = {
      if (other==null) {
        other=new java.util.LinkedList()
      }
      other
    }

    var other: java.util.List[org.openprovenance.prov.model.Other]=null
}

trait HasValue {
    @BeanProperty
    var value: org.openprovenance.prov.model.Value=null
}

trait HasType {
     //@BeanProperty
    var typex: java.util.List[org.openprovenance.prov.model.Type]=new java.util.LinkedList()
    def getType() = { typex }
    
}

trait Identifiable {
    @BeanProperty
    var id: org.openprovenance.prov.model.QualifiedName=null
}

trait Hashable {
      
      @inline final def h(x: AnyRef) = {
          if (x==null) 0 else x.hashCode
      }
      
      @inline final def pr(v0: Int,v1:Int) = {
          prime*v0+v1
      }
    
      final private val prime=37
}

abstract class Term
class Entity extends  org.openprovenance.prov.model.Entity with Identifiable with HasLocation with HasValue with HasLabel with HasType with HasOther with Hashable {     

    @BeanProperty
    val kind: Kind=PROV_ENTITY
    
    def canEqual(a: Any) = a.isInstanceOf[Entity]

    override def equals(that: Any): Boolean =
    that match {
      case that: Entity => that.canEqual(this) && 
                           this.id == that.id && 
                           this.value == that.value && 
                           this.location == that.location &&  
                           this.label == that.label && 
                           this.typex == that.typex && 
                           this.other == that.other                   
      case _ => false
    }
   
    override def hashCode:Int = {
      pr(pr(pr(pr(pr(h(id),h(value)),h(location)),h(label)),h(typex)),h(other))
    }
    
    override def toString () :String = {
      "entity(" + id + "," + hashCode + ",V" + value + ",T" + typex + ",L" + location + ",L" + label +  ",O" + other + ")"
    }

           
}



class Activity extends org.openprovenance.prov.model.Activity with Identifiable with HasLocation with HasLabel with HasType with HasOther with Hashable {
 
    @BeanProperty
    var endTime: XMLGregorianCalendar=null
    
    @BeanProperty
    var startTime: XMLGregorianCalendar=null
    
    @BeanProperty
    val kind: Kind=PROV_ACTIVITY
    
    
        
    def canEqual(a: Any) = a.isInstanceOf[Activity]

    override def equals(that: Any): Boolean =
    that match {
      case that: Activity => that.canEqual(this) && 
                           this.id == that.id && 
                           this.startTime == that.startTime &&
                           this.endTime == that.endTime &&
                           this.location == that.location &&  
                           this.label == that.label && 
                           this.typex == that.typex && 
                           this.other == that.other                   
      case _ => false
    }
    
    override def hashCode:Int = {
      pr(pr(pr(pr(pr(pr(h(id),h(startTime)),h(endTime)),h(location)),h(label)),h(typex)),h(other))
    }
    
    override def toString () :String = {
      "activity(" + id + "," + ",T" + typex + ",L" + location + ",L" + label +  ",O" + other + ")"
    }

           
}

class Agent extends org.openprovenance.prov.model.Agent with Identifiable with HasLocation with HasValue with HasLabel with HasType with HasOther with Hashable {     

    @BeanProperty
    val kind: Kind=PROV_AGENT
    
    def canEqual(a: Any) = a.isInstanceOf[Agent]

    override def equals(that: Any): Boolean =
    that match {
      case that: Agent => that.canEqual(this) && 
                           this.id == that.id && 
                           this.value == that.value && 
                           this.location == that.location &&  
                           this.label == that.label && 
                           this.typex == that.typex && 
                           this.other == that.other                   
      case _ => false
    }
    
    override def hashCode:Int = {
        pr(pr(pr(pr(pr(h(id),h(value)),h(location)),h(label)),h(typex)),h(other))

    }
           
}


class Used extends org.openprovenance.prov.model.Used with Identifiable with HasLocation with HasLabel with HasType with HasOther with HasRole with Hashable {
  
    @BeanProperty
    var activity: org.openprovenance.prov.model.QualifiedName=null
    
    @BeanProperty
    var entity: org.openprovenance.prov.model.QualifiedName=null
    
    @BeanProperty
    var time: javax.xml.datatype.XMLGregorianCalendar=null
    
    @BeanProperty
    val kind: Kind=PROV_USAGE
    
    def canEqual(a: Any) = a.isInstanceOf[Used]

    override def equals(that: Any): Boolean =
    that match {
      case that: Used => that.canEqual(this) && 
                           this.id == that.id && 
                           this.activity == that.activity && 
                           this.entity == that.entity && 
                           this.time == that.time &&
                           this.location == that.location &&  
                           this.label == that.label && 
                           this.typex == that.typex && 
                           this.role == that.role && 
                           this.other == that.other                   
      case _ => false
    }
    
    override def hashCode:Int = {
        pr(pr(pr(pr(pr(pr(pr(h(id),h(activity)),h(entity)),h(location)),h(label)),h(typex)),h(role)),h(other))

    }

}

class WasGeneratedBy extends org.openprovenance.prov.model.WasGeneratedBy with Identifiable with HasLocation with HasLabel with HasType with HasOther with HasRole with Hashable {
  
    @BeanProperty
    var activity: org.openprovenance.prov.model.QualifiedName=null
    
    @BeanProperty
    var entity: org.openprovenance.prov.model.QualifiedName=null
    
    @BeanProperty
    var time: javax.xml.datatype.XMLGregorianCalendar=null
    
    @BeanProperty
    val kind: Kind=PROV_GENERATION
    
    def canEqual(a: Any) = a.isInstanceOf[WasGeneratedBy]

    override def equals(that: Any): Boolean =
    that match {
      case that: WasGeneratedBy => that.canEqual(this) && 
                           this.id == that.id && 
                           this.activity == that.activity && 
                           this.entity == that.entity && 
                           this.time == that.time &&
                           this.location == that.location &&  
                           this.label == that.label && 
                           this.typex == that.typex && 
                           this.role == that.role && 
                           this.other == that.other                   
      case _ => false
    }
   
    override def hashCode:Int = {
        pr(pr(pr(pr(pr(pr(pr(h(id),h(activity)),h(entity)),h(location)),h(label)),h(typex)),h(role)),h(other))

    }


}


class WasInvalidatedBy extends org.openprovenance.prov.model.WasInvalidatedBy with Identifiable with HasLocation with HasLabel with HasType with HasOther with HasRole with Hashable {
  
    @BeanProperty
    var activity: org.openprovenance.prov.model.QualifiedName=null
    
    @BeanProperty
    var entity: org.openprovenance.prov.model.QualifiedName=null
    
    @BeanProperty
    var time: javax.xml.datatype.XMLGregorianCalendar=null
    
    @BeanProperty
    val kind: Kind=PROV_INVALIDATION
    
    def canEqual(a: Any) = a.isInstanceOf[WasInvalidatedBy]

    override def equals(that: Any): Boolean =
    that match {
      case that: WasInvalidatedBy => that.canEqual(this) && 
                                     this.id == that.id && 
                                     this.activity == that.activity && 
                                     this.entity == that.entity && 
                                     this.time == that.time &&
                                     this.location == that.location &&  
                                     this.label == that.label && 
                                     this.typex == that.typex && 
                                     this.role == that.role && 
                                     this.other == that.other                   
      case _ => false
    }
    
    override def hashCode:Int = {
        pr(pr(pr(pr(pr(pr(pr(h(id),h(activity)),h(entity)),h(location)),h(label)),h(typex)),h(role)),h(other))

    }


}

class WasStartedBy extends org.openprovenance.prov.model.WasStartedBy with Identifiable with HasLocation with HasLabel with HasType with HasOther with HasRole with Hashable {
  
    @BeanProperty
    var activity: org.openprovenance.prov.model.QualifiedName=null
    
    @BeanProperty
    var trigger: org.openprovenance.prov.model.QualifiedName=null
    
    @BeanProperty
    var starter: org.openprovenance.prov.model.QualifiedName=null
    
    @BeanProperty
    var time: javax.xml.datatype.XMLGregorianCalendar=null
    
    @BeanProperty
    val kind: Kind=PROV_START
    
    
        
    
    def canEqual(a: Any) = a.isInstanceOf[WasStartedBy]

    override def equals(that: Any): Boolean =
    that match {
      case that: WasStartedBy => that.canEqual(this) && 
                           this.id == that.id && 
                           this.activity == that.activity && 
                           this.trigger == that.trigger && 
                           this.starter == that.starter &&  
                           this.time == that.time &&  
                           this.location == that.location &&  
                           this.label == that.label && 
                           this.typex == that.typex && 
                           this.other == that.other                   
      case _ => false
    }
   
    override def hashCode:Int = {
        pr(pr(pr(pr(pr(pr(pr(pr(h(id),h(activity)),h(trigger)),h(starter)),h(time)),h(location)), h(label)),h(typex)),h(other))

    }

}

class WasEndedBy extends org.openprovenance.prov.model.WasEndedBy with Identifiable with HasLocation with HasLabel with HasType with HasOther with HasRole with Hashable {
  
    @BeanProperty
    var activity: org.openprovenance.prov.model.QualifiedName=null
    
    @BeanProperty
    var trigger: org.openprovenance.prov.model.QualifiedName=null
    
    @BeanProperty
    var ender: org.openprovenance.prov.model.QualifiedName=null
    
    @BeanProperty
    var time: javax.xml.datatype.XMLGregorianCalendar=null
    
    @BeanProperty
    val kind: Kind=PROV_END
    
    
        
    
    def canEqual(a: Any) = a.isInstanceOf[WasEndedBy]

    override def equals(that: Any): Boolean =
    that match {
      case that: WasEndedBy => that.canEqual(this) && 
                           this.id == that.id && 
                           this.activity == that.activity && 
                           this.trigger == that.trigger && 
                           this.ender == that.ender &&  
                           this.time == that.time &&  
                           this.location == that.location &&  
                           this.label == that.label && 
                           this.typex == that.typex && 
                           this.other == that.other                   
      case _ => false
    }
   
    override def hashCode:Int = {
        pr(pr(pr(pr(pr(pr(pr(pr(h(id),h(activity)),h(trigger)),h(ender)),h(time)),h(location)), h(label)),h(typex)),h(other))

    }


}

class WasDerivedFrom extends org.openprovenance.prov.model.WasDerivedFrom with Identifiable  with HasLabel with HasType with HasOther with Hashable {
  
    @BeanProperty
    var generatedEntity: org.openprovenance.prov.model.QualifiedName=null
    
    @BeanProperty
    var usedEntity: org.openprovenance.prov.model.QualifiedName=null
    
    @BeanProperty
    var activity: org.openprovenance.prov.model.QualifiedName=null
    
    @BeanProperty
    var generation: org.openprovenance.prov.model.QualifiedName=null
    
    @BeanProperty
    var usage: org.openprovenance.prov.model.QualifiedName=null

    @BeanProperty
    val kind: Kind=PROV_DERIVATION
    
    
    def canEqual(a: Any) = a.isInstanceOf[WasDerivedFrom]

    override def equals(that: Any): Boolean =
    that match {
      case that: WasDerivedFrom => that.canEqual(this) && 
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
    
    override def hashCode:Int = {
              pr(pr(pr(pr(pr(pr(pr(pr(h(id),h(generatedEntity)),h(usedEntity)),h(activity)),h(usage)),h(generation)),h(label)),h(typex)),h(other))
    }

}



class SpecializationOf extends org.openprovenance.prov.model.SpecializationOf with QualifiedSpecializationOf with Identifiable with HasLabel with HasType with HasOther with Hashable {
  
    @BeanProperty
    var generalEntity: org.openprovenance.prov.model.QualifiedName=null
    
    @BeanProperty
    var specificEntity: org.openprovenance.prov.model.QualifiedName=null
    
    @BeanProperty
    val kind: Kind=PROV_SPECIALIZATION
    
    def canEqual(a: Any) = a.isInstanceOf[SpecializationOf]

    override def equals(that: Any): Boolean =
    that match {
      case that: SpecializationOf => that.canEqual(this) && 
                           this.id == that.id && 
                           this.generalEntity == that.generalEntity && 
                           this.specificEntity == that.specificEntity && 
                           this.label == that.label && 
                           this.typex == that.typex && 
                           this.other == that.other                   
      case _ => false
    }
    
    override def hashCode:Int = {
        pr(pr(pr(pr(pr(h(id),h(generalEntity)),h(specificEntity)),h(label)),h(typex)),h(other))

    }
    override def isUnqualified: Boolean = (id==null) && (label.isEmpty) && (typex.isEmpty) && (other.isEmpty)

}

class AlternateOf extends org.openprovenance.prov.model.AlternateOf with QualifiedAlternateOf with Identifiable with HasLabel with HasType with HasOther with Hashable {
  
    @BeanProperty
    var alternate1: org.openprovenance.prov.model.QualifiedName=null
    
    @BeanProperty
    var alternate2: org.openprovenance.prov.model.QualifiedName=null
    
    @BeanProperty
    val kind: Kind=PROV_ALTERNATE
    
    def canEqual(a: Any) = a.isInstanceOf[AlternateOf]

    override def equals(that: Any): Boolean =
    that match {
      case that: AlternateOf => that.canEqual(this) && 
                           this.id == that.id && 
                           this.alternate1 == that.alternate1 && 
                           this.alternate2 == that.alternate2 && 
                           this.label == that.label && 
                           this.typex == that.typex && 
                           this.other == that.other                   
      case _ => false
    }
    
    override def hashCode:Int = {
        pr(pr(pr(pr(pr(h(id),h(alternate1)),h(alternate2)),h(label)),h(typex)),h(other))

    }

    override def isUnqualified: Boolean = (id==null) && (label.isEmpty) && (typex.isEmpty) && (other.isEmpty)


}


class HadMember extends org.openprovenance.prov.model.HadMember with QualifiedHadMember with Identifiable  with HasLabel with HasType with HasOther with Hashable {
  
    @BeanProperty
    var collection: org.openprovenance.prov.model.QualifiedName=null
    
    @BeanProperty
    var entity: java.util.List[org.openprovenance.prov.model.QualifiedName]=new java.util.LinkedList()
    
    @BeanProperty
    val kind: Kind=PROV_MEMBERSHIP
    
    
    def canEqual(a: Any) = a.isInstanceOf[HadMember]

    override def equals(that: Any): Boolean =
    that match {
      case that: HadMember => that.canEqual(this) && 
                           this.id == that.id && 
                           this.collection == that.collection && 
                           this.entity == that.entity && 
                           this.label == that.label && 
                           this.typex == that.typex && 
                           this.other == that.other                   
      case _ => false
    }
    
    override def hashCode:Int = {
        pr(pr(pr(pr(pr(h(id),h(collection)),h(entity)),h(label)),h(typex)),h(other))
    }
    override def isUnqualified: Boolean = (id==null) && (label.isEmpty) && (typex.isEmpty) && (other.isEmpty)


}



class WasInfluencedBy extends org.openprovenance.prov.model.WasInfluencedBy with Identifiable with HasLocation with HasLabel with HasType with HasOther with Hashable {
  
    @BeanProperty
    var influencee: org.openprovenance.prov.model.QualifiedName=null
    
    @BeanProperty
    var influencer: org.openprovenance.prov.model.QualifiedName=null
    
    @BeanProperty
    val kind: Kind=PROV_INFLUENCE
    
    
    def canEqual(a: Any) = a.isInstanceOf[WasInfluencedBy]

    override def equals(that: Any): Boolean =
    that match {
      case that: WasInfluencedBy => that.canEqual(this) && 
                           this.id == that.id && 
                           this.influencee == that.influencee && 
                           this.influencer == that.influencer && 
                           this.label == that.label && 
                           this.typex == that.typex && 
                           this.other == that.other                   
      case _ => false
    }
    
    override def hashCode:Int = {
        pr(pr(pr(pr(pr(h(id),h(influencee)),h(influencer)),h(label)),h(typex)),h(other))

    }

}



class WasInformedBy extends org.openprovenance.prov.model.WasInformedBy with Identifiable with HasLabel with HasType  with HasOther with Hashable {
  
    @BeanProperty
    var informed: org.openprovenance.prov.model.QualifiedName=null
    
    @BeanProperty
    var informant: org.openprovenance.prov.model.QualifiedName=null
    
    @BeanProperty
    val kind: Kind=PROV_COMMUNICATION
    
    def canEqual(a: Any) = a.isInstanceOf[WasInformedBy]

    override def equals(that: Any): Boolean =
    that match {
      case that: WasInformedBy => that.canEqual(this) && 
                           this.id == that.id && 
                           this.informed == that.informed && 
                           this.informant == that.informant && 
                           this.label == that.label && 
                           this.typex == that.typex && 
                           this.other == that.other                   
      case _ => false
    }
  
    override def hashCode:Int = {
        pr(pr(pr(pr(pr(h(id),h(informed)),h(informant)),h(label)),h(typex)),h(other))

    }

}




class WasAttributedTo extends org.openprovenance.prov.model.WasAttributedTo with Identifiable with HasLabel with HasType with HasOther with Hashable {
  
    @BeanProperty
    var entity: org.openprovenance.prov.model.QualifiedName=null
    
    @BeanProperty
    var agent: org.openprovenance.prov.model.QualifiedName=null
    
    @BeanProperty
    val kind: Kind=PROV_ATTRIBUTION
    
    def canEqual(a: Any) = a.isInstanceOf[WasAttributedTo]

    override def equals(that: Any): Boolean =
    that match {
      case that: WasAttributedTo => that.canEqual(this) && 
                           this.id == that.id && 
                           this.entity == that.entity && 
                           this.agent == that.agent && 
                           this.label == that.label && 
                           this.typex == that.typex && 
                           this.other == that.other                   
      case _ => false
    }
   
    override def hashCode:Int = {
        pr(pr(pr(pr(pr(h(id),h(entity)),h(agent)),h(label)),h(typex)),h(other))

    }

}



class WasAssociatedWith extends org.openprovenance.prov.model.WasAssociatedWith with Identifiable with HasLabel with HasType with HasOther with HasRole with Hashable {
  
    @BeanProperty
    var activity: org.openprovenance.prov.model.QualifiedName=null
    
    @BeanProperty
    var agent: org.openprovenance.prov.model.QualifiedName=null
    
    @BeanProperty
    var plan: org.openprovenance.prov.model.QualifiedName=null
    
    @BeanProperty
    val kind: Kind=PROV_ASSOCIATION
    
    
         
    def canEqual(a: Any) = a.isInstanceOf[WasAssociatedWith]

    override def equals(that: Any): Boolean =
    that match {
      case that: WasAssociatedWith => that.canEqual(this) && 
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
   
    override def hashCode:Int = {
        pr(pr(pr(pr(pr(pr(pr(h(id),h(activity)),h(agent)),h(plan)),h(label)),h(typex)),h(role)),h(other))

    }
}



class ActedOnBehalfOf extends org.openprovenance.prov.model.ActedOnBehalfOf with Identifiable  with HasLabel with HasType with HasOther with Hashable {
  
    @BeanProperty
    var delegate: org.openprovenance.prov.model.QualifiedName=null
    
    @BeanProperty
    var responsible: org.openprovenance.prov.model.QualifiedName=null
    
    @BeanProperty
    var activity: org.openprovenance.prov.model.QualifiedName=null
    
    @BeanProperty
    val kind: Kind=PROV_DELEGATION
    
        
    def canEqual(a: Any) = a.isInstanceOf[ActedOnBehalfOf]

    override def equals(that: Any): Boolean =
    that match {
      case that: ActedOnBehalfOf => that.canEqual(this) && 
                                     this.id == that.id && 
                                     this.delegate == that.delegate && 
                                     this.responsible == that.responsible && 
                                     this.activity == that.activity &&
                                     this.label == that.label && 
                                     this.typex == that.typex && 
                                     this.other == that.other                   
      case _ => false
    }

    override def hashCode:Int = {
        pr(pr(pr(pr(pr(pr(h(id),h(delegate)),h(responsible)),h(activity)),h(label)),h(typex)),h(other))

    }
    
}


class Bundle extends org.openprovenance.prov.model.Bundle with StatementOrBundle with Identifiable with Hashable {     

    @BeanProperty
    var namespace: Namespace=null
    
    @BeanProperty
    val kind: Kind=PROV_BUNDLE
    
    @BeanProperty
    var statement: java.util.List[Statement]=new java.util.LinkedList()
    
    def canEqual(a: Any) = a.isInstanceOf[Bundle]

    override def equals(that: Any): Boolean =
    that match {
      case that: Bundle => that.canEqual(this) && 
                                    (this.statement).asScala.toSet.equals( (that.statement).asScala.toSet)
      case _ => false
    }

    override def hashCode:Int = {
        h(statement)
    }
           
}


class Document extends org.openprovenance.prov.model.Document with Hashable {     

    @BeanProperty
    var namespace: Namespace=null
    
    @BeanProperty
    var statementOrBundle: java.util.List[StatementOrBundle]=new java.util.LinkedList()
      
    def canEqual(a: Any) = a.isInstanceOf[Document]

    override def equals(that: Any): Boolean =
    that match {
      case that: Document => that.canEqual(this) && 
                                     this.statementOrBundle == that.statementOrBundle 
      case _ => false
    }
  
    override def hashCode:Int = {
        h(statementOrBundle)
    }
           
}

class TypedValue extends org.openprovenance.prov.model.TypedValue {
	def convertValueToObject(x$1: org.openprovenance.prov.model.ValueConverter): Object = ???
  def getConvertedValue(): Object = ???
  
  var typ:  org.openprovenance.prov.model.QualifiedName=null
  def getType(): org.openprovenance.prov.model.QualifiedName = { typ }
  def setType(t: org.openprovenance.prov.model.QualifiedName): Unit = { typ=t }
  
  var value: Object=null
  def getValue(): Object = value
  def setValue(s: String): Unit = { value=s }
  def setValue(qn: org.openprovenance.prov.model.QualifiedName): Unit = { value=qn }
  def setValue(s: org.openprovenance.prov.model.LangString): Unit = { value=s }
  
  def setValueFromObject(value: Any): Unit = {
    value match {
      case qn:org.openprovenance.prov.model.QualifiedName => setValue(qn)
      case s:String => setValue(s)
      case s:org.openprovenance.prov.model.LangString => setValue(s)
    }
  }
}

trait ToNotationString {
    def getElementName(): org.openprovenance.prov.model.QualifiedName
    def getValue(): Object
    def getType(): org.openprovenance.prov.model.QualifiedName
    def toNotationString(): String = {
            val s1=org.openprovenance.prov.model.DOMProcessing.qualifiedNameToString(getElementName())
                    s1 ++ " = " ++ org.openprovenance.prov.model.ProvUtilities.valueToNotationString(getValue(), getType())
    }
    
}

object Type {
      val myName=(new ProvFactory).getName.PROV_TYPE
}

class Type() extends TypedValue with org.openprovenance.prov.model.Type  with org.openprovenance.prov.model.Attribute with ToNotationString with Hashable {
    import Type.myName
    
    @BeanProperty
    var elementName : org.openprovenance.prov.model.QualifiedName = myName 
    
 
    def this(value: Any, 
             atyp: org.openprovenance.prov.model.QualifiedName) {
      this()
      typ=atyp;
      setValueFromObject(value)
    }
  
  
    def getAttributeKind(x$1: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Attribute.AttributeKind = ???
  
    def getKind(): org.openprovenance.prov.model.Attribute.AttributeKind = org.openprovenance.prov.model.Attribute.AttributeKind.PROV_TYPE
	
    def getQualifiedName(x$1: org.openprovenance.prov.model.Attribute.AttributeKind): org.openprovenance.prov.model.QualifiedName = ???
 
    def canEqual(a: Any) = a.isInstanceOf[Type]

    
    override def equals(that: Any): Boolean =
    that match {
      case that: Type => that.canEqual(this) && 
                           this.elementName == that.elementName && 
                           this.typ == that.typ && 
                           this.value == that.value                  
      case _ => false
    }
    
    override def hashCode:Int = {
      pr(pr(h(elementName),h(typ)),h(value))
    }
 
}

object Role {
      val myName=(new ProvFactory).getName.PROV_ROLE
}
class Role() extends TypedValue with org.openprovenance.prov.model.Role  with org.openprovenance.prov.model.Attribute with ToNotationString with Hashable {
    import Role.myName
    @BeanProperty
    var elementName : org.openprovenance.prov.model.QualifiedName = myName 
    
 
    def this(value: Any, 
             atyp: org.openprovenance.prov.model.QualifiedName) {
      this()
      typ=atyp;
      setValueFromObject(value)
    }
  
  
    def getAttributeKind(x$1: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Attribute.AttributeKind = ???
  
    def getKind(): org.openprovenance.prov.model.Attribute.AttributeKind = org.openprovenance.prov.model.Attribute.AttributeKind.PROV_ROLE
	
    def getQualifiedName(x$1: org.openprovenance.prov.model.Attribute.AttributeKind): org.openprovenance.prov.model.QualifiedName = ???
    
    def canEqual(a: Any) = a.isInstanceOf[Role]

    override def equals(that: Any): Boolean =
    that match {
      case that: Role => that.canEqual(this) && 
                           this.elementName == that.elementName && 
                           this.typ == that.typ && 
                           this.value == that.value                  
      case _ => false
    }

    override def hashCode:Int = {
      pr(pr(h(elementName),h(typ)),h(value))
    }
    
}
object Location {
      val myName=(new ProvFactory).getName.PROV_LOCATION  //TODO: move this to the ProvFactory companion!
}
class Location() extends TypedValue with org.openprovenance.prov.model.Location  with org.openprovenance.prov.model.Attribute with ToNotationString with Hashable {
    import Location.myName
    @BeanProperty
    var elementName : org.openprovenance.prov.model.QualifiedName = myName 
    
 
    def this(value: Any, 
             atyp: org.openprovenance.prov.model.QualifiedName) {
      this()
      typ=atyp;
      setValueFromObject(value)
    }
  
  
    def getAttributeKind(x$1: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Attribute.AttributeKind = ???
  
    def getKind(): org.openprovenance.prov.model.Attribute.AttributeKind = org.openprovenance.prov.model.Attribute.AttributeKind.PROV_LOCATION
	
    def getQualifiedName(x$1: org.openprovenance.prov.model.Attribute.AttributeKind): org.openprovenance.prov.model.QualifiedName = ???
    
    def canEqual(a: Any) = a.isInstanceOf[Location]

    override def equals(that: Any): Boolean =
    that match {
      case that: Location => that.canEqual(this) && 
                           this.elementName == that.elementName && 
                           this.typ == that.typ && 
                           this.value == that.value                  
      case _ => false
    }
    override def hashCode:Int = {
      pr(pr(h(elementName),h(typ)),h(value))
    }  
}

object Value {
      val myName=(new ProvFactory).getName.PROV_LOCATION  //TODO: move this to the ProvFactory companion!
}

class Value() extends TypedValue with org.openprovenance.prov.model.Value  with org.openprovenance.prov.model.Attribute with ToNotationString with Hashable {
    import Value.myName
    @BeanProperty
    var elementName : org.openprovenance.prov.model.QualifiedName = myName 
    
 
    def this(value: Any, 
             atyp: org.openprovenance.prov.model.QualifiedName) {
      this()
      typ=atyp;
      setValueFromObject(value)
    }
  
  
    def getAttributeKind(x$1: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Attribute.AttributeKind = ???
  
    def getKind(): org.openprovenance.prov.model.Attribute.AttributeKind = org.openprovenance.prov.model.Attribute.AttributeKind.PROV_VALUE
	
    def getQualifiedName(x$1: org.openprovenance.prov.model.Attribute.AttributeKind): org.openprovenance.prov.model.QualifiedName = ???
    
    
    def canEqual(a: Any) = a.isInstanceOf[Value]

    override def equals(that: Any): Boolean =
    that match {
      case that: Value => that.canEqual(this) && 
                           this.elementName == that.elementName && 
                           this.typ == that.typ && 
                           this.value == that.value                  
      case _ => false
    }
    override def hashCode:Int = {
      pr(pr(h(elementName),h(typ)),h(value))
    }      
}

object Label {
      val myName=(new ProvFactory).getName.PROV_LABEL //TODO: move this to the ProvFactory companion!
}

class Label() extends TypedValue with org.openprovenance.prov.model.Label  with org.openprovenance.prov.model.Attribute with ToNotationString with Hashable {
    import Label.myName
    @BeanProperty
    var elementName : org.openprovenance.prov.model.QualifiedName = myName 
    
 
    def this(value: Any, 
             atyp: org.openprovenance.prov.model.QualifiedName) {
      this()
      typ=atyp;
      setValueFromObject(value)
    }
  
  
    def getAttributeKind(x$1: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Attribute.AttributeKind = ???
  
    def getKind(): org.openprovenance.prov.model.Attribute.AttributeKind = org.openprovenance.prov.model.Attribute.AttributeKind.PROV_VALUE
	
    def getQualifiedName(x$1: org.openprovenance.prov.model.Attribute.AttributeKind): org.openprovenance.prov.model.QualifiedName = ???
    
    
    def canEqual(a: Any) = a.isInstanceOf[Label]

    override def equals(that: Any): Boolean =
    that match {
      case that: Label => that.canEqual(this) && 
                           this.elementName == that.elementName && 
                           this.typ == that.typ && 
                           this.value == that.value                  
      case _ => false
    }
    override def hashCode:Int = {
      pr(pr(h(elementName),h(typ)),h(value))
    }      
}


class Other() extends TypedValue with org.openprovenance.prov.model.Other  with org.openprovenance.prov.model.Attribute with ToNotationString with Hashable {
  
    @BeanProperty
    var elementName : org.openprovenance.prov.model.QualifiedName = null 
 
    def this(qualifiedName: org.openprovenance.prov.model.QualifiedName,
             value: Any, 
             atyp: org.openprovenance.prov.model.QualifiedName) {
      this()
      elementName=qualifiedName
      typ=atyp;
      setValueFromObject(value)
    }
  
  
    def getAttributeKind(x$1: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Attribute.AttributeKind = ???
  
    def getKind(): org.openprovenance.prov.model.Attribute.AttributeKind = org.openprovenance.prov.model.Attribute.AttributeKind.OTHER
	
    def getQualifiedName(x$1: org.openprovenance.prov.model.Attribute.AttributeKind): org.openprovenance.prov.model.QualifiedName = ???
 
    def canEqual(a: Any) = a.isInstanceOf[Other]

  
    override def equals(that: Any): Boolean =
    that match {
      case that: Other => that.canEqual(this) && 
                           this.elementName == that.elementName && 
                           this.typ == that.typ && 
                           this.value == that.value                  
      case _ => false
    }

    override def hashCode:Int = {
      pr(pr(h(elementName),h(typ)),h(value))
    }      
    
}


class LangString () extends org.openprovenance.prov.model.LangString with Hashable{
  
	   @BeanProperty var value: String=null
	   @BeanProperty var lang: String=null
  
    def canEqual(a: Any) = a.isInstanceOf[LangString]

    override def equals(that: Any): Boolean =
    that match {
      case that: LangString => that.canEqual(this) && 
                           this.lang == that.lang && 
                           this.value == that.value                  
      case _ => false
    }
    
    override val hashCode:Int = pr(h(lang),h(value))
  
}

class QualifiedName () extends org.openprovenance.prov.model.QualifiedName {    
    @BeanProperty
    var prefix: String=null
    @BeanProperty
    var localPart: String=null
    @BeanProperty
    var namespaceURI: String=null
    
    def this(namespace: String, local: String, pref: String) {
        this()
        namespaceURI=namespace
        localPart=local
        prefix=pref
    }

    var uri: String=null
    override def getUri() = {
        getNamespaceURI() + getUnescapedLocalPart();
    } 
    override def setUri(discard: String) = {}

    val qnU=new QualifiedNameUtils;
    
    
    def getUnescapedLocalPart () = { qnU.unescapeProvLocalName(localPart)}
   
    override def toString() ={
      //"<" + prefix + ":" + namespaceURI + ":" + localPart + ">"
       prefix + ":" + localPart
    }
    
    override def toQName() : javax.xml.namespace.QName= {
            val escapedLocal=qnU.escapeToXsdLocalName(getUnescapedLocalPart())
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
    
    override def equals (other: Any) : Boolean = other match { 
        case other: QualifiedName => this.namespaceURI == other.namespaceURI && this.localPart == other.localPart
        case _ => false
    }
    
    override def hashCode = this.namespaceURI.hashCode ^ this.localPart.hashCode()


}

class ObjectFactory extends org.openprovenance.prov.model.ObjectFactory {
  override def createEntity () = new Entity
  override def createActivity () = new Activity
  override def createAgent () = new Agent
  override def createDocument () = new Document
  override def createNamedBundle () = new Bundle
  override def createWasGeneratedBy () = new WasGeneratedBy
  override def createWasInvalidatedBy () = new WasInvalidatedBy
  override def createUsed() = new     Used
  override def createWasAssociatedWith() = new     WasAssociatedWith
  override def createWasAttributedTo() = new     WasAttributedTo
  override def createWasDerivedFrom() = new     WasDerivedFrom
  override def createWasEndedBy() = new     WasEndedBy
  override def createWasInfluencedBy() = new     WasInfluencedBy
  override def createWasInformedBy() = new     WasInformedBy
  override def createWasStartedBy() = new     WasStartedBy
  override def createSpecializationOf() = new     SpecializationOf
  override def createActedOnBehalfOf() = new     ActedOnBehalfOf
  override def createHadMember() = new     HadMember   
  override def createAlternateOf() = new     AlternateOf

  override def createQualifiedSpecializationOf() = new SpecializationOf
  override def createQualifiedHadMember() = new HadMember   
  override def createQualifiedAlternateOf() = new AlternateOf

  override def createLocation() = new Location
  override def createOther():org.openprovenance.prov.model.Other = {
   new Other
  }
  override def createRole() = new Role
  override def createType() = new Type
  
  override def createTypedValue() = throw new UnsupportedOperationException
  override def createValue() = new Value
  override def createInternationalizedString() = new LangString

  
  override def createDerivedByInsertionFrom() = throw new UnsupportedOperationException
  override def createDerivedByRemovalFrom() = throw new UnsupportedOperationException
  override def createDictionaryMembership() = throw new UnsupportedOperationException
  override def createEntry() = throw new UnsupportedOperationException
  override def createKey() = throw new UnsupportedOperationException
  override def createMentionOf() = throw new UnsupportedOperationException


}
 
class ProvFactory extends org.openprovenance.prov.model.ProvFactory (new ObjectFactory) {
 
  //println("In ProvFactory Constructor")
  
  override def getSerializer() = throw new UnsupportedOperationException
  override def newQualifiedName(x1: String, x2:String, x3: String, x4:org.openprovenance.prov.model.ProvUtilities.BuildFlag) = throw new UnsupportedOperationException
  override def newAttribute(kind: org.openprovenance.prov.model.Attribute.AttributeKind,value: Any,typ: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Attribute = {
    kind match {
      case AttributeKind.PROV_TYPE => new Type(value,typ)
      case AttributeKind.PROV_ROLE => new Role(value,typ)
      case AttributeKind.PROV_LOCATION => new Location(value,typ)
      case AttributeKind.PROV_VALUE => new Value(value,typ)
      case AttributeKind.PROV_LABEL => new Label(value,typ)
      case AttributeKind.PROV_KEY => throw new UnsupportedOperationException //TODO: need to implement
      case AttributeKind.OTHER =>throw new UnsupportedOperationException // error, call the other newAttributed function
    }
  }
  val prov_type: model.QualifiedName =getName.PROV_TYPE
  val prov_role: model.QualifiedName =getName.PROV_ROLE
  val prov_location: model.QualifiedName =getName.PROV_LOCATION
  val prov_value: model.QualifiedName =getName.PROV_VALUE
  val prov_key: model.QualifiedName =getName.PROV_KEY
  val prov_label: model.QualifiedName =getName.PROV_LABEL
  
  def attributeKind(qualifiedName: org.openprovenance.prov.model.QualifiedName): AttributeKind = {
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
  
  override def newAttribute(qualifiedName: org.openprovenance.prov.model.QualifiedName,
		                        value: Any, 
		                        typ: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Attribute = {
    val kind=attributeKind(qualifiedName)
    kind match {
      case AttributeKind.OTHER => new Other(qualifiedName,value,typ)
      case _ => newAttribute(kind,value,typ)
    }
      
  }
  /** Deep copy of a document with the current factory */
  override def newDocument(doc: org.openprovenance.prov.model.Document): org.openprovenance.prov.model.Document = {
    val coll1=doc.getStatementOrBundle.asScala
                .toList.filter { x => x.isInstanceOf[org.openprovenance.prov.model.Statement] }
                .map(x => newStatement(x.asInstanceOf[org.openprovenance.prov.model.Statement]))
    val coll2=doc.getStatementOrBundle.asScala
                .toList.filter { x => x.isInstanceOf[org.openprovenance.prov.model.Bundle] }
                .map(_.asInstanceOf[org.openprovenance.prov.model.Bundle])
                .map(b => newNamedBundle(b.getId(), b.getNamespace(), b.getStatement()))

    newDocument(doc.getNamespace, coll1.asJava, coll2.asJava)
  }
  

  
  override def newKey(x$1: Any,x$2: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Key = throw new UnsupportedOperationException
 
  override def newQualifiedName(namespace: String, local: String, prefix: String): org.openprovenance.prov.model.QualifiedName = {
    new QualifiedName(namespace,local,prefix)
  }
}

object Entity {
  def entity(id: QualifiedName):Entity = {
    val e=new Entity
    e.id=id
    e
  }  
  def activity(id: QualifiedName)(implicit startTime: XMLGregorianCalendar=null, endTime: XMLGregorianCalendar=null)= {
    val act=new Activity
    act.id=id
    act.startTime=startTime
    act.endTime=endTime
    act
  }
  
  def test () = {
    val  e=entity(null)
    val  a1=activity(null)
    val  a2=activity(null)(null)
    val  a3=activity(null)(null,null)
  }
}
