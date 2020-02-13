package org.openprovenance.prov.vanilla;

import org.openprovenance.apache.commons.lang.builder.*;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.model.LangString;
import org.openprovenance.prov.model.QualifiedName;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

 public class TypedValue implements org.openprovenance.prov.model.TypedValue, Equals, ToString, HashCode {


     private static final QualifiedName QualifiedName_XSD_STRING         = ProvFactory.getFactory().getName().XSD_STRING;
     private static final QualifiedName QualifiedName_PROV_TYPE          = ProvFactory.getFactory().getName().PROV_TYPE;
     private static final QualifiedName QualifiedName_PROV_LABEL         = ProvFactory.getFactory().getName().PROV_LABEL;
     private static final QualifiedName QualifiedName_PROV_VALUE         = ProvFactory.getFactory().getName().PROV_VALUE;
     private static final QualifiedName QualifiedName_PROV_LOCATION      = ProvFactory.getFactory().getName().PROV_LOCATION;
     private static final QualifiedName QualifiedName_PROV_ROLE          = ProvFactory.getFactory().getName().PROV_ROLE;
     private static final QualifiedName QualifiedName_XSD_HEX_BINARY     = ProvFactory.getFactory().getName().XSD_HEX_BINARY;
     private static final QualifiedName QualifiedName_XSD_BASE64_BINARY  = ProvFactory.getFactory().getName().XSD_BASE64_BINARY;
     private static final QualifiedName QualifiedName_PROV_QUALIFIEDNAME = ProvFactory.getFactory().getName().PROV_QUALIFIED_NAME;

     public static Object castToStringOrLangStringOrQualifiedName(Object value, QualifiedName type) {
         return (value instanceof org.openprovenance.prov.model.LangString)? value : ((value instanceof org.openprovenance.prov.model.QualifiedName)? value : ((QualifiedName_XSD_STRING.equals(type))? new org.openprovenance.prov.vanilla.LangString(value.toString(),null): value.toString()));
     }


     protected Object value;


    protected QualifiedName type;


    transient protected Object valueAsJavaObject;

    public TypedValue(QualifiedName type, Object value) {
        this.type=type;
        this.value=value;
    }

     /* dummy, introduced to distinguish the constructors. */
     public TypedValue(QualifiedName type, Object value, Object _dummy, Object ignore) {
         this.type=type;
         setValueFromObject(value);
     }

    protected TypedValue() {}


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
    public QualifiedName getType() {
        return type;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public Object getConvertedValue() {
        return valueAsJavaObject;
    }




    public QualifiedName getQualifiedName(Attribute.AttributeKind kind) {
        switch (kind) {
            case  PROV_TYPE:     return QualifiedName_PROV_TYPE;
            case  PROV_LABEL:    return QualifiedName_PROV_LABEL;
            case  PROV_VALUE:    return QualifiedName_PROV_VALUE;
            case  PROV_LOCATION: return QualifiedName_PROV_LOCATION;
            case  PROV_ROLE:     return QualifiedName_PROV_ROLE;
            case OTHER:
            default:             return null;
        }
    }

    /* (non-Javadoc)
     * @see org.openprovenance.prov.model.Attribute#getAttributeKind(org.openprovenance.prov.model.QualifiedName)
     */

    public Attribute.AttributeKind getAttributeKind(QualifiedName q) {
        if (q.equals(QualifiedName_PROV_TYPE))     return Attribute.AttributeKind.PROV_TYPE;
        if (q.equals(QualifiedName_PROV_LABEL))    return Attribute.AttributeKind.PROV_LABEL;
        if (q.equals(QualifiedName_PROV_VALUE))    return Attribute.AttributeKind.PROV_VALUE;
        if (q.equals(QualifiedName_PROV_LOCATION)) return Attribute.AttributeKind.PROV_LOCATION;
        if (q.equals(QualifiedName_PROV_ROLE))     return Attribute.AttributeKind.PROV_ROLE;
        return Attribute.AttributeKind.OTHER;
    }



    @Override
    public void setType(QualifiedName value) {
        this.type=value;

    }

    @Override
    public void setValue(LangString value) {
        this.value=value;

    }

    @Override
    public void setValue(QualifiedName value) {
        this.value=value;

    }

    @Override
    public void setValue(String value) {
        if ((type!=null) && QualifiedName_PROV_QUALIFIEDNAME.equals(type)) {
            System.out.println("HACK: deserializing value " + value + "type " + type);
            Namespace ns=null;
            setValue(ns.stringToQualifiedName(value, ProvFactory.getFactory(), false));
            throw new UnsupportedOperationException("we should not be here with " + value);
            //setValue(CustomQualifiedNameDeserializer.theNS.stringToQualifiedName(value, ProvFactory.getFactory(), false));
        } else {
            this.value = value;
        }

    }


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

    /** Converts a byte array in base64 or hexadecimal according to specified type.
     *
     * @param bytes array of bytes to convert
     */

    private void setValueFromObject(byte[] bytes) {
        if (type.equals(QualifiedName_XSD_BASE64_BINARY)) {
            setValue(ProvFactory.getFactory().base64Encoding(bytes));
        } else if (type.equals(QualifiedName_XSD_HEX_BINARY)) {
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
        EqualsBuilder equalsBuilder = new EqualsBuilder();
        equals(object, equalsBuilder);
        return equalsBuilder.isEquals();
    }

    public void hashCode(HashCodeBuilder hashCodeBuilder) {
        hashCodeBuilder.append(this.getValue());
        hashCodeBuilder.append(this.getType());
    }

    public int hashCode() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            Object theValue;
            theValue = this.getValue();
            toStringBuilder.append("value", theValue);
            toStringBuilder.append("DEBUG_type1", theValue.getClass());
        }
        {
            QualifiedName theType;
            theType = this.getType();
            toStringBuilder.append("type", theType);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new ToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }


}
