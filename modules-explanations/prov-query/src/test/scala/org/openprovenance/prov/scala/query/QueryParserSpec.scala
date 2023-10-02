
package org.openprovenance.prov.scala.query

import org.openprovenance.prov.model.Namespace
import org.openprovenance.prov.scala.immutable.{ProvFactory, QualifiedName}
import org.parboiled2.ParseError
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.openprovenance.prov.scala.query.QueryAST.{Schema, toSchema}

import scala.util.{Failure, Success}


class QueryParserSpec extends AnyFlatSpec with Matchers {
  val EX_NS: String ="http://example.org/"
  val ipf: ProvFactory =new org.openprovenance.prov.scala.immutable.ProvFactory
  val xsd_string: QualifiedName =QualifiedName(ipf.xsd_string)
  val prov_qualified_name: QualifiedName =QualifiedName(ipf.prov_qualified_name)

  def q(local: String): QualifiedName = {
   new QualifiedName("ex",local,EX_NS)
  }



  val ns2=new Namespace
  ns2.addKnownNamespaces()

  val proc=new Processor(null,null)



  def doCheckQuery(s: String): Operator = {
    val p=new ProvQLParser(proc, s,ns2)
    p.query.run() match {
      case Success(ast) => println("parse " + ast); ast.asInstanceOf[Operator]
      case Failure(e: ParseError) => println(p.formatError(e)); null
      case Failure(e) =>e.printStackTrace(); null
    }
  }




  "A   select  clause" should "parse" in {

    doCheckQuery(
      """
        select * from  ent a prov:Entity

      """.stripMargin) should be (
        Scan("prov:Entity", toSchema("ent"), None))


    doCheckQuery(
      """
        select * from  ent a prov:Entity
        where ent[prov:type] >= 'prov:Person'
      """.stripMargin) should be (
      Filter(Eq("includesQualifiedName",Property("ent","prov:type"),Value("prov:Person")),
        Scan("prov:Entity",toSchema("ent"), None)))


    doCheckQuery(
      """
        select * from  ent a prov:Entity
        from wgb a prov:WasGeneratedBy
        join ent.id = wgb.entity
        where ent[prov:type] >= 'prov:Person'
      """.stripMargin) should be (
      Filter(Eq("includesQualifiedName",Property("ent","prov:type"),Value("prov:Person")),
        Join(Scan("prov:Entity",toSchema("ent"), None),"ent","id", Scan("prov:WasGeneratedBy", toSchema("wgb"), None), "wgb", "entity")))


    doCheckQuery(
      """
        select ent from  ent a prov:Entity
        from wgb a prov:WasGeneratedBy
        join ent.id = wgb.entity
        where ent[prov:type] >= 'prov:Person'
      """.stripMargin) should be (
      Project(toSchema("ent"),toSchema("ent"),Filter(Eq("includesQualifiedName",Property("ent","prov:type"),Value("prov:Person")),
        Join(Scan("prov:Entity",toSchema("ent"), None),"ent","id", Scan("prov:WasGeneratedBy", toSchema("wgb"), None), "wgb", "entity"))))


    doCheckQuery(
      """
        select ent as foo from  ent a prov:Entity
        from wgb a prov:WasGeneratedBy
        join ent.id = wgb.entity
        where ent[prov:type] >= 'prov:Person'
      """.stripMargin) should be (
      Project(toSchema("foo"),toSchema("ent"),Filter(Eq("includesQualifiedName",Property("ent","prov:type"),Value("prov:Person")),
        Join(Scan("prov:Entity",toSchema("ent"), None),"ent","id", Scan("prov:WasGeneratedBy", toSchema("wgb"), None), "wgb", "entity"))))


    doCheckQuery(
      """
        select ent as foo, wgb as gen from  ent a prov:Entity
        from wgb a prov:WasGeneratedBy
        join ent.id = wgb.entity
        where ent[prov:type] >= 'prov:Person'
      """.stripMargin) should be (
      Project(toSchema("foo", "gen"),toSchema("ent", "wgb"),Filter(Eq("includesQualifiedName",Property("ent","prov:type"),Value("prov:Person")),
        Join(Scan("prov:Entity",toSchema("ent"), None),"ent","id", Scan("prov:WasGeneratedBy", toSchema("wgb"), None), "wgb", "entity"))))


    doCheckQuery(
      """
         prefix foaf <http://foaf/>

        select * from  ent a prov:Entity
        from wgb a prov:WasGeneratedBy
        join ent.id = wgb.entity
        where ent[prov:type] >= 'prov:Person'
        and ent[foaf:Name] exists
      """.stripMargin) should be (
      Filter(Eq("exists",Property("ent","foaf:Name"),null),
        Filter(Eq("includesQualifiedName",Property("ent","prov:type"),Value("prov:Person")),
          Join(Scan("prov:Entity",toSchema("ent"), None),"ent","id",
            Scan("prov:WasGeneratedBy", toSchema("wgb"), None), "wgb", "entity")))
    )

    doCheckQuery(
      """

    prefix ln <https://plead-project.org/ns/loan#>
    prefix sk <https://scikit-learn.org/stable/modules/generated/sklearn.>
    prefix pl <https://plead-project.org/ns/plead#>


            select * from  waw a prov:WasAssociatedWith
            from actor a prov:Agent
             join waw.agent = actor.id
            from do a prov:Activity
             join waw.activity=do.id
            from wgb a prov:WasGeneratedBy
             join do.id=wgb.activity
            from pipeline a prov:Entity
             join wgb.entity=pipeline.id
            from wdf a prov:WasDerivedFrom
             join pipeline.id=wdf .usedEntity
            from recommendation a prov:Entity
             join wdf.generatedEntity=recommendation.id
            where actor[prov:type] >= 'prov:Person'
              and do[prov:type] >= 'ln:PipelineApproval'
              and recommendation[prov:type] >= 'ln:AutomatedLoanRecommendation'
              and pipeline[prov:type] >= 'sk:pipeline.Pipeline'

          """.stripMargin) should be (Filter(Eq("includesQualifiedName",Property("pipeline","prov:type"),Value("sk:pipeline.Pipeline")),Filter(Eq("includesQualifiedName",Property("recommendation","prov:type"),Value("ln:AutomatedLoanRecommendation")),Filter(Eq("includesQualifiedName",Property("do","prov:type"),Value("ln:PipelineApproval")),
      Filter(Eq("includesQualifiedName",Property("actor","prov:type"),Value("prov:Person")),Join(Join(Join(Join(Join(Join(Scan("prov:WasAssociatedWith",toSchema("waw"), None),"waw","agent",Scan("prov:Agent",toSchema("actor"), None),"actor","id"),"waw","activity",Scan("prov:Activity",toSchema("do"), None),"do","id"),"do","id",
        Scan("prov:WasGeneratedBy",toSchema("wgb"), None),"wgb","activity"),"wgb","entity",Scan("prov:Entity",toSchema("pipeline"), None),"pipeline","id"),"pipeline","id",Scan("prov:WasDerivedFrom",toSchema("wdf"), None),
        "wdf","usedEntity"),"wdf","generatedEntity",Scan("prov:Entity",toSchema("recommendation"), None),"recommendation","id")))))
    )




    doCheckQuery(
      """
         prefix ln <https://plead-project.org/ns/loan#>
         prefix sk <https://scikit-learn.org/stable/modules/generated/sklearn.>


            select * from  waw a prov:WasAssociatedWith
            from actor a prov:Agent
             join waw.agent = actor.id
            from do a prov:Activity
             join waw.activity=do.id
            from wgb a prov:WasGeneratedBy
             join do.id=wgb.activity
            from pipeline a prov:Entity
             join wgb.entity=pipeline.id
            from wdf a prov:WasDerivedFrom
             join pipeline.id=wdf .usedEntity
            from recommendation a prov:Entity
             join wdf.generatedEntity=recommendation.id
            where actor[prov:type] >= 'prov:Person'
              and do[prov:type] >= 'ln:PipelineApproval'
              and recommendation[prov:type] >= 'ln:AutomatedLoanRecommendation'
              and pipeline[prov:type] >= 'sk:pipeline.Pipeline'

          """.stripMargin) should be (Filter(Eq("includesQualifiedName",Property("pipeline","prov:type"),Value("sk:pipeline.Pipeline")),Filter(Eq("includesQualifiedName",Property("recommendation","prov:type"),Value("ln:AutomatedLoanRecommendation")),Filter(Eq("includesQualifiedName",Property("do","prov:type"),Value("ln:PipelineApproval")),
      Filter(Eq("includesQualifiedName",Property("actor","prov:type"),Value("prov:Person")),Join(Join(Join(Join(Join(Join(Scan("prov:WasAssociatedWith",toSchema("waw"), None),"waw","agent",Scan("prov:Agent",toSchema("actor"), None),"actor","id"),"waw","activity",Scan("prov:Activity",toSchema("do"), None),"do","id"),"do","id",
        Scan("prov:WasGeneratedBy",toSchema("wgb"), None),"wgb","activity"),"wgb","entity",Scan("prov:Entity",toSchema("pipeline"), None),"pipeline","id"),"pipeline","id",Scan("prov:WasDerivedFrom",toSchema("wdf"), None),
        "wdf","usedEntity"),"wdf","generatedEntity",Scan("prov:Entity",toSchema("recommendation"), None),"recommendation","id")))))
    )


    doCheckQuery(
      """
        select ent as foo1, wgb as gen from  ent a prov:Entity in document my_doc
        from wgb a prov:WasGeneratedBy
        join ent.id = wgb.entity
        where ent[prov:type] >= 'prov:Person'
      """.stripMargin) should be (
      Project(toSchema("foo1", "gen"),toSchema("ent", "wgb"),Filter(Eq("includesQualifiedName",Property("ent","prov:type"),Value("prov:Person")),
        Join(Scan("prov:Entity",toSchema("ent"), Some("my_doc")),"ent","id", Scan("prov:WasGeneratedBy", toSchema("wgb"), None), "wgb", "entity"))))


    doCheckQuery(
      """
        select ent as foo1, wgb as gen from  ent a prov:Entity in document my_doc3
        from wgb a prov:WasGeneratedBy in document my_doc2
        join ent.id = wgb.entity
        where ent[prov:type] >= 'prov:Person'
      """.stripMargin) should be (
      Project(
        toSchema("foo1", "gen"),
        toSchema("ent", "wgb"),
        Filter(Eq("includesQualifiedName",Property("ent","prov:type"),Value("prov:Person")),
        Join(Scan("prov:Entity",toSchema("ent"), Some("my_doc3")),"ent","id", Scan("prov:WasGeneratedBy", toSchema("wgb"), Some("my_doc2")), "wgb", "entity"))))

  }

}
