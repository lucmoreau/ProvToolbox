package org.openprovenance.prov.core.test;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import com.networknt.schema.Schema;
import com.networknt.schema.SchemaRegistry;
import com.networknt.schema.SchemaRegistryConfig;
import com.networknt.schema.SpecificationVersion;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import static org.openprovenance.prov.core.jsonld11.serialization.Constants.*;

/**
 * The schemas the serialisations are held to: the PROV-JSON schema (draft 4, as the W3C submission's, with the
 * membership and mention definitions it lacks) and the PROV-JSONLD schema shipped with the module (draft 7), its
 * 2026-10-14 version, the submission's 2024-08-25 one kept for comparison.
 */
public class Schemas {

    static final ObjectMapper mapper = JsonMapper.builderWithJackson2Defaults().build();

    /** Formats are annotations: {@code format: uri} in the PROV-JSON schema marks a qualified name, and {@code e1} is one. */
    static final SchemaRegistryConfig NO_FORMAT_ASSERTIONS = SchemaRegistryConfig.builder().formatAssertionsEnabled(false).build();

    /** The PROV-JSON schema, in this module's test resources; the PROV-JSONLD schema, in its main resources. */
    public static final Schema PROV_JSON = load(SpecificationVersion.DRAFT_4, "prov-json-schema-v4.json", "src/test/resources/prov-json-schema-v4.json");
    public static final Schema PROV_JSONLD = load(SpecificationVersion.DRAFT_7, JSONLDSCHEMA_2026_10_14, "src/main/resources/" + JSONLDSCHEMA_2026_10_14);
    /** The schema of the W3C member submission, which admits no node reference as an attribute value. */
    public static final Schema PROV_JSONLD_2024_08_25 = load(SpecificationVersion.DRAFT_7, JSONLDSCHEMA_2024_08_25, "src/main/resources/" + JSONLDSCHEMA_2024_08_25);
    /** The PROV-JSONLD schema with the openprov terms admitted, for a document that cites the openprov context. */
    public static final Schema OPENPROV_JSONLD = load(SpecificationVersion.DRAFT_7, OPENPROV_SCHEMA_RESOURCE, "src/main/resources/" + OPENPROV_SCHEMA_RESOURCE);

    /** A factory resolving the published PROV-JSONLD schema, which the openprov schema references, from the module's own copy. */
    /* json-schema-validator 3.x (Jackson 3): the registry replaces the 1.x JsonSchemaFactory, and the
     * configuration (no format assertions) is now per registry rather than per schema. */
    static SchemaRegistry registry(SpecificationVersion version) {
        return SchemaRegistry.withDefaultDialect(version, builder -> builder
                .schemaRegistryConfig(NO_FORMAT_ASSERTIONS)
                .schemaIdResolvers(resolvers -> resolvers.mapPrefix(JSONLD_SCHEMA_URL, "classpath:" + JSONLDSCHEMA_2026_10_14)));
    }

    static Schema load(SpecificationVersion version, String resource, String file) {
        try (InputStream in = open(resource, file)) {
            return registry(version).getSchema(in);
        } catch (IOException e) {
            throw new IllegalStateException("schema not readable: " + resource, e);
        }
    }

    /**
     * A file of this module, by classpath resource when the tests run from another module (against the jars), by
     * path when they run from this module's directory.
     */
    public static InputStream open(String resource, String file) throws IOException {
        InputStream in = Schemas.class.getClassLoader().getResourceAsStream(resource);
        return in != null ? in : new java.io.FileInputStream(file);
    }

    /** The violations of a tree, as messages; none when it conforms. */
    public static List<String> violations(Schema schema, JsonNode tree) {
        List<com.networknt.schema.Error> errors = schema.validate(tree);
        // 3.x: getMessage() no longer holds the instance location; toString() prefixes it, as 1.x getMessage() did
        return errors.stream().map(com.networknt.schema.Error::toString).distinct().sorted().collect(Collectors.toList());
    }

    public static List<String> violations(Schema schema, File file) throws IOException {
        return violations(schema, mapper.readTree(file));
    }

    /** The schema a PROV-JSONLD document is held to: the openprov one when its context cites the openprov context. */
    public static Schema schemaFor(JsonNode document) {
        JsonNode context = document.path("@context");
        for (JsonNode c : context) if (c.isString() && OPENPROV_CONTEXT_URL.equals(c.asString())) return OPENPROV_JSONLD;
        return PROV_JSONLD;
    }

    /** The violations of a PROV-JSONLD document against the schema its context calls for. */
    public static List<String> violations(File file) throws IOException {
        JsonNode tree = mapper.readTree(file);
        return violations(schemaFor(tree), tree);
    }

    public static JsonNode read(String file) throws IOException {
        return mapper.readTree(new File(file));
    }

    public static JsonNode parse(String json) throws IOException {
        return mapper.readTree(json);
    }
}
