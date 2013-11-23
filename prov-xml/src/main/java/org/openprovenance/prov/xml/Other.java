package org.openprovenance.prov.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;
import org.jvnet.jaxb2_commons.lang.Equals;
import org.jvnet.jaxb2_commons.lang.HashCode;
import org.jvnet.jaxb2_commons.lang.ToString;
import org.openprovenance.prov.model.DOMProcessing;


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
@XmlType(name = "Other", namespace = "http://www.w3.org/ns/prov#")
public class Other extends TypedValue
    implements Equals, HashCode, ToString, org.openprovenance.prov.model.Other, org.openprovenance.prov.model.Attribute
{

    private static final AttributeKind OTHER_KIND = org.openprovenance.prov.model.Attribute.AttributeKind.OTHER;
 
    QName elementName;
    
    @Override
    public QName getElementName() {
	return elementName;
    }
    
    public void setElementName(QName elementName) {
	this.elementName=elementName;
    }

    @Override
    public AttributeKind getKind() {
	return OTHER_KIND;
    }

    @Override
    public String toNotationString() {
        return DOMProcessing.qnameToString(getElementName()) + " = " + Helper.valueToNotationString(getValue(), getType());
    }
   
}
