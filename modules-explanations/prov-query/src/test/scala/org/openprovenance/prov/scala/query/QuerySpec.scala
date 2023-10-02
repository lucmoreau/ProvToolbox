


package org.openprovenance.prov.scala.query

import org.openprovenance.prov.model.Namespace
import org.openprovenance.prov.scala.immutable._
import org.openprovenance.prov.scala.interop.{FileInput, Input}
import org.openprovenance.prov.scala.nf.CommandLine
import org.openprovenance.prov.scala.nlgspec_transformer.Environment
import org.openprovenance.prov.scala.query.QueryAST.toSchema
import org.openprovenance.prov.scala.query.Run.MainEngine
import org.openprovenance.prov.scala.streaming.{DocBuilder, DocBuilderFunctions}
import org.openprovenance.prov.scala.query.Value
import org.parboiled2.ParseError
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.io.File
import scala.annotation.unused
import scala.util.{Failure, Success}

trait HasLuc {
  def luc (): Unit
}

class QuerySpec extends AnyFlatSpec with Matchers  {
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





  trait ToTest extends QueryAST  {
    val tests: Map[String, Operator] = Map(
      "t1" -> Scan("prov:Entity", toSchema("dummy"), None),
      "t2" -> Project(toSchema("Name"), toSchema("Name"), Scan("prov:Entity", toSchema("e"), None))
    )

  }


  "A query" should "work on a simple document " in {


    val doc=parse("""
            document
             prefix tmpl <http://openprovenance.org/tmpl#>
             prefix ex <http://example.org/>
             prefix foaf <http://xmlns.com/foaf/0.1/>
             agent(ex:ag1, [ prov:type='prov:Person', foaf:givenName = "Derek1"])
             agent(ex:ag1, [  foaf:givenName = "Derek2"])
             agent(ex:ag1, [ prov:type='prov:Software', foaf:givenName = "Derek3"])
             entity(ex:e, [ prov:type='ex:Cake'])
             entity(ex:e2, [ prov:type='ex:Cake'])
             activity(ex:a, [ prov:type='ex:Baking'])
            activity(ex:a, [ prov:type='ex:OtherBaking'])
            wasGeneratedBy(ex:e,ex:a,-)
             wasGeneratedBy(ex:e2,ex:a,-)
             wasAssociatedWith(ex:a,ex:ag,-)
            endDocument
            """")

    val doc1=Document(doc,QuerySetup.gensym,QuerySetup.NLG_PREFIX,QuerySetup.NLG_URI)

    val si=new StatementIndexer(doc1)
    val doc_realiser=(_:Option[String]) => si


    def engine: Engine with MainEngine with QueryInterpreter with HasLuc =
      new Engine with MainEngine with QueryInterpreter with HasLuc  {
        override def liftTable(n: QueryAST.Table): QueryAST.Table = n
        override def eval(): Unit = run()

        override val statementFinder:Option[String]=>StatementAccessor=doc_realiser

        override val environment: Environment = Environment(Map("ex" ->"http://example.org/#",
          "foaf" -> "http://xmlns.com/foaf/0.1/",
          "ln" -> "https://plead-project.org/ns/loan#",
          "pd" -> "https://pandas.pydata.org/#",
         "prov" -> "http://www.w3.org/ns/prov#"),null,null,new Array[String](0), List())


        val t1:Operator=Scan("prov:WasGeneratedBy", toSchema("dummy"), None)

        val t2:Operator
        =Join(Scan("prov:WasGeneratedBy", toSchema("wgb"), None),
          "wgb",
          "activity",
          Scan("prov:Activity", toSchema("act"), None),
          "act",
          "id")

        val t3:Operator
        =Filter(Eq("includesQualifiedName",Property("ag","prov:type"),Value("prov:Person")),Scan("prov:Agent", toSchema("ag"), None))


        def luc (): Unit = {
          Namespace.withThreadNamespace(doc1.getNamespace)
          execQuery(Print(t1))
          execQuery(Print(t2))
          execQuery(Print(t3))

        }

      }

    engine.luc()

  }


  "A query for automation explanation" should "work on a ML pipeline " in {


    val doc= readDoc("src/test/resources/prov/loan/128350251.provn")
    val doc1=Document(doc,QuerySetup.gensym,QuerySetup.NLG_PREFIX,QuerySetup.NLG_URI)


    val si=new StatementIndexer(doc1)
    val doc_realiser=(_:Option[String]) => si


    def engine: Engine with MainEngine with QueryInterpreter with HasLuc =
      new Engine with MainEngine with QueryInterpreter with HasLuc  {
        override def liftTable(n: QueryAST.Table): QueryAST.Table = n

        override def eval(): Unit = run()

        override val statementFinder:Option[String]=>StatementAccessor=doc_realiser

        override val environment: Environment = Environment(Map("ex" ->"http://example.org/#",
          "foaf" -> "http://xmlns.com/foaf/0.1/",
          "ln" -> "https://plead-project.org/ns/loan#",
          "pd" -> "https://pandas.pydata.org/#",
          "prov" -> "http://www.w3.org/ns/prov#"),null,null,new Array[String](0), List())



        val automation2query: Join = {

        val (_decision, _application, _fico, _reco, _credit, _officer, _reviewing, _assoc, _der1, _der2, _der3, _der4)
        =(Filter(Eq("includesQualifiedName",Property("decision","prov:type"),   Value("ln:HumanDecision")),               Scan("prov:Entity",   toSchema("decision"), None)),
          Filter(Eq("includesQualifiedName",Property("application","prov:type"),Value("ln:LoanApplication")),             Scan("prov:Entity",   toSchema("application"), None)),
          Filter(Eq("includesQualifiedName",Property("fico","prov:type"),       Value("ln:FICOScore")),                   Scan("prov:Entity",   toSchema("fico"), None)),
          Filter(Eq("includesQualifiedName",Property("reco","prov:type"),       Value("ln:AutomatedLoanRecommendation")), Scan("prov:Entity",   toSchema("reco"), None)),
          Filter(Eq("includesQualifiedName",Property("credit","prov:type"),     Value("ln:CreditReference")),             Scan("prov:Entity",   toSchema("credit"), None)),
          Filter(Eq("includesQualifiedName",Property("officer","prov:type"),    Value("ln:CreditOfficer")),               Scan("prov:Agent",    toSchema("officer"), None)),
          Filter(Eq("includesQualifiedName",Property("reviewing","prov:type"),  Value("ln:LoanAssessment")),              Scan("prov:Activity", toSchema("reviewing"), None)),
          Scan("prov:WasAssociatedWith", toSchema("assoc"), None),
          Scan("prov:WasDerivedFrom",    toSchema("der1"), None),
          Scan("prov:WasDerivedFrom",    toSchema("der2"), None),
          Scan("prov:WasDerivedFrom",    toSchema("der3"), None),
          Scan("prov:WasDerivedFrom",    toSchema("der4"), None))



        val j2:Operator
          =Join(Join(_assoc, "assoc", "agent",  /* == */       _officer, "officer",  "id"), "assoc", "activity", _reviewing, "reviewing",    "id")

        @unused
        val j3=Join(_der1, "der1", "usedEntity",       /* == */       _application, "application",  "id")

        val j4:Operator
        =Join(Join(Join(_der1,
                   "der1", "usedEntity",                   /* == */       _application, "application",  "id"),
                   "der1", "generatedEntity",              /* == */       _decision,    "decision",     "id"),
                   "der1", "activity",                     /* == */       _reviewing,   "reviewing",    "id")
        val j5:Operator
        =Join(Join(Join(_der2,
          "der2", "usedEntity",                   /* == */       _fico,        "fico",      "id"),
          "der2", "generatedEntity",              /* == */       _decision,    "decision",  "id"),
          "der2", "activity",                     /* == */       _reviewing,   "reviewing", "id")


        val j6:Operator
        =Join(Join(Join(_der3,
          "der3", "usedEntity",                   /* == */       _reco,        "reco",      "id"),
          "der3", "generatedEntity",              /* == */       _decision,    "decision",  "id"),
          "der3", "activity",                     /* == */       _reviewing,   "reviewing", "id")


        val j7:Operator
        =Join(Join(Join(_der4,
          "der4", "usedEntity",                   /* == */       _credit,      "credit",    "id"),
          "der4", "generatedEntity",              /* == */       _decision,    "decision",  "id"),
          "der4", "activity",                     /* == */       _reviewing,   "reviewing", "id")


        val j10= Join(Join(Join(Join(j4,"decision","id",  j5, "decision", "id"),"decision","id",  j6, "decision", "id"), "decision", "id", j7, "decision", "id"), "reviewing", "id", j2, "reviewing", "id")

          j10 }



          def luc (): Unit = {
          Namespace.withThreadNamespace(doc1.getNamespace)
         /* execQuery(Print(_reviewing))

          execQuery(Print(j2))
          execQuery(Print(_der1))
          execQuery(Print(_application))
          execQuery(Print(j3))

          execQuery(Print(j4))
          execQuery(Print(j5))
          execQuery(Print(j6))
          execQuery(Print(j7)) */


          execQuery(Print(automation2query))



        }

      }

    engine.luc()

  }




  "A query for responsibility explanation" should "work on a ML pipeline " in {


    val doc= readDoc("src/test/resources/prov/loan/loan2.provn")  //128350251
    val doc1=Document(doc,QuerySetup.gensym,QuerySetup.NLG_PREFIX,QuerySetup.NLG_URI)


    //val doc_realiser=new StatementIndexer(doc1)
    val si=new StatementIndexer(doc1)
    val doc_realiser=(_:Option[String]) => si


    def engine: Engine with MainEngine with QueryInterpreter with HasLuc =
      new Engine with MainEngine with QueryInterpreter  with HasLuc {
        override def liftTable(n: QueryAST.Table): QueryAST.Table = n

        override def eval(): Unit = run()

        override val statementFinder:Option[String]=>StatementAccessor=doc_realiser

        override val environment: Environment = Environment(Map("ex" ->"http://example.org/#",
          "foaf" -> "http://xmlns.com/foaf/0.1/",
          "ln" -> "https://plead-project.org/ns/loan#",
          "pd" -> "https://pandas.pydata.org/#",
          "prov" -> "http://www.w3.org/ns/prov#"),null,null,new Array[String](0), List())


        val responsibility1_query: Join = {

          val (_recommendation, _pipeline, _actor, _do, _wdf, _wgb, _waw)
          =(Filter(Eq("includesQualifiedName",Property("recommendation","prov:type"),  Value("ln:AutomatedLoanRecommendation")),  Scan("prov:Entity",   toSchema("recommendation"), None)),
            Filter(Eq("includesQualifiedName",Property("pipeline","prov:type"),        Value("sk:pipeline.Pipeline")),            Scan("prov:Entity",   toSchema("pipeline"), None)),
            Filter(Eq("includesQualifiedName",Property("actor","prov:type"),           Value("prov:Person")),                     Scan("prov:Agent",    toSchema("actor"), None)),
            Filter(Eq("includesQualifiedName",Property("do","prov:type"),              Value("ln:PipelineApproval")),             Scan("prov:Activity", toSchema("do"), None)),
            Scan("prov:WasDerivedFrom",    toSchema("wdf"), None),
            Scan("prov:WasGeneratedBy",    toSchema("wgb"), None),
            Scan("prov:WasAssociatedWith", toSchema("waw"), None))


          val q=
            Join(Join(Join(Join(Join(Join(_waw,   "waw", "agent",
              _actor, "actor", "id"),  "waw", "activity",
              _do, "do", "id"), "do", "id",
              _wgb, "wgb", "activity"), "wgb", "entity",
              _pipeline, "pipeline", "id"), "pipeline", "id",
              _wdf, "wdf", "usedEntity"), "wdf", "generatedEntity",
              _recommendation, "recommendation", "id")



          q
        }


        def luc (): Unit = {
          Namespace.withThreadNamespace(doc1.getNamespace)


          execQuery(Print(responsibility1_query))



        }

      }

    engine.luc()

  }





  "A query for responsibility2 explanation" should "work on a ML pipeline " in {


    val doc= readDoc("src/test/resources/prov/loan/loan2.provn")  //128350251
    val doc1=Document(doc,QuerySetup.gensym,QuerySetup.NLG_PREFIX,QuerySetup.NLG_URI)


    val si=new StatementIndexer(doc1)
    val doc_realiser=(_:Option[String]) => si


    def engine: Engine with MainEngine with QueryInterpreter with HasLuc =
      new Engine with MainEngine with QueryInterpreter with HasLuc  {
        override def liftTable(n: QueryAST.Table): QueryAST.Table = n

        override def eval(): Unit = run()

        override val statementFinder:Option[String]=>StatementAccessor=doc_realiser

        override val environment: Environment = Environment(Map("ex" ->"http://example.org/#",
          "foaf" -> "http://xmlns.com/foaf/0.1/",
          "ln" -> "https://plead-project.org/ns/loan#",
          "pd" -> "https://pandas.pydata.org/#",
          "prov" -> "http://www.w3.org/ns/prov#"),null,null,new Array[String](0), List())


        val responsibility1_query: Project = {

          val (_recommendation, _pipeline, _actor, _do, _wdf, _wgb, _waw, _pipeline2, _actor2, _do2, _wdf2, _wgb2, _waw2)
          =(Filter(Eq("includesQualifiedName",Property("recommendation","prov:type"),  Value("ln:AutomatedLoanRecommendation")),  Scan("prov:Entity",   toSchema("recommendation"), None)),

            Filter(Eq("includesQualifiedName",Property("pipeline","prov:type"),        Value("sk:pipeline.Pipeline")),            Scan("prov:Entity",   toSchema("pipeline"), None)),
            Filter(Eq("includesQualifiedName",Property("actor","prov:type"),           Value("prov:Person")),                     Scan("prov:Agent",    toSchema("actor"), None)),
            Filter(Eq("includesQualifiedName",Property("do","prov:type"),              Value("ln:PipelineApproval")),             Scan("prov:Activity", toSchema("do"), None)),
            Scan("provext:WasDerivedFromPlus",    toSchema("wdf"), None),
            Scan("prov:WasGeneratedBy",           toSchema("wgb"), None),
            Scan("prov:WasAssociatedWith",        toSchema("waw"), None),


            Filter(Eq("includesQualifiedName",Property("pipeline2","prov:type"),        Value("sk:pipeline.Pipeline")),            Scan("prov:Entity",   toSchema("pipeline2"), None)),
            Filter(Eq("includesQualifiedName",Property("actor2","prov:type"),           Value("prov:Person")),                     Scan("prov:Agent",    toSchema("actor2"), None)),
            Filter(Eq("includesQualifiedName",Property("do2","prov:type"),              Value("ex:DataFitting")),                  Scan("prov:Activity", toSchema("do2"), None)),
            Scan("provext:WasDerivedFromPlus",    toSchema("wdf2"), None),
            Scan("prov:WasGeneratedBy",           toSchema("wgb2"), None),
            Scan("prov:WasAssociatedWith",        toSchema("waw2"), None))


          val q=
            Join(Join(Join(Join(Join(Join(_waw,   "waw", "agent",
              _actor, "actor", "id"),  "waw", "activity",
              _do, "do", "id"), "do", "id",
              _wgb, "wgb", "activity"), "wgb", "entity",
              _pipeline, "pipeline", "id"), "pipeline", "id",
              _wdf, "wdf", "usedEntity"), "wdf", "generatedEntity",
              _recommendation, "recommendation", "id")

          val q2=
            Join(Join(Join(Join(Join(Join(_waw2,   "waw2", "agent",
              _actor2, "actor2", "id"),  "waw2", "activity",
              _do2, "do2", "id"), "do2", "id",
              _wgb2, "wgb2", "activity"), "wgb2", "entity",
              _pipeline2, "pipeline2", "id"), "pipeline2", "id",
              _wdf2, "wdf2", "usedEntity"), "wdf2", "generatedEntity",
              _recommendation, "recommendation", "id")

          Project(toSchema("a","b"), toSchema("actor","actor2"), Join(q,"recommendation","id",q2,"recommendation","id"))
        }


        def luc (): Unit = {
          Namespace.withThreadNamespace(doc1.getNamespace)
          execQuery(Print(responsibility1_query))
        }
      }

    engine.luc()

  }



  "A neat query for responsibility explanation" should "work on a ML pipeline " in {


    val doc= readDoc("src/test/resources/prov/loan/loan2.provn")
    val doc1=Document(doc,QuerySetup.gensym,QuerySetup.NLG_PREFIX,QuerySetup.NLG_URI)


    val si=new StatementIndexer(doc1)
    val doc_realiser=(_:Option[String]) => si


    def engine: Engine with QueryInterpreter with HasLuc =
      new Engine with QueryInterpreter with HasLuc {
        override def liftTable(n: QueryAST.Table): QueryAST.Table = n

        override def eval(): Unit = run()

        override val statementFinder: Option[String] => StatementAccessor = doc_realiser

        override val environment: Environment = Environment(Map("ex" -> "http://example.org/#",
          "foaf" -> "http://xmlns.com/foaf/0.1/",
          "ln" -> "https://plead-project.org/ns/loan#",
          "pd" -> "https://pandas.pydata.org/#",
          "prov" -> "http://www.w3.org/ns/prov#"), null, null, new Array[String](0), List())



        @unused
        val responsibility1_query: Join = {

          val (_recommendation, _pipeline, _actor, _do, _wdf, _wgb, _waw)
          =(Filter(Eq("includesQualifiedName",Property("recommendation","prov:type"),  Value("ln:AutomatedLoanRecommendation")),  Scan("prov:Entity",   toSchema("recommendation"), None)),
            Filter(Eq("includesQualifiedName",Property("pipeline","prov:type"),        Value("sk:pipeline.Pipeline")),            Scan("prov:Entity",   toSchema("pipeline"), None)),
            Filter(Eq("includesQualifiedName",Property("actor","prov:type"),           Value("prov:Person")),                     Scan("prov:Agent",    toSchema("actor"), None)),
            Filter(Eq("includesQualifiedName",Property("do","prov:type"),              Value("ln:PipelineApproval")),             Scan("prov:Activity", toSchema("do"), None)),
            Scan("prov:WasDerivedFrom",    toSchema("wdf"), None),
            Scan("prov:WasGeneratedBy",    toSchema("wgb"), None),
            Scan("prov:WasAssociatedWith", toSchema("waw"), None))


          val q=
            Join(Join(Join(Join(Join(Join(_waw,   "waw", "agent",
              _actor, "actor", "id"),  "waw", "activity",
              _do, "do", "id"), "do", "id",
              _wgb, "wgb", "activity"), "wgb", "entity",
              _pipeline, "pipeline", "id"), "pipeline", "id",
              _wdf, "wdf", "usedEntity"), "wdf", "generatedEntity",
              _recommendation, "recommendation", "id")



          q
        }


        val ns2 = new Namespace
        ns2.addKnownNamespaces()
        Map(
          "ex" -> "http://example.org/#",
          "foaf" -> "http://xmlns.com/foaf/0.1/",
          "ln" -> "https://plead-project.org/ns/loan#",
          "sk" -> "https://scikit-learn.org/stable/modules/generated/sklearn.",
          "pd" -> "https://pandas.pydata.org/#",
          "prov" -> "http://www.w3.org/ns/prov#").foreach{case (p,u) => ns2.register(p,u)}


        val proc=new Processor(null,null)

        def doParse(s: String): Operator =  {
          val p = new ProvQLParser(proc, s, ns2)
          p.query.run() match {
            case Success(ast) => println("parse " + ast); ast
            case Failure(e: ParseError) => println(p.formatError(e)); null
            case Failure(e) => e.printStackTrace(); null
          }
        }

        val t1: Operator =doParse("select * from ent a prov:Entity")
        val t2: Operator =doParse("select ent as renamed from ent a prov:Entity")
        val t3: Operator =doParse("select * from ent a prov:Entity where ent[prov:type] >= 'pd:DataFrame'")
        val t4: Operator =doParse("select * from waw a prov:WasAssociatedWith from actor a prov:Agent join waw.agent = actor.id")
        val t5: Operator =doParse("select * from waw a prov:WasAssociatedWith from actor a prov:Agent join waw.agent = actor.id  where actor[prov:type] >= 'prov:Person'")

        val t6: Operator =doParse(
          """

            select * from waw a prov:WasAssociatedWith
            from actor a prov:Agent
             join waw.agent = actor.id
            from do a prov:Activity
             join waw.activity=do.id
            from wgb a prov:WasGeneratedBy
             join do.id=wgb.activity
            from pipeline a prov:Entity
             join wgb.entity=pipeline.id
            from wdf a prov:WasDerivedFrom
             join pipeline.id=wdf.usedEntity
            from recommendation a prov:Entity
             join wdf.generatedEntity=recommendation.id
            where actor[prov:type] >= 'prov:Person'
              and do[prov:type] >= 'ln:PipelineApproval'
              and recommendation[prov:type] >= 'ln:AutomatedLoanRecommendation'
              and pipeline[prov:type] >= 'sk:pipeline.Pipeline'

          """.stripMargin)

        val ast1: Scan    = Scan("prov:Entity",toSchema("ent"), None)
        val ast2: Project = Project(toSchema("renamed"), toSchema("ent"), Scan("prov:Entity",toSchema("ent"), None))
        val ast3: Filter  = Filter(Eq("includesQualifiedName",Property("ent","prov:type"),Value("pd:DataFrame")),Scan("prov:Entity",toSchema("ent"), None))
        val ast4: Join    = Join(Scan("prov:WasAssociatedWith",toSchema("waw"), None),"waw","agent",Scan("prov:Agent",toSchema("actor"), None),"actor","id")
        val ast5: Filter  = Filter(Eq("includesQualifiedName",Property("actor","prov:type"),Value("prov:Person")),Join(Scan("prov:WasAssociatedWith",toSchema("waw"), None),"waw","agent",Scan("prov:Agent",toSchema("actor"), None),"actor","id"))
        val ast6: Filter  = Filter(Eq("includesQualifiedName",Property("pipeline","prov:type"),Value("sk:pipeline.Pipeline")),Filter(Eq("includesQualifiedName",Property("recommendation","prov:type"),Value("ln:AutomatedLoanRecommendation")),Filter(Eq("includesQualifiedName",Property("do","prov:type"),Value("ln:PipelineApproval")),
          Filter(Eq("includesQualifiedName",Property("actor","prov:type"),Value("prov:Person")),Join(Join(Join(Join(Join(Join(Scan("prov:WasAssociatedWith",toSchema("waw"), None),"waw","agent",Scan("prov:Agent",toSchema("actor"), None), "actor","id"),"waw","activity",Scan("prov:Activity",toSchema("do"), None),"do","id"),"do","id",
            Scan("prov:WasGeneratedBy",toSchema("wgb"), None),"wgb","activity"),"wgb","entity",Scan("prov:Entity",toSchema("pipeline"), None),"pipeline","id"),"pipeline","id",Scan("prov:WasDerivedFrom",toSchema("wdf"), None),
            "wdf","usedEntity"),"wdf","generatedEntity",Scan("prov:Entity",toSchema("recommendation"), None),"recommendation","id")))))

        def luc (): Unit = {
          Namespace.withThreadNamespace(doc1.getNamespace)

          t1 should be (ast1)
          t2 should be (ast2)
          t3 should be (ast3)
          t4 should be (ast4)
          t5 should be (ast5)
          t6 should be (ast6)


          println(t6)

         // execQuery(Print(t6))



        }

        override def query: String = ???

        override def filename: String = ???
      }

    engine.luc()

  }




}