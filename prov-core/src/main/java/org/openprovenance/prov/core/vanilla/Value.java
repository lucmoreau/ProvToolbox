package org.openprovenance.prov.core.vanilla;

import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.DOMProcessing;
import org.openprovenance.prov.model.ProvUtilities;
import org.openprovenance.prov.model.QualifiedName;

/* Deerialisation requires type to be known to properly parse value. */

public class Value extends TypedValue implements org.openprovenance.prov.model.Value, Attribute {

    private static final AttributeKind PROV_VALUE_KIND = AttributeKind.PROV_VALUE;
    private static final QualifiedName PROV_VALUE_QualifiedName = ProvFactory.getFactory().getName().PROV_VALUE;

    @Override
    public QualifiedName getElementName() {
        return PROV_VALUE_QualifiedName;
    }

    /* for the purpose of json deserialisation only */
    private void setElementName(QualifiedName q) {}

    @Override
    public AttributeKind getKind() {
        return PROV_VALUE_KIND;
    }

    @Override
    public String toNotationString() {
        return DOMProcessing.qualifiedNameToString(getElementName()) + " = "
                + ProvUtilities.valueToNotationString(getValue(), getType());
    }

    public Value(QualifiedName type, Object value) {
        super(type, castToStringOrLangStringOrQualifiedName(value,type));
    }

    private Value() {
        super();
    }


}
