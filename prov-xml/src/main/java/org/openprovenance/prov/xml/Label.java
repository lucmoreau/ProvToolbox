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
 * <p>Java class for Location complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Location">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>anySimpleType">
 *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}QName" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Role", namespace = "http://www.w3.org/ns/prov#")
public class Label extends TypedValue implements Equals, HashCode, ToString,
	org.openprovenance.prov.model.Label,
	org.openprovenance.prov.model.Attribute {

    private static final AttributeKind PROV_LABEL_KIND = org.openprovenance.prov.model.Attribute.AttributeKind.PROV_LABEL;
    private static final QualifiedName PROV_LABEL_QNAME = ProvFactory.getFactory().getName().PROV_LABEL;

    @Override
    public QualifiedName getElementName() {
	return PROV_LABEL_QNAME;
    }

    @Override
    public AttributeKind getKind() {
	return PROV_LABEL_KIND;
    }

    @Override
    public String toNotationString() {
	return DOMProcessing.qualifiedNameToString(getElementName()) + " = "
		+ ProvUtilities.valueToNotationString(getValue(), getType());
    }

}
