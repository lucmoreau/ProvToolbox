package org.openprovenance.prov.scala.nf

import java.io.File
import javax.xml.datatype.XMLGregorianCalendar
import org.openprovenance.prov.model.Namespace
import org.openprovenance.prov.model.StatementOrBundle.Kind._
import org.openprovenance.prov.model.exception.ParserException
import org.openprovenance.prov.scala.immutable
import org.openprovenance.prov.scala.immutable.ProvFactory.pf
import org.openprovenance.prov.scala.immutable.{Kind, _}
import org.openprovenance.prov.scala.interop.{FileInput, Input, StandardInput, StreamInput}
import org.openprovenance.prov.scala.nf.Denormalize.{complete_args_list, elements_args_list, oneCombination, someCombinations}
import org.openprovenance.prov.scala.nf.KeyIndexer.notKeyable
import org.openprovenance.prov.scala.nf.MX_Factory._
import org.openprovenance.prov.scala.nf.Statement.nullOption
import org.openprovenance.prov.scala.nf.StatementIndexer.add
import org.openprovenance.prov.scala.streaming.{DocBuilder, DocBuilderFunctions, NFBuilder}
import org.parboiled2.ParseError
import org.openprovenance.prov.scala.nf.StatementIndexer.extend

import scala.annotation.{tailrec, unused}
import scala.beans.BeanProperty
import scala.jdk.CollectionConverters._
import scala.io.BufferedSource
import scala.util.{Failure, Success}

trait HasLocation {
  def getLocation(): java.util.List[org.openprovenance.prov.model.Location] = location.toSeq.asInstanceOf[Seq[org.openprovenance.prov.model.Location]].asJava
  val location: Set[Location]
}

trait HasRole {
  def getRole(): java.util.List[org.openprovenance.prov.model.Role] = role.toSeq.asInstanceOf[Seq[org.openprovenance.prov.model.Role]].asJava
  val role: Set[Role]
}

trait HasLabel {
  def getLabel(): java.util.List[org.openprovenance.prov.model.LangString] = label.toSeq.asInstanceOf[Seq[org.openprovenance.prov.model.LangString]].asJava

  val label: Set[LangString]
}

trait HasOther {
  //val other: Set[Other] 
  val other: Map[QualifiedName, Set[Other]]

  def getOther(): java.util.List[org.openprovenance.prov.model.Other] = {
    val setnil: Set[Other] = Set()
    def op(s: Set[Other], entry: (QualifiedName, Set[Other])): Set[Other] = { s ++ entry._2 }
    val set: Set[Other] = other.foldLeft(setnil)(op)
    set.toSeq.asInstanceOf[Seq[org.openprovenance.prov.model.Other]].asJava
  }

}

trait HasValue {
  val value: Set[Value]

  def getValue() = value

  def setValue(x: org.openprovenance.prov.model.Value) {
    throw new UnsupportedOperationException
  }
}

trait HasType {
  val typex: Set[Type]

  def getType(): java.util.List[org.openprovenance.prov.model.Type] = typex.toSeq.asInstanceOf[Seq[org.openprovenance.prov.model.Type]].asJava
}

trait SingleIdentifiable extends Identifiable //extends org.openprovenance.prov.scala.immutable.Identifiable 
{
  val id: QualifiedName
}

trait MultiIdentifiable extends Identifiable //extends org.openprovenance.prov.scala.immutable.Identifiable 
{
  val id: Set[QualifiedName]
}

trait Identifiable {
  def idSets(): Set[Set[QualifiedName]]

  def optionalId(id: QualifiedName, sb: StringBuilder):Unit = {
    if (id != null) {
      sb ++= id.toString()
      sb += ';'
    }
  }
  def optionalId(id: Set[QualifiedName], sb: StringBuilder): Unit = {
    if (id != null) {
      id.addString(sb, "{", ",", "}")
      sb += ';'
    }
  }

  def idOrMarker(set: Set[QualifiedName], sb: StringBuilder): Unit = {
    if (set != null) {
      set.addString(sb, "{", ",", "}")
      //sb++=set.toString()
    } else {
      sb += '-'
    }
  }

  def timeOrMarker(time: Set[XMLGregorianCalendar], sb: StringBuilder): Unit = {
    if (time != null) {
      time.addString(sb, "{", ",", "}") //sb++=time.toString()
    } else { sb += '-' }
  }

  //def nullOption(q: QualifiedName) = {
  //      if (q==null) None else Some(q)
  //}

  def mapMerge(map1: Map[QualifiedName, Set[Other]], map2: Map[QualifiedName, Set[Other]]): Map[QualifiedName, Set[Other]] = {
    (map1.toSet ++ map2.toSet).groupBy(_._1).view.mapValues(x => x.flatMap(_._2).toSet).toMap
  }

}

trait Hashable {

  @inline final def h(x: AnyRef): Int = {
    if (x == null) 0 else x.hashCode
  }

  @inline final def pr(v0: Int, v1: Int): Int = {
    prime * v0 + v1
  }

  final private val prime = 37
}

trait ImmutableEntity extends org.openprovenance.prov.model.HasLabel
  with org.openprovenance.prov.model.HasType
  with org.openprovenance.prov.model.HasLocation
  with org.openprovenance.prov.model.HasOther
  with MultiIdentifiable
  with HasLocation
  with HasValue
  with HasLabel
  with HasType
  with HasOther
  with Hashable {
  @BeanProperty
  val kind = PROV_ENTITY

  val enumType: Kind.Value = Kind.ent

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

  override lazy val hashCode: Int = {
    pr(pr(pr(pr(pr(h(id), h(value)), h(location)), h(label)), h(typex)), h(other))
  }

  def idSets(): Set[Set[QualifiedName]] = {
    Set(id)
  }

  def getAttributes :Set[Attribute]= {
    label.map(l =>new Label(pf.prov_label,l).asInstanceOf[Attribute])++typex++location++value++other.values.flatten
  }


  def toNotation(sb: StringBuilder): Unit = {
    sb ++= "entity("
    //sb ++= id.toString()      
    id.addString(sb, "{", ",", "}")
    Attribute.toNotation(sb, label, typex, value, location, Set(), other)
    sb += ')'
  }

}

trait ImmutableAgent extends org.openprovenance.prov.model.HasLabel
  with org.openprovenance.prov.model.HasType
  with org.openprovenance.prov.model.HasLocation
  with org.openprovenance.prov.model.HasOther
  with MultiIdentifiable
  with HasLocation
  with HasValue
  with HasLabel
  with HasType
  with HasOther
  with Hashable {

  @BeanProperty
  val kind = PROV_AGENT

  val enumType: Kind.Value = Kind.ag

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

  override lazy val hashCode: Int = {
    pr(pr(pr(pr(pr(h(id), h(value)), h(location)), h(label)), h(typex)), h(other))
  }

  def idSets(): Set[Set[QualifiedName]] = {
    Set(id)
  }

  def getAttributes :Set[Attribute]= {
    label.map(l =>new Label(pf.prov_label,l).asInstanceOf[Attribute])++typex++location++value++other.values.flatten
  }


  def toNotation(sb: StringBuilder): Unit = {
    sb ++= "agent("
    //sb ++= id.toString()
    id.addString(sb, "{", ",", "}")

    Attribute.toNotation(sb, label, typex, value, location, Set(), other)
    sb += ')'
  }

}

trait ImmutableActivity extends org.openprovenance.prov.model.HasLabel
  with org.openprovenance.prov.model.HasType
  with org.openprovenance.prov.model.HasLocation
  with org.openprovenance.prov.model.HasOther
  with MultiIdentifiable
  with HasLocation
  with HasLabel
  with HasType
  with HasOther
  with Hashable {

  val startTime: Set[XMLGregorianCalendar]
  def getStartTime(): Set[XMLGregorianCalendar] = startTime

  val endTime: Set[XMLGregorianCalendar]
  def getEndTime(): Set[XMLGregorianCalendar] = endTime

  def setEndTime(x$1: javax.xml.datatype.XMLGregorianCalendar): Unit = throw new UnsupportedOperationException
  def setStartTime(x$1: javax.xml.datatype.XMLGregorianCalendar) = throw new UnsupportedOperationException

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

  override lazy val hashCode: Int = {
    pr(pr(pr(pr(pr(pr(h(id), h(startTime)), h(endTime)), h(location)), h(label)), h(typex)), h(other))
  }

  def idSets(): Set[Set[QualifiedName]] = {
    Set(id)
  }

  def getAttributes :Set[Attribute]= {
    label.map(l =>new Label(pf.prov_label,l).asInstanceOf[Attribute])++typex++location++other.values.flatten
  }

  def toNotation(sb: StringBuilder): Unit = {
    sb ++= "activity("
    id.addString(sb, "{", ",", "}")
    //sb ++= id.toString()
    sb += ','
    timeOrMarker(startTime, sb)
    sb += ','
    timeOrMarker(endTime, sb)
    Attribute.toNotation(sb, label, typex, Set(), location, Set(), other)
    sb += ')'
  }
}

trait ImmutableWasDerivedFrom extends Relation with MultiIdentifiable with HasLabel with HasType with HasOther with Hashable {

  val generatedEntity: Set[QualifiedName]

  val usedEntity: Set[QualifiedName]

  val activity: Set[QualifiedName]

  val generation: Set[QualifiedName]

  val usage: Set[QualifiedName]

  @BeanProperty
  val kind = PROV_DERIVATION
  val enumType: Kind.Value = Kind.wdf

  def getCause: Set[QualifiedName] = usedEntity
  def getEffect: Set[QualifiedName] = generatedEntity

  def getActivity: Set[QualifiedName] = activity
  def getGeneratedEntity: Set[QualifiedName] = generatedEntity
  def getGeneration: Set[QualifiedName] = generation
  def getUsage: Set[QualifiedName] = usage
  def getUsedEntity: Set[QualifiedName] = usedEntity

  def setActivity(x$1: org.openprovenance.prov.model.QualifiedName) = throw new UnsupportedOperationException
  def setGeneratedEntity(x$1: org.openprovenance.prov.model.QualifiedName) = throw new UnsupportedOperationException
  def setGeneration(x$1: org.openprovenance.prov.model.QualifiedName) = throw new UnsupportedOperationException
  def setUsage(x$1: org.openprovenance.prov.model.QualifiedName) = throw new UnsupportedOperationException
  def setUsedEntity(x$1: org.openprovenance.prov.model.QualifiedName) = throw new UnsupportedOperationException

  def canEqual(a: Any): Boolean = a.isInstanceOf[ImmutableWasDerivedFrom]

  override def equals(that: Any): Boolean =
    that match {
      case that: ImmutableWasDerivedFrom => that.canEqual(this) &&
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

  override lazy val hashCode: Int = {
    pr(pr(pr(pr(pr(pr(pr(pr(h(id), h(generatedEntity)), h(usedEntity)), h(activity)), h(usage)), h(generation)), h(label)), h(typex)), h(other))

  }

  def idSets(): Set[Set[QualifiedName]] = {
    Set(id, generatedEntity, usedEntity, activity, generation, usage)
  }

  def getAttributes :Set[Attribute]= {
    label.map(l =>new Label(pf.prov_label,l).asInstanceOf[Attribute])++typex++other.values.flatten
  }


  def toNotation(sb: StringBuilder): Unit = {
    sb ++= "wasDerivedFrom("
    optionalId(id, sb)
    idOrMarker(generatedEntity, sb)
    sb += ','
    idOrMarker(usedEntity, sb)
    if (!(activity == null && generation == null && usage == null)) {
      idOrMarker(activity, sb)
      sb += ','
      idOrMarker(generation, sb)
      sb += ','
      idOrMarker(usage, sb)
    }
    Attribute.toNotation(sb, label, typex, Set(), Set(), Set(), other)
    sb += ')'
  }

}

trait ImmutableWasGeneratedBy extends Relation with MultiIdentifiable with HasLabel with HasType with HasOther with HasLocation with HasRole with Hashable {

  val entity: Set[QualifiedName]

  val activity: Set[QualifiedName]

  val time: Set[javax.xml.datatype.XMLGregorianCalendar]
  def getTime: Set[XMLGregorianCalendar] = time

  @BeanProperty
  val kind = PROV_GENERATION
  val enumType: Kind.Value = Kind.wgb

  def getCause: Set[QualifiedName] = activity
  def getEffect: Set[QualifiedName] = entity

  def getActivity: Set[QualifiedName] = activity
  def getEntity: Set[QualifiedName] = entity

  def setActivity(x$1: org.openprovenance.prov.model.QualifiedName) = throw new UnsupportedOperationException
  def setEntity(x$1: org.openprovenance.prov.model.QualifiedName) = throw new UnsupportedOperationException
  def setTime(x$1: javax.xml.datatype.XMLGregorianCalendar): Unit = throw new UnsupportedOperationException

  def canEqual(a: Any): Boolean = a.isInstanceOf[ImmutableWasGeneratedBy]

  override def equals(that: Any): Boolean =
    that match {
      case that: ImmutableWasGeneratedBy => that.canEqual(this) &&
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

  override lazy val hashCode: Int = {
    pr(pr(pr(pr(pr(pr(pr(h(id), h(activity)), h(entity)), h(location)), h(label)), h(typex)), h(role)), h(other))

  }
  def idSets(): Set[Set[QualifiedName]] = {
    Set(id, entity, activity)
  }

  def getAttributes :Set[Attribute]= {
    label.map(l =>new Label(pf.prov_label,l).asInstanceOf[Attribute])++location++typex++role++other.values.flatten
  }


  def toNotation(sb: StringBuilder): Unit = {
    sb ++= "wasGeneratedBy("
    optionalId(id, sb)
    idOrMarker(entity, sb)
    sb += ','
    idOrMarker(activity, sb)
    sb += ','
    timeOrMarker(time, sb)
    Attribute.toNotation(sb, label, typex, Set(), location, role, other)
    sb += ')'
  }

}

trait ImmutableUsed extends Relation with MultiIdentifiable with HasLabel with HasType with HasOther with HasLocation with HasRole with Hashable {

  val entity: Set[QualifiedName]

  val activity: Set[QualifiedName]

  val time: Set[javax.xml.datatype.XMLGregorianCalendar]
  def getTime: Set[XMLGregorianCalendar] = time

  @BeanProperty
  val kind = PROV_USAGE
  val enumType: Kind.Value = Kind.usd

  def getCause: Set[QualifiedName] = entity
  def getEffect: Set[QualifiedName] = activity

  def setActivity(x$1: org.openprovenance.prov.model.QualifiedName): Unit = throw new UnsupportedOperationException
  def setEntity(x$1: org.openprovenance.prov.model.QualifiedName):Unit = throw new UnsupportedOperationException
  def setTime(x$1: javax.xml.datatype.XMLGregorianCalendar): Unit = throw new UnsupportedOperationException

  def canEqual(a: Any): Boolean = a.isInstanceOf[ImmutableUsed]

  override def equals(that: Any): Boolean =
    that match {
      case that: ImmutableUsed => that.canEqual(this) &&
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

  override lazy val hashCode: Int = {
    pr(pr(pr(pr(pr(pr(pr(h(id), h(activity)), h(entity)), h(location)), h(label)), h(typex)), h(role)), h(other))

  }
  def idSets(): Set[Set[QualifiedName]] = {
    Set(id, entity, activity)
  }

  def getAttributes :Set[Attribute]= {
    label.map(l =>new Label(pf.prov_label,l).asInstanceOf[Attribute])++location++typex++role++other.values.flatten
  }


  @unused
  def toNotation(sb: StringBuilder): Unit = {
    sb ++= "used("
    optionalId(id, sb)
    idOrMarker(activity, sb)
    sb += ','
    idOrMarker(entity, sb)
    sb += ','
    timeOrMarker(time, sb)
    Attribute.toNotation(sb, label, typex, Set(), location, role, other)
    sb += ')'
  }
}

trait ImmutableWasInvalidatedBy extends Relation with MultiIdentifiable with HasLabel with HasType with HasOther with HasLocation with HasRole with Hashable {

  val entity: Set[QualifiedName]

  val activity: Set[QualifiedName]

  val time: Set[javax.xml.datatype.XMLGregorianCalendar]
  def getTime: Set[XMLGregorianCalendar] = time

  @BeanProperty
  val kind = PROV_INVALIDATION
  val enumType: Kind.Value = Kind.wib

  def getCause: Set[QualifiedName] = activity
  def getEffect: Set[QualifiedName] = entity

  def getActivity: Set[QualifiedName] = activity
  def getEntity: Set[QualifiedName] = entity

  def setActivity(x$1: org.openprovenance.prov.model.QualifiedName) = throw new UnsupportedOperationException
  def setEntity(x$1: org.openprovenance.prov.model.QualifiedName) = throw new UnsupportedOperationException
  def setTime(x$1: javax.xml.datatype.XMLGregorianCalendar): Unit = throw new UnsupportedOperationException

  def canEqual(a: Any): Boolean = a.isInstanceOf[ImmutableWasInvalidatedBy]

  override def equals(that: Any): Boolean =
    that match {
      case that: ImmutableWasInvalidatedBy => that.canEqual(this) &&
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

  override lazy val hashCode: Int = {
    pr(pr(pr(pr(pr(pr(pr(h(id), h(activity)), h(entity)), h(location)), h(label)), h(typex)), h(role)), h(other))

  }
  def idSets(): Set[Set[QualifiedName]] = {
    Set(id, entity, activity)
  }

  def getAttributes :Set[Attribute]= {
    label.map(l =>new Label(pf.prov_label,l).asInstanceOf[Attribute])++location++typex++role++other.values.flatten
  }


  def toNotation(sb: StringBuilder): Unit = {
    sb ++= "wasInvalidatedBy("
    optionalId(id, sb)
    idOrMarker(entity, sb)
    sb += ','
    idOrMarker(activity, sb)
    sb += ','
    timeOrMarker(time, sb)
    Attribute.toNotation(sb, label, typex, Set(), location, role, other)
    sb += ')'
  }

}

trait ImmutableWasStartedBy extends Relation with MultiIdentifiable with HasLocation with HasRole with HasLabel with HasType with HasOther with Hashable {

  val activity: Set[QualifiedName]

  val trigger: Set[QualifiedName]

  val starter: Set[QualifiedName]

  val time: Set[javax.xml.datatype.XMLGregorianCalendar]
  def getTime: Set[XMLGregorianCalendar] = time

  @BeanProperty
  val kind = PROV_START
  val enumType: Kind.Value = Kind.wsb

  def getCause: Set[QualifiedName] = trigger
  def getEffect: Set[QualifiedName] = activity

  def getActivity: Set[QualifiedName] = activity
  def getTrigger: Set[QualifiedName] = trigger
  def getStarter: Set[QualifiedName] = starter

  def setActivity(x$1: org.openprovenance.prov.model.QualifiedName) = throw new UnsupportedOperationException
  def setTrigger(x$1: org.openprovenance.prov.model.QualifiedName) = throw new UnsupportedOperationException
  def setStarter(x$1: org.openprovenance.prov.model.QualifiedName) = throw new UnsupportedOperationException
  def setTime(x$1: javax.xml.datatype.XMLGregorianCalendar): Unit = throw new UnsupportedOperationException

  private def canEqual(a: Any): Boolean = a.isInstanceOf[ImmutableWasStartedBy]

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
        this.typex == that.typex &&
        this.other == that.other
      case _ => false
    }

  override lazy val hashCode: Int = {
    pr(pr(pr(pr(pr(pr(pr(pr(h(id), h(activity)), h(trigger)), h(starter)), h(time)), h(location)), h(label)), h(typex)), h(other))

  }
  def idSets(): Set[Set[QualifiedName]] = {
    Set(id, activity, trigger, starter)
  }



  def getAttributes: Set[Attribute]= {
    label.map(l =>new Label(pf.prov_label,l).asInstanceOf[Attribute])++location++typex++role++other.values.flatten
  }


  def toNotation(sb: StringBuilder): Unit = {
    sb ++= "wasStartedBy("
    optionalId(id, sb)
    idOrMarker(activity, sb)
    sb += ','
    idOrMarker(trigger, sb)
    sb += ','
    idOrMarker(starter, sb)
    sb += ','
    timeOrMarker(time, sb)
    Attribute.toNotation(sb, label, typex, Set(), location, role, other)
    sb += ')'
  }

}

trait ImmutableWasEndedBy extends Relation with MultiIdentifiable with HasLocation with HasRole with HasLabel with HasType with HasOther with Hashable {

  val activity: Set[QualifiedName]

  val trigger: Set[QualifiedName]

  val ender: Set[QualifiedName]

  val time: Set[javax.xml.datatype.XMLGregorianCalendar]
  def getTime: Set[XMLGregorianCalendar] = time

  @BeanProperty
  val kind = PROV_END
  val enumType: Kind.Value = Kind.web

  def getCause: Set[QualifiedName] = trigger
  def getEffect: Set[QualifiedName] = activity

  def getActivity: Set[QualifiedName] = activity
  def getTrigger: Set[QualifiedName] = trigger
  def getEnder: Set[QualifiedName] = ender

  def setActivity(x$1: org.openprovenance.prov.model.QualifiedName) = throw new UnsupportedOperationException
  def setTrigger(x$1: org.openprovenance.prov.model.QualifiedName) = throw new UnsupportedOperationException
  def setEnder(x$1: org.openprovenance.prov.model.QualifiedName) = throw new UnsupportedOperationException
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
        this.other == that.other
      case _ => false
    }

  override lazy val hashCode: Int = {
    pr(pr(pr(pr(pr(pr(pr(pr(h(id), h(activity)), h(trigger)), h(ender)), h(time)), h(location)), h(label)), h(typex)), h(other))

  }
  def idSets(): Set[Set[QualifiedName]] = {
    Set(id, activity, trigger, ender)
  }

  def getAttributes :Set[Attribute]= {
    label.map(l =>new Label(pf.prov_label,l).asInstanceOf[Attribute])++location++typex++role++other.values.flatten
  }


  def toNotation(sb: StringBuilder): Unit = {
    sb ++= "wasEndedBy("
    optionalId(id, sb)
    idOrMarker(activity, sb)
    sb += ','
    idOrMarker(trigger, sb)
    sb += ','
    idOrMarker(ender, sb)
    sb += ','
    timeOrMarker(time, sb)
    Attribute.toNotation(sb, label, typex, Set(), location, role, other)
    sb += ')'
  }

}

trait ImmutableActedOnBehalfOf extends Relation with MultiIdentifiable with HasLabel with HasType with HasOther with Hashable {

  val delegate: Set[QualifiedName]

  val responsible: Set[QualifiedName]

  val activity: Set[QualifiedName]

  @BeanProperty
  val kind = PROV_DELEGATION
  val enumType: Kind.Value = Kind.aobo

  def getCause: Set[QualifiedName] = responsible
  def getEffect: Set[QualifiedName] = delegate

  def getActivity: Set[QualifiedName] = activity
  def getResponsible: Set[QualifiedName] = responsible
  def getDeleqgate: Set[QualifiedName] = delegate

  def setActivity(x$1: org.openprovenance.prov.model.QualifiedName) = throw new UnsupportedOperationException
  def setResponsible(x$1: org.openprovenance.prov.model.QualifiedName) = throw new UnsupportedOperationException
  def setDelegate(x$1: org.openprovenance.prov.model.QualifiedName) = throw new UnsupportedOperationException

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

  override lazy val hashCode: Int = {
    pr(pr(pr(pr(pr(pr(h(id), h(delegate)), h(responsible)), h(activity)), h(label)), h(typex)), h(other))
  }
  def idSets(): Set[Set[QualifiedName]] = {
    Set(id, activity, delegate, responsible)
  }


  def getAttributes :Set[Attribute]= {
    label.map(l =>new Label(pf.prov_label,l).asInstanceOf[Attribute])++typex++other.values.flatten
  }

  def toNotation(sb: StringBuilder): Unit = {
    sb ++= "actedOnBehalfOf("
    optionalId(id, sb)
    idOrMarker(delegate, sb)
    sb += ','
    idOrMarker(responsible, sb)
    sb += ','
    idOrMarker(activity, sb)
    Attribute.toNotation(sb, label, typex, Set(), Set(), Set(), other)
    sb += ')'
  }

}

trait ImmutableWasAssociatedWith extends Relation with MultiIdentifiable with HasLabel with HasType with HasOther with HasRole with Hashable {

  val activity: Set[QualifiedName]

  val agent: Set[QualifiedName]

  val plan: Set[QualifiedName]

  @BeanProperty
  val kind = PROV_ASSOCIATION
  val enumType: Kind.Value = Kind.waw

  def getCause: Set[QualifiedName] = agent
  def getEffect: Set[QualifiedName] = activity
  def getOtherCause: Set[_ <: Set[QualifiedName]] = if (plan == null) Set() else Set(plan)

  def getActivity: Set[QualifiedName] = activity
  def getAgent: Set[QualifiedName] = agent
  def getPlan: Set[QualifiedName] = plan

  def setActivity(x$1: org.openprovenance.prov.model.QualifiedName) = throw new UnsupportedOperationException
  def setAgent(x$1: org.openprovenance.prov.model.QualifiedName) = throw new UnsupportedOperationException
  def setPlan(x$1: org.openprovenance.prov.model.QualifiedName) = throw new UnsupportedOperationException

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

  override lazy val hashCode: Int = {
    pr(pr(pr(pr(pr(pr(pr(h(id), h(activity)), h(agent)), h(plan)), h(label)), h(typex)), h(role)), h(other))

  }
  def idSets(): Set[Set[QualifiedName]] = {
    Set(id, activity, agent, plan)
  }


  def getAttributes :Set[Attribute]= {
    label.map(l =>new Label(pf.prov_label,l).asInstanceOf[Attribute])++typex++role++other.values.flatten
  }

  def toNotation(sb: StringBuilder): Unit = {
    sb ++= "wasAssociatedWith("
    optionalId(id, sb)
    idOrMarker(activity, sb)
    sb += ','
    idOrMarker(agent, sb)
    sb += ','
    idOrMarker(plan, sb)
    Attribute.toNotation(sb, label, typex, Set(), Set(), role, other)
    sb += ')'
  }
}

trait ImmutableWasAttributedTo extends Relation with MultiIdentifiable with HasLabel with HasType with HasOther with Hashable {

  val entity: Set[QualifiedName]

  val agent: Set[QualifiedName]

  @BeanProperty
  val kind = PROV_ATTRIBUTION
  val enumType: Kind.Value = Kind.wat

  def getCause: Set[QualifiedName] = agent
  def getEffect: Set[QualifiedName] = entity

  def getEntity: Set[QualifiedName] = entity
  def getAgent: Set[QualifiedName] = agent

  def setEntity(x$1: org.openprovenance.prov.model.QualifiedName) = throw new UnsupportedOperationException
  def setAgent(x$1: org.openprovenance.prov.model.QualifiedName) = throw new UnsupportedOperationException

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

  override lazy val hashCode: Int = {
    pr(pr(pr(pr(pr(h(id), h(entity)), h(agent)), h(label)), h(typex)), h(other))
  }

  def idSets(): Set[Set[QualifiedName]] = {
    Set(id, entity, agent)
  }

  def getAttributes :Set[Attribute]= {
    label.map(l =>new Label(pf.prov_label,l).asInstanceOf[Attribute])++typex++other.values.flatten
  }

  def toNotation(sb: StringBuilder): Unit = {
    sb ++= "wasAttributedTo("
    optionalId(id, sb)
    idOrMarker(entity, sb)
    sb += ','
    idOrMarker(agent, sb)
    Attribute.toNotation(sb, label, typex, Set(), Set(), Set(), other)
    sb += ')'
  }
}

trait ImmutableSpecializationOf extends Relation with MultiIdentifiable with HasLabel with HasType with HasOther with Hashable {

  val generalEntity: Set[QualifiedName]

  val specificEntity: Set[QualifiedName]

  @BeanProperty
  val kind = PROV_SPECIALIZATION
  val enumType: Kind.Value = Kind.spec

  def getCause: Set[QualifiedName] = generalEntity
  def getEffect: Set[QualifiedName] = specificEntity

  def getGeneralEntity: Set[QualifiedName] = generalEntity
  def getSpecificEntity: Set[QualifiedName] = specificEntity

  def setGeneralEntity(x$1: org.openprovenance.prov.model.QualifiedName) = throw new UnsupportedOperationException
  def setSpecificEntity(x$1: org.openprovenance.prov.model.QualifiedName) = throw new UnsupportedOperationException

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

  override lazy val hashCode: Int = {
    pr(pr(pr(pr(pr(h(id), h(generalEntity)), h(specificEntity)), h(label)), h(typex)), h(other))
  }
  def idSets(): Set[Set[QualifiedName]] = {
    Set(id, generalEntity, specificEntity)
  }

  def getAttributes :Set[Attribute]= {
    label.map(l =>new Label(pf.prov_label,l).asInstanceOf[Attribute])++typex++other.values.flatten
  }

  def toNotation(sb: StringBuilder): Unit = {
    val isExt = (id != null) || label.nonEmpty || typex.nonEmpty || other.nonEmpty

    sb ++= (if (isExt) "provext:specializationOf(" else "specializationOf(")
    optionalId(id, sb)
    idOrMarker(specificEntity, sb)
    sb += ','
    idOrMarker(generalEntity, sb)
    Attribute.toNotation(sb, label, typex, Set(), Set(), Set(), other)
    sb += ')'
  }

}

trait ImmutableAlternateOf extends Relation with MultiIdentifiable with HasLabel with HasType with HasOther with Hashable {

  val alternate1: Set[QualifiedName]

  val alternate2: Set[QualifiedName]

  @BeanProperty
  val kind = PROV_ALTERNATE
  val enumType: Kind.Value = Kind.alt

  def getCause: Set[QualifiedName] = alternate2
  def getEffect: Set[QualifiedName] = alternate1

  def getAlternate1: Set[QualifiedName] = alternate1
  def getAlternate2: Set[QualifiedName] = alternate2

  def setAlternate1(x$1: org.openprovenance.prov.model.QualifiedName) = throw new UnsupportedOperationException
  def setAlternate2(x$1: org.openprovenance.prov.model.QualifiedName) = throw new UnsupportedOperationException

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

  override lazy val hashCode: Int = {
    pr(pr(pr(pr(pr(h(id), h(alternate1)), h(alternate2)), h(label)), h(typex)), h(other))
  }
  def idSets(): Set[Set[QualifiedName]] = {
    Set(id, alternate1, alternate2)
  }

  def getAttributes :Set[Attribute]= {
    label.map(l =>new Label(pf.prov_label,l).asInstanceOf[Attribute])++typex++other.values.flatten
  }

  def toNotation(sb: StringBuilder): Unit = {
    val isExt = (id != null) || label.nonEmpty || typex.nonEmpty || other.nonEmpty

    sb ++= (if (isExt) "provext:alternateOf(" else "alternateOf(")
    optionalId(id, sb)
    idOrMarker(alternate1, sb)
    sb += ','
    idOrMarker(alternate2, sb)
    Attribute.toNotation(sb, label, typex, Set(), Set(), Set(), other)
    sb += ')'
  }
}

trait ImmutableWasInformedBy extends Relation with MultiIdentifiable with HasLabel with HasType with HasOther with Hashable {

  val informed: Set[QualifiedName]

  val informant: Set[QualifiedName]

  @BeanProperty
  val kind = PROV_COMMUNICATION
  val enumType: Kind.Value = Kind.winfob

  def getCause: Set[QualifiedName] = informant
  def getEffect: Set[QualifiedName] = informed

  def getInformant: Set[QualifiedName] = informant
  def getInformed: Set[QualifiedName] = informed

  def setInformed(x$1: org.openprovenance.prov.model.QualifiedName) = throw new UnsupportedOperationException
  def setInformant(x$1: org.openprovenance.prov.model.QualifiedName) = throw new UnsupportedOperationException

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

  override lazy val hashCode: Int = {
    pr(pr(pr(pr(pr(h(id), h(informed)), h(informant)), h(label)), h(typex)), h(other))

  }

  def idSets(): Set[Set[QualifiedName]] = {
    Set(id, informed, informant)
  }


  def getAttributes :Set[Attribute]= {
    label.map(l =>new Label(pf.prov_label,l).asInstanceOf[Attribute])++typex++other.values.flatten
  }

  def toNotation(sb: StringBuilder): Unit = {
    sb ++= "wasInformedBy("
    optionalId(id, sb)
    idOrMarker(informed, sb)
    sb += ','
    idOrMarker(informant, sb)
    Attribute.toNotation(sb, label, typex, Set(), Set(), Set(), other)
    sb += ')'
  }

}

trait ImmutableWasInfluencedBy extends Relation with MultiIdentifiable with HasLabel with HasType with HasOther with Hashable {

  val influencee: Set[QualifiedName]

  val influencer: Set[QualifiedName]

  @BeanProperty
  val kind = PROV_INFLUENCE
  val enumType: Kind.Value = Kind.winfl

  def getCause: Set[QualifiedName] = influencer
  def getEffect: Set[QualifiedName] = influencee

  def getInfluencer: Set[QualifiedName] = influencer
  def getInfluencee: Set[QualifiedName] = influencee

  def setInfluencee(x$1: org.openprovenance.prov.model.QualifiedName) = throw new UnsupportedOperationException
  def setInfluencer(x$1: org.openprovenance.prov.model.QualifiedName) = throw new UnsupportedOperationException

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

  override lazy val hashCode: Int = {
    pr(pr(pr(pr(pr(h(id), h(influencee)), h(influencer)), h(label)), h(typex)), h(other))

  }

  def idSets(): Set[Set[QualifiedName]] = {
    Set(id, influencee, influencer)
  }

  def getAttributes :Set[Attribute]= {
    label.map(l =>new Label(pf.prov_label,l).asInstanceOf[Attribute])++typex++other.values.flatten
  }

  def toNotation(sb: StringBuilder): Unit = {
    sb ++= "wasInfluencedBy("
    optionalId(id, sb)
    idOrMarker(influencee, sb)
    sb += ','
    idOrMarker(influencer, sb)
    Attribute.toNotation(sb, label, typex, Set(), Set(), Set(), other)
    sb += ')'
  }
}

trait ImmutableHadMember extends Relation with MultiIdentifiable with HasLabel with HasType with HasOther with Hashable {

  val collection: Set[QualifiedName]

  val entity: Set[QualifiedName]

  @BeanProperty
  val kind = PROV_MEMBERSHIP
  val enumType: Kind.Value = Kind.mem

  def getCause: Set[QualifiedName] = entity
  def getEffect: Set[QualifiedName] = collection

  def getCollection: Set[QualifiedName] = collection
  def setCollection(collection: org.openprovenance.prov.model.QualifiedName) = throw new UnsupportedOperationException
  def getEntity: Set[QualifiedName] = entity

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

  override lazy val hashCode: Int = {
    pr(pr(pr(pr(pr(h(id), h(collection)), h(entity)), h(label)), h(typex)), h(other))
  }

  def idSets(): Set[Set[QualifiedName]] = {
    Set(id, collection, entity)
  }

  def getAttributes :Set[Attribute]= {
    label.map(l =>new Label(pf.prov_label,l).asInstanceOf[Attribute])++typex++other.values.flatten
  }

  def toNotation(sb: StringBuilder): Unit = {
    val isExt = (id != null) || label.nonEmpty || typex.nonEmpty || other.nonEmpty
    sb ++= (if (isExt) "provext:hadMember(" else "hadMember(")
    optionalId(id, sb)
    idOrMarker(collection, sb)
    sb += ','
    idOrMarker(entity, sb)
    Attribute.toNotation(sb, label, typex, Set(), Set(), Set(), other)
    sb += ')'
  }

}

object Statement {
  def nullOption(q: QualifiedName): Set[QualifiedName] = {
    if (q == null) Set() else Set(q)
  }
  def apply(statement: org.openprovenance.prov.scala.immutable.Statement): Statement = {
    statement match {
      case e: org.openprovenance.prov.scala.immutable.Entity           => new Entity(e)
      case a: org.openprovenance.prov.scala.immutable.Activity         => new Activity(a)
      case ag: org.openprovenance.prov.scala.immutable.Agent           => new Agent(ag)
      case wdf: org.openprovenance.prov.scala.immutable.WasDerivedFrom => new WasDerivedFrom(wdf)
      // LUC: add other cases.
    }
  }
}

abstract class Statement extends Identifiable {
  val kind: org.openprovenance.prov.model.StatementOrBundle.Kind
  override def toString(): String = {
    val sb = new StringBuilder
    toNotation(sb)
    sb.toString()
  }
  def toNotation(sb: StringBuilder): Unit
  def toTerms: List[_ <: org.openprovenance.prov.scala.immutable.Statement]

  def unify(m: Map[QualifiedName, Set[QualifiedName]]): Statement

  def idSets(): Set[Set[QualifiedName]]

}

trait Mergeable[T <:Statement] {
  def merge(s: T): T
}

trait Relation {
  def getCause: Set[QualifiedName]
  def getEffect: Set[QualifiedName]
}

trait Serial[TYPE <: org.openprovenance.prov.scala.immutable.Statement] {
  def toTerms: List[TYPE]

  def addToFirst(ll: List[TYPE], attr: Set[Attribute]): List[TYPE] = {
    ll match {
      case (first :: rest) => addAttributes(first, attr) :: rest
      case _ => throw new IllegalStateException
    }
  }

  def addAttributes[TERM <: org.openprovenance.prov.scala.immutable.Statement](t: TERM, attr: Set[Attribute]): TERM = {
    t match {
      case ent: org.openprovenance.prov.scala.immutable.Entity =>
        pf.newEntity(ent.id, attr ++ org.openprovenance.prov.scala.immutable.Attribute(pf.getAttributes(ent))).asInstanceOf[TERM]
      case ag: org.openprovenance.prov.scala.immutable.Agent =>
        pf.newAgent(ag.id, attr ++ org.openprovenance.prov.scala.immutable.Attribute(pf.getAttributes(ag))).asInstanceOf[TERM]
      case act: org.openprovenance.prov.scala.immutable.Activity =>
        pf.newActivity(act.id, act.startTime, act.endTime, attr ++ org.openprovenance.prov.scala.immutable.Attribute(pf.getAttributes(act))).asInstanceOf[TERM]
      case wdf: org.openprovenance.prov.scala.immutable.WasDerivedFrom =>
        pf.newWasDerivedFrom(wdf.id, wdf.generatedEntity, wdf.usedEntity, wdf.activity, wdf.generation, wdf.usage, attr ++ org.openprovenance.prov.scala.immutable.Attribute(pf.getAttributes(wdf))).asInstanceOf[TERM]
      case wgb: org.openprovenance.prov.scala.immutable.WasGeneratedBy =>
        pf.newWasGeneratedBy(wgb.id, wgb.entity, wgb.activity, wgb.time, attr ++ org.openprovenance.prov.scala.immutable.Attribute(pf.getAttributes(wgb))).asInstanceOf[TERM]
      case usd: org.openprovenance.prov.scala.immutable.Used =>
        pf.newUsed(usd.id, usd.activity, usd.entity, usd.time, attr ++ org.openprovenance.prov.scala.immutable.Attribute(pf.getAttributes(usd))).asInstanceOf[TERM]
      case wib: org.openprovenance.prov.scala.immutable.WasInvalidatedBy =>
        pf.newWasInvalidatedBy(wib.id, wib.entity, wib.activity, wib.time, attr ++ org.openprovenance.prov.scala.immutable.Attribute(pf.getAttributes(wib))).asInstanceOf[TERM]
      case wsb: org.openprovenance.prov.scala.immutable.WasStartedBy =>
        pf.newWasStartedBy(wsb.id, wsb.activity, wsb.trigger, wsb.starter, wsb.time, attr ++ org.openprovenance.prov.scala.immutable.Attribute(pf.getAttributes(wsb))).asInstanceOf[TERM]
      case web: org.openprovenance.prov.scala.immutable.WasEndedBy =>
        pf.newWasEndedBy(web.id, web.activity, web.trigger, web.ender, web.time, attr ++ org.openprovenance.prov.scala.immutable.Attribute(pf.getAttributes(web))).asInstanceOf[TERM]
      case winflb: org.openprovenance.prov.scala.immutable.WasInfluencedBy =>
        pf.newWasInfluencedBy(winflb.id, winflb.influencee, winflb.influencer, attr ++ org.openprovenance.prov.scala.immutable.Attribute(pf.getAttributes(winflb))).asInstanceOf[TERM]
      case winfob: org.openprovenance.prov.scala.immutable.WasInformedBy =>
        pf.newWasInformedBy(winfob.id, winfob.informed, winfob.informant, attr ++ org.openprovenance.prov.scala.immutable.Attribute(pf.getAttributes(winfob))).asInstanceOf[TERM]
      case spe: org.openprovenance.prov.scala.immutable.SpecializationOf =>
        pf.newSpecializationOf(spe.id, spe.specificEntity, spe.generalEntity, attr ++ org.openprovenance.prov.scala.immutable.Attribute(pf.getAttributes(spe))).asInstanceOf[TERM]
      case alt: org.openprovenance.prov.scala.immutable.AlternateOf =>
        pf.newAlternateOf(alt.id, alt.alternate1, alt.alternate2, attr ++ org.openprovenance.prov.scala.immutable.Attribute(pf.getAttributes(alt))).asInstanceOf[TERM]
      case mem: org.openprovenance.prov.scala.immutable.HadMember =>
        pf.newHadMember(mem.id, mem.collection, mem.entity, attr ++ org.openprovenance.prov.scala.immutable.Attribute(pf.getAttributes(mem))).asInstanceOf[TERM]
      case wat: org.openprovenance.prov.scala.immutable.WasAttributedTo =>
        pf.newWasAttributedTo(wat.id, wat.entity, wat.agent, attr ++ org.openprovenance.prov.scala.immutable.Attribute(pf.getAttributes(wat))).asInstanceOf[TERM]
      case waw: org.openprovenance.prov.scala.immutable.WasAssociatedWith =>
        pf.newWasAssociatedWith(waw.id, waw.activity, waw.agent, waw.plan, attr ++ org.openprovenance.prov.scala.immutable.Attribute(pf.getAttributes(waw))).asInstanceOf[TERM]
      case del: org.openprovenance.prov.scala.immutable.ActedOnBehalfOf =>
        pf.newActedOnBehalfOf(del.id, del.delegate, del.responsible, del.activity, attr ++ org.openprovenance.prov.scala.immutable.Attribute(pf.getAttributes(del))).asInstanceOf[TERM]
      case _ => throw new IllegalStateException("Unknown statement type")
    }
  }
}

class Entity(val id: Set[QualifiedName],
             val label: Set[LangString],
             val typex: Set[Type],
             val value: Set[Value],
             val location: Set[Location],
             val other: Map[QualifiedName, Set[Other]]) extends Statement with Mergeable[Entity] with ImmutableEntity with Serial[org.openprovenance.prov.scala.immutable.Entity] {

  def this(ent: org.openprovenance.prov.scala.immutable.Entity) {
    this(Set(ent.id), ent.label, ent.typex, Set(ent.value).flatten, ent.location, ent.other)
  }

  override
  def merge(ent: Entity): Entity = {
    new Entity(id ++ ent.id, label ++ ent.label, typex ++ ent.typex, value ++ ent.value, location ++ ent.location, mapMerge(other, ent.other))
  }

  def toTerms: List[immutable.Entity] = {
    if (value.size > 1) {
      val attr: Set[Attribute] = (label.map(_.toLabel) ++ typex ++ location ++ other.values.flatten).asInstanceOf[Set[Attribute]]
      id.toList.flatMap(id1 => addToFirst(value.toList.map((v: Attribute) => pf.newEntity(id1, Set(v))), attr))
    } else {
      val attr: Set[Attribute] = (label.map(_.toLabel) ++ typex ++ value ++ location ++ other.values.flatten).asInstanceOf[Set[Attribute]]
      id.map(id1 => pf.newEntity(id1, attr)).toList
    }
  }

  def unify(m: Map[QualifiedName, Set[QualifiedName]]): Entity = {
    new Entity(id.flatMap(m), label, typex, value, location, other)
  }

}

class Agent(val id: Set[QualifiedName],
            val label: Set[LangString],
            val typex: Set[Type],
            val value: Set[Value],
            val location: Set[Location],
            val other: Map[QualifiedName, Set[Other]]) extends Statement with Mergeable[Agent] with ImmutableAgent with Serial[org.openprovenance.prov.scala.immutable.Agent] {

  def this(ag: org.openprovenance.prov.scala.immutable.Agent) {
    this(Set(ag.id), ag.label, ag.typex, Set(ag.value).flatten, ag.location, ag.other)
  }

  def merge(ag: Agent): Agent = {
    new Agent(id, label ++ ag.label, typex ++ ag.typex, value ++ ag.value, location ++ ag.location, mapMerge(other, ag.other))
  }

  def toTerms: List[immutable.Agent] = {
    if (value.size > 1) {
      val attr: Set[Attribute] = (label.map(_.toLabel) ++ typex ++ location ++ other.values.flatten).asInstanceOf[Set[Attribute]]
      id.toList.flatMap(id1 => addToFirst(value.toList.map((v: Attribute) => pf.newAgent(id1, Set(v))), attr))
    } else {
      val attr: Set[Attribute] = (label.map(_.toLabel) ++ typex ++ value ++ location ++ other.values.flatten).asInstanceOf[Set[Attribute]]
      id.toList.map(id1 => pf.newAgent(id1, attr))
    }
  }

  def unify(m: Map[QualifiedName, Set[QualifiedName]]): Agent = {
    new Agent(id.flatMap(m), label, typex, value, location, other)
  }

}

class Activity(val id: Set[QualifiedName],
               val startTime: Set[XMLGregorianCalendar],
               val endTime: Set[XMLGregorianCalendar],
               val label: Set[LangString],
               val typex: Set[Type],
               val location: Set[Location],
               val other: Map[QualifiedName, Set[Other]]) extends Statement with Mergeable[Activity] with ImmutableActivity with Serial[org.openprovenance.prov.scala.immutable.Activity] {

  @BeanProperty
  val kind = PROV_ACTIVITY
  val enumType: Kind.Value = Kind.act

  def this(act: org.openprovenance.prov.scala.immutable.Activity) {
    this(Set(act.id), Set(act.startTime).flatten, Set(act.endTime).flatten, act.label, act.typex, act.location, act.other)
  }

  def merge(act: Activity): Activity = {
    new Activity(id, startTime ++ act.startTime, endTime ++ act.endTime, label ++ act.label, typex ++ act.typex, location ++ act.location, mapMerge(other, act.other))
  }

  def toTerms: List[immutable.Activity] = {
    val ll1: MX_List[Set[QualifiedName], Set[XMLGregorianCalendar]] = MX_Cons2(startTime, MX_Cons2(endTime, MX_NIL[Set[QualifiedName], Set[XMLGregorianCalendar]]()))
    val attr: Set[Attribute] = (label.map(_.toLabel) ++ typex ++ location ++ other.values.flatten).asInstanceOf[Set[Attribute]]
    id.toList.flatMap(id1 => addToFirst(oneCombination(complete_args_list(elements_args_list(ll1))).map(createActivity(id1, _)), attr))

  }

  def unify(m: Map[QualifiedName, Set[QualifiedName]]): Activity = {
    new Activity(id.flatMap(m), startTime, endTime, label, typex, location, other)
  }

}

class WasDerivedFrom(val id: Set[QualifiedName],
                     val generatedEntity: Set[QualifiedName],
                     val usedEntity: Set[QualifiedName],
                     val activity: Set[QualifiedName],
                     val generation: Set[QualifiedName],
                     val usage: Set[QualifiedName],
                     val label: Set[LangString],
                     val typex: Set[Type],
                     val other: Map[QualifiedName, Set[Other]]) extends Statement with Mergeable[WasDerivedFrom] with ImmutableWasDerivedFrom with Serial[org.openprovenance.prov.scala.immutable.WasDerivedFrom] {

  def this(wdf: org.openprovenance.prov.scala.immutable.WasDerivedFrom) {
    this(nullOption(wdf.id), Set(wdf.generatedEntity), Set(wdf.usedEntity), nullOption(wdf.activity), nullOption(wdf.generation), nullOption(wdf.usage), wdf.label, wdf.typex, wdf.other)
  }

  def merge(wdf: WasDerivedFrom): WasDerivedFrom = {
    new WasDerivedFrom(id++wdf.id, generatedEntity ++ wdf.generatedEntity, usedEntity ++ wdf.usedEntity, activity ++ wdf.activity, generation ++ wdf.generation, usage ++ wdf.usage, label ++ wdf.label, typex ++ wdf.typex, mapMerge(other, wdf.other))
  }

  def toTerms: List[immutable.WasDerivedFrom] = {
    val ll1: MX_List[Set[QualifiedName], Set[XMLGregorianCalendar]] = MX_Cons1(generatedEntity, MX_Cons1(usedEntity, MX_Cons1(activity, MX_Cons1(generation, MX_Cons1(usage, MX_NIL[Set[QualifiedName], Set[XMLGregorianCalendar]]())))))
    val attr: Set[Attribute] = (label.map(_.toLabel) ++ typex ++ other.values.flatten).asInstanceOf[Set[Attribute]]
    (if (id.isEmpty) List(null) else id.toList).flatMap(id1 => addToFirst(someCombinations(id1, complete_args_list(elements_args_list(ll1))).map(createWasDerivedFrom(id1, _)), attr))
  }

  def unify(m: Map[QualifiedName, Set[QualifiedName]]): WasDerivedFrom = {
    new WasDerivedFrom(id.flatMap(m), generatedEntity.flatMap(m), usedEntity.flatMap(m), activity.flatMap(m), generation.flatMap(m), usage.flatMap(m), label, typex, other)
  }

}

class WasGeneratedBy(val id: Set[QualifiedName],
                     val entity: Set[QualifiedName],
                     val activity: Set[QualifiedName],
                     val time: Set[XMLGregorianCalendar],
                     val label: Set[LangString],
                     val typex: Set[Type],
                     val location: Set[Location],
                     val role: Set[Role],
                     val other: Map[QualifiedName, Set[Other]]) extends Statement with Mergeable[WasGeneratedBy] with ImmutableWasGeneratedBy with Serial[org.openprovenance.prov.scala.immutable.WasGeneratedBy] {

  def this() {
    this(Set(), Set(), Set(), Set(), Set(), Set(), Set(), Set(), Map())
  }

  def this(wgb: org.openprovenance.prov.scala.immutable.WasGeneratedBy) {
    this(nullOption(wgb.id), Set(wgb.entity), nullOption(wgb.activity), Set(wgb.time).flatten, wgb.label, wgb.typex, wgb.location, wgb.role, wgb.other)
  }

  def merge(wgb: WasGeneratedBy): WasGeneratedBy = {
    new WasGeneratedBy(id++wgb.id, entity ++ wgb.entity, activity ++ wgb.activity, time ++ wgb.time, label ++ wgb.label, typex ++ wgb.typex, location ++ wgb.location, role ++ wgb.role, mapMerge(other, wgb.other))
  }

  def toTerms: List[immutable.WasGeneratedBy] = {
    val ll1: MX_List[Set[QualifiedName], Set[XMLGregorianCalendar]] = MX_Cons1(entity, MX_Cons1(activity, MX_Cons2(time, MX_NIL[Set[QualifiedName], Set[XMLGregorianCalendar]]())))
    val attr: Set[Attribute] = (label.map(_.toLabel) ++ typex ++ location ++ role ++ other.values.flatten).asInstanceOf[Set[Attribute]]
    (if (id.isEmpty) List(null) else id.toList).flatMap(id1 => addToFirst(someCombinations(id1, complete_args_list(elements_args_list(ll1))).map(createWasGeneratedBy(id1, _)), attr))
  }

  def unify(m: Map[QualifiedName, Set[QualifiedName]]): WasGeneratedBy = {
    new WasGeneratedBy(id.flatMap(m), entity.flatMap(m), activity.flatMap(m), time, label, typex, location, role, other)
  }
}

class Used(val id: Set[QualifiedName],
           val activity: Set[QualifiedName],
           val entity: Set[QualifiedName],
           val time: Set[XMLGregorianCalendar],
           val label: Set[LangString],
           val typex: Set[Type],
           val location: Set[Location],
           val role: Set[Role],
           val other: Map[QualifiedName, Set[Other]]) extends Statement with Mergeable[Used] with ImmutableUsed with Serial[org.openprovenance.prov.scala.immutable.Used] {

  def this() {
    this(Set(), Set(), Set(), Set(), Set(), Set(), Set(), Set(), Map())
  }

  def this(usd: org.openprovenance.prov.scala.immutable.Used) {
    this(nullOption(usd.id), Set(usd.activity), nullOption(usd.entity), Set(usd.time).flatten, usd.label, usd.typex, usd.location, usd.role, usd.other)
  }

  def merge(usd: Used): Used = {
    new Used(id ++ usd.id, activity ++ usd.activity, entity ++ usd.entity, time ++ usd.time, label ++ usd.label, typex ++ usd.typex, location ++ usd.location, role ++ usd.role, mapMerge(other, usd.other))
  }

  def toTerms: List[immutable.Used] = {
    val ll1: MX_List[Set[QualifiedName], Set[XMLGregorianCalendar]] = MX_Cons1(activity, MX_Cons1(entity, MX_Cons2(time, MX_NIL[Set[QualifiedName], Set[XMLGregorianCalendar]]())))
    val attr: Set[Attribute] = (label.map(_.toLabel) ++ typex ++ location ++ role ++ other.values.flatten).asInstanceOf[Set[Attribute]]
    (if (id.isEmpty) List(null) else id.toList).flatMap(id1 => addToFirst(someCombinations(id1, complete_args_list(elements_args_list(ll1))).map(createUsed(id1, _)), attr))
  }

  def unify(m: Map[QualifiedName, Set[QualifiedName]]): Used = {
    new Used(id.flatMap(m), activity.flatMap(m), entity.flatMap(m), time, label, typex, location, role, other)
  }
}

class WasInvalidatedBy(val id: Set[QualifiedName],
                       val entity: Set[QualifiedName],
                       val activity: Set[QualifiedName],
                       val time: Set[XMLGregorianCalendar],
                       val label: Set[LangString],
                       val typex: Set[Type],
                       val location: Set[Location],
                       val role: Set[Role],
                       val other: Map[QualifiedName, Set[Other]]) extends Statement with Mergeable[WasInvalidatedBy] with ImmutableWasInvalidatedBy with Serial[org.openprovenance.prov.scala.immutable.WasInvalidatedBy] {

  def this() {
    this(Set(), Set(), Set(), Set(), Set(), Set(), Set(), Set(), Map())
  }

  def this(wib: org.openprovenance.prov.scala.immutable.WasInvalidatedBy) {
    this(nullOption(wib.id), Set(wib.entity), nullOption(wib.activity), Set(wib.time).flatten, wib.label, wib.typex, wib.location, wib.role, wib.other)
  }

  def merge(wib: WasInvalidatedBy): WasInvalidatedBy = {
    new WasInvalidatedBy(id ++ wib.id, entity ++ wib.entity, activity ++ wib.activity, time ++ wib.time, label ++ wib.label, typex ++ wib.typex, location ++ wib.location, role ++ wib.role, mapMerge(other, wib.other))
  }

  def toTerms: List[immutable.WasInvalidatedBy] = {
    val ll1: MX_List[Set[QualifiedName], Set[XMLGregorianCalendar]] = MX_Cons1(entity, MX_Cons1(activity, MX_Cons2(time, MX_NIL[Set[QualifiedName], Set[XMLGregorianCalendar]]())))
    val attr: Set[Attribute] = (label.map(_.toLabel) ++ typex ++ location ++ role ++ other.values.flatten).asInstanceOf[Set[Attribute]]
    (if (id.isEmpty) List(null) else id.toList).flatMap(id1 => addToFirst(someCombinations(id1, complete_args_list(elements_args_list(ll1))).map(createWasInvalidatedBy(id1, _)), attr))
  }

  def unify(m: Map[QualifiedName, Set[QualifiedName]]): WasInvalidatedBy = {
    new WasInvalidatedBy(id.flatMap(m), entity.flatMap(m), activity.flatMap(m), time, label, typex, location, role, other)
  }

}

class WasStartedBy(val id: Set[QualifiedName],
                   val activity: Set[QualifiedName],
                   val trigger: Set[QualifiedName],
                   val starter: Set[QualifiedName],
                   val time: Set[XMLGregorianCalendar],
                   val label: Set[LangString],
                   val typex: Set[Type],
                   val location: Set[Location],
                   val role: Set[Role],
                   val other: Map[QualifiedName, Set[Other]]) extends Statement with Mergeable[WasStartedBy] with ImmutableWasStartedBy with Serial[org.openprovenance.prov.scala.immutable.WasStartedBy] {

  def this() {
    this(Set(), Set(), Set(), Set(), Set(), Set(), Set(), Set(), Set(), Map())
  }

  def this(wsb: org.openprovenance.prov.scala.immutable.WasStartedBy) {
    this(nullOption(wsb.id), Set(wsb.activity), nullOption(wsb.trigger), nullOption(wsb.starter), Set(wsb.time).flatten, wsb.label, wsb.typex, wsb.location, wsb.role, wsb.other)
  }

  def merge(wsb: WasStartedBy): WasStartedBy = {
    new WasStartedBy(id ++ wsb.id, activity ++ wsb.activity, trigger ++ wsb.trigger, starter ++ wsb.starter, time ++ wsb.time, label ++ wsb.label, typex ++ wsb.typex, location ++ wsb.location, role ++ wsb.role, mapMerge(other, wsb.other))
  }

  def toTerms: List[immutable.WasStartedBy] = {
    val ll1: MX_List[Set[QualifiedName], Set[XMLGregorianCalendar]] = MX_Cons1(activity, MX_Cons1(trigger, MX_Cons1(starter, MX_Cons2(time, MX_NIL[Set[QualifiedName], Set[XMLGregorianCalendar]]()))))
    val attr: Set[Attribute] = (label.map(_.toLabel) ++ typex ++ location ++ role ++ other.values.flatten).asInstanceOf[Set[Attribute]]
    (if (id.isEmpty) List(null) else id.toList).flatMap(id1 => addToFirst(someCombinations(id1, complete_args_list(elements_args_list(ll1))).map(createWasStartedBy(id1, _)), attr))
  }

  def unify(m: Map[QualifiedName, Set[QualifiedName]]): WasStartedBy = {
    new WasStartedBy(id.flatMap(m), activity.flatMap(m), trigger.flatMap(m), starter.flatMap(m), time, label, typex, location, role, other)
  }

}

class WasEndedBy(val id: Set[QualifiedName],
                 val activity: Set[QualifiedName],
                 val trigger: Set[QualifiedName],
                 val ender: Set[QualifiedName],
                 val time: Set[XMLGregorianCalendar],
                 val label: Set[LangString],
                 val typex: Set[Type],
                 val location: Set[Location],
                 val role: Set[Role],
                 val other: Map[QualifiedName, Set[Other]]) extends Statement with Mergeable[WasEndedBy] with ImmutableWasEndedBy with Serial[org.openprovenance.prov.scala.immutable.WasEndedBy] {

  def this() {
    this(Set(), Set(), Set(), Set(), Set(), Set(), Set(), Set(), Set(), Map())
  }

  def this(web: org.openprovenance.prov.scala.immutable.WasEndedBy) {
    this(nullOption(web.id), Set(web.activity), nullOption(web.trigger), nullOption(web.ender), Set(web.time).flatten, web.label, web.typex, web.location, web.role, web.other)
  }

  def merge(web: WasEndedBy): WasEndedBy = {
    new WasEndedBy(id ++ web.id, activity ++ web.activity, trigger ++ web.trigger, ender ++ web.ender, time ++ web.time, label ++ web.label, typex ++ web.typex, location ++ web.location, role ++ web.role, mapMerge(other, web.other))
  }

  def toTerms: List[immutable.WasEndedBy] = {
    val ll1: MX_List[Set[QualifiedName], Set[XMLGregorianCalendar]] = MX_Cons1(activity, MX_Cons1(trigger, MX_Cons1(ender, MX_Cons2(time, MX_NIL[Set[QualifiedName], Set[XMLGregorianCalendar]]()))))
    val attr: Set[Attribute] = (label.map(_.toLabel) ++ typex ++ location ++ role ++ other.values.flatten).asInstanceOf[Set[Attribute]]
    (if (id.isEmpty) List(null) else id.toList).flatMap(id1 => addToFirst(someCombinations(id1, complete_args_list(elements_args_list(ll1))).map(createWasEndedBy(id1, _)), attr))
  }

  def unify(m: Map[QualifiedName, Set[QualifiedName]]): WasEndedBy = {
    new WasEndedBy(id.flatMap(m), activity.flatMap(m), trigger.flatMap(m), ender.flatMap(m), time, label, typex, location, role, other)
  }

}

class ActedOnBehalfOf(val id: Set[QualifiedName],
                      val delegate: Set[QualifiedName],
                      val responsible: Set[QualifiedName],
                      val activity: Set[QualifiedName],
                      val label: Set[LangString],
                      val typex: Set[Type],
                      val other: Map[QualifiedName, Set[Other]]) extends Statement with Mergeable[ActedOnBehalfOf] with ImmutableActedOnBehalfOf with Serial[org.openprovenance.prov.scala.immutable.ActedOnBehalfOf] {

  def this(aobo: org.openprovenance.prov.scala.immutable.ActedOnBehalfOf) {
    this(nullOption(aobo.id), Set(aobo.delegate), Set(aobo.responsible), nullOption(aobo.activity), aobo.label, aobo.typex, aobo.other)
  }

  def merge(aobo: ActedOnBehalfOf): ActedOnBehalfOf = {
    new ActedOnBehalfOf(id, delegate ++ aobo.delegate, responsible ++ aobo.responsible, activity ++ aobo.activity, label ++ aobo.label, typex ++ aobo.typex, mapMerge(other, aobo.other))
  }

  def toTerms: List[immutable.ActedOnBehalfOf] = {
    val ll1: MX_List[Set[QualifiedName], Set[XMLGregorianCalendar]] = MX_Cons1(delegate, MX_Cons1(responsible, MX_Cons1(activity, MX_NIL[Set[QualifiedName], Set[XMLGregorianCalendar]]())))
    val attr: Set[Attribute] = (label.map(_.toLabel) ++ typex ++ other.values.flatten).asInstanceOf[Set[Attribute]]
    (if (id.isEmpty) List(null) else id.toList).flatMap(id1 => addToFirst(someCombinations(id1, complete_args_list(elements_args_list(ll1))).map(createActedOnBehalfOf(id1, _)), attr))
  }

  def unify(m: Map[QualifiedName, Set[QualifiedName]]): ActedOnBehalfOf = {
    new ActedOnBehalfOf(id.flatMap(m), delegate.flatMap(m), responsible.flatMap(m), activity.flatMap(m), label, typex, other)
  }
}

class WasAssociatedWith(val id: Set[QualifiedName],
                        val activity: Set[QualifiedName],
                        val agent: Set[QualifiedName],
                        val plan: Set[QualifiedName],
                        val label: Set[LangString],
                        val typex: Set[Type],
                        val role: Set[Role],
                        val other: Map[QualifiedName, Set[Other]]) extends Statement with Mergeable[WasAssociatedWith] with ImmutableWasAssociatedWith with Serial[org.openprovenance.prov.scala.immutable.WasAssociatedWith] {

  def this(waw: org.openprovenance.prov.scala.immutable.WasAssociatedWith) {
    this(nullOption(waw.id), Set(waw.activity), nullOption(waw.agent), nullOption(waw.plan), waw.label, waw.typex, waw.role, waw.other)
  }

  def merge(waw: WasAssociatedWith): WasAssociatedWith = {
    new WasAssociatedWith(id, activity ++ waw.activity, agent ++ waw.agent, plan ++ waw.plan, label ++ waw.label, typex ++ waw.typex, role ++ waw.role, mapMerge(other, waw.other))
  }

  def toTerms: List[immutable.WasAssociatedWith] = {
    val ll1: MX_List[Set[QualifiedName], Set[XMLGregorianCalendar]] = MX_Cons1(activity, MX_Cons1(agent, MX_Cons1(plan, MX_NIL[Set[QualifiedName], Set[XMLGregorianCalendar]]())))
    val attr: Set[Attribute] = (label.map(_.toLabel) ++ typex ++ role ++ other.values.flatten).asInstanceOf[Set[Attribute]]
    (if (id.isEmpty) List(null) else id.toList).flatMap(id1 => addToFirst(someCombinations(id1, complete_args_list(elements_args_list(ll1))).map(createWasAssociatedWith(id1, _)), attr))
  }

  def unify(m: Map[QualifiedName, Set[QualifiedName]]): WasAssociatedWith = {
    new WasAssociatedWith(id.flatMap(m), activity.flatMap(m), agent.flatMap(m), plan.flatMap(m), label, typex, role, other)
  }
}

class WasAttributedTo(val id: Set[QualifiedName],
                      val entity: Set[QualifiedName],
                      val agent: Set[QualifiedName],
                      val label: Set[LangString],
                      val typex: Set[Type],
                      val other: Map[QualifiedName, Set[Other]]) extends Statement with Mergeable[WasAttributedTo] with ImmutableWasAttributedTo with Serial[org.openprovenance.prov.scala.immutable.WasAttributedTo] {

  def this(wat: org.openprovenance.prov.scala.immutable.WasAttributedTo) {
    this(nullOption(wat.id), Set(wat.entity), Set(wat.agent), wat.label, wat.typex, wat.other)
  }

  def merge(wat: WasAttributedTo): WasAttributedTo = {
    new WasAttributedTo(id, entity ++ wat.entity, agent ++ wat.agent, label ++ wat.label, typex ++ wat.typex, mapMerge(other, wat.other))
  }

  def toTerms: List[immutable.WasAttributedTo] = {
    val ll1: MX_List[Set[QualifiedName], Set[XMLGregorianCalendar]] = MX_Cons1(entity, MX_Cons1(agent, MX_NIL[Set[QualifiedName], Set[XMLGregorianCalendar]]()))
    val attr: Set[Attribute] = (label.map(_.toLabel) ++ typex ++ other.values.flatten).asInstanceOf[Set[Attribute]]
    (if (id.isEmpty) List(null) else id.toList).flatMap(id1 => addToFirst(someCombinations(id1, elements_args_list(ll1)).map(createWasAttributedTo(id1, _)), attr))
  }

  def unify(m: Map[QualifiedName, Set[QualifiedName]]): WasAttributedTo = {
    new WasAttributedTo(id.flatMap(m), entity.flatMap(m), agent.flatMap(m), label, typex, other)
  }
}

class SpecializationOf(val id: Set[QualifiedName],
                       val specificEntity: Set[QualifiedName],
                       val generalEntity: Set[QualifiedName],
                       val label: Set[LangString],
                       val typex: Set[Type],
                       val other: Map[QualifiedName, Set[Other]]) extends Statement with Mergeable[SpecializationOf] with ImmutableSpecializationOf with Serial[org.openprovenance.prov.scala.immutable.SpecializationOf] {

  def this(spec: org.openprovenance.prov.scala.immutable.SpecializationOf) {
    this(nullOption(spec.id), Set(spec.specificEntity), Set(spec.generalEntity), spec.label, spec.typex, spec.other)
  }

  def merge(spe: SpecializationOf): SpecializationOf = {
    new SpecializationOf(id, specificEntity ++ spe.specificEntity, generalEntity ++ spe.generalEntity, label ++ spe.label, typex ++ spe.typex, mapMerge(other, spe.other))
  }

  def toTerms: List[immutable.SpecializationOf] = {
    val ll1: MX_List[Set[QualifiedName], Set[XMLGregorianCalendar]] = MX_Cons1(specificEntity, MX_Cons1(generalEntity, MX_NIL[Set[QualifiedName], Set[XMLGregorianCalendar]]()))
    val attr: Set[Attribute] = (label.map(_.toLabel) ++ typex ++ other.values.flatten).asInstanceOf[Set[Attribute]]
    (if (id.isEmpty) List(null) else id.toList).flatMap(id1 => addToFirst(someCombinations(id1, elements_args_list(ll1)).map(createSpecializationOf(id1, _)), attr))
  }

  def unify(m: Map[QualifiedName, Set[QualifiedName]]): SpecializationOf = {
    new SpecializationOf(id.flatMap(m), specificEntity.flatMap(m), generalEntity.flatMap(m), label, typex, other)
  }

}

class AlternateOf(val id: Set[QualifiedName],
                  val alternate1: Set[QualifiedName],
                  val alternate2: Set[QualifiedName],
                  val label: Set[LangString],
                  val typex: Set[Type],
                  val other: Map[QualifiedName, Set[Other]]) extends Statement with Mergeable[AlternateOf] with ImmutableAlternateOf with Serial[org.openprovenance.prov.scala.immutable.AlternateOf] {

  def this(alt: org.openprovenance.prov.scala.immutable.AlternateOf) {
    this(nullOption(alt.id), Set(alt.alternate1), Set(alt.alternate2), alt.label, alt.typex, alt.other)
  }

  def merge(alt: AlternateOf): AlternateOf = {
    new AlternateOf(id, alternate1 ++ alt.alternate1, alternate2 ++ alt.alternate2, label ++ alt.label, typex ++ alt.typex, mapMerge(other, alt.other))
  }

  def toTerms: List[immutable.AlternateOf] = {
    val ll1: MX_List[Set[QualifiedName], Set[XMLGregorianCalendar]] = MX_Cons1(alternate1, MX_Cons1(alternate2, MX_NIL[Set[QualifiedName], Set[XMLGregorianCalendar]]()))
    val attr: Set[Attribute] = (label.map(_.toLabel) ++ typex ++ other.values.flatten).asInstanceOf[Set[Attribute]]
    (if (id.isEmpty) List(null) else id.toList).flatMap(id1 => addToFirst(someCombinations(id1, elements_args_list(ll1)).map(createAlternateOf(id1, _)), attr))
  }

  def unify(m: Map[QualifiedName, Set[QualifiedName]]): AlternateOf = {
    new AlternateOf(id.flatMap(m), alternate1.flatMap(m), alternate2.flatMap(m), label, typex, other)
  }

}

class WasInformedBy(val id: Set[QualifiedName],
                    val informed: Set[QualifiedName],
                    val informant: Set[QualifiedName],
                    val label: Set[LangString],
                    val typex: Set[Type],
                    val other: Map[QualifiedName, Set[Other]]) extends Statement with Mergeable[WasInformedBy] with ImmutableWasInformedBy with Serial[org.openprovenance.prov.scala.immutable.WasInformedBy] {

  def this(winfb: org.openprovenance.prov.scala.immutable.WasInformedBy) {
    this(nullOption(winfb.id), Set(winfb.informed), Set(winfb.informant), winfb.label, winfb.typex, winfb.other)
  }

  def merge(winfb: WasInformedBy): WasInformedBy = {
    new WasInformedBy(id, informed ++ winfb.informed, informant ++ winfb.informant, label ++ winfb.label, typex ++ winfb.typex, mapMerge(other, winfb.other))
  }

  def toTerms: List[immutable.WasInformedBy] = {
    val ll1: MX_List[Set[QualifiedName], Set[XMLGregorianCalendar]] = MX_Cons1(informed, MX_Cons1(informant, MX_NIL[Set[QualifiedName], Set[XMLGregorianCalendar]]()))
    val attr: Set[Attribute] = (label.map(_.toLabel) ++ typex ++ other.values.flatten).asInstanceOf[Set[Attribute]]
    (if (id.isEmpty) List(null) else id.toList).flatMap(id1 => addToFirst(someCombinations(id1, elements_args_list(ll1)).map(createWasInformedBy(id1, _)), attr))
  }

  def unify(m: Map[QualifiedName, Set[QualifiedName]]): WasInformedBy = {
    new WasInformedBy(id.flatMap(m), informed.flatMap(m), informant.flatMap(m), label, typex, other)
  }
}

class WasInfluencedBy(val id: Set[QualifiedName],
                      val influencee: Set[QualifiedName],
                      val influencer: Set[QualifiedName],
                      val label: Set[LangString],
                      val typex: Set[Type],
                      val other: Map[QualifiedName, Set[Other]]) extends Statement with Mergeable[WasInfluencedBy] with ImmutableWasInfluencedBy with Serial[org.openprovenance.prov.scala.immutable.WasInfluencedBy] {

  def this(winflb: org.openprovenance.prov.scala.immutable.WasInfluencedBy) {
    this(nullOption(winflb.id), Set(winflb.influencee), Set(winflb.influencer), winflb.label, winflb.typex, winflb.other)
  }

  def merge(winflb: WasInfluencedBy): WasInfluencedBy = {
    new WasInfluencedBy(id, influencee ++ winflb.influencee, influencer ++ winflb.influencer, label ++ winflb.label, typex ++ winflb.typex, mapMerge(other, winflb.other))
  }

  def toTerms: List[immutable.WasInfluencedBy] = {
    val ll1: MX_List[Set[QualifiedName], Set[XMLGregorianCalendar]] = MX_Cons1(influencee, MX_Cons1(influencer, MX_NIL[Set[QualifiedName], Set[XMLGregorianCalendar]]()))
    val attr: Set[Attribute] = (label.map(_.toLabel) ++ typex ++ other.values.flatten).asInstanceOf[Set[Attribute]]
    (if (id.isEmpty) List(null) else id.toList).flatMap(id1 => addToFirst(someCombinations(id1, elements_args_list(ll1)).map(createWasInfluencedBy(id1, _)), attr))
  }

  def unify(m: Map[QualifiedName, Set[QualifiedName]]): WasInfluencedBy = {
    new WasInfluencedBy(id.flatMap(m), influencee.flatMap(m), influencer.flatMap(m), label, typex, other)
  }

}

class HadMember(val id: Set[QualifiedName],
                val collection: Set[QualifiedName],
                val entity: Set[QualifiedName],
                val label: Set[LangString],
                val typex: Set[Type],
                val other: Map[QualifiedName, Set[Other]]) extends Statement with Mergeable[HadMember] with ImmutableHadMember with Serial[org.openprovenance.prov.scala.immutable.HadMember] {

  def this(mem: org.openprovenance.prov.scala.immutable.HadMember) {
    this(nullOption(mem.id), Set(mem.collection), mem.entity, mem.label, mem.typex, mem.other)
  }

  def merge(mem: HadMember): HadMember = {
    new HadMember(id, collection ++ mem.collection, entity ++ mem.entity, label ++ mem.label, typex ++ mem.typex, mapMerge(other, mem.other))
  }

  def toTerms: List[immutable.HadMember] = {
    val ll1: MX_List[Set[QualifiedName], Set[XMLGregorianCalendar]] = MX_Cons1(collection, MX_Cons1(entity, MX_NIL[Set[QualifiedName], Set[XMLGregorianCalendar]]()))
    val attr: Set[Attribute] = (label.map(_.toLabel) ++ typex ++ other.values.flatten).asInstanceOf[Set[Attribute]]
    (if (id.isEmpty) List(null) else id.toList).flatMap(id1 => addToFirst(someCombinations(id1, elements_args_list(ll1)).map(createHadMember(id1, _)), attr))
  }

  def unify(m: Map[QualifiedName, Set[QualifiedName]]): HadMember = {
    new HadMember(id.flatMap(m), collection.flatMap(m), entity.flatMap(m), label, typex, other)
  }
}

object Bundle {
  def apply(bun: org.openprovenance.prov.model.Bundle): Bundle = {
    bun match {
      case b: Bundle                               => b
      case b: org.openprovenance.prov.model.Bundle => {
        Normalizer.inNormalFormBundle(b.asInstanceOf[org.openprovenance.prov.scala.immutable.Bundle])
      }
    }
  }
}

class Bundle(val id: Set[QualifiedName],
             val statement: Set[Statement],
             @BeanProperty val namespace: Namespace) extends MultiIdentifiable with Hashable with HasNamespace {

  @BeanProperty
  val kind = PROV_BUNDLE
  val enumType: Kind.Value = Kind.bun

  def canEqual(a: Any): Boolean = a.isInstanceOf[Bundle]

  override def equals(that: Any): Boolean =
    that match {
      case that: Bundle => that.canEqual(this) &&
        this.id == that.id &&
        this.statement == that.statement
      case _ => false
    }

  override lazy val hashCode: Int = {
    pr(h(id), h(statement))
  }

  def setNamespace(x: org.openprovenance.prov.model.Namespace): Unit = {
    throw new UnsupportedOperationException
  }
  def idSets(): Set[Set[QualifiedName]] = {
    Set(id)
  }
  def toNotation(sb: StringBuilder): Unit = {
    sb ++= "bundle "
    sb ++= id.toString()
    sb += '\n'
    printNamespace(sb)
    //statement.addString(sb, "    ", "\n    ", "\n")
    val currentNS = new Namespace(Namespace.getThreadNamespace())
    Namespace.withThreadNamespace(namespace);
    addString2(sb, statement, "    ", "\n    ", "\n")
    Namespace.withThreadNamespace(currentNS);

    sb ++= "  endBundle"
  }
  override def toString(): String = {
    val sb = new StringBuilder
    toNotation(sb)
    sb.toString()
  }

  // See https://github.com/scala/scala/blob/v2.10.2/src/library/scala/collection/TraversableOnce.scala#L316
  // extended to support a stringbuilder
  def addString2[T <: Statement](b: StringBuilder, set: IterableOnce[T], start: String, sep: String, end: String): StringBuilder = {
    var first = true

    b append start
    for (x <- set) {
      if (first) {
        x.toNotation(b)
        first = false
      } else {
        b append sep
        x.toNotation(b)
      }
    }
    b append end
    b
  }

}


object Normalizer {
  def newNormalForm(e: org.openprovenance.prov.scala.immutable.Entity)            = new Entity(e)
  def newNormalForm(e: org.openprovenance.prov.scala.immutable.Activity)          = new Activity(e)
  def newNormalForm(e: org.openprovenance.prov.scala.immutable.Agent)             = new Agent(e)
  def newNormalForm(e: org.openprovenance.prov.scala.immutable.WasDerivedFrom)    = new WasDerivedFrom(e)
  def newNormalForm(e: org.openprovenance.prov.scala.immutable.WasGeneratedBy)    = new WasGeneratedBy(e)
  def newNormalForm(e: org.openprovenance.prov.scala.immutable.Used)              = new Used(e)
  def newNormalForm(e: org.openprovenance.prov.scala.immutable.WasInvalidatedBy)  = new WasInvalidatedBy(e)
  def newNormalForm(e: org.openprovenance.prov.scala.immutable.WasStartedBy)      = new WasStartedBy(e)
  def newNormalForm(e: org.openprovenance.prov.scala.immutable.WasEndedBy)        = new WasEndedBy(e)
  def newNormalForm(e: org.openprovenance.prov.scala.immutable.WasAttributedTo)   = new WasAttributedTo(e)
  def newNormalForm(e: org.openprovenance.prov.scala.immutable.WasAssociatedWith) = new WasAssociatedWith(e)
  def newNormalForm(e: org.openprovenance.prov.scala.immutable.ActedOnBehalfOf)   = new ActedOnBehalfOf(e)
  def newNormalForm(e: org.openprovenance.prov.scala.immutable.WasInfluencedBy)   = new WasInfluencedBy(e)
  def newNormalForm(e: org.openprovenance.prov.scala.immutable.WasInformedBy)     = new WasInformedBy(e)
  def newNormalForm(e: org.openprovenance.prov.scala.immutable.SpecializationOf)  = new SpecializationOf(e)
  def newNormalForm(e: org.openprovenance.prov.scala.immutable.AlternateOf)       = new AlternateOf(e)
  def newNormalForm(e: org.openprovenance.prov.scala.immutable.HadMember)         = new HadMember(e)

  def inNormalFormBundle(b: org.openprovenance.prov.scala.immutable.Bundle): Bundle = {
    val ss = b.statement
    val ss2=ss.map(s => inNormalForm(s)).toSet
    new Bundle(Set(b.id),
      ss2,
      b.getNamespace)
  }

  def inNormalForm(s: org.openprovenance.prov.scala.immutable.Statement): Statement = {
    s match {
      case a: org.openprovenance.prov.scala.immutable.Entity            => newNormalForm(a)
      case a: org.openprovenance.prov.scala.immutable.Activity          => newNormalForm(a)
      case a: org.openprovenance.prov.scala.immutable.Agent             => newNormalForm(a)
      case a: org.openprovenance.prov.scala.immutable.WasDerivedFrom    => newNormalForm(a)
      case a: org.openprovenance.prov.scala.immutable.WasGeneratedBy    => newNormalForm(a)
      case a: org.openprovenance.prov.scala.immutable.Used              => newNormalForm(a)
      case a: org.openprovenance.prov.scala.immutable.WasInvalidatedBy  => newNormalForm(a)
      case a: org.openprovenance.prov.scala.immutable.WasStartedBy      => newNormalForm(a)
      case a: org.openprovenance.prov.scala.immutable.WasEndedBy        => newNormalForm(a)
      case a: org.openprovenance.prov.scala.immutable.ActedOnBehalfOf   => newNormalForm(a)
      case a: org.openprovenance.prov.scala.immutable.WasAssociatedWith => newNormalForm(a)
      case a: org.openprovenance.prov.scala.immutable.WasAttributedTo   => newNormalForm(a)
      case a: org.openprovenance.prov.scala.immutable.WasInfluencedBy   => newNormalForm(a)
      case a: org.openprovenance.prov.scala.immutable.WasInformedBy     => newNormalForm(a)
      case a: org.openprovenance.prov.scala.immutable.SpecializationOf  => newNormalForm(a)
      case a: org.openprovenance.prov.scala.immutable.AlternateOf       => newNormalForm(a)
      case a: org.openprovenance.prov.scala.immutable.HadMember         => newNormalForm(a)
    }

  }

  def merge[T <: Statement with Mergeable[T]](s1: T,s2: T): T = {
    s1.merge(s2).asInstanceOf[T]
  }


  def mergeList[Tx <: Statement with Mergeable[Tx]](ss: Set[Tx]) : Tx = {
    ss.reduce((s1,s2)=>merge(s1,s2))
  }

  def mergeList2(ss: Set[Statement]) : Statement ={
    ss.head match {
      case s: Entity            => mergeList(ss.asInstanceOf[Set[Entity]])
      case s: Activity          => mergeList(ss.asInstanceOf[Set[Activity]])
      case s: Agent             => mergeList(ss.asInstanceOf[Set[Agent]])
      case s: WasDerivedFrom    => mergeList(ss.asInstanceOf[Set[WasDerivedFrom]])
      case s: WasGeneratedBy    => mergeList(ss.asInstanceOf[Set[WasGeneratedBy]])
      case s: Used              => mergeList(ss.asInstanceOf[Set[Used]])
      case s: WasInvalidatedBy  => mergeList(ss.asInstanceOf[Set[WasInvalidatedBy]])
      case s: WasStartedBy      => mergeList(ss.asInstanceOf[Set[WasStartedBy]])
      case s: WasEndedBy        => mergeList(ss.asInstanceOf[Set[WasEndedBy]])
      case s: ActedOnBehalfOf   => mergeList(ss.asInstanceOf[Set[ActedOnBehalfOf]])
      case s: WasAssociatedWith => mergeList(ss.asInstanceOf[Set[WasAssociatedWith]])
      case s: WasAttributedTo   => mergeList(ss.asInstanceOf[Set[WasAttributedTo]])
      case s: WasInfluencedBy   => mergeList(ss.asInstanceOf[Set[WasInfluencedBy]])
      case s: WasInformedBy     => mergeList(ss.asInstanceOf[Set[WasInformedBy]])
      case s: SpecializationOf  => mergeList(ss.asInstanceOf[Set[SpecializationOf]])
      case s: AlternateOf       => mergeList(ss.asInstanceOf[Set[AlternateOf]])
      case s: HadMember         => mergeList(ss.asInstanceOf[Set[HadMember]])
    }
  }



  def makeMembership(equiv: Iterable[Set[QualifiedName]]): Map[QualifiedName, Set[QualifiedName]] = {
    val tc = new TransitiveClosure2[QualifiedName]
    equiv.foreach { s => tc.add2(s) }
    val part = tc.transitiveClosure()
    part
  }


  case class IndexingPair(k: Any, kind: org.openprovenance.prov.model.StatementOrBundle.Kind) {

    @inline private final def pr(v0: Int, v1: Int): Int = {
      prime * v0 + v1
    }

    final private val prime = 37

    override lazy val hashCode: Int = {  // caching hashCode.
      pr(k.hashCode,kind.hashCode)
    }

  }


  def makeIndex(ss: Set[Statement], key: Statement=>Any, activeKey: Any=>Boolean): Map[IndexingPair, Set[Statement]] = {
    val grp=ss.groupBy { s => IndexingPair(key(s),s.kind) }

    val index=grp.map{case (k,s)=>(k,if (activeKey(k.k)) Set(mergeList2(s)) else s)}  // merge if we have an id!

    index
  }

  def applyPartition(index: Map[IndexingPair, Set[Statement]]): Iterable[Set[QualifiedName]] = {
    index.values.flatMap{_.flatMap(_.idSets())}
  }


  def index_membership(ss: Set[Statement], key: Statement=>Any, activeKey: Any=>Boolean, equiv_init: Set[Set[QualifiedName]]): (Map[IndexingPair, Set[Statement]], Map[QualifiedName, Set[QualifiedName]]) = {

    val index=makeIndex(ss,key,activeKey)

    //val equiv=index.values.flatMap{_.flatMap(_.idSets())}
    val equiv=applyPartition(index)

    val psi=makeMembership(equiv ++ equiv_init)

    (index,psi)
  }

  def index_membership1(ss: Set[Statement], equiv_init: Set[Set[QualifiedName]]=Set()): (Map[IndexingPair, Set[Statement]], Map[QualifiedName, Set[QualifiedName]]) = {
    index_membership(ss,
      s=>s.asInstanceOf[MultiIdentifiable].id,
      {idset:Any=>idset.asInstanceOf[Set[QualifiedName]].nonEmpty},
      equiv_init)
  }


  def index_membership2(ss: Set[Statement], equiv_init: Set[Set[QualifiedName]]=Set()): (Map[IndexingPair, Set[Statement]], Map[QualifiedName, Set[QualifiedName]]) = {
    index_membership(ss,
      {
        case wgb: WasGeneratedBy   => if (wgb.entity.isEmpty  || wgb.activity.isEmpty) None else Some(ComplexKey(wgb))
        case wib: WasInvalidatedBy => if (wib.entity.isEmpty  || wib.activity.isEmpty) None else Some(ComplexKey(wib))
        case wsb: WasStartedBy     => if (wsb.activity.isEmpty || wsb.starter.isEmpty) None else Some(ComplexKey(wsb))
        case web: WasEndedBy       => if (web.activity.isEmpty || web.ender.isEmpty)   None else Some(ComplexKey(web))
        case _ => None
      },
      {
        case None => false
        case _ => true
      },
      equiv_init)
  }



  def fusion1(ss: Set[Statement]): (Set[Statement], Map[QualifiedName,Set[QualifiedName]]) = {
    val (index,psi)=index_membership1(ss)
    //println("-unif1 " + ss)
    //println("-index1 " + index)
    (index.values.flatMap(_.map(_.unify(psi))).toSet,psi)
  }

  def fusion1(b: Bundle): Bundle = {
    val ss: Set[Statement]=b.statement
    val (index,psi)=index_membership1(ss,Set(b.id))
    //println("-unif1 " + ss)
    //println("-index1 " + index)
    new Bundle(b.id.flatMap(psi),
      index.values.flatMap(_.map(_.unify(psi))).toSet,
      b.namespace)
  }


  def fusion2(ss: Set[Statement], psi: Map[QualifiedName,Set[QualifiedName]]): (Set[Statement],Map[QualifiedName,Set[QualifiedName]]) = {
    val (index2,psi2)=index_membership2(ss)
    //println("-unify2 " + ss)
    //println("-index2 " + index)
    if (psi2==psi) {
      (index2.values.flatten.toSet,psi)
    } else {
      (index2.values.flatMap(_.map(_.unify(psi2))).toSet,psi2)
    }
  }

  def fusion2(b: Bundle): Bundle = {
    val ss: Set[Statement]=b.statement
    val (index,psi)=index_membership2(ss,Set(b.id))
    new Bundle(b.id.flatMap(psi),
      index.values.flatMap(_.map(_.unify(psi))).toSet,
      b.namespace)
  }

  @tailrec
  def fusion(ss: Set[Statement], psi: Option[Map[QualifiedName,Set[QualifiedName]]]): Set[Statement] = {
    //println("unify " + ss)
    val (ss1, psi1)=fusion1(ss)
    val (ss2, psi2)=fusion2(ss1,psi1)
    // if (psi1==psi2) {
    //  ss2
    if (ss==ss2) {
      ss
    } else {
      fusion(ss2, Some(psi2))
    }
  }

  @tailrec
  def fusion(b: Bundle): Bundle = {
    //println("unify " + b)
    val b2=fusion2(fusion1(b))
    if (b==b2) {
      b
    } else {
      fusion(b2)
    }
  }

  def mergeNamespaces(ns1: Namespace, ns2: Namespace): Namespace = {
    val ns=new Namespace
    ns.extendWith(ns1)
    ns.extendWith(ns2)
    ns
  }

  def mergeBundles(b1:Bundle, b2:Bundle): Bundle = {
    new Bundle(b1.id++b2.id,b1.statement++b2.statement,mergeNamespaces(b1.namespace,b2.namespace))
  }

  def fusionBundles(bs: Set[Bundle]): Set[Bundle] = {
    val bs2=bs.groupBy { b => b.id}.mapValues{ ll => ll.reduce{(b1,b2)=>mergeBundles(b1,b2)}}.values.toSet
    bs2.map(fusion)
  }


  def fusion(doc: org.openprovenance.prov.scala.immutable.Document): DocumentProxyFromStatements  = {
    val ss=doc.statements().map(x => inNormalForm(x)).toSet
    val bs=fusionBundles(doc.bundles().map(x => inNormalFormBundle(x)).toSet)

    val ss2=fusion(ss,None)

    bs.foreach { b => b.namespace.setParent(doc.namespace) }
    new DocumentProxyFromStatements(ss2,bs,doc.namespace)

  }
}

object StatementIndexer {

  def add(m: Map[QualifiedName, Entity], e: org.openprovenance.prov.scala.immutable.Entity): Map[QualifiedName, Entity] = {
    extend(m,e.id,new Entity(e))
  }

  def add(m: Map[QualifiedName, Activity], a: org.openprovenance.prov.scala.immutable.Activity): Map[QualifiedName, Activity] = {
    extend(m,a.id,new Activity(a))
  }

  def add(m: Map[QualifiedName, Agent], a: org.openprovenance.prov.scala.immutable.Agent): Map[QualifiedName, Agent] = {
    extend(m,a.id,new Agent(a))
  }

  def add(m: Map[QualifiedName, WasDerivedFrom], a: org.openprovenance.prov.scala.immutable.WasDerivedFrom): Map[QualifiedName, WasDerivedFrom] = {
    extend(m,a.id,new WasDerivedFrom(a))
  }

  def add(m: Map[QualifiedName, WasGeneratedBy], a: org.openprovenance.prov.scala.immutable.WasGeneratedBy): Map[QualifiedName, WasGeneratedBy] = {
    extend(m,a.id,new WasGeneratedBy(a)) //LUC CHECK attrribute
  }

  def add(m: Map[QualifiedName, Used], a: org.openprovenance.prov.scala.immutable.Used): Map[QualifiedName, Used] = {
    extend(m,a.id,new Used(a))
  }

  def add(m: Map[QualifiedName, WasInvalidatedBy], a: org.openprovenance.prov.scala.immutable.WasInvalidatedBy): Map[QualifiedName, WasInvalidatedBy] = {
    extend(m,a.id,new WasInvalidatedBy(a))
  }
  def add(m: Map[QualifiedName, WasStartedBy], a: org.openprovenance.prov.scala.immutable.WasStartedBy): Map[QualifiedName, WasStartedBy] = {
    extend(m,a.id,new WasStartedBy(a))
  }
  def add(m: Map[QualifiedName, WasEndedBy], a: org.openprovenance.prov.scala.immutable.WasEndedBy): Map[QualifiedName, WasEndedBy] = {
    extend(m,a.id,new WasEndedBy(a))
  }
  def add(m: Map[QualifiedName, WasAssociatedWith], a: org.openprovenance.prov.scala.immutable.WasAssociatedWith): Map[QualifiedName, WasAssociatedWith] = {
    extend(m,a.id,new WasAssociatedWith(a))
  }
  def add(m: Map[QualifiedName, WasAttributedTo], a: org.openprovenance.prov.scala.immutable.WasAttributedTo): Map[QualifiedName, WasAttributedTo] = {
    extend(m,a.id,new WasAttributedTo(a))
  }
  def add(m: Map[QualifiedName, ActedOnBehalfOf], a: org.openprovenance.prov.scala.immutable.ActedOnBehalfOf): Map[QualifiedName, ActedOnBehalfOf] = {
    extend(m,a.id,new ActedOnBehalfOf(a))
  }
  def add(m: Map[QualifiedName, WasInformedBy], a: org.openprovenance.prov.scala.immutable.WasInformedBy): Map[QualifiedName, WasInformedBy] = {
    extend(m,a.id,new WasInformedBy(a))
  }
  def add(m: Map[QualifiedName, WasInfluencedBy], a: org.openprovenance.prov.scala.immutable.WasInfluencedBy): Map[QualifiedName, WasInfluencedBy] = {
    extend(m,a.id,new WasInfluencedBy(a))
  }
  def add(m: Map[QualifiedName, SpecializationOf], a: org.openprovenance.prov.scala.immutable.SpecializationOf): Map[QualifiedName, SpecializationOf] = {
    extend(m,a.id,new SpecializationOf(a))
  }
  def add(m: Map[QualifiedName, AlternateOf], a: org.openprovenance.prov.scala.immutable.AlternateOf): Map[QualifiedName, AlternateOf] = {
    extend(m,a.id,new AlternateOf(a))
  }
  def add(m: Map[QualifiedName, HadMember], a: org.openprovenance.prov.scala.immutable.HadMember): Map[QualifiedName, HadMember] = {
    extend(m,a.id,new HadMember(a))
  }

  def extend[T <: Statement with Mergeable[T]](m: Map[QualifiedName, T], id: QualifiedName, a: T): Map[QualifiedName, T] = {
    m.get(id) match {
      case None     => m + (id -> a)
      case Some(nf) => m ++ Set((id, nf.merge(a)))
    }
  }


}


class StatementIndexer(
                        val entity: Map[QualifiedName, Entity],
                        val activity: Map[QualifiedName, Activity],
                        val agent: Map[QualifiedName, Agent],
                        val wasDerivedFrom: Map[QualifiedName, WasDerivedFrom],
                        val wasGeneratedBy: Map[QualifiedName, WasGeneratedBy],
                        val used: Map[QualifiedName, Used],
                        val wasInvalidatedBy: Map[QualifiedName, WasInvalidatedBy],
                        val wasStartedBy: Map[QualifiedName, WasStartedBy],
                        val wasEndedBy: Map[QualifiedName, WasEndedBy],
                        val actedOnBehalfOf: Map[QualifiedName, ActedOnBehalfOf],
                        val wasAssociatedWith: Map[QualifiedName, WasAssociatedWith],
                        val wasAttributedTo: Map[QualifiedName, WasAttributedTo],
                        val wasInfluencedBy: Map[QualifiedName, WasInfluencedBy],
                        val wasInformedBy: Map[QualifiedName, WasInformedBy],
                        val specializationOf: Map[QualifiedName, SpecializationOf],
                        val alternateOf: Map[QualifiedName, AlternateOf],
                        val hadMember: Map[QualifiedName, HadMember]) extends Hashable {

  import StatementIndexer.extend

  def toNotation(sb: StringBuilder) : Unit = {
    if (entity.values.nonEmpty) addString(sb, entity.values, "  ", "\n  ", "\n")
    if (activity.values.nonEmpty) addString(sb, activity.values, "  ", "\n  ", "\n")
    if (agent.values.nonEmpty) addString(sb, agent.values, "  ", "\n  ", "\n")
    if (wasDerivedFrom.values.nonEmpty) addString(sb, wasDerivedFrom.values, "  ", "\n  ", "\n")
    if (wasGeneratedBy.values.nonEmpty) addString(sb, wasGeneratedBy.values, "  ", "\n  ", "\n")
    if (wasInvalidatedBy.values.nonEmpty) addString(sb, wasInvalidatedBy.values, "  ", "\n  ", "\n")
    if (used.values.nonEmpty) addString(sb, used.values, "  ", "\n  ", "\n")
    if (wasStartedBy.values.nonEmpty) addString(sb, wasStartedBy.values, "  ", "\n  ", "\n")
    if (wasEndedBy.values.nonEmpty) addString(sb, wasEndedBy.values, "  ", "\n  ", "\n")
    if (actedOnBehalfOf.values.nonEmpty) addString(sb, actedOnBehalfOf.values, "  ", "\n  ", "\n")
    if (wasAssociatedWith.values.nonEmpty) addString(sb, wasAssociatedWith.values, "  ", "\n  ", "\n")
    if (wasAttributedTo.values.nonEmpty) addString(sb, wasAttributedTo.values, "  ", "\n  ", "\n")
    if (wasInfluencedBy.values.nonEmpty) addString(sb, wasInfluencedBy.values, "  ", "\n  ", "\n")
    if (wasInformedBy.values.nonEmpty) addString(sb, wasInformedBy.values, "  ", "\n  ", "\n")
    if (specializationOf.values.nonEmpty) addString(sb, specializationOf.values, "  ", "\n  ", "\n")
    if (alternateOf.values.nonEmpty) addString(sb, alternateOf.values, "  ", "\n  ", "\n")
    if (hadMember.values.nonEmpty) addString(sb, hadMember.values, "  ", "\n  ", "\n")
  }

  def addString[T <: Statement](b: StringBuilder, set: IterableOnce[T], start: String, sep: String, end: String): StringBuilder = {
    var first = true

    b append start
    for (x <- set) {
      if (first) {
        x.toNotation(b)
        first = false
      } else {
        b append sep
        x.toNotation(b)
      }
    }
    b append end
    b
  }

  def this() {
    this(Map(), Map(), Map(), Map(), Map(), Map(), Map(), Map(), Map(), Map(), Map(), Map(), Map(), Map(), Map(), Map(), Map())
  }
  def this(si: StatementIndexer, e: org.openprovenance.prov.scala.immutable.Entity) {
    this(add(si.entity, e), si.activity, si.agent, si.wasDerivedFrom, si.wasGeneratedBy, si.used, si.wasInvalidatedBy, si.wasStartedBy, si.wasEndedBy, si.actedOnBehalfOf, si.wasAssociatedWith, si.wasAttributedTo, si.wasInfluencedBy, si.wasInformedBy, si.specializationOf, si.alternateOf, si.hadMember)
  }

  def this(si: StatementIndexer, a: org.openprovenance.prov.scala.immutable.Activity) {
    this(si.entity, add(si.activity, a), si.agent, si.wasDerivedFrom, si.wasGeneratedBy, si.used, si.wasInvalidatedBy, si.wasStartedBy, si.wasEndedBy, si.actedOnBehalfOf, si.wasAssociatedWith, si.wasAttributedTo, si.wasInfluencedBy, si.wasInformedBy, si.specializationOf, si.alternateOf, si.hadMember)
  }

  def this(si: StatementIndexer, a: org.openprovenance.prov.scala.immutable.Agent) {
    this(si.entity, si.activity, add(si.agent, a), si.wasDerivedFrom, si.wasGeneratedBy, si.used, si.wasInvalidatedBy, si.wasStartedBy, si.wasEndedBy, si.actedOnBehalfOf, si.wasAssociatedWith, si.wasAttributedTo, si.wasInfluencedBy, si.wasInformedBy, si.specializationOf, si.alternateOf, si.hadMember)
  }

  def this(si: StatementIndexer, a: org.openprovenance.prov.scala.immutable.WasDerivedFrom) {
    this(si.entity, si.activity, si.agent, add(si.wasDerivedFrom, a), si.wasGeneratedBy, si.used, si.wasInvalidatedBy, si.wasStartedBy, si.wasEndedBy, si.actedOnBehalfOf, si.wasAssociatedWith, si.wasAttributedTo, si.wasInfluencedBy, si.wasInformedBy, si.specializationOf, si.alternateOf, si.hadMember)
  }

  def this(si: StatementIndexer, a: org.openprovenance.prov.scala.immutable.WasGeneratedBy) {
    this(si.entity, si.activity, si.agent, si.wasDerivedFrom, add(si.wasGeneratedBy, a), si.used, si.wasInvalidatedBy, si.wasStartedBy, si.wasEndedBy, si.actedOnBehalfOf, si.wasAssociatedWith, si.wasAttributedTo, si.wasInfluencedBy, si.wasInformedBy, si.specializationOf, si.alternateOf, si.hadMember)
  }

  def this(si: StatementIndexer, a: org.openprovenance.prov.scala.immutable.Used) {
    this(si.entity, si.activity, si.agent, si.wasDerivedFrom, si.wasGeneratedBy, add(si.used, a), si.wasInvalidatedBy, si.wasStartedBy, si.wasEndedBy, si.actedOnBehalfOf, si.wasAssociatedWith, si.wasAttributedTo, si.wasInfluencedBy, si.wasInformedBy, si.specializationOf, si.alternateOf, si.hadMember)
  }

  def this(si: StatementIndexer, a: org.openprovenance.prov.scala.immutable.WasInvalidatedBy) {
    this(si.entity, si.activity, si.agent, si.wasDerivedFrom, si.wasGeneratedBy, si.used, add(si.wasInvalidatedBy, a), si.wasStartedBy, si.wasEndedBy, si.actedOnBehalfOf, si.wasAssociatedWith, si.wasAttributedTo, si.wasInfluencedBy, si.wasInformedBy, si.specializationOf, si.alternateOf, si.hadMember)
  }

  def this(si: StatementIndexer, a: org.openprovenance.prov.scala.immutable.WasStartedBy) {
    this(si.entity, si.activity, si.agent, si.wasDerivedFrom, si.wasGeneratedBy, si.used, si.wasInvalidatedBy, add(si.wasStartedBy, a), si.wasEndedBy, si.actedOnBehalfOf, si.wasAssociatedWith, si.wasAttributedTo, si.wasInfluencedBy, si.wasInformedBy, si.specializationOf, si.alternateOf, si.hadMember)
  }

  def this(si: StatementIndexer, a: org.openprovenance.prov.scala.immutable.WasEndedBy) {
    this(si.entity, si.activity, si.agent, si.wasDerivedFrom, si.wasGeneratedBy, si.used, si.wasInvalidatedBy, si.wasStartedBy, add(si.wasEndedBy, a), si.actedOnBehalfOf, si.wasAssociatedWith, si.wasAttributedTo, si.wasInfluencedBy, si.wasInformedBy, si.specializationOf, si.alternateOf, si.hadMember)
  }

  def this(si: StatementIndexer, a: org.openprovenance.prov.scala.immutable.ActedOnBehalfOf) {
    this(si.entity, si.activity, si.agent, si.wasDerivedFrom, si.wasGeneratedBy, si.used, si.wasInvalidatedBy, si.wasStartedBy, si.wasEndedBy, add(si.actedOnBehalfOf, a), si.wasAssociatedWith, si.wasAttributedTo, si.wasInfluencedBy, si.wasInformedBy, si.specializationOf, si.alternateOf, si.hadMember)
  }

  def this(si: StatementIndexer, a: org.openprovenance.prov.scala.immutable.WasAssociatedWith) {
    this(si.entity, si.activity, si.agent, si.wasDerivedFrom, si.wasGeneratedBy, si.used, si.wasInvalidatedBy, si.wasStartedBy, si.wasEndedBy, si.actedOnBehalfOf, add(si.wasAssociatedWith, a), si.wasAttributedTo, si.wasInfluencedBy, si.wasInformedBy, si.specializationOf, si.alternateOf, si.hadMember)
  }

  def this(si: StatementIndexer, a: org.openprovenance.prov.scala.immutable.WasAttributedTo) {
    this(si.entity, si.activity, si.agent, si.wasDerivedFrom, si.wasGeneratedBy, si.used, si.wasInvalidatedBy, si.wasStartedBy, si.wasEndedBy, si.actedOnBehalfOf, si.wasAssociatedWith, add(si.wasAttributedTo, a), si.wasInfluencedBy, si.wasInformedBy, si.specializationOf, si.alternateOf, si.hadMember)
  }

  def this(si: StatementIndexer, a: org.openprovenance.prov.scala.immutable.WasInfluencedBy) {
    this(si.entity, si.activity, si.agent, si.wasDerivedFrom, si.wasGeneratedBy, si.used, si.wasInvalidatedBy, si.wasStartedBy, si.wasEndedBy, si.actedOnBehalfOf, si.wasAssociatedWith, si.wasAttributedTo, add(si.wasInfluencedBy, a), si.wasInformedBy, si.specializationOf, si.alternateOf, si.hadMember)
  }

  def this(si: StatementIndexer, a: org.openprovenance.prov.scala.immutable.WasInformedBy) {
    this(si.entity, si.activity, si.agent, si.wasDerivedFrom, si.wasGeneratedBy, si.used, si.wasInvalidatedBy, si.wasStartedBy, si.wasEndedBy, si.actedOnBehalfOf, si.wasAssociatedWith, si.wasAttributedTo, si.wasInfluencedBy, add(si.wasInformedBy, a), si.specializationOf, si.alternateOf, si.hadMember)
  }

  def this(si: StatementIndexer, a: org.openprovenance.prov.scala.immutable.SpecializationOf) {
    this(si.entity, si.activity, si.agent, si.wasDerivedFrom, si.wasGeneratedBy, si.used, si.wasInvalidatedBy, si.wasStartedBy, si.wasEndedBy, si.actedOnBehalfOf, si.wasAssociatedWith, si.wasAttributedTo, si.wasInfluencedBy, si.wasInformedBy, add(si.specializationOf, a), si.alternateOf, si.hadMember)
  }

  def this(si: StatementIndexer, a: org.openprovenance.prov.scala.immutable.AlternateOf) {
    this(si.entity, si.activity, si.agent, si.wasDerivedFrom, si.wasGeneratedBy, si.used, si.wasInvalidatedBy, si.wasStartedBy, si.wasEndedBy, si.actedOnBehalfOf, si.wasAssociatedWith, si.wasAttributedTo, si.wasInfluencedBy, si.wasInformedBy, si.specializationOf, add(si.alternateOf, a), si.hadMember)
  }

  def this(si: StatementIndexer, a: org.openprovenance.prov.scala.immutable.HadMember) {
    this(si.entity, si.activity, si.agent, si.wasDerivedFrom, si.wasGeneratedBy, si.used, si.wasInvalidatedBy, si.wasStartedBy, si.wasEndedBy, si.actedOnBehalfOf, si.wasAssociatedWith, si.wasAttributedTo, si.wasInfluencedBy, si.wasInformedBy, si.specializationOf, si.alternateOf, add(si.hadMember, a))
  }

  def this(si: StatementIndexer, id: QualifiedName, e: Entity) {
    this(extend(si.entity, id, e), si.activity, si.agent, si.wasDerivedFrom, si.wasGeneratedBy, si.used, si.wasInvalidatedBy, si.wasStartedBy, si.wasEndedBy, si.actedOnBehalfOf, si.wasAssociatedWith, si.wasAttributedTo, si.wasInfluencedBy, si.wasInformedBy, si.specializationOf, si.alternateOf, si.hadMember)
  }

  def this(si: StatementIndexer, id: QualifiedName,  a: Activity) {
    this(si.entity, extend(si.activity, id, a), si.agent, si.wasDerivedFrom, si.wasGeneratedBy, si.used, si.wasInvalidatedBy, si.wasStartedBy, si.wasEndedBy, si.actedOnBehalfOf, si.wasAssociatedWith, si.wasAttributedTo, si.wasInfluencedBy, si.wasInformedBy, si.specializationOf, si.alternateOf, si.hadMember)
  }

  def this(si: StatementIndexer, id: QualifiedName,  a: Agent) {
    this(si.entity, si.activity, extend(si.agent, id, a), si.wasDerivedFrom, si.wasGeneratedBy, si.used, si.wasInvalidatedBy, si.wasStartedBy, si.wasEndedBy, si.actedOnBehalfOf, si.wasAssociatedWith, si.wasAttributedTo, si.wasInfluencedBy, si.wasInformedBy, si.specializationOf, si.alternateOf, si.hadMember)
  }

  def this(si: StatementIndexer, id: QualifiedName,  a: WasDerivedFrom) {
    this(si.entity, si.activity, si.agent, extend(si.wasDerivedFrom, id, a), si.wasGeneratedBy, si.used, si.wasInvalidatedBy, si.wasStartedBy, si.wasEndedBy, si.actedOnBehalfOf, si.wasAssociatedWith, si.wasAttributedTo, si.wasInfluencedBy, si.wasInformedBy, si.specializationOf, si.alternateOf, si.hadMember)
  }

  def this(si: StatementIndexer, id: QualifiedName,  a: WasGeneratedBy) {
    this(si.entity, si.activity, si.agent, si.wasDerivedFrom, extend(si.wasGeneratedBy, id, a), si.used, si.wasInvalidatedBy, si.wasStartedBy, si.wasEndedBy, si.actedOnBehalfOf, si.wasAssociatedWith, si.wasAttributedTo, si.wasInfluencedBy, si.wasInformedBy, si.specializationOf, si.alternateOf, si.hadMember)
  }

  def this(si: StatementIndexer, id: QualifiedName,  a: Used) {
    this(si.entity, si.activity, si.agent, si.wasDerivedFrom, si.wasGeneratedBy, extend(si.used, id, a), si.wasInvalidatedBy, si.wasStartedBy, si.wasEndedBy, si.actedOnBehalfOf, si.wasAssociatedWith, si.wasAttributedTo, si.wasInfluencedBy, si.wasInformedBy, si.specializationOf, si.alternateOf, si.hadMember)
  }

  def this(si: StatementIndexer, id: QualifiedName,  a: WasInvalidatedBy) {
    this(si.entity, si.activity, si.agent, si.wasDerivedFrom, si.wasGeneratedBy, si.used, extend(si.wasInvalidatedBy, id, a), si.wasStartedBy, si.wasEndedBy, si.actedOnBehalfOf, si.wasAssociatedWith, si.wasAttributedTo, si.wasInfluencedBy, si.wasInformedBy, si.specializationOf, si.alternateOf, si.hadMember)
  }

  def this(si: StatementIndexer, id: QualifiedName,  a: WasStartedBy) {
    this(si.entity, si.activity, si.agent, si.wasDerivedFrom, si.wasGeneratedBy, si.used, si.wasInvalidatedBy, extend(si.wasStartedBy, id, a), si.wasEndedBy, si.actedOnBehalfOf, si.wasAssociatedWith, si.wasAttributedTo, si.wasInfluencedBy, si.wasInformedBy, si.specializationOf, si.alternateOf, si.hadMember)
  }

  def this(si: StatementIndexer, id: QualifiedName,  a: WasEndedBy) {
    this(si.entity, si.activity, si.agent, si.wasDerivedFrom, si.wasGeneratedBy, si.used, si.wasInvalidatedBy, si.wasStartedBy, extend(si.wasEndedBy, id, a), si.actedOnBehalfOf, si.wasAssociatedWith, si.wasAttributedTo, si.wasInfluencedBy, si.wasInformedBy, si.specializationOf, si.alternateOf, si.hadMember)
  }

  def this(si: StatementIndexer, id: QualifiedName,  a: ActedOnBehalfOf) {
    this(si.entity, si.activity, si.agent, si.wasDerivedFrom, si.wasGeneratedBy, si.used, si.wasInvalidatedBy, si.wasStartedBy, si.wasEndedBy, extend(si.actedOnBehalfOf, id, a), si.wasAssociatedWith, si.wasAttributedTo, si.wasInfluencedBy, si.wasInformedBy, si.specializationOf, si.alternateOf, si.hadMember)
  }

  def this(si: StatementIndexer, id: QualifiedName,  a: WasAssociatedWith) {
    this(si.entity, si.activity, si.agent, si.wasDerivedFrom, si.wasGeneratedBy, si.used, si.wasInvalidatedBy, si.wasStartedBy, si.wasEndedBy, si.actedOnBehalfOf, extend(si.wasAssociatedWith, id, a), si.wasAttributedTo, si.wasInfluencedBy, si.wasInformedBy, si.specializationOf, si.alternateOf, si.hadMember)
  }

  def this(si: StatementIndexer, id: QualifiedName,  a: WasAttributedTo) {
    this(si.entity, si.activity, si.agent, si.wasDerivedFrom, si.wasGeneratedBy, si.used, si.wasInvalidatedBy, si.wasStartedBy, si.wasEndedBy, si.actedOnBehalfOf, si.wasAssociatedWith, extend(si.wasAttributedTo, id, a), si.wasInfluencedBy, si.wasInformedBy, si.specializationOf, si.alternateOf, si.hadMember)
  }

  def this(si: StatementIndexer, id: QualifiedName,  a: WasInfluencedBy) {
    this(si.entity, si.activity, si.agent, si.wasDerivedFrom, si.wasGeneratedBy, si.used, si.wasInvalidatedBy, si.wasStartedBy, si.wasEndedBy, si.actedOnBehalfOf, si.wasAssociatedWith, si.wasAttributedTo, extend(si.wasInfluencedBy, id, a), si.wasInformedBy, si.specializationOf, si.alternateOf, si.hadMember)
  }

  def this(si: StatementIndexer, id: QualifiedName,  a: WasInformedBy) {
    this(si.entity, si.activity, si.agent, si.wasDerivedFrom, si.wasGeneratedBy, si.used, si.wasInvalidatedBy, si.wasStartedBy, si.wasEndedBy, si.actedOnBehalfOf, si.wasAssociatedWith, si.wasAttributedTo, si.wasInfluencedBy, extend(si.wasInformedBy, id, a), si.specializationOf, si.alternateOf, si.hadMember)
  }

  def this(si: StatementIndexer, id: QualifiedName,  a: SpecializationOf) {
    this(si.entity, si.activity, si.agent, si.wasDerivedFrom, si.wasGeneratedBy, si.used, si.wasInvalidatedBy, si.wasStartedBy, si.wasEndedBy, si.actedOnBehalfOf, si.wasAssociatedWith, si.wasAttributedTo, si.wasInfluencedBy, si.wasInformedBy, extend(si.specializationOf, id, a), si.alternateOf, si.hadMember)
  }

  def this(si: StatementIndexer, id: QualifiedName,  a: AlternateOf) {
    this(si.entity, si.activity, si.agent, si.wasDerivedFrom, si.wasGeneratedBy, si.used, si.wasInvalidatedBy, si.wasStartedBy, si.wasEndedBy, si.actedOnBehalfOf, si.wasAssociatedWith, si.wasAttributedTo, si.wasInfluencedBy, si.wasInformedBy, si.specializationOf, extend(si.alternateOf, id, a), si.hadMember)
  }

  def this(si: StatementIndexer, id: QualifiedName,  a: HadMember) {
    this(si.entity, si.activity, si.agent, si.wasDerivedFrom, si.wasGeneratedBy, si.used, si.wasInvalidatedBy, si.wasStartedBy, si.wasEndedBy, si.actedOnBehalfOf, si.wasAssociatedWith, si.wasAttributedTo, si.wasInfluencedBy, si.wasInformedBy, si.specializationOf, si.alternateOf, extend(si.hadMember, id, a))
  }

  def add2(s: org.openprovenance.prov.scala.immutable.Statement) = {
    s match {
      case a: org.openprovenance.prov.scala.immutable.Entity            => new StatementIndexer(this, a)
      case a: org.openprovenance.prov.scala.immutable.Activity          => new StatementIndexer(this, a)
      case a: org.openprovenance.prov.scala.immutable.Agent             => new StatementIndexer(this, a)
      case a: org.openprovenance.prov.scala.immutable.WasDerivedFrom    => new StatementIndexer(this, a)
      case a: org.openprovenance.prov.scala.immutable.WasGeneratedBy    => new StatementIndexer(this, a)
      case a: org.openprovenance.prov.scala.immutable.Used              => new StatementIndexer(this, a)
      case a: org.openprovenance.prov.scala.immutable.WasInvalidatedBy  => new StatementIndexer(this, a)
      case a: org.openprovenance.prov.scala.immutable.WasStartedBy      => new StatementIndexer(this, a)
      case a: org.openprovenance.prov.scala.immutable.WasEndedBy        => new StatementIndexer(this, a)
      case a: org.openprovenance.prov.scala.immutable.ActedOnBehalfOf   => new StatementIndexer(this, a)
      case a: org.openprovenance.prov.scala.immutable.WasAssociatedWith => new StatementIndexer(this, a)
      case a: org.openprovenance.prov.scala.immutable.WasAttributedTo   => new StatementIndexer(this, a)
      case a: org.openprovenance.prov.scala.immutable.WasInfluencedBy   => new StatementIndexer(this, a)
      case a: org.openprovenance.prov.scala.immutable.WasInformedBy     => new StatementIndexer(this, a)
      case a: org.openprovenance.prov.scala.immutable.SpecializationOf  => new StatementIndexer(this, a)
      case a: org.openprovenance.prov.scala.immutable.AlternateOf       => new StatementIndexer(this, a)
      case a: org.openprovenance.prov.scala.immutable.HadMember         => new StatementIndexer(this, a)
    }
  }

  def extend2(id:QualifiedName, a: Statement): StatementIndexer={
    a match {
      case a: Entity            => new StatementIndexer(this, id, a)
      case a: Activity          => new StatementIndexer(this, id, a)
      case a: Agent             => new StatementIndexer(this, id, a)
      case a: WasDerivedFrom    => new StatementIndexer(this, id, a)
      case a: WasGeneratedBy    => new StatementIndexer(this, id, a)
      case a: Used              => new StatementIndexer(this, id, a)
      case a: WasInvalidatedBy  => new StatementIndexer(this, id, a)
      case a: WasStartedBy      => new StatementIndexer(this, id, a)
      case a: WasEndedBy        => new StatementIndexer(this, id, a)
      case a: ActedOnBehalfOf   => new StatementIndexer(this, id, a)
      case a: WasAssociatedWith => new StatementIndexer(this, id, a)
      case a: WasAttributedTo   => new StatementIndexer(this, id, a)
      case a: WasInfluencedBy   => new StatementIndexer(this, id, a)
      case a: WasInformedBy     => new StatementIndexer(this, id, a)
      case a: SpecializationOf  => new StatementIndexer(this, id, a)
      case a: AlternateOf       => new StatementIndexer(this, id, a)
      case a: HadMember         => new StatementIndexer(this, id, a)
    }
  }

  def extend2(ids:Set[QualifiedName], a: Statement): StatementIndexer={
    ids.foldLeft(this)((si,id) => si.extend2(id,a))
  }


  def canEqual(a: Any): Boolean = a.isInstanceOf[StatementIndexer]

  override def equals(that: Any): Boolean =
    that match {
      case that: StatementIndexer => that.canEqual(this) &&
        this.entity == that.entity &&
        this.activity == that.activity &&
        this.agent == that.agent &&
        this.wasDerivedFrom == that.wasDerivedFrom &&
        this.wasGeneratedBy == that.wasGeneratedBy &&
        this.used == that.used &&
        this.wasInvalidatedBy == that.wasInvalidatedBy &&
        this.wasStartedBy == that.wasStartedBy &&
        this.wasEndedBy == that.wasEndedBy &&
        this.wasAssociatedWith == that.wasAssociatedWith &&
        this.wasAttributedTo == that.wasAttributedTo &&
        this.actedOnBehalfOf == that.actedOnBehalfOf &&
        this.wasInfluencedBy == that.wasInfluencedBy &&
        this.wasInformedBy == that.wasInformedBy &&
        this.specializationOf == that.specializationOf &&
        this.alternateOf == that.alternateOf &&
        this.hadMember == that.hadMember

      case _ => false
    }

  override lazy val hashCode: Int = {
    h(entity) + h(activity) + h(agent) + h(wasDerivedFrom) + h(wasGeneratedBy) + h(used) + h(wasInvalidatedBy) + h(wasStartedBy) +
      h(wasEndedBy) + h(wasAssociatedWith) + h(wasAttributedTo) + h(actedOnBehalfOf) + h(wasInfluencedBy) + h(wasInformedBy) + h(specializationOf) +
      h(alternateOf) + h(hadMember)
  }

  def values() = {
    this.entity.values ++
      this.activity.values ++
      this.agent.values ++
      this.wasDerivedFrom.values ++
      this.wasGeneratedBy.values ++
      this.used.values ++
      this.wasInvalidatedBy.values ++
      this.wasStartedBy.values ++
      this.wasEndedBy.values ++
      this.wasAssociatedWith.values ++
      this.wasAttributedTo.values ++
      this.actedOnBehalfOf.values ++
      this.wasInfluencedBy.values ++
      this.wasInformedBy.values ++
      this.specializationOf.values ++
      this.alternateOf.values ++
      this.hadMember.values
  }

  def unify(m: Map[QualifiedName, Set[QualifiedName]]): StatementIndexer = {
    val u_entity            = entity.           flatMap{case (q,e) => val e2=e.unify(m); Set(q).flatMap(m).map(q1 => (q1,e2))}.toMap
    val u_agent             = agent.            flatMap{case (q,e) => val e2=e.unify(m); Set(q).flatMap(m).map(q1 => (q1,e2))}.toMap
    val u_activity          = activity.         flatMap{case (q,e) => val e2=e.unify(m); Set(q).flatMap(m).map(q1 => (q1,e2))}.toMap
    val u_wasDerivedFrom    = wasDerivedFrom.   flatMap{case (q,e) => val e2=e.unify(m); Set(q).flatMap(m).map(q1 => (q1,e2))}.toMap
    val u_wasGeneratedBy    = wasGeneratedBy.   flatMap{case (q,e) => val e2=e.unify(m); Set(q).flatMap(m).map(q1 => (q1,e2))}.toMap
    val u_used              = used.             flatMap{case (q,e) => val e2=e.unify(m); Set(q).flatMap(m).map(q1 => (q1,e2))}.toMap
    val u_wasInvalidatedBy  = wasInvalidatedBy. flatMap{case (q,e) => val e2=e.unify(m); Set(q).flatMap(m).map(q1 => (q1,e2))}.toMap
    val u_wasStartedBy      = wasStartedBy.     flatMap{case (q,e) => val e2=e.unify(m); Set(q).flatMap(m).map(q1 => (q1,e2))}.toMap
    val u_wasEndedBy        = wasEndedBy.       flatMap{case (q,e) => val e2=e.unify(m); Set(q).flatMap(m).map(q1 => (q1,e2))}.toMap
    val u_actedOnBehalfOf   = actedOnBehalfOf.  flatMap{case (q,e) => val e2=e.unify(m); Set(q).flatMap(m).map(q1 => (q1,e2))}.toMap
    val u_wasAssociatedWith = wasAssociatedWith.flatMap{case (q,e) => val e2=e.unify(m); Set(q).flatMap(m).map(q1 => (q1,e2))}.toMap
    val u_wasAttributedTo   = wasAttributedTo.  flatMap{case (q,e) => val e2=e.unify(m); Set(q).flatMap(m).map(q1 => (q1,e2))}.toMap
    val u_wasInfluencedBy   = wasInfluencedBy.  flatMap{case (q,e) => val e2=e.unify(m); Set(q).flatMap(m).map(q1 => (q1,e2))}.toMap
    val u_wasInformedBy     = wasInformedBy.    flatMap{case (q,e) => val e2=e.unify(m); Set(q).flatMap(m).map(q1 => (q1,e2))}.toMap
    val u_specializationOf  = specializationOf. flatMap{case (q,e) => val e2=e.unify(m); Set(q).flatMap(m).map(q1 => (q1,e2))}.toMap
    val u_alternateOf       = alternateOf.      flatMap{case (q,e) => val e2=e.unify(m); Set(q).flatMap(m).map(q1 => (q1,e2))}.toMap
    val u_hadMember         = hadMember.        flatMap{case (q,e) => val e2=e.unify(m); Set(q).flatMap(m).map(q1 => (q1,e2))}.toMap


    new StatementIndexer(u_entity,
      u_activity,
      u_agent,
      u_wasDerivedFrom,
      u_wasGeneratedBy,
      u_used,
      u_wasInvalidatedBy,
      u_wasStartedBy,
      u_wasEndedBy,
      u_actedOnBehalfOf,
      u_wasAssociatedWith,
      u_wasAttributedTo,
      u_wasInfluencedBy,
      u_wasInformedBy,
      u_specializationOf,
      u_alternateOf,
      u_hadMember)

  }

  def idSets(): Iterable[Set[QualifiedName]] = {
    val s = values().flatMap(_.idSets())
    s
  }

}

// Consider indexing

class NoIdStatementIndexer(val statements: Set[Statement]) {

  def this() {
    this(Set())
  }

  lazy val wasGeneratedBy: Set[WasGeneratedBy] = statements.filter(_.isInstanceOf[WasGeneratedBy]).asInstanceOf[Set[WasGeneratedBy]]
  lazy val wasInvalidatedBy: Set[WasInvalidatedBy] = statements.filter(_.isInstanceOf[WasInvalidatedBy]).asInstanceOf[Set[WasInvalidatedBy]]
  lazy val used: Set[Used] = statements.filter(_.isInstanceOf[Used]).asInstanceOf[Set[Used]]
  lazy val wasStartedBy: Set[WasStartedBy] = statements.filter(_.isInstanceOf[WasStartedBy]).asInstanceOf[Set[WasStartedBy]]
  lazy val wasEndedBy: Set[WasEndedBy] = statements.filter(_.isInstanceOf[WasEndedBy]).asInstanceOf[Set[WasEndedBy]]

  lazy val nonEmpty: Boolean =statements.nonEmpty



  def add(s: Statement): NoIdStatementIndexer ={
    new NoIdStatementIndexer(statements+s)
  }

  def canEqual(a: Any): Boolean = a.isInstanceOf[NoIdStatementIndexer]

  override def equals(that: Any): Boolean =
    that match {
      case that: NoIdStatementIndexer => that.canEqual(this) &&
        this.statements == that.statements
      case _ => false
    }

  override lazy val hashCode: Int = {
    statements.hashCode()
  }

  override def toString(): String = {
    "<<" + statements.toString() + ">>"
  }

}

object ComplexKey {
  def apply(wgb: WasGeneratedBy) : ComplexKey[WasGeneratedBy]= {
    ComplexKey(wgb.entity,wgb.activity)
  }
  def apply(wib: WasInvalidatedBy) : ComplexKey[WasInvalidatedBy]= {
    ComplexKey(wib.entity,wib.activity)
  }
  //  def apply(usd: Used) : ComplexKey[Used]= {
  //		  ComplexKey(usd.activity,usd.entity)
  // }
  def apply(wsb: WasStartedBy) : ComplexKey[WasStartedBy]= {
    ComplexKey(wsb.activity,wsb.starter)
  }
  def apply(web: WasEndedBy) : ComplexKey[WasEndedBy]= {
    ComplexKey(web.activity,web.ender)
  }
}
case class ComplexKey[TERM](_1:Set[QualifiedName],_2:Set[QualifiedName]) {
  override lazy val hashCode: Int = _1.hashCode() + _2.hashCode()
}

object KeyIndexer {
  def addme[T,T2](index:  Map[ComplexKey[T],Set[T2]],
                  key:ComplexKey[T],
                  id: T2): Map[ComplexKey[T],Set[T2]] = {
    index.get(key) match {
      case None => index + {key -> Set(id)}
      case Some(set) => index + {key -> (set + id)}
    }
  }

  val notKeyable: Statement => Boolean = { x:Statement => x match {
    case s:WasGeneratedBy => false
    case s:WasInvalidatedBy => false
    case s:Used => false
    case s:WasStartedBy => false
    case s:WasEndedBy => false
    case _ => true }
  }
}

class KeyIndexer (val nisi: NoIdStatementIndexer,
                  val si: StatementIndexer,
                  val wasGeneratedBy:  Map[ComplexKey[WasGeneratedBy],Set[WasGeneratedBy]],
                  val wasInvalidatedBy:Map[ComplexKey[WasInvalidatedBy],Set[WasInvalidatedBy]],
                  val wasStartedBy:Map[ComplexKey[WasStartedBy],Set[WasStartedBy]],
                  val wasEndedBy:Map[ComplexKey[WasEndedBy],Set[WasEndedBy]],
                  val namespace: Namespace) {


  def split[T, T2<:T](s: Set[T], pred: T=>Option[T2]): (Set[T2],Set[T])={
    s.foldLeft((Set[T2](),Set[T]())){ (rest,first)=> rest match {
      case (yes,no) => (pred(first)) match {
        case Some(t) => ((yes+t),no)
        case None => (yes,no+first)
      }}}}


  def this(nisi:NoIdStatementIndexer, si:StatementIndexer, namespace:Namespace) {
    this(nisi,
      si,
      (nisi.wasGeneratedBy++si.wasGeneratedBy.values).groupBy(ComplexKey(_)),   // TODO: no need to index those of nisi, since we know that they don't have keys.
      (nisi.wasInvalidatedBy++si.wasInvalidatedBy.values).groupBy(ComplexKey(_)),
      (nisi.wasStartedBy++si.wasStartedBy.values).groupBy(ComplexKey(_)),
      (nisi.wasEndedBy++si.wasEndedBy.values).groupBy(ComplexKey(_)),
      namespace)
  }

  def toNormalForm(): DocumentProxy = {
    val nf0=new DocumentProxy(nisi,si,namespace)

    val nf1=wasGeneratedBy.  values.flatten.foldLeft(nf0)((nf,s) => nf.add(s))
    val nf2=wasInvalidatedBy.values.flatten.foldLeft(nf1)((nf,s) => nf.add(s))
    val nf3=wasStartedBy.    values.flatten.foldLeft(nf2)((nf,s) => nf.add(s))
    val nf4=wasEndedBy.      values.flatten.foldLeft(nf3)((nf,s) => nf.add(s))

    nf4
  }


  def mapMerge[T](map1: Map[ComplexKey[T], Set[T]], map2: Map[ComplexKey[T], Set[T]]): Map[ComplexKey[T], Set[T]] = {
    (map1.toSet ++ map2.toSet).groupBy(_._1).view.mapValues(x => x.flatMap(_._2).toSet).toMap
  }
  private def mapMerge2[T](map1: Map[ComplexKey[T], Set[T]], set: Set[(ComplexKey[T],Set[T])]) = {
    (map1.toSet ++ set).groupBy(_._1).view.mapValues(x => x.flatMap(_._2).toSet).toMap
  }

  def unify(m: Map[QualifiedName, Set[QualifiedName]]): KeyIndexer = {
    new KeyIndexer(new NoIdStatementIndexer(nisi.statements.map(_.unify(m))),
      si.unify(m),
      wasGeneratedBy.view.mapValues { x => x.map(_.unify(m))}.toMap,
      wasInvalidatedBy.view.mapValues { x => x.map(_.unify(m))}.toMap,
      wasStartedBy.view.mapValues { x => x.map(_.unify(m))}.toMap,
      wasEndedBy.view.mapValues { x => x.map(_.unify(m))}.toMap,
      namespace)
  }

  def idSets(): Iterable[Set[QualifiedName]] = {
    val ids1=wasGeneratedBy.  values.flatMap(wgbs => wgbs.flatMap(_.idSets()))
    val ids2=wasInvalidatedBy.values.flatMap(wibs => wibs.flatMap(_.idSets()))
    val ids3=wasStartedBy.    values.flatMap(wsbs => wsbs.flatMap(_.idSets()))
    val ids4=wasEndedBy.      values.flatMap(webs => webs.flatMap(_.idSets()))

    val sets=ids1++ids2++ids3++ids4++si.idSets()++nisi.statements.flatMap(_.idSets())

    val ks1=wasGeneratedBy.  map{case (k,wgbs) =>wgbs.flatMap(_.id)}
    val ks2=wasInvalidatedBy.map{case (k,wibs) =>wibs.flatMap(_.id)}
    val ks3=wasStartedBy.    map{case (k,wsbs) =>wsbs.flatMap(_.id)}
    val ks4=wasEndedBy.      map{case (k,webs) =>webs.flatMap(_.id)}

    sets++ks1++ks2++ks3++ks4

  }


  def keyConstraints(): KeyIndexer = {
    val sets=idSets()

    val tc = new TransitiveClosure[QualifiedName]
    sets.foreach { s => tc.add2(s) }
    val part = tc.partition()

    val (wgb_yes,wgb_no)=split(nisi.statements, {s:Statement => s match { case wgb:WasGeneratedBy => if (wasGeneratedBy.isDefinedAt(ComplexKey(wgb)))
      Some(wgb) else None
    case _ => None }})
    val (wib_yes,wib_no)=split(wgb_no,          {s:Statement => s match { case wib:WasInvalidatedBy => if (wasInvalidatedBy.isDefinedAt(ComplexKey(wib)))
      Some(wib) else None
    case _ => None }})

    val (wsb_yes,wsb_no)=split(wib_no,          {s:Statement => s match { case wsb:WasStartedBy => if (wasStartedBy.isDefinedAt(ComplexKey(wsb)))
      Some(wsb) else None
    case _ => None }})

    val (web_yes,web_no)=split(wsb_no,          {s:Statement => s match { case web:WasEndedBy => if (wasEndedBy.isDefinedAt(ComplexKey(web)))
      Some(web) else None
    case _ => None }})

    //val allOthers=nisi.statements.filter(notKeyable)
    val ki= new KeyIndexer(new NoIdStatementIndexer(web_no), // allOthers++wgb_no++wib_no++usd_no++wsb_no++web_no
      si,
      mapMerge2(wasGeneratedBy,  wgb_yes.map { wgb => (ComplexKey(wgb),Set(wgb))}).view.mapValues{s => Set(s.foldLeft(new WasGeneratedBy())((a,b) => a.merge(b)))}.toMap,
      mapMerge2(wasInvalidatedBy,wib_yes.map { wib => (ComplexKey(wib),Set(wib))}).view.mapValues{s => Set(s.foldLeft(new WasInvalidatedBy())((a,b) => a.merge(b)))}.toMap,
      mapMerge2(wasStartedBy,    wsb_yes.map { wsb => (ComplexKey(wsb),Set(wsb))}).view.mapValues{s => Set(s.foldLeft(new WasStartedBy())((a,b) => a.merge(b)))}.toMap,
      mapMerge2(wasEndedBy,      web_yes.map { web => (ComplexKey(web),Set(web))}).view.mapValues{s => Set(s.foldLeft(new WasEndedBy())((a,b) => a.merge(b)))}.toMap,
      namespace)

    //println(wgb_yes)
    //println(wasGeneratedBy)
    //println(ki.wasGeneratedBy)

    //println("luc")
    //println(part)
    //println(allOthers)
    //println(ki.nisi)
    //println(ki.wasGeneratedBy)
    //TODO: extend to used, wsb,web

    // TODO: toDocument() method
    ki.unify(part)
  }

  def toDocument(): Document = {
    val ll1 = si.values.filter{x => notKeyable(x)}.map(_.toTerms).flatMap { x => x }
    val ll2 = nisi.statements.flatMap(_.toTerms)
    val ll3=wasGeneratedBy.values.flatMap( x=>x ).map(_.toTerms).flatMap { x => x } ++
      wasInvalidatedBy.values.flatMap( x=>x ).map(_.toTerms).flatMap { x => x } ++
      wasStartedBy.values.flatMap( x=>x ).map(_.toTerms).flatMap { x => x } ++
      wasEndedBy.values.flatMap( x=>x ).map(_.toTerms).flatMap { x => x }             // TODO: I don't understand why I have 2 levels of brackets


    val ss: Set[org.openprovenance.prov.scala.immutable.StatementOrBundle] = (ll2 ++ ll1 ++ ll3).asInstanceOf[Set[org.openprovenance.prov.scala.immutable.StatementOrBundle]]
    new org.openprovenance.prov.scala.immutable.Document(ss, namespace)

  }



}

class DocumentProxyFromStatements(val statements: Set[Statement],
                                  val bundles: Set[Bundle],
                                  @BeanProperty val namespace: Namespace) extends HasNamespace with Hashable with ProxyWithStatements {
  private def canEqual(a: Any): Boolean = a.isInstanceOf[DocumentProxyFromStatements]

  def getStatements (): Set[Statement] = {
    statements
  }

  override def equals(that: Any): Boolean =
    that match {
      case that: DocumentProxyFromStatements => that.canEqual(this) &&
        this.statements == that.statements &&
        this.bundles == that.bundles
      case _ => false
    }

  override lazy val hashCode: Int = {
    h(statements) + h(bundles)
  }

  def addString1[T <: Statement](b: StringBuilder, set: IterableOnce[T], start: String, sep: String, end: String): StringBuilder = {
    var first = true

    b append start
    for (x <- set) {
      if (first) {
        x.toNotation(b)
        first = false
      } else {
        b append sep
        x.toNotation(b)
      }
    }
    b append end
    b
  }
  def addString2(b: StringBuilder, set: IterableOnce[Bundle], start: String, sep: String, end: String): StringBuilder = {
    var first = true

    b append start
    for (x <- set) {
      if (first) {
        x.toNotation(b)
        first = false
      } else {
        b append sep
        x.toNotation(b)
      }
    }
    b append end
    b
  }

  def toNotation(sb: StringBuilder): Unit = {
    Namespace.withThreadNamespace(namespace)
    sb ++= "normalForm\n"
    printNamespace(sb)
    Namespace.withThreadNamespace(namespace)
    if (statements.nonEmpty) {
      addString1(sb, statements, "   ", "\n   ", "\n")
    }
    if (bundles.nonEmpty) {
      addString2(sb, bundles, "   ", "\n   ", "\n")
    }

    sb ++= "endNormalForm\n"
  }

  override def toString(): String = {
    val sb = new StringBuilder
    toNotation(sb)
    sb.toString()
  }


}

trait ProxyWithStatements extends HasNamespace {
  def getStatements: Set[Statement]
}

class DocumentProxy(val statement: NoIdStatementIndexer, //Set[Statement],
                    val indexer: StatementIndexer,
                    @BeanProperty val namespace: Namespace) extends HasNamespace with Hashable with ProxyWithStatements with DocumentProxyInterface {

  def getStatements: Set[Statement] = {
    statement.statements ++ indexer.values()
  }

  def canEqual(a: Any): Boolean = a.isInstanceOf[DocumentProxy]

  def this(namespace: Namespace) {
    this(new NoIdStatementIndexer,new StatementIndexer,namespace)
  }

  override def equals(that: Any): Boolean =
    that match {
      case that: DocumentProxy => that.canEqual(this) &&
        this.statement == that.statement &&
        this.indexer == that.indexer
      case _ => false
    }

  override lazy val hashCode: Int = {
    h(statement) + h(indexer)
  }

  def setNamespace(x: org.openprovenance.prov.model.Namespace): Unit = {
    throw new UnsupportedOperationException
  }

  def idSets(): Iterable[Set[QualifiedName]] = {
    val s = indexer.idSets()++statement.statements.flatMap(_.idSets)
    s
  }

  def unify(): DocumentProxy = {
    val sets1 = idSets
    //println(sets1)

    val tc = new TransitiveClosure[QualifiedName]

    sets1.foreach { s => tc.add2(s) }
    //println(tc.set)
    //println(tc.transitiveClosure())
    val part = tc.partition()

    //println(part)
    new DocumentProxy(new NoIdStatementIndexer(statement.statements.map(_.unify(part))), indexer.unify(part), namespace)

  }

  def keyConstraints(): KeyIndexer = {
    val ki=new KeyIndexer(statement,indexer, namespace).keyConstraints()
    ki

  }

  def toNotation(sb: StringBuilder): Unit = {
    Namespace.withThreadNamespace(namespace)
    sb ++= "normalForm\n"
    printNamespace(sb)
    //statementOrBundle.addString(sb, "  ", "\n  ", "\n")
    if (statement.nonEmpty) {
      sb ++= " unindexed\n"
      indexer.addString(sb, statement.statements, "   ", "\n   ", "\n")
      sb ++= " endUnindexed\n"
    }
    sb ++= " indexed\n"
    indexer.toNotation(sb)
    sb ++= " endIndexed\n"
    sb ++= "endNormalForm\n"
  }

  override def toString(): String = {
    val sb = new StringBuilder
    toNotation(sb)
    sb.toString()
  }

  def add(s: org.openprovenance.prov.scala.immutable.Statement): DocumentProxy = {
    if (s.getId() == null) {
      new DocumentProxy(statement.add(Normalizer.inNormalForm(s)), indexer, namespace)
    } else {
      new DocumentProxy(statement, indexer.add2(s), namespace)
    }
  }

  def add(s: Statement): DocumentProxy = {
    s match {
      case sid: SingleIdentifiable => if (sid.id==null)
        new DocumentProxy(statement.add(sid),indexer,namespace)
      else new DocumentProxy(statement,indexer.extend2(sid.id,s),namespace)
      case mid: MultiIdentifiable => if (mid.id.isEmpty)
        new DocumentProxy(statement.add(mid),indexer,namespace)
      else new DocumentProxy(statement,indexer.extend2(mid.id,s),namespace)
    }
  }

  def toDocument(): org.openprovenance.prov.scala.immutable.Document = {
    val ll1 = indexer.values.map(_.toTerms).flatMap { x => x }
    val ll2 = statement.statements.flatMap(_.toTerms)

    val ss: Set[org.openprovenance.prov.scala.immutable.StatementOrBundle] = (ll2 ++ ll1).asInstanceOf[Set[org.openprovenance.prov.scala.immutable.StatementOrBundle]]
    new org.openprovenance.prov.scala.immutable.Document(ss, namespace)
  }

}

class FooException extends Exception

sealed abstract class MX_List[A, B]
case class MX_NIL[A, B]() extends MX_List[A, B]
case class MX_Cons1[A, B](a: A, l: MX_List[A, B]) extends MX_List[A, B]
case class MX_Cons2[A, B](b: B, l: MX_List[A, B]) extends MX_List[A, B]

object Denormalize {
  val dash = new QualifiedName("a", "b", "c")
  val time0: XMLGregorianCalendar = null

  def elements_args_list(sl: MX_List[Set[QualifiedName], Set[XMLGregorianCalendar]]): MX_List[List[QualifiedName], List[XMLGregorianCalendar]] = {
    elements_args_list_sort(sl, _.toList, _.toList)
  }

  def elements_args_list_sort[A, B](sl: MX_List[Set[A], Set[B]], sA: Set[A] => List[A], sB: Set[B] => List[B]): MX_List[List[A], List[B]] = {
    sl match {
      case MX_NIL()        => MX_NIL()
      case MX_Cons1(as, l) => MX_Cons1(sA(as), elements_args_list_sort(l, sA, sB))
      case MX_Cons2(bs, l) => MX_Cons2(sB(bs), elements_args_list_sort(l, sA, sB))
    }
  }

  def dashify_ifnull(l: List[QualifiedName]): List[QualifiedName] = {
    l match {
      case Nil => (dash :: Nil)
      case ll  => ll
    }
  }

  def zeroify_ifnull(l: List[XMLGregorianCalendar]): List[XMLGregorianCalendar] = {
    l match {
      case Nil => (time0 :: Nil)
      case ll  => ll
    }
  }

  def complete_args_list(l: MX_List[List[QualifiedName], List[XMLGregorianCalendar]]): MX_List[List[QualifiedName], List[XMLGregorianCalendar]] = {
    l match {
      case MX_NIL()        => MX_NIL()
      case MX_Cons1(as, l) => MX_Cons1(dashify_ifnull(as), complete_args_list(l))
      case MX_Cons2(bs, l) => MX_Cons2(zeroify_ifnull(bs), complete_args_list(l))
    }
  }

  @tailrec
  def check_if_all_last[A, B](ll: MX_List[List[A], List[B]]): Boolean = {
    ll match {
      case MX_NIL()              => true
      case MX_Cons1(a :: Nil, l) => check_if_all_last(l)
      case MX_Cons2(b :: Nil, l) => check_if_all_last(l)
      case _                     => false
    }
  }

  def map_car[A, B](ll: MX_List[List[A], List[B]]): MX_List[A, B] = {
    ll match {
      case MX_NIL()             => MX_NIL()
      case MX_Cons1(a :: la, l) => MX_Cons1(a, map_car(l))
      case MX_Cons2(b :: lb, l) => MX_Cons2(b, map_car(l))
      case _ => throw new IllegalStateException
    }
  }

  /* Takes the cdr of each list, provided that contain more than one element. If
	 * they contain one element, just keep them.
	 */
  def map_cdr_if[A, B](ll: MX_List[List[A], List[B]]): MX_List[List[A], List[B]] = {
    ll match {
      case MX_NIL()              => MX_NIL()
      case MX_Cons1(a :: Nil, l) => MX_Cons1(a :: Nil, map_cdr_if(l))
      case MX_Cons1(a :: la, l)  => MX_Cons1(la, map_cdr_if(l))
      case MX_Cons2(b :: Nil, l) => MX_Cons2(b :: Nil, map_cdr_if(l))
      case MX_Cons2(b :: lb, l)  => MX_Cons2(lb, map_cdr_if(l))
      case _ => throw new IllegalStateException
    }
  }

  def oneCombination[A, B](ll: MX_List[List[A], List[B]]): List[MX_List[A, B]] = {
    map_car(ll) :: (if (check_if_all_last(ll)) {
      Nil
    } else {
      oneCombination(map_cdr_if(ll))
    })
  }

  def allCombinations[A, B](ll: MX_List[List[A], List[B]]): List[MX_List[A, B]] = {
    ll match {
      case MX_NIL()        => List(MX_NIL())
      case MX_Cons1(la, l) => allCombinations(l).flatMap(r => la.map(a => MX_Cons1(a, r)))
      case MX_Cons2(lb, l) => allCombinations(l).flatMap(r => lb.map(b => MX_Cons2(b, r)))
    }

  }

  def someCombinations[A, B](id: QualifiedName, ll: MX_List[List[A], List[B]]): List[MX_List[A, B]] = {
    if (id != null) {
      oneCombination(ll)
    } else {
      allCombinations(ll)
    }
  }

}

object MX_Factory {

  val nullId: QualifiedName = null

  val noAttribute: Set[Attribute] = Set()

  def someTime(t: XMLGregorianCalendar): Option[XMLGregorianCalendar] = {
    if (t == Denormalize.time0) None else { Some(t) }
  }

  def someQualifiedName(t: QualifiedName): Option[QualifiedName] = {
    if (t == Denormalize.dash) None else { Some(t) }
  }

  def nullQualifiedName(t: QualifiedName): QualifiedName = {
    if (t == Denormalize.dash) null else { t }
  }

  def createActivity(id: QualifiedName, ll: MX_List[QualifiedName, XMLGregorianCalendar]): immutable.Activity = {
    ll match {
      case MX_Cons2(startTime, MX_Cons2(endTime, MX_NIL())) => pf.newActivity(id, startTime, endTime, noAttribute)
      case _ => throw new IllegalStateException
    }
  }

  def createWasDerivedFrom(id: QualifiedName, ll: MX_List[QualifiedName, XMLGregorianCalendar]): immutable.WasDerivedFrom = {
    ll match {
      case MX_Cons1(generatedEntity, MX_Cons1(usedEntity, MX_Cons1(activity, MX_Cons1(generation, MX_Cons1(usage, MX_NIL()))))) => pf.newWasDerivedFrom(id, generatedEntity, usedEntity, nullQualifiedName(activity), nullQualifiedName(generation), nullQualifiedName(usage), noAttribute)
      case _ => throw new IllegalStateException
    }
  }

  def createWasGeneratedBy(id: QualifiedName, ll: MX_List[QualifiedName, XMLGregorianCalendar]): immutable.WasGeneratedBy = {
    ll match {
      case MX_Cons1(entity, MX_Cons1(activity, MX_Cons2(time, MX_NIL()))) => pf.newWasGeneratedBy(id, entity, nullQualifiedName(activity), someTime(time), noAttribute)
      case _ => throw new IllegalStateException
    }
  }

  def createWasInvalidatedBy(id: QualifiedName, ll: MX_List[QualifiedName, XMLGregorianCalendar]): immutable.WasInvalidatedBy = {
    ll match {
      case MX_Cons1(entity, MX_Cons1(activity, MX_Cons2(time, MX_NIL()))) => pf.newWasInvalidatedBy(id, entity, nullQualifiedName(activity), someTime(time), noAttribute)
      case _ => throw new IllegalStateException
    }
  }

  def createUsed(id: QualifiedName, ll: MX_List[QualifiedName, XMLGregorianCalendar]): immutable.Used = {
    ll match {
      case MX_Cons1(activity, MX_Cons1(entity, MX_Cons2(time, MX_NIL()))) => pf.newUsed(id, activity, nullQualifiedName(entity), someTime(time), noAttribute)
      case _ => throw new IllegalStateException
    }
  }

  def createWasStartedBy(id: QualifiedName, ll: MX_List[QualifiedName, XMLGregorianCalendar]): immutable.WasStartedBy = {
    ll match {
      case MX_Cons1(activity, MX_Cons1(trigger, MX_Cons1(starter, MX_Cons2(time, MX_NIL())))) => pf.newWasStartedBy(id, activity, nullQualifiedName(trigger), nullQualifiedName(starter), someTime(time), noAttribute)
      case _ => throw new IllegalStateException
    }
  }

  def createWasEndedBy(id: QualifiedName, ll: MX_List[QualifiedName, XMLGregorianCalendar]): immutable.WasEndedBy = {
    ll match {
      case MX_Cons1(activity, MX_Cons1(trigger, MX_Cons1(ender, MX_Cons2(time, MX_NIL())))) => pf.newWasEndedBy(id, activity, nullQualifiedName(trigger), nullQualifiedName(ender), someTime(time), noAttribute)
      case _ => throw new IllegalStateException
    }
  }

  def createWasAssociatedWith(id: QualifiedName, ll: MX_List[QualifiedName, XMLGregorianCalendar]): immutable.WasAssociatedWith = {
    ll match {
      case MX_Cons1(activity, MX_Cons1(agent, MX_Cons1(plan, MX_NIL()))) => pf.newWasAssociatedWith(id, activity, agent, nullQualifiedName(plan), noAttribute)
      case _ => throw new IllegalStateException
    }
  }

  def createWasAttributedTo(id: QualifiedName, ll: MX_List[QualifiedName, XMLGregorianCalendar]): immutable.WasAttributedTo = {
    ll match {
      case MX_Cons1(entity, MX_Cons1(agent, MX_NIL())) => pf.newWasAttributedTo(id, entity, agent, noAttribute)
      case _ => throw new IllegalStateException
    }
  }

  def createActedOnBehalfOf(id: QualifiedName, ll: MX_List[QualifiedName, XMLGregorianCalendar]): immutable.ActedOnBehalfOf = {
    ll match {
      case MX_Cons1(delegate, MX_Cons1(responsible, MX_Cons1(activity, MX_NIL()))) => pf.newActedOnBehalfOf(id, delegate, responsible, nullQualifiedName(activity), noAttribute)
      case _ => throw new IllegalStateException
    }
  }

  def createSpecializationOf(id: QualifiedName, ll: MX_List[QualifiedName, XMLGregorianCalendar]): immutable.SpecializationOf = {
    ll match {
      case MX_Cons1(specificEntity, MX_Cons1(generalEntity, MX_NIL())) => pf.newSpecializationOf(id, specificEntity, generalEntity, noAttribute)
      case _ => throw new IllegalStateException
    }
  }

  def createAlternateOf(id: QualifiedName, ll: MX_List[QualifiedName, XMLGregorianCalendar]): immutable.AlternateOf = {
    ll match {
      case MX_Cons1(alternate1, MX_Cons1(alternate2, MX_NIL())) => pf.newAlternateOf(id, alternate1, alternate2, noAttribute)
      case _ => throw new IllegalStateException
    }
  }

  def createWasInfluencedBy(id: QualifiedName, ll: MX_List[QualifiedName, XMLGregorianCalendar]): immutable.WasInfluencedBy = {
    ll match {
      case MX_Cons1(influencee, MX_Cons1(influencer, MX_NIL())) => pf.newWasInfluencedBy(id, influencee, influencer, noAttribute)
      case _ => throw new IllegalStateException
    }
  }

  def createWasInformedBy(id: QualifiedName, ll: MX_List[QualifiedName, XMLGregorianCalendar]): immutable.WasInformedBy = {
    ll match {
      case MX_Cons1(informed, MX_Cons1(informant, MX_NIL())) => pf.newWasInformedBy(id, informed, informant, noAttribute)
      case _ => throw new IllegalStateException
    }
  }

  def createHadMember(id: QualifiedName, ll: MX_List[QualifiedName, XMLGregorianCalendar]): immutable.HadMember = {
    ll match {
      case MX_Cons1(collection, MX_Cons1(entity, MX_NIL())) => pf.newHadMember(id, collection, Set(entity), noAttribute)
      case _ => throw new IllegalStateException
    }
  }

}

object CommandLine{


  class MyDocumentProxyFactory extends DocumentProxyFactory {
    override def make(ns: Namespace): DocumentProxyInterface = {
      new org.openprovenance.prov.scala.nf.DocumentProxy(ns)
    }
  }

  // TODO: remove parseDocument from here, since it is now in provnInputer. Replace calls of parseDocument by inputer.
  def parseDocument (in:Input): Document = {
    //val stream=new SimpleStreamStats(1000)  //TODO: write an output stream
    val actions=new MyActions()
    val actions2=new MyActions2()
    val funs=new DocBuilderFunctions()


    val db=new DocBuilder(funs)
    parseDocument(in, db).asInstanceOf[DocBuilder].document()
  }

  def parseDocumentToNormalForm (in:Input): org.openprovenance.prov.scala.nf.DocumentProxy = {
    //val stream=new SimpleStreamStats(1000)  //TODO: write an output stream
    val db=new NFBuilder(new MyDocumentProxyFactory)
    parseDocument(in, db, nsFlag = false).asInstanceOf[NFBuilder].document().asInstanceOf[DocumentProxy]
  }

  def toBufferedSource(in:Input): BufferedSource = {
    in match {
      case StandardInput() => io.Source.fromInputStream(System.in)
      case FileInput(f:File) => io.Source.fromFile(f)
      case StreamInput(is:java.io.InputStream) => io.Source.fromInputStream(is)
    }
  }

  def parseDocument (in:Input, str: ProvStream): ProvStream = {
    parseDocument(in,str,nsFlag = true)
  }

  def parseDocument (in:Input, str: ProvStream, nsFlag: Boolean): ProvStream = {
    val ns=new Namespace
    ns.addKnownNamespaces()
    if (nsFlag) {
      // ns.register("t", "http://openprovenance.og/summary/types#")
      ns.register("sum",  "http://openprovenance.org/summary/ns#")  // TODO: QUICK HACK
    }
    ns.register("provext", "http://openprovenance.org/prov/extension#")

    val actions=new MyActions()
    val actions2=new MyActions2()
    actions2.bun_ns=None
    actions2.docns=ns;
    actions2.next=str

    val bufferedSource=toBufferedSource(in)

    val p=new MyParser(bufferedSource.mkString,actions2,actions)
    val doc =p.document.run() match {
      case Success(result) => str
      case Failure(e: ParseError) => println("Expression is not valid: " + p.formatError(e));  e.printStackTrace(); throw new ParserException(e)
      case Failure(e) => println("Unexpected error during parsing: " + e.getMessage); e.printStackTrace(); throw new ParserException(e)
    }

    Namespace.withThreadNamespace(ns)
    doc
  }



}