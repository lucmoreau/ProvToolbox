package org.openprovenance.prov.scala.jsonld11.serialization.deserial;

import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.deser.std.StdDeserializer;
import org.openprovenance.prov.scala.immutable.ProvFactory;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.QualifiedName;

import java.util.*;

import static org.openprovenance.prov.core.jsonld11.serialization.Constants.ID_COERCED_ATTRIBUTE_TERMS;

public class CustomAttributeMapDeserializer extends StdDeserializer<Map> {


    static final ProvFactory pf=new ProvFactory();


    public CustomAttributeMapDeserializer(JavaType vc) {
        super(vc);

    }

    @Override
    public Map deserialize(JsonParser jp, DeserializationContext deserializationContext) {
        Namespace ns= (Namespace) deserializationContext.getAttribute(CustomNamespaceDeserializer.CONTEXT_KEY_NAMESPACE);
        Map<QualifiedName, Set<Attribute>> result=new HashMap<>();
        JsonNode node = deserializationContext.readTree(jp) /* jackson 3: the ongoing context, see catalogue #21 */;

        Iterator<Map.Entry<String, JsonNode>> it=node.properties().iterator();
        while (it.hasNext()) {
            Map.Entry<String, JsonNode> pair=it.next();
            QualifiedName elementName=ns.stringToQualifiedName(pair.getKey(),pf);
            deserializationContext.setAttribute(CustomKeyDeserializer.ID_COERCED_CONTEXT_KEY, ID_COERCED_ATTRIBUTE_TERMS.contains(pair.getKey()));
            JsonNode vObj=pair.getValue();
            Iterator<JsonNode> elements=vObj.values().iterator();
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
