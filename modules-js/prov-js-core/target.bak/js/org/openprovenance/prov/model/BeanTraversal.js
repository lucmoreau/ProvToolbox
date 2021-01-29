/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var org;
(function (org) {
    var openprovenance;
    (function (openprovenance) {
        var prov;
        (function (prov) {
            var model;
            (function (model) {
                /**
                 * Generic Traversal of a PROV model bean.
                 * Makes use of the "visitor" in {@link ProvUtilities#doAction(StatementOrBundle, StatementActionValue)}
                 *
                 * @author lavm
                 * @param {*} c
                 * @param {org.openprovenance.prov.model.ProvFactory} pFactory
                 * @class
                 */
                class BeanTraversal {
                    constructor(c, pFactory) {
                        if (this.c === undefined) {
                            this.c = null;
                        }
                        if (this.pFactory === undefined) {
                            this.pFactory = null;
                        }
                        this.u = new org.openprovenance.prov.model.ProvUtilities();
                        this.c = c;
                        this.pFactory = pFactory;
                    }
                    doAction$org_openprovenance_prov_model_ActedOnBehalfOf(del) {
                        const attrs = ([]);
                        this.convertTypeAttributes(del, attrs);
                        this.convertLabelAttributes(del, attrs);
                        this.convertAttributes(del, attrs);
                        const attrs2 = this.copyAttributes(attrs);
                        return this.c.newActedOnBehalfOf(this.copyQ(del.getId()), this.copyQ(del.getDelegate()), this.copyQ(del.getResponsible()), this.copyQ(del.getActivity()), attrs2);
                    }
                    doAction$org_openprovenance_prov_model_Activity(e) {
                        const attrs = ([]);
                        this.convertTypeAttributes(e, attrs);
                        this.convertLabelAttributes(e, attrs);
                        this.convertLocationAttributes(e, attrs);
                        this.convertAttributes(e, attrs);
                        const attrs2 = this.copyAttributes(attrs);
                        return this.c.newActivity(this.copyQ(e.getId()), e.getStartTime(), e.getEndTime(), attrs2);
                    }
                    doAction$org_openprovenance_prov_model_Agent(e) {
                        const attrs = ([]);
                        this.convertTypeAttributes(e, attrs);
                        this.convertLabelAttributes(e, attrs);
                        this.convertLocationAttributes(e, attrs);
                        this.convertAttributes(e, attrs);
                        const attrs2 = this.copyAttributes(attrs);
                        return this.c.newAgent(this.copyQ(e.getId()), attrs2);
                    }
                    doAction$org_openprovenance_prov_model_AlternateOf(o) {
                        return this.c.newAlternateOf(this.copyQ(o.getAlternate1()), this.copyQ(o.getAlternate2()));
                    }
                    doAction$org_openprovenance_prov_model_DerivedByInsertionFrom(o) {
                        const attrs = ([]);
                        this.convertTypeAttributes(o, attrs);
                        this.convertLabelAttributes(o, attrs);
                        this.convertAttributes(o, attrs);
                        return this.c.newDerivedByInsertionFrom(o.getId(), o.getNewDictionary(), o.getOldDictionary(), o.getKeyEntityPair(), attrs);
                    }
                    doAction$org_openprovenance_prov_model_DerivedByRemovalFrom(o) {
                        const attrs = ([]);
                        this.convertTypeAttributes(o, attrs);
                        this.convertLabelAttributes(o, attrs);
                        this.convertAttributes(o, attrs);
                        return this.c.newDerivedByRemovalFrom(o.getId(), o.getNewDictionary(), o.getOldDictionary(), o.getKey(), attrs);
                    }
                    doAction$org_openprovenance_prov_model_DictionaryMembership(o) {
                        return this.c.newDictionaryMembership(o.getDictionary(), o.getKeyEntityPair());
                    }
                    doAction$org_openprovenance_prov_model_Document(doc) {
                        const bRecords = ([]);
                        const sRecords = ([]);
                        const docNamespace = doc.getNamespace();
                        org.openprovenance.prov.model.Namespace.withThreadNamespace(docNamespace);
                        this.c.startDocument(doc.getNamespace());
                        {
                            let array182 = this.u.getStatement$org_openprovenance_prov_model_Document(doc);
                            for (let index181 = 0; index181 < array182.length; index181++) {
                                let s = array182[index181];
                                {
                                    /* add */ (sRecords.push(this.u.doAction$org_openprovenance_prov_model_StatementOrBundle$org_openprovenance_prov_model_StatementActionValue(s, this)) > 0);
                                }
                            }
                        }
                        {
                            let array184 = this.u.getNamedBundle(doc);
                            for (let index183 = 0; index183 < array184.length; index183++) {
                                let bu = array184[index183];
                                {
                                    org.openprovenance.prov.model.Namespace.withThreadNamespace(new org.openprovenance.prov.model.Namespace(docNamespace));
                                    const o = this.doAction$org_openprovenance_prov_model_Bundle$org_openprovenance_prov_model_ProvUtilities(bu, this.u);
                                    if (o != null) /* add */
                                        (bRecords.push(o) > 0);
                                }
                            }
                        }
                        return this.c.newDocument(doc.getNamespace(), sRecords, bRecords);
                    }
                    doAction$org_openprovenance_prov_model_Entity(e) {
                        const attrs = ([]);
                        this.convertTypeAttributes(e, attrs);
                        this.convertLabelAttributes(e, attrs);
                        this.convertLocationAttributes(e, attrs);
                        this.convertValueAttributes(e, attrs);
                        this.convertAttributes(e, attrs);
                        const attrs2 = this.copyAttributes(attrs);
                        return this.c.newEntity(this.copyQ(e.getId()), attrs2);
                    }
                    copyAttributes(attrs) {
                        const attrs2 = ([]);
                        for (let index185 = 0; index185 < attrs.length; index185++) {
                            let attr = attrs[index185];
                            {
                                /* add */ (attrs2.push(this.pFactory.newAttribute$org_openprovenance_prov_model_QualifiedName$java_lang_Object$org_openprovenance_prov_model_QualifiedName(this.copyQ(attr.getElementName()), this.copyValue(attr.getValue()), this.copyQ(attr.getType()))) > 0);
                            }
                        }
                        return attrs2;
                    }
                    copyValue(value) {
                        if (value != null && (value.constructor != null && value.constructor["__interfaces"] != null && value.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.LangString") >= 0)) {
                            const ls = value;
                            return this.pFactory.newInternationalizedString$java_lang_String$java_lang_String(ls.getValue(), ls.getLang());
                        }
                        if (value != null && (value.constructor != null && value.constructor["__interfaces"] != null && value.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) {
                            const qn = value;
                            return this.copyQ(qn);
                        }
                        return value;
                    }
                    copyQ(qn) {
                        if (qn == null)
                            return null;
                        return this.pFactory.newQualifiedName$java_lang_String$java_lang_String$java_lang_String(qn.getNamespaceURI(), qn.getLocalPart(), qn.getPrefix());
                    }
                    doAction$org_openprovenance_prov_model_HadMember(o) {
                        const qq = ([]);
                        if (o.getEntity() != null) {
                            {
                                let array187 = o.getEntity();
                                for (let index186 = 0; index186 < array187.length; index186++) {
                                    let eid = array187[index186];
                                    {
                                        /* add */ (qq.push(this.copyQ(eid)) > 0);
                                    }
                                }
                            }
                        }
                        return this.c.newHadMember(this.copyQ(o.getCollection()), qq);
                    }
                    doAction$org_openprovenance_prov_model_MentionOf(o) {
                        return this.c.newMentionOf(o.getSpecificEntity(), o.getGeneralEntity(), o.getBundle());
                    }
                    doAction$org_openprovenance_prov_model_Bundle$org_openprovenance_prov_model_ProvUtilities(b, u) {
                        const sRecords = ([]);
                        const bundleId = this.copyQ(b.getId());
                        const old = org.openprovenance.prov.model.Namespace.getThreadNamespace();
                        let bundleNamespace;
                        if (b.getNamespace() != null) {
                            bundleNamespace = new org.openprovenance.prov.model.Namespace(b.getNamespace());
                        }
                        else {
                            bundleNamespace = new org.openprovenance.prov.model.Namespace();
                        }
                        bundleNamespace.setParent(new org.openprovenance.prov.model.Namespace(old));
                        org.openprovenance.prov.model.Namespace.withThreadNamespace(bundleNamespace);
                        this.c.startBundle(bundleId, b.getNamespace());
                        {
                            let array189 = u.getStatement$org_openprovenance_prov_model_Bundle(b);
                            for (let index188 = 0; index188 < array189.length; index188++) {
                                let s = array189[index188];
                                {
                                    /* add */ (sRecords.push(u.doAction$org_openprovenance_prov_model_StatementOrBundle$org_openprovenance_prov_model_StatementActionValue(s, this)) > 0);
                                }
                            }
                        }
                        return this.c.newNamedBundle(bundleId, b.getNamespace(), sRecords);
                    }
                    doAction(b, u) {
                        if (((b != null && (b.constructor != null && b.constructor["__interfaces"] != null && b.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Bundle") >= 0)) || b === null) && ((u != null && u instanceof org.openprovenance.prov.model.ProvUtilities) || u === null)) {
                            return this.doAction$org_openprovenance_prov_model_Bundle$org_openprovenance_prov_model_ProvUtilities(b, u);
                        }
                        else if (((b != null && (b.constructor != null && b.constructor["__interfaces"] != null && b.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.ActedOnBehalfOf") >= 0)) || b === null) && u === undefined) {
                            return this.doAction$org_openprovenance_prov_model_ActedOnBehalfOf(b);
                        }
                        else if (((b != null && (b.constructor != null && b.constructor["__interfaces"] != null && b.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Activity") >= 0)) || b === null) && u === undefined) {
                            return this.doAction$org_openprovenance_prov_model_Activity(b);
                        }
                        else if (((b != null && (b.constructor != null && b.constructor["__interfaces"] != null && b.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Agent") >= 0)) || b === null) && u === undefined) {
                            return this.doAction$org_openprovenance_prov_model_Agent(b);
                        }
                        else if (((b != null && (b.constructor != null && b.constructor["__interfaces"] != null && b.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.AlternateOf") >= 0)) || b === null) && u === undefined) {
                            return this.doAction$org_openprovenance_prov_model_AlternateOf(b);
                        }
                        else if (((b != null && (b.constructor != null && b.constructor["__interfaces"] != null && b.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.DerivedByInsertionFrom") >= 0)) || b === null) && u === undefined) {
                            return this.doAction$org_openprovenance_prov_model_DerivedByInsertionFrom(b);
                        }
                        else if (((b != null && (b.constructor != null && b.constructor["__interfaces"] != null && b.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.DerivedByRemovalFrom") >= 0)) || b === null) && u === undefined) {
                            return this.doAction$org_openprovenance_prov_model_DerivedByRemovalFrom(b);
                        }
                        else if (((b != null && (b.constructor != null && b.constructor["__interfaces"] != null && b.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.DictionaryMembership") >= 0)) || b === null) && u === undefined) {
                            return this.doAction$org_openprovenance_prov_model_DictionaryMembership(b);
                        }
                        else if (((b != null && (b.constructor != null && b.constructor["__interfaces"] != null && b.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Document") >= 0)) || b === null) && u === undefined) {
                            return this.doAction$org_openprovenance_prov_model_Document(b);
                        }
                        else if (((b != null && (b.constructor != null && b.constructor["__interfaces"] != null && b.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Entity") >= 0)) || b === null) && u === undefined) {
                            return this.doAction$org_openprovenance_prov_model_Entity(b);
                        }
                        else if (((b != null && (b.constructor != null && b.constructor["__interfaces"] != null && b.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HadMember") >= 0)) || b === null) && u === undefined) {
                            return this.doAction$org_openprovenance_prov_model_HadMember(b);
                        }
                        else if (((b != null && (b.constructor != null && b.constructor["__interfaces"] != null && b.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.MentionOf") >= 0)) || b === null) && u === undefined) {
                            return this.doAction$org_openprovenance_prov_model_MentionOf(b);
                        }
                        else if (((b != null && (b.constructor != null && b.constructor["__interfaces"] != null && b.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.extension.QualifiedSpecializationOf") >= 0)) || b === null) && u === undefined) {
                            return this.doAction$org_openprovenance_prov_model_extension_QualifiedSpecializationOf(b);
                        }
                        else if (((b != null && (b.constructor != null && b.constructor["__interfaces"] != null && b.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.SpecializationOf") >= 0)) || b === null) && u === undefined) {
                            return this.doAction$org_openprovenance_prov_model_SpecializationOf(b);
                        }
                        else if (((b != null && (b.constructor != null && b.constructor["__interfaces"] != null && b.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.extension.QualifiedAlternateOf") >= 0)) || b === null) && u === undefined) {
                            return this.doAction$org_openprovenance_prov_model_extension_QualifiedAlternateOf(b);
                        }
                        else if (((b != null && (b.constructor != null && b.constructor["__interfaces"] != null && b.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.extension.QualifiedHadMember") >= 0)) || b === null) && u === undefined) {
                            return this.doAction$org_openprovenance_prov_model_extension_QualifiedHadMember(b);
                        }
                        else if (((b != null && (b.constructor != null && b.constructor["__interfaces"] != null && b.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Used") >= 0)) || b === null) && u === undefined) {
                            return this.doAction$org_openprovenance_prov_model_Used(b);
                        }
                        else if (((b != null && (b.constructor != null && b.constructor["__interfaces"] != null && b.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasAssociatedWith") >= 0)) || b === null) && u === undefined) {
                            return this.doAction$org_openprovenance_prov_model_WasAssociatedWith(b);
                        }
                        else if (((b != null && (b.constructor != null && b.constructor["__interfaces"] != null && b.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasAttributedTo") >= 0)) || b === null) && u === undefined) {
                            return this.doAction$org_openprovenance_prov_model_WasAttributedTo(b);
                        }
                        else if (((b != null && (b.constructor != null && b.constructor["__interfaces"] != null && b.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasDerivedFrom") >= 0)) || b === null) && u === undefined) {
                            return this.doAction$org_openprovenance_prov_model_WasDerivedFrom(b);
                        }
                        else if (((b != null && (b.constructor != null && b.constructor["__interfaces"] != null && b.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasEndedBy") >= 0)) || b === null) && u === undefined) {
                            return this.doAction$org_openprovenance_prov_model_WasEndedBy(b);
                        }
                        else if (((b != null && (b.constructor != null && b.constructor["__interfaces"] != null && b.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasGeneratedBy") >= 0)) || b === null) && u === undefined) {
                            return this.doAction$org_openprovenance_prov_model_WasGeneratedBy(b);
                        }
                        else if (((b != null && (b.constructor != null && b.constructor["__interfaces"] != null && b.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInfluencedBy") >= 0)) || b === null) && u === undefined) {
                            return this.doAction$org_openprovenance_prov_model_WasInfluencedBy(b);
                        }
                        else if (((b != null && (b.constructor != null && b.constructor["__interfaces"] != null && b.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInformedBy") >= 0)) || b === null) && u === undefined) {
                            return this.doAction$org_openprovenance_prov_model_WasInformedBy(b);
                        }
                        else if (((b != null && (b.constructor != null && b.constructor["__interfaces"] != null && b.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInvalidatedBy") >= 0)) || b === null) && u === undefined) {
                            return this.doAction$org_openprovenance_prov_model_WasInvalidatedBy(b);
                        }
                        else if (((b != null && (b.constructor != null && b.constructor["__interfaces"] != null && b.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasStartedBy") >= 0)) || b === null) && u === undefined) {
                            return this.doAction$org_openprovenance_prov_model_WasStartedBy(b);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    doAction$org_openprovenance_prov_model_SpecializationOf(o) {
                        return this.c.newSpecializationOf(this.copyQ(o.getSpecificEntity()), this.copyQ(o.getGeneralEntity()));
                    }
                    doAction$org_openprovenance_prov_model_extension_QualifiedSpecializationOf(o) {
                        const attrs = ([]);
                        this.convertTypeAttributes(o, attrs);
                        this.convertLabelAttributes(o, attrs);
                        this.convertAttributes(o, attrs);
                        const c2 = this.c;
                        const attrs2 = this.copyAttributes(attrs);
                        return c2.newQualifiedSpecializationOf(this.copyQ(o.getId()), this.copyQ(o.getSpecificEntity()), this.copyQ(o.getGeneralEntity()), attrs2);
                    }
                    doAction$org_openprovenance_prov_model_extension_QualifiedAlternateOf(o) {
                        const attrs = ([]);
                        this.convertTypeAttributes(o, attrs);
                        this.convertLabelAttributes(o, attrs);
                        this.convertAttributes(o, attrs);
                        const attrs2 = this.copyAttributes(attrs);
                        const c2 = this.c;
                        return c2.newQualifiedAlternateOf(this.copyQ(o.getId()), this.copyQ(o.getAlternate1()), this.copyQ(o.getAlternate2()), attrs2);
                    }
                    doAction$org_openprovenance_prov_model_extension_QualifiedHadMember(o) {
                        const attrs = ([]);
                        this.convertTypeAttributes(o, attrs);
                        this.convertLabelAttributes(o, attrs);
                        this.convertAttributes(o, attrs);
                        const attrs2 = this.copyAttributes(attrs);
                        const c2 = this.c;
                        const qq = ([]);
                        if (o.getEntity() != null) {
                            {
                                let array191 = o.getEntity();
                                for (let index190 = 0; index190 < array191.length; index190++) {
                                    let eid = array191[index190];
                                    {
                                        /* add */ (qq.push(this.copyQ(eid)) > 0);
                                    }
                                }
                            }
                        }
                        return c2.newQualifiedHadMember(this.copyQ(o.getId()), this.copyQ(o.getCollection()), qq, attrs2);
                    }
                    doAction$org_openprovenance_prov_model_Used(use) {
                        const attrs = ([]);
                        this.convertTypeAttributes(use, attrs);
                        this.convertLabelAttributes(use, attrs);
                        this.convertLocationAttributes(use, attrs);
                        this.convertRoleAttributes(use, attrs);
                        this.convertAttributes(use, attrs);
                        const attrs2 = this.copyAttributes(attrs);
                        return this.c.newUsed(this.copyQ(use.getId()), this.copyQ(use.getActivity()), this.copyQ(use.getEntity()), use.getTime(), attrs2);
                    }
                    doAction$org_openprovenance_prov_model_WasAssociatedWith(assoc) {
                        const attrs = ([]);
                        this.convertTypeAttributes(assoc, attrs);
                        this.convertLabelAttributes(assoc, attrs);
                        this.convertRoleAttributes(assoc, attrs);
                        this.convertAttributes(assoc, attrs);
                        const attrs2 = this.copyAttributes(attrs);
                        return this.c.newWasAssociatedWith(this.copyQ(assoc.getId()), this.copyQ(assoc.getActivity()), this.copyQ(assoc.getAgent()), this.copyQ(assoc.getPlan()), attrs2);
                    }
                    doAction$org_openprovenance_prov_model_WasAttributedTo(att) {
                        const attrs = ([]);
                        this.convertTypeAttributes(att, attrs);
                        this.convertLabelAttributes(att, attrs);
                        this.convertAttributes(att, attrs);
                        const attrs2 = this.copyAttributes(attrs);
                        return this.c.newWasAttributedTo(this.copyQ(att.getId()), this.copyQ(att.getEntity()), this.copyQ(att.getAgent()), attrs2);
                    }
                    doAction$org_openprovenance_prov_model_WasDerivedFrom(deriv) {
                        const attrs = ([]);
                        this.convertTypeAttributes(deriv, attrs);
                        this.convertLabelAttributes(deriv, attrs);
                        this.convertAttributes(deriv, attrs);
                        const attrs2 = this.copyAttributes(attrs);
                        return this.c.newWasDerivedFrom(this.copyQ(deriv.getId()), this.copyQ(deriv.getGeneratedEntity()), this.copyQ(deriv.getUsedEntity()), this.copyQ(deriv.getActivity()), this.copyQ(deriv.getGeneration()), this.copyQ(deriv.getUsage()), attrs2);
                    }
                    doAction$org_openprovenance_prov_model_WasEndedBy(end) {
                        const attrs = ([]);
                        this.convertTypeAttributes(end, attrs);
                        this.convertLabelAttributes(end, attrs);
                        this.convertLocationAttributes(end, attrs);
                        this.convertRoleAttributes(end, attrs);
                        this.convertAttributes(end, attrs);
                        const attrs2 = this.copyAttributes(attrs);
                        return this.c.newWasEndedBy(this.copyQ(end.getId()), this.copyQ(end.getActivity()), this.copyQ(end.getTrigger()), this.copyQ(end.getEnder()), end.getTime(), attrs2);
                    }
                    doAction$org_openprovenance_prov_model_WasGeneratedBy(gen) {
                        const attrs = ([]);
                        this.convertTypeAttributes(gen, attrs);
                        this.convertLabelAttributes(gen, attrs);
                        this.convertLocationAttributes(gen, attrs);
                        this.convertRoleAttributes(gen, attrs);
                        this.convertAttributes(gen, attrs);
                        const attrs2 = this.copyAttributes(attrs);
                        return this.c.newWasGeneratedBy(this.copyQ(gen.getId()), this.copyQ(gen.getEntity()), this.copyQ(gen.getActivity()), gen.getTime(), attrs2);
                    }
                    doAction$org_openprovenance_prov_model_WasInfluencedBy(infl) {
                        const attrs = ([]);
                        this.convertTypeAttributes(infl, attrs);
                        this.convertLabelAttributes(infl, attrs);
                        this.convertAttributes(infl, attrs);
                        const attrs2 = this.copyAttributes(attrs);
                        return this.c.newWasInfluencedBy(this.copyQ(infl.getId()), this.copyQ(infl.getInfluencee()), this.copyQ(infl.getInfluencer()), attrs2);
                    }
                    doAction$org_openprovenance_prov_model_WasInformedBy(inf) {
                        const attrs = ([]);
                        this.convertTypeAttributes(inf, attrs);
                        this.convertLabelAttributes(inf, attrs);
                        this.convertAttributes(inf, attrs);
                        const attrs2 = this.copyAttributes(attrs);
                        return this.c.newWasInformedBy(this.copyQ(inf.getId()), this.copyQ(inf.getInformed()), this.copyQ(inf.getInformant()), attrs2);
                    }
                    doAction$org_openprovenance_prov_model_WasInvalidatedBy(inv) {
                        const attrs = ([]);
                        this.convertTypeAttributes(inv, attrs);
                        this.convertLabelAttributes(inv, attrs);
                        this.convertLocationAttributes(inv, attrs);
                        this.convertRoleAttributes(inv, attrs);
                        this.convertAttributes(inv, attrs);
                        const attrs2 = this.copyAttributes(attrs);
                        return this.c.newWasInvalidatedBy(this.copyQ(inv.getId()), this.copyQ(inv.getEntity()), this.copyQ(inv.getActivity()), inv.getTime(), attrs2);
                    }
                    doAction$org_openprovenance_prov_model_WasStartedBy(start) {
                        const attrs = ([]);
                        this.convertTypeAttributes(start, attrs);
                        this.convertLabelAttributes(start, attrs);
                        this.convertLocationAttributes(start, attrs);
                        this.convertRoleAttributes(start, attrs);
                        this.convertAttributes(start, attrs);
                        const attrs2 = this.copyAttributes(attrs);
                        return this.c.newWasStartedBy(this.copyQ(start.getId()), this.copyQ(start.getActivity()), this.copyQ(start.getTrigger()), this.copyQ(start.getStarter()), start.getTime(), attrs2);
                    }
                    convertAttributes(e, acc) {
                        const ll = e.getOther();
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(acc, ll);
                        return acc;
                    }
                    convertLabelAttributes(e, acc) {
                        const labels = e.getLabel();
                        for (let index192 = 0; index192 < labels.length; index192++) {
                            let label = labels[index192];
                            {
                                /* add */ (acc.push(this.pFactory.newAttribute$org_openprovenance_prov_model_Attribute_AttributeKind$java_lang_Object$org_openprovenance_prov_model_QualifiedName(org.openprovenance.prov.model.Attribute.AttributeKind.PROV_LABEL, label, this.pFactory.getName().XSD_STRING)) > 0);
                            }
                        }
                        return acc;
                    }
                    convertLocationAttributes(e, acc) {
                        const locations = e.getLocation();
                        for (let index193 = 0; index193 < locations.length; index193++) {
                            let location = locations[index193];
                            {
                                /* add */ (acc.push(this.pFactory.newAttribute$org_openprovenance_prov_model_Attribute_AttributeKind$java_lang_Object$org_openprovenance_prov_model_QualifiedName(org.openprovenance.prov.model.Attribute.AttributeKind.PROV_LOCATION, location.getValue(), location.getType())) > 0);
                            }
                        }
                        return acc;
                    }
                    convertRoleAttributes(e, acc) {
                        const roles = e.getRole();
                        for (let index194 = 0; index194 < roles.length; index194++) {
                            let role = roles[index194];
                            {
                                /* add */ (acc.push(this.pFactory.newAttribute$org_openprovenance_prov_model_Attribute_AttributeKind$java_lang_Object$org_openprovenance_prov_model_QualifiedName(org.openprovenance.prov.model.Attribute.AttributeKind.PROV_ROLE, role.getValue(), role.getType())) > 0);
                            }
                        }
                        return acc;
                    }
                    convertTypeAttributes(e, acc) {
                        const types = e.getType();
                        for (let index195 = 0; index195 < types.length; index195++) {
                            let type = types[index195];
                            {
                                /* add */ (acc.push(this.pFactory.newAttribute$org_openprovenance_prov_model_Attribute_AttributeKind$java_lang_Object$org_openprovenance_prov_model_QualifiedName(org.openprovenance.prov.model.Attribute.AttributeKind.PROV_TYPE, type.getValue(), type.getType())) > 0);
                            }
                        }
                        return acc;
                    }
                    convertValueAttributes(e, acc) {
                        const value = e.getValue();
                        if (value == null)
                            return acc;
                        /* add */ (acc.push(this.pFactory.newAttribute$org_openprovenance_prov_model_Attribute_AttributeKind$java_lang_Object$org_openprovenance_prov_model_QualifiedName(org.openprovenance.prov.model.Attribute.AttributeKind.PROV_VALUE, value.getValue(), value.getType())) > 0);
                        return acc;
                    }
                }
                model.BeanTraversal = BeanTraversal;
                BeanTraversal["__class"] = "org.openprovenance.prov.model.BeanTraversal";
                BeanTraversal["__interfaces"] = ["org.openprovenance.prov.model.StatementActionValue"];
            })(model = prov.model || (prov.model = {}));
        })(prov = openprovenance.prov || (openprovenance.prov = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
