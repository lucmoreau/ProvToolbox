package org.openprovenance.prov.scala.immutable

import org.openprovenance.prov.model.NamespacePrefixMapper

object Indexer {


	def swap[Alpha,Beta](m: Map[Alpha,Beta]): Map[Beta, Alpha] = {
		Map() ++ m.map(_.swap)
	}

	val PROV_ENTITY=new QualifiedName(NamespacePrefixMapper.PROV_PREFIX,
                                    "Entity",
                                    NamespacePrefixMapper.PROV_NS)
  val PROV_ACTIVITY=new QualifiedName(NamespacePrefixMapper.PROV_PREFIX,
                                    "Activity",
                                    NamespacePrefixMapper.PROV_NS)
  val PROV_AGENT=new QualifiedName(NamespacePrefixMapper.PROV_PREFIX,
                                    "Agent",
                                    NamespacePrefixMapper.PROV_NS)  
  val PROV_PLAN=new QualifiedName(NamespacePrefixMapper.PROV_PREFIX,
                                  "Plan",
                                  NamespacePrefixMapper.PROV_NS)  
  
  val PROV_BUNDLE=new QualifiedName(NamespacePrefixMapper.PROV_PREFIX,
                                  "Bundle",
                                  NamespacePrefixMapper.PROV_NS)  
  
   def identity (s: Statement):Seq[QualifiedName]  = nullable(s.id) 
       
	 @inline def nullable(id:QualifiedName):Seq[QualifiedName] = {
          if (id==null) Seq() else Seq(id) 
   }
 	 @inline def nullable(set:Set[QualifiedName], id:QualifiedName):Set[QualifiedName] = {
          if (id==null) set else set + id 
   }
       
  
   def ids (s: StatementOrBundle):Iterable[QualifiedName]  = { //TODO: move away froms sets at this stage
            s match {
              case b:Bundle              => nullable(s.id) ++ b.statement.flatMap(ids) //identity or ids?
              case e:Entity              => nullable(s.id)
              case a:Activity            => nullable(s.id)
              case ag:Agent              => nullable(s.id)
              case wdf:WasDerivedFrom    => fourNullable(s.id,wdf.generatedEntity,wdf.usedEntity,wdf.activity) 
              case wat:WasAttributedTo   => threeNullable(s.id,wat.entity,wat.agent) 
              case waw:WasAssociatedWith => fourNullable(s.id,waw.activity,waw.agent,waw.plan) 
              case aobo:ActedOnBehalfOf  => fourNullable(s.id,aobo.delegate,aobo.responsible,aobo.activity) 
              case wgb:WasGeneratedBy    => threeNullable(s.id,wgb.entity,wgb.activity)  
              case wib:WasInvalidatedBy  => threeNullable(s.id,wib.entity,wib.activity) 
              case wsb:WasStartedBy      => fourNullable(s.id,wsb.activity,wsb.trigger,wsb.starter) 
              case web:WasEndedBy        => fourNullable(s.id,web.activity,web.trigger,web.ender) 
              case usd:Used              => threeNullable(s.id,usd.entity,usd.activity)
              //case wib:WasInfluencedBy   => ids ++ nullable(wib.influencer)      ++ nullable(wib.influencee) 
              case mem:HadMember         => threeNullable(s.id,mem.collection,mem.entity.head) //TODO support multiple entities
              case spe:SpecializationOf  => threeNullable(s.id,spe.specificEntity,spe.generalEntity)
              case alt:AlternateOf       => threeNullable(s.id,alt.alternate2,alt.alternate1)
              case winfb:WasInformedBy   => threeNullable(s.id,winfb.informant,winfb.informed)  
              case men:MentionOf         => fourNullable(s.id,men.specificEntity,men.generalEntity,men.bundle)
              case winfl:WasInfluencedBy => threeNullable(s.id,winfl.influencee,winfl.influencer)  
   
            }
  }

  def addIds (set: Set[QualifiedName], s: StatementOrBundle):Set[QualifiedName]  = { // cautiously use the set-cons operator
            s match {
              case b:Bundle              => nullable(set,s.id) ++ b.statement.flatMap(ids) //identity or ids?
              case e:Entity              => nullable(set,s.id)
              case a:Activity            => nullable(set,s.id)
              case ag:Agent              => nullable(set,s.id)
              case wdf:WasDerivedFrom    => fourNullable(set,s.id,wdf.generatedEntity,wdf.usedEntity,wdf.activity) 
              case wat:WasAttributedTo   => threeNullable(set,s.id,wat.entity,wat.agent) 
              case waw:WasAssociatedWith => fourNullable(set,s.id,waw.activity,waw.agent,waw.plan) 
              case aobo:ActedOnBehalfOf  => fourNullable(set,s.id,aobo.delegate,aobo.responsible,aobo.activity) 
              case wgb:WasGeneratedBy    => threeNullable(set,s.id,wgb.entity,wgb.activity)  
              case wib:WasInvalidatedBy  => threeNullable(set,s.id,wib.entity,wib.activity) 
              case wsb:WasStartedBy      => fourNullable(set,s.id,wsb.activity,wsb.trigger,wsb.starter) 
              case web:WasEndedBy        => fourNullable(set,s.id,web.activity,web.trigger,web.ender) 
              case usd:Used              => threeNullable(set,s.id,usd.entity,usd.activity)
              case mem:HadMember         => threeNullable(set,s.id,mem.collection,mem.entity.head) //TODO support multiple entities
              case spe:SpecializationOf  => threeNullable(set,s.id,spe.specificEntity,spe.generalEntity)
              case alt:AlternateOf       => threeNullable(set,s.id,alt.alternate2,alt.alternate1)
              case winfb:WasInformedBy   => threeNullable(set,s.id,winfb.informant,winfb.informed)  
              case men:MentionOf         => fourNullable(set,s.id,men.specificEntity,men.generalEntity,men.bundle)
              case winfl:WasInfluencedBy => threeNullable(set,s.id,winfl.influencee,winfl.influencer)
							case ext:ExtensionStatement => ext.addIds(set)
            }
  }

  private final def threeNullable(q1: QualifiedName, q2: QualifiedName, q3: QualifiedName): Seq[QualifiedName] = {
		  if (q1==null) {
			  if (q2==null) {
				  if (q3==null) {
					  Seq()
				  } else {
					  Seq(q3)
				  }
			  } else {
				  if (q3==null) {
					  Seq(q2)
				  } else {
					  Seq(q2,q3)
				  }
			  }
		  } else {
			  if (q2==null) {
				  if (q3==null) {
					  Seq(q1)
				  } else {
					  Seq(q1,q3)
				  }
			  } else {
				  if (q3==null) {
					  Seq(q1,q2)
				  } else {
					  Seq(q1, q2,q3)
				  }
			  }
		  }
  }
  
  private final def threeNullable(set: Set[QualifiedName], q1: QualifiedName, q2: QualifiedName, q3: QualifiedName): Set[QualifiedName] = {
		  if (q1==null) {
			  if (q2==null) {
				  if (q3==null) {
					  set
				  } else {
					  set + q3
				  }
			  } else {
				  if (q3==null) {
					  set + q2
				  } else {
					  set + q2 + q3
				  }
			  }
		  } else {
			  if (q2==null) {
				  if (q3==null) {
					  set + q1
				  } else {
					  set + q1 + q3
				  }
			  } else {
				  if (q3==null) {
					  set + q1 + q2
				  } else {
					  set + q1+ q2 + q3
				  }
			  }
		  }
  }
  
  private final def fourNullable(q1: QualifiedName, q2: QualifiedName, q3: QualifiedName, q4: QualifiedName): Seq[QualifiedName] = {
		  if (q4==null) {
			  if (q1==null) {
				  if (q2==null) {
					  if (q3==null) {
						  Seq()
					  } else {
						  Seq(q3)
					  }
				  } else {
					  if (q3==null) {
						  Seq(q2)
					  } else {
						  Seq(q2,q3)
					  }
				  }
			  } else {
				  if (q2==null) {
					  if (q3==null) {
						  Seq(q1)
					  } else {
						  Seq(q1,q3)
					  }
				  } else {
					  if (q3==null) {
						  Seq(q1,q2)
					  } else {
						  Seq(q1, q2,q3)
					  }
				  }
			  }
		  } else {
			  if (q1==null) {
				  if (q2==null) {
					  if (q3==null) {
						  Seq(q4)
					  } else {
						  Seq(q3,q4)
					  }
				  } else {
					  if (q3==null) {
						  Seq(q2,q4)
					  } else {
						  Seq(q2,q3,q4)
					  }
				  }
			  } else {
				  if (q2==null) {
					  if (q3==null) {
						  Seq(q1,q4)
					  } else {
						  Seq(q1,q3,q4)
					  }
				  } else {
					  if (q3==null) {
						  Seq(q1,q2,q4)
					  } else {
						  Seq(q1, q2,q3,q4)
					  }
				  }
			  }
		  }
  }
  
  final def fourNullable(set: Set[QualifiedName], q1: QualifiedName, q2: QualifiedName, q3: QualifiedName, q4: QualifiedName): Set[QualifiedName] = {
		  if (q4==null) {
			  if (q1==null) {
				  if (q2==null) {
					  if (q3==null) {
						  set
					  } else {
						  set + q3
					  }
				  } else {
					  if (q3==null) {
						  set + q2
					  } else {
						  set + q2 + q3
					  }
				  }
			  } else {
				  if (q2==null) {
					  if (q3==null) {
						  set + q1
					  } else {
						  set + q1 + q3
					  }
				  } else {
					  if (q3==null) {
						  set + q1 + q2
					  } else {
						  set + q1 + q2 + q3
					  }
				  }
			  }
		  } else {
			  if (q1==null) {
				  if (q2==null) {
					  if (q3==null) {
						  set + q4
					  } else {
						  set + q3 + q4
					  }
				  } else {
					  if (q3==null) {
						  set + q2 + q4
					  } else {
						  set + q2 + q3 + q4
					  }
				  }
			  } else {
				  if (q2==null) {
					  if (q3==null) {
						  set + q1 + q4
					  } else {
						  set + q1 + q3 + q4
					  }
				  } else {
					  if (q3==null) {
						  set + q1 + q2 + q4
					  } else {
						  set + q1 + q2 + q3 + q4
					  }
				  }
			  }
		  }
  }
  
  
}


case class SizeRange (edgeMin:Int, edgeMax:Int, nodeMin:Int, nodeMax:Int) 

trait Indexing {
     val amap: Map[QualifiedName,Int]
     val succ: Map[Int,Map[Int,Iterable[Relation]]]
     val pred: Map[Int,Map[Int,Iterable[Relation]]]     
     val nodes: Map[Int,Node]
     val types: Map[Int,Set[QualifiedName]]
     def document (): Document 
     val absentNodes: Set[Int]
     
     val idsVec: Iterable[QualifiedName]
     val idsFun: Map[Int,QualifiedName]

    val sizeRange:SizeRange
    
    def idPrinter: QualifiedName => String = (q:QualifiedName) => amap(q).toString
    
    def getEdgeQuantities (succ: Map[Int,Map[Int,Iterable[Relation]]]): Iterable[Int] = {
      succ.flatMap{ case (src,next) => next.flatMap{ case (dst, rels) 
                                                          => rels.flatMap(r => r.other.get(NS.QN_SIZE) ) }    }
                            .flatten
                            .map(o => o.value.toString.toInt)
    }


}


class Indexer (doc: Document, idsVecInit: Vector[QualifiedName]=Vector(), weakInference: Boolean=false) extends Indexing {
    import Indexer._
    
    def document (): Document = doc 

    
    def getIds(doc: Document):Set[QualifiedName] = {      
        // OLD VERSION: uses flatMap
        // doc.statementOrBundle.flatMap{s =>Indexer.ids(s)}.toSet
        doc.statementOrBundle.foldLeft(Set[QualifiedName]()){case (set, s) => Indexer.addIds(set,s)}
    }
    
    private final def getEffect(r: Relation) = {
      amap(r.getEffect())
    }
    
    private final def getCause(r: Relation) = {
    		val cause=r.getCause()
    	  if (cause==null) -1 else { amap(cause) }
    }
       
    def getSuccessors(amap: Map[QualifiedName,Int]): Map[Int, Map[Int, Iterable[Relation]]] = {           
    	val relations:Iterable[Relation]=doc.statementOrBundle.collect{case r:Relation => r} 
      relations.groupBy(getEffect).mapValues(rels => rels.groupBy(getCause)).toMap
    }        
    
    def getPredecessors(amap: Map[QualifiedName,Int]): Map[Int, Map[Int, Iterable[Relation]]] = {       
      val entries:Iterable[Relation]=doc.statementOrBundle.collect{case r:Relation => r} 
      entries.groupBy(getCause).mapValues(rels => rels.groupBy(getEffect)).toMap
    }
                        
    def getNodes(doc: Document, amap: Map[QualifiedName,Int]): Map[Int,Node] = {
        doc.statementOrBundle.collect{case n:Node => amap(n.id) -> n}.toMap
    }
    
   
    
    def getSinks(ids: Set[QualifiedName], idsVec: Map[Int,QualifiedName], rel: Map[Int, Map[Int, Iterable[Relation]]]): Set[QualifiedName] = {
    		ids -- rel.keySet.flatMap(i => if (i == -1) None else Some(idsVec(i)))
    }
     
     
     //TODO: amap SHOULD be a parameter as it it is getTypes
     @inline def ifNotNull(q:QualifiedName, t:QualifiedName):Set[(Int,QualifiedName)] = {
       if (q!=null) Set((amap(q),t)) else Set()
     }
	   @inline def ifNotNull(q:QualifiedName, t:QualifiedName, weakInference: Boolean):Set[(Int,QualifiedName)] = {
		   if ((q!=null)&& !weakInference) Set((amap(q),t)) else Set()
	   }
     def typesInStatement(s: StatementOrBundle, weakInference: Boolean): Set[(Int,QualifiedName)] = {
       s match {
         case e:Entity              => ifNotNull(e.id,PROV_ENTITY)
         case a:Activity            => ifNotNull(a.id,PROV_ACTIVITY)
         case ag:Agent              => ifNotNull(ag.id,PROV_AGENT)
         case wdf:WasDerivedFrom    => ifNotNull(wdf.generatedEntity,PROV_ENTITY,weakInference) ++ ifNotNull(wdf.usedEntity,PROV_ENTITY,weakInference)  ++ ifNotNull(wdf.activity,PROV_ACTIVITY,weakInference)
         case waw:WasAssociatedWith => ifNotNull(waw.activity,PROV_ACTIVITY,weakInference)      ++ ifNotNull(waw.agent,PROV_AGENT,weakInference)        ++ ifNotNull(waw.plan,PROV_ENTITY,weakInference) ++ ifNotNull(waw.plan,PROV_PLAN,weakInference)
         case wat:WasAttributedTo   => ifNotNull(wat.entity,PROV_ENTITY,weakInference)          ++ ifNotNull(wat.agent,PROV_AGENT,weakInference)
         case aob:ActedOnBehalfOf   => ifNotNull(aob.activity,PROV_ACTIVITY,weakInference)      ++ ifNotNull(aob.delegate,PROV_AGENT,weakInference)     ++ ifNotNull(aob.responsible,PROV_AGENT,weakInference)
         case wgb:WasGeneratedBy    => ifNotNull(wgb.entity,PROV_ENTITY,weakInference)          ++ ifNotNull(wgb.activity,PROV_ACTIVITY,weakInference)
         case wib:WasInvalidatedBy  => ifNotNull(wib.entity,PROV_ENTITY,weakInference)          ++ ifNotNull(wib.activity,PROV_ACTIVITY,weakInference)
         case wsb:WasStartedBy      => ifNotNull(wsb.activity,PROV_ACTIVITY,weakInference)      ++ ifNotNull(wsb.starter,PROV_ACTIVITY,weakInference)   ++ ifNotNull(wsb.trigger,PROV_ENTITY,weakInference)
         case web:WasEndedBy        => ifNotNull(web.activity,PROV_ACTIVITY,weakInference)      ++ ifNotNull(web.ender,PROV_ACTIVITY,weakInference)     ++ ifNotNull(web.trigger,PROV_ENTITY,weakInference)
         case usd:Used              => ifNotNull(usd.entity,PROV_ENTITY,weakInference)          ++ ifNotNull(usd.activity,PROV_ACTIVITY,weakInference)
         case com:WasInformedBy     => ifNotNull(com.informed,PROV_ACTIVITY,weakInference)      ++ ifNotNull(com.informant,PROV_ACTIVITY,weakInference)
         case com:WasInfluencedBy   => Set()
         case spe:SpecializationOf  => ifNotNull(spe.generalEntity,PROV_ENTITY,weakInference)   ++ ifNotNull(spe.specificEntity,PROV_ENTITY,weakInference)
         case alt:AlternateOf       => ifNotNull(alt.alternate1,PROV_ENTITY,weakInference)      ++ ifNotNull(alt.alternate2,PROV_ENTITY,weakInference)
         case mem:HadMember         => ifNotNull(mem.collection,PROV_ENTITY,weakInference)      ++ ifNotNull(mem.entity.head,PROV_ENTITY,weakInference) // TODO: support multiple entities
         case bun:Bundle            => bun.statement.flatMap(typesInStatement(_,weakInference)).toSet ++ ifNotNull(bun.id,PROV_ENTITY,weakInference)
         case men:MentionOf         => ifNotNull(men.generalEntity,PROV_ENTITY,weakInference)   ++ ifNotNull(men.specificEntity,PROV_ENTITY,weakInference)  ++ ifNotNull(men.bundle,PROV_ENTITY,weakInference)  ++ ifNotNull(men.bundle,PROV_BUNDLE,weakInference)
     
       }
     }
     
     def getTypes(doc: Document, amap: Map[QualifiedName,Int]): Map[Int, Set[QualifiedName]] = {
       doc.statementOrBundle.flatMap(s => typesInStatement(s,weakInference)).groupBy(_._1).mapValues(_.map(_._2).toSet).toMap
     }
 
    
     val ids: Set[QualifiedName] =getIds(doc)
     
     val newIds: Set[QualifiedName] =ids -- idsVecInit
     
     val idsVec: Seq[QualifiedName] =idsVecInit.++(newIds)  // Use the one received first, and add the missing ones

     val amap: Map[QualifiedName, Int] =idsVec.zipWithIndex.toMap
     lazy val idsFun: Map[Int, QualifiedName] =swap(amap)
     
     lazy val succ: Map[Int, Map[Int, Iterable[Relation]]] =getSuccessors(amap)
     lazy val nodes: Map[Int, Node] =getNodes(doc,amap)
     lazy val pred: Map[Int, Map[Int, Iterable[Relation]]] =getPredecessors(amap)
     
     lazy val absentNodes: Set[Int] ={val relationIds=doc.statementOrBundle.collect{case r:Relation if r.id!=null => amap(r.id)}
                           ids.map(q => amap(q)) -- nodes.keySet -- relationIds}
     lazy val sinks: Set[QualifiedName] =getSinks(ids, idsFun, pred)
     

     lazy val sources: Set[QualifiedName] =getSinks(ids, idsFun, succ)
 
     lazy val types: Map[Int, Set[QualifiedName]] =getTypes(doc, amap)
     
     lazy val edgeQuantities: Iterable[Int] =getEdgeQuantities(succ)
                            
     lazy val edgeMax: Int =if (edgeQuantities.isEmpty) 0 else edgeQuantities.max
     lazy val edgeMin: Int =if (edgeQuantities.isEmpty) 0 else edgeQuantities.min

     val nodeMin=1  ///TODO: compute this
     val nodeMax=1

     lazy val sizeRange: SizeRange =SizeRange(edgeMin,edgeMax,nodeMin,nodeMax)
                            
     def sortNodes(rels: Map[Int,Map[Int,Iterable[Relation]]] = succ) : Seq[Set[Int]] = {
       val nodesIndex=nodes.keySet
       val result=sortNodes(nodesIndex,rels)    
       result
     }
                            
     def sortNodes(nodes: Set[Int], rels: Map[Int,Map[Int,Iterable[Relation]]]) : Seq[Set[Int]] = {
    		 if (nodes.isEmpty) {
    		   Seq()
    		 } else {
    		   val successors=nodes.flatMap(n => rels.get(n)).flatMap(_.keySet)
    		   val withoutPredecessor = nodes -- successors
    		   val rest= nodes -- withoutPredecessor
    		   Seq(withoutPredecessor) ++ sortNodes(rest,rels)
    		 }
     }
     
     def sortQualifiedNames(rels: Map[Int,Map[Int,Iterable[Relation]]] = succ) : Seq[Set[QualifiedName]] = {
       val nodes=sortNodes(rels)
       nodes.map(s => s.map(idsFun))
     }
     
     lazy val entities: Iterable[Entity] =doc.statementOrBundle.collect{case e:Entity   => e}
     lazy val activities: Iterable[Activity] =doc.statementOrBundle.collect{case a:Activity => a}

}