package org.openprovenance.prov.template.json;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;



import java.util.List;

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
}
