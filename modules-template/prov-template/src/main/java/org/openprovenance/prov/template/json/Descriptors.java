package org.openprovenance.prov.template.json;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.openprovenance.prov.template.json.deserializer.DescriptorsDeserializer;


import java.util.List;
import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Descriptors that = (Descriptors) o;
        return Objects.equals(values, that.values);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(values);
    }
}
