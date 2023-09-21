package org.openprovenance.prov.scala.summary.types
import com.fasterxml.jackson.annotation.{JsonIgnoreProperties, JsonSubTypes, JsonTypeInfo}

import scala.runtime.ScalaRunTime





trait ForwardType {}
trait BackwardType {}

trait BasicProvType extends ForwardType with BackwardType {
	def depth (): Int= 0
}


trait CompositeForwardProvType extends ProvType with ForwardType {
	val t: Set[ProvType with ForwardType]
	override def depth (): Int =  maxValPlusOne(t)
  override def getTypes: Set[String] = t.flatMap(_.getTypes)
	override def previous(): Set[ProvType] = t.asInstanceOf[Set[ProvType]]
	override def sortedTypes(): String= label() + "(" + sortedTypesSeq(t) + ")"
	override def flattenTypes(flattener: Set[_ <: ProvType]=>NumberedFlatType): FlatType= CompositeFlatType(label(), flattener(t))
}

trait CompositeBackwardProvType extends ProvType with BackwardType {
	val t: Set[ProvType with BackwardType]
	override def depth (): Int =  maxValPlusOne(t)
	override def getTypes: Set[String] = t.flatMap(_.getTypes)
	override def previous(): Set[ProvType] = t.asInstanceOf[Set[ProvType]]
	override def sortedTypes(): String= label() + "(" + sortedTypesSeq(t) + ")"
	override def flattenTypes(flattener: Set[_ <: ProvType]=>NumberedFlatType): FlatType= CompositeFlatType(label(), flattener(t))
}

object ProvType {
	def sortedTypesSeq[T <: ProvType](t: Set[T ]): String = {
		t.toSeq.map(_.sortedTypes()).sorted.mkString("[", ",", "]")
	}
	def flattenTypes[T <: ProvType](t: Set[T], flattener: Set[_ <: ProvType]=>NumberedFlatType): Set[FlatType] = {
		t.map(_.flattenTypes(flattener))
	}


	final val sto: SummaryTypesOrder = new SummaryTypesOrder {}
	final val stn: SummaryTypesNames = new SummaryTypesNames {}
	final val order: Map[String, Int] =stn.name_order_map
	final val names: Map[Int, String] =order.map{case (s,i) => i -> s}
}


@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,
		include = JsonTypeInfo.As.PROPERTY,
		property = "type")
@JsonSubTypes(Array(
		new JsonSubTypes.Type(value = classOf[Resource],name = ProvType.stn.RESOURCE),
		new JsonSubTypes.Type(value = classOf[Ent],     name = ProvType.stn.ENT),
		new JsonSubTypes.Type(value = classOf[Act],     name = ProvType.stn.ACT),
		new JsonSubTypes.Type(value = classOf[Ag],      name = ProvType.stn.AG),
		new JsonSubTypes.Type(value = classOf[Prim],    name = ProvType.stn.PRIM),
		new JsonSubTypes.Type(value = classOf[Wgb],     name = ProvType.stn.WGB),
		new JsonSubTypes.Type(value = classOf[Usd],     name = ProvType.stn.USD),
	  new JsonSubTypes.Type(value = classOf[Wdf],     name = ProvType.stn.WDF),
	  new JsonSubTypes.Type(value = classOf[Wdf_triangle],     name = ProvType.stn.WDF_triangle),
		new JsonSubTypes.Type(value = classOf[Wsb],     name = ProvType.stn.WSB),
		new JsonSubTypes.Type(value = classOf[Web],     name = ProvType.stn.WEB),
		new JsonSubTypes.Type(value = classOf[Winvb],   name = ProvType.stn.WINVB),
		new JsonSubTypes.Type(value = classOf[Waw],     name = ProvType.stn.WAW),
		new JsonSubTypes.Type(value = classOf[Wat],     name = ProvType.stn.WAT),
		new JsonSubTypes.Type(value = classOf[Aobo],    name = ProvType.stn.AOBO),
		new JsonSubTypes.Type(value = classOf[Winflb],  name = ProvType.stn.WINFLB),
		new JsonSubTypes.Type(value = classOf[Winfob],  name = ProvType.stn.WINFOB),
		new JsonSubTypes.Type(value = classOf[Alt],     name = ProvType.stn.ALT),
		new JsonSubTypes.Type(value = classOf[Spec],    name = ProvType.stn.SPEC),
		new JsonSubTypes.Type(value = classOf[Mem],     name = ProvType.stn.MEM),
		new JsonSubTypes.Type(value = classOf[Men],     name = ProvType.stn.MEN),

		new JsonSubTypes.Type(value = classOf[Wgb_1],     name = "Wgb_1"),
		new JsonSubTypes.Type(value = classOf[Usd_1],     name = "Usd_1"),
		new JsonSubTypes.Type(value = classOf[Wdf_1],     name = "Wdf_1"),
	  new JsonSubTypes.Type(value = classOf[Wdf_triangle_1],     name = "Wdf_triangle_1"),
  	new JsonSubTypes.Type(value = classOf[Wsb_1],     name = "Wsb_1"),
		new JsonSubTypes.Type(value = classOf[Web_1],     name = "Web_1"),
		new JsonSubTypes.Type(value = classOf[Winvb_1],   name = "Winvb_1"),
		new JsonSubTypes.Type(value = classOf[Waw_1],     name = "Waw_1"),
		new JsonSubTypes.Type(value = classOf[Wat_1],     name = "Wat_1"),
		new JsonSubTypes.Type(value = classOf[Aobo_1],    name = "Aobo_1"),
		new JsonSubTypes.Type(value = classOf[Winflb_1],  name = "Winflb_1"),
		new JsonSubTypes.Type(value = classOf[Winfob_1],  name = "Winfob_1"),
		new JsonSubTypes.Type(value = classOf[Alt_1],     name = "Alt_1"),
		new JsonSubTypes.Type(value = classOf[Spec_1],    name = "Spec_1"),
		new JsonSubTypes.Type(value = classOf[Mem_1],     name = "Mem_1")
		))
@JsonIgnoreProperties(Array("hashCode", "depth", "sortedTypes", "types", "getTypes", "order", "previous"))
abstract class ProvType {
	def getTypes:Set[String] = Set()
	def depth ():Int
	//def discard(types: Set[String]) = Option[ProvType]
	def discard(types: Set[String]): Boolean=false

	def prune[T <: ProvType](types: Set[String]): Option[T]=Some(this.asInstanceOf[T])

	def order():Int
	def label():String

	def sortedTypes(): String
	def flattenTypes(flattener: Set[_ <: ProvType]=>NumberedFlatType): FlatType

	protected def sortedTypesSeq[T <: ProvType](t: Set[T]): String = {
		t.toSeq.map(_.sortedTypes()).sorted.mkString("[", ",", "]")
	}
	protected def sortedTypesSeq[T <: ProvType](t: Set[T], mapper: String => String): String = {
		mapper(t.toSeq.map(_.sortedTypes()).sorted.mkString("[", ",", "]"))
	}
	@inline final def maxValPlusOne[T <: ProvType](t: Set[T]): Int = {
		if (t.isEmpty) 0 else 1 + t.map(_.depth()).max
	}
	def previous():Set[ProvType] = Set()
	def others():Set[ProvType] = Set()
}

case class Resource() extends ProvType with BasicProvType {
	override lazy val hashCode: Int = ScalaRunTime._hashCode(Resource.this)
	override def sortedTypes(): String = label() + "()"
	final override def order(): Int = ProvType.sto.RESOURCE
	final override def label(): String = ProvType.stn.RESOURCE
	override def flattenTypes(mapper: Set[_ <: ProvType]=>NumberedFlatType): FlatType= BasicFlatType(label(), this)

}
case class Ent()                    extends ProvType with BasicProvType  {
	override lazy val hashCode: Int = ScalaRunTime._hashCode(Ent.this)
	override def sortedTypes(): String = label() + "()"
	final override def order(): Int = ProvType.sto.ENT
	final override def label(): String = ProvType.stn.ENT
	override def flattenTypes(mapper: Set[_ <: ProvType]=>NumberedFlatType): FlatType= BasicFlatType(label(),this)
}
case class Act() extends ProvType with BasicProvType {
	override lazy val hashCode: Int = ScalaRunTime._hashCode(Act.this)
	override def sortedTypes(): String = label() + "()"
	final override def order(): Int = ProvType.sto.ACT
	final override def label(): String = ProvType.stn.ACT
	override def flattenTypes(mapper: Set[_ <: ProvType]=>NumberedFlatType): FlatType= BasicFlatType(label(), this)
}
case class Ag() extends ProvType with BasicProvType {
	override lazy val hashCode: Int = ScalaRunTime._hashCode(Ag.this)
	override def sortedTypes(): String = label() + "()"
	final override def order(): Int = ProvType.sto.AG
	final override def label(): String = ProvType.stn.AG
	override def flattenTypes(mapper: Set[_ <: ProvType]=>NumberedFlatType): FlatType= BasicFlatType(label(), this)
}

abstract class FlatType {}
case class BasicFlatType(l: String, b: ProvType with BasicProvType) extends FlatType
case class NumberedFlatType(level:Int, i: Int) extends FlatType
case class CompositeFlatType(l:String, s: NumberedFlatType) extends FlatType
case class CompositeFlatType2(l:String, s1: FlatType, s2: FlatType) extends FlatType

object Prim {
	def apply (ss: String): Prim = {
		Prim(Set(ss))
	}
}
case class Prim  (s: Set[String])        extends ProvType with BasicProvType {
	override lazy val hashCode: Int = ScalaRunTime._hashCode(Prim.this)
	override def discard(types: Set[String]): Boolean = s.exists(s_ => types.contains(s_))  //TODO: LUC TOO radical!
	override def getTypes: Set[String] = s
	override def sortedTypes(): String = label() + "(" + s.toSeq.sorted.mkString("[", ",", "]") + ")"
	final override def order(): Int = ProvType.sto.PRIM
	final override def label(): String = ProvType.stn.PRIM
	override def flattenTypes(mapper: Set[_ <: ProvType]=>NumberedFlatType): FlatType= BasicFlatType(label(), this)
}

case class Wgb   (t: Set[ProvType with ForwardType]) extends ProvType with CompositeForwardProvType {
	override lazy val hashCode: Int = ScalaRunTime._hashCode(Wgb.this)
	override def discard(types: Set[String]): Boolean = t.exists(pt => pt.discard(types))
	final override def order(): Int = ProvType.sto.WGB
	final override def label(): String = ProvType.stn.WGB
	override def prune[T <: ProvType](types: Set[String]): Option[T] = {
		Some(Wgb(t.flatMap(_.prune[ProvType with ForwardType](types))).asInstanceOf[T])
	}

}


case class Usd   (t: Set[ProvType with ForwardType]) extends ProvType with CompositeForwardProvType{
	override lazy val hashCode: Int = ScalaRunTime._hashCode(Usd.this)
	override def discard(types: Set[String]): Boolean = t.exists(pt => pt.discard(types))
	final override def order(): Int = ProvType.sto.USD
	final override def label(): String = ProvType.stn.USD
	override def prune[T <: ProvType](types: Set[String]): Option[T] = {
		Some(Usd(t.flatMap(_.prune[ProvType with ForwardType](types))).asInstanceOf[T])
	}
}

case class Wdf   (t: Set[ProvType with ForwardType], a: Set[ProvType with ForwardType]=Set()) extends ProvType with CompositeForwardProvType {
	override lazy val hashCode: Int = ScalaRunTime._hashCode(Wdf.this)
	override def discard(types: Set[String]): Boolean = t.exists(pt => pt.discard(types)) || a.exists(pt => pt.discard(types))
	override def depth (): Int =  scala.math.max(maxValPlusOne(t) , maxValPlusOne(a))
	override def sortedTypes(): String= label() + "(" + sortedTypesSeq(t) + "," + sortedTypesSeq(a)+ ")"
	final override def order(): Int = ProvType.sto.WDF
	final override def label(): String = ProvType.stn.WDF
	override def prune[T <: ProvType](types: Set[String]): Option[T] = {
		Some(Wdf(t.flatMap(_.prune[ProvType with ForwardType](types)), a.flatMap(_.prune[ProvType with ForwardType](types))).asInstanceOf[T])
	}
}
case class Wdf_triangle   (t: Set[ProvType with ForwardType], a: Set[ProvType with ForwardType]=Set()) extends ProvType with CompositeForwardProvType {
	override lazy val hashCode: Int = ScalaRunTime._hashCode(Wdf_triangle.this)
	override def discard(types: Set[String]): Boolean = t.exists(pt => pt.discard(types)) || a.exists(pt => pt.discard(types))
	override def depth (): Int =  scala.math.max(maxValPlusOne(t) , maxValPlusOne(a))
	override def sortedTypes(): String= label() + "(" + sortedTypesSeq(t) + "," + sortedTypesSeq(a)+ ")"
	final override def order(): Int = ProvType.sto.WDF  // should I introduce a different number?
	final override def label(): String = ProvType.stn.WDF_triangle
	override def prune[T <: ProvType](types: Set[String]): Option[T] = {
		Some(Wdf_triangle(t.flatMap(_.prune[ProvType with ForwardType](types)), a.flatMap(_.prune[ProvType with ForwardType](types))).asInstanceOf[T])
	}
}

case class Wsb   (t: Set[ProvType with ForwardType], a: Set[ProvType with ForwardType]=Set()) extends ProvType with CompositeForwardProvType {
	override lazy val hashCode: Int = ScalaRunTime._hashCode(Wsb.this)
	override def discard(types: Set[String]): Boolean = t.exists(pt => pt.discard(types))|| a.exists(pt => pt.discard(types))
	override def depth (): Int =  scala.math.max(maxValPlusOne(t) , maxValPlusOne(a))
	override def sortedTypes(): String= label() + "(" + sortedTypesSeq(t) + "," + sortedTypesSeq(a) + ")"
	final override def order(): Int = ProvType.sto.WSB
	override def others(): Set[ProvType] = a.asInstanceOf[Set[ProvType]]
	final override def label(): String = ProvType.stn.WSB
	override def prune[T <: ProvType](types: Set[String]): Option[T] = {
		Some(Wsb(t.flatMap(_.prune[ProvType with ForwardType](types)), a.flatMap(_.prune[ProvType with ForwardType](types))).asInstanceOf[T])
	}
}
case class Web   (t: Set[ProvType with ForwardType], e: Set[ProvType with ForwardType]=Set()) extends ProvType with CompositeForwardProvType {
	override lazy val hashCode: Int = ScalaRunTime._hashCode(Web.this)
	override def discard(types: Set[String]): Boolean = t.exists(pt => pt.discard(types))|| e.exists(pt => pt.discard(types))
	override def depth (): Int =  scala.math.max(maxValPlusOne(t) , maxValPlusOne(e))
	override def sortedTypes(): String= label() + "(" + sortedTypesSeq(t) + "," + sortedTypesSeq(e) + ")"
	final override def order(): Int = ProvType.sto.WEB
	override def others(): Set[ProvType] = e.asInstanceOf[Set[ProvType]]
	final override def label(): String = ProvType.stn.WEB
	override def prune[T <: ProvType](types: Set[String]): Option[T] = {
		Some(Web(t.flatMap(_.prune[ProvType with ForwardType](types)), e.flatMap(_.prune[ProvType with ForwardType](types))).asInstanceOf[T])
	}
}

case class Winvb (t: Set[ProvType with ForwardType]) extends ProvType with CompositeForwardProvType {
	override lazy val hashCode: Int = ScalaRunTime._hashCode(Winvb.this)
  override def discard(types: Set[String]): Boolean = t.exists(pt => pt.discard(types))
	override def sortedTypes(): String= label() + "(" + sortedTypesSeq(t) + ")"
	final override def order(): Int = ProvType.sto.WINVB
	final override def label(): String = ProvType.stn.WINVB
	override def prune[T <: ProvType](types: Set[String]): Option[T] = {
		Some(Winvb(t.flatMap(_.prune[ProvType with ForwardType](types))).asInstanceOf[T])
	}
}
case class Waw   (t: Set[ProvType with ForwardType], pl: Set[ProvType with ForwardType]=Set()) extends ProvType with CompositeForwardProvType {
	override lazy val hashCode: Int = ScalaRunTime._hashCode(Waw.this)
	override def discard(types: Set[String]): Boolean = t.exists(pt => pt.discard(types)) || pl.exists(pt => pt.discard(types))
  override def depth (): Int =  scala.math.max(maxValPlusOne(t) , maxValPlusOne(pl))
	override def sortedTypes(): String= label() + "(" + sortedTypesSeq(t) + "," + sortedTypesSeq(pl) + ")"
	final override def order(): Int = ProvType.sto.WAW
	override def others(): Set[ProvType] = pl.asInstanceOf[Set[ProvType]]
	final override def label(): String = ProvType.stn.WAW
	override def prune[T <: ProvType](types: Set[String]): Option[T] = {
		Some(Waw(t.flatMap(_.prune[ProvType with ForwardType](types)), pl.flatMap(_.prune[ProvType with ForwardType](types))).asInstanceOf[T])
	}
}
case class Wat   (t: Set[ProvType with ForwardType]) extends ProvType with CompositeForwardProvType {
	override lazy val hashCode: Int = ScalaRunTime._hashCode(Wat.this)
	override def discard(types: Set[String]): Boolean = t.exists(pt => pt.discard(types))
	final override def order(): Int = ProvType.sto.WAT
	final override def label(): String = ProvType.stn.WAT
	override def prune[T <: ProvType](types: Set[String]): Option[T] = {
		Some(Wat(t.flatMap(_.prune[ProvType with ForwardType](types))).asInstanceOf[T])
	}
}
case class Aobo  (t: Set[ProvType with ForwardType], a: Set[ProvType with ForwardType]=Set()) extends ProvType with CompositeForwardProvType {
	override lazy val hashCode: Int = ScalaRunTime._hashCode(Aobo.this)
	override def discard(types: Set[String]): Boolean = t.exists(pt => pt.discard(types))  || a.exists(pt => pt.discard(types))
	override def depth (): Int =  scala.math.max(maxValPlusOne(t) , maxValPlusOne(a))
	override def sortedTypes(): String= label() + "(" + sortedTypesSeq(t) + "," + sortedTypesSeq(a) + ")"
	final override def order(): Int = ProvType.sto.AOBO
	override def others(): Set[ProvType] = a.asInstanceOf[Set[ProvType]]
	final override def label(): String = ProvType.stn.AOBO
	override def prune[T <: ProvType](types: Set[String]): Option[T] = {
		Some(Aobo(t.flatMap(_.prune[ProvType with ForwardType](types)), a.flatMap(_.prune[ProvType with ForwardType](types))).asInstanceOf[T])
	}
}
case class Winflb(t: Set[ProvType with ForwardType]) extends ProvType with CompositeForwardProvType {
	override lazy val hashCode: Int = ScalaRunTime._hashCode(Winflb.this)
	override def discard(types: Set[String]): Boolean = t.exists(pt => pt.discard(types))
	final override def order(): Int = ProvType.sto.WINFLB
	final override def label(): String = ProvType.stn.WINFLB
	override def prune[T <: ProvType](types: Set[String]): Option[T] = {
		Some(Winflb(t.flatMap(_.prune[ProvType with ForwardType](types))).asInstanceOf[T])
	}
}
case class Winfob(t: Set[ProvType with ForwardType]) extends ProvType with CompositeForwardProvType {
	override lazy val hashCode: Int = ScalaRunTime._hashCode(Winfob.this)
	override def discard(types: Set[String]): Boolean = t.exists(pt => pt.discard(types))
	final override def order(): Int = ProvType.sto.WINFOB
	final override def label(): String = ProvType.stn.WINFOB
	override def prune[T <: ProvType](types: Set[String]): Option[T] = {
		Some(Winfob(t.flatMap(_.prune[ProvType with ForwardType](types))).asInstanceOf[T])
	}
}
case class Alt   (t: Set[ProvType with ForwardType]) extends ProvType with CompositeForwardProvType {
	override lazy val hashCode: Int = ScalaRunTime._hashCode(Alt.this)
	override def discard(types: Set[String]): Boolean = t.exists(pt => pt.discard(types))
	final override def order(): Int = ProvType.sto.ALT
	final override def label(): String = ProvType.stn.ALT
	override def prune[T <: ProvType](types: Set[String]): Option[T] = {
		Some(Alt(t.flatMap(_.prune[ProvType with ForwardType](types))).asInstanceOf[T])
	}
}
case class Spec  (t: Set[ProvType with ForwardType]) extends ProvType with CompositeForwardProvType {
	override lazy val hashCode: Int = ScalaRunTime._hashCode(Spec.this)
	override def discard(types: Set[String]): Boolean = t.exists(pt => pt.discard(types))
	final override def order(): Int = ProvType.sto.SPEC
	final override def label(): String = ProvType.stn.SPEC
	override def prune[T <: ProvType](types: Set[String]): Option[T] = {
		Some(Spec(t.flatMap(_.prune[ProvType with ForwardType](types))).asInstanceOf[T])
	}
}
case class Mem   (t: Set[ProvType with ForwardType]) extends ProvType with CompositeForwardProvType {
	override lazy val hashCode: Int = ScalaRunTime._hashCode(Mem.this)
	override def discard(types: Set[String]): Boolean = t.exists(pt => pt.discard(types))
	final override def order(): Int = ProvType.sto.MEM
	final override def label(): String = ProvType.stn.MEM
	override def prune[T <: ProvType](types: Set[String]): Option[T] = {
		Some(Mem(t.flatMap(_.prune[ProvType with ForwardType](types))).asInstanceOf[T])
	}
}
case class Men   (t: Set[ProvType with ForwardType]) extends ProvType with CompositeForwardProvType {
	override lazy val hashCode: Int = ScalaRunTime._hashCode(Men.this)
	override def discard(types: Set[String]): Boolean = t.exists(pt => pt.discard(types))
	final override def order(): Int = ProvType.sto.MEN
	final override def label(): String = ProvType.stn.MEN
	override def prune[T <: ProvType](types: Set[String]): Option[T] = {
		Some(Men(t.flatMap(_.prune[ProvType with ForwardType](types))).asInstanceOf[T])
	}
}

case class Wgb_1   (t: Set[ProvType with BackwardType ]) extends ProvType with CompositeBackwardProvType  {
	override lazy val hashCode: Int = ScalaRunTime._hashCode(Wgb_1.this)
	override def discard(types: Set[String]): Boolean = t.exists(pt => pt.discard(types))
	final override def order(): Int = ProvType.sto.WGB_1
	final override def label(): String = ProvType.stn.WGB_1
}
case class Usd_1   (t: Set[ProvType with BackwardType ]) extends ProvType with CompositeBackwardProvType   {
	override lazy val hashCode: Int = ScalaRunTime._hashCode(Usd_1.this)
	override def discard(types: Set[String]): Boolean = t.exists(pt => pt.discard(types))
	final override def order(): Int = ProvType.sto.USD_1
	final override def label(): String = ProvType.stn.USD_1
	override def prune[T <: ProvType](types: Set[String]): Option[T] = {
		Some(Usd_1(t.flatMap(_.prune[ProvType with BackwardType](types))).asInstanceOf[T])
	}
}
case class Wdf_1   (t: Set[ProvType with BackwardType ], a: Set[ProvType with BackwardType]=Set()) extends ProvType with CompositeBackwardProvType   {
	override lazy val hashCode: Int = ScalaRunTime._hashCode(Wdf_1.this)
	override def discard(types: Set[String]): Boolean = t.exists(pt => pt.discard(types)) || a.exists(pt => pt.discard(types))
	override def depth (): Int =  scala.math.max(maxValPlusOne(t) , maxValPlusOne(a))
	override def sortedTypes(): String= label() + "(" + sortedTypesSeq(t) + "," + sortedTypesSeq(a) + ")"
	final override def order(): Int = ProvType.sto.WDF_1
	override def others(): Set[ProvType] = a.asInstanceOf[Set[ProvType]]
	final override def label(): String = ProvType.stn.WDF_1
	override def prune[T <: ProvType](types: Set[String]): Option[T] = {
		Some(Wdf_1(t.flatMap(_.prune[ProvType with BackwardType](types)), a.flatMap(_.prune[ProvType with BackwardType](types))).asInstanceOf[T])
	}
}
case class Wdf_triangle_1   (t: Set[ProvType with BackwardType], a: Set[ProvType with BackwardType]=Set()) extends ProvType with CompositeBackwardProvType {
	override lazy val hashCode: Int = ScalaRunTime._hashCode(Wdf_triangle_1.this)
	override def discard(types: Set[String]): Boolean = t.exists(pt => pt.discard(types)) || a.exists(pt => pt.discard(types))
	override def depth (): Int =  scala.math.max(maxValPlusOne(t) , maxValPlusOne(a))
	override def sortedTypes(): String= label() + "(" + sortedTypesSeq(t) + "," + sortedTypesSeq(a)+ ")"
	final override def order(): Int = ProvType.sto.WDF_1  // should I introduce a different number?
	final override def label(): String = ProvType.stn.WDF_triangle_1
	override def prune[T <: ProvType](types: Set[String]): Option[T] = {
		Some(Wdf_triangle_1(t.flatMap(_.prune[ProvType with BackwardType](types)), a.flatMap(_.prune[ProvType with BackwardType](types))).asInstanceOf[T])
	}
}

case class Wsb_1   (t: Set[ProvType with BackwardType ], a: Set[ProvType with BackwardType]=Set()) extends ProvType with CompositeBackwardProvType   {
	override lazy val hashCode: Int = ScalaRunTime._hashCode(Wsb_1.this)
	override def discard(types: Set[String]): Boolean = t.exists(pt => pt.discard(types))|| a.exists(pt => pt.discard(types))
	override def depth (): Int =  scala.math.max(maxValPlusOne(t) , maxValPlusOne(a))
	override def sortedTypes(): String= label() + "(" + sortedTypesSeq(t) + "," + sortedTypesSeq(a) + ")"
	final override def order(): Int = ProvType.sto.WSB_1
	override def others(): Set[ProvType] = a.asInstanceOf[Set[ProvType]]
	final override def label(): String = ProvType.stn.WSB_1
	override def prune[T <: ProvType](types: Set[String]): Option[T] = {
		Some(Wsb_1(t.flatMap(_.prune[ProvType with BackwardType](types)), a.flatMap(_.prune[ProvType with BackwardType](types))).asInstanceOf[T])
	}}
case class Web_1   (t: Set[ProvType with BackwardType ], e: Set[ProvType with BackwardType]=Set()) extends ProvType with CompositeBackwardProvType   {
	override lazy val hashCode: Int = ScalaRunTime._hashCode(Web_1.this)
	override def discard(types: Set[String]): Boolean = t.exists(pt => pt.discard(types))|| e.exists(pt => pt.discard(types))
	override def depth (): Int =  scala.math.max(maxValPlusOne(t) , maxValPlusOne(e))
	override def sortedTypes(): String= label() + "(" + sortedTypesSeq(t) + "," + sortedTypesSeq(e) + ")"
	final override def order(): Int = ProvType.sto.WEB_1
	override def others(): Set[ProvType] = e.asInstanceOf[Set[ProvType]]
	final override def label(): String = ProvType.stn.WEB_1
	override def prune[T <: ProvType](types: Set[String]): Option[T] = {
		Some(Web_1(t.flatMap(_.prune[ProvType with BackwardType](types)), e.flatMap(_.prune[ProvType with BackwardType](types))).asInstanceOf[T])
	}}
case class Winvb_1 (t: Set[ProvType with BackwardType ]) extends ProvType with CompositeBackwardProvType   {
	override lazy val hashCode: Int = ScalaRunTime._hashCode(Winvb_1.this)
	override def discard(types: Set[String]): Boolean = t.exists(pt => pt.discard(types))
	final override def order(): Int = ProvType.sto.WINVB_1
	final override def label(): String = ProvType.stn.WINVB_1
	override def prune[T <: ProvType](types: Set[String]): Option[T] = {
		Some(Winvb_1(t.flatMap(_.prune[ProvType with BackwardType](types))).asInstanceOf[T])
	}
}

case class Waw_1   (t: Set[ProvType with BackwardType ], pl: Set[ProvType with BackwardType]=Set()) extends ProvType with CompositeBackwardProvType   {
	override lazy val hashCode: Int = ScalaRunTime._hashCode(Waw_1.this)
	override def discard(types: Set[String]): Boolean = t.exists(pt => pt.discard(types)) || pl.exists(pt => pt.discard(types))
	override def depth (): Int =  scala.math.max(maxValPlusOne(t) , maxValPlusOne(pl))
	override def sortedTypes(): String= label() + "(" + sortedTypesSeq(t) + "," + sortedTypesSeq(pl) + ")"
	final override def order(): Int = ProvType.sto.WAW_1
	override def others(): Set[ProvType] = pl.asInstanceOf[Set[ProvType]]
	final override def label(): String = ProvType.stn.WAW_1
	override def prune[T <: ProvType](types: Set[String]): Option[T] = {
		Some(Waw_1(t.flatMap(_.prune[ProvType with BackwardType](types)), pl.flatMap(_.prune[ProvType with BackwardType](types))).asInstanceOf[T])
	}
}
case class Wat_1   (t: Set[ProvType with BackwardType ]) extends ProvType with CompositeBackwardProvType   {
	override lazy val hashCode: Int = ScalaRunTime._hashCode(Wat_1.this)
	override def discard(types: Set[String]): Boolean = t.exists(pt => pt.discard(types))
	final override def order(): Int = ProvType.sto.WAT_1
	final override def label(): String = ProvType.stn.WAT_1
	override def prune[T <: ProvType](types: Set[String]): Option[T] = {
		Some(Wat_1(t.flatMap(_.prune[ProvType with BackwardType](types))).asInstanceOf[T])
	}
}
case class Aobo_1  (t: Set[ProvType with BackwardType ], a: Set[ProvType with BackwardType]=Set()) extends ProvType with CompositeBackwardProvType   {
	override lazy val hashCode: Int = ScalaRunTime._hashCode(Aobo_1.this)
	override def discard(types: Set[String]): Boolean = t.exists(pt => pt.discard(types))  || a.exists(pt => pt.discard(types))
	override def depth (): Int =  scala.math.max(maxValPlusOne(t) , maxValPlusOne(a))
	override def sortedTypes(): String= label() + "(" + sortedTypesSeq(t) + "," + sortedTypesSeq(a) + ")"
	final override def order(): Int = ProvType.sto.AOBO_1
	override def others(): Set[ProvType] = a.asInstanceOf[Set[ProvType]]
	final override def label(): String = ProvType.stn.AOBO_1
}
case class Winflb_1(t: Set[ProvType with BackwardType ]) extends ProvType with CompositeBackwardProvType  {
	override lazy val hashCode: Int = ScalaRunTime._hashCode(Winflb_1.this)
	override def discard(types: Set[String]): Boolean = t.exists(pt => pt.discard(types))
	final override def order(): Int = ProvType.sto.WINFLB_1
	final override def label(): String = ProvType.stn.WINFLB_1
	override def prune[T <: ProvType](types: Set[String]): Option[T] = {
		Some(Winflb_1(t.flatMap(_.prune[ProvType with BackwardType](types))).asInstanceOf[T])
	}}
case class Winfob_1(t: Set[ProvType with BackwardType ]) extends ProvType with CompositeBackwardProvType   {
	override lazy val hashCode: Int = ScalaRunTime._hashCode(Winfob_1.this)
	override def discard(types: Set[String]): Boolean = t.exists(pt => pt.discard(types))
	final override def order(): Int = ProvType.sto.WINFOB_1
	final override def label(): String = ProvType.stn.WINFOB_1
	override def prune[T <: ProvType](types: Set[String]): Option[T] = {
		Some(Winfob_1(t.flatMap(_.prune[ProvType with BackwardType](types))).asInstanceOf[T])
	}}
case class Alt_1   (t: Set[ProvType with BackwardType ]) extends ProvType with CompositeBackwardProvType   {
	override lazy val hashCode: Int = ScalaRunTime._hashCode(Alt_1.this)
	override def discard(types: Set[String]): Boolean = t.exists(pt => pt.discard(types))
	final override def order(): Int = ProvType.sto.ALT_1
	final override def label(): String = ProvType.stn.ALT_1
	override def prune[T <: ProvType](types: Set[String]): Option[T] = {
		Some(Alt_1(t.flatMap(_.prune[ProvType with BackwardType](types))).asInstanceOf[T])
	}}
case class Spec_1  (t: Set[ProvType with BackwardType ]) extends ProvType with CompositeBackwardProvType  {
	override lazy val hashCode: Int = ScalaRunTime._hashCode(Spec_1.this)
	override def discard(types: Set[String]): Boolean = t.exists(pt => pt.discard(types))
	final override def order(): Int = ProvType.sto.SPEC_1
	final override def label(): String = ProvType.stn.SPEC_1
	override def prune[T <: ProvType](types: Set[String]): Option[T] = {
		Some(Spec_1(t.flatMap(_.prune[ProvType with BackwardType](types))).asInstanceOf[T])
	}}
case class Mem_1   (t: Set[ProvType with BackwardType ]) extends ProvType with CompositeBackwardProvType   {
	override lazy val hashCode: Int = ScalaRunTime._hashCode(Mem_1.this)
	override def discard(types: Set[String]): Boolean = t.exists(pt => pt.discard(types))
	final override def order(): Int = ProvType.sto.MEM_1
	final override def label(): String = ProvType.stn.MEM_1
	override def prune[T <: ProvType](types: Set[String]): Option[T] = {
		Some(Mem_1(t.flatMap(_.prune[ProvType with BackwardType](types))).asInstanceOf[T])
	}}
case class Men_1   (t: Set[ProvType with BackwardType ]) extends ProvType with CompositeBackwardProvType    {
	override lazy val hashCode: Int = ScalaRunTime._hashCode(Men_1.this)
	override def discard(types: Set[String]): Boolean = t.exists(pt => pt.discard(types))
	final override def order(): Int = ProvType.sto.MEN_1
	final override def label(): String = ProvType.stn.MEN_1
	override def prune[T <: ProvType](types: Set[String]): Option[T] = {
		Some(Men_1(t.flatMap(_.prune[ProvType with BackwardType](types))).asInstanceOf[T])
	}}

