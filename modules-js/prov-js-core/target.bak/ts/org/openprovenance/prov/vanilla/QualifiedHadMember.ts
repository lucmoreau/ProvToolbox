/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.vanilla {
    export class QualifiedHadMember implements org.openprovenance.prov.model.extension.QualifiedHadMember, org.openprovenance.apache.commons.lang.builder.Equals, org.openprovenance.apache.commons.lang.builder.HashCode, org.openprovenance.apache.commons.lang.builder.ToString, org.openprovenance.prov.vanilla.HasAttributes {
        /*private*/ id: org.openprovenance.prov.model.QualifiedName;

        /*private*/ labels: Array<org.openprovenance.prov.model.LangString>;

        /*private*/ other: Array<org.openprovenance.prov.model.Other>;

        /*private*/ type: Array<org.openprovenance.prov.model.Type>;

        collection: org.openprovenance.prov.model.QualifiedName;

        entity: Array<org.openprovenance.prov.model.QualifiedName>;

        u: org.openprovenance.prov.vanilla.ProvUtilities;

        /**
         * 
         * @return {boolean}
         */
        public isUnqualified(): boolean {
            return /* isEmpty */(this.id == null) && /* isEmpty */(this.other.length == 0) && /* isEmpty */(this.labels.length == 0) && /* isEmpty */(this.type.length == 0);
        }

        public constructor(id?: any, collection?: any, entity?: any, attributes?: any) {
            if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((collection != null && (collection.constructor != null && collection.constructor["__interfaces"] != null && collection.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || collection === null) && ((entity != null && (entity instanceof Array)) || entity === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                let __args = arguments;
                if (this.collection === undefined) { this.collection = null; } 
                this.id = null;
                this.labels = <any>([]);
                this.other = <any>([]);
                this.type = <any>([]);
                this.entity = <any>([]);
                this.u = new org.openprovenance.prov.vanilla.ProvUtilities();
                this.setId(id);
                this.collection = collection;
                /* addAll */((l1, l2) => l1.push.apply(l1, l2))(this.entity, entity);
                this.u.populateAttributes(attributes, this.labels, <any>([]), this.type, <any>([]), this.other, null);
            } else if (id === undefined && collection === undefined && entity === undefined && attributes === undefined) {
                let __args = arguments;
                if (this.collection === undefined) { this.collection = null; } 
                this.id = null;
                this.labels = <any>([]);
                this.other = <any>([]);
                this.type = <any>([]);
                this.entity = <any>([]);
                this.u = new org.openprovenance.prov.vanilla.ProvUtilities();
            } else throw new Error('invalid overload');
        }

        /**
         * 
         * @param {*} collection
         */
        public setCollection(collection: org.openprovenance.prov.model.QualifiedName) {
            this.collection = collection;
        }

        /**
         * 
         * @return {*[]}
         */
        public getEntity(): Array<org.openprovenance.prov.model.QualifiedName> {
            return this.entity;
        }

        /**
         * 
         * @return {*}
         */
        public getCollection(): org.openprovenance.prov.model.QualifiedName {
            return this.collection;
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
            return StatementOrBundle.Kind.PROV_MEMBERSHIP;
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
            if (!(object != null && object instanceof <any>org.openprovenance.prov.vanilla.QualifiedHadMember)){
                equalsBuilder.appendSuper(false);
                return;
            }
            if (this === object){
                return;
            }
            const that: QualifiedHadMember = (<QualifiedHadMember>object);
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getId(), that.getId());
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getCollection(), that.getCollection());
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

        public equals2(object: any, equalsBuilder: org.openprovenance.apache.commons.lang.builder.EqualsBuilder) {
            if (!(object != null && object instanceof <any>org.openprovenance.prov.vanilla.HadMember)){
                equalsBuilder.appendSuper(false);
                return;
            }
            if (this === object){
                return;
            }
            const that: org.openprovenance.prov.vanilla.HadMember = (<org.openprovenance.prov.vanilla.HadMember>object);
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getCollection(), that.getCollection());
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getEntity(), that.getEntity());
        }

        public equals$java_lang_Object(object: any): boolean {
            if (!(object != null && object instanceof <any>org.openprovenance.prov.vanilla.QualifiedHadMember)){
                if (object != null && object instanceof <any>org.openprovenance.prov.vanilla.HadMember){
                    if (this.isUnqualified()){
                        console.info("QualifiedHadMember.equals with HadMember");
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
            const unqualified: boolean = this.isUnqualified();
            if (!unqualified){
                hashCodeBuilder.append$java_lang_Object(this.getId());
            }
            hashCodeBuilder.append$java_lang_Object(this.getCollection());
            hashCodeBuilder.append$java_lang_Object(this.getEntity());
            if (!unqualified){
                hashCodeBuilder.append$java_lang_Object(this.getIndexedAttributes());
            }
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
                let theCollection: org.openprovenance.prov.model.QualifiedName;
                theCollection = this.getCollection();
                toStringBuilder.append$java_lang_String$java_lang_Object("collection", theCollection);
            };
            {
                let theEntity: Array<org.openprovenance.prov.model.QualifiedName>;
                theEntity = this.getEntity();
                toStringBuilder.append$java_lang_String$java_lang_Object("entity", theEntity);
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
            const locations: Array<org.openprovenance.prov.model.Location> = <any>([]);
            this.u.distribute(<org.openprovenance.prov.model.QualifiedName><any>qn, attributes, this.getLabel(), values, locations, this.getType(), java.util.Collections.EMPTY_LIST, this.getOther());
        }

        /**
         * 
         * @return {*}
         */
        public getIndexedAttributes(): any {
            return this.u.split$java_util_Collection(this.getAttributes());
        }
    }
    QualifiedHadMember["__class"] = "org.openprovenance.prov.vanilla.QualifiedHadMember";
    QualifiedHadMember["__interfaces"] = ["org.openprovenance.prov.model.extension.QualifiedHadMember","org.openprovenance.prov.model.QualifiedRelation","org.openprovenance.prov.model.HasOther","org.openprovenance.prov.model.UnqualifiedRelation","org.openprovenance.prov.model.HasType","org.openprovenance.prov.model.StatementOrBundle","org.openprovenance.apache.commons.lang.builder.ToString","org.openprovenance.prov.model.Statement","org.openprovenance.prov.vanilla.HasAttributes","org.openprovenance.apache.commons.lang.builder.HashCode","org.openprovenance.prov.model.HasLabel","org.openprovenance.prov.model.HadMember","org.openprovenance.apache.commons.lang.builder.Equals","org.openprovenance.prov.model.Relation","org.openprovenance.prov.model.Identifiable"];


}

