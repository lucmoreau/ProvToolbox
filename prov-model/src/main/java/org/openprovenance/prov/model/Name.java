package org.openprovenance.prov.model;

public class Name {
    final private ProvFactory pFactory;
    
    public Name(ProvFactory pFactory) {
	this.pFactory=pFactory;
	QNAME_PROV_INTERNATIONALIZED_STRING = newProvQualifiedName("InternationalizedString");
	QNAME_PROV_REVISION = newProvQualifiedName("Revision");
	QNAME_PROV_QUOTATION = newProvQualifiedName("Quotation");
	QNAME_PROV_PRIMARY_SOURCE = newProvQualifiedName("PrimarySource");
	QNAME_PROV_BUNDLE = newProvQualifiedName("Bundle");
	QNAME_PROV_TYPE=newProvQualifiedName("type"); 
	QNAME_PROV_LABEL=newProvQualifiedName("label"); 
	QNAME_PROV_ROLE=newProvQualifiedName("role");
	QNAME_PROV_LOCATION=newProvQualifiedName("location");
	QNAME_PROV_VALUE=newProvQualifiedName("value");
	QNAME_PROV_KEY=newProvQualifiedName("key");

	QNAME_XSD_STRING=newXsdQualifiedName("string"); //*
	QNAME_XSD_INT=newXsdQualifiedName("int");//*
	QNAME_XSD_LONG=newXsdQualifiedName("long");//*
	QNAME_XSD_SHORT=newXsdQualifiedName("short");//*
	QNAME_XSD_DOUBLE=newXsdQualifiedName("double"); //*
	QNAME_XSD_FLOAT=newXsdQualifiedName("float"); //*
	QNAME_XSD_DECIMAL=newXsdQualifiedName("decimal"); //*
	QNAME_XSD_BOOLEAN=newXsdQualifiedName("boolean"); //*
	QNAME_XSD_BYTE=newXsdQualifiedName("byte");//*
	QNAME_XSD_UNSIGNED_INT=newXsdQualifiedName("unsignedInt");//*
	QNAME_XSD_UNSIGNED_LONG=newXsdQualifiedName("unsignedLong");//*
	QNAME_XSD_INTEGER=newXsdQualifiedName("integer"); //*
	QNAME_XSD_UNSIGNED_SHORT=newXsdQualifiedName("unsignedShort");//*
	QNAME_XSD_NON_NEGATIVE_INTEGER=newXsdQualifiedName("nonNegativeInteger");//*
	QNAME_XSD_NON_POSITIVE_INTEGER=newXsdQualifiedName("nonPositiveInteger");//*
	QNAME_XSD_POSITIVE_INTEGER=newXsdQualifiedName("positiveInteger");//*
	QNAME_XSD_UNSIGNED_BYTE=newXsdQualifiedName("unsignedByte");//*
	QNAME_XSD_ANY_URI=newXsdQualifiedName("anyURI");
	QNAME_XSD_QNAME=newXsdQualifiedName("QName");
	QNAME_XSD_DATETIME=newXsdQualifiedName("dateTime"); //*
	QNAME_XSD_GYEAR=newXsdQualifiedName("gYear"); //*

	QNAME_XSD_GMONTH=newXsdQualifiedName("gMonth"); //*
	QNAME_XSD_GDAY=newXsdQualifiedName("gDay"); //*
	QNAME_XSD_GYEAR_MONTH=newXsdQualifiedName("gYearMonth"); //*
	QNAME_XSD_GMONTH_DAY=newXsdQualifiedName("gMonthDay"); //*
	QNAME_XSD_DURATION=newXsdQualifiedName("duration"); //*
	QNAME_XSD_YEAR_MONTH_DURATION=newXsdQualifiedName("yearMonthDuration"); //*
	QNAME_XSD_DAY_TIME_DURATION=newXsdQualifiedName("dayTimeDuration"); //*

	QNAME_XSD_HEX_BINARY=newXsdQualifiedName("hexBinary"); //*
	QNAME_XSD_BASE64_BINARY=newXsdQualifiedName("base64Binary"); //*

	QNAME_XSD_LANGUAGE=newXsdQualifiedName("language"); //*
	QNAME_XSD_NORMALIZED_STRING=newXsdQualifiedName("normalizedString"); //*
	QNAME_XSD_TOKEN=newXsdQualifiedName("token"); //*
	QNAME_XSD_NMTOKEN=newXsdQualifiedName("NMTOKEN"); //*
	QNAME_XSD_NAME=newXsdQualifiedName("Name"); //*
	QNAME_XSD_NCNAME=newXsdQualifiedName("NCName"); //*

	QNAME_XSD_TIME=newXsdQualifiedName("time");  //*
	QNAME_XSD_DATE=newXsdQualifiedName("date"); //*
	QNAME_XSD_DATETIMESTAMP=newXsdQualifiedName("dateTimeStamp");  //*

	QNAME_XSD_HASH_STRING=newXsdHashQualifiedName("string");
	QNAME_XSD_HASH_INT=newXsdHashQualifiedName("int");
	QNAME_XSD_HASH_LONG=newXsdHashQualifiedName("long");
	QNAME_XSD_HASH_SHORT=newXsdHashQualifiedName("short");
	QNAME_XSD_HASH_DOUBLE=newXsdHashQualifiedName("double");
	QNAME_XSD_HASH_FLOAT=newXsdHashQualifiedName("float");
	QNAME_XSD_HASH_DECIMAL=newXsdHashQualifiedName("decimal");
	QNAME_XSD_HASH_BOOLEAN=newXsdHashQualifiedName("boolean");
	QNAME_XSD_HASH_BYTE=newXsdHashQualifiedName("byte");
	QNAME_XSD_HASH_UNSIGNED_INT=newXsdHashQualifiedName("unsignedInt");
	QNAME_XSD_HASH_UNSIGNED_LONG=newXsdHashQualifiedName("unsignedLong");
	QNAME_XSD_HASH_INTEGER=newXsdHashQualifiedName("integer");
	QNAME_XSD_HASH_UNSIGNED_SHORT=newXsdHashQualifiedName("unsignedShort");
	QNAME_XSD_HASH_NON_NEGATIVE_INTEGER=newXsdHashQualifiedName("nonNegativeInteger");
	QNAME_XSD_HASH_NON_POSITIVE_INTEGER=newXsdHashQualifiedName("nonPositiveInteger");
	QNAME_XSD_HASH_POSITIVE_INTEGER=newXsdHashQualifiedName("positiveInteger");
	QNAME_XSD_HASH_UNSIGNED_BYTE=newXsdHashQualifiedName("unsignedByte");
	QNAME_XSD_HASH_ANY_URI=newXsdHashQualifiedName("anyURI");
	QNAME_XSD_HASH_QNAME=newXsdHashQualifiedName("QName");
	QNAME_XSD_HASH_DATETIME=newXsdHashQualifiedName("dateTime");
	QNAME_XSD_HASH_GYEAR=newXsdQualifiedName("gYear");


	QNAME_RDF_LITERAL=newRdfQualifiedName("XMLLiteral");


	QNAME_UNKNOWN=newXsdQualifiedName("UNKNOWN");

    }

    public QualifiedName newXsdQualifiedName(String local) {
        return pFactory.newQualifiedName(NamespacePrefixMapper.XSD_NS, local, NamespacePrefixMapper.XSD_PREFIX);
    }

    public QualifiedName newXsdHashQualifiedName(String local) {
	return pFactory.newQualifiedName(NamespacePrefixMapper.XSD_HASH_NS, local, NamespacePrefixMapper.XSD_PREFIX);
    }
    
    
    public QualifiedName newRdfQualifiedName(String local) {
        return pFactory.newQualifiedName(NamespacePrefixMapper.RDF_NS, local, "rdf");
    }
    
    public QualifiedName newProvQualifiedName(String local) {
        return pFactory.newQualifiedName(NamespacePrefixMapper.PROV_NS,local,"prov");
    }
    
    
    final public QualifiedName QNAME_PROV_INTERNATIONALIZED_STRING ;
    final public QualifiedName QNAME_PROV_REVISION ;
    final public QualifiedName QNAME_PROV_QUOTATION ;
    final public QualifiedName QNAME_PROV_PRIMARY_SOURCE ;
    final public QualifiedName QNAME_PROV_BUNDLE ;
    
    final public QualifiedName QNAME_PROV_TYPE;
    final public QualifiedName QNAME_PROV_LABEL;
    final public QualifiedName QNAME_PROV_ROLE;
    final public QualifiedName QNAME_PROV_LOCATION;
    final public QualifiedName QNAME_PROV_VALUE;
    final public QualifiedName QNAME_PROV_KEY;

    final public QualifiedName QNAME_XSD_STRING;
    final public QualifiedName QNAME_XSD_INT;
    final public QualifiedName QNAME_XSD_LONG;
    final public QualifiedName QNAME_XSD_SHORT;
    final public QualifiedName QNAME_XSD_DOUBLE;
    final public QualifiedName QNAME_XSD_FLOAT;
    final public QualifiedName QNAME_XSD_DECIMAL;
    final public QualifiedName QNAME_XSD_BOOLEAN;
    final public QualifiedName QNAME_XSD_BYTE;
    final public QualifiedName QNAME_XSD_UNSIGNED_INT;
    final public QualifiedName QNAME_XSD_UNSIGNED_LONG;
    final public QualifiedName QNAME_XSD_INTEGER;
    final public QualifiedName QNAME_XSD_UNSIGNED_SHORT;
    final public QualifiedName QNAME_XSD_NON_NEGATIVE_INTEGER;
    final public QualifiedName QNAME_XSD_NON_POSITIVE_INTEGER;
    final public QualifiedName QNAME_XSD_POSITIVE_INTEGER;
    final public QualifiedName QNAME_XSD_UNSIGNED_BYTE;
    final public QualifiedName QNAME_XSD_ANY_URI;
    final public QualifiedName QNAME_XSD_QNAME;
    final public QualifiedName QNAME_XSD_DATETIME;
    final public QualifiedName QNAME_XSD_GYEAR;

    final public QualifiedName QNAME_XSD_GMONTH;
    final public QualifiedName QNAME_XSD_GDAY;
    final public QualifiedName QNAME_XSD_GYEAR_MONTH;
    final public QualifiedName QNAME_XSD_GMONTH_DAY;
    final public QualifiedName QNAME_XSD_DURATION;
    final public QualifiedName QNAME_XSD_YEAR_MONTH_DURATION;
    final public QualifiedName QNAME_XSD_DAY_TIME_DURATION;

    final public QualifiedName QNAME_XSD_HEX_BINARY;
    final public QualifiedName QNAME_XSD_BASE64_BINARY;

    final public QualifiedName QNAME_XSD_LANGUAGE;
    final public QualifiedName QNAME_XSD_NORMALIZED_STRING;
    final public QualifiedName QNAME_XSD_TOKEN;
    final public QualifiedName QNAME_XSD_NMTOKEN;
    final public QualifiedName QNAME_XSD_NAME;
    final public QualifiedName QNAME_XSD_NCNAME;

    final public QualifiedName QNAME_XSD_TIME;
    final public QualifiedName QNAME_XSD_DATE;
    final public QualifiedName QNAME_XSD_DATETIMESTAMP;

    final public QualifiedName QNAME_XSD_HASH_STRING;
    final public QualifiedName QNAME_XSD_HASH_INT;
    final public QualifiedName QNAME_XSD_HASH_LONG;
    final public QualifiedName QNAME_XSD_HASH_SHORT;
    final public QualifiedName QNAME_XSD_HASH_DOUBLE;
    final public QualifiedName QNAME_XSD_HASH_FLOAT;
    final public QualifiedName QNAME_XSD_HASH_DECIMAL;
    final public QualifiedName QNAME_XSD_HASH_BOOLEAN;
    final public QualifiedName QNAME_XSD_HASH_BYTE;
    final public QualifiedName QNAME_XSD_HASH_UNSIGNED_INT;
    final public QualifiedName QNAME_XSD_HASH_UNSIGNED_LONG;
    final public QualifiedName QNAME_XSD_HASH_INTEGER;
    final public QualifiedName QNAME_XSD_HASH_UNSIGNED_SHORT;
    final public QualifiedName QNAME_XSD_HASH_NON_NEGATIVE_INTEGER;
    final public QualifiedName QNAME_XSD_HASH_NON_POSITIVE_INTEGER;
    final public QualifiedName QNAME_XSD_HASH_POSITIVE_INTEGER;
    final public QualifiedName QNAME_XSD_HASH_UNSIGNED_BYTE;
    final public QualifiedName QNAME_XSD_HASH_ANY_URI;
    final public QualifiedName QNAME_XSD_HASH_QNAME;
    final public QualifiedName QNAME_XSD_HASH_DATETIME;
    final public QualifiedName QNAME_XSD_HASH_GYEAR;

    
    final public QualifiedName QNAME_RDF_LITERAL;

    
    final public QualifiedName QNAME_UNKNOWN;

}
