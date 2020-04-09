
package org.openprovenance.prov.scala

import org.openprovenance.prov.model.Namespace
import org.openprovenance.prov.scala.immutable._
import org.openprovenance.prov.scala.streaming.{DocBuilder, DocBuilderFunctions}
import org.parboiled2.ParseError
import org.scalatest._

import scala.util.{Failure, Success}


class ParserSpec extends FlatSpec with Matchers {
  val EX_NS="http://example.org/"
  val ipf=new org.openprovenance.prov.scala.immutable.ProvFactory
  val xsd_string=QualifiedName(ipf.xsd_string)
  val prov_qualified_name=QualifiedName(ipf.prov_qualified_name)

  def q(local: String) = {
    new QualifiedName("ex",local,EX_NS)
  }

  val actions=new MyActions()
  val actions2=new MyActions2()
  val funs=new DocBuilderFunctions()


  def doCheckDocument (s: String, doc: Document): Boolean = {
    funs.reset()
    val docBuilder: DocBuilder =new DocBuilder(funs)
    actions2.bun_ns=None
    actions2.next=docBuilder
    val p=new MyParser(s,null)
    p.document.run() match {
      case Success(result) => p.getNext().asInstanceOf[DocBuilder].document==doc
      case Failure(e: ParseError) => false
      case Failure(e) =>false
    }
  }

  def doCheckEntity (s: String, ns: Namespace, ent: Entity): Boolean = {
    funs.reset()
    val docBuilder: DocBuilder =new DocBuilder(funs)
    actions2.docns=ns
    actions2.bun_ns=None
    actions2.next=docBuilder

    val p=new MyParser(s,actions2,actions)
    p.entity.run() match {
      case Success(result) => Namespace.withThreadNamespace(ns); println("Success " + result); result==ent
      case Failure(e: ParseError) => println("Error 1 " + p.formatError(e)); false
      case Failure(e) =>println("Error 2 " + e); false
    }
  }

  def doCheckActivity (s: String, ns: Namespace, ent: Activity): Boolean = {
    funs.reset()
    val docBuilder: DocBuilder =new DocBuilder(funs)
    actions2.docns=ns
    actions2.bun_ns=None
    actions2.next=docBuilder
    val p=new MyParser(s,actions2,actions)
    p.activity.run() match {
      case Success(result) => Namespace.withThreadNamespace(ns); println("Success " + result); result==ent
      case Failure(e: ParseError) => println("Error 1 " + p.formatError(e)); e.printStackTrace(); false
      case Failure(e) =>println("Error 2 " + e); e.printStackTrace(); false
    }
  }

  

  "An entity" should "parse" in {
    val ns=new Namespace
    ns.addKnownNamespaces()
    ns.register("ex", EX_NS)

    doCheckEntity("entity(ex:e1)", ns, new Entity(q("e1"),Set(),Set(),None, Set(),Map())) should be (true)
    doCheckEntity("entity(ex:e1, [])", ns, new Entity(q("e1"),Set(),Set(),None, Set(),Map())) should be (true) //TODO
    doCheckEntity("entity ( ex:e1 ) ", ns, new Entity(q("e1"),Set(),Set(),None, Set(),Map())) should be (true)
    doCheckEntity("entity ( ex:e1, [ ] ) ", ns, new Entity(q("e1"),Set(),Set(),None, Set(),Map())) should be (true)  //TODO: not supported yet
    doCheckEntity("""entity ( ex:e1, [prov:label="hello" ] ) """, ns, new Entity(q("e1"),Set(new LangString("hello", None)),Set(),None, Set(),Map())) should be (true)
    doCheckEntity("""entity ( ex:e1, [prov:label="hello", prov:value="val" %% xsd:string] ) """, 
                  ns, 
                  new Entity(q("e1"),Set(new LangString("hello", None)),Set(),Some(new Value(xsd_string,"val")),Set(),Map())) should be (true)

    doCheckEntity("""entity ( ex:e1, [prov:label="hello", prov:value="val" %% xsd:string, prov:type='ex:Foo'] ) """, 
                  ns, 
                  new Entity(q("e1"),Set(new LangString("hello", None)),Set(new Type(prov_qualified_name,q("Foo"))),Some(new Value(xsd_string,"val")),Set(),Map())) should be (true)
    doCheckEntity("""entity ( ex:e1, [prov:label="hello", prov:value="val" %% xsd:string, prov:type='ex:Foo', prov:location="liege"] ) """, 
                  ns, 
                  new Entity(q("e1"),Set(new LangString("hello", None)),Set(new Type(prov_qualified_name,q("Foo"))),Some(new Value(xsd_string,"val")),Set(new Location(xsd_string,"liege")),Map())) should be (true)

    doCheckEntity("""entity ( ex:e1, [prov:label="hello", prov:value="val" %% xsd:string, prov:type='ex:Foo', prov:location="liege",ex:attr="whatever1",ex:attr="whatever2"] ) """, 
                  ns, 
                  new Entity(q("e1"),
                             Set(new LangString("hello", None)),
                             Set(new Type(prov_qualified_name,q("Foo"))),
                             Some(new Value(xsd_string,"val")),
                             Set(new Location(xsd_string,"liege")),
                             Map((q("attr"),Set(new Other(q("attr"),xsd_string,"whatever1"), new Other(q("attr"),xsd_string,"whatever2")))))) should be (true)

  }
  

  "An actviity" should "parse" in {
    val ns=new Namespace
    ns.addKnownNamespaces()
    ns.register("ex", EX_NS)

   
    doCheckActivity("""activity ( ex:e1, [prov:label="hello", prov:type='ex:Foo', prov:location="liege",ex:attr="whatever1",ex:attr="whatever2"] ) """, 
                  ns, 
                  new Activity(q("e1"), None,None,
                             Set(new LangString("hello", None)),
                             Set(new Type(prov_qualified_name,q("Foo"))),
                             Set(new Location(xsd_string,"liege")),
                             Map((q("attr"),Set(new Other(q("attr"),xsd_string,"whatever1"), new Other(q("attr"),xsd_string,"whatever2")))))) should be (true)

  }

}
