package org.openprovenance.prov.core.jsonld11;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "@id" })
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public interface JLD_Generic2 extends Identifiable, HasKind, HasLabel, HasType, HasOther, HasAttributes {



}
