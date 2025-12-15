package org.openprovenance.prov.scala.nlg


import org.openprovenance.prov.scala.immutable._
import org.openprovenance.prov.scala.nlgspec_transformer.Environment
import org.openprovenance.prov.scala.nlgspec_transformer.defs.callSimplenlgLibrary
import org.openprovenance.prov.scala.nlgspec_transformer.specTypes.{Phrase, TransformEnvironment}
import org.openprovenance.prov.scala.primitive.{Keywords, Triple}
import org.openprovenance.prov.scala.query.QueryInterpreter.{RField, StatementOrNull, getSeqStatement, getStatement, getStatementOrNull}
import org.openprovenance.prov.scala.summary.TypePropagator


object SentenceMaker {



  def toJsonSentence(s: Map[String,Object]): String = {
    //TypePropagator.om.writeValueAsString(Template(query = null, sentence = s, context = null, select = null, where = null, name = null))
    TypePropagator.om.writeValueAsString(s)
  }

  def toJsonSentence(s: Array[Object]): String = {
    TypePropagator.om.writeValueAsString(s)
  }
  def fromJson(s:String): Map[String,Object] = {
    val m = TypePropagator.om.readValue(s, classOf[Map[String,Object]])
    m
  }

  def extractPotentialJSon(s: String): Object = {
    if (s.startsWith("{")) fromJson(s) else s
  }
  def extractPotentialJSon(m: Array[Object]): Array[Object] = {
    m.map {
      case s: String => if (s.startsWith("{")) fromJson(s) else s
      case value => value
    }
  }

  def extractPotentialJSon(m: Map[String, Object]): Map[String, Object] = {
    val value = m(Keywords.TMP)
    value match {
      case s:String => fromJson(s)
      case _ => value.asInstanceOf[Map[String, Object]]
    }
  }

}

class SentenceMaker () {



  def transform(selected_objects: Map[String,RField],
                phrase:Phrase,
                environment0: Environment,
                triples: scala.collection.mutable.Set[Triple],
                profile: String): Option[Phrase] = {




    val te=new TransformEnvironment {
      override val environment: Environment = environment0
      override val statements: Map[String, Statement] = selected_objects.flatMap{case (s,f) => getStatementOrNull(f) match {
        case None => None
        case Some(m:StatementOrNull) =>
          m match {
            case Some(st) => Some((s,st))
            case None => Some((s,null)) // allowing for null to be returned because of leftjoins
          }
      }}
      override val seqStatements: Map[String, Seq[Statement]] = selected_objects.flatMap{case (s,f) => getSeqStatement(f) match {
        case None => None
        case Some(m) => Some((s,m))
      }}
    }

    val phraseSpec = phrase.transform[Phrase](te)

    phraseSpec

  }

/*
  def surface_realiser(s:Map[String,Object], h:String, p:Integer, log: Output): String = {

    val snlg: String = SentenceMaker.toJsonSentence(s)

    if (log!=null) {
      //TODO: this shoud be returned ... back to CommandLine
    //  org.openprovenance.prov.scala.interop.CommandLine.toOutput(log,snlg)
    }

    callSimplenlgServer(h, p, snlg)

  }


  def callSimplenlgServer(h: String, p: Integer, snlg: String): String = {

    println("snlg: " + snlg)

    val post = new HttpPost("http://" + h + ":" + p + "/generateSentence")

    // set the Content-type
    post.setHeader("Content-type", "application/json")
    // add the JSON as a StringEntity
    post.setEntity(new StringEntity(snlg))

    // send the post request
    val response: CloseableHttpResponse = HttpClientBuilder.create().build().execute(post)

    scala.io.Source.fromInputStream(response.getEntity.getContent).mkString
  }
 */

  val mylib=true

  val emptyRealisation: (String, String, () => String) =("", "", () => "")
  def realisation(phrase:Option[Phrase], option: Int): (String, String, () => String) = {


    val realised: Option[(String, String, () => String)] =phrase.map(phrase => callSimplenlgLibrary(phrase,option))

    realised.getOrElse(emptyRealisation)
  }



}
