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

public class Role extends TypedValue implements org.openprovenance.prov.model.Role, Attribute {

    private static final AttributeKind PROV_ROLE_KIND = AttributeKind.PROV_ROLE;
    private static final QualifiedName PROV_ROLE_QualifiedName = ProvFactory.getFactory().getName().PROV_ROLE;

    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    @Override
    public QualifiedName getElementName() {
        return PROV_ROLE_QualifiedName;
    }

    /* for the purpose of json deserialisation only */
    private void setElementName(QualifiedName q) {}

    @JsonIgnore
    @Override
    public AttributeKind getKind() {
        return PROV_ROLE_KIND;
    }

    @Override
    public String toNotationString() {
        return DOMProcessing.qualifiedNameToString(getElementName()) + " = "
                + ProvUtilities.valueToNotationString(getValue(), getType());
    }

    public Role(QualifiedName type, Object value) {
        super(type, castToStringOrLangStringOrQualifiedName(value));
    }

    private Role() {
        super();
    }


}
