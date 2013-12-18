package org.openprovenance.prov.rdf;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.xml.UncheckedTestException;
import org.openrdf.rio.RDFFormat;

public class AttributeTest extends org.openprovenance.prov.xml.AttributeTest {
    final ProvFactory pFactory = new org.openprovenance.prov.xml.ProvFactory();
    final Ontology onto = new Ontology(pFactory);
    final Utility u = new Utility(pFactory, onto);

    public AttributeTest(String testName) {
	super(testName);
    }

    @Override
    public String extension() {
	return ".trig";
    }

    @Override
    public Document readDocument(String file) {
	try {
	    Document doc2 = u.parseRDF(file);
	    return doc2;
	} catch (Exception e) {
	    throw new UncheckedTestException(e);
	}
    }

    @Override
    public void writeDocument(Document doc, String file) {
	try {
	    u.dumpRDF(doc, RDFFormat.TRIG, file);
	} catch (Exception e) {
	    e.printStackTrace();
	    throw new UncheckedTestException(e);
	}
    }

    @Override
    public boolean checkSchema(String name) {
	return false;
	
    }
    public void testDictionaryInsertionWithOneKey22 () {
	System.out.println(" --- not dealing with QName as key");
    }    
    public void testDictionaryInsertionWithOneKey23 () {
	System.out.println(" --- not dealing with QName as key");	
    }    
    public void testDictionaryInsertionWithOneKey26 () {
	System.out.println(" --- not dealing with QName as key");	
    }
    

}
