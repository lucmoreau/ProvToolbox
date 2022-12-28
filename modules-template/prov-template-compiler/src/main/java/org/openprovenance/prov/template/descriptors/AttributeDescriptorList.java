package org.openprovenance.prov.template.descriptors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.List;

public class AttributeDescriptorList implements Descriptor{
    List<AttributeDescriptor> items;

    @JsonValue
    public List<AttributeDescriptor> getItems() {
        return items;
    }

    public void setItems(List<AttributeDescriptor> items) {
        this.items = items;
    }

    @JsonIgnore
    public DescriptorTypes getDescriptorType () {
        return DescriptorTypes.ATTRIBUTE;
    }
}
