package org.openprovenance.prov.scala.jsonld11;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.openprovenance.prov.scala.jsonld11.serialization.deserial.CustomQualifiedNameDeserializer;

public interface Identifiable extends org.openprovenance.prov.model.Identifiable {
    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    @JsonProperty("@id")
    org.openprovenance.prov.model.QualifiedName getId();

    @JsonProperty("@id")
    void setId(org.openprovenance.prov.model.QualifiedName value);
}
