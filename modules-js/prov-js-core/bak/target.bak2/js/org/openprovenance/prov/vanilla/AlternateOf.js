/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var org;
(function (org) {
    var openprovenance;
    (function (openprovenance) {
        var prov;
        (function (prov) {
            var vanilla;
            (function (vanilla) {
                class AlternateOf {
                    constructor(alternate1, alternate2) {
                        if (((alternate1 != null && (alternate1.constructor != null && alternate1.constructor["__interfaces"] != null && alternate1.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || alternate1 === null) && ((alternate2 != null && (alternate2.constructor != null && alternate2.constructor["__interfaces"] != null && alternate2.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || alternate2 === null)) {
                            let __args = arguments;
                            if (this.alternate1 === undefined) {
                                this.alternate1 = null;
                            }
                            if (this.alternate2 === undefined) {
                                this.alternate2 = null;
                            }
                            this.alternate1 = alternate1;
                            this.alternate2 = alternate2;
                        }
                        else if (alternate1 === undefined && alternate2 === undefined) {
                            let __args = arguments;
                            if (this.alternate1 === undefined) {
                                this.alternate1 = null;
                            }
                            if (this.alternate2 === undefined) {
                                this.alternate2 = null;
                            }
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    /**
                     *
                     * @param {*} alternate1
                     */
                    setAlternate1(alternate1) {
                        this.alternate1 = alternate1;
                    }
                    /**
                     *
                     * @param {*} alternate2
                     */
                    setAlternate2(alternate2) {
                        this.alternate2 = alternate2;
                    }
                    /**
                     *
                     * @return {*}
                     */
                    getAlternate2() {
                        return this.alternate2;
                    }
                    /**
                     *
                     * @return {*}
                     */
                    getAlternate1() {
                        return this.alternate1;
                    }
                    /**
                     *
                     * @return {org.openprovenance.prov.model.StatementOrBundle.Kind}
                     */
                    getKind() {
                        return StatementOrBundle.Kind.PROV_ALTERNATE;
                    }
                    equals$java_lang_Object$org_openprovenance_apache_commons_lang_builder_EqualsBuilder(object, equalsBuilder) {
                        if (!(object != null && object instanceof org.openprovenance.prov.vanilla.AlternateOf)) {
                            equalsBuilder.appendSuper(false);
                            return;
                        }
                        if (this === object) {
                            return;
                        }
                        const that = object;
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getAlternate1(), that.getAlternate1());
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getAlternate2(), that.getAlternate2());
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
                    equals2(object, equalsBuilder) {
                        if (!(object != null && object instanceof org.openprovenance.prov.vanilla.QualifiedAlternateOf)) {
                            equalsBuilder.appendSuper(false);
                            return;
                        }
                        if (this === object) {
                            return;
                        }
                        const that = object;
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getAlternate1(), that.getAlternate1());
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getAlternate2(), that.getAlternate2());
                    }
                    equals$java_lang_Object(object) {
                        if (!(object != null && object instanceof org.openprovenance.prov.vanilla.AlternateOf)) {
                            if (object != null && object instanceof org.openprovenance.prov.vanilla.QualifiedAlternateOf) {
                                const qalt = object;
                                if (qalt.isUnqualified()) {
                                    const equalsBuilder2 = new org.openprovenance.apache.commons.lang.builder.EqualsBuilder();
                                    this.equals2(object, equalsBuilder2);
                                    return equalsBuilder2.isEquals();
                                }
                                return false;
                            }
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
                        hashCodeBuilder.append$java_lang_Object(this.getAlternate1());
                        hashCodeBuilder.append$java_lang_Object(this.getAlternate2());
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
                            let theEntity;
                            theEntity = this.getAlternate2();
                            toStringBuilder.append$java_lang_String$java_lang_Object("alternate2", theEntity);
                        }
                        ;
                        {
                            let theAgent;
                            theAgent = this.getAlternate1();
                            toStringBuilder.append$java_lang_String$java_lang_Object("alternate1", theAgent);
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
                vanilla.AlternateOf = AlternateOf;
                AlternateOf["__class"] = "org.openprovenance.prov.vanilla.AlternateOf";
                AlternateOf["__interfaces"] = ["org.openprovenance.prov.model.UnqualifiedRelation", "org.openprovenance.apache.commons.lang.builder.HashCode", "org.openprovenance.prov.model.AlternateOf", "org.openprovenance.apache.commons.lang.builder.Equals", "org.openprovenance.prov.model.Relation", "org.openprovenance.prov.model.StatementOrBundle", "org.openprovenance.apache.commons.lang.builder.ToString", "org.openprovenance.prov.model.Statement"];
            })(vanilla = prov.vanilla || (prov.vanilla = {}));
        })(prov = openprovenance.prov || (openprovenance.prov = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
