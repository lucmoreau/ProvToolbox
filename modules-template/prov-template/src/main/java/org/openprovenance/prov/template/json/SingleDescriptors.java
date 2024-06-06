package org.openprovenance.prov.template.json;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SingleDescriptors implements Descriptor {
    @JsonValue
    public List<SingleDescriptor> values;

    @Override
    public Object toObject() {
        return values.stream().map(Descriptor::toObject).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "SingleDescriptors{" +
                "values=" + values +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SingleDescriptors that = (SingleDescriptors) o;
        return Objects.equals(values, that.values);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(values);
    }
}
