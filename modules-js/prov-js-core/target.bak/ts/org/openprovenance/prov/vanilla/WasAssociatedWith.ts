/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.vanilla {
    export class WasAssociatedWith implements org.openprovenance.prov.model.WasAssociatedWith, org.openprovenance.apache.commons.lang.builder.Equals, org.openprovenance.apache.commons.lang.builder.HashCode, org.openprovenance.apache.commons.lang.builder.ToString, org.openprovenance.prov.vanilla.HasAttributes {
        /*private*/ id: org.openprovenance.prov.model.QualifiedName;

        /*private*/ labels: Array<org.openprovenance.prov.model.LangString>;

        /*private*/ other: Array<org.openprovenance.prov.model.Other>;

        /*private*/ type: Array<org.openprovenance.prov.model.Type>;

        /*private*/ role: Array<org.openprovenance.prov.model.Role>;

        activity: org.openprovenance.prov.model.QualifiedName;

        agent: org.openprovenance.prov.model.QualifiedName;

        plan: org.openprovenance.prov.model.QualifiedName;

        static u: org.openprovenance.prov.vanilla.ProvUtilities; public static u_$LI$(): org.openprovenance.prov.vanilla.ProvUtilities { if (WasAssociatedWith.u == null) { WasAssociatedWith.u = new org.openprovenance.prov.vanilla.ProvUtilities(); }  return WasAssociatedWith.u; }

        public constructor(id?: any, activity?: any, agent?: any, plan?: any, attributes?: any) {
            if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((activity != null && (activity.constructor != null && activity.constructor["__interfaces"] != null && activity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || activity === null) && ((agent != null && (agent.constructor != null && agent.constructor["__interfaces"] != null && agent.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || agent === null) && ((plan != null && (plan.constructor != null && plan.constructor["__interfaces"] != null && plan.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || plan === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                let __args = arguments;
                if (this.activity === undefined) { this.activity = null; } 
                if (this.agent === undefined) { this.agent = null; } 
                this.id = null;
                this.labels = <any>([]);
                this.other = <any>([]);
                this.type = <any>([]);
                this.role = <any>([]);
                this.plan = null;
                this.setId(id);
                this.activity = activity;
                this.agent = agent;
                this.plan = plan;
                WasAssociatedWith.u_$LI$().populateAttributes(attributes, this.labels, <any>([]), this.type, this.role, this.other, null);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((activity != null && (activity.constructor != null && activity.constructor["__interfaces"] != null && activity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || activity === null) && ((agent != null && (agent.constructor != null && agent.constructor["__interfaces"] != null && agent.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || agent === null) && ((plan != null && (plan instanceof Array)) || plan === null) && attributes === undefined) {
                let __args = arguments;
                let attributes: any = __args[3];
                if (this.activity === undefined) { this.activity = null; } 
                if (this.agent === undefined) { this.agent = null; } 
                this.id = null;
                this.labels = <any>([]);
                this.other = <any>([]);
                this.type = <any>([]);
                this.role = <any>([]);
                this.plan = null;
                this.setId(id);
                this.activity = activity;
                this.agent = agent;
                WasAssociatedWith.u_$LI$().populateAttributes(attributes, this.labels, <any>([]), this.type, this.role, this.other, null);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((activity != null && (activity instanceof Array)) || activity === null) && agent === undefined && plan === undefined && attributes === undefined) {
                let __args = arguments;
                let attributes: any = __args[1];
                if (this.activity === undefined) { this.activity = null; } 
                if (this.agent === undefined) { this.agent = null; } 
                this.id = null;
                this.labels = <any>([]);
                this.other = <any>([]);
                this.type = <any>([]);
                this.role = <any>([]);
                this.plan = null;
                this.setId(id);
                WasAssociatedWith.u_$LI$().populateAttributes(attributes, this.labels, <any>([]), this.type, this.role, this.other, null);
            } else if (id === undefined && activity === undefined && agent === undefined && plan === undefined && attributes === undefined) {
                let __args = arguments;
                if (this.activity === undefined) { this.activity = null; } 
                if (this.agent === undefined) { this.agent = null; } 
                this.id = null;
                this.labels = <any>([]);
                this.other = <any>([]);
                this.type = <any>([]);
                this.role = <any>([]);
                this.plan = null;
            } else throw new Error('invalid overload');
        }

        /**
         * 
         * @param {*} aid
         */
        public setActivity(aid: org.openprovenance.prov.model.QualifiedName) {
            this.activity = aid;
        }

        /**
         * 
         * @param {*} agid
         */
        public setAgent(agid: org.openprovenance.prov.model.QualifiedName) {
            this.agent = agid;
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
        public getActivity(): org.openprovenance.prov.model.QualifiedName {
            return this.activity;
        }

        /**
         * Gets the value of the role property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore, any modification made to the
         * returned list will be present inside the object.
         * This is why there is not a <CODE>set</CODE> method for the role property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         * getRole().add(newItem);
         * </pre>
         * 
         * @return {*[]} a list of objects of type
         * {@link Role}
         */
        public getRole(): Array<org.openprovenance.prov.model.Role> {
            return this.role;
        }

        /**
         * 
         * @return {*}
         */
        public getPlan(): org.openprovenance.prov.model.QualifiedName {
            return /* orElse */((v, v2) => v == null ? v2 : v)(this.plan,null);
        }

        /**
         * Set Plan
         * 
         * @param {*} plan
         */
        public setPlan(plan: org.openprovenance.prov.model.QualifiedName) {
            this.plan = plan;
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
            return StatementOrBundle.Kind.PROV_ASSOCIATION;
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
            if (!(object != null && object instanceof <any>org.openprovenance.prov.vanilla.WasAssociatedWith)){
                equalsBuilder.appendSuper(false);
                return;
            }
            if (this === object){
                return;
            }
            const that: WasAssociatedWith = (<WasAssociatedWith>object);
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getId(), that.getId());
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getActivity(), that.getActivity());
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getAgent(), that.getAgent());
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getPlan(), that.getPlan());
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
            if (!(object != null && object instanceof <any>org.openprovenance.prov.vanilla.WasAssociatedWith)){
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
            hashCodeBuilder.append$java_lang_Object(this.getActivity());
            hashCodeBuilder.append$java_lang_Object(this.getAgent());
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
                let theActivity: org.openprovenance.prov.model.QualifiedName;
                theActivity = this.getActivity();
                toStringBuilder.append$java_lang_String$java_lang_Object("activity", theActivity);
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
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(result, this.getRole());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(result, this.getOther().stream().map<any>((o) => <org.openprovenance.prov.model.Attribute><any>o).collect<any, any>(java.util.stream.Collectors.toList<any>()));
            return result;
        }

        public setIndexedAttributes(qn: any, attributes: Array<org.openprovenance.prov.model.Attribute>) {
            const values: Array<org.openprovenance.prov.model.Value> = <any>([]);
            const locations: Array<org.openprovenance.prov.model.Location> = <any>([]);
            WasAssociatedWith.u_$LI$().distribute(<org.openprovenance.prov.model.QualifiedName><any>qn, attributes, this.getLabel(), values, locations, this.getType(), this.getRole(), this.getOther());
        }

        /**
         * 
         * @return {*}
         */
        public getIndexedAttributes(): any {
            return WasAssociatedWith.u_$LI$().split$java_util_Collection(this.getAttributes());
        }
    }
    WasAssociatedWith["__class"] = "org.openprovenance.prov.vanilla.WasAssociatedWith";
    WasAssociatedWith["__interfaces"] = ["org.openprovenance.prov.model.HasOther","org.openprovenance.prov.model.QualifiedRelation","org.openprovenance.prov.model.WasAssociatedWith","org.openprovenance.prov.model.HasType","org.openprovenance.prov.model.StatementOrBundle","org.openprovenance.apache.commons.lang.builder.ToString","org.openprovenance.prov.model.Statement","org.openprovenance.prov.vanilla.HasAttributes","org.openprovenance.apache.commons.lang.builder.HashCode","org.openprovenance.prov.model.Influence","org.openprovenance.prov.model.HasLabel","org.openprovenance.prov.model.HasRole","org.openprovenance.apache.commons.lang.builder.Equals","org.openprovenance.prov.model.Identifiable","org.openprovenance.prov.model.Relation"];


}

