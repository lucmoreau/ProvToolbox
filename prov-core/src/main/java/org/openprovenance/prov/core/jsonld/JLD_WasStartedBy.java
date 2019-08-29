package org.openprovenance.prov.core.jsonld;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.openprovenance.prov.core.serialization.CustomQualifiedNameDeserializer;
import org.openprovenance.prov.model.QualifiedName;

import javax.xml.datatype.XMLGregorianCalendar;

@JsonPropertyOrder({ "@id", "activity", "trigger", "starter", "time" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface JLD_WasStartedBy extends JLD_Generic, HasRole {

    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getTrigger();

    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getActivity();


    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getStarter();

    XMLGregorianCalendar getTime();


}
