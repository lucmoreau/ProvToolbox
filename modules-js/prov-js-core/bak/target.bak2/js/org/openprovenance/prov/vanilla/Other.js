/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var org;
(function (org) {
    var openprovenance;
    (function (openprovenance) {
        var prov;
        (function (prov) {
            var vanilla;
            (function (vanilla) {
                class Other extends org.openprovenance.prov.vanilla.TypedValue {
                    constructor(elementName, type, value) {
                        if (((elementName != null && (elementName.constructor != null && elementName.constructor["__interfaces"] != null && elementName.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || elementName === null) && ((type != null && (type.constructor != null && type.constructor["__interfaces"] != null && type.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || type === null) && ((value != null) || value === null)) {
                            let __args = arguments;
                            super(type, vanilla.TypedValue.castToStringOrLangStringOrQualifiedName(value, type));
                            if (this.elementName === undefined) {
                                this.elementName = null;
                            }
                            this.elementName = elementName;
                        }
                        else if (elementName === undefined && type === undefined && value === undefined) {
                            let __args = arguments;
                            super();
                            if (this.elementName === undefined) {
                                this.elementName = null;
                            }
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    static PROV_OTHER_KIND_$LI$() { if (Other.PROV_OTHER_KIND == null) {
                        Other.PROV_OTHER_KIND = Attribute.AttributeKind.OTHER;
                    } return Other.PROV_OTHER_KIND; }
                    /**
                     *
                     * @return {*}
                     */
                    getElementName() {
                        return this.elementName;
                    }
                    /**
                     *
                     * @return {org.openprovenance.prov.model.Attribute.AttributeKind}
                     */
                    getKind() {
                        return Other.PROV_OTHER_KIND_$LI$();
                    }
                    /**
                     *
                     * @param {*} elementName
                     */
                    setElementName(elementName) {
                        this.elementName = elementName;
                    }
                    /**
                     *
                     * @return {string}
                     */
                    toNotationString() {
                        return org.openprovenance.prov.model.DOMProcessing.qualifiedNameToString$org_openprovenance_prov_model_QualifiedName(this.getElementName()) + " = " + org.openprovenance.prov.model.ProvUtilities.valueToNotationString$java_lang_Object$org_openprovenance_prov_model_QualifiedName(this.getValue(), this.getType());
                    }
                    equals$java_lang_Object$org_openprovenance_apache_commons_lang_builder_EqualsBuilder(object, equalsBuilder) {
                        if (!(object != null && object instanceof org.openprovenance.prov.vanilla.Other)) {
                            equalsBuilder.appendSuper(false);
                            return;
                        }
                        if (this === object) {
                            return;
                        }
                        const that = object;
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getValue(), that.getValue());
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getType(), that.getType());
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getElementName(), that.getElementName());
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
                        if (!(object != null && (object.constructor != null && object.constructor["__interfaces"] != null && object.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Other") >= 0))) {
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
                        hashCodeBuilder.append$java_lang_Object(this.getElementName());
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
                    toString$org_openprovenance_apache_commons_lang_builder_ToStringBuilder(toStringBuilder) {
                        {
                            let theValue;
                            theValue = this.getValue();
                            toStringBuilder.append$java_lang_String$java_lang_Object("name", this.getElementName());
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
                }
                vanilla.Other = Other;
                Other["__class"] = "org.openprovenance.prov.vanilla.Other";
                Other["__interfaces"] = ["org.openprovenance.prov.model.Other", "org.openprovenance.prov.model.TypedValue", "org.openprovenance.apache.commons.lang.builder.HashCode", "org.openprovenance.apache.commons.lang.builder.Equals", "org.openprovenance.prov.model.Attribute", "org.openprovenance.apache.commons.lang.builder.ToString"];
            })(vanilla = prov.vanilla || (prov.vanilla = {}));
        })(prov = openprovenance.prov || (openprovenance.prov = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
