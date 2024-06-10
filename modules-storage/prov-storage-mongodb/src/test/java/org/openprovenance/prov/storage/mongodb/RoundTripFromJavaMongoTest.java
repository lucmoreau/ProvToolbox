package org.openprovenance.prov.storage.mongodb;

import org.openprovenance.prov.core.jsonld11.RoundTripFromJavaJSONLD11Test;
import org.openprovenance.prov.interop.Formats;
import org.openprovenance.prov.model.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RoundTripFromJavaMongoTest extends RoundTripFromJavaJSONLD11Test {


    Map<String, String> m=new HashMap<>();

    static MongoDocumentResourceStorage docS=new MongoDocumentResourceStorage("provtest");

    @Override
    public String extension() {
        return "";
    }

    @Override
    public void writeDocumentToFile(Document doc, String file) throws IOException {

       System.out.println("storing (mongo) " + file);


        String loc=docS.newStore(Formats.ProvFormat.JSONLD);
        m.put(file,loc);

        docS.writeDocument(loc, doc, Formats.ProvFormat.JSONLD);
    }

    @Override
    public Document readDocumentFromFile(String file) throws IOException {
        System.out.println(" retrieving from " + file);

        String loc=m.get(file);

        assertNotNull(loc);

        Document doc=docS.readDocument(loc);

        assertNotNull(doc);

        return doc;
    }

    public boolean checkTest(String name) {
        if (name.contains("DictionaryMembership")) {
            System.out.println(escapeRed("########## Skipping testing for " + name + " in " + extension()));
            return false;
        }
        return true;
    }

    @Override
    public String jsonSchemaLocation() {
        return "../../modules-core/prov-jsonld/" + super.jsonSchemaLocation();
    }
}
