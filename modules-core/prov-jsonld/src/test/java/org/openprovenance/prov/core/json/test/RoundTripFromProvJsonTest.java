package org.openprovenance.prov.core.json.test;

import junit.framework.TestCase;
import org.openprovenance.prov.core.json.serialization.ProvDeserialiser;
import org.openprovenance.prov.core.json.serialization.ProvSerialiser;
import org.openprovenance.prov.core.test.Schemas;
import org.openprovenance.prov.model.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * PROV-JSON written by hand in the submission's own style — a typed literal per attribute, a native number —
 * read into the model, then written as PROV-JSON and PROV-JSONLD and read back from both.
 */
public class RoundTripFromProvJsonTest extends TestCase {

    static final ProvFactory pFactory = new org.openprovenance.prov.vanilla.ProvFactory();
    static final Name name = pFactory.getName();

    public Document loadFromProvJson(String file) throws IOException {
        return new ProvDeserialiser().deserialiseDocument(new File(file));
    }

    public void writeToProvJson(Document doc, String file) throws IOException {
        try (FileOutputStream out = new FileOutputStream(file)) {
            new ProvSerialiser().serialiseDocument(out, doc, true);
        }
    }

    public void writeToProvJsonLD(Document doc, String file) throws IOException {
        Namespace.withThreadNamespace(doc.getNamespace());
        try (FileOutputStream out = new FileOutputStream(file)) {
            new org.openprovenance.prov.core.jsonld11.serialization.ProvSerialiser().serialiseDocument(out, doc, true);
        }
    }

    public Document loadFromProvJsonLD(String file) throws IOException {
        try (java.io.FileInputStream in = new java.io.FileInputStream(file)) {
            return new org.openprovenance.prov.core.jsonld11.serialization.ProvDeserialiser().deserialiseDocument(in);
        }
    }

    /** The entity's other attributes by local name. */
    static Map<String, Other> others(Entity e) {
        return e.getOther().stream().collect(Collectors.toMap(o -> o.getElementName().getLocalPart(), o -> o));
    }

    static Entity onlyEntity(Document doc) {
        List<Entity> entities = doc.getStatementOrBundle().stream().filter(s -> s instanceof Entity).map(s -> (Entity) s).collect(Collectors.toList());
        assertEquals(1, entities.size());
        return entities.get(0);
    }

    static void assertTyped(Other o, String value, QualifiedName type) {
        assertEquals(type, o.getType());
        assertEquals(value, String.valueOf(o.getValue()));
    }

    private Document roundTrips(String issueName) throws IOException {
        Document doc = loadFromProvJson("src/test/resources/issues/" + issueName + ".json");

        String json = "target/" + issueName + ".json";
        writeToProvJson(doc, json);
        assertEquals(List.of(), Schemas.violations(Schemas.PROV_JSON, new File(json)));
        assertEquals(doc, loadFromProvJson(json));

        String jsonld = "target/" + issueName + ".jsonld";
        writeToProvJsonLD(doc, jsonld);
        assertEquals(List.of(), Schemas.violations(Schemas.PROV_JSONLD, new File(jsonld)));
        assertEquals(doc, loadFromProvJsonLD(jsonld));
        return doc;
    }

    public void testIssue231Simple() throws IOException {
        Document doc = roundTrips("issue-231-simple");
        Entity e1 = onlyEntity(doc);
        assertEquals("http://example.org/", e1.getId().getNamespaceURI());
        assertEquals("e1", e1.getId().getLocalPart());
        Map<String, Other> others = others(e1);
        assertEquals(others.keySet().toString(), 2, others.size());
        assertTyped(others.get("prop1"), "82.5e-2", name.XSD_DOUBLE);
        // a native JSON integer is an xsd:int
        assertTyped(others.get("prop2"), "1", name.XSD_INT);
    }

    public void testIssue231() throws IOException {
        Document doc = roundTrips("issue-231");
        Map<String, Other> others = others(onlyEntity(doc));
        assertEquals(others.keySet().toString(), 4, others.size());
        assertTyped(others.get("byteSize"), "1034", name.XSD_POSITIVE_INTEGER);
        assertTyped(others.get("compression"), "82.5e-2", name.XSD_DOUBLE);
        assertTyped(others.get("content"), "Y29udGVudCBoZXJl", name.XSD_BASE64_BINARY);
        assertTyped(others.get("prop1"), "2026-09-03T17:36:39.415+01:00", name.XSD_DATETIME);
    }
}
