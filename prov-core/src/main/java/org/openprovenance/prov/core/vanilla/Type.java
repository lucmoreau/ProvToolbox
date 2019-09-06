package org.openprovenance.prov.core.vanilla;


import org.openprovenance.prov.model.*;
import org.openprovenance.prov.model.QualifiedName;


public class Type extends TypedValue implements org.openprovenance.prov.model.Type, org.openprovenance.prov.model.Attribute {

    private static final AttributeKind PROV_TYPE_KIND = org.openprovenance.prov.model.Attribute.AttributeKind.PROV_TYPE;
    private static final QualifiedName PROV_TYPE_QualifiedName = ProvFactory.getFactory().getName().PROV_TYPE;

    @Override
    public org.openprovenance.prov.model.QualifiedName getElementName() {
        return PROV_TYPE_QualifiedName;
    }

    /* for the purpose of json deserialisation only */
    private void setElementName(org.openprovenance.prov.model.QualifiedName q) {}

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
