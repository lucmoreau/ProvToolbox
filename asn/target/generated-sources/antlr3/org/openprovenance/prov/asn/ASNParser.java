// $ANTLR 3.1.3 Mar 17, 2009 19:23:44 org/openprovenance/prov/asn/ASN.g 2011-11-08 13:49:43

  package org.openprovenance.prov.asn;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

/*******************************************************************************
 * Copyright (c) 2009 Scott Stanchfield
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
public class ASNParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "IDENT", "STRING_LITERAL", "CHAR_LITERAL", "LETTER", "DIGIT", "INTEGER", "WS", "COMMENT", "'container'", "'='", "'end'", "'.'", "'entity'", "'('", "')'"
    };
    public static final int INTEGER=9;
    public static final int WS=10;
    public static final int STRING_LITERAL=5;
    public static final int T__16=16;
    public static final int T__15=15;
    public static final int T__18=18;
    public static final int CHAR_LITERAL=6;
    public static final int T__17=17;
    public static final int T__12=12;
    public static final int LETTER=7;
    public static final int T__14=14;
    public static final int T__13=13;
    public static final int IDENT=4;
    public static final int DIGIT=8;
    public static final int COMMENT=11;
    public static final int EOF=-1;

    // delegates
    // delegators


        public ASNParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public ASNParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return ASNParser.tokenNames; }
    public String getGrammarFileName() { return "org/openprovenance/prov/asn/ASN.g"; }



    // $ANTLR start "container"
    // org/openprovenance/prov/asn/ASN.g:22:1: container : 'container' IDENT '=' ( record )* 'end' IDENT '.' ;
    public final void container() throws RecognitionException {
        try {
            // org/openprovenance/prov/asn/ASN.g:23:2: ( 'container' IDENT '=' ( record )* 'end' IDENT '.' )
            // org/openprovenance/prov/asn/ASN.g:23:4: 'container' IDENT '=' ( record )* 'end' IDENT '.'
            {
            match(input,12,FOLLOW_12_in_container42); 
            match(input,IDENT,FOLLOW_IDENT_in_container44); 
            match(input,13,FOLLOW_13_in_container46); 
            // org/openprovenance/prov/asn/ASN.g:24:3: ( record )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==16) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // org/openprovenance/prov/asn/ASN.g:24:3: record
            	    {
            	    pushFollow(FOLLOW_record_in_container50);
            	    record();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

            match(input,14,FOLLOW_14_in_container55); 
            match(input,IDENT,FOLLOW_IDENT_in_container57); 
            match(input,15,FOLLOW_15_in_container59); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "container"


    // $ANTLR start "record"
    // org/openprovenance/prov/asn/ASN.g:28:1: record : entityRecord ;
    public final void record() throws RecognitionException {
        try {
            // org/openprovenance/prov/asn/ASN.g:29:2: ( entityRecord )
            // org/openprovenance/prov/asn/ASN.g:29:4: entityRecord
            {
            pushFollow(FOLLOW_entityRecord_in_record70);
            entityRecord();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "record"


    // $ANTLR start "entityRecord"
    // org/openprovenance/prov/asn/ASN.g:32:1: entityRecord : 'entity' '(' ')' ;
    public final void entityRecord() throws RecognitionException {
        try {
            // org/openprovenance/prov/asn/ASN.g:33:2: ( 'entity' '(' ')' )
            // org/openprovenance/prov/asn/ASN.g:33:4: 'entity' '(' ')'
            {
            match(input,16,FOLLOW_16_in_entityRecord81); 
            match(input,17,FOLLOW_17_in_entityRecord83); 
            match(input,18,FOLLOW_18_in_entityRecord87); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entityRecord"

    // Delegated rules


 

    public static final BitSet FOLLOW_12_in_container42 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENT_in_container44 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_13_in_container46 = new BitSet(new long[]{0x0000000000014000L});
    public static final BitSet FOLLOW_record_in_container50 = new BitSet(new long[]{0x0000000000014000L});
    public static final BitSet FOLLOW_14_in_container55 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENT_in_container57 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_15_in_container59 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_entityRecord_in_record70 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_16_in_entityRecord81 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_17_in_entityRecord83 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_entityRecord87 = new BitSet(new long[]{0x0000000000000002L});

}