


package org.openprovenance.prov.scala

import org.openprovenance.prov.model.Namespace
import org.openprovenance.prov.scala.immutable._
import org.openprovenance.prov.scala.interop.{FileInput, Input}
import org.openprovenance.prov.scala.narrator.{EventOrder, EventOrganiser, LinearOrder, NoEvent}
import org.openprovenance.prov.scala.nf.CommandLine
import org.openprovenance.prov.scala.nlg._
import org.openprovenance.prov.scala.nlgspec_transformer.{Language, SpecLoader, specTypes}
import org.openprovenance.prov.scala.query.QueryInterpreter.RField
import org.openprovenance.prov.scala.query.StatementIndexer
import org.openprovenance.prov.scala.streaming.{DocBuilder, DocBuilderFunctions}
import org.openprovenance.prov.scala.xplain.RealiserFactory
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.io.{File, StringWriter}
import java.util
import scala.jdk.CollectionConverters._
import scala.util.Success


class NlgSpec extends AnyFlatSpec with Matchers {
  val EX_NS="http://example.org/"
  val pf=new ProvFactory
  val ipf=new org.openprovenance.prov.scala.immutable.ProvFactory

  def q(local: String): QualifiedName = {
    new QualifiedName("ex",local,EX_NS)
  }


  def readDoc(f: String): Document = {
    val in:Input=FileInput(new File(f))
    val doc=CommandLine.parseDocument(in)
    doc
  }


  def parse(d: String):Document = {
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
      case Success(_) => db.document()
      case x => throw new IllegalStateException("Parsing failed: " + x)
    }
    doc
  }



  "a Template" should "be readable" in {
    val template=SpecLoader.templateImport("src/main/resources/nlg/templates/provbasic/entity1.json")
  }

  val maker = new SentenceMaker()

  "An entity" should "be describe-able by a sentence" in {


    val doc = parse(
      """
            document
             prefix tmpl <http://openprovenance.org/tmpl#>
             prefix ex <http://example.org/>
             entity(ex:e1)
            endDocument
            """")
    val entity1 = doc.statements().head

    println(entity1)
    val template = SpecLoader.templateImport("src/main/resources/nlg/templates/provbasic/entity1.json")

    val objects: Map[String, RField] = Map("ent" -> entity1)

    val triples=scala.collection.mutable.Set[primitive.Triple]()

    val result: Option[specTypes.Phrase] = maker.transform(objects, template.sentence, null, triples, "")


    println(result)

    val (text,_,_) = maker.realisation(result, 0)

    println(text)

    text should be("E1 is an entity.")

  }



  "An activity" should "be describe-able by a sentence" in {


    val doc=parse("""
            document
             prefix tmpl <http://openprovenance.org/tmpl#>
             prefix ex <http://example.org/>
             activity(ex:a1)
            endDocument
            """")
    val s: Statement =doc.statements().head

    val template=SpecLoader.templateImport("src/main/resources/nlg/templates/provbasic/activity1.json")

    val objects: Map[String, RField] = Map("act" -> s)

    val triples=scala.collection.mutable.Set[primitive.Triple]()


    val result: Option[specTypes.Phrase] =maker.transform(objects,template.sentence,null,triples, "")


    println(result)

    val sw=new StringWriter()
    SpecLoader.phraseExport(sw,result.get)
    println(sw.getBuffer.toString)

    val (text,_,_)=maker.realisation(result,0)

    println(text)

    text should be ("A1 is an activity.")

  }

  "An agent" should "be describe-able by a sentence" in {


    val doc=parse("""
            document
             prefix tmpl <http://openprovenance.org/tmpl#>
             prefix ex <http://example.org/>
             agent(ex:ag1)
            endDocument
            """")
    val s: Statement =doc.statements().head

    val template=SpecLoader.templateImport("src/main/resources/nlg/templates/provbasic/agent1.json")

    val objects: Map[String, RField] = Map("ag" -> s)

    val triples=scala.collection.mutable.Set[primitive.Triple]()

    val result=maker.transform(objects,template.sentence,null,triples, "")


    println(result)

    val (text, _,_)=maker.realisation(result,0)

    println(text)

    text should be ("Ag1 is an agent.")

  }

  "An attribution" should "be describe-able by a sentence" in {


    val doc=parse("""
            document
             prefix tmpl <http://openprovenance.org/tmpl#>
             prefix ex <http://example.org/>
             wasAttributedTo(ex:e1,ex:ag1)
            endDocument
            """")
    val wat=doc.statements().head

    println(wat)
    val template=SpecLoader.templateImport("src/main/resources/nlg/templates/provbasic/attribution1.json")

    val objects: Map[String, RField] = Map("attr" -> wat)

    val triples=scala.collection.mutable.Set[primitive.Triple]()

    val result=maker.transform(objects,template.sentence,null,triples, "")


    println("tranformation result: " + result)

    val (text, _,_)=maker.realisation(result,0)

    println(text)

    text should be ("E1 was attributed to ag1.")

  }

  "A derivation" should "be describe-able by a sentence" in {


    val doc=parse("""
            document
             prefix tmpl <http://openprovenance.org/tmpl#>
             prefix ex <http://example.org/>
             wasDerivedFrom(ex:cups,ex:clay)
            endDocument
            """")
    val wdf=doc.statements().head

    println(wdf)
    val template=SpecLoader.templateImport("src/main/resources/nlg/templates/provbasic/derivation1.json")

    val objects: Map[String, RField] = Map("der" -> wdf)

    val triples=scala.collection.mutable.Set[primitive.Triple]()

    println(template.sentence)

    val result=maker.transform(objects,template.sentence,null,triples, "")


    println(result)

    val (text, _,_)=maker.realisation(result,0)

    println(text)

    text should be ("Cups were derived from clay.")

  }


  "A generation" should "be describe-able by a sentence" in {


    val doc=parse("""
             document
              prefix tmpl <http://openprovenance.org/tmpl#>
              prefix ex <http://example.org/>
              wasGeneratedBy(ex:e1,ex:a1,-)
              wasGeneratedBy(ex:e2,ex:a2,2012-03-02T10:30:00.000Z)
            endDocument
             """")
    val wgb=doc.statements().head
    val wgb2=doc.statements().tail.head


    println(wgb)
    val template=SpecLoader.templateImport("src/main/resources/nlg/templates/provbasic/generation1.json")



    val objects: Map[String, RField] = Map("gen" -> wgb)

    val triples=scala.collection.mutable.Set[primitive.Triple]()

    val result=maker.transform(objects,template.sentence,null,triples, "")

    println(result)

    val (text, _,_)=maker.realisation(result,0)

    println(text)

    text should be ("E1 was generated by a1.")


    println(wgb2)


    val objects2: Map[String, RField] = Map("gen" -> wgb2.asInstanceOf[Statement])

    val triples2=scala.collection.mutable.Set[primitive.Triple]()

    val result2=maker.transform(objects2,template.sentence,null,triples2,"")

    println(result2)

    val (text2, _,_)=maker.realisation(result2,0)

    println(text2)

    text2 should be ("E2 was generated by a2 at 2012-03-02T10:30:00.000Z.")


  }

  "A usage" should "be describe-able by a sentence" in {


    val doc=parse("""
            document
             prefix tmpl <http://openprovenance.org/tmpl#>
             prefix ex <http://example.org/>
             used(ex:a1,ex:e1,-)
             used(ex:a2,ex:e2,2012-03-02T10:30:00.000Z)
           endDocument
            """")
    val usd=doc.statements().head
    val usd2=doc.statements().tail.head

    println(usd)
    val template=SpecLoader.templateImport("src/main/resources/nlg/templates/provbasic/usage1.json")


    val objects: Map[String, RField] = Map("usd" -> usd)

    val triples2=scala.collection.mutable.Set[primitive.Triple]()


    val result=maker.transform(objects,template.sentence,null,triples2, "")


    println(result)

    val (text, _,_)=maker.realisation(result,0)

    println(text)

    text should be ("A1 used e1.")

    println(usd2)

    val objects2: Map[String, RField] = Map("usd" -> usd2)

    val triples=scala.collection.mutable.Set[primitive.Triple]()


    val result2=maker.transform(objects2,template.sentence,null,triples, "")

    println(result2)

    val (text2, _,_)=maker.realisation(result2,0)

    println(text2)

    text2 should be ("A2 used e2 at 2012-03-02T10:30:00.000Z.")

  }


  "An invalidation" should "be describe-able by a sentence" in {


    val doc=parse("""
             document
              prefix tmpl <http://openprovenance.org/tmpl#>
              prefix ex <http://example.org/>
              wasInvalidatedBy(ex:e1,ex:a1,-)
              wasInvalidatedBy(ex:e2,ex:a2,2012-03-02T10:30:00.000Z)
            endDocument
             """")
    val wib=doc.statements().head
    val wib2=doc.statements().tail.head


    println(wib)
    val template=SpecLoader.templateImport("src/main/resources/nlg/templates/provbasic/invalidation1.json")



    val objects: Map[String, RField] = Map("wib" -> wib)

    val triples2=scala.collection.mutable.Set[primitive.Triple]()


    val result=maker.transform(objects,template.sentence,null,triples2, "")

    val (text, _,_)=maker.realisation(result,0)

    println(text)

    text should be ("E1 was invalidated by a1.")


    println(wib2)


    val objects2: Map[String, RField] = Map("wib" -> wib2)

    val triples=scala.collection.mutable.Set[primitive.Triple]()


    val result2=maker.transform(objects2,template.sentence,null,triples, "")

    println(result2)

    val (text2, _,_)=maker.realisation(result2,0)

    println(text2)

    text2 should be ("E2 was invalidated by a2 at 2012-03-02T10:30:00.000Z.")


  }




  "A start without trigger" should "be describe-able by a sentence" in {


    val doc=parse("""
             document
              prefix tmpl <http://openprovenance.org/tmpl#>
              prefix ex <http://example.org/>
              wasStartedBy(ex:a2,- ,ex:a1,-)
              wasStartedBy(ex:a4,- ,ex:a3,2012-03-02T10:30:00.000Z)
            endDocument
             """")
    val wsb=doc.statements().head
    val wsb2=doc.statements().tail.head


    println(wsb)
    val template=SpecLoader.templateImport("src/main/resources/nlg/templates/provbasic/start1.json")



    val objects: Map[String, RField] = Map("wsb" -> wsb)

    val triples2=scala.collection.mutable.Set[primitive.Triple]()


    val result=maker.transform(objects,template.sentence,null,triples2, "")

    val (text, _,_)=maker.realisation(result,0)

    println(text)

    text should be ("The activity a2 was started by a1.")


    println(wsb2)


    val objects2: Map[String, RField] = Map("wsb" -> wsb2)

    val triples=scala.collection.mutable.Set[primitive.Triple]()


    val result2=maker.transform(objects2,template.sentence,null,triples, "")

    println(result2)

    val (text2, _,_)=maker.realisation(result2,0)

    println(text2)

    text2 should be ("The activity a4 was started at 2012-03-02T10:30:00.000Z by a3.")


  }

  "A start with trigger and no starter" should "be describe-able by a sentence" in {


    val doc=parse("""
             document
              prefix tmpl <http://openprovenance.org/tmpl#>
              prefix ex <http://example.org/>
              wasStartedBy(ex:a2,ex:e1,-,-)
              wasStartedBy(ex:a4,ex:e2,-,2012-03-02T10:30:00.000Z)
            endDocument
             """")
    val wsb=doc.statements().head
    val wsb2=doc.statements().tail.head


    println(wsb)
    val template=SpecLoader.templateImport("src/main/resources/nlg/templates/provbasic/start2.json")



    val objects: Map[String, RField] = Map("wsb" -> wsb)

    val triples2=scala.collection.mutable.Set[primitive.Triple]()


    val result=maker.transform(objects,template.sentence,null,triples2, "")

    val (text, _,_)=maker.realisation(result,0)

    println(text)

    text should be ("The start of a2 was triggered by e1.")


    println(wsb2)


    val objects2: Map[String, RField] = Map("wsb" -> wsb2)

    val triples3=scala.collection.mutable.Set[primitive.Triple]()


    val result2=maker.transform(objects2,template.sentence,null,triples3,"")

    println(result2)

    val (text2, _,_)=maker.realisation(result2,0)

    println(text2)

    text2 should be ("The start of a4 was triggered at 2012-03-02T10:30:00.000Z by e2.")


  }

  "A start with trigger and with starter" should "be describe-able by a sentence" in {


    val doc=parse("""
             document
              prefix tmpl <http://openprovenance.org/tmpl#>
              prefix ex <http://example.org/>
              wasStartedBy(ex:a2,ex:e1,ex:a1,-)
              wasStartedBy(ex:a4,ex:e2,ex:a3,2012-03-02T10:30:00.000Z)
            endDocument
             """")
    val wsb=doc.statements().head
    val wsb2=doc.statements().tail.head


    println(wsb)
    val template=SpecLoader.templateImport("src/main/resources/nlg/templates/provbasic/start3.json")



    val objects: Map[String, RField] = Map("wsb" -> wsb)

    val triples2=scala.collection.mutable.Set[primitive.Triple]()


    val result=maker.transform(objects,template.sentence,null,triples2, "")

    val (text, _,_)=maker.realisation(result,0)

    println(text)

    text should be ("The start of a2 was triggered by e1 generated by a1.")


    println(wsb2)


    val objects2: Map[String, RField] = Map("wsb" -> wsb2)

    val triples=scala.collection.mutable.Set[primitive.Triple]()


    val result2=maker.transform(objects2,template.sentence,null,triples, "")

    println(result2)

    val (text2, _,_)=maker.realisation(result2,0)

    println(text2)

    text2 should be ("The start of a4 was triggered at 2012-03-02T10:30:00.000Z by e2 generated by a3.")


  }





  "An end without trigger" should "be describe-able by a sentence" in {


    val doc=parse("""
             document
              prefix tmpl <http://openprovenance.org/tmpl#>
              prefix ex <http://example.org/>
              wasEndedBy(ex:a2,ex:e1,ex:a1,-)
              wasEndedBy(ex:a4,ex:e2,ex:a3,2012-03-02T10:30:00.000Z)
            endDocument
             """")
    val web=doc.statements().head
    val web2=doc.statements().tail.head


    println(web)
    val template=SpecLoader.templateImport("src/main/resources/nlg/templates/provbasic/end1.json")



    val objects: Map[String, RField] = Map("web" -> web)

    val triples2=scala.collection.mutable.Set[primitive.Triple]()

    val result=maker.transform(objects,template.sentence,null,triples2, "")

    val (text, _,_)=maker.realisation(result,0)

    println(text)

    text should be ("The activity a2 was ended by a1.")


    println(web2)


    val objects2: Map[String, RField] = Map("web" -> web2)

    val triples=scala.collection.mutable.Set[primitive.Triple]()


    val result2=maker.transform(objects2,template.sentence,null,triples, "")

    println(result2)

    val (text2, _,_)=maker.realisation(result2,0)

    println(text2)

    text2 should be ("The activity a4 was ended at 2012-03-02T10:30:00.000Z by a3.")


  }


  "A end with trigger and no ender" should "be describe-able by a sentence" in {


    val doc=parse("""
             document
              prefix tmpl <http://openprovenance.org/tmpl#>
              prefix ex <http://example.org/>
              wasEndedBy(ex:a2,ex:e1,-,-)
              wasEndedBy(ex:a4,ex:e2,-,2012-03-02T10:30:00.000Z)
            endDocument
             """")
    val wsb=doc.statements().head
    val wsb2=doc.statements().tail.head


    println(wsb)
    val template=SpecLoader.templateImport("src/main/resources/nlg/templates/provbasic/end2.json")



    val objects: Map[String, RField] = Map("web" -> wsb.asInstanceOf[Statement])

    val triples2=scala.collection.mutable.Set[primitive.Triple]()


    val result=maker.transform(objects,template.sentence,null,triples2, "")

    val (text, _,_)=maker.realisation(result,0)

    println(text)

    text should be ("The end of a2 was triggered by e1.")


    println(wsb2)


    val objects2: Map[String, RField] = Map("web" -> wsb2.asInstanceOf[Statement])

    val triples=scala.collection.mutable.Set[primitive.Triple]()


    val result2=maker.transform(objects2,template.sentence,null,triples, "")

    println(result2)

    val (text2, _,_)=maker.realisation(result2,0)

    println(text2)

    text2 should be ("The end of a4 was triggered at 2012-03-02T10:30:00.000Z by e2.")


  }


  "An end with trigger and with ender" should "be describe-able by a sentence" in {


    val doc=parse("""
             document
              prefix tmpl <http://openprovenance.org/tmpl#>
              prefix ex <http://example.org/>
              wasEndedBy(ex:a2,ex:e1,ex:a1,-)
              wasEndedBy(ex:a4,ex:e2,ex:a3,2012-03-02T10:30:00.000Z)
            endDocument
             """")
    val web=doc.statements().head
    val web2=doc.statements().tail.head


    println(web)
    val template=SpecLoader.templateImport("src/main/resources/nlg/templates/provbasic/end3.json")



    val objects: Map[String, RField] = Map("web" -> web)

    val triples2=scala.collection.mutable.Set[primitive.Triple]()


    val result=maker.transform(objects,template.sentence,null,triples2, "")

    val (text,_,_)=maker.realisation(result,0)

    println(text)

    text should be ("The end of a2 was triggered by e1 generated by a1.")


    println(web2)


    val objects2: Map[String, RField] = Map("web" -> web2)

    val triples=scala.collection.mutable.Set[primitive.Triple]()


    val result2=maker.transform(objects2,template.sentence,null,triples, "")

    println(result2)

    val (text2,_,_)=maker.realisation(result2,0)

    println(text2)

    text2 should be ("The end of a4 was triggered at 2012-03-02T10:30:00.000Z by e2 generated by a3.")


  }

  import org.openprovenance.prov.scala.narrator.EventOrganiser._

  "An document" should "be describe-able by a text" in {


    val doc_OLD=parse("""
            document
             prefix tmpl <http://openprovenance.org/tmpl#>
             prefix ex <http://example.org/>
             entity(ex:e1)
             entity(ex:e2)
             activity(ex:act1)
             used(ex:usd;ex:act1,ex:e1,2012-03-02T10:30:00.000Z)
             wasGeneratedBy(ex:gen;ex:e2,ex:act1,2012-03-02T10:30:00.000Z)
             wasDerivedFrom(ex:e2, ex:e1,ex:act1,ex:gen, ex:usd)
             agent(ex:ag1)
             wasAttributedTo(ex:e2,ex:e1)
            endDocument
            """")

    val docXX=readDoc("src/test/resources/nlg/summary_map0_prov_0.provn")
    val docYY=readDoc("src/test/resources/prov/tester.provn")
    val doc=parse("""
            document
             prefix tmpl <http://openprovenance.org/tmpl#>
             prefix ex <http://example.org/>
             entity(ex:ei)
             entity(ex:e1)
             entity(ex:e2)
             activity(ex:act)
             activity(ex:act1)
             activity(ex:act2)
             used(ex:usd1;ex:act1,ex:e1,2012-03-02T10:30:00.000Z)
             wasGeneratedBy(ex:gen1;ex:ei,ex:act1,2012-03-02T10:30:00.000Z)
             wasDerivedFrom(ex:ei, ex:e1,ex:act1,ex:gen1, ex:usd1)
             used(ex:usd2;ex:act2,ex:ei,2012-03-02T10:30:00.000Z)
             wasGeneratedBy(ex:gen2;ex:e2,ex:act2,2012-03-02T10:30:00.000Z)
             wasDerivedFrom(ex:e2, ex:ei,ex:act2,ex:gen2, ex:usd2)
             used(ex:usd;ex:act,ex:e1,2012-03-02T10:30:00.000Z)
             wasGeneratedBy(ex:gen;ex:e2,ex:act,2012-03-02T10:30:00.000Z)
             wasDerivedFrom(ex:e2, ex:e1,ex:act,ex:gen, ex:usd)
             agent(ex:ag1)
             wasAttributedTo(ex:e2,ex:ag1)
            endDocument
            """")
    val doc2=Document(docYY,gensym,NLG_PREFIX,NLG_URI)

    println(doc2)

    val (mat,idx: util.Hashtable[String, Integer],evts: Map[String, Statement])=findOrder(doc2)


    val amap:util.Map[String,Integer]=idx
    println(amap)
    val ofInterest: Map[String, Integer] =findEventsFromSentence(amap)

    val allEvents=amap.values()

    println(ofInterest)

    println(evts)

    ofInterest.foreach{case (l,i) => println(i + ": " + evts.get(l))}

    println(mat.displayMatrix2())
    println(mat.displayMatrix3())

    amap.forEach{case (l,i) => println("" + i + ": " + evts.get(l))}


    val documentProcessor = new EventOrganiser(mat)
    //
    // val order: EventOrder =new DocumentProcessor(mat).orderEvents(ofInterest.values.toSet,NoEvent())
    val order2: EventOrder =documentProcessor.linearizeEvents(allEvents.asScala.toSet,NoEvent())

    //println(order)
    println(order2)

    println(orderToTeX(order2))

    doTex(amap, ofInterest, order2, evts, "target/foo.tex")

    println(mat.m)

    println("=============>")

    documentProcessor.partitions(Set(1,2,3,4)).foreach(println)

    //val order3: LinearOrder =new DocumentProcessor(mat).linearizeEvents(ofInterest.values.toSet,NoEvent())
    val order3: LinearOrder =documentProcessor.linearizeEvents(allEvents.asScala.toSet,NoEvent())
    println(order3)

    val idx2: Set[(String, Integer)] =amap.asScala.toSet

    val idx3=idx2.map{ case (s,i) => (i,s)}.toMap

    val activityOrder: scala.collection.mutable.Map[String,LinearOrder] =scala.collection.mutable.Map()

    val ap = new ActivityProcessor(mat,idx3, evts)
    ap.filterPerActivity(order3,activityOrder)
    println(activityOrder)

    val activityInfos: Map[String, ActivityInfo] = ap.toActivityInfo(activityOrder)
    println(activityInfos)

    activityInfos.foreach{case (s,info) => println(ActivityInfo.oldestEvent(info.body))}


    println(activityInfos.values.toSet)


    println("=============>>")
    println("order " + order3)

    println(doc2.statements())

    val theStatements: Seq[Statement] =ap.convertToSequence(order3,doc2.statements().toSet,scala.collection.mutable.Set())

    println(theStatements)

    val sorted: Array[ActivityInfo] =scala.util.Sorting.stableSort(activityInfos.values.toSeq)


    println(sorted.mkString("==[\n","\n","\n==]"))

    println(activityOrder)

    println("=============>>>")

    doTex(amap, ofInterest, order3, evts, "target/foo2.tex")

    //val order4: EventOrder =documentProcessor.reorganize(order3)
    //println(order4)

    //doTex(amap, ofInterest, order4, evts, "foo3.tex")

    //ealise(doc2)


  }



  def doTex(amap: util.Map[String, Integer], ofInterest: Map[String, Integer], order2: EventOrder, evts: Map[String, Statement], file: String): Unit = {
    val sb = new StringBuffer()
    amap.asScala.toVector.sortWith { case ((s1, d1), (s2, d2)) => d1 < d2 }.foreach { case (l, i) => sb.append(i + ": " + evts.get(l) + "\n") }
    sb.append("\n\n")
    ofInterest.foreach { case (s, i) => sb.append(i + ": " + s + "\n") }


    doLatex(file,
      () => orderToTeX(order2),
      () => sb.toString
    )
  }


  "A vocabulary" should "be readable" in {

    val (name, templates,_) = Language.read("src/main/resources/nlg/templates/provbasic/provbasic.json")

    name should be ("provbasic")

    val x=new RealiserFactory("src/main/resources/nlg/templates/provbasic/provbasic.json", true).make(List())
  }


  "a map of List " should "become a list of maps " in {
    val amap=Map("a" -> List(0,1,2), "b" -> List(11,12), "c" -> List(100))
    val res=List(Map("c" -> 100, "b" -> 11, "a" -> 0), Map("c" -> 100, "b" -> 11, "a" -> 1), Map("c" -> 100, "b" -> 11, "a" -> 2), Map("c" -> 100, "b" -> 12, "a" -> 0), Map("c" -> 100, "b" -> 12, "a" -> 1), Map("c" -> 100, "b" -> 12, "a" -> 2))

    StatementIndexer.enumerateAllCombinations(amap.toList) should be (res)


    val amap2=Map("a" -> List(0,1,2,4), "b" -> List(11,12,13), "c" -> List(100,2,3))

    StatementIndexer.enumerateAllCombinations(amap2.toList).size should be (36)

  }




  "A usage" should "be describe-able by a sentence (2)" in {


    val doc=parse("""
            document
             prefix tmpl <http://openprovenance.org/tmpl#>
             prefix ex <http://example.org/>
             used(ex:a1,ex:e1,-)
             used(ex:a2,ex:e2,2012-03-02T10:30:00.000Z)
           endDocument
            """")
    val usd=doc.statements().head
    val usd2=doc.statements().tail.head

    val realiser=new RealiserFactory("src/main/resources/nlg/templates/provbasic/provbasic.json",true)

    val results: Iterable[String] =realiser.make(doc).realise(null).sentences.reverse


    println(results)

  }


  "A foaf name" should "be describe-able by a sentence " in {


    val doc=parse("""
            document
             prefix tmpl <http://openprovenance.org/tmpl#>
             prefix ex <http://example.org/>
             prefix foaf <http://xmlns.com/foaf/0.1/>
             agent(ex:ag1, [ prov:type='prov:Person', foaf:givenName = "Derek",
                             foaf:mbox= "<mailto:derekexample.org>"])
            endDocument
            """")

    val realiser=new RealiserFactory(Seq("src/main/resources/nlg/templates/provbasic/provbasic.json", "src/main/resources/nlg/templates/foaf/foaf.json"), true).make(doc)

    val results: Iterable[String] =realiser.realise(null).sentences.reverse


    println(results)

  }



  "A baked cake" should "be describe-able by a sentence " in {


    val doc=parse("""
            document
             prefix tmpl <http://openprovenance.org/tmpl#>
             prefix ex <http://example.org/>
             prefix foaf <http://xmlns.com/foaf/0.1/>
             agent(ex:ag1, [ prov:type='prov:Person', foaf:givenName = "Derek"])
             entity(ex:e, [ prov:type='ex:Cake'])
             activity(ex:a, [ prov:type='ex:Baking'])
             wasGeneratedBy(ex:e,ex:a,-)
             wasAssociatedWith(ex:a,ex:ag,-)
            endDocument
            """")

    val realiser=new RealiserFactory(Seq("src/main/resources/nlg/templates/provbasic/provbasic.json", "src/main/resources/nlg/templates/foaf/foaf.json"), true).make(doc)

    val results: Iterable[String] =realiser.realise(null).sentences.reverse


    println(results)

  }



  "A baked cake" should "be BETTER describe-able by a sentence " in {


    val doc=parse("""
            document
             prefix tmpl <http://openprovenance.org/tmpl#>
             prefix ex <http://example.org/>
             prefix foaf <http://xmlns.com/foaf/0.1/>
             agent(ex:ag1, [ prov:type='prov:Person', foaf:givenName = "Derek"])
             entity(ex:e, [ prov:type='ex:Cake'])
             activity(ex:a, [ prov:type='ex:Baking'])
             wasGeneratedBy(ex:e,ex:a,-)
             wasAssociatedWith(ex:a,ex:ag,-)
            endDocument
            """")

    val doc1=Document(doc,gensym,NLG_PREFIX,NLG_URI)


    val realiser=new RealiserFactory(Seq("src/main/resources/nlg/templates/provbasic/provbasic.json", "src/main/resources/nlg/templates/foaf/foaf.json", "src/main/resources/nlg/templates/baking/baking.json"), true).make(doc1)

    val results: Iterable[String] =realiser.realise(null).sentences.reverse


    println(results)

  }


}