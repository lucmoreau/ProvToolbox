/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.vanilla {
    export class Document implements org.openprovenance.prov.model.Document, org.openprovenance.apache.commons.lang.builder.Equals, org.openprovenance.apache.commons.lang.builder.ToString, org.openprovenance.apache.commons.lang.builder.HashCode {
        /*private*/ statementsOrBundle: Array<org.openprovenance.prov.model.StatementOrBundle>;

        /*private*/ namespace: org.openprovenance.prov.model.Namespace;

        public constructor(namespace?: any, statements?: any, bundles?: any) {
            if (((namespace != null && namespace instanceof <any>org.openprovenance.prov.model.Namespace) || namespace === null) && ((statements != null && (statements instanceof Array)) || statements === null) && ((bundles != null && (bundles instanceof Array)) || bundles === null)) {
                let __args = arguments;
                if (this.statementsOrBundle === undefined) { this.statementsOrBundle = null; } 
                this.namespace = new org.openprovenance.prov.model.Namespace();
                if (namespace != null)this.namespace = namespace;
                this.statementsOrBundle = <any>([]);
                if (statements != null)/* addAll */((l1, l2) => l1.push.apply(l1, l2))(this.statementsOrBundle, statements);
                if (bundles != null)/* addAll */((l1, l2) => l1.push.apply(l1, l2))(this.statementsOrBundle, bundles);
            } else if (((namespace != null && namespace instanceof <any>org.openprovenance.prov.model.Namespace) || namespace === null) && ((statements != null && (statements instanceof Array)) || statements === null) && bundles === undefined) {
                let __args = arguments;
                let statementsOrBundle: any = __args[1];
                if (this.statementsOrBundle === undefined) { this.statementsOrBundle = null; } 
                this.namespace = new org.openprovenance.prov.model.Namespace();
                if (namespace != null)this.namespace = namespace;
                this.statementsOrBundle = statementsOrBundle;
            } else if (((namespace != null && (namespace instanceof Array)) || namespace === null) && statements === undefined && bundles === undefined) {
                let __args = arguments;
                let statementsOrBundle: any = __args[0];
                if (this.statementsOrBundle === undefined) { this.statementsOrBundle = null; } 
                this.namespace = new org.openprovenance.prov.model.Namespace();
                this.statementsOrBundle = statementsOrBundle;
            } else if (namespace === undefined && statements === undefined && bundles === undefined) {
                let __args = arguments;
                if (this.statementsOrBundle === undefined) { this.statementsOrBundle = null; } 
                this.namespace = new org.openprovenance.prov.model.Namespace();
                this.statementsOrBundle = <any>([]);
            } else throw new Error('invalid overload');
        }

        /**
         * 
         * @return {org.openprovenance.prov.model.Namespace}
         */
        public getNamespace(): org.openprovenance.prov.model.Namespace {
            return this.namespace;
        }

        /**
         * 
         * @return {*[]}
         */
        public getStatementOrBundle(): Array<org.openprovenance.prov.model.StatementOrBundle> {
            return this.statementsOrBundle;
        }

        /**
         * 
         * @param {org.openprovenance.prov.model.Namespace} namespace
         */
        public setNamespace(namespace: org.openprovenance.prov.model.Namespace) {
            this.namespace = namespace;
        }

        public equals$java_lang_Object$org_openprovenance_apache_commons_lang_builder_EqualsBuilder(object: any, equalsBuilder: org.openprovenance.apache.commons.lang.builder.EqualsBuilder) {
            if (!(object != null && object instanceof <any>org.openprovenance.prov.vanilla.Document)){
                equalsBuilder.appendSuper(false);
                return;
            }
            if (this === object){
                return;
            }
            const that: Document = (<Document>object);
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getStatementOrBundle(), that.getStatementOrBundle());
        }

        public equals(object?: any, equalsBuilder?: any) {
            if (((object != null) || object === null) && ((equalsBuilder != null && equalsBuilder instanceof <any>org.openprovenance.apache.commons.lang.builder.EqualsBuilder) || equalsBuilder === null)) {
                return <any>this.equals$java_lang_Object$org_openprovenance_apache_commons_lang_builder_EqualsBuilder(object, equalsBuilder);
            } else if (((object != null) || object === null) && equalsBuilder === undefined) {
                return <any>this.equals$java_lang_Object(object);
            } else throw new Error('invalid overload');
        }

        public equals$java_lang_Object(object: any): boolean {
            if (!(object != null && object instanceof <any>org.openprovenance.prov.vanilla.Document)){
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
            hashCodeBuilder.append$java_lang_Object(this.getStatementOrBundle());
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
                let theStatementOrBundle: Array<org.openprovenance.prov.model.StatementOrBundle>;
                theStatementOrBundle = this.getStatementOrBundle();
                toStringBuilder.append$java_lang_String$java_lang_Object("statementOrBundle", theStatementOrBundle);
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
    Document["__class"] = "org.openprovenance.prov.vanilla.Document";
    Document["__interfaces"] = ["org.openprovenance.prov.model.Document","org.openprovenance.apache.commons.lang.builder.HashCode","org.openprovenance.apache.commons.lang.builder.Equals","org.openprovenance.apache.commons.lang.builder.ToString"];


}

