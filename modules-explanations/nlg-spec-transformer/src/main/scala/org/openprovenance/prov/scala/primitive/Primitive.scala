package org.openprovenance.prov.scala.primitive

import nlg.wrapper.Constants
import org.openprovenance.prov.model
import org.openprovenance.prov.scala.immutable.{ProvFactory, _}
import org.openprovenance.prov.scala.nlgspec_transformer.Environment
import org.openprovenance.prov.scala.nlgspec_transformer.defs._
import org.openprovenance.prov.scala.nlgspec_transformer.specTypes.{Features, NounForm, Phrase, TransformEnvironment}
import org.openprovenance.prov.scala.utilities.{Utils, WasDerivedFromPlus, WasDerivedFromStar}


case class Triple (subject: QualifiedName,
                   predicate: String,
                   obj: Object) {
  override val hashCode: Int = scala.runtime.ScalaRunTime._hashCode(Triple.this)
}

case class Result private (string: Option[String], phrase:Option[Phrase], map:Option[Map[String,Object]], set:Option[Seq[Object]]) {
  def this(s:String) = {
    this(Option(s),None,None,None)
  }
  def this (map:Phrase) = {
    this(None, Option(map), None, None)
  }
  def this (set:Seq[Object]) = {
    this(None, None,None,  Option(set))
  }
  def this (map:Map[String,Object]) = {
    this(None,None,  Option(map), None)
  }
}
/*
case class Triple (subject: QualifiedName,
                   predicate: String,
                   obj: Object) {
  override val hashCode: Int = scala.runtime.ScalaRunTime._hashCode(Triple.this)
}

 */

object Primitive {

  def processFunction(getStatement: String=>Statement,
                      getSeqStatement: String=>Seq[Statement],
                      amap:Map[String,Object],
                      environment: Environment): (Option[Result],Set[Triple])= {

    val afield = amap.get(Keywords.FIELD)
    if (afield.isDefined) {
      val obj = amap(Keywords.OBJECT).toString
      val field = amap(Keywords.FIELD).toString
      val function = amap(Keywords.FUNCTION).toString
      val optionp: Boolean = amap.contains(Keywords.OPTIONAL)
      val index: Option[Int] = amap.get(Keywords.INDEX) match {
        case None => None
        case Some(s:String) => Some(s.toInt)
        case Some(x) => throw new UnsupportedOperationException("impossible value for @index: " + x)
      }
      val statement: Statement = getStatement(obj)
      val arg1: Option[Object] = amap.get(Keywords.ARG1)
      val object1 = amap.get(Keywords.OBJECT1)
      arg1 match {
        case None =>
          object1 match {
            case None =>
              applyFun(function, applyField(field, statement), optionp)

            case Some(obj1) =>
              val field1: Option[Object] = amap.get("@field1")
              field1 match {
                case None     => applyFun1(function, applyField(field, statement), optionp, (Seq(obj1),Set()),environment)
                case Some(v1) => applyFun1(function, applyField(field, statement), optionp, applyField(v1.toString,getStatement(obj1.toString)),environment)
              }
          }
        case Some(a1) => applyFun1(function, applyField(field, statement), optionp, (Seq(a1),Set()),environment)
      }

    }
    else {

      val an_object = amap.get(Keywords.OBJECT)

      if (an_object.isDefined) {

        val obj = amap(Keywords.OBJECT).toString
        val property = amap(Keywords.PROPERTY).toString
        val function = amap(Keywords.FUNCTION).toString
        val optionp: Boolean = amap.contains(Keywords.OPTIONAL)
        val index: Option[Int] = amap.get(Keywords.INDEX) match {
          case None => None
          case Some(s: String) => Some(s.toInt)
          case _ => throw new UnsupportedOperationException("@index not associated with string")
        }
        val statement: Statement = getStatement(obj)
        val arg1 = amap.get(Keywords.ARG1)
        val arg2 = amap.get(Keywords.ARG2)

        val object1 = amap.get(Keywords.OBJECT1)
        arg1 match {
          case None =>
            object1 match {
              case None => //throw new UnsupportedOperationException ("processFunction: " + function + "[ no @arg1, no @object1]")
                applyFun(function, applyProperty(property, index, statement, environment), optionp)
              case Some(obj1) =>
                applyFun1(function, applyProperty(property, index, statement, environment), optionp, (Seq(obj1), Set()), environment)
            }

          case Some(a1) =>
            arg2 match {
              case None => applyFun1(function, applyProperty(property, index, statement, environment), optionp, (Seq(a1), Set()), environment)

              case Some(a2) =>
                val arg3 = amap.get(Keywords.ARG3)

                arg3 match {
                  case None =>
                    applyFun2(function, applyProperty(property, index, statement, environment), optionp, (Seq(a1), Set()), (Seq(a2), Set()), environment)
                  case Some(a3) =>
                    applyFun3(function, applyProperty(property, index, statement, environment), optionp, (Seq(a1), Set()), (Seq(a2), Set()), (Seq(a3), Set()), environment)

                }
            }
        }
      } else {
        val aAggregate=amap.get(Keywords.AGGREGATE)
        if (aAggregate.isDefined) {
          //ok, the function applies to
          val function = amap(Keywords.FUNCTION).toString
          val optionp: Boolean = amap.contains(Keywords.OPTIONAL)
          val seqStatement = getSeqStatement(aAggregate.get.toString)
          if (seqStatement!=null) {
            println("processFunction: " + function + " " + seqStatement)
            applyFun(function = function, value = (seqStatement,Set()), optionp = optionp)
          } else {
            val seqValue=getSeqStatement
          }

        } else {
          throw new UnsupportedOperationException("processFunction: " + amap)
        }
      }
    }
  }

  def applyFun(function:String, value:(Seq[Object],Set[Triple]), optionp:Boolean): (Option[Result],Set[Triple]) = {
    val (objects,triples)=value

    if (objects.isEmpty) {
      if (optionp) {
        (None,triples)
      } else {
        (Some(doFun(function, objects)),triples)
      }
    } else {
      (Some(doFun(function, objects)),triples)
    }
  }

  def applyFun1(function: String,
                arg1: (Seq[Object],Set[Triple]),
                optionp: Boolean,
                arg2: (Seq[Object],Set[Triple]),
                environment: Environment): (Option[Result],Set[Triple]) = {
    val (values1,triples1)=arg1
    val (values2,triples2)=arg2

    (values1.isEmpty,values2.isEmpty) match {
      case (true,_) => (None,Set())
      case (_,true) => (None,Set())
      case (false,false) => (Some(doFun1(function, values1,  values2, environment)), triples1 ++ triples2)
    }
  }


  def applyFun2(function: String,
                arg1: (Seq[Object], Set[Triple]),
                optionp: Boolean,
                arg2: (Seq[Object], Set[Triple]),
                arg3: (Seq[Object], Set[Triple]),
                environment: Environment): (Option[Result], Set[Triple]) = {
    val (values1,triples1)=arg1
    val (values2,triples2)=arg2
    val (values3,triples3)=arg3

    (values1.isEmpty,values2.isEmpty,values3.isEmpty) match {
      case (true,_,_) => (None,Set())
      case (_,true,_) => (None,Set())
      case (_,_,true) => (None,Set())
      case (false,false,false) => (Some(doFun2(function, values1,  values2, values3, environment)), triples1 ++ triples2 ++ triples3)
    }
  }

  def applyFun3(function: String,
                arg1: (Seq[Object], Set[Triple]),
                optionp: Boolean,
                arg2: (Seq[Object], Set[Triple]),
                arg3: (Seq[Object], Set[Triple]),
                arg4: (Seq[Object], Set[Triple]),
                environment: Environment): (Option[Result], Set[Triple]) = {
    val (values1,triples1)=arg1
    val (values2,triples2)=arg2
    val (values3,triples3)=arg3
    val (values4,triples4)=arg4

    (values1.isEmpty,values2.isEmpty,values3.isEmpty,values4.isEmpty) match {
      case (true,_,_,_) => (None,Set())
      case (_,true,_,_) => (None,Set())
      case (_,_,true,_) => (None,Set())
      case (_,_,_,true) => (None,Set())
      case (false,false,false,false) => (Some(doFun3(function, values1,  values2, values3, values4,environment)), triples1 ++ triples2 ++ triples3 ++ triples4)
    }
  }


  import scala.language.implicitConversions

  implicit def sToResult(x: String): Result = Result(Option(x),None, None, None)
  implicit def sToResult(x: Map[String,Object]): Result = Result(None,None, Option(x), None)
  implicit def sToResult(x: Phrase): Result = Result(None,Option(x),None, None)
  implicit def sToResult(x: Seq[Object]): Result = Result(None,None,None, Option(x))


  def unwrap(aString: Object): String = {
    aString match {
      case a:LangString => a.value
      case _ => aString.toString
    }
  }

  def ordinal(o: Object): String = {
    val i:Int=o match {
      case o:Integer => o
      case s:String => Integer.valueOf(s)
      case _ => Integer.valueOf(o.toString)
    }
    i match {
      case 0 => "zeroth"
      case 1 => "first"
      case 2 => "second"
      case 3 => "thrid"
      case 4 => "fourth"
      case 5 => "fifth"
      case 6 => "sixth"
      case 7 => "seventh"
      case 8 => "eighth"
      case 9 => "ninth"
      case 10 => "tenth"
      case _ => i.toString + "th"
    }
  }
  def cardinal(o: Object): String = {
    val i:Int=o match {
      case o:Integer => o
      case s:String => Integer.valueOf(s)
      case _ => Integer.valueOf(o.toString)
    }
    i match {
      case 0 => "zero"
      case 1 => "one"
      case 2 => "two"
      case 3 => "three"
      case 4 => "four"
      case 5 => "five"
      case 6 => "six"
      case 7 => "seven"
      case 8 => "eight"
      case 9 => "nin"
      case 10 => "ten"
      case _ => i.toString
    }
  }
  def cardinality(o: Object): String = {
    val i:Int=o match {
      case o:Integer => o
      case s:String => Integer.valueOf(s)
      case _ => Integer.valueOf(o.toString)
    }
    i match {
      case 0 => "singular"
      case 1 => "singular"
      case _ => "plural"
    }
  }
  def  doFun(function:String, values:Seq[Object]): Result = {
    //println("doFun " + function + " " + value)
    function match {
      case "localname"  => values.head.asInstanceOf[QualifiedName].getLocalPart()
      case "pluralp"    => if (values.head.toString.endsWith("s")) "plural" else "singular"
      case "timestring" => values.head.toString
      case "identity"   => if (values.isEmpty) "NULL" else values.head.toString
      case "string"     => if (values.isEmpty) "NULL" else unwrap(values.head)
      case "ordinal"     => if (values.isEmpty) "NULL" else ordinal(values.head)
      case "cardinal"    => if (values.isEmpty) "NULL" else cardinal(values.head)
      case "cardinality" => if (values.isEmpty) "NULL" else cardinality(values.head)
      case "count"       => values.size + ""
      case "count+cardinality" => cardinality(values.size+"")


      case "flatten"    =>  values
      case "percentage" => if (values.isEmpty) "NULL" else {
        val num: Double =values.head.toString.toDouble * 100
        "%2.2f".format(num)+"%"
      }
      case "email"      => if (values.isEmpty) "NULL" else values.head.toString
      case "exists"     => if (values.isEmpty) "false" else "true"
      case _ => throw new UnsupportedOperationException ("doFun: " + function + "[" + (if (values==null) "NULL" else values.toString) + "]")
    }
  }

  def doFun1(function:String, value:Seq[Object], arg1: Seq[Object], environment: Environment): Result = {
    function match {
      case "equals" => (value.head == arg1.head).toString

      case "equalsQualifiedName" =>
        //println ("doFun1 (equalsQualifiedName) " + value + " (" + value.getClass + ") " + arg1 + " (" + arg1.getClass + ") " )
        (value.head.toString == arg1.head).toString

      case "includesQualifiedName" =>
        arg1.map(_.toString).toSet.subsetOf(value.map(_.toString).toSet).toString

      case "includesQualifiedNameNot" =>
        (!arg1.map(_.toString).toSet.subsetOf(value.map(_.toString).toSet)).toString

      case "noun+localname" =>
        val str: Result =doFun(function = "localname",values = value)
        str match {
          case Result(s,None,None,None) => arg1.head.toString + " (" + s.get + ")"
          case _ => throw new UnsupportedOperationException("should never be in this situation")
        }

      case "noun+identity" =>
        val str: Result =doFun(function = "identity",values = value)
        str match {
          case Result(s,None,None,None) => arg1.head.toString + " (" + s.get + ")"
          case _ => throw new UnsupportedOperationException("should never be in this situation")
        }
      case "lookup-attribute" =>
        lookup_attribute(value, arg1, environment)

      case "lookup-type" =>
        lookup_type(value, arg1, Seq(), Map(), environment)


      case "markup-for-id" =>
        val uri=value.head.asInstanceOf[QualifiedName].getUri()
        val clazz=arg1.head.toString
        "data-id=\"" + uri + "\" class=\"" + clazz + "\""

      case _ => throw new UnsupportedOperationException ("doFun1: " + function + "[found: " + (if (value==null) "NULL" else value.toString) + ", expected: " + arg1.toString + "]")
    }
  }

  def lookup_attribute(value: Seq[Object], arg1: Seq[Object], environment: Environment): Result = {
    val accessor = arg1.head.toString
    val myValue = value.head.asInstanceOf[QualifiedName]

    // println("lookup-attribute " + accessor + " " + myValue)


    accessor match {
      case "noun_phrase" =>
        val amap: Result = environment.dictionary.get(myValue) match {
          case None => Map[String, Object]("type" -> "noun_phrase", "head" -> ("!" + myValue.toString))
          case Some(p:Phrase) => p
        }
        amap
      case "adverb_phrase" =>
        val amap: Result = environment.dictionary.get(myValue) match {
          case None => Map[String, Object]("type" -> "adverb_phrase", "head" -> ("!" + myValue.toString))
          case Some(p:Phrase) => p
        }
        println("Found adverb " + amap)
        amap
      // case "self" => Json.toJsonSentence(Map[String, Object]("@splice" -> myValue.toString))
      case _ => throw new UnsupportedOperationException("environment lookup-attribute accessor " + accessor)
    }
  }

  def lookup_type(value: Seq[Object], arg1: Seq[Object], arg2: Seq[Object], features: Features, environment: Environment): Result = {
    val accessor: String = arg1.head.toString
    val myValue: QualifiedName = if (arg2.isEmpty) {
      val head = value.head
      head match {
        case qn:QualifiedName => qn
        case qn:org.openprovenance.prov.model.QualifiedName => QualifiedName(qn)  // allows for non scala qualified name here.
      }

    } else {
      /*val prefix=arg2.head.asInstanceOf[String]
      val ns=environment.context(prefix)
      value.filter(v => v.asInstanceOf[QualifiedName].namespaceURI==ns).head.asInstanceOf[QualifiedName] */

      val arg2_string=arg2.head.asInstanceOf[String]
      val prefixes: Array[String] =if (arg2_string.contains(",")) arg2_string.split(",") else Array(arg2_string)



      value.find(v => prefixes.exists(prefix => v.asInstanceOf[QualifiedName].namespaceURI==getPrefixValue(environment, prefix) )).get.asInstanceOf[QualifiedName]
    }

    accessor match {
      case "noun_phrase" =>
        lookupTypedPhrase( "noun_phrase", myValue, features, environment )
      case "verb_phrase" =>
        lookupTypedPhrase( "verb_phrase", myValue, features, environment )
      case "string_phrase" =>
        lookupTypedPhrase( "string_phrase", myValue, features, environment )
      case "string" =>
        lookupTypedPhrase( "string", myValue, features, environment )
      case _ => throw new UnsupportedOperationException("environment lookup-type accessor " + accessor)

    }
  }

  def lookup_ground_type_with_default(value: Seq[Object], arg1: Seq[Object], arg2: Seq[Object], features: Features, environment: Environment): Result = {



    val accessor: String = arg1.head.toString
    val default_value: String = {
      val arg2_string=arg2.head.asInstanceOf[String]

      val prefixes: Array[String] =if (arg2_string.contains(",")) arg2_string.split(",") else Array(arg2_string)

      arg2_string
    }

    val default_value_qn: QualifiedName=null // to be derived from default_value??



    accessor match {
      case "noun_phrase" =>
        lookupTypedPhraseWithDefault( "noun_phrase", default_value_qn, features, environment )
      case "verb_phrase" =>
        lookupTypedPhraseWithDefault( "verb_phrase", default_value_qn, features, environment )
      case "string_phrase" =>
        lookupTypedPhraseWithDefault( "string_phrase", default_value_qn, features, environment )
      case "string" =>
        lookupTypedPhraseWithDefault( "string", default_value_qn, features, environment )
      case _ => throw new UnsupportedOperationException("environment lookup-type accessor " + accessor)

    }
  }


  def lookupTypedPhrase(kind: String, myValue: QualifiedName, features: Features, environment: Environment ): Result = {
    val amap: Result = environment.dictionary.get(myValue) match {
      case None => Map[String, Object]("type" ->kind, "head" -> ("!" + myValue.toString))
      case Some(p:Phrase) => p //TODO:?? addFeaturesToPhrase(getComputedFeatures(features, environment),p)
    }
    amap
  }


  def lookupTypedPhraseWithDefault(kind: String, myValue: QualifiedName, features: Features, environment: Environment ): Result = {
    val amap: Result = environment.dictionary.get(myValue) match {
      case None => Map[String, Object]("type" ->kind, "head" -> ("!" + myValue.toString))
      case Some(p:Phrase) => p //TODO:?? addFeaturesToPhrase(getComputedFeatures(features, environment),p)
    }
    amap
  }

  //TODO: do an auto-convert of noun/verb_phrase to String when there is only a head

  private def map2phrase(m: Map[String, Object], e: TransformEnvironment): Option[Phrase] = {
    m("type") match {
      case Constants.NOUN_PHRASE =>

        val assertedFeatures: Option[Object] = m.get("features")

        val castAssertedFeatures = assertedFeatures.getOrElse(Map()).asInstanceOf[Features]

        val other_post_modifiers: Seq[Phrase] = m.getOrElse("post-modifiers",Seq()).asInstanceOf[Seq[Phrase]].flatMap(s => s.transform[Phrase](e))

        NounPhrase(
          determiner = m.getOrElse("determiner", null).asInstanceOf[String],
          head = m.getOrElse("head", null).asInstanceOf[String],
          modifiers = Seq(),
          pre_modifiers = Seq(),
          post_modifiers =other_post_modifiers,
          specifier = m.get("specifier").flatMap(s => StringPhrase(s.asInstanceOf[String]).transform[NounForm](e)),
          complements = Seq(),
          comments = null,
          features = castAssertedFeatures )


      case Constants.VERB_PHRASE =>

        val assertedFeatures: Option[Object] = m.get("features")

        val castAssertedFeatures: Features = assertedFeatures.getOrElse(Map()).asInstanceOf[Features]

        if (castAssertedFeatures.isEmpty) {

          StringPhrase(m.getOrElse("head", null).asInstanceOf[String])

        } else {

          VerbPhrase(
            head = m.getOrElse("head", null).asInstanceOf[String],
            modifiers = Seq(),
            pre_modifiers = Seq(),
            post_modifiers = Seq(),
            features = castAssertedFeatures )
        }

      case Constants.STRING_PHRASE =>
        StringPhrase(m.getOrElse("value", null).asInstanceOf[String])

    }


  }


  def doFun2(function:String, value:Seq[Object], arg1: Seq[Object], arg2: Seq[Object], environment: Environment): Result = {
    //println("doFun2 " + function + " " + value + " " + arg1 + " " + arg2)

    function match {
      case "lookup-type" =>
        lookup_type(value, arg1, arg2, Map(), environment)

      case "lookup-ground-type-with-default" =>
        lookup_ground_type_with_default(value, arg1, arg2, Map(), environment)

      case _ => throw new UnsupportedOperationException("doFun2: " + function + "[found: " + (if (value == null) "NULL" else value.toString) + ", expected: " + arg1.toString + "]")

    }
  }




  def doFun3(function:String, value:Seq[Object], arg1: Seq[Object], arg2: Seq[Object], arg3: Seq[Object], environment: Environment): Result = {

    function match {
      case "lookup-type" =>
        val features=arg3.head.asInstanceOf[Features]
        lookup_type(value, arg1, arg2, features,environment)
      case _ => throw new UnsupportedOperationException("doFun2: " + function + "[found: " + (if (value == null) "NULL" else value.toString) + ", expected: " + arg1.toString + "]")

    }
  }

  def isNull(in:Object):Option[Object]={
    Option(in)
  }

  def isNullSet(in:Set[_ <: Object]):Option[Object]={
    if (in==null) None else if (in.size==0) None else Some(in.head)
  }

  val provns: String =ProvFactory.pf.prov_location.namespaceURI

  def applyProperty(property:String, index: Option[Int], value:Statement, environment: Environment): (Seq[Object], Set[Triple]) = {
    if (property=="@attributes") {
      index match {
        case None => (getNonProvAttributes(value).map(_.elementName), Set())
        case Some(num) => (Seq(getNonProvAttributes(value).toIndexedSeq(num).elementName), Set())
      }
    }
    else {

      val obj: Seq[Object] = applyProperty1(property, value, environment)
      if (obj.isEmpty) {
        (obj, Set())
      } else {
        index match {
          case None => (obj, obj.map(Triple(value.getId().asInstanceOf[QualifiedName], property, _)).toSet)
          case Some(num) => (Seq(obj.toIndexedSeq(num)), Set(Triple(value.getId().asInstanceOf[QualifiedName], property, obj.toIndexedSeq(num))))
        }
      }
    }
  }

  def getNonProvAttributes(value: Statement): Seq[Attribute] = {
    value.getAttributes.filterNot(a => a.elementName.namespaceURI.contentEquals(provns) || a.elementName.localPart.contentEquals("created_at"))
      .toSeq.sortWith{case (a1,a2) => a1.elementName.getUri() < a2.elementName.getUri()}
  }

  def applyProperty1(property:String, value:Statement, environment: Environment): Seq[Object] = {
    val strings=property.split(":")
    val prefix=strings(0)
    val local=strings(1)
    val uri=getPrefixValue(environment, prefix) + local
    val attributes: Seq[Attribute] =value.getAttributes.toSeq.sortWith{case (a1,a2) => a1.elementName.getUri() < a2.elementName.getUri()}
    attributes.filter(attr => attr.elementName.getUri()==uri).map(attr => attr.value)

  }

  private def getPrefixValue(environment: Environment, prefix: String): String = {
    if ("prov".equals(prefix)) {
      provns
    } else {
      environment.context.get(prefix) match {
        case None => throw new NullPointerException("Prefix " + prefix + " does not exist in " + environment.context)
        case Some(v) => v
      }
    }
  }

  def applyField(field:String, value:Statement): (Seq[Object], Set[Triple]) = {
    val obj: Option[Object] =applyField1(field,value)
    obj match{
      case None    => (Seq(), Set())
      case Some(v) => (Seq(v), Set(Triple(value.id, field, v)))
    }
  }

  def applyField1(field:String, value:Statement): Option[Object] = {
    field match {
      case "id" => isNull(value.getId())
      case "entity" => value match {
        case wat:WasAttributedTo => isNull(wat.entity)
        case wgb:WasGeneratedBy => isNull(wgb.entity)
        case usd:Used => isNull(usd.entity)
        case wib:WasInvalidatedBy => isNull(wib.entity)
        case mem:HadMember => isNullSet(mem.entity)
      }
      case "activity" => value match {
        case wgb:WasGeneratedBy => isNull(wgb.activity)
        case wib:WasInvalidatedBy => isNull(wib.activity)
        case usd:Used => isNull(usd.activity)
        case wsb:WasStartedBy => isNull(wsb.activity)
        case web:WasEndedBy => isNull(web.activity)
        case waw:WasAssociatedWith => isNull(waw.activity)
        case wdf:WasDerivedFrom => isNull(wdf.activity)

      }
      case "agent" => value match {
        case wat:WasAttributedTo => isNull(wat.agent)
        case waw:WasAssociatedWith => isNull(waw.agent)
      }
      case "plan" => value match {
        case waw:WasAssociatedWith => isNull(waw.plan)
      }
      case "generatedEntity" => value match {
        case wdf:WasDerivedFrom => isNull(wdf.generatedEntity)
        case wdf:WasDerivedFromPlus => isNull(wdf.generatedEntity)
        case wdf:WasDerivedFromStar => isNull(wdf.generatedEntity)
      }
      case "generalEntity" => value match {
        case spe:SpecializationOf => isNull(spe.generalEntity)
      }
      case "specificEntity" => value match {
        case spe:SpecializationOf => isNull(spe.specificEntity)
      }
      case "alternate1" => value match {
        case alt:AlternateOf => isNull(alt.alternate1)
      }
      case "alternate2" => value match {
        case alt:AlternateOf => isNull(alt.alternate2)
      }
      case "delegate" => value match {
        case aobo:ActedOnBehalfOf => isNull(aobo.delegate)
      }
      case "responsible" => value match {
        case aobo:ActedOnBehalfOf => isNull(aobo.responsible)
      }
      case "collection" => value match {
        case mem:HadMember => isNull(mem.collection)
      }
      case "element" => value match {
        case mem:HadMember => isNullSet(mem.entity)  // Note, only returns one element in set
      }
      case "usedEntity" => value match {
        case wdf:WasDerivedFrom     => isNull(wdf.usedEntity)
        case wdf:WasDerivedFromPlus => isNull(wdf.usedEntity)
        case wdf:WasDerivedFromStar => isNull(wdf.usedEntity)
      }
      case "time" => value match {
        case wgb:WasGeneratedBy => wgb.time
        case wib:WasInvalidatedBy => wib.time
        case usd:Used => usd.time
        case wsb:WasStartedBy => wsb.time
        case web:WasEndedBy => web.time
      }
      case "startTime" => value match {
        case act:Activity => act.startTime
      }
      case "endTime" => value match {
        case act:Activity => act.endTime
      }
      case "trigger" => value match {
        case wsb:WasStartedBy => isNull(wsb.trigger)
        case web:WasEndedBy => isNull(web.trigger)
      }
      case "starter" => value match {
        case wsb: WasStartedBy => isNull(wsb.starter)
      }
      case "ender" => value match {
        case web: WasEndedBy => isNull(web.ender)
      }

      case fieldString =>
        val arr=fieldString.split(":")
        val pre=arr(0)
        val post=arr(1)
        val attributes =value.getAttributes.toSeq
        //print(attributes)
        val selected: Option[Attribute] =attributes.find(attr => {
          val elem=attr.elementName
          elem.prefix==pre  && elem.localPart==post
        })
        // println(selected)
        selected match {
          case Some(s) => Some(s.value) // Luc: 29-11-2020: now returning the value instead of a string representatiton, to ensure join over properties
          case None => None
        }
    }
  }

  def applyFunction(function: String, environment: Environment, arg1: Seq[String], arg2: Seq[String]): Set[String] = {
    function match {
      case "set-minus" =>
        val set1 = arg1.toSet
        val set2 = arg2.toSet
        val result = set1 -- set2
        result
    }
  }


  def applyPatternFun(function: String, environment: Environment, arg1: String, arg2: Option[String]): Object = {
    function match {
      case "profile-features" =>
        val profiles: Map[String, Map[String, Object]] =environment.profiles(arg1).asInstanceOf[Map[String,Map[String,Object]]]
        val arr: Array[Object] =environment.theprofile.flatMap{ p:String => profiles.get(p)}.flatMap(amap => amap.get("features"))
        if (arr.isEmpty) {
          Map()
        } else {
          arr(0)
        }


      case "set-minus" =>
        println(arg1)
        println(arg2)
        val set2: Set[String] =arg2 match {
          case None => Set[String]()
          case Some(s2) => s2.split(",").toSet
        }
        val set1: Set[String] = arg1.split(",").toSet
        val result: Set[String] =set1 -- set2
        result


      case "difference" =>
        val set2: Set[String] =arg2 match {
          case None => Set[String]()
          case Some(s2) => s2.split(",").flatMap(attr => {
            val elems: Array[String] = diffTable.getOrElse(attr, Array("!!" + attr))
            elems
          }).toSet
        }
        val set1: Set[String] = arg1.split(",").map(_.replace("[", "").replace("]", "")).toSet
        val result: Set[String] =set1 -- set2
        result


      case "difference-lookup-attribute" =>
        val set2: Set[String] =arg2 match {
          case None => Set[String]()
          case Some(s2) => s2.split(",").flatMap(attr => {
            val elems: Array[String] = diffTable.getOrElse(attr, Array("!!" + attr))
            elems
          }).toSet
        }
        val set1: Set[String] = arg1.split(",").map(_.replace("[", "").replace("]", "")).toSet
        val result: Set[String] =set1 -- set2

        val ns=Utils.toNamespace(environment.context)

        def fun(s:String): model.QualifiedName = {
          val two=s.split(":")
          //println("prefix " + two(0).trim)  //TODO fix problem, with whitespace in prefix (probably due to parsing?)
          //println("local " + two(1))
          //ns.qualifiedName(two(0),two(1),ProvFactory.pf)
          //ProvFactory.pf.newQualifiedName(environment.context(two(0).trim),two(1),two(0).trim)
          ns.stringToQualifiedName(s,ProvFactory.pf)
        }

        val terms: Set[Object] =result.map(res => doFun1("lookup-attribute",Seq(fun(res)),Seq("noun_phrase"),environment))
        terms

      case "difference-lookup-attributeOLD" =>
        val set2: Set[String] =arg2 match {
          case None => Set[String]()
          case Some(s2) => s2.split(",").flatMap(attr => {
            val elems: Array[String] = diffTable.getOrElse(attr, Array("!!" + attr))
            elems
          }).toSet
        }
        val set1: Set[String] = arg1.split(",").map(_.replace("[", "").replace("]", "")).toSet
        val result: Set[String] =set1 -- set2

        val ns=Utils.toNamespace(environment.context)

        def fun(s:String): model.QualifiedName = {
          val two=s.split(":")
          //println("prefix " + two(0).trim)  //TODO fix problem, with whitespace in prefix (probably due to parsing?)
          //println("local " + two(1))
          //ns.qualifiedName(two(0),two(1),ProvFactory.pf)
          //ProvFactory.pf.newQualifiedName(environment.context(two(0).trim),two(1),two(0).trim)
          ns.stringToQualifiedName(s,ProvFactory.pf)
        }

        val terms: Set[Object] =result.map(res => doFun1("lookup-attribute",Seq(fun(res)),Seq("noun_phrase"),environment))
        terms
    }


  }

  val diffTable: Map[String, Array[String]] =Map[String,Array[String]](
    "ln:attr_ln:attr_id"->Array("ln:attr_ln:attr_id"),
    "ln:attr_loan_amnt"->Array("ln:attr_loan_amnt"),
    "ln:attr_term"->Array("ln:attr_term"),
    "ln:attr_int_rate"->Array("ln:attr_int_rate"),
    "ln:attr_installment"->Array("ln:attr_installment"),
    "ln:attr_emp_length"->Array("ln:attr_emp_length"),
    "ln:attr_issue_d"->Array("ln:attr_issue_d"),
    "ln:attr_dti"->Array("ln:attr_dti"),
    "ln:attr_earliest_cr_line"->Array("ln:attr_earliest_cr_line"),
    "ln:attr_open_acc"->Array("ln:attr_open_acc"),
    "ln:attr_pub_rec"->Array("ln:attr_pub_rec"),
    "ln:attr_revol_util"->Array("ln:attr_revol_util"),
    "ln:attr_total_acc"->Array("ln:attr_total_acc"),
    "ln:attr_mort_acc"->Array("ln:attr_mort_acc"),
    "ln:attr_pub_rec_bankruptcies"->Array("ln:attr_pub_rec_bankruptcies"),
    "ln:attr_log_annual_inc"->Array("ln:attr_annual_inc"),
    "ln:attr_fico_score"->Array("ln:fico_range_high", "ln:fico_range_low"),
    "ln:attr_log_revol_bal"->Array("ln:attr_revol_bal"),
    "ln:attr_charged_off"->Array("ln:attr_loan_status"),
    "ln:attr_sub_grade_A2"->Array("ln:attr_sub_grade"),
    "ln:attr_sub_grade_A3"->Array("ln:attr_sub_grade"),
    "ln:attr_sub_grade_A4"->Array("ln:attr_sub_grade"),
    "ln:attr_sub_grade_A5"->Array("ln:attr_sub_grade"),
    "ln:attr_sub_grade_B1"->Array("ln:attr_sub_grade"),
    "ln:attr_sub_grade_B2"->Array("ln:attr_sub_grade"),
    "ln:attr_sub_grade_B3"->Array("ln:attr_sub_grade"),
    "ln:attr_sub_grade_B4"->Array("ln:attr_sub_grade"),
    "ln:attr_sub_grade_B5"->Array("ln:attr_sub_grade"),
    "ln:attr_sub_grade_C1"->Array("ln:attr_sub_grade"),
    "ln:attr_sub_grade_C2"->Array("ln:attr_sub_grade"),
    "ln:attr_sub_grade_C3"->Array("ln:attr_sub_grade"),
    "ln:attr_sub_grade_C4"->Array("ln:attr_sub_grade"),
    "ln:attr_sub_grade_C5"->Array("ln:attr_sub_grade"),
    "ln:attr_sub_grade_D1"->Array("ln:attr_sub_grade"),
    "ln:attr_sub_grade_D2"->Array("ln:attr_sub_grade"),
    "ln:attr_sub_grade_D3"->Array("ln:attr_sub_grade"),
    "ln:attr_sub_grade_D4"->Array("ln:attr_sub_grade"),
    "ln:attr_sub_grade_D5"->Array("ln:attr_sub_grade"),
    "ln:attr_sub_grade_E1"->Array("ln:attr_sub_grade"),
    "ln:attr_sub_grade_E2"->Array("ln:attr_sub_grade"),
    "ln:attr_sub_grade_E3"->Array("ln:attr_sub_grade"),
    "ln:attr_sub_grade_E4"->Array("ln:attr_sub_grade"),
    "ln:attr_sub_grade_E5"->Array("ln:attr_sub_grade"),
    "ln:attr_sub_grade_F1"->Array("ln:attr_sub_grade"),
    "ln:attr_sub_grade_F2"->Array("ln:attr_sub_grade"),
    "ln:attr_sub_grade_F3"->Array("ln:attr_sub_grade"),
    "ln:attr_sub_grade_F4"->Array("ln:attr_sub_grade"),
    "ln:attr_sub_grade_F5"->Array("ln:attr_sub_grade"),
    "ln:attr_sub_grade_G1"->Array("ln:attr_sub_grade"),
    "ln:attr_sub_grade_G2"->Array("ln:attr_sub_grade"),
    "ln:attr_sub_grade_G3"->Array("ln:attr_sub_grade"),
    "ln:attr_sub_grade_G4"->Array("ln:attr_sub_grade"),
    "ln:attr_sub_grade_G5"->Array("ln:attr_sub_grade"),
    "ln:attr_home_ownership_OTHER"->Array("ln:attr_home_ownership"),
    "ln:attr_home_ownership_OWN"->Array("ln:attr_home_ownership"),
    "ln:attr_home_ownership_RENT"->Array("ln:attr_home_ownership"),
    "ln:attr_verification_status_Source Verified"->Array("ln:attr_verification_status"),
    "ln:attr_verification_status_Verified"->Array("ln:attr_verification_status"),
    "ln:attr_purpose_credit_card"->Array("ln:attr_purpose"),
    "ln:attr_purpose_debt_consolidation"->Array("ln:attr_purpose"),
    "ln:attr_purpose_educational"->Array("ln:attr_purpose"),
    "ln:attr_purpose_home_improvement"->Array("ln:attr_purpose"),
    "ln:attr_purpose_house"->Array("ln:attr_purpose"),
    "ln:attr_purpose_major_purchase"->Array("ln:attr_purpose"),
    "ln:attr_purpose_medical"->Array("ln:attr_purpose"),
    "ln:attr_purpose_moving"->Array("ln:attr_purpose"),
    "ln:attr_purpose_other"->Array("ln:attr_purpose"),
    "ln:attr_purpose_renewable_energy"->Array("ln:attr_purpose"),
    "ln:attr_purpose_small_business"->Array("ln:attr_purpose"),
    "ln:attr_purpose_vacation"->Array("ln:attr_purpose"),
    "ln:attr_purpose_wedding"->Array("ln:attr_purpose"),
    "ln:attr_addr_state_AL"->Array("ln:attr_addr_state"),
    "ln:attr_addr_state_AR"->Array("ln:attr_addr_state"),
    "ln:attr_addr_state_AZ"->Array("ln:attr_addr_state"),
    "ln:attr_addr_state_CA"->Array("ln:attr_addr_state"),
    "ln:attr_addr_state_CO"->Array("ln:attr_addr_state"),
    "ln:attr_addr_state_CT"->Array("ln:attr_addr_state"),
    "ln:attr_addr_state_DC"->Array("ln:attr_addr_state"),
    "ln:attr_addr_state_DE"->Array("ln:attr_addr_state"),
    "ln:attr_addr_state_FL"->Array("ln:attr_addr_state"),
    "ln:attr_addr_state_GA"->Array("ln:attr_addr_state"),
    "ln:attr_addr_state_HI"->Array("ln:attr_addr_state"),
    "ln:attr_addr_state_IA"->Array("ln:attr_addr_state"),
    "ln:attr_addr_state_ID"->Array("ln:attr_addr_state"),
    "ln:attr_addr_state_IL"->Array("ln:attr_addr_state"),
    "ln:attr_addr_state_IN"->Array("ln:attr_addr_state"),
    "ln:attr_addr_state_KS"->Array("ln:attr_addr_state"),
    "ln:attr_addr_state_KY"->Array("ln:attr_addr_state"),
    "ln:attr_addr_state_LA"->Array("ln:attr_addr_state"),
    "ln:attr_addr_state_MA"->Array("ln:attr_addr_state"),
    "ln:attr_addr_state_MD"->Array("ln:attr_addr_state"),
    "ln:attr_addr_state_ME"->Array("ln:attr_addr_state"),
    "ln:attr_addr_state_MI"->Array("ln:attr_addr_state"),
    "ln:attr_addr_state_MN"->Array("ln:attr_addr_state"),
    "ln:attr_addr_state_MO"->Array("ln:attr_addr_state"),
    "ln:attr_addr_state_MS"->Array("ln:attr_addr_state"),
    "ln:attr_addr_state_MT"->Array("ln:attr_addr_state"),
    "ln:attr_addr_state_NC"->Array("ln:attr_addr_state"),
    "ln:attr_addr_state_ND"->Array("ln:attr_addr_state"),
    "ln:attr_addr_state_NE"->Array("ln:attr_addr_state"),
    "ln:attr_addr_state_NH"->Array("ln:attr_addr_state"),
    "ln:attr_addr_state_NJ"->Array("ln:attr_addr_state"),
    "ln:attr_addr_state_NM"->Array("ln:attr_addr_state"),
    "ln:attr_addr_state_NV"->Array("ln:attr_addr_state"),
    "ln:attr_addr_state_NY"->Array("ln:attr_addr_state"),
    "ln:attr_addr_state_OH"->Array("ln:attr_addr_state"),
    "ln:attr_addr_state_OK"->Array("ln:attr_addr_state"),
    "ln:attr_addr_state_OR"->Array("ln:attr_addr_state"),
    "ln:attr_addr_state_PA"->Array("ln:attr_addr_state"),
    "ln:attr_addr_state_RI"->Array("ln:attr_addr_state"),
    "ln:attr_addr_state_SC"->Array("ln:attr_addr_state"),
    "ln:attr_addr_state_SD"->Array("ln:attr_addr_state"),
    "ln:attr_addr_state_TN"->Array("ln:attr_addr_state"),
    "ln:attr_addr_state_TX"->Array("ln:attr_addr_state"),
    "ln:attr_addr_state_UT"->Array("ln:attr_addr_state"),
    "ln:attr_addr_state_VA"->Array("ln:attr_addr_state"),
    "ln:attr_addr_state_VT"->Array("ln:attr_addr_state"),
    "ln:attr_addr_state_WA"->Array("ln:attr_addr_state"),
    "ln:attr_addr_state_WI"->Array("ln:attr_addr_state"),
    "ln:attr_addr_state_WV"->Array("ln:attr_addr_state"),
    "ln:attr_addr_state_WY"->Array("ln:attr_addr_state"),
    "ln:attr_initial_list_status_w"->Array("ln:attr_initial_list_status"),
    "ln:attr_application_type_Joint_App"->Array("ln:attr_application_type")
  )

}

