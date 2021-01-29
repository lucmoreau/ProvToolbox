/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var org;
(function (org) {
    var openprovenance;
    (function (openprovenance) {
        var prov;
        (function (prov) {
            var vanilla;
            (function (vanilla) {
                class TypedValue {
                    constructor(type, value, _dummy, ignore) {
                        if (((type != null && (type.constructor != null && type.constructor["__interfaces"] != null && type.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || type === null) && ((value != null) || value === null) && ((_dummy != null) || _dummy === null) && ((ignore != null) || ignore === null)) {
                            let __args = arguments;
                            if (this.value === undefined) {
                                this.value = null;
                            }
                            if (this.type === undefined) {
                                this.type = null;
                            }
                            if (this.valueAsJavaObject === undefined) {
                                this.valueAsJavaObject = null;
                            }
                            this.type = type;
                            this.setValueFromObject$java_lang_Object(value);
                        }
                        else if (((type != null && (type.constructor != null && type.constructor["__interfaces"] != null && type.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || type === null) && ((value != null) || value === null) && _dummy === undefined && ignore === undefined) {
                            let __args = arguments;
                            if (this.value === undefined) {
                                this.value = null;
                            }
                            if (this.type === undefined) {
                                this.type = null;
                            }
                            if (this.valueAsJavaObject === undefined) {
                                this.valueAsJavaObject = null;
                            }
                            this.type = type;
                            this.value = value;
                        }
                        else if (type === undefined && value === undefined && _dummy === undefined && ignore === undefined) {
                            let __args = arguments;
                            if (this.value === undefined) {
                                this.value = null;
                            }
                            if (this.type === undefined) {
                                this.type = null;
                            }
                            if (this.valueAsJavaObject === undefined) {
                                this.valueAsJavaObject = null;
                            }
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    static QualifiedName_XSD_STRING_$LI$() { if (TypedValue.QualifiedName_XSD_STRING == null) {
                        TypedValue.QualifiedName_XSD_STRING = org.openprovenance.prov.vanilla.ProvFactory.getFactory().getName().XSD_STRING;
                    } return TypedValue.QualifiedName_XSD_STRING; }
                    static QualifiedName_PROV_TYPE_$LI$() { if (TypedValue.QualifiedName_PROV_TYPE == null) {
                        TypedValue.QualifiedName_PROV_TYPE = org.openprovenance.prov.vanilla.ProvFactory.getFactory().getName().PROV_TYPE;
                    } return TypedValue.QualifiedName_PROV_TYPE; }
                    static QualifiedName_PROV_LABEL_$LI$() { if (TypedValue.QualifiedName_PROV_LABEL == null) {
                        TypedValue.QualifiedName_PROV_LABEL = org.openprovenance.prov.vanilla.ProvFactory.getFactory().getName().PROV_LABEL;
                    } return TypedValue.QualifiedName_PROV_LABEL; }
                    static QualifiedName_PROV_VALUE_$LI$() { if (TypedValue.QualifiedName_PROV_VALUE == null) {
                        TypedValue.QualifiedName_PROV_VALUE = org.openprovenance.prov.vanilla.ProvFactory.getFactory().getName().PROV_VALUE;
                    } return TypedValue.QualifiedName_PROV_VALUE; }
                    static QualifiedName_PROV_LOCATION_$LI$() { if (TypedValue.QualifiedName_PROV_LOCATION == null) {
                        TypedValue.QualifiedName_PROV_LOCATION = org.openprovenance.prov.vanilla.ProvFactory.getFactory().getName().PROV_LOCATION;
                    } return TypedValue.QualifiedName_PROV_LOCATION; }
                    static QualifiedName_PROV_ROLE_$LI$() { if (TypedValue.QualifiedName_PROV_ROLE == null) {
                        TypedValue.QualifiedName_PROV_ROLE = org.openprovenance.prov.vanilla.ProvFactory.getFactory().getName().PROV_ROLE;
                    } return TypedValue.QualifiedName_PROV_ROLE; }
                    static QualifiedName_XSD_HEX_BINARY_$LI$() { if (TypedValue.QualifiedName_XSD_HEX_BINARY == null) {
                        TypedValue.QualifiedName_XSD_HEX_BINARY = org.openprovenance.prov.vanilla.ProvFactory.getFactory().getName().XSD_HEX_BINARY;
                    } return TypedValue.QualifiedName_XSD_HEX_BINARY; }
                    static QualifiedName_XSD_BASE64_BINARY_$LI$() { if (TypedValue.QualifiedName_XSD_BASE64_BINARY == null) {
                        TypedValue.QualifiedName_XSD_BASE64_BINARY = org.openprovenance.prov.vanilla.ProvFactory.getFactory().getName().XSD_BASE64_BINARY;
                    } return TypedValue.QualifiedName_XSD_BASE64_BINARY; }
                    static QualifiedName_PROV_QUALIFIEDNAME_$LI$() { if (TypedValue.QualifiedName_PROV_QUALIFIEDNAME == null) {
                        TypedValue.QualifiedName_PROV_QUALIFIEDNAME = org.openprovenance.prov.vanilla.ProvFactory.getFactory().getName().PROV_QUALIFIED_NAME;
                    } return TypedValue.QualifiedName_PROV_QUALIFIEDNAME; }
                    static QualifiedName_PROV_LANG_STRING_$LI$() { if (TypedValue.QualifiedName_PROV_LANG_STRING == null) {
                        TypedValue.QualifiedName_PROV_LANG_STRING = org.openprovenance.prov.vanilla.ProvFactory.getFactory().getName().PROV_LANG_STRING;
                    } return TypedValue.QualifiedName_PROV_LANG_STRING; }
                    static castToStringOrLangStringOrQualifiedName(value, type) {
                        return (value != null && (value.constructor != null && value.constructor["__interfaces"] != null && value.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.LangString") >= 0)) ? value : ((value != null && (value.constructor != null && value.constructor["__interfaces"] != null && value.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) ? value : (((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(TypedValue.QualifiedName_XSD_STRING_$LI$(), type) ? new org.openprovenance.prov.vanilla.LangString(value.toString(), null) : value.toString()));
                    }
                    convertValueToObject(vconv) {
                        if (this.valueAsJavaObject == null) {
                            if (typeof this.value === 'string') {
                                this.valueAsJavaObject = vconv.convertToJava(this.getType(), this.value);
                            }
                            else {
                                this.valueAsJavaObject = this.value;
                            }
                        }
                        return this.valueAsJavaObject;
                    }
                    /**
                     *
                     * @return {*}
                     */
                    getType() {
                        return this.type;
                    }
                    /**
                     *
                     * @return {*}
                     */
                    getValue() {
                        return this.value;
                    }
                    /**
                     *
                     * @return {*}
                     */
                    getConvertedValue() {
                        return this.valueAsJavaObject;
                    }
                    getQualifiedName(kind) {
                        switch ((kind)) {
                            case org.openprovenance.prov.model.Attribute.AttributeKind.PROV_TYPE:
                                return TypedValue.QualifiedName_PROV_TYPE_$LI$();
                            case org.openprovenance.prov.model.Attribute.AttributeKind.PROV_LABEL:
                                return TypedValue.QualifiedName_PROV_LABEL_$LI$();
                            case org.openprovenance.prov.model.Attribute.AttributeKind.PROV_VALUE:
                                return TypedValue.QualifiedName_PROV_VALUE_$LI$();
                            case org.openprovenance.prov.model.Attribute.AttributeKind.PROV_LOCATION:
                                return TypedValue.QualifiedName_PROV_LOCATION_$LI$();
                            case org.openprovenance.prov.model.Attribute.AttributeKind.PROV_ROLE:
                                return TypedValue.QualifiedName_PROV_ROLE_$LI$();
                            case org.openprovenance.prov.model.Attribute.AttributeKind.OTHER:
                            default:
                                return null;
                        }
                    }
                    getAttributeKind(q) {
                        if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(q, TypedValue.QualifiedName_PROV_TYPE_$LI$()))
                            return org.openprovenance.prov.model.Attribute.AttributeKind.PROV_TYPE;
                        if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(q, TypedValue.QualifiedName_PROV_LABEL_$LI$()))
                            return org.openprovenance.prov.model.Attribute.AttributeKind.PROV_LABEL;
                        if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(q, TypedValue.QualifiedName_PROV_VALUE_$LI$()))
                            return org.openprovenance.prov.model.Attribute.AttributeKind.PROV_VALUE;
                        if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(q, TypedValue.QualifiedName_PROV_LOCATION_$LI$()))
                            return org.openprovenance.prov.model.Attribute.AttributeKind.PROV_LOCATION;
                        if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(q, TypedValue.QualifiedName_PROV_ROLE_$LI$()))
                            return org.openprovenance.prov.model.Attribute.AttributeKind.PROV_ROLE;
                        return org.openprovenance.prov.model.Attribute.AttributeKind.OTHER;
                    }
                    /**
                     *
                     * @param {*} value
                     */
                    setType(value) {
                        this.type = value;
                    }
                    setValue$org_openprovenance_prov_model_LangString(value) {
                        this.value = value;
                    }
                    /**
                     *
                     * @param {*} value
                     */
                    setValue(value) {
                        if (((value != null && (value.constructor != null && value.constructor["__interfaces"] != null && value.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.LangString") >= 0)) || value === null)) {
                            return this.setValue$org_openprovenance_prov_model_LangString(value);
                        }
                        else if (((value != null && (value.constructor != null && value.constructor["__interfaces"] != null && value.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || value === null)) {
                            return this.setValue$org_openprovenance_prov_model_QualifiedName(value);
                        }
                        else if (((typeof value === 'string') || value === null)) {
                            return this.setValue$java_lang_String(value);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    setValue$org_openprovenance_prov_model_QualifiedName(value) {
                        this.value = value;
                    }
                    setValue$java_lang_String(value) {
                        if ((this.type != null) && /* equals */ ((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(TypedValue.QualifiedName_PROV_QUALIFIEDNAME_$LI$(), this.type)) {
                            console.info("HACK: deserializing value " + value + "type " + this.type);
                            const ns = null;
                            this.setValue$org_openprovenance_prov_model_QualifiedName(ns.stringToQualifiedName$java_lang_String$org_openprovenance_prov_model_ProvFactory$boolean(value, org.openprovenance.prov.vanilla.ProvFactory.getFactory(), false));
                            throw Object.defineProperty(new Error("we should not be here with " + value), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.UnsupportedOperationException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                        }
                        else {
                            this.value = value;
                        }
                    }
                    setValueFromObject$java_lang_Object(anObject) {
                        if ((anObject != null) && (this.value == null)) {
                            if (anObject != null && (anObject.constructor != null && anObject.constructor["__interfaces"] != null && anObject.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) {
                                this.setValue$org_openprovenance_prov_model_QualifiedName(anObject);
                            }
                            else if (anObject != null && (anObject.constructor != null && anObject.constructor["__interfaces"] != null && anObject.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.LangString") >= 0)) {
                                this.setValue$org_openprovenance_prov_model_LangString(anObject);
                            }
                            else if (anObject != null && anObject instanceof Array && (anObject.length == 0 || anObject[0] == null || typeof anObject[0] === 'number')) {
                                this.setValueFromObject$byte_A(anObject);
                            }
                            else if (anObject != null && (anObject.constructor != null && anObject.constructor["__interfaces"] != null && anObject.constructor["__interfaces"].indexOf("org.w3c.dom.Node") >= 0)) {
                                this.setValueFromObject$org_w3c_dom_Node(anObject);
                            }
                            else {
                                this.setValue$java_lang_String(anObject.toString());
                            }
                        }
                        this.valueAsJavaObject = anObject;
                    }
                    setValueFromObject$byte_A(bytes) {
                        if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(this.type, TypedValue.QualifiedName_XSD_BASE64_BINARY_$LI$())) {
                            this.setValue$java_lang_String(org.openprovenance.prov.vanilla.ProvFactory.getFactory().base64Encoding(bytes));
                        }
                        else if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(this.type, TypedValue.QualifiedName_XSD_HEX_BINARY_$LI$())) {
                            this.setValue$java_lang_String(org.openprovenance.prov.vanilla.ProvFactory.getFactory().hexEncoding(bytes));
                        }
                    }
                    /**
                     * Converts a byte array in base64 or hexadecimal according to specified type.
                     *
                     * @param {byte[]} bytes array of bytes to convert
                     * @private
                     */
                    setValueFromObject(bytes) {
                        if (((bytes != null && bytes instanceof Array && (bytes.length == 0 || bytes[0] == null || (typeof bytes[0] === 'number'))) || bytes === null)) {
                            return this.setValueFromObject$byte_A(bytes);
                        }
                        else if (((bytes != null && (bytes.constructor != null && bytes.constructor["__interfaces"] != null && bytes.constructor["__interfaces"].indexOf("org.w3c.dom.Node") >= 0)) || bytes === null)) {
                            return this.setValueFromObject$org_w3c_dom_Node(bytes);
                        }
                        else if (((bytes != null) || bytes === null)) {
                            return this.setValueFromObject$java_lang_Object(bytes);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    /*private*/ setValueFromObject$org_w3c_dom_Node(n) {
                        org.openprovenance.prov.model.DOMProcessing.trimNode$org_w3c_dom_Node(n);
                        try {
                            this.setValue$java_lang_String(org.openprovenance.prov.model.DOMProcessing.writeToString(n));
                        }
                        catch (__e) {
                            if (__e != null && __e instanceof javax.xml.transform.TransformerConfigurationException) {
                                const e = __e;
                                this.setValue$java_lang_String(n.toString());
                            }
                            if (__e != null && __e instanceof javax.xml.transform.TransformerException) {
                                const e = __e;
                                this.setValue$java_lang_String(n.toString());
                            }
                        }
                    }
                    equals$java_lang_Object$org_openprovenance_apache_commons_lang_builder_EqualsBuilder(object, equalsBuilder) {
                        if (!(object != null && object instanceof org.openprovenance.prov.vanilla.TypedValue)) {
                            equalsBuilder.appendSuper(false);
                            return;
                        }
                        if (this === object) {
                            return;
                        }
                        const that = object;
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getValue(), that.getValue());
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getType(), that.getType());
                    }
                    equals(object, equalsBuilder) {
                        if (((object != null) || object === null) && ((equalsBuilder != null && equalsBuilder instanceof org.openprovenance.apache.commons.lang.builder.EqualsBuilder) || equalsBuilder === null)) {
                            return this.equals$java_lang_Object$org_openprovenance_apache_commons_lang_builder_EqualsBuilder(object, equalsBuilder);
                        }
                        else if (((object != null) || object === null) && equalsBuilder === undefined) {
                            return this.equals$java_lang_Object(object);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    equals$java_lang_Object(object) {
                        if (!(object != null && object instanceof org.openprovenance.prov.vanilla.TypedValue)) {
                            return false;
                        }
                        if (this === object) {
                            return true;
                        }
                        const equalsBuilder = new org.openprovenance.apache.commons.lang.builder.EqualsBuilder();
                        this.equals$java_lang_Object$org_openprovenance_apache_commons_lang_builder_EqualsBuilder(object, equalsBuilder);
                        return equalsBuilder.isEquals();
                    }
                    hashCode$org_openprovenance_apache_commons_lang_builder_HashCodeBuilder(hashCodeBuilder) {
                        hashCodeBuilder.append$java_lang_Object(this.getValue());
                        hashCodeBuilder.append$java_lang_Object(this.getType());
                    }
                    hashCode(hashCodeBuilder) {
                        if (((hashCodeBuilder != null && hashCodeBuilder instanceof org.openprovenance.apache.commons.lang.builder.HashCodeBuilder) || hashCodeBuilder === null)) {
                            return this.hashCode$org_openprovenance_apache_commons_lang_builder_HashCodeBuilder(hashCodeBuilder);
                        }
                        else if (hashCodeBuilder === undefined) {
                            return this.hashCode$();
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    hashCode$() {
                        const hashCodeBuilder = new org.openprovenance.apache.commons.lang.builder.HashCodeBuilder();
                        this.hashCode$org_openprovenance_apache_commons_lang_builder_HashCodeBuilder(hashCodeBuilder);
                        return hashCodeBuilder.toHashCode();
                    }
                    toString$org_openprovenance_apache_commons_lang_builder_ToStringBuilder(toStringBuilder) {
                        {
                            let theValue;
                            theValue = this.getValue();
                            toStringBuilder.append$java_lang_String$java_lang_Object("value", theValue);
                            toStringBuilder.append$java_lang_String$java_lang_Object("DEBUG_type1", theValue.constructor);
                        }
                        ;
                        {
                            let theType;
                            theType = this.getType();
                            toStringBuilder.append$java_lang_String$java_lang_Object("type", theType);
                        }
                        ;
                    }
                    toString(toStringBuilder) {
                        if (((toStringBuilder != null && toStringBuilder instanceof org.openprovenance.apache.commons.lang.builder.ToStringBuilder) || toStringBuilder === null)) {
                            return this.toString$org_openprovenance_apache_commons_lang_builder_ToStringBuilder(toStringBuilder);
                        }
                        else if (toStringBuilder === undefined) {
                            return this.toString$();
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    toString$() {
                        const toStringBuilder = new org.openprovenance.apache.commons.lang.builder.ToStringBuilder(this);
                        this.toString$org_openprovenance_apache_commons_lang_builder_ToStringBuilder(toStringBuilder);
                        return toStringBuilder.toString();
                    }
                    getLanguageString() {
                        if (this.getValue() != null && (this.getValue().constructor != null && this.getValue().constructor["__interfaces"] != null && this.getValue().constructor["__interfaces"].indexOf("org.openprovenance.prov.model.LangString") >= 0)) {
                            return this.getValue().getLang();
                        }
                        return null;
                    }
                    getTypeString() {
                        if (((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(TypedValue.QualifiedName_XSD_STRING_$LI$(), this.type) || ((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(TypedValue.QualifiedName_PROV_LANG_STRING_$LI$(), this.type)) {
                            return null;
                        }
                        return TypedValue.prnt(this.type);
                    }
                    static prnt(qn) {
                        return qn.getPrefix() + ":" + qn.getLocalPart();
                    }
                }
                vanilla.TypedValue = TypedValue;
                TypedValue["__class"] = "org.openprovenance.prov.vanilla.TypedValue";
                TypedValue["__interfaces"] = ["org.openprovenance.prov.model.TypedValue", "org.openprovenance.apache.commons.lang.builder.HashCode", "org.openprovenance.apache.commons.lang.builder.Equals", "org.openprovenance.apache.commons.lang.builder.ToString"];
            })(vanilla = prov.vanilla || (prov.vanilla = {}));
        })(prov = openprovenance.prov || (openprovenance.prov = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
