package org.openprovenance.prov.sql;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.ManyToOne;

import org.openprovenance.prov.sql.AValue;
import org.openprovenance.prov.model.LangString;
import org.openprovenance.prov.sql.ProvFactory;
import org.openprovenance.prov.model.Attribute.AttributeKind;
import org.openprovenance.prov.model.DOMProcessing;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.ValueConverter;

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
//@Inheritance(strategy = InheritanceType.JOINED)
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)

public class TypedValue implements org.openprovenance.prov.model.TypedValue {
    private static final QualifiedName QNAME_PROV_TYPE = ProvFactory.getFactory().getName().PROV_TYPE;
    private static final QualifiedName QNAME_PROV_LABEL = ProvFactory.getFactory().getName().PROV_LABEL;
    private static final QualifiedName QNAME_PROV_VALUE = ProvFactory.getFactory().getName().PROV_VALUE;
    private static final QualifiedName QNAME_PROV_LOCATION = ProvFactory.getFactory().getName().PROV_LOCATION;
    private static final QualifiedName QNAME_PROV_ROLE = ProvFactory.getFactory().getName().PROV_ROLE;
    private static final QualifiedName QNAME_XSD_HEX_BINARY = ProvFactory.getFactory().getName().XSD_HEX_BINARY;
    private static final QualifiedName QNAME_XSD_BASE64_BINARY = ProvFactory.getFactory().getName().XSD_BASE64_BINARY;
    
    
    @XmlValue
    @XmlSchemaType(name = "anySimpleType")
    protected Object value;
    
    @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(QNameAdapter.class)
    @XmlAttribute(name = "type", namespace = "http://www.w3.org/2001/XMLSchema-instance")
    protected QualifiedName type;

    
    public TypedValue() {
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
            QualifiedName lhsType;
            lhsType = this.getType();
            QualifiedName rhsType;
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



    
    @ManyToOne(targetEntity = org.openprovenance.prov.sql.QualifiedName.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "TYPE")
    public QualifiedName getType() {
	return type;
    }

    public void setType(QualifiedName type) {
	this.type=type;
    }
   

    

    @Transient
    public Object getValue() {
	return value;
    }

    public void setValue(String value) {
 	this.value=value;
    }

    public void setValue(QualifiedName value) {
 	this.value=value;
    }

    public void setValue(LangString value) {
 	this.value=value;
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
    @Embedded
    public AValue getValueItem() {

    	if ((avalue==null) && (value!=null)) {
    		if (type==null) {
    			avalue=SQLValueConverter.convertToAValue(vc.getXsdType(value), value);
    		} else if (value instanceof LangString) {
    			avalue=SQLValueConverter.convertToAValue(type,  ((LangString) value).getValue());
    		} else if (value instanceof org.openprovenance.prov.model.QualifiedName) {
    			avalue=SQLValueConverter.convertToAValue(type,  (QualifiedName) value);
    		} else {
    			avalue=SQLValueConverter.convertToAValue(type, vc.convertToJava(type, (String)value));
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
            QualifiedName theType;
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
    public Object convertValueToObject(ValueConverter vconv) {
    	if (valueAsJava==null) {
    		valueAsJava=vconv.convertToJava(getType(), (String)value);
    	}
        return valueAsJava;
    }
    @Transient
    public Object getConvertedValue() {
        return valueAsJava;
    }

    
    /** Converts a byte array in base64 or hexadecimal according to specified type. 
     * 
     * @param bytes array of bytes to convert
     */

    public void setValueAsJava(final byte[] bytes) {
	if (type.equals(QNAME_XSD_BASE64_BINARY)) {
	    this.value=ProvFactory.getFactory().base64Encoding(bytes);
	} else if (type.equals(QNAME_XSD_HEX_BINARY)) {
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
    public void setValueFromObject(Object valueAsJava) {
 	if ((valueAsJava!=null) && (value==null)) {
 	    if (valueAsJava instanceof QualifiedName) { 
 		this.value=valueAsJava;
 	    } else if (valueAsJava instanceof LangString) { 
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


    @XmlAttribute(name = "pk")
    Long pk;

    @Id
    @Column(name = "PK")
    //    @GeneratedValue(strategy = GenerationType.AUTO)
    @GeneratedValue(strategy = GenerationType.TABLE)
    // see http://stackoverflow.com/questions/916169/cannot-use-identity-column-key-generation-with-union-subclass-table-per-clas

    public Long getPk() {
        return pk;
    }

    public void setPk(Long value) {
        this.pk = value;
    }
    

    
    public QualifiedName getQualifiedName(AttributeKind kind) {
        switch (kind) {
        case  PROV_TYPE: return QNAME_PROV_TYPE;
        case  PROV_LABEL: return QNAME_PROV_LABEL;
        case  PROV_VALUE: return QNAME_PROV_VALUE;
        case  PROV_LOCATION: return QNAME_PROV_LOCATION;
        case  PROV_ROLE: return QNAME_PROV_ROLE;
        case OTHER:
        default: 
                return null;
        }
    }
    
    /* (non-Javadoc)
     * @see org.openprovenance.prov.xml.AttrIN#getAttributeKind(javax.xml.namespace.QName)
     */
    
    public AttributeKind getAttributeKind(QualifiedName q) {
        if (q.equals(QNAME_PROV_TYPE)) return AttributeKind.PROV_TYPE;
        if (q.equals(QNAME_PROV_LABEL)) return AttributeKind.PROV_LABEL;
        if (q.equals(QNAME_PROV_VALUE)) return AttributeKind.PROV_VALUE;
        if (q.equals(QNAME_PROV_LOCATION)) return AttributeKind.PROV_LOCATION;
        if (q.equals(QNAME_PROV_ROLE)) return AttributeKind.PROV_ROLE;
        return AttributeKind.OTHER;
    }




}
