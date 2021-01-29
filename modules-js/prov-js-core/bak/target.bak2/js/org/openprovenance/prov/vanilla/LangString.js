/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var org;
(function (org) {
    var openprovenance;
    (function (openprovenance) {
        var prov;
        (function (prov) {
            var vanilla;
            (function (vanilla) {
                class LangString {
                    constructor(value, lang) {
                        if (((typeof value === 'string') || value === null) && ((typeof lang === 'string') || lang === null)) {
                            let __args = arguments;
                            if (this.lang === undefined) {
                                this.lang = null;
                            }
                            if (this.value === undefined) {
                                this.value = null;
                            }
                            this.value = value;
                            this.lang = lang;
                        }
                        else if (((typeof value === 'string') || value === null) && lang === undefined) {
                            let __args = arguments;
                            if (this.lang === undefined) {
                                this.lang = null;
                            }
                            if (this.value === undefined) {
                                this.value = null;
                            }
                            this.value = value;
                            this.lang = null;
                        }
                        else if (value === undefined && lang === undefined) {
                            let __args = arguments;
                            if (this.lang === undefined) {
                                this.lang = null;
                            }
                            if (this.value === undefined) {
                                this.value = null;
                            }
                            this.lang = null;
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    /**
                     *
                     * @return {string}
                     */
                    getValue() {
                        return this.value;
                    }
                    /**
                     *
                     * @param {string} value
                     */
                    setValue(value) {
                        this.value = value;
                    }
                    /**
                     *
                     * @return {string}
                     */
                    getLang() {
                        return /* orElse */ ((v, v2) => v == null ? v2 : v)(this.lang, null);
                    }
                    /**
                     *
                     * @param {string} value
                     */
                    setLang(value) {
                        this.lang = value;
                    }
                    equals$java_lang_Object$org_openprovenance_apache_commons_lang_builder_EqualsBuilder(object, equalsBuilder) {
                        if (!(object != null && object instanceof org.openprovenance.prov.vanilla.LangString)) {
                            equalsBuilder.appendSuper(false);
                            return;
                        }
                        if (this === object) {
                            return;
                        }
                        const that = object;
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getValue(), that.getValue());
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getLang(), that.getLang());
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
                        if (!(object != null && object instanceof org.openprovenance.prov.vanilla.LangString)) {
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
                        hashCodeBuilder.append$java_lang_Object(this.getLang());
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
                        }
                        ;
                        {
                            let theLang;
                            theLang = this.getLang();
                            toStringBuilder.append$java_lang_String$java_lang_Object("lang", theLang);
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
                vanilla.LangString = LangString;
                LangString["__class"] = "org.openprovenance.prov.vanilla.LangString";
                LangString["__interfaces"] = ["org.openprovenance.apache.commons.lang.builder.HashCode", "org.openprovenance.apache.commons.lang.builder.Equals", "org.openprovenance.apache.commons.lang.builder.ToString", "org.openprovenance.prov.model.LangString"];
            })(vanilla = prov.vanilla || (prov.vanilla = {}));
        })(prov = openprovenance.prov || (openprovenance.prov = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
