package org.openprovenance.prov.core.xml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.openprovenance.prov.model.NamespacePrefixMapper;
import org.openprovenance.prov.model.QualifiedName;

@JsonPropertyOrder({ "@id", "alternate1", "alternate2"})
@JsonInclude(JsonInclude.Include.NON_NULL)

public interface XML_QualifiedAlternateOf extends XML_Generic2, XML_Qualified {

    @REF_Qualified_Name
    @JacksonXmlProperty( namespace = NamespacePrefixMapper.PROV_NS)
    public QualifiedName getAlternate2();

    @REF_Qualified_Name
    @JacksonXmlProperty( namespace = NamespacePrefixMapper.PROV_NS)
    public QualifiedName getAlternate1();

}
