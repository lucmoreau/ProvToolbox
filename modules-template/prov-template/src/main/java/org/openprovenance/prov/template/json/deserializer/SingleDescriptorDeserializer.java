package org.openprovenance.prov.template.json.deserializer;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.core.TreeNode;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.deser.std.StdDeserializer;
import org.openprovenance.prov.template.json.QDescriptor;
import org.openprovenance.prov.template.json.SingleDescriptor;
import org.openprovenance.prov.template.json.VDescriptor;

import static org.openprovenance.prov.template.json.QDescriptor.AT_ID;
import static org.openprovenance.prov.template.json.VDescriptor.AT_VALUE;

public class SingleDescriptorDeserializer extends StdDeserializer<SingleDescriptor> {

    protected SingleDescriptorDeserializer() {
        super(SingleDescriptor.class);
    }

    @Override
    public SingleDescriptor deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) {
        JsonNode node = deserializationContext.readTree(jsonParser); // jackson 3: readTreeAsValue takes a JsonNode

        // Select the concrete class based on the existence of a property
        if (node.get(AT_ID) != null) {
            return deserializationContext.readTreeAsValue(node, QDescriptor.class);
        }
        if (node.get(AT_VALUE) != null) {
            return deserializationContext.readTreeAsValue(node, VDescriptor.class);
        }
        throw new IllegalStateException("SingleDescriptorDeserializer.deserialize cannot find suitable properties");
    }

}