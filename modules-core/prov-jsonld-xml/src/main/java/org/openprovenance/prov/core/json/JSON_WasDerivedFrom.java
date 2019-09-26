package org.openprovenance.prov.core.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.openprovenance.prov.core.json.serialization.deserial.CustomQualifiedNameDeserializer;
import org.openprovenance.prov.model.QualifiedName;

@JsonPropertyOrder({ "@id", "prov:generatedEntity", "prov:usedEntity", "prov:activity", "prov:generation", "prov:usage" })

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface JSON_WasDerivedFrom extends JSON_Generic2 {

    @JsonProperty("prov:activity")
    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getActivity();

    @JsonProperty("prov:generatedEntity")
    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getGeneratedEntity();

    @JsonProperty("prov:usedEntity")
    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getUsedEntity();

    @JsonProperty("prov:usage")
    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getUsage();


    @JsonProperty("prov:generation")
    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getGeneration();

}
