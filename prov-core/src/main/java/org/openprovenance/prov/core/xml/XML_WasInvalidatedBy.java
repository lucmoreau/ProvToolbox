package org.openprovenance.prov.core.xml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.openprovenance.prov.model.NamespacePrefixMapper;
import org.openprovenance.prov.model.QualifiedName;

import javax.xml.datatype.XMLGregorianCalendar;

@JsonPropertyOrder({ "@id", "entity", "activity", "time" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface XML_WasInvalidatedBy extends XML_Generic, HasRole {

    @REF_Qualified_Name
    @JacksonXmlProperty( namespace = NamespacePrefixMapper.PROV_NS)
    public QualifiedName getEntity();

    @REF_Qualified_Name
    @JacksonXmlProperty( namespace = NamespacePrefixMapper.PROV_NS)
    public QualifiedName getActivity();

    @JacksonXmlProperty( namespace = NamespacePrefixMapper.PROV_NS)
    XMLGregorianCalendar getTime();


}
