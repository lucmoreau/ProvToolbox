package org.openprovenance.prov.core.jsonld11;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.openprovenance.prov.core.jsonld11.serialization.Constants;
import org.openprovenance.prov.core.jsonld11.serialization.deserial.CustomQualifiedNameDeserializer;
import org.openprovenance.prov.core.jsonld11.serialization.serial.CustomAttributeValueSerializer;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.vanilla.Label;
import org.openprovenance.prov.vanilla.Type;


@JsonPropertyOrder({ "elementName", "type", "value" })
@JsonInclude(JsonInclude.Include.NON_NULL)

public interface JLD_Attribute extends Constants {

    @JsonProperty(access= JsonProperty.Access.WRITE_ONLY)
    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    QualifiedName getElementName();

    @JsonIgnore
    Attribute.AttributeKind getKind();

    @JsonProperty(value=PROPERTY_AT_TYPE, access= JsonProperty.Access.WRITE_ONLY)
    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    QualifiedName getType();

    @JsonProperty(PROPERTY_AT_VALUE)
    @JsonSerialize(using = CustomAttributeValueSerializer.class)
    Object getValue();

    @JsonIgnore
    Object getConvertedValue();

    @JsonIgnore
    void setValueFromObject(Object anObject);

    @JsonIgnore
    void setValueFromObject(org.w3c.dom.Node n);

    @JsonIgnore
    void setValueFromObject(byte[] bytes);

    @JsonProperty(PROPERTY_STRING_LANG)
    String getLanguageString();
    @JsonProperty(PROPERTY_AT_TYPE)
    String getTypeString();

}
