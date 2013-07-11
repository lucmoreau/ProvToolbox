package org.openprovenance.prov.xml;
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


@XmlJavaTypeAdapter(LocationAdapter.class)
@javax.persistence.Entity(name = "Location")
@Table(name = "LOCATION")
@Inheritance(strategy = InheritanceType.JOINED)
public class Location {
    
    private Object val;
    private QName xsdType;

    
    public Location() {
    }
    
    public Location(Object val, QName xsdType) {
 	this.val = val;
 	this.xsdType = xsdType;
     }

   
    public boolean equals(Object o) {
	if (o instanceof Location) {
	    Location other = (Location) o;
	    return val.equals(other.val) && xsdType.equals(other.xsdType);

	}
	return false;
    }


    @Transient
    public QName getXsdType() {
	return xsdType;
    }

    //    @Basic
    //    @Column(name = "XSDTYPE")
    public String getXsdTypeItem() { 
        System.out.println("#---> getXsdTypeItem() reading " + xsdType);
	if (xsdType==null) return null;
	return Attribute.QNameToString(xsdType);
    }

    public void setXsdTypeItem(String name) {
	System.out.println("#---> setXsdTypeItem() reading " + name);
	xsdType=Attribute.stringToQName(name);
	System.out.println("#---> setXsdTypeItem() got " + xsdType);
    }

    

    @Transient
    public Object getValue() {
	return val;
    }
    
    ValueConverter vc=new ValueConverter(ProvFactory.getFactory());
	
    AValue avalue;
    
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
        System.out.println("#---> getValueItem() reading " + val);
        if ((avalue==null) && (val!=null)) avalue=SQLValueConverter.convertToAValue(vc.getXsdType(val), val); //TODO, I am not using the one saved!
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
        this.val = SQLValueConverter.convertFromAValue(value);
    }


    @Override
    public int hashCode() {
	int hash = 0;
	if (val != null)
	    hash ^= val.hashCode();
	if (xsdType != null)
	    hash ^= xsdType.hashCode();
	return hash;
    }


    public String toStringDebug() { 
	return "[loc " + val + " " + xsdType + "]";
    }


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
