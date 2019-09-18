package org.openprovenance.prov.core.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.openprovenance.prov.core.json.serialization.deserial.CustomQualifiedNameDeserializer;
import org.openprovenance.prov.model.QualifiedName;

@JsonPropertyOrder({ "prov:specificEntity", "prov:generalEntity"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface JSON_SpecializationOf extends HasKind {


    @JsonProperty("prov:generalEntity")
    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getGeneralEntity();

    @JsonProperty("prov:specificEntity")
    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getSpecificEntity();

}
