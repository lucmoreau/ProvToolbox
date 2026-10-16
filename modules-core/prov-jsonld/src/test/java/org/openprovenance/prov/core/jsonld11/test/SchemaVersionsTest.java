package org.openprovenance.prov.core.jsonld11.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;
import org.openprovenance.prov.core.jsonld11.serialization.Constants;
import org.openprovenance.prov.core.test.Schemas;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * The bundled versions of the PROV-JSONLD schema, each with the openprov schema generated from it: what a TTF
 * {@code schemaCheck} chooses between. An openprov schema refers to its own version's definitions, so each of its
 * references into JSONLD_SCHEMA_URL must resolve in that version.
 */
public class SchemaVersionsTest extends TestCase {

    static final ObjectMapper mapper = new ObjectMapper();

    static JsonNode resource(String resource) throws IOException {
        try (InputStream in = Schemas.open(resource, "src/main/resources/" + resource)) {
            assertNotNull(resource, in);
            return mapper.readTree(in);
        }
    }

    static void collectRefs(JsonNode node, List<String> refs) {
        if (node.isObject()) node.fields().forEachRemaining(f -> { if (f.getKey().equals("$ref")) refs.add(f.getValue().asText()); else collectRefs(f.getValue(), refs); });
        else if (node.isArray()) node.forEach(n -> collectRefs(n, refs));
    }

    public void testEachVersionHasItsSchemas() throws IOException {
        for (String version : Constants.JSONLD_SCHEMA_VERSIONS) {
            JsonNode spec = resource(Constants.jsonldSchemaResource(version));
            assertEquals(version, Constants.JSONLD_SCHEMA_URL, spec.get("$id").asText());
            JsonNode openprov = resource(Constants.openprovSchemaResource(version));
            assertEquals(version, Constants.OPENPROV_SCHEMA_URL, openprov.get("$id").asText());
            List<String> refs = new ArrayList<>();
            collectRefs(openprov.get("definitions"), refs);
            String prefix = Constants.JSONLD_SCHEMA_URL + "#/definitions/";
            for (String ref : refs) {
                if (ref.startsWith(prefix)) assertTrue(version + ": " + ref, spec.get("definitions").has(ref.substring(prefix.length())));
                else assertTrue(version + ": " + ref, openprov.get("definitions").has(ref.substring("#/definitions/".length())));
            }
        }
    }

    public void testDefaultIsTheNewest() throws IOException {
        List<String> versions = Constants.JSONLD_SCHEMA_VERSIONS;
        assertEquals(Constants.JSONLD_SCHEMA_DEFAULT_VERSION, versions.get(versions.size() - 1));
        assertEquals(Constants.JSONLDSCHEMA_2026_10_14, Constants.jsonldSchemaResource(Constants.JSONLD_SCHEMA_DEFAULT_VERSION));
        assertEquals(resource(Constants.OPENPROV_SCHEMA_RESOURCE), resource(Constants.openprovSchemaResource(Constants.JSONLD_SCHEMA_DEFAULT_VERSION)));
    }

    /**
     * A strict variant is its version's schema with QualifiedName's pattern and $comment, and the $id, changed
     * (openprov/prov-jsonld's script/schema-qn.py), and shares its version's openprov schema.
     */
    public void testStrictVariants() throws IOException {
        for (String version : Constants.JSONLD_SCHEMA_QN_VERSIONS) {
            assertTrue(version, Constants.JSONLD_SCHEMA_VERSIONS.contains(version));
            String strict = version + Constants.JSONLD_SCHEMA_QN_SUFFIX;
            JsonNode qn = resource(Constants.jsonldSchemaResource(strict)).deepCopy();
            JsonNode spec = resource(Constants.jsonldSchemaResource(version));
            assertEquals(strict, "https://openprovenance.org/prov-jsonld/schema-" + strict + ".json", qn.get("$id").asText());
            String pattern = qn.get("definitions").get("QualifiedName").get("pattern").asText();
            assertTrue(strict, "ex:shed".matches(pattern) && !"ex:a:b".matches(pattern) && !"ex:café".matches(pattern));
            ((com.fasterxml.jackson.databind.node.ObjectNode) qn).put("$id", spec.get("$id").asText());
            com.fasterxml.jackson.databind.node.ObjectNode qname = (com.fasterxml.jackson.databind.node.ObjectNode) qn.get("definitions").get("QualifiedName");
            JsonNode specQname = spec.get("definitions").get("QualifiedName");
            qname.put("pattern", specQname.get("pattern").asText());
            if (specQname.has("$comment")) qname.put("$comment", specQname.get("$comment").asText()); else qname.remove("$comment");
            assertEquals(strict + " differs from " + version + " elsewhere", spec, qn);
            assertEquals(Constants.openprovSchemaResource(version), Constants.openprovSchemaResource(strict));
        }
    }

    /** What sets the versions apart: anchored @type patterns from 2026-09-12, node references from 2026-10-14. */
    public void testVersionsDiffer() throws IOException {
        JsonNode v0825 = resource(Constants.jsonldSchemaResource("2024-08-25")).get("definitions");
        JsonNode v0912 = resource(Constants.jsonldSchemaResource("2026-09-12")).get("definitions");
        JsonNode v1014 = resource(Constants.jsonldSchemaResource("2026-10-14")).get("definitions");
        assertEquals("Entity", v0825.get("prov:Entity").get("properties").get("@type").get("pattern").asText());
        assertEquals("^Entity$", v0912.get("prov:Entity").get("properties").get("@type").get("pattern").asText());
        assertEquals("^Entity$", v1014.get("prov:Entity").get("properties").get("@type").get("pattern").asText());
        assertFalse(v0912.has("node_reference"));
        assertTrue(v1014.has("node_reference"));
    }
}
