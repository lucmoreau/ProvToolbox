/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var org;
(function (org) {
    var openprovenance;
    (function (openprovenance) {
        var prov;
        (function (prov) {
            var vanilla;
            (function (vanilla) {
                class ActedOnBehalfOf {
                    constructor(id, delegate, responsible, activity, attributes) {
                        if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((delegate != null && (delegate.constructor != null && delegate.constructor["__interfaces"] != null && delegate.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || delegate === null) && ((responsible != null && (responsible.constructor != null && responsible.constructor["__interfaces"] != null && responsible.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || responsible === null) && ((activity != null && (activity.constructor != null && activity.constructor["__interfaces"] != null && activity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || activity === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                            let __args = arguments;
                            if (this.delegate === undefined) {
                                this.delegate = null;
                            }
                            if (this.responsible === undefined) {
                                this.responsible = null;
                            }
                            this.id = null;
                            this.labels = ([]);
                            this.other = ([]);
                            this.type = ([]);
                            this.activity = null;
                            this.setId(id);
                            this.delegate = delegate;
                            this.responsible = responsible;
                            this.activity = activity;
                            ActedOnBehalfOf.u_$LI$().populateAttributes(attributes, this.labels, java.util.Collections.EMPTY_LIST, this.type, java.util.Collections.EMPTY_LIST, this.other, null);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((delegate != null && (delegate instanceof Array)) || delegate === null) && responsible === undefined && activity === undefined && attributes === undefined) {
                            let __args = arguments;
                            let attributes = __args[1];
                            if (this.delegate === undefined) {
                                this.delegate = null;
                            }
                            if (this.responsible === undefined) {
                                this.responsible = null;
                            }
                            this.id = null;
                            this.labels = ([]);
                            this.other = ([]);
                            this.type = ([]);
                            this.activity = null;
                            this.setId(id);
                            ActedOnBehalfOf.u_$LI$().populateAttributes(attributes, this.labels, java.util.Collections.EMPTY_LIST, this.type, java.util.Collections.EMPTY_LIST, this.other, null);
                        }
                        else if (id === undefined && delegate === undefined && responsible === undefined && activity === undefined && attributes === undefined) {
                            let __args = arguments;
                            if (this.delegate === undefined) {
                                this.delegate = null;
                            }
                            if (this.responsible === undefined) {
                                this.responsible = null;
                            }
                            this.id = null;
                            this.labels = ([]);
                            this.other = ([]);
                            this.type = ([]);
                            this.activity = null;
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    static QUALIFIED_NAME_XSD_STRING_$LI$() { if (ActedOnBehalfOf.QUALIFIED_NAME_XSD_STRING == null) {
                        ActedOnBehalfOf.QUALIFIED_NAME_XSD_STRING = org.openprovenance.prov.vanilla.ProvFactory.getFactory().getName().XSD_STRING;
                    } return ActedOnBehalfOf.QUALIFIED_NAME_XSD_STRING; }
                    static u_$LI$() { if (ActedOnBehalfOf.u == null) {
                        ActedOnBehalfOf.u = new org.openprovenance.prov.vanilla.ProvUtilities();
                    } return ActedOnBehalfOf.u; }
                    /**
                     *
                     * @param {*} aid
                     */
                    setDelegate(aid) {
                        this.delegate = aid;
                    }
                    /**
                     *
                     * @param {*} aid
                     */
                    setResponsible(aid) {
                        this.responsible = aid;
                    }
                    /**
                     *
                     * @param {*} eid
                     */
                    setActivity(eid) {
                        this.activity = eid;
                    }
                    /**
                     *
                     * @return {*}
                     */
                    getDelegate() {
                        return this.delegate;
                    }
                    /**
                     *
                     * @return {*}
                     */
                    getResponsible() {
                        return this.responsible;
                    }
                    /**
                     *
                     * @return {*}
                     */
                    getActivity() {
                        return /* orElse */ ((v, v2) => v == null ? v2 : v)(this.activity, null);
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
                        return StatementOrBundle.Kind.PROV_DELEGATION;
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
                        if (!(object != null && object instanceof org.openprovenance.prov.vanilla.ActedOnBehalfOf)) {
                            equalsBuilder.appendSuper(false);
                            return;
                        }
                        if (this === object) {
                            return;
                        }
                        const that = object;
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getId(), that.getId());
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getActivity(), that.getActivity());
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getDelegate(), that.getDelegate());
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getResponsible(), that.getResponsible());
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
                        if (!(object != null && object instanceof org.openprovenance.prov.vanilla.ActedOnBehalfOf)) {
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
                        hashCodeBuilder.append$java_lang_Object(this.getResponsible());
                        hashCodeBuilder.append$java_lang_Object(this.getDelegate());
                        hashCodeBuilder.append$java_lang_Object(this.getActivity());
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
                            let theDelegate;
                            theDelegate = this.getDelegate();
                            toStringBuilder.append$java_lang_String$java_lang_Object("delegate", theDelegate);
                        }
                        ;
                        {
                            let theResponisble;
                            theResponisble = this.getResponsible();
                            toStringBuilder.append$java_lang_String$java_lang_Object("responsible", theResponisble);
                        }
                        ;
                        {
                            let theActivity;
                            theActivity = this.getActivity();
                            toStringBuilder.append$java_lang_String$java_lang_Object("activity", theActivity);
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
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(result, this.getLabel().stream().map((s) => new org.openprovenance.prov.vanilla.Label(ActedOnBehalfOf.QUALIFIED_NAME_XSD_STRING_$LI$(), s)).collect(java.util.stream.Collectors.toList()));
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(result, this.getType());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(result, this.getOther().stream().map((o) => o).collect(java.util.stream.Collectors.toList()));
                        return result;
                    }
                    setIndexedAttributes(qn, attributes) {
                        ActedOnBehalfOf.u_$LI$().distribute(qn, attributes, this.getLabel(), java.util.Collections.EMPTY_LIST, java.util.Collections.EMPTY_LIST, this.getType(), java.util.Collections.EMPTY_LIST, this.getOther());
                    }
                    /**
                     *
                     * @return {*}
                     */
                    getIndexedAttributes() {
                        return ActedOnBehalfOf.u_$LI$().split$java_util_Collection(this.getAttributes());
                    }
                }
                vanilla.ActedOnBehalfOf = ActedOnBehalfOf;
                ActedOnBehalfOf["__class"] = "org.openprovenance.prov.vanilla.ActedOnBehalfOf";
                ActedOnBehalfOf["__interfaces"] = ["org.openprovenance.prov.model.HasOther", "org.openprovenance.prov.model.QualifiedRelation", "org.openprovenance.prov.model.HasType", "org.openprovenance.prov.model.StatementOrBundle", "org.openprovenance.apache.commons.lang.builder.ToString", "org.openprovenance.prov.model.Statement", "org.openprovenance.prov.vanilla.HasAttributes", "org.openprovenance.apache.commons.lang.builder.HashCode", "org.openprovenance.prov.model.Influence", "org.openprovenance.prov.model.HasLabel", "org.openprovenance.apache.commons.lang.builder.Equals", "org.openprovenance.prov.model.ActedOnBehalfOf", "org.openprovenance.prov.model.Identifiable", "org.openprovenance.prov.model.Relation"];
            })(vanilla = prov.vanilla || (prov.vanilla = {}));
        })(prov = openprovenance.prov || (openprovenance.prov = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
