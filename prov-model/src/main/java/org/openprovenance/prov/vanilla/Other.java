package org.openprovenance.prov.vanilla;

import org.openprovenance.apache.commons.lang.builder.EqualsBuilder;
import org.openprovenance.apache.commons.lang.builder.HashCodeBuilder;
import org.openprovenance.apache.commons.lang.builder.ToStringBuilder;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.DOMProcessing;
import org.openprovenance.prov.model.ProvUtilities;
import org.openprovenance.prov.model.QualifiedName;

public class Other extends TypedValue implements org.openprovenance.prov.model.Other, Attribute {

    private static final AttributeKind PROV_OTHER_KIND = AttributeKind.OTHER;


    QualifiedName elementName;

    @Override
    public QualifiedName getElementName() {
        return elementName;
    }

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


    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof Other)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final Other that = ((Other) object);
        equalsBuilder.append(this.getValue(), that.getValue());
        equalsBuilder.append(this.getType(), that.getType());
        equalsBuilder.append(this.getElementName(), that.getElementName());
    }

    public boolean equals(Object object) {
        if (!(object instanceof org.openprovenance.prov.model.Other)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        EqualsBuilder equalsBuilder = new EqualsBuilder();
        equals(object, equalsBuilder);
        return equalsBuilder.isEquals();
    }

    public void hashCode(HashCodeBuilder hashCodeBuilder) {
        hashCodeBuilder.append(this.getValue());
        hashCodeBuilder.append(this.getType());
        hashCodeBuilder.append(this.getElementName());
    }


    public void toString(ToStringBuilder toStringBuilder) {
        {
            Object theValue;
            theValue = this.getValue();
            toStringBuilder.append("name", this.getElementName());
            toStringBuilder.append("value", theValue);
            toStringBuilder.append("DEBUG_type1", theValue.getClass());
        }
        {
            QualifiedName theType;
            theType = this.getType();
            toStringBuilder.append("type", theType);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new ToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }





}
