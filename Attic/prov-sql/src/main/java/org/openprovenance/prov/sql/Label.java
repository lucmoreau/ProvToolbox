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
 * Java class for Label complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="Label"&gt;
 *   &lt;simpleContent&gt;
 *     &lt;extension base="&lt;http://www.w3.org/ns/prov#&gt;TypedValue"&gt;
 *     &lt;/extension&gt;
 *   &lt;/simpleContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Label")
@Entity(name = "Label")
@Table(name = "LABEL")
public class Label extends TypedValue implements
					  org.openprovenance.prov.model.Label, Equals, HashCode, ToString,
	org.openprovenance.prov.model.Attribute {

    private static final AttributeKind PROV_LABEL_KIND = org.openprovenance.prov.model.Attribute.AttributeKind.PROV_LABEL;
    private static final QualifiedName PROV_LABEL_QualifiedName = ProvFactory.getFactory().getName().PROV_LABEL;

    @Transient
    public QualifiedName getElementName() {
	return PROV_LABEL_QualifiedName;
    }

    @Transient
    public AttributeKind getKind() {
	return PROV_LABEL_KIND;
    }

    public String toNotationString() {
	return DOMProcessing.qualifiedNameToString(getElementName())
		+ " = "
		+ ProvUtilities.valueToNotationString(getValue(),
		                                      getType());
    }

}
