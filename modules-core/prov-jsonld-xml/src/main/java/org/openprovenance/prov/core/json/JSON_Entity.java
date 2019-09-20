package org.openprovenance.prov.core.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "@id" })
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public interface JSON_Entity extends JSON_Agent {

    @JsonIgnore
    public org.openprovenance.prov.model.Value getValue();

}
