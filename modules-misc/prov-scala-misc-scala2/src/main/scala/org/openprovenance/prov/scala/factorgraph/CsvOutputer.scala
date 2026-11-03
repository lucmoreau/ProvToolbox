package org.openprovenance.prov.scala.factorgraph

import java.io._

import org.apache.commons.lang3.StringEscapeUtils.escapeCsv
import org.openprovenance.prov.model.Namespace
import org.openprovenance.prov.scala.immutable._
import org.openprovenance.prov.scala.interop._

import scala.jdk.CollectionConverters._

object CsvOutputer {
  val pf: ProvFactory =ProvFactory.pf
  val prov_time: QualifiedName       = pf.getName.newProvQualifiedName("time").asInstanceOf[QualifiedName]
  val prov_endTime: QualifiedName    = pf.getName.newProvQualifiedName("endTime").asInstanceOf[QualifiedName]
  val prov_generation: QualifiedName = pf.getName.newProvQualifiedName("generation").asInstanceOf[QualifiedName]
  val prov_usage: QualifiedName      = pf.getName.newProvQualifiedName("usage").asInstanceOf[QualifiedName]
  
}

class CsvOutputer extends Outputer {
  import CsvOutputer._
  
    def output(d:Document, out: Output, params: Map[String,String]): Unit = {
      out match {
        case StandardOutput() => {
        	throw new UnsupportedOperationException
        }
        case FileOutput(f:File) => {
          toCSV(d,new FileWriter(f),params)
        }
        case StreamOutput(os) => {
            val bw=new BufferedWriter(new OutputStreamWriter(os))
            toCSV(d,bw,params)
            bw.close()
        }
      }
    }
    
    def output(d:Indexing, out: Output, params: Map[String,String]): Unit = {
      out match {
        case StandardOutput() =>
          throw new UnsupportedOperationException
        case FileOutput(f:File) =>
          toCSV(d.document(),new FileWriter(f),params)
        case StreamOutput(_) => throw new UnsupportedOperationException
      }
    }
    

    
    def processAttributes(statement: Statement, columns: Seq[QualifiedName], out:Writer): Unit = {
        	val attrs=statement.getAttributes
        	val others=statement.asInstanceOf[HasOther].other
          columns.foreach {
            case ProvFactory.pf.prov_label => {
              val labels = statement.asInstanceOf[HasLabel].label
              if (labels.isEmpty) {
                out.write(", ")
              } else {
                out.write(", " + escapeCsv(labels.map(l => l.toString).mkString("[", ", ", "]")))
              }
            }
            case ProvFactory.pf.prov_type => out.write(", " + escapeCsv(statement.asInstanceOf[HasType].typex.map(t => t.valueToNotationString()).mkString("[", ", ", "]")))
            case `prov_time` => {
              statement match {
                case act: Activity => act.startTime match {
                  case None => out.write(", ")
                  case Some(t) => out.write(", " + escapeCsv(t.toString))
                }
                case ht: HasTime => ht.time match {
                  case None => out.write(", ")
                  case Some(t) => out.write(", " + escapeCsv(t.toString))
                }
                case _ => out.write(", ")
              }
            }
            case `prov_endTime` => {
              statement match {
                case act: Activity => act.endTime match {
                  case None => out.write(", ")
                  case Some(t) => out.write(", " + escapeCsv(t.toString))
                }
                case _ => out.write(", ")
              }
            }
            case `prov_generation` => {
              statement match {
                case der: WasDerivedFrom => if (der.generation != null) {
                  out.write(", " + escapeCsv(der.generation.toString))
                } else {
                  out.write(",")
                }
                case _ => out.write(", ")
              }
            }
            case `prov_usage` => {
              statement match {
                case der: WasDerivedFrom => if (der.usage != null) {
                  out.write(", " + escapeCsv(der.usage.toString))
                } else {
                  out.write(",")
                }
                case _ => out.write(", ")
              }
            }
            case column => others.get(column) match {
              case Some(values) => out.write(", " + escapeCsv(values.map(o => o.valueToNotationString()).mkString("[", ", ", "]")))
              case None => out.write(", ")
            }
          }
        }
    
    def getCause2(r: Relation): QualifiedName = {
    		r match {
    		case r:WasAssociatedWith => r.plan
    		case r:WasStartedBy      => r.starter
    		case r:WasEndedBy        => r.ender
    		case r:ActedOnBehalfOf   => r.activity
    		case r:WasDerivedFrom    => r.activity
    		case _                   => null
    		}
    }
    
    def toCSV(d:Document, out:Writer, params:Map[String,String]): Unit = {
      val statements=d.statements().toSet
      
      val attrs=statements.flatMap(x => x.getAttributes)
      
      Namespace.withThreadNamespace(d.namespace)
      
      val columns=Seq(prov_time, prov_endTime) ++ attrs.map(o => o.elementName).toSeq ++ Seq(prov_generation,prov_usage)
        
      out.write("# kind, id, ns, effect, cause, other")      
      out.write(columns.mkString(", ", ", ", "\n"))
            
      d.namespace.getNamespaces.asScala.foreach{case (uri:String,pre:String) => 
        out.write("PREFIX, ")
        out.write(pre)
        out.write(", ")
        out.write(escapeCsv(uri))
        out.write("\n")
      }
      
      
      
      statements.foreach{statement => 
        out.write(statement.getKind.toString)
        out.write(", ")
        val id=statement.id
        if (id!=null) {
          out.write(statement.id.toString)
        }
        out.write(", ")
        out.write(", ")
        
        
        statement match {
          case rel:Relation =>  {
            if (rel.getEffect!=null) out.write(rel.getEffect.toString)
            out.write(", ")
            if (rel.getCause!=null)  out.write(rel.getCause.toString)
            out.write(", ")
            val cause2=getCause2(rel)
            if (cause2!=null) out.write(cause2.toString)
          }
          case _ => out.write(", , ")
        }

        processAttributes(statement, columns, out)
        
        out.write("\n")}
        out.close()
      
    }
    
}
