/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.vanilla {
    export class WasAttributedTo implements org.openprovenance.prov.model.WasAttributedTo, org.openprovenance.apache.commons.lang.builder.Equals, org.openprovenance.apache.commons.lang.builder.HashCode, org.openprovenance.apache.commons.lang.builder.ToString, org.openprovenance.prov.vanilla.HasAttributes {
        /*private*/ id: org.openprovenance.prov.model.QualifiedName;

        /*private*/ labels: Array<org.openprovenance.prov.model.LangString>;

        /*private*/ other: Array<org.openprovenance.prov.model.Other>;

        /*private*/ type: Array<org.openprovenance.prov.model.Type>;

        agent: org.openprovenance.prov.model.QualifiedName;

        entity: org.openprovenance.prov.model.QualifiedName;

        static u: org.openprovenance.prov.vanilla.ProvUtilities; public static u_$LI$(): org.openprovenance.prov.vanilla.ProvUtilities { if (WasAttributedTo.u == null) { WasAttributedTo.u = new org.openprovenance.prov.vanilla.ProvUtilities(); }  return WasAttributedTo.u; }

        public constructor(id?: any, entity?: any, agent?: any, attributes?: any) {
            if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((entity != null && (entity.constructor != null && entity.constructor["__interfaces"] != null && entity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || entity === null) && ((agent != null && (agent.constructor != null && agent.constructor["__interfaces"] != null && agent.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || agent === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                let __args = arguments;
                if (this.agent === undefined) { this.agent = null; } 
                if (this.entity === undefined) { this.entity = null; } 
                this.id = null;
                this.labels = <any>([]);
                this.other = <any>([]);
                this.type = <any>([]);
                this.setId(id);
                this.agent = agent;
                this.entity = entity;
                WasAttributedTo.u_$LI$().populateAttributes(attributes, this.labels, java.util.Collections.EMPTY_LIST, this.type, java.util.Collections.EMPTY_LIST, this.other, null);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((entity != null && (entity instanceof Array)) || entity === null) && agent === undefined && attributes === undefined) {
                let __args = arguments;
                let attributes: any = __args[1];
                if (this.agent === undefined) { this.agent = null; } 
                if (this.entity === undefined) { this.entity = null; } 
                this.id = null;
                this.labels = <any>([]);
                this.other = <any>([]);
                this.type = <any>([]);
                this.setId(id);
                WasAttributedTo.u_$LI$().populateAttributes(attributes, this.labels, java.util.Collections.EMPTY_LIST, this.type, java.util.Collections.EMPTY_LIST, this.other, null);
            } else if (id === undefined && entity === undefined && agent === undefined && attributes === undefined) {
                let __args = arguments;
                if (this.agent === undefined) { this.agent = null; } 
                if (this.entity === undefined) { this.entity = null; } 
                this.id = null;
                this.labels = <any>([]);
                this.other = <any>([]);
                this.type = <any>([]);
            } else throw new Error('invalid overload');
        }

        /**
         * 
         * @param {*} aid
         */
        public setAgent(aid: org.openprovenance.prov.model.QualifiedName) {
            this.agent = aid;
        }

        /**
         * 
         * @param {*} eid
         */
        public setEntity(eid: org.openprovenance.prov.model.QualifiedName) {
            this.entity = eid;
        }

        /**
         * 
         * @return {*}
         */
        public getEntity(): org.openprovenance.prov.model.QualifiedName {
            return this.entity;
        }

        /**
         * 
         * @return {*}
         */
        public getAgent(): org.openprovenance.prov.model.QualifiedName {
            return this.agent;
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
            return StatementOrBundle.Kind.PROV_ATTRIBUTION;
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
            if (!(object != null && object instanceof <any>org.openprovenance.prov.vanilla.WasAttributedTo)){
                equalsBuilder.appendSuper(false);
                return;
            }
            if (this === object){
                return;
            }
            const that: WasAttributedTo = (<WasAttributedTo>object);
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getId(), that.getId());
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getAgent(), that.getAgent());
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getEntity(), that.getEntity());
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
            if (!(object != null && object instanceof <any>org.openprovenance.prov.vanilla.WasAttributedTo)){
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
            hashCodeBuilder.append$java_lang_Object(this.getAgent());
            hashCodeBuilder.append$java_lang_Object(this.getEntity());
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
                let theEntity: org.openprovenance.prov.model.QualifiedName;
                theEntity = this.getEntity();
                toStringBuilder.append$java_lang_String$java_lang_Object("entity", theEntity);
            };
            {
                let theAgent: org.openprovenance.prov.model.QualifiedName;
                theAgent = this.getAgent();
                toStringBuilder.append$java_lang_String$java_lang_Object("agent", theAgent);
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
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(result, this.getLabel().stream().map<any>((s) => new org.openprovenance.prov.vanilla.Label(org.openprovenance.prov.vanilla.ActedOnBehalfOf.QUALIFIED_NAME_XSD_STRING_$LI$(), s)).collect<any, any>(java.util.stream.Collectors.toList<any>()));
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(result, this.getType());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(result, this.getOther().stream().map<any>((o) => <org.openprovenance.prov.model.Attribute><any>o).collect<any, any>(java.util.stream.Collectors.toList<any>()));
            return result;
        }

        public setIndexedAttributes(qn: any, attributes: Array<org.openprovenance.prov.model.Attribute>) {
            WasAttributedTo.u_$LI$().distribute(<org.openprovenance.prov.model.QualifiedName><any>qn, attributes, this.getLabel(), java.util.Collections.EMPTY_LIST, java.util.Collections.EMPTY_LIST, this.getType(), java.util.Collections.EMPTY_LIST, this.getOther());
        }

        /**
         * 
         * @return {*}
         */
        public getIndexedAttributes(): any {
            return WasAttributedTo.u_$LI$().split$java_util_Collection(this.getAttributes());
        }
    }
    WasAttributedTo["__class"] = "org.openprovenance.prov.vanilla.WasAttributedTo";
    WasAttributedTo["__interfaces"] = ["org.openprovenance.prov.model.HasOther","org.openprovenance.prov.model.QualifiedRelation","org.openprovenance.prov.model.HasType","org.openprovenance.prov.model.StatementOrBundle","org.openprovenance.apache.commons.lang.builder.ToString","org.openprovenance.prov.model.Statement","org.openprovenance.prov.vanilla.HasAttributes","org.openprovenance.apache.commons.lang.builder.HashCode","org.openprovenance.prov.model.Influence","org.openprovenance.prov.model.HasLabel","org.openprovenance.apache.commons.lang.builder.Equals","org.openprovenance.prov.model.Identifiable","org.openprovenance.prov.model.Relation","org.openprovenance.prov.model.WasAttributedTo"];


}

