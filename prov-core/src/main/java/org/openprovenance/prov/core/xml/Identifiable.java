package org.openprovenance.prov.core.xml;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.openprovenance.prov.core.xml.serialization.CustomQualifiedNameDeserializer;

public interface Identifiable extends org.openprovenance.prov.model.Identifiable {
    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    @JsonProperty("id")
    @JacksonXmlProperty(isAttribute = true)
    org.openprovenance.prov.model.QualifiedName getId();

    @JsonProperty("id")
    void setId(org.openprovenance.prov.model.QualifiedName value);
}
