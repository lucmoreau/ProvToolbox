/*******************************************************************************
 * Copyright (c) 2011-2012 Luc Moreau
 *******************************************************************************/
grammar PROV_N;

options {
  language = Java;
  output=AST;
}

tokens {
    ID; ATTRIBUTE; ATTRIBUTES; IRI; QNAM; STRING; TYPEDLITERAL; INT; 
    EXPRESSIONS; NAMESPACE; DEFAULTNAMESPACE; NAMESPACES; PREFIX; 

    /* Component 1 */
    ENTITY; ACTIVITY; WGB; USED; WSB; WEB; WINVB; WIB; 
    TIME; START; END;

    /* Component 2 */
    WDF; WRO; PRIMARYSOURCE; WQF; INFL; 

    /* Component 3 */
    AGENT; WAT; WAW; AOBO; 

    /* Component 4 */
    BUNDLE; BUNDLES; NAMEDBUNDLE;

    /* Component 5 */
    SPECIALIZATION; ALTERNATE; CTX;

    /* Component 6 */
    DBIF; DBRF; KES; ES; KEYS; VALUES; DMEM; CMEM; TRUE; FALSE; UNKNOWN;

    /* Extensibility */
    EXT;
}

@header {
package org.openprovenance.prov.notation;
}

@lexer::header {
package org.openprovenance.prov.notation;
}

@members{
 public static boolean qnameDisabled=false; }

bundle
	:	 'bundle' 
        (namespaceDeclarations)?
		(expression )*
        (namedBundle (namedBundle)*)?
		'endBundle'
      -> {$namespaceDeclarations.tree==null}? ^(BUNDLE ^(NAMESPACES) ^(EXPRESSIONS expression*) ^(BUNDLES namedBundle*))
      -> ^(BUNDLE namespaceDeclarations? ^(EXPRESSIONS expression*) ^(BUNDLES namedBundle*))
    ;

namedBundle
 	:	'bundle' identifier
        (namespaceDeclarations)?
		(expression)*
		'endBundle'
      -> {$namespaceDeclarations.tree==null}? ^(NAMEDBUNDLE identifier ^(NAMESPACES) ^(EXPRESSIONS expression*))
      -> ^(NAMEDBUNDLE identifier namespaceDeclarations? ^(EXPRESSIONS expression*))
	;


namespaceDeclarations :
        (defaultNamespaceDeclaration | namespaceDeclaration) namespaceDeclaration*
        -> ^(NAMESPACES defaultNamespaceDeclaration? namespaceDeclaration*)
    ;


/**
  The following production uses ANTLR directives to disactivate the
  production QUALIFIED_NAME to guarantee that ensure that PREFX token is
  generated in this context only.

  A global static boolean variable (qnameDisabled) is declared in the
  parser, set to true when entering this production and set to false
  on exit.  This variable is use in the lexer to disable the QUALIFIED_NAME
  production.

  Sorry, that's the only way I have found to do this. Obviously, this
  prevents concurrent execution of the parser/lexer. */

namespaceDeclaration 
@init { qnameDisabled = true; }
@after { qnameDisabled = false; } :
        'prefix' PREFX namespace
        -> ^(NAMESPACE ^(PREFIX PREFX) namespace)
    ;
namespace :
        IRI_REF
    ;

defaultNamespaceDeclaration :
        'default' IRI_REF
        ->  ^(DEFAULTNAMESPACE IRI_REF)
    ;

expression
@init { qnameDisabled = false; } :
        (   /* component 1 */

           entityExpression | activityExpression | generationExpression  | usageExpression
         | startExpression | endExpression | invalidationExpression | communicationExpression

            /* component 2 */
        
        | agentExpression |  associationExpression | attributionExpression | responsibilityExpression

            /* component 3 */

        | derivationExpression | influenceExpression | hadPrimarySourceExpression | quotationExpression | revisionExpression

            /* component 4 */

        | alternateExpression | specializationExpression

            /* component 5 */

        | insertionExpression | removalExpression | membershipExpression 


            /* component 6 */ 
         | mentionExpression

            /* extensibility */
         | extensibilityExpression
        )
	;

/*
        Component 1: Entities/Activities

*/

entityExpression
	:	'entity' '(' identifier optionalAttributeValuePairs ')'
        -> ^(ENTITY identifier optionalAttributeValuePairs)
	;


activityExpression
	:	'activity' '(' identifier (',' (s=time | '-' ) ',' (e=time | '-'))? optionalAttributeValuePairs ')'
        -> ^(ACTIVITY identifier ^(START $s?) ^(END $e?) optionalAttributeValuePairs )
	;

generationExpression
	:	'wasGeneratedBy' '(' id0=optionalIdentifier id2=identifier (',' id1=identifierOrMarker ',' ( tt=time | '-' ))? optionalAttributeValuePairs ')'
      -> {$id1.tree==null && $tt.tree==null}?
         ^(WGB ^(ID $id0?) $id2 ^(ID)  ^(TIME) optionalAttributeValuePairs)
      -> ^(WGB ^(ID $id0?) $id2 $id1  ^(TIME time?) optionalAttributeValuePairs)
	;

/* TODO: ensure that optionalIdentifier always returns an ID??? */

optionalIdentifier
    : ((id0=identifier | '-') ';')?
     -> identifier?
    ;

identifierOrMarker
    :
        (identifier  -> identifier | '-' -> ^(ID))
    ;

usageExpression
	:	'used' '(' id0=optionalIdentifier  id2=identifier ',' id1=identifier (',' ( time | '-' ))? optionalAttributeValuePairs ')'
      -> ^(USED ^(ID $id0?)  $id2 $id1 ^(TIME time?) optionalAttributeValuePairs)
	;

startExpression
	:	'wasStartedBy' '(' id0=optionalIdentifier id2=identifier (',' id1=identifierOrMarker ',' id3=identifierOrMarker ',' ( tt=time | '-' ))? optionalAttributeValuePairs ')'
      -> {$id1.tree==null && $id3.tree==null && $tt.tree==null}?
         ^(WSB ^(ID $id0?) $id2 ^(ID) ^(ID) ^(TIME) optionalAttributeValuePairs)
      -> ^(WSB ^(ID $id0?) $id2 $id1 $id3 ^(TIME time?) optionalAttributeValuePairs)
	;

endExpression
	:	'wasEndedBy' '(' id0=optionalIdentifier id2=identifier (',' id1=identifierOrMarker ',' id3=identifierOrMarker ',' ( tt=time | '-' ))? optionalAttributeValuePairs ')'
      -> {$id1.tree==null && $id3.tree==null && $tt.tree==null}?
        ^(WEB ^(ID $id0?) $id2 ^(ID) ^(ID) ^(TIME) optionalAttributeValuePairs)
      -> ^(WEB ^(ID $id0?) $id2 $id1 $id3 ^(TIME time?) optionalAttributeValuePairs)
	;

/*TODO handle no optional everywhere */

invalidationExpression
	:	'wasInvalidatedBy' '(' id0=optionalIdentifier id2=identifier (',' id1=identifierOrMarker ',' ( time | '-' ))? optionalAttributeValuePairs ')'
      -> ^(WINVB ^(ID $id0?) $id2 $id1  ^(TIME time?) optionalAttributeValuePairs)
	;


communicationExpression
	:	'wasInformedBy' '(' id0=optionalIdentifier id2=identifier ',' id1=identifier optionalAttributeValuePairs ')'
      -> ^(WIB ^(ID $id0?) $id2 $id1 optionalAttributeValuePairs)
	;


/*
        Component 2: Agents and Responsibility

*/

agentExpression
	:	'agent' '(' identifier optionalAttributeValuePairs	')' 
        -> ^(AGENT identifier optionalAttributeValuePairs )
	;

attributionExpression
	:	'wasAttributedTo' '('  id0=optionalIdentifier e=identifier ',' ag=identifier optionalAttributeValuePairs ')'
      -> ^(WAT  ^(ID $id0?) $e $ag optionalAttributeValuePairs)
	;

associationExpression
	:	'wasAssociatedWith' '('  id0=optionalIdentifier a=identifier (',' ag=identifierOrMarker ',' pl=identifierOrMarker)? optionalAttributeValuePairs ')'
      -> {$pl.tree==null}? ^(WAW ^(ID $id0?) $a $ag? ^(ID) optionalAttributeValuePairs)
      -> {$ag.tree==null}? ^(WAW ^(ID $id0?) $a ^(ID) $pl  optionalAttributeValuePairs)
      -> ^(WAW ^(ID $id0?) $a $ag? $pl optionalAttributeValuePairs)
	;

responsibilityExpression
	:	'actedOnBehalfOf' '('   id0=optionalIdentifier ag2=identifier ',' ag1=identifier (','  a=identifierOrMarker)? optionalAttributeValuePairs ')'
      -> {$a.tree==null}? ^(AOBO  ^(ID $id0?) $ag2 $ag1 ^(ID) optionalAttributeValuePairs)
      -> ^(AOBO  ^(ID $id0?) $ag2 $ag1 $a optionalAttributeValuePairs)
	;



/*
        Component 3: Derivations

*/ 


derivationExpression
	:	'wasDerivedFrom' '(' id0=optionalIdentifier id2=identifier ',' id1=identifier (',' a=identifierOrMarker ',' g2=identifierOrMarker ',' u1=identifierOrMarker )?	optionalAttributeValuePairs ')'
      -> {$a.tree==null && $g2.tree==null && $u1.tree==null}?
          ^(WDF ^(ID $id0?) $id2 $id1 ^(ID) ^(ID) ^(ID) optionalAttributeValuePairs)
      -> ^(WDF ^(ID $id0?) $id2 $id1 $a $g2 $u1 optionalAttributeValuePairs)
	;


revisionExpression
	:	'wasRevisionOf' '('  id0=optionalIdentifier id2=identifier ',' id1=identifier (',' a=identifierOrMarker ',' g2=identifierOrMarker ',' u1=identifierOrMarker )?	optionalAttributeValuePairs ')'
      -> {$a.tree==null && $g2.tree==null && $u1.tree==null}?
          ^(WRO ^(ID $id0?) $id2 $id1 ^(ID) ^(ID) ^(ID) optionalAttributeValuePairs)
      -> ^(WRO ^(ID $id0?) $id2 $id1 $a $g2 $u1 optionalAttributeValuePairs)
	;



quotationExpression
	:	'wasQuotedFrom' '('  id0=optionalIdentifier id2=identifier ',' id1=identifier (',' a=identifierOrMarker ',' g2=identifierOrMarker ',' u1=identifierOrMarker )?	optionalAttributeValuePairs ')'
      -> {$a.tree==null && $g2.tree==null && $u1.tree==null}?
          ^(WQF ^(ID $id0?) $id2 $id1 ^(ID) ^(ID) ^(ID) optionalAttributeValuePairs)
      -> ^(WQF ^(ID $id0?) $id2 $id1 $a $g2 $u1 optionalAttributeValuePairs)
	;


hadPrimarySourceExpression
	:	'hadPrimarySource' '('  id0=optionalIdentifier id2=identifier ',' id1=identifier (',' a=identifierOrMarker ',' g2=identifierOrMarker ',' u1=identifierOrMarker )?	optionalAttributeValuePairs ')'
      -> {$a.tree==null && $g2.tree==null && $u1.tree==null}?
          ^(PRIMARYSOURCE ^(ID $id0?) $id2 $id1 ^(ID) ^(ID) ^(ID) optionalAttributeValuePairs)
      -> ^(PRIMARYSOURCE ^(ID $id0?) $id2 $id1 $a $g2 $u1 optionalAttributeValuePairs)
	;



influenceExpression
	:	'wasInfluencedBy' '('  id0=optionalIdentifier id2=identifier ',' id1=identifier optionalAttributeValuePairs ')'
      -> ^(INFL ^(ID $id0?) $id2 $id1 optionalAttributeValuePairs)
	;


/*
        Component 4: 

*/

/*
        Component 5: Alternate entities

*/

alternateExpression
	:	'alternateOf' '('  identifier ',' identifier ')'
      -> ^(ALTERNATE identifier+)
	;

specializationExpression
	:	'specializationOf' '('  identifier ',' identifier ')'
      -> ^(SPECIALIZATION identifier+)
	;

mentionExpression
	:	'mentionOf' '(' su=identifier ',' en=identifier ',' bu=identifier ')' 
        -> ^(CTX $su $bu $en)
	;


/*
        Component 6: Collections

TODO: literal used in these production needs to disable qname, to allow for intliteral

*/

insertionExpression
	:	'derivedByInsertionFrom' '('  id0=optionalIdentifier id2=identifier ',' id1=identifier ',' keyEntitySet optionalAttributeValuePairs ')'
      -> ^(DBIF ^(ID $id0?) $id2 $id1 keyEntitySet  optionalAttributeValuePairs)
	;

removalExpression
	:	'derivedByRemovalFrom' '('  id0=optionalIdentifier id2=identifier ',' id1=identifier ',' '{' literal (',' literal)* '}' optionalAttributeValuePairs ')'
      -> ^(DBRF ^(ID $id0?) $id2 $id1 ^(KEYS literal*)  optionalAttributeValuePairs)
	;

/* TODO: specify complete as optional boolean */
membershipExpression
	:	( 'memberOf' '('  id0=optionalIdentifier  id1=identifier ',' keyEntitySet ',' 'true' optionalAttributeValuePairs ')'
      -> ^(DMEM ^(ID $id0?) $id1 keyEntitySet  ^(TRUE) optionalAttributeValuePairs)
         |          
          'memberOf' '('  id0=optionalIdentifier  id1=identifier ',' keyEntitySet ',' 'false' optionalAttributeValuePairs ')'
      -> ^(DMEM ^(ID $id0?) $id1 keyEntitySet  ^(FALSE) optionalAttributeValuePairs)
         |          
          'memberOf' '('  id0=optionalIdentifier  id1=identifier ',' keyEntitySet optionalAttributeValuePairs ')'
      -> ^(DMEM ^(ID $id0?) $id1 keyEntitySet  ^(UNKNOWN) optionalAttributeValuePairs)
         |
         'memberOf' '('  id0=optionalIdentifier  id1=identifier ',' entitySet ',' 'true' optionalAttributeValuePairs ')'
      -> ^(CMEM ^(ID $id0?) $id1 entitySet  ^(TRUE) optionalAttributeValuePairs)
         |
         'memberOf' '('  id0=optionalIdentifier  id1=identifier ',' entitySet ',' 'false' optionalAttributeValuePairs ')'
      -> ^(CMEM ^(ID $id0?) $id1 entitySet  ^(FALSE) optionalAttributeValuePairs)
         |
         'memberOf' '('  id0=optionalIdentifier  id1=identifier ',' entitySet optionalAttributeValuePairs ')'
      -> ^(CMEM ^(ID $id0?) $id1 entitySet  ^(UNKNOWN) optionalAttributeValuePairs)

        )
	;

keyEntitySet
    : '{'  '(' literal ',' val=identifier  ')' ( ','  '(' literal ',' val=identifier  ')' )* '}'
      -> ^(KES ^(KEYS literal+) ^(VALUES identifier+))
    ;


entitySet
    : '{'  identifier* '}'
      -> ^(ES  identifier?)
    ;



/*
        Component: Extensibility

*/




extensibilityExpression
	:	name=QUALIFIED_NAME '(' id0=optionalIdentifier extensibilityArgument ( ','  extensibilityArgument)* attr=optionalAttributeValuePairs ')'
      -> {$attr.tree==null}?
         ^(EXT $name ^(ID $id0?) extensibilityArgument* ^(ATTRIBUTES))
      -> ^(EXT $name ^(ID $id0?) extensibilityArgument* optionalAttributeValuePairs)
	;

extensibilityArgument
    : ( identifierOrMarker | literal | time  | extensibilityExpression | extensibilityRecord ) 
;

extensibilityRecord:
 '{' extensibilityArgument (',' extensibilityArgument)* '}' |  '(' extensibilityArgument (',' extensibilityArgument)* ')'
;



iriOrMarker
    :
      ( IRI_REF -> ^(IRI IRI_REF)
       |
       '-' -> ^(IRI) )
    ;






optionalAttributeValuePairs
    :
    (',' '[' attributeValuePairs ']')?
        -> ^(ATTRIBUTES attributeValuePairs?)
    ;


identifier
	:
        QUALIFIED_NAME -> ^(ID QUALIFIED_NAME)
	;

attribute
	:
        QUALIFIED_NAME
	;

attributeValuePairs
	:
        (  | attributeValuePair ( ','! attributeValuePair )* )
	;


/*
QUALIFIED_NAME and INT_LITERAL are conflicting.  To avoid the conflict in
literal, QUALIFIED_NAME rule is disabled once an attribute has been parsed, to
give priority to INT_LITERAL.
  */

attributeValuePair
@after { qnameDisabled = false; }

	:
        attribute { qnameDisabled = true; } '='  literal  -> ^(ATTRIBUTE attribute literal)
	;


time
	:
        xsdDateTime
	;


/* TODO: complete grammar of Literal */


literal :
        ( //STRING_LITERAL -> ^(STRING STRING_LITERAL) |
         STRING_LITERAL LANGTAG? -> ^(STRING STRING_LITERAL LANGTAG?) |
         STRING_LITERAL_LONG2 -> ^(STRING STRING_LITERAL_LONG2) |
         INT_LITERAL -> ^(INT INT_LITERAL) |
         STRING_LITERAL LANGTAG? { qnameDisabled = false; } '%%' datatype -> ^(TYPEDLITERAL STRING_LITERAL datatype LANGTAG?) |
         { qnameDisabled = false; } '\'' QUALIFIED_NAME '\'' -> ^(TYPEDLITERAL QUALIFIED_NAME) | )
	;

datatype:
        (IRI_REF -> ^(IRI IRI_REF)
        |
         QUALIFIED_NAME -> ^(QNAM QUALIFIED_NAME))
	;

	

/* The production here are based on the SPARQL grammar availabe from:
   http://antlr.org/grammar/1200929755392/Sparql.g */




INT_LITERAL:
    '-'? DIGIT+
    ;

STRING_LITERAL : '"' (options {greedy=false;} : ~('"' | '\\' | EOL) | ECHAR)* '"';

STRING_LITERAL_LONG2 : '"""' (options {greedy=false;} : ('"' | '""')? (~('"'|'\\') | ECHAR))* '"""';



/* This production uses a "Disambiguating Semantic Predicates"
   checking whether we are in scope of a declaration/literal or not. If so,
   rule QUALIFIED_NAME is disabled, to the benefit of PREFX/INT_LITERAL. */

QUALIFIED_NAME:
  { !PROV_NParser.qnameDisabled }?
    (PN_PREFIX ':')? PN_LOCAL | PN_PREFIX ':'
        
  ;



/* The order of the two rules (QUALIFIED_NAME/PREFX) is crucial. By default, QUALIFIED_NAME should be used
   unless we are in the context of a declaration. */
PREFX:
    PN_PREFIX
  ;



fragment
ECHAR : '\\' ('t' | 'b' | 'n' | 'r' | 'f' | '\\' | '"' | '\'');
 

/* Note PN_CHARS_BASE is same as NCNAMESTARTCHAR (XML Schema)
   http://www.w3.org/TR/rdf-sparql-query/#rPN_CHARS_U
   http://www.w3.org/2010/01/Turtle/#prod-turtle2-PN_CHARS_U
 */

fragment
PN_CHARS_U : PN_CHARS_BASE | '_';

/* Note: this is the same as NCNAMECHAR (XML Schema) except for '.' 
   http://www.w3.org/TR/rdf-sparql-query/#rPN_CHARS 
   http://www.w3.org/2010/01/Turtle/#prod-turtle2-PN_CHARS

*/

fragment
PN_CHARS
    : PN_CHARS_U
    | MINUS
    | DIGIT
    | '\u00B7' 
    | '\u0300'..'\u036F'
    | '\u203F'..'\u2040'
    ;

/* 
http://www.w3.org/TR/rdf-sparql-query/#rPN_PREFIX
http://www.w3.org/2010/01/Turtle/#prod-turtle2-PN_PREFIX
*/

fragment
PN_PREFIX : PN_CHARS_BASE ((PN_CHARS|DOT)* PN_CHARS)?;

/* Note PN_LOCAL allows for start with a digit 

  same as http://www.w3.org/TR/rdf-sparql-query/#rPN_LOCAL, except that here we accept '/', '@'
  same http://www.w3.org/2010/01/Turtle/#prod-turtle2-PN_LOCAL, except that here we accept '/', '@'
*/
fragment
PN_LOCAL:
  (PN_CHARS_U | DIGIT | PN_CHARS_OTHERS)  ((PN_CHARS| PN_CHARS_OTHERS |{    
                    	                       if (input.LA(1)=='.') {
                    	                          int LA2 = input.LA(2);
                    	       	                  if (!((LA2>='-' && LA2<='.')||(LA2>='0' && LA2<='9')||(LA2>='A' && LA2<='Z')||LA2=='_'||(LA2>='a' && LA2<='z')||LA2=='\u00B7'||(LA2>='\u00C0' && LA2<='\u00D6')||(LA2>='\u00D8' && LA2<='\u00F6')||(LA2>='\u00F8' && LA2<='\u037D')||(LA2>='\u037F' && LA2<='\u1FFF')||(LA2>='\u200C' && LA2<='\u200D')||(LA2>='\u203F' && LA2<='\u2040')||(LA2>='\u2070' && LA2<='\u218F')||(LA2>='\u2C00' && LA2<='\u2FEF')||(LA2>='\u3001' && LA2<='\uD7FF')||(LA2>='\uF900' && LA2<='\uFDCF')||(LA2>='\uFDF0' && LA2<='\uFFFD'))) {
                    	       	                     return;
                    	       	                  }
                    	                       }
                                           } DOT)* (PN_CHARS | PN_CHARS_OTHERS ))?
;

fragment PN_CHARS_OTHERS 
    : 
      PERCENT | '/' | '@' | '~' | '&' | '+' | '?' | '#' | '$'
    ;

fragment PERCENT
     :
     '%' HEX HEX
    ;

fragment HEX 
     : DIGIT | 'A' .. 'F' | 'a' .. 'f' 
    ;
/* 
   same as http://www.w3.org/TR/rdf-sparql-query/#rPN_CHARS_BASE  except for [#10000-#EFFFF], which Java doesn't seem to support
   same as http://www.w3.org/2010/01/Turtle/#prod-turtle2-PN_CHARS_BASE except for [#10000-#EFFFF], which Java doesn't seem to support

   Note PN_CHARS_BASE is same as NCNAMESTARTCHAR (XML Schema) except for '_' and ':' 
   http://www.w3.org/TR/REC-xml/#NT-NameStartChar
*/
fragment
PN_CHARS_BASE
    : 'A'..'Z'
    | 'a'..'z'
    | '\u00C0'..'\u00D6'
    | '\u00D8'..'\u00F6'
    | '\u00F8'..'\u02FF'
    | '\u0370'..'\u037D'
    | '\u037F'..'\u1FFF'
    | '\u200C'..'\u200D'
    | '\u2070'..'\u218F'
    | '\u2C00'..'\u2FEF'
    | '\u3001'..'\uD7FF'
    | '\uF900'..'\uFDCF'
    | '\uFDF0'..'\uFFFD'
    ;
    
fragment DIGIT: '0'..'9';

fragment
EOL : '\n' | '\r';
	

LANGTAG : '@' ('A'..'Z'|'a'..'z')+ (MINUS ('A'..'Z'|'a'..'z'|DIGIT)+)*;


DOT : '.';

MINUS : '-';



/* Single and multiline comments. Comments are handle at the lexical
 * layer, and regarded as whitespace. */

COMMENT
     :
    (
    '/*' (options {greedy=false;} : .)* '*/' {$channel=HIDDEN;}
    |
     '//' (options{greedy=false;} : .)* EOL { $channel=HIDDEN; }
    );




WS : (' '| '\t'| EOL)+ { $channel=HIDDEN; };


IRI_REF
  :
  LESS
  ( options {greedy=false;}:
    ~(
      LESS
      | GREATER
      | '"'
      | OPEN_CURLY_BRACE
      | CLOSE_CURLY_BRACE
      | '|'
      | '^'
      | '\\'
      | '`'
      | ('\u0000'..'\u0020')
     )
  )*
  GREATER
  ;


LESS
  :
  '<'
  ;

GREATER
  :
  '>'
  ;
OPEN_CURLY_BRACE
  :
  '{'
  ;

CLOSE_CURLY_BRACE
  :
  '}'
  ;



/* Need to implement fully http://www.w3.org/TR/xmlschema11-2/#nt-dateTimeRep */

xsdDateTime: DateTime;

DateTime: 
(DIGIT DIGIT DIGIT DIGIT '-' DIGIT DIGIT '-' DIGIT DIGIT 'T' DIGIT DIGIT ':' DIGIT DIGIT ':' DIGIT DIGIT ('.' DIGIT DIGIT*)? ('Z' | TimeZoneOffset)?)
    ;




TimeZoneOffset: ('+' | '-') DIGIT DIGIT ':' DIGIT DIGIT;




