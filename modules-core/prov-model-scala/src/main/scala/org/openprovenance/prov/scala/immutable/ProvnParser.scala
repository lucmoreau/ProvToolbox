package org.openprovenance.prov.scala.immutable

import java.io.{InputStream, OutputStream}
import java.util

import org.parboiled2._

import scala.util.{Failure, Success}
import org.openprovenance.prov.model.Namespace
import ProvFactory.pf
import javax.xml.datatype.XMLGregorianCalendar
import org.openprovenance.prov.interop.InteropMediaType
import org.openprovenance.prov.model
import org.openprovenance.prov.scala.immutable.Parser.{actions, actions2}

import scala.annotation.tailrec
import org.openprovenance.prov.scala.streaming.{DocBuilder, DocBuilderFunctions, SimpleStreamStats, Tee}
import shapeless.HNil

import scala.io.BufferedSource

trait ProvnCore extends Parser {

  def input : ParserInput

  private val pn_chars_base: CharPredicate = CharPredicate.Alpha  ++ CharPredicate('\u00C0' to '\u00D6') ++
    CharPredicate('\u00D8' to '\u00F6') ++
    CharPredicate('\u00F8' to '\u02FF') ++
    CharPredicate('\u0370' to '\u037D') ++
    CharPredicate('\u037F' to '\u1FFF') ++
    CharPredicate('\u200C' to '\u200D') ++
    CharPredicate('\u2070' to '\u218F') ++
    CharPredicate('\u2C00' to '\u2FEF') ++
    CharPredicate('\u3001' to '\uD7FF') ++
    CharPredicate('\uF900' to '\uFDCF') ++
    CharPredicate('\uFDF0' to '\uFFFD')




  private val pn_chars_u: CharPredicate = pn_chars_base ++ '_'

  private val pn_chars: CharPredicate = pn_chars_u ++ '-' ++ CharPredicate.Digit ++
    '\u00B7' ++ CharPredicate('\u0300' to '\u036F') ++ CharPredicate('\u203F' to '\u2040')


  private val qualified_name_escape_chars: CharPredicate =CharPredicate("='(),-:;[].")

  private val qualified_name_chars_others: CharPredicate =CharPredicate("/@~&+*?#$!")

  private val pn_char_white: CharPredicate = CharPredicate(" \t \n\r")

  private val EOL: CharPredicate = CharPredicate("\n\r")


  private val DOT: CharPredicate = CharPredicate(".")

  def pn_prefix = rule { pn_chars_base ~ zeroOrMore ( pn_chars   |  ( DOT ~ zeroOrMore(DOT)  ~  pn_chars ) ) }

  private def percent: Rule[HNil, HNil] = rule {  '%'  ~ CharPredicate.HexDigit ~ CharPredicate.HexDigit }


  private def pn_chars_esc: Rule[HNil, HNil] = rule {  '\\' ~ qualified_name_escape_chars }


  private def pn_chars_others: Rule[HNil, HNil] = rule { percent | qualified_name_chars_others | pn_chars_esc }


  // Note, this had to be reorganised to  be compatible with the committed choice | or of ERG grammars
  private def pn_local = rule { ( pn_chars_u | CharPredicate.Digit | pn_chars_others ) ~
    zeroOrMore ( ( pn_chars |  pn_chars_others )  | ( oneOrMore(DOT)  ~  ( pn_chars | pn_chars_others) ) )   }

  def qualified_name:Rule1[QualifiedName] = rule { capture ((optional( pn_prefix ~ ':' ) ~  pn_local )  | (pn_prefix ~ ':' ))  ~> makeQualifiedName }

  def qualified_nameAsString: Rule1[String] = rule { capture ((optional( pn_prefix ~ ':' ) ~  pn_local )  | (pn_prefix ~ ':' ))  ~> makeText }

  def WS = rule { zeroOrMore( pn_char_white  | comment) }

  private def comment: Rule[HNil, HNil] = rule { comment1 | comment2 }

  private def comment1 = rule { "//" ~ zeroOrMore(CharPredicate.Printable) ~ oneOrMore(EOL) }

  private def comment2 = rule { "/*" ~ endcomment2 }

  private def endcomment2: Rule0 = rule { "*/" | zeroOrMore (CharPredicate.Printable) ~ zeroOrMore( pn_char_white ) ~ endcomment2 }  //TODO: will result in stack overflow

  /* parser action */
  val makeText: (String) => String
  val makeQualifiedName: (String) => QualifiedName


}


trait ProvnNamespaces extends Parser with ProvnCore {


  def namespaceDeclaration: Rule[HNil, HNil] =  rule { "prefix" ~ WS ~ capture(pn_prefix) ~ WS ~ namespace ~> registerNamespace }

  def defaultNamespaceDeclaration: Rule[HNil, HNil] =  rule { "default" ~ WS ~ namespace  ~> registerDefaultNamespace }

  def namespace =  rule { '<' ~ capture( oneOrMore(noneOf("<>")) )  ~ '>' ~ WS }   // TO REFINE, see IRI_REF def

  def namespaceDeclarations = rule  {	( defaultNamespaceDeclaration | namespaceDeclaration  ) ~ WS ~ zeroOrMore (namespaceDeclaration) }

  def theNamespace: () => Namespace

  def registerNamespace: (String, String) => Unit = (pre:String, ns:String) => theNamespace().register(pre,ns)

  def registerDefaultNamespace: String => Unit = (ns:String) => theNamespace().registerDefault(ns)
}

trait ProvnParser extends Parser with ProvnCore with ProvnNamespaces {


    def entity = rule {  "entity" ~ WS ~ '(' ~ WS ~ identifier ~ WS ~ optionalAttributeValuePairs ~ ')' ~> makeEntity ~ WS }

    def agent = rule {  "agent" ~ WS ~ '(' ~ WS ~ identifier ~ WS ~ optionalAttributeValuePairs ~ ')' ~> makeAgent ~ WS }

    def activity = rule {  "activity" ~ WS ~ '(' ~ WS ~ identifier ~ WS ~  ( ',' ~ WS ~ optional_datetime  ~ ',' ~ WS ~ optional_datetime ~ optionalAttributeValuePairs ~ ')'  ~ WS  ~> makeActivity | optionalAttributeValuePairs ~ ')'  ~ WS   ~> makeActivityNoTime ) }

    def wasGeneratedBy = rule {  "wasGeneratedBy" ~ WS ~ '(' ~ WS ~ ('-' ~ WS   ~     ';' ~ WS  ~ identifier ~ ( ',' ~ WS ~ identifierOrMarker ~ ',' ~ WS ~ optional_datetime ~ optionalAttributeValuePairs ~ ')' ~> makeWasGeneratedByNoId ~ WS | 
                                                                                                                                                                                optionalAttributeValuePairs ~ ')' ~> makeWasGeneratedByNoId2 ~ WS ) |
                                                                     identifier ~ (   ';' ~ WS  ~ identifier ~ ( ',' ~ WS ~ identifierOrMarker ~ ',' ~ WS ~ optional_datetime ~ optionalAttributeValuePairs ~ ')' ~> makeWasGeneratedByWithId ~ WS | 
                                                                                                                                                                                optionalAttributeValuePairs ~ ')' ~> makeWasGeneratedByWithId2 ~ WS ) |
                                                                                    ( ',' ~ WS                            ~ identifierOrMarker ~ ',' ~ WS ~ optional_datetime ~ optionalAttributeValuePairs ~ ')' ~> makeWasGeneratedByNoId ~ WS |
                                                                                                                                                                                optionalAttributeValuePairs ~ ')' ~> makeWasGeneratedByNoId2 ~ WS ))) }

    def wasInvalidatedBy = rule {  "wasInvalidatedBy" ~ WS ~ '(' ~ WS ~ ('-' ~ WS   ~     ';' ~ WS  ~ identifier ~ ( ',' ~ WS ~ identifierOrMarker ~ ',' ~ WS ~ optional_datetime ~ optionalAttributeValuePairs ~ ')' ~> makeWasInvalidatedByNoId ~ WS | 
                                                                                                                                                                                    optionalAttributeValuePairs ~ ')' ~> makeWasInvalidatedByNoId2 ~ WS ) |
                                                                         identifier ~ (   ';' ~ WS  ~ identifier ~ ( ',' ~ WS ~ identifierOrMarker ~ ',' ~ WS ~ optional_datetime ~ optionalAttributeValuePairs ~ ')' ~> makeWasInvalidatedByWithId ~ WS | 
                                                                                                                                                                                    optionalAttributeValuePairs ~ ')' ~> makeWasInvalidatedByWithId2 ~ WS ) |
                                                                                        ( ',' ~ WS                            ~ identifierOrMarker ~ ',' ~ WS ~ optional_datetime ~ optionalAttributeValuePairs ~ ')' ~> makeWasInvalidatedByNoId ~ WS |
                                                                                                                                                                                    optionalAttributeValuePairs ~ ')' ~> makeWasInvalidatedByNoId2 ~ WS ))) }


    def used = rule {  "used" ~ WS ~ '(' ~ WS ~ ('-' ~ WS   ~     ';' ~ WS  ~ identifier ~ ( ',' ~ WS ~ identifierOrMarker ~ ',' ~ WS ~ optional_datetime ~ optionalAttributeValuePairs ~ ')' ~> makeUsedNoId ~ WS | 
                                                                                                                                                            optionalAttributeValuePairs ~ ')' ~> makeUsedNoId2 ~ WS ) |
                                                 identifier ~ (   ';' ~ WS  ~ identifier ~ ( ',' ~ WS ~ identifierOrMarker ~ ',' ~ WS ~ optional_datetime ~ optionalAttributeValuePairs ~ ')' ~> makeUsedWithId ~ WS | 
                                                                                                                                                            optionalAttributeValuePairs ~ ')' ~> makeUsedWithId2 ~ WS ) |
                                                                ( ',' ~ WS                            ~ identifierOrMarker ~ ',' ~ WS ~ optional_datetime ~ optionalAttributeValuePairs ~ ')' ~> makeUsedNoId ~ WS |
                                                                                                                                                            optionalAttributeValuePairs ~ ')' ~> makeUsedNoId2 ~ WS ))) }

    def wasStartedBy = rule {  "wasStartedBy" ~ WS ~ '(' ~ WS ~ ('-' ~ WS   ~     ';' ~ WS  ~ identifier ~ ( ',' ~ WS ~ identifierOrMarker ~ ',' ~ WS ~ identifierOrMarker ~ ',' ~ WS ~ optional_datetime ~ optionalAttributeValuePairs ~ ')' ~> makeWasStartedByNoId ~ WS | 
                                                                                                                                                                                                            optionalAttributeValuePairs ~ ')' ~> makeWasStartedByNoId2 ~ WS ) |
                                                                 identifier ~ (   ';' ~ WS  ~ identifier ~ ( ',' ~ WS ~ identifierOrMarker ~ ',' ~ WS ~ identifierOrMarker ~ ',' ~ WS ~ optional_datetime ~ optionalAttributeValuePairs ~ ')' ~> makeWasStartedByWithId ~ WS | 
                                                                                                                                                                                                            optionalAttributeValuePairs ~ ')' ~> makeWasStartedByWithId2 ~ WS ) |
                                                                                ( ',' ~ WS                            ~ identifierOrMarker ~ ',' ~ WS ~ identifierOrMarker ~ ',' ~ WS ~ optional_datetime ~ optionalAttributeValuePairs ~ ')' ~> makeWasStartedByNoId ~ WS |
                                                                                                                                                                                                            optionalAttributeValuePairs ~ ')' ~> makeWasStartedByNoId2 ~ WS ))) }


    def wasEndedBy = rule {  "wasEndedBy" ~ WS ~ '(' ~ WS ~ ('-' ~ WS   ~     ';' ~ WS  ~ identifier ~ ( ',' ~ WS ~ identifierOrMarker ~ ',' ~ WS ~ identifierOrMarker ~ ',' ~ WS ~ optional_datetime ~ optionalAttributeValuePairs ~ ')' ~> makeWasEndedByNoId ~ WS |
                                                                                                                                                                                                        optionalAttributeValuePairs ~ ')' ~> makeWasEndedByNoId2 ~ WS ) |
                                                             identifier ~ (   ';' ~ WS  ~ identifier ~ ( ',' ~ WS ~ identifierOrMarker ~ ',' ~ WS ~ identifierOrMarker ~ ',' ~ WS ~ optional_datetime ~ optionalAttributeValuePairs ~ ')' ~> makeWasEndedByWithId ~ WS | 
                                                                                                                                                                                                        optionalAttributeValuePairs ~ ')' ~> makeWasEndedByWithId2 ~ WS ) |
                                                                            ( ',' ~ WS                            ~ identifierOrMarker ~ ',' ~ WS ~ identifierOrMarker ~ ',' ~ WS ~ optional_datetime ~ optionalAttributeValuePairs ~ ')' ~> makeWasEndedByNoId ~ WS |
                                                                                                                                                                                                        optionalAttributeValuePairs ~ ')' ~> makeWasEndedByNoId2 ~ WS ))) }


    def wasAssociatedWith = rule {  "wasAssociatedWith" ~ WS ~ '(' ~ WS ~ ('-' ~ WS   ~     ';' ~ WS  ~ identifier ~ ( ',' ~ WS ~ identifierOrMarker ~ ',' ~ WS ~ identifierOrMarker ~ optionalAttributeValuePairs ~ ')' ~> makeWasAssociatedWithNoId ~ WS | 
                                                                                                                                                                                       optionalAttributeValuePairs ~ ')' ~> makeWasAssociatedWithNoId2 ~ WS ) |
                                                                           identifier ~ (   ';' ~ WS  ~ identifier ~ ( ',' ~ WS ~ identifierOrMarker ~ ',' ~ WS ~ identifierOrMarker ~ optionalAttributeValuePairs ~ ')' ~> makeWasAssociatedWithWithId ~ WS | 
                                                                                                                                                                                       optionalAttributeValuePairs ~ ')' ~> makeWasAssociatedWithWithId2 ~ WS ) |
                                                                                          ( ',' ~ WS                            ~ identifierOrMarker ~ ',' ~ WS ~ identifierOrMarker ~ optionalAttributeValuePairs ~ ')' ~> makeWasAssociatedWithNoId ~ WS |
                                                                                                                                                                                       optionalAttributeValuePairs ~ ')' ~> makeWasAssociatedWithNoId2 ~ WS ))) }


    
    def actedOnBehalfOf = rule {  "actedOnBehalfOf" ~ WS ~ '(' ~ WS ~ ('-' ~ WS   ~     ';' ~ WS  ~ identifier ~ ',' ~ WS ~ identifier ~ ( ',' ~ WS ~ identifierOrMarker ~ optionalAttributeValuePairs ~ ')' ~> makeActedOnBehalfOfNoId ~ WS | 
                                                                                                                                                                           optionalAttributeValuePairs ~ ')' ~> makeActedOnBehalfOfNoId2 ~ WS ) |
                                                                       identifier ~ (   ';' ~ WS  ~ identifier ~ ',' ~ WS ~ identifier ~ ( ',' ~ WS ~ identifierOrMarker ~ optionalAttributeValuePairs ~ ')' ~> makeActedOnBehalfOfWithId ~ WS | 
                                                                                                                                                                           optionalAttributeValuePairs ~ ')' ~> makeActedOnBehalfOfWithId2 ~ WS ) |
                                                                                        ',' ~ WS                          ~ identifier ~ ( ',' ~ WS ~ identifierOrMarker ~ optionalAttributeValuePairs ~ ')' ~> makeActedOnBehalfOfNoId ~ WS |
                                                                                                                                                                           optionalAttributeValuePairs ~ ')' ~> makeActedOnBehalfOfNoId2 ~ WS ))) }

    def wasAttributedTo = rule {  "wasAttributedTo" ~ WS ~ '(' ~ WS ~ ('-' ~ WS   ~     ';' ~ WS  ~ identifier ~ ',' ~ WS ~ identifier ~ optionalAttributeValuePairs ~ ')' ~> makeWasAttributedToNoId ~ WS |
                                                                       identifier ~ (   ';' ~ WS  ~ identifier ~ ',' ~ WS ~ identifier ~ optionalAttributeValuePairs ~ ')' ~> makeWasAttributedToWithId ~ WS | 
                                                                                        ',' ~ WS                          ~ identifier ~ optionalAttributeValuePairs ~ ')' ~> makeWasAttributedToNoId ~ WS )) }

    def wasInfluencedBy = rule {  "wasInfluencedBy" ~ WS ~ '(' ~ WS ~ ('-' ~ WS   ~     ';' ~ WS  ~ identifier ~ ',' ~ WS ~ identifier ~ optionalAttributeValuePairs ~ ')' ~> makeWasInfluencedByNoId ~ WS |
                                                                       identifier ~ (   ';' ~ WS  ~ identifier ~ ',' ~ WS ~ identifier ~ optionalAttributeValuePairs ~ ')' ~> makeWasInfluencedByWithId ~ WS | 
                                                                                        ',' ~ WS                          ~ identifier ~ optionalAttributeValuePairs ~ ')' ~> makeWasInfluencedByNoId ~ WS )) }


    def wasInformedBy = rule {  "wasInformedBy" ~ WS ~ '(' ~ WS ~ ('-'     ~ WS   ~     ';' ~ WS  ~ identifier ~ ',' ~ WS ~ identifier ~ optionalAttributeValuePairs ~ ')' ~> makeWasInformedByNoId ~ WS |
                                                                       identifier ~ (   ';' ~ WS  ~ identifier ~ ',' ~ WS ~ identifier ~ optionalAttributeValuePairs ~ ')' ~> makeWasInformedByWithId ~ WS | 
                                                                                        ',' ~ WS                          ~ identifier ~ optionalAttributeValuePairs ~ ')' ~> makeWasInformedByNoId ~ WS )) }


    def wasDerivedFrom = rule {  "wasDerivedFrom" ~ WS ~ '(' ~ WS ~ ('-' ~ WS   ~     ';' ~ WS  ~ identifier ~ ',' ~ WS ~ identifier ~ ( ',' ~ WS ~ identifierOrMarker ~ ',' ~ WS ~ identifierOrMarker ~ ',' ~ WS ~ identifierOrMarker ~ optionalAttributeValuePairs ~ ')' ~> makeWasDerivedFromNoId ~ WS | 
                                                                                                                                                                                                                                         optionalAttributeValuePairs ~ ')' ~> makeWasDerivedFromNoId2 ~ WS ) |
                                                                     identifier ~ (   ';' ~ WS  ~ identifier ~ ',' ~ WS ~ identifier ~ ( ',' ~ WS ~ identifierOrMarker ~ ',' ~ WS ~ identifierOrMarker ~ ',' ~ WS ~ identifierOrMarker ~ optionalAttributeValuePairs ~ ')' ~> makeWasDerivedFromWithId ~ WS | 
                                                                                                                                                                                                                                         optionalAttributeValuePairs ~ ')' ~> makeWasDerivedFromWithId2 ~ WS ) |
                                                                                      ',' ~ WS                          ~ identifier ~ ( ',' ~ WS ~ identifierOrMarker ~ ',' ~ WS ~ identifierOrMarker ~',' ~ WS ~ identifierOrMarker ~ optionalAttributeValuePairs ~ ')' ~> makeWasDerivedFromNoId ~ WS |
                                                                                                                                                                                                                                        optionalAttributeValuePairs ~ ')' ~> makeWasDerivedFromNoId2 ~ WS ))) }

    

    def specializationOf = rule { "specializationOf" ~ WS ~ '(' ~ WS ~ identifier ~ ',' ~ WS ~ identifier ~ ')' ~> makeSpecializationOf ~ WS }

    def alternateOf = rule { "alternateOf" ~ WS ~ '(' ~ WS ~ identifier ~ ',' ~ WS ~ identifier ~ ')' ~> makeAlternateOf ~ WS }

    def hadMember = rule { "hadMember" ~ WS ~ '(' ~ WS ~ identifier ~ ',' ~ WS ~ identifier ~ ')' ~> makeHadMember ~ WS }

    def mentionOf = rule { "mentionOf" ~ WS ~ '(' ~ WS ~ identifier ~ ',' ~ WS ~ identifier ~ ',' ~ WS ~ identifier ~ ')' ~> makeMentionOf ~ WS }


    def qualifiedSpecializationOf = rule {  "provext:specializationOf" ~ WS ~ '(' ~ WS ~ ('-' ~ WS   ~     ';' ~ WS  ~ identifier ~ ',' ~ WS ~ identifier ~ optionalAttributeValuePairs ~ ')' ~> makeQualifiedSpecializationOfNoId ~ WS |
                                                                                          identifier ~ (   ';' ~ WS  ~ identifier ~ ',' ~ WS ~ identifier ~ optionalAttributeValuePairs ~ ')' ~> makeQualifiedSpecializationOfWithId ~ WS | 
                                                                                                           ',' ~ WS                          ~ identifier ~ optionalAttributeValuePairs ~ ')' ~> makeQualifiedSpecializationOfNoId ~ WS )) }



    def qualifiedAlternateOf = rule {  "provext:alternateOf" ~ WS ~ '(' ~ WS ~ ('-' ~ WS   ~     ';' ~ WS  ~ identifier ~ ',' ~ WS ~ identifier ~ optionalAttributeValuePairs ~ ')' ~> makeQualifiedAlternateOfNoId ~ WS |
                                                                                identifier ~ (   ';' ~ WS  ~ identifier ~ ',' ~ WS ~ identifier ~ optionalAttributeValuePairs ~ ')' ~> makeQualifiedAlternateOfWithId ~ WS | 
                                                                                                 ',' ~ WS                          ~ identifier ~ optionalAttributeValuePairs ~ ')' ~> makeQualifiedAlternateOfNoId ~ WS )) }


    def qualifiedHadMemberOf = rule {  "provext:hadMember" ~ WS ~ '(' ~ WS ~ ('-' ~ WS   ~     ';' ~ WS  ~ identifier ~ ',' ~ WS ~ identifier ~ optionalAttributeValuePairs ~ ')' ~> makeQualifiedHadMemberOfNoId ~ WS |
                                                                                identifier ~ (   ';' ~ WS  ~ identifier ~ ',' ~ WS ~ identifier ~ optionalAttributeValuePairs ~ ')' ~> makeQualifiedHadMemberOfWithId ~ WS | 
                                                                                                 ',' ~ WS                          ~ identifier ~ optionalAttributeValuePairs ~ ')' ~> makeQualifiedHadMemberOfNoId ~ WS )) }


    def identifier = qualified_name

    def identifierOrMarker = rule { ( qualified_name ~> makeOptionalIdentifier | '-' ~ WS ~> makeNoIdentifier ) }

    def statement: Rule1[Statement] = rule { entity | agent | activity |  wasGeneratedBy | wasDerivedFrom | wasInvalidatedBy | used | wasAssociatedWith | actedOnBehalfOf | wasAttributedTo | specializationOf | alternateOf | hadMember | wasInformedBy | wasStartedBy | wasEndedBy | qualifiedSpecializationOf | mentionOf | qualifiedAlternateOf | qualifiedHadMemberOf | wasInfluencedBy }

    def statementOrBundle = rule { statement | bundle }

    def optionalAttributeValuePairs: Rule1[Seq[Attribute]] = rule { optional ( ',' ~ WS ~ '[' ~ WS ~ ( attributeValuePairs ~ WS ~ ']' ~ WS  |
                                                                                                                                  ']' ~ WS ~> makeEmptyAttributeSet )  )  ~> makeAttributeSetFromOption }

    def attributeValuePairs: Rule1[Seq[Attribute]] = rule {	 attributeValuePair ~ zeroOrMore( ',' ~ WS ~ attributeValuePair ) ~> makeAttributeSet }

    def	attributeValuePair: Rule1[Attribute] = rule {  attribute ~ WS ~ '=' ~ WS ~ literal ~ WS   }

    def attribute: Rule1[QualifiedName] = qualified_name
  
    def literal = rule { typedLiteral  ~> makeAttribute | convenienceNotation }

    def typedLiteral: Rule2[String,QualifiedName] = rule { string_literal ~ WS ~ "%%" ~ WS ~ datatype }

    def datatype = rule { qualified_name }

    def convenienceNotation = rule { (string_literal ~ optional (langtag) ~> makeStringAttribute ) | int_literal  ~> makeIntAttribute | qualified_name_literal  ~> makeQualifiedNameAttribute }

    def qualified_name_literal = rule { '\'' ~ qualified_name ~ '\'' ~ WS }

    def int_literal = rule { capture ( optional ( marker ) ~ oneOrMore ( CharPredicate.Digit ) ) ~> makeText ~ WS }

    private val marker: CharPredicate = CharPredicate('-')

     def string_literal: Rule1[String] = rule { string_literal_long2 | string_literal2 }

    def string_literal2 = rule { '"' ~ capture (zeroOrMore(noneOf("\"\\\n") | echar )) ~> makeText ~ '"' ~ WS } // TODO: get proper grammar
 
    def string_literal_long2 =  rule { "\"\"\"" ~ capture (zeroOrMore(optional(ch('"') | "\"\"") ~ ( noneOf("\"\\") | echar ))) ~> makeText ~ "\"\"\"" ~ WS }

    val string_escape_chars = CharPredicate("tbnrf\\\"\'")
    private def echar = rule { '\\' ~ string_escape_chars }

    //http://www.w3.org/TR/rdf-sparql-query/#rLANGTAG
    //def langtag = rule { '@' ~ capture (oneOrMore( CharPredicate.Alpha ) ~ zeroOrMore ( '-' ~ oneOrMore ( CharPredicate.Alpha ++ CharPredicate.Digit ) ))  ~> makeText ~ WS }
    
    def langtag = rule { '@' ~ capture (oneOrMore( CharPredicate.Alpha ) ~ zeroOrMore ( marker ~ oneOrMore ( CharPredicate.Alpha ++ CharPredicate.Digit ) ))  ~> makeText ~ WS }


    // TODO: http://www.w3.org/TR/xmlschema11-2/#nt-dateTimeRep
    def datetime = rule { capture (CharPredicate.Digit ~ CharPredicate.Digit ~ CharPredicate.Digit ~ CharPredicate.Digit ~ '-' ~ CharPredicate.Digit ~ CharPredicate.Digit ~ '-' ~ CharPredicate.Digit ~ CharPredicate.Digit ~ 'T' ~ CharPredicate.Digit ~ CharPredicate.Digit ~ ':' ~ CharPredicate.Digit ~ CharPredicate.Digit ~ ':' ~ CharPredicate.Digit ~ CharPredicate.Digit ~ optional ('.' ~ CharPredicate.Digit  ~ zeroOrMore(CharPredicate.Digit)) ~  optional('Z' | TimeZoneOffset) ) ~ WS ~> makeTime }

    def optional_datetime:Rule1[Option[XMLGregorianCalendar]] = rule { datetime ~> makeOptionalTime | '-' ~ WS ~> makeNoTime }

    def TimeZoneOffset = rule { SIGN ~  CharPredicate.Digit ~ CharPredicate.Digit ~ ':' ~ CharPredicate.Digit ~ CharPredicate.Digit }

    private val SIGN: CharPredicate = CharPredicate("+-")
  //   def dateTimeLexicalRep = rule { yearFrag ~ '-' ~ monthFrag ~ '-' ~ dayFrag ~ 'T' ~ ( (hourFrag ~ ':' ~ minuteFrag ~ ':' ~ secondFrag) |  endOfDayFrag) ~ optional (timezoneFrag) }


    

    def document = rule { WS ~ "document" ~ WS ~ optional(namespaceDeclarations) ~> postStartDocument ~  zeroOrMore (statement ~> postStatement | bundle ) ~ WS ~ "endDocument"  ~> postEndDocument ~ WS  }



    def bundle = rule { WS ~ "bundle" ~> openBundle ~ WS ~ qualified_nameAsString ~ WS  ~ optional(namespaceDeclarations) ~> postStartBundle ~ zeroOrMore (statement ~> postStatement) ~ WS ~ "endBundle" ~> postEndBundle ~ WS }

    

  /* parser action */
    val makeEntity: (QualifiedName,Seq[Attribute]) => Entity
    val makeAgent: (QualifiedName,Seq[Attribute]) => Agent
    val makeActivity: (QualifiedName,Option[XMLGregorianCalendar], Option[XMLGregorianCalendar], Seq[Attribute]) => Activity
    val makeWasGeneratedByWithId: (QualifiedName,QualifiedName,Option[QualifiedName],Option[XMLGregorianCalendar], Seq[Attribute]) => WasGeneratedBy
    val makeWasGeneratedByWithId2: (QualifiedName,QualifiedName, Seq[Attribute]) => WasGeneratedBy
    val makeWasGeneratedByNoId: (QualifiedName,Option[QualifiedName],Option[XMLGregorianCalendar], Seq[Attribute]) => WasGeneratedBy
    val makeWasGeneratedByNoId2: (QualifiedName, Seq[Attribute]) => WasGeneratedBy
    val makeWasInvalidatedByWithId: (QualifiedName,QualifiedName,Option[QualifiedName],Option[XMLGregorianCalendar], Seq[Attribute]) => WasInvalidatedBy
    val makeWasInvalidatedByWithId2: (QualifiedName,QualifiedName, Seq[Attribute]) => WasInvalidatedBy
    val makeWasInvalidatedByNoId: (QualifiedName,Option[QualifiedName],Option[XMLGregorianCalendar], Seq[Attribute]) => WasInvalidatedBy
    val makeWasInvalidatedByNoId2: (QualifiedName, Seq[Attribute]) => WasInvalidatedBy
    val makeWasAssociatedWithWithId: (QualifiedName,QualifiedName,Option[QualifiedName],Option[QualifiedName], Seq[Attribute]) => WasAssociatedWith
    val makeWasAssociatedWithWithId2: (QualifiedName,QualifiedName, Seq[Attribute]) => WasAssociatedWith
    val makeWasAssociatedWithNoId: (QualifiedName,Option[QualifiedName],Option[QualifiedName], Seq[Attribute]) => WasAssociatedWith
    val makeWasAssociatedWithNoId2: (QualifiedName, Seq[Attribute]) => WasAssociatedWith
    val makeActedOnBehalfOfWithId: (QualifiedName,QualifiedName,QualifiedName,Option[QualifiedName], Seq[Attribute]) => ActedOnBehalfOf
    val makeActedOnBehalfOfWithId2: (QualifiedName,QualifiedName,QualifiedName, Seq[Attribute]) => ActedOnBehalfOf
    val makeActedOnBehalfOfNoId: (QualifiedName,QualifiedName,Option[QualifiedName], Seq[Attribute]) => ActedOnBehalfOf
    val makeActedOnBehalfOfNoId2: (QualifiedName, QualifiedName, Seq[Attribute]) => ActedOnBehalfOf
    val makeWasDerivedFromWithId: (QualifiedName,QualifiedName,QualifiedName,Option[QualifiedName],Option[QualifiedName],Option[QualifiedName], Seq[Attribute]) => WasDerivedFrom
    val makeWasDerivedFromWithId2: (QualifiedName,QualifiedName,QualifiedName, Seq[Attribute]) => WasDerivedFrom
    val makeWasDerivedFromNoId: (QualifiedName,QualifiedName,Option[QualifiedName],Option[QualifiedName],Option[QualifiedName], Seq[Attribute]) => WasDerivedFrom
    val makeWasDerivedFromNoId2: (QualifiedName, QualifiedName, Seq[Attribute]) => WasDerivedFrom
    val makeWasAttributedToWithId: (QualifiedName,QualifiedName,QualifiedName, Seq[Attribute]) => WasAttributedTo
    val makeWasAttributedToNoId: (QualifiedName,QualifiedName, Seq[Attribute]) => WasAttributedTo
    val makeWasInfluencedByWithId: (QualifiedName,QualifiedName,QualifiedName, Seq[Attribute]) => WasInfluencedBy
    val makeWasInfluencedByNoId: (QualifiedName,QualifiedName, Seq[Attribute]) => WasInfluencedBy
    val makeWasInformedByWithId: (QualifiedName,QualifiedName,QualifiedName, Seq[Attribute]) => WasInformedBy
    val makeWasInformedByNoId: (QualifiedName,QualifiedName, Seq[Attribute]) => WasInformedBy
    val makeUsedWithId: (QualifiedName,QualifiedName,Option[QualifiedName],Option[XMLGregorianCalendar], Seq[Attribute]) => Used
    val makeUsedWithId2: (QualifiedName,QualifiedName, Seq[Attribute]) => Used
    val makeUsedNoId: (QualifiedName,Option[QualifiedName],Option[XMLGregorianCalendar], Seq[Attribute]) => Used
    val makeUsedNoId2: (QualifiedName, Seq[Attribute]) => Used
    val makeWasStartedByWithId: (QualifiedName,QualifiedName,Option[QualifiedName],Option[QualifiedName],Option[XMLGregorianCalendar], Seq[Attribute]) => WasStartedBy
    val makeWasStartedByWithId2: (QualifiedName,QualifiedName, Seq[Attribute]) => WasStartedBy
    val makeWasStartedByNoId: (QualifiedName,Option[QualifiedName],Option[QualifiedName],Option[XMLGregorianCalendar], Seq[Attribute]) => WasStartedBy
    val makeWasStartedByNoId2: (QualifiedName, Seq[Attribute]) => WasStartedBy
    val makeWasEndedByWithId: (QualifiedName,QualifiedName,Option[QualifiedName],Option[QualifiedName],Option[XMLGregorianCalendar], Seq[Attribute]) => WasEndedBy
    val makeWasEndedByWithId2: (QualifiedName,QualifiedName, Seq[Attribute]) => WasEndedBy
    val makeWasEndedByNoId: (QualifiedName,Option[QualifiedName],Option[QualifiedName],Option[XMLGregorianCalendar], Seq[Attribute]) => WasEndedBy
    val makeWasEndedByNoId2: (QualifiedName, Seq[Attribute]) => WasEndedBy
    val makeSpecializationOf: (QualifiedName,QualifiedName) => SpecializationOf
    val makeQualifiedSpecializationOfWithId: (QualifiedName,QualifiedName,QualifiedName, Seq[Attribute]) => SpecializationOf
    val makeQualifiedSpecializationOfNoId: (QualifiedName,QualifiedName, Seq[Attribute]) => SpecializationOf
    val makeAlternateOf: (QualifiedName,QualifiedName) => AlternateOf
    val makeMentionOf: (QualifiedName,QualifiedName,QualifiedName) => MentionOf
    val makeQualifiedAlternateOfWithId: (QualifiedName,QualifiedName,QualifiedName, Seq[Attribute]) => AlternateOf
    val makeQualifiedAlternateOfNoId: (QualifiedName,QualifiedName, Seq[Attribute]) => AlternateOf
    val makeHadMember: (QualifiedName,QualifiedName) => HadMember
    val makeQualifiedHadMemberOfWithId: (QualifiedName,QualifiedName,QualifiedName, Seq[Attribute]) => HadMember
    val makeQualifiedHadMemberOfNoId: (QualifiedName,QualifiedName, Seq[Attribute]) => HadMember
    val makeActivityNoTime: (QualifiedName, Seq[Attribute]) => Activity
    val makeAttribute: (QualifiedName, String, QualifiedName) => Attribute
    val makeStringAttribute: (QualifiedName, String, Option[String]) => Attribute
    val makeIntAttribute: (QualifiedName, String) => Attribute
    val makeQualifiedNameAttribute: (QualifiedName, QualifiedName) => Attribute

    val makeBundle: (String, Seq[Statement]) => Bundle = (name: String, r: Seq[Statement]) => {
      val ns=theNamespace()
      val qn=makeQualifiedName(name)
      closeBundle()
      new Bundle(qn, r,ns)
    }
    val openBundle: () => Unit
    def closeBundle: () => Unit

    val makeEmptyAttributeSet: () => Seq[Attribute] = () => Seq() :Seq[Attribute]
    val makeAttributeSet: (Attribute, Seq[Attribute]) => Seq[Attribute] = (a: Attribute, r: Seq[Attribute]) => (r :+ a) :Seq[Attribute]
    val makeAttributeSetFromOption: Option[Seq[Attribute]] => Seq[Attribute] = (s: Option[Seq[Attribute]]) =>  (s match { case Some(s) => s; case _ => Seq() } ) :Seq[Attribute]
    val makeTime: (String) => XMLGregorianCalendar
    val makeOptionalTime: (XMLGregorianCalendar) => Option[XMLGregorianCalendar]
    val makeNoTime: () => Option[XMLGregorianCalendar] = () => None: Option[XMLGregorianCalendar]
    val makeOptionalIdentifier: QualifiedName => Option[QualifiedName] = (q: QualifiedName) => Some(q): Option[QualifiedName]
    val makeNoIdentifier: () => Option[QualifiedName] = () => None: Option[QualifiedName]

 /* Streaming support */  
    val postStatement: Statement => Unit
    val postStartDocument: () => Unit
    val postStartBundle: String => Unit
    val postEndBundle: () => Unit
    val postEndDocument: () => Unit
}

trait ProvStream {
  val postStatement: Statement => Unit
  val postStartDocument: Namespace => Unit
  val postStartBundle: (QualifiedName, Namespace) => Unit
  val postEndBundle: () => Unit
  val postEndDocument: () => Unit
}

/*

class MyParser2(override val input: ParserInput) extends MyParser(input,new Namespace,None, new DocBuilder)  {
  docns.addKnownNamespaces()
  docns.register("provext", "http://openprovenance.org/prov/extension#")


}
 */

final class MyActions  {

  def nullable [T >: Null](x:Option[T]):T = x match { case Some(s) => s; case None => null:T }


  val makeText: String => String = (text: String) => text

  val makeEntity: (QualifiedName, Seq[Attribute]) => Entity = (n: QualifiedName, attr: Seq[Attribute]) => pf.newEntity(n, attr)

  val makeAgent: (QualifiedName, Seq[Attribute]) => Agent = (n: QualifiedName, attr: Seq[Attribute]) => pf.newAgent(n, attr)

  val makeActivity: (QualifiedName, Option[XMLGregorianCalendar], Option[XMLGregorianCalendar], Seq[Attribute]) => Activity = (n: QualifiedName, start: Option[XMLGregorianCalendar], end: Option[XMLGregorianCalendar], attr: Seq[Attribute]) => Activity {
    pf.newActivity(n, start, end, attr)
  }


  val makeActivityNoTime: (QualifiedName, Seq[Attribute]) => Activity = (n: QualifiedName, attr: Seq[Attribute]) => Activity {
    pf.newActivity(n, None, None, attr)
  }

  val makeWasGeneratedByWithId: (QualifiedName, QualifiedName, Option[QualifiedName], Option[XMLGregorianCalendar], Seq[Attribute]) => WasGeneratedBy = (id: QualifiedName, e: QualifiedName, a: Option[QualifiedName], t: Option[XMLGregorianCalendar], attr: Seq[Attribute]) =>  pf.newWasGeneratedBy(id,e,nullable(a),t,attr)

  val makeWasGeneratedByWithId2: (QualifiedName, QualifiedName, Seq[Attribute]) => WasGeneratedBy = (id: QualifiedName, e: QualifiedName, attr: Seq[Attribute]) =>  makeWasGeneratedByWithId(id,e,None,None,attr)

  val makeWasGeneratedByNoId: (QualifiedName, Option[QualifiedName], Option[XMLGregorianCalendar], Seq[Attribute]) => WasGeneratedBy = (e: QualifiedName, a: Option[QualifiedName], t: Option[XMLGregorianCalendar], attr: Seq[Attribute]) =>  pf.newWasGeneratedBy(null,e,nullable(a),t,attr)

  val makeWasGeneratedByNoId2: (QualifiedName, Seq[Attribute]) => WasGeneratedBy = (e: QualifiedName, attr: Seq[Attribute]) =>   makeWasGeneratedByNoId(e,None,None,attr)

  val makeWasInvalidatedByWithId: (QualifiedName, QualifiedName, Option[QualifiedName], Option[XMLGregorianCalendar], Seq[Attribute]) => WasInvalidatedBy = (id: QualifiedName, e: QualifiedName, a: Option[QualifiedName], t: Option[XMLGregorianCalendar], attr: Seq[Attribute]) =>  pf.newWasInvalidatedBy(id,e,nullable(a),t,attr)

  val makeWasInvalidatedByWithId2: (QualifiedName, QualifiedName, Seq[Attribute]) => WasInvalidatedBy = (id: QualifiedName, e: QualifiedName, attr: Seq[Attribute]) =>  makeWasInvalidatedByWithId(id,e,None,None,attr)

  val makeWasInvalidatedByNoId: (QualifiedName, Option[QualifiedName], Option[XMLGregorianCalendar], Seq[Attribute]) => WasInvalidatedBy = (e: QualifiedName, a: Option[QualifiedName], t: Option[XMLGregorianCalendar], attr: Seq[Attribute]) =>  pf.newWasInvalidatedBy(null,e,nullable(a),t,attr)

  val makeWasInvalidatedByNoId2: (QualifiedName, Seq[Attribute]) => WasInvalidatedBy = (e: QualifiedName, attr: Seq[Attribute]) =>   makeWasInvalidatedByNoId(e,None,None,attr)

  val makeWasAssociatedWithWithId: (QualifiedName, QualifiedName, Option[QualifiedName], Option[QualifiedName], Seq[Attribute]) => WasAssociatedWith = (id: QualifiedName, a: QualifiedName, ag: Option[QualifiedName], pl: Option[QualifiedName], attr: Seq[Attribute]) =>  pf.newWasAssociatedWith(id,a,nullable(ag),nullable(pl),attr)

  val makeWasAssociatedWithWithId2: (QualifiedName, QualifiedName, Seq[Attribute]) => WasAssociatedWith = (id: QualifiedName, e: QualifiedName, attr: Seq[Attribute]) =>  makeWasAssociatedWithWithId(id,e,None,None,attr)

  val makeWasAssociatedWithNoId: (QualifiedName, Option[QualifiedName], Option[QualifiedName], Seq[Attribute]) => WasAssociatedWith = (a: QualifiedName, ag: Option[QualifiedName], pl: Option[QualifiedName], attr: Seq[Attribute]) =>  pf.newWasAssociatedWith(null,a,nullable(ag),nullable(pl),attr)

  val makeWasAssociatedWithNoId2: (QualifiedName, Seq[Attribute]) => WasAssociatedWith = (e: QualifiedName, attr: Seq[Attribute]) =>   makeWasAssociatedWithNoId(e,None,None,attr)

  val makeActedOnBehalfOfWithId: (QualifiedName, QualifiedName, QualifiedName, Option[QualifiedName], Seq[Attribute]) => ActedOnBehalfOf = (id: QualifiedName, ag2: QualifiedName, ag1: QualifiedName, a: Option[QualifiedName], attr: Seq[Attribute]) =>  pf.newActedOnBehalfOf(id,ag2,ag1,nullable(a),attr)

  val makeActedOnBehalfOfWithId2: (QualifiedName, QualifiedName, QualifiedName, Seq[Attribute]) => ActedOnBehalfOf = (id: QualifiedName, ag2: QualifiedName, ag1: QualifiedName, attr: Seq[Attribute]) =>  makeActedOnBehalfOfWithId(id,ag2,ag1,None,attr)

  val makeActedOnBehalfOfNoId: (QualifiedName, QualifiedName, Option[QualifiedName], Seq[Attribute]) => ActedOnBehalfOf = (ag2: QualifiedName, ag1: QualifiedName, a: Option[QualifiedName], attr: Seq[Attribute]) =>  pf.newActedOnBehalfOf(null,ag2,ag1,nullable(a),attr)

  val makeActedOnBehalfOfNoId2: (QualifiedName, QualifiedName, Seq[Attribute]) => ActedOnBehalfOf = (ag2: QualifiedName, ag1: QualifiedName, attr: Seq[Attribute]) =>   makeActedOnBehalfOfNoId(ag2,ag1,None,attr)

  val makeWasDerivedFromWithId: (QualifiedName, QualifiedName, QualifiedName, Option[QualifiedName], Option[QualifiedName], Option[QualifiedName], Seq[Attribute]) => WasDerivedFrom = (id: QualifiedName, e2: QualifiedName, e1: QualifiedName, a: Option[QualifiedName], gen: Option[QualifiedName], use: Option[QualifiedName], attr: Seq[Attribute]) =>  pf.newWasDerivedFrom(id,e2,e1,nullable(a),nullable(gen),nullable(use),attr)

  val makeWasDerivedFromWithId2: (QualifiedName, QualifiedName, QualifiedName, Seq[Attribute]) => WasDerivedFrom = (id: QualifiedName, e2: QualifiedName, e1: QualifiedName, attr: Seq[Attribute]) =>  makeWasDerivedFromWithId(id,e2,e1,None,None,None,attr)

  val makeWasDerivedFromNoId: (QualifiedName, QualifiedName, Option[QualifiedName], Option[QualifiedName], Option[QualifiedName], Seq[Attribute]) => WasDerivedFrom = (e2: QualifiedName, e1: QualifiedName, a: Option[QualifiedName], gen: Option[QualifiedName], use: Option[QualifiedName], attr: Seq[Attribute]) =>  pf.newWasDerivedFrom(null,e2,e1,nullable(a),nullable(gen),nullable(use),attr)

  val makeWasDerivedFromNoId2: (QualifiedName, QualifiedName, Seq[Attribute]) => WasDerivedFrom = (e2: QualifiedName, e1: QualifiedName, attr: Seq[Attribute]) =>   makeWasDerivedFromNoId(e2,e1,None,None,None,attr)

  val makeWasAttributedToWithId: (QualifiedName, QualifiedName, QualifiedName, Seq[Attribute]) => WasAttributedTo = (id: QualifiedName, e: QualifiedName, ag: QualifiedName, attr: Seq[Attribute]) => pf.newWasAttributedTo(id,e,ag,attr)

  val makeWasAttributedToNoId: (QualifiedName, QualifiedName, Seq[Attribute]) => WasAttributedTo = (e: QualifiedName, ag: QualifiedName, attr: Seq[Attribute]) => pf.newWasAttributedTo(null,e,ag,attr)

  val makeWasInfluencedByWithId: (QualifiedName, QualifiedName, QualifiedName, Seq[Attribute]) => WasInfluencedBy = (id: QualifiedName, r2: QualifiedName, r1: QualifiedName, attr: Seq[Attribute]) => pf.newWasInfluencedBy(id,r2,r1,attr)

  val makeWasInfluencedByNoId: (QualifiedName, QualifiedName, Seq[Attribute]) => WasInfluencedBy = (r2: QualifiedName, r1: QualifiedName, attr: Seq[Attribute]) => pf.newWasInfluencedBy(null,r2,r1,attr)

  val makeWasInformedByWithId: (QualifiedName, QualifiedName, QualifiedName, Seq[Attribute]) => WasInformedBy = (id: QualifiedName, r2: QualifiedName, r1: QualifiedName, attr: Seq[Attribute]) => pf.newWasInformedBy(id,r2,r1,attr)

  val makeWasInformedByNoId: (QualifiedName, QualifiedName, Seq[Attribute]) => WasInformedBy = (r2: QualifiedName, r1: QualifiedName, attr: Seq[Attribute]) => pf.newWasInformedBy(null,r2,r1,attr)

  val makeUsedWithId: (QualifiedName, QualifiedName, Option[QualifiedName], Option[XMLGregorianCalendar], Seq[Attribute]) => Used = (id: QualifiedName, e: QualifiedName, a: Option[QualifiedName], t: Option[XMLGregorianCalendar], attr: Seq[Attribute]) =>  pf.newUsed(id,e,nullable(a),t,attr)

  val makeUsedWithId2: (QualifiedName, QualifiedName, Seq[Attribute]) => Used = (id: QualifiedName, e: QualifiedName, attr: Seq[Attribute]) =>  makeUsedWithId(id,e,None,None,attr)

  val makeUsedNoId: (QualifiedName, Option[QualifiedName], Option[XMLGregorianCalendar], Seq[Attribute]) => Used = (e: QualifiedName, a: Option[QualifiedName], t: Option[XMLGregorianCalendar], attr: Seq[Attribute]) =>  pf.newUsed(null,e,nullable(a),t,attr)

  val makeUsedNoId2: (QualifiedName, Seq[Attribute]) => Used = (e: QualifiedName, attr: Seq[Attribute]) =>   makeUsedNoId(e,None,None,attr)

  val makeWasStartedByWithId: (QualifiedName, QualifiedName, Option[QualifiedName], Option[QualifiedName], Option[XMLGregorianCalendar], Seq[Attribute]) => WasStartedBy = (id: QualifiedName, a: QualifiedName, trigger: Option[QualifiedName], starter: Option[QualifiedName], t: Option[XMLGregorianCalendar], attr: Seq[Attribute]) =>  pf.newWasStartedBy(id,a,nullable(trigger),nullable(starter),t,attr)

  val makeWasStartedByWithId2: (QualifiedName, QualifiedName, Seq[Attribute]) => WasStartedBy = (id: QualifiedName, a: QualifiedName, attr: Seq[Attribute]) =>  makeWasStartedByWithId(id,a,None,None,None,attr)

  val makeWasStartedByNoId: (QualifiedName, Option[QualifiedName], Option[QualifiedName], Option[XMLGregorianCalendar], Seq[Attribute]) => WasStartedBy = (a: QualifiedName, trigger: Option[QualifiedName], starter: Option[QualifiedName], t: Option[XMLGregorianCalendar], attr: Seq[Attribute]) =>  pf.newWasStartedBy(null,a,nullable(trigger),nullable(starter),t,attr)

  val makeWasStartedByNoId2: (QualifiedName, Seq[Attribute]) => WasStartedBy = (a: QualifiedName, attr: Seq[Attribute]) =>   makeWasStartedByNoId(a,None,None,None,attr)

  val makeWasEndedByWithId: (QualifiedName, QualifiedName, Option[QualifiedName], Option[QualifiedName], Option[XMLGregorianCalendar], Seq[Attribute]) => WasEndedBy = (id: QualifiedName, a: QualifiedName, trigger: Option[QualifiedName], starter: Option[QualifiedName], t: Option[XMLGregorianCalendar], attr: Seq[Attribute]) =>  pf.newWasEndedBy(id,a,nullable(trigger),nullable(starter),t,attr)

  val makeWasEndedByWithId2: (QualifiedName, QualifiedName, Seq[Attribute]) => WasEndedBy = (id: QualifiedName, a: QualifiedName, attr: Seq[Attribute]) =>  makeWasEndedByWithId(id,a,None,None,None,attr)

  val makeWasEndedByNoId: (QualifiedName, Option[QualifiedName], Option[QualifiedName], Option[XMLGregorianCalendar], Seq[Attribute]) => WasEndedBy = (a: QualifiedName, trigger: Option[QualifiedName], starter: Option[QualifiedName], t: Option[XMLGregorianCalendar], attr: Seq[Attribute]) =>  pf.newWasEndedBy(null,a,nullable(trigger),nullable(starter),t,attr)

  val makeWasEndedByNoId2: (QualifiedName, Seq[Attribute]) => WasEndedBy = (a: QualifiedName, attr: Seq[Attribute]) =>   makeWasEndedByNoId(a,None,None,None,attr)

  val makeSpecializationOf: (QualifiedName, QualifiedName) => SpecializationOf = (e2: QualifiedName, e1:QualifiedName) => pf.newSpecializationOf(e2,e1)

  val makeMentionOf: (QualifiedName, QualifiedName, QualifiedName) => MentionOf = (e2: QualifiedName, e1:QualifiedName, b: QualifiedName) => pf.newMentionOf(e2,e1,b)


  val makeQualifiedSpecializationOfWithId: (QualifiedName, QualifiedName, QualifiedName, Seq[Attribute]) => SpecializationOf = (id:QualifiedName, e2:QualifiedName, e1:QualifiedName, attr:Seq[Attribute]) => pf.newSpecializationOf(id,e2,e1,attr)

  val makeQualifiedSpecializationOfNoId: (QualifiedName, QualifiedName, Seq[Attribute]) => SpecializationOf = (e2:QualifiedName, e1:QualifiedName, attr:Seq[Attribute]) => pf.newSpecializationOf(null,e2,e1,attr)

  val makeAlternateOf: (QualifiedName, QualifiedName) => AlternateOf = (e2: QualifiedName, e1: QualifiedName) =>  pf.newAlternateOf(e2,e1)

  val makeQualifiedAlternateOfWithId: (QualifiedName, QualifiedName, QualifiedName, Seq[Attribute]) => AlternateOf = (id:QualifiedName, e2:QualifiedName, e1:QualifiedName, attr:Seq[Attribute]) => pf.newAlternateOf(id,e2,e1,attr)

  val makeQualifiedAlternateOfNoId: (QualifiedName, QualifiedName, Seq[Attribute]) => AlternateOf = (e2:QualifiedName, e1:QualifiedName, attr:Seq[Attribute]) => pf.newAlternateOf(null,e2,e1,attr)

  val makeHadMember: (QualifiedName, QualifiedName) => HadMember = (e2:QualifiedName, e1:QualifiedName) => pf.newHadMember(e2,Set(e1))

  val makeQualifiedHadMemberOfWithId: (QualifiedName, QualifiedName, QualifiedName, Seq[Attribute]) => HadMember = (id:QualifiedName, e2:QualifiedName, e1:QualifiedName, attr:Seq[Attribute]) => pf.newHadMember(id,e2,Set(e1),attr)

  val makeQualifiedHadMemberOfNoId: (QualifiedName, QualifiedName, Seq[Attribute]) => HadMember = (e2:QualifiedName, e1:QualifiedName, attr:Seq[Attribute]) => pf.newHadMember(null,e2,Set(e1),attr)


  val makeAttribute: (QualifiedName, String, QualifiedName) => Attribute = (attr: QualifiedName, literal: String, datatype: QualifiedName) => Attribute {
    //println("creating attr for " ++ attr.toString ++ " " ++  literal ++ " " ++ datatype.toString)
    pf.newAttribute(attr, literal, datatype)
  }

  val makeStringAttribute: (QualifiedName, String, Option[String]) => Attribute = (attr: QualifiedName, literal: String, lang: Option[String]) => Attribute {
    //println("creating attr (str) for " ++ attr.toString ++ " " ++  literal ++ " " + lang)
    lang match {
      case None    => pf.newAttribute(attr, literal, pf.xsd_string)
      case Some(l) => pf.newAttribute(attr, pf.newInternationalizedString(literal,l), pf.xsd_string)
    }

  }
  val makeIntAttribute: (QualifiedName, String) => Attribute = (attr: QualifiedName, literal: String) => Attribute {
    //println("creating attr (int) for " ++ attr.toString ++ " " ++  literal)
    pf.newAttribute(attr, literal, pf.xsd_int)
  }

  val makeQualifiedNameAttribute: (QualifiedName, QualifiedName) => Attribute = (attr: QualifiedName, literal: QualifiedName) => Attribute {
    //println("creating attr (qn) for " ++ attr.toString ++ " " ++  literal.toString)
    pf.newAttribute(attr, literal, pf.prov_qualified_name)
  }

  val makeTime: String => XMLGregorianCalendar = (s: String) => pf.newISOTime(s): XMLGregorianCalendar

  val makeOptionalTime: XMLGregorianCalendar => Option[XMLGregorianCalendar] = (t:XMLGregorianCalendar) => Option(t): Option[XMLGregorianCalendar]


}


final class MyActions2 {

   var docns: Namespace=null
   var bun_ns: Option[Namespace]=None
   var next: ProvStream=null



  val theNamespace: () => Namespace = () => bun_ns match {
    case Some(ns) => ns
    case None => docns
  }

  val makeQualifiedName: String => QualifiedName = (s:String) => QualifiedName {
    //println("creating qn for " ++ s ++ " with ns " ++ ns.toString())
    theNamespace().stringToQualifiedName(s,pf).asInstanceOf[QualifiedName]
  }


  val openBundle: () => Unit = () => {
    val ns=pf.newNamespace()
    bun_ns=Some(ns)
    ns.addKnownNamespaces()
    ns.register("provext", "http://openprovenance.org/prov/extension#");
    ns.setParent(docns)
  }

  val closeBundle: () => Unit = () => { bun_ns=None }

  /* Streaming support */

  val postStartBundle: String => Unit = (name: String) => {
    val ns=theNamespace()
    val qn=makeQualifiedName(name)
    next.postStartBundle(qn,ns)
  }

  val postStatement: Statement => Unit = (s: Statement) => {
    next.postStatement(s)
  }

  val postStartDocument: () => Unit = () => {
    next.postStartDocument(theNamespace())
  }

  val postEndDocument: () => Unit = () => {
    next.postEndDocument()
  }

  val postEndBundle: () => Unit = () => {
    next.postEndBundle()
  }


}

final class MyParser(val input: ParserInput, val actions2:MyActions2, val actions:MyActions =new MyActions) extends ProvnParser {


  def getNext(): ProvStream = actions2.next

  override val makeText: String => String = actions.makeText

  override val makeEntity: (QualifiedName, Seq[Attribute]) => Entity = actions.makeEntity

  override val makeAgent: (QualifiedName, Seq[Attribute]) => Agent = actions.makeAgent

  override val makeActivity: (QualifiedName, Option[XMLGregorianCalendar], Option[XMLGregorianCalendar], Seq[Attribute]) => Activity = actions.makeActivity

  override val makeActivityNoTime: (QualifiedName, Seq[Attribute]) => Activity = actions.makeActivityNoTime

  def nullable [T >: Null](x:Option[T]):T = x match { case Some(s) => s; case None => null:T }

  override val makeWasGeneratedByWithId: (QualifiedName, QualifiedName, Option[QualifiedName], Option[XMLGregorianCalendar], Seq[Attribute]) => WasGeneratedBy = actions.makeWasGeneratedByWithId

  override val makeWasGeneratedByWithId2: (QualifiedName, QualifiedName, Seq[Attribute]) => WasGeneratedBy = actions.makeWasGeneratedByWithId2

  override val makeWasGeneratedByNoId: (QualifiedName, Option[QualifiedName], Option[XMLGregorianCalendar], Seq[Attribute]) => WasGeneratedBy = actions.makeWasGeneratedByNoId

  override val makeWasGeneratedByNoId2: (QualifiedName, Seq[Attribute]) => WasGeneratedBy = actions.makeWasGeneratedByNoId2

  override val makeWasInvalidatedByWithId: (QualifiedName, QualifiedName, Option[QualifiedName], Option[XMLGregorianCalendar], Seq[Attribute]) => WasInvalidatedBy = actions.makeWasInvalidatedByWithId

  override val makeWasInvalidatedByWithId2: (QualifiedName, QualifiedName, Seq[Attribute]) => WasInvalidatedBy = actions.makeWasInvalidatedByWithId2

  override val makeWasInvalidatedByNoId: (QualifiedName, Option[QualifiedName], Option[XMLGregorianCalendar], Seq[Attribute]) => WasInvalidatedBy = actions.makeWasInvalidatedByNoId

  override val makeWasInvalidatedByNoId2: (QualifiedName, Seq[Attribute]) => WasInvalidatedBy = actions.makeWasInvalidatedByNoId2

  override val makeWasAssociatedWithWithId: (QualifiedName, QualifiedName, Option[QualifiedName], Option[QualifiedName], Seq[Attribute]) => WasAssociatedWith = actions.makeWasAssociatedWithWithId

  override val makeWasAssociatedWithWithId2: (QualifiedName, QualifiedName, Seq[Attribute]) => WasAssociatedWith = actions.makeWasAssociatedWithWithId2

  override val makeWasAssociatedWithNoId: (QualifiedName, Option[QualifiedName], Option[QualifiedName], Seq[Attribute]) => WasAssociatedWith = actions.makeWasAssociatedWithNoId

  override val makeWasAssociatedWithNoId2: (QualifiedName, Seq[Attribute]) => WasAssociatedWith = actions.makeWasAssociatedWithNoId2

  override val makeActedOnBehalfOfWithId: (QualifiedName, QualifiedName, QualifiedName, Option[QualifiedName], Seq[Attribute]) => ActedOnBehalfOf = actions.makeActedOnBehalfOfWithId

  override val makeActedOnBehalfOfWithId2: (QualifiedName, QualifiedName, QualifiedName, Seq[Attribute]) => ActedOnBehalfOf = actions.makeActedOnBehalfOfWithId2

  override val makeActedOnBehalfOfNoId: (QualifiedName, QualifiedName, Option[QualifiedName], Seq[Attribute]) => ActedOnBehalfOf = actions.makeActedOnBehalfOfNoId

  override val makeActedOnBehalfOfNoId2: (QualifiedName, QualifiedName, Seq[Attribute]) => ActedOnBehalfOf = actions.makeActedOnBehalfOfNoId2

  override val makeWasDerivedFromWithId: (QualifiedName, QualifiedName, QualifiedName, Option[QualifiedName], Option[QualifiedName], Option[QualifiedName], Seq[Attribute]) => WasDerivedFrom = actions.makeWasDerivedFromWithId

  override val makeWasDerivedFromWithId2: (QualifiedName, QualifiedName, QualifiedName, Seq[Attribute]) => WasDerivedFrom = actions.makeWasDerivedFromWithId2

  override val makeWasDerivedFromNoId: (QualifiedName, QualifiedName, Option[QualifiedName], Option[QualifiedName], Option[QualifiedName], Seq[Attribute]) => WasDerivedFrom = actions.makeWasDerivedFromNoId

  override val makeWasDerivedFromNoId2: (QualifiedName, QualifiedName, Seq[Attribute]) => WasDerivedFrom = actions.makeWasDerivedFromNoId2

  override val makeWasAttributedToWithId: (QualifiedName, QualifiedName, QualifiedName, Seq[Attribute]) => WasAttributedTo = actions.makeWasAttributedToWithId

  override val makeWasAttributedToNoId: (QualifiedName, QualifiedName, Seq[Attribute]) => WasAttributedTo = actions.makeWasAttributedToNoId

  override val makeWasInfluencedByWithId: (QualifiedName, QualifiedName, QualifiedName, Seq[Attribute]) => WasInfluencedBy = actions.makeWasInfluencedByWithId

  override val makeWasInfluencedByNoId: (QualifiedName, QualifiedName, Seq[Attribute]) => WasInfluencedBy = actions.makeWasInfluencedByNoId

  override val makeWasInformedByWithId: (QualifiedName, QualifiedName, QualifiedName, Seq[Attribute]) => WasInformedBy = actions.makeWasInformedByWithId

  override val makeWasInformedByNoId: (QualifiedName, QualifiedName, Seq[Attribute]) => WasInformedBy = actions.makeWasInformedByNoId

  override val makeUsedWithId: (QualifiedName, QualifiedName, Option[QualifiedName], Option[XMLGregorianCalendar], Seq[Attribute]) => Used = actions.makeUsedWithId

  override val makeUsedWithId2: (QualifiedName, QualifiedName, Seq[Attribute]) => Used = actions.makeUsedWithId2

  override val makeUsedNoId: (QualifiedName, Option[QualifiedName], Option[XMLGregorianCalendar], Seq[Attribute]) => Used = actions.makeUsedNoId

  override val makeUsedNoId2: (QualifiedName, Seq[Attribute]) => Used = actions.makeUsedNoId2

  override val makeWasStartedByWithId: (QualifiedName, QualifiedName, Option[QualifiedName], Option[QualifiedName], Option[XMLGregorianCalendar], Seq[Attribute]) => WasStartedBy = actions.makeWasStartedByWithId

  override val makeWasStartedByWithId2: (QualifiedName, QualifiedName, Seq[Attribute]) => WasStartedBy = actions.makeWasStartedByWithId2

  override val makeWasStartedByNoId: (QualifiedName, Option[QualifiedName], Option[QualifiedName], Option[XMLGregorianCalendar], Seq[Attribute]) => WasStartedBy = actions.makeWasStartedByNoId

  override val makeWasStartedByNoId2: (QualifiedName, Seq[Attribute]) => WasStartedBy = actions.makeWasStartedByNoId2

  override val makeWasEndedByWithId: (QualifiedName, QualifiedName, Option[QualifiedName], Option[QualifiedName], Option[XMLGregorianCalendar], Seq[Attribute]) => WasEndedBy = actions.makeWasEndedByWithId

  override val makeWasEndedByWithId2: (QualifiedName, QualifiedName, Seq[Attribute]) => WasEndedBy = actions.makeWasEndedByWithId2

  override val makeWasEndedByNoId: (QualifiedName, Option[QualifiedName], Option[QualifiedName], Option[XMLGregorianCalendar], Seq[Attribute]) => WasEndedBy = actions.makeWasEndedByNoId

  override val makeWasEndedByNoId2: (QualifiedName, Seq[Attribute]) => WasEndedBy = actions.makeWasEndedByNoId2

  override val makeSpecializationOf: (QualifiedName, QualifiedName) => SpecializationOf = actions.makeSpecializationOf

  override val makeMentionOf: (QualifiedName, QualifiedName, QualifiedName) => MentionOf = actions.makeMentionOf

  override val makeQualifiedSpecializationOfWithId: (QualifiedName, QualifiedName, QualifiedName, Seq[Attribute]) => SpecializationOf = actions.makeQualifiedSpecializationOfWithId

  override val makeQualifiedSpecializationOfNoId: (QualifiedName, QualifiedName, Seq[Attribute]) => SpecializationOf = actions.makeQualifiedSpecializationOfNoId

  override val makeAlternateOf: (QualifiedName, QualifiedName) => AlternateOf = actions.makeAlternateOf

  override val makeQualifiedAlternateOfWithId: (QualifiedName, QualifiedName, QualifiedName, Seq[Attribute]) => AlternateOf = actions.makeQualifiedAlternateOfWithId

  override val makeQualifiedAlternateOfNoId: (QualifiedName, QualifiedName, Seq[Attribute]) => AlternateOf = actions.makeQualifiedAlternateOfNoId

  override val makeHadMember: (QualifiedName, QualifiedName) => HadMember = actions.makeHadMember

  override val makeQualifiedHadMemberOfWithId: (QualifiedName, QualifiedName, QualifiedName, Seq[Attribute]) => HadMember = actions.makeQualifiedHadMemberOfWithId

  override val makeQualifiedHadMemberOfNoId: (QualifiedName, QualifiedName, Seq[Attribute]) => HadMember = actions.makeQualifiedHadMemberOfNoId

  override val makeAttribute: (QualifiedName, String, QualifiedName) => Attribute = actions.makeAttribute

  override val makeStringAttribute: (QualifiedName, String, Option[String]) => Attribute = actions.makeStringAttribute

  override val makeIntAttribute: (QualifiedName, String) => Attribute = actions.makeIntAttribute

  override val makeQualifiedNameAttribute: (QualifiedName, QualifiedName) => Attribute = actions.makeQualifiedNameAttribute

  override val makeTime: String => XMLGregorianCalendar = actions.makeTime

  override val makeOptionalTime: XMLGregorianCalendar => Option[XMLGregorianCalendar] = actions.makeOptionalTime

  // namespace dependents

  override val theNamespace: () => Namespace = actions2.theNamespace

  override val makeQualifiedName: String => QualifiedName = actions2.makeQualifiedName


  override val openBundle: () => Unit = actions2.openBundle

  override val closeBundle: () => Unit = actions2.closeBundle

  /* Streaming support */

  override val postStartBundle: String => Unit = actions2.postStartBundle

  override val postStatement: Statement => Unit = actions2.postStatement

  override val postStartDocument: () => Unit = actions2.postStartDocument

  override val postEndDocument: () => Unit = actions2.postEndDocument

  override val postEndBundle: () => Unit = actions2.postEndBundle


}


class ProvDeserialiser extends org.openprovenance.prov.model.ProvDeserialiser {

  val actions=new MyActions()
  val actions2=new MyActions2()
  val funs=new DocBuilderFunctions()

  override def deserialiseDocument(in: InputStream): model.Document = {
    funs.reset()
    val docBuilder: DocBuilder =new DocBuilder(funs)
    val ns=new Namespace
    ns.addKnownNamespaces()
    ns.register("provext", "http://openprovenance.org/prov/extension#")

    val bufferedSource: BufferedSource =io.Source.fromInputStream(in)

    actions2.docns=ns
    actions2.bun_ns=None
    actions2.next=docBuilder


    val p=new MyParser(bufferedSource.mkString, actions2, actions)
    p.document.run() match {
      case Success(result) => docBuilder.document
      case Failure(e: ParseError) => { println("Expression is not valid: " + p.formatError(e)); throw new RuntimeException("Expression is not valid") }
      case Failure(e) => { println("Unexpected error during parsing run: " + e.printStackTrace); throw new RuntimeException("Unexpected error during parsing run") }
    }
  }
}

object Parser {
  val actions=new MyActions()
  val actions2=new MyActions2()
  val funs=new DocBuilderFunctions()

  def readDocument (s: String): Document = {
    val docBuilder=new DocBuilder(funs)
    val ns=new Namespace
    ns.addKnownNamespaces()
    ns.register("provext", "http://openprovenance.org/prov/extension#")

    actions2.docns=ns
    actions2.bun_ns=None
    actions2.next=docBuilder


    val inputfile : ParserInput = io.Source.fromFile(s: String).mkString

        val p=new MyParser(inputfile,actions2, actions)
        p.document.run() match {
          case Success(result) => docBuilder.document()
          case Failure(e: ParseError) => println("Expression is not valid: " + p.formatError(e)); throw new RuntimeException()
          case Failure(e) => println("Unexpected error during parsing run: " + e.printStackTrace); throw new RuntimeException()
        }
    }
}


object AParser  {

  val actions=new MyActions()
  val actions2=new MyActions2()
  val funs=new DocBuilderFunctions()

  def doCheckDoc (s: ParserInput): Unit = {
    funs.reset()
    val docBuilder=new DocBuilder(funs)
    val stream=new Tee(docBuilder,new SimpleStreamStats)
    val ns=new Namespace
    ns.addKnownNamespaces()
    ns.register("provext", "http://openprovenance.org/prov/extension#")
    actions2.docns=ns
    actions2.bun_ns=None
    actions2.next=docBuilder


    val p=new MyParser(s,actions2, actions)
    p.document.run() match {
      case Success(result) => println(docBuilder.document())
      case Failure(e: ParseError) => println("Expression is not valid: " + p.formatError(e))
      case Failure(e) => println("Unexpected error during parsing run: " + e.printStackTrace)
    }
  }


  def main(args: Array[String]) {
        lazy val inputfile : ParserInput = io.Source.fromFile(args(0)).mkString
/*
        println( "parsing " + new MyParser2("foo").pn_local.run())
        println( "parsing " + new MyParser2("-foo").pn_local.run())
        println( "parsing " + new MyParser2("foo").pn_prefix.run())
        println( "parsing " + new MyParser2("8foo").pn_prefix.run())
        println( "parsing " + new MyParser2("foo:var").qualified_name.run())
        println( "parsing " + new MyParser2("var").qualified_name.run())
        println( "parsing " + new MyParser2("var:").qualified_name.run())
        println( "parsing " + new MyParser2(":var").qualified_name.run())
        println( "parsing " + new MyParser2(":").qualified_name.run())


 */

        doCheckDoc( """
document
prefix ex <http://example.org/>
entity(ex:a)
entity(ex:b)
entity(ex:c2,[ex:d2="hi" %% xsd:string])
entity(ex:c,[ex:d1='ex:e'])
entity(ex:c3,[ex:d3=10])
activity(ex:f,2012-03-02T10:30:00,2012-03-02T10:30:00, [ex:label="foo"]) 
activity(ex:f,-,2012-03-02T10:30:00, [ex:label="foo"@en ]) 
activity(ex:f2,2012-03-02T10:30:00,-,[prov:label="foo"]) 
activity(ex:g,2012-03-02T10:30:00,2012-03-02T10:30:00)
activity(ex:g2,-,-)
activity(ex:g3)
activity(ex:g4,[prov:label="foo"])

wasGeneratedBy(ex:g1;ex:e1,ex:a1,-,[prov:label="gen"])
wasGeneratedBy(-;ex:e1,ex:a1,-,[prov:label="gen1"])
wasGeneratedBy(ex:e1,ex:a1,-,[prov:label="gen2"])
wasGeneratedBy(ex:e1,-,-,[prov:label="gen3"])
wasGeneratedBy(ex:e1,ex:a2,2012-03-02T10:30:00,[prov:label="gen4"])
wasGeneratedBy(-;ex:e1,[prov:label="gen5"])
wasGeneratedBy(ex:e1,[prov:label="gen6"])
wasGeneratedBy(ex:g2;ex:e1,[prov:label="gen7"])


wasInvalidatedBy(ex:inv1;ex:e1,ex:a1,-,[prov:label="inv"])
wasInvalidatedBy(-;ex:e1,ex:a1,-,[prov:label="inv1"])
wasInvalidatedBy(ex:e1,ex:a1,-,[prov:label="inv2"])
wasInvalidatedBy(ex:e1,-,-,[prov:label="inv3"])
wasInvalidatedBy(ex:e1,ex:a2,2012-03-02T10:30:00,[prov:label="inv4"])
wasInvalidatedBy(-;ex:e1,[prov:label="inv5"])
wasInvalidatedBy(ex:e1,[prov:label="inv6"])
wasInvalidatedBy(ex:inv2;ex:e1,[prov:label="inv7"])

used(ex:u1;ex:a1,ex:e1,-,[prov:label="use"])
used(-;ex:a1,ex:e1,-,[prov:label="use1"])
used(ex:a1,ex:e1,-,[prov:label="use2"])
used(ex:a1,-,-,[prov:label="use3"])
used(ex:a1,ex:e2,2012-03-02T10:30:00,[prov:label="use4"])
used(-;ex:e1,[prov:label="use5"])
used(ex:e1,[prov:label="use6"])
used(ex:u2;ex:e1,[prov:label="use7"])

wasAssociatedWith(ex:waw1;ex:a1,ex:ag1,-,[prov:label="waw"])
wasAssociatedWith(-;ex:a1,ex:ag1,-,[prov:label="waw1"])
wasAssociatedWith(ex:a1,ex:ag1,-,[prov:label="waw2"])
wasAssociatedWith(ex:a1,-,-,[prov:label="waw3"])
wasAssociatedWith(ex:a1,ex:ag2,ex:plan,[prov:label="waw4"])
wasAssociatedWith(-;ex:a1,[prov:label="waw5"])
wasAssociatedWith(ex:a1,[prov:label="waw6"])
wasAssociatedWith(ex:waw2;ex:a1,[prov:label="waw7"])

hadMember(ex:c,ex:e)
specializationOf(ex:e1,ex:e2)
alternateOf(ex:e1,ex:e2)

actedOnBehalfOf(ex:derek, ex:chartgen, ex:compose)
actedOnBehalfOf(ex:aobo1; ex:derek, ex:chartgen, ex:compose)
actedOnBehalfOf(-; ex:derek, ex:chartgen, ex:compose,[prov:label="aobo1"])
actedOnBehalfOf(-; ex:derek, ex:chartgen, -,[prov:label="aobo2"])
actedOnBehalfOf(ex:aobo2; ex:derek, ex:chartgen,[prov:label="aobo3"])

wasAttributedTo(ex:chart1, ex:derek)
wasAttributedTo(ex:attr1; ex:chart1, ex:derek)
wasAttributedTo(-; ex:chart1, ex:derek, [prov:label="attr2"])

wasInfluencedBy(ex:chart1, ex:derek)
wasInfluencedBy(ex:infl1; ex:chart1, ex:derek)
wasInfluencedBy(-; ex:chart1, ex:derek, [prov:label="infl2"])

wasInformedBy(ex:a2, ex:a1)
wasInformedBy(ex:comm1; ex:a2, ex:a1)
wasInformedBy(-; ex:a2, ex:a1, [prov:label="comm2"])

wasDerivedFrom(ex:dataSet2, ex:dataSet1, [prov:type='prov:Revision'])
wasDerivedFrom(ex:der1; ex:dataSet2, ex:dataSet1, [prov:type='prov:Revision'])
wasDerivedFrom(ex:der2; ex:dataSet2, ex:dataSet1, ex:a, -, -, [prov:type='prov:Revision'])
wasDerivedFrom(ex:der3; ex:dataSet2, ex:dataSet1, ex:a, ex:gen, -, [prov:type='prov:Revision'])
wasDerivedFrom(ex:der4; ex:dataSet2, ex:dataSet1, ex:a, ex:gen, ex:use, [prov:type='prov:Revision'])
wasDerivedFrom(-; ex:dataSet2, ex:dataSet1, ex:a, ex:gen, ex:use, [prov:type='prov:Revision'])



wasStartedBy(ex:start; ex:act2, ex:trigger, ex:act1, 2011-11-16T16:00:00, [ex:param="a"])
wasStartedBy(ex:act2, -, ex:act1, -)
wasStartedBy(ex:act2, -, ex:act1, 2011-11-16T16:00:00)
wasStartedBy(ex:act2, -, -, 2011-11-16T16:00:00)
wasStartedBy(ex:act2, [ex:param="a"])
wasStartedBy(ex:start; ex:act2, ex:e, ex:act1, 2011-11-16T16:00:00)


wasEndedBy(ex:end; ex:act2, ex:trigger, ex:act1, 2011-11-16T16:00:00, [ex:param="a"])
wasEndedBy(ex:act2, -, ex:act1, -)
wasEndedBy(ex:act2, -, ex:act1, 2011-11-16T16:00:00)
wasEndedBy(ex:act2, -, -, 2011-11-16T16:00:00)
wasEndedBy(ex:act2, [ex:param="a"])
wasEndedBy(ex:end; ex:act2, ex:e, ex:act1, 2011-11-16T16:00:00)

specializationOf(ex:e1,ex:e2)

provext:specializationOf(ex:spec;ex:e1,ex:e2)
provext:specializationOf(ex:spec1;ex:e1,ex:e2)
provext:specializationOf(ex:spec2;ex:e1,ex:e2, [ex:param="a"])
provext:alternateOf(ex:alt;ex:e1,ex:e2)
provext:alternateOf(ex:alt1;ex:e1,ex:e2)
provext:alternateOf(ex:alt2;ex:e1,ex:e2, [ex:param="a"])


bundle bar:hi
  prefix bar <http://bar/>
  entity(bar:hi)
endBundle


bundle foobar:hi2
  prefix foobar <http://bar/>
  entity(foobar:hi)
endBundle


bundle ex:hi3
  entity(ex:hi)
endBundle

endDocument
""" )   /// problem with prov prefix, again, it's same | operator on pn_prefix


          doCheckDoc( """
document
prefix ex <http://example.org/> 
prefix e....x <http://example.org/dot/dot/dot/>
entity(ex:aa)
entity(ex:c.c)         // test: a comment
entity(ex:d1)  /* ignore */ entity(ex:dd1)
entity(ex:1e)
entity(ex:b.....b)
entity(e....x:b.....b)
endDocument
""" )   // ex:b.. not valid, add extra test!

          doCheckDoc (inputfile)
          

	}
}


