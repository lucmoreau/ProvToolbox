package org.openprovenance.prov.xml;

import org.openprovenance.prov.model.BeanTraversal;

/**
 * Unit test for PROV roundtrip conversion between Java and XML
 */
public class RoundTripJavaToJavaTest extends RoundTripFromJavaTest {

    public RoundTripJavaToJavaTest(String testName) {
	super(testName);
	test=false;
    }


    ProvFactory pFactory=new ProvFactory();
    
    public void compareDocAndFile(Document doc, String file, boolean check) {
	BeanTraversal bc=new BeanTraversal(pFactory, pFactory, new ValueConverter(pFactory));
        org.openprovenance.prov.model.Document doc2=bc.convert(doc);
        compareDocuments(doc, doc2, check && checkTest(file));
    }


}
