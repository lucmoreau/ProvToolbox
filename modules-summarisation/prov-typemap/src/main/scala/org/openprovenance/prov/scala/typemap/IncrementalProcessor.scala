package org.openprovenance.prov.scala.typemap

import org.openprovenance.prov.scala.immutable.Document
import org.openprovenance.prov.scala.summary.{Level0Mapper, SummaryDescriptionJson}
import org.openprovenance.prov.scala.summary.types.ProvType
import org.openprovenance.prov.scala.typemap.TypeMap.prettyPrintTypeMap

import java.util
import scala.collection.mutable

object IncrementalProcessor {

  val m = scala.collection.mutable.Map[Int, Map[ProvType,Int]]()
  val s_acc = scala.collection.mutable.Set[Set[ProvType]]()
  val g= scala.collection.mutable.ListBuffer[String]()

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

}
