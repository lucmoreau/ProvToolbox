package org.openprovenance.prov.core.jsonld;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.openprovenance.prov.core.jsonld.serialization.CustomQualifiedNameDeserializer;
import org.openprovenance.prov.model.QualifiedName;

import javax.xml.datatype.XMLGregorianCalendar;

@JsonPropertyOrder({ "@id", "activity_started", "entity", "hadActivity", "atTime" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface JLD_WasStartedBy extends JLD_Generic, HasRole {

    @JsonProperty("entity")
    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getTrigger();

    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    @JsonProperty("activity_started")
    public QualifiedName getActivity();


    @JsonProperty("hadActivity")
    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getStarter();

    @JsonProperty("atTime")
    XMLGregorianCalendar getTime();


}
