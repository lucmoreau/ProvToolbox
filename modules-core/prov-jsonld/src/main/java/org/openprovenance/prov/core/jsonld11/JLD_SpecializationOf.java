package org.openprovenance.prov.core.jsonld11;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.openprovenance.prov.core.jsonld11.serialization.deserial.CustomQualifiedNameDeserializer;
import org.openprovenance.prov.model.QualifiedName;

@JsonPropertyOrder({ "specificEntity", "generalEntity"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface JLD_SpecializationOf extends HasKind {


    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    QualifiedName getGeneralEntity();

    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    QualifiedName getSpecificEntity();

}
