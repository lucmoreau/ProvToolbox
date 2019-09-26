package org.openprovenance.prov.core.xml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.openprovenance.prov.model.NamespacePrefixMapper;

import javax.xml.datatype.XMLGregorianCalendar;

@JsonPropertyOrder({ "@id", "startTime", "endTime" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface XML_Activity extends Identifiable, HasKind, HasLabel, HasLocation, HasType, HasOther, HasAttributes {


    @JacksonXmlProperty( namespace = NamespacePrefixMapper.PROV_NS)
    XMLGregorianCalendar getStartTime();

    void setStartTime(XMLGregorianCalendar value);

    @JacksonXmlProperty( namespace = NamespacePrefixMapper.PROV_NS)
    XMLGregorianCalendar getEndTime();

    void setEndTime(XMLGregorianCalendar value);



}
