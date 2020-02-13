package org.openprovenance.prov.core.jsonld11.serialization.deserial.attic;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.openprovenance.prov.core.jsonld11.serialization.deserial.CustomAttributeDeserializer;
import org.openprovenance.prov.core.jsonld11.serialization.deserial.CustomNamespaceDeserializer;
import org.openprovenance.prov.vanilla.ProvFactory;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.QualifiedName;

import java.io.IOException;
import java.util.*;

public class CustomAttributeMapDeserializer extends StdDeserializer<Map> {


    static final ProvFactory pf=new ProvFactory();

    static final CustomAttributeDeserializer customAttributeDeserializerWithRootName = new CustomAttributeDeserializer();

    public CustomAttributeMapDeserializer(JavaType vc) {
        super(vc);

    }

    @Override
    public Map deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        System.out.println("CustomAttributeMapDeserializer");


        Namespace ns= (Namespace) deserializationContext.getAttribute(CustomNamespaceDeserializer.CONTEXT_KEY_NAMESPACE);
        Map<QualifiedName, Set<Attribute>> result=new HashMap<>();
        JsonNode node = jp.getCodec().readTree(jp);

        Iterator<Map.Entry<String, JsonNode>> it=node.fields();
        while (it.hasNext()) {
            Map.Entry<String, JsonNode> pair=it.next();
            QualifiedName elementName=ns.stringToQualifiedName(pair.getKey(),pf);
            JsonNode vObj=pair.getValue();
            Iterator<JsonNode> elements=vObj.elements();
            Set<Attribute> set=new HashSet<>();
            while (elements.hasNext()) {
                JsonNode next=elements.next();
                Attribute attr= customAttributeDeserializerWithRootName.deserialize_AttributeValueAndType(elementName,next,deserializationContext);
                set.add(attr);
            }
            result.put(elementName,set);
            //System.out.println(vObj);
        }
        return result;


    }
}
