/*******************************************************************************
 * Copyright (c) 2011 Luc Moreau
 *******************************************************************************/
grammar ASN;

options {
  language = Java;
}

@header {
  package org.openprovenance.prov.asn;
}

@lexer::header {
  package org.openprovenance.prov.asn;
}

container
	:	'container' IDENT '='
		record*
		'end' IDENT '.'
	;

record
	:	entityRecord
	;

entityRecord
	:	'entity' '('
		')'
	;


STRING_LITERAL
	:	'"'
		{ StringBuilder b = new StringBuilder(); }
		(	'"' '"'				{ b.appendCodePoint('"');}
		|	c=~('"'|'\r'|'\n')	{ b.appendCodePoint(c);}
		)*
		'"'
		{ setText(b.toString()); }
	;
	
CHAR_LITERAL
	:	'\'' . '\'' {setText(getText().substring(1,2));}
	;

fragment LETTER : ('a'..'z' | 'A'..'Z') ;
fragment DIGIT : '0'..'9';
INTEGER : DIGIT+ ;
IDENT : LETTER (LETTER | DIGIT)*;
WS : (' ' | '\t' | '\n' | '\r' | '\f')+ {$channel = HIDDEN;};
COMMENT : '//' .* ('\n'|'\r') {$channel = HIDDEN;};
