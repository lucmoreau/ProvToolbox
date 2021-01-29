/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var org;
(function (org) {
    var openprovenance;
    (function (openprovenance) {
        var prov;
        (function (prov) {
            var vanilla;
            (function (vanilla) {
                class Document {
                    constructor(namespace, statements, bundles) {
                        if (((namespace != null && namespace instanceof org.openprovenance.prov.model.Namespace) || namespace === null) && ((statements != null && (statements instanceof Array)) || statements === null) && ((bundles != null && (bundles instanceof Array)) || bundles === null)) {
                            let __args = arguments;
                            if (this.statementsOrBundle === undefined) {
                                this.statementsOrBundle = null;
                            }
                            this.namespace = new org.openprovenance.prov.model.Namespace();
                            if (namespace != null)
                                this.namespace = namespace;
                            this.statementsOrBundle = ([]);
                            if (statements != null) /* addAll */
                                ((l1, l2) => l1.push.apply(l1, l2))(this.statementsOrBundle, statements);
                            if (bundles != null) /* addAll */
                                ((l1, l2) => l1.push.apply(l1, l2))(this.statementsOrBundle, bundles);
                        }
                        else if (((namespace != null && namespace instanceof org.openprovenance.prov.model.Namespace) || namespace === null) && ((statements != null && (statements instanceof Array)) || statements === null) && bundles === undefined) {
                            let __args = arguments;
                            let statementsOrBundle = __args[1];
                            if (this.statementsOrBundle === undefined) {
                                this.statementsOrBundle = null;
                            }
                            this.namespace = new org.openprovenance.prov.model.Namespace();
                            if (namespace != null)
                                this.namespace = namespace;
                            this.statementsOrBundle = statementsOrBundle;
                        }
                        else if (((namespace != null && (namespace instanceof Array)) || namespace === null) && statements === undefined && bundles === undefined) {
                            let __args = arguments;
                            let statementsOrBundle = __args[0];
                            if (this.statementsOrBundle === undefined) {
                                this.statementsOrBundle = null;
                            }
                            this.namespace = new org.openprovenance.prov.model.Namespace();
                            this.statementsOrBundle = statementsOrBundle;
                        }
                        else if (namespace === undefined && statements === undefined && bundles === undefined) {
                            let __args = arguments;
                            if (this.statementsOrBundle === undefined) {
                                this.statementsOrBundle = null;
                            }
                            this.namespace = new org.openprovenance.prov.model.Namespace();
                            this.statementsOrBundle = ([]);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    /**
                     *
                     * @return {org.openprovenance.prov.model.Namespace}
                     */
                    getNamespace() {
                        return this.namespace;
                    }
                    /**
                     *
                     * @return {*[]}
                     */
                    getStatementOrBundle() {
                        return this.statementsOrBundle;
                    }
                    /**
                     *
                     * @param {org.openprovenance.prov.model.Namespace} namespace
                     */
                    setNamespace(namespace) {
                        this.namespace = namespace;
                    }
                    equals$java_lang_Object$org_openprovenance_apache_commons_lang_builder_EqualsBuilder(object, equalsBuilder) {
                        if (!(object != null && object instanceof org.openprovenance.prov.vanilla.Document)) {
                            equalsBuilder.appendSuper(false);
                            return;
                        }
                        if (this === object) {
                            return;
                        }
                        const that = object;
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getStatementOrBundle(), that.getStatementOrBundle());
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
                        if (!(object != null && object instanceof org.openprovenance.prov.vanilla.Document)) {
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
                        hashCodeBuilder.append$java_lang_Object(this.getStatementOrBundle());
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
                            let theStatementOrBundle;
                            theStatementOrBundle = this.getStatementOrBundle();
                            toStringBuilder.append$java_lang_String$java_lang_Object("statementOrBundle", theStatementOrBundle);
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
                vanilla.Document = Document;
                Document["__class"] = "org.openprovenance.prov.vanilla.Document";
                Document["__interfaces"] = ["org.openprovenance.prov.model.Document", "org.openprovenance.apache.commons.lang.builder.HashCode", "org.openprovenance.apache.commons.lang.builder.Equals", "org.openprovenance.apache.commons.lang.builder.ToString"];
            })(vanilla = prov.vanilla || (prov.vanilla = {}));
        })(prov = openprovenance.prov || (openprovenance.prov = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
