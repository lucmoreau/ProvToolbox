package org.openprovenance.prov.template.json;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.openprovenance.prov.template.json.deserializer.DescriptorsDeserializer;


import java.util.List;
import java.util.stream.Collectors;

@JsonDeserialize(using = DescriptorsDeserializer.class)
public class Descriptors {

    @JsonValue
    public List<Descriptor> values;

    @Override
    public String toString() {
        return "Descriptors{" +
                "values=" + values +
                '}';
    }

    public List<Object> toList() {
        return values.stream().map(Descriptor::toObject).collect(Collectors.toList());
    }
}
