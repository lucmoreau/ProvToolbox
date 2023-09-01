package org.openprovenance.prov.model;

import org.openprovenance.prov.vanilla.ProvFactory;
import org.openprovenance.prov.model.BeanTraversal;
import org.openprovenance.prov.model.Document;

import static org.openprovenance.prov.model.ExtensionRoundTripFromJavaTest.deepCopy;

/**
 * Unit test for PROV roundtrip conversion between Java represenations
 */
public class RoundTripJavaToJavaTest extends RoundTripFromJavaTest {

    public RoundTripJavaToJavaTest(String testName) {
        super(testName);
        test=false;
    }


    @Override
    public void compareDocAndFile(Document doc, String file, boolean check) {
        Document doc2=deepCopy(doc);
        compareDocuments(doc, doc2, check && checkTest(file));
    }

    public boolean checkSchema(String name) {
        return false;
    }

}
