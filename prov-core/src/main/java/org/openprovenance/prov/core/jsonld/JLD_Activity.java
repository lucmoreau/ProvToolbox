package org.openprovenance.prov.core.jsonld;

import com.fasterxml.jackson.annotation.*;

import javax.xml.datatype.XMLGregorianCalendar;

@JsonPropertyOrder({ "@id", "startTime", "endTime" })

public interface JLD_Activity extends Identifiable, HasKind, HasLabel, HasLocation, HasType, HasOther, HasAttributes {


    XMLGregorianCalendar getStartTime();

    void setStartTime(XMLGregorianCalendar value);

    XMLGregorianCalendar getEndTime();

    void setEndTime(XMLGregorianCalendar value);



}
