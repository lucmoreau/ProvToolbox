package org.openprovenance.prov.model;

/** Java class defining a set of constant Qualified Names in the PROV, XSD, and RDF namespaces. 
 * <p>Given that the names are QualifiedNames, they need to be constructed with a factory. 
 * This explains why these names cannot be defined as static, final constants.*/
public class Name {
    final private ProvFactory pFactory;
    
    public Name(ProvFactory pFactory) {
	this.pFactory=pFactory;
	PROV_LANG_STRING = newProvQualifiedName("InternationalizedString");
	PROV_QUALIFIED_NAME = newProvQualifiedName("QUALIFIED_NAME");
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

	RDF_LITERAL=newRdfQualifiedName("XMLLiteral");


	QUALIFIED_NAME_UNKNOWN_TYPE=newToolboxQualifiedName("UNKNOWN_TYPE");

    }

    public QualifiedName newXsdQualifiedName(String local) {
        return pFactory.newQualifiedName(NamespacePrefixMapper.XSD_NS, local, NamespacePrefixMapper.XSD_PREFIX);
    }
    public QualifiedName newToolboxQualifiedName(String local) {
        return pFactory.newQualifiedName(NamespacePrefixMapper.TOOLBOX_NS, local, NamespacePrefixMapper.TOOLBOX_PREFIX);
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

    /** Qualified Name for <a href="http://www.w3.org/TR/xmlschema-2/#string">xsd:string</a>  */
    final public QualifiedName XSD_STRING;
    /** Qualified Name for <a href="http://www.w3.org/TR/xmlschema-2/#int">xsd:int</a>  */
    final public QualifiedName XSD_INT;
    /** Qualified Name for <a href="http://www.w3.org/TR/xmlschema-2/#long">xsd:long</a>  */
    final public QualifiedName XSD_LONG;
    /** Qualified Name for <a href="http://www.w3.org/TR/xmlschema-2/#short">xsd:short</a>  */
    final public QualifiedName XSD_SHORT;
    /** Qualified Name for <a href="http://www.w3.org/TR/xmlschema-2/#double">xsd:double</a>  */
    final public QualifiedName XSD_DOUBLE;
    /** Qualified Name for <a href="http://www.w3.org/TR/xmlschema-2/#float">xsd:float</a>  */
    final public QualifiedName XSD_FLOAT;
    /** Qualified Name for <a href="http://www.w3.org/TR/xmlschema-2/#decimal">xsd:decimal</a>  */
    final public QualifiedName XSD_DECIMAL;
    /** Qualified Name for <a href="http://www.w3.org/TR/xmlschema-2/#boolean">xsd:boolean</a>  */
    final public QualifiedName XSD_BOOLEAN;
    /** Qualified Name for <a href="http://www.w3.org/TR/xmlschema-2/#byte">xsd:byte</a>  */
    final public QualifiedName XSD_BYTE;
    /** Qualified Name for <a href="http://www.w3.org/TR/xmlschema-2/#unsignedInt">xsd:unsignedInt</a>  */
    final public QualifiedName XSD_UNSIGNED_INT;
    /** Qualified Name for <a href="http://www.w3.org/TR/xmlschema-2/#unsignedLong">xsd:unsignedLong</a>  */
    final public QualifiedName XSD_UNSIGNED_LONG;
    /** Qualified Name for <a href="http://www.w3.org/TR/xmlschema-2/#integer">xsd:integer</a>  */
    final public QualifiedName XSD_INTEGER;
    /** Qualified Name for <a href="http://www.w3.org/TR/xmlschema-2/#unsignedShort">xsd:unsignedShort</a>  */
    final public QualifiedName XSD_UNSIGNED_SHORT;
    /** Qualified Name for <a href="http://www.w3.org/TR/xmlschema-2/#nonNegativeInteger">xsd:nonNegativeInteger</a>  */
    final public QualifiedName XSD_NON_NEGATIVE_INTEGER;
    /** Qualified Name for <a href="http://www.w3.org/TR/xmlschema-2/#nonPositiveInteger">xsd:nonPositiveInteger</a>  */
    final public QualifiedName XSD_NON_POSITIVE_INTEGER;
    /** Qualified Name for <a href="http://www.w3.org/TR/xmlschema-2/#positiveInteger">xsd:positiveInteger</a>  */
    final public QualifiedName XSD_POSITIVE_INTEGER;
    /** Qualified Name for <a href="http://www.w3.org/TR/xmlschema-2/#unsignedByte">xsd:unsignedByte</a>  */
    final public QualifiedName XSD_UNSIGNED_BYTE;
    /** Qualified Name for <a href="http://www.w3.org/TR/xmlschema-2/#anyURI">xsd:anyURI</a>  */
    final public QualifiedName XSD_ANY_URI;
    
  
    /** Qualified Name for <a href="http://www.w3.org/TR/xmlschema-2/#dateTime">xsd:dateTime</a>  */
    final public QualifiedName XSD_DATETIME;
    /** Qualified Name for <a href="http://www.w3.org/TR/xmlschema-2/#gYear">xsd:gYear</a>  */
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

    
    final public QualifiedName RDF_LITERAL;

    
    final public QualifiedName QUALIFIED_NAME_UNKNOWN_TYPE;
    
    



}
