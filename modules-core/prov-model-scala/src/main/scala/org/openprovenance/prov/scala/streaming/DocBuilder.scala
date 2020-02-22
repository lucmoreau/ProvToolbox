package org.openprovenance.prov.scala.streaming

import java.io.{File, PrintWriter}
import java.time.LocalDateTime

import org.openprovenance.prov.model.Namespace
import org.openprovenance.prov.scala.immutable._

import scala.collection.immutable.Queue

class SimpleStreamStats(var threshold:Int=10000) extends ProvStream {
    var count=0;
    var times: Queue[LocalDateTime]=Queue()
    
    def processTime(count: Int, time: LocalDateTime): Unit = {
        times = times.enqueue(LocalDateTime.now())
    }

    def postStatement: Statement => Unit = (s: Statement) => {
        count=count+1
        if (count == threshold) {
            processTime(count, LocalDateTime.now())
          
          count=0;
        }
    }
    
    def postStartDocument: Namespace => Unit = (ns: Namespace) => {
      
    }
    def postEndDocument: () => Unit = () => {
        processTime(count, LocalDateTime.now())
        times.foreach { println(_) }
    }
    
    def postStartBundle: (QualifiedName, Namespace) => Unit = (qn: QualifiedName, ns: Namespace) => {
      
    }
    def postEndBundle: () => Unit = () => {
      
    }
}

class SimpleStreamStatsPrint(thresh:Int, out: PrintWriter) extends SimpleStreamStats(thresh) {
    override def processTime(count: Int, time: LocalDateTime) = {
      out.println(time.toString())
      out.flush()
    }
    override def postEndDocument: () => Unit = () => {
        processTime(count, LocalDateTime.now())
        out.close()
    }
    

}


class Tee (val s1: ProvStream, val s2: ProvStream) extends ProvStream {

    def postStatement = (s: Statement) => {
        s1.postStatement(s);
        s2.postStatement(s)
    }
    
    def postStartDocument = (ns: Namespace) => {
      s1.postStartDocument(ns);
      s2.postStartDocument(ns)
    }
    
    def postEndDocument = () => {
       s1.postEndDocument();
       s2.postEndDocument()      
    }
    
    def postStartBundle = (qn: QualifiedName, ns: Namespace) => {
      s1.postStartBundle(qn,ns);
      s2.postStartBundle(qn,ns)       
    }
    
    def postEndBundle = () => {
        s1.postEndBundle();
        s2.postEndBundle()
    }
}

final class DocBuilder extends ProvStream {
  var doc_statementOrBundles:Seq[StatementOrBundle] = Seq()
  var doc_ns:Option[Namespace]=None

  var bun_ns:Option[Namespace]=None
  var bun_id:Option[QualifiedName]=None
  var bun_statements:Option[Seq[Statement]]=None

  
  
  def postStatement: Statement => Unit = (s: Statement) => {
    (bun_id,bun_statements) match {
      case (None,None) =>  doc_statementOrBundles = doc_statementOrBundles :+ s
      case (Some(_),Some(statements))=>  bun_statements = Some(statements :+ s)
      case _ => ???
    }
  }
  def postBundle: Bundle => Unit = (b: Bundle) => {
     (bun_id,bun_statements) match {
      case (None,None) =>  doc_statementOrBundles = doc_statementOrBundles :+ b
      case (Some(_),Some(_))=>  throw new BundleNotClosedException
      case _ => ???
    }
   
  }
  def postStartDocument: Namespace => Unit = (anamespace: Namespace) => {
    doc_ns=Some(anamespace)
  }

  def postEndDocument: () => Unit = () => {
  }
  

  def postStartBundle: (QualifiedName, Namespace) => Unit = (qn: QualifiedName, ns: Namespace) => {
  //  println("start bundle " + qn.toString() + " " + bun_id)
    //println("start bundle " + ns)
    bun_id match {
      case Some(_) => throw new NestedBundleException
      case None =>
    }
    bun_ns=Some(ns)
    ns.setParent(doc_ns.get)
    bun_id=Some(qn)
    bun_statements=Some(Seq())
    
  }

  def postEndBundle: () => Unit = () => {
//    println ("end bundle " + bun_id)
    val bun= (bun_id,bun_ns,bun_statements) match {
      case (Some(id),Some(ns),Some(statements)) => new Bundle(id,statements,ns)
      case _ => throw new BundleNotStartedException
    }
    bun_id=None
    bun_ns=None
    bun_statements=None
    postBundle(bun)
  }
  
  def document (): Document = doc_ns match {
    case Some(ns) => new Document(doc_statementOrBundles,ns)
    case None => throw new DocumentNotStartedException
  }

}


final class NFBuilder(documentProxyFactory: org.openprovenance.prov.scala.nf.DocumentProxyFactory) extends ProvStream {
  var nf_doc:org.openprovenance.prov.scala.nf.DocumentProxyInterface=null
  var doc_ns:Option[Namespace]=None

  var bun_ns:Option[Namespace]=None
  var bun_id:Option[QualifiedName]=None
  var bun_statements:Option[Set[Statement]]=None

  
  
  def postStatement: Statement => Unit = (s: Statement) => {
    (bun_id,bun_statements) match {
      case (None,None) =>  nf_doc= nf_doc.add(s)
      case (Some(_),Some(statements))=>  bun_statements = Some(statements + s)
      case _ => ???
   }
  }
  def postBundle: Bundle => Int = (b: Bundle) => {
     (bun_id,bun_statements) match {
      case (None,None) =>  0 // TODO what do do?
      case (Some(_),Some(_))=>  throw new BundleNotClosedException
      case _ => ???
  }
   
  }
  def postStartDocument: Namespace => Unit = (anamespace: Namespace) => {
    doc_ns=Some(anamespace)
    nf_doc= documentProxyFactory.make(anamespace)
  }

  def postEndDocument: () => Unit = () => {
  }
  

  def postStartBundle=(qn: QualifiedName, ns: Namespace) => {
  //  println("start bundle " + qn.toString() + " " + bun_id)
    //println("start bundle " + ns)
    bun_id match {
      case Some(_) => throw new NestedBundleException
      case None =>
    }
    bun_ns=Some(ns)
    ns.setParent(doc_ns.get)
    bun_id=Some(qn)
    bun_statements=Some(Set())
    
  }

  def postEndBundle=() => {
//    println ("end bundle " + bun_id)
    val bun= (bun_id,bun_ns,bun_statements) match {
      case (Some(id),Some(ns),Some(statements)) => new Bundle(id,statements,ns)
      case _ => throw new BundleNotStartedException
    }
    bun_id=None
    bun_ns=None
    bun_statements=None
    postBundle(bun)
  }
  
  def document () = doc_ns match {
    case Some(ns) => nf_doc
    case None => throw new DocumentNotStartedException
  }

}

class DocumentNotStartedException extends Exception
class BundleNotStartedException extends Exception
class NestedBundleException extends Exception
class BundleNotClosedException extends Exception

class NamespaceWrapper(val namespace:Namespace) extends HasNamespace {
  
}

class FileStreamer (pw: PrintWriter, b: Boolean) extends ProvStream {
  def this(f: File, b: Boolean) {
    this(new PrintWriter(f),b)
  }
  
  def postStatement = (s: Statement) => {
      pw.println(s.toString())
  }
  def postBundle = (b: Bundle) => {      
      pw.println("endBundle")
  }
  
  def postStartDocument = (anamespace: Namespace) => {
      pw.println("document")
      Namespace.withThreadNamespace(anamespace)

      val sb=new StringBuilder
      new NamespaceWrapper(anamespace).printNamespace(sb)  // FIXME: currently, setting the namespace here, so that they can be used later
                                                           // but this is not  satisfactory, since each stream may need to have its own namespace.
      pw.println(sb)
      
  }

  def postEndDocument = () => {
      pw.println("endDocument")
      if (b) pw.close()
  }
  

  def postStartBundle=(qn: QualifiedName, ns: Namespace) => {
      pw.println("bundle " + qn.toString())
      
      val sb=new StringBuilder
      new NamespaceWrapper(ns).printNamespace(sb)
      pw.println(sb)
  }

  def postEndBundle=() => {
    pw.println ("endBundle")
  }
  


}


