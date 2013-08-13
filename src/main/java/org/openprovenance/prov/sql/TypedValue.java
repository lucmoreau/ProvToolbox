package org.openprovenance.prov.sql;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FetchType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.openprovenance.prov.sql.AValue;
import org.openprovenance.prov.sql.InternationalizedString;
import org.openprovenance.prov.xml.ProvFactory;
import org.openprovenance.prov.model.ValueConverter;

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
@XmlType(name = "TypedValue", namespace = "http://www.w3.org/ns/prov#", propOrder = {
    "value"
    })
//@XmlJavaTypeAdapter(TypedValueAdapter.class)
@javax.persistence.Entity(name = "TypedValue")
@Table(name = "TYPEDVALUE")
@Inheritance(strategy = InheritanceType.JOINED)
public class TypedValue implements org.openprovenance.prov.model.TypedValue {
    
    @XmlValue
    @XmlSchemaType(name = "anySimpleType")
    private Object value;

    @XmlAttribute(name = "type", namespace = "http://www.w3.org/2001/XMLSchema-instance")
    private QName xsdType;

    
    public TypedValue() {
    }
    
    public TypedValue(Object val, QName xsdType) {
 	this.value = val;
 	this.xsdType = xsdType;
     }

   
    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof TypedValue)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final TypedValue that = ((TypedValue) object);
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
            lhsType = this.getType();
            QName rhsType;
            rhsType = that.getType();
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
    public QName getType() {
	return xsdType;
    }

    public void setType(QName type) {
	this.xsdType=type;
    }
    @Basic
    @Column(name = "XSDTYPE")
    public String getXsdTypeItem() { 
        //System.out.println("#---> getXsdTypeItem() reading " + xsdType);
	if (xsdType==null) return null;
	return Attribute.QNameToString(xsdType);
    }

    public void setXsdTypeItem(String name) {
	//System.out.println("#---> setXsdTypeItem() reading " + name);
	if (name!=null) xsdType=Attribute.stringToQName(name);
	//System.out.println("#---> setXsdTypeItem() got " + xsdType);
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
    //@ManyToOne(targetEntity = AValue.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    //@JoinColumn(name = "VALUE")
    @Embedded
    public AValue getValueItem() {
        //System.out.println("#---> getValueItem() reading " + value);

        if ((avalue==null) && (value!=null)) {
            if (xsdType==null) {
		avalue=SQLValueConverter.convertToAValue(vc.getXsdType(value), value); //TODO, I am not using the one saved!
		//System.out.println("##---> getValueItem() reading found " + avalue);
	    } else {
                avalue=SQLValueConverter.convertToAValue(xsdType, vc.convertToJava(xsdType, (String)value));
		//System.out.println("###--> getValueItem() reading found " + avalue);
		//System.out.println("###--> getValueItem() reading found - " + SQLValueConverter.convertFromAValue(avalue));
	    }
	}
        return avalue;
    }
    
    private static final String PERSISTENCE_UNIT_NAME = "org.openprovenance.prov.sql";
   
    /**
     * Sets the value of the test property.
     * 
     * @param value
     *     allowed object is
     *     {@link AValue }
     *     
     */
    public void setValueItem(AValue value) {
        

        
        //System.out.println("#---> setValueItem() reading " + value);
        //System.out.println("#---> setValueItem() reading (id) " + ((value==null)? "none" : value.getHjid()));
        //System.out.println("#---> setValueItem() reading (string) " + ((value==null)? null : value.getString()));
        //System.out.println("#---> setValueItem() reading (int) " + ((value==null)? null : value.getInt()));
       
        
   
        this.avalue=value;
	if (value!=null) {
	    Object o=SQLValueConverter.convertFromAValue(value);
	    if (o!=null)
		this.value = o.toString();
	}
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

    public int hashCode(ObjectLocator locator, HashCodeStrategy strategy) {
        int currentHashCode = 1;
        {
            Object theValue;
            theValue = this.getValue();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "value", theValue), currentHashCode, theValue);
        }
        {
            QName theType;
            theType = this.getType();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "type", theType), currentHashCode, theType);
        }
        return currentHashCode;
    }


    public String toStringDebug() { 
	return "[loc " + value + " " + xsdType + "]";
    }

    transient protected Object valueAsJava;


    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getValueAsJava(ValueConverter vconv) {
    	if (valueAsJava==null) {
    		valueAsJava=vconv.convertToJava(getType(), (String)value);
    	}
        return valueAsJava;
    }


    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setValueAsJava(Object valueAsJava) {
	if (valueAsJava!=null) {
	    if (valueAsJava instanceof QName) {
		QName q=(QName) valueAsJava;
		this.value=q.getPrefix()+":"+q.getLocalPart();
	    } else {
		this.value = valueAsJava.toString();
	    }
	}
        this.valueAsJava = valueAsJava;
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
