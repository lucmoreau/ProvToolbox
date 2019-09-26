package org.openprovenance.prov.core.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.openprovenance.prov.core.json.serialization.deserial.CustomQualifiedNameDeserializer;
import org.openprovenance.prov.model.QualifiedName;

@JsonPropertyOrder({ "@id", "prov:entity", "prov:agent"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface JSON_WasAttributedTo extends JSON_Generic2 {

    @JsonProperty("prov:agent")
    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getAgent();

    @JsonProperty("prov:entity")
    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getEntity();


}
