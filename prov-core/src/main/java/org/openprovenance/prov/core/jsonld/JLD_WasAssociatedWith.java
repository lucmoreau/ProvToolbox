package org.openprovenance.prov.core.jsonld;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.openprovenance.prov.core.jsonld.serialization.CustomQualifiedNameDeserializer;
import org.openprovenance.prov.model.QualifiedName;

@JsonPropertyOrder({ "@id", "activity_associated", "agent", "hadPlan" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface JLD_WasAssociatedWith extends JLD_Generic2, HasRole {

    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getAgent();


    @JsonProperty("activity_associated")
    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getActivity();

    @JsonProperty("hadPlan")
    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getPlan();


}
