/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var org;
(function (org) {
    var openprovenance;
    (function (openprovenance) {
        var prov;
        (function (prov) {
            var vanilla;
            (function (vanilla) {
                class WasAssociatedWith {
                    constructor(id, activity, agent, plan, attributes) {
                        if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((activity != null && (activity.constructor != null && activity.constructor["__interfaces"] != null && activity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || activity === null) && ((agent != null && (agent.constructor != null && agent.constructor["__interfaces"] != null && agent.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || agent === null) && ((plan != null && (plan.constructor != null && plan.constructor["__interfaces"] != null && plan.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || plan === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                            let __args = arguments;
                            if (this.activity === undefined) {
                                this.activity = null;
                            }
                            if (this.agent === undefined) {
                                this.agent = null;
                            }
                            this.id = null;
                            this.labels = ([]);
                            this.other = ([]);
                            this.type = ([]);
                            this.role = ([]);
                            this.plan = null;
                            this.setId(id);
                            this.activity = activity;
                            this.agent = agent;
                            this.plan = plan;
                            WasAssociatedWith.u_$LI$().populateAttributes(attributes, this.labels, ([]), this.type, this.role, this.other, null);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((activity != null && (activity.constructor != null && activity.constructor["__interfaces"] != null && activity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || activity === null) && ((agent != null && (agent.constructor != null && agent.constructor["__interfaces"] != null && agent.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || agent === null) && ((plan != null && (plan instanceof Array)) || plan === null) && attributes === undefined) {
                            let __args = arguments;
                            let attributes = __args[3];
                            if (this.activity === undefined) {
                                this.activity = null;
                            }
                            if (this.agent === undefined) {
                                this.agent = null;
                            }
                            this.id = null;
                            this.labels = ([]);
                            this.other = ([]);
                            this.type = ([]);
                            this.role = ([]);
                            this.plan = null;
                            this.setId(id);
                            this.activity = activity;
                            this.agent = agent;
                            WasAssociatedWith.u_$LI$().populateAttributes(attributes, this.labels, ([]), this.type, this.role, this.other, null);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((activity != null && (activity instanceof Array)) || activity === null) && agent === undefined && plan === undefined && attributes === undefined) {
                            let __args = arguments;
                            let attributes = __args[1];
                            if (this.activity === undefined) {
                                this.activity = null;
                            }
                            if (this.agent === undefined) {
                                this.agent = null;
                            }
                            this.id = null;
                            this.labels = ([]);
                            this.other = ([]);
                            this.type = ([]);
                            this.role = ([]);
                            this.plan = null;
                            this.setId(id);
                            WasAssociatedWith.u_$LI$().populateAttributes(attributes, this.labels, ([]), this.type, this.role, this.other, null);
                        }
                        else if (id === undefined && activity === undefined && agent === undefined && plan === undefined && attributes === undefined) {
                            let __args = arguments;
                            if (this.activity === undefined) {
                                this.activity = null;
                            }
                            if (this.agent === undefined) {
                                this.agent = null;
                            }
                            this.id = null;
                            this.labels = ([]);
                            this.other = ([]);
                            this.type = ([]);
                            this.role = ([]);
                            this.plan = null;
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    static u_$LI$() { if (WasAssociatedWith.u == null) {
                        WasAssociatedWith.u = new org.openprovenance.prov.vanilla.ProvUtilities();
                    } return WasAssociatedWith.u; }
                    /**
                     *
                     * @param {*} aid
                     */
                    setActivity(aid) {
                        this.activity = aid;
                    }
                    /**
                     *
                     * @param {*} agid
                     */
                    setAgent(agid) {
                        this.agent = agid;
                    }
                    /**
                     *
                     * @return {*}
                     */
                    getAgent() {
                        return this.agent;
                    }
                    /**
                     *
                     * @return {*}
                     */
                    getActivity() {
                        return this.activity;
                    }
                    /**
                     * Gets the value of the role property.
                     *
                     * <p>
                     * This accessor method returns a reference to the live list,
                     * not a snapshot. Therefore, any modification made to the
                     * returned list will be present inside the object.
                     * This is why there is not a <CODE>set</CODE> method for the role property.
                     *
                     * <p>
                     * For example, to add a new item, do as follows:
                     * <pre>
                     * getRole().add(newItem);
                     * </pre>
                     *
                     * @return {*[]} a list of objects of type
                     * {@link Role}
                     */
                    getRole() {
                        return this.role;
                    }
                    /**
                     *
                     * @return {*}
                     */
                    getPlan() {
                        return /* orElse */ ((v, v2) => v == null ? v2 : v)(this.plan, null);
                    }
                    /**
                     * Set Plan
                     *
                     * @param {*} plan
                     */
                    setPlan(plan) {
                        this.plan = plan;
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
                        return StatementOrBundle.Kind.PROV_ASSOCIATION;
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
                        if (!(object != null && object instanceof org.openprovenance.prov.vanilla.WasAssociatedWith)) {
                            equalsBuilder.appendSuper(false);
                            return;
                        }
                        if (this === object) {
                            return;
                        }
                        const that = object;
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getId(), that.getId());
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getActivity(), that.getActivity());
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getAgent(), that.getAgent());
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getPlan(), that.getPlan());
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
                        if (!(object != null && object instanceof org.openprovenance.prov.vanilla.WasAssociatedWith)) {
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
                        hashCodeBuilder.append$java_lang_Object(this.getActivity());
                        hashCodeBuilder.append$java_lang_Object(this.getAgent());
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
                            let theActivity;
                            theActivity = this.getActivity();
                            toStringBuilder.append$java_lang_String$java_lang_Object("activity", theActivity);
                        }
                        ;
                        {
                            let theAgent;
                            theAgent = this.getAgent();
                            toStringBuilder.append$java_lang_String$java_lang_Object("agent", theAgent);
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
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(result, this.getRole());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(result, this.getOther().stream().map((o) => o).collect(java.util.stream.Collectors.toList()));
                        return result;
                    }
                    setIndexedAttributes(qn, attributes) {
                        const values = ([]);
                        const locations = ([]);
                        WasAssociatedWith.u_$LI$().distribute(qn, attributes, this.getLabel(), values, locations, this.getType(), this.getRole(), this.getOther());
                    }
                    /**
                     *
                     * @return {*}
                     */
                    getIndexedAttributes() {
                        return WasAssociatedWith.u_$LI$().split$java_util_Collection(this.getAttributes());
                    }
                }
                vanilla.WasAssociatedWith = WasAssociatedWith;
                WasAssociatedWith["__class"] = "org.openprovenance.prov.vanilla.WasAssociatedWith";
                WasAssociatedWith["__interfaces"] = ["org.openprovenance.prov.model.HasOther", "org.openprovenance.prov.model.QualifiedRelation", "org.openprovenance.prov.model.WasAssociatedWith", "org.openprovenance.prov.model.HasType", "org.openprovenance.prov.model.StatementOrBundle", "org.openprovenance.apache.commons.lang.builder.ToString", "org.openprovenance.prov.model.Statement", "org.openprovenance.prov.vanilla.HasAttributes", "org.openprovenance.apache.commons.lang.builder.HashCode", "org.openprovenance.prov.model.Influence", "org.openprovenance.prov.model.HasLabel", "org.openprovenance.prov.model.HasRole", "org.openprovenance.apache.commons.lang.builder.Equals", "org.openprovenance.prov.model.Identifiable", "org.openprovenance.prov.model.Relation"];
            })(vanilla = prov.vanilla || (prov.vanilla = {}));
        })(prov = openprovenance.prov || (openprovenance.prov = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
