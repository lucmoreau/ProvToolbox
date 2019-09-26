package org.openprovenance.prov.core.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.xml.datatype.XMLGregorianCalendar;

@JsonPropertyOrder({ "@id", "prov:startTime", "prov:endTime" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface JSON_Activity extends Identifiable, HasKind, HasLabel, HasLocation, HasType, HasOther, HasAttributes {


    @JsonProperty("prov:startTime")
    XMLGregorianCalendar getStartTime();

    void setStartTime(XMLGregorianCalendar value);

    @JsonProperty("prov:endTime")
    XMLGregorianCalendar getEndTime();

    void setEndTime(XMLGregorianCalendar value);



}
