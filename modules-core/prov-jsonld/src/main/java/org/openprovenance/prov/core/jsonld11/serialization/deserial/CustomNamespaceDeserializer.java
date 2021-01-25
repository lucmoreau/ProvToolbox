package org.openprovenance.prov.core.jsonld11.serialization.deserial;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.openprovenance.prov.model.Namespace;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;


public class CustomNamespaceDeserializer extends StdDeserializer<Namespace> {

    public static final Object CONTEXT_KEY_NAMESPACE = "CONTEXT_KEY_NAMESPACE";
    private final JavaType tr;


    public CustomNamespaceDeserializer(JavaType tr) {
        super(tr);
        this.tr=tr;
    }

    @Override
    public Namespace deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {

        Namespace previous=(Namespace)deserializationContext.getAttribute(CONTEXT_KEY_NAMESPACE);

        Namespace ns=new Namespace();
        if (previous!=null) ns.setParent(previous);  //FIXME: needs a mechanism to restore to previous namespace context when leaving bundle.


        if (jp.isExpectedStartArrayToken()) {
            Object[] objects=jp.readValueAs(Object[].class);
            for (Object o: objects) {
                processContextObject(ns, o);
            }
        }


        deserializationContext.setAttribute(CONTEXT_KEY_NAMESPACE,ns);

        Namespace.withThreadNamespace(ns);

        return ns;
    }

    public void processContextObject(Namespace ns, Object o) {
        if (o instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) o;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                final Object value = entry.getValue();
                if (value instanceof String) {
                    final String valueString = (String) value;
                    final String key = entry.getKey();
                    switch (key) {
                        case "@namespace":
                            ns.setDefaultNamespace(valueString);
                            break;
                        case "@version":
                            break;
                        default:
                            ns.register(key, valueString);
                    }
                }
            }
        }
    }
}
