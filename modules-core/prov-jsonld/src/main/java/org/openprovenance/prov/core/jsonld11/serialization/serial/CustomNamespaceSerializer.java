package org.openprovenance.prov.core.jsonld11.serialization.serial;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.openprovenance.prov.core.jsonld11.serialization.Constants;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.exception.UncheckedException;

import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.openprovenance.prov.core.jsonld11.serialization.Constants.JSONLDCONTEXT_2024_08_25;
import static org.openprovenance.prov.core.jsonld11.serialization.Constants.OPENPROV_CONTEXT_RESOURCE;

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


    /** Set on the writer by the serialiser when the document uses openprov terms: the context cited is then openprov's. */
    public static final String USES_OPENPROV = "USES_OPENPROV";

    static List<Object> openprovExtension;

    /** The second element of the openprov context: what it adds to the PROV-JSONLD context. */
    static public List<Object> readOpenprovExtension() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> o = mapper.readValue(CustomNamespaceSerializer.class.getResourceAsStream("/" + OPENPROV_CONTEXT_RESOURCE),
                mapper.getTypeFactory().constructMapType(HashMap.class, String.class, Object.class));
        @SuppressWarnings("unchecked")
        List<Object> context = (List<Object>) o.get("@context");
        return context.subList(1, context.size());
    }

    static {
        try {
            openprovExtension = readOpenprovExtension();
        } catch (IOException e) {
            throw new UncheckedException(e);
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
        boolean usesOpenprov = Boolean.TRUE.equals(serializerProvider.getAttribute(USES_OPENPROV));
        List<Object> theContext = new ArrayList<>();
        theContext.add(prefixes);
        if (embedContext) {
            theContext.add(embeddedContext);
            if (usesOpenprov) theContext.addAll(openprovExtension);
        } else {
            theContext.add(usesOpenprov ? Constants.OPENPROV_CONTEXT_URL : Constants.JSONLD_CONTEXT_URL);
        }
        // set codec to serialize an array, was required for Mongo-storage.
        jsonGenerator.setCodec(mapper);

        jsonGenerator.writeObject(theContext.toArray());
    }
}
