package org.openprovenance.prov.core.jsonld11.test;

import org.openprovenance.prov.core.jsonld11.serialization.ProvDeserialiser;
import org.openprovenance.prov.core.jsonld11.serialization.ProvSerialiser;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.test.RoundTripFromJavaTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Every construct written as PROV-JSONLD, read, written as PROV-JSON, read again, and compared with what was
 * first written: the two serialisations agree on the model, this way round too.
 */
public class RoundTripFromJavaJSONLDViaJSONTest extends RoundTripFromJavaTest {

    public void writeDocumentToFile(Document doc, String file) throws IOException {
        try (FileOutputStream out = new FileOutputStream(file)) {
            new ProvSerialiser().serialiseDocument(out, doc, true);
        }
    }

    public Document readDocumentFromFile(String file) throws IOException {
        Document fromJsonLd;
        try (FileInputStream in = new FileInputStream(file)) {
            fromJsonLd = new ProvDeserialiser().deserialiseDocument(in);
        }
        String json = file.replace(extension(), ".via.json");
        Namespace.withThreadNamespace(fromJsonLd.getNamespace());
        try (FileOutputStream out = new FileOutputStream(json)) {
            new org.openprovenance.prov.core.json.serialization.ProvSerialiser().serialiseDocument(out, fromJsonLd, true);
        }
        return new org.openprovenance.prov.core.json.serialization.ProvDeserialiser().deserialiseDocument(new File(json));
    }

    public String extension() {
        return ".viajson.jsonld";
    }

    public boolean checkSchema(String name) {
        return false;
    }

    /** PROV-JSON has one alternateOf: see the PROV-JSON round trip. */
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
