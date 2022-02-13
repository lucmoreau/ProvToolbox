package org.openprovenance.prov.template.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;
import java.util.stream.Collectors;

@JsonPropertyOrder({"@type", "value"})
public class DescriptorAtom implements Descriptor {
    List<String> value;

    public DescriptorAtom(String value) {
        this.value = List.of(value);
    }
    public DescriptorAtom(List<String> value) {
        this.value = value;
    }

    @Override
    public String toText() {
        return value.stream().collect(Collectors.joining(","));
    }

    @Override
    @JsonProperty("@type")
    public String getCategory() {
        return "atom";
    }

    public List<String> getValue() {
        return value;
    }
}
