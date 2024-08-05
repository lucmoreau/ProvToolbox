
package org.openprovenance.prov.scala.wrapper

import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id
import com.fasterxml.jackson.annotation._
import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.annotation.{JsonDeserialize, JsonSerialize}
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.{ObjectMapper, SerializationFeature}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.ScalaObjectMapper
import nlg.wrapper.{Block, BlocklyContents, Constants}
import defs.StringPhrase
import simplenlg.features._
import simplenlg.format.english._
import simplenlg.framework._
import simplenlg.lexicon.NIHDBLexicon
import simplenlg.phrasespec.{AdjPhraseSpec, AdvPhraseSpec, NPPhraseSpec, PPPhraseSpec, VPPhraseSpec}
import simplenlg.realiser.english.Realiser

import java.io.{FileInputStream, FileOutputStream, InputStream, OutputStream}
import java.util
import java.util.logging.{Level, Logger}




object defs {
  val xplainlexicon = "XPLAIN_LEXICON"

  val exporter=new BlocklyExport

  val lexiconLocation: String = {
    //See https://stackoverflow.com/questions/9931156/is-there-a-way-to-silence-hsqldb-logging
    System.setProperty("hsqldb.reconfig_logging", "false")
    Logger.getLogger("hsqldb.db").setLevel(Level.WARNING)
    val sysProperty= System.getProperty(xplainlexicon)
    if (sysProperty!=null) {
      sysProperty
    } else {
      val prop = new java.util.Properties()
      prop.load(getClass.getResourceAsStream("/lexicon.properties"))
      val property = prop.get("lexicon.location").asInstanceOf[String]
      println("lexicon.location=" + property)
      if (property==null) {
        println("sysProperty (" + sysProperty + ") not defined, nor 'lexicon.location' in file /lexicon.properties" + prop)
      }
      property
    }
  }

  val lexicon=new NIHDBLexicon(lexiconLocation)

  val nlgFactory: NLGFactory =new NLGFactory(lexicon)

  case class Template(select: Object, query: Array[String], sentence: Phrase)


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
    new Type(value = classOf[EnumeratedList],    name=Constants.ENUMERATED_LIST),
    new Type(value = classOf[Paragraph],         name=Constants.PARAGRAPH)
  ))
  @JsonInclude(JsonInclude.Include.NON_NULL)
  trait Phrase extends Object with Constants with ExportableToBlockly {
    def expand(): Object
    def toBlockly(): Block
  }

  type Head = String

  type Features=Map[String,String]

  @JsonTypeInfo(use = Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
  @JsonSubTypes(Array(
    new Type(value = classOf[NounPhrase],        name=Constants.NOUN_PHRASE),
    new Type(value = classOf[CoordinatedPhrase], name=Constants.COORDINATED_PHRASE),
    new Type(value = classOf[StringPhrase],      name=Constants.STRING_PHRASE)
  ))
  trait NounForm {
    def toBlockly(): Block
  }


  @JsonTypeInfo(use = Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
  @JsonSubTypes(Array(
    new Type(value = classOf[VerbPhrase],        name=Constants.VERB_PHRASE),
    new Type(value = classOf[CoordinatedPhrase], name=Constants.COORDINATED_PHRASE),
    new Type(value = classOf[StringPhrase],      name=Constants.STRING_PHRASE)
  ))
  trait VerbForm {
    def toBlockly(): Block
  }

  case class StringPhrase(value: String) extends Phrase with NounForm with VerbForm {
    def expand (): String = value


    override def toBlockly(): Block = {
      exporter.newBlockWithTextField(value)
    }

  }


  trait HasFrontModifiers {
    val front_modifiers: Seq[Phrase]
    def expandFrontModifiers(element: PhraseElement): Unit = {
      if (front_modifiers!=null) front_modifiers.foreach(pre_modifier => element.addFrontModifier(pre_modifier.expand().asInstanceOf[NLGElement]))
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
      if (post_modifiers != null) {
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
    val specifier: Option[NounPhrase]
    def expandSpecifier(element: PhraseElement): Unit = {
      if ((specifier != null) && specifier.isDefined)
        element.setFeature(InternalFeature.SPECIFIER, specifier.get.expand())
    }
  }

  trait HasFeatures {
    val features: Features


    def featuresToBlockly(l: util.List[BlocklyContents]): Unit = {
      if (features != null) {
        l.add(exporter.newValueWithBlock("FEATURES", exporter.newFeaturesBlock(features)))
      }
    }


    def processFeature(key: String, value: String, element: NLGElement): Unit = {

      def setFeature(feature: String, value: Any): Unit ={
        element.setFeature(feature,value)
      }
      key match {
        case "tense"          => setFeature(Feature.TENSE,          value match { case "past" => Tense.PAST case "present" => Tense.PRESENT case "future" => Tense.FUTURE} )
        case "number"         => setFeature(Feature.NUMBER,         value match { case "singular" => NumberAgreement.SINGULAR case "plural" => NumberAgreement.PLURAL } )
        case "passive"        => setFeature(Feature.PASSIVE,        value.toBoolean )
        case "perfect"        => setFeature(Feature.PERFECT,        value.toBoolean )
        case "cue_phrase"     => setFeature(Feature.CUE_PHRASE,     value )
        case "modal"          => setFeature(Feature.MODAL,          value )
        case "complementiser" => setFeature(Feature.COMPLEMENTISER, value )
        case "form"           => setFeature(Feature.FORM,           value match { case "pastParticiple" => Form.PAST_PARTICIPLE case "presentParticiple" => Form.PRESENT_PARTICIPLE})
        case "pronominal"     => setFeature(Feature.PRONOMINAL,     value.toBoolean )
        case "possessive"     => setFeature(Feature.POSSESSIVE,     value.toBoolean )
        case "gender"         => setFeature(LexicalFeature.GENDER,  value match { case "masculine" => Gender.MASCULINE case "feminine" => Gender.FEMININE case "neutral" => Gender.NEUTER} )
        case "person"         => setFeature(Feature.PERSON,         value match { case "first" => Person.FIRST case "second" => Person.SECOND case "third" => Person.THIRD} )

        case key              => setFeature(key,value)
      }
    }

    def expandFeatures(element: NLGElement): Unit = {
      if (features!=null) features.foreach{case (k,v) => processFeature(k,v,element)}
    }
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
                    features:        Features=Map(),
                    @JsonProperty("@features")
                    cfeatures:      Features=Map()) extends Phrase with HasModifiers with HasFrontModifiers with HasPreModifiers with HasPostModifiers with HasFeatures with HasComplements {

    @Override
    def expand (): PhraseElement = {
      val res=nlgFactory.createClause()
      if (subject!=null)         subject.foreach(subject => res.setSubject(subject.asInstanceOf[Phrase].expand()))
      if (verb!=null)            verb.foreach(verb => res.setVerb(verb.asInstanceOf[Phrase].expand()))
      if (direct_object!=null)   direct_object.foreach(obj => res.setObject(obj.expand()))
      if (indirect_object!=null) indirect_object.foreach(obj => res.setIndirectObject(obj.expand()))

      expandModifiers(res)
      expandFrontModifiers(res)
      expandPreModifiers(res)
      expandPostModifiers(res)
      expandFeatures(res)
      expandComplements(res)
      if (complementiser!=null) res.setFeature(Feature.COMPLEMENTISER,complementiser)
      res
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

  case class NounPhrase(determiner:     String,
                        head:           Head,
                        modifiers:      Seq[Phrase]=Seq(),
                        @JsonProperty("pre-modifiers")
                        pre_modifiers:  Seq[Phrase]=Seq(),
                        @JsonProperty("post-modifiers")
                        post_modifiers: Seq[Phrase]=Seq(),
                        specifier:      Option[NounPhrase]=None,
                        complements:    Seq[Phrase]=Seq(),
                        comments:       String=null,
                        features:       Features=Map(),
                        @JsonProperty("@features")
                        cfeatures:      Features=Map())
    extends Phrase with NounForm with HasPreModifiers with HasModifiers with HasPostModifiers with HasFeatures with HasComplements with HasSpecifier {

    @Override
    def expand (): NPPhraseSpec = {
      val res= nlgFactory.createNounPhrase()

      res.setNoun(head)

      propagateMarkupFeaturesToHead(features, res)

      res.setDeterminer(determiner)

      expandModifiers(res)
      expandPreModifiers(res)
      expandPostModifiers(res)
      expandComplements(res)
      expandFeatures(res)
      expandSpecifier(res)

      res
    }

    override def toBlockly(): Block = {
      val id: Head = exporter.newId
      val l: util.List[BlocklyContents] = new util.LinkedList[BlocklyContents]()
      if (head!=null)  l.add(exporter.newValueWithBlock("HEAD", exporter.newBlockWithTextField(head)))
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


  val knownMarkupFeatures: Set[String] =Set(Constants.MARKUP_ELEMENT, Constants.MARKUP_ATTRIBUTES)
  val knownHeadMarkupFeatures: Map[String,String] =Map(Constants.HEAD_MARKUP_ELEMENT -> Constants.MARKUP_ELEMENT, Constants.HEAD_MARKUP_ATTRIBUTES -> Constants.MARKUP_ATTRIBUTES)



  private def propagateMarkupFeaturesToHead(features: Features, res: NPPhraseSpec): Unit = {

    if (features != null) {
      features.keySet.intersect(knownHeadMarkupFeatures.keySet).foreach(
        feature => {
          val elem = features.get(feature)
          elem match {
            case None =>
            case Some(s) => {
              res.getNoun match {
                case element: WordElement =>
                  element.setFeature(knownHeadMarkupFeatures(feature), s)
                case element: StringElement =>
                  element.setFeature(knownHeadMarkupFeatures(feature), s)
                case _ =>
                  println("???? " + res.printTree(" "))
              }
            }
          }
        })
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
                        features: Features=Map(),
                        @JsonProperty("@features")
                        cfeatures:      Features=Map())
    extends Phrase with VerbForm with HasPreModifiers with HasModifiers with HasPostModifiers with HasFeatures with HasComplements {

    @Override
    def expand (): VPPhraseSpec = {
      val res= nlgFactory.createVerbPhrase(head)
      //res.setHead(head)
      if (direct_object!=null)   direct_object.foreach(obj => res.setObject(obj.expand()))
      if (indirect_object!=null) indirect_object.foreach(obj => res.setIndirectObject(obj.expand()))

      expandModifiers(res)
      expandPreModifiers(res)
      expandPostModifiers(res)
      expandComplements(res)
      expandFeatures(res)

      res
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


  case class AdjectivePhrase(head: Head,
                             modifiers: Seq[Phrase]=Seq(),
                             @JsonProperty("pre-modifiers")
                             pre_modifiers: Seq[Phrase]=Seq(),
                             @JsonProperty("post-modifiers")
                             post_modifiers: Seq[Phrase]=Seq(),
                             features: Features=Map(),
                             @JsonProperty("@features")
                             cfeatures:      Features=Map())
    extends Phrase with HasPreModifiers with HasModifiers with HasPostModifiers with HasFeatures {

    @Override
    def expand (): AdjPhraseSpec = {

      val res = nlgFactory.createAdjectivePhrase(head)
      //res.setHead(head)

      expandModifiers(res)
      expandPreModifiers(res)
      expandPostModifiers(res)
      expandFeatures(res)

      res
    }

    override def toBlockly(): Block = {
      val id: Head = exporter.newId
      val l: util.List[BlocklyContents] = new util.LinkedList[BlocklyContents]()
      if (head!=null) {
        l.add(exporter.newValueWithBlock("HEAD", exporter.newBlockWithTextField(head)))
      }
      pre_modifiersToBlockly(l)
      post_modifiersToBlockly(l)
      modifiersToBlockly(l)
      featuresToBlockly(l)
      new Block(id, "adjective_phrase",null,null,l)
    }
  }



  case class AdverbPhrase(head: Head,
                             modifiers: Seq[Phrase]=Seq(),
                             @JsonProperty("pre-modifiers")
                             pre_modifiers: Seq[Phrase]=Seq(),
                             @JsonProperty("post-modifiers")
                             post_modifiers: Seq[Phrase]=Seq(),
                             features: Features=Map(),
                             @JsonProperty("@features")
                             cfeatures:      Features=Map())
    extends Phrase with HasPreModifiers with HasModifiers with HasPostModifiers with HasFeatures {

    @Override
    def expand (): AdvPhraseSpec = {

      val res = nlgFactory.createAdverbPhrase(head)
      //res.setHead(head)

      expandModifiers(res)
      expandPreModifiers(res)
      expandPostModifiers(res)
      expandFeatures(res)

      res
    }

    override def toBlockly(): Block = {
      val id: Head = exporter.newId
      val l: util.List[BlocklyContents] = new util.LinkedList[BlocklyContents]()
      if (head!=null) {
        l.add(exporter.newValueWithBlock("HEAD", exporter.newBlockWithTextField(head)))
      }
      pre_modifiersToBlockly(l)
      post_modifiersToBlockly(l)
      modifiersToBlockly(l)
      featuresToBlockly(l)
      new Block(id, "adjverb_phrase",null,null,l)
    }
  }

  case class PrepositionPhrase(preposition: String,
                               @JsonDeserialize(using = classOf[CustomOptionNounFormDeserializer]) noun: Option[NounForm],
                               complements: Seq[Phrase]=Seq(),
                               specifier: Option[NounPhrase]=None,
                               features:        Features=Map(),
                               @JsonProperty("@features")
                               cfeatures:      Features=Map()) extends Phrase with HasComplements with HasSpecifier with HasFeatures  {

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


  val liFeature = Map(Constants.MARKUP_ELEMENT -> "li")
  val olFeature = Map(Constants.MARKUP_ELEMENT -> "ol")
  val ulFeature = Map(Constants.MARKUP_ELEMENT -> "ul")
  val spanFeature = Map(Constants.MARKUP_ELEMENT -> "span")

  @JsonIgnoreProperties(ignoreUnknown = true)
  case class CoordinatedPhrase(head: Head,
                               @JsonDeserialize(contentUsing=classOf[CustomStringOrPhraseDeserializer])
                               @JsonSerialize(contentUsing=classOf[CustomOptionPhraseSerializer])
                               coordinates: Seq[Option[Phrase]]=Seq(),
                               conjunction: String,
                               @JsonProperty("post-modifiers")
                               post_modifiers: Seq[Phrase]=Seq(),
                               features:    Features=Map()) extends Phrase with NounForm with VerbForm with HasPostModifiers with HasFeatures {

    def expand (): CoordinatedPhraseElement = {

      val res=nlgFactory.createCoordinatedPhrase

      val theFeatures: Map[String, String] =if (features==null) Map() else features

      coordinates.flatten.foreach(coordinate => {
        res.addCoordinate(propagateItemMarkupToChild(theFeatures, coordinate).expand())
      })

      expandPostModifiers(res)
      expandFeatures(res)

      res.setFeature(simplenlg.features.Feature.CONJUNCTION,conjunction)

      res
    }

    override def toBlockly(): Block = {
      val id: Head = exporter.newId
      val l: util.List[BlocklyContents] = new util.LinkedList[BlocklyContents]()
      if (conjunction!=null) l.add(exporter.newValueWithBlock("CONJUNCTION", exporter.newBlockWithTextField(conjunction)))
      l.add(exporter.toBlocklyArray2("COORDINATES", coordinates))
      post_modifiersToBlockly(l)
      featuresToBlockly(l)
      new Block(id, "coordinated_phrase",null,null,l)
    }

    private def propagateItemMarkupToChild(theFeatures: Map[String, String], coordinate: Phrase): Phrase = {
      coordinate match {
        case np: NounPhrase =>
          theFeatures.get(Constants.MARKUP_ELEMENT) match {
            case Some("ol") => np.copy(features = np.features ++ liFeature)
            case Some("ul") => np.copy(features = np.features ++ liFeature)
            case _ => coordinate
          }
        case sp: StringPhrase =>
          theFeatures.get(Constants.MARKUP_ELEMENT) match {
            case Some("ol") => NounPhrase(null, sp.value, Seq(), Seq(), Seq(), None, Seq(), null, liFeature)
            case Some("ul") => NounPhrase(null, sp.value, Seq(), Seq(), Seq(), None, Seq(), null, liFeature)
            case _ => coordinate
          }
        case _ =>
          coordinate
      }
    }
  }

  case class EnumeratedList(items: Seq[Phrase], properties: Map[String,String]) extends Phrase {
    override def expand(): DocumentElement = {
      val res=nlgFactory.createEnumeratedList()
      items.foreach(phrase => res.addComponent(makeItem(phrase)))
      res
    }

    override def toBlockly(): Block = ???
  }
  case class Paragraph(items: Seq[Phrase], properties: Map[String,String]) extends Phrase {
    override def expand(): DocumentElement = {
      val res=nlgFactory.createParagraph()
      items.foreach(phrase => res.addComponent(phrase.expand().asInstanceOf[NLGElement]))
      res
    }

    override def toBlockly(): Block = ???
  }


  private def makeItem(phrase: Phrase): DocumentElement = {
    val item = nlgFactory.createListItem()
    item.addComponent(phrase.expand().asInstanceOf[NLGElement])
    item
  }
  private def makeItem(element: NLGElement): DocumentElement = {
    val item = nlgFactory.createListItem()
    item.addComponent(element)
    item
  }

  import scala.language.implicitConversions

  implicit def T2OptionT( x : NounPhrase) : Option[NounPhrase] = Option(x)
  implicit def T2OptionT( x : VerbPhrase) : Option[VerbPhrase] = Option(x)
  implicit def T2OptionT( x : StringPhrase) : Option[StringPhrase] = Option(x)
  //implicit def T2OptionT( x : String) : Option[String] = if ( x == null ) None else Some(x)





  val theRealiser=new Realiser(lexicon)

  def realise (sentence: Phrase): String = {
    theRealiser.realiseSentence(sentence.expand().asInstanceOf[NLGElement])
  }

  def realise (sentence: NLGElement, realiser:Realiser=theRealiser): String = {
    realiser.realiseSentence(sentence)
  }

  def withHTMLFormatter (): Realiser = {
    val arealiser=new Realiser(lexicon)
    arealiser.setFormatter( new HTMLFormatter( ) )
    arealiser
  }

  def withMarkupFormatter (): Realiser = {
    val arealiser: MarkupRealiser =new MarkupRealiser(lexicon)
    arealiser.morphology=new MarkupMorphologyProcessor()
    arealiser.morphology.initialise()
    arealiser.syntax=new MarkupSyntaxProcessor()
    arealiser.syntax.initialise()
    arealiser.orthography= new MarkupOrthographyProcessor()
    arealiser.orthography.initialise()

    arealiser.setFormatter( new MarkupFormatter( ) )
    arealiser
  }


}

object IO {

  import defs.Phrase

  val mapper:ObjectMapper = new ObjectMapper  with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule)
  mapper.configure(SerializationFeature.INDENT_OUTPUT, true)

  val module = new SimpleModule("MyModule", new Version(1, 0, 0, null, null, null))
  module.addSerializer(classOf[StringPhrase], new CustomStringPhraseSerializer)


 // module.addDeserializer(classOf[NounForm], new CustomNounFormDeserializer)

  mapper.registerModule(module)


  def phraseExport (out: OutputStream, phrase: Phrase): Unit = {
    mapper.writeValue(out,phrase)
  }

  def phraseExport (out: String, phrase: Phrase): Unit = {
    phraseExport(new FileOutputStream(out), phrase)
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

}
