package org.openprovenance.prov.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.openprovenance.prov.core.serialization.CustomQualifiedNameDeserializer;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.model.QualifiedName;


@JsonPropertyOrder({ "elementName", "type", "value" })
@JsonTypeName("prov:type")
public class Type extends TypedValue implements org.openprovenance.prov.model.Type, org.openprovenance.prov.model.Attribute {

    private static final AttributeKind PROV_TYPE_KIND = org.openprovenance.prov.model.Attribute.AttributeKind.PROV_TYPE;
    private static final QualifiedName PROV_TYPE_QualifiedName = ProvFactory.getFactory().getName().PROV_TYPE;

    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    @JsonProperty("elementName")
    @Override
    public org.openprovenance.prov.model.QualifiedName getElementName() {
        return PROV_TYPE_QualifiedName;
    }

    /* for the purpose of json deserialisation only */
    private void setElementName(org.openprovenance.prov.model.QualifiedName q) {}

    @JsonIgnore
    @Override
    public AttributeKind getKind() {
        return PROV_TYPE_KIND;
    }

    @Override
    public String toNotationString() {
        return DOMProcessing.qualifiedNameToString(getElementName()) + " = "
                + ProvUtilities.valueToNotationString(getValue(), getType());
    }

    public Type (org.openprovenance.prov.model.QualifiedName type, Object value) {
        super(type, castToStringOrLangStringOrQualifiedName(value,type));
    }


    private Type () {
    }


}
