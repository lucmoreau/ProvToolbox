package org.openprovenance.prov.core.xml;

import com.fasterxml.jackson.annotation.JsonProperty;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.openprovenance.prov.core.xml.serialization.deserial.CustomQualifiedNameDeserializer;
import org.openprovenance.prov.core.xml.serialization.serial.CustomQualifiedNameSerializer;
import org.openprovenance.prov.model.NamespacePrefixMapper;
import org.openprovenance.prov.model.QualifiedName;

public interface Identifiable extends org.openprovenance.prov.model.Identifiable {
    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    @JsonSerialize(using = CustomQualifiedNameSerializer.class)
    @JsonProperty("id")
    @JacksonXmlProperty(isAttribute = true, localName = "id", namespace = NamespacePrefixMapper.PROV_NS)
    QualifiedName getId();

    @JsonProperty("id")
    void setId(QualifiedName value);
}
