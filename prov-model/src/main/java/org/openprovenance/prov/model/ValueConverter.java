
package org.openprovenance.prov.model;
import java.math.BigInteger;
import java.net.URI;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;



public class ValueConverter  {
    
    public static QName newXsdQName(String local) {
        return new QName(NamespacePrefixMapper.XSD_NS, local, NamespacePrefixMapper.XSD_PREFIX);
    }

    public static QName newXsdHashQName(String local) {
	return new QName(NamespacePrefixMapper.XSD_HASH_NS, local, NamespacePrefixMapper.XSD_PREFIX);
    }
    
    public static QName newRdfQName(String local) {
        return new QName("http://www.w3.org/1999/02/22-rdf-syntax-ns#", local, "rdf");
    }
    
    public static QName newProvQName(String local) {
        return new QName(NamespacePrefixMapper.PROV_NS,local,"prov");
    }
    public static final QName QNAME_PROV_INTERNATIONALIZED_STRING = newProvQName("InternationalizedString");
    public static final QName QNAME_PROV_REVISION = newProvQName("Revision");
    public static final QName QNAME_PROV_QUOTATION = newProvQName("Quotation");
    public static final QName QNAME_PROV_PRIMARY_SOURCE = newProvQName("PrimarySource");
    public static final QName QNAME_PROV_BUNDLE = newProvQName("Bundle");

    public static QName QNAME_XSD_STRING=newXsdQName("string"); //*
    public static QName QNAME_XSD_INT=newXsdQName("int");//*
    public static QName QNAME_XSD_LONG=newXsdQName("long");//*
    public static QName QNAME_XSD_SHORT=newXsdQName("short");//*
    public static QName QNAME_XSD_DOUBLE=newXsdQName("double"); //*
    public static QName QNAME_XSD_FLOAT=newXsdQName("float"); //*
    public static QName QNAME_XSD_DECIMAL=newXsdQName("decimal"); //*
    public static QName QNAME_XSD_BOOLEAN=newXsdQName("boolean"); //*
    public static QName QNAME_XSD_BYTE=newXsdQName("byte");//*
    public static QName QNAME_XSD_UNSIGNED_INT=newXsdQName("unsignedInt");//*
    public static QName QNAME_XSD_UNSIGNED_LONG=newXsdQName("unsignedLong");//*
    public static QName QNAME_XSD_INTEGER=newXsdQName("integer"); //*
    public static QName QNAME_XSD_UNSIGNED_SHORT=newXsdQName("unsignedShort");//*
    public static QName QNAME_XSD_NON_NEGATIVE_INTEGER=newXsdQName("nonNegativeInteger");//*
    public static QName QNAME_XSD_NON_POSITIVE_INTEGER=newXsdQName("nonPositiveInteger");//*
    public static QName QNAME_XSD_POSITIVE_INTEGER=newXsdQName("positiveInteger");//*
    public static QName QNAME_XSD_UNSIGNED_BYTE=newXsdQName("unsignedByte");//*
    public static QName QNAME_XSD_ANY_URI=newXsdQName("anyURI");
    public static QName QNAME_XSD_QNAME=newXsdQName("QName");
    public static QName QNAME_XSD_DATETIME=newXsdQName("dateTime"); //*
    public static QName QNAME_XSD_GYEAR=newXsdQName("gYear"); //*

    public static QName QNAME_XSD_GMONTH=newXsdQName("gMonth"); //*
    public static QName QNAME_XSD_GDAY=newXsdQName("gDay"); //*
    public static QName QNAME_XSD_GYEAR_MONTH=newXsdQName("gYearMonth"); //*
    public static QName QNAME_XSD_GMONTH_DAY=newXsdQName("gMonthDay"); //*
    public static QName QNAME_XSD_DURATION=newXsdQName("duration"); //*
    public static QName QNAME_XSD_YEAR_MONTH_DURATION=newXsdQName("yearMonthDuration"); //*
    public static QName QNAME_XSD_DAY_TIME_DURATION=newXsdQName("dayTimeDuration"); //*

    public static QName QNAME_XSD_HEX_BINARY=newXsdQName("hexBinary"); //*
    public static QName QNAME_XSD_BASE64_BINARY=newXsdQName("base64Binary"); //*

    public static QName QNAME_XSD_LANGUAGE=newXsdQName("language"); //*
    public static QName QNAME_XSD_NORMALIZED_STRING=newXsdQName("normalizedString"); //*
    public static QName QNAME_XSD_TOKEN=newXsdQName("token"); //*
    public static QName QNAME_XSD_NMTOKEN=newXsdQName("NMTOKEN"); //*
    public static QName QNAME_XSD_NAME=newXsdQName("Name"); //*
    public static QName QNAME_XSD_NCNAME=newXsdQName("NCName"); //*

    public static QName QNAME_XSD_TIME=newXsdQName("time");  //*
    public static QName QNAME_XSD_DATE=newXsdQName("date"); //*
    public static QName QNAME_XSD_DATETIMESTAMP=newXsdQName("dateTimeStamp");  //*

    public static QName QNAME_XSD_HASH_STRING=newXsdHashQName("string");
    public static QName QNAME_XSD_HASH_INT=newXsdHashQName("int");
    public static QName QNAME_XSD_HASH_LONG=newXsdHashQName("long");
    public static QName QNAME_XSD_HASH_SHORT=newXsdHashQName("short");
    public static QName QNAME_XSD_HASH_DOUBLE=newXsdHashQName("double");
    public static QName QNAME_XSD_HASH_FLOAT=newXsdHashQName("float");
    public static QName QNAME_XSD_HASH_DECIMAL=newXsdHashQName("decimal");
    public static QName QNAME_XSD_HASH_BOOLEAN=newXsdHashQName("boolean");
    public static QName QNAME_XSD_HASH_BYTE=newXsdHashQName("byte");
    public static QName QNAME_XSD_HASH_UNSIGNED_INT=newXsdHashQName("unsignedInt");
    public static QName QNAME_XSD_HASH_UNSIGNED_LONG=newXsdHashQName("unsignedLong");
    public static QName QNAME_XSD_HASH_INTEGER=newXsdHashQName("integer");
    public static QName QNAME_XSD_HASH_UNSIGNED_SHORT=newXsdHashQName("unsignedShort");
    public static QName QNAME_XSD_HASH_NON_NEGATIVE_INTEGER=newXsdHashQName("nonNegativeInteger");
    public static QName QNAME_XSD_HASH_NON_POSITIVE_INTEGER=newXsdHashQName("nonPositiveInteger");
    public static QName QNAME_XSD_HASH_POSITIVE_INTEGER=newXsdHashQName("positiveInteger");
    public static QName QNAME_XSD_HASH_UNSIGNED_BYTE=newXsdHashQName("unsignedByte");
    public static QName QNAME_XSD_HASH_ANY_URI=newXsdHashQName("anyURI");
    public static QName QNAME_XSD_HASH_QNAME=newXsdHashQName("QName");
    public static QName QNAME_XSD_HASH_DATETIME=newXsdHashQName("dateTime");
    public static QName QNAME_XSD_HASH_GYEAR=newXsdQName("gYear");

    
    public static QName QNAME_RDF_LITERAL=newRdfQName("XMLLiteral");

    
    public static QName QNAME_UNKNOWN=newXsdQName("UNKNOWN");

    final private LiteralConstructor pFactory;
    final QNameConstructor qnConst;
    
    public ValueConverter(LiteralConstructor pFactory, QNameConstructor qnConst) {
	this.pFactory=pFactory;
	this.qnConst=qnConst;
    }
    
    
    // should be implemented with a hash table of converters
    
    public Object convertToJava(QName datatype, String value) {
	if (datatype.equals(QNAME_XSD_STRING) || datatype.equals(QNAME_XSD_HASH_STRING))
	    return value;
	if (datatype.equals(QNAME_XSD_INT) || datatype.equals(QNAME_XSD_HASH_INT))
	    return Integer.parseInt(value);
	if (datatype.equals(QNAME_XSD_LONG) || datatype.equals(QNAME_XSD_HASH_LONG))
	    return Long.parseLong(value);
	if (datatype.equals(QNAME_XSD_SHORT) || datatype.equals(QNAME_XSD_HASH_SHORT))
	    return Short.parseShort(value);
	if (datatype.equals(QNAME_XSD_DOUBLE) || datatype.equals(QNAME_XSD_HASH_DOUBLE))
	    return Double.parseDouble(value);
	if (datatype.equals(QNAME_XSD_FLOAT) || datatype.equals(QNAME_XSD_HASH_FLOAT))
	    return Float.parseFloat(value);
	if (datatype.equals(QNAME_XSD_DECIMAL) || datatype.equals(QNAME_XSD_HASH_DECIMAL))
	    return new java.math.BigDecimal(value);
	if (datatype.equals(QNAME_XSD_BOOLEAN) || datatype.equals(QNAME_XSD_HASH_BOOLEAN))
	    return Boolean.parseBoolean(value);
	if (datatype.equals(QNAME_XSD_BYTE) || datatype.equals(QNAME_XSD_HASH_BYTE))
	    return Byte.parseByte(value);
	if (datatype.equals(QNAME_XSD_UNSIGNED_INT) || datatype.equals(QNAME_XSD_HASH_UNSIGNED_INT))
	    return Long.parseLong(value);
	if (datatype.equals(QNAME_XSD_UNSIGNED_SHORT) || datatype.equals(QNAME_XSD_HASH_UNSIGNED_SHORT))
	    return Integer.parseInt(value);
	if (datatype.equals(QNAME_XSD_UNSIGNED_BYTE) || datatype.equals(QNAME_XSD_HASH_UNSIGNED_BYTE))
	    return Short.parseShort(value);
	if (datatype.equals(QNAME_XSD_UNSIGNED_LONG) || datatype.equals(QNAME_XSD_HASH_UNSIGNED_LONG))
	    return new java.math.BigInteger(value);
	if (datatype.equals(QNAME_XSD_INTEGER) || datatype.equals(QNAME_XSD_HASH_INTEGER))
	    return new java.math.BigInteger(value);
	if (datatype.equals(QNAME_XSD_NON_NEGATIVE_INTEGER) || datatype.equals(QNAME_XSD_HASH_NON_NEGATIVE_INTEGER))
	    return new java.math.BigInteger(value);
	if (datatype.equals(QNAME_XSD_NON_POSITIVE_INTEGER) || datatype.equals(QNAME_XSD_HASH_NON_POSITIVE_INTEGER))
	    return new java.math.BigInteger(value);
	if (datatype.equals(QNAME_XSD_POSITIVE_INTEGER) || datatype.equals(QNAME_XSD_HASH_POSITIVE_INTEGER))
	    return new java.math.BigInteger(value);
	if (datatype.equals(QNAME_XSD_ANY_URI) || datatype.equals(QNAME_XSD_HASH_ANY_URI)) {
	    URIWrapper u = new URIWrapper();
	    u.setValue(URI.create(value));
	    return u;
	}
	if (datatype.equals(QNAME_XSD_QNAME) || datatype.equals(QNAME_XSD_HASH_QNAME)) {
	    return qnConst.newQName(value);
	}
	if (datatype.equals(QNAME_XSD_DATETIME) || datatype.equals(QNAME_XSD_HASH_DATETIME)) {
	    return pFactory.newISOTime(value);
	}
        if (datatype.equals(QNAME_XSD_GYEAR) || datatype.equals(QNAME_XSD_HASH_GYEAR)) {
            return pFactory.newGYear(new Integer(value));
        }

        if (datatype.equals(QNAME_XSD_GMONTH)) {
            // format is --02
            return pFactory.newGMonth(new Integer(value.substring(2)));
        }
        if (datatype.equals(QNAME_XSD_GMONTH_DAY)) {
            // format is --12-25            
            return pFactory.newGMonthDay(new Integer(value.substring(2,4)), 
                                         new Integer(value.substring(5,7)));
        }

        if (datatype.equals(QNAME_XSD_GDAY)) {
            // format is ---30
            return pFactory.newGDay(new Integer(value.substring(3)));
        }
        
        if (datatype.equals(QNAME_XSD_DURATION)) {
            return pFactory.newDuration(value);
        }
        
        if (datatype.equals(QNAME_XSD_DAY_TIME_DURATION)) {
            return pFactory.newDuration(value);
        }
        
        if (datatype.equals(QNAME_XSD_YEAR_MONTH_DURATION)) {
            return pFactory.newDuration(value);
        }
        
        if (datatype.equals(QNAME_XSD_LANGUAGE)) {
            return value;
        }

        if (datatype.equals(QNAME_XSD_TOKEN)) {
            return value;
        }
        if (datatype.equals(QNAME_XSD_NMTOKEN)) {
            return value;
        }

        if (datatype.equals(QNAME_XSD_NAME)) {
            return value;
        }

        if (datatype.equals(QNAME_XSD_NCNAME)) {
            return value;
        }

        if (datatype.equals(QNAME_XSD_NORMALIZED_STRING)) {
            return value;
        }

        if (datatype.equals(QNAME_XSD_HEX_BINARY)) {
            return pFactory.hexDecoding(value);
        }

        if (datatype.equals(QNAME_XSD_BASE64_BINARY)) {
            return pFactory.base64Decoding(value);
        }

	//transform to qname!!
	if (datatype.equals(QNAME_RDF_LITERAL)) {
	    return value;
	}

	throw new UnsupportedOperationException("UNKNOWN literal type "
		+ datatype);
    }
    
    

    public QName getXsdType(Object o) {
	if (o instanceof Integer)
	    return QNAME_XSD_INT; //"xsd:int";
	if (o instanceof String)
	    return QNAME_XSD_STRING; //"xsd:string";
	if (o instanceof InternationalizedString)
	    return QNAME_XSD_STRING; //"xsd:string";
	if (o instanceof BigInteger)
		return QNAME_XSD_INTEGER;
	if (o instanceof Long)
	    return QNAME_XSD_LONG; //"xsd:long";
	if (o instanceof Short)
	    return QNAME_XSD_SHORT; //"xsd:short";
	if (o instanceof Double)
	    return QNAME_XSD_DOUBLE; //"xsd:double";
	if (o instanceof Float)
	    return QNAME_XSD_FLOAT; //"xsd:float";
	if (o instanceof java.math.BigDecimal)
	    return QNAME_XSD_DECIMAL; //"xsd:decimal";
	if (o instanceof Boolean)
	    return QNAME_XSD_BOOLEAN; //"xsd:boolean";
	if (o instanceof Byte)
	    return QNAME_XSD_BYTE; //"xsd:byte";
	if (o instanceof URIWrapper)
	    return QNAME_XSD_ANY_URI; //"xsd:anyURI";
	if (o instanceof QName)
	    return QNAME_XSD_QNAME; //"xsd:QName";
	if (o instanceof XMLGregorianCalendar) {
	    XMLGregorianCalendar cal=(XMLGregorianCalendar)o;
	    QName t=cal.getXMLSchemaType();
            if (t.getLocalPart().equals(QNAME_XSD_GYEAR.getLocalPart())) return QNAME_XSD_GYEAR;
            if (t.getLocalPart().equals(QNAME_XSD_DATETIME.getLocalPart())) return QNAME_XSD_DATETIME;
            //TODO: need to support all time related xsd types
            // default, return xsd:datetime
            return QNAME_XSD_DATETIME;
	}
	//FIXME: see issue #54, value can be an element, when xsi:type was unspecified.
	System.out.println("getXsdType() " + o.getClass());
	return QNAME_UNKNOWN;
    }


    
}
