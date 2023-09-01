package org.openprovenance.prov.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import org.openprovenance.prov.xml.builder.Equals;
import org.openprovenance.prov.xml.builder.HashCode;
import org.openprovenance.prov.xml.builder.ToString;
import org.openprovenance.prov.model.DOMProcessing;
import org.openprovenance.prov.model.QualifiedName;


/**
 * <p>Java class for Role complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Location"&gt;
 *   &lt;simpleContent&gt;
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;anySimpleType"&gt;
 *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}QName" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/simpleContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Role", namespace = "http://www.w3.org/ns/prov#")
public class Role extends TypedValue implements Equals, HashCode, ToString,
	org.openprovenance.prov.model.Role,
	org.openprovenance.prov.model.Attribute {

    private static final AttributeKind PROV_ROLE_KIND = org.openprovenance.prov.model.Attribute.AttributeKind.PROV_ROLE;
    private static final QualifiedName PROV_ROLE_QualifiedName = ProvFactory.getFactory().getName().PROV_ROLE;

    @Override
    public QualifiedName getElementName() {
	return PROV_ROLE_QualifiedName;
    }

    @Override
    public AttributeKind getKind() {
	return PROV_ROLE_KIND;
    }

    @Override
    public String toNotationString() {
	return DOMProcessing.qualifiedNameToString(getElementName()) + " = "
		+ ProvUtilities.valueToNotationString(getValue(), getType());
    }

}
