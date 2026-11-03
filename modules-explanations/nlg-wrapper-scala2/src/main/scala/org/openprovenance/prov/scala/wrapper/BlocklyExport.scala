package org.openprovenance.prov.scala.wrapper

import nlg.wrapper._
import org.openprovenance.prov.scala.wrapper.defs.{Head, Phrase}

import java.util

trait ExportableToBlockly {
  def toBlockly(): Block
}

class BlocklyExport {


  def newId: String = {
    count = count + 1
    val id = "id" + count
    id
  }

  var count=1;

  def newBlockWithTextField(s: String): Block = {
    val id: Head = newId
    val l: util.List[BlocklyContents] = new util.LinkedList[BlocklyContents]()
    l.add(new Field("TEXT", s))
    new Block(id, "text", null, null, l)
  }

  def newValueWithBlock(name:String,b:Block): Value ={
    val id: Head = newId
    val l: util.List[Block] = new util.LinkedList[Block]()
    l.add(b);
    new Value(name,l)
  }
  def newFeaturesBlock(features: Map[String,String]): Block = {
    val id: Head = newId
    val l: util.List[BlocklyContents] = new util.LinkedList[BlocklyContents]()
    features.foreach{ case (k:String,v:String) =>
      val kUpper = k.toUpperCase()
      l.add(new Field(getKeyword(kUpper + "_CHOICE"),getValue(kUpper,v.toUpperCase)))
      l.add(new Field(getKeyword(kUpper + "_TICK"),"TRUE"))
    }
    new Block(id, "features", null, null, l)
  }


  val theKeywordMap=Map(
    "FORM_CHOICE"    -> "PARTICIPLE_CHOICE",
    "FORM_TICK"      -> "PARTICIPLE_TICK",
    "PASSIVE_CHOICE" -> "VOICE_CHOICE",
    "PASSIVE_TICK"   -> "VOICE_TICK",
    "PASTPARTICIPLE" -> "PAST_PARTICIPLE",
    "PRESENTPARTICIPLE" -> "PRESENT_PARTICIPLE")
  def getKeyword(v: String): String = {
    theKeywordMap.getOrElse(v,v)
  }

  def getValue(keyword:String, value:String): String = {
    keyword match {
      case "PASSIVE" => if (value equals "TRUE") "PASSIVE" else "ACTIVE"
      case  _ => value
    }
  }

  def toBlocklyArray2(name: String, coordinates1: Seq[Option[Phrase]]): Value = {
    val ll: util.List[BlocklyContents] = new util.LinkedList[BlocklyContents]()
    // mutation ll.add(new Block())

    var count = 0

    def countInc(): Int = {
      val tmp = count;
      count = count + 1
      tmp
    }
    coordinates1.foreach(o => o.map(phrase => ll.add(newValueWithBlock("ADD" + countInc(), phrase.toBlockly()))))
    newValueWithBlock(name, new Block(newId, "lists_create_with", null, null, ll))
  }


  def toBlocklyArray(name: String, array: Seq[ExportableToBlockly]): Value = {
    val ll: util.List[BlocklyContents] = new util.LinkedList[BlocklyContents]()
    ll.add(new Mutation(array.length.toString,""))
    var count = 0

    def countInc(): Int = {
      val tmp = count;
      count = count + 1
      tmp
    }
    array.foreach(phrase => ll.add(newValueWithBlock("ADD" + countInc(), phrase.toBlockly())))
    newValueWithBlock(name, new Block(newId, "lists_create_with", null, null, ll))
  }



}
