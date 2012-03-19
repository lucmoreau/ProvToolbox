/*******************************************************************************
 * Copyright (c) 2011-2012 Luc Moreau
 *******************************************************************************/
grammar PROV_N;

options {
  language = Java;
  output=AST;
}

tokens {
    ATTRIBUTE; ATTRIBUTES; START; END; IRI; QNAM; AGENT; ENTITY; ACTIVITY; WGB; WSB; WEB; USED; WDF; TIME; WDO; WCB; STRING; TYPEDLITERAL; CONTAINER; ID; A; G; U; T; NAMESPACE; DEFAULTNAMESPACE; NAMESPACES; PREFIX; COMPLEMENTARITY; WAW; WAT; INT; PLAN; SPECIALIZATION; ALTERNATE; AOBO; WIB; WSBA;
}

@header {
package org.openprovenance.prov.notation;
}

@lexer::header {
package org.openprovenance.prov.notation;
}


container
	:	'container' 
        (namespaceDeclarations)?
		record*
		'endContainer'

      -> ^(CONTAINER namespaceDeclarations? record*)
	;


namespaceDeclarations :
        (defaultNamespaceDeclaration | namespaceDeclaration) namespaceDeclaration*
        -> ^(NAMESPACES defaultNamespaceDeclaration? namespaceDeclaration*)
    ;

namespaceDeclaration :
        prefix namespace
        -> ^(NAMESPACE prefix namespace)
    ;

/* Problem: NCNAME is a fragment, and failed to be matched.
Put QNAME instead, but that's not correct, it should really be a NCNAME! */

prefix :
       'prefix' QNAME -> ^(PREFIX QNAME)
    ;


namespace :
        IRI_REF
    ;

defaultNamespaceDeclaration :
        'default' IRI_REF
        ->  ^(DEFAULTNAMESPACE IRI_REF)
    ;

record
	:	(entityExpression | activityExpression | agentExpression | generationExpression  | usageExpression | startExpression | endExpression | derivationExpression | dependenceExpression  | controlExpression | alternateExpression | specializationExpression  |  associationExpression | attributionExpression | responsibilityExpression | informExpression | wasStartedByActivityExpression)
	;

/*
        Component 2: Agents and Responsibility

*/

entityExpression
	:	'entity' '(' identifier optionalAttributeValuePairs ')'
        -> ^(ENTITY identifier optionalAttributeValuePairs)
	;


activityExpression
	:	'activity' '(' identifier (',' (startTime | '-' ) ',' (endTime | '-'))? optionalAttributeValuePairs ')'
        -> ^(ACTIVITY identifier ^(START startTime?) ^(END endTime?) optionalAttributeValuePairs )
	;

generationExpression
	:	'wasGeneratedBy' '(' ((id0=identifier | '-') ',')? id2=identifier ',' ((id1=identifier) | '-') ',' ( '-' | time) optionalAttributeValuePairs ')'
      -> {$id1.tree==null}? ^(WGB ^(ID $id0?) $id2 ^(ID)  ^(TIME time?) optionalAttributeValuePairs)
      -> ^(WGB ^(ID $id0?) $id2 $id1  ^(TIME time?) optionalAttributeValuePairs)
	;

usageExpression
	:	'used' '(' optionalIdentifier identifier ',' identifier optionalTime optionalAttributeValuePairs ')'
      -> ^(USED optionalIdentifier identifier+ optionalTime optionalAttributeValuePairs)
	;

startExpression
	:	'wasStartedBy' '(' ((id0=identifier | '-') ',')? id2=identifier ',' ((id1=identifier) | '-') ',' ( '-' | time) optionalAttributeValuePairs ')'
      -> {$id1.tree==null}? ^(WSB ^(ID $id0?) $id2 ^(ID)  ^(TIME time?) optionalAttributeValuePairs)
      -> ^(WSB ^(ID $id0?) $id2 $id1  ^(TIME time?) optionalAttributeValuePairs)
	;

endExpression
	:	'wasEndedBy' '(' ((id0=identifier | '-') ',')? id2=identifier ',' ((id1=identifier) | '-') ',' ( '-' | time) optionalAttributeValuePairs ')'
      -> {$id1.tree==null}? ^(WEB ^(ID $id0?) $id2 ^(ID)  ^(TIME time?) optionalAttributeValuePairs)
      -> ^(WEB ^(ID $id0?) $id2 $id1  ^(TIME time?) optionalAttributeValuePairs)
	;

informExpression
	:	'wasInformedBy' '(' optionalIdentifier identifier ',' identifier optionalAttributeValuePairs ')'
      -> ^(WIB optionalIdentifier identifier+ optionalAttributeValuePairs)
	;

wasStartedByActivityExpression
	:	'wasStartedByActivity' '(' optionalIdentifier identifier ',' identifier optionalAttributeValuePairs ')'
      -> ^(WSBA optionalIdentifier identifier+ optionalAttributeValuePairs)
	;


/*
        Component 2: Agents and Responsibility

*/

agentExpression
	:	'agent' '(' identifier optionalAttributeValuePairs	')' -> ^(AGENT identifier optionalAttributeValuePairs )
	;

attributionExpression
	:	'wasAttributedTo' '('  optionalIdentifier e=identifier ',' ag=identifier optionalAttributeValuePairs ')'
      -> ^(WAT optionalIdentifier $e $ag optionalAttributeValuePairs)
	;

associationExpression
	:	'wasAssociatedWith' '('  ((id0=identifier | '-') ',')? a=identifier ',' (ag=identifier | '-') ',' (pl=identifier | '-') optionalAttributeValuePairs ')'
      -> {$ag.tree==null}? ^(WAW ^(ID $id0?) $a ^(ID) ^(PLAN $pl?) optionalAttributeValuePairs)
      -> ^(WAW ^(ID $id0?) $a $ag? ^(PLAN $pl?) optionalAttributeValuePairs)
	;

responsibilityExpression
	:	'actedOnBehalfOf' '('   ((id0=identifier | '-') ',')? ag2=identifier ',' ag1=identifier ','  (a=identifier | '-') optionalAttributeValuePairs ')'
      -> {$a.tree==null}? ^(AOBO  ^(ID $id0?) $ag2 $ag1 ^(ID) optionalAttributeValuePairs)
      -> ^(AOBO  ^(ID $id0?) $ag2 $ag1 $a? optionalAttributeValuePairs)
	;



/*
        Component 3: Derivations

*/


/* Use conditional test in order to remove ^(A ), ^(G ), ^(U ) */

derivationExpression
	:	'wasDerivedFrom' '(' ((id0=identifier | '-') ',')? id2=identifier ',' id1=identifier (',' (a=identifier | '-') ',' (g2=identifier  | '-') ',' (u1=identifier | '-') )?	optionalAttributeValuePairs ')'
      -> ^(WDF ^(ID $id0?) $id2 $id1 ^(A $a?)  ^(G $g2?) ^(U $u1?) optionalAttributeValuePairs)
	;


/* TODO: wasRevisionOf(id,e2,e1,ag,attrs) */
/* TODO: wasQuotedFrom(id,e2,e1,ag2,ag1,attrs) */
/* TODO: hadOriginalSource(id,e2,e1,attrs) */
/* TODO: tracedTo(id,e2,e1,attrs) */

/*
        Component 4: Alternate entities

*/

alternateExpression
	:	'alternateOf' '('  optionalIdentifier identifier ',' identifier optionalAttributeValuePairs ')'
      -> ^(ALTERNATE optionalIdentifier identifier+ optionalAttributeValuePairs)
	;

specializationExpression
	:	'specializationOf' '('  optionalIdentifier identifier ',' identifier optionalAttributeValuePairs ')'
      -> ^(SPECIALIZATION optionalIdentifier identifier+ optionalAttributeValuePairs)
	;

/*
        Component 5: Collections

*/

/*
        Component 6: Annotations

*/

/*
        OLD STUFF

*/


dependenceExpression
	:	'dependedUpon' '(' optionalIdentifier identifier ',' identifier optionalAttributeValuePairs ')'
      -> ^(WDO optionalIdentifier identifier+ optionalAttributeValuePairs)
	;

controlExpression
	:	'wasControlledBy' '(' optionalIdentifier identifier ',' identifier optionalAttributeValuePairs ')'
      -> ^(WCB optionalIdentifier identifier+ optionalAttributeValuePairs)
	;



optionalAttributeValuePairs
    :
    (',' '[' attributeValuePairs ']')?
        -> ^(ATTRIBUTES attributeValuePairs?)
    ;

optionalTime
    :
    (',' time )?
        -> ^(TIME time?)
    ;

optionalIdentifier
    :
    (identifier ',')?
        -> ^(ID identifier?)
    ;



identifier
	:
        QNAME -> ^(ID QNAME)
	;

attribute
	:
        QNAME
	;

attributeValuePairs
	:
        (  | attributeValuePair ( ','! attributeValuePair )* )
	;


attributeValuePair
	:
        attribute '=' literal  -> ^(ATTRIBUTE attribute literal)
	;


time
	:
        xsdDateTime
	;
startTime
	:
        xsdDateTime
	;
endTime
	:
        xsdDateTime
	;

recipeLink
	:
        IRI_REF
	;

/* TODO: complete grammar of Literal */
literal
	:
        (STRINGLITERAL -> ^(STRING STRINGLITERAL) |
         INTLITERAL -> ^(INT INTLITERAL) |
         STRINGLITERAL '%%' datatype -> ^(TYPEDLITERAL STRINGLITERAL datatype))
	;

datatype
	:
        (IRI_REF -> ^(IRI IRI_REF)
        |
         QNAME -> ^(QNAM QNAME))
	;
	
/** QNAME Syntax to be agreed, here allows all digits in the local part. */

QNAME	
	:	NCNAME (':' (NCNAME | POSINTLITERAL))?  
	;


fragment CHAR
	: ('\u0009' | '\u000A' | '\u000D' | '\u0020'..'\uD7FF' | '\uE000'..'\uFFFD' )
	;

/* fragment DIGITS 	   
	: ('0'..'9')+
	;
*/

fragment NCNAMESTARTCHAR
	: ('A'..'Z') | '_' | ('a'..'z') | ('\u00C0'..'\u00D6') | ('\u00D8'..'\u00F6') | ('\u00F8'..'\u02FF') | ('\u0370'..'\u037D') | ('\u037F'..'\u1FFF') | ('\u200C'..'\u200D') | ('\u2070'..'\u218F') | ('\u2C00'..'\u2FEF') | ('\u3001'..'\uD7FF') | ('\uF900'..'\uFDCF') | ('\uFDF0'..'\uFFFD')
	;
	
fragment NCNAMECHAR
	:   	NCNAMESTARTCHAR | '-' | '.' | '0'..'9' | '\u00B7' | '\u0300'..'\u036F' | '\u203F'..'\u2040'
	;
	
fragment NAMECHAR	   
	:   ':' 
	| NCNAMECHAR
	;
	
fragment NAMESTARTCHAR
	:  ':' 
	| NCNAMESTARTCHAR
	;
	


	
fragment NCNAME	           
 	:  NCNAMESTARTCHAR NCNAMECHAR* 
	;	


NCNAME_COLON_STAR
	: NCNAME ':' '*'
	;
STAR_COLON_NCNAME
	: '*' ':' NCNAME;

/*
NUMERICLITERAL 	   
	: ( ('.' DIGITS) |(DIGITS ('.' ('0'..'9')*)?)) (('e'|'E') ('+'|'-')? DIGITS)?
	;
		
*/
fragment QUOTE	           
	: '"'
	;
	
fragment APOS		   
	: '\''
	;
	
fragment ESCAPEQUOTE 	   
	: QUOTE QUOTE
	;
	
	
fragment ESCAPEAPOS 	   
	: APOS APOS
	;
	
fragment CHARNOQUOTE	   
	: ~(~CHAR | QUOTE)
	;
	
	
fragment CHARNOAPOS	   
	: ~(~CHAR | APOS)
	;


STRINGLITERAL		   
	: (QUOTE (ESCAPEQUOTE | CHARNOQUOTE)* QUOTE) 
	| (APOS  (ESCAPEAPOS | CHARNOAPOS)* APOS)
	;
			 

/* 
This lexer rule for comments handles multiline, nested comments
*/
COMMENT_CONTENTS
        :       '(:'
                {
                        $channel=98;
                }
                (       ~('('|':')
                        |       ('(' ~':') => '('
                        |       (':' ~')') => ':'
                        |       COMMENT_CONTENTS
                )*
                ':)'
        ;


WS		
	: (' '|'\r'|'\t'|'\u000C'|'\n')+ {$channel = HIDDEN;}
	;


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




xsdDateTime: IsoDateTime;




IsoDateTime: (DIGIT DIGIT DIGIT DIGIT '-' DIGIT DIGIT '-' DIGIT DIGIT 'T' DIGIT DIGIT ':' DIGIT DIGIT ':' DIGIT DIGIT ('.' DIGIT (DIGIT DIGIT?)?)? ('Z' | TimeZoneOffset)?)
    ;

fragment DIGIT: '0'..'9';

POSINTLITERAL:
    ('0'..'9')+
    ;

INTLITERAL:
    '-'? POSINTLITERAL
    ;


TimeZoneOffset: ('+' | '-') DIGIT DIGIT ':' DIGIT DIGIT;




