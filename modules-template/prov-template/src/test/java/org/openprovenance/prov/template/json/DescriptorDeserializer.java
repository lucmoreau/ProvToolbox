package org.openprovenance.prov.template.json;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DescriptorDeserializer extends  JsonDeserializer<Descriptor> {
    @Override
    public Descriptor deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        System.out.println("*** in DescriptorDeserializer");
        if (jsonParser.isExpectedStartArrayToken()) {
            List<SingleDescriptor> descriptorList = new ArrayList<>();

            while (true) {
                JsonToken jsonToken = jsonParser.nextToken();
                System.out.println("jsonToken = " + jsonToken);
                if (jsonToken == null || jsonToken.isStructEnd()) break;
                SingleDescriptor singleDescriptor = jsonParser.readValueAs(SingleDescriptor.class);
                descriptorList.add(singleDescriptor);
            }
            ArrayDescriptor arrayDescriptor=new ArrayDescriptor();
            arrayDescriptor.values=descriptorList;
            return arrayDescriptor;
        } else {
            // If it's an object, deserialize and return a single descriptor object
            System.out.println("Here");
            SingleDescriptor singleDescriptor = jsonParser.readValueAs(SingleDescriptor.class);
            System.out.println(singleDescriptor);
            return singleDescriptor;
        }
    }
}