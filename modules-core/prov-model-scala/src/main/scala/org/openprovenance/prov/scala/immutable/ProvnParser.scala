package org.openprovenance.prov.scala.immutable

import org.parboiled2._

import scala.util.{Failure, Success}
import org.openprovenance.prov.model.Namespace
import ProvFactory.pf
import javax.xml.datatype.XMLGregorianCalendar

import scala.annotation.tailrec
import org.openprovenance.prov.scala.streaming.DocBuilder
import org.openprovenance.prov.scala.streaming.Tee
import org.openprovenance.prov.scala.streaming.SimpleStreamStats
import shapeless.HNil

trait ProvnCore extends Parser {

  val pn_chars_base = CharPredicate.Alpha  ++ CharPredicate('\u00C0' to '\u00D6') ++
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




  val pn_chars_u = pn_chars_base ++ '_'

  val pn_chars = pn_chars_u ++ '-' ++ CharPredicate.Digit ++
    '\u00B7' ++ CharPredicate('\u0300' to '\u036F') ++ CharPredicate('\u203F' to '\u2040')

  val marker = CharPredicate('-')

  val qualified_name_escape_chars=CharPredicate("='(),-:;[].")

  val qualified_name_chars_others=CharPredicate("/@~&+*?#$!")

  val pn_char_white = CharPredicate(" \t \n\r")

  val EOL = CharPredicate("\n\r")

  val SIGN = CharPredicate("+-")

  def pn_prefix = rule{ pn_chars_base ~
    zeroOrMore ( pn_chars   |
      ( oneOrMore('.')  ~  pn_chars ) ) }

  def percent = rule {  '%'  ~ CharPredicate.HexDigit ~ CharPredicate.HexDigit }


  def pn_chars_esc = rule {  '\\' ~ qualified_name_escape_chars }


  def pn_chars_others = rule { percent | qualified_name_chars_others | pn_chars_esc }


  // Note, this had to be reorganised to  be compatible with the committed choice | or of ERG grammars
  def pn_local = rule { ( pn_chars_u | CharPredicate.Digit | pn_chars_others ) ~
    zeroOrMore ( ( pn_chars |  pn_chars_others )  |
      ( oneOrMore('.')  ~  ( pn_chars | pn_chars_others) ) )   }

  def qualified_name = rule { capture ((optional( pn_prefix ~ ':' ) ~  pn_local )  | (pn_prefix ~ ':' ))  ~> makeQualifiedName }

  def qualified_nameAsString = rule { capture ((optional( pn_prefix ~ ':' ) ~  pn_local )  | (pn_prefix ~ ':' ))  ~> makeText }

  def WS = rule { zeroOrMore( pn_char_white  | comment) }

  def comment = rule { comment1 | comment2 }

  def comment1 = rule { "//" ~ zeroOrMore(CharPredicate.Printable) ~ oneOrMore(EOL) }

  def comment2 = rule { "/*" ~ endcomment2 }

  def endcomment2: Rule0 = rule { "*/" | zeroOrMore (CharPredicate.Printable) ~ zeroOrMore( pn_char_white ) ~ endcomment2 }  //TODO: will result in stack overflow

  /* parser action */
  def makeText: (String) => String
  def makeQualifiedName: (String) => QualifiedName


}

trait ProvnNamespaces extends Parser with ProvnCore {


  def namespaceDeclaration: Rule[HNil, HNil] =  rule { "prefix" ~ WS ~ capture(pn_prefix) ~ WS ~ namespace ~> registerNamespace }

  def defaultNamespaceDeclaration =  rule { "default" ~ WS ~ namespace  ~> registerDefaultNamespace }

  def namespace =  rule { '<' ~ capture( oneOrMore(noneOf("<>")) )  ~ '>' ~ WS }   // TO REFINE, see IRI_REF def

  def namespaceDeclarations = rule  {	( defaultNamespaceDeclaration | namespaceDeclaration  ) ~ WS ~ zeroOrMore (namespaceDeclaration) }

  def theNamespace: () => Namespace

  def registerNamespace = (pre:String,ns:String) => theNamespace().register(pre,ns)

  def registerDefaultNamespace = (ns:String) => theNamespace().registerDefault(ns)
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

    def optionalAttributeValuePairs: Rule1[Set[Attribute]] = rule { optional ( ',' ~ WS ~ '[' ~ WS ~ ( attributeValuePairs ~ WS ~ ']' ~ WS  |
                                                                                                                                  ']' ~ WS ~> makeEmptyAttributeSet )  )  ~> makeAttributeSetFromOption }

    def attributeValuePairs: Rule1[Set[Attribute]] = rule {	 attributeValuePair ~ zeroOrMore( ',' ~ WS ~ attributeValuePair ) ~> makeAttributeSet }

    def	attributeValuePair: Rule1[Attribute] = rule {  attribute ~ WS ~ '=' ~ WS ~ literal ~ WS   }

    def attribute: Rule1[QualifiedName] = qualified_name
  
    def literal = rule { typedLiteral  ~> makeAttribute | convenienceNotation }

    def typedLiteral: Rule2[String,QualifiedName] = rule { string_literal ~ WS ~ "%%" ~ WS ~ datatype }

    def datatype = rule { qualified_name }

    def convenienceNotation = rule { (string_literal ~ optional (langtag) ~> makeStringAttribute ) | int_literal  ~> makeIntAttribute | qualified_name_literal  ~> makeQualifiedNameAttribute }

    def qualified_name_literal = rule { '\'' ~ qualified_name ~ '\'' ~ WS }

    def int_literal = rule { capture ( optional ( marker ) ~ oneOrMore ( CharPredicate.Digit ) ) ~> makeText ~ WS }

    def string_literal: Rule1[String] = rule { string_literal_long2 | string_literal2 }

    def string_literal2 = rule { '"' ~ capture (zeroOrMore(noneOf("\"\\\n") | echar )) ~> makeText ~ '"' ~ WS } // TODO: get proper grammar
 
    def string_literal_long2 =  rule { "\"\"\"" ~ capture (zeroOrMore(optional(ch('"') | "\"\"") ~ ( noneOf("\"\\") | echar ))) ~> makeText ~ "\"\"\"" ~ WS }

    val string_escape_chars = CharPredicate("tbnrf\\\"\'")
    def echar = rule { '\\' ~ string_escape_chars }

    //http://www.w3.org/TR/rdf-sparql-query/#rLANGTAG
    //def langtag = rule { '@' ~ capture (oneOrMore( CharPredicate.Alpha ) ~ zeroOrMore ( '-' ~ oneOrMore ( CharPredicate.Alpha ++ CharPredicate.Digit ) ))  ~> makeText ~ WS }
    
    def langtag = rule { '@' ~ capture (oneOrMore( CharPredicate.Alpha ) ~ zeroOrMore ( '-' ~ oneOrMore ( CharPredicate.Alpha ++ CharPredicate.Digit ) ))  ~> makeText ~ WS }


    // TODO: http://www.w3.org/TR/xmlschema11-2/#nt-dateTimeRep
    def datetime = rule { capture (CharPredicate.Digit ~ CharPredicate.Digit ~ CharPredicate.Digit ~ CharPredicate.Digit ~ '-' ~ CharPredicate.Digit ~ CharPredicate.Digit ~ '-' ~ CharPredicate.Digit ~ CharPredicate.Digit ~ 'T' ~ CharPredicate.Digit ~ CharPredicate.Digit ~ ':' ~ CharPredicate.Digit ~ CharPredicate.Digit ~ ':' ~ CharPredicate.Digit ~ CharPredicate.Digit ~ optional ('.' ~ CharPredicate.Digit  ~ zeroOrMore(CharPredicate.Digit)) ~  optional('Z' | TimeZoneOffset) ) ~ WS ~> makeTime }

    def optional_datetime:Rule1[Option[XMLGregorianCalendar]] = rule { datetime ~> makeOptionalTime | '-' ~ WS ~> makeNoTime }

    def TimeZoneOffset = rule { SIGN ~  CharPredicate.Digit ~ CharPredicate.Digit ~ ':' ~ CharPredicate.Digit ~ CharPredicate.Digit }

  //   def dateTimeLexicalRep = rule { yearFrag ~ '-' ~ monthFrag ~ '-' ~ dayFrag ~ 'T' ~ ( (hourFrag ~ ':' ~ minuteFrag ~ ':' ~ secondFrag) |  endOfDayFrag) ~ optional (timezoneFrag) }


    

    def document = rule { WS ~ "document" ~ WS ~ optional(namespaceDeclarations) ~> postStartDocument ~  zeroOrMore (statement ~> postStatement | bundle ) ~ WS ~ "endDocument"  ~> postEndDocument ~ WS  }



    def bundle = rule { WS ~ "bundle" ~> openBundle ~ WS ~ qualified_nameAsString ~ WS  ~ optional(namespaceDeclarations) ~> postStartBundle ~ zeroOrMore (statement ~> postStatement) ~ WS ~ "endBundle" ~> postEndBundle ~ WS }

    

  /* parser action */
    def makeEntity: (QualifiedName,Set[Attribute]) => Entity
    def makeAgent: (QualifiedName,Set[Attribute]) => Agent
    def makeActivity: (QualifiedName,Option[XMLGregorianCalendar], Option[XMLGregorianCalendar], Set[Attribute]) => Activity
    def makeWasGeneratedByWithId: (QualifiedName,QualifiedName,Option[QualifiedName],Option[XMLGregorianCalendar], Set[Attribute]) => WasGeneratedBy
    def makeWasGeneratedByWithId2: (QualifiedName,QualifiedName, Set[Attribute]) => WasGeneratedBy
    def makeWasGeneratedByNoId: (QualifiedName,Option[QualifiedName],Option[XMLGregorianCalendar], Set[Attribute]) => WasGeneratedBy
    def makeWasGeneratedByNoId2: (QualifiedName, Set[Attribute]) => WasGeneratedBy
    def makeWasInvalidatedByWithId: (QualifiedName,QualifiedName,Option[QualifiedName],Option[XMLGregorianCalendar], Set[Attribute]) => WasInvalidatedBy
    def makeWasInvalidatedByWithId2: (QualifiedName,QualifiedName, Set[Attribute]) => WasInvalidatedBy
    def makeWasInvalidatedByNoId: (QualifiedName,Option[QualifiedName],Option[XMLGregorianCalendar], Set[Attribute]) => WasInvalidatedBy
    def makeWasInvalidatedByNoId2: (QualifiedName, Set[Attribute]) => WasInvalidatedBy
    def makeWasAssociatedWithWithId: (QualifiedName,QualifiedName,Option[QualifiedName],Option[QualifiedName], Set[Attribute]) => WasAssociatedWith
    def makeWasAssociatedWithWithId2: (QualifiedName,QualifiedName, Set[Attribute]) => WasAssociatedWith
    def makeWasAssociatedWithNoId: (QualifiedName,Option[QualifiedName],Option[QualifiedName], Set[Attribute]) => WasAssociatedWith
    def makeWasAssociatedWithNoId2: (QualifiedName, Set[Attribute]) => WasAssociatedWith
    def makeActedOnBehalfOfWithId: (QualifiedName,QualifiedName,QualifiedName,Option[QualifiedName], Set[Attribute]) => ActedOnBehalfOf
    def makeActedOnBehalfOfWithId2: (QualifiedName,QualifiedName,QualifiedName, Set[Attribute]) => ActedOnBehalfOf
    def makeActedOnBehalfOfNoId: (QualifiedName,QualifiedName,Option[QualifiedName], Set[Attribute]) => ActedOnBehalfOf
    def makeActedOnBehalfOfNoId2: (QualifiedName, QualifiedName, Set[Attribute]) => ActedOnBehalfOf
    def makeWasDerivedFromWithId: (QualifiedName,QualifiedName,QualifiedName,Option[QualifiedName],Option[QualifiedName],Option[QualifiedName], Set[Attribute]) => WasDerivedFrom
    def makeWasDerivedFromWithId2: (QualifiedName,QualifiedName,QualifiedName, Set[Attribute]) => WasDerivedFrom
    def makeWasDerivedFromNoId: (QualifiedName,QualifiedName,Option[QualifiedName],Option[QualifiedName],Option[QualifiedName], Set[Attribute]) => WasDerivedFrom
    def makeWasDerivedFromNoId2: (QualifiedName, QualifiedName, Set[Attribute]) => WasDerivedFrom
    def makeWasAttributedToWithId: (QualifiedName,QualifiedName,QualifiedName, Set[Attribute]) => WasAttributedTo
    def makeWasAttributedToNoId: (QualifiedName,QualifiedName, Set[Attribute]) => WasAttributedTo
    def makeWasInfluencedByWithId: (QualifiedName,QualifiedName,QualifiedName, Set[Attribute]) => WasInfluencedBy
    def makeWasInfluencedByNoId: (QualifiedName,QualifiedName, Set[Attribute]) => WasInfluencedBy
    def makeWasInformedByWithId: (QualifiedName,QualifiedName,QualifiedName, Set[Attribute]) => WasInformedBy
    def makeWasInformedByNoId: (QualifiedName,QualifiedName, Set[Attribute]) => WasInformedBy
    def makeUsedWithId: (QualifiedName,QualifiedName,Option[QualifiedName],Option[XMLGregorianCalendar], Set[Attribute]) => Used
    def makeUsedWithId2: (QualifiedName,QualifiedName, Set[Attribute]) => Used
    def makeUsedNoId: (QualifiedName,Option[QualifiedName],Option[XMLGregorianCalendar], Set[Attribute]) => Used
    def makeUsedNoId2: (QualifiedName, Set[Attribute]) => Used
    def makeWasStartedByWithId: (QualifiedName,QualifiedName,Option[QualifiedName],Option[QualifiedName],Option[XMLGregorianCalendar], Set[Attribute]) => WasStartedBy
    def makeWasStartedByWithId2: (QualifiedName,QualifiedName, Set[Attribute]) => WasStartedBy
    def makeWasStartedByNoId: (QualifiedName,Option[QualifiedName],Option[QualifiedName],Option[XMLGregorianCalendar], Set[Attribute]) => WasStartedBy
    def makeWasStartedByNoId2: (QualifiedName, Set[Attribute]) => WasStartedBy
    def makeWasEndedByWithId: (QualifiedName,QualifiedName,Option[QualifiedName],Option[QualifiedName],Option[XMLGregorianCalendar], Set[Attribute]) => WasEndedBy
    def makeWasEndedByWithId2: (QualifiedName,QualifiedName, Set[Attribute]) => WasEndedBy
    def makeWasEndedByNoId: (QualifiedName,Option[QualifiedName],Option[QualifiedName],Option[XMLGregorianCalendar], Set[Attribute]) => WasEndedBy
    def makeWasEndedByNoId2: (QualifiedName, Set[Attribute]) => WasEndedBy
    def makeSpecializationOf: (QualifiedName,QualifiedName) => SpecializationOf
    def makeQualifiedSpecializationOfWithId: (QualifiedName,QualifiedName,QualifiedName, Set[Attribute]) => SpecializationOf
    def makeQualifiedSpecializationOfNoId: (QualifiedName,QualifiedName, Set[Attribute]) => SpecializationOf
    def makeAlternateOf: (QualifiedName,QualifiedName) => AlternateOf
    def makeMentionOf: (QualifiedName,QualifiedName,QualifiedName) => MentionOf
    def makeQualifiedAlternateOfWithId: (QualifiedName,QualifiedName,QualifiedName, Set[Attribute]) => AlternateOf
    def makeQualifiedAlternateOfNoId: (QualifiedName,QualifiedName, Set[Attribute]) => AlternateOf
    def makeHadMember: (QualifiedName,QualifiedName) => HadMember
    def makeQualifiedHadMemberOfWithId: (QualifiedName,QualifiedName,QualifiedName, Set[Attribute]) => HadMember
    def makeQualifiedHadMemberOfNoId: (QualifiedName,QualifiedName, Set[Attribute]) => HadMember
    def makeActivityNoTime: (QualifiedName, Set[Attribute]) => Activity
    def makeAttribute: (QualifiedName, String, QualifiedName) => Attribute
    def makeStringAttribute: (QualifiedName, String, Option[String]) => Attribute
    def makeIntAttribute: (QualifiedName, String) => Attribute
    def makeQualifiedNameAttribute: (QualifiedName, QualifiedName) => Attribute

    def makeBundle = (name: String, r: Seq[Statement]) => {
      val ns=theNamespace()
      val qn=makeQualifiedName(name)
      closeBundle()
      new Bundle(qn, r.toSet,ns)
    }
    def openBundle: () => Unit
    def closeBundle: () => Unit

    def makeEmptyAttributeSet = () => Set() :Set[Attribute]
    def makeAttributeSet = (a: Attribute, r: Seq[Attribute]) => (r.toSet + a) :Set[Attribute]
    def makeAttributeSetFromOption = (s: Option[Set[Attribute]]) =>  (s match { case Some(s) => s; case _ => Set() } ) :Set[Attribute]
    def makeTime: (String) => XMLGregorianCalendar
    def makeOptionalTime: (XMLGregorianCalendar) => Option[XMLGregorianCalendar]
    def makeNoTime= () => None: Option[XMLGregorianCalendar]
    def makeOptionalIdentifier= (q: QualifiedName) => Some(q): Option[QualifiedName]
    def makeNoIdentifier= () => None: Option[QualifiedName]

 /* Streaming support */  
    def postStatement: Statement => Unit
    def postStartDocument: () => Unit
    def postStartBundle: String => Unit
    def postEndBundle: () => Unit
    def postEndDocument: () => Unit
}

trait ProvStream {
  def postStatement: Statement => Unit
  def postStartDocument: Namespace => Unit
  def postStartBundle: (QualifiedName, Namespace) => Unit
  def postEndBundle: () => Unit
  def postEndDocument: () => Unit
}


class MyParser2(override val input: ParserInput) extends MyParser(input,new Namespace,None, new DocBuilder)  {
  docns.addKnownNamespaces()
  docns.register("provext", "http://openprovenance.org/prov/extension#");


}

class MyParser(val input: ParserInput, val docns: Namespace, var bun_ns: Option[Namespace]=None, val next: ProvStream=new DocBuilder) extends ProvnParser {
  
    def getNext()= next
    
    def makeText = (text: String) => text
            
    def makeEntity = (n: QualifiedName, attr: Set[Attribute]) => pf.newEntity(n,attr)  

    def makeAgent = (n: QualifiedName, attr: Set[Attribute]) => pf.newAgent(n,attr)

    def makeActivity =(n: QualifiedName, start: Option[XMLGregorianCalendar], end: Option[XMLGregorianCalendar], attr: Set[Attribute]) => Activity {
      pf.newActivity(n, start, end, attr)
    }

    def makeActivityNoTime =(n: QualifiedName, attr: Set[Attribute]) => Activity {
      pf.newActivity(n, None, None, attr)
    }

    def nullable [T >: Null](x:Option[T]):T = x match { case Some(s) => s; case None => null:T }

    def makeWasGeneratedByWithId = (id: QualifiedName, e: QualifiedName, a: Option[QualifiedName], t: Option[XMLGregorianCalendar], attr: Set[Attribute]) =>  pf.newWasGeneratedBy(id,e,nullable(a),t,attr)

    def makeWasGeneratedByWithId2 = (id: QualifiedName, e: QualifiedName, attr: Set[Attribute]) =>  makeWasGeneratedByWithId(id,e,None,None,attr)

    def makeWasGeneratedByNoId = (e: QualifiedName, a: Option[QualifiedName], t: Option[XMLGregorianCalendar], attr: Set[Attribute]) =>  pf.newWasGeneratedBy(null,e,nullable(a),t,attr)

    def makeWasGeneratedByNoId2 = (e: QualifiedName, attr: Set[Attribute]) =>   makeWasGeneratedByNoId(e,None,None,attr) 

    def makeWasInvalidatedByWithId = (id: QualifiedName, e: QualifiedName, a: Option[QualifiedName], t: Option[XMLGregorianCalendar], attr: Set[Attribute]) =>  pf.newWasInvalidatedBy(id,e,nullable(a),t,attr)

    def makeWasInvalidatedByWithId2 = (id: QualifiedName, e: QualifiedName, attr: Set[Attribute]) =>  makeWasInvalidatedByWithId(id,e,None,None,attr)

    def makeWasInvalidatedByNoId = (e: QualifiedName, a: Option[QualifiedName], t: Option[XMLGregorianCalendar], attr: Set[Attribute]) =>  pf.newWasInvalidatedBy(null,e,nullable(a),t,attr)

    def makeWasInvalidatedByNoId2 = (e: QualifiedName, attr: Set[Attribute]) =>   makeWasInvalidatedByNoId(e,None,None,attr) 

    def makeWasAssociatedWithWithId = (id: QualifiedName, a: QualifiedName, ag: Option[QualifiedName], pl: Option[QualifiedName], attr: Set[Attribute]) =>  pf.newWasAssociatedWith(id,a,nullable(ag),nullable(pl),attr)

    def makeWasAssociatedWithWithId2 = (id: QualifiedName, e: QualifiedName, attr: Set[Attribute]) =>  makeWasAssociatedWithWithId(id,e,None,None,attr)

    def makeWasAssociatedWithNoId = (a: QualifiedName, ag: Option[QualifiedName], pl: Option[QualifiedName], attr: Set[Attribute]) =>  pf.newWasAssociatedWith(null,a,nullable(ag),nullable(pl),attr)

    def makeWasAssociatedWithNoId2 = (e: QualifiedName, attr: Set[Attribute]) =>   makeWasAssociatedWithNoId(e,None,None,attr) 

    def makeActedOnBehalfOfWithId = (id: QualifiedName, ag2: QualifiedName, ag1: QualifiedName, a: Option[QualifiedName], attr: Set[Attribute]) =>  pf.newActedOnBehalfOf(id,ag2,ag1,nullable(a),attr)

    def makeActedOnBehalfOfWithId2 = (id: QualifiedName, ag2: QualifiedName, ag1: QualifiedName, attr: Set[Attribute]) =>  makeActedOnBehalfOfWithId(id,ag2,ag1,None,attr)

    def makeActedOnBehalfOfNoId = (ag2: QualifiedName, ag1: QualifiedName, a: Option[QualifiedName], attr: Set[Attribute]) =>  pf.newActedOnBehalfOf(null,ag2,ag1,nullable(a),attr)

    def makeActedOnBehalfOfNoId2 = (ag2: QualifiedName, ag1: QualifiedName, attr: Set[Attribute]) =>   makeActedOnBehalfOfNoId(ag2,ag1,None,attr) 
  
    def makeWasDerivedFromWithId = (id: QualifiedName, e2: QualifiedName, e1: QualifiedName, a: Option[QualifiedName], gen: Option[QualifiedName], use: Option[QualifiedName], attr: Set[Attribute]) =>  pf.newWasDerivedFrom(id,e2,e1,nullable(a),nullable(gen),nullable(use),attr)

    def makeWasDerivedFromWithId2 = (id: QualifiedName, e2: QualifiedName, e1: QualifiedName, attr: Set[Attribute]) =>  makeWasDerivedFromWithId(id,e2,e1,None,None,None,attr)

    def makeWasDerivedFromNoId = (e2: QualifiedName, e1: QualifiedName, a: Option[QualifiedName], gen: Option[QualifiedName], use: Option[QualifiedName], attr: Set[Attribute]) =>  pf.newWasDerivedFrom(null,e2,e1,nullable(a),nullable(gen),nullable(use),attr)

    def makeWasDerivedFromNoId2 = (e2: QualifiedName, e1: QualifiedName, attr: Set[Attribute]) =>   makeWasDerivedFromNoId(e2,e1,None,None,None,attr) 
  
    def makeWasAttributedToWithId= (id: QualifiedName,e: QualifiedName,ag: QualifiedName, attr: Set[Attribute]) => pf.newWasAttributedTo(id,e,ag,attr)

    def makeWasAttributedToNoId= (e: QualifiedName,ag: QualifiedName, attr: Set[Attribute]) => pf.newWasAttributedTo(null,e,ag,attr)

    def makeWasInfluencedByWithId= (id: QualifiedName,r2: QualifiedName,r1: QualifiedName, attr: Set[Attribute]) => pf.newWasInfluencedBy(id,r2,r1,attr)

    def makeWasInfluencedByNoId= (r2: QualifiedName,r1: QualifiedName, attr: Set[Attribute]) => pf.newWasInfluencedBy(null,r2,r1,attr)

    def makeWasInformedByWithId= (id: QualifiedName,r2: QualifiedName,r1: QualifiedName, attr: Set[Attribute]) => pf.newWasInformedBy(id,r2,r1,attr)

    def makeWasInformedByNoId= (r2: QualifiedName,r1: QualifiedName, attr: Set[Attribute]) => pf.newWasInformedBy(null,r2,r1,attr)

    def makeUsedWithId = (id: QualifiedName, e: QualifiedName, a: Option[QualifiedName], t: Option[XMLGregorianCalendar], attr: Set[Attribute]) =>  pf.newUsed(id,e,nullable(a),t,attr)

    def makeUsedWithId2 = (id: QualifiedName, e: QualifiedName, attr: Set[Attribute]) =>  makeUsedWithId(id,e,None,None,attr)

    def makeUsedNoId = (e: QualifiedName, a: Option[QualifiedName], t: Option[XMLGregorianCalendar], attr: Set[Attribute]) =>  pf.newUsed(null,e,nullable(a),t,attr)

    def makeUsedNoId2 = (e: QualifiedName, attr: Set[Attribute]) =>   makeUsedNoId(e,None,None,attr) 

    def makeWasStartedByWithId  = (id: QualifiedName, a: QualifiedName, trigger: Option[QualifiedName], starter: Option[QualifiedName], t: Option[XMLGregorianCalendar], attr: Set[Attribute]) =>  pf.newWasStartedBy(id,a,nullable(trigger),nullable(starter),t,attr)

    def makeWasStartedByWithId2 = (id: QualifiedName, a: QualifiedName, attr: Set[Attribute]) =>  makeWasStartedByWithId(id,a,None,None,None,attr)

    def makeWasStartedByNoId = (a: QualifiedName, trigger: Option[QualifiedName], starter: Option[QualifiedName], t: Option[XMLGregorianCalendar], attr: Set[Attribute]) =>  pf.newWasStartedBy(null,a,nullable(trigger),nullable(starter),t,attr)

    def makeWasStartedByNoId2 = (a: QualifiedName, attr: Set[Attribute]) =>   makeWasStartedByNoId(a,None,None,None,attr) 

    def makeWasEndedByWithId  = (id: QualifiedName, a: QualifiedName, trigger: Option[QualifiedName], starter: Option[QualifiedName], t: Option[XMLGregorianCalendar], attr: Set[Attribute]) =>  pf.newWasEndedBy(id,a,nullable(trigger),nullable(starter),t,attr)

    def makeWasEndedByWithId2 = (id: QualifiedName, a: QualifiedName, attr: Set[Attribute]) =>  makeWasEndedByWithId(id,a,None,None,None,attr)

    def makeWasEndedByNoId = (a: QualifiedName, trigger: Option[QualifiedName], starter: Option[QualifiedName], t: Option[XMLGregorianCalendar], attr: Set[Attribute]) =>  pf.newWasEndedBy(null,a,nullable(trigger),nullable(starter),t,attr)

    def makeWasEndedByNoId2 = (a: QualifiedName, attr: Set[Attribute]) =>   makeWasEndedByNoId(a,None,None,None,attr) 

    def makeSpecializationOf= (e2: QualifiedName, e1:QualifiedName) => pf.newSpecializationOf(e2,e1)
    
    def makeMentionOf= (e2: QualifiedName, e1:QualifiedName, b: QualifiedName) => pf.newMentionOf(e2,e1,b)


    def makeQualifiedSpecializationOfWithId = (id:QualifiedName,e2:QualifiedName,e1:QualifiedName, attr:Set[Attribute]) => pf.newSpecializationOf(id,e2,e1,attr)

    def makeQualifiedSpecializationOfNoId = (e2:QualifiedName,e1:QualifiedName, attr:Set[Attribute]) => pf.newSpecializationOf(null,e2,e1,attr)

    def makeAlternateOf= (e2: QualifiedName, e1: QualifiedName) =>  pf.newAlternateOf(e2,e1)

    def makeQualifiedAlternateOfWithId = (id:QualifiedName,e2:QualifiedName,e1:QualifiedName, attr:Set[Attribute]) => pf.newAlternateOf(id,e2,e1,attr)

    def makeQualifiedAlternateOfNoId = (e2:QualifiedName,e1:QualifiedName, attr:Set[Attribute]) => pf.newAlternateOf(null,e2,e1,attr)

    def makeHadMember= (e2:QualifiedName,e1:QualifiedName) => pf.newHadMember(e2,Set(e1))

    def makeQualifiedHadMemberOfWithId = (id:QualifiedName,e2:QualifiedName,e1:QualifiedName, attr:Set[Attribute]) => pf.newHadMember(id,e2,Set(e1),attr)

    def makeQualifiedHadMemberOfNoId = (e2:QualifiedName,e1:QualifiedName, attr:Set[Attribute]) => pf.newHadMember(null,e2,Set(e1),attr)

    def theNamespace= () => bun_ns match{
      case Some(ns) => ns
      case None => docns
    }

    def makeQualifiedName = (s:String) => QualifiedName {
        //println("creating qn for " ++ s ++ " with ns " ++ ns.toString())
        theNamespace().stringToQualifiedName(s,pf).asInstanceOf[QualifiedName]
    }
    
    def makeAttribute =(attr: QualifiedName, literal: String, datatype: QualifiedName) => Attribute {
      //println("creating attr for " ++ attr.toString ++ " " ++  literal ++ " " ++ datatype.toString)
      pf.newAttribute(attr, literal, datatype)
    }
    
    def makeStringAttribute =(attr: QualifiedName, literal: String, lang: Option[String]) => Attribute {
        //println("creating attr (str) for " ++ attr.toString ++ " " ++  literal ++ " " + lang)
        lang match {
          case None    => pf.newAttribute(attr, literal, pf.xsd_string)
          case Some(l) => pf.newAttribute(attr, pf.newInternationalizedString(literal,l), pf.xsd_string)
        }
        
    }   
    def makeIntAttribute =(attr: QualifiedName, literal: String) => Attribute {
      //println("creating attr (int) for " ++ attr.toString ++ " " ++  literal)
        pf.newAttribute(attr, literal, pf.xsd_int)
    }

    def makeQualifiedNameAttribute =(attr: QualifiedName, literal: QualifiedName) => Attribute {
        //println("creating attr (qn) for " ++ attr.toString ++ " " ++  literal.toString)
        pf.newAttribute(attr, literal, pf.prov_qualified_name)
    }

    def makeTime = (s: String) => (pf.newISOTime(s)): XMLGregorianCalendar

    def makeOptionalTime = (t:XMLGregorianCalendar) => (if (t==null) None else Some(t)): Option[XMLGregorianCalendar]

    def openBundle = () => {
      val ns=pf.newNamespace()
      bun_ns=Some(ns)
      ns.addKnownNamespaces();
      ns.register("provext", "http://openprovenance.org/prov/extension#");
      ns.setParent(docns);
    }
    
    def closeBundle = () => { bun_ns=None }

    /* Streaming support */

    def postStartBundle = (name: String) => {
      val ns=theNamespace()
      val qn=makeQualifiedName(name)
      next.postStartBundle(qn,ns)
    }

    def postStatement = (s: Statement) => {
      next.postStatement(s)
    }

    def postStartDocument= () => {
      next.postStartDocument(theNamespace())
    }

    def postEndDocument= () => {
      next.postEndDocument()
    }

    def postEndBundle= () => {
      next.postEndBundle()
    }


}

object Parser {
      def readDocument (s: String): Document = {
        val inputfile : ParserInput = io.Source.fromFile(s: String).mkString
        val docBuilder=new DocBuilder
        val ns=new Namespace
        ns.addKnownNamespaces()
        ns.register("provext", "http://openprovenance.org/prov/extension#");

        
        val p=new MyParser(inputfile,ns,None,docBuilder)
        p.document.run() match {
          case Success(result) => docBuilder.document
          case Failure(e: ParseError) => { println("Expression is not valid: " + p.formatError(e)); throw new RuntimeException() }
          case Failure(e) => { println("Unexpected error during parsing run: " + e.printStackTrace); throw new RuntimeException() }
        }
    }
}


object AParser  {

  def doCheckDoc (s: ParserInput) = {
        val docBuilder=new DocBuilder
        val stream=new Tee(docBuilder,new SimpleStreamStats)
        val ns=new Namespace
        ns.addKnownNamespaces()
        ns.register("provext", "http://openprovenance.org/prov/extension#");

        
        val p=new MyParser(s,ns,None,stream)
        p.document.run() match {
          case Success(result) => println(docBuilder.document)
          case Failure(e: ParseError) => println("Expression is not valid: " + p.formatError(e))
          case Failure(e) => println("Unexpected error during parsing run: " + e.printStackTrace)
        }
    }


    def main(args: Array[String]) {
        lazy val inputfile : ParserInput = io.Source.fromFile(args(0)).mkString

        println( "parsing " + new MyParser2("foo").pn_local.run())
        println( "parsing " + new MyParser2("-foo").pn_local.run())
        println( "parsing " + new MyParser2("foo").pn_prefix.run())
        println( "parsing " + new MyParser2("8foo").pn_prefix.run())
        println( "parsing " + new MyParser2("foo:var").qualified_name.run())
        println( "parsing " + new MyParser2("var").qualified_name.run())
        println( "parsing " + new MyParser2("var:").qualified_name.run())
        println( "parsing " + new MyParser2(":var").qualified_name.run())
        println( "parsing " + new MyParser2(":").qualified_name.run())
        

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


