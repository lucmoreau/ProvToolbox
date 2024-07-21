package org.openprovenance.prov.core.json.test;

import org.openprovenance.prov.core.json.serialization.ProvDeserialiser;
import org.openprovenance.prov.core.json.serialization.ProvSerialiser;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.test.RoundTripFromJavaTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class RoundTripFromJavaJSONTest extends RoundTripFromJavaTest {

    public Document readDocumentFromFile(String file) throws IOException {
        System.out.println(" reading from " + file);

        ProvDeserialiser deserial=new ProvDeserialiser();
        return deserial.deserialiseDocument(new File(file));
    }

    public void writeDocumentToFile(Document doc, String file) throws IOException {
        System.out.println("writing to " + file);

        ProvSerialiser serial=new ProvSerialiser();
        serial.serialiseDocument(new FileOutputStream(file), doc, true);
    }

    public String extension() {
        return ".json";
    }

    public boolean checkTest(String name) {
        if (name.endsWith("entity101" + extension())) {
            System.out.println(escapeRed("########## Skipping testing for entity101 in JSON"));
            return false;
        }
        return true;
    }
    public boolean checkSchema(String name) {
        return false;
    }

    @Override
    public void testDefault1() {
        System.out.println(escapeRed("########## Skipping testing for default1 in JSON"));
        super.testDefault1();
    }
    @Override
    public void testDictionaryMembership1() {
        System.out.println(escapeRed("########## Skipping testDictionaryMembership1 (json)"));
    }
    @Override
    public void testDictionaryMembership2() {
        System.out.println(escapeRed("########## Skipping testDictionaryMembership2 (json)"));
    }
    @Override public void testDictionaryMembership3() {
        System.out.println(escapeRed("########## Skipping testDictionaryMembership3 (json)"));
    }
    @Override public void testDictionaryMembership4() {
        System.out.println(escapeRed("########## Skipping testDictionaryMembership4 (json)"));
    }


}
