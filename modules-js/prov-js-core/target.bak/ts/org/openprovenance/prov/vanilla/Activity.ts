/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.vanilla {
    export class Activity implements org.openprovenance.prov.model.Activity, org.openprovenance.apache.commons.lang.builder.Equals, org.openprovenance.apache.commons.lang.builder.HashCode, org.openprovenance.apache.commons.lang.builder.ToString, org.openprovenance.prov.vanilla.HasAttributes {
        /*private*/ id: org.openprovenance.prov.model.QualifiedName;

        /*private*/ startTime: javax.xml.datatype.XMLGregorianCalendar;

        /*private*/ endTime: javax.xml.datatype.XMLGregorianCalendar;

        /*private*/ labels: Array<org.openprovenance.prov.model.LangString>;

        /*private*/ location: Array<org.openprovenance.prov.model.Location>;

        /*private*/ other: Array<org.openprovenance.prov.model.Other>;

        /*private*/ type: Array<org.openprovenance.prov.model.Type>;

        static u: org.openprovenance.prov.vanilla.ProvUtilities; public static u_$LI$(): org.openprovenance.prov.vanilla.ProvUtilities { if (Activity.u == null) { Activity.u = new org.openprovenance.prov.vanilla.ProvUtilities(); }  return Activity.u; }

        public constructor(id?: any, startTime?: any, endTime?: any, attributes?: any) {
            if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((startTime != null && startTime instanceof <any>javax.xml.datatype.XMLGregorianCalendar) || startTime === null) && ((endTime != null && endTime instanceof <any>javax.xml.datatype.XMLGregorianCalendar) || endTime === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                let __args = arguments;
                if (this.id === undefined) { this.id = null; } 
                this.startTime = null;
                this.endTime = null;
                this.labels = <any>([]);
                this.location = <any>([]);
                this.other = <any>([]);
                this.type = <any>([]);
                this.setId(id);
                this.setStartTime(startTime);
                this.setEndTime(endTime);
                Activity.u_$LI$().populateAttributes(attributes, this.labels, this.location, this.type, <any>([]), this.other, null);
            } else if (id === undefined && startTime === undefined && endTime === undefined && attributes === undefined) {
                let __args = arguments;
                if (this.id === undefined) { this.id = null; } 
                this.startTime = null;
                this.endTime = null;
                this.labels = <any>([]);
                this.location = <any>([]);
                this.other = <any>([]);
                this.type = <any>([]);
            } else throw new Error('invalid overload');
        }

        /**
         * 
         * @return {*}
         */
        public getId(): org.openprovenance.prov.model.QualifiedName {
            return /* orElse */((v, v2) => v == null ? v2 : v)(this.id,null);
        }

        /**
         * 
         * @return {org.openprovenance.prov.model.StatementOrBundle.Kind}
         */
        public getKind(): StatementOrBundle.Kind {
            return StatementOrBundle.Kind.PROV_ACTIVITY;
        }

        /**
         * 
         * @param {*} value
         */
        public setId(value: org.openprovenance.prov.model.QualifiedName) {
            this.id = value;
        }

        /**
         * 
         * @return {javax.xml.datatype.XMLGregorianCalendar}
         */
        public getStartTime(): javax.xml.datatype.XMLGregorianCalendar {
            return /* orElse */((v, v2) => v == null ? v2 : v)(this.startTime,null);
        }

        /**
         * 
         * @param {javax.xml.datatype.XMLGregorianCalendar} value
         */
        public setStartTime(value: javax.xml.datatype.XMLGregorianCalendar) {
            this.startTime = value;
        }

        /**
         * 
         * @return {javax.xml.datatype.XMLGregorianCalendar}
         */
        public getEndTime(): javax.xml.datatype.XMLGregorianCalendar {
            return /* orElse */((v, v2) => v == null ? v2 : v)(this.endTime,null);
        }

        /**
         * 
         * @param {javax.xml.datatype.XMLGregorianCalendar} value
         */
        public setEndTime(value: javax.xml.datatype.XMLGregorianCalendar) {
            this.endTime = value;
        }

        /**
         * 
         * @return {*[]}
         */
        public getLabel(): Array<org.openprovenance.prov.model.LangString> {
            return this.labels;
        }

        /**
         * 
         * @return {*[]}
         */
        public getLocation(): Array<org.openprovenance.prov.model.Location> {
            return this.location;
        }

        /**
         * 
         * @return {*[]}
         */
        public getType(): Array<org.openprovenance.prov.model.Type> {
            return this.type;
        }

        /**
         * 
         * @return {*[]}
         */
        public getOther(): Array<org.openprovenance.prov.model.Other> {
            return this.other;
        }

        public equals$java_lang_Object$org_openprovenance_apache_commons_lang_builder_EqualsBuilder(object: any, equalsBuilder: org.openprovenance.apache.commons.lang.builder.EqualsBuilder) {
            if (!(object != null && object instanceof <any>org.openprovenance.prov.vanilla.Activity)){
                equalsBuilder.appendSuper(false);
                return;
            }
            if (this === object){
                return;
            }
            const that: Activity = (<Activity>object);
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getStartTime(), that.getStartTime());
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getEndTime(), that.getEndTime());
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getId(), that.getId());
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getIndexedAttributes(), that.getIndexedAttributes());
        }

        public equals(object?: any, equalsBuilder?: any) {
            if (((object != null) || object === null) && ((equalsBuilder != null && equalsBuilder instanceof <any>org.openprovenance.apache.commons.lang.builder.EqualsBuilder) || equalsBuilder === null)) {
                return <any>this.equals$java_lang_Object$org_openprovenance_apache_commons_lang_builder_EqualsBuilder(object, equalsBuilder);
            } else if (((object != null) || object === null) && equalsBuilder === undefined) {
                return <any>this.equals$java_lang_Object(object);
            } else throw new Error('invalid overload');
        }

        public equals$java_lang_Object(object: any): boolean {
            if (!(object != null && object instanceof <any>org.openprovenance.prov.vanilla.Activity)){
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
            hashCodeBuilder.append$java_lang_Object(this.getStartTime());
            hashCodeBuilder.append$java_lang_Object(this.getEndTime());
            hashCodeBuilder.append$java_lang_Object(this.getId());
            hashCodeBuilder.append$java_lang_Object(this.getIndexedAttributes());
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
                let theStartTime: javax.xml.datatype.XMLGregorianCalendar;
                theStartTime = this.getStartTime();
                toStringBuilder.append$java_lang_String$java_lang_Object("startTime", theStartTime);
            };
            {
                let theEndTime: javax.xml.datatype.XMLGregorianCalendar;
                theEndTime = this.getEndTime();
                toStringBuilder.append$java_lang_String$java_lang_Object("endTime", theEndTime);
            };
            {
                let theAttributes: any;
                theAttributes = this.getIndexedAttributes();
                toStringBuilder.append$java_lang_String$java_lang_Object("attributes", theAttributes);
            };
            {
                let theId: org.openprovenance.prov.model.QualifiedName;
                theId = this.getId();
                toStringBuilder.append$java_lang_String$java_lang_Object("id", theId);
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

        /**
         * 
         * @return {*[]}
         */
        public getAttributes(): Array<org.openprovenance.prov.model.Attribute> {
            const result: Array<org.openprovenance.prov.model.Attribute> = <any>([]);
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(result, this.getLabel().stream().map<any>((s) => new org.openprovenance.prov.vanilla.Label(org.openprovenance.prov.vanilla.ActedOnBehalfOf.QUALIFIED_NAME_XSD_STRING_$LI$(), s)).collect<any, any>(java.util.stream.Collectors.toList<any>()));
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(result, this.getType());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(result, this.getLocation());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(result, this.getOther().stream().map<any>((o) => <org.openprovenance.prov.model.Attribute><any>o).collect<any, any>(java.util.stream.Collectors.toList<any>()));
            return result;
        }

        public setIndexedAttributes(qn: any, attributes: Array<org.openprovenance.prov.model.Attribute>) {
            const values_discard: Array<org.openprovenance.prov.model.Value> = <any>([]);
            const roles_discard: Array<org.openprovenance.prov.model.Role> = <any>([]);
            Activity.u_$LI$().distribute(<org.openprovenance.prov.model.QualifiedName><any>qn, attributes, this.getLabel(), values_discard, this.getLocation(), this.getType(), roles_discard, this.getOther());
        }

        /**
         * 
         * @return {*}
         */
        public getIndexedAttributes(): any {
            return Activity.u_$LI$().split$java_util_Collection(this.getAttributes());
        }
    }
    Activity["__class"] = "org.openprovenance.prov.vanilla.Activity";
    Activity["__interfaces"] = ["org.openprovenance.prov.model.HasOther","org.openprovenance.prov.model.HasType","org.openprovenance.prov.model.HasLocation","org.openprovenance.prov.model.StatementOrBundle","org.openprovenance.apache.commons.lang.builder.ToString","org.openprovenance.prov.model.Activity","org.openprovenance.prov.model.Statement","org.openprovenance.prov.model.Element","org.openprovenance.prov.vanilla.HasAttributes","org.openprovenance.apache.commons.lang.builder.HashCode","org.openprovenance.prov.model.HasLabel","org.openprovenance.apache.commons.lang.builder.Equals","org.openprovenance.prov.model.Identifiable"];


}

