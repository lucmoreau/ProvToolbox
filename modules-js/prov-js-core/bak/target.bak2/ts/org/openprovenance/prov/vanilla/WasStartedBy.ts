/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.vanilla {
    export class WasStartedBy implements org.openprovenance.prov.model.WasStartedBy, org.openprovenance.apache.commons.lang.builder.Equals, org.openprovenance.apache.commons.lang.builder.HashCode, org.openprovenance.apache.commons.lang.builder.ToString, org.openprovenance.prov.vanilla.HasAttributes {
        /*private*/ id: org.openprovenance.prov.model.QualifiedName;

        /*private*/ time: javax.xml.datatype.XMLGregorianCalendar;

        /*private*/ labels: Array<org.openprovenance.prov.model.LangString>;

        /*private*/ location: Array<org.openprovenance.prov.model.Location>;

        /*private*/ other: Array<org.openprovenance.prov.model.Other>;

        /*private*/ type: Array<org.openprovenance.prov.model.Type>;

        /*private*/ role: Array<org.openprovenance.prov.model.Role>;

        activity: org.openprovenance.prov.model.QualifiedName;

        trigger: org.openprovenance.prov.model.QualifiedName;

        starter: org.openprovenance.prov.model.QualifiedName;

        static u: org.openprovenance.prov.vanilla.ProvUtilities; public static u_$LI$(): org.openprovenance.prov.vanilla.ProvUtilities { if (WasStartedBy.u == null) { WasStartedBy.u = new org.openprovenance.prov.vanilla.ProvUtilities(); }  return WasStartedBy.u; }

        public constructor(id?: any, activity?: any, trigger?: any, starter?: any, time?: any, attributes?: any) {
            if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((activity != null && (activity.constructor != null && activity.constructor["__interfaces"] != null && activity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || activity === null) && ((trigger != null && (trigger.constructor != null && trigger.constructor["__interfaces"] != null && trigger.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || trigger === null) && ((starter != null && (starter.constructor != null && starter.constructor["__interfaces"] != null && starter.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || starter === null) && ((time != null && time instanceof <any>javax.xml.datatype.XMLGregorianCalendar) || time === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                let __args = arguments;
                if (this.activity === undefined) { this.activity = null; } 
                this.id = null;
                this.time = null;
                this.labels = <any>([]);
                this.location = <any>([]);
                this.other = <any>([]);
                this.type = <any>([]);
                this.role = <any>([]);
                this.trigger = null;
                this.starter = null;
                this.setId(id);
                this.activity = activity;
                this.trigger = trigger;
                this.starter = starter;
                this.time = time;
                WasStartedBy.u_$LI$().populateAttributes(attributes, this.labels, this.location, this.type, this.role, this.other, null);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((activity != null && (activity instanceof Array)) || activity === null) && trigger === undefined && starter === undefined && time === undefined && attributes === undefined) {
                let __args = arguments;
                let attributes: any = __args[1];
                if (this.activity === undefined) { this.activity = null; } 
                this.id = null;
                this.time = null;
                this.labels = <any>([]);
                this.location = <any>([]);
                this.other = <any>([]);
                this.type = <any>([]);
                this.role = <any>([]);
                this.trigger = null;
                this.starter = null;
                this.setId(id);
                this.time = null;
                WasStartedBy.u_$LI$().populateAttributes(attributes, this.labels, this.location, this.type, this.role, this.other, null);
            } else if (id === undefined && activity === undefined && trigger === undefined && starter === undefined && time === undefined && attributes === undefined) {
                let __args = arguments;
                if (this.activity === undefined) { this.activity = null; } 
                this.id = null;
                this.time = null;
                this.labels = <any>([]);
                this.location = <any>([]);
                this.other = <any>([]);
                this.type = <any>([]);
                this.role = <any>([]);
                this.trigger = null;
                this.starter = null;
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
         * @param {*} eid
         */
        public setTrigger(eid: org.openprovenance.prov.model.QualifiedName) {
            this.trigger = eid;
        }

        /**
         * 
         * @return {*}
         */
        public getTrigger(): org.openprovenance.prov.model.QualifiedName {
            return /* orElse */((v, v2) => v == null ? v2 : v)(this.trigger,null);
        }

        /**
         * 
         * @return {*}
         */
        public getActivity(): org.openprovenance.prov.model.QualifiedName {
            return this.activity;
        }

        /**
         * 
         * @param {*} aid
         */
        public setStarter(aid: org.openprovenance.prov.model.QualifiedName) {
            this.starter = aid;
        }

        /**
         * 
         * @return {*}
         */
        public getStarter(): org.openprovenance.prov.model.QualifiedName {
            return /* orElse */((v, v2) => v == null ? v2 : v)(this.starter,null);
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
         * Get time instant
         * 
         * @return {javax.xml.datatype.XMLGregorianCalendar} {@link XMLGregorianCalendar}
         */
        public getTime(): javax.xml.datatype.XMLGregorianCalendar {
            return /* orElse */((v, v2) => v == null ? v2 : v)(this.time,null);
        }

        /**
         * Set time instant
         * 
         * @param {javax.xml.datatype.XMLGregorianCalendar} time
         */
        public setTime(time: javax.xml.datatype.XMLGregorianCalendar) {
            this.time = time;
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
            return StatementOrBundle.Kind.PROV_START;
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
            if (!(object != null && object instanceof <any>org.openprovenance.prov.vanilla.WasStartedBy)){
                equalsBuilder.appendSuper(false);
                return;
            }
            if (this === object){
                return;
            }
            const that: WasStartedBy = (<WasStartedBy>object);
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getId(), that.getId());
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getActivity(), that.getActivity());
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getTrigger(), that.getTrigger());
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getStarter(), that.getStarter());
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getTime(), that.getTime());
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
            if (!(object != null && object instanceof <any>org.openprovenance.prov.vanilla.WasStartedBy)){
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
            hashCodeBuilder.append$java_lang_Object(this.getTrigger());
            hashCodeBuilder.append$java_lang_Object(this.getStarter());
            hashCodeBuilder.append$java_lang_Object(this.getTime());
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
                let theTrigger: org.openprovenance.prov.model.QualifiedName;
                theTrigger = this.getTrigger();
                toStringBuilder.append$java_lang_String$java_lang_Object("trigger", theTrigger);
            };
            {
                let theStarter: org.openprovenance.prov.model.QualifiedName;
                theStarter = this.getTrigger();
                toStringBuilder.append$java_lang_String$java_lang_Object("starter", theStarter);
            };
            {
                let theTime: javax.xml.datatype.XMLGregorianCalendar;
                theTime = this.getTime();
                toStringBuilder.append$java_lang_String$java_lang_Object("time", theTime);
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
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(result, this.getLocation());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(result, this.getRole());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(result, this.getOther().stream().map<any>((o) => <org.openprovenance.prov.model.Attribute><any>o).collect<any, any>(java.util.stream.Collectors.toList<any>()));
            return result;
        }

        public setIndexedAttributes(qn: any, attributes: Array<org.openprovenance.prov.model.Attribute>) {
            const values: Array<org.openprovenance.prov.model.Value> = <any>([]);
            WasStartedBy.u_$LI$().distribute(<org.openprovenance.prov.model.QualifiedName><any>qn, attributes, this.getLabel(), values, this.getLocation(), this.getType(), this.getRole(), this.getOther());
        }

        /**
         * 
         * @return {*}
         */
        public getIndexedAttributes(): any {
            return WasStartedBy.u_$LI$().split$java_util_Collection(this.getAttributes());
        }
    }
    WasStartedBy["__class"] = "org.openprovenance.prov.vanilla.WasStartedBy";
    WasStartedBy["__interfaces"] = ["org.openprovenance.prov.model.HasOther","org.openprovenance.prov.model.QualifiedRelation","org.openprovenance.prov.model.HasType","org.openprovenance.prov.model.HasLocation","org.openprovenance.prov.model.StatementOrBundle","org.openprovenance.apache.commons.lang.builder.ToString","org.openprovenance.prov.model.Statement","org.openprovenance.prov.model.HasTime","org.openprovenance.prov.vanilla.HasAttributes","org.openprovenance.prov.model.WasStartedBy","org.openprovenance.apache.commons.lang.builder.HashCode","org.openprovenance.prov.model.Influence","org.openprovenance.prov.model.HasLabel","org.openprovenance.prov.model.HasRole","org.openprovenance.apache.commons.lang.builder.Equals","org.openprovenance.prov.model.Identifiable","org.openprovenance.prov.model.Relation"];


}

