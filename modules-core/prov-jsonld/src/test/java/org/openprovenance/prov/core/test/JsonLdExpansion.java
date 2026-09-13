package org.openprovenance.prov.core.test;

import com.apicatalog.jsonld.JsonLd;
import com.apicatalog.jsonld.JsonLdError;
import com.apicatalog.jsonld.JsonLdOptions;
import com.apicatalog.jsonld.document.Document;
import com.apicatalog.jsonld.document.JsonDocument;
import com.apicatalog.jsonld.loader.DocumentLoader;
import com.apicatalog.jsonld.loader.DocumentLoaderOptions;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.openprovenance.prov.core.jsonld11.serialization.Constants.JSONLDCONTEXT_2024_08_25;
import static org.openprovenance.prov.core.jsonld11.serialization.Constants.*;

/**
 * Expands a PROV-JSONLD document with a JSON-LD 1.1 processor, the published context served from the module's copy:
 * what the serialiser writes must be JSON-LD that expands, and expand to PROV terms.
 */
public class JsonLdExpansion {

    static final String PROV_NS = "http://www.w3.org/ns/prov#";

    /** The published contexts, the specification's and openprov's, resolved from the module's own copies, so the check needs no network. */
    static final DocumentLoader LOCAL_CONTEXT = (URI url, DocumentLoaderOptions options) -> {
        String resource;
        if (JSONLD_CONTEXT_URL.equals(url.toString())) resource = JSONLDCONTEXT_2024_08_25;
        else if (OPENPROV_CONTEXT_URL.equals(url.toString())) resource = OPENPROV_CONTEXT_RESOURCE;
        else throw new JsonLdError(com.apicatalog.jsonld.JsonLdErrorCode.LOADING_REMOTE_CONTEXT_FAILED, "not served here: " + url);
        try (InputStream in = Schemas.open(resource, "src/main/resources/" + resource)) {
            return JsonDocument.of(in);
        } catch (IOException e) {
            throw new JsonLdError(com.apicatalog.jsonld.JsonLdErrorCode.LOADING_REMOTE_CONTEXT_FAILED, e);
        }
    };

    public static JsonArray expand(File file) throws IOException, JsonLdError {
        try (InputStream in = new FileInputStream(file)) {
            Document doc = JsonDocument.of(in);
            JsonLdOptions options = new JsonLdOptions();
            options.setDocumentLoader(LOCAL_CONTEXT);
            return JsonLd.expand(doc).options(options).get();
        }
    }

    /** Every node of the expansion whose {@code @type} is not a PROV term, or whose property is in no namespace. */
    public static List<String> problems(JsonArray expanded) {
        List<String> problems = new ArrayList<>();
        for (JsonValue v : expanded) check(v.asJsonObject(), problems);
        return problems;
    }

    static void check(JsonObject node, List<String> problems) {
        JsonValue types = node.get("@type");
        if (types != null) {
            for (JsonValue t : types.asJsonArray()) {
                String type = ((JsonString) t).getString();
                if (!type.startsWith(PROV_NS) && !type.contains("#") && !type.contains("/")) problems.add("type is not an IRI: " + type);
            }
        }
        for (String key : node.keySet()) {
            if (key.startsWith("@")) continue;
            if (!key.contains(":")) problems.add("property is not an IRI: " + key);
            for (JsonValue v : node.get(key).asJsonArray()) {
                if (v.getValueType() == JsonValue.ValueType.OBJECT && v.asJsonObject().containsKey("@id") && v.asJsonObject().size() > 1) {
                    check(v.asJsonObject(), problems);
                }
            }
        }
    }
}
