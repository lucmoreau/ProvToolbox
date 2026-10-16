package org.openprovenance.prov.core.json.test;

import org.openprovenance.prov.core.json.serialization.ProvDeserialiser;
import org.openprovenance.prov.core.json.serialization.ProvSerialiser;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.test.RoundTripFromJavaTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Every construct written as PROV-JSON, read, written as PROV-JSONLD, read again, and compared with what was
 * first written: the two serialisations agree on the model.
 */
public class RoundTripFromJavaJSONViaJSONLDTest extends RoundTripFromJavaTest {

    public void writeDocumentToFile(Document doc, String file) throws IOException {
        try (FileOutputStream out = new FileOutputStream(file)) {
            new ProvSerialiser().serialiseDocument(out, doc, true);
        }
    }

    public Document readDocumentFromFile(String file) throws IOException {
        Document fromJson = new ProvDeserialiser().deserialiseDocument(new File(file));
        String jsonld = file.replace(extension(), ".via.jsonld");
        Namespace.withThreadNamespace(fromJson.getNamespace());
        try (FileOutputStream out = new FileOutputStream(jsonld)) {
            new org.openprovenance.prov.core.jsonld11.serialization.ProvSerialiser().serialiseDocument(out, fromJson, true);
        }
        try (FileInputStream in = new FileInputStream(jsonld)) {
            return new org.openprovenance.prov.core.jsonld11.serialization.ProvDeserialiser().deserialiseDocument(in);
        }
    }

    public String extension() {
        return ".viajsonld.json";
    }

    public boolean checkSchema(String name) {
        return false;
    }

    /** PROV-JSON has one alternateOf: see {@link RoundTripFromJavaJSONTest#testQualifiedAlternateOf2}. */
    @Override
    public void testQualifiedAlternateOf2() {
    }

    @Override
    public void testDictionaryMembership1() {
    }
    @Override
    public void testDictionaryMembership2() {
    }
    @Override
    public void testDictionaryMembership3() {
    }
    @Override
    public void testDictionaryMembership4() {
    }
}
