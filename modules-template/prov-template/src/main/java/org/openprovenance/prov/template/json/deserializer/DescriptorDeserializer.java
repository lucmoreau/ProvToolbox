package org.openprovenance.prov.template.json.deserializer;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.openprovenance.prov.template.json.SingleDescriptors;
import org.openprovenance.prov.template.json.Descriptor;
import org.openprovenance.prov.template.json.SingleDescriptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DescriptorDeserializer extends  JsonDeserializer<Descriptor> {

    SingleDescriptorDeserializer sdd=new SingleDescriptorDeserializer();
    @Override
    public Descriptor deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
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