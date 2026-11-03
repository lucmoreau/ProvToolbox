package org.openprovenance.prov.core.xml;

import com.fasterxml.jackson.annotation.*;
import tools.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import tools.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import tools.jackson.dataformat.xml.annotation.JacksonXmlText;
import org.openprovenance.prov.core.xml.serialization.Constants;
import org.openprovenance.prov.model.NamespacePrefixMapper;

//@JsonPropertyOrder({ "value", "lang" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface XML_LangString {


    @JacksonXmlText
    /*@JacksonXmlElementWrapper(useWrapping = false)
    @JsonProperty(Constants.PROPERTY_AT_VALUE)
    @JsonUnwrapped*/
    public String getValue();

   // @JsonProperty(Constants.PROPERTY_STRING_LANG)
   // @JacksonXmlProperty(localName = Constants.PROPERTY_STRING_LANG, isAttribute = true, namespace = NamespacePrefixMapper.XML_NS)
    @JsonIgnore
    public String getLang();

}
