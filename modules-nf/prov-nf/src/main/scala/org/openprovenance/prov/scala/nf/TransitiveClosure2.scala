package org.openprovenance.prov.scala.nf

import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.ListBuffer

object ShortestPath {
  /**
 * Returns shortest paths between all pairs using Floyd-Warshall algorithm.
 * Nodes are assumed to be enumerated without any holes and enumeration
 * starts from 0.
 *
 * @param nodes the set of vertices
 * @param links the map of edges with costs
 * @return shortest paths between all pairs, including the source and destination
 */
def allPairsShortestPath(nodes: Set[Int], links: Map[Int, Set[Int]]): Map[Int, Map[Int, Seq[Int]]] = {
  val n = nodes.size
  val inf = Int.MaxValue

  // Initialize distance matrix.
  val ds = Array.fill[Int](n, n)(inf)
  for (i <- 0 until n) ds(i)(i) = 0
  for (i <- links.keys)
    for (j <- links(i))
      ds(i)(j) = 1

  // Initialize next vertex matrix.
  val ns = Array.fill[Int](n, n)(-1)

  // Here goes the magic!
  for (k <- 0 until n; i <- 0 until n; j <- 0 until n)
    if (ds(i)(k) != inf && ds(k)(j) != inf && ds(i)(k) + ds(k)(j) < ds(i)(j)) {
      ds(i)(j) = ds(i)(k) + ds(k)(j)
      ns(i)(j) = k
    }

  // Helper function to carve out paths from the next vertex matrix.
  def extractPath(path: ArrayBuffer[Int], i: Int, j: Int) {
    if (ds(i)(j) == inf) return
    val k = ns(i)(j)
    if (k != -1) {
      extractPath(path, i, k)
      path.append(k)
      extractPath(path, k, j)
    }
  }

  // Extract paths.
  val pss = scala.collection.mutable.Map[Int, Map[Int, Seq[Int]]]()
  for (i <- 0 until n) {
    val ps = scala.collection.mutable.Map[Int, Seq[Int]]()
    for (j <- 0 until n)
      if (ds(i)(j) != inf) {
        val p = new ArrayBuffer[Int]()
        p.append(i)
        if (i != j) {
          extractPath(p, i, j)
          p.append(j)
        }
        ps(j) = p
      }
    pss(i) = ps.toMap
  }

  // Return extracted paths.
  pss.toMap
}
}


object FloydWarshall {
  

	def computeTransitiveClosure(n: Int, ds: Array[Array[Boolean]]) {
		// Here goes the magic!
		for (k <- 0 until n; i <- 0 until n) {
		  if (!(i==k)) {  // we know it's reflexive, no need to update the array, we add it at the end in computeMembershipo
			  val ds_i=ds(i)
			  if ( ds_i(k) ) {
				  val ds_k=ds(k)
				  for (j <- 0 until n) {
					  if (!(j==i)) {  // we know it's reflexive, no need to update the array, we add it at the end in computeMembershipo
						  if ( ds_k(j) ) {
							  ds_i(j) = true
						  }
					  }
				  }
			  }  
		  }
		}
	}
	
	@inline def symmetricCheck(ds: Array[Array[Boolean]], k: Int, j: Int, ds_k: Array[Boolean]) = {
	  if (j < k) {
	    ds_k(j)
	  } else {
	    ds(j)(k)
	  }
	}

	
	def computeTransitiveClosureSymmetric(n: Int, ds: Array[Array[Boolean]]) {
		// Here goes the magic!
		for (k <- 0 until n; i <- 0 until n) { 
		  if (!(i==k)) {  // we know it's reflexive, no need to update the array, we add it at the end in computeMembershipo
			  val ds_i=ds(i)
			  if ( symmetricCheck(ds,i,k,ds_i) ) {  //rely on symmetric if appropriate
				  val ds_k=ds(k)
				  for (j <- 0 until i) { // OPTIMISATION: j loops till i, ensures ds(i)(j)  j<i
					  if (!(j==i)) {  // we know it's reflexive, no need to update the array, we add it at the end in computeMembershipo
						  if (!(k==j)) {  // we know it's reflexive, no need to update the array, we add it at the end in computeMembershipo
							  if ( symmetricCheck(ds,k,j,ds_k) ) { //rely on symmetric if appropriate
								  ds_i(j) = true
							  }
						  }
					  }
				  }
			  }  
		  }
		}
	}

	def computeTransitiveClosureOriginal(n: Int, ds: Array[Array[Boolean]]) {
		// Here goes the magic!
		for (k <- 0 until n; i <- 0 until n) {
			if ( ds(i)(k) ) {
				val dsk=ds(k)
				val dsi=ds(i)
			  for (j <- 0 until n)
				 if ( dsk(j) ) {
					 dsi(j) = true
			  }
			}
		}
	}

	def transitiveClosureMap(nodes: Set[Int], links: Map[Int, Set[Int]]): Map[Int, Map[Int, Boolean]] = {
			val n = nodes.size

					// Initialize distance matrix.
					val ds = Array.fill[Boolean](n, n)(false)
					for (i <- 0 until n) ds(i)(i) = true
					for (i <- links.keys)
						for (j <- links(i))
							ds(i)(j) = true
							transitiveClosureMap(n,ds)
	}


	def transitiveClosureMap(n: Int, ds: Array[Array[Boolean]]): Map[Int, Map[Int, Boolean]] = {
			computeTransitiveClosure(n,ds)
			// Extract matrix as map.
			val pss = scala.collection.mutable.Map[Int, Map[Int, Boolean]]()
			for (i <- 0 until n) {
				val ps = scala.collection.mutable.Map[Int, Boolean]()
						for (j <- 0 until n)
							if (ds(i)(j) ) {
								ps(j) = true
							}
				pss(i) = ps.toMap
			}
			// Return extracted paths.
			pss.toMap
	}
}



class TransitiveClosure2[A <: Ordered[A]] {
  var set:Set[Set[A]]=Set()
  var computed=false
  
  def duplicate() = {
    val tc=new TransitiveClosure2[A]()
    tc.set=set
    tc.computed=computed
    tc
  }

  
  def add2(ss: Set[A]) {
	  if (!ss.isEmpty) {
     set += ss
     computed=false;
	  }
  }
  
  def showds(ds: Array[Array[Boolean]]){
    val n=ds.size
    for (i <- 0 until n) {
			for (j <- 0 until n) {
			  print(if (ds(i)(j)) "1," else  "0,")
			}
			println("")
	  }
  }

  var membership:Map[A, Set[A]]=Map()
  
  def computeMembership(n: Int, ds: Array[Array[Boolean]], idsVec: Vector[A]): Map[A, Set[A]] = {
    val memb = ListBuffer[(A, Set[A])]()
    
    for (i <- 0 until n) {
		  val mem=new ListBuffer[A]()
		  val elem_i=idsVec(i)
		  mem += elem_i      // need to add self, to make relation reflexive

		  val ds_i=ds(i)
			for (j <- 0 until n) {
			  if (ds_i(j)) {
			    mem += idsVec(j)
			  }
			}
		  val pair:(A, Set[A])= (elem_i,mem.toSet)
      memb += pair
	  }
    memb.toMap
  }
  
  @inline def symmetricCheck(ds: Array[Array[Boolean]], i: Int, j: Int, ds_i: Array[Boolean]) = {
	  if (j < i) {
	    ds_i(j)
	  } else {
	    ds(j)(i)
	  }
	}

		  
  def computeMembershipSymmetric(n: Int, ds: Array[Array[Boolean]], idsVec: Vector[A]): Map[A, Set[A]] = {
    val memb = ListBuffer[(A, Set[A])]()
    
    for (i <- 0 until n) {
		  val mem=new ListBuffer[A]()
		  val elem_i=idsVec(i)
		  mem += elem_i      // need to add self, to make relation reflexive

		  val ds_i=ds(i)
			for (j <- 0 until n) {
			  if ((!(i==j)) && symmetricCheck(ds,i,j,ds_i)) {  //(ds_i(j))
			    mem += idsVec(j)
			  }
			}
		  val pair:(A, Set[A])= (elem_i,mem.toSet)
      memb += pair
	  }
    memb.toMap
  }
		  
  var ds: Array[Array[Boolean]]=null
  
  def transitiveClosure(optimized: Boolean=true) = {
    if (!computed) {
      val ids=set.flatten
      val idsVec=ids.toVector 
      val amap=idsVec.zipWithIndex.toMap
      
      val n = idsVec.size
      
      //println("found set of size " + n)

      // Initialize distance matrix.
      val ds = Array.fill[Boolean](n, n)(false)
      set.foreach { s => {
                           val i=amap(s.head)
                           val ds_i=ds(i)
                           s.foreach { elem => val j=amap(elem)
                                               if (!(i==j)) {
                                            	   ds_i(j)=true
                                            		 ds(j)(i)=true
                                              }}
                           }}
      
      if (optimized) {
        //showds(ds)
    	  FloydWarshall.computeTransitiveClosureSymmetric(n, ds)     
    	  membership=computeMembershipSymmetric(n,ds,idsVec)
    	  this.ds=ds
      } else {
        FloydWarshall.computeTransitiveClosure(n, ds)     
    	  membership=computeMembership(n,ds,idsVec)
    	  this.ds=ds

      }
      computed=true
      
    }
    membership
  }
  

  
}
	
