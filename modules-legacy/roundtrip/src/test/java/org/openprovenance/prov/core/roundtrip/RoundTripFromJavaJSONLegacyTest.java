package org.openprovenance.prov.core.roundtrip;

import org.openprovenance.prov.core.RoundTripFromJavaJSONTest;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.RoundTripFromJavaTest;

import java.io.IOException;

public class RoundTripFromJavaJSONLegacyTest extends RoundTripFromJavaJSONTest {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public RoundTripFromJavaJSONLegacyTest(String testName) {
        super(testName);
    }

    public Document readDocumentFromFile(String file)
            throws IOException {

        System.out.println("reading from " + file);

        Document o = new org.openprovenance.prov.json.Converter(RoundTripFromJavaTest.pFactory)
                .readDocument(file);
        return o;
    }


//    @Override
 //   public void writeDocumentToFile(Document doc, String file) throws IOException {
  //  }

    public String extension() {
        return "_b.json";
    }

    @Override
    public void testQualifiedSpecializationOf1 () {}

    @Override
    public void testQualifiedSpecializationOf2 () {}

    @Override
    public void testQualifiedAlternateOf1 () {}

    @Override
    public void testQualifiedAlternateOf2 () {}

    @Override
    public void testQualifiedHadMember1 () {}

    @Override
    public void testQualifiedHadMember2 () {}

    @Override
    public void testBundle4 () {}


}
