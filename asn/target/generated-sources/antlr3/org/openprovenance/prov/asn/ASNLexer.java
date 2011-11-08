// $ANTLR 3.1.3 Mar 17, 2009 19:23:44 org/openprovenance/prov/asn/ASN.g 2011-11-08 13:49:44

  package org.openprovenance.prov.asn;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class ASNLexer extends Lexer {
    public static final int INTEGER=9;
    public static final int WS=10;
    public static final int STRING_LITERAL=5;
    public static final int T__16=16;
    public static final int T__15=15;
    public static final int T__18=18;
    public static final int T__17=17;
    public static final int CHAR_LITERAL=6;
    public static final int T__12=12;
    public static final int T__14=14;
    public static final int LETTER=7;
    public static final int T__13=13;
    public static final int IDENT=4;
    public static final int DIGIT=8;
    public static final int COMMENT=11;
    public static final int EOF=-1;

    // delegates
    // delegators

    public ASNLexer() {;} 
    public ASNLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public ASNLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "org/openprovenance/prov/asn/ASN.g"; }

    // $ANTLR start "T__12"
    public final void mT__12() throws RecognitionException {
        try {
            int _type = T__12;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/ASN.g:11:7: ( 'container' )
            // org/openprovenance/prov/asn/ASN.g:11:9: 'container'
            {
            match("container"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__12"

    // $ANTLR start "T__13"
    public final void mT__13() throws RecognitionException {
        try {
            int _type = T__13;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/openprovenance/prov/asn/ASN.g:12:7: ( '=' )
            // org/openprovenance/prov/asn/ASN.g:12:9: '='
            {
            match('='); 

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
            // org/openprovenance/prov/asn/ASN.g:13:7: ( 'end' )
            // org/openprovenance/prov/asn/ASN.g:13:9: 'end'
            {
            match("end"); 


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
            // org/openprovenance/prov/asn/ASN.g:14:7: ( '.' )
            // org/openprovenance/prov/asn/ASN.g:14:9: '.'
            {
            match('.'); 

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
            // org/openprovenance/prov/asn/ASN.g:15:7: ( 'entity' )
            // org/openprovenance/prov/asn/ASN.g:15:9: 'entity'
            {
            match("entity"); 


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
            // org/openprovenance/prov/asn/ASN.g:16:7: ( '(' )
            // org/openprovenance/prov/asn/ASN.g:16:9: '('
            {
            match('('); 

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
            // org/openprovenance/prov/asn/ASN.g:17:7: ( ')' )
            // org/openprovenance/prov/asn/ASN.g:17:9: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__18"

    // $ANTLR start "STRING_LITERAL"
    public final void mSTRING_LITERAL() throws RecognitionException {
        try {
            int _type = STRING_LITERAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            int c;

            // org/openprovenance/prov/asn/ASN.g:39:2: ( '\"' ( '\"' '\"' | c=~ ( '\"' | '\\r' | '\\n' ) )* '\"' )
            // org/openprovenance/prov/asn/ASN.g:39:4: '\"' ( '\"' '\"' | c=~ ( '\"' | '\\r' | '\\n' ) )* '\"'
            {
            match('\"'); 
             StringBuilder b = new StringBuilder(); 
            // org/openprovenance/prov/asn/ASN.g:41:3: ( '\"' '\"' | c=~ ( '\"' | '\\r' | '\\n' ) )*
            loop1:
            do {
                int alt1=3;
                int LA1_0 = input.LA(1);

                if ( (LA1_0=='\"') ) {
                    int LA1_1 = input.LA(2);

                    if ( (LA1_1=='\"') ) {
                        alt1=1;
                    }


                }
                else if ( ((LA1_0>='\u0000' && LA1_0<='\t')||(LA1_0>='\u000B' && LA1_0<='\f')||(LA1_0>='\u000E' && LA1_0<='!')||(LA1_0>='#' && LA1_0<='\uFFFF')) ) {
                    alt1=2;
                }


                switch (alt1) {
            	case 1 :
            	    // org/openprovenance/prov/asn/ASN.g:41:5: '\"' '\"'
            	    {
            	    match('\"'); 
            	    match('\"'); 
            	     b.appendCodePoint('"');

            	    }
            	    break;
            	case 2 :
            	    // org/openprovenance/prov/asn/ASN.g:42:5: c=~ ( '\"' | '\\r' | '\\n' )
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
            	    break loop1;
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
            // org/openprovenance/prov/asn/ASN.g:49:2: ( '\\'' . '\\'' )
            // org/openprovenance/prov/asn/ASN.g:49:4: '\\'' . '\\''
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
            // org/openprovenance/prov/asn/ASN.g:52:17: ( ( 'a' .. 'z' | 'A' .. 'Z' ) )
            // org/openprovenance/prov/asn/ASN.g:52:19: ( 'a' .. 'z' | 'A' .. 'Z' )
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
            // org/openprovenance/prov/asn/ASN.g:53:16: ( '0' .. '9' )
            // org/openprovenance/prov/asn/ASN.g:53:18: '0' .. '9'
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
            // org/openprovenance/prov/asn/ASN.g:54:9: ( ( DIGIT )+ )
            // org/openprovenance/prov/asn/ASN.g:54:11: ( DIGIT )+
            {
            // org/openprovenance/prov/asn/ASN.g:54:11: ( DIGIT )+
            int cnt2=0;
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0>='0' && LA2_0<='9')) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // org/openprovenance/prov/asn/ASN.g:54:11: DIGIT
            	    {
            	    mDIGIT(); 

            	    }
            	    break;

            	default :
            	    if ( cnt2 >= 1 ) break loop2;
                        EarlyExitException eee =
                            new EarlyExitException(2, input);
                        throw eee;
                }
                cnt2++;
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
            // org/openprovenance/prov/asn/ASN.g:55:7: ( LETTER ( LETTER | DIGIT )* )
            // org/openprovenance/prov/asn/ASN.g:55:9: LETTER ( LETTER | DIGIT )*
            {
            mLETTER(); 
            // org/openprovenance/prov/asn/ASN.g:55:16: ( LETTER | DIGIT )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0>='0' && LA3_0<='9')||(LA3_0>='A' && LA3_0<='Z')||(LA3_0>='a' && LA3_0<='z')) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // org/openprovenance/prov/asn/ASN.g:
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
            	    break loop3;
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
            // org/openprovenance/prov/asn/ASN.g:56:4: ( ( ' ' | '\\t' | '\\n' | '\\r' | '\\f' )+ )
            // org/openprovenance/prov/asn/ASN.g:56:6: ( ' ' | '\\t' | '\\n' | '\\r' | '\\f' )+
            {
            // org/openprovenance/prov/asn/ASN.g:56:6: ( ' ' | '\\t' | '\\n' | '\\r' | '\\f' )+
            int cnt4=0;
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0>='\t' && LA4_0<='\n')||(LA4_0>='\f' && LA4_0<='\r')||LA4_0==' ') ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // org/openprovenance/prov/asn/ASN.g:
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
            	    if ( cnt4 >= 1 ) break loop4;
                        EarlyExitException eee =
                            new EarlyExitException(4, input);
                        throw eee;
                }
                cnt4++;
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
            // org/openprovenance/prov/asn/ASN.g:57:9: ( '//' ( . )* ( '\\n' | '\\r' ) )
            // org/openprovenance/prov/asn/ASN.g:57:11: '//' ( . )* ( '\\n' | '\\r' )
            {
            match("//"); 

            // org/openprovenance/prov/asn/ASN.g:57:16: ( . )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0=='\n'||LA5_0=='\r') ) {
                    alt5=2;
                }
                else if ( ((LA5_0>='\u0000' && LA5_0<='\t')||(LA5_0>='\u000B' && LA5_0<='\f')||(LA5_0>='\u000E' && LA5_0<='\uFFFF')) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // org/openprovenance/prov/asn/ASN.g:57:16: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop5;
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
        // org/openprovenance/prov/asn/ASN.g:1:8: ( T__12 | T__13 | T__14 | T__15 | T__16 | T__17 | T__18 | STRING_LITERAL | CHAR_LITERAL | INTEGER | IDENT | WS | COMMENT )
        int alt6=13;
        alt6 = dfa6.predict(input);
        switch (alt6) {
            case 1 :
                // org/openprovenance/prov/asn/ASN.g:1:10: T__12
                {
                mT__12(); 

                }
                break;
            case 2 :
                // org/openprovenance/prov/asn/ASN.g:1:16: T__13
                {
                mT__13(); 

                }
                break;
            case 3 :
                // org/openprovenance/prov/asn/ASN.g:1:22: T__14
                {
                mT__14(); 

                }
                break;
            case 4 :
                // org/openprovenance/prov/asn/ASN.g:1:28: T__15
                {
                mT__15(); 

                }
                break;
            case 5 :
                // org/openprovenance/prov/asn/ASN.g:1:34: T__16
                {
                mT__16(); 

                }
                break;
            case 6 :
                // org/openprovenance/prov/asn/ASN.g:1:40: T__17
                {
                mT__17(); 

                }
                break;
            case 7 :
                // org/openprovenance/prov/asn/ASN.g:1:46: T__18
                {
                mT__18(); 

                }
                break;
            case 8 :
                // org/openprovenance/prov/asn/ASN.g:1:52: STRING_LITERAL
                {
                mSTRING_LITERAL(); 

                }
                break;
            case 9 :
                // org/openprovenance/prov/asn/ASN.g:1:67: CHAR_LITERAL
                {
                mCHAR_LITERAL(); 

                }
                break;
            case 10 :
                // org/openprovenance/prov/asn/ASN.g:1:80: INTEGER
                {
                mINTEGER(); 

                }
                break;
            case 11 :
                // org/openprovenance/prov/asn/ASN.g:1:88: IDENT
                {
                mIDENT(); 

                }
                break;
            case 12 :
                // org/openprovenance/prov/asn/ASN.g:1:94: WS
                {
                mWS(); 

                }
                break;
            case 13 :
                // org/openprovenance/prov/asn/ASN.g:1:97: COMMENT
                {
                mCOMMENT(); 

                }
                break;

        }

    }


    protected DFA6 dfa6 = new DFA6(this);
    static final String DFA6_eotS =
        "\1\uffff\1\12\1\uffff\1\12\11\uffff\3\12\1\23\2\12\1\uffff\4\12"+
        "\1\32\1\12\1\uffff\1\12\1\35\1\uffff";
    static final String DFA6_eofS =
        "\36\uffff";
    static final String DFA6_minS =
        "\1\11\1\157\1\uffff\1\156\11\uffff\1\156\1\144\1\164\1\60\1\151"+
        "\1\141\1\uffff\1\164\1\151\1\171\1\156\1\60\1\145\1\uffff\1\162"+
        "\1\60\1\uffff";
    static final String DFA6_maxS =
        "\1\172\1\157\1\uffff\1\156\11\uffff\1\156\2\164\1\172\1\151\1\141"+
        "\1\uffff\1\164\1\151\1\171\1\156\1\172\1\145\1\uffff\1\162\1\172"+
        "\1\uffff";
    static final String DFA6_acceptS =
        "\2\uffff\1\2\1\uffff\1\4\1\6\1\7\1\10\1\11\1\12\1\13\1\14\1\15\6"+
        "\uffff\1\3\6\uffff\1\5\2\uffff\1\1";
    static final String DFA6_specialS =
        "\36\uffff}>";
    static final String[] DFA6_transitionS = {
            "\2\13\1\uffff\2\13\22\uffff\1\13\1\uffff\1\7\4\uffff\1\10\1"+
            "\5\1\6\4\uffff\1\4\1\14\12\11\3\uffff\1\2\3\uffff\32\12\6\uffff"+
            "\2\12\1\1\1\12\1\3\25\12",
            "\1\15",
            "",
            "\1\16",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\17",
            "\1\20\17\uffff\1\21",
            "\1\22",
            "\12\12\7\uffff\32\12\6\uffff\32\12",
            "\1\24",
            "\1\25",
            "",
            "\1\26",
            "\1\27",
            "\1\30",
            "\1\31",
            "\12\12\7\uffff\32\12\6\uffff\32\12",
            "\1\33",
            "",
            "\1\34",
            "\12\12\7\uffff\32\12\6\uffff\32\12",
            ""
    };

    static final short[] DFA6_eot = DFA.unpackEncodedString(DFA6_eotS);
    static final short[] DFA6_eof = DFA.unpackEncodedString(DFA6_eofS);
    static final char[] DFA6_min = DFA.unpackEncodedStringToUnsignedChars(DFA6_minS);
    static final char[] DFA6_max = DFA.unpackEncodedStringToUnsignedChars(DFA6_maxS);
    static final short[] DFA6_accept = DFA.unpackEncodedString(DFA6_acceptS);
    static final short[] DFA6_special = DFA.unpackEncodedString(DFA6_specialS);
    static final short[][] DFA6_transition;

    static {
        int numStates = DFA6_transitionS.length;
        DFA6_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA6_transition[i] = DFA.unpackEncodedString(DFA6_transitionS[i]);
        }
    }

    class DFA6 extends DFA {

        public DFA6(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 6;
            this.eot = DFA6_eot;
            this.eof = DFA6_eof;
            this.min = DFA6_min;
            this.max = DFA6_max;
            this.accept = DFA6_accept;
            this.special = DFA6_special;
            this.transition = DFA6_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( T__12 | T__13 | T__14 | T__15 | T__16 | T__17 | T__18 | STRING_LITERAL | CHAR_LITERAL | INTEGER | IDENT | WS | COMMENT );";
        }
    }
 

}