package org.openprovenance.prov.template.json.deserializer;


import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;
import tools.jackson.databind.DeserializationContext;
import org.openprovenance.prov.template.json.Descriptor;
import org.openprovenance.prov.template.json.Descriptors;
import org.openprovenance.prov.template.json.deserializer.DescriptorDeserializer;
import tools.jackson.databind.ValueDeserializer;

import java.util.ArrayList;
import java.util.List;

public class DescriptorsDeserializer extends  ValueDeserializer<Descriptors> {
    final DescriptorDeserializer dd=new DescriptorDeserializer();
    @Override
    public Descriptors deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) {
        if (jsonParser.isExpectedStartArrayToken()) {
            List<Descriptor> descriptorList = new ArrayList<>();

            while (true) {
                JsonToken jsonToken = jsonParser.nextToken();
                if (jsonToken == null || jsonToken.isStructEnd()) break;
                Descriptor descriptor=dd.deserialize(jsonParser,deserializationContext);
                descriptorList.add(descriptor);
            }
            Descriptors descriptors=new Descriptors();
            descriptors.values=descriptorList;
            return descriptors;
        } else {
            // If it's an object, deserialize and return a single descriptor object
            throw new IllegalStateException("Expected an array");
        }
    }
}