package org.openprovenance.prov.core.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.openprovenance.prov.core.jsonld.serialization.CustomQualifiedNameDeserializer;
import org.openprovenance.prov.model.QualifiedName;

@JsonPropertyOrder({ "@id", "generatedEntity", "usedEntity", "activity", "generation", "usage" })

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface JSON_WasDerivedFrom extends JSON_Generic2 {

    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getActivity();

    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getGeneratedEntity();

    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getUsedEntity();

    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getUsage();


    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getGeneration();

}
