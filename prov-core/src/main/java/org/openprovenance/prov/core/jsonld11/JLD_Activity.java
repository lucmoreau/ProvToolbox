package org.openprovenance.prov.core.jsonld11;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.xml.datatype.XMLGregorianCalendar;

@JsonPropertyOrder({ "@id", "startTime", "endTime" })

public interface JLD_Activity extends Identifiable, HasKind, HasLabel, HasLocation, HasType, HasOther, HasAttributes {


    XMLGregorianCalendar getStartTime();

    void setStartTime(XMLGregorianCalendar value);

    XMLGregorianCalendar getEndTime();

    void setEndTime(XMLGregorianCalendar value);



}
