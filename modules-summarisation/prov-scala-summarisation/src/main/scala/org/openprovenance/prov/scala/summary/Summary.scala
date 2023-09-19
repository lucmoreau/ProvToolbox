package org.openprovenance.prov.scala.summary
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import org.apache.logging.log4j.{LogManager, Logger}
import org.openprovenance.prov.model.Namespace
import org.openprovenance.prov.scala.immutable.NS.QN_LEVEL0
import org.openprovenance.prov.scala.immutable._
import org.openprovenance.prov.scala.summary.Summary.logger
import org.openprovenance.prov.scala.summary.TypePropagator.QN_NBR
import org.openprovenance.prov.scala.summary.types.{FlatType, NumberedFlatType, Prim, ProvType}

import java.io.{File, InputStream, OutputStream, StringWriter}
import scala.jdk.CollectionConverters._
import scala.collection.mutable


trait Level0 {
  import org.openprovenance.prov.scala.summary.types._
  def typeToProvType(name: Type): Option[ProvType with ForwardType with BackwardType]
  def typeToProvType(name: Other): Option[ProvType with ForwardType with BackwardType]
  val properties: Set[String]
}

object Summary {
  val logger: Logger = LogManager.getLogger("Summary")
}

class DefaultLevel0 extends Level0 {
  import org.openprovenance.prov.scala.summary.types._
  override def typeToProvType(name: Type): Option[ProvType with ForwardType with BackwardType] = {
    Some(Prim(name.value.toString))
  }
  override def typeToProvType(name: Other): Option[ProvType with ForwardType with BackwardType] = {
    Some(Prim(name.value.toString))
  }
  override val properties: Set[String]=Set()

}

@JsonIgnoreProperties(ignoreUnknown = true)
case class Level0MapperJson (mapper: Map[String,String], ignore: Set[String], properties: Set[String]) {}

@JsonIgnoreProperties(ignoreUnknown = true)
case class SummaryConfiguration(level: Int, level0: Level0MapperJson, aggregated: Boolean){}

case class FeaturesJson  (@JsonDeserialize(contentAs = classOf[java.lang.Integer]) features:       Map[String,Int]) {}

@JsonIgnoreProperties(ignoreUnknown = true)
case class SummaryDescriptionJson  (@JsonDeserialize(keyAs = classOf[java.lang.Integer])      types:       Map[Int,Set[ProvType]],
                                    @JsonDeserialize(keyAs = classOf[java.lang.Integer])      typeStrings: Map[Int,String],
                                    @JsonDeserialize(contentAs = classOf[java.lang.Integer])  names:       Map[String,Int],
                                    @JsonDeserialize(keyAs = classOf[java.lang.Integer])      base:        Map[Int,Set[String]],
                                    @JsonDeserialize(keyAs = classOf[java.lang.Integer])      prettyNames: Map[Int,String],
                                    prefixes:    Map[String,String],
                                    nsBase:      String) {

/*
  private val reverseTypes: Map[Set[ProvType], NumberedFlatType] = types.map{case (k,v) => (v,NumberedFlatType(-33,k))}



  def printFlattener(s: Set[_ <: ProvType]):NumberedFlatType = {
    println("flattener " + s + " " + reverseTypes.get(s.asInstanceOf[Set[ProvType]]))
    reverseTypes.getOrElse(s.asInstanceOf[Set[ProvType]], NumberedFlatType(-10, -1))
  }


 */
  def printFlattener2(level:Int, reverseTypes: Map[Set[ProvType], NumberedFlatType]): Set[_ <: ProvType] => NumberedFlatType = {
    (s: Set[_ <: ProvType]) => {
      logger.info("flattener2 " + s + " for level " + level + " # " + reverseTypes.get(s.asInstanceOf[Set[ProvType]]))
      reverseTypes(s.asInstanceOf[Set[ProvType]])
    }
  }
  /*
  def processProvTypes(typesMap: Map[Int, Set[ProvType]]): Map[Int, Set[FlatType]] = {

    val m: Map[Int, Set[FlatType]] =typesMap.map{ case (k,typeSet) =>
      val str: Set[FlatType] =ProvType.flattenTypes(typeSet,printFlattener)
      (k,str)
    }
    m
  }

   */

  def processProvTypes(level:Int, typesMaps: mutable.Map[Int,Map[Int, Set[ProvType]]]): Map[Int, Set[FlatType]] = {
    logger.info("processTypes " + level)


    val previousLevelTypesMap: Map[Int, Set[ProvType]] =typesMaps.getOrElse(level-1, Map())
    val reverseTypes: Map[Set[ProvType], NumberedFlatType] = previousLevelTypesMap.map{case (k,v) => (v,NumberedFlatType(level-1,k))}


    val m: Map[Int, Set[FlatType]] =typesMaps(level).map{ case (k,typeSet) =>
      val str: Set[FlatType] =ProvType.flattenTypes(typeSet,printFlattener2(level,reverseTypes))
      (k,str)
    }
    m
  }

  /*
  def toBeanTODELETE_MAYBE: SummaryDescriptionBean = {
    val sdb = new SummaryDescriptionBean
    sdb.setNames(names.map{case (s,i)=>(s,i:java.lang.Integer)})
    sdb.setTypeStrings(typeStrings.map{case (i,s)=>(i:java.lang.Integer,s)})
    sdb.setPrettyNames(prettyNames.map{case (i,s)=>(i:java.lang.Integer,s)})
    sdb.setPrefixes(prefixes)
    sdb.setNsBase(nsBase)
    sdb.setBase(base.map{case (i,s)=>(i:java.lang.Integer,s.asJava)})
    processProvTypes(types)
    sdb
  }

   */

  def getFeatures: Map[String,Int] = {
    val allKeys = typeStrings.keys
    allKeys.map(k => (typeStrings(k),base(k).size)).toMap
  }
  def setFeatures(in: Map[String,Int]): Unit = {}

  private var myFlatTypes:Map[Int, Set[FlatType]]=null

  def getFlatTypes: Map[Int, Set[FlatType]] = {
   myFlatTypes
  }

  def getFlatTypes(level:Int, allDescriptions:mutable.Map[Int,SummaryDescriptionJson]): Map[Int, Set[FlatType]] = {
    //println("getFlatTypes " + level + " " + allDescriptions.keySet)
    myFlatTypes=processProvTypes(level, allDescriptions.map{case (k,v) => (k,v.types)})
    myFlatTypes
  }

  def setFlatTypes(in: Map[Int,Set[FlatType]]): Unit = {}
}

object Level0Mapper {
  def apply(s: String): Level0Mapper = {
    new Level0Mapper(TypePropagator.om.readValue(s, classOf[Level0MapperJson]))
  }
  def apply(f: File): Level0Mapper = {
    new Level0Mapper(TypePropagator.om.readValue(f, classOf[Level0MapperJson]))
  }
  def apply(is: InputStream): Level0Mapper = {
    new Level0Mapper(TypePropagator.om.readValue(is, classOf[Level0MapperJson]))
  }
  def apply(b: org.openprovenance.prov.summary.Level0MapperJson): Level0Mapper = {
    if (b==null) null else
      new Level0Mapper(if (b.getMapper!=null) b.getMapper.asScala.toMap else null,
        if (b.getIgnore!=null) b.getIgnore.asScala.toSet else null,
        if (b.getProperties!=null) b.getProperties.asScala.toSet else null)
  }
}


class Level0Mapper (mapper: Map[String,String], ignore: Set[String], override val properties: Set[String]) extends DefaultLevel0 {
  import TypePropagator.om
  def this(l0: Level0MapperJson) {
    this(l0.mapper,l0.ignore, l0.properties)
  }
  import org.openprovenance.prov.scala.summary.types._
  override def typeToProvType(name: Type): Option[ProvType with ForwardType with BackwardType] = {

    val o=name.value
    val s= o match {
      case q:QualifiedName => q.getUri
      case _ => o.toString
    }

    if (ignore.contains(s)) {
      None
    } else {
      val inp=mapper.get(s)
      inp match {
        case None => super.typeToProvType(name)
        case Some(s2) => Some(Prim(s2))
      }
    }
  }


  def exportToJSon(): StringWriter = {
    val outw = new StringWriter
    om.writeValue(outw, asJavaBean())
    outw
  }

  def asJavaBean(): Level0MapperJson = {
    Level0MapperJson(mapper, ignore, properties)
  }

  def exportToJSon(out: OutputStream): Unit = {
    om.writeValue(out, asJavaBean())
  }

}


object NamespaceHelper {
  def toNamespace(qns: Iterable[QualifiedName]): Namespace = {
    qns.map(q => (q.prefix,q.namespaceURI))
      .foldLeft(new Namespace){ case (old:Namespace,(pref:String, value:String)) =>
        old.register(pref, value)
        old }
  }
  def toNamespace(prefixes: Map[String,String]): Namespace = {
    prefixes
      .seq
      .foldLeft(new Namespace){ case (old:Namespace,(pref:String, value:String)) =>
        old.register(pref, value)
        old }
  }


}
object PrettyMethod extends Enumeration {
  type Pretty = Value
  val Name, Type, Anonymous = Value
}

object SummaryConstructor {
  import TypePropagator.u

  def anonymousName(i: Int): String ={
    "T"+i
  }


  def typeQualifiedName(i: Int, ns: String): QualifiedName = {
    new QualifiedName("t", anonymousName(i),ns) // TODO: heuristics required here
  }


  def commonLocalName(set:Set[String]): String = {
    val s=u.commonLocalName(set.toList.asJava)
    if (s==null||s.equals("")) "T" else s
  }

  def makePrettyNames(mapToBaseUri: Map[Int,Set[String]],
                      provTypeIndex: Map[Set[ProvType], Int],
                      prettyMethod : PrettyMethod.Pretty): Map[Int,String] = {
    prettyMethod match {
      case PrettyMethod.Name => mapToBaseUri.map{case (i,set) => (i,commonLocalName(set)+'_'+i.toString)}
      case PrettyMethod.Type => provTypeIndex.map{case (set, i) => (i, set.flatMap(_.getTypes).mkString("-")+'_'+i.toString)}
      case PrettyMethod.Anonymous => provTypeIndex.map{case (set, i) => (i, anonymousName(i))}
    }
  }

}

class SummaryConstructor (val ind: Indexing,
                          val provtypes: Map[Int,Set[ProvType]],
                          val nsBase:String,
                          val prettyMethod : PrettyMethod.Pretty = PrettyMethod.Name,
                          val withLevel0Description: Boolean= false) {
  import SummaryConstructor.{makePrettyNames, typeQualifiedName}
  import TypePropagator.QN_SIZE
  import org.openprovenance.prov.scala.immutable.Indexer.{PROV_ACTIVITY, PROV_AGENT, PROV_ENTITY}

  //type SummaryIndex=Int
  //type baseIndex=Int


  val xsd_string: QualifiedName =ProvFactory.pf.xsd_string
  val xsd_int: QualifiedName =ProvFactory.pf.xsd_int


  val typesVector: Seq[Set[ProvType]] =provtypes.values.to(scala.collection.immutable.HashSet).toVector
  val ordered: Seq[Set[ProvType]] =typesVector.sortBy(t => if (t.isEmpty) (0,t.hashCode()) else (t.map(_.depth()).max,t.hashCode())) // order by maximal depth, and then hash code
  val provTypeIndex: Map[Set[ProvType], Int] =ordered.zipWithIndex.toMap   //.asInstanceOf[Map[Set[ProvType],SummaryIndex]]

  val qn: Map[Int, QualifiedName] =provTypeIndex.map{ case (s,i) => (i,typeQualifiedName(i,nsBase)) }
  val amap: Map[QualifiedName, Int] =Indexer.swap(qn) // inverse of qn


  val toSummary0: Map[Int,Int]=provtypes.map{ case (src,t) => (src,provTypeIndex(t)) }
  val toSummary1: Map[Int, Int] = toSummary0 + {(-1,-1)}

  def toSummary2 (src: Int) : Int = {
    toSummary1.get(src) match {
      case Some(result) => result
      case None =>
        println("trouble toSummary is incomplete" ++ toSummary1.toString())
        println("ProvTypes are: " ++ provtypes.toString())
        println("Index map is: " ++ ind.amap.toString())
        toSummary1(src)
    }
  }


  def sumQn (q: QualifiedName): QualifiedName = {
    if (q==null) null else qn(toSummary2(ind.amap(q)))
  }


  def labAttr(s:String): Attribute = {
    new Label(xsd_string,
      new LangString(s,None))
  }


  def label(q: QualifiedName): Set[LangString] = {
    val str=prettyNames(amap(q))
    Set(new LangString(str, None))
  }

  def nodeWithCountAndLabel(kind: Kind.Kind, id: QualifiedName,c: Int,  nbr: Option[Int]): Node = {
    kind match {
      case Kind.ent => new Entity  (id,label(id), Set(), None, Set(), other(c, nbr))
      case Kind.ag  => new Agent   (id,label(id), Set(), None, Set(), other(c, nbr))
      case Kind.act => new Activity(id,None,None,label(id),Set(),Set(), other(c, nbr))	  			}
  }
  def nodeWithLabel(kind: Kind.Kind, id: QualifiedName): Node = {
    nodeWithCountAndLabel(kind,id,0, None)
  }


  def convertRelation(rel:Relation): Relation = {
    convertRelation(rel,Map())
  }

  def convertRelation(rel:Relation, other: Map[QualifiedName,Set[Other]]): Relation = {
    val effect=sumQn(rel.getEffect)
    val cause =sumQn(rel.getCause)

    rel.enumType match {
      case Kind.wgb    => new WasGeneratedBy(null, effect, cause, None, Set(), Set(), Set(), Set(), other)
      case Kind.wib    => new WasInvalidatedBy(null, effect, cause, None, Set(), Set(), Set(), Set(), other)
      case Kind.usd    => new Used(null, effect, cause, None, Set(), Set(), Set(), Set(), other)
      case Kind.wsb    => new WasStartedBy(null, effect, cause, sumQn(rel.asInstanceOf[WasStartedBy].starter), None, Set(), Set(), Set(), Set(), other)
      case Kind.web    => new WasEndedBy  (null, effect, cause, sumQn(rel.asInstanceOf[WasEndedBy].ender)    , None, Set(), Set(), Set(), Set(), other)
      case Kind.wat    => new WasAttributedTo(null, effect, cause, Set(),  Set(), other)
      case Kind.waw    => new WasAssociatedWith(null, effect, cause, sumQn(rel.asInstanceOf[WasAssociatedWith].plan), Set(),  Set(), Set(), other)
      case Kind.aobo   => new ActedOnBehalfOf(null, effect, cause, sumQn(rel.asInstanceOf[ActedOnBehalfOf].activity), Set(), Set(), other)
      case Kind.spec   => new SpecializationOf(null, effect, cause, Set(),  Set(), other)
      case Kind.alt    => new AlternateOf(null, effect, cause, Set(),  Set(), other)
      case Kind.mem    => new HadMember(null, effect, Set(cause), Set(), Set(), other) // TODO: This does not seem to work for multiple members
      case Kind.winfob => new WasInformedBy(null, effect, cause, Set(),  Set(), other)
      case Kind.winfl  => new WasInfluencedBy(null, effect, cause, Set(),  Set(), other)
      case Kind.wdf    => new WasDerivedFrom(null,effect, cause, sumQn(rel.asInstanceOf[WasDerivedFrom].activity), null, null, Set(), Set(), other)
      case Kind.men    => new MentionOf(null, effect, cause,sumQn(rel.asInstanceOf[MentionOf].bundle), Set(), Set(), other)

    }
  }


  def other(c:Int): Map[QualifiedName,Set[Other]] = {
    Map((QN_SIZE, Set(new Other(QN_SIZE,
      xsd_int,
      c.toString))))
  }

  // only construct if needed in other() function, when withLevel0Description flag is on
  lazy val level0Descriptors: Map[Int, Set[String]] = if (withLevel0Description) {
    // extracts the Prim types (this of course assumes that level0 types were inserted in level N types.
    provTypeIndex.map{ case (types,k) => (k,types.collect{case Prim(x) => x}.flatten)}
  } else {
    // no need of a level0 Descriptors.
    Map()
  }

  var done=false

  def other(size:Int, nbr:Option[Int]): Map[QualifiedName,Set[Other]] = {
    val map_nbr:   Option[Map[QualifiedName, Set[Other]]] =nbr  .map(nbr   => Map((QN_NBR,   Set(new Other(QN_NBR,   xsd_int, nbr.toString)))))

    val result:    Map[QualifiedName, Set[Other]] = Map((QN_SIZE, Set(new Other(QN_SIZE, xsd_int, size.toString))))
    val result2: Map[QualifiedName, Set[Other]] = result ++ map_nbr.getOrElse(Map())


    if (withLevel0Description) {
      val result3: Option[Map[QualifiedName, Set[Other]]] = nbr.map(num  =>{
        val descriptors: Set[String] =level0Descriptors(num)
        Map((QN_LEVEL0, descriptors.map(d => new Other(QN_LEVEL0, xsd_string, d))))
      })
      result2++result3.getOrElse(Map())
    } else result2
  }

  // In this function, QualifiedNames are already those of the summary, no mapping necessary for these. Just add a size!
  def relationWithCount(rel:Relation,c: Int): Relation = {
    val effect=rel.getEffect
    val cause=rel.getCause

    val o=other(c)

    rel.enumType match {
      case Kind.wgb    => new WasGeneratedBy(null, effect, cause, None, Set(), Set(), Set(), Set(), o)
      case Kind.wib    => new WasInvalidatedBy(null, effect, cause, None, Set(), Set(), Set(), Set(), o)
      case Kind.usd    => new Used(null, effect, cause, None, Set(), Set(), Set(), Set(), o)
      case Kind.wsb    => new WasStartedBy(null, effect, cause, rel.asInstanceOf[WasStartedBy].starter, None, Set(), Set(), Set(), Set(), o)
      case Kind.web    => new WasEndedBy  (null, effect, cause, rel.asInstanceOf[WasEndedBy].ender    , None, Set(), Set(), Set(), Set(), o)
      case Kind.wat    => new WasAttributedTo(null, effect, cause, Set(),  Set(), o)
      case Kind.waw    => new WasAssociatedWith(null, effect, cause, rel.asInstanceOf[WasAssociatedWith].plan, Set(),  Set(), Set(), o)
      case Kind.aobo   => new ActedOnBehalfOf(null, effect, cause, rel.asInstanceOf[ActedOnBehalfOf].activity, Set(), Set(), o)
      case Kind.spec   => new SpecializationOf(null, effect, cause, Set(),  Set(), o)
      case Kind.alt    => new AlternateOf(null, effect, cause, Set(),  Set(), o)
      case Kind.mem    => new HadMember(null, effect, Set(cause), Set(), Set(), o) // TODO: This does not seem to work for multiple members
      case Kind.winfob => new WasInformedBy(null, effect, cause, Set(),  Set(), o)
      case Kind.winfl  => new WasInfluencedBy(null, effect, cause, Set(),  Set(), o)
      case Kind.wdf    => new WasDerivedFrom(null,effect, cause, rel.asInstanceOf[WasDerivedFrom].activity, null, null, Set(), Set(), o)
      case Kind.men    => new MentionOf(null, effect, cause,rel.asInstanceOf[MentionOf].bundle, Set(), Set(), o)

    }
  }


  // In this function, QualifiedNames are already those of the summary, no mapping necessary for these. Just add a size!
  def relationWithCount(t:Kind.Kind, effect:QualifiedName , cause: QualifiedName, c: Int, rel:Relation): Relation = {
    //val effect=rel.getEffect
    //val cause=rel.getCause

    val o=other(c)

    t match {
      case Kind.wgb    => new WasGeneratedBy(null, effect, cause, None, Set(), Set(), Set(), Set(), o)
      case Kind.wib    => new WasInvalidatedBy(null, effect, cause, None, Set(), Set(), Set(), Set(), o)
      case Kind.usd    => new Used(null, effect, cause, None, Set(), Set(), Set(), Set(), o)
      case Kind.wsb    => new WasStartedBy(null, effect, cause, rel.asInstanceOf[WasStartedBy].starter, None, Set(), Set(), Set(), Set(), o)
      case Kind.web    => new WasEndedBy  (null, effect, cause, rel.asInstanceOf[WasEndedBy].ender    , None, Set(), Set(), Set(), Set(), o)
      case Kind.wat    => new WasAttributedTo(null, effect, cause, Set(),  Set(), o)
      case Kind.waw    => new WasAssociatedWith(null, effect, cause, rel.asInstanceOf[WasAssociatedWith].plan, Set(),  Set(), Set(), o)
      case Kind.aobo   => new ActedOnBehalfOf(null, effect, cause, rel.asInstanceOf[ActedOnBehalfOf].activity, Set(), Set(), o)
      case Kind.spec   => new SpecializationOf(null, effect, cause, Set(),  Set(), o)
      case Kind.alt    => new AlternateOf(null, effect, cause, Set(),  Set(), o)
      case Kind.mem    => new HadMember(null, effect, Set(cause), Set(), Set(), o) // TODO: This does not seem to work for multiple members
      case Kind.winfob => new WasInformedBy(null, effect, cause, Set(),  Set(), o)
      case Kind.winfl  => new WasInfluencedBy(null, effect, cause, Set(),  Set(), o)
      case Kind.wdf    => new WasDerivedFrom(null,effect, cause, rel.asInstanceOf[WasDerivedFrom].activity, null, null, Set(), Set(), o)
      case Kind.men    => new MentionOf(null, effect, cause,rel.asInstanceOf[MentionOf].bundle, Set(), Set(), o)

    }
  }


  //TODO: why map to String? space efficiency to think about?  merge map and groupby

  lazy val mapToBaseUriOLD:Map[Int,Set[String]]=provtypes.map{ case (i,set) => (provTypeIndex(set),ind.idsFun(i).getUri) }.groupBy(_._1).mapValues( s => s.map(_._2).toSet ).toMap
  lazy val mapToBaseUri:Map[Int,Set[String]]= toSummary0.toSeq.map{ case(i,j) => (j,i) }.groupBy(_._1).mapValues( s => s.map(i => ind.idsFun(i._2).getUri).toSet).toMap

  lazy val prettyNames: Map[Int, String] =makePrettyNames(mapToBaseUri,provTypeIndex,prettyMethod)

  private final def getEffect(r: Relation): Int = {
    sumInt(r.getEffect)
  }

  private final def getCause(r: Relation): Int = {
    val cause=r.getCause
    if (cause==null) -1 else { sumInt(cause) }
  }

  def getSuccessors(amap: Map[QualifiedName,Int], ss: Iterable[StatementOrBundle]): Map[Int, Map[Int, Iterable[Relation]]] = {
    ss.collect{case r:Relation => r}
      .groupBy(getEffect)
      .view.mapValues(rels => rels.groupBy(getCause)
                             .view.mapValues(rels2 => rels2.groupBy(convertRelation)
                                                      .map{case (r,ll) => relationWithCount(r,ll.size)}).toMap)
  }.toMap

  lazy val succ: Map[Int, Map[Int, Iterable[Relation]]] =getSuccessors(amap,ind.document().statementOrBundle)

  def primitivesToKind(name: QualifiedName): Kind.Kind = {
    name match {
      case `PROV_ENTITY`   => Kind.ent
      case `PROV_AGENT`    => Kind.ag
      case `PROV_ACTIVITY` => Kind.act
    }
  }


  private final def sumInt(q: QualifiedName): Int = toSummary2(ind.amap(q))

  lazy val summaryNormalNodes: Set[Node] =
    ind
      .document()
      .statementOrBundle
      .collect{case n:Node => n}
      .groupBy(n => sumInt(n.id))
      .map{case(i,iter) => nodeWithCountAndLabel(iter.head.enumType,
                                                 sumQn(iter.head.id),
                                                 iter.size,
                                                 Some(toSummary2(ind.amap(iter.head.id))))
                                                  }.toSet

  lazy val summaryAbsentNodes: Set[Node]
     = ind.absentNodes
          .collect{case i:Int if !(ind.types.get(i).isEmpty)
                    => nodeWithLabel(primitivesToKind(ind.types(i).head), sumQn(ind.idsFun(i)))}

  lazy val summaryNodes:Set[Node]= summaryNormalNodes ++ summaryAbsentNodes

  lazy val idsVec: Iterable[QualifiedName] =amap.keys

  def makeIndex (): SummaryIndex = {
    new SummaryIndex(provTypeIndex, idsVec, qn, summaryNodes, succ, amap, mapToBaseUri, prettyNames, nsBase)
  }
}

object SummaryIndex {

  def exportToJsonDescription(outw: java.io.Writer, si: SummaryIndex): SummaryDescriptionJson = {
    TypePropagator.om.enable(SerializationFeature.INDENT_OUTPUT)
    val summaryDescriptionJson = si.summaryDescription()
    TypePropagator.om.writeValue(outw, summaryDescriptionJson)
    summaryDescriptionJson
  }
  def exportToJsonDescription(os: java.io.OutputStream, si: SummaryIndex): Unit = {
    TypePropagator.om.enable(SerializationFeature.INDENT_OUTPUT)
    TypePropagator.om.writeValue(os, si.summaryDescription())
  }

  def exporToJsonDescription(outw: java.io.Writer, atype: SummaryDescriptionJson): Unit = {
    TypePropagator.om.enable(SerializationFeature.INDENT_OUTPUT)
    TypePropagator.om.writeValue(outw, atype)
  }

  def exportToFeatures(outw: java.io.Writer, si: SummaryIndex): Unit = {
    TypePropagator.om.enable(SerializationFeature.INDENT_OUTPUT)
    TypePropagator.om.writeValue(outw, FeaturesJson(si.summaryDescription().getFeatures))
  }

}

class SummaryIndex(val provTypeIndex: Map[Set[ProvType],Int],
                   val idsVec:        Iterable[QualifiedName],
                   val idsFun:        Map[Int,QualifiedName],
                   val sumNodes:      Set[Node],
                   val succ:          Map[Int,Map[Int,Iterable[Relation]]],
                   val amap:          Map[QualifiedName,Int],
                   val mapToBaseUri:  Map[Int,Set[String]],
                   val prettyNames:   Map[Int,String],
                   val nsBase:        String) extends Indexing {

  import Indexer.swap
  import TypePropagator.QN_SIZE

  def this(desc: SummaryDescriptionJson, ind: Indexing) {
    this(Indexer.swap(desc.types),
      ind.idsVec,
      ind.idsFun,
      ind.nodes.values.toSet,
      ind.succ,
      ind.idsVec.zipWithIndex.toMap,
      desc.base,
      desc.prettyNames,
      desc.nsBase)
  }

  lazy val edgeQuantities: Iterable[Int] =getEdgeQuantities(succ)


  lazy val edgeMax: Int =if (edgeQuantities.isEmpty) 0 else edgeQuantities.max
  lazy val edgeMin: Int =if (edgeQuantities.isEmpty) 0 else edgeQuantities.min


  lazy val nodeQuantities: Set[Int] =sumNodes.flatMap(s  => s.other.get(QN_SIZE) )
    .flatten
    .map(o => o.value.toString.toInt)

  lazy val nodeMax: Int =if (nodeQuantities.isEmpty) 0 else nodeQuantities.max
  lazy val nodeMin: Int =if (nodeQuantities.isEmpty) 0 else nodeQuantities.min

  lazy val sizeRange: SizeRange = SizeRange(edgeMin,edgeMax,nodeMin,nodeMax)



  val pred: Null =null //TODO
  val nodes: Map[Int, Node] =sumNodes.map (n => (amap(n.id),n)).toMap
  val types: Null =null//TODO

  val absentNodes: Set[Int] =Set[Int]()

  val qns: Iterable[QualifiedName] =amap.keys
  val ns: Namespace =NamespaceHelper.toNamespace(qns)

  val prefixes: Map[String, String] =ns.getPrefixes.asScala.toMap


  def document (): Document = {
    makeDocument(sumNodes,succ)  //TODO: casting of nodes to statement is not nice, why aren't they subclasses?
  }

  def makeDocument(nodes: Set[_ <: Statement], rels: Map[Int, Map[Int, Iterable[Relation]]] ): Document = {
    val ns=new Namespace
    ns.addKnownNamespaces()
    ns.register("t", nsBase)
    ns.register("sum", TypePropagator.SUM_NS)
    ns.register("provext", "http://openprovenance.org/prov/extension#")


    val set: Iterable[Relation] =rels.values.flatMap(x=>x.values.flatMap(y=>y) )

    val newset=nodes ++ set

    new Document(newset,ns)
  }


  def summaryDescription(): SummaryDescriptionJson = {
     SummaryDescriptionJson(swap(provTypeIndex),
                            swap(provTypeIndex).map{ case (k,v) => (k,ProvType.sortedTypesSeq(v))},
                            amap.map{ case(n,i) => (ns.qualifiedNameToString(n),i)},
                            mapToBaseUri,
                            prettyNames,
                            prefixes,
                            nsBase)
  }

  def exportToJsonDescription(outw: java.io.Writer): SummaryDescriptionJson = {
    SummaryIndex.exportToJsonDescription(outw, this)
  }
  def exportToJsonDescription(os: java.io.OutputStream): Unit = {
    SummaryIndex.exportToJsonDescription(os, this)
  }

  def exportToFeatures(outw: java.io.Writer): Unit = {
    SummaryIndex.exportToFeatures(outw, this)
  }

  def diff (other: SummaryIndex): (SummaryDescriptionJson, SummaryDescriptionJson, SummaryDescriptionJson, SummaryDescriptionJson) = {
    val meProvTypes   = this.provTypeIndex.keySet
    val otherProvTypes=other.provTypeIndex.keySet

    val mePrefixes    = this .prefixes
    val otherPrefixes = other.prefixes

    val meNamespace    = NamespaceHelper.toNamespace(mePrefixes)
    val otherNamespace = NamespaceHelper.toNamespace(otherPrefixes)

    val meQualifiedNames    =  this.amap.keySet
    val otherQualifiedNames = other.amap.keySet

    val onlyMeProvTypes=meProvTypes.diff(otherProvTypes)
    val commonProvTypes=meProvTypes.intersect(otherProvTypes)
    val onlyOtherProvTypes=otherProvTypes.diff(meProvTypes)

    val onlyMeProvTypeIndex     = this.provTypeIndex.filterKeys(k => onlyMeProvTypes.contains(k))
    val meCommonProvTypeIndex   = this.provTypeIndex.filterKeys(k => commonProvTypes.contains(k))
    val onlyOtherProvTypeIndex  =other.provTypeIndex.filterKeys(k => onlyOtherProvTypes.contains(k))
    val otherCommonProvTypeIndex=other.provTypeIndex.filterKeys(k => commonProvTypes.contains(k))

    val onlyMeIndexes     =onlyMeProvTypeIndex.values.toSet
    val meCommonIndexes   =meCommonProvTypeIndex.values.toSet
    val onlyOtherIndexes  =onlyOtherProvTypeIndex.values.toSet
    val otherCommonIndexes=otherCommonProvTypeIndex.values.toSet

    def filteredBaseuri (provTypeIndex: Map[Set[ProvType],Int], onlyMeBaseuri: Map[Set[ProvType],Int], mapToBaseUri:  Map[Int,Set[String]])= {
      provTypeIndex.map{     case (provtype,     i:Int) =>
        (onlyMeBaseuri.get(provtype), i) }
        .flatMap{ case (optionalInt,  i:Int) => optionalInt match { case Some(value) => Some(value -> mapToBaseUri(i))
        case None => None }}
    }

    val onlyMeBaseuri     =filteredBaseuri(provTypeIndex,       onlyMeProvTypeIndex.toMap,       this.mapToBaseUri)
    val meCommonBaseuri   =filteredBaseuri(provTypeIndex,       meCommonProvTypeIndex.toMap,     this.mapToBaseUri)
    val onlyOtherBaseuri  =filteredBaseuri(other.provTypeIndex, onlyOtherProvTypeIndex.toMap,   other.mapToBaseUri)
    val otherCommonBaseuri=filteredBaseuri(other.provTypeIndex, otherCommonProvTypeIndex.toMap, other.mapToBaseUri)


    val onlyMeNames     =this.amap.filter{case (n,i) => onlyMeIndexes.contains(i)}.map{ case(n,i) => (meNamespace.qualifiedNameToString(n),i)}
    val meCommonNames   =this.amap.filter{case (n,i) => meCommonIndexes.contains(i)}.map{ case(n,i) => (meNamespace.qualifiedNameToString(n),i)}
    val onlyOtherNames  =other.amap.filter{case (n,i) => onlyOtherIndexes.contains(i)}.map{ case(n,i) => (otherNamespace.qualifiedNameToString(n),i)}
    val otherCommonNames=other.amap.filter{case (n,i) => otherCommonIndexes.contains(i)}.map{ case(n,i) => (otherNamespace.qualifiedNameToString(n),i)}
/*
    val onlyMePrettyNames     =this.prettyNames.filter{case (i,n) => onlyMeIndexes.contains(i)}
    val meCommonPrettyNames   =this.prettyNames.filter{case (i,n) => meCommonIndexes.contains(i)}
    val onlyOtherPrettyNames  =other.prettyNames.filter{case (i,n) => onlyOtherIndexes.contains(i)}
    val otherCommonPrettyNames=other.prettyNames.filter{case (i,n) => otherCommonIndexes.contains(i)}


 */
    // TODO: I use the same nsBase for all. Doesn't seem right.

    val onlyMeSummaryDescription=       SummaryDescriptionJson(swap(onlyMeProvTypeIndex.toMap),      swap(onlyMeProvTypeIndex.toMap).map{ case (k,v) => (k,v.toString)},      onlyMeNames,      onlyMeBaseuri,      prettyNames, this.prefixes, this.nsBase)
    val meCommonSummaryDescription=     SummaryDescriptionJson(swap(meCommonProvTypeIndex.toMap),    swap(meCommonProvTypeIndex.toMap).map{ case (k,v) => (k,v.toString)},    meCommonNames,    meCommonBaseuri,    prettyNames, this.prefixes, this.nsBase)
    val onlyOtherSummaryDescription=    SummaryDescriptionJson(swap(onlyOtherProvTypeIndex.toMap),   swap(onlyOtherProvTypeIndex.toMap).map{ case (k,v) => (k,v.toString)},   onlyOtherNames,   onlyOtherBaseuri,   prettyNames, other.prefixes, this.nsBase)
    val otherCommonSummaryDescription=  SummaryDescriptionJson(swap(otherCommonProvTypeIndex.toMap), swap(otherCommonProvTypeIndex.toMap).map{ case (k,v) => (k,v.toString)}, otherCommonNames, otherCommonBaseuri, prettyNames, other.prefixes, this.nsBase)

    (onlyMeSummaryDescription, meCommonSummaryDescription, onlyOtherSummaryDescription, otherCommonSummaryDescription)

  }


}
