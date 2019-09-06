package org.openprovenance.prov.core.xml;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.openprovenance.prov.core.xml.serialization.CustomQualifiedNameDeserializer;
import org.openprovenance.prov.core.xml.serialization.CustomQualifiedNameSerializer;
import org.openprovenance.prov.model.NamespacePrefixMapper;

public interface Identifiable extends org.openprovenance.prov.model.Identifiable {
    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    @JsonSerialize(using = CustomQualifiedNameSerializer.class)
    @JsonProperty("id")
    @JacksonXmlProperty(isAttribute = true, localName = "id", namespace = NamespacePrefixMapper.PROV_NS)
    org.openprovenance.prov.model.QualifiedName getId();

    @JsonProperty("id")
    void setId(org.openprovenance.prov.model.QualifiedName value);
}
