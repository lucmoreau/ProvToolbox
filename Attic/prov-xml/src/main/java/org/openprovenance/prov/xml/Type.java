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
 * <p>Java class for Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Type"&gt;
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
@XmlType(name = "Type", namespace = "http://www.w3.org/ns/prov#")

public class Type extends TypedValue implements Equals, HashCode, ToString,
	org.openprovenance.prov.model.Type,
	org.openprovenance.prov.model.Attribute {

    private static final AttributeKind PROV_TYPE_KIND = org.openprovenance.prov.model.Attribute.AttributeKind.PROV_TYPE;
    private static final QualifiedName PROV_TYPE_QualifiedName = ProvFactory.getFactory().getName().PROV_TYPE;

    @Override
    public QualifiedName getElementName() {
	return PROV_TYPE_QualifiedName;
    }

    @Override
    public AttributeKind getKind() {
	return PROV_TYPE_KIND;
    }

    @Override
    public String toNotationString() {
	return DOMProcessing.qualifiedNameToString(getElementName()) + " = "
		+ ProvUtilities.valueToNotationString(getValue(), getType());
    }

}
