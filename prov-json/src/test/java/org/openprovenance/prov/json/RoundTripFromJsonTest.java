package org.openprovenance.prov.json;

import java.io.File;

import junit.framework.TestCase;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.DocumentEquality;
import org.openprovenance.prov.xml.ProvFactory;
import org.openprovenance.prov.xml.ProvSerialiser;

public class RoundTripFromJsonTest extends TestCase {
    static ProvFactory pFactory=ProvFactory.getFactory();
    final Converter convert=new Converter(pFactory);

    public RoundTripFromJsonTest(String name) {
	super(name);
    }


    public void loadFromJsonSaveAndReload(String file, Boolean compare) throws Throwable {
	System.out.println("-------------- File: " + file);
	DocumentEquality de = new DocumentEquality(true,null);

	Document doc1 = convert.readDocument("src/test/resources/" + file);
	file = file.replace('/', '_');
	convert.writeDocument(doc1, "target/" + file);
	Document doc2 = convert.readDocument("target/" + file);

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
		      .serialiseDocument(new File("target/" + file + "-2" + ".xml"),
					 doc2, true);
	ProvSerialiser.getThreadProvSerialiser()
	      .serialiseDocument(new File("target/" + file + ".xml"),
				 doc1, true);
    }

    
    private void testIssue(String issueName) throws Throwable {
	loadFromJsonSaveAndReload("issues/" + issueName + ".json", true);
    }
    

  
    public void testBundles() throws Throwable {
	testIssue("issue96");
	testIssue("issue96-b");
	testIssue("issue");
	testIssue("issue2");
	testIssue("issue3");
	testIssue("issue4");
	testIssue("issue5");
    }

}
