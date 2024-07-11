package org.openprovenance.prov.core.jsonld11;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.openprovenance.prov.core.jsonld11.serialization.Constants;

@JsonPropertyOrder({ "value", "lang" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface JLD_LangString {

    @JsonProperty(Constants.PROPERTY_STRING_VALUE)
    String getValue();

    @JsonProperty(Constants.PROPERTY_STRING_LANG)
    String getLang();

}
