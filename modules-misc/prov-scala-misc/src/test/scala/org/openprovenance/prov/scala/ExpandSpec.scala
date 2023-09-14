package org.openprovenance.prov.scala
import java.util.LinkedList

import org.openprovenance.prov.model.Namespace
import org.openprovenance.prov.scala.immutable.ProvFactory.pf
import org.openprovenance.prov.scala.immutable._
import org.openprovenance.prov.scala.streaming.{DocBuilder, DocBuilderFunctions}
import org.openprovenance.prov.scala.template.Expander
import org.openprovenance.prov.template.Bindings
import org.openprovenance.prov.template.ExpandUtil.{LABEL, TMPL_NS, TMPL_PREFIX, VAR_NS}
import org.scalatest._

import scala.util.Success

class ExpandSpec extends FlatSpec with Matchers {
  
  val EX_NS="http://example.org/"
  val EX_PRE="ex"
  
  
  val var_a=pf.newQualifiedName(VAR_NS, "a", "var")
  val var_b=pf.newQualifiedName(VAR_NS, "b", "var")
  val var_c=pf.newQualifiedName(VAR_NS, "c", "var")
  val var_d=pf.newQualifiedName(VAR_NS, "d", "var")
  val var_e=pf.newQualifiedName(VAR_NS, "e", "var")
  val var_ag=pf.newQualifiedName(VAR_NS, "ag", "var")
  val var_pl=pf.newQualifiedName(VAR_NS, "pl", "var")
  val var_label=pf.newQualifiedName(VAR_NS, "label", "var")
  val var_start=pf.newQualifiedName(VAR_NS, "start", "var")
  val var_end=pf.newQualifiedName(VAR_NS, "end", "var")
  val var_time=pf.newQualifiedName(VAR_NS, "time", "var")
  
  val val_e=pf.newQualifiedName(EX_NS, "e", EX_PRE)
  val val_ag=pf.newQualifiedName(EX_NS, "ag", EX_PRE)
  
  val tmpl_label=pf.newQualifiedName(TMPL_NS, LABEL, TMPL_PREFIX)
  
  
  val bind1=new Bindings(pf)
  bind1.addVariable(var_e,val_e)
  bind1.addVariable(var_ag,val_ag)

  val bind2=new Bindings(pf)
  bind2.addVariable(var_e,val_e)
  bind2.addVariable(var_ag,val_ag)
  
  val ll1=new LinkedList[org.openprovenance.prov.model.TypedValue]()
  ll1.add(pf.newAttribute(pf.newQualifiedName(TMPL_NS, "label", TMPL_PREFIX), "Luc@example", pf.getName().XSD_STRING))    
  bind2.addAttribute(var_label, ll1)
  
  val ll2=new LinkedList[org.openprovenance.prov.model.TypedValue]()
  ll2.add(pf.newAttribute(pf.newQualifiedName(TMPL_NS, "time", TMPL_PREFIX), "2012-03-02T10:30:00.000Z", pf.getName().XSD_DATETIME))
  bind2.addAttribute(var_time, ll2)

  def parse(d: String):Document = {
    val actions=new MyActions()
    val actions2=new MyActions2()
    val funs=new DocBuilderFunctions()

    funs.reset()
    val db: DocBuilder =new DocBuilder(funs)
    val ns=new Namespace
    ns.addKnownNamespaces()
    actions2.docns=ns
    actions2.bun_ns=None
    actions2.next=db

    val p=new MyParser(d,actions2,actions)
    val doc =p.document.run() match {
          case Success(result) => db.document
          case _ => ???
    }
    doc
  }
  
  def expand1(d: String,b:Bindings,allUpdatedRequired:Boolean=true,addOrderp:Boolean=true):Document = {
    val doc=parse(d)
    println("parsed doc:\n" + doc)
    val exp=new Expander(allUpdatedRequired,addOrderp)
    val expansion=exp.expander(doc, "ignore", b)
    //println("expansion done")
    println("expansion is:\n" + expansion.toString())
    expansion
  }
  
  "an entity expansion " should " be correct" in {
    
    val doc=expand1("""
            document
             prefix tmpl <http://openprovenance.org/tmpl#>
             prefix var <http://openprovenance.org/var#>
             prefix ex <http://example.org/>
             bundle ex:b123
             entity(var:e)
             endBundle
            endDocument
            """",
            bind1)

doc.statementOrBundle.head.asInstanceOf[Bundle].statement.head match {
 case e:Entity => e.id should be (val_e)
 case _ => ???
}


expand1("""
       document
        prefix tmpl <http://openprovenance.org/tmpl#>
        prefix var <http://openprovenance.org/var#>
        prefix ex <http://example.org/>
        bundle ex:b123
        entity(var:a)
        endBundle
       endDocument
       """",
       bind1).statementOrBundle.head.asInstanceOf[Bundle].statement.toSet shouldBe empty

expand1("""
       document
        prefix tmpl <http://openprovenance.org/tmpl#>
        prefix var <http://openprovenance.org/var#>
        prefix ex <http://example.org/>
        bundle ex:b123
        entity(var:a)
        endBundle
       endDocument
       """",
       bind1,false).statementOrBundle.head.asInstanceOf[Bundle].statement.head match {
     case e:Entity => e.id should be (var_a)
}

expand1("""
       document
        prefix tmpl <http://openprovenance.org/tmpl#>
        prefix var <http://openprovenance.org/var#>
        prefix ex <http://example.org/>
        bundle ex:b5
        entity(var:e,[tmpl:label='var:label',ex:foo="abc"])
        endBundle
       endDocument
       """",
       bind2,false,false) should be  (parse("""
         document
        prefix tmpl <http://openprovenance.org/tmpl#>
        prefix var <http://openprovenance.org/var#>
        prefix ex <http://example.org/>
        bundle ex:b5
        entity(ex:e,[prov:label="Luc@example",ex:foo="abc"])
        endBundle
       endDocument
         """))


    
  }


  "an association expansion " should " be correct" in {
    
    val doc=expand1("""
            document
             prefix tmpl <http://openprovenance.org/tmpl#>
             prefix var <http://openprovenance.org/var#>
             prefix ex <http://example.org/>
             bundle ex:b123
             wasAssociatedWith(var:e,var:ag,-)
             endBundle
            endDocument
            """",
            bind1)
            
    doc.statementOrBundle.head.asInstanceOf[Bundle].statement.head match {
      case waw:WasAssociatedWith => {
              waw.activity should be (val_e)
              waw.agent should be (val_ag)
              waw.plan should be (null)
      }
    }
    
    
     expand1("""
            document
             prefix tmpl <http://openprovenance.org/tmpl#>
             prefix var <http://openprovenance.org/var#>
             prefix ex <http://example.org/>
             bundle ex:b123
             wasAssociatedWith(var:e_NO,var:ag,-)
             endBundle
            endDocument
            """",
            bind1).statementOrBundle.head.asInstanceOf[Bundle].statement shouldBe empty
            
     expand1("""
            document
             prefix tmpl <http://openprovenance.org/tmpl#>
             prefix var <http://openprovenance.org/var#>
             prefix ex <http://example.org/>
             bundle ex:b123
             wasAssociatedWith(var:e,var:ag_NO,-)
             endBundle
            endDocument
            """",
            bind1).statementOrBundle.head.asInstanceOf[Bundle].statement shouldBe empty
            
     expand1("""
            document
             prefix tmpl <http://openprovenance.org/tmpl#>
             prefix var <http://openprovenance.org/var#>
             prefix ex <http://example.org/>
             bundle ex:b123
             wasAssociatedWith(var:e,var:ag,var:e)
             endBundle
            endDocument
            """",
            bind1).statementOrBundle.head.asInstanceOf[Bundle].statement.head match {
         case waw:WasAssociatedWith => {
              waw.activity should be (val_e)
              waw.agent should be (val_ag)
              waw.plan should be (val_e)
         }      
     }
  
    
  }

  
  
  
  
  
  
  
  
  
  
  "a generation expansion " should " be correct" in {
    
    expand1("""
            document
             prefix tmpl <http://openprovenance.org/tmpl#>
             prefix var <http://openprovenance.org/var#>
             prefix ex <http://example.org/>
             bundle ex:b123
             wasGeneratedBy(var:e,var:ag,-)
             endBundle
            endDocument
            """",
            bind1).statementOrBundle.head.asInstanceOf[Bundle].statement.head match {
      case wgb:WasGeneratedBy => {
              wgb.activity should be (val_ag)
              wgb.entity should be (val_e)
              wgb.time should be (None)
      }
    }
    
    
     expand1("""
            document
             prefix tmpl <http://openprovenance.org/tmpl#>
             prefix var <http://openprovenance.org/var#>
             prefix ex <http://example.org/>
             bundle ex:b123
             wasGeneratedBy(var:e_NO,var:ag,-)
             endBundle
            endDocument
            """",
            bind1).statementOrBundle.head.asInstanceOf[Bundle].statement shouldBe empty
            
     expand1("""
            document
             prefix tmpl <http://openprovenance.org/tmpl#>
             prefix var <http://openprovenance.org/var#>
             prefix ex <http://example.org/>
             bundle ex:b123
             wasGeneratedBy(var:e,var:ag_NO,-)
             endBundle
            endDocument
            """",
            bind1).statementOrBundle.head.asInstanceOf[Bundle].statement shouldBe empty
            
     expand1("""
            document
             prefix tmpl <http://openprovenance.org/tmpl#>
             prefix var <http://openprovenance.org/var#>
             prefix ex <http://example.org/>
             bundle ex:b1234
             wasGeneratedBy(var:e,var:ag,2012-03-02T10:30:00.000Z)
             endBundle
            endDocument
            """",
            bind1).statementOrBundle.head.asInstanceOf[Bundle].statement.head match {
         case wgb:WasGeneratedBy => {
              wgb.activity should be (val_ag)
              wgb.entity should be (val_e)
              wgb.time should be (Some(pf.newISOTime("2012-03-02T10:30:00.000Z")))
         }    
     }
     
     
      expand1("""
            document
             prefix tmpl <http://openprovenance.org/tmpl#>
             prefix var <http://openprovenance.org/var#>
             prefix ex <http://example.org/>
           bundle ex:b1235
             wasGeneratedBy(var:e,var:ag,-,[ex:foo="abc",tmpl:time='var:time',tmpl:label='var:label'])
             endBundle
            endDocument
            """",
            bind2,false,false) should be (parse("""
              document
             prefix tmpl <http://openprovenance.org/tmpl#>
             prefix var <http://openprovenance.org/var#>
             prefix ex <http://example.org/>
             bundle ex:b1235
              wasGeneratedBy(ex:e,ex:ag,2012-03-02T10:30:00.000Z,[prov:label="Luc@example",ex:foo="abc"])
             endBundle
            endDocument
              """))
     
  
    
  }

  

  
  
  
  "an invalidation expansion " should " be correct" in {
    
    expand1("""
            document
             prefix tmpl <http://openprovenance.org/tmpl#>
             prefix var <http://openprovenance.org/var#>
             prefix ex <http://example.org/>
             bundle ex:b123
             wasInvalidatedBy(var:e,var:ag,-)
             endBundle
            endDocument
            """",
            bind1).statementOrBundle.head.asInstanceOf[Bundle].statement.head match {
      case wib:WasInvalidatedBy => {
              wib.activity should be (val_ag)
              wib.entity should be (val_e)
              wib.time should be (None)
      }
    }
    
    
     expand1("""
            document
             prefix tmpl <http://openprovenance.org/tmpl#>
             prefix var <http://openprovenance.org/var#>
             prefix ex <http://example.org/>
             bundle ex:b123
             wasInvalidatedBy(var:e_NO,var:ag,-)
             endBundle
            endDocument
            """",
            bind1).statementOrBundle.head.asInstanceOf[Bundle].statement shouldBe empty
            
     expand1("""
            document
             prefix tmpl <http://openprovenance.org/tmpl#>
             prefix var <http://openprovenance.org/var#>
             prefix ex <http://example.org/>
             bundle ex:b123
             wasInvalidatedBy(var:e,var:ag_NO,-)
             endBundle
            endDocument
            """",
            bind1).statementOrBundle.head.asInstanceOf[Bundle].statement shouldBe empty
            
     expand1("""
            document
             prefix tmpl <http://openprovenance.org/tmpl#>
             prefix var <http://openprovenance.org/var#>
             prefix ex <http://example.org/>
             bundle ex:b1234
             wasInvalidatedBy(var:e,var:ag,2012-03-02T10:30:00.000Z)
             endBundle
            endDocument
            """",
            bind1).statementOrBundle.head.asInstanceOf[Bundle].statement.head match {
         case wib:WasInvalidatedBy => {
              wib.activity should be (val_ag)
              wib.entity should be (val_e)
              wib.time should be (Some(pf.newISOTime("2012-03-02T10:30:00.000Z")))
         }    
     }
     
     
      expand1("""
            document
             prefix tmpl <http://openprovenance.org/tmpl#>
             prefix var <http://openprovenance.org/var#>
             prefix ex <http://example.org/>
           bundle ex:b1235
             wasInvalidatedBy(var:e,var:ag,-,[ex:foo="abc",tmpl:time='var:time',tmpl:label='var:label'])
             endBundle
            endDocument
            """",
            bind2,false,false) should be (parse("""
              document
             prefix tmpl <http://openprovenance.org/tmpl#>
             prefix var <http://openprovenance.org/var#>
             prefix ex <http://example.org/>
             bundle ex:b1235
              wasInvalidatedBy(ex:e,ex:ag,2012-03-02T10:30:00.000Z,[prov:label="Luc@example",ex:foo="abc"])
             endBundle
            endDocument
              """))
     
  
    
  }  
  
 
  
  
  
  "a usage expansion " should " be correct" in {
    
    expand1("""
            document
             prefix tmpl <http://openprovenance.org/tmpl#>
             prefix var <http://openprovenance.org/var#>
             prefix ex <http://example.org/>
             bundle ex:b123
             used(var:e,var:ag,-)
             endBundle
            endDocument
            """",
            bind1).statementOrBundle.head.asInstanceOf[Bundle].statement.head match {
      case usd:Used => {
              usd.activity should be (val_e)
              usd.entity should be (val_ag)
              usd.time should be (None)
      }
    }
    
    
     expand1("""
            document
             prefix tmpl <http://openprovenance.org/tmpl#>
             prefix var <http://openprovenance.org/var#>
             prefix ex <http://example.org/>
             bundle ex:b123
             used(var:e_NO,var:ag,-)
             endBundle
            endDocument
            """",
            bind1).statementOrBundle.head.asInstanceOf[Bundle].statement shouldBe empty
            
     expand1("""
            document
             prefix tmpl <http://openprovenance.org/tmpl#>
             prefix var <http://openprovenance.org/var#>
             prefix ex <http://example.org/>
             bundle ex:b123
             used(var:e,var:ag_NO,-)
             endBundle
            endDocument
            """",
            bind1).statementOrBundle.head.asInstanceOf[Bundle].statement shouldBe empty
            
     expand1("""
            document
             prefix tmpl <http://openprovenance.org/tmpl#>
             prefix var <http://openprovenance.org/var#>
             prefix ex <http://example.org/>
             bundle ex:b1234
             used(var:e,var:ag,2012-03-02T10:30:00.000Z)
             endBundle
            endDocument
            """",
            bind1).statementOrBundle.head.asInstanceOf[Bundle].statement.head match {
         case usd:Used => {
              usd.activity should be (val_e)
              usd.entity should be (val_ag)
              usd.time should be (Some(pf.newISOTime("2012-03-02T10:30:00.000Z")))
         }    
     }
     
     
      expand1("""
            document
             prefix tmpl <http://openprovenance.org/tmpl#>
             prefix var <http://openprovenance.org/var#>
             prefix ex <http://example.org/>
           bundle ex:b1235
             used(var:e,var:ag,-,[ex:foo="abc",tmpl:time='var:time',tmpl:label='var:label'])
             endBundle
            endDocument
            """",
            bind2,false,false) should be (parse("""
              document
             prefix tmpl <http://openprovenance.org/tmpl#>
             prefix var <http://openprovenance.org/var#>
             prefix ex <http://example.org/>
             bundle ex:b1235
              used(ex:e,ex:ag,2012-03-02T10:30:00.000Z,[prov:label="Luc@example",ex:foo="abc"])
             endBundle
            endDocument
              """))
     
  
    
  } 
  
   "a communication/influence/specialization/alternate expansion " should " be correct" in {
     
          
      expand1("""
            document
             prefix tmpl <http://openprovenance.org/tmpl#>
             prefix var <http://openprovenance.org/var#>
             prefix ex <http://example.org/>
           bundle ex:b2001
             wasInformedBy  (var:e,var:ag,[ex:foo="abc",tmpl:label='var:label'])
             wasInfluencedBy(var:e,var:ag,[ex:foo="def",tmpl:label='var:label'])
             specializationOf(var:e,var:ag)
             alternateOf(var:e,var:ag)
             endBundle
            endDocument
            """",
            bind2,false,false) should be (parse("""
              document
             prefix tmpl <http://openprovenance.org/tmpl#>
             prefix var <http://openprovenance.org/var#>
             prefix ex <http://example.org/>
             bundle ex:b2001
              wasInformedBy  (ex:e,ex:ag,[prov:label="Luc@example",ex:foo="abc"])
              wasInfluencedBy(ex:e,ex:ag,[prov:label="Luc@example",ex:foo="def"])
              specializationOf(ex:e,ex:ag)
              alternateOf(ex:e,ex:ag)
             endBundle
            endDocument
              """))
      
      //TODO specialization/alternate with Attributes
      
      
     
   }
   
    "an activity expansion " should " be correct" in {
     
          
      expand1("""
            document
             prefix tmpl <http://openprovenance.org/tmpl#>
             prefix var <http://openprovenance.org/var#>
             prefix ex <http://example.org/>
             bundle ex:b3001
               activity (ex:a1,[ex:foo="abc",tmpl:startTime='var:time',tmpl:label='var:label'])
               activity (ex:a2,[ex:foo="abc",tmpl:startTime='var:time',tmpl:endTime='var:time',tmpl:label='var:label'])
               activity (var:e,[ex:foo="abc",tmpl:endTime='var:time',tmpl:label='var:label'])
             endBundle
            endDocument
            """",
            bind2,false,false) should be (parse("""
              document
             prefix tmpl <http://openprovenance.org/tmpl#>
             prefix var <http://openprovenance.org/var#>
             prefix ex <http://example.org/>
             bundle ex:b3001
               activity (ex:a1,2012-03-02T10:30:00.000Z,-,[ex:foo="abc",prov:label="Luc@example"])
               activity (ex:a2,2012-03-02T10:30:00.000Z,2012-03-02T10:30:00.000Z,[ex:foo="abc",prov:label="Luc@example"])
               activity (ex:e,-,2012-03-02T10:30:00.000Z,[ex:foo="abc",prov:label="Luc@example"])
             endBundle
            endDocument
              """))
      
      
      
     
    }
    
    "an agent expansion " should " be correct" in {
     
          
      expand1("""
            document
             prefix tmpl <http://openprovenance.org/tmpl#>
             prefix var <http://openprovenance.org/var#>
             prefix ex <http://example.org/>
             bundle ex:b4001
               agent (var:e,[ex:foo="abc",tmpl:endTime='var:time',tmpl:label='var:label'])
             endBundle
            endDocument
            """",
            bind2,false,false) should be (parse("""
              document
             prefix tmpl <http://openprovenance.org/tmpl#>
             prefix var <http://openprovenance.org/var#>
             prefix ex <http://example.org/>
             bundle ex:b4001
               agent (ex:e,[ex:foo="abc",prov:label="Luc@example"])
             endBundle
            endDocument
              """))
      
      
      
     
   }
 
    
    "a derivation expansion " should " be correct" in {
     
          
      expand1("""
            document
             prefix tmpl <http://openprovenance.org/tmpl#>
             prefix var <http://openprovenance.org/var#>
             prefix ex <http://example.org/>
             bundle ex:b5001
               wasDerivedFrom (var:e,var:e,[ex:foo="abc",tmpl:endTime='var:time',tmpl:label='var:label'])
               wasDerivedFrom (ex:der1; var:e,var:e, var:ag, -, -, [ex:foo="abc",tmpl:endTime='var:time',tmpl:label='var:label'])
               wasDerivedFrom (ex:der2; var:e,var:e, var:ag, var:ag, var:ag, [ex:foo="abc",tmpl:endTime='var:time',tmpl:label='var:label'])
             endBundle
            endDocument
            """",
            bind2,false,false) should be (parse("""
              document
             prefix tmpl <http://openprovenance.org/tmpl#>
             prefix var <http://openprovenance.org/var#>
             prefix ex <http://example.org/>
             bundle ex:b5001
               wasDerivedFrom (ex:e,ex:e,[ex:foo="abc",prov:label="Luc@example"])
               wasDerivedFrom (ex:der1; ex:e,ex:e, ex:ag, -, -, [ex:foo="abc",prov:label="Luc@example"])
               wasDerivedFrom (ex:der2; ex:e,ex:e, ex:ag, ex:ag, ex:ag, [ex:foo="abc",prov:label="Luc@example"])
             endBundle
            endDocument
              """))
      
      
      
     
   }
 
  
    "a delegation expansion " should " be correct" in {
     
          
      expand1("""
            document
             prefix tmpl <http://openprovenance.org/tmpl#>
             prefix var <http://openprovenance.org/var#>
             prefix ex <http://example.org/>
             bundle ex:b5001
               actedOnBehalfOf (var:e,var:e,-,[ex:foo="abc",tmpl:endTime='var:time',tmpl:label='var:label'])
               actedOnBehalfOf (ex:ao1; var:e,var:e, var:ag, [ex:foo="abc",tmpl:endTime='var:time',tmpl:label='var:label'])
             endBundle
            endDocument
            """",
            bind2,false,false) should be (parse("""
              document
             prefix tmpl <http://openprovenance.org/tmpl#>
             prefix var <http://openprovenance.org/var#>
             prefix ex <http://example.org/>
             bundle ex:b5001
               actedOnBehalfOf (ex:e,ex:e,[ex:foo="abc",prov:label="Luc@example"])
               actedOnBehalfOf (ex:ao1; ex:e,ex:e, ex:ag,[ex:foo="abc",prov:label="Luc@example"])
             endBundle
            endDocument
              """))
      
      
      
     
   }
 
}