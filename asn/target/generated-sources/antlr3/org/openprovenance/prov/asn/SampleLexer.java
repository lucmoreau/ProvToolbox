// $ANTLR 3.1.3 Mar 17, 2009 19:23:44 org/openprovenance/prov/asn/Sample.g 2011-11-08 13:49:45

  package org.openprovenance.prov.asn;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class SampleLexer extends Lexer {
    public static final int T__29=29;
    public static final int T__28=28;
    public static final int T__27=27;
    public static final int T__26=26;
    public static final int T__25=25;
    public static final int T__24=24;
    public static final int T__23=23;
    public static final int LETTER=9;
    public static final int T__22=22;
    public static final int T__21=21;
    public static final int T__20=20;
    public static final int T__60=60;
    public static final int EOF=-1;
    public static final int T__55=55;
    public static final int T__19=19;
    public static final int T__56=56;
    public static final int T__57=57;
    public static final int MULTILINE_COMMENT=8;
    public static final int T__58=58;
    public static final int STRING_LITERAL=6;
    public static final int T__16=16;
    public static final int T__51=51;
    public static final int T__15=15;
    public static final int T__52=52;
    public static final int T__53=53;
    public static final int T__18=18;
    public static final int T__54=54;
    public static final int T__17=17;
    public static final int T__14=14;
    public static final int T__13=13;
    public static final int T__59=59;
    public static final int IDENT=4;
    public static final int DIGIT=10;
    public static final int COMMENT=12;
    public static final int T__50=50;
    public static final int INTEGER=5;
    public static final int T__42=42;
    public static final int T__43=43;
    public static final int T__40=40;
    public static final int T__41=41;
    public static final int T__46=46;
    public static final int T__47=47;
    public static final int T__44=44;
    public static final int T__45=45;
    public static final int T__48=48;
    public static final int T__49=49;
    public static final int T__30=30;
    public static final int T__31=31;
    public static final int T__32=32;
    public static final int T__33=33;
    public static final int WS=11;
    public static final int T__34=34;
    public static final int T__35=35;
    public static final int T__36=36;
    public static final int CHAR_LITERAL=7;
    public static final int T__37=37;
    public static final int T__38=38;
    public static final int T__39=39;

    // delegates
    // delegators

    public SampleLexer() {;} 
    public SampleLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public SampleLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "org/openprovenance/prov/asn/Sample.g"; }

    // $ANTLR start "T__13"
    public final void mT__13() throws RecognitionException {
        try {
            int _type = T__13;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/Sample.g:11:7: ( 'program' )
            // org/openprovenance/prov/asn/Sample.g:11:9: 'program'
            {
            match("program"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__13"

    // $ANTLR start "T__14"
    public final void mT__14() throws RecognitionException {
        try {
            int _type = T__14;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/Sample.g:12:7: ( '=' )
            // org/openprovenance/prov/asn/Sample.g:12:9: '='
            {
            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__14"

    // $ANTLR start "T__15"
    public final void mT__15() throws RecognitionException {
        try {
            int _type = T__15;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/Sample.g:13:7: ( 'begin' )
            // org/openprovenance/prov/asn/Sample.g:13:9: 'begin'
            {
            match("begin"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__15"

    // $ANTLR start "T__16"
    public final void mT__16() throws RecognitionException {
        try {
            int _type = T__16;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/Sample.g:14:7: ( 'end' )
            // org/openprovenance/prov/asn/Sample.g:14:9: 'end'
            {
            match("end"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__16"

    // $ANTLR start "T__17"
    public final void mT__17() throws RecognitionException {
        try {
            int _type = T__17;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/Sample.g:15:7: ( '.' )
            // org/openprovenance/prov/asn/Sample.g:15:9: '.'
            {
            match('.'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__17"

    // $ANTLR start "T__18"
    public final void mT__18() throws RecognitionException {
        try {
            int _type = T__18;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/Sample.g:16:7: ( 'constant' )
            // org/openprovenance/prov/asn/Sample.g:16:9: 'constant'
            {
            match("constant"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__18"

    // $ANTLR start "T__19"
    public final void mT__19() throws RecognitionException {
        try {
            int _type = T__19;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/Sample.g:17:7: ( ':' )
            // org/openprovenance/prov/asn/Sample.g:17:9: ':'
            {
            match(':'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__19"

    // $ANTLR start "T__20"
    public final void mT__20() throws RecognitionException {
        try {
            int _type = T__20;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/Sample.g:18:7: ( ':=' )
            // org/openprovenance/prov/asn/Sample.g:18:9: ':='
            {
            match(":="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__20"

    // $ANTLR start "T__21"
    public final void mT__21() throws RecognitionException {
        try {
            int _type = T__21;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/Sample.g:19:7: ( ';' )
            // org/openprovenance/prov/asn/Sample.g:19:9: ';'
            {
            match(';'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__21"

    // $ANTLR start "T__22"
    public final void mT__22() throws RecognitionException {
        try {
            int _type = T__22;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/Sample.g:20:7: ( 'var' )
            // org/openprovenance/prov/asn/Sample.g:20:9: 'var'
            {
            match("var"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__22"

    // $ANTLR start "T__23"
    public final void mT__23() throws RecognitionException {
        try {
            int _type = T__23;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/Sample.g:21:7: ( ',' )
            // org/openprovenance/prov/asn/Sample.g:21:9: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__23"

    // $ANTLR start "T__24"
    public final void mT__24() throws RecognitionException {
        try {
            int _type = T__24;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/Sample.g:22:7: ( 'Integer' )
            // org/openprovenance/prov/asn/Sample.g:22:9: 'Integer'
            {
            match("Integer"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__24"

    // $ANTLR start "T__25"
    public final void mT__25() throws RecognitionException {
        try {
            int _type = T__25;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/Sample.g:23:7: ( 'Boolean' )
            // org/openprovenance/prov/asn/Sample.g:23:9: 'Boolean'
            {
            match("Boolean"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__25"

    // $ANTLR start "T__26"
    public final void mT__26() throws RecognitionException {
        try {
            int _type = T__26;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/Sample.g:24:7: ( 'String' )
            // org/openprovenance/prov/asn/Sample.g:24:9: 'String'
            {
            match("String"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__26"

    // $ANTLR start "T__27"
    public final void mT__27() throws RecognitionException {
        try {
            int _type = T__27;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/Sample.g:25:7: ( 'Char' )
            // org/openprovenance/prov/asn/Sample.g:25:9: 'Char'
            {
            match("Char"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__27"

    // $ANTLR start "T__28"
    public final void mT__28() throws RecognitionException {
        try {
            int _type = T__28;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/Sample.g:26:7: ( 'type' )
            // org/openprovenance/prov/asn/Sample.g:26:9: 'type'
            {
            match("type"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__28"

    // $ANTLR start "T__29"
    public final void mT__29() throws RecognitionException {
        try {
            int _type = T__29;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/Sample.g:27:7: ( 'array' )
            // org/openprovenance/prov/asn/Sample.g:27:9: 'array'
            {
            match("array"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__29"

    // $ANTLR start "T__30"
    public final void mT__30() throws RecognitionException {
        try {
            int _type = T__30;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/Sample.g:28:7: ( '[' )
            // org/openprovenance/prov/asn/Sample.g:28:9: '['
            {
            match('['); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__30"

    // $ANTLR start "T__31"
    public final void mT__31() throws RecognitionException {
        try {
            int _type = T__31;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/Sample.g:29:7: ( '..' )
            // org/openprovenance/prov/asn/Sample.g:29:9: '..'
            {
            match(".."); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__31"

    // $ANTLR start "T__32"
    public final void mT__32() throws RecognitionException {
        try {
            int _type = T__32;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/Sample.g:30:7: ( ']' )
            // org/openprovenance/prov/asn/Sample.g:30:9: ']'
            {
            match(']'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__32"

    // $ANTLR start "T__33"
    public final void mT__33() throws RecognitionException {
        try {
            int _type = T__33;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/Sample.g:31:7: ( 'of' )
            // org/openprovenance/prov/asn/Sample.g:31:9: 'of'
            {
            match("of"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__33"

    // $ANTLR start "T__34"
    public final void mT__34() throws RecognitionException {
        try {
            int _type = T__34;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/Sample.g:32:7: ( 'record' )
            // org/openprovenance/prov/asn/Sample.g:32:9: 'record'
            {
            match("record"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__34"

    // $ANTLR start "T__35"
    public final void mT__35() throws RecognitionException {
        try {
            int _type = T__35;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/Sample.g:33:7: ( '<' )
            // org/openprovenance/prov/asn/Sample.g:33:9: '<'
            {
            match('<'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__35"

    // $ANTLR start "T__36"
    public final void mT__36() throws RecognitionException {
        try {
            int _type = T__36;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/Sample.g:34:7: ( '>' )
            // org/openprovenance/prov/asn/Sample.g:34:9: '>'
            {
            match('>'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__36"

    // $ANTLR start "T__37"
    public final void mT__37() throws RecognitionException {
        try {
            int _type = T__37;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/Sample.g:35:7: ( '(' )
            // org/openprovenance/prov/asn/Sample.g:35:9: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__37"

    // $ANTLR start "T__38"
    public final void mT__38() throws RecognitionException {
        try {
            int _type = T__38;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/Sample.g:36:7: ( ')' )
            // org/openprovenance/prov/asn/Sample.g:36:9: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__38"

    // $ANTLR start "T__39"
    public final void mT__39() throws RecognitionException {
        try {
            int _type = T__39;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/Sample.g:37:7: ( 'if' )
            // org/openprovenance/prov/asn/Sample.g:37:9: 'if'
            {
            match("if"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__39"

    // $ANTLR start "T__40"
    public final void mT__40() throws RecognitionException {
        try {
            int _type = T__40;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/Sample.g:38:7: ( 'then' )
            // org/openprovenance/prov/asn/Sample.g:38:9: 'then'
            {
            match("then"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__40"

    // $ANTLR start "T__41"
    public final void mT__41() throws RecognitionException {
        try {
            int _type = T__41;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/Sample.g:39:7: ( 'elsif' )
            // org/openprovenance/prov/asn/Sample.g:39:9: 'elsif'
            {
            match("elsif"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__41"

    // $ANTLR start "T__42"
    public final void mT__42() throws RecognitionException {
        try {
            int _type = T__42;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/Sample.g:40:7: ( 'else' )
            // org/openprovenance/prov/asn/Sample.g:40:9: 'else'
            {
            match("else"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__42"

    // $ANTLR start "T__43"
    public final void mT__43() throws RecognitionException {
        try {
            int _type = T__43;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/Sample.g:41:7: ( 'exit' )
            // org/openprovenance/prov/asn/Sample.g:41:9: 'exit'
            {
            match("exit"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__43"

    // $ANTLR start "T__44"
    public final void mT__44() throws RecognitionException {
        try {
            int _type = T__44;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/Sample.g:42:7: ( 'when' )
            // org/openprovenance/prov/asn/Sample.g:42:9: 'when'
            {
            match("when"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__44"

    // $ANTLR start "T__45"
    public final void mT__45() throws RecognitionException {
        try {
            int _type = T__45;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/Sample.g:43:7: ( 'while' )
            // org/openprovenance/prov/asn/Sample.g:43:9: 'while'
            {
            match("while"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__45"

    // $ANTLR start "T__46"
    public final void mT__46() throws RecognitionException {
        try {
            int _type = T__46;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/Sample.g:44:7: ( 'loop' )
            // org/openprovenance/prov/asn/Sample.g:44:9: 'loop'
            {
            match("loop"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__46"

    // $ANTLR start "T__47"
    public final void mT__47() throws RecognitionException {
        try {
            int _type = T__47;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/Sample.g:45:7: ( 'return' )
            // org/openprovenance/prov/asn/Sample.g:45:9: 'return'
            {
            match("return"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__47"

    // $ANTLR start "T__48"
    public final void mT__48() throws RecognitionException {
        try {
            int _type = T__48;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/Sample.g:46:7: ( 'procedure' )
            // org/openprovenance/prov/asn/Sample.g:46:9: 'procedure'
            {
            match("procedure"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__48"

    // $ANTLR start "T__49"
    public final void mT__49() throws RecognitionException {
        try {
            int _type = T__49;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/Sample.g:47:7: ( 'function' )
            // org/openprovenance/prov/asn/Sample.g:47:9: 'function'
            {
            match("function"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__49"

    // $ANTLR start "T__50"
    public final void mT__50() throws RecognitionException {
        try {
            int _type = T__50;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/Sample.g:48:7: ( 'not' )
            // org/openprovenance/prov/asn/Sample.g:48:9: 'not'
            {
            match("not"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__50"

    // $ANTLR start "T__51"
    public final void mT__51() throws RecognitionException {
        try {
            int _type = T__51;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/Sample.g:49:7: ( '+' )
            // org/openprovenance/prov/asn/Sample.g:49:9: '+'
            {
            match('+'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__51"

    // $ANTLR start "T__52"
    public final void mT__52() throws RecognitionException {
        try {
            int _type = T__52;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/Sample.g:50:7: ( '-' )
            // org/openprovenance/prov/asn/Sample.g:50:9: '-'
            {
            match('-'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__52"

    // $ANTLR start "T__53"
    public final void mT__53() throws RecognitionException {
        try {
            int _type = T__53;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/Sample.g:51:7: ( '*' )
            // org/openprovenance/prov/asn/Sample.g:51:9: '*'
            {
            match('*'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__53"

    // $ANTLR start "T__54"
    public final void mT__54() throws RecognitionException {
        try {
            int _type = T__54;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/Sample.g:52:7: ( '/' )
            // org/openprovenance/prov/asn/Sample.g:52:9: '/'
            {
            match('/'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__54"

    // $ANTLR start "T__55"
    public final void mT__55() throws RecognitionException {
        try {
            int _type = T__55;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/Sample.g:53:7: ( 'mod' )
            // org/openprovenance/prov/asn/Sample.g:53:9: 'mod'
            {
            match("mod"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__55"

    // $ANTLR start "T__56"
    public final void mT__56() throws RecognitionException {
        try {
            int _type = T__56;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/Sample.g:54:7: ( '/=' )
            // org/openprovenance/prov/asn/Sample.g:54:9: '/='
            {
            match("/="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__56"

    // $ANTLR start "T__57"
    public final void mT__57() throws RecognitionException {
        try {
            int _type = T__57;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/Sample.g:55:7: ( '<=' )
            // org/openprovenance/prov/asn/Sample.g:55:9: '<='
            {
            match("<="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__57"

    // $ANTLR start "T__58"
    public final void mT__58() throws RecognitionException {
        try {
            int _type = T__58;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/Sample.g:56:7: ( '>=' )
            // org/openprovenance/prov/asn/Sample.g:56:9: '>='
            {
            match(">="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__58"

    // $ANTLR start "T__59"
    public final void mT__59() throws RecognitionException {
        try {
            int _type = T__59;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/Sample.g:57:7: ( 'and' )
            // org/openprovenance/prov/asn/Sample.g:57:9: 'and'
            {
            match("and"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__59"

    // $ANTLR start "T__60"
    public final void mT__60() throws RecognitionException {
        try {
            int _type = T__60;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/Sample.g:58:7: ( 'or' )
            // org/openprovenance/prov/asn/Sample.g:58:9: 'or'
            {
            match("or"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__60"

    // $ANTLR start "MULTILINE_COMMENT"
    public final void mMULTILINE_COMMENT() throws RecognitionException {
        try {
            int _type = MULTILINE_COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/Sample.g:178:19: ( '/*' ( . )* '*/' )
            // org/openprovenance/prov/asn/Sample.g:178:21: '/*' ( . )* '*/'
            {
            match("/*"); 

            // org/openprovenance/prov/asn/Sample.g:178:26: ( . )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0=='*') ) {
                    int LA1_1 = input.LA(2);

                    if ( (LA1_1=='/') ) {
                        alt1=2;
                    }
                    else if ( ((LA1_1>='\u0000' && LA1_1<='.')||(LA1_1>='0' && LA1_1<='\uFFFF')) ) {
                        alt1=1;
                    }


                }
                else if ( ((LA1_0>='\u0000' && LA1_0<=')')||(LA1_0>='+' && LA1_0<='\uFFFF')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // org/openprovenance/prov/asn/Sample.g:178:26: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

            match("*/"); 

            _channel = HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "MULTILINE_COMMENT"

    // $ANTLR start "STRING_LITERAL"
    public final void mSTRING_LITERAL() throws RecognitionException {
        try {
            int _type = STRING_LITERAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            int c;

            // org/openprovenance/prov/asn/Sample.g:181:2: ( '\"' ( '\"' '\"' | c=~ ( '\"' | '\\r' | '\\n' ) )* '\"' )
            // org/openprovenance/prov/asn/Sample.g:181:4: '\"' ( '\"' '\"' | c=~ ( '\"' | '\\r' | '\\n' ) )* '\"'
            {
            match('\"'); 
             StringBuilder b = new StringBuilder(); 
            // org/openprovenance/prov/asn/Sample.g:183:3: ( '\"' '\"' | c=~ ( '\"' | '\\r' | '\\n' ) )*
            loop2:
            do {
                int alt2=3;
                int LA2_0 = input.LA(1);

                if ( (LA2_0=='\"') ) {
                    int LA2_1 = input.LA(2);

                    if ( (LA2_1=='\"') ) {
                        alt2=1;
                    }


                }
                else if ( ((LA2_0>='\u0000' && LA2_0<='\t')||(LA2_0>='\u000B' && LA2_0<='\f')||(LA2_0>='\u000E' && LA2_0<='!')||(LA2_0>='#' && LA2_0<='\uFFFF')) ) {
                    alt2=2;
                }


                switch (alt2) {
            	case 1 :
            	    // org/openprovenance/prov/asn/Sample.g:183:5: '\"' '\"'
            	    {
            	    match('\"'); 
            	    match('\"'); 
            	     b.appendCodePoint('"');

            	    }
            	    break;
            	case 2 :
            	    // org/openprovenance/prov/asn/Sample.g:184:5: c=~ ( '\"' | '\\r' | '\\n' )
            	    {
            	    c= input.LA(1);
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='!')||(input.LA(1)>='#' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}

            	     b.appendCodePoint(c);

            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);

            match('\"'); 
             setText(b.toString()); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "STRING_LITERAL"

    // $ANTLR start "CHAR_LITERAL"
    public final void mCHAR_LITERAL() throws RecognitionException {
        try {
            int _type = CHAR_LITERAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/Sample.g:191:2: ( '\\'' . '\\'' )
            // org/openprovenance/prov/asn/Sample.g:191:4: '\\'' . '\\''
            {
            match('\''); 
            matchAny(); 
            match('\''); 
            setText(getText().substring(1,2));

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "CHAR_LITERAL"

    // $ANTLR start "LETTER"
    public final void mLETTER() throws RecognitionException {
        try {
            // org/openprovenance/prov/asn/Sample.g:194:17: ( ( 'a' .. 'z' | 'A' .. 'Z' ) )
            // org/openprovenance/prov/asn/Sample.g:194:19: ( 'a' .. 'z' | 'A' .. 'Z' )
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "LETTER"

    // $ANTLR start "DIGIT"
    public final void mDIGIT() throws RecognitionException {
        try {
            // org/openprovenance/prov/asn/Sample.g:195:16: ( '0' .. '9' )
            // org/openprovenance/prov/asn/Sample.g:195:18: '0' .. '9'
            {
            matchRange('0','9'); 

            }

        }
        finally {
        }
    }
    // $ANTLR end "DIGIT"

    // $ANTLR start "INTEGER"
    public final void mINTEGER() throws RecognitionException {
        try {
            int _type = INTEGER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/Sample.g:196:9: ( ( DIGIT )+ )
            // org/openprovenance/prov/asn/Sample.g:196:11: ( DIGIT )+
            {
            // org/openprovenance/prov/asn/Sample.g:196:11: ( DIGIT )+
            int cnt3=0;
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0>='0' && LA3_0<='9')) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // org/openprovenance/prov/asn/Sample.g:196:11: DIGIT
            	    {
            	    mDIGIT(); 

            	    }
            	    break;

            	default :
            	    if ( cnt3 >= 1 ) break loop3;
                        EarlyExitException eee =
                            new EarlyExitException(3, input);
                        throw eee;
                }
                cnt3++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INTEGER"

    // $ANTLR start "IDENT"
    public final void mIDENT() throws RecognitionException {
        try {
            int _type = IDENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/Sample.g:197:7: ( LETTER ( LETTER | DIGIT )* )
            // org/openprovenance/prov/asn/Sample.g:197:9: LETTER ( LETTER | DIGIT )*
            {
            mLETTER(); 
            // org/openprovenance/prov/asn/Sample.g:197:16: ( LETTER | DIGIT )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0>='0' && LA4_0<='9')||(LA4_0>='A' && LA4_0<='Z')||(LA4_0>='a' && LA4_0<='z')) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // org/openprovenance/prov/asn/Sample.g:
            	    {
            	    if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "IDENT"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/Sample.g:198:4: ( ( ' ' | '\\t' | '\\n' | '\\r' | '\\f' )+ )
            // org/openprovenance/prov/asn/Sample.g:198:6: ( ' ' | '\\t' | '\\n' | '\\r' | '\\f' )+
            {
            // org/openprovenance/prov/asn/Sample.g:198:6: ( ' ' | '\\t' | '\\n' | '\\r' | '\\f' )+
            int cnt5=0;
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( ((LA5_0>='\t' && LA5_0<='\n')||(LA5_0>='\f' && LA5_0<='\r')||LA5_0==' ') ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // org/openprovenance/prov/asn/Sample.g:
            	    {
            	    if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||(input.LA(1)>='\f' && input.LA(1)<='\r')||input.LA(1)==' ' ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt5 >= 1 ) break loop5;
                        EarlyExitException eee =
                            new EarlyExitException(5, input);
                        throw eee;
                }
                cnt5++;
            } while (true);

            _channel = HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WS"

    // $ANTLR start "COMMENT"
    public final void mCOMMENT() throws RecognitionException {
        try {
            int _type = COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/Sample.g:199:9: ( '//' ( . )* ( '\\n' | '\\r' ) )
            // org/openprovenance/prov/asn/Sample.g:199:11: '//' ( . )* ( '\\n' | '\\r' )
            {
            match("//"); 

            // org/openprovenance/prov/asn/Sample.g:199:16: ( . )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0=='\n'||LA6_0=='\r') ) {
                    alt6=2;
                }
                else if ( ((LA6_0>='\u0000' && LA6_0<='\t')||(LA6_0>='\u000B' && LA6_0<='\f')||(LA6_0>='\u000E' && LA6_0<='\uFFFF')) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // org/openprovenance/prov/asn/Sample.g:199:16: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);

            if ( input.LA(1)=='\n'||input.LA(1)=='\r' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            _channel = HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COMMENT"

    public void mTokens() throws RecognitionException {
        // org/openprovenance/prov/asn/Sample.g:1:8: ( T__13 | T__14 | T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | T__26 | T__27 | T__28 | T__29 | T__30 | T__31 | T__32 | T__33 | T__34 | T__35 | T__36 | T__37 | T__38 | T__39 | T__40 | T__41 | T__42 | T__43 | T__44 | T__45 | T__46 | T__47 | T__48 | T__49 | T__50 | T__51 | T__52 | T__53 | T__54 | T__55 | T__56 | T__57 | T__58 | T__59 | T__60 | MULTILINE_COMMENT | STRING_LITERAL | CHAR_LITERAL | INTEGER | IDENT | WS | COMMENT )
        int alt7=55;
        alt7 = dfa7.predict(input);
        switch (alt7) {
            case 1 :
                // org/openprovenance/prov/asn/Sample.g:1:10: T__13
                {
                mT__13(); 

                }
                break;
            case 2 :
                // org/openprovenance/prov/asn/Sample.g:1:16: T__14
                {
                mT__14(); 

                }
                break;
            case 3 :
                // org/openprovenance/prov/asn/Sample.g:1:22: T__15
                {
                mT__15(); 

                }
                break;
            case 4 :
                // org/openprovenance/prov/asn/Sample.g:1:28: T__16
                {
                mT__16(); 

                }
                break;
            case 5 :
                // org/openprovenance/prov/asn/Sample.g:1:34: T__17
                {
                mT__17(); 

                }
                break;
            case 6 :
                // org/openprovenance/prov/asn/Sample.g:1:40: T__18
                {
                mT__18(); 

                }
                break;
            case 7 :
                // org/openprovenance/prov/asn/Sample.g:1:46: T__19
                {
                mT__19(); 

                }
                break;
            case 8 :
                // org/openprovenance/prov/asn/Sample.g:1:52: T__20
                {
                mT__20(); 

                }
                break;
            case 9 :
                // org/openprovenance/prov/asn/Sample.g:1:58: T__21
                {
                mT__21(); 

                }
                break;
            case 10 :
                // org/openprovenance/prov/asn/Sample.g:1:64: T__22
                {
                mT__22(); 

                }
                break;
            case 11 :
                // org/openprovenance/prov/asn/Sample.g:1:70: T__23
                {
                mT__23(); 

                }
                break;
            case 12 :
                // org/openprovenance/prov/asn/Sample.g:1:76: T__24
                {
                mT__24(); 

                }
                break;
            case 13 :
                // org/openprovenance/prov/asn/Sample.g:1:82: T__25
                {
                mT__25(); 

                }
                break;
            case 14 :
                // org/openprovenance/prov/asn/Sample.g:1:88: T__26
                {
                mT__26(); 

                }
                break;
            case 15 :
                // org/openprovenance/prov/asn/Sample.g:1:94: T__27
                {
                mT__27(); 

                }
                break;
            case 16 :
                // org/openprovenance/prov/asn/Sample.g:1:100: T__28
                {
                mT__28(); 

                }
                break;
            case 17 :
                // org/openprovenance/prov/asn/Sample.g:1:106: T__29
                {
                mT__29(); 

                }
                break;
            case 18 :
                // org/openprovenance/prov/asn/Sample.g:1:112: T__30
                {
                mT__30(); 

                }
                break;
            case 19 :
                // org/openprovenance/prov/asn/Sample.g:1:118: T__31
                {
                mT__31(); 

                }
                break;
            case 20 :
                // org/openprovenance/prov/asn/Sample.g:1:124: T__32
                {
                mT__32(); 

                }
                break;
            case 21 :
                // org/openprovenance/prov/asn/Sample.g:1:130: T__33
                {
                mT__33(); 

                }
                break;
            case 22 :
                // org/openprovenance/prov/asn/Sample.g:1:136: T__34
                {
                mT__34(); 

                }
                break;
            case 23 :
                // org/openprovenance/prov/asn/Sample.g:1:142: T__35
                {
                mT__35(); 

                }
                break;
            case 24 :
                // org/openprovenance/prov/asn/Sample.g:1:148: T__36
                {
                mT__36(); 

                }
                break;
            case 25 :
                // org/openprovenance/prov/asn/Sample.g:1:154: T__37
                {
                mT__37(); 

                }
                break;
            case 26 :
                // org/openprovenance/prov/asn/Sample.g:1:160: T__38
                {
                mT__38(); 

                }
                break;
            case 27 :
                // org/openprovenance/prov/asn/Sample.g:1:166: T__39
                {
                mT__39(); 

                }
                break;
            case 28 :
                // org/openprovenance/prov/asn/Sample.g:1:172: T__40
                {
                mT__40(); 

                }
                break;
            case 29 :
                // org/openprovenance/prov/asn/Sample.g:1:178: T__41
                {
                mT__41(); 

                }
                break;
            case 30 :
                // org/openprovenance/prov/asn/Sample.g:1:184: T__42
                {
                mT__42(); 

                }
                break;
            case 31 :
                // org/openprovenance/prov/asn/Sample.g:1:190: T__43
                {
                mT__43(); 

                }
                break;
            case 32 :
                // org/openprovenance/prov/asn/Sample.g:1:196: T__44
                {
                mT__44(); 

                }
                break;
            case 33 :
                // org/openprovenance/prov/asn/Sample.g:1:202: T__45
                {
                mT__45(); 

                }
                break;
            case 34 :
                // org/openprovenance/prov/asn/Sample.g:1:208: T__46
                {
                mT__46(); 

                }
                break;
            case 35 :
                // org/openprovenance/prov/asn/Sample.g:1:214: T__47
                {
                mT__47(); 

                }
                break;
            case 36 :
                // org/openprovenance/prov/asn/Sample.g:1:220: T__48
                {
                mT__48(); 

                }
                break;
            case 37 :
                // org/openprovenance/prov/asn/Sample.g:1:226: T__49
                {
                mT__49(); 

                }
                break;
            case 38 :
                // org/openprovenance/prov/asn/Sample.g:1:232: T__50
                {
                mT__50(); 

                }
                break;
            case 39 :
                // org/openprovenance/prov/asn/Sample.g:1:238: T__51
                {
                mT__51(); 

                }
                break;
            case 40 :
                // org/openprovenance/prov/asn/Sample.g:1:244: T__52
                {
                mT__52(); 

                }
                break;
            case 41 :
                // org/openprovenance/prov/asn/Sample.g:1:250: T__53
                {
                mT__53(); 

                }
                break;
            case 42 :
                // org/openprovenance/prov/asn/Sample.g:1:256: T__54
                {
                mT__54(); 

                }
                break;
            case 43 :
                // org/openprovenance/prov/asn/Sample.g:1:262: T__55
                {
                mT__55(); 

                }
                break;
            case 44 :
                // org/openprovenance/prov/asn/Sample.g:1:268: T__56
                {
                mT__56(); 

                }
                break;
            case 45 :
                // org/openprovenance/prov/asn/Sample.g:1:274: T__57
                {
                mT__57(); 

                }
                break;
            case 46 :
                // org/openprovenance/prov/asn/Sample.g:1:280: T__58
                {
                mT__58(); 

                }
                break;
            case 47 :
                // org/openprovenance/prov/asn/Sample.g:1:286: T__59
                {
                mT__59(); 

                }
                break;
            case 48 :
                // org/openprovenance/prov/asn/Sample.g:1:292: T__60
                {
                mT__60(); 

                }
                break;
            case 49 :
                // org/openprovenance/prov/asn/Sample.g:1:298: MULTILINE_COMMENT
                {
                mMULTILINE_COMMENT(); 

                }
                break;
            case 50 :
                // org/openprovenance/prov/asn/Sample.g:1:316: STRING_LITERAL
                {
                mSTRING_LITERAL(); 

                }
                break;
            case 51 :
                // org/openprovenance/prov/asn/Sample.g:1:331: CHAR_LITERAL
                {
                mCHAR_LITERAL(); 

                }
                break;
            case 52 :
                // org/openprovenance/prov/asn/Sample.g:1:344: INTEGER
                {
                mINTEGER(); 

                }
                break;
            case 53 :
                // org/openprovenance/prov/asn/Sample.g:1:352: IDENT
                {
                mIDENT(); 

                }
                break;
            case 54 :
                // org/openprovenance/prov/asn/Sample.g:1:358: WS
                {
                mWS(); 

                }
                break;
            case 55 :
                // org/openprovenance/prov/asn/Sample.g:1:361: COMMENT
                {
                mCOMMENT(); 

                }
                break;

        }

    }


    protected DFA7 dfa7 = new DFA7(this);
    static final String DFA7_eotS =
        "\1\uffff\1\46\1\uffff\2\46\1\56\1\46\1\61\1\uffff\1\46\1\uffff\6"+
        "\46\2\uffff\2\46\1\77\1\101\2\uffff\5\46\3\uffff\1\112\1\46\5\uffff"+
        "\5\46\2\uffff\1\46\2\uffff\11\46\1\133\1\134\1\46\4\uffff\1\137"+
        "\4\46\4\uffff\3\46\1\151\3\46\1\156\7\46\1\166\2\uffff\2\46\1\uffff"+
        "\4\46\1\175\1\176\3\46\1\uffff\1\46\1\u0083\1\u0084\1\46\1\uffff"+
        "\3\46\1\u0089\1\u008a\1\u008b\1\46\1\uffff\2\46\1\u008f\1\46\1\u0091"+
        "\1\46\2\uffff\2\46\1\u0095\1\u0096\2\uffff\4\46\3\uffff\1\u009b"+
        "\2\46\1\uffff\1\u009e\1\uffff\3\46\2\uffff\3\46\1\u00a5\1\uffff"+
        "\1\u00a6\1\u00a7\1\uffff\1\46\1\u00a9\2\46\1\u00ac\1\u00ad\3\uffff"+
        "\1\46\1\uffff\1\46\1\u00b0\2\uffff\1\u00b1\1\u00b2\3\uffff";
    static final String DFA7_eofS =
        "\u00b3\uffff";
    static final String DFA7_minS =
        "\1\11\1\162\1\uffff\1\145\1\154\1\56\1\157\1\75\1\uffff\1\141\1"+
        "\uffff\1\156\1\157\1\164\2\150\1\156\2\uffff\1\146\1\145\2\75\2"+
        "\uffff\1\146\1\150\1\157\1\165\1\157\3\uffff\1\52\1\157\5\uffff"+
        "\1\157\1\147\1\144\1\163\1\151\2\uffff\1\156\2\uffff\1\162\1\164"+
        "\1\157\1\162\1\141\1\160\1\145\1\162\1\144\2\60\1\143\4\uffff\1"+
        "\60\1\145\1\157\1\156\1\164\4\uffff\1\144\1\143\1\151\1\60\1\145"+
        "\1\164\1\163\1\60\1\145\1\154\1\151\1\162\1\145\1\156\1\141\1\60"+
        "\2\uffff\1\157\1\165\1\uffff\1\156\1\154\1\160\1\143\2\60\1\162"+
        "\1\145\1\156\1\uffff\1\146\2\60\1\164\1\uffff\1\147\1\145\1\156"+
        "\3\60\1\171\1\uffff\2\162\1\60\1\145\1\60\1\164\2\uffff\1\141\1"+
        "\144\2\60\2\uffff\1\141\1\145\1\141\1\147\3\uffff\1\60\1\144\1\156"+
        "\1\uffff\1\60\1\uffff\1\151\1\155\1\165\2\uffff\1\156\1\162\1\156"+
        "\1\60\1\uffff\2\60\1\uffff\1\157\1\60\1\162\1\164\2\60\3\uffff\1"+
        "\156\1\uffff\1\145\1\60\2\uffff\2\60\3\uffff";
    static final String DFA7_maxS =
        "\1\172\1\162\1\uffff\1\145\1\170\1\56\1\157\1\75\1\uffff\1\141\1"+
        "\uffff\1\156\1\157\1\164\1\150\1\171\1\162\2\uffff\1\162\1\145\2"+
        "\75\2\uffff\1\146\1\150\1\157\1\165\1\157\3\uffff\1\75\1\157\5\uffff"+
        "\1\157\1\147\1\144\1\163\1\151\2\uffff\1\156\2\uffff\1\162\1\164"+
        "\1\157\1\162\1\141\1\160\1\145\1\162\1\144\2\172\1\164\4\uffff\1"+
        "\172\1\151\1\157\1\156\1\164\4\uffff\1\144\1\147\1\151\1\172\1\151"+
        "\1\164\1\163\1\172\1\145\1\154\1\151\1\162\1\145\1\156\1\141\1\172"+
        "\2\uffff\1\157\1\165\1\uffff\1\156\1\154\1\160\1\143\2\172\1\162"+
        "\1\145\1\156\1\uffff\1\146\2\172\1\164\1\uffff\1\147\1\145\1\156"+
        "\3\172\1\171\1\uffff\2\162\1\172\1\145\1\172\1\164\2\uffff\1\141"+
        "\1\144\2\172\2\uffff\1\141\1\145\1\141\1\147\3\uffff\1\172\1\144"+
        "\1\156\1\uffff\1\172\1\uffff\1\151\1\155\1\165\2\uffff\1\156\1\162"+
        "\1\156\1\172\1\uffff\2\172\1\uffff\1\157\1\172\1\162\1\164\2\172"+
        "\3\uffff\1\156\1\uffff\1\145\1\172\2\uffff\2\172\3\uffff";
    static final String DFA7_acceptS =
        "\2\uffff\1\2\5\uffff\1\11\1\uffff\1\13\6\uffff\1\22\1\24\4\uffff"+
        "\1\31\1\32\5\uffff\1\47\1\50\1\51\2\uffff\1\62\1\63\1\64\1\65\1"+
        "\66\5\uffff\1\23\1\5\1\uffff\1\10\1\7\14\uffff\1\55\1\27\1\56\1"+
        "\30\5\uffff\1\54\1\61\1\67\1\52\20\uffff\1\25\1\60\2\uffff\1\33"+
        "\11\uffff\1\4\4\uffff\1\12\7\uffff\1\57\6\uffff\1\46\1\53\4\uffff"+
        "\1\36\1\37\4\uffff\1\17\1\20\1\34\3\uffff\1\40\1\uffff\1\42\3\uffff"+
        "\1\3\1\35\4\uffff\1\21\2\uffff\1\41\6\uffff\1\16\1\26\1\43\1\uffff"+
        "\1\1\2\uffff\1\14\1\15\2\uffff\1\6\1\45\1\44";
    static final String DFA7_specialS =
        "\u00b3\uffff}>";
    static final String[] DFA7_transitionS = {
            "\2\47\1\uffff\2\47\22\uffff\1\47\1\uffff\1\43\4\uffff\1\44\1"+
            "\27\1\30\1\40\1\36\1\12\1\37\1\5\1\41\12\45\1\7\1\10\1\25\1"+
            "\2\1\26\2\uffff\1\46\1\14\1\16\5\46\1\13\11\46\1\15\7\46\1\21"+
            "\1\uffff\1\22\3\uffff\1\20\1\3\1\6\1\46\1\4\1\34\2\46\1\31\2"+
            "\46\1\33\1\42\1\35\1\23\1\1\1\46\1\24\1\46\1\17\1\46\1\11\1"+
            "\32\3\46",
            "\1\50",
            "",
            "\1\51",
            "\1\53\1\uffff\1\52\11\uffff\1\54",
            "\1\55",
            "\1\57",
            "\1\60",
            "",
            "\1\62",
            "",
            "\1\63",
            "\1\64",
            "\1\65",
            "\1\66",
            "\1\70\20\uffff\1\67",
            "\1\72\3\uffff\1\71",
            "",
            "",
            "\1\73\13\uffff\1\74",
            "\1\75",
            "\1\76",
            "\1\100",
            "",
            "",
            "\1\102",
            "\1\103",
            "\1\104",
            "\1\105",
            "\1\106",
            "",
            "",
            "",
            "\1\110\4\uffff\1\111\15\uffff\1\107",
            "\1\113",
            "",
            "",
            "",
            "",
            "",
            "\1\114",
            "\1\115",
            "\1\116",
            "\1\117",
            "\1\120",
            "",
            "",
            "\1\121",
            "",
            "",
            "\1\122",
            "\1\123",
            "\1\124",
            "\1\125",
            "\1\126",
            "\1\127",
            "\1\130",
            "\1\131",
            "\1\132",
            "\12\46\7\uffff\32\46\6\uffff\32\46",
            "\12\46\7\uffff\32\46\6\uffff\32\46",
            "\1\135\20\uffff\1\136",
            "",
            "",
            "",
            "",
            "\12\46\7\uffff\32\46\6\uffff\32\46",
            "\1\140\3\uffff\1\141",
            "\1\142",
            "\1\143",
            "\1\144",
            "",
            "",
            "",
            "",
            "\1\145",
            "\1\147\3\uffff\1\146",
            "\1\150",
            "\12\46\7\uffff\32\46\6\uffff\32\46",
            "\1\153\3\uffff\1\152",
            "\1\154",
            "\1\155",
            "\12\46\7\uffff\32\46\6\uffff\32\46",
            "\1\157",
            "\1\160",
            "\1\161",
            "\1\162",
            "\1\163",
            "\1\164",
            "\1\165",
            "\12\46\7\uffff\32\46\6\uffff\32\46",
            "",
            "",
            "\1\167",
            "\1\170",
            "",
            "\1\171",
            "\1\172",
            "\1\173",
            "\1\174",
            "\12\46\7\uffff\32\46\6\uffff\32\46",
            "\12\46\7\uffff\32\46\6\uffff\32\46",
            "\1\177",
            "\1\u0080",
            "\1\u0081",
            "",
            "\1\u0082",
            "\12\46\7\uffff\32\46\6\uffff\32\46",
            "\12\46\7\uffff\32\46\6\uffff\32\46",
            "\1\u0085",
            "",
            "\1\u0086",
            "\1\u0087",
            "\1\u0088",
            "\12\46\7\uffff\32\46\6\uffff\32\46",
            "\12\46\7\uffff\32\46\6\uffff\32\46",
            "\12\46\7\uffff\32\46\6\uffff\32\46",
            "\1\u008c",
            "",
            "\1\u008d",
            "\1\u008e",
            "\12\46\7\uffff\32\46\6\uffff\32\46",
            "\1\u0090",
            "\12\46\7\uffff\32\46\6\uffff\32\46",
            "\1\u0092",
            "",
            "",
            "\1\u0093",
            "\1\u0094",
            "\12\46\7\uffff\32\46\6\uffff\32\46",
            "\12\46\7\uffff\32\46\6\uffff\32\46",
            "",
            "",
            "\1\u0097",
            "\1\u0098",
            "\1\u0099",
            "\1\u009a",
            "",
            "",
            "",
            "\12\46\7\uffff\32\46\6\uffff\32\46",
            "\1\u009c",
            "\1\u009d",
            "",
            "\12\46\7\uffff\32\46\6\uffff\32\46",
            "",
            "\1\u009f",
            "\1\u00a0",
            "\1\u00a1",
            "",
            "",
            "\1\u00a2",
            "\1\u00a3",
            "\1\u00a4",
            "\12\46\7\uffff\32\46\6\uffff\32\46",
            "",
            "\12\46\7\uffff\32\46\6\uffff\32\46",
            "\12\46\7\uffff\32\46\6\uffff\32\46",
            "",
            "\1\u00a8",
            "\12\46\7\uffff\32\46\6\uffff\32\46",
            "\1\u00aa",
            "\1\u00ab",
            "\12\46\7\uffff\32\46\6\uffff\32\46",
            "\12\46\7\uffff\32\46\6\uffff\32\46",
            "",
            "",
            "",
            "\1\u00ae",
            "",
            "\1\u00af",
            "\12\46\7\uffff\32\46\6\uffff\32\46",
            "",
            "",
            "\12\46\7\uffff\32\46\6\uffff\32\46",
            "\12\46\7\uffff\32\46\6\uffff\32\46",
            "",
            "",
            ""
    };

    static final short[] DFA7_eot = DFA.unpackEncodedString(DFA7_eotS);
    static final short[] DFA7_eof = DFA.unpackEncodedString(DFA7_eofS);
    static final char[] DFA7_min = DFA.unpackEncodedStringToUnsignedChars(DFA7_minS);
    static final char[] DFA7_max = DFA.unpackEncodedStringToUnsignedChars(DFA7_maxS);
    static final short[] DFA7_accept = DFA.unpackEncodedString(DFA7_acceptS);
    static final short[] DFA7_special = DFA.unpackEncodedString(DFA7_specialS);
    static final short[][] DFA7_transition;

    static {
        int numStates = DFA7_transitionS.length;
        DFA7_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA7_transition[i] = DFA.unpackEncodedString(DFA7_transitionS[i]);
        }
    }

    class DFA7 extends DFA {

        public DFA7(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 7;
            this.eot = DFA7_eot;
            this.eof = DFA7_eof;
            this.min = DFA7_min;
            this.max = DFA7_max;
            this.accept = DFA7_accept;
            this.special = DFA7_special;
            this.transition = DFA7_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( T__13 | T__14 | T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | T__26 | T__27 | T__28 | T__29 | T__30 | T__31 | T__32 | T__33 | T__34 | T__35 | T__36 | T__37 | T__38 | T__39 | T__40 | T__41 | T__42 | T__43 | T__44 | T__45 | T__46 | T__47 | T__48 | T__49 | T__50 | T__51 | T__52 | T__53 | T__54 | T__55 | T__56 | T__57 | T__58 | T__59 | T__60 | MULTILINE_COMMENT | STRING_LITERAL | CHAR_LITERAL | INTEGER | IDENT | WS | COMMENT );";
        }
    }
 

}