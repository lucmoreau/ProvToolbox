package org.openprovenance.prov.model;

import junit.framework.TestCase;

public class QualifiedNameTest extends TestCase { 
    public QualifiedNameTest(String name) {
	super(name);
    }
    
    QualifiedNameUtils u=new QualifiedNameUtils();
    
    boolean doEscape(String in, String out) {
	String val=u.escapeProvLocalName(in);
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
	assertTrue(doEscape("a01bc","a01bc"));
	assertTrue(doEscape("a01b\\c","a01b\\c"));
	assertTrue(doEscape("a01b=c","a01b\\=c"));
	assertTrue(doEscape("a01b'c","a01b\\'c"));
	assertTrue(doEscape("a01b(c","a01b\\(c"));
	assertTrue(doEscape("a01b)c","a01b\\)c"));
	assertTrue(doEscape("a01b,c","a01b\\,c"));
	assertTrue(doEscape("a01b-c","a01b\\-c"));
	assertTrue(doEscape("a01b:c","a01b\\:c"));
	assertTrue(doEscape("a01b;c","a01b\\;c"));
	assertTrue(doEscape("a01b[c","a01b\\[c"));
	assertTrue(doEscape("a01b]c","a01b\\]c"));
	assertTrue(doEscape("a01b.c","a01b\\.c"));
	assertTrue(doEscape("='(),-:;[].","\\=\\'\\(\\)\\,\\-\\:\\;\\[\\]\\."));
	assertTrue(doEscape("?a\\=b","?a\\\\=b"));
	assertTrue(doEscape("55348dff-4fcc-4ac2-ab56-641798c64400","55348dff\\-4fcc\\-4ac2\\-ab56\\-641798c64400"));


   }

    public void testUnescape1 () {
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
	assertTrue(doUnescape("55348dff-4fcc-4ac2-ab56-641798c64400","55348dff\\-4fcc\\-4ac2\\-ab56\\-641798c64400"));

   }
    
    public void testRoundTripFromUnescaped1 () {
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
	assertTrue(doRT1("?a=b"));
	assertTrue(doRT1("55348dff-4fcc-4ac2-ab56-641798c64400"));


   }

    public void testRoundTripFromEscaped2 () {
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
	assertTrue(doRT2("?a\\=b"));
	assertTrue(doRT2("55348dff\\-4fcc\\-4ac2\\-ab56\\-641798c64400"));

	
   }

 
}
