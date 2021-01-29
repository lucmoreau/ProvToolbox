/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var org;
(function (org) {
    var openprovenance;
    (function (openprovenance) {
        var prov;
        (function (prov) {
            var vanilla;
            (function (vanilla) {
                class HadMember {
                    constructor(collection, entity) {
                        if (((collection != null && (collection.constructor != null && collection.constructor["__interfaces"] != null && collection.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || collection === null) && ((entity != null && (entity instanceof Array)) || entity === null)) {
                            let __args = arguments;
                            if (this.collection === undefined) {
                                this.collection = null;
                            }
                            this.entity = ([]);
                            this.collection = collection;
                            this.entity = entity;
                        }
                        else if (collection === undefined && entity === undefined) {
                            let __args = arguments;
                            if (this.collection === undefined) {
                                this.collection = null;
                            }
                            this.entity = ([]);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    /**
                     * Get an identifier for the collection whose member is asserted
                     *
                     * @return {*} QualifiedName for the collection
                     * @see <a href="http://www.w3.org/TR/prov-dm/#membership.collection">membership collection</a>
                     */
                    getCollection() {
                        return this.collection;
                    }
                    /**
                     * Get the list of identifiers of entities that are member of the collection.
                     *
                     * @return {*[]} a list of {@link QualifiedName}
                     *
                     *
                     * <p>
                     * This accessor method returns a reference to the live list,
                     * not a snapshot. Therefore any modification you make to the
                     * returned list will be present inside the JAXB object.
                     * This is why there is not a <CODE>set</CODE> method for the entity property.
                     *
                     * <p>
                     * For example, to add a new item, do as follows:
                     * <pre>
                     * getEntity().add(newItem);
                     * </pre>
                     *
                     *
                     * <p>
                     * Objects of the following type(s) are allowed in the list
                     * {@link QualifiedName}
                     * @see <a href="http://www.w3.org/TR/prov-dm/#membership.entity">membership entity</a>
                     */
                    getEntity() {
                        return this.entity;
                    }
                    /**
                     * Set an identifier for the collection whose member is asserted
                     *
                     * @param {*} collection QualifiedName for the collection
                     * @see <a href="http://www.w3.org/TR/prov-dm/#membership.collection">membership collection</a>
                     */
                    setCollection(collection) {
                        this.collection = collection;
                    }
                    /**
                     * Gets the type of a provenance statement
                     *
                     * @return {org.openprovenance.prov.model.StatementOrBundle.Kind} {@link Kind}
                     */
                    getKind() {
                        return StatementOrBundle.Kind.PROV_MEMBERSHIP;
                    }
                    equals$java_lang_Object$org_openprovenance_apache_commons_lang_builder_EqualsBuilder(object, equalsBuilder) {
                        if (!(object != null && object instanceof org.openprovenance.prov.vanilla.HadMember)) {
                            equalsBuilder.appendSuper(false);
                            return;
                        }
                        if (this === object) {
                            return;
                        }
                        const that = object;
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getEntity(), that.getEntity());
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getCollection(), that.getCollection());
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
                        if (!(object != null && object instanceof org.openprovenance.prov.vanilla.QualifiedHadMember)) {
                            equalsBuilder.appendSuper(false);
                            return;
                        }
                        if (this === object) {
                            return;
                        }
                        const that = object;
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getEntity(), that.getEntity());
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getCollection(), that.getCollection());
                    }
                    equals$java_lang_Object(object) {
                        if (!(object != null && object instanceof org.openprovenance.prov.vanilla.HadMember)) {
                            if (object != null && object instanceof org.openprovenance.prov.vanilla.QualifiedHadMember) {
                                const qmem = object;
                                if (qmem.isUnqualified()) {
                                    const equalsBuilder2 = new org.openprovenance.apache.commons.lang.builder.EqualsBuilder();
                                    this.equals2(object, equalsBuilder2);
                                    return equalsBuilder2.isEquals();
                                }
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
                        hashCodeBuilder.append$java_lang_Object(this.getEntity());
                        hashCodeBuilder.append$java_lang_Object(this.getCollection());
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
                vanilla.HadMember = HadMember;
                HadMember["__class"] = "org.openprovenance.prov.vanilla.HadMember";
                HadMember["__interfaces"] = ["org.openprovenance.prov.model.UnqualifiedRelation", "org.openprovenance.apache.commons.lang.builder.HashCode", "org.openprovenance.prov.model.HadMember", "org.openprovenance.apache.commons.lang.builder.Equals", "org.openprovenance.prov.model.Relation", "org.openprovenance.prov.model.StatementOrBundle", "org.openprovenance.apache.commons.lang.builder.ToString", "org.openprovenance.prov.model.Statement"];
            })(vanilla = prov.vanilla || (prov.vanilla = {}));
        })(prov = openprovenance.prov || (openprovenance.prov = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
