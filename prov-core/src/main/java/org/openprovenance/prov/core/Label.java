package org.openprovenance.prov.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.openprovenance.prov.core.serialization.CustomQualifiedNameDeserializer;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.DOMProcessing;
import org.openprovenance.prov.model.ProvUtilities;
import org.openprovenance.prov.model.QualifiedName;

/* Deerialisation requires type to be known to properly parse value. */

@JsonPropertyOrder({ "elementName", "type", "value" })

public class Label extends TypedValue implements org.openprovenance.prov.model.Label, Attribute {

    private static final AttributeKind PROV_LABEL_KIND = AttributeKind.PROV_LABEL;
    private static final QualifiedName PROV_LABEL_QualifiedName = ProvFactory.getFactory().getName().PROV_LABEL;

    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    @Override
    public QualifiedName getElementName() {
        return PROV_LABEL_QualifiedName;
    }

    /* for the purpose of json deserialisation only */
    private void setElementName(QualifiedName q) {}

    @JsonIgnore
    @Override
    public AttributeKind getKind() {
        return PROV_LABEL_KIND;
    }

    @Override
    public String toNotationString() {
        return DOMProcessing.qualifiedNameToString(getElementName()) + " = "
                + ProvUtilities.valueToNotationString(getValue(), getType());
    }

    public Label(QualifiedName type, Object value) {
        super(type, castToStringOrLangStringOrQualifiedName(value, type));
    }

    private Label() {
        super();
    }


}
