
package org.openprovenance.prov.scala.query

import org.openprovenance.prov.model.Namespace
import org.openprovenance.prov.scala.immutable.{ProvFactory, QualifiedName}
import org.parboiled2.ParseError
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

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



  def doCheckQuery(s: String): proc.Operator = {
    val p=new ProvQLParser(proc, s,ns2)
    p.query.run() match {
      case Success(ast) => println("parse " + ast); ast.asInstanceOf[proc.Operator]
      case Failure(e: ParseError) => println(p.formatError(e)); null
      case Failure(e) =>e.printStackTrace(); null
    }
  }




  "A   select  clause" should "parse" in {

    doCheckQuery(
      """
        select * from  ent a prov:Entity

      """.stripMargin) should be (
        proc.Scan("prov:Entity",proc.Schema("ent"), None))


    doCheckQuery(
      """
        select * from  ent a prov:Entity
        where ent[prov:type] >= 'prov:Person'
      """.stripMargin) should be (
      proc.Filter(proc.Eq("includesQualifiedName",proc.Property("ent","prov:type"),proc.Value("prov:Person")),
        proc.Scan("prov:Entity",proc.Schema("ent"), None)))


    doCheckQuery(
      """
        select * from  ent a prov:Entity
        from wgb a prov:WasGeneratedBy
        join ent.id = wgb.entity
        where ent[prov:type] >= 'prov:Person'
      """.stripMargin) should be (
      proc.Filter(proc.Eq("includesQualifiedName",proc.Property("ent","prov:type"),proc.Value("prov:Person")),
        proc.Join(proc.Scan("prov:Entity",proc.Schema("ent"), None),"ent","id", proc.Scan("prov:WasGeneratedBy", proc.Schema("wgb"), None), "wgb", "entity")))


    doCheckQuery(
      """
        select ent from  ent a prov:Entity
        from wgb a prov:WasGeneratedBy
        join ent.id = wgb.entity
        where ent[prov:type] >= 'prov:Person'
      """.stripMargin) should be (
      proc.Project(proc.Schema("ent"),proc.Schema("ent"),proc.Filter(proc.Eq("includesQualifiedName",proc.Property("ent","prov:type"),proc.Value("prov:Person")),
        proc.Join(proc.Scan("prov:Entity",proc.Schema("ent"), None),"ent","id", proc.Scan("prov:WasGeneratedBy", proc.Schema("wgb"), None), "wgb", "entity"))))


    doCheckQuery(
      """
        select ent as foo from  ent a prov:Entity
        from wgb a prov:WasGeneratedBy
        join ent.id = wgb.entity
        where ent[prov:type] >= 'prov:Person'
      """.stripMargin) should be (
      proc.Project(proc.Schema("foo"),proc.Schema("ent"),proc.Filter(proc.Eq("includesQualifiedName",proc.Property("ent","prov:type"),proc.Value("prov:Person")),
        proc.Join(proc.Scan("prov:Entity",proc.Schema("ent"), None),"ent","id", proc.Scan("prov:WasGeneratedBy", proc.Schema("wgb"), None), "wgb", "entity"))))


    doCheckQuery(
      """
        select ent as foo, wgb as gen from  ent a prov:Entity
        from wgb a prov:WasGeneratedBy
        join ent.id = wgb.entity
        where ent[prov:type] >= 'prov:Person'
      """.stripMargin) should be (
      proc.Project(proc.Schema("foo", "gen"),proc.Schema("ent", "wgb"),proc.Filter(proc.Eq("includesQualifiedName",proc.Property("ent","prov:type"),proc.Value("prov:Person")),
        proc.Join(proc.Scan("prov:Entity",proc.Schema("ent"), None),"ent","id", proc.Scan("prov:WasGeneratedBy", proc.Schema("wgb"), None), "wgb", "entity"))))


    doCheckQuery(
      """
         prefix foaf <http://foaf/>

        select * from  ent a prov:Entity
        from wgb a prov:WasGeneratedBy
        join ent.id = wgb.entity
        where ent[prov:type] >= 'prov:Person'
        and ent[foaf:Name] exists
      """.stripMargin) should be (
      proc.Filter(proc.Eq("exists",proc.Property("ent","foaf:Name"),null),
        proc.Filter(proc.Eq("includesQualifiedName",proc.Property("ent","prov:type"),proc.Value("prov:Person")),
          proc.Join(proc.Scan("prov:Entity",proc.Schema("ent"), None),"ent","id",
            proc.Scan("prov:WasGeneratedBy", proc.Schema("wgb"), None), "wgb", "entity")))
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

          """.stripMargin) should be (proc.Filter(proc.Eq("includesQualifiedName",proc.Property("pipeline","prov:type"),proc.Value("sk:pipeline.Pipeline")),proc.Filter(proc.Eq("includesQualifiedName",proc.Property("recommendation","prov:type"),proc.Value("ln:AutomatedLoanRecommendation")),proc.Filter(proc.Eq("includesQualifiedName",proc.Property("do","prov:type"),proc.Value("ln:PipelineApproval")),
      proc.Filter(proc.Eq("includesQualifiedName",proc.Property("actor","prov:type"),proc.Value("prov:Person")),proc.Join(proc.Join(proc.Join(proc.Join(proc.Join(proc.Join(proc.Scan("prov:WasAssociatedWith",proc.Schema("waw"), None),"waw","agent",proc.Scan("prov:Agent",proc.Schema("actor"), None),"actor","id"),"waw","activity",proc.Scan("prov:Activity",proc.Schema("do"), None),"do","id"),"do","id",
        proc.Scan("prov:WasGeneratedBy",proc.Schema("wgb"), None),"wgb","activity"),"wgb","entity",proc.Scan("prov:Entity",proc.Schema("pipeline"), None),"pipeline","id"),"pipeline","id",proc.Scan("prov:WasDerivedFrom",proc.Schema("wdf"), None),
        "wdf","usedEntity"),"wdf","generatedEntity",proc.Scan("prov:Entity",proc.Schema("recommendation"), None),"recommendation","id")))))
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

          """.stripMargin) should be (proc.Filter(proc.Eq("includesQualifiedName",proc.Property("pipeline","prov:type"),proc.Value("sk:pipeline.Pipeline")),proc.Filter(proc.Eq("includesQualifiedName",proc.Property("recommendation","prov:type"),proc.Value("ln:AutomatedLoanRecommendation")),proc.Filter(proc.Eq("includesQualifiedName",proc.Property("do","prov:type"),proc.Value("ln:PipelineApproval")),
      proc.Filter(proc.Eq("includesQualifiedName",proc.Property("actor","prov:type"),proc.Value("prov:Person")),proc.Join(proc.Join(proc.Join(proc.Join(proc.Join(proc.Join(proc.Scan("prov:WasAssociatedWith",proc.Schema("waw"), None),"waw","agent",proc.Scan("prov:Agent",proc.Schema("actor"), None),"actor","id"),"waw","activity",proc.Scan("prov:Activity",proc.Schema("do"), None),"do","id"),"do","id",
        proc.Scan("prov:WasGeneratedBy",proc.Schema("wgb"), None),"wgb","activity"),"wgb","entity",proc.Scan("prov:Entity",proc.Schema("pipeline"), None),"pipeline","id"),"pipeline","id",proc.Scan("prov:WasDerivedFrom",proc.Schema("wdf"), None),
        "wdf","usedEntity"),"wdf","generatedEntity",proc.Scan("prov:Entity",proc.Schema("recommendation"), None),"recommendation","id")))))
    )


    doCheckQuery(
      """
        select ent as foo1, wgb as gen from  ent a prov:Entity in document my_doc
        from wgb a prov:WasGeneratedBy
        join ent.id = wgb.entity
        where ent[prov:type] >= 'prov:Person'
      """.stripMargin) should be (
      proc.Project(proc.Schema("foo1", "gen"),proc.Schema("ent", "wgb"),proc.Filter(proc.Eq("includesQualifiedName",proc.Property("ent","prov:type"),proc.Value("prov:Person")),
        proc.Join(proc.Scan("prov:Entity",proc.Schema("ent"), Some("my_doc")),"ent","id", proc.Scan("prov:WasGeneratedBy", proc.Schema("wgb"), None), "wgb", "entity"))))


    doCheckQuery(
      """
        select ent as foo1, wgb as gen from  ent a prov:Entity in document my_doc3
        from wgb a prov:WasGeneratedBy in document my_doc2
        join ent.id = wgb.entity
        where ent[prov:type] >= 'prov:Person'
      """.stripMargin) should be (
      proc.Project(proc.Schema("foo1", "gen"),proc.Schema("ent", "wgb"),proc.Filter(proc.Eq("includesQualifiedName",proc.Property("ent","prov:type"),proc.Value("prov:Person")),
        proc.Join(proc.Scan("prov:Entity",proc.Schema("ent"), Some("my_doc3")),"ent","id", proc.Scan("prov:WasGeneratedBy", proc.Schema("wgb"), Some("my_doc2")), "wgb", "entity"))))

  }

}
