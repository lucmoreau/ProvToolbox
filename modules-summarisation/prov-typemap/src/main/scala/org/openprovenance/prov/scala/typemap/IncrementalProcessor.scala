package org.openprovenance.prov.scala.typemap

import org.openprovenance.prov.java.typemap.TypeMapProcessor
import org.openprovenance.prov.scala.immutable.Document
import org.openprovenance.prov.scala.summary.{Level0Mapper, SummaryDescriptionJson}
import org.openprovenance.prov.scala.summary.types.{Ag, Ent, ProvType, Wat, Waw, Wgb}

import java.util
import scala.collection.mutable
import org.openprovenance.prov.scala.summary.TypePropagator.om

import scala.jdk.CollectionConverters.{ListHasAsScala, MapHasAsScala};

object IncrementalProcessor {
  // in a json object, keys are strings, so we need to convert the keys to integers
  def convert(m: mutable.Map[String, Map[String, Int]]): mutable.Map[Int, Map[ProvType, Int]] = {
    m.map{case (k,v) => (k.toInt, v.map{case (k1,v1) => (om.readValue(k1,classOf[ProvType]), v1)})}
  }
}

class IncrementalProcessor (m: scala.collection.mutable.Map[Int, Map[ProvType,Int]],
                            s_acc: scala.collection.mutable.Set[Set[ProvType]],
                            g: scala.collection.mutable.ListBuffer[String]) {

  def this() = {
    this(scala.collection.mutable.Map[Int, Map[ProvType,Int]](),
      scala.collection.mutable.Set[Set[ProvType]](),
      scala.collection.mutable.ListBuffer[String]())
  }

  def this(m: String, s_acc: String, g: String) = {
    this(IncrementalProcessor.convert(om.readValue(m, classOf[scala.collection.mutable.Map[String, Map[String,Int]]])),
      om.readValue(s_acc, classOf[scala.collection.mutable.Set[Set[ProvType]]]),
      om.readValue(g, classOf[scala.collection.mutable.ListBuffer[String]]))
  }

  def printme(): Unit = {
    println(m)
    println(s_acc)
    println(g)
  }



  /*
  val m = scala.collection.mutable.Map[Int, Map[ProvType,Int]]()
  val s_acc = scala.collection.mutable.Set[Set[ProvType]]()
  val g= scala.collection.mutable.ListBuffer[String]()

   */

  val level = 10

  val level0: org.openprovenance.prov.summary.Level0MapperJson=new org.openprovenance.prov.summary.Level0MapperJson()

  val level0Mapper = Level0Mapper(level0)


  def process(doc: Document): (Map[String, Int], Map[Seq[(Int, Int)], Int] )= {
    val desc: SummaryDescriptionJson =new TypeMapProcessing().process(doc, accumulator = m, ground=g, set_accumulator=s_acc, recurse=false, level0Mapper, level=level)
    
   // val revTypes: Map[Set[ProvType], Int] = desc.types.map{case (k,v) => (v,k)}.toMap
    val revTypeStrings: Map[String, Int] = desc.typeStrings.map{case (k,v) => (v,k)}.toMap
    

    //val (tm,tsm): (Map[Int, ProvType], Map[Int, Set[ProvType]]) =TypeMap.typeMap(m, s_acc)
    //TypeMap.prettyPrintTypeMap(tm)
    //println(desc)
    val features: Map[String, Int] =desc.getFeatures
    //println(features)
    val features1: Map[Seq[(Int, Int)] ,Int] =features.map{case (k,v) => (libraryType(desc.types(revTypeStrings(k)),m),v)}
    (features,features1)
  }

  def libraryType(types: Set[ProvType], m: mutable.Map[Int, Map[ProvType, Int]]): Seq[(Int, Int)] ={
    types.map(t => {
      val d=t.depth()
      val entries=m(d)
      (d,entries(t))
    }).toSeq.sortWith((a,b) => a._1 < b._1 || (a._1 == b._1 && a._2 < b._2))
  }

  def process(doc: org.openprovenance.prov.model.Document): (util.Map[String, Integer], util.Map[util.List[(Integer, Integer)], Integer]) = {
    val doc2 = org.openprovenance.prov.scala.immutable.ProvFactory.pf.newDocument(doc);
    val (features: Map[String, Int],features1: Map[Seq[(Int, Int)], Int]) =process(doc2)
    // convert to java map
    import scala.jdk.CollectionConverters._
    (features.map{case (k,i) => (k, Integer.valueOf(i))}.asJava, features1.map{case (k,i) => k.map(p => (Integer.valueOf(p._1), Integer.valueOf(p._2))).asJava -> Integer.valueOf(i)}.asJava)
  }

  def reconstructFeatures(features: util.Map[String, Integer]): Map[Set[ProvType], Int] = {
    val theTypeMap: Map[Int, Map[Int, ProvType]] =m.map{case (k,v) => (k, v.map{case (k1,v1) => (v1,k1)})}.toMap

    val parsedFeatures: Map[List[(Int, Int)], Int] =features.asScala.map{case (k,v) => (om.readValue(k,classOf[List[(Int,Int)]]),v.toInt)}.toMap

    parsedFeatures.map { case (l, v) => (l.map { case (d, i) => theTypeMap(d)(i) }.toSet, v) }
  }

  def backToScala(features: util.Map[util.List[(Integer, Integer)], Integer]) : Map[Set[ProvType], Int] = {
    val theTypeMap: Map[Int, Map[Int, ProvType]] =m.map{case (k,v) => (k, v.map{case (k1,v1) => (v1,k1)})}.toMap

    features
      .asScala
      .map{case (k,v) => (k.asScala.map(p => (p._1.toInt, p._2.toInt)).toSeq -> v.toInt)}
      .map{ case (l, v) => (l.map { case (d, i) => theTypeMap(d)(i) }.toSet, v)}.toMap
  }

  def selectEntitiesWithoutAttribution(features: Map[Set[ProvType], Int]): Map[Set[ProvType],Int] = {
    features.filter{case (k,v)
    => k.contains(Ent()) &&
      !k.contains(Wat(Set(Ag()))) &&
      !containsWgbThenWaw(k)}.map{case (k,i) => (k, i)}.toMap
  }

  def containsWgbThenWaw(feat: Set[ProvType]): Boolean = {
    val indirectAttribution=
      feat
      .filter(x => x.isInstanceOf[Wgb])
      .collect(x => x.asInstanceOf[Wgb])
      .filter{ case Wgb(t) => t.contains(Waw(Set(Ag()))) } // check does not work if plan
    indirectAttribution.foreach(x => println("indirect attribution: " + x))
    indirectAttribution.nonEmpty
    }

  def countEntitiesWithoutAttribution(features: Map[Set[ProvType], Int]): Int = {
    val selected = selectEntitiesWithoutAttribution(features)
    selected.foreach(x => println("no attribution: " + x))
    selected.values.sum
  }

  def countEntities(features: Map[Set[ProvType], Int]): Int = {
    features.filter{case (k,v) => k.contains(Ent())}.values.sum
  }



  def getProvType(depth: Int, index: Int): ProvType = {
    m(depth).find{case (k,v) => v==index}.get._1
  }

  def process[T](processor: TypeMapProcessor[T]): T = {
    val tm_map: String =om.writeValueAsString(m.map{case (k,v) => (k.toString, v.map{case (k1,v1) => (om.writeValueAsString(k1), v1)})})
    val tm_set: String =om.writeValueAsString(s_acc)
    val tm_ground=om.writeValueAsString(g)
    processor.process(tm_map, tm_set, tm_ground)
  }
  def processMapOfStrings[T](fun:Function[String,T]):T = {
    fun(om.writeValueAsString(m.map{case (k,v) => (k.toString, v)}))
  }



}
