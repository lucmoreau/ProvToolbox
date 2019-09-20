package org.openprovenance.prov.sql;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.openprovenance.prov.xml.builder.Equals;
import org.openprovenance.prov.xml.builder.HashCode;
import org.openprovenance.prov.xml.builder.ToString;
import org.openprovenance.prov.model.DOMProcessing;
import org.openprovenance.prov.model.ProvUtilities;
import org.openprovenance.prov.model.QualifiedName;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Location", namespace = "http://www.w3.org/ns/prov#")
@javax.persistence.Entity(name = "Location")
@Table(name = "LOCATION")
public class Location extends TypedValue implements Equals, HashCode, ToString, org.openprovenance.prov.model.Location,
	org.openprovenance.prov.model.Attribute {

    private static final AttributeKind PROV_LOCATION_KIND = org.openprovenance.prov.model.Attribute.AttributeKind.PROV_LOCATION;
    private static final QualifiedName PROV_LOCATION_QualifiedName = ProvFactory.getFactory().getName().PROV_LOCATION;

    @Transient
    public QualifiedName getElementName() {
	return PROV_LOCATION_QualifiedName;
    }
    
    @Transient
    public AttributeKind getKind() {
	return PROV_LOCATION_KIND;
    }
    

    public String toNotationString() {
	return DOMProcessing.qualifiedNameToString(getElementName()) + " = "
		+ ProvUtilities.valueToNotationString(getValue(), getType());
    }
    

}
