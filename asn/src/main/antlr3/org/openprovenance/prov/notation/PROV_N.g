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
    CONTAINER; NAMESPACE; DEFAULTNAMESPACE; NAMESPACES; PREFIX; 

    /* Component 1 */
    ENTITY; ACTIVITY; WGB; USED; WSB; WEB; WIB; WSBA;
    TIME; START; END;

    /* Component 2 */
    AGENT; PLAN; WAT; WAW; AOBO; 
    /* Component 3 */
    WDF; WRO; ORIGINALSOURCE; WQF; TRACEDTO; 
    /* Component 4 */
    SPECIALIZATION; ALTERNATE; 
    /* Component 5 */
    /* Component 6 */
    NOTE; HAN;
}

@header {
package org.openprovenance.prov.notation;
}

@lexer::header {
package org.openprovenance.prov.notation;
}

@members{
 public static boolean qnameDisabled=false; }

container
	:	ML_COMMENT* 'container' 
        (namespaceDeclarations)?
		(record | ML_COMMENT | SL_COMMENT)*
		'endContainer'

      -> ^(CONTAINER namespaceDeclarations? record*)
	;


namespaceDeclarations :
        (defaultNamespaceDeclaration | namespaceDeclaration) namespaceDeclaration*
        -> ^(NAMESPACES defaultNamespaceDeclaration? namespaceDeclaration*)
    ;


/**
  The following production uses ANTLR directives to disactivate the
  production QNAME to guarantee that ensure that PREFX token is
  generated in this context only.

  A global static boolean variable (qnameDisabled) is declared in the
  parser, set to true when entering this production and set to false
  on exit.  This variable is use in the lexer to disable the QNAME
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

record
@init { qnameDisabled = false; } :
        (   /* component 1 */

           entityExpression | activityExpression | generationExpression  | usageExpression
         | startExpression | endExpression | communicationExpression | startByActivityExpression

            /* component 2 */
        
        | agentExpression |  associationExpression | attributionExpression | responsibilityExpression

            /* component 3 */

        | derivationExpression | tracedToExpression | hadOriginalSourceExpression | quotationExpression | revisionExpression

            /* component 4 */

        | alternateExpression | specializationExpression

            /* component 5 */
            /* component 6 */ 
        | noteExpression | hasAnnotationExpression
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
	:	'wasGeneratedBy' '(' ((id0=identifier | '-') ',')? id2=identifier ',' ((id1=identifier) | '-') ',' ( time | '-' ) optionalAttributeValuePairs ')'
      -> {$id1.tree==null}? ^(WGB ^(ID $id0?) $id2 ^(ID)  ^(TIME time?) optionalAttributeValuePairs)
      -> ^(WGB ^(ID $id0?) $id2 $id1  ^(TIME time?) optionalAttributeValuePairs)
	;

usageExpression
	:	'used' '(' ((id0=identifier | '-') ',')?  id2=identifier ',' id1=identifier ',' ( time | '-' ) optionalAttributeValuePairs ')'
      -> ^(USED ^(ID $id0?)  $id2 $id1 ^(TIME time?) optionalAttributeValuePairs)
	;

startExpression
	:	'wasStartedBy' '(' ((id0=identifier | '-') ',')? id2=identifier ',' ((id1=identifier) | '-') ',' ( time | '-' ) optionalAttributeValuePairs ')'
      -> {$id1.tree==null}? ^(WSB ^(ID $id0?) $id2 ^(ID)  ^(TIME time?) optionalAttributeValuePairs)
      -> ^(WSB ^(ID $id0?) $id2 $id1  ^(TIME time?) optionalAttributeValuePairs)
	;

endExpression
	:	'wasEndedBy' '(' ((id0=identifier | '-') ',')? id2=identifier ',' ((id1=identifier) | '-') ',' ( time | '-' ) optionalAttributeValuePairs ')'
      -> {$id1.tree==null}? ^(WEB ^(ID $id0?) $id2 ^(ID)  ^(TIME time?) optionalAttributeValuePairs)
      -> ^(WEB ^(ID $id0?) $id2 $id1  ^(TIME time?) optionalAttributeValuePairs)
	;


communicationExpression
	:	'wasInformedBy' '(' ((id0=identifier | '-') ',')? id2=identifier ',' id1=identifier optionalAttributeValuePairs ')'
      -> ^(WIB ^(ID $id0?) $id2 $id1 optionalAttributeValuePairs)
	;

startByActivityExpression
	:	'wasStartedByActivity' '(' ((id0=identifier | '-') ',')? id2=identifier ',' id1=identifier optionalAttributeValuePairs ')'
      -> ^(WSBA ^(ID $id0?) $id2 $id1 optionalAttributeValuePairs)
	;


/*
        Component 2: Agents and Responsibility

*/

agentExpression
	:	'agent' '(' identifier optionalAttributeValuePairs	')' 
        -> ^(AGENT identifier optionalAttributeValuePairs )
	;

attributionExpression
	:	'wasAttributedTo' '('  ((id0=identifier | '-') ',')? e=identifier ',' ag=identifier optionalAttributeValuePairs ')'
      -> ^(WAT  ^(ID $id0?) $e $ag optionalAttributeValuePairs)
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


derivationExpression
	:	'wasDerivedFrom' '(' ((id0=identifier | '-') ',')? id2=identifier ',' id1=identifier (',' (a=identifier | '-') ',' (g2=identifier  | '-') ',' (u1=identifier | '-') )?	optionalAttributeValuePairs ')'
      -> {$a.tree==null && $g2.tree==null && $u1.tree==null}?
          ^(WDF ^(ID $id0?) $id2 $id1 ^(ID) ^(ID) ^(ID) optionalAttributeValuePairs)
      -> {$a.tree!=null && $g2.tree==null && $u1.tree==null}?
          ^(WDF ^(ID $id0?) $id2 $id1 $a ^(ID) ^(ID) optionalAttributeValuePairs)
      -> {$a.tree==null && $g2.tree!=null && $u1.tree==null}?
          ^(WDF ^(ID $id0?) $id2 $id1 ^(ID) $g2 ^(ID) optionalAttributeValuePairs)
      -> {$a.tree!=null && $g2.tree!=null && $u1.tree==null}?
          ^(WDF ^(ID $id0?) $id2 $id1 $a $g2 ^(ID) optionalAttributeValuePairs)

      -> {$a.tree==null && $g2.tree==null && $u1.tree!=null}?
          ^(WDF ^(ID $id0?) $id2 $id1 ^(ID) ^(ID) $u1 optionalAttributeValuePairs)
      -> {$a.tree!=null && $g2.tree==null && $u1.tree!=null}?
          ^(WDF ^(ID $id0?) $id2 $id1 $a ^(ID) $u1 optionalAttributeValuePairs)
      -> {$a.tree==null && $g2.tree!=null && $u1.tree!=null}?
          ^(WDF ^(ID $id0?) $id2 $id1 ^(ID) $g2 $u1 optionalAttributeValuePairs)
      -> ^(WDF ^(ID $id0?) $id2 $id1 $a $g2 $u1 optionalAttributeValuePairs)
	;


revisionExpression
	:	'wasRevisionOf' '('  ((id0=identifier | '-') ',')? id2=identifier ',' id1=identifier ',' (ag=identifier | '-')optionalAttributeValuePairs ')'
      -> {$ag.tree==null}? ^(WRO ^(ID $id0?) $id2 $id1 ^(ID) optionalAttributeValuePairs)
      -> ^(WRO ^(ID $id0?) $id2 $id1 $ag optionalAttributeValuePairs)
	;


quotationExpression
	:	'wasQuotedFrom' '('  ((id0=identifier | '-') ',')? id2=identifier ',' id1=identifier (',' (ag2=identifier | '-')',' (ag1=identifier | '-'))? optionalAttributeValuePairs ')'
      -> {$ag1.tree==null && $ag2.tree==null}? ^(WQF ^(ID $id0?) $id2 $id1 ^(ID) ^(ID) optionalAttributeValuePairs)
      -> {$ag1.tree!=null && $ag2.tree==null}? ^(WQF ^(ID $id0?) $id2 $id1 $ag1 ^(ID) optionalAttributeValuePairs)
      -> {$ag1.tree==null && $ag2.tree!=null}? ^(WQF ^(ID $id0?) $id2 $id1 ^(ID) $ag2 optionalAttributeValuePairs)
      -> ^(WQF ^(ID $id0?) $id2 $id1 $ag2? $ag1? optionalAttributeValuePairs)
	;

hadOriginalSourceExpression
	:	'hadOriginalSource' '('   ((id0=identifier | '-') ',')? id2=identifier ',' id1=identifier optionalAttributeValuePairs ')'
      -> ^(ORIGINALSOURCE  ^(ID $id0?) $id2 $id1 optionalAttributeValuePairs)
	;

tracedToExpression
	:	'tracedTo' '('  ((id0=identifier | '-') ',')? id2=identifier ',' id1=identifier optionalAttributeValuePairs ')'
      -> ^(TRACEDTO ^(ID $id0?) $id2 $id1 optionalAttributeValuePairs)
	;


/*
        Component 4: Alternate entities

*/

alternateExpression
	:	'alternateOf' '('  identifier ',' identifier ')'
      -> ^(ALTERNATE identifier+)
	;

specializationExpression
	:	'specializationOf' '('  identifier ',' identifier ')'
      -> ^(SPECIALIZATION identifier+)
	;

/*
        Component 5: Collections

*/

/* TODO */

/*
        Component 6: Annotations

*/

noteExpression
	:	'note' '(' identifier optionalAttributeValuePairs	')' 
        -> ^(NOTE identifier optionalAttributeValuePairs )
	;

hasAnnotationExpression
	:	'hasAnnotation' '(' identifier ',' identifier	')' 
        -> ^(HAN identifier+ )
	;






optionalAttributeValuePairs
    :
    (',' '[' attributeValuePairs ']')?
        -> ^(ATTRIBUTES attributeValuePairs?)
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


/*
QNAME and INTLITERAL are conflicting.  To avoid the conflict in
literal, QNAME rule is disabled once an attribute has been parsed, to
give priority to INTLITERAL.
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


/* TODO: complete grammar of Literal
*/


literal :
        (STRINGLITERAL -> ^(STRING STRINGLITERAL) |
         INTLITERAL -> ^(INT INTLITERAL) |
         STRINGLITERAL { qnameDisabled = false; } '%%' datatype -> ^(TYPEDLITERAL STRINGLITERAL datatype))
	;

datatype:
        (IRI_REF -> ^(IRI IRI_REF)
        |
         QNAME -> ^(QNAM QNAME))
	;
	

/* 
The production here are based on the SPARQL grammar availabe from:

http://antlr.org/grammar/1200929755392/Sparql.g 

*/




INTLITERAL:
    '-'? DIGIT+
    ;

STRINGLITERAL : '"' (options {greedy=false;} : ~('"' | '\\' | EOL) | ECHAR)* '"';


/* This production uses a "Disambiguating Semantic Predicates"
   checking whether we are in scope of a declaration/literal or not. If so,
   rule QNAME is disabled, to the benefit of PREFX/INTLITERAL. */

QNAME:
  { !PROV_NParser.qnameDisabled }?
    (PN_PREFIX ':')? PN_LOCAL
        
  ;

/* The order of these two rules is crucial. By default, QNAME should be used
   unless we are in the context of a declaration. */
PREFX:
    PN_PREFIX
  ;



fragment
ECHAR : '\\' ('t' | 'b' | 'n' | 'r' | 'f' | '\\' | '"' | '\'');
 

fragment
PN_CHARS_U : PN_CHARS_BASE | '_';

fragment
PN_CHARS
    : PN_CHARS_U
    | MINUS
    | DIGIT
    | '\u00B7' 
    | '\u0300'..'\u036F'
    | '\u203F'..'\u2040'
    ;

fragment
PN_PREFIX : PN_CHARS_BASE ((PN_CHARS|DOT)* PN_CHARS)?;


fragment
PN_LOCAL:
  (PN_CHARS_U|DIGIT)  ((PN_CHARS|{    
                    	                       if (input.LA(1)=='.') {
                    	                          int LA2 = input.LA(2);
                    	       	                  if (!((LA2>='-' && LA2<='.')||(LA2>='0' && LA2<='9')||(LA2>='A' && LA2<='Z')||LA2=='_'||(LA2>='a' && LA2<='z')||LA2=='\u00B7'||(LA2>='\u00C0' && LA2<='\u00D6')||(LA2>='\u00D8' && LA2<='\u00F6')||(LA2>='\u00F8' && LA2<='\u037D')||(LA2>='\u037F' && LA2<='\u1FFF')||(LA2>='\u200C' && LA2<='\u200D')||(LA2>='\u203F' && LA2<='\u2040')||(LA2>='\u2070' && LA2<='\u218F')||(LA2>='\u2C00' && LA2<='\u2FEF')||(LA2>='\u3001' && LA2<='\uD7FF')||(LA2>='\uF900' && LA2<='\uFDCF')||(LA2>='\uFDF0' && LA2<='\uFFFD'))) {
                    	       	                     return;
                    	       	                  }
                    	                       }
                                           } DOT)* PN_CHARS)?
;

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
	
DOT : '.';

MINUS : '-';


/* Multiline comment */
ML_COMMENT
    :   '/*' (options {greedy=false;} : .)* '*/' {$channel=HIDDEN;}
    ;

/* Singleline comment */
SL_COMMENT : '//' (options{greedy=false;} : .)* EOL { $channel=HIDDEN; };


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




xsdDateTime: IsoDateTime;



IsoDateTime: (DIGIT DIGIT DIGIT DIGIT '-' DIGIT DIGIT '-' DIGIT DIGIT 'T' DIGIT DIGIT ':' DIGIT DIGIT ':' DIGIT DIGIT ('.' DIGIT (DIGIT DIGIT?)?)? ('Z' | TimeZoneOffset)?)
    ;




TimeZoneOffset: ('+' | '-') DIGIT DIGIT ':' DIGIT DIGIT;




