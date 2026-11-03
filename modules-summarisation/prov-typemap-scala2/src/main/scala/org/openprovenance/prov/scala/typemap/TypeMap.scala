package org.openprovenance.prov.scala.typemap

import org.openprovenance.prov.scala.immutable.{Document, Indexer}

import java.io.{File, FileInputStream}
import org.openprovenance.prov.scala.summary.types.{CompositeBackwardProvType, CompositeForwardProvType, Prim, ProvType}
import org.openprovenance.prov.scala.summary.{Level0Mapper, SummaryAPI, SummaryConfig, SummaryDescriptionJson, SummaryIndex, TypePropagator}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.io.Source

case class PT   (id: Int, t: Int, o:Int, g:Seq[Int]) {
  override def toString: String = {
    //"type(" +
    ProvType.names(id) + "(" + t + "," + o + pretty1(g) + ")"
  }

  def pretty1(t: Seq[Int]): String = {
    if (t.isEmpty) {
      ""
    } else {
      t.mkString("[", ",", "]")
    }
  }
  def pretty2(t: Seq[Int]): String = {
    if (t.isEmpty) {
      ""
    } else {
      t.mkString(",[", ",", "]")
    }
  }
}

case class Export (tm:Map[Int,ProvType], ctsm:Map[Int,Seq[Int]], gm: Map[String,Int],  ctm: Map[Int, PT], st: Map[Int,Seq[Seq[Int]]], pretty: String)


object TypeMap {


  def groundMap(g: ListBuffer[String]): Map[String,Int] = {
    g.toSet.zipWithIndex.toMap
  }

  def main(args: Array[String]): Unit = {
    doIndexFile(args,args(1),1000)
  }

  def doIndexFile(args: Array[String],  tmap:String, every: Int=1000): Export = {
    val m = scala.collection.mutable.Map[Int, Map[ProvType,Int]]()
    val s_acc = scala.collection.mutable.Set[Set[ProvType]]()
    val g= scala.collection.mutable.ListBuffer[String]()

    var count: Int =0;
    val indexFile: String =args(0)
    val it: Iterator[String] =Source.fromFile(indexFile).getLines()
    val dir: String =indexFile.substring(0,indexFile.lastIndexOf("/")+1)
    it.foreach(s => {
      val s2: String =s.trim
      val theFile: String =dir+s2
      count=count+1
      if (count % every == 0) {
        println(count)
      }

      new TypeMapProcessing().process(theFile, accumulator = m, ground=g, set_accumulator=s_acc, recurse=false)
    })

    val gm:Map[String,Int]=groundMap(g)

    val (tm,tsm): (Map[Int, ProvType],Map[Int, Set[ProvType]]) = typeMap(m.map{case (k,v) => (k,v.keySet)},s_acc)

    val rtm: Map[ProvType, Int] = tm.map{case (x,y) => (y,x)}
    val rtsm: Map[Set[ProvType], Int] = tsm.map{case (x,y) => (y,x)}
    val ctsm:Map[Int,Seq[Int]]=tsm.view.mapValues(ss => ss.toSeq.map(rtm).sorted).toMap

    val ctm=makeConciseTypeMap(tm,rtm,gm,tsm,rtsm)

    val sb=new StringBuffer()
    prettyPrintConciseTypeMap(ctm,sb)

    val s_acc_indexed_by_depth: Map[Int, mutable.Set[Set[ProvType]]] =s_acc.groupBy(ss => ss.map(s=>s.depth()).max)

    val st: Map[Int, Seq[Seq[Int]]] =s_acc_indexed_by_depth.view.mapValues(vv => vv.map(s => s.toSeq.map(rtm).sorted).toSeq.sortWith{case (l1,l2) => l1.size < l2.size}).toMap

    val exportVal = Export(tm, ctsm, gm, ctm, st, sb.toString)
    TypePropagator.om.writeValue(new File(tmap), exportVal)

    exportVal

  }

  def doOneFile(args: Array[String]): Unit = {
    val m = scala.collection.mutable.Map[Int, Map[ProvType,Int]]()
    val s_acc = scala.collection.mutable.Set[Set[ProvType]]()
    val g= scala.collection.mutable.ListBuffer[String]()

    args.foreach(s =>
      new TypeMapProcessing().process(s, accumulator = m, ground=g, set_accumulator=s_acc, recurse=true ))

    println("--- Map[Int, Set[ProvType]] ---")
    println(m)

    println("--- Ground Map ---")
    val gm:Map[String,Int]=groundMap(g)
    println(gm)

    val (tm,tsm): (Map[Int, ProvType], Map[Int, Set[ProvType]]) = typeMap(m.map{case (k,v) => (k,v.keySet)},s_acc)


    val rtm: Map[ProvType, Int] =tm.map{case (x,y) => (y,x)}
    val rtsm: Map[Set[ProvType], Int] =tsm.map{case (x,y) => (y,x)}

    val ctsm:Map[Int,Seq[Int]]=tsm.view.mapValues(ss => ss.toSeq.map(rtm).sorted).toMap

    println("--- Type Map ---")
    prettyPrintTypeMap(tm)

    val ctm=makeConciseTypeMap(tm,rtm,gm,tsm,rtsm)

    val sb=new StringBuffer()

    prettyPrintConciseTypeMap(ctm,sb)

    val s_acc_indexed_by_depth: Map[Int, mutable.Set[Set[ProvType]]] =s_acc.groupBy(ss => ss.map(s=>s.depth()).max)

    val st: Map[Int, Seq[Seq[Int]]] =s_acc_indexed_by_depth.view.mapValues(vv => vv.map(s => s.toSeq.map(rtm).sorted).toSeq.sortWith{case (l1,l2) => l1.size < l2.size}).toMap

    val str = sb.toString

    println("--- Pretty Print ---")
    println(str)


    println("--- Out ---")
    TypePropagator.om.writeValue(System.out, Export(tm,ctsm,gm,ctm,st,str))



  }

  def makeConciseTypeMap(tm: Map[Int, ProvType], rtm: Map[ProvType, Int], gm:Map[String,Int], tsm:Map[Int, Set[ProvType]], rtsm:Map[Set[ProvType],Int]): Map[Int, PT] = {
    tm.map{case (n,t) =>

  //    println("--->")
  //    println(rtsm)
  //    println(t)
  //    println(t.previous())
      val val1 = rtsm.getOrElse(t.previous(),-1)
      val val2 = rtsm.getOrElse(t.others(), -2)
      (n, PT(t.order(), val1, val2, t.getTypes.toSeq.map(gm).sorted))}
  }



  def prettyPrintTypeMap(tm: Map[Int, ProvType]): Unit = {
    tm.keys.toSeq.sorted.foreach(k => println(k + " -> " + tm(k)))
  }

  def prettyPrintConciseTypeMap(ctm: Map[Int, PT], s: StringBuffer): Unit = {
    ctm.keys.toSeq.sorted.foreach(k => s.append(k + " -> " + ctm(k) + "\n"))
  }

  def depthSet(ss: Set[ProvType]): Int = {
    ss.map(s => s.depth()).max
  }

  def typeMap(m: scala.collection.mutable.Map[Int, Set[ProvType]], s_acc: scala.collection.mutable.Set[Set[ProvType]]): (Map[Int, ProvType], Map[Int, Set[ProvType]]) = {

    val keys: Seq[Int] = m.keys.toSeq.sorted

    var count: Int = 1

    var mm = Map[Int, ProvType]()
    var mmSet = Map[Int, Set[ProvType]]()

    mmSet = mmSet + (0 -> Set())

    keys.foreach(level => {
      val types: Set[ProvType] = m(level)

      val sortedTypes=types.toSeq.sortWith{(t1,t2) => t1.order()<t2.order()}

      sortedTypes.foreach(atype => {

        val thisCount = count
        count = count + 1
        mm = mm + (thisCount -> atype)


      })

      s_acc.filter(ss => level == depthSet(ss)).foreach(ss => {
        val thisCount = count
        count = count + 1
        mmSet = mmSet + (thisCount -> ss)
      })
    })

    (mm, mmSet)

  }
}


final class TypeMapProcessing {

  def trim (s:String): String = {
    if (s.startsWith("\"")) {
      s.substring(1,s.length-1)
    } else {
      s
    }
  }

  def process(pathToSummaryDescription: String,
              accumulator: scala.collection.mutable.Map[Int, Map[ProvType,Int]],
              ground: scala.collection.mutable.ListBuffer[String],
              set_accumulator: scala.collection.mutable.Set[Set[ProvType]],
              recurse: Boolean=false): Unit = {
    val desc: SummaryDescriptionJson = TypePropagator.om.readValue(new FileInputStream(pathToSummaryDescription), classOf[SummaryDescriptionJson])
    processSummary(desc, accumulator, ground, set_accumulator, recurse)
  }

  def process(doc: Document,
              accumulator: scala.collection.mutable.Map[Int,  Map[ProvType,Int]],
              ground: scala.collection.mutable.ListBuffer[String],
              set_accumulator: scala.collection.mutable.Set[Set[ProvType]],
              recurse: Boolean,
              level0: Level0Mapper,
              level:Int): SummaryDescriptionJson = {

    val kernel=true;
    val aggregated=true;


    val config: SummaryConfig = new SummaryConfig(level, kernel, aggregated)
    val pair = SummaryAPI.sum(doc, config, level0)
    val ind: Indexer = pair._1
    val tp: TypePropagator = pair._2
    val indexed: SummaryIndex = SummaryAPI.makeSummaryIndex(config, tp, ind, level, null, null)
    val desc: SummaryDescriptionJson = indexed.summaryDescription()
    processSummary(desc, accumulator, ground, set_accumulator, recurse)
    desc
  }




  def processSummary(desc: SummaryDescriptionJson,
                     accumulator: scala.collection.mutable.Map[Int,  Map[ProvType,Int]],
                     ground: scala.collection.mutable.ListBuffer[String],
                     set_accumulator: scala.collection.mutable.Set[Set[ProvType]],
                     recurse: Boolean=false): Unit = {


    val allTypes: Map[Int, Set[ProvType]] = desc.types

    allTypes.foreach { case (k, provType) =>

      accumulateProvTypes(provType)
    }

    def accumulateProvTypes[T <: ProvType](provType: Set[T]): Unit = {
      set_accumulator += provType.asInstanceOf[Set[ProvType]]
      provType.foreach(accumulateProvType)
    }

    def accumulateProvType(pt: ProvType): Unit = {
      val depth = pt.depth()
      val m: Map[ProvType, Int] = accumulator.getOrElseUpdate(depth, Map())
      if (!m.contains(pt)) {
        val id = m.size
        accumulator.put(depth, m + (pt -> id))
      }

      pt match {
        case Prim(ss) => ground ++= ss
        case _ => // do nothing
      }

      if (recurse) {
        // normally, there is no need to do the recursion, as types of smaller depth should been declared, but just making sure!
        pt match {
          case t1: CompositeForwardProvType => accumulateProvTypes(t1.t)
          case t2: CompositeBackwardProvType => accumulateProvTypes(t2.t)
          case _ => // nothing to recurse on
        }
      }
    }
  }

}