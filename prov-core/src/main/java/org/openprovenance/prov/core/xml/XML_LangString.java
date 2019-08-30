package org.openprovenance.prov.core.xml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import org.openprovenance.prov.core.xml.serialization.Constants;

@JsonPropertyOrder({ "value", "lang" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface XML_LangString {

    @JacksonXmlText
    @JacksonXmlElementWrapper(useWrapping = false)
    @JsonProperty(Constants.PROPERTY_STRING_VALUE)
    @JsonUnwrapped
    public String getValue();

    @JsonProperty(Constants.PROPERTY_STRING_LANG)
    @JacksonXmlProperty(isAttribute = true)
    public String getLang();

}
