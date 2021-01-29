/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var org;
(function (org) {
    var openprovenance;
    (function (openprovenance) {
        var prov;
        (function (prov) {
            var vanilla;
            (function (vanilla) {
                class Bundle {
                    constructor(id, namespace, statements) {
                        if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((namespace != null && namespace instanceof org.openprovenance.prov.model.Namespace) || namespace === null) && ((statements != null && (statements instanceof Array)) || statements === null)) {
                            let __args = arguments;
                            if (this.id === undefined) {
                                this.id = null;
                            }
                            this.namespaces = new org.openprovenance.prov.model.Namespace();
                            this.statements = ([]);
                            this.id = id;
                            if (namespace != null) {
                                this.namespaces = namespace;
                            }
                            else {
                                this.namespaces = new org.openprovenance.prov.model.Namespace();
                            }
                            if (statements != null) /* addAll */
                                ((l1, l2) => l1.push.apply(l1, l2))(this.statements, statements);
                        }
                        else if (id === undefined && namespace === undefined && statements === undefined) {
                            let __args = arguments;
                            if (this.id === undefined) {
                                this.id = null;
                            }
                            this.namespaces = new org.openprovenance.prov.model.Namespace();
                            this.statements = ([]);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    /**
                     *
                     * @return {*[]}
                     */
                    getStatement() {
                        return this.statements;
                    }
                    /**
                     *
                     * @param {org.openprovenance.prov.model.Namespace} namespaces
                     */
                    setNamespace(namespaces) {
                        if (namespaces != null)
                            this.namespaces = namespaces;
                    }
                    /**
                     *
                     * @return {org.openprovenance.prov.model.Namespace}
                     */
                    getNamespace() {
                        return this.namespaces;
                    }
                    /**
                     * Gets the value of the id property.  A null value means that the object has not been identified.  {@link Entity}, {@link Activity},
                     * {@link Agent} have a non-null identifier.
                     *
                     * @return {*} possible object is
                     * {@link QualifiedName}
                     */
                    getId() {
                        return this.id;
                    }
                    /**
                     * Sets the value of the id property.
                     *
                     * @param {*} value allowed object is
                     * {@link QualifiedName}
                     */
                    setId(value) {
                        this.id = value;
                    }
                    /**
                     * Gets the type of a provenance statement
                     *
                     * @return {org.openprovenance.prov.model.StatementOrBundle.Kind} {@link Kind}
                     */
                    getKind() {
                        return StatementOrBundle.Kind.PROV_BUNDLE;
                    }
                    equals$java_lang_Object$org_openprovenance_apache_commons_lang_builder_EqualsBuilder(object, equalsBuilder) {
                        if (!(object != null && object instanceof org.openprovenance.prov.vanilla.Bundle)) {
                            equalsBuilder.appendSuper(false);
                            return;
                        }
                        if (this === object) {
                            return;
                        }
                        const that = object;
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getId(), that.getId());
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getStatement(), that.getStatement());
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
                        if (!(object != null && object instanceof org.openprovenance.prov.vanilla.Bundle)) {
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
                        hashCodeBuilder.append$java_lang_Object(this.getStatement());
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
                            let theStatement;
                            theStatement = this.getStatement();
                            toStringBuilder.append$java_lang_String$java_lang_Object("id", this.getId());
                            toStringBuilder.append$java_lang_String$java_lang_Object("statement", theStatement);
                            const theNamespace = this.getNamespace();
                            toStringBuilder.append$java_lang_String$java_lang_Object("namespace", theNamespace);
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
                vanilla.Bundle = Bundle;
                Bundle["__class"] = "org.openprovenance.prov.vanilla.Bundle";
                Bundle["__interfaces"] = ["org.openprovenance.prov.model.Bundle", "org.openprovenance.apache.commons.lang.builder.HashCode", "org.openprovenance.apache.commons.lang.builder.Equals", "org.openprovenance.prov.model.Identifiable", "org.openprovenance.prov.model.StatementOrBundle", "org.openprovenance.apache.commons.lang.builder.ToString"];
            })(vanilla = prov.vanilla || (prov.vanilla = {}));
        })(prov = openprovenance.prov || (openprovenance.prov = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
