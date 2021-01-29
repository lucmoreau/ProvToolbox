/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.vanilla {
    export class QualifiedAlternateOf implements org.openprovenance.prov.model.extension.QualifiedAlternateOf, org.openprovenance.apache.commons.lang.builder.Equals, org.openprovenance.apache.commons.lang.builder.HashCode, org.openprovenance.apache.commons.lang.builder.ToString, org.openprovenance.prov.vanilla.HasAttributes {
        /*private*/ id: org.openprovenance.prov.model.QualifiedName;

        /*private*/ labels: Array<org.openprovenance.prov.model.LangString>;

        /*private*/ other: Array<org.openprovenance.prov.model.Other>;

        /*private*/ type: Array<org.openprovenance.prov.model.Type>;

        alternate1: org.openprovenance.prov.model.QualifiedName;

        alternate2: org.openprovenance.prov.model.QualifiedName;

        u: org.openprovenance.prov.vanilla.ProvUtilities;

        /**
         * 
         * @return {boolean}
         */
        public isUnqualified(): boolean {
            return /* isEmpty */(this.id == null) && /* isEmpty */(this.other.length == 0) && /* isEmpty */(this.labels.length == 0) && /* isEmpty */(this.type.length == 0);
        }

        public constructor(id?: any, alternate1?: any, alternate2?: any, attributes?: any) {
            if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((alternate1 != null && (alternate1.constructor != null && alternate1.constructor["__interfaces"] != null && alternate1.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || alternate1 === null) && ((alternate2 != null && (alternate2.constructor != null && alternate2.constructor["__interfaces"] != null && alternate2.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || alternate2 === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                let __args = arguments;
                if (this.alternate1 === undefined) { this.alternate1 = null; } 
                if (this.alternate2 === undefined) { this.alternate2 = null; } 
                this.id = null;
                this.labels = <any>([]);
                this.other = <any>([]);
                this.type = <any>([]);
                this.u = new org.openprovenance.prov.vanilla.ProvUtilities();
                this.setId(id);
                this.alternate1 = alternate1;
                this.alternate2 = alternate2;
                this.u.populateAttributes(attributes, this.labels, <any>([]), this.type, <any>([]), this.other, null);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((alternate1 != null && (alternate1 instanceof Array)) || alternate1 === null) && alternate2 === undefined && attributes === undefined) {
                let __args = arguments;
                let attributes: any = __args[1];
                if (this.alternate1 === undefined) { this.alternate1 = null; } 
                if (this.alternate2 === undefined) { this.alternate2 = null; } 
                this.id = null;
                this.labels = <any>([]);
                this.other = <any>([]);
                this.type = <any>([]);
                this.u = new org.openprovenance.prov.vanilla.ProvUtilities();
                this.setId(id);
                this.u.populateAttributes(attributes, this.labels, <any>([]), this.type, <any>([]), this.other, null);
            } else if (id === undefined && alternate1 === undefined && alternate2 === undefined && attributes === undefined) {
                let __args = arguments;
                if (this.alternate1 === undefined) { this.alternate1 = null; } 
                if (this.alternate2 === undefined) { this.alternate2 = null; } 
                this.id = null;
                this.labels = <any>([]);
                this.other = <any>([]);
                this.type = <any>([]);
                this.u = new org.openprovenance.prov.vanilla.ProvUtilities();
            } else throw new Error('invalid overload');
        }

        /**
         * 
         * @param {*} informed
         */
        public setAlternate1(informed: org.openprovenance.prov.model.QualifiedName) {
            this.alternate1 = informed;
        }

        /**
         * 
         * @param {*} alternate2
         */
        public setAlternate2(alternate2: org.openprovenance.prov.model.QualifiedName) {
            this.alternate2 = alternate2;
        }

        /**
         * 
         * @return {*}
         */
        public getAlternate2(): org.openprovenance.prov.model.QualifiedName {
            return this.alternate2;
        }

        /**
         * 
         * @return {*}
         */
        public getAlternate1(): org.openprovenance.prov.model.QualifiedName {
            return this.alternate1;
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
            return StatementOrBundle.Kind.PROV_ALTERNATE;
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
            if (!(object != null && object instanceof <any>org.openprovenance.prov.vanilla.QualifiedAlternateOf)){
                equalsBuilder.appendSuper(false);
                return;
            }
            if (this === object){
                return;
            }
            const that: QualifiedAlternateOf = (<QualifiedAlternateOf>object);
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getId(), that.getId());
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getAlternate1(), that.getAlternate1());
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getAlternate2(), that.getAlternate2());
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
            if (!(object != null && object instanceof <any>org.openprovenance.prov.vanilla.QualifiedAlternateOf)){
                equalsBuilder.appendSuper(false);
                return;
            }
            if (this === object){
                return;
            }
            const that: QualifiedAlternateOf = (<QualifiedAlternateOf>object);
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getAlternate1(), that.getAlternate1());
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getAlternate2(), that.getAlternate2());
        }

        public equals$java_lang_Object(object: any): boolean {
            if (!(object != null && object instanceof <any>org.openprovenance.prov.vanilla.QualifiedAlternateOf)){
                if (object != null && object instanceof <any>org.openprovenance.prov.vanilla.AlternateOf){
                    if (this.isUnqualified()){
                        const alt: org.openprovenance.prov.vanilla.AlternateOf = <org.openprovenance.prov.vanilla.AlternateOf>object;
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
            hashCodeBuilder.append$java_lang_Object(this.getAlternate1());
            hashCodeBuilder.append$java_lang_Object(this.getAlternate2());
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
                let theInformed: org.openprovenance.prov.model.QualifiedName;
                theInformed = this.getAlternate1();
                toStringBuilder.append$java_lang_String$java_lang_Object("alternate1", theInformed);
            };
            {
                let theInformant: org.openprovenance.prov.model.QualifiedName;
                theInformant = this.getAlternate2();
                toStringBuilder.append$java_lang_String$java_lang_Object("alternate2", theInformant);
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
    QualifiedAlternateOf["__class"] = "org.openprovenance.prov.vanilla.QualifiedAlternateOf";
    QualifiedAlternateOf["__interfaces"] = ["org.openprovenance.prov.model.QualifiedRelation","org.openprovenance.prov.model.HasOther","org.openprovenance.prov.model.UnqualifiedRelation","org.openprovenance.prov.model.HasType","org.openprovenance.prov.model.StatementOrBundle","org.openprovenance.apache.commons.lang.builder.ToString","org.openprovenance.prov.model.Statement","org.openprovenance.prov.model.extension.QualifiedAlternateOf","org.openprovenance.prov.vanilla.HasAttributes","org.openprovenance.apache.commons.lang.builder.HashCode","org.openprovenance.prov.model.AlternateOf","org.openprovenance.prov.model.HasLabel","org.openprovenance.apache.commons.lang.builder.Equals","org.openprovenance.prov.model.Relation","org.openprovenance.prov.model.Identifiable"];


}

