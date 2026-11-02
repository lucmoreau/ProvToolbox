package org.openprovenance.prov.scala.factorgraph
import java.io.File
import java.io.FileWriter
import java.io.Writer

import scala.annotation.tailrec
import org.openprovenance.prov.model.ProvUtilities
import org.openprovenance.prov.scala.immutable.Bundle
import org.openprovenance.prov.scala.immutable.Document
import org.openprovenance.prov.scala.immutable.HasLabel
import org.openprovenance.prov.scala.immutable.HasOther
import org.openprovenance.prov.scala.immutable.Indexer
import org.openprovenance.prov.scala.immutable.Indexing
import org.openprovenance.prov.scala.immutable.Kind
import org.openprovenance.prov.scala.immutable.OrderedDocument
import org.openprovenance.prov.scala.immutable.QualifiedName
import org.openprovenance.prov.scala.immutable.Relation
import org.openprovenance.prov.scala.immutable.Statement
import org.openprovenance.prov.scala.immutable.Used
import org.openprovenance.prov.scala.immutable.WasGeneratedBy
import org.openprovenance.prov.scala.interop._


sealed abstract class GraphElement (id: String, properties: Map[String,String]) {
  def toMatlab(outw: Writer): Unit
}

case class Vertex (id: String, properties: Map[String,String]) extends GraphElement(id,properties) {
    	
	 @tailrec private def iterateArray(i: Int, xs: Array[String], fun: (Int,String) => Any ): Unit = {
	  	if (i < xs.length) {
	  		fun(i,xs(i))
	  		iterateArray(i+1, xs, fun)
	  	}
	 }
    def toMatlab(outw: Writer): Unit = {
        if (false) {
        	outw.write("% id: ")
        	outw.write(properties(FactorGraph.label))
        	outw.write("\n")
        }
    		properties(FactorGraph.kind) match {
    		  case "Var" => {
    		    outw.write("varinf(")
    		    outw.write(id)
    		    outw.write(").name='")
    		    outw.write(properties(FactorGraph.identifier))
    		    outw.write("';\n")
    		  }
    		  case "Proc" => {
    			  outw.write("pot{")
    			  outw.write(id)
    			  outw.write("}.variables=[")
    			  outw.write(properties(FactorGraph.neighboors))
    			  outw.write("];\n")
    		    if (properties(FactorGraph.cpd).contains("::")) {
    		      val strings=properties(FactorGraph.cpd).split("::")
    		      iterateArray(0,strings, (i,x) => {     
    		    	  outw.write("pot{")
    		    	  outw.write(id)
    		    	  outw.write("}.table(:,:,")
    		    	  outw.write((i+1).toString)
    		    	  outw.write(")=[")
    		    	  outw.write(x)
    		    	  outw.write("];\n")
    		      })
    		    } else {
    		    	outw.write("pot{")
    		    	outw.write(id)
    		    	outw.write("}.table=[")
    		    	outw.write(properties(FactorGraph.cpd))
    		    	outw.write("];\n") 
    		    }
    		  }
    		}
        
    }

}
case class Edge (id: String, from: String, to: String, properties: Map[String,String]) extends GraphElement(id,properties) {
    def toMatlab(outw: Writer): Unit = {
        outw.append(from)
        outw.append(" -> ")
        outw.append(to)
        outw.append("\t[")
        outw.write(properties.map{ case (p,v) => p + "=\"" + v + "\""}.mkString(" ",",\n\t  ",""))
        outw.append("]\n")
        outw.append("\n")
    }
}

case class SubGraph (id: String, properties: Map[String,String], elements: Iterable[GraphElement]) extends GraphElement(id,properties) {
   def toMatlab(outw: Writer): Unit = {
       elements.foreach { x => x.toMatlab(outw) }
   }
}


object FactorGraph {

  val kind="kind"
  val identifier="identifier"
  
  val label="label"
  
  
  val cpd="cpd"

  
  val neighboors="neighboors"
 
  val  DOT_NS="http://openprovenance.org/Toolbox/dot#"
  val  QMRA_NS="http://provenance.ecs.soton.ac.uk/qmra#"
  
  val cpdQN      =new QualifiedName("qmra", "cpd", QMRA_NS)

  
  val pu=new ProvUtilities

  def findNeighboors(s:Statement, ind: Indexing, 
                     entities: Map[QualifiedName,Int], entities_names: Seq[String],
                     m: Map[String,String]): Map[String,String] = {
    val sid=ind.amap(s.id)
    val used=ind.succ.get(sid) match {
      case Some(map) => map.values.flatten.filter(_.isInstanceOf[Used]).map(_.asInstanceOf[Used])
      case None => Set()
    }
    val gen=ind.pred.get(sid) match {
      case Some(map) => map.values.flatten.filter(_.isInstanceOf[WasGeneratedBy]).map(_.asInstanceOf[WasGeneratedBy])
      case None => Set()
    }
    val dest=used.map(_.entity.asInstanceOf[QualifiedName]).map(q => entities_names(entities(q)))
    val src =gen .map(_.entity.asInstanceOf[QualifiedName]).map(q => entities_names(entities(q)))
    val all = dest ++ src
    m + (neighboors -> all.mkString(" "))
  }
  
  def toElements(s: Statement,idmap: QualifiedName=>String, r: Indexing, 
                 entities: Map[QualifiedName,Int], entities_names: Seq[String],
                 activities: Map[QualifiedName,Int], activities_names: Seq[String]): Set[GraphElement] = {

        s.enumType match {
          case Kind.ent    => Set(Vertex(entities_names(entities(s.id)),addKind("Var", addLabel(s,Map(identifier -> s.id.localPart)))))
          case Kind.ag     => Set()
          case Kind.act    => Set(Vertex(activities(s.id).toString,findNeighboors(s, r, entities, entities_names, addKind("Proc", addLabel(s,addProperties(s,Map()))))))
          case _           => Set()
          
        }
  }
  
  def addProperties(s: Statement, m: Map[String,String]): Map[String,String] = {
    val value=s.asInstanceOf[HasOther].other.get(cpdQN)
    value match {
      case None => m
      case Some(set) => m + (cpd -> set.head.value.toString)
    }

  }
 
  def toElementsBundle(s: Bundle,idmap: QualifiedName=>String, r: Indexing,
                 entities: Map[QualifiedName,Int], entities_names: Seq[String],
                 activities: Map[QualifiedName,Int], activities_names: Seq[String]): Seq[GraphElement] = {

          Seq(SubGraph(idmap(s.id),Map(label -> s.id.localPart),s.statement.flatMap{ s => toElements(s,idmap,r, entities, entities_names, activities, activities_names) }))
  }
  

  
  def addKind(value: String, m: Map[String,String]): Map[String,String] = {
    m  + (kind -> value)
  }

  def addLabel(s:Statement, m: Map[String,String]): Map[String,String] = {
    val res= s match {
      case s:HasLabel =>
        val labelOpt=s.label.headOption // Note, picks one, order unspecified
        labelOpt match {
                   case Some(str) => m  + (label -> str.value)
                   case None => if (s.id==null) {
                                  m
                                } else {
                                  m+ (label -> s.id.toString)
                                }
        }
      case _ => if (s.id==null) {
    	             m  
                } else {
                	m + (label -> s.id.toString)
                }
    } 
    res 
  }
  
 
  /*
  def toElements(s: Relation,idmap: QualifiedName=>String, r: Indexing, 
                 entities: Map[QualifiedName,Int], entities_names: Seq[String],
                 activities: Map[QualifiedName,Int], activities_names: Seq[String]): Seq[GraphElement] = {
    
          Seq()

  }

   */
    
  def toElementsDocument(doc: Document, idmap: QualifiedName=>String, r: Indexing,
                 entities: Map[QualifiedName,Int], entities_names: Seq[String],
                 activities: Map[QualifiedName,Int], activities_names: Seq[String]): Seq[GraphElement] = {
    val seq1=entities  .toSeq.sortWith{case ((q1,i1),(q2,i2)) => i1 < i2}.map(p => r.nodes(r.amap(p._1)))
    val seq2=activities.toSeq.sortWith{case ((q1,i1),(q2,i2)) => i1 < i2}.map(p => r.nodes(r.amap(p._1)))

    /*
      doc.statements.flatMap { s => toElements(s,idmap,r, entities, entities_names, activities, activities_names) } ++ 
               doc.bundles.flatMap { s => toElements(s,idmap,r, entities, entities_names, activities, activities_names) }
               * 
               */
    (seq1++seq2).flatMap { s => toElements(s.asInstanceOf[Statement],idmap,r, entities, entities_names, activities, activities_names) }
  }
  
  /*
  def toElements(doc: OrderedDocument, idmap: QualifiedName=>String, r: Indexing, 
                 entities: Map[QualifiedName,Int], entities_names: Seq[String],
                 activities: Map[QualifiedName,Int], activities_names: Seq[String]): Seq[GraphElement] = {
      doc.orderedStatements.flatMap { s => toElements(s,idmap,r, entities, entities_names, activities, activities_names) } ++ 
         doc.orderedBundles.flatMap { s => toElements(s,idmap,r, entities, entities_names, activities, activities_names) }
  }*/
  
  def nameNodes(doc:Document, ind: Indexer): (Map[QualifiedName, Int], Seq[String], Map[QualifiedName, Int], Seq[String]) = {
    val foundEntities   =ind.entities.map(_.id).toSet
    val foundActivities =ind.activities.map(_.id).toSet
    val order=ind.sortQualifiedNames(ind.pred)
    val entities_vec  =order.flatMap(x => x.filter(p => foundEntities  .contains(p)).toSeq)
    val activities_vec=order.flatMap(x => x.filter(p => foundActivities.contains(p)).toSeq)
    //val entities_vec=doc.statements().filter(_.isInstanceOf[Entity]).map(_.id).toSeq
   
    val entities=entities_vec.zipWithIndex.toMap
    //val entities_names=entities_vec.map("a" + entities(_))
    val entities_names  =entities_vec  .map(_.localPart)
    val activities_names=activities_vec.map(_.localPart)

    //val activities_vec=doc.statements().filter(_.isInstanceOf[Activity]).map(_.id).toSeq
    val activities=activities_vec.zipWithIndex.toMap.view.mapValues(x=>x+1)

    (entities, entities_names, activities.toMap, activities_names)
  }
  
  def variableDeclaration(names: Seq[String], outw: Writer): Unit = {
    outw.write("variables=1:")
    outw.write(names.length.toString)
    outw.write(";\n")
    
    outw.write(names.mkString("[", " ", "]"))
    outw.write ("=assign(variables); ")
    outw.write("\n")
  }
  
  def create(doc: Document, idmap: QualifiedName=>String, r: Indexer, outw: Writer): Unit = {
    outw.write("import qillBRML.*\n")
    outw.write("% factor graph start\n")
    
    val (entities,entities_names, activities, activities_names)=nameNodes(doc, r)
    variableDeclaration(entities_names,outw)
    
    toElementsDocument(doc, idmap, r, entities, entities_names, activities, activities_names)
      .foreach(e => e.toMatlab(outw))
      
    outw.write("% factor graph end\n");
    outw.close()
               
  }
  
  def create(doc: OrderedDocument, idmap: QualifiedName=>String, r: Indexer, outw: Writer): Unit = {
    outw.write("import qillBRML.*\n")
		outw.write("% factor graph start\n")
    
		val (entities,entities_names, activities, activities_names)=nameNodes(doc, r)
    variableDeclaration(entities_names,outw)
    
    toElementsDocument(doc, idmap, r, entities, entities_names, activities, activities_names)
      .foreach(e => e.toMatlab(outw))
      
    outw.write("% factor graph end\n");
    outw.close()
               
  }
  
  def create(doc: Document, idmap: QualifiedName=>String, r: Indexer, outw: File): Unit = {
    create_with_writer(doc,idmap,r,new FileWriter(outw));
  }

  def create_with_writer(doc: Document, idmap: QualifiedName=>String, r: Indexer, outw: Writer): Unit = {
    doc match {
      case od:OrderedDocument => create(od,idmap, r, outw)
      case _ => create(doc,idmap, r, outw)
    }
  }
  
  def create(ind: Indexer, outw: File): Unit = {
     create(ind.document(),ind.idPrinter,ind,outw)
  }
  


  
}

class MatlabOutputer extends Outputer {
    def output(d:Document, out: Output, params: Map[String,String]): Unit = {
        val ind=new Indexer(d)
        output(ind,out,params)
    }
    
    def output(d:Indexing, out: Output, params: Map[String,String]): Unit = {
      out match {
        case StandardOutput() =>
          throw new UnsupportedOperationException
        case FileOutput(f:File) =>
          FactorGraph.create(d.asInstanceOf[Indexer],f)
        case StreamOutput(_) => throw new UnsupportedOperationException
      }
    }
}

