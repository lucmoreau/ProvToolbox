package org.openprovenance.prov.core.json.test;

import org.openprovenance.prov.core.json.serialization.ProvDeserialiser;
import org.openprovenance.prov.core.json.serialization.ProvSerialiser;
import org.openprovenance.prov.core.test.Schemas;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.extension.QualifiedAlternateOf;
import org.openprovenance.prov.model.test.RoundTripFromJavaTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Every construct of the model written as PROV-JSON, checked against the PROV-JSON schema, read back and
 * compared with what was written.
 */
public class RoundTripFromJavaJSONTest extends RoundTripFromJavaTest {

    public Document readDocumentFromFile(String file) throws IOException {
        ProvDeserialiser deserial = new ProvDeserialiser();
        return deserial.deserialiseDocument(new File(file));
    }

    public void writeDocumentToFile(Document doc, String file) throws IOException {
        ProvSerialiser serial = new ProvSerialiser();
        try (FileOutputStream out = new FileOutputStream(file)) {
            serial.serialiseDocument(out, doc, true);
        }
    }

    public String extension() {
        return ".json";
    }

    /**
     * Constructs missing an argument PROV-DM requires (a usage without activity, a derivation without used
     * entity, ...): they round-trip, but the schema rightly wants the property, so they are not held to it.
     */
    static final Set<String> INCOMPLETE = Set.of("usage1", "association2", "attribution1", "attribution2",
            "delegation1", "delegation2", "derivation1", "derivation2", "derivation9", "end1", "end4",
            "influence1", "influence2", "communication1", "communication2", "start1", "start4");

    @Override
    public boolean checkSchema(String name) {
        String stem = name.substring(name.lastIndexOf('/') + 1).replace(extension(), "");
        return !INCOMPLETE.contains(stem);
    }

    @Override
    public void doCheckSchema1(String file) {
        try {
            List<String> violations = Schemas.violations(Schemas.PROV_JSON, new File(file));
            assertTrue(file + " violates the PROV-JSON schema: " + violations, violations.isEmpty());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * PROV-JSON has one alternateOf; a qualified alternate with neither identifier nor attributes is written as it
     * and read back as the plain relation, which says the same thing.
     */
    @Override
    public void testQualifiedAlternateOf2() {
        QualifiedAlternateOf alt = pFactory.newQualifiedAlternateOf(null, q("e1"), q("e2"), null);
        Document doc = makeDocument(new org.openprovenance.prov.model.Statement[]{alt}, null);
        writeDocument(doc, "target/qualified-alternate2.json");
        doCheckSchema1("target/qualified-alternate2.json");
        Document back = readDocument("target/qualified-alternate2.json");
        Document plain = makeDocument(new org.openprovenance.prov.model.Statement[]{pFactory.newAlternateOf(q("e1"), q("e2"))}, null);
        assertEquals(plain.getStatementOrBundle(), back.getStatementOrBundle());
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
