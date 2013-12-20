package org.openprovenance.prov.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import org.openprovenance.prov.xml.builder.Equals;
import org.openprovenance.prov.xml.builder.HashCode;
import org.openprovenance.prov.xml.builder.ToString;
import org.openprovenance.prov.xml.builder.JAXBEqualsBuilder;
import org.openprovenance.prov.xml.builder.JAXBHashCodeBuilder;
import org.openprovenance.prov.xml.builder.JAXBToStringBuilder;
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
@XmlType(name = "Other", namespace = "http://www.w3.org/ns/prov#")
public class Other extends TypedValue
    implements Equals, HashCode, ToString, org.openprovenance.prov.model.Other, org.openprovenance.prov.model.Attribute
{

    private static final AttributeKind OTHER_KIND = org.openprovenance.prov.model.Attribute.AttributeKind.OTHER;
 
    QualifiedName elementName;
    
    @Override
    public QualifiedName getElementName() {
	return elementName;
    }
    
    public void setElementName(QualifiedName elementName) {
	this.elementName=elementName;
    }

    @Override
    public AttributeKind getKind() {
	return OTHER_KIND;
    }

    @Override
    public String toNotationString() {
        return DOMProcessing.qualifiedNameToString(getElementName()) + " = " + ProvUtilities.valueToNotationString(getValue(), getType());
    }
   
}
