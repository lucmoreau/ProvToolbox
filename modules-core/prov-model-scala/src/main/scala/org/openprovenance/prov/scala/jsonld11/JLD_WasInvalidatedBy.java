package org.openprovenance.prov.scala.jsonld11;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.openprovenance.prov.scala.jsonld11.serialization.deserial.CustomQualifiedNameDeserializer;
import org.openprovenance.prov.model.QualifiedName;

import javax.xml.datatype.XMLGregorianCalendar;

@JsonPropertyOrder({ "@id", "entity", "activity", "time" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface JLD_WasInvalidatedBy extends JLD_Generic, HasRole {

    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getEntity();

    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getActivity();

    XMLGregorianCalendar getTime();


}
