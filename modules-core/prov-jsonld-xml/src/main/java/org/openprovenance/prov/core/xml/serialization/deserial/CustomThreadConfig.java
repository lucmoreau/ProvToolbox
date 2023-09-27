package org.openprovenance.prov.core.xml.serialization.deserial;

import org.openprovenance.prov.model.Namespace;

import java.util.HashMap;
import java.util.Map;

public class CustomThreadConfig {

    static private final ThreadLocal<Map<String, Namespace>> attributes= ThreadLocal.withInitial(HashMap::new);

    static public ThreadLocal<Map<String, Namespace>> getAttributes() {
        return attributes;
    }




}
