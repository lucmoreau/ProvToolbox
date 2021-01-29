/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var org;
(function (org) {
    var openprovenance;
    (function (openprovenance) {
        var prov;
        (function (prov) {
            var vanilla;
            (function (vanilla) {
                class Activity {
                    constructor(id, startTime, endTime, attributes) {
                        if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((startTime != null && startTime instanceof javax.xml.datatype.XMLGregorianCalendar) || startTime === null) && ((endTime != null && endTime instanceof javax.xml.datatype.XMLGregorianCalendar) || endTime === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                            let __args = arguments;
                            if (this.id === undefined) {
                                this.id = null;
                            }
                            this.startTime = null;
                            this.endTime = null;
                            this.labels = ([]);
                            this.location = ([]);
                            this.other = ([]);
                            this.type = ([]);
                            this.setId(id);
                            this.setStartTime(startTime);
                            this.setEndTime(endTime);
                            Activity.u_$LI$().populateAttributes(attributes, this.labels, this.location, this.type, ([]), this.other, null);
                        }
                        else if (id === undefined && startTime === undefined && endTime === undefined && attributes === undefined) {
                            let __args = arguments;
                            if (this.id === undefined) {
                                this.id = null;
                            }
                            this.startTime = null;
                            this.endTime = null;
                            this.labels = ([]);
                            this.location = ([]);
                            this.other = ([]);
                            this.type = ([]);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    static u_$LI$() { if (Activity.u == null) {
                        Activity.u = new org.openprovenance.prov.vanilla.ProvUtilities();
                    } return Activity.u; }
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
                        return StatementOrBundle.Kind.PROV_ACTIVITY;
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
                     * @return {javax.xml.datatype.XMLGregorianCalendar}
                     */
                    getStartTime() {
                        return /* orElse */ ((v, v2) => v == null ? v2 : v)(this.startTime, null);
                    }
                    /**
                     *
                     * @param {javax.xml.datatype.XMLGregorianCalendar} value
                     */
                    setStartTime(value) {
                        this.startTime = value;
                    }
                    /**
                     *
                     * @return {javax.xml.datatype.XMLGregorianCalendar}
                     */
                    getEndTime() {
                        return /* orElse */ ((v, v2) => v == null ? v2 : v)(this.endTime, null);
                    }
                    /**
                     *
                     * @param {javax.xml.datatype.XMLGregorianCalendar} value
                     */
                    setEndTime(value) {
                        this.endTime = value;
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
                        if (!(object != null && object instanceof org.openprovenance.prov.vanilla.Activity)) {
                            equalsBuilder.appendSuper(false);
                            return;
                        }
                        if (this === object) {
                            return;
                        }
                        const that = object;
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getStartTime(), that.getStartTime());
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getEndTime(), that.getEndTime());
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getId(), that.getId());
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
                        if (!(object != null && object instanceof org.openprovenance.prov.vanilla.Activity)) {
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
                        hashCodeBuilder.append$java_lang_Object(this.getStartTime());
                        hashCodeBuilder.append$java_lang_Object(this.getEndTime());
                        hashCodeBuilder.append$java_lang_Object(this.getId());
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
                            let theStartTime;
                            theStartTime = this.getStartTime();
                            toStringBuilder.append$java_lang_String$java_lang_Object("startTime", theStartTime);
                        }
                        ;
                        {
                            let theEndTime;
                            theEndTime = this.getEndTime();
                            toStringBuilder.append$java_lang_String$java_lang_Object("endTime", theEndTime);
                        }
                        ;
                        {
                            let theAttributes;
                            theAttributes = this.getIndexedAttributes();
                            toStringBuilder.append$java_lang_String$java_lang_Object("attributes", theAttributes);
                        }
                        ;
                        {
                            let theId;
                            theId = this.getId();
                            toStringBuilder.append$java_lang_String$java_lang_Object("id", theId);
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
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(result, this.getOther().stream().map((o) => o).collect(java.util.stream.Collectors.toList()));
                        return result;
                    }
                    setIndexedAttributes(qn, attributes) {
                        const values_discard = ([]);
                        const roles_discard = ([]);
                        Activity.u_$LI$().distribute(qn, attributes, this.getLabel(), values_discard, this.getLocation(), this.getType(), roles_discard, this.getOther());
                    }
                    /**
                     *
                     * @return {*}
                     */
                    getIndexedAttributes() {
                        return Activity.u_$LI$().split$java_util_Collection(this.getAttributes());
                    }
                }
                vanilla.Activity = Activity;
                Activity["__class"] = "org.openprovenance.prov.vanilla.Activity";
                Activity["__interfaces"] = ["org.openprovenance.prov.model.HasOther", "org.openprovenance.prov.model.HasType", "org.openprovenance.prov.model.HasLocation", "org.openprovenance.prov.model.StatementOrBundle", "org.openprovenance.apache.commons.lang.builder.ToString", "org.openprovenance.prov.model.Activity", "org.openprovenance.prov.model.Statement", "org.openprovenance.prov.model.Element", "org.openprovenance.prov.vanilla.HasAttributes", "org.openprovenance.apache.commons.lang.builder.HashCode", "org.openprovenance.prov.model.HasLabel", "org.openprovenance.apache.commons.lang.builder.Equals", "org.openprovenance.prov.model.Identifiable"];
            })(vanilla = prov.vanilla || (prov.vanilla = {}));
        })(prov = openprovenance.prov || (openprovenance.prov = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
