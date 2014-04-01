package org.openprovenance.prov.model;

/** Java class defining a set of constant Qualified Names in the PROV, XSD, and RDF namespaces. 
 * <p>Given that the names are QualifiedNames, they need to be constructed with a factory. 
 * This explains why these names cannot be defined as static, final constants.*/
public class Name {
    final private ProvFactory pFactory;
    
    public Name(ProvFactory pFactory) {
	this.pFactory=pFactory;
	PROV_LANG_STRING = newProvQualifiedName("InternationalizedString");
	PROV_QUALIFIED_NAME = newProvQualifiedName("QualifiedName");
	PROV_REVISION = newProvQualifiedName("Revision");
	PROV_QUOTATION = newProvQualifiedName("Quotation");
	PROV_PRIMARY_SOURCE = newProvQualifiedName("PrimarySource");
	PROV_BUNDLE = newProvQualifiedName("Bundle");
	PROV_TYPE=newProvQualifiedName("type"); 
	PROV_LABEL=newProvQualifiedName("label"); 
	PROV_ROLE=newProvQualifiedName("role");
	PROV_LOCATION=newProvQualifiedName("location");
	PROV_VALUE=newProvQualifiedName("value");
	PROV_KEY=newProvQualifiedName("key");

	XSD_STRING=newXsdQualifiedName("string");
	XSD_INT=newXsdQualifiedName("int");
	XSD_LONG=newXsdQualifiedName("long");
	XSD_SHORT=newXsdQualifiedName("short");
	XSD_DOUBLE=newXsdQualifiedName("double");
	XSD_FLOAT=newXsdQualifiedName("float");
	XSD_DECIMAL=newXsdQualifiedName("decimal");
	XSD_BOOLEAN=newXsdQualifiedName("boolean");
	XSD_BYTE=newXsdQualifiedName("byte");
	XSD_UNSIGNED_INT=newXsdQualifiedName("unsignedInt");
	XSD_UNSIGNED_LONG=newXsdQualifiedName("unsignedLong");
	XSD_INTEGER=newXsdQualifiedName("integer");
	XSD_UNSIGNED_SHORT=newXsdQualifiedName("unsignedShort");
	XSD_NON_NEGATIVE_INTEGER=newXsdQualifiedName("nonNegativeInteger");
	XSD_NON_POSITIVE_INTEGER=newXsdQualifiedName("nonPositiveInteger");
	XSD_POSITIVE_INTEGER=newXsdQualifiedName("positiveInteger");
	XSD_UNSIGNED_BYTE=newXsdQualifiedName("unsignedByte");
	XSD_ANY_URI=newXsdQualifiedName("anyURI");
	XSD_QNAME=newXsdQualifiedName("QName");
	XSD_DATETIME=newXsdQualifiedName("dateTime");
	XSD_GYEAR=newXsdQualifiedName("gYear"); 

	XSD_GMONTH=newXsdQualifiedName("gMonth");
	XSD_GDAY=newXsdQualifiedName("gDay");
	XSD_GYEAR_MONTH=newXsdQualifiedName("gYearMonth");
	XSD_GMONTH_DAY=newXsdQualifiedName("gMonthDay");
	XSD_DURATION=newXsdQualifiedName("duration");
	XSD_YEAR_MONTH_DURATION=newXsdQualifiedName("yearMonthDuration");
	XSD_DAY_TIME_DURATION=newXsdQualifiedName("dayTimeDuration");

	XSD_HEX_BINARY=newXsdQualifiedName("hexBinary");
	XSD_BASE64_BINARY=newXsdQualifiedName("base64Binary");

	XSD_LANGUAGE=newXsdQualifiedName("language");
	XSD_NORMALIZED_STRING=newXsdQualifiedName("normalizedString");
	XSD_TOKEN=newXsdQualifiedName("token");
	XSD_NMTOKEN=newXsdQualifiedName("NMTOKEN");
	XSD_NAME=newXsdQualifiedName("Name");
	XSD_NCNAME=newXsdQualifiedName("NCName");

	XSD_TIME=newXsdQualifiedName("time");
	XSD_DATE=newXsdQualifiedName("date");
	XSD_DATETIMESTAMP=newXsdQualifiedName("dateTimeStamp"); 

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


	RDF_LITERAL=newRdfQualifiedName("XMLLiteral");


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
    
    
    final public QualifiedName PROV_LANG_STRING ;
    final public QualifiedName PROV_QUALIFIED_NAME ;
    final public QualifiedName PROV_REVISION ;
    final public QualifiedName PROV_QUOTATION ;
    final public QualifiedName PROV_PRIMARY_SOURCE ;
    final public QualifiedName PROV_BUNDLE ;
    
    final public QualifiedName PROV_TYPE;
    final public QualifiedName PROV_LABEL;
    final public QualifiedName PROV_ROLE;
    final public QualifiedName PROV_LOCATION;
    final public QualifiedName PROV_VALUE;
    final public QualifiedName PROV_KEY;

    final public QualifiedName XSD_STRING;
    final public QualifiedName XSD_INT;
    final public QualifiedName XSD_LONG;
    final public QualifiedName XSD_SHORT;
    final public QualifiedName XSD_DOUBLE;
    final public QualifiedName XSD_FLOAT;
    final public QualifiedName XSD_DECIMAL;
    final public QualifiedName XSD_BOOLEAN;
    final public QualifiedName XSD_BYTE;
    final public QualifiedName XSD_UNSIGNED_INT;
    final public QualifiedName XSD_UNSIGNED_LONG;
    final public QualifiedName XSD_INTEGER;
    final public QualifiedName XSD_UNSIGNED_SHORT;
    final public QualifiedName XSD_NON_NEGATIVE_INTEGER;
    final public QualifiedName XSD_NON_POSITIVE_INTEGER;
    final public QualifiedName XSD_POSITIVE_INTEGER;
    final public QualifiedName XSD_UNSIGNED_BYTE;
    final public QualifiedName XSD_ANY_URI;
    final public QualifiedName XSD_QNAME;
    final public QualifiedName XSD_DATETIME;
    final public QualifiedName XSD_GYEAR;

    final public QualifiedName XSD_GMONTH;
    final public QualifiedName XSD_GDAY;
    final public QualifiedName XSD_GYEAR_MONTH;
    final public QualifiedName XSD_GMONTH_DAY;
    final public QualifiedName XSD_DURATION;
    final public QualifiedName XSD_YEAR_MONTH_DURATION;
    final public QualifiedName XSD_DAY_TIME_DURATION;

    final public QualifiedName XSD_HEX_BINARY;
    final public QualifiedName XSD_BASE64_BINARY;

    final public QualifiedName XSD_LANGUAGE;
    final public QualifiedName XSD_NORMALIZED_STRING;
    final public QualifiedName XSD_TOKEN;
    final public QualifiedName XSD_NMTOKEN;
    final public QualifiedName XSD_NAME;
    final public QualifiedName XSD_NCNAME;

    final public QualifiedName XSD_TIME;
    final public QualifiedName XSD_DATE;
    final public QualifiedName XSD_DATETIMESTAMP;

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

    
    final public QualifiedName RDF_LITERAL;

    
    final public QualifiedName QNAME_UNKNOWN;

}
