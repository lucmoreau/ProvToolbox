package org.openprovenance.prov.core.vanilla;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.openprovenance.prov.core.serialization.CustomQualifiedNameDeserializer;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.DOMProcessing;
import org.openprovenance.prov.model.ProvUtilities;
import org.openprovenance.prov.model.QualifiedName;

@JsonPropertyOrder({ "elementName", "value", "type" })

public class Other extends TypedValue implements org.openprovenance.prov.model.Other, Attribute {

    private static final AttributeKind PROV_OTHER_KIND = AttributeKind.OTHER;


    QualifiedName elementName;

    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    @Override
    public QualifiedName getElementName() {
        return elementName;
    }

    @JsonIgnore
    @Override
    public AttributeKind getKind() {
        return PROV_OTHER_KIND;
    }

    @Override
    public void setElementName(QualifiedName elementName) {
        this.elementName=elementName;
    }


    @Override
    public String toNotationString() {
        return DOMProcessing.qualifiedNameToString(getElementName()) + " = "
                + ProvUtilities.valueToNotationString(getValue(), getType());
    }

    public Other(QualifiedName elementName, QualifiedName type, Object value) {
        super(type,castToStringOrLangStringOrQualifiedName(value, type));
        this.elementName=elementName;
    }
    private Other() {
        super();
    }




}
