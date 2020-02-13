package org.openprovenance.prov.core.jsonld11;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.openprovenance.prov.core.jsonld11.serialization.deserial.CustomQualifiedNameDeserializer;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.QualifiedName;

@JsonPropertyOrder({ "elementName", "type", "value" })
public interface JLD_Attribute {

    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    QualifiedName getElementName();

    @JsonIgnore
    Attribute.AttributeKind getKind();

    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    QualifiedName getType();

    Object getValue();

    @JsonIgnore
    Object getConvertedValue();

    @JsonIgnore
    void setValueFromObject(Object anObject);

    @JsonIgnore
    void setValueFromObject(org.w3c.dom.Node n);

    @JsonIgnore
    void setValueFromObject(byte[] bytes);

}
