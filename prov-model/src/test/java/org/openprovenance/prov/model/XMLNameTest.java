package org.openprovenance.prov.model;

import junit.framework.TestCase;

public class XMLNameTest extends TestCase { 
    public XMLNameTest(String name) {
	super(name);
    }
    
    QualifiedNameUtils u=new QualifiedNameUtils();
    
    boolean doEscape(String in, String out) {
	String val=u.escapeToXsdLocalName(in);
	
	System.err.println("Escape " + in + " " + val);
	assertTrue(u.is_NC_Name(out));
	return val.equals(out);
    }
    boolean doUnescape(String out, String in) {
	String val=u.unescapeFromXsdLocalName(in);
	System.err.println("Unescape " + in + " " + val);
	return val.equals(out);
    }
    boolean doRT1(String in) {
	String val=u.unescapeFromXsdLocalName(u.escapeToXsdLocalName(in));
	System.err.println("RT1 " + in + " " + val);
	return val.equals(in);
    }
    boolean doRT2(String in) {
	String val=u.escapeToXsdLocalName(u.unescapeFromXsdLocalName(in));
	System.err.println("RT2 " + in + " " + val);
	return val.equals(in);
    }
        
    public void testEscape1 () {
	assertTrue(doEscape("abc","abc"));
	assertTrue(doEscape("abc01","abc01"));
	assertTrue(doEscape("01","_01"));
	assertTrue(doEscape("","_"));
	assertTrue(doEscape("_","___"));
	assertTrue(doEscape("a@b","a_40b"));
	assertTrue(doEscape("a~b","a_7Eb"));
	assertTrue(doEscape("a&b","a_26b"));
	assertTrue(doEscape("a+b","a_2Bb"));
	assertTrue(doEscape("a*b","a_2Ab"));
	assertTrue(doEscape("a#b","a_23b"));
	assertTrue(doEscape("a$b","a_24b"));
	assertTrue(doEscape("a!b","a_21b"));

	assertTrue(doEscape("a01bc","a01bc"));
	assertTrue(doEscape("a01/bc","a01_2Fbc"));
	assertTrue(doEscape("a01b\\c","a01b_5Cc"));
	assertTrue(doEscape("a01b=c","a01b_3Dc"));
	assertTrue(doEscape("a01b'c","a01b_27c"));
	assertTrue(doEscape("a01b(c","a01b_28c"));
	assertTrue(doEscape("a01b)c","a01b_29c"));
	assertTrue(doEscape("a01b,c","a01b_2Cc"));
	assertTrue(doEscape("a01b_c","a01b__c"));
	assertTrue(doEscape("a01b:c","a01b_3Ac"));
	assertTrue(doEscape("a01b;c","a01b_3Bc"));
	assertTrue(doEscape("a01b[c","a01b_5Bc"));
	assertTrue(doEscape("a01b]c","a01b_5Dc"));
	assertTrue(doEscape("a01b.c","a01b.c"));
	assertTrue(doEscape("a01bc.","a01bc."));

	assertTrue(doEscape("='(),_:;[].@~","__3D_27_28_29_2C___3A_3B_5B_5D._40_7E"));
	assertTrue(doEscape("?a\\=b","__3Fa_5C_3Db"));
	assertTrue(doEscape("55348dff-4fcc-4ac2-ab56-641798c64400","_55348dff-4fcc-4ac2-ab56-641798c64400"));

	assertTrue(doEscape("À-ÖØ-öø-˿Ͱͽ","À-ÖØ-öø-˿Ͱͽ"));


   }

    public void testUnescape1 () {
	assertTrue(doUnescape("abc","abc"));
	assertTrue(doUnescape("abc01","abc01"));
	assertTrue(doUnescape("01","_01"));
	assertTrue(doUnescape("","_"));
	assertTrue(doUnescape("_","___"));
	assertTrue(doUnescape("a@b","a_40b"));
	assertTrue(doUnescape("a~b","a_7Eb"));
	assertTrue(doUnescape("a&b","a_26b"));
	assertTrue(doUnescape("a+b","a_2Bb"));
	assertTrue(doUnescape("a*b","a_2Ab"));
	assertTrue(doUnescape("a#b","a_23b"));
	assertTrue(doUnescape("a$b","a_24b"));
	assertTrue(doUnescape("a!b","a_21b"));

	assertTrue(doUnescape("a01bc","a01bc"));
	assertTrue(doUnescape("a01/bc","a01_2Fbc"));
	assertTrue(doUnescape("a01b\\c","a01b_5Cc"));
	assertTrue(doUnescape("a01b=c","a01b_3Dc"));
	assertTrue(doUnescape("a01b'c","a01b_27c"));
	assertTrue(doUnescape("a01b(c","a01b_28c"));
	assertTrue(doUnescape("a01b)c","a01b_29c"));
	assertTrue(doUnescape("a01b,c","a01b_2Cc"));
	assertTrue(doUnescape("a01b_c","a01b__c"));
	assertTrue(doUnescape("a01b:c","a01b_3Ac"));
	assertTrue(doUnescape("a01b;c","a01b_3Bc"));
	assertTrue(doUnescape("a01b[c","a01b_5Bc"));
	assertTrue(doUnescape("a01b]c","a01b_5Dc"));
	assertTrue(doUnescape("a01b.c","a01b.c"));
	assertTrue(doUnescape("='(),_:;[].@~","__3D_27_28_29_2C___3A_3B_5B_5D._40_7E"));
	assertTrue(doUnescape("?a\\=b","__3Fa_5C_3Db"));
	assertTrue(doUnescape("55348dff-4fcc-4ac2-ab56-641798c64400","_55348dff-4fcc-4ac2-ab56-641798c64400"));
	assertTrue(doUnescape("À-ÖØ-öø-˿Ͱͽ","À-ÖØ-öø-˿Ͱͽ"));

	
   }

    
    public void testRoundTripFromUnescaped1 () {
	assertTrue(doRT1("abc"));
	assertTrue(doRT1("abc01"));
	assertTrue(doRT1("01"));
	assertTrue(doRT1(""));
	assertTrue(doRT1("_"));
	assertTrue(doRT1("a@b"));
	assertTrue(doRT1("a~b"));
	assertTrue(doRT1("a&b"));
	assertTrue(doRT1("a+b"));
	assertTrue(doRT1("a*b"));
	assertTrue(doRT1("a#b"));
	assertTrue(doRT1("a$b"));
	assertTrue(doRT1("a!b"));

	assertTrue(doRT1("a01bc"));
	assertTrue(doRT1("a01/bc"));
	assertTrue(doRT1("a01b\\c"));
	assertTrue(doRT1("a01b=c"));
	assertTrue(doRT1("a01b'c"));
	assertTrue(doRT1("a01b(c"));
	assertTrue(doRT1("a01b)c"));
	assertTrue(doRT1("a01b,c"));
	assertTrue(doRT1("a01b_c"));
	assertTrue(doRT1("a01b:c"));
	assertTrue(doRT1("a01b;c"));
	assertTrue(doRT1("a01b[c"));
	assertTrue(doRT1("a01b]c"));
	assertTrue(doRT1("a01b.c"));
	assertTrue(doRT1("='(),_:;[].@~"));
	assertTrue(doRT1("?a\\=b"));
	assertTrue(doRT1("55348dff-4fcc-4ac2-ab56-641798c64400"));

	assertTrue(doRT1("À-ÖØ-öø-˿Ͱͽ"));

   }

    public void testRoundTripFromUnescaped2 () {
	assertTrue(doRT2("abc"));
	assertTrue(doRT2("abc01"));
	assertTrue(doRT2("_0001"));
	assertTrue(doRT2("_"));
	assertTrue(doRT2("___"));
	assertTrue(doRT2("a_40b"));
	assertTrue(doRT2("a_7Eb"));
	assertTrue(doRT2("a_26b"));
	assertTrue(doRT2("a_2Bb"));
	assertTrue(doRT2("a_2Ab"));
	assertTrue(doRT2("a_23b"));
	assertTrue(doRT2("a_24b"));
	assertTrue(doRT2("a_21b"));

	assertTrue(doRT2("a01bc"));
	assertTrue(doRT2("a01_2Fbc"));
	assertTrue(doRT2("a01b_5Cc"));
	assertTrue(doRT2("a01b_3Dc"));
	assertTrue(doRT2("a01b_27c"));
	assertTrue(doRT2("a01b_28c"));
	assertTrue(doRT2("a01b_29c"));
	assertTrue(doRT2("a01b_2Cc"));
	assertTrue(doRT2("a01b__c"));
	assertTrue(doRT2("a01b_3Ac"));
	assertTrue(doRT2("a01b_3Bc"));
	assertTrue(doRT2("a01b_5Bc"));
	assertTrue(doRT2("a01b_5Dc"));
	assertTrue(doRT2("a01b.c"));
	assertTrue(doRT2("_3D_27_28_29_2C___3A_3B_5B_5D._40_7E"));
	assertTrue(doRT2("_3Fa_5C_3Db"));
	assertTrue(doRT2("_55348dff-4fcc-4ac2-ab56-641798c64400"));

	assertTrue(doRT2("À-ÖØ-öø-˿Ͱͽ"));


   }

 
}
