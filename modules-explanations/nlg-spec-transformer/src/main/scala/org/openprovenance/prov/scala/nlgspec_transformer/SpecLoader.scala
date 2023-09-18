package org.openprovenance.prov.scala.nlgspec_transformer

import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id
import com.fasterxml.jackson.annotation._
import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.{ObjectMapper, SerializationFeature}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import nlg.wrapper._
import org.openprovenance.prov.model.Bundle
import org.openprovenance.prov.scala.immutable.{ProvFactory, Statement}
import org.openprovenance.prov.scala.jsonld11.serialization.{ProvDeserialiser, ProvMixin2}
import org.openprovenance.prov.scala.nlgspec_transformer.defs.{AdjectivePhrase, AdverbPhrase, Clause, CoordinatedPhrase, Dictionary, Funcall, NounPhrase, Paragraph, PhraseIterator, PrepositionPhrase, StringPhrase, Template, VerbPhrase}
import org.openprovenance.prov.scala.nlgspec_transformer.specTypes.Phrase
import org.openprovenance.prov.scala.primitive
import org.openprovenance.prov.scala.primitive.{Keywords, Primitive, Result}
import org.openprovenance.prov.scala.wrapper.defs.{lexicon, nlgFactory}
import org.openprovenance.prov.scala.wrapper.{BlocklyExport, ExportableToBlockly, IO}
import org.openprovenance.prov.vanilla.Document
import simplenlg.features._
import simplenlg.framework._
import simplenlg.phrasespec._
import simplenlg.realiser.english.Realiser

import scala.jdk.CollectionConverters._
import java.io._
import java.util
import scala.collection.immutable
import scala.reflect.ClassTag


object specTypes {

  val exporter=new BlocklyExport

  trait TransformEnvironment {
    val environment: Environment
    val statements: Map[String , Statement]
    val seqStatements: Map[String , Seq[Statement]]
  }

  val nullTransformEnvironment: TransformEnvironment =new TransformEnvironment {
    override val environment: Environment = new Environment(Map(),Seq(),Map(),null, List())
    override val statements: Map[String, Statement] = Map()
    override val seqStatements: Map[String, Seq[Statement]] = Map()
  }

  val  pf=new ProvFactory

  def convertToTransformEnvironment(document: Document, dictionary: Dictionary, context: Map[String,String], profiles: Map[String,Object], theProfile: String): TransformEnvironment = {
    val m1= scala.collection.mutable.Map[String,Statement]()
    val m2= scala.collection.mutable.Map[String,List[Statement]]()

    document.getStatementOrBundle.forEach {
      case b: Bundle => if (b.getStatement == null) {

      } else if (b.getStatement.size() == 0) {

      } else if (b.getStatement.size() == 1) {
        m1.put(b.getId.getLocalPart, Statement(b.getStatement.get(0)))
      } else {
        m2.getOrElseUpdate(b.getId.getLocalPart, List())
        b.getStatement.forEach(s => {
          val ss: immutable.List[Statement] = m2(b.getId.getLocalPart)
          m2.put(b.getId.getLocalPart, Statement(s) :: ss)
        })
      }
      case _ =>
    }

    new TransformEnvironment {
      override val environment: Environment = Environment(context, Seq(dictionary), profiles, theProfile)
      override val statements: Map[String, Statement] = m1.toMap
      override val seqStatements: Map[String, Seq[Statement]] = m2.toMap

    }
  }


  @JsonTypeInfo(use = Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
  @JsonSubTypes(Array(
    new Type(value = classOf[Clause],            name=Constants.CLAUSE),
    new Type(value = classOf[NounPhrase],        name=Constants.NOUN_PHRASE),
    new Type(value = classOf[VerbPhrase],        name=Constants.VERB_PHRASE),
    new Type(value = classOf[AdjectivePhrase],   name=Constants.ADJECTIVE_PHRASE),
    new Type(value = classOf[AdverbPhrase],      name=Constants.ADVERB_PHRASE),
    new Type(value = classOf[CoordinatedPhrase], name=Constants.COORDINATED_PHRASE),
    new Type(value = classOf[PrepositionPhrase], name=Constants.PREPOSITION_PHRASE),
    new Type(value = classOf[StringPhrase],      name=Constants.STRING_PHRASE),
    new Type(value = classOf[PhraseIterator],    name=Constants.AT_ITERATOR),
    new Type(value = classOf[Funcall],           name=Constants.AT_FUNCALL),
    new Type(value = classOf[Paragraph],         name=Constants.PARAGRAPH)
  ))
  @JsonInclude(JsonInclude.Include.NON_NULL)
  trait Phrase extends Object with Constants with ExportableToBlockly {
    def expand(): Object
    def transform[F <: Phrase](e: TransformEnvironment)(implicit c: ClassTag[F]):  Option[F]
    def toBlockly(): Block
  }

  type Head = String

  type Features=Map[String,Object]

  @JsonTypeInfo(use = Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
  @JsonSubTypes(Array(
    new Type(value = classOf[NounPhrase],        name=Constants.NOUN_PHRASE),
    new Type(value = classOf[CoordinatedPhrase], name=Constants.COORDINATED_PHRASE),
    new Type(value = classOf[Funcall],           name=Constants.AT_FUNCALL),
    new Type(value = classOf[StringPhrase],      name=Constants.STRING_PHRASE)
  ))
  trait NounForm extends Phrase{
    def transform[F <: Phrase](e: TransformEnvironment)(implicit c: ClassTag[F]): Option[F]
    def toBlockly(): Block
  }

  @JsonTypeInfo(use = Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
  @JsonSubTypes(Array(
    new Type(value = classOf[Funcall],            name=Constants.AT_FUNCALL),
    new Type(value = classOf[StringPhrase],       name=Constants.STRING_PHRASE)
  ))
  trait HeadForm extends Phrase {
    def transform[F <: Phrase](e: TransformEnvironment)(implicit c: ClassTag[F]): Option[F]
  }

  @JsonTypeInfo(use = Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
  @JsonSubTypes(Array(
    new Type(value = classOf[VerbPhrase],        name=Constants.VERB_PHRASE),
    new Type(value = classOf[CoordinatedPhrase], name=Constants.COORDINATED_PHRASE),
    new Type(value = classOf[Funcall],           name=Constants.AT_FUNCALL),
    new Type(value = classOf[StringPhrase],      name=Constants.STRING_PHRASE)
  ))
  trait VerbForm extends Phrase{
    def transform[F <: Phrase](e: TransformEnvironment)(implicit c: ClassTag[F]): Option[F]
    def toBlockly(): Block
  }

  trait HasFrontModifiers {
    val front_modifiers: Seq[Phrase]
    def expandFrontModifiers(element: PhraseElement): Unit = {
      if (front_modifiers!=null) front_modifiers.foreach(front_modifier => element.addFrontModifier(front_modifier.expand().asInstanceOf[NLGElement]))
    }
    def front_modifiersToBlockly(l: util.List[BlocklyContents]): Unit = {
      if (front_modifiers != null) {
        l.add(exporter.toBlocklyArray("FRONT_MODIFIERS", front_modifiers))
      }
    }
  }
  trait HasPreModifiers {
    val pre_modifiers: Seq[Phrase]
    def expandPreModifiers(element: PhraseElement): Unit = {
      if (pre_modifiers!=null) pre_modifiers.foreach(pre_modifier => element.addPreModifier(pre_modifier.expand().asInstanceOf[NLGElement]))
    }
    def pre_modifiersToBlockly(l: util.List[BlocklyContents]): Unit = {
      if (pre_modifiers != null) {
        l.add(exporter.toBlocklyArray("PRE_MODIFIERS", pre_modifiers))
      }
    }
  }
  trait HasModifiers {
    val modifiers: Seq[Phrase]
    def expandModifiers(element: PhraseElement): Unit = {
      if (modifiers!=null)  modifiers.foreach(modifier => element.addModifier(modifier.expand().asInstanceOf[NLGElement]))
    }
    def modifiersToBlockly(l: util.List[BlocklyContents]): Unit = {
      if (modifiers != null) {
        l.add(exporter.toBlocklyArray("MODIFIERS", modifiers))
      }
    }
  }
  trait HasPostModifiers {
    val post_modifiers: Seq[Phrase]
    def expandPostModifiers(element: PhraseElement): Unit = {
      if (post_modifiers!=null) post_modifiers.foreach(post_modifier => element.addPostModifier(post_modifier.expand().asInstanceOf[NLGElement]))
    }
    def expandPostModifiers(element: CoordinatedPhraseElement): Unit = {
      if (post_modifiers!=null) post_modifiers.foreach(post_modifier => element.addPostModifier(post_modifier.expand().asInstanceOf[NLGElement]))
    }
    def post_modifiersToBlockly(l: util.List[BlocklyContents]): Unit = {
      if ((post_modifiers != null) && post_modifiers.nonEmpty) {
        l.add(exporter.toBlocklyArray("POST_MODIFIERS", post_modifiers))
      }
    }
  }
  trait HasComplements {
    val complements: Seq[Phrase]
    def expandComplements(element: PhraseElement): Unit = {
      if (complements!=null)  complements.foreach(complement => element.addComplement(complement.expand().asInstanceOf[NLGElement]))
    }
    def complementsToBlockly(l: util.List[BlocklyContents]): Unit = {
      if (complements != null) {
        l.add(exporter.toBlocklyArray("COMPLEMENTS", complements))
      }
    }
  }

  trait HasSpecifier {
    val specifier: Option[NounForm]
    def expandSpecifier(element: PhraseElement): Unit = {
      if ((specifier != null) && specifier.isDefined)
        element.setFeature(InternalFeature.SPECIFIER, specifier.get.expand())
    }
  }
  val specialKeywords: Seq[String] =List("markup_element", "markup_attributes", "head_markup_element", "head_markup_attributes").map(_.toUpperCase)

  trait HasFeatures {
    val features: Features

    def specialKey(s: String): Boolean = false

    def serializeValue(v: Object): String =  {
      v match {
        case s:String => s
        case m:Map[_,_] => SpecLoader.mapper.writeValueAsString(m)
        case o => o.toString
      }
    }


    // NOTE potential partial code duplication
    def newFeaturesBlock2(features: Map[String,Object]): Block = {
      //TODO Need to do expansion
      val id: Head = exporter.newId
      val l: util.List[BlocklyContents] = new util.LinkedList[BlocklyContents]()
      var first_block:Option[Block]=None
      var previous_block:Option[Block]=None
      features.foreach{ case (k:String,v:Object) => {
        val kUpper = k.toUpperCase()

        if (specialKey(kUpper)) { // always false
          val ll_pair:util.List[BlocklyContents] = new util.LinkedList[BlocklyContents]()

          val ll_key: util.List[BlocklyContents] = new util.LinkedList[BlocklyContents]()
          ll_key.add(new Field("TEXT", kUpper))
          val contents_key=exporter.newValueWithBlock("KEY",new Block(exporter.newId,"text",null,null,ll_key))
          ll_pair.add(contents_key)


          val ll_value: util.List[BlocklyContents] = new util.LinkedList[BlocklyContents]()
          ll_value.add(new Field("TEXT", serializeValue(v)))
          val contents_value=exporter.newValueWithBlock("VALUE",new Block(exporter.newId,"text",null,null,ll_value))
          ll_pair.add(contents_value)

          val block_pair=new Block(id, "map", null, null, ll_pair)

          previous_block match {
            case None =>
              previous_block=Some(block_pair)
              first_block=Some(block_pair) //TODO: should be statement
            case Some(b) =>
              val next=new Next(block_pair)
              b.getBody.add(next)
              previous_block=Some(block_pair)
          }






        } else {

          v match {
            case s: String =>
              if (kUpper.indexOf("MARKUP")== (-1)) {
                l.add(new Field(exporter.getKeyword(kUpper + "_CHOICE"), exporter.getValue(kUpper, s.toUpperCase)))
              } else {
                val blocks: util.List[Block]=new util.LinkedList[Block]()
                val ll: util.List[BlocklyContents] = new util.LinkedList[BlocklyContents]()
                ll.add(new Field("TEXT",s))
                val b=new Block(exporter.newId, "text", null,null,ll)
                blocks.add(b)
                l.add(new Value(kUpper,blocks))
              }
              l.add(new Field(exporter.getKeyword(kUpper + "_TICK"), "TRUE"))
            case _ =>
              if (kUpper.indexOf("MARKUP")== (-1)) {
                l.add(new Field(exporter.getKeyword(kUpper + "_CHOICE"), v.toString))
              } else {
                //println(v)
                //println(v.getClass)
                val m=v.asInstanceOf[Map[String,Object]]

                val funcall=new Funcall(Seq(),Seq(), Map())
                funcall.set(Keywords.FUNCTION,m(Keywords.FUNCTION))
                funcall.set(Keywords.OBJECT,m(Keywords.OBJECT))
                funcall.set(Keywords.FIELD,m(Keywords.FIELD))
                funcall.set(Keywords.ARG1,m(Keywords.ARG1))
                val b=funcall.toBlockly()
                val blocks: util.List[Block]=new util.LinkedList[Block]()
                blocks.add(b)
                l.add(new Value(kUpper,blocks))
              }
              l.add(new Field(exporter.getKeyword(kUpper + "_TICK"), "TRUE"))
          }
        }

      }}

      if (first_block.isDefined) {
        l.add(new nlg.wrapper.Statement("MAP",first_block.get))
      }
      new Block(id, "features", null, null, l)
    }

    def featuresToBlockly(l: util.List[BlocklyContents]): Unit = {
      if ((features != null) && features.nonEmpty) {
        l.add(exporter.newValueWithBlock("FEATURES", newFeaturesBlock2(features)))
      }
    }


    def processFeature(key: String, value: Object, element: NLGElement): Unit = {

      def setFeature(feature: String, value: Any): Unit ={
        element.setFeature(feature,value)
      }

      value match {
        case value: String =>

          key match {
            case "tense" =>          setFeature(Feature.TENSE,          value match { case "past" => Tense.PAST case "present" => Tense.PRESENT case "future" => Tense.FUTURE })
            case "number" =>         setFeature(Feature.NUMBER,         value match { case "singular" => NumberAgreement.SINGULAR case "plural" => NumberAgreement.PLURAL })
            case "passive" =>        setFeature(Feature.PASSIVE,        value.toBoolean)
            case "perfect" =>        setFeature(Feature.PERFECT,        value.toBoolean)
            case "progressive" =>    setFeature(Feature.PROGRESSIVE,    value.toBoolean)
            case "cue_phrase" =>     setFeature(Feature.CUE_PHRASE,     value)
            case "complementiser" => setFeature(Feature.COMPLEMENTISER, value)
            case "form"           => setFeature(Feature.FORM,           value match { case "pastParticiple" => Form.PAST_PARTICIPLE case "presentParticiple" => Form.PRESENT_PARTICIPLE})
            case "pronominal" =>     setFeature(Feature.PRONOMINAL,     value.toBoolean)
            case "possessive" =>     setFeature(Feature.POSSESSIVE,     value.toBoolean)
            case "gender" =>         setFeature(LexicalFeature.GENDER,  value match { case "masculine" => Gender.MASCULINE case "feminine" => Gender.FEMININE case "neutral" => Gender.NEUTER })
            case "person" =>         setFeature(Feature.PERSON,         value match { case "first" => Person.FIRST case "second" => Person.SECOND case "third" => Person.THIRD })

            case key              => setFeature(key,value)

          }
        case m:Features @ unchecked =>
          def cheating() = {
            if (m.getOrElse(Keywords.OBJECT,"") == "der") "plural" else "singular"
          }

          println(m)
          processFeature(key,cheating(),element)  //TODO: need to calculate function instead of cheatign!
      }
    }

    def expandFeatures(element: NLGElement): Unit = {
      if (features!=null) features.foreach{case (k,v) => processFeature(k,v,element)}
    }
  }


}

object defs  {
  import specTypes._

  //val defaultLexicon: Lexicon = Lexicon.getDefaultLexicon// =org.openprovenance.prov.scala.wrapper.defs.lexicon //. Lexicon.getDefaultLexicon

  //val nlgFactory: NLGFactory =org.openprovenance.prov.scala.wrapper.defs.nlgFactory //new NLGFactory(defaultLexicon) // org.openprovenance.prov.scala.wrapper.defs.nlgFactory //new NLGFactory(lexicon)

  case class Template(var name:String,
                      select: Map[String,Map[String,String]],
                      query: Object,
                      sentence: Phrase,
                      context: Map[String,String],
                      where: Object)





  case class StringPhrase(value: String) extends Phrase with NounForm with VerbForm with HeadForm {
    override def expand (): String = value

    override def transform[T <: Phrase](e: TransformEnvironment)(implicit c: ClassTag[T]): Option[T] = {
      if (value.startsWith("##")) {
        val snippet: Option[Phrase] =e.environment.snippets.get(value)
        if (snippet.isDefined) {
          snippet.flatMap(_.transform[T](e))
        } else
          Some(this.asInstanceOf[T])
      } else {
        Some(this.asInstanceOf[T])
      }
    }

    override def toBlockly(): Block = {
      exporter.newBlockWithTextField(value)
    }


  }


  def transform_snippets(s:String, environment: Environment): Option[Object] = {
    if (s.startsWith("##")) environment.snippets.get(s) else  None
  }

  case class Clause(@JsonDeserialize(using = classOf[CustomOptionNounFormDeserializer]) subject:  Option[NounForm],
                    @JsonDeserialize(using = classOf[CustomOptionVerbFormDeserializer]) verb:     Option[VerbForm],
                    @JsonProperty("object")
                    direct_object:   Option[Phrase],
                    indirect_object: Option[Phrase],
                    complements:     Seq[Phrase]=Seq(),
                    modifiers:       Seq[Phrase]=Seq(),
                    @JsonProperty("front-modifiers")
                    front_modifiers:  Seq[Phrase]=Seq(),
                    @JsonProperty("pre-modifiers")
                    pre_modifiers:  Seq[Phrase]=Seq(),
                    @JsonProperty("post-modifiers")
                    post_modifiers: Seq[Phrase]=Seq(),
                    complementiser:  String=null,
                    comments:        String=null,
                    features:        Features=Map()) extends Phrase with HasModifiers with HasPreModifiers with HasPostModifiers with HasFrontModifiers with HasFeatures with HasComplements {

    @Override
    def expand (): PhraseElement = {
      val res=nlgFactory.createClause()
      if (subject!=null) subject.foreach(subject => res.setSubject(subject.asInstanceOf[Phrase].expand()))
      if (verb!=null) verb.foreach(verb => res.setVerb(verb.asInstanceOf[Phrase].expand()))
      direct_object.foreach(obj => res.setObject(obj.expand()))
      indirect_object.foreach(obj => res.setIndirectObject(obj.expand()))

      expandModifiers(res)
      expandPreModifiers(res)
      expandFrontModifiers(res)
      expandPostModifiers(res)
      expandFeatures(res)
      expandComplements(res)
      if (complementiser!=null) res.setFeature(Feature.COMPLEMENTISER,complementiser)
      res
    }

    def transform[F <: Phrase](e: TransformEnvironment)(implicit c: ClassTag[F]): Option[F] = {
      Some(Clause(  nullOpt(subject).flatMap(s => s.transform[NounForm](e)),
                    nullOpt(verb).flatMap(s => s.transform[VerbForm](e)),
                    nullOpt(direct_object).flatMap(s => s.transform[Phrase](e)),
                    nullOpt(indirect_object).flatMap(s => s.transform[Phrase](e)),
                    nullSeq(complements).flatMap(s => s.transform[Phrase](e)),
                    nullSeq(modifiers).flatMap(s => s.transform[Phrase](e)),
                    nullSeq(front_modifiers).flatMap(s => s.transform[Phrase](e)),
                    nullSeq(pre_modifiers).flatMap(s => s.transform[Phrase](e)),
                    nullSeq(post_modifiers).flatMap(s => s.transform[Phrase](e)),
                    complementiser,
                    null,
                    features).asInstanceOf[F])
    }


    override def toBlockly(): Block = {
      val id: Head = exporter.newId
      val l: util.List[BlocklyContents] = new util.LinkedList[BlocklyContents]()
      if (subject!=null)         subject.map(s => exporter.newValueWithBlock("SUBJECT",s.toBlockly())).foreach(v => l.add(v))
      if (verb!=null)            verb.map(s => exporter.newValueWithBlock("VERB",s.toBlockly())).foreach(v => l.add(v))
      if (direct_object!=null)   direct_object.map(s => exporter.newValueWithBlock("DIRECT_OBJECT",s.toBlockly())).foreach(v => l.add(v))
      if (indirect_object!=null) indirect_object.map(s => exporter.newValueWithBlock("INDIRECT_OBJECT",s.toBlockly())).foreach(v => l.add(v))
      modifiersToBlockly(l)
      front_modifiersToBlockly(l)
      pre_modifiersToBlockly(l)
      post_modifiersToBlockly(l)
      featuresToBlockly(l)
      if (complementiser != null) { l.add(exporter.newValueWithBlock("COMPLEMENTISER", exporter.newBlockWithTextField(complementiser)))}
      complementsToBlockly(l)
      new Block(id, "clause",null,null,l)
    }

  }

  def nullFeatures(features: Features): Features = {
    if (features==null) Map() else features
  }
  def nullSeq[T](v:Seq[T]): Seq[T] = {
    if (v==null) Seq() else v
  }
  def nullOpt[T](v:Option[T]): Option[T] = {
    if (v==null) None else v
  }
  case class NounPhrase(determiner:     String,
                        @JsonDeserialize(using = classOf[CustomHeadFormDeserializer])
                        head:           Option[HeadForm],
                        modifiers:      Seq[Phrase]=Seq(),
                        @JsonProperty("pre-modifiers")
                        pre_modifiers:  Seq[Phrase]=Seq(),
                        @JsonProperty("post-modifiers")
                        post_modifiers: Seq[Phrase]=Seq(),
                        @JsonDeserialize(using = classOf[CustomOptionNounFormDeserializer]) specifier:      Option[NounForm]=None,
                        complements:    Seq[Phrase]=Seq(),
                        comments:       String=null,
                        features:       Features=Map())
    extends Phrase with NounForm with HasPreModifiers with HasModifiers with HasPostModifiers with HasFeatures with HasComplements with HasSpecifier {

    @Override
    def expand (): NPPhraseSpec = {
      val res= nlgFactory.createNounPhrase()

      res.setNoun(head.get.asInstanceOf[StringPhrase].expand())
      res.setDeterminer(determiner)

      expandModifiers(res)
      expandPreModifiers(res)
      expandPostModifiers(res)
      expandComplements(res)
      expandFeatures(res)
      expandSpecifier(res)

      res
    }





    def transform[F <: Phrase](e: TransformEnvironment)(implicit c: ClassTag[F]): Option[F] = {
      Some(NounPhrase(  determiner,
                        head.flatMap(s => s.transform[StringPhrase](e)),
                        nullSeq(modifiers).flatMap(s => s.transform(e)),
                        nullSeq(pre_modifiers).flatMap(s => s.transform[Phrase](e)),
                        nullSeq(post_modifiers).flatMap(s => s.transform[Phrase](e)),
                        nullOpt(specifier).flatMap(s => s.transform[NounForm](e)),
                        nullSeq(complements).flatMap(s => s.transform[Phrase](e)),
                        comments,
                        getComputedFeatures(nullFeatures(features), e)).asInstanceOf[F])
          }


    override def toBlockly(): Block = {
      val id: Head = exporter.newId
      val l: util.List[BlocklyContents] = new util.LinkedList[BlocklyContents]()
      if (head!=null)  head.foreach(h => l.add(exporter.newValueWithBlock("HEAD", h.toBlockly())))
      if (determiner!=null) l.add(exporter.newValueWithBlock("DETERMINER", exporter.newBlockWithTextField(determiner)))
      pre_modifiersToBlockly(l)
      post_modifiersToBlockly(l)
      modifiersToBlockly(l)
      if (specifier!=null) {
        specifier.foreach(v => l.add(exporter.newValueWithBlock("SPECIFIER", v.toBlockly())))
      }
      complementsToBlockly(l)
      featuresToBlockly(l)
      new Block(id, "noun_phrase",null,null,l)
    }
  }


  case class VerbPhrase(head: Head,
                        @JsonProperty("object")@JsonDeserialize(using = classOf[CustomOptionPhraseDeserializer])
                        direct_object:   Option[Phrase]=None,
                        indirect_object: Option[Phrase]=None,
                        modifiers: Seq[Phrase]=Seq(),
                        @JsonProperty("pre-modifiers")
                        pre_modifiers: Seq[Phrase]=Seq(),
                        @JsonProperty("post-modifiers")
                        post_modifiers: Seq[Phrase]=Seq(),
                        complements:     Seq[Phrase]=Seq(),
                        features: Features=Map())
    extends Phrase with VerbForm with HasPreModifiers with HasModifiers with HasPostModifiers with HasFeatures with HasComplements {

    @Override
    def expand (): VPPhraseSpec = {
      val res= nlgFactory.createVerbPhrase()
      res.setHead(head)
      nullOpt(direct_object).foreach(obj => res.setObject(obj.expand()))
      indirect_object.foreach(obj => res.setIndirectObject(obj.expand()))
      expandModifiers(res)
      expandPreModifiers(res)
      expandPostModifiers(res)
      expandComplements(res)
      expandFeatures(res)

      res
    }


    override def transform[F <: Phrase](e: TransformEnvironment)(implicit c: ClassTag[F]): Option[F] = {
      Some(VerbPhrase(head,
                      nullOpt(direct_object).flatMap(s => s.transform[Phrase](e)),
                      nullOpt(indirect_object).flatMap(s => s.transform[Phrase](e)),
                      nullSeq(modifiers).flatMap(s => s.transform(e)),
                      nullSeq(pre_modifiers).flatMap(s => s.transform(e)),
                      nullSeq(post_modifiers).flatMap(s => s.transform(e)),
                      nullSeq(complements).flatMap(s => s.transform[Phrase](e)),
                      features).asInstanceOf[F])
    }


    override def toBlockly(): Block = {
      val id: Head = exporter.newId
      val l: util.List[BlocklyContents] = new util.LinkedList[BlocklyContents]()
      if (head!=null) {
        l.add(exporter.newValueWithBlock("HEAD", exporter.newBlockWithTextField(head)))
      }
      if (direct_object!=null)   direct_object.map(s => exporter.newValueWithBlock("DIRECT_OBJECT",s.toBlockly())).foreach(v => l.add(v))
      if (indirect_object!=null) indirect_object.map(s => exporter.newValueWithBlock("INDIRECT_OBJECT",s.toBlockly())).foreach(v => l.add(v))
      pre_modifiersToBlockly(l)
      post_modifiersToBlockly(l)
      modifiersToBlockly(l)
      complementsToBlockly(l)
      featuresToBlockly(l)
      new Block(id, "verb_phrase",null,null,l)
    }

  }


  case class AdjectivePhrase(@JsonDeserialize(using = classOf[CustomHeadFormDeserializer])
                             head: Option[HeadForm],
                             modifiers: Seq[Phrase]=Seq(),
                             @JsonProperty("pre-modifiers")
                             pre_modifiers: Seq[Phrase]=Seq(),
                             @JsonProperty("post-modifiers")
                             post_modifiers: Seq[Phrase]=Seq(),
                             features: Features=Map())
    extends Phrase with HasPreModifiers with HasModifiers with HasPostModifiers with HasFeatures {

    @Override
    def expand (): AdjPhraseSpec = {

      val res = nlgFactory.createAdjectivePhrase()
      res.setHead(head.get.asInstanceOf[StringPhrase].expand())

      expandModifiers(res)
      expandPreModifiers(res)
      expandPostModifiers(res)
      expandFeatures(res)

      res
    }

    override def transform[F <: Phrase](e: TransformEnvironment)(implicit c: ClassTag[F]): Option[F] = {
      Some(AdjectivePhrase(head.flatMap(s => s.transform[HeadForm](e)),
                          nullSeq(modifiers).flatMap(s => s.transform[Phrase](e)),
                          nullSeq(pre_modifiers).flatMap(s => s.transform[Phrase](e)),
                          nullSeq(post_modifiers).flatMap(s => s.transform[Phrase](e)),
                          getComputedFeatures(nullFeatures(features), e)).asInstanceOf[F])
    }
    override def toBlockly(): Block = {
      val id: Head = exporter.newId
      val l: util.List[BlocklyContents] = new util.LinkedList[BlocklyContents]()
      if (head!=null) {
        head.foreach(h => l.add(exporter.newValueWithBlock("HEAD",h.toBlockly())))
      }
      pre_modifiersToBlockly(l)
      post_modifiersToBlockly(l)
      modifiersToBlockly(l)
      featuresToBlockly(l)
      new Block(id, "adjective_phrase",null,null,l)
    }

  }


  case class AdverbPhrase(@JsonDeserialize(using = classOf[CustomHeadFormDeserializer])
                             head: Option[HeadForm],
                             modifiers: Seq[Phrase]=Seq(),
                             @JsonProperty("pre-modifiers")
                             pre_modifiers: Seq[Phrase]=Seq(),
                             @JsonProperty("post-modifiers")
                             post_modifiers: Seq[Phrase]=Seq(),
                             features: Features=Map())
    extends Phrase with HasPreModifiers with HasModifiers with HasPostModifiers with HasFeatures {

    @Override
    def expand (): AdvPhraseSpec = {

      val res = nlgFactory.createAdverbPhrase()
      res.setHead(head.get.asInstanceOf[StringPhrase].expand())

      expandModifiers(res)
      expandPreModifiers(res)
      expandPostModifiers(res)
      expandFeatures(res)

      res
    }

    override def transform[F <: Phrase](e: TransformEnvironment)(implicit c: ClassTag[F]): Option[F] = {
      Some(AdverbPhrase(head.flatMap(s => s.transform[HeadForm](e)),
        nullSeq(modifiers).flatMap(s => s.transform[Phrase](e)),
        nullSeq(pre_modifiers).flatMap(s => s.transform[Phrase](e)),
        nullSeq(post_modifiers).flatMap(s => s.transform[Phrase](e)),
        getComputedFeatures(nullFeatures(features), e)).asInstanceOf[F])
    }
    override def toBlockly(): Block = {
      val id: Head = exporter.newId
      val l: util.List[BlocklyContents] = new util.LinkedList[BlocklyContents]()
      if (head!=null) {
        head.foreach(h => l.add(exporter.newValueWithBlock("HEAD",h.toBlockly())))
      }
      pre_modifiersToBlockly(l)
      post_modifiersToBlockly(l)
      modifiersToBlockly(l)
      featuresToBlockly(l)
      new Block(id, "adverb_phrase",null,null,l)
    }

  }

  case class PrepositionPhrase(preposition: String,
                               @JsonDeserialize(using = classOf[CustomOptionNounFormDeserializer]) noun: Option[NounForm],  //TODO: I HAD TO MAKE THIS AN OPTION, FOR THE DESERIALIZER TO BE CALLED???
                               complements: Seq[Phrase]=Seq(),
                               specifier: Option[NounPhrase]=None,
                               features: Features=Map()) extends Phrase with HasComplements with HasSpecifier with HasFeatures {

    @Override
    def expand (): PPPhraseSpec = {

      val res = nlgFactory.createPrepositionPhrase()

      res.setPreposition(preposition)
      noun.get.asInstanceOf[Phrase].expand() match {
        case s:String =>  res.addComplement(s)
        case o: NLGElement =>  res.addComplement(o)
      }

      expandComplements(res)
      expandSpecifier(res)
      expandFeatures(res)


      res
    }

    override def transform[F <: Phrase](e: TransformEnvironment)(implicit c: ClassTag[F]): Option[F] = {
      val noun2: Option[NounForm] =nullOpt(noun).flatMap(s => s.transform[NounForm](e))
      noun2.map(n =>
          PrepositionPhrase(  preposition,
                              noun2, // make sure noun2 is defined
                              nullSeq(complements).flatMap(s => s.transform[Phrase](e)),
                              nullOpt(specifier).flatMap(s => s.transform[NounPhrase](e)),
                              getComputedFeatures(nullFeatures(features), e)).asInstanceOf[F])
    }

    override def toBlockly(): Block = {
      val id: Head = exporter.newId
      val l: util.List[BlocklyContents] = new util.LinkedList[BlocklyContents]()
      if (preposition!=null) {
        l.add(exporter.newValueWithBlock("PREPOSITION", exporter.newBlockWithTextField(preposition)))
      }
      if (noun!=null) {
        noun.foreach(n => l.add(exporter.newValueWithBlock("NOUN", n.toBlockly())))
      }
      if (specifier!=null) specifier.foreach(s => l.add(exporter.newValueWithBlock("SPECIFIER",s.toBlockly())))
      complementsToBlockly(l)
      featuresToBlockly(l)
      new Block(id, "preposition_phrase",null,null,l)
    }
  }

  case class CoordinatedPhrase(head: Head,
                               coordinates: Seq[Phrase]=Seq(),
                               //@JsonProperty("@posteval") posteval: Object,                 //TODO: DELETE
                               @JsonProperty(Constants.AT_ITERATOR) iterator: Option[PhraseIterator],
                               //@JsonProperty("@pick")     pick:     Option[PhraseIterator], //TODO: DELETE
                               conjunction: String,
                               @JsonProperty("post-modifiers")
                               post_modifiers: Seq[Phrase]=Seq(),
                               features:    Features=Map()) extends Phrase with NounForm with VerbForm with HasPostModifiers with HasFeatures {
    def expand (): CoordinatedPhraseElement = {
      val res=nlgFactory.createCoordinatedPhrase
      coordinates.foreach(coordinate => res.addCoordinate(coordinate.expand()))

      expandFeatures(res)
      expandPostModifiers(res)

      res.setFeature(simplenlg.features.Feature.CONJUNCTION,conjunction)
      res
    }

    override def transform[F <: Phrase](e: TransformEnvironment)(implicit c: ClassTag[F]): Option[F] = {

      val v: Option[PhraseIterator] =nullOpt(iterator)

      v match {
        case None =>
          Some(CoordinatedPhrase( head,
                                  nullSeq(coordinates).flatMap(s => s.transform[Phrase](e)),
                                  nullOpt(iterator).flatMap(s => s.transform[PhraseIterator](e)),
                                  conjunction,
                                  nullSeq(post_modifiers).flatMap(s => s.transform[Phrase](e)),
                                  features).asInstanceOf[F])

        case Some(iterator) =>
          iterator.head=head
          iterator.conjunction=conjunction
          iterator.features=features

          iterator.transform[F](e)
      }
    }

    override def toBlockly(): Block = {
      val id: Head = exporter.newId
      val l: util.List[BlocklyContents] = new util.LinkedList[BlocklyContents]()
      if (conjunction!=null) l.add(exporter.newValueWithBlock("CONJUNCTION", exporter.newBlockWithTextField(conjunction)))
      if (coordinates!=null) l.add(exporter.toBlocklyArray("COORDINATES", coordinates))
      if (iterator!=null) iterator.foreach(it => l.add(exporter.newValueWithBlock("PHRASE_ITERATOR",it.toBlockly())))
      post_modifiersToBlockly(l)
      featuresToBlockly(l)
      new Block(id, "coordinated_phrase",null,null,l)
    }
  }

  case class PhraseIterator (@JsonProperty("@variable") variable:     String,
                             @JsonProperty("@until")    until_string: String,
                             @JsonProperty("@from")     from_string:  String,
                             @JsonProperty("@flatten")  flatten:      String,
                             @JsonProperty("@clause")   clause:       String,
                             @JsonProperty("@property") property:     String,
                             @JsonProperty("@element")  element:      Phrase,
                             @JsonProperty(Constants.AT_ITERATOR) iterator:     PhraseIterator) extends Phrase {

    var head:Head=null
    var conjunction:String=null
    var features:Features=null

    val until_value: Int => Int =if (until_string==null) ((len:Int) => len) else ((len:Int) => until_string.toInt)
    val from_value: Int =if (from_string==null)  0 else from_string.toInt
    override def expand(): AnyRef = ???

    val flatten_value: Boolean =if (flatten==null) false else flatten.toBoolean


    override def toBlockly(): Block = {
      val id: Head = exporter.newId
      val l: util.List[BlocklyContents] = new util.LinkedList[BlocklyContents]()

      if (variable!=null)     l.add(exporter.newValueWithBlock("VARIABLE",exporter.newBlockWithTextField(variable)))
      if (until_string!=null) l.add(exporter.newValueWithBlock("UNTIL_STRING",exporter.newBlockWithTextField(until_string)))
      if (from_string!=null)  l.add(exporter.newValueWithBlock("FROM",exporter.newBlockWithTextField(from_string)))
      if (flatten!=null)      l.add(exporter.newValueWithBlock("FLATTEN",exporter.newBlockWithTextField(flatten)))
      if (clause!=null)       l.add(exporter.newValueWithBlock("CLAUSE",exporter.newBlockWithTextField(clause)))
      if (property!=null)     l.add(exporter.newValueWithBlock("PROPERTY",exporter.newBlockWithTextField(property)))
      if (element!=null)      l.add(exporter.newValueWithBlock("ELEMENT",element.toBlockly()))
      if (iterator!=null)     l.add(exporter.newValueWithBlock("ITERATOR",iterator.toBlockly()))

      new Block(id, "phrase_iterator",null,null,l)
    }


    override def transform[F <: Phrase](e: TransformEnvironment)(implicit c: ClassTag[F]): Option[F] = {

      val variables: Array[String] = variable.split(",").map(_.trim)

      property match {
        case null =>
          variables match {
            case Array("@values") => transformIteratorOverValues[F] (e)
            case _ => transformIteratorOverAttributes[F] (e, variables)
          }
        case _ => transformIteratorOverProperties[F](e, variables)
      }
    }

    private def transformIteratorOverValues[F <: Phrase](e: TransformEnvironment)(implicit c: ClassTag[F]): Option[F] = {

//      println("--- transformOverValues")
      val result=element.transform[F](e)

      result
    }

    private def transformIteratorOverAttributes[F <: Phrase](e: TransformEnvironment, variables: Array[String])(implicit c: ClassTag[F]): Option[F] = {
     // val trimmedClause: String = clause.trim

      val statementSequences: Array[Seq[Statement]] = variables.map(e.seqStatements)
      val length: Int = statementSequences.map(_.size).max


      val all_indices: Array[Int] = (from_value until (until_value(length))).toArray


      val values_indexed: Array[Array[(String, Statement)]] = all_indices.map(i => variables.zip(statementSequences.map(seq => if (i > seq.size) seq.last: Statement else seq(i): Statement)))

      val phrases: Seq[F] = values_indexed.flatMap(bindings => {
        val newTe = new TransformEnvironment {
          override val environment: Environment = e.environment
          override val statements: Map[String, Statement] = e.statements ++ bindings
          override val seqStatements: Map[String, Seq[Statement]] = e.seqStatements
        }
        element.transform[F](newTe)
      })


      if (flatten_value) {
        val coordinates = phrases.flatMap(phrase => phrase.asInstanceOf[CoordinatedPhrase].coordinates)
        CoordinatedPhrase(head, coordinates, None, conjunction,Seq(),features).transform[F](e)
      } else {
        CoordinatedPhrase(head, phrases, None, conjunction,Seq(),features).transform[F](e)
      }
    }

    private def transformIteratorOverProperties[F <: Phrase](e: TransformEnvironment, variables: Array[String])(implicit c: ClassTag[F]): Option[F] = {
      val num_attributes: Int = Primitive.applyProperty(Keywords.ATTRIBUTES, None, e.statements(variables(0)), e.environment)._1.size

      val all_indices: Array[Int] = (0 until num_attributes).toArray

      val phrases: Seq[F] = all_indices.flatMap(index => {
        val funcall = updateFuncallIndex(element, index)
        funcall.transform[F](e)
      })

      CoordinatedPhrase(head, phrases,  None, conjunction).transform[F](e)
    }
  }

  def updateFuncallIndex[F <: Phrase](element: Phrase, index: Int): Funcall = {
    val fun = element.asInstanceOf[Funcall]
    val fun2=fun.copy()
    fun.copyIntoWithIndex(fun2,index)
    fun2

  }

  case class Funcall(@JsonProperty("post-modifiers")
                     post_modifiers: Seq[Phrase]=Seq(),
                     @JsonProperty("@args")
                     args:           Seq[Phrase],
                     //@JsonProperty("features")
                     features:      Features=Map()) extends Phrase with HeadForm with NounForm with VerbForm  with HasFeatures with HasPostModifiers {
    private val properties = scala.collection.mutable.Map[String,Object]()


    def copyIntoWithIndex(other: Funcall, index: Int): Unit = {
      properties.foreach{case (k,v) => other.set(k,if (k=="@index") index.toString else v)}
    }

    import com.fasterxml.jackson.annotation.JsonAnySetter

    override def toBlockly(): Block = {
      val id: Head = exporter.newId
      val l: util.List[BlocklyContents] = new util.LinkedList[BlocklyContents]()

      properties.foreach{case (k,v) =>
          l.add(exporter.newValueWithBlock(k.toUpperCase.substring(1), exporter.newBlockWithTextField(v.asInstanceOf[String])))
      }

      post_modifiersToBlockly(l)
      featuresToBlockly(l)
      new Block(id, "funcall",null,null,l)
    }


    @JsonAnySetter def set(fieldName: String, value: Object): Unit = {
      properties.put(fieldName, value)
    }

    @JsonAnyGetter
    def get (): java.util.Map[String, Object] = {
      properties.asJava
    }

    def get(fieldName: String): Any = properties(fieldName)

    override def toString (): String = {
      "<" + super.toString + " " + properties + ">"
    }

    override def expand(): AnyRef = ???


    override def transform[F <: Phrase](e: TransformEnvironment)(implicit c: ClassTag[F]): Option[Phrase with F]  = {

      if (args!=null && args.nonEmpty) {

        transformWithArgs[F](e)

      } else {

        val statements: Map[String, Statement] = e.statements
        e.seqStatements

        def friendly(s:String):Statement ={
          try {
            statements(s)
          } catch {
            case e: Throwable =>
              println("current statements " + statements)
              throw e
          }
        }

        val result: (Option[Result], Set[primitive.Triple]) = Primitive.processFunction(friendly, properties.toMap, e.environment)

        val modifiers: Seq[Phrase] = nullSeq(post_modifiers).flatMap(s => s.transform[Phrase](e))

        val cfeatures: Features =getComputedFeatures(nullFeatures(features), e)

        result._1 match {
          case None => {
            val option: Boolean = properties.getOrElse(Keywords.OPTIONAL, "false").toString.toBoolean
            if (option) {
              None
            } else {
              Some(StringPhrase("<nothing>").asInstanceOf[Phrase with F])
            }
          }
          case Some(Result(Some(s), None, None, None)) =>
            Some(StringPhrase(s).asInstanceOf[Phrase with F])

          case Some(Result(None, Some(p), None, None)) =>
            val m2: Phrase = addFeaturesToPhrase(cfeatures,addModifierToPhrase(modifiers, p))
            val x=m2.transform[Phrase with F](e).map(castToStringPhrase(_)).asInstanceOf[Option[Phrase with F]]
            x

          case Some(Result(None, None, Some(m), None)) =>

            map2phrase(m, modifiers, cfeatures, e).asInstanceOf[Option[Phrase with F]]

          case Some(Result(None, None, None, Some(set))) =>
            Some(CoordinatedPhrase(null,set.map(s=>s.toString).map(StringPhrase).toSeq,null,null,Seq(),cfeatures).asInstanceOf[Phrase with F])

          case Some(Result(_, _,_,_)) => throw new UnsupportedOperationException
        }
      }
    }

    private def transformWithArgs[F <: Phrase](e: TransformEnvironment): Option[Phrase with F] = {
//      println("HACK HACK HACK")

      val v1: Seq[Option[CoordinatedPhrase]] = args.map(arg => arg.transform[CoordinatedPhrase](e))

      val vv1: Seq[Seq[String]] = v1.map(value => value.get.coordinates.map(p => p.asInstanceOf[StringPhrase].value))

      val result: Object = Primitive.applyPatternFun(properties(Keywords.FUNCTION).asInstanceOf[String], e.environment, vv1.head.mkString("", ",", ""), Some(vv1(1).mkString("", ",", "")))
      // LUC TODO: don't convert vv1(0) and vv1(1) into strings. Keep them as seq, to be able to perform operation on them
      //val result2: Set[String] = Primitive.applyFunction(properties(Keywords.FUNCTION).asInstanceOf[String], e.environment, vv1(0), vv1(1))

      val res1: Set[Object] = result.asInstanceOf[Set[Object]]

      val res2: Set[Phrase] = res1.map {
        case r: Result => r.phrase.get
        case s: String => StringPhrase(s)
      }

      val post: Seq[Phrase]=nullSeq(post_modifiers).flatMap(arg => arg.transform[Phrase](e))

      CoordinatedPhrase(null, res2.toSeq, null, "and", post).transform[Phrase with F](e)
    }
  }

  def addFeaturesToPhrase(newfeatures: Features, p: Phrase) : Phrase = {
    val m2 = p match {
      case m: NounPhrase   => m.copy(features = nullFeatures(m.features) ++ newfeatures)
      case m: VerbPhrase   => m.copy(features = nullFeatures(m.features) ++ newfeatures)
      case m: AdverbPhrase => m.copy(features = nullFeatures(m.features) ++ newfeatures)
      case m: StringPhrase => m
    }
    m2
  }

  private def addModifierToPhrase[F <: Phrase](modifiers: Seq[Phrase], p: Phrase) : Phrase = {
    val m2 = p match {
      case m: NounPhrase   => m.copy(post_modifiers = modifiers)
      case m: VerbPhrase   => m.copy(post_modifiers = modifiers)
      case m: AdverbPhrase => m.copy(post_modifiers = modifiers)
      case m: StringPhrase => m
    }
    m2
  }


  private def castToStringPhrase(p: Phrase): Phrase = {

    if ((p.isInstanceOf[HasPostModifiers]
      &&  (p.asInstanceOf[HasPostModifiers].post_modifiers==null
      || p.asInstanceOf[HasPostModifiers].post_modifiers.isEmpty))

      &&
      (p.isInstanceOf[HasPreModifiers]
        &&  (p.asInstanceOf[HasPreModifiers].pre_modifiers==null
        || p.asInstanceOf[HasPreModifiers].pre_modifiers.isEmpty))

      &&
      (p.isInstanceOf[HasModifiers]
        &&  (p.asInstanceOf[HasModifiers].modifiers==null
        || p.asInstanceOf[HasModifiers].modifiers.isEmpty))

      && (p.isInstanceOf[HasFeatures]
      && (p.asInstanceOf[HasFeatures].features==null
      ||  p.asInstanceOf[HasFeatures].features.isEmpty))

      && (!p.isInstanceOf[HasSpecifier]
      ||  (p.isInstanceOf[HasSpecifier]
           && (p.asInstanceOf[HasSpecifier].specifier==null
      ||       !p.asInstanceOf[HasSpecifier].specifier.isDefined)))

      && (!p.isInstanceOf[HasComplements]
      || (p.asInstanceOf[HasComplements].complements == null
      || p.asInstanceOf[HasComplements].complements.isEmpty))

      && (!p.isInstanceOf[NounPhrase] || p.asInstanceOf[NounPhrase].determiner==null)) {

      p match {
        case n: NounPhrase   => if (n.head.isDefined && n.head.get.isInstanceOf[StringPhrase]) n.head.get else n
        case v: VerbPhrase   => if (v.head.isDefined && v.head.get.isInstanceOf[StringPhrase]) v.head.get else v
        case v: AdverbPhrase => v

      }
    } else {
      p
    }
  }

  //TODO: do an auto-convert of noun/verb_phrase to String when there is only a head


  def map2phrase(m: Map[String, Object], post_modifiers: Seq[Phrase], features2:Features, e: TransformEnvironment): Option[Phrase] = {
    m("type") match {
      case Constants.NOUN_PHRASE => {

        val assertedFeatures: Option[Object] = m.get("features")

        val castComputedFeatures = getComputedFeatures(m, e)
        val castAssertedFeatures = assertedFeatures.getOrElse(Map()).asInstanceOf[Features]

        val other_post_modifiers: Seq[Phrase] = m.getOrElse("post-modifiers",Seq()).asInstanceOf[Seq[Phrase]].flatMap(s => s.transform[Phrase](e))

        NounPhrase(
          determiner = m.getOrElse("determiner", null).asInstanceOf[String],
          head = m.getOrElse("head", null).asInstanceOf[String],
          modifiers = Seq(),
          pre_modifiers = Seq(),
          post_modifiers = post_modifiers ++ other_post_modifiers,
          specifier = m.get("specifier").flatMap(s => StringPhrase(s.asInstanceOf[String]).transform[NounForm](e)),
          complements = Seq(),
          comments = null,
          features = castAssertedFeatures ++ castComputedFeatures ++ features2)
      }


      case Constants.VERB_PHRASE => {

        val assertedFeatures: Option[Object] = m.get("features")

        val castComputedFeatures: Features = getComputedFeatures(m, e)
        val castAssertedFeatures: Features = assertedFeatures.getOrElse(Map()).asInstanceOf[Features]

        if ((post_modifiers==null || post_modifiers.isEmpty)
             && castComputedFeatures.isEmpty
          && post_modifiers.isEmpty) {

          StringPhrase(m.getOrElse("head", null).asInstanceOf[String])

        } else {

          VerbPhrase(
            head = m.getOrElse("head", null).asInstanceOf[String],
            modifiers = Seq(),
            pre_modifiers = Seq(),
            post_modifiers = post_modifiers,
            features = castAssertedFeatures ++ castComputedFeatures ++ features2)
        }
      }

      case Constants.STRING_PHRASE => {
        StringPhrase(m.getOrElse("value", null).asInstanceOf[String])
      }

    }


  }


  case class Paragraph(items: Seq[Phrase], properties: Map[String,String]) extends Phrase {
    override def expand(): DocumentElement = {
      val res=nlgFactory.createParagraph()
      items.foreach(phrase => phrase.expand() match {
        case s: String => res.addComponent(nlgFactory.createSentence(s))
        case e: NLGElement => res.addComponent(e)
      })
      res
    }

    override def toBlockly(): Block = ???

    override def transform[F <: Phrase](e: TransformEnvironment)(implicit c: ClassTag[F]): Option[F] = {
      Some(Paragraph(items.flatMap(i => i.transform(e)), properties).asInstanceOf[F])
    }
  }


  def getComputedFeatures(m: Map[String, Object], e: TransformEnvironment): Features = {

     val m1 = nullFeatures(m)

    nullFeatures(m).get(Keywords.KEY) match {   //TODO: change syntax, get rid of @Key, and just have a single structure kind
      case Some(_) => {
        //println("Evaluating features (1)" + m)

        val fun: Option[String] = m1.get(Keywords.FUNCTION).asInstanceOf[Option[String]]
        val res = fun match {
          case None => Map().asInstanceOf[Features]
          case Some(f) =>
            val arg1 = m1(Keywords.ARG1).asInstanceOf[String]              //TODO, why not evaluate
            val arg2 = m1.get(Keywords.ARG2).asInstanceOf[Option[String]]  //TODO, why not evaluate
            Primitive.applyPatternFun(f, e.environment, arg1, arg2).asInstanceOf[Features]
        }
        res
      }
      case _ => {

        //println("Evaluating features (2) " + m)


        val result: Map[String, Object] =
          m1.map { case (k, v) =>
            (k,
              v match {
                case m2: Map[String, Object] @ unchecked =>

                  val result = Primitive.processFunction(e.statements, m2, e.environment)
              //    println(result._1)
                  result._1 match {
                    case None => "<<none>>"
                    case Some(Result(Some(v), _, _, _)) => v
                    case _ => "<<none2>>"
                  }
                case _ =>
                  v
              })
          }

        //println("features result " + result)
        result
      }
    }

  }

  val realiser=new Realiser(lexicon)

  // this realise method calls expand on sentences in this subproject, rather than export to json, importing into nlg-wrapper, as callSImplenlglibrary does.
  // good to test, not to be used long term
  def realise (sentence: Phrase): String = {
    realiser.realiseSentence(sentence.expand().asInstanceOf[NLGElement])
  }

  def realise (sentence: NLGElement): String = {
    realiser.realiseSentence(sentence)
  }


  def callSimplenlgLibrary(p: Phrase, option: Int): (String, String, () => String) = {
    val sw=new StringWriter()
    SpecLoader.phraseExport(sw,p)
    val snlg: String =sw.getBuffer.toString

    val phrase: org.openprovenance.prov.scala.wrapper.defs.Phrase =IO.phraseImportFromString(snlg)

    val realiser: Realiser =if (option==1) org.openprovenance.prov.scala.wrapper.defs.withMarkupFormatter() else org.openprovenance.prov.scala.wrapper.defs.theRealiser
    val spec =phrase.expand().asInstanceOf[NLGElement]

    val tree=() => spec.printTree("  ")

    val result: String =realiser.realiseSentence(spec)
    (result, snlg, tree)
  }


  import scala.language.implicitConversions

  implicit def T2OptionT( x : NounPhrase) : Option[NounPhrase] = Option(x)
  implicit def T2OptionT( x : VerbPhrase) : Option[VerbPhrase] = Option(x)
  implicit def T2OptionT( x : StringPhrase) : Option[StringPhrase] = Option(x)

  implicit def T2OptionT( x : String) : Option[HeadForm] = if (x==null) None else Some(StringPhrase(x))


  case class Dictionary (help: String,
                         snippets: Map[String,Phrase],
                         dictionary: Map[String, Phrase],
                         context: Map[String,String])


}

object SpecLoader {

  val pf: ProvMixin2 =new ProvDeserialiser().provMixin()

  val mapper:ObjectMapper = new ObjectMapper  with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule)
  mapper.configure(SerializationFeature.INDENT_OUTPUT, true)

  val module = new SimpleModule("MyModule", new Version(1, 0, 0, null, null, null))
  module.addSerializer(classOf[StringPhrase], new CustomStringPhraseSerializer)
 // module.addDeserializer(classOf[HeadForm], new CustomHeadFormDeserializer)

 // module.addDeserializer(classOf[NounForm], new CustomNounFormDeserializer)

  mapper.registerModule(module)


  def phraseExport (out: OutputStream, phrase: Phrase): Unit = {
    mapper.writeValue(out,phrase)
  }

  def phraseExport (out: String, phrase: Phrase): Unit = {
    phraseExport(new FileOutputStream(out), phrase)
  }

  def phraseExport (out: Writer, phrase: Phrase): Unit = {
    mapper.writeValue(out,phrase)
  }


  def phraseImport (in: InputStream): Phrase = {
    mapper.readValue(in, classOf[Phrase])
  }

  def phraseImport (in: String): Phrase = {
    phraseImport(new FileInputStream(in))
  }

  def phraseImportFromString (in: String): Phrase = {
    mapper.readValue(in, classOf[Phrase])
  }


  def templateImport (in: InputStream): Template = {
    mapper.readValue(in, classOf[Template])
  }

  def templateImport (in: String): Template = {
    templateImport(new FileInputStream(in))
  }

  def languageImport (in: InputStream): Language = {
    mapper.readValue(in, classOf[Language])
  }

  def languageImport (in: String): Language = {
    languageImport(new FileInputStream(in))
  }

  def dictionaryImport (in: String): Dictionary ={
    dictionaryImport (new FileInputStream(in))
  }

  def dictionaryImport (in:InputStream): Dictionary = {
    mapper.readValue(in, classOf[Dictionary])
  }

  def dictionaryImport (in:InputStreamReader): Dictionary = {
    mapper.readValue(in, classOf[Dictionary])
  }

}
