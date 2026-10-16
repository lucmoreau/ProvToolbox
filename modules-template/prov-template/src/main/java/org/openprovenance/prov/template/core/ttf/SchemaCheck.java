package org.openprovenance.prov.template.core.ttf;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SchemaLocation;
import com.networknt.schema.SchemaValidatorsConfig;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import org.openprovenance.prov.core.jsonld11.serialization.Constants;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * A TTF's {@code schemaCheck}: the version of the PROV-JSONLD schema a generated {@code jsonld} output is validated
 * against, among the versions the prov-jsonld artifact bundles. {@code "none"} (or no value) checks nothing,
 * {@code "default"} is the version ProvToolbox writes, and a date such as {@code "2024-08-25"} names a bundled
 * version. A {@code -qn} suffix ({@code "default-qn"}, {@code "2026-10-14-qn"}) selects a version's strict variant,
 * which also enforces the syntax of a qualified name. A document citing the openprov context is validated against
 * the openprov schema generated from that version, any other against the version's schema.
 */
public final class SchemaCheck {

    public static final String NONE = "none";
    public static final String DEFAULT = "default";
    public static final String QN = Constants.JSONLD_SCHEMA_QN_SUFFIX;

    static final ObjectMapper mapper = new ObjectMapper();

    /** Formats are annotations: {@code format: date-time} is not asserted, as in prov-jsonld's own checks. */
    static final SchemaValidatorsConfig CONFIG = SchemaValidatorsConfig.builder().formatAssertionsEnabled(false).build();

    /** One compiled schema per version and kind (spec or openprov); tasks may run concurrently. */
    static final Map<String, JsonSchema> schemas = new ConcurrentHashMap<>();

    private SchemaCheck() {
    }

    /**
     * The bundled version a schemaCheck value names, or null when nothing is to be checked.
     * @throws IllegalArgumentException for a value that is neither none, default nor a bundled version
     */
    public static String version(String value) {
        if (value == null || NONE.equals(value)) return null;
        if (DEFAULT.equals(value)) return Constants.JSONLD_SCHEMA_DEFAULT_VERSION;
        if (Constants.JSONLD_SCHEMA_VERSIONS.contains(value)) return value;
        boolean strict = value.endsWith(QN);
        String base = strict ? value.substring(0, value.length() - QN.length()) : value;
        if (strict && DEFAULT.equals(base)) base = Constants.JSONLD_SCHEMA_DEFAULT_VERSION;
        if (strict && Constants.JSONLD_SCHEMA_QN_VERSIONS.contains(base)) return base + QN;
        throw new IllegalArgumentException("schemaCheck '" + value + "': not one of " + String.join(", ", values()));
    }

    /** The values a schemaCheck may take. */
    public static List<String> values() {
        List<String> values = new java.util.ArrayList<>(List.of(NONE, DEFAULT));
        if (Constants.JSONLD_SCHEMA_QN_VERSIONS.contains(Constants.JSONLD_SCHEMA_DEFAULT_VERSION)) values.add(DEFAULT + QN);
        values.addAll(Constants.JSONLD_SCHEMA_VERSIONS);
        for (String v : Constants.JSONLD_SCHEMA_QN_VERSIONS) values.add(v + QN);
        return values;
    }

    /** The schemaCheck that applies to a task: its own if it has one, else its batch's. */
    public static String effective(String batchValue, String taskValue) {
        return taskValue != null ? taskValue : batchValue;
    }

    /** The violations of a PROV-JSONLD file against a bundled version of the schema, as sorted distinct messages; none when it conforms. */
    public static List<String> violations(File file, String version) throws IOException {
        JsonNode document = mapper.readTree(file);
        return schema(version, citesOpenprov(document)).validate(document).stream()
                .map(ValidationMessage::getMessage).distinct().sorted().collect(Collectors.toList());
    }

    static boolean citesOpenprov(JsonNode document) {
        for (JsonNode c : document.path("@context")) {
            if (c.isTextual() && Constants.OPENPROV_CONTEXT_URL.equals(c.asText())) return true;
        }
        return Constants.OPENPROV_CONTEXT_URL.equals(document.path("@context").asText());
    }

    static JsonSchema schema(String version, boolean openprov) {
        return schemas.computeIfAbsent(version + (openprov ? "/openprov" : "/spec"), k -> {
            // the schemas known by their published URLs, so the openprov schema's references resolve to this version;
            // a strict variant has its own $id, against which its internal references resolve: known by both
            String spec = read(Constants.jsonldSchemaResource(version));
            Map<String, String> known = new java.util.HashMap<>();
            known.put(Constants.JSONLD_SCHEMA_URL, spec);
            known.put(Constants.OPENPROV_SCHEMA_URL, read(Constants.openprovSchemaResource(version)));
            known.put(id(spec), spec);
            JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7, builder ->
                    builder.schemaLoaders(loaders -> loaders.schemas(known)));
            return factory.getSchema(SchemaLocation.of(openprov ? Constants.OPENPROV_SCHEMA_URL : id(spec)), CONFIG);
        });
    }

    static String id(String schema) {
        try {
            return mapper.readTree(schema).path("$id").asText(Constants.JSONLD_SCHEMA_URL);
        } catch (IOException e) {
            throw new IllegalStateException("schema not parsable", e);
        }
    }

    static String read(String resource) {
        try (InputStream in = Constants.class.getResourceAsStream("/" + resource)) {
            if (in == null) throw new IllegalStateException("schema not bundled: " + resource);
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("schema not readable: " + resource, e);
        }
    }
}
