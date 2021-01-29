/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var org;
(function (org) {
    var openprovenance;
    (function (openprovenance) {
        var prov;
        (function (prov) {
            var vanilla;
            (function (vanilla) {
                class WasDerivedFrom {
                    constructor(id, generatedEntity, usedEntity, activity, generation, usage, attributes) {
                        if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((generatedEntity != null && (generatedEntity.constructor != null && generatedEntity.constructor["__interfaces"] != null && generatedEntity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || generatedEntity === null) && ((usedEntity != null && (usedEntity.constructor != null && usedEntity.constructor["__interfaces"] != null && usedEntity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || usedEntity === null) && ((activity != null && (activity.constructor != null && activity.constructor["__interfaces"] != null && activity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || activity === null) && ((generation != null && (generation.constructor != null && generation.constructor["__interfaces"] != null && generation.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || generation === null) && ((usage != null && (usage.constructor != null && usage.constructor["__interfaces"] != null && usage.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || usage === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                            let __args = arguments;
                            if (this.generatedEntity === undefined) {
                                this.generatedEntity = null;
                            }
                            if (this.usedEntity === undefined) {
                                this.usedEntity = null;
                            }
                            this.id = null;
                            this.labels = ([]);
                            this.other = ([]);
                            this.type = ([]);
                            this.activity = null;
                            this.generation = null;
                            this.usage = null;
                            this.setId(id);
                            this.setActivity(activity);
                            this.usedEntity = usedEntity;
                            this.generatedEntity = generatedEntity;
                            this.setGeneration(generation);
                            this.setUsage(usage);
                            WasDerivedFrom.u_$LI$().populateAttributes(attributes, this.labels, java.util.Collections.EMPTY_LIST, this.type, java.util.Collections.EMPTY_LIST, this.other, null);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((generatedEntity != null && (generatedEntity.constructor != null && generatedEntity.constructor["__interfaces"] != null && generatedEntity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || generatedEntity === null) && ((usedEntity != null && (usedEntity.constructor != null && usedEntity.constructor["__interfaces"] != null && usedEntity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || usedEntity === null) && ((activity != null && (activity.constructor != null && activity.constructor["__interfaces"] != null && activity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || activity === null) && ((generation != null && (generation instanceof Array)) || generation === null) && usage === undefined && attributes === undefined) {
                            let __args = arguments;
                            let attributes = __args[4];
                            if (this.generatedEntity === undefined) {
                                this.generatedEntity = null;
                            }
                            if (this.usedEntity === undefined) {
                                this.usedEntity = null;
                            }
                            this.id = null;
                            this.labels = ([]);
                            this.other = ([]);
                            this.type = ([]);
                            this.activity = null;
                            this.generation = null;
                            this.usage = null;
                            this.setId(id);
                            this.setActivity(activity);
                            this.usedEntity = usedEntity;
                            this.generatedEntity = generatedEntity;
                            WasDerivedFrom.u_$LI$().populateAttributes(attributes, this.labels, java.util.Collections.EMPTY_LIST, this.type, java.util.Collections.EMPTY_LIST, this.other, null);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((generatedEntity != null && (generatedEntity.constructor != null && generatedEntity.constructor["__interfaces"] != null && generatedEntity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || generatedEntity === null) && ((usedEntity != null && (usedEntity.constructor != null && usedEntity.constructor["__interfaces"] != null && usedEntity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || usedEntity === null) && ((activity != null && (activity instanceof Array)) || activity === null) && generation === undefined && usage === undefined && attributes === undefined) {
                            let __args = arguments;
                            let attributes = __args[3];
                            if (this.generatedEntity === undefined) {
                                this.generatedEntity = null;
                            }
                            if (this.usedEntity === undefined) {
                                this.usedEntity = null;
                            }
                            this.id = null;
                            this.labels = ([]);
                            this.other = ([]);
                            this.type = ([]);
                            this.activity = null;
                            this.generation = null;
                            this.usage = null;
                            this.setId(id);
                            this.usedEntity = usedEntity;
                            this.generatedEntity = generatedEntity;
                            WasDerivedFrom.u_$LI$().populateAttributes(attributes, this.labels, java.util.Collections.EMPTY_LIST, this.type, java.util.Collections.EMPTY_LIST, this.other, null);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((generatedEntity != null && (generatedEntity instanceof Array)) || generatedEntity === null) && usedEntity === undefined && activity === undefined && generation === undefined && usage === undefined && attributes === undefined) {
                            let __args = arguments;
                            let attributes = __args[1];
                            if (this.generatedEntity === undefined) {
                                this.generatedEntity = null;
                            }
                            if (this.usedEntity === undefined) {
                                this.usedEntity = null;
                            }
                            this.id = null;
                            this.labels = ([]);
                            this.other = ([]);
                            this.type = ([]);
                            this.activity = null;
                            this.generation = null;
                            this.usage = null;
                            this.setId(id);
                            WasDerivedFrom.u_$LI$().populateAttributes(attributes, this.labels, java.util.Collections.EMPTY_LIST, this.type, java.util.Collections.EMPTY_LIST, this.other, null);
                        }
                        else if (id === undefined && generatedEntity === undefined && usedEntity === undefined && activity === undefined && generation === undefined && usage === undefined && attributes === undefined) {
                            let __args = arguments;
                            if (this.generatedEntity === undefined) {
                                this.generatedEntity = null;
                            }
                            if (this.usedEntity === undefined) {
                                this.usedEntity = null;
                            }
                            this.id = null;
                            this.labels = ([]);
                            this.other = ([]);
                            this.type = ([]);
                            this.activity = null;
                            this.generation = null;
                            this.usage = null;
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    static u_$LI$() { if (WasDerivedFrom.u == null) {
                        WasDerivedFrom.u = new org.openprovenance.prov.vanilla.ProvUtilities();
                    } return WasDerivedFrom.u; }
                    /**
                     *
                     * @param {*} aid
                     */
                    setActivity(aid) {
                        this.activity = aid;
                    }
                    /**
                     *
                     * @param {*} gen
                     */
                    setGeneration(gen) {
                        this.generation = gen;
                    }
                    /**
                     *
                     * @param {*} use
                     */
                    setUsage(use) {
                        this.usage = use;
                    }
                    /**
                     *
                     * @param {*} eid
                     */
                    setUsedEntity(eid) {
                        this.usedEntity = eid;
                    }
                    /**
                     *
                     * @param {*} eid
                     */
                    setGeneratedEntity(eid) {
                        this.generatedEntity = eid;
                    }
                    /**
                     *
                     * @return {*}
                     */
                    getUsedEntity() {
                        return this.usedEntity;
                    }
                    /**
                     *
                     * @return {*}
                     */
                    getUsage() {
                        return /* orElse */ ((v, v2) => v == null ? v2 : v)(this.usage, null);
                    }
                    /**
                     *
                     * @return {*}
                     */
                    getGeneratedEntity() {
                        return this.generatedEntity;
                    }
                    /**
                     *
                     * @return {*}
                     */
                    getGeneration() {
                        return /* orElse */ ((v, v2) => v == null ? v2 : v)(this.generation, null);
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
                        return StatementOrBundle.Kind.PROV_DERIVATION;
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
                        if (!(object != null && object instanceof org.openprovenance.prov.vanilla.WasDerivedFrom)) {
                            equalsBuilder.appendSuper(false);
                            return;
                        }
                        if (this === object) {
                            return;
                        }
                        const that = object;
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getId(), that.getId());
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getGeneratedEntity(), that.getGeneratedEntity());
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getUsedEntity(), that.getUsedEntity());
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getActivity(), that.getActivity());
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getUsage(), that.getUsage());
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getGeneration(), that.getGeneration());
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
                        if (!(object != null && object instanceof org.openprovenance.prov.vanilla.WasDerivedFrom)) {
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
                        hashCodeBuilder.append$java_lang_Object(this.getGeneratedEntity());
                        hashCodeBuilder.append$java_lang_Object(this.getUsedEntity());
                        hashCodeBuilder.append$java_lang_Object(this.getActivity());
                        hashCodeBuilder.append$java_lang_Object(this.getGeneration());
                        hashCodeBuilder.append$java_lang_Object(this.getUsage());
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
                            let theEntity;
                            theEntity = this.getGeneratedEntity();
                            toStringBuilder.append$java_lang_String$java_lang_Object("generatedEntity", theEntity);
                        }
                        ;
                        {
                            let theEntity;
                            theEntity = this.getUsedEntity();
                            toStringBuilder.append$java_lang_String$java_lang_Object("usedEntity", theEntity);
                        }
                        ;
                        {
                            let theActivity;
                            theActivity = this.getActivity();
                            toStringBuilder.append$java_lang_String$java_lang_Object("activity", theActivity);
                        }
                        ;
                        {
                            let theActivity;
                            theActivity = this.getGeneration();
                            toStringBuilder.append$java_lang_String$java_lang_Object("generation", theActivity);
                        }
                        ;
                        {
                            let theActivity;
                            theActivity = this.getUsage();
                            toStringBuilder.append$java_lang_String$java_lang_Object("usage", theActivity);
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
                        WasDerivedFrom.u_$LI$().distribute(qn, attributes, this.getLabel(), values, java.util.Collections.EMPTY_LIST, this.getType(), java.util.Collections.EMPTY_LIST, this.getOther());
                    }
                    /**
                     *
                     * @return {*}
                     */
                    getIndexedAttributes() {
                        return WasDerivedFrom.u_$LI$().split$java_util_Collection(this.getAttributes());
                    }
                }
                vanilla.WasDerivedFrom = WasDerivedFrom;
                WasDerivedFrom["__class"] = "org.openprovenance.prov.vanilla.WasDerivedFrom";
                WasDerivedFrom["__interfaces"] = ["org.openprovenance.prov.model.HasOther", "org.openprovenance.prov.model.QualifiedRelation", "org.openprovenance.prov.model.HasType", "org.openprovenance.prov.model.WasDerivedFrom", "org.openprovenance.prov.model.StatementOrBundle", "org.openprovenance.apache.commons.lang.builder.ToString", "org.openprovenance.prov.model.Statement", "org.openprovenance.prov.vanilla.HasAttributes", "org.openprovenance.apache.commons.lang.builder.HashCode", "org.openprovenance.prov.model.Influence", "org.openprovenance.prov.model.HasLabel", "org.openprovenance.apache.commons.lang.builder.Equals", "org.openprovenance.prov.model.Identifiable", "org.openprovenance.prov.model.Relation"];
            })(vanilla = prov.vanilla || (prov.vanilla = {}));
        })(prov = openprovenance.prov || (openprovenance.prov = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
