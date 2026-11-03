package org.openprovenance.prov.template.compiler;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import com.networknt.schema.Schema;
import com.networknt.schema.SchemaLocation;
import com.networknt.schema.SchemaRegistry;
import com.networknt.schema.SpecificationVersion;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class JsonSchemaTesting {

    private ObjectMapper mapper = JsonMapper.builderWithJackson2Defaults().build();

    protected JsonNode getJsonNodeFromClasspath(String name) throws IOException {
        InputStream is1 = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(name);
        return mapper.readTree(is1);
    }

    protected JsonNode getJsonNodeFromStringContent(String content) throws IOException {
        return mapper.readTree(content);
    }

    protected JsonNode getJsonNodeFromUrl(String url) throws IOException {
        try (java.io.InputStream in = new URL(url).openStream()) { // jackson 3: no readTree(URL)
            return mapper.readTree(in);
        }
    }

    protected Schema getJsonSchemaFromClasspath(String name) {
        SchemaRegistry factory = SchemaRegistry.withDefaultDialect(SpecificationVersion.DRAFT_4); // json-schema-validator 3.x
        InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(name);
        return factory.getSchema(is);
    }
    protected Schema getJsonSchemaFromClasspathV7(String name) {
        SchemaRegistry factory = SchemaRegistry.withDefaultDialect(SpecificationVersion.DRAFT_7); // json-schema-validator 3.x
        InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(name);
        return factory.getSchema(is);
    }

    protected Schema getJsonSchemaFromFile(String name) throws FileNotFoundException {
        SchemaRegistry factory = SchemaRegistry.withDefaultDialect(SpecificationVersion.DRAFT_4); // json-schema-validator 3.x
        InputStream is = new FileInputStream(name);
        return factory.getSchema(is);
    }

    protected Schema getJsonSchemaFromStringContent(String schemaContent) {
        SchemaRegistry factory = SchemaRegistry.withDefaultDialect(SpecificationVersion.DRAFT_4); // json-schema-validator 3.x
        return factory.getSchema(schemaContent);
    }

    protected Schema getJsonSchemaFromUrl(String uri) throws URISyntaxException {
        SchemaRegistry factory = SchemaRegistry.withDefaultDialect(SpecificationVersion.DRAFT_4); // json-schema-validator 3.x
        return factory.getSchema(SchemaLocation.of(new URI(uri).toString()));
    }

    protected Schema getJsonSchemaFromJsonNode(JsonNode jsonNode) {
        SchemaRegistry factory = SchemaRegistry.withDefaultDialect(SpecificationVersion.DRAFT_4); // json-schema-validator 3.x
        return factory.getSchema(jsonNode);
    }

    // Automatically detect version for given JsonNode
    protected Schema getJsonSchemaFromJsonNodeAutomaticVersion(JsonNode jsonNode) {
        SchemaRegistry factory = SchemaRegistry.withDefaultDialect(SpecificationVersion.fromSchemaNode(jsonNode).orElseThrow()); // json-schema-validator 3.x
        return factory.getSchema(jsonNode);
    }

}