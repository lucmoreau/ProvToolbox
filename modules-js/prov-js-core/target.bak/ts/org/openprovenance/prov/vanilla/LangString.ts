/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.vanilla {
    export class LangString implements org.openprovenance.prov.model.LangString, org.openprovenance.apache.commons.lang.builder.Equals, org.openprovenance.apache.commons.lang.builder.HashCode, org.openprovenance.apache.commons.lang.builder.ToString {
        lang: string;

        value: string;

        public constructor(value?: any, lang?: any) {
            if (((typeof value === 'string') || value === null) && ((typeof lang === 'string') || lang === null)) {
                let __args = arguments;
                if (this.lang === undefined) { this.lang = null; } 
                if (this.value === undefined) { this.value = null; } 
                this.value = value;
                this.lang = lang;
            } else if (((typeof value === 'string') || value === null) && lang === undefined) {
                let __args = arguments;
                if (this.lang === undefined) { this.lang = null; } 
                if (this.value === undefined) { this.value = null; } 
                this.value = value;
                this.lang = null;
            } else if (value === undefined && lang === undefined) {
                let __args = arguments;
                if (this.lang === undefined) { this.lang = null; } 
                if (this.value === undefined) { this.value = null; } 
                this.lang = null;
            } else throw new Error('invalid overload');
        }

        /**
         * 
         * @return {string}
         */
        public getValue(): string {
            return this.value;
        }

        /**
         * 
         * @param {string} value
         */
        public setValue(value: string) {
            this.value = value;
        }

        /**
         * 
         * @return {string}
         */
        public getLang(): string {
            return /* orElse */((v, v2) => v == null ? v2 : v)(this.lang,null);
        }

        /**
         * 
         * @param {string} value
         */
        public setLang(value: string) {
            this.lang = value;
        }

        public equals$java_lang_Object$org_openprovenance_apache_commons_lang_builder_EqualsBuilder(object: any, equalsBuilder: org.openprovenance.apache.commons.lang.builder.EqualsBuilder) {
            if (!(object != null && object instanceof <any>org.openprovenance.prov.vanilla.LangString)){
                equalsBuilder.appendSuper(false);
                return;
            }
            if (this === object){
                return;
            }
            const that: LangString = (<LangString>object);
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getValue(), that.getValue());
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getLang(), that.getLang());
        }

        public equals(object?: any, equalsBuilder?: any) {
            if (((object != null) || object === null) && ((equalsBuilder != null && equalsBuilder instanceof <any>org.openprovenance.apache.commons.lang.builder.EqualsBuilder) || equalsBuilder === null)) {
                return <any>this.equals$java_lang_Object$org_openprovenance_apache_commons_lang_builder_EqualsBuilder(object, equalsBuilder);
            } else if (((object != null) || object === null) && equalsBuilder === undefined) {
                return <any>this.equals$java_lang_Object(object);
            } else throw new Error('invalid overload');
        }

        public equals$java_lang_Object(object: any): boolean {
            if (!(object != null && object instanceof <any>org.openprovenance.prov.vanilla.LangString)){
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
            hashCodeBuilder.append$java_lang_Object(this.getLang());
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
                let theValue: string;
                theValue = this.getValue();
                toStringBuilder.append$java_lang_String$java_lang_Object("value", theValue);
            };
            {
                let theLang: string;
                theLang = this.getLang();
                toStringBuilder.append$java_lang_String$java_lang_Object("lang", theLang);
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
    LangString["__class"] = "org.openprovenance.prov.vanilla.LangString";
    LangString["__interfaces"] = ["org.openprovenance.apache.commons.lang.builder.HashCode","org.openprovenance.apache.commons.lang.builder.Equals","org.openprovenance.apache.commons.lang.builder.ToString","org.openprovenance.prov.model.LangString"];


}

