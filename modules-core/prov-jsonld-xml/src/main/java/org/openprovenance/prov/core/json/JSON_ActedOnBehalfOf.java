package org.openprovenance.prov.core.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.openprovenance.prov.core.json.serialization.deserial.CustomQualifiedNameDeserializer;
import org.openprovenance.prov.model.QualifiedName;

@JsonPropertyOrder({ "@id", "prov:delegate", "prov:responsible", "prov:activity" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface JSON_ActedOnBehalfOf extends JSON_Generic2 {

    @JsonProperty("prov:delegate")
    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getDelegate();

    @JsonProperty("prov:responsible")
    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getResponsible();


    @JsonProperty("prov:activity")
    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getActivity();



}
