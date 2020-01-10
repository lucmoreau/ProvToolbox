package org.openprovenance.prov.core.jsonld11;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.xml.datatype.XMLGregorianCalendar;

@JsonPropertyOrder({ "@id", "startTime", "endTime" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface JLD_Activity extends JLD_Identifiable, HasKind, HasLabel, HasLocation, HasType, HasOther, HasAttributes {


    XMLGregorianCalendar getStartTime();

    void setStartTime(XMLGregorianCalendar value);

    XMLGregorianCalendar getEndTime();

    void setEndTime(XMLGregorianCalendar value);



}
