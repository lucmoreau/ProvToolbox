/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var org;
(function (org) {
    var openprovenance;
    (function (openprovenance) {
        var prov;
        (function (prov) {
            var vanilla;
            (function (vanilla) {
                class SpecializationOf {
                    constructor(specificEntity, generalEntity) {
                        if (((specificEntity != null && (specificEntity.constructor != null && specificEntity.constructor["__interfaces"] != null && specificEntity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || specificEntity === null) && ((generalEntity != null && (generalEntity.constructor != null && generalEntity.constructor["__interfaces"] != null && generalEntity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || generalEntity === null)) {
                            let __args = arguments;
                            if (this.specificEntity === undefined) {
                                this.specificEntity = null;
                            }
                            if (this.generalEntity === undefined) {
                                this.generalEntity = null;
                            }
                            this.QUALIFIED_NAME_XSD_STRING = org.openprovenance.prov.vanilla.ProvFactory.getFactory().getName().XSD_STRING;
                            this.specificEntity = specificEntity;
                            this.generalEntity = generalEntity;
                        }
                        else if (specificEntity === undefined && generalEntity === undefined) {
                            let __args = arguments;
                            if (this.specificEntity === undefined) {
                                this.specificEntity = null;
                            }
                            if (this.generalEntity === undefined) {
                                this.generalEntity = null;
                            }
                            this.QUALIFIED_NAME_XSD_STRING = org.openprovenance.prov.vanilla.ProvFactory.getFactory().getName().XSD_STRING;
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    static u_$LI$() { if (SpecializationOf.u == null) {
                        SpecializationOf.u = new org.openprovenance.prov.vanilla.ProvUtilities();
                    } return SpecializationOf.u; }
                    /**
                     *
                     * @param {*} aid
                     */
                    setSpecificEntity(aid) {
                        this.specificEntity = aid;
                    }
                    /**
                     *
                     * @param {*} eid
                     */
                    setGeneralEntity(eid) {
                        this.generalEntity = eid;
                    }
                    /**
                     *
                     * @return {*}
                     */
                    getGeneralEntity() {
                        return this.generalEntity;
                    }
                    /**
                     *
                     * @return {*}
                     */
                    getSpecificEntity() {
                        return this.specificEntity;
                    }
                    /**
                     *
                     * @return {org.openprovenance.prov.model.StatementOrBundle.Kind}
                     */
                    getKind() {
                        return StatementOrBundle.Kind.PROV_SPECIALIZATION;
                    }
                    equals$java_lang_Object$org_openprovenance_apache_commons_lang_builder_EqualsBuilder(object, equalsBuilder) {
                        if (!(object != null && object instanceof org.openprovenance.prov.vanilla.SpecializationOf)) {
                            equalsBuilder.appendSuper(false);
                            return;
                        }
                        if (this === object) {
                            return;
                        }
                        const that = object;
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getSpecificEntity(), that.getSpecificEntity());
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getGeneralEntity(), that.getGeneralEntity());
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
                        if (!(object != null && object instanceof org.openprovenance.prov.vanilla.QualifiedSpecializationOf)) {
                            equalsBuilder.appendSuper(false);
                            return;
                        }
                        if (this === object) {
                            return;
                        }
                        const that = object;
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getSpecificEntity(), that.getSpecificEntity());
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getGeneralEntity(), that.getGeneralEntity());
                    }
                    equals$java_lang_Object(object) {
                        if (!(object != null && object instanceof org.openprovenance.prov.vanilla.SpecializationOf)) {
                            if (object != null && object instanceof org.openprovenance.prov.vanilla.QualifiedSpecializationOf) {
                                const qspec = object;
                                if (qspec.isUnqualified()) {
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
                        hashCodeBuilder.append$java_lang_Object(this.getSpecificEntity());
                        hashCodeBuilder.append$java_lang_Object(this.getGeneralEntity());
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
                            let theSpecificEntity;
                            theSpecificEntity = this.getSpecificEntity();
                            toStringBuilder.append$java_lang_String$java_lang_Object("specificEntity", theSpecificEntity);
                        }
                        ;
                        {
                            let theGeneralEntity;
                            theGeneralEntity = this.getGeneralEntity();
                            toStringBuilder.append$java_lang_String$java_lang_Object("generalEntity", theGeneralEntity);
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
                vanilla.SpecializationOf = SpecializationOf;
                SpecializationOf["__class"] = "org.openprovenance.prov.vanilla.SpecializationOf";
                SpecializationOf["__interfaces"] = ["org.openprovenance.prov.model.UnqualifiedRelation", "org.openprovenance.apache.commons.lang.builder.HashCode", "org.openprovenance.prov.model.SpecializationOf", "org.openprovenance.apache.commons.lang.builder.Equals", "org.openprovenance.prov.model.Relation", "org.openprovenance.prov.model.StatementOrBundle", "org.openprovenance.apache.commons.lang.builder.ToString", "org.openprovenance.prov.model.Statement"];
            })(vanilla = prov.vanilla || (prov.vanilla = {}));
        })(prov = openprovenance.prov || (openprovenance.prov = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
