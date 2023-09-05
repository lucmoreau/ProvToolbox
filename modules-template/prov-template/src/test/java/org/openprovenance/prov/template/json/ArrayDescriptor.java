package org.openprovenance.prov.template.json;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.List;

public class ArrayDescriptor implements Descriptor {
    @JsonValue
    public List<SingleDescriptor> values;

}
