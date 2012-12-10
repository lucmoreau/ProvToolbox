package org.openprovenance.prov.xml;

/**
 * Unit test for PROV roundtrip conversion between Java and XML
 */
public class RoundTripJavaToJavaTest extends RoundTripFromJavaTest {

    public RoundTripJavaToJavaTest(String testName) {
	super(testName);
    }

    ProvFactory pFactory=new ProvFactory();
    
    public void compareDocAndFile(Document doc, String file, boolean check) {
	BeanTraversal bc=new BeanTraversal(pFactory, pFactory);
        Document doc2=bc.convert(doc);
        compareDocuments(doc, doc2, check && checkTest(file));
    }


}
