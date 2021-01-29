/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var org;
(function (org) {
    var openprovenance;
    (function (openprovenance) {
        var prov;
        (function (prov) {
            var vanilla;
            (function (vanilla) {
                class WasInformedBy {
                    constructor(id, informed, informant, attributes) {
                        if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((informed != null && (informed.constructor != null && informed.constructor["__interfaces"] != null && informed.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || informed === null) && ((informant != null && (informant.constructor != null && informant.constructor["__interfaces"] != null && informant.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || informant === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                            let __args = arguments;
                            if (this.informed === undefined) {
                                this.informed = null;
                            }
                            if (this.informant === undefined) {
                                this.informant = null;
                            }
                            this.id = null;
                            this.labels = ([]);
                            this.other = ([]);
                            this.type = ([]);
                            this.setId(id);
                            this.informed = informed;
                            this.informant = informant;
                            WasInformedBy.u_$LI$().populateAttributes(attributes, this.labels, ([]), this.type, ([]), this.other, null);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((informed != null && (informed instanceof Array)) || informed === null) && informant === undefined && attributes === undefined) {
                            let __args = arguments;
                            let attributes = __args[1];
                            if (this.informed === undefined) {
                                this.informed = null;
                            }
                            if (this.informant === undefined) {
                                this.informant = null;
                            }
                            this.id = null;
                            this.labels = ([]);
                            this.other = ([]);
                            this.type = ([]);
                            this.setId(id);
                            WasInformedBy.u_$LI$().populateAttributes(attributes, this.labels, ([]), this.type, ([]), this.other, null);
                        }
                        else if (id === undefined && informed === undefined && informant === undefined && attributes === undefined) {
                            let __args = arguments;
                            if (this.informed === undefined) {
                                this.informed = null;
                            }
                            if (this.informant === undefined) {
                                this.informant = null;
                            }
                            this.id = null;
                            this.labels = ([]);
                            this.other = ([]);
                            this.type = ([]);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    static u_$LI$() { if (WasInformedBy.u == null) {
                        WasInformedBy.u = new org.openprovenance.prov.vanilla.ProvUtilities();
                    } return WasInformedBy.u; }
                    /**
                     *
                     * @param {*} informed
                     */
                    setInformed(informed) {
                        this.informed = informed;
                    }
                    /**
                     *
                     * @param {*} informant
                     */
                    setInformant(informant) {
                        this.informant = informant;
                    }
                    /**
                     *
                     * @return {*}
                     */
                    getInformant() {
                        return this.informant;
                    }
                    /**
                     *
                     * @return {*}
                     */
                    getInformed() {
                        return this.informed;
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
                        return StatementOrBundle.Kind.PROV_COMMUNICATION;
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
                        if (!(object != null && object instanceof org.openprovenance.prov.vanilla.WasInformedBy)) {
                            equalsBuilder.appendSuper(false);
                            return;
                        }
                        if (this === object) {
                            return;
                        }
                        const that = object;
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getId(), that.getId());
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getInformed(), that.getInformed());
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getInformant(), that.getInformant());
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
                    equals$java_lang_Object(object) {
                        if (!(object != null && object instanceof org.openprovenance.prov.vanilla.WasInformedBy)) {
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
                        hashCodeBuilder.append$java_lang_Object(this.getId());
                        hashCodeBuilder.append$java_lang_Object(this.getInformed());
                        hashCodeBuilder.append$java_lang_Object(this.getInformant());
                        hashCodeBuilder.append$java_lang_Object(this.getIndexedAttributes());
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
                            theInformed = this.getInformed();
                            toStringBuilder.append$java_lang_String$java_lang_Object("informed", theInformed);
                        }
                        ;
                        {
                            let theInformant;
                            theInformant = this.getInformant();
                            toStringBuilder.append$java_lang_String$java_lang_Object("informant", theInformant);
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
                        WasInformedBy.u_$LI$().distribute(qn, attributes, this.getLabel(), values, locations, this.getType(), java.util.Collections.EMPTY_LIST, this.getOther());
                    }
                    /**
                     *
                     * @return {*}
                     */
                    getIndexedAttributes() {
                        return WasInformedBy.u_$LI$().split$java_util_Collection(this.getAttributes());
                    }
                }
                vanilla.WasInformedBy = WasInformedBy;
                WasInformedBy["__class"] = "org.openprovenance.prov.vanilla.WasInformedBy";
                WasInformedBy["__interfaces"] = ["org.openprovenance.prov.model.HasOther", "org.openprovenance.prov.model.QualifiedRelation", "org.openprovenance.prov.model.HasType", "org.openprovenance.prov.model.WasInformedBy", "org.openprovenance.prov.model.StatementOrBundle", "org.openprovenance.apache.commons.lang.builder.ToString", "org.openprovenance.prov.model.Statement", "org.openprovenance.prov.vanilla.HasAttributes", "org.openprovenance.apache.commons.lang.builder.HashCode", "org.openprovenance.prov.model.Influence", "org.openprovenance.prov.model.HasLabel", "org.openprovenance.apache.commons.lang.builder.Equals", "org.openprovenance.prov.model.Identifiable", "org.openprovenance.prov.model.Relation"];
            })(vanilla = prov.vanilla || (prov.vanilla = {}));
        })(prov = openprovenance.prov || (openprovenance.prov = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
