package org.openprovenance.prov.sql;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.openprovenance.prov.model.DOMProcessing;
import org.openprovenance.prov.model.ProvUtilities;
import org.openprovenance.prov.model.QualifiedName;

import org.openprovenance.prov.xml.builder.Equals;
import org.openprovenance.prov.xml.builder.HashCode;
import org.openprovenance.prov.xml.builder.ToString;
import org.openprovenance.prov.model.DOMProcessing;
import org.openprovenance.prov.model.QualifiedName;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Key", namespace = "http://www.w3.org/ns/prov#")
@javax.persistence.Entity(name = "Key")
@Table(name = "KEY")
public class Key extends TypedValue implements Equals, HashCode, ToString, org.openprovenance.prov.model.Key,
	org.openprovenance.prov.model.Attribute {

    private static final AttributeKind PROV_KEY_KIND = org.openprovenance.prov.model.Attribute.AttributeKind.PROV_KEY;
    private static final QualifiedName PROV_KEY_QNAME = ProvFactory.getFactory().getName().PROV_KEY;

    @Transient
    public QualifiedName getElementName() {
	return PROV_KEY_QNAME;
    }
    
    @Transient
    public AttributeKind getKind() {
	return PROV_KEY_KIND;
    }
    

    public String toNotationString() {
	return DOMProcessing.qualifiedNameToString(getElementName()) + " = "
		+ ProvUtilities.valueToNotationString(getValue(), getType());
    }
    


}
