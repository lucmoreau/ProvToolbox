package org.openprovenance.prov.sql;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import org.openprovenance.prov.xml.builder.Equals;
import org.openprovenance.prov.xml.builder.HashCode;
import org.openprovenance.prov.xml.builder.ToString;
import org.openprovenance.prov.model.DOMProcessing;
import org.openprovenance.prov.model.ProvUtilities;
import org.openprovenance.prov.model.QualifiedName;


/**
 * <p>Java class for Other complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Other">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/ns/prov#>TypedValue">
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Other")
@Entity(name = "Other")
@Table(name = "OTHER")
public class Other
    extends TypedValue
    implements Equals, HashCode, ToString, org.openprovenance.prov.model.Other, org.openprovenance.prov.model.Attribute
{

    QualifiedName elementName;

    
    @ManyToOne(targetEntity = org.openprovenance.prov.sql.QualifiedName.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "ELEMENT")
    public QualifiedName getElementName() {
	return elementName;
    }
    
    public void setElementName(QualifiedName elementName) {
	this.elementName=elementName;
    }

    
    private static final AttributeKind OTHER_KIND = org.openprovenance.prov.model.Attribute.AttributeKind.OTHER;
    

    @Transient    
    public AttributeKind getKind() {
	return OTHER_KIND;
    }

    public String toNotationString() {
        return DOMProcessing.qualifiedNameToString(getElementName()) + " = " + ProvUtilities.valueToNotationString(getValue(), getType());
    }




}
