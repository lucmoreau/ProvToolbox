package org.openprovenance.prov.xml;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.openprovenance.prov.sql.AValue;
import org.openprovenance.prov.sql.InternationalizedString;
import org.openprovenance.prov.sql.SQLValueConverter;

import java.util.Collection;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;

import org.jvnet.jaxb2_commons.lang.Equals;
import org.jvnet.jaxb2_commons.lang.EqualsStrategy;
import org.jvnet.jaxb2_commons.lang.HashCode;
import org.jvnet.jaxb2_commons.lang.HashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBEqualsStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBHashCodeStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Location", namespace = "http://www.w3.org/ns/prov#", propOrder = {
    "value"
    })
//@XmlJavaTypeAdapter(LocationAdapter.class)
@javax.persistence.Entity(name = "Location")
@Table(name = "LOCATION")
@Inheritance(strategy = InheritanceType.JOINED)
public class Location {
    
    @XmlValue
    @XmlSchemaType(name = "anySimpleType")
    private Object value;

    @XmlAttribute(name = "type", namespace = "http://www.w3.org/2001/XMLSchema-instance")
    private QName xsdType;

    
    public Location() {
    }
    
    public Location(Object val, QName xsdType) {
 	this.value = val;
 	this.xsdType = xsdType;
     }

   
    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof Location)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final Location that = ((Location) object);
        {
            Object lhsValue;
            lhsValue = this.getValue();
            Object rhsValue;
            rhsValue = that.getValue();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "value", lhsValue), LocatorUtils.property(thatLocator, "value", rhsValue), lhsValue, rhsValue)) {
                return false;
            }
        }
        {
            QName lhsType;
            lhsType = this.getXsdType();
            QName rhsType;
            rhsType = that.getXsdType();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "type", lhsType), LocatorUtils.property(thatLocator, "type", rhsType), lhsType, rhsType)) {
                return false;
            }
        }
        return true;
    }

    public boolean equals(Object object) {
        final EqualsStrategy strategy = JAXBEqualsStrategy.INSTANCE;
        return equals(null, null, object, strategy);
    }



    @Transient
    public QName getXsdType() {
	return xsdType;
    }

    @Basic
    @Column(name = "XSDTYPE")
    public String getXsdTypeItem() { 
        System.out.println("#---> getXsdTypeItem() reading " + xsdType);
	if (xsdType==null) return null;
	return Attribute.QNameToString(xsdType);
    }

    public void setXsdTypeItem(String name) {
	System.out.println("#---> setXsdTypeItem() reading " + name);
	if (name!=null) xsdType=Attribute.stringToQName(name);
	System.out.println("#---> setXsdTypeItem() got " + xsdType);
    }

    

    @Transient
    public Object getValue() {
	return value;
    }

    public void setValue(Object val) {
	this.value=val;
    }

    
    
    static ValueConverter vc=new ValueConverter(ProvFactory.getFactory());
	
    transient AValue avalue;
    
    /**
     * Gets the value of the test property.
     * 
     * @return
     *     possible object is
     *     {@link AValue }
     *     
     */
    @ManyToOne(targetEntity = AValue.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "VALUE_LOCATION_HJID")
    public AValue getValueItem() {
        System.out.println("#---> getValueItem() reading " + value);

        if ((avalue==null) && (value!=null)) {
            if (xsdType==null) {
		avalue=SQLValueConverter.convertToAValue(vc.getXsdType(value), value); //TODO, I am not using the one saved!
	    } else {
                avalue=SQLValueConverter.convertToAValue(xsdType, vc.convertToJava(xsdType, (String)value));
	    }
	}
        return avalue;
    }

    /**
     * Sets the value of the test property.
     * 
     * @param value
     *     allowed object is
     *     {@link AValue }
     *     
     */
    public void setValueItem(AValue value) {
        System.out.println("#---> setValueItem() reading " + value);
        System.out.println("#---> setValueItem() reading " + ((value==null)? null : value.getString()));

        this.avalue=value;
	if (value!=null)
        this.value = SQLValueConverter.convertFromAValue(value).toString();
    }


    @Override
    public int hashCode() {
	int hash = 0;
	if (value != null)
	    hash ^= value.hashCode();
	if (xsdType != null)
	    hash ^= xsdType.hashCode();
	return hash;
    }


    public String toStringDebug() { 
	return "[loc " + value + " " + xsdType + "]";
    }


    @XmlAttribute(name = "Hjid")
    Long hjid;

    @Id
    @Column(name = "HJID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getHjid() {
        return hjid;
    }

    public void setHjid(Long value) {
        this.hjid = value;
    }

}
