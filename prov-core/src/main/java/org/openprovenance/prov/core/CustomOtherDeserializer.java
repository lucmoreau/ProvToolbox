package org.openprovenance.prov.core;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.QualifiedName;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class CustomOtherDeserializer extends StdDeserializer<Other> {

static public Namespace theNS;

    private final Namespace ns;
    ProvFactory pf=new ProvFactory();

    public CustomOtherDeserializer() {
        this(Attribute.class);
    }


    public CustomOtherDeserializer(Class<?> vc) {
        super(vc);
        Namespace ns=new Namespace();
        ns.addKnownNamespaces();
        ns.register("ex", "http://example.org/");
        ns.register("ex2", "http://example2.org/");
        this.ns=ns;
        theNS=ns;

    }

    @Override
    public Other deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        //System.out.println(node);
        Map.Entry<String, JsonNode> pair=node.fields().next();

        QualifiedName elementName=ns.stringToQualifiedName(pair.getKey(),pf);
        JsonNode vObj=pair.getValue();

        Iterator<Map.Entry<String, JsonNode>> pairs=vObj.fields();
        String type=pairs.next().getValue().textValue();
        String value=pairs.next().getValue().textValue();

        //System.out.println("Found value " + value);
        //System.out.println("Found type " + type);

        QualifiedName typeQN=ns.stringToQualifiedName(type,pf);
        if (pf.getName().PROV_QUALIFIED_NAME.equals(typeQN)) {
            return new Other(elementName, ns.stringToQualifiedName(type,pf),ns.stringToQualifiedName(value,pf));
        } else { //TODO: need to handle LangString
            return new Other(elementName, ns.stringToQualifiedName(type,pf),value);

        }


    }
}
