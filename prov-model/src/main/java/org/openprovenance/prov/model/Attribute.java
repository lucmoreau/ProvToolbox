package org.openprovenance.prov.model;

import javax.xml.namespace.QName;

public interface Attribute {

    public enum AttributeKind {
	PROV_TYPE,
	PROV_LABEL,
	PROV_ROLE,
	PROV_LOCATION,
	PROV_VALUE,
	OTHER	
    }

    public abstract QName getQName(AttributeKind kind);

    public abstract AttributeKind getAttributeKind(QName q);

    public abstract QName getElementName();

    public abstract AttributeKind getKind();

    public abstract Object getValue();

    public abstract QName getXsdType();

    /** A method to generate the prov-n representation of an attribute  ex:attr="value" %% xsd:type */

    public abstract String toNotationString();

}
