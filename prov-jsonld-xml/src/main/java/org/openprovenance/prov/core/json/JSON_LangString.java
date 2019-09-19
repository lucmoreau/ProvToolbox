package org.openprovenance.prov.core.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.openprovenance.prov.core.json.serialization.Constants;

@JsonPropertyOrder({ "value", "lang" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface JSON_LangString {


    @JsonProperty(Constants.PROPERTY_STRING_VALUE)
    public String getValue();

    @JsonProperty(Constants.PROPERTY_STRING_LANG)
    public String getLang();

}
