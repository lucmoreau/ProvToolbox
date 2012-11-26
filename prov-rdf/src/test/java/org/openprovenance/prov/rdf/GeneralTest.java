package org.openprovenance.prov.rdf;

import junit.framework.TestCase;

import org.openprovenance.prov.xml.Document;
import org.openprovenance.prov.xml.DocumentEquality;
import org.openprovenance.prov.xml.ProvFactory;
import org.openrdf.rio.RDFFormat;

public class GeneralTest extends TestCase {
	final Utility u = new Utility();
	final org.openprovenance.prov.notation.Utility u2 = new org.openprovenance.prov.notation.Utility();
	final ProvFactory pFactory = new ProvFactory();

	public void testMultipleTypes() throws Exception
	{
		DocumentEquality de = new DocumentEquality(true);
		Document doc1 = u.parseRDF("src/test/resources/test_multiple_types.ttl");
		u.dumpRDF(pFactory, doc1, RDFFormat.TURTLE, "/tmp/test.ttl");
		Document doc2 = u.parseRDF("/tmp/test.ttl");
		boolean result=de.check(doc1, doc2);
		org.openprovenance.prov.xml.Entity e1 = (org.openprovenance.prov.xml.Entity)(doc1.getEntityOrActivityOrWasGeneratedBy().get(0));
		assertEquals(e1.getType().size(), 2);
		org.openprovenance.prov.xml.Entity e2 = (org.openprovenance.prov.xml.Entity)(doc2.getEntityOrActivityOrWasGeneratedBy().get(0));
		assertEquals(e2.getType().size(), 2);
		assertTrue(result);
	}
}
