package org.openprovenance.prov.scala.summary

import org.openprovenance.prov.scala.immutable.{Indexer, QualifiedName}

object Merge {

	def renameOther(ind1: SummaryIndex, ind2: SummaryIndex, prettyMethod : PrettyMethod.Pretty): SummaryIndex = {


	  val provTypesIndex1=ind1.provTypeIndex
		val ind1size=provTypesIndex1.values.max

		val provTypesIndex2=ind2.provTypeIndex

		val commonProvTypes         = provTypesIndex1.keySet.intersect( provTypesIndex2.keySet )
		val commonProvTypesIndexes2 = commonProvTypes.map(provTypesIndex2)
	 	val commonProvTypesQNames2  = commonProvTypesIndexes2.map(ind2.idsFun)


	 	val uniqueProvTypes2        = provTypesIndex2.keySet -- commonProvTypes
	 	val uniqueProvTypesIndexes2 = uniqueProvTypes2.map(provTypesIndex2)
	 	val uniqueProvTypesQNames2  = uniqueProvTypesIndexes2.map(ind2.idsFun)

	 	val uniqueProvTypesQNamesVec2=uniqueProvTypesQNames2.toVector


		val uniqueRemappedIndex2=(uniqueProvTypesQNamesVec2 zip (Stream from ind1size+11)).toMap

	  val convertPreparation=uniqueRemappedIndex2.map{case (q,i) => (q,SummaryConstructor.typeQualifiedName(i,ind2.nsBase),i)}
	  val newAmapForUnique=convertPreparation.map{case (q1,q2,i) => (q2,i)}.toMap

	  // maps QualifiedName to new QualifiedName (if unique) or to self (if common)
	  val qConverter:Map[QualifiedName,QualifiedName] =convertPreparation.map{case (q1,q2,i) => (q1,q2)}.toMap ++ commonProvTypesQNames2.map(q => (q,q))

	  //println(qConverter)
	  //println(newAmapForUnique)


		val amap=commonProvTypesQNames2.map(qConverter).map(q => (q, ind1.amap(q))).toMap ++ newAmapForUnique


		val idsFun=Indexer.swap(amap)

		def renamingFunction(i: Int) = {
				//println("renaming")
				//println(i)
				//println(ind2.idsFun)
				//println(qConverter)
				//println(amap)
				val v=amap(qConverter(ind2.idsFun(i)))
				//println(v)
				//println("done")
				v
	  }


	  val mapToBaseUri=ind2.mapToBaseUri.map{case (i,s) => (renamingFunction(i),s)}
	  val provTypeIndex=ind2.provTypeIndex.map{case (s, i) => (s,renamingFunction(i))}
	  val prettyNames=SummaryConstructor.makePrettyNames(mapToBaseUri, provTypeIndex,prettyMethod)
		val oldPrettyNames=ind2.prettyNames. map{case (i,s) => (renamingFunction(i),s)}
	  val labelMapper= prettyNames.keySet.map(i => (oldPrettyNames(i), prettyNames(i))).toMap

    val si=new SummaryIndex(ind2.provTypeIndex.view.mapValues(renamingFunction).toMap,
					                 newAmapForUnique.keys,
					                 idsFun,
					                 ind2.sumNodes.map(n => n.rename(qConverter,labelMapper)),
					                 ind2.succ.map{case (i1,m) => (renamingFunction(i1),m.map{case (i2,s) => (renamingFunction(i2), s.map(r => r.rename(qConverter)))})},
					                 amap,
					                 mapToBaseUri,
					                 prettyNames,
					                 ind2.nsBase) //TODO???
    si

	}


  def merge(ind1: SummaryIndex, ind2: SummaryIndex) = {

		  val ids1 = ind1.idsVec
		  val ids2 = ind2.idsVec
		  //val commonIds = ids1.intersect(ids2)

		 // val nodes1=commonIds.map(ind1.amap(_))
		  //val nodes2=commonIds.map(ind2.amap(_))

		  val provTypes1  = ind1.provTypeIndex.keySet
      val provTypes2  = ind2.provTypeIndex.keySet

      val prefixes1 = ind1 .prefixes
      val prefixes2 = ind2.prefixes

      val namespace1 = NamespaceHelper.toNamespace(prefixes1)
      val namespace2 = NamespaceHelper.toNamespace(prefixes2)

      val qualifiedNames1 = ind1.amap.keySet
      val qualifiedNames2 = ind2.amap.keySet


  }

}
