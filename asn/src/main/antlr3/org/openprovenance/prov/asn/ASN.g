/*******************************************************************************
 * Copyright (c) 2011 Luc Moreau
 *******************************************************************************/
grammar ASN;

options {
  language = Java;
  output=AST;
}

tokens {
    ATTRIBUTE; ATTRIBUTES; RECIPE; START; END; IRI; QNAM; AGENT; ENTITY; ACTIVITY; WGB; USED; WDF; TIME; WDO; WCB; STRING; TYPEDLITERAL; CONTAINER; ID;
}

@header {
  package org.openprovenance.prov.asn;
}

@lexer::header {
  package org.openprovenance.prov.asn;
}


container
	:	'container' '('
		record*
		')'

      -> ^(CONTAINER record*)
	;

record
	:	(entityRecord | activityRecord | agentRecord | generationRecord  | useRecord | derivationRecord | dependenceRecord  | controlRecord )
	;

entityRecord
	:	'entity' '(' identifier ',' '[' attributeValuePairs ']'
		')'
        -> ^(ENTITY identifier ^(ATTRIBUTES attributeValuePairs?))
	;

activityRecord
	:	'activity' '(' identifier (',' recipeLink)? ',' (startTime)? ',' (endTime)? ',' '[' attributeValuePairs ']'
		')'
        -> ^(ACTIVITY identifier ^(RECIPE recipeLink?) ^(START startTime?) ^(END endTime?) ^(ATTRIBUTES attributeValuePairs?))
	;

agentRecord
	:	'agent' '(' identifier 	')' -> ^(AGENT identifier)
	;

generationRecord
	:	'wasGeneratedBy' '(' identifier ',' identifier ',' '[' attributeValuePairs ']' ( ',' time)?	')'
      -> ^(WGB identifier+ ^(ATTRIBUTES attributeValuePairs?)  ^(TIME time?))
	;

useRecord
	:	'used' '(' identifier ',' identifier ',' '[' attributeValuePairs ']' ( ',' time)?	')'
      -> ^(USED identifier+ ^(ATTRIBUTES attributeValuePairs?) ^(TIME time?))
	;

/* TODO the rewrite rule concanetas all attributes */

derivationRecord
	:	'wasDerivedFrom' '(' identifier ',' identifier (',' identifier ',' '[' dst=attributeValuePairs ']' ',' '[' src=attributeValuePairs ']')?	')'
      -> ^(WDF identifier+  ^(ATTRIBUTES $dst?) ^(ATTRIBUTES $src?))
	;

dependenceRecord
	:	'dependedUpon' '(' identifier ',' identifier ')'
      -> ^(WDO identifier+)
	;

controlRecord
	:	'wasControlledBy' '(' identifier ',' identifier ',' '[' attributeValuePairs ']' ')'
      -> ^(WCB identifier+ ^(ATTRIBUTES attributeValuePairs?))
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
        (STRINGLITERAL -> ^(STRING STRINGLITERAL)|
         STRINGLITERAL '%%' datatype -> ^(TYPEDLITERAL STRINGLITERAL datatype))
	;

datatype
	:
        (IRI_REF -> ^(IRI IRI_REF)
        |
         QNAME -> ^(QNAM QNAME))
	;
	
QNAME	
	:	NCNAME (':' NCNAME)?  
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


/* TODO, this grammar is not right, since requires space after 'T'??? */

TimeZoneOffset: ('+' | '-') DIGIT DIGIT ':' DIGIT DIGIT;


IsoDateTime: (DIGIT DIGIT DIGIT DIGIT '-' DIGIT DIGIT '-' DIGIT DIGIT 'T' DIGIT DIGIT ':' DIGIT DIGIT ':' DIGIT DIGIT ('.' DIGIT (DIGIT DIGIT?)?)? ('Z' | TimeZoneOffset)?)
    ;



DIGIT: '0'..'9';

