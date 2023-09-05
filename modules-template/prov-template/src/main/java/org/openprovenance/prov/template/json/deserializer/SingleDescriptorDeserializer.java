package org.openprovenance.prov.template.json.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.openprovenance.prov.template.json.QDescriptor;
import org.openprovenance.prov.template.json.SingleDescriptor;
import org.openprovenance.prov.template.json.VDescriptor;

import java.io.IOException;

import static org.openprovenance.prov.template.json.QDescriptor.AT_ID;
import static org.openprovenance.prov.template.json.VDescriptor.AT_VALUE;

public class SingleDescriptorDeserializer extends StdDeserializer<SingleDescriptor> {

    protected SingleDescriptorDeserializer() {
        super(SingleDescriptor.class);
    }

    @Override
    public SingleDescriptor deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        TreeNode node = jsonParser.readValueAsTree();

        // Select the concrete class based on the existence of a property
        if (node.get(AT_ID) != null) {
            return jsonParser.getCodec().treeToValue(node, QDescriptor.class);
        }
        if (node.get(AT_VALUE) != null) {
            return jsonParser.getCodec().treeToValue(node, VDescriptor.class);
        }
        throw new IllegalStateException("SingleDescriptorDeserializer.deserialize cannot find suitable properties");
    }

}