/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.model {
    /**
     * Java class defining a set of constant Qualified Names in the PROV, XSD, and RDF namespaces.
     * <p>Given that the names are QualifiedNames, they need to be constructed with a factory.
     * This explains why these names cannot be defined as static, final constants.
     * @param {org.openprovenance.prov.model.ProvFactory} pFactory
     * @class
     */
    export class Name {
        /*private*/ pFactory: org.openprovenance.prov.model.ProvFactory;

        public constructor(pFactory: org.openprovenance.prov.model.ProvFactory) {
            if (this.pFactory === undefined) { this.pFactory = null; }
            if (this.PROV_LANG_STRING === undefined) { this.PROV_LANG_STRING = null; }
            if (this.PROV_QUALIFIED_NAME === undefined) { this.PROV_QUALIFIED_NAME = null; }
            if (this.PROV_REVISION === undefined) { this.PROV_REVISION = null; }
            if (this.PROV_QUOTATION === undefined) { this.PROV_QUOTATION = null; }
            if (this.PROV_PRIMARY_SOURCE === undefined) { this.PROV_PRIMARY_SOURCE = null; }
            if (this.PROV_BUNDLE === undefined) { this.PROV_BUNDLE = null; }
            if (this.PROV_TYPE === undefined) { this.PROV_TYPE = null; }
            if (this.PROV_LABEL === undefined) { this.PROV_LABEL = null; }
            if (this.PROV_ROLE === undefined) { this.PROV_ROLE = null; }
            if (this.PROV_LOCATION === undefined) { this.PROV_LOCATION = null; }
            if (this.PROV_VALUE === undefined) { this.PROV_VALUE = null; }
            if (this.PROV_KEY === undefined) { this.PROV_KEY = null; }
            if (this.XSD_STRING === undefined) { this.XSD_STRING = null; }
            if (this.XSD_INT === undefined) { this.XSD_INT = null; }
            if (this.XSD_LONG === undefined) { this.XSD_LONG = null; }
            if (this.XSD_SHORT === undefined) { this.XSD_SHORT = null; }
            if (this.XSD_DOUBLE === undefined) { this.XSD_DOUBLE = null; }
            if (this.XSD_FLOAT === undefined) { this.XSD_FLOAT = null; }
            if (this.XSD_DECIMAL === undefined) { this.XSD_DECIMAL = null; }
            if (this.XSD_BOOLEAN === undefined) { this.XSD_BOOLEAN = null; }
            if (this.XSD_BYTE === undefined) { this.XSD_BYTE = null; }
            if (this.XSD_UNSIGNED_INT === undefined) { this.XSD_UNSIGNED_INT = null; }
            if (this.XSD_UNSIGNED_LONG === undefined) { this.XSD_UNSIGNED_LONG = null; }
            if (this.XSD_INTEGER === undefined) { this.XSD_INTEGER = null; }
            if (this.XSD_UNSIGNED_SHORT === undefined) { this.XSD_UNSIGNED_SHORT = null; }
            if (this.XSD_NON_NEGATIVE_INTEGER === undefined) { this.XSD_NON_NEGATIVE_INTEGER = null; }
            if (this.XSD_NON_POSITIVE_INTEGER === undefined) { this.XSD_NON_POSITIVE_INTEGER = null; }
            if (this.XSD_POSITIVE_INTEGER === undefined) { this.XSD_POSITIVE_INTEGER = null; }
            if (this.XSD_UNSIGNED_BYTE === undefined) { this.XSD_UNSIGNED_BYTE = null; }
            if (this.XSD_ANY_URI === undefined) { this.XSD_ANY_URI = null; }
            if (this.XSD_DATETIME === undefined) { this.XSD_DATETIME = null; }
            if (this.XSD_GYEAR === undefined) { this.XSD_GYEAR = null; }
            if (this.XSD_GMONTH === undefined) { this.XSD_GMONTH = null; }
            if (this.XSD_GDAY === undefined) { this.XSD_GDAY = null; }
            if (this.XSD_GYEAR_MONTH === undefined) { this.XSD_GYEAR_MONTH = null; }
            if (this.XSD_GMONTH_DAY === undefined) { this.XSD_GMONTH_DAY = null; }
            if (this.XSD_DURATION === undefined) { this.XSD_DURATION = null; }
            if (this.XSD_YEAR_MONTH_DURATION === undefined) { this.XSD_YEAR_MONTH_DURATION = null; }
            if (this.XSD_DAY_TIME_DURATION === undefined) { this.XSD_DAY_TIME_DURATION = null; }
            if (this.XSD_HEX_BINARY === undefined) { this.XSD_HEX_BINARY = null; }
            if (this.XSD_BASE64_BINARY === undefined) { this.XSD_BASE64_BINARY = null; }
            if (this.XSD_LANGUAGE === undefined) { this.XSD_LANGUAGE = null; }
            if (this.XSD_NORMALIZED_STRING === undefined) { this.XSD_NORMALIZED_STRING = null; }
            if (this.XSD_TOKEN === undefined) { this.XSD_TOKEN = null; }
            if (this.XSD_NMTOKEN === undefined) { this.XSD_NMTOKEN = null; }
            if (this.XSD_NAME === undefined) { this.XSD_NAME = null; }
            if (this.XSD_NCNAME === undefined) { this.XSD_NCNAME = null; }
            if (this.XSD_TIME === undefined) { this.XSD_TIME = null; }
            if (this.XSD_DATE === undefined) { this.XSD_DATE = null; }
            if (this.XSD_DATETIMESTAMP === undefined) { this.XSD_DATETIMESTAMP = null; }
            if (this.RDF_LITERAL === undefined) { this.RDF_LITERAL = null; }
            if (this.QUALIFIED_NAME_UNKNOWN_TYPE === undefined) { this.QUALIFIED_NAME_UNKNOWN_TYPE = null; }
            this.pFactory = pFactory;
            this.PROV_LANG_STRING = this.newProvQualifiedName("InternationalizedString");
            this.PROV_QUALIFIED_NAME = this.newProvQualifiedName("QUALIFIED_NAME");
            this.PROV_REVISION = this.newProvQualifiedName("Revision");
            this.PROV_QUOTATION = this.newProvQualifiedName("Quotation");
            this.PROV_PRIMARY_SOURCE = this.newProvQualifiedName("PrimarySource");
            this.PROV_BUNDLE = this.newProvQualifiedName("Bundle");
            this.PROV_TYPE = this.newProvQualifiedName("type");
            this.PROV_LABEL = this.newProvQualifiedName("label");
            this.PROV_ROLE = this.newProvQualifiedName("role");
            this.PROV_LOCATION = this.newProvQualifiedName("location");
            this.PROV_VALUE = this.newProvQualifiedName("value");
            this.PROV_KEY = this.newProvQualifiedName("key");
            this.XSD_STRING = this.newXsdQualifiedName("string");
            this.XSD_INT = this.newXsdQualifiedName("int");
            this.XSD_LONG = this.newXsdQualifiedName("long");
            this.XSD_SHORT = this.newXsdQualifiedName("short");
            this.XSD_DOUBLE = this.newXsdQualifiedName("double");
            this.XSD_FLOAT = this.newXsdQualifiedName("float");
            this.XSD_DECIMAL = this.newXsdQualifiedName("decimal");
            this.XSD_BOOLEAN = this.newXsdQualifiedName("boolean");
            this.XSD_BYTE = this.newXsdQualifiedName("byte");
            this.XSD_UNSIGNED_INT = this.newXsdQualifiedName("unsignedInt");
            this.XSD_UNSIGNED_LONG = this.newXsdQualifiedName("unsignedLong");
            this.XSD_INTEGER = this.newXsdQualifiedName("integer");
            this.XSD_UNSIGNED_SHORT = this.newXsdQualifiedName("unsignedShort");
            this.XSD_NON_NEGATIVE_INTEGER = this.newXsdQualifiedName("nonNegativeInteger");
            this.XSD_NON_POSITIVE_INTEGER = this.newXsdQualifiedName("nonPositiveInteger");
            this.XSD_POSITIVE_INTEGER = this.newXsdQualifiedName("positiveInteger");
            this.XSD_UNSIGNED_BYTE = this.newXsdQualifiedName("unsignedByte");
            this.XSD_ANY_URI = this.newXsdQualifiedName("anyURI");
            this.XSD_DATETIME = this.newXsdQualifiedName("dateTime");
            this.XSD_GYEAR = this.newXsdQualifiedName("gYear");
            this.XSD_GMONTH = this.newXsdQualifiedName("gMonth");
            this.XSD_GDAY = this.newXsdQualifiedName("gDay");
            this.XSD_GYEAR_MONTH = this.newXsdQualifiedName("gYearMonth");
            this.XSD_GMONTH_DAY = this.newXsdQualifiedName("gMonthDay");
            this.XSD_DURATION = this.newXsdQualifiedName("duration");
            this.XSD_YEAR_MONTH_DURATION = this.newXsdQualifiedName("yearMonthDuration");
            this.XSD_DAY_TIME_DURATION = this.newXsdQualifiedName("dayTimeDuration");
            this.XSD_HEX_BINARY = this.newXsdQualifiedName("hexBinary");
            this.XSD_BASE64_BINARY = this.newXsdQualifiedName("base64Binary");
            this.XSD_LANGUAGE = this.newXsdQualifiedName("language");
            this.XSD_NORMALIZED_STRING = this.newXsdQualifiedName("normalizedString");
            this.XSD_TOKEN = this.newXsdQualifiedName("token");
            this.XSD_NMTOKEN = this.newXsdQualifiedName("NMTOKEN");
            this.XSD_NAME = this.newXsdQualifiedName("Name");
            this.XSD_NCNAME = this.newXsdQualifiedName("NCName");
            this.XSD_TIME = this.newXsdQualifiedName("time");
            this.XSD_DATE = this.newXsdQualifiedName("date");
            this.XSD_DATETIMESTAMP = this.newXsdQualifiedName("dateTimeStamp");
            this.RDF_LITERAL = this.newRdfQualifiedName("XMLLiteral");
            this.QUALIFIED_NAME_UNKNOWN_TYPE = this.newToolboxQualifiedName("UNKNOWN_TYPE");
        }

        public newXsdQualifiedName(local: string): org.openprovenance.prov.model.QualifiedName {
            return this.pFactory.newQualifiedName$java_lang_String$java_lang_String$java_lang_String(org.openprovenance.prov.model.NamespacePrefixMapper.XSD_NS, local, org.openprovenance.prov.model.NamespacePrefixMapper.XSD_PREFIX);
        }

        public newToolboxQualifiedName(local: string): org.openprovenance.prov.model.QualifiedName {
            return this.pFactory.newQualifiedName$java_lang_String$java_lang_String$java_lang_String(org.openprovenance.prov.model.NamespacePrefixMapper.TOOLBOX_NS_$LI$(), local, org.openprovenance.prov.model.NamespacePrefixMapper.TOOLBOX_PREFIX);
        }

        public newRdfQualifiedName(local: string): org.openprovenance.prov.model.QualifiedName {
            return this.pFactory.newQualifiedName$java_lang_String$java_lang_String$java_lang_String(org.openprovenance.prov.model.NamespacePrefixMapper.RDF_NS, local, "rdf");
        }

        public newProvQualifiedName(local: string): org.openprovenance.prov.model.QualifiedName {
            return this.pFactory.newQualifiedName$java_lang_String$java_lang_String$java_lang_String(org.openprovenance.prov.model.NamespacePrefixMapper.PROV_NS, local, "prov");
        }

        public newProvExtQualifiedName(local: string): org.openprovenance.prov.model.QualifiedName {
            return this.pFactory.newQualifiedName$java_lang_String$java_lang_String$java_lang_String(org.openprovenance.prov.model.NamespacePrefixMapper.PROV_EXT_NS, local, "provext");
        }

        public PROV_LANG_STRING: org.openprovenance.prov.model.QualifiedName;

        public PROV_QUALIFIED_NAME: org.openprovenance.prov.model.QualifiedName;

        public PROV_REVISION: org.openprovenance.prov.model.QualifiedName;

        public PROV_QUOTATION: org.openprovenance.prov.model.QualifiedName;

        public PROV_PRIMARY_SOURCE: org.openprovenance.prov.model.QualifiedName;

        public PROV_BUNDLE: org.openprovenance.prov.model.QualifiedName;

        public PROV_TYPE: org.openprovenance.prov.model.QualifiedName;

        public PROV_LABEL: org.openprovenance.prov.model.QualifiedName;

        public PROV_ROLE: org.openprovenance.prov.model.QualifiedName;

        public PROV_LOCATION: org.openprovenance.prov.model.QualifiedName;

        public PROV_VALUE: org.openprovenance.prov.model.QualifiedName;

        public PROV_KEY: org.openprovenance.prov.model.QualifiedName;

        /**
         * Qualified Name for <a href="http://www.w3.org/TR/xmlschema-2/#string">xsd:string</a>
         */
        public XSD_STRING: org.openprovenance.prov.model.QualifiedName;

        /**
         * Qualified Name for <a href="http://www.w3.org/TR/xmlschema-2/#int">xsd:int</a>
         */
        public XSD_INT: org.openprovenance.prov.model.QualifiedName;

        /**
         * Qualified Name for <a href="http://www.w3.org/TR/xmlschema-2/#long">xsd:long</a>
         */
        public XSD_LONG: org.openprovenance.prov.model.QualifiedName;

        /**
         * Qualified Name for <a href="http://www.w3.org/TR/xmlschema-2/#short">xsd:short</a>
         */
        public XSD_SHORT: org.openprovenance.prov.model.QualifiedName;

        /**
         * Qualified Name for <a href="http://www.w3.org/TR/xmlschema-2/#double">xsd:double</a>
         */
        public XSD_DOUBLE: org.openprovenance.prov.model.QualifiedName;

        /**
         * Qualified Name for <a href="http://www.w3.org/TR/xmlschema-2/#float">xsd:float</a>
         */
        public XSD_FLOAT: org.openprovenance.prov.model.QualifiedName;

        /**
         * Qualified Name for <a href="http://www.w3.org/TR/xmlschema-2/#decimal">xsd:decimal</a>
         */
        public XSD_DECIMAL: org.openprovenance.prov.model.QualifiedName;

        /**
         * Qualified Name for <a href="http://www.w3.org/TR/xmlschema-2/#boolean">xsd:boolean</a>
         */
        public XSD_BOOLEAN: org.openprovenance.prov.model.QualifiedName;

        /**
         * Qualified Name for <a href="http://www.w3.org/TR/xmlschema-2/#byte">xsd:byte</a>
         */
        public XSD_BYTE: org.openprovenance.prov.model.QualifiedName;

        /**
         * Qualified Name for <a href="http://www.w3.org/TR/xmlschema-2/#unsignedInt">xsd:unsignedInt</a>
         */
        public XSD_UNSIGNED_INT: org.openprovenance.prov.model.QualifiedName;

        /**
         * Qualified Name for <a href="http://www.w3.org/TR/xmlschema-2/#unsignedLong">xsd:unsignedLong</a>
         */
        public XSD_UNSIGNED_LONG: org.openprovenance.prov.model.QualifiedName;

        /**
         * Qualified Name for <a href="http://www.w3.org/TR/xmlschema-2/#integer">xsd:integer</a>
         */
        public XSD_INTEGER: org.openprovenance.prov.model.QualifiedName;

        /**
         * Qualified Name for <a href="http://www.w3.org/TR/xmlschema-2/#unsignedShort">xsd:unsignedShort</a>
         */
        public XSD_UNSIGNED_SHORT: org.openprovenance.prov.model.QualifiedName;

        /**
         * Qualified Name for <a href="http://www.w3.org/TR/xmlschema-2/#nonNegativeInteger">xsd:nonNegativeInteger</a>
         */
        public XSD_NON_NEGATIVE_INTEGER: org.openprovenance.prov.model.QualifiedName;

        /**
         * Qualified Name for <a href="http://www.w3.org/TR/xmlschema-2/#nonPositiveInteger">xsd:nonPositiveInteger</a>
         */
        public XSD_NON_POSITIVE_INTEGER: org.openprovenance.prov.model.QualifiedName;

        /**
         * Qualified Name for <a href="http://www.w3.org/TR/xmlschema-2/#positiveInteger">xsd:positiveInteger</a>
         */
        public XSD_POSITIVE_INTEGER: org.openprovenance.prov.model.QualifiedName;

        /**
         * Qualified Name for <a href="http://www.w3.org/TR/xmlschema-2/#unsignedByte">xsd:unsignedByte</a>
         */
        public XSD_UNSIGNED_BYTE: org.openprovenance.prov.model.QualifiedName;

        /**
         * Qualified Name for <a href="http://www.w3.org/TR/xmlschema-2/#anyURI">xsd:anyURI</a>
         */
        public XSD_ANY_URI: org.openprovenance.prov.model.QualifiedName;

        /**
         * Qualified Name for <a href="http://www.w3.org/TR/xmlschema-2/#dateTime">xsd:dateTime</a>
         */
        public XSD_DATETIME: org.openprovenance.prov.model.QualifiedName;

        /**
         * Qualified Name for <a href="http://www.w3.org/TR/xmlschema-2/#gYear">xsd:gYear</a>
         */
        public XSD_GYEAR: org.openprovenance.prov.model.QualifiedName;

        public XSD_GMONTH: org.openprovenance.prov.model.QualifiedName;

        public XSD_GDAY: org.openprovenance.prov.model.QualifiedName;

        public XSD_GYEAR_MONTH: org.openprovenance.prov.model.QualifiedName;

        public XSD_GMONTH_DAY: org.openprovenance.prov.model.QualifiedName;

        public XSD_DURATION: org.openprovenance.prov.model.QualifiedName;

        public XSD_YEAR_MONTH_DURATION: org.openprovenance.prov.model.QualifiedName;

        public XSD_DAY_TIME_DURATION: org.openprovenance.prov.model.QualifiedName;

        public XSD_HEX_BINARY: org.openprovenance.prov.model.QualifiedName;

        public XSD_BASE64_BINARY: org.openprovenance.prov.model.QualifiedName;

        public XSD_LANGUAGE: org.openprovenance.prov.model.QualifiedName;

        public XSD_NORMALIZED_STRING: org.openprovenance.prov.model.QualifiedName;

        public XSD_TOKEN: org.openprovenance.prov.model.QualifiedName;

        public XSD_NMTOKEN: org.openprovenance.prov.model.QualifiedName;

        public XSD_NAME: org.openprovenance.prov.model.QualifiedName;

        public XSD_NCNAME: org.openprovenance.prov.model.QualifiedName;

        public XSD_TIME: org.openprovenance.prov.model.QualifiedName;

        public XSD_DATE: org.openprovenance.prov.model.QualifiedName;

        public XSD_DATETIMESTAMP: org.openprovenance.prov.model.QualifiedName;

        public RDF_LITERAL: org.openprovenance.prov.model.QualifiedName;

        public QUALIFIED_NAME_UNKNOWN_TYPE: org.openprovenance.prov.model.QualifiedName;
    }
    Name["__class"] = "org.openprovenance.prov.model.Name";

}

