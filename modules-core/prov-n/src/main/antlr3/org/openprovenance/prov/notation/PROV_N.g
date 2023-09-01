/*******************************************************************************
 * Copyright (c) 2011-2012 Luc Moreau
 *******************************************************************************/
grammar PROV_N;

options {
  language = Java;
  output=AST;
}

tokens {
    ID; ATTRIBUTE; ATTRIBUTES; IRI; QNAM; STRING; STRING_LONG; TYPEDLITERAL; INT; 
    EXPRESSIONS; NAMESPACE; DEFAULTNAMESPACE; NAMESPACES; PREFIX; 

    /* Component 1 */
    ENTITY; ACTIVITY; WGB; USED; WSB; WEB; WINVB; WIB; 
    TIME; START; END;

    /* Component 2 */
    WDF; WRO; PRIMARYSOURCE; WQF; INFL; 

    /* Component 3 */
    AGENT; WAT; WAW; AOBO; 

    /* Component 4 */
    DOCUMENT; BUNDLES; BUNDLE;

    /* Component 5 */
    SPECIALIZATION; ALTERNATE; CTX;

    /* Component 6 */
    MEM; DBIF; DBRF; KES; ES; KEYS; VALUES; DMEM; CMEM; TRUE; FALSE; UNKNOWN;

    /* Extensibility */
    EXT;
}

@header {
package org.openprovenance.prov.notation;
}

@lexer::header {
package org.openprovenance.prov.notation;
}

/* See        https://theantlrguy.atlassian.net/wiki/display/ANTLR3/Error+reporting+and+recovery */     


@lexer::members {
  @Override
  public void displayRecognitionError(String [] tokenNames,
                                      RecognitionException e) {
      String hdr = getErrorHeader(e);
      String msg = getErrorMessage(e, tokenNames);
      org.openprovenance.prov.notation.Utility.warn(hdr + " " + msg);
  }
}        

        
@members{

            
  @Override
  public void displayRecognitionError(String [] tokenNames,
                                      RecognitionException e) {
      String hdr = getErrorHeader(e);
      String msg = getErrorMessage(e, tokenNames);
      org.openprovenance.prov.notation.Utility.warn(hdr + " " + msg);
  }
            
 public static boolean qnameDisabled=false; }

document
	:	 'document' 
        (namespaceDeclarations)?
		(expression )*
        (bundle (bundle)*)?
		'endDocument'
      -> {$namespaceDeclarations.tree==null}? ^(DOCUMENT ^(NAMESPACES) ^(EXPRESSIONS expression*) ^(BUNDLES bundle*))
      -> ^(DOCUMENT namespaceDeclarations? ^(EXPRESSIONS expression*) ^(BUNDLES bundle*))
    ;

bundle
 	:	'bundle' identifier
        (namespaceDeclarations)?
		(expression)*
		'endBundle'
      -> {$namespaceDeclarations.tree==null}? ^(BUNDLE identifier ^(NAMESPACES) ^(EXPRESSIONS expression*))
      -> ^(BUNDLE identifier namespaceDeclarations? ^(EXPRESSIONS expression*))
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

        | insertionExpression | removalExpression | collectionMembershipExpression  | dictionaryMembershipExpression


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
      -> {$id1.tree==null }?  // both are absent
         ^(WGB ^(ID $id0?) $id2 ^(ID)  ^(TIME) optionalAttributeValuePairs)
      -> {$id1.tree!=null && $tt.tree==null}?
         ^(WGB ^(ID $id0?) $id2 $id1 ^(TIME) optionalAttributeValuePairs)
      -> ^(WGB ^(ID $id0?) $id2 $id1 $tt optionalAttributeValuePairs)
	;

/* TODO: ensure that optionalIdentifier always returns an ID??? */

optionalIdentifier
    : ((id0=identifier | '-') SEMICOLON)?
     -> identifier?
    ;

identifierOrMarker
    :
        (identifier  -> identifier | '-' -> ^(ID))
    ;

usageExpression
	:	'used' '(' id0=optionalIdentifier  id2=identifier (',' id1=identifierOrMarker ',' ( tt=time | '-' ))? optionalAttributeValuePairs ')'
      -> {$id1.tree==null }?  // both are absent
         ^(USED ^(ID $id0?)  $id2 ^(ID) ^(TIME) optionalAttributeValuePairs)
      -> {$id1.tree!=null && $tt.tree==null}?
         ^(USED ^(ID $id0?)  $id2 $id1 ^(TIME) optionalAttributeValuePairs)
      -> ^(USED ^(ID $id0?)  $id2 $id1 time optionalAttributeValuePairs)
	;
startExpression
	:	'wasStartedBy' '(' id0=optionalIdentifier id2=identifier (',' id1=identifierOrMarker ',' id3=identifierOrMarker ',' ( tt=time | '-' ))? optionalAttributeValuePairs ')'
      -> {$id1.tree==null}?  // all are absent
         ^(WSB ^(ID $id0?) $id2 ^(ID) ^(ID) ^(TIME) optionalAttributeValuePairs)
      -> {$id1.tree!=null && $tt.tree==null}?  
         ^(WSB ^(ID $id0?) $id2 $id1 $id3 ^(TIME) optionalAttributeValuePairs)
      -> ^(WSB ^(ID $id0?) $id2 $id1 $id3  time optionalAttributeValuePairs)
	;

endExpression
	:	'wasEndedBy' '(' id0=optionalIdentifier id2=identifier (',' id1=identifierOrMarker ',' id3=identifierOrMarker ',' ( tt=time | '-' ))? optionalAttributeValuePairs ')'
      -> {$id1.tree==null}? // all are absent
        ^(WEB ^(ID $id0?) $id2 ^(ID) ^(ID) ^(TIME) optionalAttributeValuePairs)
      -> {$id1.tree!=null && $tt.tree==null}?  
        ^(WEB ^(ID $id0?) $id2 $id1 $id3 ^(TIME) optionalAttributeValuePairs)
      -> ^(WEB ^(ID $id0?) $id2 $id1 $id3  time optionalAttributeValuePairs)
	;

/*TODO handle no optional everywhere */

invalidationExpression
	:	'wasInvalidatedBy' '(' id0=optionalIdentifier id2=identifier (',' id1=identifierOrMarker ',' ( tt=time | '-' ))? optionalAttributeValuePairs ')'
      -> {$id1.tree==null }?  // both are absent
         ^(WINVB ^(ID $id0?) $id2 ^(ID)  ^(TIME) optionalAttributeValuePairs)
      -> {$id1.tree!=null && $tt.tree==null}?
         ^(WINVB ^(ID $id0?) $id2 $id1  ^(TIME) optionalAttributeValuePairs)
      -> ^(WINVB ^(ID $id0?) $id2 $id1  time optionalAttributeValuePairs)
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
        -> ^(CTX $su $en $bu)
	;


/*
        Component 6: Collections

TODO: literal used in these production needs to disable qname, to allow for intliteral

*/

insertionExpression
@after { qnameDisabled = false; }
	: 	'prov:derivedByInsertionFrom('  id0=optionalIdentifier id2=identifier ',' id1=identifier ',' { qnameDisabled=true; } keyEntitySet optionalAttributeValuePairs ')'
      -> ^(DBIF ^(ID $id0?) $id2 $id1 keyEntitySet  optionalAttributeValuePairs)
	;

// todo: check that qname is properly disabled
removalExpression
@after { qnameDisabled = false; }
	:	'prov:derivedByRemovalFrom('  id0=optionalIdentifier id2=identifier ',' id1=identifier ',' { qnameDisabled=true; } '{' literal (',' literal)* '}' { qnameDisabled=false; }  optionalAttributeValuePairs ')'
      -> ^(DBRF ^(ID $id0?) $id2 $id1 ^(KEYS literal*)  optionalAttributeValuePairs)
	;



dictionaryMembershipExpression
	:	 'prov:hadDictionaryMember('  id1=identifier ',' id2=identifier ',' literal ')'
      -> ^(DMEM ^(ID) $id1 ^(KES ^(KEYS literal) ^(VALUES $id2)))
	;



collectionMembershipExpression
	:	 'hadMember' '('  id2=identifier ',' id1=identifier ')'
      -> ^(MEM $id2 $id1)
  ; 


//@after { qnameDisabled = false; } 
// 
// 
//  {qnameDisabled=false; }
// {qnameDisabled=true; }

keyEntitySet

    : '{'  '('   { qnameDisabled=true; } literal {qnameDisabled=false; } ','  identifier  ')' ( ','  '('  {qnameDisabled=true; } literal {qnameDisabled=false; }  ',' identifier  ')' )* '}'
      -> ^(KES ^(KEYS literal+) ^(VALUES identifier+))
    ;





entitySet
    : '{'  identifier* '}'
      -> ^(ES  identifier?)
    ;



/*
        Component: Extensibility

*/

// { !$qn.text.contains("prov:") }?
extensibilityExpression
	:	qn=QUALIFIED_NAME { System.out.println("extensibility: qnameDisabled " + qnameDisabled + " " + $qn.text); }  '(' id0=optionalIdentifier extensibilityArgument ( ','  extensibilityArgument)* attr=optionalAttributeValuePairs ')'
      -> {$attr.tree==null}?
         ^(EXT ^(ID QUALIFIED_NAME) ^(ID $id0?) extensibilityArgument* ^(ATTRIBUTES))
      -> ^(EXT ^(ID QUALIFIED_NAME) ^(ID $id0?) extensibilityArgument* optionalAttributeValuePairs)
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
    (',' OPEN_SQUARE_BRACE attributeValuePairs CLOSE_SQUARE_BRACE)?
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
        attribute { qnameDisabled = true; } EQUAL  literal  -> ^(ATTRIBUTE attribute literal)
	;


time
	:
        xsdDateTime -> ^(TIME xsdDateTime)
	;


/* TODO: complete grammar of Literal */


literal :
        (
         STRING_LITERAL LANGTAG? -> ^(STRING STRING_LITERAL LANGTAG?) |
         STRING_LITERAL_LONG2  LANGTAG? -> ^(STRING_LONG STRING_LITERAL_LONG2 LANGTAG?) |
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
                                           } DOT )* (PN_CHARS | PN_CHARS_OTHERS ))?
;

fragment PN_CHARS_OTHERS 
    : 
      PERCENT | '/' | '@' | '~' | '&' | '+' | '*' | '?' | '#' | '$' | '!'  | PN_CHARS_ESC
    ;

//fragment PN_CHARS_ESC: '\\=' | '\\\'' | '\\(' | '\\)' | '\\,' | '\\-'| '\\:'| '\\;' | '\\[' | '\\]'  | '\\.' ;

fragment PN_CHARS_ESC: '\\' ('=' | '\'' | '(' | ')' | ',' | '-'| ':'| ';' | '[' | ']'  | '.' ) ;

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

EQUAL : '=';

DOT : '.';

MINUS : '-';

SEMICOLON : ';';



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

OPEN_SQUARE_BRACE
  :
  '['
  ;

CLOSE_SQUARE_BRACE
  :
  ']'
  ;



/* Need to implement fully http://www.w3.org/TR/xmlschema11-2/#nt-dateTimeRep */

xsdDateTime: DateTime;

DateTime: 
(DIGIT DIGIT DIGIT DIGIT '-' DIGIT DIGIT '-' DIGIT DIGIT 'T' DIGIT DIGIT ':' DIGIT DIGIT ':' DIGIT DIGIT ('.' DIGIT DIGIT*)? ('Z' | TimeZoneOffset)?)
    ;




TimeZoneOffset: ('+' | '-') DIGIT DIGIT ':' DIGIT DIGIT;




