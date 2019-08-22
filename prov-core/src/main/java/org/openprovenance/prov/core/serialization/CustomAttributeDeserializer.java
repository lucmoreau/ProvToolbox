package org.openprovenance.prov.core.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.openprovenance.prov.core.ProvFactory;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.QualifiedName;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class CustomAttributeDeserializer extends StdDeserializer<Attribute> {

static public Namespace theNS;

    private final Namespace ns;
    ProvFactory pf=new ProvFactory();

    public CustomAttributeDeserializer() {
        this(Attribute.class);
    }


    public CustomAttributeDeserializer(Class<?> vc) {
        super(vc);
        Namespace ns=new Namespace();
        ns.addKnownNamespaces();
        ns.register("ex", "http://example.org/");
        ns.register("ex2", "http://example2.org/");
        this.ns=ns;
        theNS=ns;

    }

    @Override
    public Attribute deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        return deserialize(node,deserializationContext);
    }

    public Attribute deserialize(JsonNode node, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {

        Map.Entry<String, JsonNode> pair=node.fields().next();

        QualifiedName elementName=ns.stringToQualifiedName(pair.getKey(),pf);
        JsonNode vObj=pair.getValue();

        return deserialize(elementName,vObj,deserializationContext);
    }

    public Attribute deserialize(QualifiedName elementName, JsonNode vObj, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {


        Iterator<Map.Entry<String, JsonNode>> pairs=vObj.fields();
        Map.Entry<String, JsonNode> pair1=pairs.next();
        Map.Entry<String, JsonNode> pair2=pairs.next();

        String key1=pair1.getKey();
        String key2=pair2.getKey();

        String type=null;
        String value=null;
        if ("@type".equals(key1)   ) type=pair1.getValue().textValue();
        if ("@type".equals(key2)   ) type=pair2.getValue().textValue();

        if ("@value".equals(key1)   ) value=pair1.getValue().textValue();
        if ("@value".equals(key2)   ) value=pair2.getValue().textValue();



        System.out.println("Found @value " + value);
        System.out.println("Found @type " + type);

        QualifiedName typeQN=ns.stringToQualifiedName(type,pf);
        return pf.newAttribute(elementName,value, ns.stringToQualifiedName(type,pf));
    }
}
