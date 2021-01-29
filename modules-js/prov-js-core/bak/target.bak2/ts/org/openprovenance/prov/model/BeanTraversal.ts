/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.model {
    /**
     * Generic Traversal of a PROV model bean.
     * Makes use of the "visitor" in {@link ProvUtilities#doAction(StatementOrBundle, StatementActionValue)}
     * 
     * @author lavm
     * @param {*} c
     * @param {org.openprovenance.prov.model.ProvFactory} pFactory
     * @class
     */
    export class BeanTraversal implements org.openprovenance.prov.model.StatementActionValue {
        /*private*/ c: org.openprovenance.prov.model.ModelConstructor;

        /*private*/ pFactory: org.openprovenance.prov.model.ProvFactory;

        u: org.openprovenance.prov.model.ProvUtilities;

        public constructor(c: org.openprovenance.prov.model.ModelConstructor, pFactory: org.openprovenance.prov.model.ProvFactory) {
            if (this.c === undefined) { this.c = null; }
            if (this.pFactory === undefined) { this.pFactory = null; }
            this.u = new org.openprovenance.prov.model.ProvUtilities();
            this.c = c;
            this.pFactory = pFactory;
        }

        public doAction$org_openprovenance_prov_model_ActedOnBehalfOf(del: org.openprovenance.prov.model.ActedOnBehalfOf): org.openprovenance.prov.model.ActedOnBehalfOf {
            const attrs: Array<org.openprovenance.prov.model.Attribute> = <any>([]);
            this.convertTypeAttributes(del, attrs);
            this.convertLabelAttributes(del, attrs);
            this.convertAttributes(del, attrs);
            const attrs2: Array<org.openprovenance.prov.model.Attribute> = this.copyAttributes(attrs);
            return this.c.newActedOnBehalfOf(this.copyQ(del.getId()), this.copyQ(del.getDelegate()), this.copyQ(del.getResponsible()), this.copyQ(del.getActivity()), attrs2);
        }

        public doAction$org_openprovenance_prov_model_Activity(e: org.openprovenance.prov.model.Activity): org.openprovenance.prov.model.Activity {
            const attrs: Array<org.openprovenance.prov.model.Attribute> = <any>([]);
            this.convertTypeAttributes(e, attrs);
            this.convertLabelAttributes(e, attrs);
            this.convertLocationAttributes(e, attrs);
            this.convertAttributes(e, attrs);
            const attrs2: Array<org.openprovenance.prov.model.Attribute> = this.copyAttributes(attrs);
            return this.c.newActivity(this.copyQ(e.getId()), e.getStartTime(), e.getEndTime(), attrs2);
        }

        public doAction$org_openprovenance_prov_model_Agent(e: org.openprovenance.prov.model.Agent): org.openprovenance.prov.model.Agent {
            const attrs: Array<org.openprovenance.prov.model.Attribute> = <any>([]);
            this.convertTypeAttributes(e, attrs);
            this.convertLabelAttributes(e, attrs);
            this.convertLocationAttributes(e, attrs);
            this.convertAttributes(e, attrs);
            const attrs2: Array<org.openprovenance.prov.model.Attribute> = this.copyAttributes(attrs);
            return this.c.newAgent(this.copyQ(e.getId()), attrs2);
        }

        public doAction$org_openprovenance_prov_model_AlternateOf(o: org.openprovenance.prov.model.AlternateOf): org.openprovenance.prov.model.AlternateOf {
            return this.c.newAlternateOf(this.copyQ(o.getAlternate1()), this.copyQ(o.getAlternate2()));
        }

        public doAction$org_openprovenance_prov_model_DerivedByInsertionFrom(o: org.openprovenance.prov.model.DerivedByInsertionFrom): org.openprovenance.prov.model.Relation {
            const attrs: Array<org.openprovenance.prov.model.Attribute> = <any>([]);
            this.convertTypeAttributes(o, attrs);
            this.convertLabelAttributes(o, attrs);
            this.convertAttributes(o, attrs);
            return this.c.newDerivedByInsertionFrom(o.getId(), o.getNewDictionary(), o.getOldDictionary(), o.getKeyEntityPair(), attrs);
        }

        public doAction$org_openprovenance_prov_model_DerivedByRemovalFrom(o: org.openprovenance.prov.model.DerivedByRemovalFrom): org.openprovenance.prov.model.Relation {
            const attrs: Array<org.openprovenance.prov.model.Attribute> = <any>([]);
            this.convertTypeAttributes(o, attrs);
            this.convertLabelAttributes(o, attrs);
            this.convertAttributes(o, attrs);
            return this.c.newDerivedByRemovalFrom(o.getId(), o.getNewDictionary(), o.getOldDictionary(), o.getKey(), attrs);
        }

        public doAction$org_openprovenance_prov_model_DictionaryMembership(o: org.openprovenance.prov.model.DictionaryMembership): org.openprovenance.prov.model.Relation {
            return this.c.newDictionaryMembership(o.getDictionary(), o.getKeyEntityPair());
        }

        public doAction$org_openprovenance_prov_model_Document(doc: org.openprovenance.prov.model.Document): org.openprovenance.prov.model.Document {
            const bRecords: Array<org.openprovenance.prov.model.Bundle> = <any>([]);
            const sRecords: Array<org.openprovenance.prov.model.Statement> = <any>([]);
            const docNamespace: org.openprovenance.prov.model.Namespace = doc.getNamespace();
            org.openprovenance.prov.model.Namespace.withThreadNamespace(docNamespace);
            this.c.startDocument(doc.getNamespace());
            {
                let array182 = this.u.getStatement$org_openprovenance_prov_model_Document(doc);
                for(let index181=0; index181 < array182.length; index181++) {
                    let s = array182[index181];
                    {
                        /* add */(sRecords.push(<org.openprovenance.prov.model.Statement><any>this.u.doAction$org_openprovenance_prov_model_StatementOrBundle$org_openprovenance_prov_model_StatementActionValue(s, this))>0);
                    }
                }
            }
            {
                let array184 = this.u.getNamedBundle(doc);
                for(let index183=0; index183 < array184.length; index183++) {
                    let bu = array184[index183];
                    {
                        org.openprovenance.prov.model.Namespace.withThreadNamespace(new org.openprovenance.prov.model.Namespace(docNamespace));
                        const o: org.openprovenance.prov.model.Bundle = this.doAction$org_openprovenance_prov_model_Bundle$org_openprovenance_prov_model_ProvUtilities(bu, this.u);
                        if (o != null)/* add */(bRecords.push(o)>0);
                    }
                }
            }
            return this.c.newDocument(doc.getNamespace(), sRecords, bRecords);
        }

        public doAction$org_openprovenance_prov_model_Entity(e: org.openprovenance.prov.model.Entity): org.openprovenance.prov.model.Entity {
            const attrs: Array<org.openprovenance.prov.model.Attribute> = <any>([]);
            this.convertTypeAttributes(e, attrs);
            this.convertLabelAttributes(e, attrs);
            this.convertLocationAttributes(e, attrs);
            this.convertValueAttributes(e, attrs);
            this.convertAttributes(e, attrs);
            const attrs2: Array<org.openprovenance.prov.model.Attribute> = this.copyAttributes(attrs);
            return this.c.newEntity(this.copyQ(e.getId()), attrs2);
        }

        public copyAttributes(attrs: Array<org.openprovenance.prov.model.Attribute>): Array<org.openprovenance.prov.model.Attribute> {
            const attrs2: Array<org.openprovenance.prov.model.Attribute> = <any>([]);
            for(let index185=0; index185 < attrs.length; index185++) {
                let attr = attrs[index185];
                {
                    /* add */(attrs2.push(this.pFactory.newAttribute$org_openprovenance_prov_model_QualifiedName$java_lang_Object$org_openprovenance_prov_model_QualifiedName(this.copyQ(attr.getElementName()), this.copyValue(attr.getValue()), this.copyQ(attr.getType())))>0);
                }
            }
            return attrs2;
        }

        public copyValue(value: any): any {
            if (value != null && (value.constructor != null && value.constructor["__interfaces"] != null && value.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.LangString") >= 0)){
                const ls: org.openprovenance.prov.model.LangString = <org.openprovenance.prov.model.LangString><any>value;
                return this.pFactory.newInternationalizedString$java_lang_String$java_lang_String(ls.getValue(), ls.getLang());
            }
            if (value != null && (value.constructor != null && value.constructor["__interfaces"] != null && value.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)){
                const qn: org.openprovenance.prov.model.QualifiedName = <org.openprovenance.prov.model.QualifiedName><any>value;
                return this.copyQ(qn);
            }
            return value;
        }

        public copyQ(qn: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.QualifiedName {
            if (qn == null)return null;
            return this.pFactory.newQualifiedName$java_lang_String$java_lang_String$java_lang_String(qn.getNamespaceURI(), qn.getLocalPart(), qn.getPrefix());
        }

        public doAction$org_openprovenance_prov_model_HadMember(o: org.openprovenance.prov.model.HadMember): org.openprovenance.prov.model.HadMember {
            const qq: Array<org.openprovenance.prov.model.QualifiedName> = <any>([]);
            if (o.getEntity() != null){
                {
                    let array187 = o.getEntity();
                    for(let index186=0; index186 < array187.length; index186++) {
                        let eid = array187[index186];
                        {
                            /* add */(qq.push(this.copyQ(eid))>0);
                        }
                    }
                }
            }
            return this.c.newHadMember(this.copyQ(o.getCollection()), qq);
        }

        public doAction$org_openprovenance_prov_model_MentionOf(o: org.openprovenance.prov.model.MentionOf): org.openprovenance.prov.model.MentionOf {
            return this.c.newMentionOf(o.getSpecificEntity(), o.getGeneralEntity(), o.getBundle());
        }

        public doAction$org_openprovenance_prov_model_Bundle$org_openprovenance_prov_model_ProvUtilities(b: org.openprovenance.prov.model.Bundle, u: org.openprovenance.prov.model.ProvUtilities): org.openprovenance.prov.model.Bundle {
            const sRecords: Array<org.openprovenance.prov.model.Statement> = <any>([]);
            const bundleId: org.openprovenance.prov.model.QualifiedName = this.copyQ(b.getId());
            const old: org.openprovenance.prov.model.Namespace = org.openprovenance.prov.model.Namespace.getThreadNamespace();
            let bundleNamespace: org.openprovenance.prov.model.Namespace;
            if (b.getNamespace() != null){
                bundleNamespace = new org.openprovenance.prov.model.Namespace(b.getNamespace());
            } else {
                bundleNamespace = new org.openprovenance.prov.model.Namespace();
            }
            bundleNamespace.setParent(new org.openprovenance.prov.model.Namespace(old));
            org.openprovenance.prov.model.Namespace.withThreadNamespace(bundleNamespace);
            this.c.startBundle(bundleId, b.getNamespace());
            {
                let array189 = u.getStatement$org_openprovenance_prov_model_Bundle(b);
                for(let index188=0; index188 < array189.length; index188++) {
                    let s = array189[index188];
                    {
                        /* add */(sRecords.push(<org.openprovenance.prov.model.Statement><any>u.doAction$org_openprovenance_prov_model_StatementOrBundle$org_openprovenance_prov_model_StatementActionValue(s, this))>0);
                    }
                }
            }
            return this.c.newNamedBundle(bundleId, b.getNamespace(), sRecords);
        }

        public doAction(b?: any, u?: any): any {
            if (((b != null && (b.constructor != null && b.constructor["__interfaces"] != null && b.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Bundle") >= 0)) || b === null) && ((u != null && u instanceof <any>org.openprovenance.prov.model.ProvUtilities) || u === null)) {
                return <any>this.doAction$org_openprovenance_prov_model_Bundle$org_openprovenance_prov_model_ProvUtilities(b, u);
            } else if (((b != null && (b.constructor != null && b.constructor["__interfaces"] != null && b.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.ActedOnBehalfOf") >= 0)) || b === null) && u === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_ActedOnBehalfOf(b);
            } else if (((b != null && (b.constructor != null && b.constructor["__interfaces"] != null && b.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Activity") >= 0)) || b === null) && u === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_Activity(b);
            } else if (((b != null && (b.constructor != null && b.constructor["__interfaces"] != null && b.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Agent") >= 0)) || b === null) && u === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_Agent(b);
            } else if (((b != null && (b.constructor != null && b.constructor["__interfaces"] != null && b.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.AlternateOf") >= 0)) || b === null) && u === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_AlternateOf(b);
            } else if (((b != null && (b.constructor != null && b.constructor["__interfaces"] != null && b.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.DerivedByInsertionFrom") >= 0)) || b === null) && u === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_DerivedByInsertionFrom(b);
            } else if (((b != null && (b.constructor != null && b.constructor["__interfaces"] != null && b.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.DerivedByRemovalFrom") >= 0)) || b === null) && u === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_DerivedByRemovalFrom(b);
            } else if (((b != null && (b.constructor != null && b.constructor["__interfaces"] != null && b.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.DictionaryMembership") >= 0)) || b === null) && u === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_DictionaryMembership(b);
            } else if (((b != null && (b.constructor != null && b.constructor["__interfaces"] != null && b.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Document") >= 0)) || b === null) && u === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_Document(b);
            } else if (((b != null && (b.constructor != null && b.constructor["__interfaces"] != null && b.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Entity") >= 0)) || b === null) && u === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_Entity(b);
            } else if (((b != null && (b.constructor != null && b.constructor["__interfaces"] != null && b.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HadMember") >= 0)) || b === null) && u === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_HadMember(b);
            } else if (((b != null && (b.constructor != null && b.constructor["__interfaces"] != null && b.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.MentionOf") >= 0)) || b === null) && u === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_MentionOf(b);
            } else if (((b != null && (b.constructor != null && b.constructor["__interfaces"] != null && b.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.extension.QualifiedSpecializationOf") >= 0)) || b === null) && u === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_extension_QualifiedSpecializationOf(b);
            } else if (((b != null && (b.constructor != null && b.constructor["__interfaces"] != null && b.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.SpecializationOf") >= 0)) || b === null) && u === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_SpecializationOf(b);
            } else if (((b != null && (b.constructor != null && b.constructor["__interfaces"] != null && b.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.extension.QualifiedAlternateOf") >= 0)) || b === null) && u === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_extension_QualifiedAlternateOf(b);
            } else if (((b != null && (b.constructor != null && b.constructor["__interfaces"] != null && b.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.extension.QualifiedHadMember") >= 0)) || b === null) && u === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_extension_QualifiedHadMember(b);
            } else if (((b != null && (b.constructor != null && b.constructor["__interfaces"] != null && b.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Used") >= 0)) || b === null) && u === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_Used(b);
            } else if (((b != null && (b.constructor != null && b.constructor["__interfaces"] != null && b.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasAssociatedWith") >= 0)) || b === null) && u === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_WasAssociatedWith(b);
            } else if (((b != null && (b.constructor != null && b.constructor["__interfaces"] != null && b.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasAttributedTo") >= 0)) || b === null) && u === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_WasAttributedTo(b);
            } else if (((b != null && (b.constructor != null && b.constructor["__interfaces"] != null && b.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasDerivedFrom") >= 0)) || b === null) && u === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_WasDerivedFrom(b);
            } else if (((b != null && (b.constructor != null && b.constructor["__interfaces"] != null && b.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasEndedBy") >= 0)) || b === null) && u === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_WasEndedBy(b);
            } else if (((b != null && (b.constructor != null && b.constructor["__interfaces"] != null && b.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasGeneratedBy") >= 0)) || b === null) && u === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_WasGeneratedBy(b);
            } else if (((b != null && (b.constructor != null && b.constructor["__interfaces"] != null && b.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInfluencedBy") >= 0)) || b === null) && u === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_WasInfluencedBy(b);
            } else if (((b != null && (b.constructor != null && b.constructor["__interfaces"] != null && b.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInformedBy") >= 0)) || b === null) && u === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_WasInformedBy(b);
            } else if (((b != null && (b.constructor != null && b.constructor["__interfaces"] != null && b.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInvalidatedBy") >= 0)) || b === null) && u === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_WasInvalidatedBy(b);
            } else if (((b != null && (b.constructor != null && b.constructor["__interfaces"] != null && b.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasStartedBy") >= 0)) || b === null) && u === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_WasStartedBy(b);
            } else throw new Error('invalid overload');
        }

        public doAction$org_openprovenance_prov_model_SpecializationOf(o: org.openprovenance.prov.model.SpecializationOf): org.openprovenance.prov.model.SpecializationOf {
            return this.c.newSpecializationOf(this.copyQ(o.getSpecificEntity()), this.copyQ(o.getGeneralEntity()));
        }

        public doAction$org_openprovenance_prov_model_extension_QualifiedSpecializationOf(o: org.openprovenance.prov.model.extension.QualifiedSpecializationOf): org.openprovenance.prov.model.SpecializationOf {
            const attrs: Array<org.openprovenance.prov.model.Attribute> = <any>([]);
            this.convertTypeAttributes(o, attrs);
            this.convertLabelAttributes(o, attrs);
            this.convertAttributes(o, attrs);
            const c2: org.openprovenance.prov.model.ModelConstructorExtension = <org.openprovenance.prov.model.ModelConstructorExtension><any>this.c;
            const attrs2: Array<org.openprovenance.prov.model.Attribute> = this.copyAttributes(attrs);
            return c2.newQualifiedSpecializationOf(this.copyQ(o.getId()), this.copyQ(o.getSpecificEntity()), this.copyQ(o.getGeneralEntity()), attrs2);
        }

        public doAction$org_openprovenance_prov_model_extension_QualifiedAlternateOf(o: org.openprovenance.prov.model.extension.QualifiedAlternateOf): org.openprovenance.prov.model.AlternateOf {
            const attrs: Array<org.openprovenance.prov.model.Attribute> = <any>([]);
            this.convertTypeAttributes(o, attrs);
            this.convertLabelAttributes(o, attrs);
            this.convertAttributes(o, attrs);
            const attrs2: Array<org.openprovenance.prov.model.Attribute> = this.copyAttributes(attrs);
            const c2: org.openprovenance.prov.model.ModelConstructorExtension = <org.openprovenance.prov.model.ModelConstructorExtension><any>this.c;
            return c2.newQualifiedAlternateOf(this.copyQ(o.getId()), this.copyQ(o.getAlternate1()), this.copyQ(o.getAlternate2()), attrs2);
        }

        public doAction$org_openprovenance_prov_model_extension_QualifiedHadMember(o: org.openprovenance.prov.model.extension.QualifiedHadMember): org.openprovenance.prov.model.HadMember {
            const attrs: Array<org.openprovenance.prov.model.Attribute> = <any>([]);
            this.convertTypeAttributes(o, attrs);
            this.convertLabelAttributes(o, attrs);
            this.convertAttributes(o, attrs);
            const attrs2: Array<org.openprovenance.prov.model.Attribute> = this.copyAttributes(attrs);
            const c2: org.openprovenance.prov.model.ModelConstructorExtension = <org.openprovenance.prov.model.ModelConstructorExtension><any>this.c;
            const qq: Array<org.openprovenance.prov.model.QualifiedName> = <any>([]);
            if (o.getEntity() != null){
                {
                    let array191 = o.getEntity();
                    for(let index190=0; index190 < array191.length; index190++) {
                        let eid = array191[index190];
                        {
                            /* add */(qq.push(this.copyQ(eid))>0);
                        }
                    }
                }
            }
            return c2.newQualifiedHadMember(this.copyQ(o.getId()), this.copyQ(o.getCollection()), qq, attrs2);
        }

        public doAction$org_openprovenance_prov_model_Used(use: org.openprovenance.prov.model.Used): org.openprovenance.prov.model.Used {
            const attrs: Array<org.openprovenance.prov.model.Attribute> = <any>([]);
            this.convertTypeAttributes(use, attrs);
            this.convertLabelAttributes(use, attrs);
            this.convertLocationAttributes(use, attrs);
            this.convertRoleAttributes(use, attrs);
            this.convertAttributes(use, attrs);
            const attrs2: Array<org.openprovenance.prov.model.Attribute> = this.copyAttributes(attrs);
            return this.c.newUsed(this.copyQ(use.getId()), this.copyQ(use.getActivity()), this.copyQ(use.getEntity()), use.getTime(), attrs2);
        }

        public doAction$org_openprovenance_prov_model_WasAssociatedWith(assoc: org.openprovenance.prov.model.WasAssociatedWith): org.openprovenance.prov.model.WasAssociatedWith {
            const attrs: Array<org.openprovenance.prov.model.Attribute> = <any>([]);
            this.convertTypeAttributes(assoc, attrs);
            this.convertLabelAttributes(assoc, attrs);
            this.convertRoleAttributes(assoc, attrs);
            this.convertAttributes(assoc, attrs);
            const attrs2: Array<org.openprovenance.prov.model.Attribute> = this.copyAttributes(attrs);
            return this.c.newWasAssociatedWith(this.copyQ(assoc.getId()), this.copyQ(assoc.getActivity()), this.copyQ(assoc.getAgent()), this.copyQ(assoc.getPlan()), attrs2);
        }

        public doAction$org_openprovenance_prov_model_WasAttributedTo(att: org.openprovenance.prov.model.WasAttributedTo): org.openprovenance.prov.model.WasAttributedTo {
            const attrs: Array<org.openprovenance.prov.model.Attribute> = <any>([]);
            this.convertTypeAttributes(att, attrs);
            this.convertLabelAttributes(att, attrs);
            this.convertAttributes(att, attrs);
            const attrs2: Array<org.openprovenance.prov.model.Attribute> = this.copyAttributes(attrs);
            return this.c.newWasAttributedTo(this.copyQ(att.getId()), this.copyQ(att.getEntity()), this.copyQ(att.getAgent()), attrs2);
        }

        public doAction$org_openprovenance_prov_model_WasDerivedFrom(deriv: org.openprovenance.prov.model.WasDerivedFrom): org.openprovenance.prov.model.WasDerivedFrom {
            const attrs: Array<org.openprovenance.prov.model.Attribute> = <any>([]);
            this.convertTypeAttributes(deriv, attrs);
            this.convertLabelAttributes(deriv, attrs);
            this.convertAttributes(deriv, attrs);
            const attrs2: Array<org.openprovenance.prov.model.Attribute> = this.copyAttributes(attrs);
            return this.c.newWasDerivedFrom(this.copyQ(deriv.getId()), this.copyQ(deriv.getGeneratedEntity()), this.copyQ(deriv.getUsedEntity()), this.copyQ(deriv.getActivity()), this.copyQ(deriv.getGeneration()), this.copyQ(deriv.getUsage()), attrs2);
        }

        public doAction$org_openprovenance_prov_model_WasEndedBy(end: org.openprovenance.prov.model.WasEndedBy): org.openprovenance.prov.model.WasEndedBy {
            const attrs: Array<org.openprovenance.prov.model.Attribute> = <any>([]);
            this.convertTypeAttributes(end, attrs);
            this.convertLabelAttributes(end, attrs);
            this.convertLocationAttributes(end, attrs);
            this.convertRoleAttributes(end, attrs);
            this.convertAttributes(end, attrs);
            const attrs2: Array<org.openprovenance.prov.model.Attribute> = this.copyAttributes(attrs);
            return this.c.newWasEndedBy(this.copyQ(end.getId()), this.copyQ(end.getActivity()), this.copyQ(end.getTrigger()), this.copyQ(end.getEnder()), end.getTime(), attrs2);
        }

        public doAction$org_openprovenance_prov_model_WasGeneratedBy(gen: org.openprovenance.prov.model.WasGeneratedBy): org.openprovenance.prov.model.WasGeneratedBy {
            const attrs: Array<org.openprovenance.prov.model.Attribute> = <any>([]);
            this.convertTypeAttributes(gen, attrs);
            this.convertLabelAttributes(gen, attrs);
            this.convertLocationAttributes(gen, attrs);
            this.convertRoleAttributes(gen, attrs);
            this.convertAttributes(gen, attrs);
            const attrs2: Array<org.openprovenance.prov.model.Attribute> = this.copyAttributes(attrs);
            return this.c.newWasGeneratedBy(this.copyQ(gen.getId()), this.copyQ(gen.getEntity()), this.copyQ(gen.getActivity()), gen.getTime(), attrs2);
        }

        public doAction$org_openprovenance_prov_model_WasInfluencedBy(infl: org.openprovenance.prov.model.WasInfluencedBy): org.openprovenance.prov.model.WasInfluencedBy {
            const attrs: Array<org.openprovenance.prov.model.Attribute> = <any>([]);
            this.convertTypeAttributes(infl, attrs);
            this.convertLabelAttributes(infl, attrs);
            this.convertAttributes(infl, attrs);
            const attrs2: Array<org.openprovenance.prov.model.Attribute> = this.copyAttributes(attrs);
            return this.c.newWasInfluencedBy(this.copyQ(infl.getId()), this.copyQ(infl.getInfluencee()), this.copyQ(infl.getInfluencer()), attrs2);
        }

        public doAction$org_openprovenance_prov_model_WasInformedBy(inf: org.openprovenance.prov.model.WasInformedBy): org.openprovenance.prov.model.WasInformedBy {
            const attrs: Array<org.openprovenance.prov.model.Attribute> = <any>([]);
            this.convertTypeAttributes(inf, attrs);
            this.convertLabelAttributes(inf, attrs);
            this.convertAttributes(inf, attrs);
            const attrs2: Array<org.openprovenance.prov.model.Attribute> = this.copyAttributes(attrs);
            return this.c.newWasInformedBy(this.copyQ(inf.getId()), this.copyQ(inf.getInformed()), this.copyQ(inf.getInformant()), attrs2);
        }

        public doAction$org_openprovenance_prov_model_WasInvalidatedBy(inv: org.openprovenance.prov.model.WasInvalidatedBy): org.openprovenance.prov.model.WasInvalidatedBy {
            const attrs: Array<org.openprovenance.prov.model.Attribute> = <any>([]);
            this.convertTypeAttributes(inv, attrs);
            this.convertLabelAttributes(inv, attrs);
            this.convertLocationAttributes(inv, attrs);
            this.convertRoleAttributes(inv, attrs);
            this.convertAttributes(inv, attrs);
            const attrs2: Array<org.openprovenance.prov.model.Attribute> = this.copyAttributes(attrs);
            return this.c.newWasInvalidatedBy(this.copyQ(inv.getId()), this.copyQ(inv.getEntity()), this.copyQ(inv.getActivity()), inv.getTime(), attrs2);
        }

        public doAction$org_openprovenance_prov_model_WasStartedBy(start: org.openprovenance.prov.model.WasStartedBy): org.openprovenance.prov.model.WasStartedBy {
            const attrs: Array<org.openprovenance.prov.model.Attribute> = <any>([]);
            this.convertTypeAttributes(start, attrs);
            this.convertLabelAttributes(start, attrs);
            this.convertLocationAttributes(start, attrs);
            this.convertRoleAttributes(start, attrs);
            this.convertAttributes(start, attrs);
            const attrs2: Array<org.openprovenance.prov.model.Attribute> = this.copyAttributes(attrs);
            return this.c.newWasStartedBy(this.copyQ(start.getId()), this.copyQ(start.getActivity()), this.copyQ(start.getTrigger()), this.copyQ(start.getStarter()), start.getTime(), attrs2);
        }

        public convertAttributes(e: org.openprovenance.prov.model.HasOther, acc: Array<org.openprovenance.prov.model.Attribute>): Array<org.openprovenance.prov.model.Attribute> {
            const ll: Array<any> = e.getOther();
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(acc, <Array<org.openprovenance.prov.model.Attribute>><any>ll);
            return acc;
        }

        public convertLabelAttributes(e: org.openprovenance.prov.model.HasLabel, acc: Array<org.openprovenance.prov.model.Attribute>): Array<org.openprovenance.prov.model.Attribute> {
            const labels: Array<org.openprovenance.prov.model.LangString> = e.getLabel();
            for(let index192=0; index192 < labels.length; index192++) {
                let label = labels[index192];
                {
                    /* add */(acc.push(this.pFactory.newAttribute$org_openprovenance_prov_model_Attribute_AttributeKind$java_lang_Object$org_openprovenance_prov_model_QualifiedName(org.openprovenance.prov.model.Attribute.AttributeKind.PROV_LABEL, label, this.pFactory.getName().XSD_STRING))>0);
                }
            }
            return acc;
        }

        public convertLocationAttributes(e: org.openprovenance.prov.model.HasLocation, acc: Array<org.openprovenance.prov.model.Attribute>): Array<org.openprovenance.prov.model.Attribute> {
            const locations: Array<org.openprovenance.prov.model.Location> = e.getLocation();
            for(let index193=0; index193 < locations.length; index193++) {
                let location = locations[index193];
                {
                    /* add */(acc.push(this.pFactory.newAttribute$org_openprovenance_prov_model_Attribute_AttributeKind$java_lang_Object$org_openprovenance_prov_model_QualifiedName(org.openprovenance.prov.model.Attribute.AttributeKind.PROV_LOCATION, location.getValue(), location.getType()))>0);
                }
            }
            return acc;
        }

        public convertRoleAttributes(e: org.openprovenance.prov.model.HasRole, acc: Array<org.openprovenance.prov.model.Attribute>): Array<org.openprovenance.prov.model.Attribute> {
            const roles: Array<org.openprovenance.prov.model.Role> = e.getRole();
            for(let index194=0; index194 < roles.length; index194++) {
                let role = roles[index194];
                {
                    /* add */(acc.push(this.pFactory.newAttribute$org_openprovenance_prov_model_Attribute_AttributeKind$java_lang_Object$org_openprovenance_prov_model_QualifiedName(org.openprovenance.prov.model.Attribute.AttributeKind.PROV_ROLE, role.getValue(), role.getType()))>0);
                }
            }
            return acc;
        }

        public convertTypeAttributes(e: org.openprovenance.prov.model.HasType, acc: Array<org.openprovenance.prov.model.Attribute>): Array<org.openprovenance.prov.model.Attribute> {
            const types: Array<org.openprovenance.prov.model.Type> = e.getType();
            for(let index195=0; index195 < types.length; index195++) {
                let type = types[index195];
                {
                    /* add */(acc.push(this.pFactory.newAttribute$org_openprovenance_prov_model_Attribute_AttributeKind$java_lang_Object$org_openprovenance_prov_model_QualifiedName(org.openprovenance.prov.model.Attribute.AttributeKind.PROV_TYPE, type.getValue(), type.getType()))>0);
                }
            }
            return acc;
        }

        public convertValueAttributes(e: org.openprovenance.prov.model.HasValue, acc: Array<org.openprovenance.prov.model.Attribute>): any {
            const value: org.openprovenance.prov.model.Value = e.getValue();
            if (value == null)return acc;
            /* add */(acc.push(this.pFactory.newAttribute$org_openprovenance_prov_model_Attribute_AttributeKind$java_lang_Object$org_openprovenance_prov_model_QualifiedName(org.openprovenance.prov.model.Attribute.AttributeKind.PROV_VALUE, value.getValue(), value.getType()))>0);
            return acc;
        }
    }
    BeanTraversal["__class"] = "org.openprovenance.prov.model.BeanTraversal";
    BeanTraversal["__interfaces"] = ["org.openprovenance.prov.model.StatementActionValue"];


}

