package org.openprovenance.prov.scala.jsonld11.serialization.deserial;

import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.deser.std.StdDeserializer;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.QualifiedName;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class CustomAttributeSetDeserializer extends StdDeserializer<Set> {




    public CustomAttributeSetDeserializer(JavaType vc) {
        super(vc);
    }

    @Override
    public Set deserialize(JsonParser jp, DeserializationContext deserializationContext) {
        JsonNode node = deserializationContext.readTree(jp) /* jackson 3: the ongoing context, see catalogue #21 */;

        QualifiedName context=(QualifiedName)deserializationContext.getAttribute(CustomKeyDeserializer.PROV_ATTRIBUTE_CONTEXT_KEY);

        Iterator<JsonNode> elements=node.values().iterator();
        Set<Attribute> set=new HashSet<>();
        while (elements.hasNext()) {
            JsonNode next=elements.next();

            Attribute attr;
            if (next.isObject() ) {
                attr = new CustomAttributeDeserializerWithRootName().deserialize(context, next, deserializationContext);
            } else {
                attr = new CustomAttributeDeserializerWithRootName().deserialize(context, next.stringValue(null), deserializationContext);
            }
            set.add(attr);

        }
        return set;
    }

}
