package org.openprovenance.prov.sql;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.datatype.XMLGregorianCalendar;

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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.openprovenance.prov.xml.builder.Equals;
import org.openprovenance.prov.xml.builder.HashCode;
import org.openprovenance.prov.xml.builder.ToString;
import org.openprovenance.prov.xml.builder.JAXBEqualsBuilder;
import org.openprovenance.prov.xml.builder.JAXBHashCodeBuilder;
import org.openprovenance.prov.xml.builder.JAXBToStringBuilder;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TypedValue", namespace = "http://www.w3.org/ns/prov#", propOrder = {
    "value"
    })
//@XmlJavaTypeAdapter(TypedValueAdapter.class)
@javax.persistence.Entity(name = "TypedValue")
@Table(name = "TYPEDVALUE")
//@Inheritance(strategy = InheritanceType.JOINED)
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)

public class TypedValue implements Equals, 
				   ToString, org.openprovenance.prov.model.TypedValue {
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
    
   
    //TODO: need to check types.

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof TypedValue)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final TypedValue that = ((TypedValue) object);      
        equalsBuilder.append(this.getValue(), that.getValue());
        equalsBuilder.append(this.getType(), that.getType());
    }
   
    public boolean equals(Object object) {
        if (!(object instanceof TypedValue)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final EqualsBuilder equalsBuilder = new JAXBEqualsBuilder();
        equals(object, equalsBuilder);
        return equalsBuilder.isEquals();
    }
    public void hashCode(HashCodeBuilder hashCodeBuilder) {}

    
    /*
    public void hashCode(HashCodeBuilder hashCodeBuilder) {
        hashCodeBuilder.append(this.getValue());
        hashCodeBuilder.append(this.getType());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }
    */
    public void toString(ToStringBuilder toStringBuilder) {
        {
            Object theValue;
            theValue = this.getValue();
            toStringBuilder.append("value", theValue);
        }
        {
            QualifiedName theType;
            theType = this.getType();
            toStringBuilder.append("type", theType);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
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
	    if (o!=null) {
	    	if (o instanceof QualifiedName) {
	    		this.value=o;
	    	} else {
	    		this.value = o.toString();
	    	}
	    }
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
     * @param valueAsJava
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
