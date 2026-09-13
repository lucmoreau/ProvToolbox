package org.openprovenance.prov.core.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SchemaValidatorsConfig;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.openprovenance.prov.core.jsonld11.serialization.Constants.JSONLDSCHEMA_2024_08_25;

/**
 * The schemas the serialisations are held to: the PROV-JSON schema (draft 4, as the W3C submission's, with the
 * membership and mention definitions it lacks) and the PROV-JSONLD schema shipped with the module (draft 7).
 */
public class Schemas {

    static final ObjectMapper mapper = new ObjectMapper();

    /** Formats are annotations: {@code format: uri} in the PROV-JSON schema marks a qualified name, and {@code e1} is one. */
    static final SchemaValidatorsConfig NO_FORMAT_ASSERTIONS = SchemaValidatorsConfig.builder().formatAssertionsEnabled(false).build();

    /** The PROV-JSON schema, in this module's test resources; the PROV-JSONLD schema, in its main resources. */
    public static final JsonSchema PROV_JSON = load(SpecVersion.VersionFlag.V4, "prov-json-schema-v4.json", "src/test/resources/prov-json-schema-v4.json");
    public static final JsonSchema PROV_JSONLD = load(SpecVersion.VersionFlag.V7, JSONLDSCHEMA_2024_08_25, "src/main/resources/" + JSONLDSCHEMA_2024_08_25);

    static JsonSchema load(SpecVersion.VersionFlag version, String resource, String file) {
        try (InputStream in = open(resource, file)) {
            return JsonSchemaFactory.getInstance(version).getSchema(in, NO_FORMAT_ASSERTIONS);
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
    public static List<String> violations(JsonSchema schema, JsonNode tree) {
        Set<ValidationMessage> errors = schema.validate(tree);
        return errors.stream().map(ValidationMessage::getMessage).sorted().collect(Collectors.toList());
    }

    public static List<String> violations(JsonSchema schema, File file) throws IOException {
        return violations(schema, mapper.readTree(file));
    }

    public static JsonNode read(String file) throws IOException {
        return mapper.readTree(new File(file));
    }

    public static JsonNode parse(String json) throws IOException {
        return mapper.readTree(json);
    }
}
