package org.openprovenance.prov.template.json.deserializer;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.openprovenance.prov.template.json.Descriptor;
import org.openprovenance.prov.template.json.Descriptors;
import org.openprovenance.prov.template.json.deserializer.DescriptorDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DescriptorsDeserializer extends  JsonDeserializer<Descriptors> {
    final DescriptorDeserializer dd=new DescriptorDeserializer();
    @Override
    public Descriptors deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
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