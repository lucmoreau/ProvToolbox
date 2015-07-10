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
	String val=u.unescapeProvLocalName(in);
	System.err.println("Unescape " + in + " " + val);
	return val.equals(out);
    }
    boolean doRT1(String in) {
	String val=u.unescapeProvLocalName(u.escapeProvLocalName(in));
	System.err.println("RT1 " + in + " " + val);
	return val.equals(in);
    }
    boolean doRT2(String in) {
	String val=u.escapeProvLocalName(u.unescapeProvLocalName(in));
	System.err.println("RT2 " + in + " " + val);
	return val.equals(in);
    }
        
    public void testEscape1 () {
	assertTrue(doEscape("abc","abc"));
	assertTrue(doEscape("abc01","abc01"));
	assertTrue(doEscape("01","-01"));
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

    public void IGN_testUnescape1 () {
	assertTrue(doUnescape("a01bc","a01bc"));
	assertTrue(doUnescape("a01b\\c","a01b\\c"));
	assertTrue(doUnescape("a01b=c","a01b\\=c"));
	assertTrue(doUnescape("a01b'c","a01b\\'c"));
	assertTrue(doUnescape("a01b(c","a01b\\(c"));
	assertTrue(doUnescape("a01b)c","a01b\\)c"));
	assertTrue(doUnescape("a01b,c","a01b\\,c"));
	assertTrue(doUnescape("a01b-c","a01b\\-c"));
	assertTrue(doUnescape("a01b:c","a01b\\:c"));
	assertTrue(doUnescape("a01b;c","a01b\\;c"));
	assertTrue(doUnescape("a01b[c","a01b\\[c"));
	assertTrue(doUnescape("a01b]c","a01b\\]c"));
	assertTrue(doUnescape("a01b.c","a01b\\.c"));
	assertTrue(doUnescape("='(),-:;[].","\\=\\'\\(\\)\\,\\-\\:\\;\\[\\]\\."));
	assertTrue(doUnescape("?a\\=b","?a\\\\=b"));

   }
    
    public void IGNtestRoundTripFromUnescaped1 () {
	assertTrue(doRT1("a01bc"));
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
	assertTrue(doRT1("='(),-:;[]."));
	assertTrue(doRT1("?a\\=b"));

   }

    public void IGNtestRoundTripFromUnescaped2 () {
	assertTrue(doRT2("a01bc"));
	assertTrue(doRT2("a01b\\c"));
	assertTrue(doRT2("a01b\\=c"));
	assertTrue(doRT2("a01b\\'c"));
	assertTrue(doRT2("a01b\\(c"));
	assertTrue(doRT2("a01b\\)c"));
	assertTrue(doRT2("a01b\\,c"));
	assertTrue(doRT2("a01b\\-c"));
	assertTrue(doRT2("a01b\\:c"));
	assertTrue(doRT2("a01b\\;c"));
	assertTrue(doRT2("a01b\\[c"));
	assertTrue(doRT2("a01b\\]c"));
	assertTrue(doRT2("a01b\\.c"));
	assertTrue(doRT2("\\=\\'\\(\\)\\,\\-\\:\\;\\[\\]\\."));
   }

 
}
