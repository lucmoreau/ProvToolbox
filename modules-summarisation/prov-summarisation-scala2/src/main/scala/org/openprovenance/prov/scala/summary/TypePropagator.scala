package org.openprovenance.prov.scala.summary
import java.io.InputStream
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.{ClassTagExtensions, DefaultScalaModule}
import org.openprovenance.prov.immutable.Utils
import org.openprovenance.prov.model.Namespace
import org.openprovenance.prov.scala.immutable._
import org.openprovenance.prov.scala.summary.types.{BackwardType, ForwardType, _}
import org.openprovenance.prov.scala.viz.Graphics

import scala.collection.immutable


object TypePropagator {

  // val TYPE_NS = "http://openprovenance.og/summary/types#"
  val SUM_NS: String = NS.SUM_NS
  val om: ObjectMapper with ClassTagExtensions = new ObjectMapper() with ClassTagExtensions
  om.registerModule(DefaultScalaModule)
  import org.openprovenance.prov.scala.immutable.ProvFactory.pf
  
  val u=new Utils


  val QN_SIZE: QualifiedName  = NS.QN_SIZE
  val QN_COUNT: QualifiedName = NS.QN_COUNT
  val QN_NBR: QualifiedName   = NS.QN_NBR
  val QN_LEVEL0: QualifiedName=NS.QN_LEVEL0

      
  def highlightStatement (s: Statement, allIds: Set[QualifiedName]): Statement = {
    s match {
      case rel:Relation => if (allIds.contains(rel.getCause) && allIds.contains(rel.getEffect)) {
    	                       s
                           } else {
                        	   s.addAttributes(attr())
                           }
      case _ =>
        val id=s.id
        if (allIds.contains(id)) {
          s
        } else {
          s.addAttributes(attr())
        }
    }
  }

    def attr(): Set[Attribute] = {
			Set(new Other(Graphics.colorQN,
					          ProvFactory.pf.xsd_string,
					          "gray52"),
					new Other(Graphics.fillcolorQN,
					          ProvFactory.pf.xsd_string,
					          "gray99"),
					new Other(Graphics.fontcolorQN,
					          ProvFactory.pf.xsd_string,
					          "gray52"))
    }
    
    def highlight (doc: OrderedDocument, desc: SummaryDescriptionJson): OrderedDocument = {
        val ns=new Namespace(doc.getNamespace)        
        ns.register("dot",Graphics.DOT_NS)
        val allIds=desc.names.keySet.map(s => ns.stringToQualifiedName(s, pf).asInstanceOf[QualifiedName])

        val newStatements=doc.orderedStatements().map(highlightStatement(_, allIds))
        new OrderedDocument(newStatements, ns)
    }
    
    def binaryRelation (rel:Relation): Boolean = {
     rel.enumType match {
          case Kind.wdf    => false
          case Kind.wgb    => true
          case Kind.usd    => true
          case Kind.wsb    => false
          case Kind.web    => false
          case Kind.wib    => true
          case Kind.waw    => false
          case Kind.wat    => true
          case Kind.aobo   => false
          case Kind.winfob => true
          case Kind.winfl  => true
          case Kind.alt    => true
          case Kind.spec   => true
          case Kind.mem    => true
          case Kind.men    => false
        }
    }

  // determines if src is the root of a triangle ending in dst
  // src is the source of the propagation: in forward propagation, it's the older element
  // with wdf(dst,src), usd(i,src), wgb(dst,i)
  // here allRelations, from cause to effect
  def triangularRelation (rel:Relation, src:Int, dst:Int, allRelations: Map[Int,Map[Int,Iterable[Relation]]]): Option[Seq[Int]] = rel.enumType match {
    case Kind.wdf    =>
      val succ: Map[Int, Iterable[Relation]] =allRelations.getOrElse(src,Map())
      val intermediary: Map[Int, Iterable[Relation]] =succ.filter{case (i,l)=> l.exists(r => r.isInstanceOf[Used])}  // starting from oldest, find used
      val completingTriangle: Seq[Int] =intermediary.flatMap{case (i,_) =>
        val succ2: Map[Int, Iterable[Relation]] =allRelations.getOrElse(i,Map())
        val last =succ2.filter{case (j,l)=> l.exists(r => r.isInstanceOf[WasGeneratedBy] && j==dst)}   // and then wgb
        if (last.isEmpty) Seq() else Seq(i)
      }.toSeq
      if (completingTriangle.isEmpty) None else  Some(completingTriangle) //println("triFR", src, dst, completingTriangle);

    case Kind.wgb    => None
    case Kind.usd    => None
    case Kind.wsb    => None
    case Kind.web    => None
    case Kind.wib    => None
    case Kind.waw    => None
    case Kind.wat    => None
    case Kind.aobo   => None
    case Kind.winfob => None
    case Kind.winfl  => None
    case Kind.alt    => None
    case Kind.spec   => None
    case Kind.mem    => None
    case Kind.men    => None
  }

  // determines if src is the root of a triangle ending in dst
  // src is the source of the propagation: in backward propagation, it's the most recent element
  // with wdf(src,dst), usd(i,dst), wgb(src,i)
  // here allReverseRelations, from effect to cause
  def triangularBackwardRelation (rel:Relation, _src:Int, _dst:Int, allReverseRelations: Map[Int,Map[Int,Iterable[Relation]]]): Option[Seq[Int]] =
    rel.enumType match {
      case Kind.wdf =>
        val succ: Map[Int, Iterable[Relation]] = allReverseRelations.getOrElse(_src, Map())
        val intermediary: Map[Int, Iterable[Relation]] = succ.filter { case (i, l) => l.exists(r => r.isInstanceOf[WasGeneratedBy]) }  // starting from most recent, find wgb first
        val completingTriangle: Seq[Int] = intermediary.flatMap { case (i, _) =>
          val succ2: Map[Int, Iterable[Relation]] = allReverseRelations.getOrElse(i, Map())
          val last = succ2.filter { case (j, l) => l.exists(r => r.isInstanceOf[Used] && j == _dst) }  // and then used
          if (last.isEmpty) Seq() else Seq(i)
        }.toSeq
        if (completingTriangle.isEmpty) None else  Some(completingTriangle) //println("triBR", _src, _dst, completingTriangle);

      case Kind.wgb => None
      case Kind.usd => None
      case Kind.wsb => None
      case Kind.web => None
      case Kind.wib => None
      case Kind.waw => None
      case Kind.wat => None
      case Kind.aobo => None
      case Kind.winfob => None
      case Kind.winfl => None
      case Kind.alt => None
      case Kind.spec => None
      case Kind.mem => None
      case Kind.men => None
    }


  // @inline def ifNotNull(q:QualifiedName) = {
 //      if (q!=null) Some(q) else None
 // }
  
  def ternaryElement (rel:Relation): Option[QualifiedName] = {
    rel.enumType match {
          case Kind.wdf    => Option(rel.asInstanceOf[WasDerivedFrom].activity)
          case Kind.wgb    => None
          case Kind.usd    => None
          case Kind.wsb    => Option(rel.asInstanceOf[WasStartedBy].starter)
          case Kind.web    => Option(rel.asInstanceOf[WasEndedBy].ender)
          case Kind.wib    => None
          case Kind.waw    => Option(rel.asInstanceOf[WasAssociatedWith].plan)
          case Kind.wat    => None
          case Kind.aobo   => Option(rel.asInstanceOf[ActedOnBehalfOf].activity)
          case Kind.winfob => None
          case Kind.winfl  => None
          case Kind.alt    => None
          case Kind.spec   => None
          case Kind.mem    => None
          case Kind.men    => Option(rel.asInstanceOf[MentionOf].bundle)
        }
  }

  def readType(in: InputStream): ProvType ={
    val expr=om.readValue(in,classOf[ProvType])
    expr
  }

  def readType(contents: String): ProvType ={
    val expr=om.readValue(contents,classOf[ProvType])
    expr
  }

  def readTypes(contents: String): Set[ProvType] ={
    val expr: Set[ProvType] =om.readValue(contents,classOf[Set[ProvType]])
    expr
  }

}

trait Propagator[Direction] {
    import org.openprovenance.prov.scala.summary.types._

    def propagateTypesOverBinaryRelation (rel: Relation, 
                                         subjectTypes: Set[ProvType with Direction], 
                                         filter: Set[String]=Set()): Set[ProvType with Direction]
    
    
    def propagateTypesOverTernaryRelation (rel: Relation, 
		                                     subjectTypes: Set[ProvType with Direction], 
		                                     subjectTypes2: Set[ProvType with Direction],
                                           src: Int,
                                           dst: Int,
                                           triangles: Seq[(Int,Int,Int)],
		                                     filter: Set[String]=Set()): Set[ProvType with Direction]
    
    def propagateType(rel: Map[Int,Map[Int,Iterable[Relation]]], 
    		              typen: Map[Int,Set[ProvType with Direction]], 
    		              ind: Indexing, 
    		              filter: Set[String]=Set()): Map[Int,Set[ProvType with Direction]]
    
    
   def getProvType(n: Int): Map[Int,Set[ProvType with Direction]]

}



class CommonTypePropagator(ind:Indexing, level0: Level0, primitivep:Boolean) {
  import org.openprovenance.prov.scala.immutable.Indexer.{PROV_ACTIVITY, PROV_AGENT, PROV_ENTITY}
  import org.openprovenance.prov.scala.summary.types._
  
  def this(doc:Document, level0: Level0 = new DefaultLevel0, primitivep:Boolean) = {
    this(new Indexer(doc),level0, primitivep)
  }
  
  val set_Ent: Set[ProvType with ForwardType with BackwardType]=Set(Ent())
  val set_Ag:  Set[ProvType with ForwardType with BackwardType]=Set(Ag())
  val set_Act: Set[ProvType with ForwardType with BackwardType]=Set(Act())
  //val set_Pln: Set[ProvType with ForwardType with BackwardType]=Set(Plan())
  val set_nil: Set[ProvType with ForwardType with BackwardType]=Set()
  val res_Res: Set[ProvType with ForwardType]=Set(Resource())
  
  def primitivesProvTypes(name: QualifiedName): Set[ProvType with ForwardType with BackwardType] = {
     name match {
      case `PROV_ENTITY`   => set_Ent
      case `PROV_AGENT`    => set_Ag
      case `PROV_ACTIVITY` => set_Act
      case _               => set_nil
     }
  }
  
  def primitiveTypes(types: Set[QualifiedName]) : Set[ProvType with ForwardType with BackwardType] = {
      types.flatMap(primitivesProvTypes)                          
  }

  def aggregatePrimitiveTypes(types: Set[ProvType with ForwardType with BackwardType]): Set[ProvType with ForwardType with BackwardType] = {
    val primitives: Set[Prim] =types.collect{case pt:Prim => pt}
    val others: Set[ProvType with ForwardType with BackwardType] =types -- primitives
    val thePrimitives: Set[String] = primitives.flatMap(_.s)
    if (thePrimitives.isEmpty) {
      others
    } else {
      val aprim: Prim = Prim(thePrimitives)
      others + aprim
    }
  }

  def primitiveTypes(s: Node, types: Set[QualifiedName]) : Set[ProvType with ForwardType with BackwardType] = {

    val found1: Set[ProvType with ForwardType with BackwardType] = if (primitivep) {
      val found: Set[ProvType with ForwardType with BackwardType] =s.typex.flatMap(t => level0.typeToProvType(t))
      primitiveTypes(types) ++ aggregatePrimitiveTypes(found)
    } else {
      primitiveTypes(types)
    }

    // now consider other optional properties
    val prop = level0.properties
    if (prop == null || prop.isEmpty) {
      found1
    } else {
      //found1 ++ s.other.filter{case (q:QualifiedName, others:Set[Other]) =>{val aURI=q.getUri(); prop.contains(aURI)}}.flatMap(p => p._2.flatMap(level0.typeToProvType) )
      if (primitivep) {
        val found2: Set[ProvType with ForwardType with BackwardType] =found1 ++ s.other.collect { case (q: QualifiedName, others: Set[Other]) if prop.contains(q.getUri()) => others }.flatMap(other => other.flatMap(t => level0.typeToProvType(t)))
        aggregatePrimitiveTypes(found2)
      } else {
        found1
      }
    }
  }


 
  def provtypeZero(ind: Indexing): Map[Int, Set[ProvType with ForwardType]]= {
      val nodes: Map[Int, Node] =ind.nodes
      val types: Map[Int, Set[QualifiedName]] =ind.types
      val prTypes: Map[Int, Set[ProvType with ForwardType]] =
        types.map{ case (i:Int,t:Set[QualifiedName])=>
          i -> { val atype=nodes.get(i) match {
            case Some(node) => primitiveTypes(node,types(i))
            case None => primitiveTypes(t) }
            atype.asInstanceOf[Set[ProvType with ForwardType]]    } }

    val resTypes: Set[(Int, Set[ProvType with ForwardType])]=ind.absentNodes.collect{case i:Int if ind.types.get(i).isEmpty => (i, res_Res)}
      
      prTypes ++ resTypes
  }



  
}



class ForwardPropagator(ind: Indexing,
                        common: CommonTypePropagator,
                        triangle:Boolean,
                        always_with_type_0:Boolean) extends Propagator[ForwardType] {

  
  def propagateTypesOverBinaryRelation (rel: Relation, 
                                        subjectTypes: Set[ProvType with ForwardType], 
                                        filter: Set[String]=Set()): Set[ProvType with ForwardType] = {
    if (subjectTypes.isEmpty) { 
      Set() 
      }
    else {
      val selectedTypes=if (filter.isEmpty) subjectTypes else subjectTypes.flatMap(atype => atype.prune[ProvType with ForwardType](filter))
      if (selectedTypes.isEmpty) {
        Set()
      }
      else {
    	  rel.enumType match {
      	  case Kind.wdf    => throw new IllegalArgumentException
      	  case Kind.wgb    => Set(Wgb(selectedTypes))
    	    case Kind.usd    => Set(Usd(selectedTypes))
      	  case Kind.wsb    => throw new IllegalArgumentException
    	    case Kind.web    => throw new IllegalArgumentException
    	    case Kind.wib    => Set(Winvb(selectedTypes))
    	    case Kind.waw    => throw new IllegalArgumentException
    	    case Kind.wat    => Set(Wat(selectedTypes))
    	    case Kind.aobo   => throw new IllegalArgumentException
    	    case Kind.winfob => Set(Winfob(selectedTypes))
    	    case Kind.winfl  => Set(Winflb(subjectTypes))
    	    case Kind.alt    => Set(Alt(selectedTypes))
    	    case Kind.spec   => Set(Spec(selectedTypes))
    	    case Kind.mem    => Set(Mem(selectedTypes))
    	    case Kind.men    => throw new IllegalArgumentException
    	  }
      }
    }
  }

  def propagateTypesOverBinaryRelationExcluding(rel: Relation,
                                                subjectTypes: Set[ProvType with ForwardType],
                                                src: Int,
                                                dst: Int,
                                                triangles: Seq[(Int,Int,Int)],
                                                allRelations: Map[Int, Map[Int, Iterable[Relation]]],
                                                filter: Set[String]=Set()): Set[ProvType with ForwardType] = {
    //println("entering propagateTypesOverBinaryRelationExcluding (fwd) ", rel, src, dst, subjectTypes)

    if (subjectTypes.isEmpty) {
      Set()
    }
    else {
      val selectedTypes=if (filter.isEmpty) subjectTypes else subjectTypes.flatMap(atype => atype.prune[ProvType with ForwardType](filter))
      if (selectedTypes.isEmpty) {
        Set()
      }
      else {
        rel.enumType match {
          case Kind.wdf    => throw new IllegalArgumentException
          case Kind.wgb    =>
            val succ: Map[Int, Iterable[Relation]]
            =allRelations
              .getOrElse(src,Map())
              .filterNot{case (i,_) => triangles.exists{case (ts,ti,td) => td==i && src==ti }}
              .filter{case(j,l) => l.exists(r => r.isInstanceOf[WasGeneratedBy])}// && j==dst}
            //println("(wgb)found succ " + src + " " + dst + " " + succ)
            if (succ.isEmpty) Set() else Set(Wgb(selectedTypes))
          case Kind.usd    => {
            val succ: Map[Int, Iterable[Relation]]
            =allRelations
              .getOrElse(src,Map())
              .filterNot{case (i,_) => triangles.exists{case (ts,ti,td) => ti==i} }
              .filter{case(j,l) => l.exists(r => r.isInstanceOf[Used]) && j==dst}
            //println("(usd)found succ " + src + " " + dst + " " + succ)
            if (succ.isEmpty) Set() else Set(Usd(selectedTypes))
          }
          case Kind.wsb    => throw new IllegalArgumentException
          case Kind.web    => throw new IllegalArgumentException
          case Kind.wib    => Set(Winvb(selectedTypes))
          case Kind.waw    => throw new IllegalArgumentException
          case Kind.wat    => Set(Wat(selectedTypes))
          case Kind.aobo   => throw new IllegalArgumentException
          case Kind.winfob => Set(Winfob(selectedTypes))
          case Kind.winfl  => Set(Winflb(subjectTypes))
          case Kind.alt    => Set(Alt(selectedTypes))
          case Kind.spec   => Set(Spec(selectedTypes))
          case Kind.mem    => Set(Mem(selectedTypes))
          case Kind.men    => throw new IllegalArgumentException
        }
      }
    }
  }



  def propagateTypesOverTernaryRelation (rel: Relation, 
		                                     subjectTypes: Set[ProvType with ForwardType], 
		                                     subjectTypes2: Set[ProvType with ForwardType],
                                         src: Int,
                                         dst: Int,
                                         triangles: Seq[(Int,Int,Int)],
		                                     filter: Set[String]=Set()): Set[ProvType with ForwardType] = {
    if (subjectTypes.isEmpty) {
      Set() 
    } else {
    	val selectedTypes  = if (filter.isEmpty) subjectTypes  else  subjectTypes.flatMap (atype => atype.prune[ProvType with ForwardType](filter))
    	val selectedTypes2 = if (filter.isEmpty) subjectTypes2 else  subjectTypes2.flatMap(atype => atype.prune[ProvType with ForwardType](filter))

    	if (selectedTypes.isEmpty && selectedTypes2.isEmpty) {
        Set()
      } else {
        rel.enumType match {
          case Kind.wdf    => if (triangles.exists{case (s,i,d) => s==src && d==dst})
            Set(Wdf_triangle(selectedTypes,selectedTypes2))
            else
            Set(Wdf(selectedTypes,selectedTypes2))

          case Kind.wgb    => throw new IllegalArgumentException
          case Kind.usd    => throw new IllegalArgumentException
          case Kind.wsb    => Set(Wsb(selectedTypes,selectedTypes2)) 
          case Kind.web    => Set(Web(selectedTypes,selectedTypes2)) 
          case Kind.wib    => throw new IllegalArgumentException
          case Kind.waw    => Set(Waw(selectedTypes,selectedTypes2))
          case Kind.wat    => throw new IllegalArgumentException
          case Kind.aobo   => Set(Aobo(selectedTypes,selectedTypes2)) 
          case Kind.winfob => throw new IllegalArgumentException
          case Kind.winfl  => throw new IllegalArgumentException
          case Kind.alt    => throw new IllegalArgumentException
          case Kind.spec   => throw new IllegalArgumentException
          case Kind.mem    => throw new IllegalArgumentException
          case Kind.men    => Set(Men(selectedTypes))  //TODO: add second arg
        }
      }
    }
  }


  def propagateType_triangle(allRelations: Map[Int,Map[Int,Iterable[Relation]]],
                             typen: Map[Int,Set[ProvType with ForwardType]],
                             typenm1: Map[Int,Set[ProvType with ForwardType]],
                             ind: Indexing, filter: Set[String]=Set()): Map[Int,Set[ProvType with ForwardType]] = {
    val empty=Set[ProvType with ForwardType]()
    //println(allRelations)

    val triangles: Seq[(Int, Int, Int)] = computeTriangles(allRelations)
    //println(triangles)

    allRelations.toSeq.flatMap{ case (src,m) =>
    m.map { case (dst,rels) =>
        (dst,rels.flatMap( rel => {
          if (TypePropagator.binaryRelation(rel)) {
            propagateTypesOverBinaryRelationExcluding(rel, typen.getOrElse(src,empty), src, dst, triangles, allRelations): Set[ProvType with ForwardType]
          } else {
            val elem=TypePropagator.ternaryElement(rel)
            val aset: Set[ProvType with ForwardType]=elem match {
              case None => empty
              case Some(qn) => typen.getOrElse(ind.amap(qn),empty)
            }
            propagateTypesOverTernaryRelation(rel, typen.getOrElse(src,empty), aset, src, dst, triangles): Set[ProvType with ForwardType]
          } } ).toSet ) } }
      .filter { case ( dst:Int , set: Set[ProvType with ForwardType] ) => set.nonEmpty }
      // at that point, a Seq[Int,Set[ProvType]]
      .groupBy(_._1)
      // at that point, a Map[Int,Seq[Int,Set[ProvType]]]
      .view.mapValues(x=>x.flatMap(_._2).toSet)
  }.toMap


  def computeTriangles(allRelations: Map[Int, Map[Int, Iterable[Relation]]]): Seq[(Int, Int, Int)] = {
    allRelations.toSeq.flatMap { case (src, m) =>
      m.flatMap { case (dst, rels) =>
        rels.flatMap(rel => {
          allRelations.toSeq.flatMap { case (src, m) => TypePropagator.triangularRelation(rel, src, dst, allRelations).getOrElse(Seq()).map(i => (src, i, dst)) }
        })
      }
    }
  }

  def propagateType(rel: Map[Int,Map[Int,Iterable[Relation]]],
                    typen: Map[Int,Set[ProvType with ForwardType]],
                    ind: Indexing,
                    filter: Set[String]=Set()): Map[Int,Set[ProvType with ForwardType]] = {
    val empty=Set[ProvType with ForwardType]()
    val allpairs: Seq[(Int, Set[ProvType with ForwardType])] =
        rel.toSeq.flatMap{ case (src:Int,m:Map[Int,Iterable[Relation]]) =>
                        m.map { case (dst:Int,rels:Iterable[Relation]) =>
                                  (dst,rels.flatMap( rel => 
                                                       if (TypePropagator.binaryRelation(rel)) {
                                                         propagateTypesOverBinaryRelation(rel, 
                                                        		                              typen.getOrElse(src,empty),
                                                        		                              filter)
                                                       } else {
                                                         val elem=TypePropagator.ternaryElement(rel)
                                                         val aset: Set[ProvType with ForwardType]=elem match {
                                                           case None => empty
                                                           case Some(qn) => typen.getOrElse(ind.amap(qn),empty)
                                                         }
                                                         propagateTypesOverTernaryRelation(rel, 
                                                                                           typen.getOrElse(src,empty), 
                                                                                           aset,
                                                           -1,
                                                           -1,
                                                           Seq(),
                                                                                           filter)
                                                       } ).toSet) } }
                         .filter { case ( dst:Int , set: Set[ProvType with ForwardType] ) => set.nonEmpty }
       // at that point, a Seq[Int,Set[ProvType]]
                          
     val res= scala.collection.mutable.Map[Int,Set[ProvType with ForwardType]]()
     allpairs.foreach{case (i,set) => res.get(i) match {
                                        case None       => res.put(i,set)
                                        case Some(set2) => res.put(i,set++set2)}
                                      }
     res.toMap
  }

  var aggregateProvTypesForward:  Map[Int,Map[Int,Set[ProvType with ForwardType]]]=Map()


  def merge[ALPHA](type_0: Map[Int, Set[ALPHA]],
                   type_n: Map[Int, Set[ALPHA]]): Map[Int, Set[ALPHA]] = {
    val keys0=type_0.keySet
    val keysn=type_n.keySet
    val keys=keys0 union keysn
    keys.map(k => (k, type_0.getOrElse(k,Set()) union type_n.getOrElse(k,Set()))).toMap
  }

  def getProvType(n: Int): Map[Int,Set[ProvType with ForwardType]] = {
    require (n >= 0)
    aggregateProvTypesForward.get(n) match {
      case Some(m) => m
      case None => 
        if (n==0) {
          val type0=common.provtypeZero(ind)
          aggregateProvTypesForward += (0 -> type0)
          type0
        } else {
          val type_n_1: Map[Int, Set[ProvType with ForwardType]] =getProvType(n-1)
          val type_n: Map[Int, Set[ProvType with ForwardType]] =
                    if (triangle)
                       propagateType_triangle(ind.pred,type_n_1,Map(), ind)
                    else
                       propagateType(ind.pred,type_n_1,ind)
          val type_0=getProvType(0)
          val true_type_n=if (!triangle && always_with_type_0) merge(type_0,type_n) else type_n
          aggregateProvTypesForward += (n -> true_type_n)
          true_type_n
        }
    }
  }    
  
  // propagate types at level base (from base-1) assuming they belong to the set of filters
  def getProvTypeWithFilter(n: Int, filter: Set[String]): Map[Int,Set[ProvType with ForwardType]] = {
	  require (n > 0)
    println("now calculating with n " + n)
    aggregateProvTypesForward.get(n) match {
      case Some(m) => m
      case None =>
        val typen=getProvType(n-1)
        val typeBase=propagateType(ind.pred,typen,ind,filter)
        val type_0=getProvType(0)
        val true_typeBase=if (always_with_type_0) merge(type_0,typeBase) else typeBase
        aggregateProvTypesForward += (n -> true_typeBase)
        true_typeBase
    }
  }  
  
  lazy val type0:Map[Int,Set[ProvType with ForwardType]]=getProvType(0) //provtypeZero(ind)

  
}

// means propagation of types from most recent to oldest nodes, meaning that we propagate inverse labels
class BackwardPropagator(ind: Indexing, common: CommonTypePropagator, fwd: ForwardPropagator, triangle:Boolean) extends Propagator[BackwardType] {
  
  def propagateTypesOverBinaryRelation (rel: Relation, 
                                        subjectTypes: Set[ProvType with BackwardType], 
                                        filter: Set[String]=Set()): Set[ProvType with BackwardType] = {
     if (subjectTypes.isEmpty) { 
      Set() 
      }
     else {
      val selectedTypes=if (filter.isEmpty) subjectTypes else  subjectTypes.flatMap(atype => atype.prune[ProvType with BackwardType](filter))
      if (selectedTypes.isEmpty) {
        Set()
      }
      else {
        rel.enumType match {
          case Kind.wdf    => throw new IllegalArgumentException
          case Kind.wgb    => Set(Wgb_1(selectedTypes))
          case Kind.usd    => Set(Usd_1(selectedTypes))
          case Kind.wsb    => throw new IllegalArgumentException
          case Kind.web    => throw new IllegalArgumentException
          case Kind.wib    => Set(Winvb_1(selectedTypes))
          case Kind.waw    => throw new IllegalArgumentException
          case Kind.wat    => Set(Wat_1(selectedTypes))
          case Kind.aobo   => throw new IllegalArgumentException
          case Kind.winfob => Set(Winfob_1(selectedTypes))
          case Kind.winfl  => Set(Winflb_1(selectedTypes))
          case Kind.alt    => Set(Alt_1(selectedTypes))
          case Kind.spec   => Set(Spec_1(selectedTypes))
          case Kind.mem    => Set(Mem_1(selectedTypes))
          case Kind.men    => throw new IllegalArgumentException
        }
      }
    }
  }

  def propagateTypesOverBinaryRelationExcluding(rel: Relation,
                                                subjectTypes: Set[ProvType with BackwardType],
                                                src: Int,
                                                dst: Int,
                                                triangles: Seq[(Int,Int,Int)],
                                                allRelations: Map[Int, Map[Int, Iterable[Relation]]],
                                                filter: Set[String]=Set()): Set[ProvType with BackwardType] = {
   //    println("entering propagateTypesOverBinaryRelationExcluding ", rel, src, dst, subjectTypes)

    if (subjectTypes.isEmpty) {
      Set()
    }
    else {
      val selectedTypes: Set[ProvType with BackwardType] =if (filter.isEmpty) subjectTypes else subjectTypes.flatMap(atype => atype.prune[ProvType with BackwardType](filter))
      if (selectedTypes.isEmpty) {
        Set()
      }
      else {
        rel.enumType match {
          case Kind.wdf    => throw new IllegalArgumentException
          case Kind.wgb    =>
            val succ: Map[Int, Iterable[Relation]]
            = allRelations
              .getOrElse(src, Map())
              .filterNot { case (i, _) => triangles.exists { case (ts, ti, td) => dst == ti } }
              .filter { case (j, l) => l.exists(r => r.isInstanceOf[WasGeneratedBy]) } // && j==dst}
           // println("(wgb_)found succ " + src + " " + dst + " " + succ)
            if (succ.isEmpty) Set() else Set(Wgb_1(selectedTypes))
          case Kind.usd    =>
            val succ: Map[Int, Iterable[Relation]]
            =allRelations
              .getOrElse(src,Map())
              .filterNot{case (i,_) => triangles.exists{case (ts,ti,td) => dst==td} }
              .filter{case(j,l) => l.exists(r => r.isInstanceOf[Used]) } //&& j==dst
            //("(usd_)found succ " + src + " " + dst + " " + succ)
            if (succ.isEmpty) Set() else Set(Usd_1(selectedTypes))

          case Kind.wsb    => throw new IllegalArgumentException
          case Kind.web    => throw new IllegalArgumentException
          case Kind.wib    => Set(Winvb_1(selectedTypes))
          case Kind.waw    => throw new IllegalArgumentException
          case Kind.wat    => Set(Wat_1(selectedTypes))
          case Kind.aobo   => throw new IllegalArgumentException
          case Kind.winfob => Set(Winfob_1(selectedTypes))
          case Kind.winfl  => Set(Winflb_1(selectedTypes))
          case Kind.alt    => Set(Alt_1(selectedTypes))
          case Kind.spec   => Set(Spec_1(selectedTypes))
          case Kind.mem    => Set(Mem_1(selectedTypes))
          case Kind.men    => throw new IllegalArgumentException
        }
      }
    }
  }



  def propagateTypesOverTernaryRelation (rel: Relation,
                                          subjectTypes: Set[ProvType with BackwardType], 
                                          subjectTypes2: Set[ProvType with BackwardType],
                                          src: Int,
                                          dst: Int,
                                          triangles: Seq[(Int,Int,Int)],
                                          filter: Set[String]=Set()): Set[ProvType with BackwardType] = {
     if (subjectTypes.isEmpty) {
      Set() 
     } else {
    	val selectedTypes  = if (filter.isEmpty) subjectTypes  else subjectTypes.flatMap (atype => atype.prune[ProvType with BackwardType](filter))
    	val selectedTypes2 = if (filter.isEmpty) subjectTypes2 else subjectTypes2.flatMap(atype => atype.prune[ProvType with BackwardType](filter))

    	if (selectedTypes.isEmpty && selectedTypes2.isEmpty) {
        Set()
      } else {
        rel.enumType match {
          case Kind.wdf    => //println((src,dst,triangles))
            if (triangles.exists{case (s,i,d) => s==src && d==dst})
            Set(Wdf_triangle_1(selectedTypes,selectedTypes2))
            else {
              Set(Wdf_1(selectedTypes, selectedTypes2))
            }

          case Kind.wgb    => throw new IllegalArgumentException
          case Kind.usd    => throw new IllegalArgumentException
          case Kind.wsb    => Set(Wsb_1(selectedTypes,selectedTypes2)) 
          case Kind.web    => Set(Web_1(selectedTypes,selectedTypes2)) 
          case Kind.wib    => throw new IllegalArgumentException
          case Kind.waw    => Set(Waw_1(selectedTypes,selectedTypes2))
          case Kind.wat    => throw new IllegalArgumentException
          case Kind.aobo   => Set(Aobo_1(selectedTypes,selectedTypes2)) 
          case Kind.winfob => throw new IllegalArgumentException
          case Kind.winfl  => throw new IllegalArgumentException
          case Kind.alt    => throw new IllegalArgumentException
          case Kind.spec   => throw new IllegalArgumentException
          case Kind.mem    => throw new IllegalArgumentException
          case Kind.men    => Set(Men_1(selectedTypes))  //TODO: add second arg
        }
      }
    }
     
     
  }


  def propagateType_triangle(allRelations: Map[Int,Map[Int,Iterable[Relation]]],
                             typen: Map[Int,Set[ProvType with BackwardType]],
                             typenm1: Map[Int,Set[ProvType with BackwardType]],
                             ind: Indexing, filter: Set[String]=Set()): Map[Int,Set[ProvType with BackwardType]] = {
    val empty=Set[ProvType with BackwardType]()
   // println(allRelations)

    val triangles: Seq[(Int, Int, Int)] =computeBackwardTriangles(allRelations)
    //println(triangles)

    allRelations.toSeq.flatMap{ case (src,m) =>
      m.map { case (dst,rels) =>
        (dst,rels.flatMap( rel => {
          if (TypePropagator.binaryRelation(rel)) {
            propagateTypesOverBinaryRelationExcluding(rel, typen.getOrElse(src,empty), src, dst, triangles, allRelations): Set[ProvType with BackwardType]
          } else {
            val elem=TypePropagator.ternaryElement(rel)
            val aset: Set[ProvType with BackwardType]=elem match {
              case None => empty
              case Some(qn) => typen.getOrElse(ind.amap(qn),empty)
            }
            propagateTypesOverTernaryRelation(rel, typen.getOrElse(src,empty), aset, src, dst, triangles): Set[ProvType with BackwardType]
          } } ).toSet ) } }
      .filter { case ( dst:Int , set: Set[ProvType with BackwardType] ) => set.nonEmpty }
      // at that point, a Seq[Int,Set[ProvType]]
      .groupBy(_._1)
      // at that point, a Map[Int,Seq[Int,Set[ProvType]]]
      .view.mapValues(x=>x.flatMap(_._2).toSet).toMap
  }

  def computeBackwardTriangles(allRelations: Map[Int, Map[Int, Iterable[Relation]]]): Seq[(Int, Int, Int)] = {
    allRelations.toSeq.flatMap { case (src, m) =>
      m.flatMap { case (dst, rels) =>
        rels.flatMap(rel => {
          allRelations.toSeq.flatMap { case (src, m) => TypePropagator.triangularBackwardRelation(rel, src, dst, allRelations).getOrElse(Seq()).map(i => (src, i, dst)) }
        })
      }
    }
  }

  def propagateType(rel: Map[Int,Map[Int,Iterable[Relation]]], typen: Map[Int,Set[ProvType with BackwardType]], ind: Indexing, filter: Set[String]=Set()): Map[Int,Set[ProvType with BackwardType]] = {
    val empty=Set[ProvType with BackwardType]()
    rel.toSeq.flatMap{ case (src,m) => 
                        m.map { case (dst,rels) =>  
                                  (dst,rels.flatMap( rel => 
                                                       if (TypePropagator.binaryRelation(rel)) {
                                                         propagateTypesOverBinaryRelation(rel, typen.getOrElse(src,empty))
                                                       } else {
                                                         val elem=TypePropagator.ternaryElement(rel)
                                                         val aset: Set[ProvType with BackwardType]=elem match {
                                                           case None => empty
                                                           case Some(qn) => typen.getOrElse(ind.amap(qn),empty)
                                                         }
                                                         propagateTypesOverTernaryRelation(rel, typen.getOrElse(src,empty), aset, src, dst, Seq())
                                                       } ).toSet ) } }
                         .filter { case ( dst:Int , set: Set[ProvType with BackwardType] ) => set.nonEmpty }
       // at that point, a Seq[Int,Set[ProvType]]
     .groupBy(_._1)
      // at that point, a Map[Int,Seq[Int,Set[ProvType]]]
     .view.mapValues(x=>x.flatMap(_._2).toSet)
  }.toMap


  def propagateType(typen: Map[Int,Set[ProvType with BackwardType ]], ind: Indexing): Map[Int,Set[ProvType with BackwardType]] = {
    val succ=ind.succ
    propagateType(succ,typen,ind)
  }
  
  

  var aggregateProvTypesBackward: Map[Int,Map[Int,Set[ProvType with BackwardType]]]=Map()

  lazy val type0_ : Map[Int, Set[ProvType with BackwardType]] = fwd.type0.asInstanceOf[Map[Int,Set[ProvType with BackwardType]]]
    
  def getProvType(n: Int): Map[Int,Set[ProvType with BackwardType]] = {
    require (n <= -1)
    aggregateProvTypesBackward.get(n) match {
      case Some(m) => m
      case None => 
        if (n == -1) {
          val type_back1=if (triangle) propagateType_triangle(ind.succ,type0_,Map(),ind) else propagateType(type0_,ind)
          aggregateProvTypesBackward += (-1 -> type_back1)
          type_back1
        } else {
          val typen_1=getProvType(n+1)
          //val typen=propagateType(typen_1,ind)

          val typen=if (triangle) propagateType_triangle(ind.succ,typen_1,Map(), ind) else propagateType(ind.succ,typen_1,ind)

          aggregateProvTypesBackward += (n -> typen)
          typen
        }
    }
  }
}

class TypePropagator(ind: Indexing, level0: Level0, primitivep: Boolean, triangle: Boolean, always_with_type_0:Boolean) {
   
	def this(doc:Document, triangle: Boolean, always_with_type_0:Boolean, weakInference:Boolean, primitivep:Boolean=true, level0: Level0 = new DefaultLevel0) = {
		this(new Indexer(doc,Vector(),weakInference),level0,primitivep,triangle, always_with_type_0)  // TODO: weakinf
	}
   
  val common=new CommonTypePropagator(ind,level0,primitivep)
 
  val fwd=new ForwardPropagator(ind,common, triangle,always_with_type_0)
  val bak=new BackwardPropagator(ind,common,fwd, triangle)
   
  // Convenience accessors of some provenance types
  
  lazy val type0:Map[Int,Set[ProvType with ForwardType]]=fwd.type0 //provtypeZero(ind)
  //println("type0: " + type0)
  
  lazy val type0_ : Map[Int, Set[ProvType with BackwardType]] = bak.type0_

  lazy val type1:Map[Int,Set[ProvType with ForwardType]]=fwd.getProvType(1) //propagateTypeForward(type0,ind)
  //println("type1: " + type1)

  lazy val type2:Map[Int,Set[ProvType with ForwardType]]=fwd.getProvType(2) //propagateTypeForward(type1,ind)
  //println("type2: " + type2)

  lazy val type_back1:Map[Int,Set[ProvType with BackwardType]]=bak.getProvType(-1) //propagateTypeBackward(type0,ind)
  //println("type back1: " + type1)

  
   /* Aggregate all provenance types up to level. */
  def aggregateProvTypesToLevel(level:Int, aggregatep: Boolean=true, aggregate0p:Boolean=false): ProvTypes = {
    //require (level>=0)
    if (level>=0) {
      if (aggregatep) {
        val r = 0 until level + 1
        val nums = r.toSet
        aggregateProvTypesToLevel(nums)
      } else {
        if (aggregate0p) {
          aggregateProvTypesToLevel(Set(0, level)) // LUC, TODO: I included level 0, make this optional?
        } else {
          aggregateProvTypesToLevel(Set(level))
        }
      }
    } else {
      val r=level until 1 //LUC, to check 0 or 1?
    	val nums=r.toSet
    	aggregateProvTypesToLevel(nums) 
    }
   }

  /*
  def aggregateProvTypesToLevelOLD(nums:Set[Int]): ProvTypes = {    
    nums
    .flatMap(num => if (num>=0) {
                      fwd.getProvType(num).asInstanceOf[Map[Int,Set[ProvType]]]
                    } else {
                      bak.getProvType(num).asInstanceOf[Map[Int,Set[ProvType]]]
                    })
    .groupBy(_._1)  //EXPENSIVE, can be avoided
    .mapValues(set1 => set1.flatMap(_._2))                                                                                
  }
     */
  
  def aggregateProvTypesToLevel(nums:Set[Int]): ProvTypes = {
    val nums0=nums--Set(0)
    val includes0=nums.contains(0)
    val numbers=  nums0.toVector
    val types0: Map[Int, Set[ProvType]] =fwd.getProvType(0).asInstanceOf[Map[Int,Set[ProvType]]]
    val allOtherProvTypes: immutable.Seq[Map[Int, Set[ProvType]]] =numbers.map(num => if (num>=0) {
                                          fwd.getProvType(num).asInstanceOf[Map[Int,Set[ProvType]]]
                                        } else {
                                        	bak.getProvType(num).asInstanceOf[Map[Int,Set[ProvType]]]
                                        })
    val allProvTypes: ProvTypes= if (includes0) {
      types0.map{case (i,typ)
                    => (i, typ ++ allOtherProvTypes.flatMap(types_x => types_x.get(i) match {
                                                                    case None      => Set[ProvType]()
                                                                    case Some(set) => set}))
      }
      } else {
      types0.map{case (i,_)
                    => (i, allOtherProvTypes.flatMap(types_x => types_x.get(i) match {
                                                                    case None      => Set[ProvType]()
                                                                    case Some(set) => set}).toSet)
      }
    }
    allProvTypes
  }
    
   /* Aggregate all provenance types up to level. */
  def aggregateProvTypesToLevelAndFilter(level:Int, maxlevel:Int, filter: Set[String]):  ProvTypes = {
    //require (level>=0)
    if (level>=0) {
    	val r=0 until level+1
    	val nums=r.toSet
    	val nums2=level+1 until maxlevel+1
      aggregateProvTypesToLevel(nums,nums2,filter)
    } else {
      val r=level until 1 //LUC, to check 0 or 1?
    	val nums=r.toSet
    	aggregateProvTypesToLevel(nums) 
    }
   }
  def aggregateProvTypesToLevel(nums:Set[Int], nums2: Seq[Int], filter: Set[String]): ProvTypes = {    
    val set1=nums.flatMap(num => if (num>=0) {
                      fwd.getProvType(num).asInstanceOf[Map[Int,Set[ProvType]]]
                    } else {
                      bak.getProvType(num).asInstanceOf[Map[Int,Set[ProvType]]]
                    })
    val set2=nums2.flatMap(num => if (num>=0) {
                      fwd.getProvTypeWithFilter(num,filter).asInstanceOf[Map[Int,Set[ProvType]]]
                    } else {
                      Map[Int,Set[ProvType]]()
                    })
    println("WITH FILTER")
        
    (set1++set2)
    .groupBy(_._1)
    .view.mapValues(set1 => set1.flatMap(_._2))
  }.toMap
  
  type ProvTypes=Map[Int,Set[ProvType]]
 
  def getConstructorForLevel (level: Int, nsBase:String, prettyMethod:PrettyMethod.Pretty, aggregatep: Boolean,aggregate0p: Boolean, withLevel0Description: Boolean): SummaryConstructor = {
    val agg_provtypes_0_level: ProvTypes =this.aggregateProvTypesToLevel(level,aggregatep,aggregate0p)
    val index1=new SummaryConstructor(ind, agg_provtypes_0_level, nsBase, prettyMethod, withLevel0Description)
    index1
  }
  
  def getConstructorForLevel (level: Int, filter: Set[String], maxlevel: Int,  nsBase:String, prettyMethod:PrettyMethod.Pretty, withLevel0Description: Boolean): SummaryConstructor = {
    val agg_provtypes_0_level:  ProvTypes =this.aggregateProvTypesToLevelAndFilter(level,maxlevel,filter)
    val index1=new SummaryConstructor(ind, agg_provtypes_0_level, nsBase, prettyMethod, withLevel0Description)
    index1
  }
  
}

