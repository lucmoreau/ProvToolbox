package org.openprovenance.prov.storage.mongodb;

import org.openprovenance.prov.core.RoundTripFromJavaJSONLD11Test;
import org.openprovenance.prov.interop.Formats;
import org.openprovenance.prov.model.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RoundTripFromJavaMongoTest extends RoundTripFromJavaJSONLD11Test {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public RoundTripFromJavaMongoTest(String testName) {
        super(testName);
    }

    Map<String, String> m=new HashMap<>();

    static MongoDocumentResourceStorage docS=new MongoDocumentResourceStorage();

    @Override
    public void writeDocumentToFile(Document doc, String file) throws IOException {

        System.out.println("storing " + file);


        String loc=docS.newStore(Formats.ProvFormat.JSONLD);
        m.put(file,loc);

        docS.writeDocument(loc, Formats.ProvFormat.JSONLD,doc);
    }

    @Override
    public Document readDocumentFromFile(String file) throws IOException {

        String loc=m.get(file);

        assertNotNull(loc);

        Document doc=docS.readDocument(loc);
        return doc;
    }
}
