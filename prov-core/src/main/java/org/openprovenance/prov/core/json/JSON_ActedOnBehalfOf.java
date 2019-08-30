package org.openprovenance.prov.core.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.openprovenance.prov.core.jsonld.serialization.CustomQualifiedNameDeserializer;
import org.openprovenance.prov.model.QualifiedName;

@JsonPropertyOrder({ "@id", "delegate", "responsible", "activity" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface JSON_ActedOnBehalfOf extends JSON_Generic2 {

    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getDelegate();

    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getResponsible();


    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getActivity();



}
