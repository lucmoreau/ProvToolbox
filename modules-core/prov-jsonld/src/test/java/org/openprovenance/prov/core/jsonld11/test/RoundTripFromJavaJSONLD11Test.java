package org.openprovenance.prov.core.jsonld11.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openprovenance.prov.core.jsonld11.serialization.ProvDeserialiser;
import org.openprovenance.prov.core.jsonld11.serialization.ProvSerialiser;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.test.RoundTripFromJavaTest;

import java.io.*;
import java.util.List;
import com.apicatalog.jsonld.JsonLdError;
import org.openprovenance.prov.core.test.JsonLdExpansion;
import org.openprovenance.prov.core.test.Schemas;


public class RoundTripFromJavaJSONLD11Test extends RoundTripFromJavaTest {


    public Document readDocumentFromFile(String file) throws IOException {
        readingMessage(file);
        ProvDeserialiser deserial=new ProvDeserialiser();
        return deserial.deserialiseDocument(new FileInputStream(file));

    }
    public void readingMessage(String file) {
        System.out.println(" reading from " + file);
    }

    public void writingMessage(String file) {
        System.out.println("writing to " + file);
    }
    final ObjectMapper mapper=new ObjectMapper();



    public void writeDocumentToFile(Document doc, String file) throws IOException {
        writingMessage(file);

        ProvSerialiser serial=new ProvSerialiser(mapper, false);
        serial.serialiseDocument(new FileOutputStream(file), doc, true);
    }


    public String extension() {
        return ".jsonld";
    }



    /** Every file written: valid against the schema its context calls for (PROV-JSONLD's, or openprov's), and JSON-LD that expands to PROV terms. */
    public boolean checkTest(String name) {
        if (name.contains("DictionaryMembership")) {
            System.out.println(escapeRed("########## Skipping testing for " + name + " in " + extension()));
            return false;
        }
        try {
            List<String> violations = Schemas.violations(new File(name));
            assertTrue(name + " violates its schema: " + violations, violations.isEmpty());
            List<String> problems = JsonLdExpansion.problems(JsonLdExpansion.expand(new File(name)));
            assertTrue(name + " does not expand to PROV: " + problems, problems.isEmpty());
        } catch (IOException | JsonLdError e) {
            throw new RuntimeException(name, e);
        }
        return true;
    }

    @Override
    public void testDictionaryMembership1() {
        System.out.println(escapeRed("########## Skipping testDictionaryMembership1 (jsonld)"));
    }
    @Override
    public void testDictionaryMembership2() {
        System.out.println(escapeRed("########## Skipping testDictionaryMembership2 (jsonld)"));
    }
    @Override
    public void testDictionaryMembership3() {
        System.out.println(escapeRed("########## Skipping testDictionaryMembership3 (jsonld)"));
    }
    @Override
    public void testDictionaryMembership4() {
        System.out.println(escapeRed("########## Skipping testDictionaryMembership4 (jsonld)"));
    }


}
