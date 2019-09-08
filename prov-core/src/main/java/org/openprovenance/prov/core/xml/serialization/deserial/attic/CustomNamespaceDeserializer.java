package org.openprovenance.prov.core.xml.serialization.deserial.attic;

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


abstract public class CustomNamespaceDeserializer extends StdDeserializer<Namespace> {

    public static final Object CONTEXT_KEY_NAMESPACE = "CONTEXT_KEY_NAMESPACE";

    public CustomNamespaceDeserializer(JavaType tr) {
        super(tr);
    }

    @Override
    public Namespace deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        


        JsonNode map2=jp.readValueAsTree();
        JsonNode prefixes=map2.get("prefixes");
        JsonNode def=map2.get("defaultNamespace");



        Hashtable<String, String> map=new Hashtable<String,String>();
        for (Iterator<String> it = prefixes.fieldNames(); it.hasNext(); ) {
            String prefix = it.next();
            String namespace=prefixes.get(prefix).textValue();
            map.put(prefix,namespace);
        }

        Namespace ns=new Namespace(map);
        ns.setDefaultNamespace(def.textValue());

        deserializationContext.setAttribute(CONTEXT_KEY_NAMESPACE,ns);


        return ns;
    }
}
