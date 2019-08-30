package org.openprovenance.prov.core.jsonld;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.openprovenance.prov.core.jsonld.serialization.Constants;

@JsonPropertyOrder({ "value", "lang" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface JLD_LangString {


    @JsonProperty(Constants.PROPERTY_STRING_VALUE)
    public String getValue();

    @JsonProperty(Constants.PROPERTY_STRING_LANG)
    public String getLang();

}
