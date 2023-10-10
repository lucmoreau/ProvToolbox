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
import org.openprovenance.prov.model.{Attribute, Namespace, QualifiedNameUtils, Statement, StatementOrBundle}
import org.openprovenance.prov.model.extension.QualifiedSpecializationOf
import org.openprovenance.prov.model.extension.QualifiedAlternateOf
import org.openprovenance.prov.model.extension.QualifiedHadMember
import org.openprovenance.prov.model.exception.QualifiedNameException

import javax.xml.datatype.XMLGregorianCalendar
import org.openprovenance.prov.model
import org.openprovenance.prov.model.Attribute.AttributeKind
import org.openprovenance.prov.scala.mutable.ProvFactory.splitrec

import java.util
import java.util.stream.Collectors


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
    def setOther(others: java.util.List[org.openprovenance.prov.model.Other]): Unit = {
      other=others
    }
    def getOther(): util.List[model.Other] = {
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
    def getType(): util.List[model.Type] = { typex }
    
}

trait Identifiable {
    @BeanProperty
    var id: org.openprovenance.prov.model.QualifiedName=null
}

trait Hashable {
      
      @inline final def h(x: AnyRef): Int = {
          if (x==null) 0 else x.hashCode
      }
      
      @inline final def pr(v0: Int,v1:Int): Int = {
          prime*v0+v1
      }
    
      final private val prime=37
}

abstract class Term
class Entity extends  org.openprovenance.prov.model.Entity with Identifiable with HasLocation with HasValue with HasLabel with HasType with HasOther with Hashable {     

    @BeanProperty
    val kind: Kind=PROV_ENTITY
    
    def canEqual(a: Any): Boolean = a.isInstanceOf[Entity]

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
    
    def canEqual(a: Any): Boolean = a.isInstanceOf[Agent]

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
    
    def canEqual(a: Any): Boolean = a.isInstanceOf[Used]

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
    
    
        
    
    def canEqual(a: Any): Boolean = a.isInstanceOf[WasStartedBy]

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
    
    
        
    
    def canEqual(a: Any): Boolean = a.isInstanceOf[WasEndedBy]

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
    
    
    def canEqual(a: Any): Boolean = a.isInstanceOf[WasDerivedFrom]

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
    
    def canEqual(a: Any): Boolean = a.isInstanceOf[SpecializationOf]

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
    
    def canEqual(a: Any): Boolean = a.isInstanceOf[AlternateOf]

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
    
    
    def canEqual(a: Any): Boolean = a.isInstanceOf[HadMember]

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
    
    
    def canEqual(a: Any): Boolean = a.isInstanceOf[WasInfluencedBy]

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
    
    def canEqual(a: Any): Boolean = a.isInstanceOf[WasInformedBy]

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
    
    def canEqual(a: Any): Boolean = a.isInstanceOf[WasAttributedTo]

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
    
    
         
    def canEqual(a: Any): Boolean = a.isInstanceOf[WasAssociatedWith]

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
    
        
    def canEqual(a: Any): Boolean = a.isInstanceOf[ActedOnBehalfOf]

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
    
    def canEqual(a: Any): Boolean = a.isInstanceOf[Bundle]

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
      
    def canEqual(a: Any): Boolean = a.isInstanceOf[Document]

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
      val myName: model.QualifiedName =(new ProvFactory).getName.PROV_TYPE
}

class Type() extends TypedValue with org.openprovenance.prov.model.Type  with org.openprovenance.prov.model.Attribute with ToNotationString with Hashable {
    import Type.myName
    
    @BeanProperty
    var elementName : org.openprovenance.prov.model.QualifiedName = myName 
    
 
    def this(value: Any, 
             atyp: org.openprovenance.prov.model.QualifiedName) = {
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
      val myName: model.QualifiedName =(new ProvFactory).getName.PROV_ROLE
}
class Role() extends TypedValue with org.openprovenance.prov.model.Role  with org.openprovenance.prov.model.Attribute with ToNotationString with Hashable {
    import Role.myName
    @BeanProperty
    var elementName : org.openprovenance.prov.model.QualifiedName = myName 
    
 
    def this(value: Any, 
             atyp: org.openprovenance.prov.model.QualifiedName) = {
      this()
      typ=atyp;
      setValueFromObject(value)
    }
  
  
    def getAttributeKind(x$1: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Attribute.AttributeKind = ???
  
    def getKind(): org.openprovenance.prov.model.Attribute.AttributeKind = org.openprovenance.prov.model.Attribute.AttributeKind.PROV_ROLE
	
    def getQualifiedName(x$1: org.openprovenance.prov.model.Attribute.AttributeKind): org.openprovenance.prov.model.QualifiedName = ???
    
    def canEqual(a: Any): Boolean = a.isInstanceOf[Role]

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
      val myName: model.QualifiedName =(new ProvFactory).getName.PROV_LOCATION  //TODO: move this to the ProvFactory companion!
}
class Location() extends TypedValue with org.openprovenance.prov.model.Location  with org.openprovenance.prov.model.Attribute with ToNotationString with Hashable {
    import Location.myName
    @BeanProperty
    var elementName : org.openprovenance.prov.model.QualifiedName = myName 
    
 
    def this(value: Any, 
             atyp: org.openprovenance.prov.model.QualifiedName) = {
      this()
      typ=atyp;
      setValueFromObject(value)
    }
  
  
    def getAttributeKind(x$1: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Attribute.AttributeKind = ???
  
    def getKind(): org.openprovenance.prov.model.Attribute.AttributeKind = org.openprovenance.prov.model.Attribute.AttributeKind.PROV_LOCATION
	
    def getQualifiedName(x$1: org.openprovenance.prov.model.Attribute.AttributeKind): org.openprovenance.prov.model.QualifiedName = ???
    
    def canEqual(a: Any): Boolean = a.isInstanceOf[Location]

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
      val myName: model.QualifiedName =(new ProvFactory).getName.PROV_LOCATION  //TODO: move this to the ProvFactory companion!
}

class Value() extends TypedValue with org.openprovenance.prov.model.Value  with org.openprovenance.prov.model.Attribute with ToNotationString with Hashable {
    import Value.myName
    @BeanProperty
    var elementName : org.openprovenance.prov.model.QualifiedName = myName 
    
 
    def this(value: Any, 
             atyp: org.openprovenance.prov.model.QualifiedName) = {
      this()
      typ=atyp;
      setValueFromObject(value)
    }
  
  
    def getAttributeKind(x$1: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Attribute.AttributeKind = ???
  
    def getKind(): org.openprovenance.prov.model.Attribute.AttributeKind = org.openprovenance.prov.model.Attribute.AttributeKind.PROV_VALUE
	
    def getQualifiedName(x$1: org.openprovenance.prov.model.Attribute.AttributeKind): org.openprovenance.prov.model.QualifiedName = ???
    
    
    def canEqual(a: Any): Boolean = a.isInstanceOf[Value]

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
      val myName: model.QualifiedName =(new ProvFactory).getName.PROV_LABEL //TODO: move this to the ProvFactory companion!
}

class Label() extends TypedValue with org.openprovenance.prov.model.Label  with org.openprovenance.prov.model.Attribute with ToNotationString with Hashable {
    import Label.myName
    @BeanProperty
    var elementName : org.openprovenance.prov.model.QualifiedName = myName 
    
 
    def this(value: Any, 
             atyp: org.openprovenance.prov.model.QualifiedName) = {
      this()
      typ=atyp;
      setValueFromObject(value)
    }
  
  
    def getAttributeKind(x$1: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Attribute.AttributeKind = ???
  
    def getKind(): org.openprovenance.prov.model.Attribute.AttributeKind = org.openprovenance.prov.model.Attribute.AttributeKind.PROV_VALUE
	
    def getQualifiedName(x$1: org.openprovenance.prov.model.Attribute.AttributeKind): org.openprovenance.prov.model.QualifiedName = ???
    
    
    def canEqual(a: Any): Boolean = a.isInstanceOf[Label]

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
             atyp: org.openprovenance.prov.model.QualifiedName) = {
      this()
      elementName=qualifiedName
      typ=atyp;
      setValueFromObject(value)
    }
  
  
    def getAttributeKind(x$1: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Attribute.AttributeKind = ???
  
    def getKind(): org.openprovenance.prov.model.Attribute.AttributeKind = org.openprovenance.prov.model.Attribute.AttributeKind.OTHER
	
    def getQualifiedName(x$1: org.openprovenance.prov.model.Attribute.AttributeKind): org.openprovenance.prov.model.QualifiedName = ???
 
    def canEqual(a: Any): Boolean = a.isInstanceOf[Other]

  
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
  
    def canEqual(a: Any): Boolean = a.isInstanceOf[LangString]

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
    
    def this(namespace: String, local: String, pref: String) = {
        this()
        namespaceURI=namespace
        localPart=local
        prefix=pref
    }

    var uri: String=null
    override def getUri(): String = {
        getNamespaceURI() + getUnescapedLocalPart();
    } 
    override def setUri(discard: String): Unit = {}

    val qnU=new QualifiedNameUtils;
    
    
    def getUnescapedLocalPart (): String = { qnU.unescapeProvLocalName(localPart)}
   
    override def toString(): String = {
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
    
    override def hashCode: Int = this.namespaceURI.hashCode ^ this.localPart.hashCode()


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

object ProvFactory {
    @scala.annotation.tailrec
    def splitrec(attributes: util.List[Attribute],
                 ls: util.List[org.openprovenance.prov.model.Label],
                 ts: util.List[org.openprovenance.prov.model.Type],
                 vs: util.List[org.openprovenance.prov.model.Value],
                 locs: util.List[org.openprovenance.prov.model.Location],
                 rs: util.List[org.openprovenance.prov.model.Role],
                 os: util.List[org.openprovenance.prov.model.Other])
    : (util.List[org.openprovenance.prov.model.Label],
       util.List[org.openprovenance.prov.model.Type],
       util.List[org.openprovenance.prov.model.Value],
       util.List[org.openprovenance.prov.model.Location],
       util.List[org.openprovenance.prov.model.Role],
       util.List[org.openprovenance.prov.model.Other]) = {
        if ((attributes == null) || (attributes.isEmpty)) {
            (ls, ts, vs, locs, rs, os)
        } else{
            val attr = attributes.get(0)
            val rest=attributes.subList(1, attributes.size())
            attr match {
                case l: org.openprovenance.prov.model.Label    => splitrec(rest, extend(ls, l), ts, vs, locs, rs, os)
                case t: org.openprovenance.prov.model.Type     => splitrec(rest, ls, extend(ts, t), vs, locs, rs, os)
                case v: org.openprovenance.prov.model.Value    => splitrec(rest, ls, ts, extend(vs, v), locs, rs, os)
                case l: org.openprovenance.prov.model.Location => splitrec(rest, ls, ts, vs, extend(locs, l), rs, os)
                case r: org.openprovenance.prov.model.Role     => splitrec(rest, ls, ts, vs, locs, extend(rs, r), os)
                case o: org.openprovenance.prov.model.Other    => splitrec(rest, ls, ts, vs, locs, rs, extend(os, o))
            }
        }
    }


    private def extend[T](ls: util.List[T], l: T):  util.List[T]= {
        if (ls==null) {
            val ls2=new util.LinkedList[T]()
            ls2.add(l)
            ls2
        } else {
            ls.add(l)
            ls
        }
    }
}
 
class ProvFactory extends org.openprovenance.prov.model.ProvFactory (new ObjectFactory) {
 
  //println("In ProvFactory Constructor")
  
  override def getSerializer() = throw new UnsupportedOperationException
  override def newQualifiedName(x1: String, x2:String, x3: String, x4:org.openprovenance.prov.model.ProvUtilities.BuildFlag) = throw new UnsupportedOperationException
  override def newAttribute(kind: org.openprovenance.prov.model.Attribute.AttributeKind, value: Any, typ: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Attribute = {
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

    /** A factory method to create an instance of a delegation {@link ActedOnBehalfOf}
     *
     * @param id          identifier for the delegation association between delegate and responsible
     * @param delegate    identifier for the agent associated with an activity, acting on behalf of the responsible agent
     * @param responsible identifier for the agent, on behalf of which the delegate agent acted
     * @param activity    optional identifier of an activity for which the delegation association holds
     * @param attributes  optional set  of attributes representing additional information about this delegation association
     * @return an instance of {@link ActedOnBehalfOf}
     */
    override def newActedOnBehalfOf(id: model.QualifiedName,
                                    delegate: model.QualifiedName,
                                    responsible: model.QualifiedName,
                                    activity: model.QualifiedName,
                                    attributes: util.Collection[Attribute]): model.ActedOnBehalfOf = {
        val a=new ActedOnBehalfOf
        a.id=id
        a.delegate=delegate
        a.responsible=responsible
        a.activity=activity

        val (ls, ts, vs, locs, rs, os) = splitrec(attributeList(attributes), new util.LinkedList[model.Label](), a.getType(), new util.LinkedList[model.Value](), new util.LinkedList[model.Location](), new util.LinkedList[model.Role](), a.getOther())
        a.setLabel(ls.stream().map(lab => lab.getValue.asInstanceOf[model.LangString]).collect(Collectors.toList()))
        if (!vs.isEmpty) throw new UnsupportedOperationException("ActedOnBehalfOf cannot have values")
        if (!locs.isEmpty) throw new UnsupportedOperationException("ActedOnBehalfOf cannot have locations")
        if (!rs.isEmpty) throw new UnsupportedOperationException("ActedOnBehalfOf cannot have roles")

        // todo what about attributes

        a
    }

    /** A factory method to create an instance of an Association {@link WasAssociatedWith}
     *
     * @param id         an optional identifier for the association between an activity and an agent
     * @param activity   an identifier for the activity
     * @param agent      an optional identifier for the agent associated with the activity
     * @param plan       an optional identifier for the plan the agent relied on in the context of this activity
     * @param attributes an optional set of attribute-value pairs representing additional information about this association of this activity with this agent.
     * @return an instance of {@link WasAssociatedWith}
     */
    override def newWasAssociatedWith(id: model.QualifiedName, activity: model.QualifiedName, agent: model.QualifiedName, plan: model.QualifiedName, attributes: util.Collection[Attribute]): model.WasAssociatedWith = {
        val a = new WasAssociatedWith
        a.id = id
        a.activity=activity
        a.agent = agent
        a.plan = plan
        val (ls, ts, vs, locs, rs, os) = splitrec(attributeList(attributes), new util.LinkedList[model.Label](), a.getType(), new util.LinkedList[model.Value](), new util.LinkedList[model.Location](), a.getRole(), a.getOther())
        a.setLabel(ls.stream().map(lab => lab.getValue.asInstanceOf[model.LangString]).collect(Collectors.toList()))
        if (!vs.isEmpty) throw new UnsupportedOperationException("WasAssociatedWith cannot have values")
        if (!locs.isEmpty) throw new UnsupportedOperationException("WasAssociatedWith cannot have locations")
        a
    }

    /** A factory method to create an instance of an attribution {@link WasAttributedTo}
     *
     * @param id         an optional identifier for the relation
     * @param entity     an entity identifier
     * @param agent      the identifier of the agent whom the entity is ascribed to, and therefore bears some responsibility for its existence
     * @param attributes an optional set of attribute-value pairs representing additional information about this attribution.
     * @return an instance of {@linkWasAttributedTo}
     */
    override def newWasAttributedTo(id: model.QualifiedName, entity: model.QualifiedName, agent: model.QualifiedName, attributes: util.Collection[Attribute]): model.WasAttributedTo = {
        val a = new WasAttributedTo
        a.id = id
        a.entity = entity
        a.agent = agent
        val (ls, ts, vs, locs, rs, os) = splitrec(attributeList(attributes), new util.LinkedList[model.Label](), a.getType(), new util.LinkedList[model.Value](), new util.LinkedList[model.Location](), new util.LinkedList[model.Role](), a.getOther())
        a.setLabel(ls.stream().map(lab => lab.getValue.asInstanceOf[model.LangString]).collect(Collectors.toList()))
        if (!vs.isEmpty) throw new UnsupportedOperationException("WasAttributedTo cannot have values")
        if (!locs.isEmpty) throw new UnsupportedOperationException("WasAttributedTo cannot have locations")
        if (!rs.isEmpty) throw new UnsupportedOperationException("WasAttributedTo cannot have roles")
        a
    }

    /** A factory method to create an instance of an communication {@link WasInformedBy}
     *
     * @param id         an optional identifier identifying the association;
     * @param informed   the identifier of the informed activity;
     * @param informant  the identifier of the informant activity;
     * @param attributes an optional set of attribute-value pairs representing additional information about this communication.
     * @return an instance of {@link WasInformedBy}
     */
    override def newWasInformedBy(id: model.QualifiedName, informed: model.QualifiedName, informant: model.QualifiedName, attributes: util.Collection[Attribute]): model.WasInformedBy = {
        val a = new WasInformedBy
        a.id = id
        a.informed = informed
        a.informant = informant
        val (ls, ts, vs, locs, rs, os) = splitrec(attributeList(attributes), new util.LinkedList[model.Label](), a.getType(), new util.LinkedList[model.Value](), new util.LinkedList[model.Location](), new util.LinkedList[model.Role](), a.getOther())
        a.setLabel(ls.stream().map(lab => lab.getValue.asInstanceOf[model.LangString]).collect(Collectors.toList()))
        if (!vs.isEmpty) throw new UnsupportedOperationException("WasInformedBy cannot have values")
        if (!locs.isEmpty) throw new UnsupportedOperationException("WasInformedBy cannot have locations")
        if (!rs.isEmpty) throw new UnsupportedOperationException("WasInformedBy cannot have roles")
        a
    }


    /** A factory method to create an instance of an invalidation {@link WasInvalidatedBy}
     *
     * @param id         an optional identifier for a usage
     * @param entity     an identifier for the created <a href="http://www.w3.org/TR/prov-dm/#invalidation.entity">entity</a>
     * @param activity   an optional identifier  for the <a href="http://www.w3.org/TR/prov-dm/#invalidation.activity">activity</a> that creates the entity
     * @param time       an optional "invalidation time", the time at which the entity was completely created
     * @param attributes an optional set of attribute-value pairs representing additional information about this invalidation
     * @return an instance of {@link WasInvalidatedBy}
     */
    override def newWasInvalidatedBy(id: model.QualifiedName, entity: model.QualifiedName, activity: model.QualifiedName, time: XMLGregorianCalendar, attributes: util.Collection[model.Attribute]): model.WasInvalidatedBy = {
        val a = new WasInvalidatedBy
        a.id = id
        a.entity = entity
        a.activity = activity
        a.time=time
        val (ls, ts, vs, locs, rs, os) = splitrec(attributeList(attributes), new util.LinkedList[model.Label](), a.getType(), new util.LinkedList[model.Value](), a.getLocation(), a.getRole(), a.getOther())
        a.setLabel(ls.stream().map(lab => lab.getValue.asInstanceOf[model.LangString]).collect(Collectors.toList()))
        if (!vs.isEmpty) throw new UnsupportedOperationException("WasInvalidatedBy cannot have values")
        a
    }

    private def attributeList(attributes: util.Collection[Attribute]): util.LinkedList[Attribute] = {
        if (attributes==null) {
            new util.LinkedList[Attribute]()
        } else {
            new util.LinkedList[Attribute](attributes)
        }
    }

    /** A factory method to create an instance of a Usage {@link Used}
     *
     * @param id         an optional identifier for a usage
     * @param activity   the identifier  of the <a href="http://www.w3.org/TR/prov-dm/#usage.activity">activity</a> that used an entity
     * @param entity     an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#usage.entity">entity</a> being used
     * @param time       an optional "usage time", the <a href="http://www.w3.org/TR/prov-dm/#usage.time">time</a> at which the entity started to be used
     * @param attributes an optional set of attribute-value pairs representing additional information about this usage
     * @return an instance of {@link Used}
     */
    override def newUsed(id: model.QualifiedName, activity: model.QualifiedName, entity: model.QualifiedName, time: XMLGregorianCalendar, attributes: util.Collection[Attribute]): model.Used = {
        val a = new Used
        a.id = id
        a.entity = entity
        a.activity = activity
        a.time=time
        val (ls, ts, vs, locs, rs, os) = splitrec(attributeList(attributes), new util.LinkedList[model.Label](), a.getType(), new util.LinkedList[model.Value](), a.getLocation(), a.getRole(), a.getOther())
        a.setLabel(ls.stream().map(lab => lab.getValue.asInstanceOf[model.LangString]).collect(Collectors.toList()))
        if (!vs.isEmpty) throw new UnsupportedOperationException("Used cannot have values")
        a

    }

    /** A factory method to create an instance of an end {@link WasEndedBy}
     *
     * @param id         an optional identifier for a end
     * @param activity   an identifier for the ended <a href="http://www.w3.org/TR/prov-dm/#end.activity">activity</a>
     * @param trigger    an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#end.trigger">entity triggering</a> the activity ending
     * @param ender      an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#end.ender">activity</a> that generated the (possibly unspecified) entity
     * @param time       the optional <a href="http://www.w3.org/TR/prov-dm/#end.time">time</a>  at which the activity was ended
     * @param attributes an optional set of <a href="http://www.w3.org/TR/prov-dm/#end.attributes">attribute-value pairs</a> representing additional information about this activity start
     * @return an instance of {@link WasStartedBy}
     */
    override def newWasEndedBy(id: model.QualifiedName, activity: model.QualifiedName, trigger: model.QualifiedName, ender: model.QualifiedName, time: XMLGregorianCalendar, attributes: util.Collection[Attribute]): model.WasEndedBy = {
        val a = new WasEndedBy
        a.id = id
        a.ender = ender
        a.activity = activity
        a.trigger = trigger
        a.time=time
        val (ls, ts, vs, locs, rs, os) = splitrec(attributeList(attributes), new util.LinkedList[model.Label](), a.getType(), new util.LinkedList[model.Value](), a.getLocation(), a.getRole(), a.getOther())
        a.setLabel(ls.stream().map(lab => lab.getValue.asInstanceOf[model.LangString]).collect(Collectors.toList()))
        if (!vs.isEmpty) throw new UnsupportedOperationException("WasEndedBy cannot have values")
        a

    }

    /** A factory method to create an instance of a start {@link WasStartedBy}
     *
     * @param id         an optional identifier for a start
     * @param activity   an identifier for the started <a href="http://www.w3.org/TR/prov-dm/#start.activity">activity</a>
     * @param trigger    an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#start.trigger">entity triggering</a> the activity
     * @param starter    an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#start.starter">activity</a> that generated the (possibly unspecified) entity
     * @param time       the optional <a href="http://www.w3.org/TR/prov-dm/#start.time">time</a>  at which the activity was started
     * @param attributes an optional set of <a href="http://www.w3.org/TR/prov-dm/#start.attributes">attribute-value pairs</a> representing additional information about this activity start
     * @return an instance of {@link WasStartedBy}
     */
    override def newWasStartedBy(id: model.QualifiedName, activity: model.QualifiedName, trigger: model.QualifiedName, starter: model.QualifiedName, time: XMLGregorianCalendar, attributes: util.Collection[Attribute]): model.WasStartedBy = {
        val a = new WasStartedBy
        a.id = id
        a.starter = starter
        a.activity = activity
        a.trigger = trigger
        a.time=time
        val (ls, ts, vs, locs, rs, os) = splitrec(attributeList(attributes), new util.LinkedList[model.Label](), a.getType(), new util.LinkedList[model.Value](), a.getLocation(), a.getRole(), a.getOther())
        a.setLabel(ls.stream().map(lab => lab.getValue.asInstanceOf[model.LangString]).collect(Collectors.toList()))
        if (!vs.isEmpty) throw new UnsupportedOperationException("WasStartedBy cannot have values")
        a
    }

    /** A factory method to create an instance of a generation {@link WasGeneratedBy}
     *
     * @param id         an optional identifier for a usage
     * @param entity     an identifier for the created <a href="http://www.w3.org/TR/prov-dm/#generation.entity">entity</a>
     * @param activity   an optional identifier  for the <a href="http://www.w3.org/TR/prov-dm/#generation.activity">activity</a> that creates the entity
     * @param time       an optional "generation time", the time at which the entity was completely created
     * @param attributes an optional set of attribute-value pairs representing additional information about this generation
     * @return an instance of {@link WasGeneratedBy}
     */
    override def newWasGeneratedBy(id: model.QualifiedName, entity: model.QualifiedName, activity: model.QualifiedName, time: XMLGregorianCalendar, attributes: util.Collection[Attribute]): model.WasGeneratedBy = {
        val a = new WasGeneratedBy
        a.id = id
        a.entity = entity
        a.activity = activity
        a.time=time
        val (ls, ts, vs, locs, rs, os) = splitrec(attributeList(attributes), new util.LinkedList[model.Label](), a.getType(), new util.LinkedList[model.Value](), a.getLocation(), a.getRole(), a.getOther())
        a.setLabel(ls.stream().map(lab => lab.getValue.asInstanceOf[model.LangString]).collect(Collectors.toList()))
        if (!vs.isEmpty) throw new UnsupportedOperationException("WasGeneratedBy cannot have values")
        a
    }

    override def newLabel(value: Any, `type`: model.QualifiedName): Attribute = {
        newAttribute(AttributeKind.PROV_LABEL, value, `type`)
    }

    /** A factory method to create an instance of an influence {@link WasInfluencedBy}
     *
     * @param id         optional identifier identifying the association
     * @param influencee an identifier for an entity, activity, or agent
     * @param influencer an identifier for an ancestor entity, activity, or agent that the former depends on
     * @param attributes an optional set of attribute-value pairs representing additional information about this association
     * @return an instance of {@link WasInfluencedBy}
     */
    override def newWasInfluencedBy(id: model.QualifiedName, influencee: model.QualifiedName, influencer: model.QualifiedName, attributes: util.Collection[Attribute]): model.WasInfluencedBy =  {
        val a = new WasInfluencedBy
        a.id = id
        a.influencer = influencer
        a.influencee = influencee
        val (ls, ts, vs, locs, rs, os) = splitrec(attributeList(attributes), new util.LinkedList[model.Label](), a.getType(), new util.LinkedList[model.Value](), a.getLocation(), new util.LinkedList[model.Role](), a.getOther())
        a.setLabel(ls.stream().map(lab => lab.getValue.asInstanceOf[model.LangString]).collect(Collectors.toList()))
        if (!vs.isEmpty) throw new UnsupportedOperationException("WasInfluencedBy cannot have values")
        if (!locs.isEmpty) throw new UnsupportedOperationException("WasInfluencedBy cannot have locations")
        if (!rs.isEmpty) throw new UnsupportedOperationException("WasInfluencedBy cannot have roles")
        a
    }

    override def newActivity(id: model.QualifiedName, startTime: XMLGregorianCalendar, endTime: XMLGregorianCalendar, attributes: util.Collection[Attribute]): model.Activity = {
        val a = new Activity
        a.id=id
        a.startTime=startTime
        a.endTime=endTime
        val (ls, ts, vs, locs, rs, os) = splitrec(attributeList(attributes), new util.LinkedList[model.Label](), a.getType(), new util.LinkedList[model.Value](), a.getLocation(), new util.LinkedList[model.Role](), a.getOther())
        a.setLabel(ls.stream().map(lab => lab.getValue.asInstanceOf[model.LangString]).collect(Collectors.toList()))
        if (!vs.isEmpty) throw new UnsupportedOperationException("Activity cannot have values")
        if (!locs.isEmpty) throw new UnsupportedOperationException("Activity cannot have locations")
        if (!rs.isEmpty) throw new UnsupportedOperationException("Activity cannot have roles")
        a
    }

    override def newAgent(id: model.QualifiedName, attributes: util.Collection[Attribute]): model.Agent = {
        val a = new Agent
        a.id = id
        val (ls, ts, vs, locs, rs, os) = splitrec(attributeList(attributes), new util.LinkedList[model.Label](), a.getType(), new util.LinkedList[model.Value](), a.getLocation(), new util.LinkedList[model.Role](), a.getOther())
        a.setLabel(ls.stream().map(lab => lab.getValue.asInstanceOf[model.LangString]).collect(Collectors.toList()))
        if (!vs.isEmpty) throw new UnsupportedOperationException("Agent cannot have values")
        if (!locs.isEmpty) throw new UnsupportedOperationException("Agent cannot have locations")
        if (!rs.isEmpty) throw new UnsupportedOperationException("Agent cannot have roles")
        a
    }

    override def newAlternateOf(e1: model.QualifiedName, e2: model.QualifiedName): model.AlternateOf = {
        val a = new AlternateOf
        a.id = null
        a.alternate1 = e1
        a.alternate2 = e2
        a
    }

    /** A factory method to create an instance of a derivation {@link WasDerivedFrom}
     *
     * @param id         an optional identifier for a derivation
     * @param e2         the identifier  of the <a href="http://www.w3.org/TR/prov-dm/#derivation.generatedEntity">entity generated</a> by the derivation
     * @param e1         the identifier  of the <a href="http://www.w3.org/TR/prov-dm/#derivation.usedEntity">entity used</a> by the derivation
     * @param activity   an identifier for the <a href="http://www.w3.org/TR/prov-dm/#derivation.activity">activity</a> underpinning the derivation
     * @param generation an identifier for the <a href="http://www.w3.org/TR/prov-dm/#derivation.genertion">generation</a> associated with the derivation
     * @param usage      an identifier for the <a href="http://www.w3.org/TR/prov-dm/#derivation.usage">usage</a> associated with the derivation
     * @param attributes an optional set of <a href="http://www.w3.org/TR/prov-dm/#end.attributes">attribute-value pairs</a> representing additional information about this derivation
     * @return an instance of {@link WasDerivedFrom}
     */
    override def newWasDerivedFrom(id: model.QualifiedName, e2: model.QualifiedName, e1: model.QualifiedName, activity: model.QualifiedName, generation: model.QualifiedName, usage: model.QualifiedName, attributes: util.Collection[Attribute]): model.WasDerivedFrom = {
        val a = new WasDerivedFrom
        a.id = id
        a.generatedEntity = e2
        a.usedEntity = e1
        a.activity = activity
        a.generation = generation
        a.usage = usage
        val (ls, ts, vs, locs, rs, os) = splitrec(attributeList(attributes), new util.LinkedList[model.Label](), a.getType(), new util.LinkedList[model.Value](), new util.LinkedList[model.Location](), new util.LinkedList[model.Role](), a.getOther())
        a.setLabel(ls.stream().map(lab => lab.getValue.asInstanceOf[model.LangString]).collect(Collectors.toList()))
        if (!vs.isEmpty) throw new UnsupportedOperationException("WasDerivedFrom cannot have values")
        if (!locs.isEmpty) throw new UnsupportedOperationException("WasDerivedFrom cannot have locations")
        if (!rs.isEmpty) throw new UnsupportedOperationException("WasDerivedFrom cannot have roles")
        a
    }

    override def newSpecializationOf(e2: model.QualifiedName, e1: model.QualifiedName): model.SpecializationOf = {

        val a = new SpecializationOf
        a.id = null
        a.specificEntity = e2
        a.generalEntity = e1
        a
    }
}

object Entity {
  def entity(id: QualifiedName):Entity = {
    val e=new Entity
    e.id=id
    e
  }  
  def activity(id: QualifiedName)(implicit startTime: XMLGregorianCalendar=null, endTime: XMLGregorianCalendar=null): Activity = {
    val act=new Activity
    act.id=id
    act.startTime=startTime
    act.endTime=endTime
    act
  }
  
  def test (): Unit = {
    val  e=entity(null)
    val  a1=activity(null)
    val  a2=activity(null)(null)
    val  a3=activity(null)(null,null)
  }
}
