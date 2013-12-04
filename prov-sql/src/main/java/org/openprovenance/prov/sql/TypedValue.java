package org.openprovenance.prov.sql;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

import javax.persistence.Basic;
import javax.persistence.Embedded;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.openprovenance.prov.sql.AValue;
import org.openprovenance.prov.model.InternationalizedString;
import org.openprovenance.prov.xml.ProvFactory;
import org.openprovenance.prov.model.Attribute.AttributeKind;
import org.openprovenance.prov.model.DOMProcessing;
import org.openprovenance.prov.model.Name;
import org.openprovenance.prov.model.QNameConstructor;
import org.openprovenance.prov.model.ValueConverter;

import javax.xml.namespace.QName;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.jvnet.jaxb2_commons.lang.EqualsStrategy;
import org.jvnet.jaxb2_commons.lang.HashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBEqualsStrategy;
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
    protected Object value;

    @XmlAttribute(name = "type", namespace = "http://www.w3.org/2001/XMLSchema-instance")
    protected QName type;

    
    public TypedValue() {
    }
    
    public TypedValue(Object val, QName xsdType) {
 	this.value = val;
 	this.type = xsdType;
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
	return type;
    }

    public void setType(QName type) {
	this.type=type;
    }
    @Basic
    @Column(name = "XSDTYPE")
    public String getTypeItem() { 
	if (type==null) return null;
	return Helper2.QNameToString(type);
    }

    public void setTypeItem(String name) {
	if (name!=null) type=Helper2.stringToQName(name);
    }

    

    @Transient
    public Object getValue() {
	return value;
    }

    public void setValue(Object value) {
	this.value=value;
    }

    
    
    static ValueConverter vc=new ValueConverter(ProvFactory.getFactory(),
    		new QNameConstructor() {

				@Override
				public Object newQName(String value) {
				    System.out.println("QNameConstructo.newQName() " + value);
					return new QName(value); //FIXME
				}
    	
    });
	
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
    		if (type==null) {
    			avalue=SQLValueConverter.convertToAValue(vc.getXsdType(value), value); //TODO, I am not using the one saved!
    			//System.out.println("##---> getValueItem() reading found " + avalue);
    		} else if (value instanceof InternationalizedString) {
    			avalue=SQLValueConverter.convertToAValue(type,  ((InternationalizedString) value).getValue());
    		} else if (value instanceof QName) {
    			avalue=SQLValueConverter.convertToAValue(type,  (QName) value);
    		} else if (value instanceof org.openprovenance.prov.model.QualifiedName) {
    			avalue=SQLValueConverter.convertToAValue(type,  (QualifiedName) value);
    		} else {
    			avalue=SQLValueConverter.convertToAValue(type, vc.convertToJava(type, (String)value));
    			//System.out.println("###--> getValueItem() reading found " + avalue);
    			//System.out.println("###--> getValueItem() reading found - " + SQLValueConverter.convertFromAValue(avalue));
    		}
    	}
    	return avalue;
    }
    
    //private static final String PERSISTENCE_UNIT_NAME = "org.openprovenance.prov.sql";
   
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
	if (type != null)
	    hash ^= type.hashCode();
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
	return "[loc " + value + " " + type + "]";
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
    public Object getValueAsObject(ValueConverter vconv) {
    	if (valueAsJava==null) {
    		valueAsJava=vconv.convertToJava(getType(), (String)value);
    	}
        return valueAsJava;
    }
    @Transient
    public Object getValueAsObject() {
        return valueAsJava;
    }


    
    /** Converts a byte array in base64 or hexadecimal according to specified type. 
     * 
     * @param bytes array of bytes to convert
     */

    public void setValueAsJava(final byte[] bytes) {
	if (type.equals(Name.QNAME_XSD_BASE64_BINARY)) {
	    this.value=ProvFactory.getFactory().base64Encoding(bytes);
	} else if (type.equals(Name.QNAME_XSD_HEX_BINARY)) {
	    this.value=ProvFactory.getFactory().hexEncoding(bytes);
	}
    }


    /** Converts a DOM into a string representation, after "normalizing" it.
     * 
     * @param n DOM Node to convert.
     */
    public void setValueAsJava(org.w3c.dom.Node n) {
	DOMProcessing.trimNode(n);
	try {
	    this.value=DOMProcessing.writeToString(n);
	} catch (TransformerConfigurationException e) {
	    this.value=n.toString();  // TODO: not the most compelling handling
	} catch (TransformerException e) {
	    this.value=n.toString(); //TODO: not the most compelling handling
	}
    }
    

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setValueAsObject(Object valueAsJava) {
 	if ((valueAsJava!=null) && (value==null)) {
 	    if (valueAsJava instanceof QName) {
 		this.value=valueAsJava;
 	    } else if (valueAsJava instanceof QualifiedName) { 
 		this.value=valueAsJava;
 	    } else if (valueAsJava instanceof InternationalizedString) { 
 		this.value=valueAsJava;
 	    } else if (valueAsJava instanceof byte[]) {
 		setValueAsJava((byte[]) valueAsJava);
 	    } else if (valueAsJava instanceof org.w3c.dom.Node) {
 		setValueAsJava((org.w3c.dom.Node)valueAsJava);
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
    

    
    public QName getQName(AttributeKind kind) {
        switch (kind) {
        case  PROV_TYPE: return Name.QNAME_PROV_TYPE;
        case  PROV_LABEL: return Name.QNAME_PROV_LABEL;
        case  PROV_VALUE: return Name.QNAME_PROV_VALUE;
        case  PROV_LOCATION: return Name.QNAME_PROV_LOCATION;
        case  PROV_ROLE: return Name.QNAME_PROV_ROLE;
        case OTHER:
        default: 
                return null;
        }
    }
    
    /* (non-Javadoc)
     * @see org.openprovenance.prov.xml.AttrIN#getAttributeKind(javax.xml.namespace.QName)
     */
    
    public AttributeKind getAttributeKind(QName q) {
        if (q.equals(Name.QNAME_PROV_TYPE)) return AttributeKind.PROV_TYPE;
        if (q.equals(Name.QNAME_PROV_LABEL)) return AttributeKind.PROV_LABEL;
        if (q.equals(Name.QNAME_PROV_VALUE)) return AttributeKind.PROV_VALUE;
        if (q.equals(Name.QNAME_PROV_LOCATION)) return AttributeKind.PROV_LOCATION;
        if (q.equals(Name.QNAME_PROV_ROLE)) return AttributeKind.PROV_ROLE;
        return AttributeKind.OTHER;
    }




}
