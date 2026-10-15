package org.openprovenance.prov.core.jsonld11.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import junit.framework.TestCase;
import org.openprovenance.prov.core.jsonld11.serialization.ProvDeserialiser;
import org.openprovenance.prov.core.jsonld11.serialization.ProvSerialiser;
import org.openprovenance.prov.core.test.JsonLdExpansion;
import org.openprovenance.prov.core.test.Schemas;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.vanilla.LangString;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * https://github.com/openprov/prov-jsonld/issues/7: a qualified-name value under an extension property. The same
 * agent, {@code ex:arthur}, names its past project {@code ex:shed} in the two forms the issue compares: a node
 * reference {@code {"@id": "ex:shed"}}, which a JSON-LD processor reads as the IRI http://example.org/shed, and a
 * bare string {@code "ex:shed"}, which it reads as a plain literal.
 * <p>
 * ProvToolbox reads both as a JSON-LD processor does: the node reference as the qualified name ex:shed, the bare
 * string as the string "ex:shed". Both conform to the 2026-10-14 schema; the node reference does not conform to the
 * 2024-08-25 one, the W3C submission's. A bare string stays a qualified name only under a key
 * the context coerces to {@code @id} ({@code type}, {@code location}, {@code role}, the openprov scoped terms); the
 * writer writes a qualified name as a bare string under those keys and as a node reference under any other.
 */
public class ProvJsonld7Test extends TestCase {

    static final String EX = "http://example.org/";
    static final String FOAF = "http://xmlns.com/foaf/0.1/";
    static final ProvFactory pf = new org.openprovenance.prov.vanilla.ProvFactory();
    static final ObjectMapper mapper = new ObjectMapper();

    static final String DIR = "src/test/resources/prov-jsonld-7/";

    Document read(String file) throws IOException {
        try (FileInputStream in = new FileInputStream(DIR + file)) {
            return new ProvDeserialiser().deserialiseDocument(in);
        }
    }

    static Document parse(String json) {
        return new ProvDeserialiser().deserialiseDocument(new ByteArrayInputStream(json.getBytes()));
    }

    /** The document as the serialiser writes it, in a file under target. */
    static File write(Document doc, String file) throws IOException {
        Namespace.withThreadNamespace(doc.getNamespace());
        try (FileOutputStream out = new FileOutputStream("target/" + file)) {
            new ProvSerialiser().serialiseDocument(out, doc, true);
        }
        return new File("target/" + file);
    }

    static Document reread(File file) throws IOException {
        try (FileInputStream in = new FileInputStream(file)) {
            return new ProvDeserialiser().deserialiseDocument(in);
        }
    }

    static Agent arthur(Document doc) {
        List<Agent> agents = doc.getStatementOrBundle().stream().filter(s -> s instanceof Agent).map(s -> (Agent) s).collect(Collectors.toList());
        assertEquals(1, agents.size());
        Agent arthur = agents.get(0);
        assertEquals(pf.newQualifiedName(EX, "arthur", "ex"), arthur.getId());
        return arthur;
    }

    /** The single foaf:pastProject attribute of the single agent of the document. */
    static Other pastProject(Document doc) {
        List<Other> others = arthur(doc).getOther();
        assertEquals(others.toString(), 1, others.size());
        Other o = others.get(0);
        assertEquals(pf.newQualifiedName(FOAF, "pastProject", "foaf"), o.getElementName());
        return o;
    }

    static void assertIsShed(Other o) {
        assertEquals(pf.newQualifiedName(EX, "shed", "ex"), o.getValue());
        assertEquals(pf.getName().PROV_QUALIFIED_NAME, o.getType());
    }

    static void assertIsString(Other o) {
        assertEquals(new LangString("ex:shed"), o.getValue());
        assertEquals(pf.getName().XSD_STRING, o.getType());
    }

    /** The agent's node in the document as written. */
    static JsonNode writtenArthur(File file) throws IOException {
        for (JsonNode n : mapper.readTree(file).get("@graph")) if (n.path("@id").asText().equals("ex:arthur")) return n;
        throw new AssertionError("no ex:arthur in " + file);
    }

    /** What a JSON-LD processor expands the agent's foaf:pastProject to. */
    static JsonArray expandedPastProject(File file) throws Exception {
        JsonArray expanded = JsonLdExpansion.expand(file);
        assertEquals(List.of(), JsonLdExpansion.problems(expanded));
        for (JsonValue v : expanded) {
            JsonObject node = v.asJsonObject();
            if ((EX + "arthur").equals(node.getString("@id", null))) return node.getJsonArray(FOAF + "pastProject");
        }
        throw new AssertionError("no " + EX + "arthur in " + expanded);
    }

    public void testNodeReference() throws IOException {
        assertIsShed(pastProject(read("node-reference.jsonld")));
    }

    public void testString() throws IOException {
        assertIsString(pastProject(read("string.jsonld")));
    }

    public void testNodeReferenceConformsToSchema() throws IOException {
        assertEquals(List.of(), Schemas.violations(new File(DIR + "node-reference.jsonld")));
    }

    public void testStringConformsToSchema() throws IOException {
        assertEquals(List.of(), Schemas.violations(new File(DIR + "string.jsonld")));
    }

    /** The 2026-10-14 schema is what admits the node reference: the submission's 2024-08-25 one admits the string only. */
    public void testNodeReferenceViolates20240825Schema() throws IOException {
        List<String> violations = Schemas.violations(Schemas.PROV_JSONLD_2024_08_25, new File(DIR + "node-reference.jsonld"));
        assertTrue(String.join("\n", violations), violations.contains(
                "/@graph/1/foaf:pastProject/0: property '@id' is not defined in the schema and the schema does not allow additional properties"));
        assertEquals(List.of(), Schemas.violations(Schemas.PROV_JSONLD_2024_08_25, new File(DIR + "string.jsonld")));
    }

    /** ProvToolbox and a JSON-LD processor agree on both documents: an IRI for the node reference, a literal for the string. */
    public void testReadAsJsonLdExpandsThem() throws Exception {
        assertEquals("[{\"@id\":\"" + EX + "shed\"}]", expandedPastProject(new File(DIR + "node-reference.jsonld")).toString());
        assertEquals("[{\"@value\":\"ex:shed\"}]", expandedPastProject(new File(DIR + "string.jsonld")).toString());
    }

    public void testNodeReferenceRoundTrips() throws Exception {
        Document doc = read("node-reference.jsonld");
        File file = write(doc, "prov-jsonld-7-node-reference.jsonld");
        assertEquals("[{\"@id\":\"ex:shed\"}]", writtenArthur(file).get("foaf:pastProject").toString());
        assertEquals(List.of(), Schemas.violations(file));
        assertEquals("[{\"@id\":\"" + EX + "shed\"}]", expandedPastProject(file).toString());
        assertEquals(doc.getStatementOrBundle(), reread(file).getStatementOrBundle());
    }

    public void testStringRoundTrips() throws Exception {
        Document doc = read("string.jsonld");
        File file = write(doc, "prov-jsonld-7-string.jsonld");
        assertEquals("[{\"@value\":\"ex:shed\"}]", writtenArthur(file).get("foaf:pastProject").toString());
        assertEquals(List.of(), Schemas.violations(file));
        assertEquals(doc.getStatementOrBundle(), reread(file).getStatementOrBundle());
    }

    /** Under {@code type}, which the context coerces to {@code @id}, a bare string is still a qualified name, and is written back bare. */
    public void testTypeStaysBare() throws Exception {
        Document doc = parse("{\"@context\": [{\"ex\": \"http://example.org/\", \"foaf\": \"http://xmlns.com/foaf/0.1/\"},"
                + " \"https://openprovenance.org/prov-jsonld/context.jsonld\"],"
                + " \"@graph\": [{\"@type\": \"Agent\", \"@id\": \"ex:arthur\", \"type\": [\"ex:Builder\"], \"foaf:pastProject\": [{\"@id\": \"ex:shed\"}]}]}");
        Agent arthur = arthur(doc);
        assertEquals(1, arthur.getType().size());
        assertEquals(pf.newQualifiedName(EX, "Builder", "ex"), arthur.getType().get(0).getValue());
        assertEquals(pf.getName().PROV_QUALIFIED_NAME, arthur.getType().get(0).getType());

        File file = write(doc, "prov-jsonld-7-type.jsonld");
        JsonNode json = writtenArthur(file);
        assertEquals("[\"ex:Builder\"]", json.get("type").toString());
        assertEquals("[{\"@id\":\"ex:shed\"}]", json.get("foaf:pastProject").toString());
        assertEquals(List.of(), Schemas.violations(file));
        assertEquals(doc.getStatementOrBundle(), reread(file).getStatementOrBundle());
    }

    /** A node reference is the IRI and nothing else. */
    public void testNodeReferenceWithValueIsRejected() {
        try {
            parse("{\"@context\": [{\"ex\": \"http://example.org/\", \"foaf\": \"http://xmlns.com/foaf/0.1/\"},"
                    + " \"https://openprovenance.org/prov-jsonld/context.jsonld\"],"
                    + " \"@graph\": [{\"@type\": \"Agent\", \"@id\": \"ex:arthur\", \"foaf:pastProject\": [{\"@id\": \"ex:shed\", \"@value\": \"shed\"}]}]}");
            fail("a node reference with a @value was read");
        } catch (org.openprovenance.prov.model.exception.UncheckedException e) {
            assertTrue(String.valueOf(e.getCause()), String.valueOf(e.getCause().getMessage()).contains("a node reference takes no @value"));
        }
    }


    /**
     * A review found that "Meeting at 10:30", a string under an extension property as the reader reads it, failed the
     * 2026-10-14 schema, which admitted a bare string there only as a QualifiedName, whose local name takes no colon.
     * Extension properties and value now take ArrayOfStringValues, where a bare string is any string; type, location
     * and role keep ArrayOfValues, where a bare string is a qualified name.
     */
    public void testStringWithColonConformsToSchema() throws Exception {
        File file = new File(DIR + "string-with-colon.jsonld");
        Document doc = read("string-with-colon.jsonld");
        Other note = ((HasOther) doc.getStatementOrBundle().get(0)).getOther().get(0);
        assertEquals(new LangString("Meeting at 10:30"), note.getValue());
        assertEquals(pf.getName().XSD_STRING, note.getType());
        assertEquals(List.of(), Schemas.violations(file));
        assertEquals(List.of(), Schemas.violations(Schemas.PROV_JSONLD_2024_08_25, file));

        File written = write(doc, "prov-jsonld-7-string-with-colon.jsonld");
        assertEquals("[{\"@value\":\"Meeting at 10:30\"}]", mapper.readTree(written).get("@graph").get(0).get("ex:note").toString());
        assertEquals(List.of(), Schemas.violations(written));
    }

    /** Under type, coerced to @id, a bare string is a qualified name: one with a colon in its local name, or a sentence, is rejected. */
    public void testTypeTakesQualifiedNamesOnly() throws Exception {
        for (String bad : List.of("ex:a:b", "Meeting at 10:30")) {
            String json = "{\"@context\": [{\"ex\": \"http://example.org/\"}, \"https://openprovenance.org/prov-jsonld/context.jsonld\"],"
                    + " \"@graph\": [{\"@type\": \"Activity\", \"@id\": \"ex:meeting\", \"type\": [\"" + bad + "\"]}]}";
            List<String> violations = Schemas.violations(Schemas.PROV_JSONLD, Schemas.parse(json));
            assertTrue(bad + ": " + violations, violations.stream().anyMatch(v -> v.startsWith("/@graph/0/type/0: does not match the regex pattern")));
        }
    }

    /** The openprov schema, regenerated from the 2026-10-14 one, takes the same string under an extension property of a scoped relation. */
    public void testStringWithColonInOpenprovRelation() throws Exception {
        String json = "{\"@context\": [{\"ex\": \"http://example.org/\"}, \"https://openprovenance.org/ns/openprov.jsonld\"],"
                + " \"@graph\": [{\"@type\": \"Attribution\", \"@id\": \"ex:att\", \"entity\": \"ex:e\", \"agent\": \"ex:ag\","
                + " \"association\": \"ex:asc\", \"ex:note\": [\"Meeting at 10:30\"]}]}";
        assertEquals(List.of(), Schemas.violations(Schemas.OPENPROV_JSONLD, Schemas.parse(json)));
    }
}
