/*******************************************************************************
 * Copyright (c) 2011 Luc Moreau
 *******************************************************************************/
grammar ASN;

options {
  language = Java;
  output=AST;
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
	;

record
	:	(entityRecord | activityRecord | agentRecord )
	;

entityRecord
	:	'entity' '(' identifier ',' '[' attributeValuePairs ']'
		')'
	;

activityRecord
	:	'activity' '(' identifier (',' recipeLink)? ',' (time)? ',' (time)? ',' '[' attributeValuePairs ']'
		')'
	;

agentRecord
	:	'agent' '(' identifier 	')'
	;

identifier
	:
        QNAME
	;

attribute
	:
        QNAME
	;

attributeValuePairs
	:
        (  | attributeValuePair ( ',' attributeValuePair )* )
	;


attributeValuePair
	:
        attribute '=' literal
	;

/* TODO: grammar of xsd time */
time
	:
        STRINGLITERAL
	;

/* TODO: grammar of recipeLink */
recipeLink
	:
        STRINGLITERAL
	;

/* TODO: grammar of Literal */
literal
	:
        STRINGLITERAL
	;
	
QNAME	
	:	NCNAME (':' NCNAME)?
	;
	

fragment CHAR
	: ('\u0009' | '\u000A' | '\u000D' | '\u0020'..'\uD7FF' | '\uE000'..'\uFFFD' )
	;

fragment DIGITS 	   
	: ('0'..'9')+
	;

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

NUMERICLITERAL 	   
	: ( ('.' DIGITS) |(DIGITS ('.' ('0'..'9')*)?)) (('e'|'E') ('+'|'-')? DIGITS)?
	;
		

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
