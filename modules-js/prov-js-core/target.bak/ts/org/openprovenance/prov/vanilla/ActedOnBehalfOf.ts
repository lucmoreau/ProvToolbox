/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.vanilla {
    export class ActedOnBehalfOf implements org.openprovenance.prov.model.ActedOnBehalfOf, org.openprovenance.apache.commons.lang.builder.Equals, org.openprovenance.apache.commons.lang.builder.HashCode, org.openprovenance.apache.commons.lang.builder.ToString, org.openprovenance.prov.vanilla.HasAttributes {
        public static QUALIFIED_NAME_XSD_STRING: org.openprovenance.prov.model.QualifiedName; public static QUALIFIED_NAME_XSD_STRING_$LI$(): org.openprovenance.prov.model.QualifiedName { if (ActedOnBehalfOf.QUALIFIED_NAME_XSD_STRING == null) { ActedOnBehalfOf.QUALIFIED_NAME_XSD_STRING = org.openprovenance.prov.vanilla.ProvFactory.getFactory().getName().XSD_STRING; }  return ActedOnBehalfOf.QUALIFIED_NAME_XSD_STRING; }

        /*private*/ id: org.openprovenance.prov.model.QualifiedName;

        /*private*/ labels: Array<org.openprovenance.prov.model.LangString>;

        /*private*/ other: Array<org.openprovenance.prov.model.Other>;

        /*private*/ type: Array<org.openprovenance.prov.model.Type>;

        delegate: org.openprovenance.prov.model.QualifiedName;

        responsible: org.openprovenance.prov.model.QualifiedName;

        /*private*/ activity: org.openprovenance.prov.model.QualifiedName;

        static u: org.openprovenance.prov.vanilla.ProvUtilities; public static u_$LI$(): org.openprovenance.prov.vanilla.ProvUtilities { if (ActedOnBehalfOf.u == null) { ActedOnBehalfOf.u = new org.openprovenance.prov.vanilla.ProvUtilities(); }  return ActedOnBehalfOf.u; }

        public constructor(id?: any, delegate?: any, responsible?: any, activity?: any, attributes?: any) {
            if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((delegate != null && (delegate.constructor != null && delegate.constructor["__interfaces"] != null && delegate.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || delegate === null) && ((responsible != null && (responsible.constructor != null && responsible.constructor["__interfaces"] != null && responsible.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || responsible === null) && ((activity != null && (activity.constructor != null && activity.constructor["__interfaces"] != null && activity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || activity === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                let __args = arguments;
                if (this.delegate === undefined) { this.delegate = null; } 
                if (this.responsible === undefined) { this.responsible = null; } 
                this.id = null;
                this.labels = <any>([]);
                this.other = <any>([]);
                this.type = <any>([]);
                this.activity = null;
                this.setId(id);
                this.delegate = delegate;
                this.responsible = responsible;
                this.activity = activity;
                ActedOnBehalfOf.u_$LI$().populateAttributes(attributes, this.labels, java.util.Collections.EMPTY_LIST, this.type, java.util.Collections.EMPTY_LIST, this.other, null);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((delegate != null && (delegate instanceof Array)) || delegate === null) && responsible === undefined && activity === undefined && attributes === undefined) {
                let __args = arguments;
                let attributes: any = __args[1];
                if (this.delegate === undefined) { this.delegate = null; } 
                if (this.responsible === undefined) { this.responsible = null; } 
                this.id = null;
                this.labels = <any>([]);
                this.other = <any>([]);
                this.type = <any>([]);
                this.activity = null;
                this.setId(id);
                ActedOnBehalfOf.u_$LI$().populateAttributes(attributes, this.labels, java.util.Collections.EMPTY_LIST, this.type, java.util.Collections.EMPTY_LIST, this.other, null);
            } else if (id === undefined && delegate === undefined && responsible === undefined && activity === undefined && attributes === undefined) {
                let __args = arguments;
                if (this.delegate === undefined) { this.delegate = null; } 
                if (this.responsible === undefined) { this.responsible = null; } 
                this.id = null;
                this.labels = <any>([]);
                this.other = <any>([]);
                this.type = <any>([]);
                this.activity = null;
            } else throw new Error('invalid overload');
        }

        /**
         * 
         * @param {*} aid
         */
        public setDelegate(aid: org.openprovenance.prov.model.QualifiedName) {
            this.delegate = aid;
        }

        /**
         * 
         * @param {*} aid
         */
        public setResponsible(aid: org.openprovenance.prov.model.QualifiedName) {
            this.responsible = aid;
        }

        /**
         * 
         * @param {*} eid
         */
        public setActivity(eid: org.openprovenance.prov.model.QualifiedName) {
            this.activity = eid;
        }

        /**
         * 
         * @return {*}
         */
        public getDelegate(): org.openprovenance.prov.model.QualifiedName {
            return this.delegate;
        }

        /**
         * 
         * @return {*}
         */
        public getResponsible(): org.openprovenance.prov.model.QualifiedName {
            return this.responsible;
        }

        /**
         * 
         * @return {*}
         */
        public getActivity(): org.openprovenance.prov.model.QualifiedName {
            return /* orElse */((v, v2) => v == null ? v2 : v)(this.activity,null);
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
            return StatementOrBundle.Kind.PROV_DELEGATION;
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
         * @return {*[]}
         */
        public getLabel(): Array<org.openprovenance.prov.model.LangString> {
            return this.labels;
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
            if (!(object != null && object instanceof <any>org.openprovenance.prov.vanilla.ActedOnBehalfOf)){
                equalsBuilder.appendSuper(false);
                return;
            }
            if (this === object){
                return;
            }
            const that: ActedOnBehalfOf = (<ActedOnBehalfOf>object);
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getId(), that.getId());
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getActivity(), that.getActivity());
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getDelegate(), that.getDelegate());
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getResponsible(), that.getResponsible());
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
            if (!(object != null && object instanceof <any>org.openprovenance.prov.vanilla.ActedOnBehalfOf)){
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
            hashCodeBuilder.append$java_lang_Object(this.getResponsible());
            hashCodeBuilder.append$java_lang_Object(this.getDelegate());
            hashCodeBuilder.append$java_lang_Object(this.getActivity());
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
                let theId: org.openprovenance.prov.model.QualifiedName;
                theId = this.getId();
                toStringBuilder.append$java_lang_String$java_lang_Object("id", theId);
            };
            {
                let theDelegate: org.openprovenance.prov.model.QualifiedName;
                theDelegate = this.getDelegate();
                toStringBuilder.append$java_lang_String$java_lang_Object("delegate", theDelegate);
            };
            {
                let theResponisble: org.openprovenance.prov.model.QualifiedName;
                theResponisble = this.getResponsible();
                toStringBuilder.append$java_lang_String$java_lang_Object("responsible", theResponisble);
            };
            {
                let theActivity: org.openprovenance.prov.model.QualifiedName;
                theActivity = this.getActivity();
                toStringBuilder.append$java_lang_String$java_lang_Object("activity", theActivity);
            };
            {
                let theAttributes: any;
                theAttributes = this.getIndexedAttributes();
                toStringBuilder.append$java_lang_String$java_lang_Object("attributes", theAttributes);
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
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(result, this.getLabel().stream().map<any>((s) => new org.openprovenance.prov.vanilla.Label(ActedOnBehalfOf.QUALIFIED_NAME_XSD_STRING_$LI$(), s)).collect<any, any>(java.util.stream.Collectors.toList<any>()));
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(result, this.getType());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(result, this.getOther().stream().map<any>((o) => <org.openprovenance.prov.model.Attribute><any>o).collect<any, any>(java.util.stream.Collectors.toList<any>()));
            return result;
        }

        public setIndexedAttributes(qn: any, attributes: Array<org.openprovenance.prov.model.Attribute>) {
            ActedOnBehalfOf.u_$LI$().distribute(<org.openprovenance.prov.model.QualifiedName><any>qn, attributes, this.getLabel(), java.util.Collections.EMPTY_LIST, java.util.Collections.EMPTY_LIST, this.getType(), java.util.Collections.EMPTY_LIST, this.getOther());
        }

        /**
         * 
         * @return {*}
         */
        public getIndexedAttributes(): any {
            return ActedOnBehalfOf.u_$LI$().split$java_util_Collection(this.getAttributes());
        }
    }
    ActedOnBehalfOf["__class"] = "org.openprovenance.prov.vanilla.ActedOnBehalfOf";
    ActedOnBehalfOf["__interfaces"] = ["org.openprovenance.prov.model.HasOther","org.openprovenance.prov.model.QualifiedRelation","org.openprovenance.prov.model.HasType","org.openprovenance.prov.model.StatementOrBundle","org.openprovenance.apache.commons.lang.builder.ToString","org.openprovenance.prov.model.Statement","org.openprovenance.prov.vanilla.HasAttributes","org.openprovenance.apache.commons.lang.builder.HashCode","org.openprovenance.prov.model.Influence","org.openprovenance.prov.model.HasLabel","org.openprovenance.apache.commons.lang.builder.Equals","org.openprovenance.prov.model.ActedOnBehalfOf","org.openprovenance.prov.model.Identifiable","org.openprovenance.prov.model.Relation"];


}

