package org.openprovenance.prov.core.json.serialization;

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


public class CustomNamespacePrefixDeserializer extends StdDeserializer<Map<String,String>> {

    public static final Object CONTEXT_KEY_NAMESPACE = "CONTEXT_KEY_NAMESPACE";

    public CustomNamespacePrefixDeserializer(JavaType tr) {
        super(tr);
    }

    public CustomNamespacePrefixDeserializer() {
        super(Map.class);
    }


    @Override
    public Map<String, String> deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {


        System.out.println("---< deserialize prefix");

        JsonNode prefixes=jp.readValueAsTree();

        Namespace ns=(Namespace)deserializationContext.getAttribute(CONTEXT_KEY_NAMESPACE);
        if (ns==null) {
            ns=new Namespace();
            deserializationContext.setAttribute(CONTEXT_KEY_NAMESPACE,ns);
        }


        Hashtable<String, String> map=new Hashtable<String,String>();
        for (Iterator<String> it = prefixes.fieldNames(); it.hasNext(); ) {
            String prefix = it.next();
            String namespace=prefixes.get(prefix).textValue();
            System.out.println(" registering " + prefix + " " + namespace);
            map.put(prefix,namespace);
            ns.register(prefix,namespace);
        }

        System.out.println("---< deserialize prefix " + map);

        return map;
    }
}
