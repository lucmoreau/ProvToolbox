package org.openprovenance.prov.core.jsonld11;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.openprovenance.prov.core.jsonld11.serialization.CustomQualifiedNameDeserializer;
import org.openprovenance.prov.model.QualifiedName;

@JsonPropertyOrder({ "@id", "delegate", "agent", "hadActivity" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface JLD_ActedOnBehalfOf extends JLD_Generic2 {

    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getDelegate();

    @JsonProperty("agent")
    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getResponsible();

    @JsonProperty("hadActivity")
    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getActivity();



}
