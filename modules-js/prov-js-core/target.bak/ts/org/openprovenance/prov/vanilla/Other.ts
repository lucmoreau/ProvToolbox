/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.vanilla {
    export class Other extends org.openprovenance.prov.vanilla.TypedValue implements org.openprovenance.prov.model.Other, org.openprovenance.prov.model.Attribute {
        static PROV_OTHER_KIND: Attribute.AttributeKind; public static PROV_OTHER_KIND_$LI$(): Attribute.AttributeKind { if (Other.PROV_OTHER_KIND == null) { Other.PROV_OTHER_KIND = Attribute.AttributeKind.OTHER; }  return Other.PROV_OTHER_KIND; }

        elementName: org.openprovenance.prov.model.QualifiedName;

        /**
         * 
         * @return {*}
         */
        public getElementName(): org.openprovenance.prov.model.QualifiedName {
            return this.elementName;
        }

        /**
         * 
         * @return {org.openprovenance.prov.model.Attribute.AttributeKind}
         */
        public getKind(): Attribute.AttributeKind {
            return Other.PROV_OTHER_KIND_$LI$();
        }

        /**
         * 
         * @param {*} elementName
         */
        public setElementName(elementName: org.openprovenance.prov.model.QualifiedName) {
            this.elementName = elementName;
        }

        /**
         * 
         * @return {string}
         */
        public toNotationString(): string {
            return org.openprovenance.prov.model.DOMProcessing.qualifiedNameToString$org_openprovenance_prov_model_QualifiedName(this.getElementName()) + " = " + org.openprovenance.prov.model.ProvUtilities.valueToNotationString$java_lang_Object$org_openprovenance_prov_model_QualifiedName(this.getValue(), this.getType());
        }

        public constructor(elementName?: any, type?: any, value?: any) {
            if (((elementName != null && (elementName.constructor != null && elementName.constructor["__interfaces"] != null && elementName.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || elementName === null) && ((type != null && (type.constructor != null && type.constructor["__interfaces"] != null && type.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || type === null) && ((value != null) || value === null)) {
                let __args = arguments;
                super(type, TypedValue.castToStringOrLangStringOrQualifiedName(value, type));
                if (this.elementName === undefined) { this.elementName = null; } 
                this.elementName = elementName;
            } else if (elementName === undefined && type === undefined && value === undefined) {
                let __args = arguments;
                super();
                if (this.elementName === undefined) { this.elementName = null; } 
            } else throw new Error('invalid overload');
        }

        public equals$java_lang_Object$org_openprovenance_apache_commons_lang_builder_EqualsBuilder(object: any, equalsBuilder: org.openprovenance.apache.commons.lang.builder.EqualsBuilder) {
            if (!(object != null && object instanceof <any>org.openprovenance.prov.vanilla.Other)){
                equalsBuilder.appendSuper(false);
                return;
            }
            if (this === object){
                return;
            }
            const that: Other = (<Other>object);
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getValue(), that.getValue());
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getType(), that.getType());
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getElementName(), that.getElementName());
        }

        public equals(object?: any, equalsBuilder?: any) {
            if (((object != null) || object === null) && ((equalsBuilder != null && equalsBuilder instanceof <any>org.openprovenance.apache.commons.lang.builder.EqualsBuilder) || equalsBuilder === null)) {
                return <any>this.equals$java_lang_Object$org_openprovenance_apache_commons_lang_builder_EqualsBuilder(object, equalsBuilder);
            } else if (((object != null) || object === null) && equalsBuilder === undefined) {
                return <any>this.equals$java_lang_Object(object);
            } else throw new Error('invalid overload');
        }

        public equals$java_lang_Object(object: any): boolean {
            if (!(object != null && (object.constructor != null && object.constructor["__interfaces"] != null && object.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Other") >= 0))){
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
            hashCodeBuilder.append$java_lang_Object(this.getValue());
            hashCodeBuilder.append$java_lang_Object(this.getType());
            hashCodeBuilder.append$java_lang_Object(this.getElementName());
        }

        public hashCode(hashCodeBuilder?: any) {
            if (((hashCodeBuilder != null && hashCodeBuilder instanceof <any>org.openprovenance.apache.commons.lang.builder.HashCodeBuilder) || hashCodeBuilder === null)) {
                return <any>this.hashCode$org_openprovenance_apache_commons_lang_builder_HashCodeBuilder(hashCodeBuilder);
            } else if (hashCodeBuilder === undefined) {
                return <any>this.hashCode$();
            } else throw new Error('invalid overload');
        }

        public toString$org_openprovenance_apache_commons_lang_builder_ToStringBuilder(toStringBuilder: org.openprovenance.apache.commons.lang.builder.ToStringBuilder) {
            {
                let theValue: any;
                theValue = this.getValue();
                toStringBuilder.append$java_lang_String$java_lang_Object("name", this.getElementName());
                toStringBuilder.append$java_lang_String$java_lang_Object("value", theValue);
                toStringBuilder.append$java_lang_String$java_lang_Object("DEBUG_type1", (<any>theValue.constructor));
            };
            {
                let theType: org.openprovenance.prov.model.QualifiedName;
                theType = this.getType();
                toStringBuilder.append$java_lang_String$java_lang_Object("type", theType);
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
    Other["__class"] = "org.openprovenance.prov.vanilla.Other";
    Other["__interfaces"] = ["org.openprovenance.prov.model.Other","org.openprovenance.prov.model.TypedValue","org.openprovenance.apache.commons.lang.builder.HashCode","org.openprovenance.apache.commons.lang.builder.Equals","org.openprovenance.prov.model.Attribute","org.openprovenance.apache.commons.lang.builder.ToString"];


}

