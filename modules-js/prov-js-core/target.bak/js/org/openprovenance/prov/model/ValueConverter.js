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
                 * Conversion from String to Object and vice-versa for common xsd types.
                 *
                 * @author lavm
                 * @param {org.openprovenance.prov.model.ProvFactory} pFactory
                 * @class
                 */
                class ValueConverter {
                    constructor(pFactory) {
                        if (this.pFactory === undefined) {
                            this.pFactory = null;
                        }
                        if (this.name === undefined) {
                            this.name = null;
                        }
                        this.pFactory = pFactory;
                        this.name = pFactory.getName();
                    }
                    /**
                     * Converts a string into a Java object, according to type provided. This
                     * function does not convert to QualifiedName since this requires access to
                     * a prefix-namespace mapping.
                     *
                     * @param {*} datatype
                     * any xsd datatype, provided it is not xsd:QName
                     * @param {string} value
                     * is a String
                     * @return {*} an object
                     */
                    convertToJava(datatype, value) {
                        if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(datatype, this.name.XSD_STRING))
                            return value;
                        if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(datatype, this.name.XSD_INT))
                            return /* parseInt */ parseInt(value);
                        if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(datatype, this.name.XSD_LONG))
                            return /* parseLong */ parseInt(value);
                        if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(datatype, this.name.XSD_SHORT))
                            return /* parseShort */ parseInt(value);
                        if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(datatype, this.name.XSD_DOUBLE))
                            return /* parseDouble */ parseFloat(value);
                        if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(datatype, this.name.XSD_FLOAT))
                            return /* parseFloat */ parseFloat(value);
                        if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(datatype, this.name.XSD_DECIMAL))
                            return new java.math.BigDecimal(value);
                        if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(datatype, this.name.XSD_BOOLEAN))
                            return javaemul.internal.BooleanHelper.parseBoolean(value);
                        if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(datatype, this.name.XSD_BYTE))
                            return /* parseByte */ parseInt(value);
                        if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(datatype, this.name.XSD_UNSIGNED_INT))
                            return /* parseLong */ parseInt(value);
                        if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(datatype, this.name.XSD_UNSIGNED_SHORT))
                            return /* parseInt */ parseInt(value);
                        if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(datatype, this.name.XSD_UNSIGNED_BYTE))
                            return /* parseShort */ parseInt(value);
                        if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(datatype, this.name.XSD_UNSIGNED_LONG))
                            return new java.math.BigInteger(value);
                        if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(datatype, this.name.XSD_INTEGER))
                            return new java.math.BigInteger(value);
                        if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(datatype, this.name.XSD_NON_NEGATIVE_INTEGER))
                            return new java.math.BigInteger(value);
                        if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(datatype, this.name.XSD_NON_POSITIVE_INTEGER))
                            return new java.math.BigInteger(value);
                        if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(datatype, this.name.XSD_POSITIVE_INTEGER))
                            return new java.math.BigInteger(value);
                        if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(datatype, this.name.XSD_ANY_URI)) {
                            return value;
                        }
                        if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(datatype, this.name.PROV_QUALIFIED_NAME)) {
                            throw new org.openprovenance.prov.model.exception.ConverterException("Not conversion to xsd:QName");
                        }
                        if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(datatype, this.name.XSD_DATETIME)) {
                            return this.pFactory.newISOTime(value);
                        }
                        if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(datatype, this.name.XSD_GYEAR)) {
                            return this.pFactory.newGYear(new Number(value).valueOf());
                        }
                        if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(datatype, this.name.XSD_GMONTH)) {
                            return this.pFactory.newGMonth(new Number(value.substring(2)).valueOf());
                        }
                        if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(datatype, this.name.XSD_GMONTH_DAY)) {
                            return this.pFactory.newGMonthDay(new Number(value.substring(2, 4)).valueOf(), new Number(value.substring(5, 7)).valueOf());
                        }
                        if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(datatype, this.name.XSD_GDAY)) {
                            return this.pFactory.newGDay(new Number(value.substring(3)).valueOf());
                        }
                        if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(datatype, this.name.XSD_DURATION)) {
                            return this.pFactory.newDuration(value);
                        }
                        if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(datatype, this.name.XSD_DAY_TIME_DURATION)) {
                            return this.pFactory.newDuration(value);
                        }
                        if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(datatype, this.name.XSD_YEAR_MONTH_DURATION)) {
                            return this.pFactory.newDuration(value);
                        }
                        if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(datatype, this.name.XSD_LANGUAGE)) {
                            return value;
                        }
                        if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(datatype, this.name.XSD_TOKEN)) {
                            return value;
                        }
                        if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(datatype, this.name.XSD_NMTOKEN)) {
                            return value;
                        }
                        if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(datatype, this.name.XSD_NAME)) {
                            return value;
                        }
                        if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(datatype, this.name.XSD_NCNAME)) {
                            return value;
                        }
                        if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(datatype, this.name.XSD_NORMALIZED_STRING)) {
                            return value;
                        }
                        if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(datatype, this.name.XSD_HEX_BINARY)) {
                            return this.pFactory.hexDecoding(value);
                        }
                        if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(datatype, this.name.XSD_BASE64_BINARY)) {
                            return this.pFactory.base64Decoding(value);
                        }
                        if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(datatype, this.name.RDF_LITERAL)) {
                            return this.convertXMLLiteral(value);
                        }
                        throw Object.defineProperty(new Error("UNKNOWN literal type " + datatype), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.UnsupportedOperationException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                    }
                    convertXMLLiteral(value) {
                        const dp = new org.openprovenance.prov.model.DOMProcessing(this.pFactory);
                        const db = org.openprovenance.prov.model.DOMProcessing.builder_$LI$();
                        let __in;
                        let doc = null;
                        try {
                            __in = org.apache.commons.io.IOUtils.toInputStream(value, "UTF-8");
                            doc = db.parse(__in);
                        }
                        catch (__e) {
                            if (__e != null && (__e["__classes"] && __e["__classes"].indexOf("java.io.IOException") >= 0)) {
                                const e = __e;
                                console.error(e.message, e);
                            }
                            if (__e != null && __e instanceof org.xml.sax.SAXException) {
                                const e = __e;
                                console.error(e.message, e);
                            }
                        }
                        if (doc == null)
                            return null;
                        return doc.getDocumentElement();
                    }
                    getXsdType(o) {
                        if (typeof o === 'number')
                            return this.name.XSD_INT;
                        if (typeof o === 'string')
                            return this.name.XSD_STRING;
                        if (o != null && (o.constructor != null && o.constructor["__interfaces"] != null && o.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.LangString") >= 0))
                            return this.name.XSD_STRING;
                        if (o != null && false && o instanceof java.math.BigInteger) //LUC
                            return this.name.XSD_INTEGER;
                        if (typeof o === 'number')
                            return this.name.XSD_LONG;
                        if (typeof o === 'number')
                            return this.name.XSD_SHORT;
                        if (typeof o === 'number')
                            return this.name.XSD_DOUBLE;
                        if (typeof o === 'number')
                            return this.name.XSD_FLOAT;
                        if (o != null && false && o instanceof java.math.BigDecimal) //LUC
                            return this.name.XSD_DECIMAL;
                        if (typeof o === 'boolean')
                            return this.name.XSD_BOOLEAN;
                        if (typeof o === 'number')
                            return this.name.XSD_BYTE;
                        if (o != null && (o.constructor != null && o.constructor["__interfaces"] != null && o.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0))
                            return this.name.PROV_QUALIFIED_NAME;
                        if (o != null && o instanceof javax.xml.datatype.XMLGregorianCalendar) {
                            const cal = o;
                            const t = cal.getXMLSchemaType();
                            if (t.getLocalPart() === this.name.XSD_GYEAR.getLocalPart())
                                return this.name.XSD_GYEAR;
                            if (t.getLocalPart() === this.name.XSD_DATETIME.getLocalPart())
                                return this.name.XSD_DATETIME;
                            return this.name.XSD_DATETIME;
                        }
                        return this.name.QUALIFIED_NAME_UNKNOWN_TYPE;
                    }
                }
                model.ValueConverter = ValueConverter;
                ValueConverter["__class"] = "org.openprovenance.prov.model.ValueConverter";
            })(model = prov.model || (prov.model = {}));
        })(prov = openprovenance.prov || (openprovenance.prov = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
