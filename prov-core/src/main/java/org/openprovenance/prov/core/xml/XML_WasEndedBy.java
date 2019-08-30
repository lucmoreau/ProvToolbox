package org.openprovenance.prov.core.xml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.openprovenance.prov.core.xml.serialization.CustomQualifiedNameDeserializer;
import org.openprovenance.prov.model.QualifiedName;

import javax.xml.datatype.XMLGregorianCalendar;

@JsonPropertyOrder({ "@id", "activity", "trigger", "ender", "time" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface XML_WasEndedBy extends XML_Generic, HasRole {

    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getTrigger();

    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getActivity();


    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getEnder();

    XMLGregorianCalendar getTime();


}
