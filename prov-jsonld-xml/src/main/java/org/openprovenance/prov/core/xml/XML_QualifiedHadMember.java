package org.openprovenance.prov.core.xml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.openprovenance.prov.core.xml.serialization.deserial.CustomQualifiedNameDeserializerAsXmlAttribute;
import org.openprovenance.prov.core.xml.serialization.serial.CustomQualifiedNameSerializerAsXmlAttribute;
import org.openprovenance.prov.model.NamespacePrefixMapper;
import org.openprovenance.prov.model.QualifiedName;

import java.util.List;

@JsonPropertyOrder({ "@id", "collection", "entity"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface XML_QualifiedHadMember extends XML_Generic2, XML_Qualified {


    @REF_Qualified_Name
    @JacksonXmlProperty( namespace = NamespacePrefixMapper.PROV_NS)
    public QualifiedName getCollection();

    @JsonSerialize(contentUsing = CustomQualifiedNameSerializerAsXmlAttribute.class)
    @JsonDeserialize(contentUsing = CustomQualifiedNameDeserializerAsXmlAttribute.class)
    @JacksonXmlProperty( namespace = NamespacePrefixMapper.PROV_NS)
    public List<QualifiedName> getEntity();

}
