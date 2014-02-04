package org.openprovenance.prov.sql;
import org.openprovenance.prov.model.Name;
import org.openprovenance.prov.model.QualifiedName;
import javax.xml.datatype.XMLGregorianCalendar;

public class SQLValueConverter {
    
    static Name name=ProvFactory.getFactory().getName();

    static public AValue convertToAValue(QualifiedName datatype, Object value) {
	if (datatype.equals(name.XSD_STRING)) {
	    AValue res=new AValue();
	    res.setString((String) value);
	    return res;
	}
	if (datatype.equals(name.XSD_INT)) {
	    AValue res=new AValue();
	    res.setLong(((Integer) value).longValue());
	    return res;
	}
	if (datatype.equals(name.XSD_LONG)) {
	    AValue res=new AValue();
	    res.setLong((Long) value);
	    return res;
	}
	if (datatype.equals(name.XSD_SHORT)) {
	    AValue res=new AValue();
	    res.setLong(((Short) value).longValue());
	    return res;
	}
	if (datatype.equals(name.XSD_DOUBLE)) {
	    AValue res=new AValue();
	    res.setDouble((Double) value);
	    return res;
	}
	if (datatype.equals(name.XSD_FLOAT)) {
	    AValue res=new AValue();
	    res.setFloat((Float) value);
	    return res;
	}
	if (datatype.equals(name.XSD_DECIMAL)) {
	    AValue res=new AValue();
//	    res.setDecimal((java.math.BigDecimal) value);
	    return res;
	}
	if (datatype.equals(name.XSD_BOOLEAN)) {
	    AValue res=new AValue();
//	    res.setBoolean((Boolean) value);
	    return res;
	}
	if (datatype.equals(name.XSD_BYTE)) {
	    AValue res=new AValue();
	    res.setLong(((Byte) value).longValue());
	    return res;
	}
	if (datatype.equals(name.XSD_UNSIGNED_INT)) {
	    AValue res=new AValue();
	    res.setLong((Long) value);
	    return res;
	}
	if (datatype.equals(name.XSD_UNSIGNED_SHORT)) {
	    AValue res=new AValue();
	    //throw new UnsupportedOperationException();
	    return res;
	}
	if (datatype.equals(name.XSD_UNSIGNED_BYTE)) {
	    AValue res=new AValue();
	    //throw new UnsupportedOperationException();
	    return res;
	}
	if (datatype.equals(name.XSD_UNSIGNED_LONG)) {
	    AValue res=new AValue();
	    //res.setUnsignedInt((Long) value);
	    return res;
	}
	if (datatype.equals(name.XSD_INTEGER)) {
	    AValue res=new AValue();
	    //throw new UnsupportedOperationException();
	    return res;
	}
	if (datatype.equals(name.XSD_NON_NEGATIVE_INTEGER)) {
	    AValue res=new AValue();
	    //throw new UnsupportedOperationException();
	    return res;
	}
	if (datatype.equals(name.XSD_NON_POSITIVE_INTEGER)) {
	    AValue res=new AValue();
	    //throw new UnsupportedOperationException();
	    return res;
	}
	if (datatype.equals(name.XSD_POSITIVE_INTEGER)) {
	    AValue res=new AValue();
	    //throw new UnsupportedOperationException();
	    return res;
	}
	if (datatype.equals(name.XSD_ANY_URI)) {
	    AValue res=new AValue();
//	    res.setAnyURI( value.toString());
	    return res;
	}
	if (datatype.equals(name.XSD_QNAME)) {
	    AValue res=new AValue();
	    res.setQname((org.openprovenance.prov.sql.QualifiedName) value); 
	    return res;
	}
	if (datatype.equals(name.XSD_DATETIME)) {
	    AValue res=new AValue();
	    res.setDateTime((XMLGregorianCalendar) value);
	    return res;
	}
        if (datatype.equals(name.XSD_GYEAR)) {
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

	    return null;
	}

	return null;
    }

/*
    static AValue convert(Object o) {
	if (o==null) return null;
	QName type=conv.getXsdType(o);
	
	return convertToAValue(type,o);
    }
*/

    public static Object convertFromAValue(AValue target) {
	Object o;
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

	return null;
    }
	
}
