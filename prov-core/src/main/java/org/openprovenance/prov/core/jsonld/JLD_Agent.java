package org.openprovenance.prov.core.jsonld;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.xml.datatype.XMLGregorianCalendar;

@JsonPropertyOrder({ "@id" })

public interface JLD_Agent extends Identifiable, HasKind, HasLabel, HasLocation, HasType, HasOther, HasAttributes {



}
