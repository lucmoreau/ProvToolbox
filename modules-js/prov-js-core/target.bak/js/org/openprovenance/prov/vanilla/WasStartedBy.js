/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var org;
(function (org) {
    var openprovenance;
    (function (openprovenance) {
        var prov;
        (function (prov) {
            var vanilla;
            (function (vanilla) {
                class WasStartedBy {
                    constructor(id, activity, trigger, starter, time, attributes) {
                        if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((activity != null && (activity.constructor != null && activity.constructor["__interfaces"] != null && activity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || activity === null) && ((trigger != null && (trigger.constructor != null && trigger.constructor["__interfaces"] != null && trigger.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || trigger === null) && ((starter != null && (starter.constructor != null && starter.constructor["__interfaces"] != null && starter.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || starter === null) && ((time != null && time instanceof javax.xml.datatype.XMLGregorianCalendar) || time === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                            let __args = arguments;
                            if (this.activity === undefined) {
                                this.activity = null;
                            }
                            this.id = null;
                            this.time = null;
                            this.labels = ([]);
                            this.location = ([]);
                            this.other = ([]);
                            this.type = ([]);
                            this.role = ([]);
                            this.trigger = null;
                            this.starter = null;
                            this.setId(id);
                            this.activity = activity;
                            this.trigger = trigger;
                            this.starter = starter;
                            this.time = time;
                            WasStartedBy.u_$LI$().populateAttributes(attributes, this.labels, this.location, this.type, this.role, this.other, null);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((activity != null && (activity instanceof Array)) || activity === null) && trigger === undefined && starter === undefined && time === undefined && attributes === undefined) {
                            let __args = arguments;
                            let attributes = __args[1];
                            if (this.activity === undefined) {
                                this.activity = null;
                            }
                            this.id = null;
                            this.time = null;
                            this.labels = ([]);
                            this.location = ([]);
                            this.other = ([]);
                            this.type = ([]);
                            this.role = ([]);
                            this.trigger = null;
                            this.starter = null;
                            this.setId(id);
                            this.time = null;
                            WasStartedBy.u_$LI$().populateAttributes(attributes, this.labels, this.location, this.type, this.role, this.other, null);
                        }
                        else if (id === undefined && activity === undefined && trigger === undefined && starter === undefined && time === undefined && attributes === undefined) {
                            let __args = arguments;
                            if (this.activity === undefined) {
                                this.activity = null;
                            }
                            this.id = null;
                            this.time = null;
                            this.labels = ([]);
                            this.location = ([]);
                            this.other = ([]);
                            this.type = ([]);
                            this.role = ([]);
                            this.trigger = null;
                            this.starter = null;
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    static u_$LI$() { if (WasStartedBy.u == null) {
                        WasStartedBy.u = new org.openprovenance.prov.vanilla.ProvUtilities();
                    } return WasStartedBy.u; }
                    /**
                     *
                     * @param {*} aid
                     */
                    setActivity(aid) {
                        this.activity = aid;
                    }
                    /**
                     *
                     * @param {*} eid
                     */
                    setTrigger(eid) {
                        this.trigger = eid;
                    }
                    /**
                     *
                     * @return {*}
                     */
                    getTrigger() {
                        return /* orElse */ ((v, v2) => v == null ? v2 : v)(this.trigger, null);
                    }
                    /**
                     *
                     * @return {*}
                     */
                    getActivity() {
                        return this.activity;
                    }
                    /**
                     *
                     * @param {*} aid
                     */
                    setStarter(aid) {
                        this.starter = aid;
                    }
                    /**
                     *
                     * @return {*}
                     */
                    getStarter() {
                        return /* orElse */ ((v, v2) => v == null ? v2 : v)(this.starter, null);
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
                     * Get time instant
                     *
                     * @return {javax.xml.datatype.XMLGregorianCalendar} {@link XMLGregorianCalendar}
                     */
                    getTime() {
                        return /* orElse */ ((v, v2) => v == null ? v2 : v)(this.time, null);
                    }
                    /**
                     * Set time instant
                     *
                     * @param {javax.xml.datatype.XMLGregorianCalendar} time
                     */
                    setTime(time) {
                        this.time = time;
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
                        return StatementOrBundle.Kind.PROV_START;
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
                    getLocation() {
                        return this.location;
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
                        if (!(object != null && object instanceof org.openprovenance.prov.vanilla.WasStartedBy)) {
                            equalsBuilder.appendSuper(false);
                            return;
                        }
                        if (this === object) {
                            return;
                        }
                        const that = object;
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getId(), that.getId());
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getActivity(), that.getActivity());
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getTrigger(), that.getTrigger());
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getStarter(), that.getStarter());
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getTime(), that.getTime());
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
                        if (!(object != null && object instanceof org.openprovenance.prov.vanilla.WasStartedBy)) {
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
                        hashCodeBuilder.append$java_lang_Object(this.getTrigger());
                        hashCodeBuilder.append$java_lang_Object(this.getStarter());
                        hashCodeBuilder.append$java_lang_Object(this.getTime());
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
                            let theTrigger;
                            theTrigger = this.getTrigger();
                            toStringBuilder.append$java_lang_String$java_lang_Object("trigger", theTrigger);
                        }
                        ;
                        {
                            let theStarter;
                            theStarter = this.getTrigger();
                            toStringBuilder.append$java_lang_String$java_lang_Object("starter", theStarter);
                        }
                        ;
                        {
                            let theTime;
                            theTime = this.getTime();
                            toStringBuilder.append$java_lang_String$java_lang_Object("time", theTime);
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
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(result, this.getLocation());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(result, this.getRole());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(result, this.getOther().stream().map((o) => o).collect(java.util.stream.Collectors.toList()));
                        return result;
                    }
                    setIndexedAttributes(qn, attributes) {
                        const values = ([]);
                        WasStartedBy.u_$LI$().distribute(qn, attributes, this.getLabel(), values, this.getLocation(), this.getType(), this.getRole(), this.getOther());
                    }
                    /**
                     *
                     * @return {*}
                     */
                    getIndexedAttributes() {
                        return WasStartedBy.u_$LI$().split$java_util_Collection(this.getAttributes());
                    }
                }
                vanilla.WasStartedBy = WasStartedBy;
                WasStartedBy["__class"] = "org.openprovenance.prov.vanilla.WasStartedBy";
                WasStartedBy["__interfaces"] = ["org.openprovenance.prov.model.HasOther", "org.openprovenance.prov.model.QualifiedRelation", "org.openprovenance.prov.model.HasType", "org.openprovenance.prov.model.HasLocation", "org.openprovenance.prov.model.StatementOrBundle", "org.openprovenance.apache.commons.lang.builder.ToString", "org.openprovenance.prov.model.Statement", "org.openprovenance.prov.model.HasTime", "org.openprovenance.prov.vanilla.HasAttributes", "org.openprovenance.prov.model.WasStartedBy", "org.openprovenance.apache.commons.lang.builder.HashCode", "org.openprovenance.prov.model.Influence", "org.openprovenance.prov.model.HasLabel", "org.openprovenance.prov.model.HasRole", "org.openprovenance.apache.commons.lang.builder.Equals", "org.openprovenance.prov.model.Identifiable", "org.openprovenance.prov.model.Relation"];
            })(vanilla = prov.vanilla || (prov.vanilla = {}));
        })(prov = openprovenance.prov || (openprovenance.prov = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
