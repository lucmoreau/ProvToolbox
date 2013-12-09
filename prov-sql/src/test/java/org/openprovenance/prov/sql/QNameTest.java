package org.openprovenance.prov.sql;


import junit.framework.TestCase;

public class QNameTest extends TestCase {
	
	public QNameTest (String testName) {
		super(testName);
	}
  
	

	public void testQName1() {
		QualifiedName q1=new QualifiedName("http://example.org/", "v1", "ex");
		QualifiedName q2=new QualifiedName("http://example.org/", "v1", "ex");
		assertTrue(q1.equals(q2));
	}
	public void testQName2() {
		QualifiedName q1=new QualifiedName("http://example.org/", "v1", "ex");
		QualifiedName q2=new QualifiedName("http://example.org/", "v1", "exoooo");
		assertTrue(q1.equals(q2));
	}
	public void testQName3() {
		QualifiedName q1=new QualifiedName("http://example.org/", "v1", "ex");
		QualifiedName q2=new QualifiedName("http://example.org/", "v1", "ex");
		assertTrue(q1.equals(q2));
		q2.setLocalPart("v2");
		System.out.println("QName is " + q2);
		assertFalse(q1.equals(q2));
	}

}
