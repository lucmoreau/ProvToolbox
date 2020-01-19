package org.openprovenance.prov.core.jsonld11.serialization.deserial;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.openprovenance.prov.vanilla.ProvFactory;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.QualifiedName;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class CustomAttributeSetDeserializer extends StdDeserializer<Set> {


    static final ProvFactory pf=new ProvFactory();


    public CustomAttributeSetDeserializer(JavaType vc) {
        super(vc);
    }

    @Override
    public Set deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);

        QualifiedName context=(QualifiedName)deserializationContext.getAttribute(CustomKeyDeserializer.PROV_ATTRIBUTE_CONTEXT_KEY);

        Iterator<JsonNode> elements=node.elements();
        Set<Attribute> set=new HashSet<>();
        while (elements.hasNext()) {
            JsonNode next=elements.next();

            Attribute attr;
            if (next.isObject() ) {
                attr = new CustomAttributeDeserializerWithRootName().deserialize(context, next, deserializationContext);
            } else {
                attr = new CustomAttributeDeserializerWithRootName().deserialize(context, next.textValue(), deserializationContext);
            }
            set.add(attr);

        }
        return set;
    }

}
