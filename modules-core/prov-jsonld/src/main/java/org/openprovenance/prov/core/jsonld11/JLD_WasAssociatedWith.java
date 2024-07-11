package org.openprovenance.prov.core.jsonld11;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.openprovenance.prov.core.jsonld11.serialization.deserial.CustomQualifiedNameDeserializer;
import org.openprovenance.prov.model.QualifiedName;

@JsonPropertyOrder({ "@id", "activity", "agent", "plan" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface JLD_WasAssociatedWith extends JLD_Generic2, HasRole {

    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    QualifiedName getAgent();


    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    QualifiedName getActivity();

    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    QualifiedName getPlan();


}
