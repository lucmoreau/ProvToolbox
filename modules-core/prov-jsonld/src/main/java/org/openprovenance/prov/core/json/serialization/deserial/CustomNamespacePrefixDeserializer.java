package org.openprovenance.prov.core.json.serialization.deserial;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.openprovenance.prov.model.Namespace;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import static org.openprovenance.prov.core.json.serialization.deserial.CustomThreadConfig.JSON_CONTEXT_KEY_NAMESPACE;
import static org.openprovenance.prov.core.json.serialization.deserial.CustomThreadConfig.getAttributes;


public class CustomNamespacePrefixDeserializer extends StdDeserializer<Map<String,String>> {

    public static final Object CONTEXT_KEY_NAMESPACE = "CONTEXT_KEY_NAMESPACE";

    public CustomNamespacePrefixDeserializer(JavaType tr) {
        super(tr);
    }

    public CustomNamespacePrefixDeserializer() {
        super(Map.class);
    }


    @Override
    public Map<String, String> deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException {


        JsonNode prefixes=jp.readValueAsTree();


        Namespace parentNs = getAttributes().get().get(JSON_CONTEXT_KEY_NAMESPACE);

        Namespace ns=new Namespace();
        ns.setParent(parentNs);
        getAttributes().get().put(JSON_CONTEXT_KEY_NAMESPACE,ns);



        Hashtable<String, String> map= new Hashtable<>();
        for (Iterator<String> it = prefixes.fieldNames(); it.hasNext(); ) {
            String prefix = it.next();
            String namespace=prefixes.get(prefix).textValue();
            map.put(prefix,namespace);
            ns.register(prefix,namespace);
        }

        // now context is ready with current namespace

        return map;
    }
}
