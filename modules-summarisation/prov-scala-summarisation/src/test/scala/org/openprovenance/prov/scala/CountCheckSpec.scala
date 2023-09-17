
package org.openprovenance.prov.scala

import org.openprovenance.prov.model.Namespace
import org.openprovenance.prov.scala.immutable._
import org.openprovenance.prov.scala.streaming.{DocBuilder, DocBuilderFunctions}
import org.openprovenance.prov.scala.summary._
import org.openprovenance.prov.scala.summary.types._
import org.parboiled2.ParseError
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.util.{Failure, Success}

class CountCheckSpec extends AnyFlatSpec with Matchers {
  val EX_NS="http://example.org/"
  val ipf=new org.openprovenance.prov.scala.immutable.ProvFactory
  val xsd_string: QualifiedName =QualifiedName(ipf.xsd_string)
  val prov_qualified_name: QualifiedName =QualifiedName(ipf.prov_qualified_name)
  
  val nsBase="http://example.org/summary#"

  def q(local: String): QualifiedName = {
   new QualifiedName("ex",local,EX_NS)
  }
  
  def doCheckDocument (s: String, doc: Document): Boolean = {
      val p=new MyParser(s,null)
      p.document.run() match {
        case Success(result) => p.getNext().asInstanceOf[DocBuilder].document==doc
        case Failure(_: ParseError) => false
        case Failure(_) =>false
      }
  }

  def doCheckEntity (s: String, ns: Namespace, ent: Entity): Boolean = {
    val actions=new MyActions()
    val actions2=new MyActions2()
    val funs=new DocBuilderFunctions()


    val db=new DocBuilder(funs)
    actions2.docns=ns
    actions2.bun_ns=None
    actions2.next=db

    val p=new MyParser(s,actions2,actions)
      p.entity.run() match {
        case Success(result) => Namespace.withThreadNamespace(ns); println("Success " + result); result==ent
        case Failure(e: ParseError) => println("Error " + p.formatError(e)); false
        case Failure(e) =>println("Error " + e); false
      }
  }
  
  def sum(d: String,level0:Level0=new DefaultLevel0,always_with_type_0:Boolean=false): (Indexer, TypePropagator) = {
    val actions=new MyActions()
    val actions2=new MyActions2()
    val funs=new DocBuilderFunctions()


    val db=new DocBuilder(funs)
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
    val ind=new Indexer(doc)
    val s=new TypePropagator(doc.asInstanceOf[Document],false, always_with_type_0, false, true,level0)
    (ind,s)
  }




  "Provenance challenge " should "have correct summary counts" in {
    val ns=new Namespace
    ns.addKnownNamespaces()
    ns.register("ex", EX_NS)
    
    val level0b=new Level0Mapper(Map("http://openprovenance.org/primitives#File" -> "File"), Set(), Set())
    
    
    val level0=Level0Mapper(""" {"mapper":{"http://openprovenance.org/primitives#File":"File"},"ignore":[]} """)

    println(level0.exportToJSon())
    
    val (ind,s)=sum("""
                    document
  prefix prim <http://openprovenance.org/primitives#>
  prefix pc1 <http://www.ipaw.info/pc1/>
  activity(pc1:a1,-,-,[prov:type = 'prim:align_warp', prov:label = "align_warp 1"])
  activity(pc1:a2,-,-,[prov:type = 'prim:align_warp', prov:label = "align_warp 2"])
  activity(pc1:a3,-,-,[prov:type = 'prim:align_warp', prov:label = "align_warp 3"])
  activity(pc1:a4,-,-,[prov:type = 'prim:align_warp', prov:label = "align_warp 4"])
  activity(pc1:a5,-,-,[prov:type = 'prim:reslice', prov:label = "Reslice 1"])
  activity(pc1:a6,-,-,[prov:type = 'prim:reslice', prov:label = "Reslice 2"])
  activity(pc1:a7,-,-,[prov:type = 'prim:reslice', prov:label = "Reslice 3"])
  activity(pc1:a8,-,-,[prov:type = 'prim:reslice', prov:label = "Reslice 4"])
  activity(pc1:a9,-,-,[prov:type = 'prim:softmean', prov:label = "Softmean"])
  activity(pc1:a10,-,-,[prov:type = 'prim:slicer', prov:label = "Slicer 1"])
  activity(pc1:a11,-,-,[prov:type = 'prim:slicer', prov:label = "Slicer 2"])
  activity(pc1:a12,-,-,[prov:type = 'prim:slicer', prov:label = "Slicer 3"])
  activity(pc1:a13,-,-,[prov:type = 'prim:convert', prov:label = "Convert 1"])
  activity(pc1:a14,-,-,[prov:type = 'prim:convert', prov:label = "Convert 2"])
  activity(pc1:a15,-,-,[prov:type = 'prim:convert', prov:label = "Convert 3"])
  entity(pc1:e1,[prov:type = 'prim:File', prov:label = "Reference Image", pc1:url = "http://www.ipaw.info/challenge/reference.img" %% xsd:anyURI])
  entity(pc1:e2,[prov:type = 'prim:File', prov:label = "Reference Header", pc1:url = "http://www.ipaw.info/challenge/reference.hdr" %% xsd:anyURI])
  entity(pc1:e5,[prov:type = 'prim:File', prov:label = "Anatomy I2", pc1:url = "http://www.ipaw.info/challenge/anatomy2.img" %% xsd:anyURI])
  entity(pc1:e6,[prov:type = 'prim:File', prov:label = "Anatomy H2", pc1:url = "http://www.ipaw.info/challenge/anatomy2.hdr" %% xsd:anyURI])
  entity(pc1:e3,[prov:type = 'prim:File', prov:label = "Anatomy I1", pc1:url = "http://www.ipaw.info/challenge/anatomy1.img" %% xsd:anyURI])
  entity(pc1:e4,[prov:type = 'prim:File', prov:label = "Anatomy H1", pc1:url = "http://www.ipaw.info/challenge/anatomy1.hdr" %% xsd:anyURI])
  entity(pc1:e7,[prov:type = 'prim:File', prov:label = "Anatomy I3", pc1:url = "http://www.ipaw.info/challenge/anatomy3.img" %% xsd:anyURI])
  entity(pc1:e8,[prov:type = 'prim:File', prov:label = "Anatomy H3", pc1:url = "http://www.ipaw.info/challenge/anatomy3.hdr" %% xsd:anyURI])
  entity(pc1:e9,[prov:type = 'prim:File', prov:label = "Anatomy I4", pc1:url = "http://www.ipaw.info/challenge/anatomy4.img" %% xsd:anyURI])
  entity(pc1:e10,[prov:type = 'prim:File', prov:label = "Anatomy H4", pc1:url = "http://www.ipaw.info/challenge/anatomy4.hdr" %% xsd:anyURI])
  entity(pc1:e11,[prov:type = 'prim:File', prov:label = "Warp Params1", pc1:url = "http://www.ipaw.info/challenge/warp1.warp" %% xsd:anyURI])
  entity(pc1:e12,[prov:type = 'prim:File', prov:label = "Warp Params2", pc1:url = "http://www.ipaw.info/challenge/warp2.warp" %% xsd:anyURI])
  entity(pc1:e13,[prov:type = 'prim:File', prov:label = "Warp Params3", pc1:url = "http://www.ipaw.info/challenge/warp3.warp" %% xsd:anyURI])
  entity(pc1:e14,[prov:type = 'prim:File', prov:label = "Warp Params4", pc1:url = "http://www.ipaw.info/challenge/warp4.warp" %% xsd:anyURI])
  entity(pc1:e15,[prov:type = 'prim:File', prov:label = "Resliced I1", pc1:url = "http://www.ipaw.info/challenge/resliced1.img" %% xsd:anyURI])
  entity(pc1:e16,[prov:type = 'prim:File', prov:label = "Resliced H1", pc1:url = "http://www.ipaw.info/challenge/resliced1.hdr" %% xsd:anyURI])
  entity(pc1:e17,[prov:type = 'prim:File', prov:label = "Resliced I2", pc1:url = "http://www.ipaw.info/challenge/resliced2.img" %% xsd:anyURI])
  entity(pc1:e18,[prov:type = 'prim:File', prov:label = "Resliced H2", pc1:url = "http://www.ipaw.info/challenge/resliced2.hdr" %% xsd:anyURI])
  entity(pc1:e19,[prov:type = 'prim:File', prov:label = "Resliced I3", pc1:url = "http://www.ipaw.info/challenge/resliced3.img" %% xsd:anyURI])
  entity(pc1:e20,[prov:type = 'prim:File', prov:label = "Resliced H3", pc1:url = "http://www.ipaw.info/challenge/resliced3.hdr" %% xsd:anyURI])
  entity(pc1:e21,[prov:type = 'prim:File', prov:label = "Resliced I4", pc1:url = "http://www.ipaw.info/challenge/resliced4.img" %% xsd:anyURI])
  entity(pc1:e22,[prov:type = 'prim:File', prov:label = "Resliced H4", pc1:url = "http://www.ipaw.info/challenge/resliced4.hdr" %% xsd:anyURI])
  entity(pc1:e23,[prov:type = 'prim:File', prov:label = "Atlas Image", pc1:url = "http://www.ipaw.info/challenge/atlas.img" %% xsd:anyURI])
  entity(pc1:e24,[prov:type = 'prim:File', prov:label = "Atlas Header", pc1:url = "http://www.ipaw.info/challenge/atlas.hdr" %% xsd:anyURI])
  entity(pc1:e25,[prov:type = 'prim:File', prov:label = "Atlas X Slice", pc1:url = "http://www.ipaw.info/challenge/atlas-x.pgm" %% xsd:anyURI])
  entity(pc1:e25p,[prov:type = 'prim:String', prov:label = "slicer param 1", pc1:value = "-x .5" %% xsd:string])
  entity(pc1:e26,[prov:type = 'prim:File', prov:label = "Atlas Y Slice", pc1:url = "http://www.ipaw.info/challenge/atlas-y.pgm" %% xsd:anyURI])
  entity(pc1:e26p,[prov:type = 'prim:String', prov:label = "slicer param 2", pc1:value = "-y .5" %% xsd:string])
  entity(pc1:e27,[prov:type = 'prim:File', prov:label = "Atlas Z Slice", pc1:url = "http://www.ipaw.info/challenge/atlas-z.pgm" %% xsd:anyURI])
  entity(pc1:e27p,[prov:type = 'prim:String', prov:label = "slicer param 3", pc1:value = "-z .5" %% xsd:string])
  entity(pc1:e28,[prov:type = 'prim:File', prov:label = "Atlas X Graphic", pc1:url = "http://www.ipaw.info/challenge/atlas-x.gif" %% xsd:anyURI])
  entity(pc1:e29,[prov:type = 'prim:File', prov:label = "Atlas Y Graphic", pc1:url = "http://www.ipaw.info/challenge/atlas-y.gif" %% xsd:anyURI])
  entity(pc1:e30,[prov:type = 'prim:File', prov:label = "Atlas Z Graphic", pc1:url = "http://www.ipaw.info/challenge/atlas-z.gif" %% xsd:anyURI])
  agent(pc1:ag1,[prov:label = "John Doe"])
  used(pc1:a1,pc1:e3,-,[prov:role = 'prim:img'])
  used(pc1:a1,pc1:e4,-,[prov:role = 'prim:hdr'])
  used(pc1:u3;pc1:a1,pc1:e1,-,[prov:role = 'prim:imgRef'])
  used(pc1:a1,pc1:e2,-,[prov:role = 'prim:hdrRef'])
  used(pc1:a2,pc1:e5,-,[prov:role = 'prim:img'])
  used(pc1:a2,pc1:e6,-,[prov:role = 'prim:hdr'])
  used(pc1:a2,pc1:e1,-,[prov:role = 'prim:imgRef'])
  used(pc1:a2,pc1:e2,-,[prov:role = 'prim:hdrRef'])
  used(pc1:a3,pc1:e7,-,[prov:role = 'prim:img'])
  used(pc1:a3,pc1:e8,-,[prov:role = 'prim:hdr'])
  used(pc1:a3,pc1:e1,-,[prov:role = 'prim:imgRef'])
  used(pc1:a3,pc1:e2,-,[prov:role = 'prim:hdrRef'])
  used(pc1:a4,pc1:e9,-,[prov:role = 'prim:img'])
  used(pc1:a4,pc1:e10,-,[prov:role = 'prim:hdr'])
  used(pc1:a4,pc1:e1,-,[prov:role = 'prim:imgRef'])
  used(pc1:a4,pc1:e2,-,[prov:role = 'prim:hdrRef'])
  used(pc1:a5,pc1:e11,-,[prov:role = 'prim:in'])
  used(pc1:a6,pc1:e12,-,[prov:role = 'prim:in'])
  used(pc1:a7,pc1:e13,-,[prov:role = 'prim:in'])
  used(pc1:a8,pc1:e14,-,[prov:role = 'prim:in'])
  used(pc1:a9,pc1:e15,-,[prov:role = 'prim:i1'])
  used(pc1:a9,pc1:e16,-,[prov:role = 'prim:h1'])
  used(pc1:a9,pc1:e17,-,[prov:role = 'prim:i2'])
  used(pc1:a9,pc1:e18,-,[prov:role = 'prim:h2'])
  used(pc1:a9,pc1:e19,-,[prov:role = 'prim:i3'])
  used(pc1:a9,pc1:e20,-,[prov:role = 'prim:h3'])
  used(pc1:a9,pc1:e21,-,[prov:role = 'prim:i4'])
  used(pc1:a9,pc1:e22,-,[prov:role = 'prim:h4'])
  used(pc1:a10,pc1:e23,-,[prov:role = 'prim:img'])
  used(pc1:a10,pc1:e24,-,[prov:role = 'prim:hdr'])
  used(pc1:a11,pc1:e23,-,[prov:role = 'prim:img'])
  used(pc1:a11,pc1:e24,-,[prov:role = 'prim:hdr'])
  used(pc1:a12,pc1:e23,-,[prov:role = 'prim:img'])
  used(pc1:a12,pc1:e24,-,[prov:role = 'prim:hdr'])
  used(pc1:a10,pc1:e25p,-,[prov:role = 'prim:param'])
  used(pc1:a11,pc1:e26p,-,[prov:role = 'prim:param'])
  used(pc1:a12,pc1:e27p,-,[prov:role = 'prim:param'])
  used(pc1:a13,pc1:e25,-,[prov:role = 'prim:in'])
  used(pc1:a14,pc1:e26,-,[prov:role = 'prim:in'])
  used(pc1:a15,pc1:e27,-,[prov:role = 'prim:in'])
  wasGeneratedBy(pc1:wgb1;pc1:e11,pc1:a1,-,[prov:role = 'prim:out'])
  wasGeneratedBy(pc1:e12,pc1:a2,-,[prov:role = 'prim:out'])
  wasGeneratedBy(pc1:e13,pc1:a3,-,[prov:role = 'prim:out'])
  wasGeneratedBy(pc1:e14,pc1:a4,-,[prov:role = 'prim:out'])
  wasGeneratedBy(pc1:e15,pc1:a5,-,[prov:role = 'prim:img'])
  wasGeneratedBy(pc1:e16,pc1:a5,-,[prov:role = 'prim:hdr'])
  wasGeneratedBy(pc1:e17,pc1:a6,-,[prov:role = 'prim:img'])
  wasGeneratedBy(pc1:e18,pc1:a6,-,[prov:role = 'prim:hdr'])
  wasGeneratedBy(pc1:e19,pc1:a7,-,[prov:role = 'prim:img'])
  wasGeneratedBy(pc1:e20,pc1:a7,-,[prov:role = 'prim:hdr'])
  wasGeneratedBy(pc1:e21,pc1:a8,-,[prov:role = 'prim:img'])
  wasGeneratedBy(pc1:e22,pc1:a8,-,[prov:role = 'prim:hdr'])
  wasGeneratedBy(pc1:e23,pc1:a9,-,[prov:role = 'prim:img'])
  wasGeneratedBy(pc1:e24,pc1:a9,-,[prov:role = 'prim:hdr'])
  wasGeneratedBy(pc1:e25,pc1:a10,-,[prov:role = 'prim:out'])
  wasGeneratedBy(pc1:e26,pc1:a11,-,[prov:role = 'prim:out'])
  wasGeneratedBy(pc1:e27,pc1:a12,-,[prov:role = 'prim:out'])
  wasGeneratedBy(pc1:e28,pc1:a13,2012-10-26T09:58:08.407+01:00,[prov:role = 'prim:out'])
  wasGeneratedBy(pc1:e29,pc1:a14,2012-10-26T09:58:08.407+01:00,[prov:role = 'prim:out'])
  wasGeneratedBy(pc1:e30,pc1:a15,2012-10-26T09:58:08.407+01:00,[prov:role = 'prim:out'])
  wasDerivedFrom(pc1:e11, pc1:e1, pc1:a1, pc1:wgb1, pc1:u3)
  wasDerivedFrom(pc1:e11, pc1:e2)
  wasDerivedFrom(pc1:e11, pc1:e3)
  wasDerivedFrom(pc1:e11, pc1:e4)
  wasDerivedFrom(pc1:e12, pc1:e1)
  wasDerivedFrom(pc1:e12, pc1:e2)
  wasDerivedFrom(pc1:e12, pc1:e5)
  wasDerivedFrom(pc1:e12, pc1:e6)
  wasDerivedFrom(pc1:e13, pc1:e1)
  wasDerivedFrom(pc1:e13, pc1:e2)
  wasDerivedFrom(pc1:e13, pc1:e7)
  wasDerivedFrom(pc1:e13, pc1:e8)
  wasDerivedFrom(pc1:e14, pc1:e1)
  wasDerivedFrom(pc1:e14, pc1:e2)
  wasDerivedFrom(pc1:e14, pc1:e9)
  wasDerivedFrom(pc1:e14, pc1:e10)
  wasDerivedFrom(pc1:e15, pc1:e11)
  wasDerivedFrom(pc1:e16, pc1:e11)
  wasDerivedFrom(pc1:e17, pc1:e12)
  wasDerivedFrom(pc1:e18, pc1:e12)
  wasDerivedFrom(pc1:e19, pc1:e13)
  wasDerivedFrom(pc1:e20, pc1:e13)
  wasDerivedFrom(pc1:e21, pc1:e14)
  wasDerivedFrom(pc1:e22, pc1:e14)
  wasDerivedFrom(pc1:e23, pc1:e15)
  wasDerivedFrom(pc1:e23, pc1:e16)
  wasDerivedFrom(pc1:e23, pc1:e17)
  wasDerivedFrom(pc1:e23, pc1:e18)
  wasDerivedFrom(pc1:e23, pc1:e19)
  wasDerivedFrom(pc1:e23, pc1:e20)
  wasDerivedFrom(pc1:e23, pc1:e21)
  wasDerivedFrom(pc1:e23, pc1:e22)
  wasDerivedFrom(pc1:e24, pc1:e15)
  wasDerivedFrom(pc1:e24, pc1:e16)
  wasDerivedFrom(pc1:e24, pc1:e17)
  wasDerivedFrom(pc1:e24, pc1:e18)
  wasDerivedFrom(pc1:e24, pc1:e19)
  wasDerivedFrom(pc1:e24, pc1:e20)
  wasDerivedFrom(pc1:e24, pc1:e21)
  wasDerivedFrom(pc1:e24, pc1:e22)
  wasDerivedFrom(pc1:e25, pc1:e23)
  wasDerivedFrom(pc1:e25, pc1:e24)
  wasDerivedFrom(pc1:e26, pc1:e23)
  wasDerivedFrom(pc1:e26, pc1:e24)
  wasDerivedFrom(pc1:e27, pc1:e23)
  wasDerivedFrom(pc1:e27, pc1:e24)
  wasDerivedFrom(pc1:e28, pc1:e25)
  wasDerivedFrom(pc1:e29, pc1:e26)
  wasDerivedFrom(pc1:e30, pc1:e27)
  wasAssociatedWith(pc1:waw1;pc1:a1,pc1:ag1,-)
endDocument
                    """,
                    level0)
                    

    def pc1(s: String) = {
      new QualifiedName("pc1",s,"http://www.ipaw.info/pc1/")
    }

    val agg_provtypes_0: s.ProvTypes =s.aggregateProvTypesToLevel(0, aggregatep = false)
    val agg_provtypes_1: s.ProvTypes =s.aggregateProvTypesToLevel(1, aggregatep = false)
    val agg_provtypes_2: s.ProvTypes =s.aggregateProvTypesToLevel(2, aggregatep = false)
    val agg_provtypes_3: s.ProvTypes =s.aggregateProvTypesToLevel(3, aggregatep = false)
    val agg_provtypes_4: s.ProvTypes =s.aggregateProvTypesToLevel(4, aggregatep = false)
    val agg_provtypes_5: s.ProvTypes =s.aggregateProvTypesToLevel(5, aggregatep = false)
    val agg_provtypes_6: s.ProvTypes =s.aggregateProvTypesToLevel(6, aggregatep = false)
    val agg_provtypes_7: s.ProvTypes =s.aggregateProvTypesToLevel(7, aggregatep = false)
    val agg_provtypes_8: s.ProvTypes =s.aggregateProvTypesToLevel(8, aggregatep = false)
    val agg_provtypes_9: s.ProvTypes =s.aggregateProvTypesToLevel(9, aggregatep = false)
    val agg_provtypes_10: s.ProvTypes =s.aggregateProvTypesToLevel(10, aggregatep = false)
    val agg_provtypes_11: s.ProvTypes =s.aggregateProvTypesToLevel(11, aggregatep = false)

    val provtypes=Seq(agg_provtypes_0, agg_provtypes_1, agg_provtypes_2, agg_provtypes_3, agg_provtypes_4, agg_provtypes_5, agg_provtypes_6, agg_provtypes_7, agg_provtypes_8, agg_provtypes_9, agg_provtypes_10, agg_provtypes_11)
    
    ind.absentNodes shouldBe empty

    def getCount(agg_provtypes_0: s.ProvTypes): Int = {
      val index0 = new SummaryConstructor(ind, agg_provtypes_0, nsBase, PrettyMethod.Name)
      val description0 = index0.makeIndex().summaryDescription()
      val features0 = description0.getFeatures
      val count0 = features0.values.toSeq.sum

      println("count " + count0 + " features: " + features0.size)
      count0
    }

    val counts=provtypes.map(getCount)

    counts.toSet.size should be (1)






    agg_provtypes_0(ind.amap(pc1("e1"))) should be (Set(Ent(), Prim("File"))) // See how we refer to the customised provtype
    agg_provtypes_0(ind.amap(pc1("e25p"))) should be (Set(Ent(), Prim("prim:String")))  // here default provtype for this domain specific type
   

    agg_provtypes_1(ind.amap(pc1("a11"))) should be (Set(Usd(Set(Ent(), Prim("File"))),
                                                         Usd(Set(Ent(), Prim("prim:String")))))

  }
 
  def ex(s: String): QualifiedName = {
      new QualifiedName("ex",s,EX_NS)
  }



}
