package org.openprovenance.prov.scala.jsonld11.serialization.serial;

import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.ser.std.StdSerializer;
import tools.jackson.databind.type.MapType;
import tools.jackson.databind.type.TypeFactory;
import org.openprovenance.prov.model.Namespace;
import tools.jackson.databind.SerializationContext;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CustomNamespaceSerializer extends StdSerializer<Namespace> {
    private final boolean embedContext;

    public CustomNamespaceSerializer(boolean embedContext) {
        super(Namespace.class);
        this.embedContext=embedContext;
    }

    protected CustomNamespaceSerializer(Class<Namespace> t) {
        super(t);
        this.embedContext=false;
    }

    static Map<String, Object> embeddedContext;

    static public Map<String, Object> readContext() throws IOException {
        ObjectMapper mapper = JsonMapper.builderWithJackson2Defaults().build();
        TypeFactory typeFactory = mapper.getTypeFactory();
        MapType mapType = typeFactory.constructMapType(HashMap.class, String.class, Object.class);
        Map<String,Object> o=mapper.readValue(CustomNamespaceSerializer.class.getResourceAsStream("/2024-06-11/context.json"),mapType);
        return o;
    }

    static {
        try {
            embeddedContext=readContext();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void serialize(Namespace namespace, JsonGenerator jsonGenerator, SerializationContext serializerProvider) {
        Map<String, String> prefixes=new HashMap<>();
        prefixes.putAll(namespace.getPrefixes());
        String defaultNS=namespace.getDefaultNamespace();
        if (defaultNS!=null) {
            prefixes.put("@namespace",defaultNS);
        }
        Object [] theContext=new Object[2];
        theContext[0]=prefixes;
        if (embedContext) {
            theContext[1]=embeddedContext;
        } else {
            theContext[1]="http://openprovenance.org/prov.jsonld";
        }
        jsonGenerator.writePOJO(theContext);
    }
}
