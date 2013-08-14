package org.openprovenance.prov.sql;
import  org.openprovenance.prov.xml.ValueConverter;
import  org.openprovenance.prov.xml.ProvFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

public class SQLValueConverter {
    static ValueConverter conv=new ValueConverter(new ProvFactory());

    static public AValue convertToAValue(QName datatype, Object value) {
	if (datatype.equals(ValueConverter.QNAME_XSD_STRING)) {
	    AValue res=new AValue();
	    res.setString((String) value);
	    return res;
	}
	if (datatype.equals(ValueConverter.QNAME_XSD_INT)) {
	    AValue res=new AValue();
	    res.setInt((Integer) value);
	    return res;
	}
	if (datatype.equals(ValueConverter.QNAME_XSD_LONG)) {
	    AValue res=new AValue();
	    res.setLong((Long) value);
	    return res;
	}
	if (datatype.equals(ValueConverter.QNAME_XSD_SHORT)) {
	    AValue res=new AValue();
	    res.setShort((Short) value);
	    return res;
	}
	if (datatype.equals(ValueConverter.QNAME_XSD_DOUBLE)) {
	    AValue res=new AValue();
	    res.setDouble((Double) value);
	    return res;
	}
	if (datatype.equals(ValueConverter.QNAME_XSD_FLOAT)) {
	    AValue res=new AValue();
	    res.setFloat((Float) value);
	    return res;
	}
	if (datatype.equals(ValueConverter.QNAME_XSD_DECIMAL)) {
	    AValue res=new AValue();
	    res.setDecimal((java.math.BigDecimal) value);
	    return res;
	}
	if (datatype.equals(ValueConverter.QNAME_XSD_BOOLEAN)) {
	    AValue res=new AValue();
	    res.setBoolean((Boolean) value);
	    return res;
	}
	if (datatype.equals(ValueConverter.QNAME_XSD_BYTE)) {
	    AValue res=new AValue();
	    res.setByte((Byte) value);
	    return res;
	}
	if (datatype.equals(ValueConverter.QNAME_XSD_UNSIGNED_INT)) {
	    AValue res=new AValue();
	    res.setUnsignedInt((Long) value);
	    return res;
	}
	if (datatype.equals(ValueConverter.QNAME_XSD_UNSIGNED_SHORT)) {
	    AValue res=new AValue();
	    throw new UnsupportedOperationException();
	}
	if (datatype.equals(ValueConverter.QNAME_XSD_UNSIGNED_BYTE)) {
	    AValue res=new AValue();
	    throw new UnsupportedOperationException();
	}
	if (datatype.equals(ValueConverter.QNAME_XSD_UNSIGNED_LONG)) {
	    AValue res=new AValue();
	    throw new UnsupportedOperationException();
	}
	if (datatype.equals(ValueConverter.QNAME_XSD_INTEGER)) {
	    AValue res=new AValue();
	    throw new UnsupportedOperationException();
	}
	if (datatype.equals(ValueConverter.QNAME_XSD_NON_NEGATIVE_INTEGER)) {
	    AValue res=new AValue();
	    throw new UnsupportedOperationException();
	}
	if (datatype.equals(ValueConverter.QNAME_XSD_NON_POSITIVE_INTEGER)) {
	    AValue res=new AValue();
	    throw new UnsupportedOperationException();
	}
	if (datatype.equals(ValueConverter.QNAME_XSD_POSITIVE_INTEGER)) {
	    AValue res=new AValue();
	    throw new UnsupportedOperationException();
	}
	if (datatype.equals(ValueConverter.QNAME_XSD_ANY_URI)) {
	    AValue res=new AValue();
	    res.setAnyURI( value.toString());
	    return res;
	}
	if (datatype.equals(ValueConverter.QNAME_XSD_QNAME)) {
	    AValue res=new AValue();
	    res.setQname((QName) value);
	    return res;
	}
	if (datatype.equals(ValueConverter.QNAME_XSD_DATETIME)) {
	    AValue res=new AValue();
	    res.setDateTime((XMLGregorianCalendar) value);
	    return res;
	}
        if (datatype.equals(ValueConverter.QNAME_XSD_GYEAR)) {
	    AValue res=new AValue();
	    res.setGYear((XMLGregorianCalendar) value);
	    return res;
        }
	
	
	//transform to qname!!
	if ((datatype.equals("rdf:XMLLiteral"))
	    || (datatype.equals("xsd:normalizedString"))
	    || (datatype.equals("xsd:token"))
	    || (datatype.equals("xsd:language"))
	    || (datatype.equals("xsd:Name"))
	    || (datatype.equals("xsd:NCName"))
	    || (datatype.equals("xsd:NMTOKEN"))
	    || (datatype.equals("xsd:hexBinary"))
	    || (datatype.equals("xsd:base64Binary"))) {

	    throw new UnsupportedOperationException(
						    "KNOWN literal type but conversion not supported yet "
						    + datatype);
	}

	throw new UnsupportedOperationException("UNKNOWN literal type "
						+ datatype);
    }


    static AValue convert(Object o) {
	if (o==null) return null;
	QName type=conv.getXsdType(o);
	
	return convertToAValue(type,o);
    }


    public static Object convertFromAValue(AValue target) {
	Object o;
	if ((o=target.getInt())!=null) {
	    return o;
	}
	if ((o=target.getString())!=null) {
	    return o;
	}
	if ((o=target.getQname())!=null) {
	    return o;
	}
	if ((o=target.getFloat())!=null) {
	    return o;
	}
	if ((o=target.getDouble())!=null) {
	    return o;
	}
	if ((o=target.getUnsignedInt())!=null) {
	    return o;
	}
	if ((o=target.getUnsignedLong())!=null) {
	    return o;
	}
	if ((o=target.isBoolean())!=null) {
	    return o;
	}

	return null;
    }
	
}
