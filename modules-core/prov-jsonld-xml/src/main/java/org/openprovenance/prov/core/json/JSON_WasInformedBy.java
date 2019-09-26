package org.openprovenance.prov.core.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.openprovenance.prov.core.json.serialization.deserial.CustomQualifiedNameDeserializer;
import org.openprovenance.prov.model.QualifiedName;

@JsonPropertyOrder({ "@id", "prov:informed", "prov:informant"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface JSON_WasInformedBy extends JSON_Generic2 {

    @JsonProperty("prov:informed")
    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getInformed();

    @JsonProperty("prov:informant")
    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getInformant();


}
