package org.openprovenance.prov.core.xml.serialization.attic;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.openprovenance.prov.vanilla.ProvFactory;
import org.openprovenance.prov.core.xml.serialization.ProvDeserialiser;
import org.openprovenance.prov.core.xml.serialization.deserial.CustomAttributeDeserializerWithRootName;
import org.openprovenance.prov.core.xml.serialization.deserial.DeserializerUtil;
import org.openprovenance.prov.core.xml.serialization.deserial.attic.CustomNamespaceDeserializer;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.QualifiedName;

import java.io.IOException;
import java.util.*;


abstract public class CustomAttributeMapDeserializer extends StdDeserializer<Map> {


    private static final ProvFactory pf= ProvDeserialiser.pf;

    private final TypeReference tr=new TypeReference<Map<QualifiedName,Set<Attribute>>>(){};



    public CustomAttributeMapDeserializer(JavaType vc) {
        super(vc);

    }

    @Override
    public Map deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        Namespace ns= (Namespace) deserializationContext.getAttribute(CustomNamespaceDeserializer.CONTEXT_KEY_NAMESPACE);
        Map<QualifiedName, Set<Attribute>> result=new HashMap<>();
        System.out.println("#######  found " + jp.readValueAs(tr));
        JsonNode node = jp.getCodec().readTree(jp);

        Iterator<Map.Entry<String, JsonNode>> it=node.fields();
        while (it.hasNext()) {
            Map.Entry<String, JsonNode> pair=it.next();
            QualifiedName elementName= DeserializerUtil.unescapeQualifiedName(ns.stringToQualifiedName(pair.getKey(),pf));

            JsonNode vObj=pair.getValue();
            Iterator<JsonNode> elements=vObj.elements();
            Set<Attribute> set=new HashSet<>();
            while (elements.hasNext()) {
                JsonNode next=elements.next();
                Attribute attr=new CustomAttributeDeserializerWithRootName().deserialize(elementName,next,deserializationContext);
                set.add(attr);
            }
            result.put(elementName,set);
        }
        return result;


    }
}
