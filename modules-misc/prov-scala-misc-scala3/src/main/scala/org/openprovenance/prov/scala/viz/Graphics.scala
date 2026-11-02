package org.openprovenance.prov.scala.viz
import org.openprovenance.prov.scala.immutable.{Attribute, Bundle, Document, Format, HasLabel, HasOther, HasTime, Indexer, Indexing, Kind, NS, OrderedDocument, Other, QualifiedName, Relation, SizeRange, Statement}
import java.io.FileWriter
import java.io.Writer
import java.io.File
import java.io.OutputStream

import org.openprovenance.prov.scala.interop.{FileOutput, Output, Outputer, StandardOutput, StreamOutput}
import org.openprovenance.prov.model.ProvUtilities

sealed abstract class GraphElement (id: String, properties: Map[String,String]) {
  def toDot(outw: Writer): Unit
}

case class Vertex (id: String, properties: Map[String,String]) extends GraphElement(id,properties) {
    def toDot(outw: Writer): Unit = {
        outw.append(id)
        outw.append("\t[")
        outw.write(properties.map{ case (p,v) => p + "=\"" + v + "\""}.mkString(" ",",\n\t  ",""))
        outw.append("]\n")
    }

}
case class Edge (id: String, from: String, to: String, properties: Map[String,String]) extends GraphElement(id,properties) {
    def toDot(outw: Writer): Unit = {
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
   def toDot(outw: Writer): Unit = {
       outw.append("subgraph cluster" + id + " { \n")
       elements.foreach { x => x.toDot(outw) }
       outw.append("}\n")
   }
}


object Graphics {
  import org.openprovenance.prov.scala.immutable.NS.QN_SIZE

  val shape="shape"
  val side="side"
  val color="color"
  val fillcolor="fillcolor"
  val fontcolor="fontcolor"
  val style="style"
  val filled="filled"
  val polygon="polygon"
  val house="house"
  val ellipse="ellipse"
  val url="URL"
  val penwidth="penwidth"
  val width="width"
  val label="label"
  val tooltip="tooltip"
  val arrowhead="arrowhead"
  val none="none"
  val point="point"
  
  val entityFillColor="#FFFC87"  //yellow
  val entityColor="#808080"      //gray
  val entityFontColor="#808080"      //gray
  
  val agentFillColor="#FDB266"   //orange
  val agentColor="#000000" 
  val agentFontColor="#000000" 
  
  val activityFillColor="#9FB1FC"//blue
  val activityColor="#0000FF"    //blue
  val activityFontColor="#0000FF"    //blue
  
  val edgeColor="#000000" 

  val  DOT_NS="http://openprovenance.org/Toolbox/dot#"

  val colorQN    =new QualifiedName("dot", "color",     DOT_NS)
  val fillcolorQN=new QualifiedName("dot", "fillcolor", DOT_NS)
  val fontcolorQN=new QualifiedName("dot", "fontcolor", DOT_NS)    
  val tooltipQN  =new QualifiedName("dot", "tooltip",   DOT_NS) 
  
  val graphicsQNs = Set(colorQN, fillcolorQN, fontcolorQN, tooltipQN)
  
  
  val pu=new ProvUtilities

  
  def toElements(s: Statement,idmap: QualifiedName=>String, r: SizeRange): Set[GraphElement] = {

        s.enumType match {
          case Kind.ent    => Set(Vertex(idmap(s.id),addLabel(s,addWidth(s,r,Map(url -> s.id.getUri, shape->ellipse, color->colorOf(s,entityColor),   fontcolor->fontcolorOf(s,entityFontColor),   fillcolor->fillcolorOf(s,entityFillColor),   style->filled)))))
          case Kind.ag     => Set(Vertex(idmap(s.id),addLabel(s,addWidth(s,r,Map(url -> s.id.getUri, shape->house,   color->colorOf(s,agentColor),    fontcolor->fontcolorOf(s,agentFontColor),    fillcolor->fillcolorOf(s,agentFillColor),    style->filled)))))
          case Kind.act    => Set(Vertex(idmap(s.id),addLabel(s,addWidth(s,r,Map(url -> s.id.getUri, shape->polygon, color->colorOf(s,activityColor), fontcolor->fontcolorOf(s,activityFontColor), fillcolor->fillcolorOf(s,activityFillColor), style->filled, side->"4")))))
          case _ => toElements(s.asInstanceOf[Relation],idmap,r)
          
        }
  }
  
  def colorOf(s: Statement, col: String): String = {
    colorOf(s,col,colorQN)
  }
  def fillcolorOf(s: Statement, col: String): String = {
    colorOf(s,col,fillcolorQN)
  }  
  def fontcolorOf(s: Statement, col: String): String = {
    colorOf(s,col,fontcolorQN)
  }
  
  def colorOf(s: Statement, col: String, colorQN: QualifiedName): String = {
    s match {
      case o: HasOther => o.other.get(colorQN) match {
                                                  case Some(set) => set.head.value.toString()
                                                  case None => col
                                                }
      case _ => col
    }
  }
  
  def toElements(s: Bundle,idmap: QualifiedName=>String, r: SizeRange): Set[GraphElement] = {

          Set(SubGraph(idmap(s.id),Map(label -> s.id.localPart, url -> s.id.getUri),s.statement.flatMap{ s => toElements(s,idmap,r) }))
  }
  
  def getSize(s:Statement): Option[Int] = {
    s.asInstanceOf[HasOther].other.get(QN_SIZE) match {
                                                  case Some(set) => set.headOption match {
                                                                                  case Some(s) => Some(s.value.toString.toInt)
                                                                                  case None => None }
                                                  case _ => None
                                                } 
  } 


  
  def addPenWidth(s:Relation, r:SizeRange, m: Map[String,String]): Map[String,String] = {
    val sizeAttributes=getSize(s.asInstanceOf[Statement])
    sizeAttributes match {
      case Some(size) => m + (penwidth -> edgeVisualSizeAttribute(size, r.edgeMin, r.edgeMax).toString)
      case None => m
    } 
  }
  
  def addWidth(s:Statement, r:SizeRange, m: Map[String,String]): Map[String,String] = {
    val sizeAttributes=getSize(s)
    sizeAttributes match {
      case Some(size) => m + (width -> nodeVisualSizeAttribute(size, r.nodeMin, r.nodeMax).toString)
      case None => m
    } 
  }

  var useIdAsLabel=true

  def addLabel(s:Statement, m: Map[String,String]): Map[String,String] = {
    val res= s match {
      case s:HasLabel => {
        val labelOpt=s.label.headOption // Note, picks one, order unspecified
        labelOpt match {
                   case Some(str) => m  + (label -> str.value)
                   case None => if (useIdAsLabel)  m + (label -> s.getId.getLocalPart) else m
        }
      }
      case x if x.getId!=null => if (useIdAsLabel)  m + (label -> s.getId.getLocalPart) else m
      case _ => m
    } 
    res + (tooltip -> s.id.localPart)
  }
  var bncounter=0
  
  def usefulAttributes(attrs: Set[Attribute]): Set[Attribute] = {
    attrs.filterNot(attr => (attr.isInstanceOf[Other]
      && {
      val ns = attr.asInstanceOf[Other].elementName.namespaceURI
      ns.equals(DOT_NS) || ns.equals(NS.SUM_NS)
    }))
  }
  
  
  def toElements(s: Relation,idmap: QualifiedName=>String, r: SizeRange): Set[GraphElement] = {
    val mapi=Map(color->colorOf(s.asInstanceOf[Statement],edgeColor))
    val cast=s.asInstanceOf[org.openprovenance.prov.model.Relation]
    val causes=pu.getOtherCauses(cast)
    val time=if (s.isInstanceOf[HasTime]) s.asInstanceOf[HasTime].time else None
    val attrs=s.getAttributes
    if ((causes!=null) || (attrs!=null && usefulAttributes(attrs).nonEmpty) || (!time.equals(None))) {
          bncounter=bncounter+1
          val bnid="bn" + bncounter.toString;
          val aset:Set[GraphElement]=Set(Vertex(bnid, Map(label->"",shape->point)++mapi),
        		                             Edge(null,idmap(s.getEffect), bnid, addPenWidth(s,r,Map(arrowhead->none)++mapi)))
          val aset2=if (s.getCause!=null) {
        	             aset ++ Set(Edge(null,bnid, idmap(s.getCause), addPenWidth(s,r,mapi)))
                    } else {
                    	aset
                    }
          val aset3=if (causes!=null) {
                       aset2 ++ Set(Edge(null,bnid, idmap(causes.get(0).asInstanceOf[QualifiedName]), addPenWidth(s,r,mapi)))
                    } else {
                      aset2
                    }
          //TODO: display attributes
          //TODO: display time
          aset3

    } else {
        if ((s.getEffect!=null) && (s.getCause!=null)) {
          Set(Edge(null,idmap(s.getEffect), idmap(s.getCause), addPenWidth(s,r,mapi)))
	} else {
          Set()
        }
    } 

  }
    
  def toElements(doc: Document, idmap: QualifiedName=>String, r: SizeRange): Set[GraphElement] = {
      doc.statements().flatMap { s => toElements(s,idmap,r) }.toSet ++ doc.bundles().flatMap { s => toElements(s,idmap,r) }
  }
  
  def toElements(doc: OrderedDocument, idmap: QualifiedName=>String, r: SizeRange): Seq[GraphElement] = {
      doc.orderedStatements().flatMap { s => toElements(s,idmap,r) } ++ doc.orderedBundles().flatMap { s => toElements(s,idmap,r) }
  }
  
  def toDot(doc: Document, idmap: QualifiedName=>String, r: SizeRange, outw: Writer): Unit = {
    outw.write("digraph \"PROV\" { size=\"16,12\"; rankdir=\"BT\";\n")
    
    toElements(doc, idmap, r)
      .foreach(e => e.toDot(outw))
      
    outw.write("}\n");
    outw.close()
               
  }
  
  def toDot(doc: OrderedDocument, idmap: QualifiedName=>String, r: SizeRange, outw: Writer): Unit = {
    outw.write("digraph \"PROV\" { size=\"16,12\"; rankdir=\"BT\";\n")
    
    toElements(doc, idmap, r)
      .foreach(e => e.toDot(outw))
      
    outw.write("}\n");
    outw.close()
               
  }
  
  def toDot(doc: Document, idmap: QualifiedName=>String, r: SizeRange, outw: File): Unit = {
    doc match {
      case od:OrderedDocument => toDot(od,idmap, r, new FileWriter(outw))
      case _ => toDot(doc,idmap, r, new FileWriter(outw))
    }
    
  }
  
  def toDot(ind: Indexing, outw: File): Unit = {
     toDot(ind.document(),ind.idPrinter,ind.sizeRange,outw)
  }

    
  def dotConvert(dot: String, svg:String, t: String): Unit = {
		  val runtime = Runtime.getRuntime
		  val proc = runtime.exec("dot -o " + svg + " -T" + t + " " + dot)
  }
  def dotConvert(dot: String, os: OutputStream, t: String): Unit = {
		  val runtime = Runtime.getRuntime
		  val proc = runtime.exec("dot -T" + t + " " + dot)
		  val in=proc.getInputStream
		  org.apache.commons.io.IOUtils.copy(in, os)
  }

  val nodeMaxWidth=4.0
  val edgeMaxWidth=9.0

   
  def edgeVisualSizeAttribute(size: Int, min: Int, max: Int): Double = {
		  if (min==max) 1.0 else { 1.0 + edgeMaxWidth.toDouble * (size-min).toDouble / (max-min).toDouble }
  }
  def nodeVisualSizeAttribute(size: Int, min: Int, max: Int): Double = {
		  if (min==max) 1.0 else { 1.0 + nodeMaxWidth.toDouble * (size-min).toDouble / (max-min).toDouble }
  }


  
}

class SVGOutputer(otype:String="svg", dir:String="") extends Outputer {
    def output(d:Document, out: Output, params: Map[String,String]): Unit = {
        val ind=new Indexer(d)
        output(ind,out,params)
    }
    
    def output(d:Indexing, out: Output, params: Map[String,String]): Unit = {
     
      out match {
        case StandardOutput() => throw new UnsupportedOperationException
        case FileOutput(f:File) => {
          val prefix=Format.substParams(f.getPath.substring(0,f.getPath.length() -3),params)
          val suffix=f.getPath.substring(f.getPath.length() -3)
          val dot=prefix + "dot"
          val svg=prefix + suffix
          Graphics.toDot(d,new File(dot))
          Graphics.dotConvert(dot,svg,otype)
        }
        case StreamOutput(os:OutputStream) => {
          val dot=dir + "tmp." + "dot"
          val is=Graphics.toDot(d,new File(dot)) /// Ideally, would like to write  in a stream and pipe that to dot
          Graphics.dotConvert(dot,os,otype)
        }
      }
    }
}

