package org.openprovenance.prov.notation;

import java.io.File;
import junit.framework.TestCase;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.xml.DocumentEquality;
import org.openprovenance.prov.xml.ProvSerialiser;

public class RoundTripFromProvnTest extends TestCase {
    final Utility u = new Utility();

    public RoundTripFromProvnTest(String name) {
	super(name);
    }

    public String extension() {
	return ".provn";
    }

    public void loadFromProvnSaveAndReload(String file, Boolean compare) throws Throwable {
	System.out.println("-------------- File: " + file);
	org.openprovenance.prov.notation.Utility u2 = new org.openprovenance.prov.notation.Utility();

	DocumentEquality de = new DocumentEquality(true);

	Document doc1 = u.readDocument("src/test/resources/" + file);
	file = file.replace('/', '_');
	u.writeDocument(doc1, "target/" + file);
	Document doc2 = u.readDocument("target/" + file);

	boolean result = de.check(doc1, doc2);

	if (!result && compare) {
	    System.out.println(doc1);
	    System.out.println("------------------");
	    System.out.println(doc2);
	}
	if (compare) {
	    assertTrue(result);
	}
	System.out.println("result is " + result);

	ProvSerialiser.getThreadProvSerialiser()
		      .serialiseDocument(new File("target/" + file + ".xml"),
					 doc2, true);
	u2.writeDocument(doc2, "target/" + file + ".provn");

    }

    private void testIssue(String issueName) throws Throwable {
	loadFromProvnSaveAndReload("issues/" + issueName + ".provn", true);
    }

    public void testMembership() throws Throwable {
	testIssue("unification-membership-f1-FAIL-DM");
	testIssue("unification-membership-f2-FAIL-DM");

    }

}
