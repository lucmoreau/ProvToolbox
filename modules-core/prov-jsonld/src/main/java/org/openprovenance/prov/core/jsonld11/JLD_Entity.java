package org.openprovenance.prov.core.jsonld11;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "@id" })
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public interface JLD_Entity extends JLD_Agent {

    @JsonIgnore
    org.openprovenance.prov.model.Value getValue();

}
