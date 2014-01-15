package org.openprovenance.prov.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
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
import org.openprovenance.prov.model.DOMProcessing;
import org.openprovenance.prov.model.Attribute.AttributeKind;
import org.openprovenance.prov.model.QualifiedName;

/**
 * <p>Java class for Location complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Location">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>anySimpleType">
 *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}QName" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TypedValue", namespace = "http://www.w3.org/ns/prov#", propOrder = {
   "value"
})
public class TypedValue   

    implements Equals, HashCode, ToString, org.openprovenance.prov.model.TypedValue
{
    

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
    
    @XmlAttribute(name = "type", namespace = "http://www.w3.org/2001/XMLSchema-instance")
    protected QualifiedName type;


    transient protected Object valueAsJavaObject;

    /* (non-Javadoc)
     * @see org.openprovenance.prov.xml.TypIN#getValue()
     */
    @Override
    public Object getValue() {
        return value;
    }

    /* (non-Javadoc)
     * @see org.openprovenance.prov.model.TypedValue#setValue(java.lang.String)
     */
    @Override
    public void setValue(String value) {
        this.value = value;
    }
    /* (non-Javadoc)
     * @see org.openprovenance.prov.model.TypedValue#setValue(QualifiedName)
     */
    @Override
    public void setValue(QualifiedName value) {
        this.value = value;
    }
    /* (non-Javadoc)
     * @see org.openprovenance.prov.xml.TypedValue#setValue(java.lang.String)
     */
    @Override
    public void setValue(org.openprovenance.prov.model.LangString value) {
        this.value = value;
    }



    @Override
    public QualifiedName getType() {
        return type;
    }

 
    @Override
    public Object convertValueToObject(org.openprovenance.prov.model.ValueConverter vconv) {
    	if (valueAsJavaObject==null) {
    	    if (value instanceof String) {
    		valueAsJavaObject=vconv.convertToJava(getType(), (String)value);
    	    } else {
    		valueAsJavaObject=value;
    	    }
    	}
        return valueAsJavaObject;
    }

    @Override
    public Object getConvertedValue() {
        return valueAsJavaObject;
    }

    
    /** Converts a byte array in base64 or hexadecimal according to specified type. 
     * 
     * @param bytes array of bytes to convert
     */

    private void setValueFromObject(byte[] bytes) {
	if (type.equals(QNAME_XSD_BASE64_BINARY)) {
	    setValue(ProvFactory.getFactory().base64Encoding(bytes));
	} else if (type.equals(QNAME_XSD_HEX_BINARY)) {
	    setValue(ProvFactory.getFactory().hexEncoding(bytes));
	}
    }


    /** Converts a DOM into a string representation, after "normalizing" it.
     * 
     * @param n DOM Node to convert.
     */
    private void setValueFromObject(org.w3c.dom.Node n) {
	DOMProcessing.trimNode(n);
	try {
	    setValue(DOMProcessing.writeToString(n));
	} catch (TransformerConfigurationException e) {
	    setValue(n.toString());  // TODO: not the most compelling handling
	} catch (TransformerException e) {
	    setValue(n.toString()); //TODO: not the most compelling handling
	}
    }
    
    /* (non-Javadoc)
     * @see org.openprovenance.prov.xml.TypIN#setValueAsJava(java.lang.Object)
     */
    @Override
    public void setValueFromObject(Object anObject) {
	if ((anObject!=null) && (value==null)) {
	    if(anObject instanceof QualifiedName) {
		setValue((QualifiedName)anObject);
	    } else if (anObject instanceof org.openprovenance.prov.model.LangString) { 
		setValue((org.openprovenance.prov.model.LangString)anObject);
	    } else if (anObject instanceof byte[]) {
		setValueFromObject((byte[]) anObject);
	    } else if (anObject instanceof org.w3c.dom.Node) {
		setValueFromObject((org.w3c.dom.Node)anObject);
	    } else {
		setValue(anObject.toString());
	    }
	}
        this.valueAsJavaObject = anObject;
    }

   

    /*
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link QualifiedName }
     *     
     */
    /* (non-Javadoc)
     * @see org.openprovenance.prov.xml.TypedValue#setType(javax.xml.namespace.QualifiedName)
     */
    @Override
    public void setType(QualifiedName value) {
        this.type = value;
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

    public void hashCode(HashCodeBuilder hashCodeBuilder) {
        hashCodeBuilder.append(this.getValue());
        hashCodeBuilder.append(this.getType());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

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
