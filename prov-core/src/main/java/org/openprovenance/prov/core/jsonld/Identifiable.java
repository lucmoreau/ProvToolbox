package org.openprovenance.prov.core.jsonld;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.openprovenance.prov.core.jsonld.serialization.CustomQualifiedNameDeserializer;

public interface Identifiable extends org.openprovenance.prov.model.Identifiable {
    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    @JsonProperty("@id")
    org.openprovenance.prov.model.QualifiedName getId();

    @JsonProperty("@id")
    void setId(org.openprovenance.prov.model.QualifiedName value);
}
