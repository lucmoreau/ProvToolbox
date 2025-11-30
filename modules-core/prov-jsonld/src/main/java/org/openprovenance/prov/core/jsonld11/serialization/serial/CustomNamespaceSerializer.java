package org.openprovenance.prov.core.jsonld11.serialization.serial;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.openprovenance.prov.core.jsonld11.serialization.Constants;
import org.openprovenance.prov.model.Namespace;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.openprovenance.prov.core.jsonld11.serialization.Constants.JSONLDCONTEXT_2024_08_25;

public class CustomNamespaceSerializer extends StdSerializer<Namespace> {
    public static final String JSONLD_DEFAULT_NAMESPACE = "@base";
    private final boolean embedContext;
    private final ObjectMapper mapper;

    public CustomNamespaceSerializer(boolean embedContext, ObjectMapper mapper) {
        super(Namespace.class);
        this.embedContext=embedContext;
        this.mapper=mapper;
    }

    /*
    protected CustomNamespaceSerializer(Class<Namespace> t) {
        super(t);
        this.embedContext=false;
    }

     */

    static Map<String, Object> embeddedContext;

    static public Map<String, Object> readContext() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeFactory typeFactory = mapper.getTypeFactory();
        MapType mapType = typeFactory.constructMapType(HashMap.class, String.class, Object.class);
        Map<String,Object> o=mapper.readValue(CustomNamespaceSerializer.class.getResourceAsStream("/" + JSONLDCONTEXT_2024_08_25),mapType);
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
    public void serialize(Namespace namespace, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        Map<String, String> prefixes=new HashMap<>();
        prefixes.putAll(namespace.getPrefixes());
        String defaultNS=namespace.getDefaultNamespace();
        if (defaultNS!=null) {
            prefixes.put(JSONLD_DEFAULT_NAMESPACE,defaultNS);
        }
        Object [] theContext=new Object[2];
        theContext[0]=prefixes;
        if (embedContext) {
            theContext[1]=embeddedContext;
        } else {
            theContext[1]= Constants.JSONLD_CONTEXT_URL;
        }
        // set codec to serialize an array, was required for Mongo-storage.
        jsonGenerator.setCodec(mapper);

        jsonGenerator.writeObject(theContext);
    }
}
