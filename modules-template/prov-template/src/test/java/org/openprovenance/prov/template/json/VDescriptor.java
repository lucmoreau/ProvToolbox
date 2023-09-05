package org.openprovenance.prov.template.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class SingleDescriptor implements Descriptor {
    @JsonProperty("@id")
    public String id;
    @JsonProperty("@value")
    public String value;

    @Override
    public String toString() {
        return "SingleDescriptor{" +
                "id='" + id + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
