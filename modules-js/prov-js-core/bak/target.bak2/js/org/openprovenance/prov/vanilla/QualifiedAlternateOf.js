/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var org;
(function (org) {
    var openprovenance;
    (function (openprovenance) {
        var prov;
        (function (prov) {
            var vanilla;
            (function (vanilla) {
                class QualifiedAlternateOf {
                    constructor(id, alternate1, alternate2, attributes) {
                        if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((alternate1 != null && (alternate1.constructor != null && alternate1.constructor["__interfaces"] != null && alternate1.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || alternate1 === null) && ((alternate2 != null && (alternate2.constructor != null && alternate2.constructor["__interfaces"] != null && alternate2.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || alternate2 === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                            let __args = arguments;
                            if (this.alternate1 === undefined) {
                                this.alternate1 = null;
                            }
                            if (this.alternate2 === undefined) {
                                this.alternate2 = null;
                            }
                            this.id = null;
                            this.labels = ([]);
                            this.other = ([]);
                            this.type = ([]);
                            this.u = new org.openprovenance.prov.vanilla.ProvUtilities();
                            this.setId(id);
                            this.alternate1 = alternate1;
                            this.alternate2 = alternate2;
                            this.u.populateAttributes(attributes, this.labels, ([]), this.type, ([]), this.other, null);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((alternate1 != null && (alternate1 instanceof Array)) || alternate1 === null) && alternate2 === undefined && attributes === undefined) {
                            let __args = arguments;
                            let attributes = __args[1];
                            if (this.alternate1 === undefined) {
                                this.alternate1 = null;
                            }
                            if (this.alternate2 === undefined) {
                                this.alternate2 = null;
                            }
                            this.id = null;
                            this.labels = ([]);
                            this.other = ([]);
                            this.type = ([]);
                            this.u = new org.openprovenance.prov.vanilla.ProvUtilities();
                            this.setId(id);
                            this.u.populateAttributes(attributes, this.labels, ([]), this.type, ([]), this.other, null);
                        }
                        else if (id === undefined && alternate1 === undefined && alternate2 === undefined && attributes === undefined) {
                            let __args = arguments;
                            if (this.alternate1 === undefined) {
                                this.alternate1 = null;
                            }
                            if (this.alternate2 === undefined) {
                                this.alternate2 = null;
                            }
                            this.id = null;
                            this.labels = ([]);
                            this.other = ([]);
                            this.type = ([]);
                            this.u = new org.openprovenance.prov.vanilla.ProvUtilities();
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    /**
                     *
                     * @return {boolean}
                     */
                    isUnqualified() {
                        return /* isEmpty */ (this.id == null) && /* isEmpty */ (this.other.length == 0) && /* isEmpty */ (this.labels.length == 0) && /* isEmpty */ (this.type.length == 0);
                    }
                    /**
                     *
                     * @param {*} informed
                     */
                    setAlternate1(informed) {
                        this.alternate1 = informed;
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
                     * @return {*}
                     */
                    getId() {
                        return /* orElse */ ((v, v2) => v == null ? v2 : v)(this.id, null);
                    }
                    /**
                     *
                     * @return {org.openprovenance.prov.model.StatementOrBundle.Kind}
                     */
                    getKind() {
                        return StatementOrBundle.Kind.PROV_ALTERNATE;
                    }
                    /**
                     *
                     * @param {*} value
                     */
                    setId(value) {
                        this.id = value;
                    }
                    /**
                     *
                     * @return {*[]}
                     */
                    getLabel() {
                        return this.labels;
                    }
                    /**
                     *
                     * @return {*[]}
                     */
                    getType() {
                        return this.type;
                    }
                    /**
                     *
                     * @return {*[]}
                     */
                    getOther() {
                        return this.other;
                    }
                    equals$java_lang_Object$org_openprovenance_apache_commons_lang_builder_EqualsBuilder(object, equalsBuilder) {
                        if (!(object != null && object instanceof org.openprovenance.prov.vanilla.QualifiedAlternateOf)) {
                            equalsBuilder.appendSuper(false);
                            return;
                        }
                        if (this === object) {
                            return;
                        }
                        const that = object;
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getId(), that.getId());
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getAlternate1(), that.getAlternate1());
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getAlternate2(), that.getAlternate2());
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getIndexedAttributes(), that.getIndexedAttributes());
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
                        if (!(object != null && object instanceof org.openprovenance.prov.vanilla.QualifiedAlternateOf)) {
                            if (object != null && object instanceof org.openprovenance.prov.vanilla.AlternateOf) {
                                if (this.isUnqualified()) {
                                    const alt = object;
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
                        const unqualified = this.isUnqualified();
                        if (!unqualified) {
                            hashCodeBuilder.append$java_lang_Object(this.getId());
                        }
                        hashCodeBuilder.append$java_lang_Object(this.getAlternate1());
                        hashCodeBuilder.append$java_lang_Object(this.getAlternate2());
                        if (!unqualified) {
                            hashCodeBuilder.append$java_lang_Object(this.getIndexedAttributes());
                        }
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
                            let theId;
                            theId = this.getId();
                            toStringBuilder.append$java_lang_String$java_lang_Object("id", theId);
                        }
                        ;
                        {
                            let theInformed;
                            theInformed = this.getAlternate1();
                            toStringBuilder.append$java_lang_String$java_lang_Object("alternate1", theInformed);
                        }
                        ;
                        {
                            let theInformant;
                            theInformant = this.getAlternate2();
                            toStringBuilder.append$java_lang_String$java_lang_Object("alternate2", theInformant);
                        }
                        ;
                        {
                            let theAttributes;
                            theAttributes = this.getIndexedAttributes();
                            toStringBuilder.append$java_lang_String$java_lang_Object("attributes", theAttributes);
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
                    /**
                     *
                     * @return {*[]}
                     */
                    getAttributes() {
                        const result = ([]);
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(result, this.getLabel().stream().map((s) => new org.openprovenance.prov.vanilla.Label(org.openprovenance.prov.vanilla.ActedOnBehalfOf.QUALIFIED_NAME_XSD_STRING_$LI$(), s)).collect(java.util.stream.Collectors.toList()));
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(result, this.getType());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(result, this.getOther().stream().map((o) => o).collect(java.util.stream.Collectors.toList()));
                        return result;
                    }
                    setIndexedAttributes(qn, attributes) {
                        const values = ([]);
                        const locations = ([]);
                        this.u.distribute(qn, attributes, this.getLabel(), values, locations, this.getType(), java.util.Collections.EMPTY_LIST, this.getOther());
                    }
                    /**
                     *
                     * @return {*}
                     */
                    getIndexedAttributes() {
                        return this.u.split$java_util_Collection(this.getAttributes());
                    }
                }
                vanilla.QualifiedAlternateOf = QualifiedAlternateOf;
                QualifiedAlternateOf["__class"] = "org.openprovenance.prov.vanilla.QualifiedAlternateOf";
                QualifiedAlternateOf["__interfaces"] = ["org.openprovenance.prov.model.QualifiedRelation", "org.openprovenance.prov.model.HasOther", "org.openprovenance.prov.model.UnqualifiedRelation", "org.openprovenance.prov.model.HasType", "org.openprovenance.prov.model.StatementOrBundle", "org.openprovenance.apache.commons.lang.builder.ToString", "org.openprovenance.prov.model.Statement", "org.openprovenance.prov.model.extension.QualifiedAlternateOf", "org.openprovenance.prov.vanilla.HasAttributes", "org.openprovenance.apache.commons.lang.builder.HashCode", "org.openprovenance.prov.model.AlternateOf", "org.openprovenance.prov.model.HasLabel", "org.openprovenance.apache.commons.lang.builder.Equals", "org.openprovenance.prov.model.Relation", "org.openprovenance.prov.model.Identifiable"];
            })(vanilla = prov.vanilla || (prov.vanilla = {}));
        })(prov = openprovenance.prov || (openprovenance.prov = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
