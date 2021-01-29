/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.vanilla {
    export class SpecializationOf implements org.openprovenance.prov.model.SpecializationOf, org.openprovenance.apache.commons.lang.builder.Equals, org.openprovenance.apache.commons.lang.builder.HashCode, org.openprovenance.apache.commons.lang.builder.ToString {
        /*private*/ QUALIFIED_NAME_XSD_STRING: org.openprovenance.prov.model.QualifiedName;

        specificEntity: org.openprovenance.prov.model.QualifiedName;

        generalEntity: org.openprovenance.prov.model.QualifiedName;

        static u: org.openprovenance.prov.vanilla.ProvUtilities; public static u_$LI$(): org.openprovenance.prov.vanilla.ProvUtilities { if (SpecializationOf.u == null) { SpecializationOf.u = new org.openprovenance.prov.vanilla.ProvUtilities(); }  return SpecializationOf.u; }

        public constructor(specificEntity?: any, generalEntity?: any) {
            if (((specificEntity != null && (specificEntity.constructor != null && specificEntity.constructor["__interfaces"] != null && specificEntity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || specificEntity === null) && ((generalEntity != null && (generalEntity.constructor != null && generalEntity.constructor["__interfaces"] != null && generalEntity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || generalEntity === null)) {
                let __args = arguments;
                if (this.specificEntity === undefined) { this.specificEntity = null; } 
                if (this.generalEntity === undefined) { this.generalEntity = null; } 
                this.QUALIFIED_NAME_XSD_STRING = org.openprovenance.prov.vanilla.ProvFactory.getFactory().getName().XSD_STRING;
                this.specificEntity = specificEntity;
                this.generalEntity = generalEntity;
            } else if (specificEntity === undefined && generalEntity === undefined) {
                let __args = arguments;
                if (this.specificEntity === undefined) { this.specificEntity = null; } 
                if (this.generalEntity === undefined) { this.generalEntity = null; } 
                this.QUALIFIED_NAME_XSD_STRING = org.openprovenance.prov.vanilla.ProvFactory.getFactory().getName().XSD_STRING;
            } else throw new Error('invalid overload');
        }

        /**
         * 
         * @param {*} aid
         */
        public setSpecificEntity(aid: org.openprovenance.prov.model.QualifiedName) {
            this.specificEntity = aid;
        }

        /**
         * 
         * @param {*} eid
         */
        public setGeneralEntity(eid: org.openprovenance.prov.model.QualifiedName) {
            this.generalEntity = eid;
        }

        /**
         * 
         * @return {*}
         */
        public getGeneralEntity(): org.openprovenance.prov.model.QualifiedName {
            return this.generalEntity;
        }

        /**
         * 
         * @return {*}
         */
        public getSpecificEntity(): org.openprovenance.prov.model.QualifiedName {
            return this.specificEntity;
        }

        /**
         * 
         * @return {org.openprovenance.prov.model.StatementOrBundle.Kind}
         */
        public getKind(): StatementOrBundle.Kind {
            return StatementOrBundle.Kind.PROV_SPECIALIZATION;
        }

        public equals$java_lang_Object$org_openprovenance_apache_commons_lang_builder_EqualsBuilder(object: any, equalsBuilder: org.openprovenance.apache.commons.lang.builder.EqualsBuilder) {
            if (!(object != null && object instanceof <any>org.openprovenance.prov.vanilla.SpecializationOf)){
                equalsBuilder.appendSuper(false);
                return;
            }
            if (this === object){
                return;
            }
            const that: SpecializationOf = (<SpecializationOf>object);
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getSpecificEntity(), that.getSpecificEntity());
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getGeneralEntity(), that.getGeneralEntity());
        }

        public equals(object?: any, equalsBuilder?: any) {
            if (((object != null) || object === null) && ((equalsBuilder != null && equalsBuilder instanceof <any>org.openprovenance.apache.commons.lang.builder.EqualsBuilder) || equalsBuilder === null)) {
                return <any>this.equals$java_lang_Object$org_openprovenance_apache_commons_lang_builder_EqualsBuilder(object, equalsBuilder);
            } else if (((object != null) || object === null) && equalsBuilder === undefined) {
                return <any>this.equals$java_lang_Object(object);
            } else throw new Error('invalid overload');
        }

        public equals2(object: any, equalsBuilder: org.openprovenance.apache.commons.lang.builder.EqualsBuilder) {
            if (!(object != null && object instanceof <any>org.openprovenance.prov.vanilla.QualifiedSpecializationOf)){
                equalsBuilder.appendSuper(false);
                return;
            }
            if (this === object){
                return;
            }
            const that: org.openprovenance.prov.vanilla.QualifiedSpecializationOf = (<org.openprovenance.prov.vanilla.QualifiedSpecializationOf>object);
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getSpecificEntity(), that.getSpecificEntity());
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getGeneralEntity(), that.getGeneralEntity());
        }

        public equals$java_lang_Object(object: any): boolean {
            if (!(object != null && object instanceof <any>org.openprovenance.prov.vanilla.SpecializationOf)){
                if (object != null && object instanceof <any>org.openprovenance.prov.vanilla.QualifiedSpecializationOf){
                    const qspec: org.openprovenance.prov.vanilla.QualifiedSpecializationOf = <org.openprovenance.prov.vanilla.QualifiedSpecializationOf>object;
                    if (qspec.isUnqualified()){
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
            hashCodeBuilder.append$java_lang_Object(this.getSpecificEntity());
            hashCodeBuilder.append$java_lang_Object(this.getGeneralEntity());
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
                let theSpecificEntity: org.openprovenance.prov.model.QualifiedName;
                theSpecificEntity = this.getSpecificEntity();
                toStringBuilder.append$java_lang_String$java_lang_Object("specificEntity", theSpecificEntity);
            };
            {
                let theGeneralEntity: org.openprovenance.prov.model.QualifiedName;
                theGeneralEntity = this.getGeneralEntity();
                toStringBuilder.append$java_lang_String$java_lang_Object("generalEntity", theGeneralEntity);
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
    SpecializationOf["__class"] = "org.openprovenance.prov.vanilla.SpecializationOf";
    SpecializationOf["__interfaces"] = ["org.openprovenance.prov.model.UnqualifiedRelation","org.openprovenance.apache.commons.lang.builder.HashCode","org.openprovenance.prov.model.SpecializationOf","org.openprovenance.apache.commons.lang.builder.Equals","org.openprovenance.prov.model.Relation","org.openprovenance.prov.model.StatementOrBundle","org.openprovenance.apache.commons.lang.builder.ToString","org.openprovenance.prov.model.Statement"];


}

