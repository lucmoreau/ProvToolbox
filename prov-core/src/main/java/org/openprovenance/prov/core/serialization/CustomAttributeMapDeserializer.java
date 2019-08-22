package org.openprovenance.prov.core.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.openprovenance.prov.core.ProvFactory;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.QualifiedName;

import java.io.IOException;
import java.util.*;

public class CustomAttributeMapDeserializer extends StdDeserializer<Map> {

static public Namespace theNS;

    private final Namespace ns;
    ProvFactory pf=new ProvFactory();




    public CustomAttributeMapDeserializer(JavaType vc) {
        super(vc);
        Namespace ns=new Namespace();
        ns.addKnownNamespaces();
        ns.register("ex", "http://example.org/");
        ns.register("ex2", "http://example2.org/");
        this.ns=ns;
        theNS=ns;

    }

    @Override
    public Map deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        Map<QualifiedName, Set<Attribute>> result=new HashMap<>();
        JsonNode node = jp.getCodec().readTree(jp);
        System.out.println(node);
        Iterator<Map.Entry<String, JsonNode>> it=node.fields();
        while (it.hasNext()) {
            Map.Entry<String, JsonNode> pair=it.next();
            QualifiedName elementName=ns.stringToQualifiedName(pair.getKey(),pf);
            JsonNode vObj=pair.getValue();
            Iterator<JsonNode> elements=vObj.elements();
            Set<Attribute> set=new HashSet<>();
            while (elements.hasNext()) {
                JsonNode next=elements.next();
                Attribute attr=new CustomAttributeDeserializer().deserialize(elementName,next,deserializationContext);
                set.add(attr);
            }
            result.put(elementName,set);
            System.out.println(vObj);
        }
        return result;


    }
}
