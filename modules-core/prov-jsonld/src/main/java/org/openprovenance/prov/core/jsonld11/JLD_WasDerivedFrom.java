package org.openprovenance.prov.core.jsonld11;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.openprovenance.prov.core.jsonld11.serialization.deserial.CustomQualifiedNameDeserializer;
import org.openprovenance.prov.model.QualifiedName;

@JsonPropertyOrder({ "@id", "generatedEntity", "usedEntity", "activity", "generation", "usage" })

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface JLD_WasDerivedFrom extends JLD_Generic2 {

    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    QualifiedName getActivity();

    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    QualifiedName getGeneratedEntity();

    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    QualifiedName getUsedEntity();

    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    QualifiedName getUsage();

    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    QualifiedName getGeneration();

}
