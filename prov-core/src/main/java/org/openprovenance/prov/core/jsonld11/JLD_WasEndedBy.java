package org.openprovenance.prov.core.jsonld11;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.openprovenance.prov.core.jsonld11.serialization.CustomQualifiedNameDeserializer;
import org.openprovenance.prov.model.QualifiedName;

import javax.xml.datatype.XMLGregorianCalendar;

@JsonPropertyOrder({ "@id", "activity_ended", "entity", "hadActivity", "atTime" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface JLD_WasEndedBy extends JLD_Generic, HasRole {

    @JsonProperty("entity")
    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getTrigger();

    @JsonProperty("activity_ended")
    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getActivity();


    @JsonProperty("hadActivity")
    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getEnder();

    @JsonProperty("atTime")
    XMLGregorianCalendar getTime();


}
