package org.openprovenance.prov.core.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.openprovenance.prov.core.json.serialization.deserial.CustomQualifiedNameDeserializer;
import org.openprovenance.prov.model.QualifiedName;

import javax.xml.datatype.XMLGregorianCalendar;

@JsonPropertyOrder({ "@id", "prov:activity", "prov:trigger", "prov:starter", "prov:time" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface JSON_WasStartedBy extends JSON_Generic, HasRole {

    @JsonProperty("prov:trigger")
    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getTrigger();

    @JsonProperty("prov:activity")
    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getActivity();


    @JsonProperty("prov:starter")
    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getStarter();

    @JsonProperty("prov:time")
    XMLGregorianCalendar getTime();


}
