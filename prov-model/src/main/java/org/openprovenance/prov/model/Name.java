package org.openprovenance.prov.model;

import javax.xml.namespace.QName;

public class Name {

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
    
    public static final QName PROV_TYPE_QNAME=newProvQName("type"); 
    public static final QName PROV_LABEL_QNAME=newProvQName("label"); 
    public static final QName PROV_ROLE_QNAME=newProvQName("role");
    public static final QName PROV_LOCATION_QNAME=newProvQName("location");
    public static final QName PROV_VALUE_QNAME=newProvQName("value");
    public static final QName PROV_KEY_QNAME=newProvQName("key");

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

}
