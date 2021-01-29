/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.vanilla {
    export class Bundle implements org.openprovenance.prov.model.Bundle, org.openprovenance.apache.commons.lang.builder.Equals, org.openprovenance.apache.commons.lang.builder.ToString, org.openprovenance.apache.commons.lang.builder.HashCode {
        /*private*/ namespaces: org.openprovenance.prov.model.Namespace;

        /*private*/ statements: Array<org.openprovenance.prov.model.Statement>;

        /*private*/ id: org.openprovenance.prov.model.QualifiedName;

        public constructor(id?: any, namespace?: any, statements?: any) {
            if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((namespace != null && namespace instanceof <any>org.openprovenance.prov.model.Namespace) || namespace === null) && ((statements != null && (statements instanceof Array)) || statements === null)) {
                let __args = arguments;
                if (this.id === undefined) { this.id = null; } 
                this.namespaces = new org.openprovenance.prov.model.Namespace();
                this.statements = <any>([]);
                this.id = id;
                if (namespace != null){
                    this.namespaces = namespace;
                } else {
                    this.namespaces = new org.openprovenance.prov.model.Namespace();
                }
                if (statements != null)/* addAll */((l1, l2) => l1.push.apply(l1, l2))(this.statements, statements);
            } else if (id === undefined && namespace === undefined && statements === undefined) {
                let __args = arguments;
                if (this.id === undefined) { this.id = null; } 
                this.namespaces = new org.openprovenance.prov.model.Namespace();
                this.statements = <any>([]);
            } else throw new Error('invalid overload');
        }

        /**
         * 
         * @return {*[]}
         */
        public getStatement(): Array<org.openprovenance.prov.model.Statement> {
            return this.statements;
        }

        /**
         * 
         * @param {org.openprovenance.prov.model.Namespace} namespaces
         */
        public setNamespace(namespaces: org.openprovenance.prov.model.Namespace) {
            if (namespaces != null)this.namespaces = namespaces;
        }

        /**
         * 
         * @return {org.openprovenance.prov.model.Namespace}
         */
        public getNamespace(): org.openprovenance.prov.model.Namespace {
            return this.namespaces;
        }

        /**
         * Gets the value of the id property.  A null value means that the object has not been identified.  {@link Entity}, {@link Activity},
         * {@link Agent} have a non-null identifier.
         * 
         * @return {*} possible object is
         * {@link QualifiedName}
         */
        public getId(): org.openprovenance.prov.model.QualifiedName {
            return this.id;
        }

        /**
         * Sets the value of the id property.
         * 
         * @param {*} value allowed object is
         * {@link QualifiedName}
         */
        public setId(value: org.openprovenance.prov.model.QualifiedName) {
            this.id = value;
        }

        /**
         * Gets the type of a provenance statement
         * 
         * @return {org.openprovenance.prov.model.StatementOrBundle.Kind} {@link Kind}
         */
        public getKind(): StatementOrBundle.Kind {
            return StatementOrBundle.Kind.PROV_BUNDLE;
        }

        public equals$java_lang_Object$org_openprovenance_apache_commons_lang_builder_EqualsBuilder(object: any, equalsBuilder: org.openprovenance.apache.commons.lang.builder.EqualsBuilder) {
            if (!(object != null && object instanceof <any>org.openprovenance.prov.vanilla.Bundle)){
                equalsBuilder.appendSuper(false);
                return;
            }
            if (this === object){
                return;
            }
            const that: Bundle = (<Bundle>object);
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getId(), that.getId());
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getStatement(), that.getStatement());
        }

        public equals(object?: any, equalsBuilder?: any) {
            if (((object != null) || object === null) && ((equalsBuilder != null && equalsBuilder instanceof <any>org.openprovenance.apache.commons.lang.builder.EqualsBuilder) || equalsBuilder === null)) {
                return <any>this.equals$java_lang_Object$org_openprovenance_apache_commons_lang_builder_EqualsBuilder(object, equalsBuilder);
            } else if (((object != null) || object === null) && equalsBuilder === undefined) {
                return <any>this.equals$java_lang_Object(object);
            } else throw new Error('invalid overload');
        }

        public equals$java_lang_Object(object: any): boolean {
            if (!(object != null && object instanceof <any>org.openprovenance.prov.vanilla.Bundle)){
                return false;
            }
            if (this === object){
                return true;
            }
            const equalsBuilder: org.openprovenance.apache.commons.lang.builder.EqualsBuilder = new org.openprovenance.apache.commons.lang.builder.EqualsBuilder();
            this.equals$java_lang_Object$org_openprovenance_apache_commons_lang_builder_EqualsBuilder(object, equalsBuilder);
            return equalsBuilder.isEquals();
        }

        public hashCode$org_openprovenance_apache_commons_lang_builder_HashCodeBuilder(hashCodeBuilder: org.openprovenance.apache.commons.lang.builder.HashCodeBuilder) {
            hashCodeBuilder.append$java_lang_Object(this.getId());
            hashCodeBuilder.append$java_lang_Object(this.getStatement());
        }

        public hashCode(hashCodeBuilder?: any) {
            if (((hashCodeBuilder != null && hashCodeBuilder instanceof <any>org.openprovenance.apache.commons.lang.builder.HashCodeBuilder) || hashCodeBuilder === null)) {
                return <any>this.hashCode$org_openprovenance_apache_commons_lang_builder_HashCodeBuilder(hashCodeBuilder);
            } else if (hashCodeBuilder === undefined) {
                return <any>this.hashCode$();
            } else throw new Error('invalid overload');
        }

        public hashCode$(): number {
            const hashCodeBuilder: org.openprovenance.apache.commons.lang.builder.HashCodeBuilder = new org.openprovenance.apache.commons.lang.builder.HashCodeBuilder();
            this.hashCode$org_openprovenance_apache_commons_lang_builder_HashCodeBuilder(hashCodeBuilder);
            return hashCodeBuilder.toHashCode();
        }

        public toString$org_openprovenance_apache_commons_lang_builder_ToStringBuilder(toStringBuilder: org.openprovenance.apache.commons.lang.builder.ToStringBuilder) {
            {
                let theStatement: Array<org.openprovenance.prov.model.Statement>;
                theStatement = this.getStatement();
                toStringBuilder.append$java_lang_String$java_lang_Object("id", this.getId());
                toStringBuilder.append$java_lang_String$java_lang_Object("statement", theStatement);
                const theNamespace: org.openprovenance.prov.model.Namespace = this.getNamespace();
                toStringBuilder.append$java_lang_String$java_lang_Object("namespace", theNamespace);
            };
        }

        public toString(toStringBuilder?: any) {
            if (((toStringBuilder != null && toStringBuilder instanceof <any>org.openprovenance.apache.commons.lang.builder.ToStringBuilder) || toStringBuilder === null)) {
                return <any>this.toString$org_openprovenance_apache_commons_lang_builder_ToStringBuilder(toStringBuilder);
            } else if (toStringBuilder === undefined) {
                return <any>this.toString$();
            } else throw new Error('invalid overload');
        }

        public toString$(): string {
            const toStringBuilder: org.openprovenance.apache.commons.lang.builder.ToStringBuilder = new org.openprovenance.apache.commons.lang.builder.ToStringBuilder(this);
            this.toString$org_openprovenance_apache_commons_lang_builder_ToStringBuilder(toStringBuilder);
            return toStringBuilder.toString();
        }
    }
    Bundle["__class"] = "org.openprovenance.prov.vanilla.Bundle";
    Bundle["__interfaces"] = ["org.openprovenance.prov.model.Bundle","org.openprovenance.apache.commons.lang.builder.HashCode","org.openprovenance.apache.commons.lang.builder.Equals","org.openprovenance.prov.model.Identifiable","org.openprovenance.prov.model.StatementOrBundle","org.openprovenance.apache.commons.lang.builder.ToString"];


}

