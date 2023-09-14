package org.openprovenance.prov.core.json.serialization.deserial;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.openprovenance.prov.vanilla.ProvFactory;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.QualifiedName;

import java.io.IOException;
import java.util.*;

import static org.openprovenance.prov.core.json.serialization.deserial.CustomThreadConfig.JSON_CONTEXT_KEY_NAMESPACE;
import static org.openprovenance.prov.core.json.serialization.deserial.CustomThreadConfig.getAttributes;

public class CustomAttributeMapDeserializer extends StdDeserializer<Map> {


    static final ProvFactory pf=new ProvFactory();


    public CustomAttributeMapDeserializer(JavaType vc) {
        super(vc);

    }

    @Override
    public Map deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException {
        final Namespace ns = getAttributes().get().get(JSON_CONTEXT_KEY_NAMESPACE);

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
                Attribute attr=new CustomAttributeDeserializerWithRootName().deserialize(elementName,next,deserializationContext);
                set.add(attr);
            }
            result.put(elementName,set);
        }


        return result;


    }
}
