package org.openprovenance.prov.scala.streaming

import java.io.{File, PrintWriter}
import java.time.LocalDateTime
import org.openprovenance.prov.model.Namespace
import org.openprovenance.prov.scala.immutable._
import org.openprovenance.prov.scala.nf.DocumentProxyInterface

import scala.collection.immutable.Queue

class SimpleStreamStats(var threshold:Int=10000) extends ProvStream {
    var count=0;
    var times: Queue[LocalDateTime]=Queue()
    
    def processTime(count: Int, time: LocalDateTime): Unit = {
        times = times.enqueue(LocalDateTime.now())
    }

    val postStatement: Statement => Unit = (s: Statement) => {
        count=count+1
        if (count == threshold) {
            processTime(count, LocalDateTime.now())
          
          count=0;
        }
    }
    
    val postStartDocument: Namespace => Unit = (ns: Namespace) => {
      
    }
    val postEndDocument: () => Unit = () => {
        processTime(count, LocalDateTime.now())
        times.foreach { println(_) }
    }
    
    val postStartBundle: (QualifiedName, Namespace) => Unit = (qn: QualifiedName, ns: Namespace) => {
      
    }
    val postEndBundle: () => Unit = () => {
      
    }
}

class SimpleStreamStatsPrint(thresh:Int, out: PrintWriter) extends SimpleStreamStats(thresh) {
    override def processTime(count: Int, time: LocalDateTime): Unit = {
      out.println(time.toString())
      out.flush()
    }
    override val postEndDocument: () => Unit = () => {
        processTime(count, LocalDateTime.now())
        out.close()
    }
    

}


class Tee (val s1: ProvStream, val s2: ProvStream) extends ProvStream {

    val postStatement: Statement => Unit = (s: Statement) => {
        s1.postStatement(s)
        s2.postStatement(s)
    }
    
    val postStartDocument: Namespace => Unit = (ns: Namespace) => {
      s1.postStartDocument(ns)
      s2.postStartDocument(ns)
    }
    
    val postEndDocument: () => Unit = () => {
       s1.postEndDocument()
       s2.postEndDocument()      
    }
    
    val postStartBundle: (QualifiedName, Namespace) => Unit = (qn: QualifiedName, ns: Namespace) => {
      s1.postStartBundle(qn,ns)
      s2.postStartBundle(qn,ns)       
    }
    
    val postEndBundle: () => Unit = () => {
        s1.postEndBundle()
        s2.postEndBundle()
    }
}


final class DocBuilderFunctions (
  var doc_statementOrBundles:Seq[StatementOrBundle] = Seq(),
  var doc_ns:Option[Namespace]=None,

  var bun_ns:Option[Namespace]=None,
  var bun_id:Option[QualifiedName]=None,
  var bun_statements:Option[Seq[Statement]]=None) {

  def reset(): Unit = {
    doc_statementOrBundles=Seq()
    doc_ns=None
    bun_ns=None
    bun_id=None
    bun_statements=None
  }



  val postStatement: Statement => Unit = (s: Statement) => {
    (bun_id,bun_statements) match {
      case (None,None) =>  doc_statementOrBundles = doc_statementOrBundles :+ s
      case (Some(_),Some(statements))=>  bun_statements = Some(statements :+ s)
      case _ => ???
    }
  }
  val postBundle: Bundle => Unit = (b: Bundle) => {
    (bun_id,bun_statements) match {
      case (None,None) =>  doc_statementOrBundles = doc_statementOrBundles :+ b
      case (Some(_),Some(_))=>  throw new BundleNotClosedException
      case _ => ???
    }

  }
  val postStartDocument: Namespace => Unit = (anamespace: Namespace) => {
    doc_ns=Some(anamespace)
  }

  val postEndDocument: () => Unit = () => {
  }


  val postStartBundle: (QualifiedName, Namespace) => Unit = (qn: QualifiedName, ns: Namespace) => {
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

  val postEndBundle: () => Unit = () => {
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

final class DocBuilder(val funs:DocBuilderFunctions) extends ProvStream {

  val postStatement: Statement => Unit = funs.postStatement

  val postBundle: Bundle => Unit = funs.postBundle

  val postStartDocument: Namespace => Unit = funs.postStartDocument

  val postEndDocument: () => Unit = funs.postEndDocument

  val postStartBundle: (QualifiedName, Namespace) => Unit = funs.postStartBundle

  val postEndBundle: () => Unit = funs.postEndBundle
  
  def document (): Document = funs.document()

}


final class NFBuilder(documentProxyFactory: org.openprovenance.prov.scala.nf.DocumentProxyFactory) extends ProvStream {
  var nf_doc:org.openprovenance.prov.scala.nf.DocumentProxyInterface=null
  var doc_ns:Option[Namespace]=None

  var bun_ns:Option[Namespace]=None
  var bun_id:Option[QualifiedName]=None
  var bun_statements:Option[Set[Statement]]=None

  
  
  val postStatement: Statement => Unit = (s: Statement) => {
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
  val postStartDocument: Namespace => Unit = (anamespace: Namespace) => {
    doc_ns=Some(anamespace)
    nf_doc= documentProxyFactory.make(anamespace)
  }

  val postEndDocument: () => Unit = () => {
  }
  

  val postStartBundle: (QualifiedName, Namespace) => Unit = (qn: QualifiedName, ns: Namespace) => {
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

  val postEndBundle: () => Unit = () => {
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
  
  def document (): DocumentProxyInterface = doc_ns match {
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
  def this(f: File, b: Boolean) = {
    this(new PrintWriter(f),b)
  }

  override val postStatement: Statement => Unit = (s: Statement) => {
      pw.println(s.toString())
  }
  val postBundle: Bundle => Unit = (b: Bundle) => {
      pw.println("endBundle")
  }

  override val postStartDocument: Namespace => Unit = (anamespace: Namespace) => {
      pw.println("document")
      Namespace.withThreadNamespace(anamespace)

      val sb=new StringBuilder
      new NamespaceWrapper(anamespace).printNamespace(sb)  // FIXME: currently, setting the namespace here, so that they can be used later
                                                           // but this is not  satisfactory, since each stream may need to have its own namespace.
      pw.println(sb)
      
  }

  override val postEndDocument: () => Unit = () => {
      pw.println("endDocument")
      if (b) pw.close()
  }


  override val postStartBundle: (QualifiedName, Namespace) => Unit = (qn: QualifiedName, ns: Namespace) => {
      pw.println("bundle " + qn.toString())
      
      val sb=new StringBuilder
      new NamespaceWrapper(ns).printNamespace(sb)
      pw.println(sb)
  }

  override val postEndBundle: () => Unit = () => {
    pw.println ("endBundle")
  }
  


}


