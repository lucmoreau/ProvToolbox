package org.openprovenance.prov.core;

import org.openprovenance.prov.core.json.serialization.ProvDeserialiser;
import org.openprovenance.prov.core.json.serialization.ProvSerialiser;
import org.openprovenance.prov.model.Document;

import java.io.File;
import java.io.FileOutputStream;
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

    public Document readXMLDocument(String file)
            throws IOException {

        System.out.println("reading from " + file);

        Document o = new org.openprovenance.prov.json.Converter(pFactory)
                .readDocument(file);
        return o;
    }


//    @Override
 //   public void writeXMLDocument(Document doc, String file) throws IOException {
  //  }

    public String extension() {
        return "_b.json";
    }

}
