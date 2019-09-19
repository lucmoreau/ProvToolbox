package org.openprovenance.prov.core.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.openprovenance.prov.core.json.serialization.deserial.CustomQualifiedNameDeserializer;
import org.openprovenance.prov.model.QualifiedName;

@JsonPropertyOrder({ "@id", "prov:activity", "prov:agent", "prov:plan" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface JSON_WasAssociatedWith extends JSON_Generic2, HasRole {

    @JsonProperty("prov:agent")
    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getAgent();

    @JsonProperty("prov:activity")
    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getActivity();

    @JsonProperty("prov:plan")
    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getPlan();


}
