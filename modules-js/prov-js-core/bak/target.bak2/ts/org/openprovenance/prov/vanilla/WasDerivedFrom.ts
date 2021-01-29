/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.vanilla {
    export class WasDerivedFrom implements org.openprovenance.prov.model.WasDerivedFrom, org.openprovenance.apache.commons.lang.builder.Equals, org.openprovenance.apache.commons.lang.builder.HashCode, org.openprovenance.apache.commons.lang.builder.ToString, org.openprovenance.prov.vanilla.HasAttributes {
        /*private*/ id: org.openprovenance.prov.model.QualifiedName;

        /*private*/ labels: Array<org.openprovenance.prov.model.LangString>;

        /*private*/ other: Array<org.openprovenance.prov.model.Other>;

        /*private*/ type: Array<org.openprovenance.prov.model.Type>;

        generatedEntity: org.openprovenance.prov.model.QualifiedName;

        usedEntity: org.openprovenance.prov.model.QualifiedName;

        activity: org.openprovenance.prov.model.QualifiedName;

        generation: org.openprovenance.prov.model.QualifiedName;

        usage: org.openprovenance.prov.model.QualifiedName;

        static u: org.openprovenance.prov.vanilla.ProvUtilities; public static u_$LI$(): org.openprovenance.prov.vanilla.ProvUtilities { if (WasDerivedFrom.u == null) { WasDerivedFrom.u = new org.openprovenance.prov.vanilla.ProvUtilities(); }  return WasDerivedFrom.u; }

        public constructor(id?: any, generatedEntity?: any, usedEntity?: any, activity?: any, generation?: any, usage?: any, attributes?: any) {
            if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((generatedEntity != null && (generatedEntity.constructor != null && generatedEntity.constructor["__interfaces"] != null && generatedEntity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || generatedEntity === null) && ((usedEntity != null && (usedEntity.constructor != null && usedEntity.constructor["__interfaces"] != null && usedEntity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || usedEntity === null) && ((activity != null && (activity.constructor != null && activity.constructor["__interfaces"] != null && activity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || activity === null) && ((generation != null && (generation.constructor != null && generation.constructor["__interfaces"] != null && generation.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || generation === null) && ((usage != null && (usage.constructor != null && usage.constructor["__interfaces"] != null && usage.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || usage === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                let __args = arguments;
                if (this.generatedEntity === undefined) { this.generatedEntity = null; } 
                if (this.usedEntity === undefined) { this.usedEntity = null; } 
                this.id = null;
                this.labels = <any>([]);
                this.other = <any>([]);
                this.type = <any>([]);
                this.activity = null;
                this.generation = null;
                this.usage = null;
                this.setId(id);
                this.setActivity(activity);
                this.usedEntity = usedEntity;
                this.generatedEntity = generatedEntity;
                this.setGeneration(generation);
                this.setUsage(usage);
                WasDerivedFrom.u_$LI$().populateAttributes(attributes, this.labels, java.util.Collections.EMPTY_LIST, this.type, java.util.Collections.EMPTY_LIST, this.other, null);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((generatedEntity != null && (generatedEntity.constructor != null && generatedEntity.constructor["__interfaces"] != null && generatedEntity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || generatedEntity === null) && ((usedEntity != null && (usedEntity.constructor != null && usedEntity.constructor["__interfaces"] != null && usedEntity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || usedEntity === null) && ((activity != null && (activity.constructor != null && activity.constructor["__interfaces"] != null && activity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || activity === null) && ((generation != null && (generation instanceof Array)) || generation === null) && usage === undefined && attributes === undefined) {
                let __args = arguments;
                let attributes: any = __args[4];
                if (this.generatedEntity === undefined) { this.generatedEntity = null; } 
                if (this.usedEntity === undefined) { this.usedEntity = null; } 
                this.id = null;
                this.labels = <any>([]);
                this.other = <any>([]);
                this.type = <any>([]);
                this.activity = null;
                this.generation = null;
                this.usage = null;
                this.setId(id);
                this.setActivity(activity);
                this.usedEntity = usedEntity;
                this.generatedEntity = generatedEntity;
                WasDerivedFrom.u_$LI$().populateAttributes(attributes, this.labels, java.util.Collections.EMPTY_LIST, this.type, java.util.Collections.EMPTY_LIST, this.other, null);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((generatedEntity != null && (generatedEntity.constructor != null && generatedEntity.constructor["__interfaces"] != null && generatedEntity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || generatedEntity === null) && ((usedEntity != null && (usedEntity.constructor != null && usedEntity.constructor["__interfaces"] != null && usedEntity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || usedEntity === null) && ((activity != null && (activity instanceof Array)) || activity === null) && generation === undefined && usage === undefined && attributes === undefined) {
                let __args = arguments;
                let attributes: any = __args[3];
                if (this.generatedEntity === undefined) { this.generatedEntity = null; } 
                if (this.usedEntity === undefined) { this.usedEntity = null; } 
                this.id = null;
                this.labels = <any>([]);
                this.other = <any>([]);
                this.type = <any>([]);
                this.activity = null;
                this.generation = null;
                this.usage = null;
                this.setId(id);
                this.usedEntity = usedEntity;
                this.generatedEntity = generatedEntity;
                WasDerivedFrom.u_$LI$().populateAttributes(attributes, this.labels, java.util.Collections.EMPTY_LIST, this.type, java.util.Collections.EMPTY_LIST, this.other, null);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((generatedEntity != null && (generatedEntity instanceof Array)) || generatedEntity === null) && usedEntity === undefined && activity === undefined && generation === undefined && usage === undefined && attributes === undefined) {
                let __args = arguments;
                let attributes: any = __args[1];
                if (this.generatedEntity === undefined) { this.generatedEntity = null; } 
                if (this.usedEntity === undefined) { this.usedEntity = null; } 
                this.id = null;
                this.labels = <any>([]);
                this.other = <any>([]);
                this.type = <any>([]);
                this.activity = null;
                this.generation = null;
                this.usage = null;
                this.setId(id);
                WasDerivedFrom.u_$LI$().populateAttributes(attributes, this.labels, java.util.Collections.EMPTY_LIST, this.type, java.util.Collections.EMPTY_LIST, this.other, null);
            } else if (id === undefined && generatedEntity === undefined && usedEntity === undefined && activity === undefined && generation === undefined && usage === undefined && attributes === undefined) {
                let __args = arguments;
                if (this.generatedEntity === undefined) { this.generatedEntity = null; } 
                if (this.usedEntity === undefined) { this.usedEntity = null; } 
                this.id = null;
                this.labels = <any>([]);
                this.other = <any>([]);
                this.type = <any>([]);
                this.activity = null;
                this.generation = null;
                this.usage = null;
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
         * @param {*} gen
         */
        public setGeneration(gen: org.openprovenance.prov.model.QualifiedName) {
            this.generation = gen;
        }

        /**
         * 
         * @param {*} use
         */
        public setUsage(use: org.openprovenance.prov.model.QualifiedName) {
            this.usage = use;
        }

        /**
         * 
         * @param {*} eid
         */
        public setUsedEntity(eid: org.openprovenance.prov.model.QualifiedName) {
            this.usedEntity = eid;
        }

        /**
         * 
         * @param {*} eid
         */
        public setGeneratedEntity(eid: org.openprovenance.prov.model.QualifiedName) {
            this.generatedEntity = eid;
        }

        /**
         * 
         * @return {*}
         */
        public getUsedEntity(): org.openprovenance.prov.model.QualifiedName {
            return this.usedEntity;
        }

        /**
         * 
         * @return {*}
         */
        public getUsage(): org.openprovenance.prov.model.QualifiedName {
            return /* orElse */((v, v2) => v == null ? v2 : v)(this.usage,null);
        }

        /**
         * 
         * @return {*}
         */
        public getGeneratedEntity(): org.openprovenance.prov.model.QualifiedName {
            return this.generatedEntity;
        }

        /**
         * 
         * @return {*}
         */
        public getGeneration(): org.openprovenance.prov.model.QualifiedName {
            return /* orElse */((v, v2) => v == null ? v2 : v)(this.generation,null);
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
            return StatementOrBundle.Kind.PROV_DERIVATION;
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
            if (!(object != null && object instanceof <any>org.openprovenance.prov.vanilla.WasDerivedFrom)){
                equalsBuilder.appendSuper(false);
                return;
            }
            if (this === object){
                return;
            }
            const that: WasDerivedFrom = (<WasDerivedFrom>object);
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getId(), that.getId());
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getGeneratedEntity(), that.getGeneratedEntity());
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getUsedEntity(), that.getUsedEntity());
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getActivity(), that.getActivity());
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getUsage(), that.getUsage());
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getGeneration(), that.getGeneration());
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
            if (!(object != null && object instanceof <any>org.openprovenance.prov.vanilla.WasDerivedFrom)){
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
            hashCodeBuilder.append$java_lang_Object(this.getGeneratedEntity());
            hashCodeBuilder.append$java_lang_Object(this.getUsedEntity());
            hashCodeBuilder.append$java_lang_Object(this.getActivity());
            hashCodeBuilder.append$java_lang_Object(this.getGeneration());
            hashCodeBuilder.append$java_lang_Object(this.getUsage());
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
                theEntity = this.getGeneratedEntity();
                toStringBuilder.append$java_lang_String$java_lang_Object("generatedEntity", theEntity);
            };
            {
                let theEntity: org.openprovenance.prov.model.QualifiedName;
                theEntity = this.getUsedEntity();
                toStringBuilder.append$java_lang_String$java_lang_Object("usedEntity", theEntity);
            };
            {
                let theActivity: org.openprovenance.prov.model.QualifiedName;
                theActivity = this.getActivity();
                toStringBuilder.append$java_lang_String$java_lang_Object("activity", theActivity);
            };
            {
                let theActivity: org.openprovenance.prov.model.QualifiedName;
                theActivity = this.getGeneration();
                toStringBuilder.append$java_lang_String$java_lang_Object("generation", theActivity);
            };
            {
                let theActivity: org.openprovenance.prov.model.QualifiedName;
                theActivity = this.getUsage();
                toStringBuilder.append$java_lang_String$java_lang_Object("usage", theActivity);
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
            const values: Array<org.openprovenance.prov.model.Value> = <any>([]);
            WasDerivedFrom.u_$LI$().distribute(<org.openprovenance.prov.model.QualifiedName><any>qn, attributes, this.getLabel(), values, java.util.Collections.EMPTY_LIST, this.getType(), java.util.Collections.EMPTY_LIST, this.getOther());
        }

        /**
         * 
         * @return {*}
         */
        public getIndexedAttributes(): any {
            return WasDerivedFrom.u_$LI$().split$java_util_Collection(this.getAttributes());
        }
    }
    WasDerivedFrom["__class"] = "org.openprovenance.prov.vanilla.WasDerivedFrom";
    WasDerivedFrom["__interfaces"] = ["org.openprovenance.prov.model.HasOther","org.openprovenance.prov.model.QualifiedRelation","org.openprovenance.prov.model.HasType","org.openprovenance.prov.model.WasDerivedFrom","org.openprovenance.prov.model.StatementOrBundle","org.openprovenance.apache.commons.lang.builder.ToString","org.openprovenance.prov.model.Statement","org.openprovenance.prov.vanilla.HasAttributes","org.openprovenance.apache.commons.lang.builder.HashCode","org.openprovenance.prov.model.Influence","org.openprovenance.prov.model.HasLabel","org.openprovenance.apache.commons.lang.builder.Equals","org.openprovenance.prov.model.Identifiable","org.openprovenance.prov.model.Relation"];


}

