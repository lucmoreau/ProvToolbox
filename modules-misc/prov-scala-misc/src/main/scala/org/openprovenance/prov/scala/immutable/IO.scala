package org.openprovenance.prov.scala.immutable


import java.io.{BufferedWriter, File, FileWriter, OutputStreamWriter}

import org.openprovenance.prov.model.Namespace
import org.openprovenance.prov.scala.interop._
import org.openprovenance.prov.scala.streaming.{DocBuilder, DocBuilderFunctions}
import org.parboiled2.ParseError

import scala.util.{Failure, Success}

object NS {
  val SUM_NS = "http://openprovenance.org/summary/ns#"


  val QN_SIZE  = new QualifiedName("sum", "size", SUM_NS)   // the size of the node/edge for visualisation purpose
  val QN_NBR   = new QualifiedName("sum", "nbr", SUM_NS)    // the nbr associated with the type
  val QN_COUNT = new QualifiedName("sum", "count", SUM_NS)  // the number of nodes/edges in the instance graph with that type
  val QN_LEVEL0 = new QualifiedName("sum", "level0", SUM_NS)  // level0 type for that node
}



object Format extends Enumeration {
  type Format = Value
  val PROVN  = Value("provn")
  val SVG    = Value("svg")
  val PNG    = Value("png")
  val MATLAB = Value("m")
  val CSV    = Value("csv")
  val DOT    = Value("dot")
  val PDF    = Value("pdf")

  def fromMediatype(s: String): Format.Format = {
    s match {
      case "text/provenance-notation" => PROVN
      case "image/svg+xml"            => SVG
      case "application/matlab"       => MATLAB
      case "application/pdf"          => PDF
      case "text/csv"                 => CSV
      case "text/vnd.graphviz"        => DOT
    }
  }



  val inputers=Map(PROVN  -> new ProvNInputer)

  def substParams(s:String, params:Map[String,String]) = {
    params.foldLeft(s){case (old:String,(param:String, value:String)) => old.replace(param, value)}
  }

  val typeVar="%type"
  val levelVar="%level"
  val dateVar="%date"
  val summarypVar="%summaryp"
}

class ProvNInputer extends Inputer {
    def input(in: Input, params:Map[String,String]):Document = {
      parseDocument(in)
    }
    
    def parseDocument (in:Input): Document = {
    		//val stream=new SimpleStreamStats(1000)  //TODO: write an output stream
        val funs=new DocBuilderFunctions()
        funs.reset()
        val db=new DocBuilder(funs)
    	  parseDocument(in,db).asInstanceOf[DocBuilder].document
    }


    def toBufferedSource(in:Input) = {
    		in match {
    		case StandardInput() => io.Source.fromInputStream(System.in)
    		case FileInput(f:File) => io.Source.fromFile(f)
    		case StreamInput(is:java.io.InputStream) => io.Source.fromInputStream(is)	
    		}
    }
    
    def parseDocument (in:Input, str: ProvStream): ProvStream = {
    		parseDocument(in,str,true)
    }

    def parseDocument (in:Input, str: ProvStream, nsFlag: Boolean): ProvStream = {
      val ns=new Namespace
      ns.addKnownNamespaces()
      if (nsFlag) {
        ns.register("t", "http://openprovenance.org/summary/types#")
        ns.register("sum",  "http://openprovenance.org/summary/ns#")  // TODO: QUICK HACK
      }
      ns.register("provext", "http://openprovenance.org/prov/extension#");


      val actions=new MyActions()
      val actions2=new MyActions2()

      actions2.docns=ns
      actions2.bun_ns=None
      actions2.next=str


      val bufferedSource=toBufferedSource(in)

      val p=new MyParser(bufferedSource.mkString,actions2,actions)
      val doc =p.document.run() match {
        case Success(result) => str
        case Failure(e: ParseError) => println("Expression is not valid: " + p.formatError(e)); throw new RuntimeException
        case Failure(e) => println("Unexpected error during parsing run: " + e.printStackTrace); throw e
      }

      Namespace.withThreadNamespace(ns)
      doc
    }

  

}
class ProvNOutputer extends Outputer {
    def output(d:Indexing, out: Output, params: Map[String,String]) = {
      output(d.document,out, params)
    }
        
    def output(d:Document, out: Output, params: Map[String,String]) = {
      out match {
        case StandardOutput() => {
            val sb=new StringBuilder
            d.toNotation(sb)
            println(sb)
        }
        case StreamOutput(os) => {
            val sb=new StringBuilder
            val bw=new BufferedWriter(new OutputStreamWriter(os))
            d.toNotation(sb)
            bw.append(sb)
            bw.close()
        }
        case FileOutput(f:File) => {
            val newFileName=Format.substParams(f.toString,params)
            val bw=new BufferedWriter(new FileWriter(newFileName))
            val sb=new StringBuilder
            d.toNotation(sb)
            bw.append(sb)
            bw.close()
        }
      }
    }
}


