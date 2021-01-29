/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var org;
(function (org) {
    var openprovenance;
    (function (openprovenance) {
        var prov;
        (function (prov) {
            var vanilla;
            (function (vanilla) {
                class QualifiedHadMember {
                    constructor(id, collection, entity, attributes) {
                        if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((collection != null && (collection.constructor != null && collection.constructor["__interfaces"] != null && collection.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || collection === null) && ((entity != null && (entity instanceof Array)) || entity === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                            let __args = arguments;
                            if (this.collection === undefined) {
                                this.collection = null;
                            }
                            this.id = null;
                            this.labels = ([]);
                            this.other = ([]);
                            this.type = ([]);
                            this.entity = ([]);
                            this.u = new org.openprovenance.prov.vanilla.ProvUtilities();
                            this.setId(id);
                            this.collection = collection;
                            /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(this.entity, entity);
                            this.u.populateAttributes(attributes, this.labels, ([]), this.type, ([]), this.other, null);
                        }
                        else if (id === undefined && collection === undefined && entity === undefined && attributes === undefined) {
                            let __args = arguments;
                            if (this.collection === undefined) {
                                this.collection = null;
                            }
                            this.id = null;
                            this.labels = ([]);
                            this.other = ([]);
                            this.type = ([]);
                            this.entity = ([]);
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
                     * @param {*} collection
                     */
                    setCollection(collection) {
                        this.collection = collection;
                    }
                    /**
                     *
                     * @return {*[]}
                     */
                    getEntity() {
                        return this.entity;
                    }
                    /**
                     *
                     * @return {*}
                     */
                    getCollection() {
                        return this.collection;
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
                        return StatementOrBundle.Kind.PROV_MEMBERSHIP;
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
                        if (!(object != null && object instanceof org.openprovenance.prov.vanilla.QualifiedHadMember)) {
                            equalsBuilder.appendSuper(false);
                            return;
                        }
                        if (this === object) {
                            return;
                        }
                        const that = object;
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getId(), that.getId());
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getCollection(), that.getCollection());
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getEntity(), that.getEntity());
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
                        if (!(object != null && object instanceof org.openprovenance.prov.vanilla.HadMember)) {
                            equalsBuilder.appendSuper(false);
                            return;
                        }
                        if (this === object) {
                            return;
                        }
                        const that = object;
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getCollection(), that.getCollection());
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getEntity(), that.getEntity());
                    }
                    equals$java_lang_Object(object) {
                        if (!(object != null && object instanceof org.openprovenance.prov.vanilla.QualifiedHadMember)) {
                            if (object != null && object instanceof org.openprovenance.prov.vanilla.HadMember) {
                                if (this.isUnqualified()) {
                                    console.info("QualifiedHadMember.equals with HadMember");
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
                        hashCodeBuilder.append$java_lang_Object(this.getCollection());
                        hashCodeBuilder.append$java_lang_Object(this.getEntity());
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
                            let theCollection;
                            theCollection = this.getCollection();
                            toStringBuilder.append$java_lang_String$java_lang_Object("collection", theCollection);
                        }
                        ;
                        {
                            let theEntity;
                            theEntity = this.getEntity();
                            toStringBuilder.append$java_lang_String$java_lang_Object("entity", theEntity);
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
                vanilla.QualifiedHadMember = QualifiedHadMember;
                QualifiedHadMember["__class"] = "org.openprovenance.prov.vanilla.QualifiedHadMember";
                QualifiedHadMember["__interfaces"] = ["org.openprovenance.prov.model.extension.QualifiedHadMember", "org.openprovenance.prov.model.QualifiedRelation", "org.openprovenance.prov.model.HasOther", "org.openprovenance.prov.model.UnqualifiedRelation", "org.openprovenance.prov.model.HasType", "org.openprovenance.prov.model.StatementOrBundle", "org.openprovenance.apache.commons.lang.builder.ToString", "org.openprovenance.prov.model.Statement", "org.openprovenance.prov.vanilla.HasAttributes", "org.openprovenance.apache.commons.lang.builder.HashCode", "org.openprovenance.prov.model.HasLabel", "org.openprovenance.prov.model.HadMember", "org.openprovenance.apache.commons.lang.builder.Equals", "org.openprovenance.prov.model.Relation", "org.openprovenance.prov.model.Identifiable"];
            })(vanilla = prov.vanilla || (prov.vanilla = {}));
        })(prov = openprovenance.prov || (openprovenance.prov = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
