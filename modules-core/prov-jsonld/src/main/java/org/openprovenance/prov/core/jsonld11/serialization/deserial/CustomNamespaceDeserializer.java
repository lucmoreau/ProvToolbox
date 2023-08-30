package org.openprovenance.prov.core.jsonld11.serialization.deserial;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.openprovenance.prov.model.Namespace;

import java.io.IOException;
import java.util.Map;

import static org.openprovenance.prov.core.jsonld11.serialization.deserial.CustomThreadConfig.CONTEXT_KEY_NAMESPACE;
import static org.openprovenance.prov.core.jsonld11.serialization.deserial.CustomThreadConfig.getAttributes;


public class CustomNamespaceDeserializer extends StdDeserializer<Namespace> {

    private final JavaType tr;


    public CustomNamespaceDeserializer(JavaType tr) {
        super(tr);
        this.tr=tr;
    }

    // define thread local myAttributes




    @Override
    public Namespace deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException {


        //Namespace previous=(Namespace)deserializationContext.getAttribute(CONTEXT_KEY_NAMESPACE);
        Namespace previous=getAttributes().get().get(CONTEXT_KEY_NAMESPACE);




        Namespace ns=new Namespace();
        if (previous!=null) ns.setParent(previous);  //FIXME: needs a mechanism to restore to previous namespace context when leaving bundle.

       // System.out.println("--> CustomNamespaceDeserializer deserialize: thread " + Thread.currentThread().getId() );


        if (jp.isExpectedStartArrayToken()) {
            Object[] objects=jp.readValueAs(Object[].class);
            for (Object o: objects) {
                processContextObject(ns, o);
            }
        } else {
        }


        //deserializationContext.setAttribute(CONTEXT_KEY_NAMESPACE,ns);

        getAttributes().get().put(CONTEXT_KEY_NAMESPACE,ns);



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
