/*******************************************************************************
 * Copyright (c) 2011-2026 Luc Moreau
 *******************************************************************************/

/*
  Grammar for PROV-N, the Provenance Notation (https://www.w3.org/TR/prov-n/), for ANTLR 4.

  It is meant to replace the ANTLR 3 grammar (src/main/antlr3/.../PROV_N.g). ANTLR 4 no longer
  builds an abstract syntax tree with rewrite rules: the parser returns a parse tree, which
  ProvNTreeTraversal (a visitor) converts into PROV beans, through the ModelConstructor interface,
  as TreeTraversal did for the ANTLR 3 tree. For the documents both accept, the two parsers
  construct the same representation (see ParserComparisonTest). Deliberate differences:

    - the input must end after 'endDocument' (the ANTLR 3 parser ignored what followed);
    - wasRevisionOf, wasQuotedFrom and hadPrimarySource are read as derivations of type
      prov:Revision, prov:Quotation, prov:PrimarySource (the ANTLR 3 parser dropped them);
    - a datatype must be a qualified name, as in PROV-N (an IRI made TreeTraversal fail);
    - a STRING_LITERAL_LONG2 ("""...""") may have a datatype;
    - PN_CHARS_BASE includes the characters [#x10000-#xEFFFF], as in PROV-N.

  Lexical ambiguities.

  PROV-N is not lexically context free: the same characters may be a QUALIFIED_NAME or something
  else, depending on where they occur.

    - 'ex' is a QUALIFIED_NAME (an unprefixed name, in the default namespace) and also a namespace
      prefix, as in 'prefix ex <http://example.org/>';
    - '12' is a QUALIFIED_NAME (PN_LOCAL may start with a digit) and also an integer literal;

  The ANTLR 3 grammar resolved these by letting the parser switch a lexer rule off (the static
  variable PROV_NParser.qnameDisabled, set in parser actions and tested in a lexer predicate),
  which depended on how far the lexer had read ahead, and prevented concurrent parsing.

  This grammar keeps the lexer context free. ANTLR 4 lexers pick the longest match and, for
  matches of equal length, the rule that comes first. So, the more specific tokens (PREFX, DIGITS)
  are defined before QUALIFIED_NAME: they win whenever they match the whole of a name, and the
  parser rule qualifiedName accepts them as qualified names too. A namespace declaration simply
  expects a PREFX; a literal, DIGITS. No state is shared between lexer and parser, so parsers can
  run concurrently.

  Similarly, LANGTAG ('@en') comes before QUALIFIED_NAME, of which it is also an instance,
  and comments come before QUALIFIED_NAME, since PN_LOCAL may contain '/' and '*'.

  Markers.

  PROV-N requires some arguments of statements to be identifiers, e.g. the activity of an association.
  This grammar also accepts the marker '-' for them (rule identifierOrMarker), which is read as an
  absent argument (null): ProvToolbox writes the marker for an argument that a statement lacks, and
  so reads back what it writes, as the ANTLR 3 parser did through error recovery. A validator can then
  report the missing argument.
*/

grammar ProvN;

/* ======================================================================
   Documents, bundles, namespaces
   ====================================================================== */

document
    : 'document'
      namespaceDeclarations?
      expression*
      bundle*
      'endDocument'
      EOF
    ;

bundle
    : 'bundle' identifier
      namespaceDeclarations?
      expression*
      'endBundle'
    ;

namespaceDeclarations
    : (defaultNamespaceDeclaration | namespaceDeclaration) namespaceDeclaration*
    ;

namespaceDeclaration
    : 'prefix' PREFX IRI_REF
    ;

defaultNamespaceDeclaration
    : 'default' IRI_REF
    ;

expression
    : /* Component 1: entities and activities */
      entityExpression
    | activityExpression
    | generationExpression
    | usageExpression
    | startExpression
    | endExpression
    | invalidationExpression
    | communicationExpression
      /* Component 2: agents and responsibility */
    | agentExpression
    | associationExpression
    | attributionExpression
    | responsibilityExpression
      /* Component 3: derivations */
    | derivationExpression
    | influenceExpression
    | hadPrimarySourceExpression
    | quotationExpression
    | revisionExpression
      /* Component 5: alternate entities */
    | alternateExpression
    | specializationExpression
    | mentionExpression
      /* Component 6: collections and dictionaries */
    | insertionExpression
    | removalExpression
    | collectionMembershipExpression
    | dictionaryMembershipExpression
      /* Extensibility */
    | extensibilityExpression
    ;

/* ======================================================================
   Component 1: entities and activities
   ====================================================================== */

entityExpression
    : 'entity' '(' identifierOrMarker optionalAttributeValuePairs ')'
    ;

activityExpression
    : 'activity' '(' identifierOrMarker (',' startTime=timeOrMarker ',' endTime=timeOrMarker)? optionalAttributeValuePairs ')'
    ;

generationExpression
    : 'wasGeneratedBy' '(' optionalIdentifier entity=identifierOrMarker (',' activity=identifierOrMarker ',' timeOrMarker)? optionalAttributeValuePairs ')'
    ;

usageExpression
    : 'used' '(' optionalIdentifier activity=identifierOrMarker (',' entity=identifierOrMarker ',' timeOrMarker)? optionalAttributeValuePairs ')'
    ;

startExpression
    : 'wasStartedBy' '(' optionalIdentifier activity=identifierOrMarker (',' trigger=identifierOrMarker ',' starter=identifierOrMarker ',' timeOrMarker)? optionalAttributeValuePairs ')'
    ;

endExpression
    : 'wasEndedBy' '(' optionalIdentifier activity=identifierOrMarker (',' trigger=identifierOrMarker ',' ender=identifierOrMarker ',' timeOrMarker)? optionalAttributeValuePairs ')'
    ;

invalidationExpression
    : 'wasInvalidatedBy' '(' optionalIdentifier entity=identifierOrMarker (',' activity=identifierOrMarker ',' timeOrMarker)? optionalAttributeValuePairs ')'
    ;

communicationExpression
    : 'wasInformedBy' '(' optionalIdentifier informed=identifierOrMarker ',' informant=identifierOrMarker optionalAttributeValuePairs ')'
    ;

/* ======================================================================
   Component 2: agents and responsibility
   ====================================================================== */

agentExpression
    : 'agent' '(' identifierOrMarker optionalAttributeValuePairs ')'
    ;

attributionExpression
    : 'wasAttributedTo' '(' optionalIdentifier entity=identifierOrMarker ',' agent=identifierOrMarker optionalAttributeValuePairs ')'
    ;

associationExpression
    : 'wasAssociatedWith' '(' optionalIdentifier activity=identifierOrMarker (',' agent=identifierOrMarker ',' plan=identifierOrMarker)? optionalAttributeValuePairs ')'
    ;

responsibilityExpression
    : 'actedOnBehalfOf' '(' optionalIdentifier delegate=identifierOrMarker ',' responsible=identifierOrMarker (',' activity=identifierOrMarker)? optionalAttributeValuePairs ')'
    ;

/* ======================================================================
   Component 3: derivations
   ====================================================================== */

derivationExpression
    : 'wasDerivedFrom' '(' optionalIdentifier derivationArguments ')'
    ;

revisionExpression
    : 'wasRevisionOf' '(' optionalIdentifier derivationArguments ')'
    ;

quotationExpression
    : 'wasQuotedFrom' '(' optionalIdentifier derivationArguments ')'
    ;

hadPrimarySourceExpression
    : 'hadPrimarySource' '(' optionalIdentifier derivationArguments ')'
    ;

derivationArguments
    : generated=identifierOrMarker ',' used=identifierOrMarker (',' activity=identifierOrMarker ',' generation=identifierOrMarker ',' usage=identifierOrMarker)? optionalAttributeValuePairs
    ;

influenceExpression
    : 'wasInfluencedBy' '(' optionalIdentifier influencee=identifierOrMarker ',' influencer=identifierOrMarker optionalAttributeValuePairs ')'
    ;

/* ======================================================================
   Component 5: alternate entities
   ====================================================================== */

alternateExpression
    : 'alternateOf' '(' alternate1=identifierOrMarker ',' alternate2=identifierOrMarker ')'
    ;

specializationExpression
    : 'specializationOf' '(' specific=identifierOrMarker ',' general=identifierOrMarker ')'
    ;

mentionExpression
    : 'mentionOf' '(' specific=identifierOrMarker ',' general=identifierOrMarker ',' bundleId=identifierOrMarker ')'
    ;

/* ======================================================================
   Component 6: collections and dictionaries
   ====================================================================== */

insertionExpression
    : 'prov:derivedByInsertionFrom(' optionalIdentifier after=identifierOrMarker ',' before=identifierOrMarker ',' keyEntitySet optionalAttributeValuePairs ')'
    ;

removalExpression
    : 'prov:derivedByRemovalFrom(' optionalIdentifier after=identifierOrMarker ',' before=identifierOrMarker ',' '{' (literal (',' literal)*)? '}' optionalAttributeValuePairs ')'
    ;

dictionaryMembershipExpression
    : 'prov:hadDictionaryMember(' dictionary=identifierOrMarker ',' entity=identifierOrMarker ',' literal ')'
    ;

collectionMembershipExpression
    : 'hadMember' '(' collection=identifierOrMarker ',' entity=identifierOrMarker ')'
    ;

keyEntitySet
    : '{' keyEntityPair (',' keyEntityPair)* '}'
    ;

keyEntityPair
    : '(' literal ',' identifierOrMarker ')'
    ;

/* ======================================================================
   Extensibility, e.g. provext:hadMember(id; c, e, [attrs])
   ====================================================================== */

extensibilityExpression
    : qualifiedName '(' optionalIdentifier extensibilityArgument (',' extensibilityArgument)* optionalAttributeValuePairs ')'
    ;

extensibilityArgument
    : identifierOrMarker
    | literal
    | time
    | extensibilityExpression
    | extensibilityRecord
    ;

extensibilityRecord
    : '{' extensibilityArgument (',' extensibilityArgument)* '}'
    | '(' extensibilityArgument (',' extensibilityArgument)* ')'
    ;

/* ======================================================================
   Identifiers, attributes, literals, time
   ====================================================================== */

/* An optional identifier, followed by ';'. '-;' explicitly states there is no identifier. */
optionalIdentifier
    : (identifierOrMarker ';')?
    ;

identifierOrMarker
    : identifier
    | '-'
    ;

identifier
    : qualifiedName
    ;

/* PREFX and DIGITS are lexed in preference to QUALIFIED_NAME, when they match a whole name:
   these are qualified names nonetheless. */
qualifiedName
    : QUALIFIED_NAME
    | PREFX
    | DIGITS
    ;

optionalAttributeValuePairs
    : (',' '[' attributeValuePairs? ']')?
    ;

attributeValuePairs
    : attributeValuePair (',' attributeValuePair)*
    ;

attributeValuePair
    : qualifiedName '=' literal
    ;

timeOrMarker
    : time
    | '-'
    ;

time
    : DATETIME
    ;

literal
    : (STRING_LITERAL | STRING_LITERAL_LONG2) LANGTAG? ('%%' datatype)?  # stringLiteral
    | (DIGITS | NEGATIVE_INT)                                             # intLiteral
    | QUALIFIED_NAME_LITERAL                                              # qualifiedNameLiteral
    ;

datatype
    : qualifiedName
    ;


/* ======================================================================
   Lexer. Keywords, which ANTLR defines implicitly from the parser rules, come first; then, the
   order of the rules below matters for inputs that two of them match with the same length.
   ====================================================================== */

/* Single and multiline comments, regarded as whitespace. */
COMMENT
    : '/*' .*? '*/' -> channel(HIDDEN)
    ;

LINE_COMMENT
    : '//' ~[\r\n]* -> channel(HIDDEN)
    ;

WS
    : [ \t\r\n]+ -> channel(HIDDEN)
    ;

STRING_LITERAL_LONG2
    : '"""' ( ( '"' | '""' )? ( ~["\\] | ECHAR ) )* '"""'
    ;

STRING_LITERAL
    : '"' ( ~["\\\r\n] | ECHAR )* '"'
    ;

/* A qualified name literal, such as 'ex:foo'. */
QUALIFIED_NAME_LITERAL
    : '\'' QNAME '\''
    ;

IRI_REF
    : '<' ~[<>"{}|^\\`\u0000- ]* '>'
    ;

/* Needs to implement fully http://www.w3.org/TR/xmlschema11-2/#nt-dateTimeRep */
DATETIME
    : DIGIT DIGIT DIGIT DIGIT '-' DIGIT DIGIT '-' DIGIT DIGIT 'T' DIGIT DIGIT ':' DIGIT DIGIT ':' DIGIT DIGIT ('.' DIGIT+)? ('Z' | TIMEZONE_OFFSET)?
    ;

LANGTAG
    : '@' [a-zA-Z]+ ('-' [a-zA-Z0-9]+)*
    ;

/* An unsigned integer: an INT_LITERAL in a literal, a QUALIFIED_NAME elsewhere (see qualifiedName). */
DIGITS
    : DIGIT+
    ;

NEGATIVE_INT
    : '-' DIGIT+
    ;

/* A namespace prefix: also a QUALIFIED_NAME (see qualifiedName). */
PREFX
    : PN_PREFIX
    ;

QUALIFIED_NAME
    : QNAME
    ;

MINUS : '-' ;

/* ----------------------------------------------------------------------
   Fragments, based on the SPARQL and Turtle grammars:
     https://www.w3.org/TR/rdf-sparql-query/#rPN_CHARS_BASE
     https://www.w3.org/TR/prov-n/#prod-QUALIFIED_NAME
   ---------------------------------------------------------------------- */

fragment QNAME
    : (PN_PREFIX ':')? PN_LOCAL
    | PN_PREFIX ':'
    ;

fragment ECHAR
    : '\\' [tbnrf\\"']
    ;

/* Same as NCNAMESTARTCHAR (XML Schema), except for '_' and ':'. */
fragment PN_CHARS_BASE
    : [A-Z]
    | [a-z]
    | [À-Ö]
    | [Ø-ö]
    | [ø-˿]
    | [Ͱ-ͽ]
    | [Ϳ-῿]
    | [‌-‍]
    | [⁰-↏]
    | [Ⰰ-⿯]
    | [、-퟿]
    | [豈-﷏]
    | [ﷰ-�]
    | [\u{10000}-\u{EFFFF}]
    ;

fragment PN_CHARS_U
    : PN_CHARS_BASE
    | '_'
    ;

/* Same as NCNAMECHAR (XML Schema), except for '.'. */
fragment PN_CHARS
    : PN_CHARS_U
    | '-'
    | DIGIT
    | '·'
    | [̀-ͯ]
    | [‿-⁀]
    ;

fragment PN_PREFIX
    : PN_CHARS_BASE ((PN_CHARS | '.')* PN_CHARS)?
    ;

/* As in SPARQL, PN_LOCAL may start with a digit, but also with, or contain, the characters of
   PN_CHARS_OTHERS. A '.' may occur inside a local name, but not at its end. */
fragment PN_LOCAL
    : (PN_CHARS_U | DIGIT | PN_CHARS_OTHERS) ((PN_CHARS | '.' | PN_CHARS_OTHERS)* (PN_CHARS | PN_CHARS_OTHERS))?
    ;

fragment PN_CHARS_OTHERS
    : '/' | '@' | '~' | '&' | '+' | '*' | '?' | '#' | '$' | '!'
    | PERCENT
    | PN_CHARS_ESC
    ;

fragment PN_CHARS_ESC
    : '\\' [='(),\-:;[\].]
    ;

fragment PERCENT
    : '%' HEX HEX
    ;

fragment HEX
    : [0-9A-Fa-f]
    ;

fragment DIGIT
    : [0-9]
    ;

fragment TIMEZONE_OFFSET
    : ('+' | '-') DIGIT DIGIT ':' DIGIT DIGIT
    ;
