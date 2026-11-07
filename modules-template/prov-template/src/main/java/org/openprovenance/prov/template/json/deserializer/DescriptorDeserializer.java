package org.openprovenance.prov.template.json.deserializer;


import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;
import tools.jackson.databind.DeserializationContext;
import org.openprovenance.prov.template.json.SingleDescriptors;
import tools.jackson.databind.ValueDeserializer;
import org.openprovenance.prov.template.json.Descriptor;
import org.openprovenance.prov.template.json.SingleDescriptor;

import java.util.ArrayList;
import java.util.List;

public class DescriptorDeserializer extends  ValueDeserializer<Descriptor> {

    SingleDescriptorDeserializer sdd=new SingleDescriptorDeserializer();
    @Override
    public Descriptor deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) {
        if (jsonParser.isExpectedStartArrayToken()) {
            List<SingleDescriptor> descriptorList = new ArrayList<>();
            while (true) {
                JsonToken jsonToken = jsonParser.nextToken();
                if (jsonToken == null || jsonToken.isStructEnd()) break;
                SingleDescriptor singleDescriptor = sdd.deserialize(jsonParser, deserializationContext);

                descriptorList.add(singleDescriptor);
            }
            SingleDescriptors singleDescriptors =new SingleDescriptors();
            singleDescriptors.values=descriptorList;
            return singleDescriptors;
        } else {
            // If it's an object, deserialize and return a single descriptor object
            return sdd.deserialize(jsonParser, deserializationContext);
        }
    }
}