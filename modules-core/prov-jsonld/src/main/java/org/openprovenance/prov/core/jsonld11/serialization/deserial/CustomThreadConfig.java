package org.openprovenance.prov.core.jsonld11.serialization.deserial;

import org.openprovenance.prov.model.Namespace;

import java.util.HashMap;
import java.util.Map;

public class CustomThreadConfig {
    public static final String JSONLD_CONTEXT_KEY_NAMESPACE = "JSONLD_CONTEXT_KEY_NAMESPACE";

    static private final ThreadLocal<Map<String, Namespace>> attributes= ThreadLocal.withInitial(HashMap::new);

    static public ThreadLocal<Map<String, Namespace>> getAttributes() {
        return attributes;
    }
}
