package org.openprovenance.prov.template.json;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.List;
import java.util.stream.Collectors;

public class SingleDescriptors implements Descriptor {
    @JsonValue
    public List<SingleDescriptor> values;

    @Override
    public Object toObject() {
        return values.stream().map(Descriptor::toObject).collect(Collectors.toList());
    }
}
