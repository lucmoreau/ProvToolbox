/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.vanilla {
    export class HadMember implements org.openprovenance.prov.model.HadMember, org.openprovenance.apache.commons.lang.builder.Equals, org.openprovenance.apache.commons.lang.builder.HashCode, org.openprovenance.apache.commons.lang.builder.ToString {
        entity: Array<org.openprovenance.prov.model.QualifiedName>;

        collection: org.openprovenance.prov.model.QualifiedName;

        public constructor(collection?: any, entity?: any) {
            if (((collection != null && (collection.constructor != null && collection.constructor["__interfaces"] != null && collection.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || collection === null) && ((entity != null && (entity instanceof Array)) || entity === null)) {
                let __args = arguments;
                if (this.collection === undefined) { this.collection = null; } 
                this.entity = <any>([]);
                this.collection = collection;
                this.entity = entity;
            } else if (collection === undefined && entity === undefined) {
                let __args = arguments;
                if (this.collection === undefined) { this.collection = null; } 
                this.entity = <any>([]);
            } else throw new Error('invalid overload');
        }

        /**
         * Get an identifier for the collection whose member is asserted
         * 
         * @return {*} QualifiedName for the collection
         * @see <a href="http://www.w3.org/TR/prov-dm/#membership.collection">membership collection</a>
         */
        public getCollection(): org.openprovenance.prov.model.QualifiedName {
            return this.collection;
        }

        /**
         * Get the list of identifiers of entities that are member of the collection.
         * 
         * @return {*[]} a list of {@link QualifiedName}
         * 
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the entity property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         * getEntity().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link QualifiedName}
         * @see <a href="http://www.w3.org/TR/prov-dm/#membership.entity">membership entity</a>
         */
        public getEntity(): Array<org.openprovenance.prov.model.QualifiedName> {
            return this.entity;
        }

        /**
         * Set an identifier for the collection whose member is asserted
         * 
         * @param {*} collection QualifiedName for the collection
         * @see <a href="http://www.w3.org/TR/prov-dm/#membership.collection">membership collection</a>
         */
        public setCollection(collection: org.openprovenance.prov.model.QualifiedName) {
            this.collection = collection;
        }

        /**
         * Gets the type of a provenance statement
         * 
         * @return {org.openprovenance.prov.model.StatementOrBundle.Kind} {@link Kind}
         */
        public getKind(): StatementOrBundle.Kind {
            return StatementOrBundle.Kind.PROV_MEMBERSHIP;
        }

        public equals$java_lang_Object$org_openprovenance_apache_commons_lang_builder_EqualsBuilder(object: any, equalsBuilder: org.openprovenance.apache.commons.lang.builder.EqualsBuilder) {
            if (!(object != null && object instanceof <any>org.openprovenance.prov.vanilla.HadMember)){
                equalsBuilder.appendSuper(false);
                return;
            }
            if (this === object){
                return;
            }
            const that: HadMember = (<HadMember>object);
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getEntity(), that.getEntity());
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getCollection(), that.getCollection());
        }

        public equals(object?: any, equalsBuilder?: any) {
            if (((object != null) || object === null) && ((equalsBuilder != null && equalsBuilder instanceof <any>org.openprovenance.apache.commons.lang.builder.EqualsBuilder) || equalsBuilder === null)) {
                return <any>this.equals$java_lang_Object$org_openprovenance_apache_commons_lang_builder_EqualsBuilder(object, equalsBuilder);
            } else if (((object != null) || object === null) && equalsBuilder === undefined) {
                return <any>this.equals$java_lang_Object(object);
            } else throw new Error('invalid overload');
        }

        public equals2(object: any, equalsBuilder: org.openprovenance.apache.commons.lang.builder.EqualsBuilder) {
            if (!(object != null && object instanceof <any>org.openprovenance.prov.vanilla.QualifiedHadMember)){
                equalsBuilder.appendSuper(false);
                return;
            }
            if (this === object){
                return;
            }
            const that: org.openprovenance.prov.vanilla.QualifiedHadMember = (<org.openprovenance.prov.vanilla.QualifiedHadMember>object);
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getEntity(), that.getEntity());
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getCollection(), that.getCollection());
        }

        public equals$java_lang_Object(object: any): boolean {
            if (!(object != null && object instanceof <any>org.openprovenance.prov.vanilla.HadMember)){
                if (object != null && object instanceof <any>org.openprovenance.prov.vanilla.QualifiedHadMember){
                    const qmem: org.openprovenance.prov.vanilla.QualifiedHadMember = <org.openprovenance.prov.vanilla.QualifiedHadMember>object;
                    if (qmem.isUnqualified()){
                        const equalsBuilder2: org.openprovenance.apache.commons.lang.builder.EqualsBuilder = new org.openprovenance.apache.commons.lang.builder.EqualsBuilder();
                        this.equals2(object, equalsBuilder2);
                        return equalsBuilder2.isEquals();
                    }
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
            hashCodeBuilder.append$java_lang_Object(this.getEntity());
            hashCodeBuilder.append$java_lang_Object(this.getCollection());
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
                let theCollection: org.openprovenance.prov.model.QualifiedName;
                theCollection = this.getCollection();
                toStringBuilder.append$java_lang_String$java_lang_Object("collection", theCollection);
            };
            {
                let theEntity: Array<org.openprovenance.prov.model.QualifiedName>;
                theEntity = this.getEntity();
                toStringBuilder.append$java_lang_String$java_lang_Object("entity", theEntity);
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
    HadMember["__class"] = "org.openprovenance.prov.vanilla.HadMember";
    HadMember["__interfaces"] = ["org.openprovenance.prov.model.UnqualifiedRelation","org.openprovenance.apache.commons.lang.builder.HashCode","org.openprovenance.prov.model.HadMember","org.openprovenance.apache.commons.lang.builder.Equals","org.openprovenance.prov.model.Relation","org.openprovenance.prov.model.StatementOrBundle","org.openprovenance.apache.commons.lang.builder.ToString","org.openprovenance.prov.model.Statement"];


}

