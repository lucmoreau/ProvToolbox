package org.openprovenance.prov.model.test;


import org.openprovenance.prov.model.Document;

/**
 * Unit test for PROV roundtrip conversion between Java represenations
 */
public class RoundTripJavaToJavaTest extends RoundTripFromJavaTest {


    @Override
    public void compareDocAndFile(Document doc, String file, boolean check) {
        Document doc2= ExtensionRoundTripFromJavaTest.deepCopy(doc);
        writingMessage(file);
        documentComparator.compareDocuments(doc, doc2, check && checkTest(file));
    }

    public boolean checkSchema(String name) {
        return false;
    }

    public void writingMessage(String file) {
        System.out.println("copying " + file);
    }

    public boolean checkTest(String name) {
        // checking required, ensuring the clone is equal to the original
        return true;
    }

}
