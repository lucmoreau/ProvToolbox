
package org.openprovenance.prov.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import org.jvnet.jaxb2_commons.lang.Equals;
import org.jvnet.jaxb2_commons.lang.HashCode;
import org.jvnet.jaxb2_commons.lang.ToString;
import org.openprovenance.prov.model.DOMProcessing;
import org.openprovenance.prov.model.QualifiedName;


/**
 * <p>Java class for Key complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Key">
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
@XmlType(name = "Key", namespace = "http://www.w3.org/ns/prov#")
public class Key extends TypedValue implements Equals, HashCode, ToString,
	org.openprovenance.prov.model.Key,
	org.openprovenance.prov.model.Attribute {

    private static final AttributeKind PROV_KEY_KIND = org.openprovenance.prov.model.Attribute.AttributeKind.PROV_KEY;
    private static final QualifiedName PROV_KEY_QNAME = ProvFactory.getFactory().getName().QNAME_PROV_KEY;

    
    @Override
    public QualifiedName getElementName() {
	return PROV_KEY_QNAME;
    }

    @Override
    public AttributeKind getKind() {
	return PROV_KEY_KIND;
    }

    @Override
    public String toNotationString() {
	return DOMProcessing.qualifiedNameToString(getElementName()) + " = "
		+ Helper.valueToNotationString(getValue(), getType());
    }



}
