/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var org;
(function (org) {
    var openprovenance;
    (function (openprovenance) {
        var prov;
        (function (prov) {
            var model;
            (function (model) {
                /**
                 * Java class defining a set of constant Qualified Names in the PROV, XSD, and RDF namespaces.
                 * <p>Given that the names are QualifiedNames, they need to be constructed with a factory.
                 * This explains why these names cannot be defined as static, final constants.
                 * @param {org.openprovenance.prov.model.ProvFactory} pFactory
                 * @class
                 */
                class Name {
                    constructor(pFactory) {
                        if (this.pFactory === undefined) {
                            this.pFactory = null;
                        }
                        if (this.PROV_LANG_STRING === undefined) {
                            this.PROV_LANG_STRING = null;
                        }
                        if (this.PROV_QUALIFIED_NAME === undefined) {
                            this.PROV_QUALIFIED_NAME = null;
                        }
                        if (this.PROV_REVISION === undefined) {
                            this.PROV_REVISION = null;
                        }
                        if (this.PROV_QUOTATION === undefined) {
                            this.PROV_QUOTATION = null;
                        }
                        if (this.PROV_PRIMARY_SOURCE === undefined) {
                            this.PROV_PRIMARY_SOURCE = null;
                        }
                        if (this.PROV_BUNDLE === undefined) {
                            this.PROV_BUNDLE = null;
                        }
                        if (this.PROV_TYPE === undefined) {
                            this.PROV_TYPE = null;
                        }
                        if (this.PROV_LABEL === undefined) {
                            this.PROV_LABEL = null;
                        }
                        if (this.PROV_ROLE === undefined) {
                            this.PROV_ROLE = null;
                        }
                        if (this.PROV_LOCATION === undefined) {
                            this.PROV_LOCATION = null;
                        }
                        if (this.PROV_VALUE === undefined) {
                            this.PROV_VALUE = null;
                        }
                        if (this.PROV_KEY === undefined) {
                            this.PROV_KEY = null;
                        }
                        if (this.XSD_STRING === undefined) {
                            this.XSD_STRING = null;
                        }
                        if (this.XSD_INT === undefined) {
                            this.XSD_INT = null;
                        }
                        if (this.XSD_LONG === undefined) {
                            this.XSD_LONG = null;
                        }
                        if (this.XSD_SHORT === undefined) {
                            this.XSD_SHORT = null;
                        }
                        if (this.XSD_DOUBLE === undefined) {
                            this.XSD_DOUBLE = null;
                        }
                        if (this.XSD_FLOAT === undefined) {
                            this.XSD_FLOAT = null;
                        }
                        if (this.XSD_DECIMAL === undefined) {
                            this.XSD_DECIMAL = null;
                        }
                        if (this.XSD_BOOLEAN === undefined) {
                            this.XSD_BOOLEAN = null;
                        }
                        if (this.XSD_BYTE === undefined) {
                            this.XSD_BYTE = null;
                        }
                        if (this.XSD_UNSIGNED_INT === undefined) {
                            this.XSD_UNSIGNED_INT = null;
                        }
                        if (this.XSD_UNSIGNED_LONG === undefined) {
                            this.XSD_UNSIGNED_LONG = null;
                        }
                        if (this.XSD_INTEGER === undefined) {
                            this.XSD_INTEGER = null;
                        }
                        if (this.XSD_UNSIGNED_SHORT === undefined) {
                            this.XSD_UNSIGNED_SHORT = null;
                        }
                        if (this.XSD_NON_NEGATIVE_INTEGER === undefined) {
                            this.XSD_NON_NEGATIVE_INTEGER = null;
                        }
                        if (this.XSD_NON_POSITIVE_INTEGER === undefined) {
                            this.XSD_NON_POSITIVE_INTEGER = null;
                        }
                        if (this.XSD_POSITIVE_INTEGER === undefined) {
                            this.XSD_POSITIVE_INTEGER = null;
                        }
                        if (this.XSD_UNSIGNED_BYTE === undefined) {
                            this.XSD_UNSIGNED_BYTE = null;
                        }
                        if (this.XSD_ANY_URI === undefined) {
                            this.XSD_ANY_URI = null;
                        }
                        if (this.XSD_DATETIME === undefined) {
                            this.XSD_DATETIME = null;
                        }
                        if (this.XSD_GYEAR === undefined) {
                            this.XSD_GYEAR = null;
                        }
                        if (this.XSD_GMONTH === undefined) {
                            this.XSD_GMONTH = null;
                        }
                        if (this.XSD_GDAY === undefined) {
                            this.XSD_GDAY = null;
                        }
                        if (this.XSD_GYEAR_MONTH === undefined) {
                            this.XSD_GYEAR_MONTH = null;
                        }
                        if (this.XSD_GMONTH_DAY === undefined) {
                            this.XSD_GMONTH_DAY = null;
                        }
                        if (this.XSD_DURATION === undefined) {
                            this.XSD_DURATION = null;
                        }
                        if (this.XSD_YEAR_MONTH_DURATION === undefined) {
                            this.XSD_YEAR_MONTH_DURATION = null;
                        }
                        if (this.XSD_DAY_TIME_DURATION === undefined) {
                            this.XSD_DAY_TIME_DURATION = null;
                        }
                        if (this.XSD_HEX_BINARY === undefined) {
                            this.XSD_HEX_BINARY = null;
                        }
                        if (this.XSD_BASE64_BINARY === undefined) {
                            this.XSD_BASE64_BINARY = null;
                        }
                        if (this.XSD_LANGUAGE === undefined) {
                            this.XSD_LANGUAGE = null;
                        }
                        if (this.XSD_NORMALIZED_STRING === undefined) {
                            this.XSD_NORMALIZED_STRING = null;
                        }
                        if (this.XSD_TOKEN === undefined) {
                            this.XSD_TOKEN = null;
                        }
                        if (this.XSD_NMTOKEN === undefined) {
                            this.XSD_NMTOKEN = null;
                        }
                        if (this.XSD_NAME === undefined) {
                            this.XSD_NAME = null;
                        }
                        if (this.XSD_NCNAME === undefined) {
                            this.XSD_NCNAME = null;
                        }
                        if (this.XSD_TIME === undefined) {
                            this.XSD_TIME = null;
                        }
                        if (this.XSD_DATE === undefined) {
                            this.XSD_DATE = null;
                        }
                        if (this.XSD_DATETIMESTAMP === undefined) {
                            this.XSD_DATETIMESTAMP = null;
                        }
                        if (this.RDF_LITERAL === undefined) {
                            this.RDF_LITERAL = null;
                        }
                        if (this.QUALIFIED_NAME_UNKNOWN_TYPE === undefined) {
                            this.QUALIFIED_NAME_UNKNOWN_TYPE = null;
                        }
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
                    newXsdQualifiedName(local) {
                        return this.pFactory.newQualifiedName$java_lang_String$java_lang_String$java_lang_String(org.openprovenance.prov.model.NamespacePrefixMapper.XSD_NS, local, org.openprovenance.prov.model.NamespacePrefixMapper.XSD_PREFIX);
                    }
                    newToolboxQualifiedName(local) {
                        return this.pFactory.newQualifiedName$java_lang_String$java_lang_String$java_lang_String(org.openprovenance.prov.model.NamespacePrefixMapper.TOOLBOX_NS_$LI$(), local, org.openprovenance.prov.model.NamespacePrefixMapper.TOOLBOX_PREFIX);
                    }
                    newRdfQualifiedName(local) {
                        return this.pFactory.newQualifiedName$java_lang_String$java_lang_String$java_lang_String(org.openprovenance.prov.model.NamespacePrefixMapper.RDF_NS, local, "rdf");
                    }
                    newProvQualifiedName(local) {
                        return this.pFactory.newQualifiedName$java_lang_String$java_lang_String$java_lang_String(org.openprovenance.prov.model.NamespacePrefixMapper.PROV_NS, local, "prov");
                    }
                    newProvExtQualifiedName(local) {
                        return this.pFactory.newQualifiedName$java_lang_String$java_lang_String$java_lang_String(org.openprovenance.prov.model.NamespacePrefixMapper.PROV_EXT_NS, local, "provext");
                    }
                }
                model.Name = Name;
                Name["__class"] = "org.openprovenance.prov.model.Name";
            })(model = prov.model || (prov.model = {}));
        })(prov = openprovenance.prov || (openprovenance.prov = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
