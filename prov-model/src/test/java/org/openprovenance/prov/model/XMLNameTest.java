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
	assertTrue(doEscape("01","-0001"));
	assertTrue(doEscape("","-"));
	assertTrue(doEscape("-","--"));
	assertTrue(doEscape("a@b","a-40b"));
	assertTrue(doEscape("a~b","a-7Eb"));
	assertTrue(doEscape("a&b","a-26b"));
	assertTrue(doEscape("a+b","a-2Bb"));
	assertTrue(doEscape("a*b","a-2Ab"));
	assertTrue(doEscape("a#b","a-23b"));
	assertTrue(doEscape("a$b","a-24b"));
	assertTrue(doEscape("a!b","a-21b"));

	assertTrue(doEscape("a01bc","a01bc"));
	assertTrue(doEscape("a01/bc","a01-2Fbc"));
	assertTrue(doEscape("a01b\\c","a01b-5Cc"));
	assertTrue(doEscape("a01b=c","a01b-3Dc"));
	assertTrue(doEscape("a01b'c","a01b-27c"));
	assertTrue(doEscape("a01b(c","a01b-28c"));
	assertTrue(doEscape("a01b)c","a01b-29c"));
	assertTrue(doEscape("a01b,c","a01b-2Cc"));
	assertTrue(doEscape("a01b-c","a01b--c"));
	assertTrue(doEscape("a01b:c","a01b-3Ac"));
	assertTrue(doEscape("a01b;c","a01b-3Bc"));
	assertTrue(doEscape("a01b[c","a01b-5Bc"));
	assertTrue(doEscape("a01b]c","a01b-5Dc"));
	assertTrue(doEscape("a01b.c","a01b.c"));
	assertTrue(doEscape("='(),-:;[].@~","-3D-27-28-29-2C---3A-3B-5B-5D.-40-7E"));
	assertTrue(doEscape("?a\\=b","-3Fa-5C-3Db"));
	

   }

    public void testUnescape1 () {
	assertTrue(doUnescape("abc","abc"));
	assertTrue(doUnescape("abc01","abc01"));
	assertTrue(doUnescape("01","-0001"));
	assertTrue(doUnescape("","-"));
	assertTrue(doUnescape("-","--"));
	assertTrue(doUnescape("a@b","a-40b"));
	assertTrue(doUnescape("a~b","a-7Eb"));
	assertTrue(doUnescape("a&b","a-26b"));
	assertTrue(doUnescape("a+b","a-2Bb"));
	assertTrue(doUnescape("a*b","a-2Ab"));
	assertTrue(doUnescape("a#b","a-23b"));
	assertTrue(doUnescape("a$b","a-24b"));
	assertTrue(doUnescape("a!b","a-21b"));

	assertTrue(doUnescape("a01bc","a01bc"));
	assertTrue(doUnescape("a01/bc","a01-2Fbc"));
	assertTrue(doUnescape("a01b\\c","a01b-5Cc"));
	assertTrue(doUnescape("a01b=c","a01b-3Dc"));
	assertTrue(doUnescape("a01b'c","a01b-27c"));
	assertTrue(doUnescape("a01b(c","a01b-28c"));
	assertTrue(doUnescape("a01b)c","a01b-29c"));
	assertTrue(doUnescape("a01b,c","a01b-2Cc"));
	assertTrue(doUnescape("a01b-c","a01b--c"));
	assertTrue(doUnescape("a01b:c","a01b-3Ac"));
	assertTrue(doUnescape("a01b;c","a01b-3Bc"));
	assertTrue(doUnescape("a01b[c","a01b-5Bc"));
	assertTrue(doUnescape("a01b]c","a01b-5Dc"));
	assertTrue(doUnescape("a01b.c","a01b.c"));
	assertTrue(doUnescape("='(),-:;[].@~","-3D-27-28-29-2C---3A-3B-5B-5D.-40-7E"));
	assertTrue(doUnescape("?a\\=b","-3Fa-5C-3Db"));
	
   }

    
    public void testRoundTripFromUnescaped1 () {
	assertTrue(doRT1("abc"));
	assertTrue(doRT1("abc01"));
	assertTrue(doRT1("01"));
	assertTrue(doRT1(""));
	assertTrue(doRT1("-"));
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
	assertTrue(doRT1("a01b-c"));
	assertTrue(doRT1("a01b:c"));
	assertTrue(doRT1("a01b;c"));
	assertTrue(doRT1("a01b[c"));
	assertTrue(doRT1("a01b]c"));
	assertTrue(doRT1("a01b.c"));
	assertTrue(doRT1("='(),-:;[].@~"));
	assertTrue(doRT1("?a\\=b"));
	
   }

    public void testRoundTripFromUnescaped2 () {
	assertTrue(doRT2("abc"));
	assertTrue(doRT2("abc01"));
	assertTrue(doRT2("-0001"));
	assertTrue(doRT2("-"));
	assertTrue(doRT2("--"));
	assertTrue(doRT2("a-40b"));
	assertTrue(doRT2("a-7Eb"));
	assertTrue(doRT2("a-26b"));
	assertTrue(doRT2("a-2Bb"));
	assertTrue(doRT2("a-2Ab"));
	assertTrue(doRT2("a-23b"));
	assertTrue(doRT2("a-24b"));
	assertTrue(doRT2("a-21b"));

	assertTrue(doRT2("a01bc"));
	assertTrue(doRT2("a01-2Fbc"));
	assertTrue(doRT2("a01b-5Cc"));
	assertTrue(doRT2("a01b-3Dc"));
	assertTrue(doRT2("a01b-27c"));
	assertTrue(doRT2("a01b-28c"));
	assertTrue(doRT2("a01b-29c"));
	assertTrue(doRT2("a01b-2Cc"));
	assertTrue(doRT2("a01b--c"));
	assertTrue(doRT2("a01b-3Ac"));
	assertTrue(doRT2("a01b-3Bc"));
	assertTrue(doRT2("a01b-5Bc"));
	assertTrue(doRT2("a01b-5Dc"));
	assertTrue(doRT2("a01b.c"));
	assertTrue(doRT2("-3D-27-28-29-2C---3A-3B-5B-5D.-40-7E"));
	assertTrue(doRT2("-3Fa-5C-3Db"));


   }

 
}
