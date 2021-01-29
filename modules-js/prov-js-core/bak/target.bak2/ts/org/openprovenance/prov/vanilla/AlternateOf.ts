/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.vanilla {
    export class AlternateOf implements org.openprovenance.prov.model.AlternateOf, org.openprovenance.apache.commons.lang.builder.Equals, org.openprovenance.apache.commons.lang.builder.HashCode, org.openprovenance.apache.commons.lang.builder.ToString {
        alternate1: org.openprovenance.prov.model.QualifiedName;

        alternate2: org.openprovenance.prov.model.QualifiedName;

        public constructor(alternate1?: any, alternate2?: any) {
            if (((alternate1 != null && (alternate1.constructor != null && alternate1.constructor["__interfaces"] != null && alternate1.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || alternate1 === null) && ((alternate2 != null && (alternate2.constructor != null && alternate2.constructor["__interfaces"] != null && alternate2.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || alternate2 === null)) {
                let __args = arguments;
                if (this.alternate1 === undefined) { this.alternate1 = null; } 
                if (this.alternate2 === undefined) { this.alternate2 = null; } 
                this.alternate1 = alternate1;
                this.alternate2 = alternate2;
            } else if (alternate1 === undefined && alternate2 === undefined) {
                let __args = arguments;
                if (this.alternate1 === undefined) { this.alternate1 = null; } 
                if (this.alternate2 === undefined) { this.alternate2 = null; } 
            } else throw new Error('invalid overload');
        }

        /**
         * 
         * @param {*} alternate1
         */
        public setAlternate1(alternate1: org.openprovenance.prov.model.QualifiedName) {
            this.alternate1 = alternate1;
        }

        /**
         * 
         * @param {*} alternate2
         */
        public setAlternate2(alternate2: org.openprovenance.prov.model.QualifiedName) {
            this.alternate2 = alternate2;
        }

        /**
         * 
         * @return {*}
         */
        public getAlternate2(): org.openprovenance.prov.model.QualifiedName {
            return this.alternate2;
        }

        /**
         * 
         * @return {*}
         */
        public getAlternate1(): org.openprovenance.prov.model.QualifiedName {
            return this.alternate1;
        }

        /**
         * 
         * @return {org.openprovenance.prov.model.StatementOrBundle.Kind}
         */
        public getKind(): StatementOrBundle.Kind {
            return StatementOrBundle.Kind.PROV_ALTERNATE;
        }

        public equals$java_lang_Object$org_openprovenance_apache_commons_lang_builder_EqualsBuilder(object: any, equalsBuilder: org.openprovenance.apache.commons.lang.builder.EqualsBuilder) {
            if (!(object != null && object instanceof <any>org.openprovenance.prov.vanilla.AlternateOf)){
                equalsBuilder.appendSuper(false);
                return;
            }
            if (this === object){
                return;
            }
            const that: AlternateOf = (<AlternateOf>object);
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getAlternate1(), that.getAlternate1());
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getAlternate2(), that.getAlternate2());
        }

        public equals(object?: any, equalsBuilder?: any) {
            if (((object != null) || object === null) && ((equalsBuilder != null && equalsBuilder instanceof <any>org.openprovenance.apache.commons.lang.builder.EqualsBuilder) || equalsBuilder === null)) {
                return <any>this.equals$java_lang_Object$org_openprovenance_apache_commons_lang_builder_EqualsBuilder(object, equalsBuilder);
            } else if (((object != null) || object === null) && equalsBuilder === undefined) {
                return <any>this.equals$java_lang_Object(object);
            } else throw new Error('invalid overload');
        }

        public equals2(object: any, equalsBuilder: org.openprovenance.apache.commons.lang.builder.EqualsBuilder) {
            if (!(object != null && object instanceof <any>org.openprovenance.prov.vanilla.QualifiedAlternateOf)){
                equalsBuilder.appendSuper(false);
                return;
            }
            if (this === object){
                return;
            }
            const that: org.openprovenance.prov.vanilla.QualifiedAlternateOf = (<org.openprovenance.prov.vanilla.QualifiedAlternateOf>object);
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getAlternate1(), that.getAlternate1());
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getAlternate2(), that.getAlternate2());
        }

        public equals$java_lang_Object(object: any): boolean {
            if (!(object != null && object instanceof <any>org.openprovenance.prov.vanilla.AlternateOf)){
                if (object != null && object instanceof <any>org.openprovenance.prov.vanilla.QualifiedAlternateOf){
                    const qalt: org.openprovenance.prov.vanilla.QualifiedAlternateOf = <org.openprovenance.prov.vanilla.QualifiedAlternateOf>object;
                    if (qalt.isUnqualified()){
                        const equalsBuilder2: org.openprovenance.apache.commons.lang.builder.EqualsBuilder = new org.openprovenance.apache.commons.lang.builder.EqualsBuilder();
                        this.equals2(object, equalsBuilder2);
                        return equalsBuilder2.isEquals();
                    }
                    return false;
                }
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
            hashCodeBuilder.append$java_lang_Object(this.getAlternate1());
            hashCodeBuilder.append$java_lang_Object(this.getAlternate2());
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
                let theEntity: org.openprovenance.prov.model.QualifiedName;
                theEntity = this.getAlternate2();
                toStringBuilder.append$java_lang_String$java_lang_Object("alternate2", theEntity);
            };
            {
                let theAgent: org.openprovenance.prov.model.QualifiedName;
                theAgent = this.getAlternate1();
                toStringBuilder.append$java_lang_String$java_lang_Object("alternate1", theAgent);
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
    AlternateOf["__class"] = "org.openprovenance.prov.vanilla.AlternateOf";
    AlternateOf["__interfaces"] = ["org.openprovenance.prov.model.UnqualifiedRelation","org.openprovenance.apache.commons.lang.builder.HashCode","org.openprovenance.prov.model.AlternateOf","org.openprovenance.apache.commons.lang.builder.Equals","org.openprovenance.prov.model.Relation","org.openprovenance.prov.model.StatementOrBundle","org.openprovenance.apache.commons.lang.builder.ToString","org.openprovenance.prov.model.Statement"];


}

