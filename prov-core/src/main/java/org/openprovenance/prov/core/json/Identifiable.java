package org.openprovenance.prov.core.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.openprovenance.prov.core.json.serialization.deserial.CustomQualifiedNameDeserializer;

public interface Identifiable extends org.openprovenance.prov.model.Identifiable {
    @JsonIgnore
    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    @JsonProperty("@id")
    org.openprovenance.prov.model.QualifiedName getId();

    @JsonIgnore
    @JsonProperty("@id")
    void setId(org.openprovenance.prov.model.QualifiedName value);
}
