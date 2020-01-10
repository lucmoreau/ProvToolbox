package org.openprovenance.prov.core.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "@id" })
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public interface JSON_Generic extends JSON_Identifiable, HasKind, HasLabel, HasLocation, HasType, HasOther, HasAttributes {



}
