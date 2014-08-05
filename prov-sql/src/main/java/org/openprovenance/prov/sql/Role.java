
package org.openprovenance.prov.sql;

import javax.persistence.Entity;
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
 * <p>
 * Java class for Role complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="Role">
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
@XmlType(name = "Role")
@Entity(name = "Role")
@Table(name = "ROLE")
public class Role extends TypedValue implements
					 org.openprovenance.prov.model.Role, Equals, HashCode, ToString,
	org.openprovenance.prov.model.Attribute {

    private static final AttributeKind PROV_ROLE_KIND = org.openprovenance.prov.model.Attribute.AttributeKind.PROV_ROLE;
    private static final QualifiedName PROV_ROLE_QNAME = ProvFactory.getFactory().getName().PROV_ROLE;

    @Transient
    public QualifiedName getElementName() {
	return PROV_ROLE_QNAME;
    }

    @Transient
    public AttributeKind getKind() {
	return PROV_ROLE_KIND;
    }

    public String toNotationString() {
	return DOMProcessing.qualifiedNameToString(getElementName())
		+ " = "
		+ ProvUtilities.valueToNotationString(getValue(),
									      getType());
    }


}
