package org.openprovenance.prov.model.test;

import junit.framework.TestCase;
import org.openprovenance.prov.model.QualifiedNameUtils;

public class QualifiedNameTest extends TestCase {
	public QualifiedNameTest(String name) {
		super(name);
	}

	QualifiedNameUtils u=new QualifiedNameUtils();

	boolean doEscape(String in, String out) {
		String val=u.escapeProvLocalName(in);
		System.err.println("Escape " + in + " " + val);
		assertTrue(u.patternExactMatch(out));
		return val.equals(out);
	}
	boolean doUnescape(String out, String in) {
		String val=u.unescapeProvLocalName(in);
		System.err.println("Unescape " + in + " " + val);
		assertTrue(u.patternExactMatch(in));
		return val.equals(out);
	}
	boolean doEscapeUnicode(String in, String out) {
		String val=u.escapeUnicode(in);
		System.err.println("Escape Unicode " + in + " " + val);
		return val.equals(out);
	}
	boolean doUnescapeUnicode(String out, String in) {
		String val=u.unescapeUnicode(in);
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
		assertTrue(u.patternExactMatch(in));
		return val.equals(in);
	}

	public void testEscape1 () {
		assertTrue(doEscape("a01bc","a01bc"));
//	assertTrue(doEscape("a01b\\c","a01b\\c"));
		assertTrue(doEscape("a01b=c","a01b\\=c"));
		assertTrue(doEscape("a01b'c","a01b\\'c"));
		assertTrue(doEscape("a01b(c","a01b\\(c"));
		assertTrue(doEscape("a01b)c","a01b\\)c"));
		assertTrue(doEscape("a01b,c","a01b\\,c"));
		assertTrue(doEscape("a01b-c","a01b-c"));
		assertTrue(doEscape("a01b:c","a01b\\:c"));
		assertTrue(doEscape("a01b;c","a01b\\;c"));
		assertTrue(doEscape("a01b[c","a01b\\[c"));
		assertTrue(doEscape("a01b]c","a01b\\]c"));
		assertTrue(doEscape("a01b.c","a01b.c"));
		assertTrue(doEscape("a01bc.","a01bc\\."));
		assertTrue(doEscape("-","\\-"));

		//assertTrue(doEscape("a01b<.>c","a01b%3C\\.%3Ec"));

		assertTrue(doEscape("='(),-:;[].a","\\=\\'\\(\\)\\,-\\:\\;\\[\\].a"));
		assertTrue(doEscape("?a=b","?a\\=b"));
		assertTrue(doEscape("?user=ima3","?user\\=ima3"));
		assertTrue(doEscape("55348dff-4fcc-4ac2-ab56-641798c64400","55348dff-4fcc-4ac2-ab56-641798c64400"));
		assertTrue(doEscape("news/world-asia-17507976","news/world-asia-17507976"));
		assertTrue(doEscape("À-ÖØ-öø-˿Ͱͽ","À-ÖØ-öø-˿Ͱͽ"));

	}


	public void testUnescape1 () {
		assertTrue(doUnescape("a01bc","a01bc"));
//	assertTrue(doUnescape("a01b\\c","a01b\\c"));
		assertTrue(doUnescape("a01b=c","a01b\\=c"));
		assertTrue(doUnescape("a01b'c","a01b\\'c"));
		assertTrue(doUnescape("a01b(c","a01b\\(c"));
		assertTrue(doUnescape("a01b)c","a01b\\)c"));
		assertTrue(doUnescape("a01b,c","a01b\\,c"));
		assertTrue(doUnescape("a01b-c","a01b-c"));
		assertTrue(doUnescape("a01b:c","a01b\\:c"));
		assertTrue(doUnescape("a01b;c","a01b\\;c"));
		assertTrue(doUnescape("a01b[c","a01b\\[c"));
		assertTrue(doUnescape("a01b]c","a01b\\]c"));
		assertTrue(doUnescape("a01b.c","a01b\\.c"));
		assertTrue(doUnescape("a01b.c","a01b.c"));
		assertTrue(doUnescape("a01bc.","a01bc\\."));

		//assertTrue(doUnescape("a01b<.>c","a01b%3C\\.%3Ec"));

		assertTrue(doUnescape("='(),-:;[].","\\=\\'\\(\\)\\,-\\:\\;\\[\\]\\."));
		assertTrue(doUnescape("?a=b","?a\\=b"));
		assertTrue(doUnescape("55348dff-4fcc-4ac2-ab56-641798c64400","55348dff-4fcc-4ac2-ab56-641798c64400"));
		assertTrue(doUnescape("À-ÖØ-öø-˿Ͱͽ","À-ÖØ-öø-˿Ͱͽ"));

	}

	public void testRoundTripFromUnescaped () {
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
		assertTrue(doRT1(".a01bc"));
		assertTrue(doRT1("-"));

		assertTrue(doRT1("='(),-:;[]."));
		assertTrue(doRT1("?a=b"));
		//assertTrue(doRT1("a01b<.>c"));
		assertTrue(doRT1("55348dff-4fcc-4ac2-ab56-641798c64400"));
		assertTrue(doRT1("news/world-asia1-17507976"));

		assertTrue(doRT1("À-ÖØ-öø-˿Ͱͽ"));

		assertTrue(doRT1("application/6/subject/byURI/smartshare%2Fdirk/"));


	}

	public void testRoundTripFromEscaped () {
		assertTrue(doRT2("a01bc"));
		assertTrue(doRT2("a01b\\=c"));
		assertTrue(doRT2("a01b\\'c"));
		assertTrue(doRT2("a01b\\(c"));
		assertTrue(doRT2("a01b\\)c"));
		assertTrue(doRT2("a01b\\,c"));
		assertTrue(doRT2("a01b-c"));
		assertTrue(doRT2("a01b\\:c"));
		assertTrue(doRT2("a01b\\;c"));
		assertTrue(doRT2("a01b\\[c"));
		assertTrue(doRT2("a01b\\]c"));
		assertTrue(doRT2("a01b.c"));

		//assertTrue(doRT2("a01b%3C\\.%3Ec"));

		assertTrue(doRT2("\\=\\'\\(\\)\\,-\\:\\;\\[\\].a"));
		assertTrue(doRT2("?a\\=b"));
		assertTrue(doRT2("55348dff-4fcc-4ac2-ab56-641798c64400"));

		assertTrue(doRT2("foo?a\\=1"));
		assertTrue(doRT2("\\-"));
		assertTrue(doRT2("?fred\\=fish20soup"));
		assertTrue(doRT2("?fred\\=fish%20soup"));
		assertTrue(doRT2("news/world-asia-17507976"));

		assertTrue(doRT2("À-ÖØ-öø-˿Ͱͽ"));


	}


	public void testPattern1() {
		assertTrue(u.patternExactMatch("/@~&+*?#$!"));
		assertTrue(u.patternExactMatch("ab"));
		assertTrue(u.patternExactMatch("01"));
		assertFalse(u.patternExactMatch("."));
		assertFalse(u.patternExactMatch("01."));
		assertFalse(u.patternExactMatch("01[]b"));
		assertTrue(u.patternExactMatch("abc\\[\\]b"));
		assertTrue(u.patternExactMatch("_"));
		assertTrue(u.patternExactMatch("ab_"));
		assertTrue(u.patternExactMatch("ab___"));
		//assertTrue(u.patternExactMatch("c:ab_"));
		//assertFalse(u.patternExactMatch("ab_:c"));

	}


	public void testEscapeUnicode1 () {
		assertTrue(doEscapeUnicode("À-ÖØ-öø-˿Ͱͽ","À-ÖØ-öø-\\u02FF\\u0370\\u037D"));
	}
	public void testUnescapeUnicode1 () {
		assertTrue(doUnescapeUnicode("À-ÖØ-öø-˿Ͱͽ","À-ÖØ-öø-\\u02FF\\u0370\\u037D"));
	}

}
