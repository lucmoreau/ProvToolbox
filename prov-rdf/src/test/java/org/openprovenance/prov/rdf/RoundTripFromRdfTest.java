package org.openprovenance.prov.rdf;

import junit.framework.TestCase;
import java.io.File;
import org.openprovenance.prov.xml.Document;
import org.openprovenance.prov.xml.DocumentEquality;
import org.openprovenance.prov.xml.ProvFactory;
import org.openprovenance.prov.xml.ProvSerialiser;
import org.openrdf.rio.RDFFormat;

public class RoundTripFromRdfTest extends TestCase {

    public void loadFromRdfSaveAndReload(String file) throws Exception {

	Utility u = new Utility();
	org.openprovenance.prov.notation.Utility u2 = new org.openprovenance.prov.notation.Utility();
	ProvFactory pFactory = new ProvFactory();

	DocumentEquality de = new DocumentEquality(true);
	Document doc1 = u.parseRDF("src/test/resources/" + file);
	u.dumpRDF(pFactory, doc1, RDFFormat.TURTLE, "target/" + file);
	Document doc2 = u.parseRDF("target/test.ttl");
	boolean result = de.check(doc1, doc2);
	
	System.out.println("result is " + result);

	ProvSerialiser.getThreadProvSerialiser()
		      .serialiseDocument(new File("target/" + file + ".xml"),
					 doc2, true);
	u2.writeDocument(doc2, "target/" + file + ".provn");

    }

    public void testFile1() throws Exception {
	loadFromRdfSaveAndReload("test_multiple_types.ttl");
    }
    
    public void testFile2() throws Exception {
	loadFromRdfSaveAndReload("prov-o-ex2-PASS.ttl");
    }
}
