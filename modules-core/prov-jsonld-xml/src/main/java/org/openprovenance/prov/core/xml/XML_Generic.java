package org.openprovenance.prov.core.xml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "@id" })
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public interface XML_Generic extends Identifiable, HasKind, HasLabel, HasLocation, HasType, HasOther, HasAttributes {



}
