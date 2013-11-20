
package org.openprovenance.prov.model;
import java.math.BigInteger;
import java.net.URI;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;



public class ValueConverter  {
    

    final private LiteralConstructor pFactory;
    final QNameConstructor qnConst;
    
    public ValueConverter(LiteralConstructor pFactory, QNameConstructor qnConst) {
	this.pFactory=pFactory;
	this.qnConst=qnConst;
    }
    
    
    // should be implemented with a hash table of converters
    
    public Object convertToJava(QName datatype, String value) {
	if (datatype.equals(Name.QNAME_XSD_STRING) || datatype.equals(Name.QNAME_XSD_HASH_STRING))
	    return value;
	if (datatype.equals(Name.QNAME_XSD_INT) || datatype.equals(Name.QNAME_XSD_HASH_INT))
	    return Integer.parseInt(value);
	if (datatype.equals(Name.QNAME_XSD_LONG) || datatype.equals(Name.QNAME_XSD_HASH_LONG))
	    return Long.parseLong(value);
	if (datatype.equals(Name.QNAME_XSD_SHORT) || datatype.equals(Name.QNAME_XSD_HASH_SHORT))
	    return Short.parseShort(value);
	if (datatype.equals(Name.QNAME_XSD_DOUBLE) || datatype.equals(Name.QNAME_XSD_HASH_DOUBLE))
	    return Double.parseDouble(value);
	if (datatype.equals(Name.QNAME_XSD_FLOAT) || datatype.equals(Name.QNAME_XSD_HASH_FLOAT))
	    return Float.parseFloat(value);
	if (datatype.equals(Name.QNAME_XSD_DECIMAL) || datatype.equals(Name.QNAME_XSD_HASH_DECIMAL))
	    return new java.math.BigDecimal(value);
	if (datatype.equals(Name.QNAME_XSD_BOOLEAN) || datatype.equals(Name.QNAME_XSD_HASH_BOOLEAN))
	    return Boolean.parseBoolean(value);
	if (datatype.equals(Name.QNAME_XSD_BYTE) || datatype.equals(Name.QNAME_XSD_HASH_BYTE))
	    return Byte.parseByte(value);
	if (datatype.equals(Name.QNAME_XSD_UNSIGNED_INT) || datatype.equals(Name.QNAME_XSD_HASH_UNSIGNED_INT))
	    return Long.parseLong(value);
	if (datatype.equals(Name.QNAME_XSD_UNSIGNED_SHORT) || datatype.equals(Name.QNAME_XSD_HASH_UNSIGNED_SHORT))
	    return Integer.parseInt(value);
	if (datatype.equals(Name.QNAME_XSD_UNSIGNED_BYTE) || datatype.equals(Name.QNAME_XSD_HASH_UNSIGNED_BYTE))
	    return Short.parseShort(value);
	if (datatype.equals(Name.QNAME_XSD_UNSIGNED_LONG) || datatype.equals(Name.QNAME_XSD_HASH_UNSIGNED_LONG))
	    return new java.math.BigInteger(value);
	if (datatype.equals(Name.QNAME_XSD_INTEGER) || datatype.equals(Name.QNAME_XSD_HASH_INTEGER))
	    return new java.math.BigInteger(value);
	if (datatype.equals(Name.QNAME_XSD_NON_NEGATIVE_INTEGER) || datatype.equals(Name.QNAME_XSD_HASH_NON_NEGATIVE_INTEGER))
	    return new java.math.BigInteger(value);
	if (datatype.equals(Name.QNAME_XSD_NON_POSITIVE_INTEGER) || datatype.equals(Name.QNAME_XSD_HASH_NON_POSITIVE_INTEGER))
	    return new java.math.BigInteger(value);
	if (datatype.equals(Name.QNAME_XSD_POSITIVE_INTEGER) || datatype.equals(Name.QNAME_XSD_HASH_POSITIVE_INTEGER))
	    return new java.math.BigInteger(value);
	if (datatype.equals(Name.QNAME_XSD_ANY_URI) || datatype.equals(Name.QNAME_XSD_HASH_ANY_URI)) {
	    URIWrapper u = new URIWrapper();
	    u.setValue(URI.create(value));
	    return u;
	}
	if (datatype.equals(Name.QNAME_XSD_QNAME) || datatype.equals(Name.QNAME_XSD_HASH_QNAME)) {
	    return qnConst.newQName(value);
	}
	if (datatype.equals(Name.QNAME_XSD_DATETIME) || datatype.equals(Name.QNAME_XSD_HASH_DATETIME)) {
	    return pFactory.newISOTime(value);
	}
        if (datatype.equals(Name.QNAME_XSD_GYEAR) || datatype.equals(Name.QNAME_XSD_HASH_GYEAR)) {
            return pFactory.newGYear(new Integer(value));
        }

        if (datatype.equals(Name.QNAME_XSD_GMONTH)) {
            // format is --02
            return pFactory.newGMonth(new Integer(value.substring(2)));
        }
        if (datatype.equals(Name.QNAME_XSD_GMONTH_DAY)) {
            // format is --12-25            
            return pFactory.newGMonthDay(new Integer(value.substring(2,4)), 
                                         new Integer(value.substring(5,7)));
        }

        if (datatype.equals(Name.QNAME_XSD_GDAY)) {
            // format is ---30
            return pFactory.newGDay(new Integer(value.substring(3)));
        }
        
        if (datatype.equals(Name.QNAME_XSD_DURATION)) {
            return pFactory.newDuration(value);
        }
        
        if (datatype.equals(Name.QNAME_XSD_DAY_TIME_DURATION)) {
            return pFactory.newDuration(value);
        }
        
        if (datatype.equals(Name.QNAME_XSD_YEAR_MONTH_DURATION)) {
            return pFactory.newDuration(value);
        }
        
        if (datatype.equals(Name.QNAME_XSD_LANGUAGE)) {
            return value;
        }

        if (datatype.equals(Name.QNAME_XSD_TOKEN)) {
            return value;
        }
        if (datatype.equals(Name.QNAME_XSD_NMTOKEN)) {
            return value;
        }

        if (datatype.equals(Name.QNAME_XSD_NAME)) {
            return value;
        }

        if (datatype.equals(Name.QNAME_XSD_NCNAME)) {
            return value;
        }

        if (datatype.equals(Name.QNAME_XSD_NORMALIZED_STRING)) {
            return value;
        }

        if (datatype.equals(Name.QNAME_XSD_HEX_BINARY)) {
            return pFactory.hexDecoding(value);
        }

        if (datatype.equals(Name.QNAME_XSD_BASE64_BINARY)) {
            return pFactory.base64Decoding(value);
        }

	//transform to qname!!
	if (datatype.equals(Name.QNAME_RDF_LITERAL)) {
	    return value;
	}

	throw new UnsupportedOperationException("UNKNOWN literal type "
		+ datatype);
    }
    
    

    public QName getXsdType(Object o) {
	if (o instanceof Integer)
	    return Name.QNAME_XSD_INT; //"xsd:int";
	if (o instanceof String)
	    return Name.QNAME_XSD_STRING; //"xsd:string";
	if (o instanceof InternationalizedString)
	    return Name.QNAME_XSD_STRING; //"xsd:string";
	if (o instanceof BigInteger)
	    return Name.QNAME_XSD_INTEGER;
	if (o instanceof Long)
	    return Name.QNAME_XSD_LONG; //"xsd:long";
	if (o instanceof Short)
	    return Name.QNAME_XSD_SHORT; //"xsd:short";
	if (o instanceof Double)
	    return Name.QNAME_XSD_DOUBLE; //"xsd:double";
	if (o instanceof Float)
	    return Name.QNAME_XSD_FLOAT; //"xsd:float";
	if (o instanceof java.math.BigDecimal)
	    return Name.QNAME_XSD_DECIMAL; //"xsd:decimal";
	if (o instanceof Boolean)
	    return Name.QNAME_XSD_BOOLEAN; //"xsd:boolean";
	if (o instanceof Byte)
	    return Name.QNAME_XSD_BYTE; //"xsd:byte";
	if (o instanceof URIWrapper)
	    return Name.QNAME_XSD_ANY_URI; //"xsd:anyURI";
	if (o instanceof QName)
	    return Name.QNAME_XSD_QNAME; //"xsd:QName";
	if (o instanceof XMLGregorianCalendar) {
	    XMLGregorianCalendar cal=(XMLGregorianCalendar)o;
	    QName t=cal.getXMLSchemaType();
            if (t.getLocalPart().equals(Name.QNAME_XSD_GYEAR.getLocalPart())) return Name.QNAME_XSD_GYEAR;
            if (t.getLocalPart().equals(Name.QNAME_XSD_DATETIME.getLocalPart())) return Name.QNAME_XSD_DATETIME;
            //TODO: need to support all time related xsd types
            // default, return xsd:datetime
            return Name.QNAME_XSD_DATETIME;
	}
	//FIXME: see issue #54, value can be an element, when xsi:type was unspecified.
	System.out.println("getXsdType() " + o.getClass());
	return Name.QNAME_UNKNOWN;
    }


    
}
