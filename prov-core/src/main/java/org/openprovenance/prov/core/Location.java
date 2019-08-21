package org.openprovenance.prov.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.DOMProcessing;
import org.openprovenance.prov.model.ProvUtilities;
import org.openprovenance.prov.model.QualifiedName;

/* Deerialisation requires type to be known to properly parse value. */

@JsonPropertyOrder({ "elementName", "type", "value" })

public class Location extends TypedValue implements org.openprovenance.prov.model.Location, Attribute {

    private static final AttributeKind PROV_LOCATION_KIND = AttributeKind.PROV_LOCATION;
    private static final QualifiedName PROV_LOCATION_QualifiedName = ProvFactory.getFactory().getName().PROV_LOCATION;

    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    @Override
    public QualifiedName getElementName() {
        return PROV_LOCATION_QualifiedName;
    }

    /* for the purpose of json deserialisation only */
    private void setElementName(org.openprovenance.prov.model.QualifiedName q) {}

    @JsonIgnore
    @Override
    public AttributeKind getKind() {
        return PROV_LOCATION_KIND;
    }

    @Override
    public String toNotationString() {
        return DOMProcessing.qualifiedNameToString(getElementName()) + " = "
                + ProvUtilities.valueToNotationString(getValue(), getType());
    }

    public Location(QualifiedName type, Object value) {
        super(type, castToStringOrLangStringOrQualifiedName(value));
    }

    private Location () {
        super();
    }


}
