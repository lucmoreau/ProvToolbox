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
                 * Utilities for manipulating PROV Descriptions.
                 * @class
                 */
                class ProvUtilities {
                    getRelations$org_openprovenance_prov_model_Document(d) {
                        return this.getObject("org.openprovenance.prov.model.Relation", d.getStatementOrBundle());
                    }
                    getRelations(d) {
                        if (((d != null && (d.constructor != null && d.constructor["__interfaces"] != null && d.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Document") >= 0)) || d === null)) {
                            return this.getRelations$org_openprovenance_prov_model_Document(d);
                        }
                        else if (((d != null && (d.constructor != null && d.constructor["__interfaces"] != null && d.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Bundle") >= 0)) || d === null)) {
                            return this.getRelations$org_openprovenance_prov_model_Bundle(d);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    getEntity$org_openprovenance_prov_model_Document(d) {
                        return this.getObject("org.openprovenance.prov.model.Entity", d.getStatementOrBundle());
                    }
                    getEntity(d) {
                        if (((d != null && (d.constructor != null && d.constructor["__interfaces"] != null && d.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Document") >= 0)) || d === null)) {
                            return this.getEntity$org_openprovenance_prov_model_Document(d);
                        }
                        else if (((d != null && (d.constructor != null && d.constructor["__interfaces"] != null && d.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Bundle") >= 0)) || d === null)) {
                            return this.getEntity$org_openprovenance_prov_model_Bundle(d);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    getActivity$org_openprovenance_prov_model_Document(d) {
                        return this.getObject("org.openprovenance.prov.model.Activity", d.getStatementOrBundle());
                    }
                    getActivity(d) {
                        if (((d != null && (d.constructor != null && d.constructor["__interfaces"] != null && d.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Document") >= 0)) || d === null)) {
                            return this.getActivity$org_openprovenance_prov_model_Document(d);
                        }
                        else if (((d != null && (d.constructor != null && d.constructor["__interfaces"] != null && d.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Bundle") >= 0)) || d === null)) {
                            return this.getActivity$org_openprovenance_prov_model_Bundle(d);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    getAgent$org_openprovenance_prov_model_Document(d) {
                        return this.getObject("org.openprovenance.prov.model.Agent", d.getStatementOrBundle());
                    }
                    getAgent(d) {
                        if (((d != null && (d.constructor != null && d.constructor["__interfaces"] != null && d.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Document") >= 0)) || d === null)) {
                            return this.getAgent$org_openprovenance_prov_model_Document(d);
                        }
                        else if (((d != null && (d.constructor != null && d.constructor["__interfaces"] != null && d.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Bundle") >= 0)) || d === null)) {
                            return this.getAgent$org_openprovenance_prov_model_Bundle(d);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    getBundle(d) {
                        return this.getObject("org.openprovenance.prov.model.Bundle", d.getStatementOrBundle());
                    }
                    getRelations$org_openprovenance_prov_model_Bundle(d) {
                        return this.getObject2("org.openprovenance.prov.model.Relation", d.getStatement());
                    }
                    getEntity$org_openprovenance_prov_model_Bundle(d) {
                        return this.getObject2("org.openprovenance.prov.model.Entity", d.getStatement());
                    }
                    getActivity$org_openprovenance_prov_model_Bundle(d) {
                        return this.getObject2("org.openprovenance.prov.model.Activity", d.getStatement());
                    }
                    getAgent$org_openprovenance_prov_model_Bundle(d) {
                        return this.getObject2("org.openprovenance.prov.model.Agent", d.getStatement());
                    }
                    getNamedBundle(d) {
                        return this.getObject("org.openprovenance.prov.model.Bundle", d.getStatementOrBundle());
                    }
                    getStatement$org_openprovenance_prov_model_Document(d) {
                        return this.getObject("org.openprovenance.prov.model.Statement", d.getStatementOrBundle());
                    }
                    getStatement(d) {
                        if (((d != null && (d.constructor != null && d.constructor["__interfaces"] != null && d.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Document") >= 0)) || d === null)) {
                            return this.getStatement$org_openprovenance_prov_model_Document(d);
                        }
                        else if (((d != null && (d.constructor != null && d.constructor["__interfaces"] != null && d.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Bundle") >= 0)) || d === null)) {
                            return this.getStatement$org_openprovenance_prov_model_Bundle(d);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    getStatement$org_openprovenance_prov_model_Bundle(d) {
                        const res = d.getStatement();
                        return res;
                    }
                    getObject(cl, ll) {
                        const res = ([]);
                        for (let index151 = 0; index151 < ll.length; index151++) {
                            let o = ll[index151];
                            {
                                if ( /* isInstance */((c, o) => { if (typeof c === 'string')
                                    return (o.constructor && o.constructor["__interfaces"] && o.constructor["__interfaces"].indexOf(c) >= 0) || (o["__interfaces"] && o["__interfaces"].indexOf(c) >= 0);
                                else if (typeof c === 'function')
                                    return (o instanceof c) || (o.constructor && o.constructor === c); })(cl, o)) {
                                    const o2 = o;
                                    /* add */ (res.push(o2) > 0);
                                }
                            }
                        }
                        return res;
                    }
                    getObject2(cl, ll) {
                        const res = ([]);
                        for (let index152 = 0; index152 < ll.length; index152++) {
                            let o = ll[index152];
                            {
                                if ( /* isInstance */((c, o) => { if (typeof c === 'string')
                                    return (o.constructor && o.constructor["__interfaces"] && o.constructor["__interfaces"].indexOf(c) >= 0) || (o["__interfaces"] && o["__interfaces"].indexOf(c) >= 0);
                                else if (typeof c === 'function')
                                    return (o instanceof c) || (o.constructor && o.constructor === c); })(cl, o)) {
                                    const o2 = o;
                                    /* add */ (res.push(o2) > 0);
                                }
                            }
                        }
                        return res;
                    }
                    getEffect(r) {
                        if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Used") >= 0)) {
                            return r.getActivity();
                        }
                        if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasStartedBy") >= 0)) {
                            return r.getActivity();
                        }
                        if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasEndedBy") >= 0)) {
                            return r.getActivity();
                        }
                        if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasGeneratedBy") >= 0)) {
                            return r.getEntity();
                        }
                        if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasDerivedFrom") >= 0)) {
                            return r.getGeneratedEntity();
                        }
                        if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasAssociatedWith") >= 0)) {
                            return r.getActivity();
                        }
                        if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInvalidatedBy") >= 0)) {
                            return r.getEntity();
                        }
                        if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasAttributedTo") >= 0)) {
                            return r.getEntity();
                        }
                        if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.AlternateOf") >= 0)) {
                            return r.getAlternate1();
                        }
                        if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.SpecializationOf") >= 0)) {
                            return r.getSpecificEntity();
                        }
                        if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HadMember") >= 0)) {
                            return r.getCollection();
                        }
                        if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInformedBy") >= 0)) {
                            return r.getInformed();
                        }
                        if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.MentionOf") >= 0)) {
                            return r.getSpecificEntity();
                        }
                        if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInfluencedBy") >= 0)) {
                            return r.getInfluencee();
                        }
                        if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.ActedOnBehalfOf") >= 0)) {
                            return r.getDelegate();
                        }
                        if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.DerivedByInsertionFrom") >= 0)) {
                            return r.getNewDictionary();
                        }
                        console.info("Unknown relation " + r);
                        throw Object.defineProperty(new Error(), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.UnsupportedOperationException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                    }
                    getCause(r) {
                        if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Used") >= 0)) {
                            return r.getEntity();
                        }
                        if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasGeneratedBy") >= 0)) {
                            return r.getActivity();
                        }
                        if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInvalidatedBy") >= 0)) {
                            return r.getActivity();
                        }
                        if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasStartedBy") >= 0)) {
                            return r.getTrigger();
                        }
                        if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasEndedBy") >= 0)) {
                            return r.getTrigger();
                        }
                        if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasDerivedFrom") >= 0)) {
                            return r.getUsedEntity();
                        }
                        if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInfluencedBy") >= 0)) {
                            return r.getInfluencer();
                        }
                        if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasAssociatedWith") >= 0)) {
                            return r.getAgent();
                        }
                        if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasAttributedTo") >= 0)) {
                            return r.getAgent();
                        }
                        if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.AlternateOf") >= 0)) {
                            return r.getAlternate2();
                        }
                        if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.SpecializationOf") >= 0)) {
                            return r.getGeneralEntity();
                        }
                        if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HadMember") >= 0)) {
                            return /* get */ r.getEntity()[0];
                        }
                        if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.MentionOf") >= 0)) {
                            return r.getGeneralEntity();
                        }
                        if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInformedBy") >= 0)) {
                            return r.getInformant();
                        }
                        if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.ActedOnBehalfOf") >= 0)) {
                            return r.getResponsible();
                        }
                        if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.DerivedByInsertionFrom") >= 0)) {
                            return r.getOldDictionary();
                        }
                        console.info("Unknown relation " + r);
                        throw Object.defineProperty(new Error(), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.UnsupportedOperationException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                    }
                    getOtherCauses(r) {
                        if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasAssociatedWith") >= 0)) {
                            const res = ([]);
                            const e = r.getPlan();
                            if (e == null)
                                return null;
                            /* add */ (res.push(e) > 0);
                            return res;
                        }
                        if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasStartedBy") >= 0)) {
                            const res = ([]);
                            const a = r.getStarter();
                            if (a == null)
                                return null;
                            /* add */ (res.push(a) > 0);
                            return res;
                        }
                        if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.MentionOf") >= 0)) {
                            const res = ([]);
                            const a = r.getBundle();
                            if (a == null)
                                return null;
                            /* add */ (res.push(a) > 0);
                            return res;
                        }
                        if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HadMember") >= 0)) {
                            const res = ([]);
                            const entities = r.getEntity();
                            if ((entities == null) || ( /* size */entities.length <= 1))
                                return null;
                            let first = true;
                            for (let index153 = 0; index153 < entities.length; index153++) {
                                let ee = entities[index153];
                                {
                                    if (!first) /* add */
                                        (res.push(ee) > 0);
                                    first = false;
                                }
                            }
                            return res;
                        }
                        if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasEndedBy") >= 0)) {
                            const res = ([]);
                            const a = r.getEnder();
                            if (a == null)
                                return null;
                            /* add */ (res.push(a) > 0);
                            return res;
                        }
                        if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.ActedOnBehalfOf") >= 0)) {
                            const res = ([]);
                            const a = r.getActivity();
                            if (a == null)
                                return null;
                            /* add */ (res.push(a) > 0);
                            return res;
                        }
                        if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.DerivedByInsertionFrom") >= 0)) {
                            const res = ([]);
                            const dbif = r;
                            {
                                let array155 = dbif.getKeyEntityPair();
                                for (let index154 = 0; index154 < array155.length; index154++) {
                                    let entry = array155[index154];
                                    {
                                        /* add */ (res.push(entry.getEntity()) > 0);
                                    }
                                }
                            }
                            return res;
                        }
                        return null;
                    }
                    attributesWithNamespace(object, namespace) {
                        const ll = object.getOther();
                        const _attrs = new org.openprovenance.prov.model.AttributeProcessor(ll);
                        return _attrs.attributesWithNamespace(namespace);
                    }
                    forAllStatementOrBundle(records, action) {
                        for (let index156 = 0; index156 < records.length; index156++) {
                            let o = records[index156];
                            {
                                this.doAction$org_openprovenance_prov_model_StatementOrBundle$org_openprovenance_prov_model_StatementAction(o, action);
                            }
                        }
                    }
                    forAllStatement(records, action) {
                        for (let index157 = 0; index157 < records.length; index157++) {
                            let o = records[index157];
                            {
                                this.doAction$org_openprovenance_prov_model_StatementOrBundle$org_openprovenance_prov_model_StatementAction(o, action);
                            }
                        }
                    }
                    doAction$org_openprovenance_prov_model_StatementOrBundle$org_openprovenance_prov_model_StatementAction(s, action) {
                        switch ((s.getKind())) {
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_ACTIVITY:
                                action['doAction$org_openprovenance_prov_model_Activity'](s);
                                break;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_AGENT:
                                action['doAction$org_openprovenance_prov_model_Agent'](s);
                                break;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_ALTERNATE:
                                if (s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.extension.QualifiedAlternateOf") >= 0)) {
                                    action['doAction$org_openprovenance_prov_model_extension_QualifiedAlternateOf'](s);
                                }
                                else {
                                    action['doAction$org_openprovenance_prov_model_AlternateOf'](s);
                                }
                                break;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_ASSOCIATION:
                                action['doAction$org_openprovenance_prov_model_WasAssociatedWith'](s);
                                break;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_ATTRIBUTION:
                                action['doAction$org_openprovenance_prov_model_WasAttributedTo'](s);
                                break;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_BUNDLE:
                                action['doAction$org_openprovenance_prov_model_Bundle$org_openprovenance_prov_model_ProvUtilities'](s, this);
                                break;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_COMMUNICATION:
                                action['doAction$org_openprovenance_prov_model_WasInformedBy'](s);
                                break;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_DELEGATION:
                                action['doAction$org_openprovenance_prov_model_ActedOnBehalfOf'](s);
                                break;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_DERIVATION:
                                action['doAction$org_openprovenance_prov_model_WasDerivedFrom'](s);
                                break;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_DICTIONARY_INSERTION:
                                action['doAction$org_openprovenance_prov_model_DerivedByInsertionFrom'](s);
                                break;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_DICTIONARY_MEMBERSHIP:
                                action['doAction$org_openprovenance_prov_model_DictionaryMembership'](s);
                                break;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_DICTIONARY_REMOVAL:
                                action['doAction$org_openprovenance_prov_model_DerivedByRemovalFrom'](s);
                                break;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_END:
                                action['doAction$org_openprovenance_prov_model_WasEndedBy'](s);
                                break;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_ENTITY:
                                action['doAction$org_openprovenance_prov_model_Entity'](s);
                                break;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_GENERATION:
                                action['doAction$org_openprovenance_prov_model_WasGeneratedBy'](s);
                                break;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_INFLUENCE:
                                action['doAction$org_openprovenance_prov_model_WasInfluencedBy'](s);
                                break;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_INVALIDATION:
                                action['doAction$org_openprovenance_prov_model_WasInvalidatedBy'](s);
                                break;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_MEMBERSHIP:
                                if (s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.extension.QualifiedHadMember") >= 0)) {
                                    action['doAction$org_openprovenance_prov_model_extension_QualifiedHadMember'](s);
                                }
                                else {
                                    action['doAction$org_openprovenance_prov_model_HadMember'](s);
                                }
                                break;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_MENTION:
                                action['doAction$org_openprovenance_prov_model_MentionOf'](s);
                                break;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_SPECIALIZATION:
                                if (s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.extension.QualifiedSpecializationOf") >= 0)) {
                                    action['doAction$org_openprovenance_prov_model_extension_QualifiedSpecializationOf'](s);
                                }
                                else {
                                    action['doAction$org_openprovenance_prov_model_SpecializationOf'](s);
                                }
                                break;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_START:
                                action['doAction$org_openprovenance_prov_model_WasStartedBy'](s);
                                break;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_USAGE:
                                action['doAction$org_openprovenance_prov_model_Used'](s);
                                break;
                        }
                    }
                    doAction(s, action) {
                        if (((s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.StatementOrBundle") >= 0)) || s === null) && ((action != null && (action.constructor != null && action.constructor["__interfaces"] != null && action.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.StatementAction") >= 0)) || action === null)) {
                            return this.doAction$org_openprovenance_prov_model_StatementOrBundle$org_openprovenance_prov_model_StatementAction(s, action);
                        }
                        else if (((s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.StatementOrBundle") >= 0)) || s === null) && ((action != null && (action.constructor != null && action.constructor["__interfaces"] != null && action.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.StatementActionValue") >= 0)) || action === null)) {
                            return this.doAction$org_openprovenance_prov_model_StatementOrBundle$org_openprovenance_prov_model_StatementActionValue(s, action);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    doAction$org_openprovenance_prov_model_StatementOrBundle$org_openprovenance_prov_model_StatementActionValue(s, action) {
                        switch ((s.getKind())) {
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_ACTIVITY:
                                return action['doAction$org_openprovenance_prov_model_Activity'](s);
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_AGENT:
                                return action['doAction$org_openprovenance_prov_model_Agent'](s);
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_ALTERNATE:
                                if (s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.extension.QualifiedAlternateOf") >= 0)) {
                                    return action['doAction$org_openprovenance_prov_model_extension_QualifiedAlternateOf'](s);
                                }
                                else {
                                    return action['doAction$org_openprovenance_prov_model_AlternateOf'](s);
                                }
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_ASSOCIATION:
                                return action['doAction$org_openprovenance_prov_model_WasAssociatedWith'](s);
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_ATTRIBUTION:
                                return action['doAction$org_openprovenance_prov_model_WasAttributedTo'](s);
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_BUNDLE:
                                return action['doAction$org_openprovenance_prov_model_Bundle$org_openprovenance_prov_model_ProvUtilities'](s, this);
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_COMMUNICATION:
                                return action['doAction$org_openprovenance_prov_model_WasInformedBy'](s);
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_DELEGATION:
                                return action['doAction$org_openprovenance_prov_model_ActedOnBehalfOf'](s);
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_DERIVATION:
                                return action['doAction$org_openprovenance_prov_model_WasDerivedFrom'](s);
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_DICTIONARY_INSERTION:
                                return action['doAction$org_openprovenance_prov_model_DerivedByInsertionFrom'](s);
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_DICTIONARY_MEMBERSHIP:
                                return action['doAction$org_openprovenance_prov_model_DictionaryMembership'](s);
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_DICTIONARY_REMOVAL:
                                return action['doAction$org_openprovenance_prov_model_DerivedByRemovalFrom'](s);
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_END:
                                return action['doAction$org_openprovenance_prov_model_WasEndedBy'](s);
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_ENTITY:
                                return action['doAction$org_openprovenance_prov_model_Entity'](s);
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_GENERATION:
                                return action['doAction$org_openprovenance_prov_model_WasGeneratedBy'](s);
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_INFLUENCE:
                                return action['doAction$org_openprovenance_prov_model_WasInfluencedBy'](s);
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_INVALIDATION:
                                return action['doAction$org_openprovenance_prov_model_WasInvalidatedBy'](s);
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_MEMBERSHIP:
                                if (s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.extension.QualifiedHadMember") >= 0)) {
                                    return action['doAction$org_openprovenance_prov_model_extension_QualifiedHadMember'](s);
                                }
                                else {
                                    return action['doAction$org_openprovenance_prov_model_HadMember'](s);
                                }
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_MENTION:
                                return action['doAction$org_openprovenance_prov_model_MentionOf'](s);
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_SPECIALIZATION:
                                if (s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.extension.QualifiedSpecializationOf") >= 0)) {
                                    return action['doAction$org_openprovenance_prov_model_extension_QualifiedSpecializationOf'](s);
                                }
                                else {
                                    return action['doAction$org_openprovenance_prov_model_SpecializationOf'](s);
                                }
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_START:
                                return action['doAction$org_openprovenance_prov_model_WasStartedBy'](s);
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_USAGE:
                                return action['doAction$org_openprovenance_prov_model_Used'](s);
                            default:
                                throw new org.openprovenance.prov.model.exception.InvalidCaseException("Statement Kind: " + s.getKind());
                        }
                    }
                    static unescape(s) {
                        return /* replace */ s.split("\\\"").join("\"");
                    }
                    static valueToNotationString$org_openprovenance_prov_model_Key(key) {
                        return ProvUtilities.valueToNotationString$java_lang_Object$org_openprovenance_prov_model_QualifiedName(key.getValue(), key.getType());
                    }
                    static escape(s) {
                        return /* replace */ /* replace */ s.split("\\").join("\\\\").split("\"").join("\\\"");
                    }
                    static internationalizedStringUri_$LI$() { if (ProvUtilities.internationalizedStringUri == null) {
                        ProvUtilities.internationalizedStringUri = org.openprovenance.prov.model.NamespacePrefixMapper.PROV_NS + "InternationalizedString";
                    } return ProvUtilities.internationalizedStringUri; }
                    static valueToNotationString$java_lang_Object$org_openprovenance_prov_model_QualifiedName(val, xsdType) {
                        if (val != null && (val.constructor != null && val.constructor["__interfaces"] != null && val.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.LangString") >= 0)) {
                            const istring = val;
                            return "\"" + ProvUtilities.escape(istring.getValue()) + ((istring.getLang() == null) ? "\"" : "\"@" + istring.getLang()) + (((xsdType == null) || (xsdType.getUri() === ProvUtilities.internationalizedStringUri_$LI$())) ? "" : " %% " + org.openprovenance.prov.model.Namespace.qualifiedNameToStringWithNamespace(xsdType));
                        }
                        else if (val != null && (val.constructor != null && val.constructor["__interfaces"] != null && val.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) {
                            const qn = val;
                            return "\'" + org.openprovenance.prov.model.Namespace.qualifiedNameToStringWithNamespace(qn) + "\'";
                        }
                        else if (typeof val === 'string') {
                            const s = val;
                            if ( /* contains */(s.indexOf("\n") != -1)) {
                                return "\"\"\"" + ProvUtilities.escape(s) + "\"\"\"";
                            }
                            else {
                                return "\"" + ProvUtilities.escape(s) + ((xsdType == null) ? "\"" : "\" %% " + org.openprovenance.prov.model.Namespace.qualifiedNameToStringWithNamespace(xsdType));
                            }
                        }
                        else {
                            return "\"" + val + "\" %% " + org.openprovenance.prov.model.Namespace.qualifiedNameToStringWithNamespace(xsdType);
                        }
                    }
                    static valueToNotationString(val, xsdType) {
                        if (((val != null) || val === null) && ((xsdType != null && (xsdType.constructor != null && xsdType.constructor["__interfaces"] != null && xsdType.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || xsdType === null)) {
                            return org.openprovenance.prov.model.ProvUtilities.valueToNotationString$java_lang_Object$org_openprovenance_prov_model_QualifiedName(val, xsdType);
                        }
                        else if (((val != null && (val.constructor != null && val.constructor["__interfaces"] != null && val.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Key") >= 0)) || val === null) && xsdType === undefined) {
                            return org.openprovenance.prov.model.ProvUtilities.valueToNotationString$org_openprovenance_prov_model_Key(val);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    static hasType(type, attributes) {
                        for (let index158 = 0; index158 < attributes.length; index158++) {
                            let attribute = attributes[index158];
                            {
                                switch ((attribute.getKind())) {
                                    case org.openprovenance.prov.model.Attribute.AttributeKind.PROV_TYPE:
                                        if ( /* equals */((o1, o2) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(attribute.getValue(), type)) {
                                            return true;
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                        return false;
                    }
                    getter(s, i) {
                        const kind = s.getKind();
                        switch ((kind)) {
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_ACTIVITY:
                                {
                                    const a = s;
                                    switch ((i)) {
                                        case 0:
                                            return a.getId();
                                        case 1:
                                            return a.getStartTime();
                                        case 2:
                                            return a.getEndTime();
                                        case 3:
                                            return a.getOther();
                                        default:
                                            throw Object.defineProperty(new Error("ProvUtilities.getter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IndexOutOfBoundsException', 'java.lang.Object', 'java.lang.ArrayIndexOutOfBoundsException', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                                    }
                                }
                                ;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_AGENT:
                                {
                                    const a = s;
                                    switch ((i)) {
                                        case 0:
                                            return a.getId();
                                        case 1:
                                            return a.getOther();
                                        default:
                                            throw Object.defineProperty(new Error("ProvUtilities.getter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IndexOutOfBoundsException', 'java.lang.Object', 'java.lang.ArrayIndexOutOfBoundsException', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                                    }
                                }
                                ;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_ALTERNATE:
                                {
                                    const a = s;
                                    switch ((i)) {
                                        case 0:
                                            return a.getAlternate1();
                                        case 1:
                                            return a.getAlternate2();
                                        default:
                                            throw Object.defineProperty(new Error("ProvUtilities.getter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IndexOutOfBoundsException', 'java.lang.Object', 'java.lang.ArrayIndexOutOfBoundsException', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                                    }
                                }
                                ;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_ASSOCIATION:
                                {
                                    const a = s;
                                    switch ((i)) {
                                        case 0:
                                            return a.getId();
                                        case 1:
                                            return a.getActivity();
                                        case 2:
                                            return a.getAgent();
                                        case 3:
                                            return a.getPlan();
                                        case 4:
                                            return a.getOther();
                                        default:
                                            throw Object.defineProperty(new Error("ProvUtilities.getter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IndexOutOfBoundsException', 'java.lang.Object', 'java.lang.ArrayIndexOutOfBoundsException', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                                    }
                                }
                                ;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_ATTRIBUTION:
                                {
                                    const a = s;
                                    switch ((i)) {
                                        case 0:
                                            return a.getId();
                                        case 1:
                                            return a.getEntity();
                                        case 2:
                                            return a.getAgent();
                                        case 3:
                                            return a.getOther();
                                        default:
                                            throw Object.defineProperty(new Error("ProvUtilities.getter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IndexOutOfBoundsException', 'java.lang.Object', 'java.lang.ArrayIndexOutOfBoundsException', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                                    }
                                }
                                ;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_BUNDLE:
                                throw new org.openprovenance.prov.model.exception.InvalidCaseException("ProvUtilities.getter() for " + kind);
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_COMMUNICATION:
                                {
                                    const a = s;
                                    switch ((i)) {
                                        case 0:
                                            return a.getId();
                                        case 1:
                                            return a.getInformed();
                                        case 2:
                                            return a.getInformant();
                                        case 3:
                                            return a.getOther();
                                        default:
                                            throw Object.defineProperty(new Error("ProvUtilities.getter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IndexOutOfBoundsException', 'java.lang.Object', 'java.lang.ArrayIndexOutOfBoundsException', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                                    }
                                }
                                ;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_DELEGATION:
                                {
                                    const a = s;
                                    switch ((i)) {
                                        case 0:
                                            return a.getId();
                                        case 1:
                                            return a.getDelegate();
                                        case 2:
                                            return a.getResponsible();
                                        case 3:
                                            return a.getActivity();
                                        case 4:
                                            return a.getOther();
                                        default:
                                            throw Object.defineProperty(new Error("ProvUtilities.getter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IndexOutOfBoundsException', 'java.lang.Object', 'java.lang.ArrayIndexOutOfBoundsException', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                                    }
                                }
                                ;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_DERIVATION:
                                {
                                    const a = s;
                                    switch ((i)) {
                                        case 0:
                                            return a.getId();
                                        case 1:
                                            return a.getGeneratedEntity();
                                        case 2:
                                            return a.getUsedEntity();
                                        case 3:
                                            return a.getActivity();
                                        case 4:
                                            return a.getGeneration();
                                        case 5:
                                            return a.getUsage();
                                        case 6:
                                            return a.getOther();
                                        default:
                                            throw Object.defineProperty(new Error("ProvUtilities.getter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IndexOutOfBoundsException', 'java.lang.Object', 'java.lang.ArrayIndexOutOfBoundsException', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                                    }
                                }
                                ;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_DICTIONARY_INSERTION:
                                throw new org.openprovenance.prov.model.exception.InvalidCaseException("ProvUtilities.getter() for " + kind);
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_DICTIONARY_MEMBERSHIP:
                                throw new org.openprovenance.prov.model.exception.InvalidCaseException("ProvUtilities.getter() for " + kind);
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_DICTIONARY_REMOVAL:
                                throw new org.openprovenance.prov.model.exception.InvalidCaseException("ProvUtilities.getter() for " + kind);
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_END:
                                {
                                    const a = s;
                                    switch ((i)) {
                                        case 0:
                                            return a.getId();
                                        case 1:
                                            return a.getActivity();
                                        case 2:
                                            return a.getTrigger();
                                        case 3:
                                            return a.getEnder();
                                        case 4:
                                            return a.getTime();
                                        case 5:
                                            return a.getOther();
                                        default:
                                            throw Object.defineProperty(new Error("ProvUtilities.getter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IndexOutOfBoundsException', 'java.lang.Object', 'java.lang.ArrayIndexOutOfBoundsException', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                                    }
                                }
                                ;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_ENTITY:
                                {
                                    const a = s;
                                    switch ((i)) {
                                        case 0:
                                            return a.getId();
                                        case 1:
                                            return a.getOther();
                                        default:
                                            throw Object.defineProperty(new Error("ProvUtilities.getter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IndexOutOfBoundsException', 'java.lang.Object', 'java.lang.ArrayIndexOutOfBoundsException', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                                    }
                                }
                                ;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_GENERATION:
                                {
                                    const a = s;
                                    switch ((i)) {
                                        case 0:
                                            return a.getId();
                                        case 1:
                                            return a.getEntity();
                                        case 2:
                                            return a.getActivity();
                                        case 3:
                                            return a.getTime();
                                        case 4:
                                            return a.getOther();
                                        default:
                                            throw Object.defineProperty(new Error("ProvUtilities.getter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IndexOutOfBoundsException', 'java.lang.Object', 'java.lang.ArrayIndexOutOfBoundsException', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                                    }
                                }
                                ;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_INFLUENCE:
                                {
                                    const a = s;
                                    switch ((i)) {
                                        case 0:
                                            return a.getId();
                                        case 1:
                                            return a.getInfluencee();
                                        case 2:
                                            return a.getInfluencer();
                                        case 3:
                                            return a.getOther();
                                        default:
                                            throw Object.defineProperty(new Error("ProvUtilities.getter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IndexOutOfBoundsException', 'java.lang.Object', 'java.lang.ArrayIndexOutOfBoundsException', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                                    }
                                }
                                ;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_INVALIDATION:
                                {
                                    const a = s;
                                    switch ((i)) {
                                        case 0:
                                            return a.getId();
                                        case 1:
                                            return a.getEntity();
                                        case 2:
                                            return a.getActivity();
                                        case 3:
                                            return a.getTime();
                                        case 4:
                                            return a.getOther();
                                        default:
                                            throw Object.defineProperty(new Error("ProvUtilities.getter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IndexOutOfBoundsException', 'java.lang.Object', 'java.lang.ArrayIndexOutOfBoundsException', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                                    }
                                }
                                ;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_MEMBERSHIP:
                                {
                                    const a = s;
                                    switch ((i)) {
                                        case 0:
                                            return a.getCollection();
                                        case 1:
                                            return a.getEntity();
                                        default:
                                            throw Object.defineProperty(new Error("ProvUtilities.getter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IndexOutOfBoundsException', 'java.lang.Object', 'java.lang.ArrayIndexOutOfBoundsException', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                                    }
                                }
                                ;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_MENTION:
                                {
                                    const a = s;
                                    switch ((i)) {
                                        case 0:
                                            throw Object.defineProperty(new Error("ProvUtilities.getter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IndexOutOfBoundsException', 'java.lang.Object', 'java.lang.ArrayIndexOutOfBoundsException', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                                        case 1:
                                            return a.getSpecificEntity();
                                        case 2:
                                            return a.getGeneralEntity();
                                        case 3:
                                            return a.getBundle();
                                        default:
                                            throw Object.defineProperty(new Error("ProvUtilities.getter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IndexOutOfBoundsException', 'java.lang.Object', 'java.lang.ArrayIndexOutOfBoundsException', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                                    }
                                }
                                ;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_SPECIALIZATION:
                                {
                                    const a = s;
                                    switch ((i)) {
                                        case 0:
                                            return a.getSpecificEntity();
                                        case 1:
                                            return a.getGeneralEntity();
                                        default:
                                            throw Object.defineProperty(new Error("ProvUtilities.getter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IndexOutOfBoundsException', 'java.lang.Object', 'java.lang.ArrayIndexOutOfBoundsException', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                                    }
                                }
                                ;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_START:
                                {
                                    const a = s;
                                    switch ((i)) {
                                        case 0:
                                            return a.getId();
                                        case 1:
                                            return a.getActivity();
                                        case 2:
                                            return a.getTrigger();
                                        case 3:
                                            return a.getStarter();
                                        case 4:
                                            return a.getTime();
                                        case 5:
                                            return a.getOther();
                                        default:
                                            throw Object.defineProperty(new Error("ProvUtilities.getter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IndexOutOfBoundsException', 'java.lang.Object', 'java.lang.ArrayIndexOutOfBoundsException', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                                    }
                                }
                                ;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_USAGE:
                                {
                                    const a = s;
                                    switch ((i)) {
                                        case 0:
                                            return a.getId();
                                        case 1:
                                            return a.getActivity();
                                        case 2:
                                            return a.getEntity();
                                        case 3:
                                            return a.getTime();
                                        case 4:
                                            return a.getOther();
                                        default:
                                            throw Object.defineProperty(new Error("ProvUtilities.getter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IndexOutOfBoundsException', 'java.lang.Object', 'java.lang.ArrayIndexOutOfBoundsException', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                                    }
                                }
                                ;
                            default:
                                throw new org.openprovenance.prov.model.exception.InvalidCaseException("ProvUtilities.getter() for " + kind);
                        }
                    }
                    setter(s, i, val) {
                        const kind = s.getKind();
                        switch ((kind)) {
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_ACTIVITY:
                                {
                                    const a = s;
                                    switch ((i)) {
                                        case 0:
                                            a.setId(val);
                                            return;
                                        case 1:
                                            a.setStartTime(val);
                                            return;
                                        case 2:
                                            a.setEndTime(val);
                                            return;
                                        default:
                                            throw Object.defineProperty(new Error("ProvUtilities.setter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IndexOutOfBoundsException', 'java.lang.Object', 'java.lang.ArrayIndexOutOfBoundsException', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                                    }
                                }
                                ;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_AGENT:
                                {
                                    const a = s;
                                    switch ((i)) {
                                        case 0:
                                            a.setId(val);
                                            return;
                                        default:
                                            throw Object.defineProperty(new Error("ProvUtilities.setter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IndexOutOfBoundsException', 'java.lang.Object', 'java.lang.ArrayIndexOutOfBoundsException', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                                    }
                                }
                                ;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_ALTERNATE:
                                {
                                    const a = s;
                                    switch ((i)) {
                                        case 0:
                                            a.setAlternate1(val);
                                            return;
                                        case 1:
                                            a.setAlternate2(val);
                                            return;
                                        default:
                                            throw Object.defineProperty(new Error("ProvUtilities.setter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IndexOutOfBoundsException', 'java.lang.Object', 'java.lang.ArrayIndexOutOfBoundsException', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                                    }
                                }
                                ;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_ASSOCIATION:
                                {
                                    const a = s;
                                    switch ((i)) {
                                        case 0:
                                            a.setId(val);
                                            return;
                                        case 1:
                                            a.setActivity(val);
                                            return;
                                        case 2:
                                            a.setAgent(val);
                                            return;
                                        case 3:
                                            a.setPlan(val);
                                            return;
                                        default:
                                            throw Object.defineProperty(new Error("ProvUtilities.setter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IndexOutOfBoundsException', 'java.lang.Object', 'java.lang.ArrayIndexOutOfBoundsException', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                                    }
                                }
                                ;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_ATTRIBUTION:
                                {
                                    const a = s;
                                    switch ((i)) {
                                        case 0:
                                            a.setId(val);
                                            return;
                                        case 1:
                                            a.setEntity(val);
                                            return;
                                        case 2:
                                            a.setAgent(val);
                                            return;
                                        default:
                                            throw Object.defineProperty(new Error("ProvUtilities.setter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IndexOutOfBoundsException', 'java.lang.Object', 'java.lang.ArrayIndexOutOfBoundsException', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                                    }
                                }
                                ;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_BUNDLE:
                                throw new org.openprovenance.prov.model.exception.InvalidCaseException("ProvUtilities.setter() for " + kind);
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_COMMUNICATION:
                                {
                                    const a = s;
                                    switch ((i)) {
                                        case 0:
                                            a.setId(val);
                                            return;
                                        case 1:
                                            a.setInformed(val);
                                            return;
                                        case 2:
                                            a.setInformant(val);
                                            return;
                                        default:
                                            throw Object.defineProperty(new Error("ProvUtilities.setter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IndexOutOfBoundsException', 'java.lang.Object', 'java.lang.ArrayIndexOutOfBoundsException', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                                    }
                                }
                                ;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_DELEGATION:
                                {
                                    const a = s;
                                    switch ((i)) {
                                        case 0:
                                            a.setId(val);
                                            return;
                                        case 1:
                                            a.setDelegate(val);
                                            return;
                                        case 2:
                                            a.setResponsible(val);
                                            return;
                                        case 3:
                                            a.setActivity(val);
                                            return;
                                        default:
                                            throw Object.defineProperty(new Error("ProvUtilities.setter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IndexOutOfBoundsException', 'java.lang.Object', 'java.lang.ArrayIndexOutOfBoundsException', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                                    }
                                }
                                ;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_DERIVATION:
                                {
                                    const a = s;
                                    switch ((i)) {
                                        case 0:
                                            a.setId(val);
                                            return;
                                        case 1:
                                            a.setGeneratedEntity(val);
                                            return;
                                        case 2:
                                            a.setUsedEntity(val);
                                            return;
                                        case 3:
                                            a.setActivity(val);
                                            return;
                                        case 4:
                                            a.setGeneration(val);
                                            return;
                                        case 5:
                                            a.setUsage(val);
                                            return;
                                        default:
                                            throw Object.defineProperty(new Error("ProvUtilities.setter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IndexOutOfBoundsException', 'java.lang.Object', 'java.lang.ArrayIndexOutOfBoundsException', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                                    }
                                }
                                ;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_DICTIONARY_INSERTION:
                                throw new org.openprovenance.prov.model.exception.InvalidCaseException("ProvUtilities.setter() for " + kind);
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_DICTIONARY_MEMBERSHIP:
                                throw new org.openprovenance.prov.model.exception.InvalidCaseException("ProvUtilities.setter() for " + kind);
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_DICTIONARY_REMOVAL:
                                throw new org.openprovenance.prov.model.exception.InvalidCaseException("ProvUtilities.setter() for " + kind);
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_END:
                                {
                                    const a = s;
                                    switch ((i)) {
                                        case 0:
                                            a.setId(val);
                                            return;
                                        case 1:
                                            a.setActivity(val);
                                            return;
                                        case 2:
                                            a.setTrigger(val);
                                            return;
                                        case 3:
                                            a.setEnder(val);
                                            return;
                                        case 4:
                                            a.setTime(val);
                                            return;
                                        default:
                                            throw Object.defineProperty(new Error("ProvUtilities.setter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IndexOutOfBoundsException', 'java.lang.Object', 'java.lang.ArrayIndexOutOfBoundsException', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                                    }
                                }
                                ;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_ENTITY:
                                {
                                    const a = s;
                                    switch ((i)) {
                                        case 0:
                                            a.setId(val);
                                            return;
                                        default:
                                            throw Object.defineProperty(new Error("ProvUtilities.setter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IndexOutOfBoundsException', 'java.lang.Object', 'java.lang.ArrayIndexOutOfBoundsException', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                                    }
                                }
                                ;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_GENERATION:
                                {
                                    const a = s;
                                    switch ((i)) {
                                        case 0:
                                            a.setId(val);
                                            return;
                                        case 1:
                                            a.setEntity(val);
                                            return;
                                        case 2:
                                            a.setActivity(val);
                                            return;
                                        case 3:
                                            a.setTime(val);
                                            return;
                                        default:
                                            throw Object.defineProperty(new Error("ProvUtilities.setter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IndexOutOfBoundsException', 'java.lang.Object', 'java.lang.ArrayIndexOutOfBoundsException', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                                    }
                                }
                                ;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_INFLUENCE:
                                {
                                    const a = s;
                                    switch ((i)) {
                                        case 0:
                                            a.setId(val);
                                            return;
                                        case 1:
                                            a.setInfluencee(val);
                                            return;
                                        case 2:
                                            a.setInfluencer(val);
                                            return;
                                        default:
                                            throw Object.defineProperty(new Error("ProvUtilities.setter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IndexOutOfBoundsException', 'java.lang.Object', 'java.lang.ArrayIndexOutOfBoundsException', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                                    }
                                }
                                ;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_INVALIDATION:
                                {
                                    const a = s;
                                    switch ((i)) {
                                        case 0:
                                            a.setId(val);
                                            return;
                                        case 1:
                                            a.setEntity(val);
                                            return;
                                        case 2:
                                            a.setActivity(val);
                                            return;
                                        case 3:
                                            a.setTime(val);
                                            return;
                                        default:
                                            throw Object.defineProperty(new Error("ProvUtilities.setter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IndexOutOfBoundsException', 'java.lang.Object', 'java.lang.ArrayIndexOutOfBoundsException', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                                    }
                                }
                                ;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_MEMBERSHIP:
                                {
                                    const a = s;
                                    switch ((i)) {
                                        case 0:
                                            a.setCollection(val);
                                            return;
                                        case 1:
                                            /* remove */ a.getEntity().splice(0, 1)[0];
                                            /* add */ (a.getEntity().push(val) > 0);
                                            return;
                                        default:
                                            throw Object.defineProperty(new Error("ProvUtilities.setter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IndexOutOfBoundsException', 'java.lang.Object', 'java.lang.ArrayIndexOutOfBoundsException', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                                    }
                                }
                                ;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_MENTION:
                                {
                                    const a = s;
                                    switch ((i)) {
                                        case 0:
                                            throw Object.defineProperty(new Error("ProvUtilities.setter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IndexOutOfBoundsException', 'java.lang.Object', 'java.lang.ArrayIndexOutOfBoundsException', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                                        case 1:
                                            a.setSpecificEntity(val);
                                            return;
                                        case 2:
                                            a.setGeneralEntity(val);
                                            return;
                                        case 3:
                                            a.setBundle(val);
                                            return;
                                        default:
                                            throw Object.defineProperty(new Error("ProvUtilities.setter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IndexOutOfBoundsException', 'java.lang.Object', 'java.lang.ArrayIndexOutOfBoundsException', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                                    }
                                }
                                ;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_SPECIALIZATION:
                                {
                                    const a = s;
                                    switch ((i)) {
                                        case 0:
                                            a.setSpecificEntity(val);
                                            return;
                                        case 1:
                                            a.setGeneralEntity(val);
                                            return;
                                        default:
                                            throw Object.defineProperty(new Error("ProvUtilities.setter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IndexOutOfBoundsException', 'java.lang.Object', 'java.lang.ArrayIndexOutOfBoundsException', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                                    }
                                }
                                ;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_START:
                                {
                                    const a = s;
                                    switch ((i)) {
                                        case 0:
                                            a.setId(val);
                                            return;
                                        case 1:
                                            a.setActivity(val);
                                            return;
                                        case 2:
                                            a.setTrigger(val);
                                            return;
                                        case 3:
                                            a.setStarter(val);
                                            return;
                                        case 4:
                                            a.setTime(val);
                                            return;
                                        default:
                                            throw Object.defineProperty(new Error("ProvUtilities.setter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IndexOutOfBoundsException', 'java.lang.Object', 'java.lang.ArrayIndexOutOfBoundsException', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                                    }
                                }
                                ;
                            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_USAGE:
                                {
                                    const a = s;
                                    switch ((i)) {
                                        case 0:
                                            a.setId(val);
                                            return;
                                        case 1:
                                            a.setActivity(val);
                                            return;
                                        case 2:
                                            a.setEntity(val);
                                            return;
                                        case 3:
                                            a.setTime(val);
                                            return;
                                        default:
                                            throw Object.defineProperty(new Error("ProvUtilities.setter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IndexOutOfBoundsException', 'java.lang.Object', 'java.lang.ArrayIndexOutOfBoundsException', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                                    }
                                }
                                ;
                            default:
                                throw new org.openprovenance.prov.model.exception.InvalidCaseException("ProvUtilities.setter() for " + kind);
                        }
                    }
                    /**
                     * Indicates whether object has no time field.
                     *
                     * @param {*} o
                     * @return {boolean}
                     */
                    hasNoTime(o) {
                        if (o != null && (o.constructor != null && o.constructor["__interfaces"] != null && o.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HasTime") >= 0))
                            return false;
                        if (o != null && (o.constructor != null && o.constructor["__interfaces"] != null && o.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Activity") >= 0))
                            return false;
                        return true;
                    }
                    static toXMLGregorianCalendar(date) {
                        if (date == null)
                            return null;
                        const gCalendar = new Date();
                        /* setTime */ gCalendar.setTime(date.getTime());
                        let xmlCalendar = null;
                        try {
                            xmlCalendar = javax.xml.datatype.DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar);
                        }
                        catch (ex) {
                            console.error(ex.message, ex);
                            throw new org.openprovenance.prov.model.exception.UncheckedException(ex);
                        }
                        return xmlCalendar;
                    }
                    static toDate(calendar) {
                        if (calendar == null) {
                            return null;
                        }
                        return /* getTime */ (new Date(calendar.toGregorianCalendar().getTime()));
                    }
                    /**
                     * After reading/constructing a document, this method should be called to ensure that Namespaces are properly chained.
                     * @param {*} document a {@link Document} to update
                     */
                    updateNamespaces(document) {
                        const rootNamespace = org.openprovenance.prov.model.Namespace.gatherNamespaces$org_openprovenance_prov_model_Document(document);
                        document.setNamespace(rootNamespace);
                        {
                            let array160 = this.getBundle(document);
                            for (let index159 = 0; index159 < array160.length; index159++) {
                                let bu = array160[index159];
                                {
                                    let ns = bu.getNamespace();
                                    if (ns != null) {
                                        ns.setParent(rootNamespace);
                                    }
                                    else {
                                        ns = new org.openprovenance.prov.model.Namespace();
                                        ns.setParent(rootNamespace);
                                        bu.setNamespace(ns);
                                    }
                                }
                            }
                        }
                    }
                }
                model.ProvUtilities = ProvUtilities;
                ProvUtilities["__class"] = "org.openprovenance.prov.model.ProvUtilities";
                (function (ProvUtilities) {
                    let BuildFlag;
                    (function (BuildFlag) {
                        BuildFlag[BuildFlag["NOCHEK"] = 0] = "NOCHEK";
                        BuildFlag[BuildFlag["WARN"] = 1] = "WARN";
                        BuildFlag[BuildFlag["STRICT"] = 2] = "STRICT";
                    })(BuildFlag = ProvUtilities.BuildFlag || (ProvUtilities.BuildFlag = {}));
                })(ProvUtilities = model.ProvUtilities || (model.ProvUtilities = {}));
            })(model = prov.model || (prov.model = {}));
        })(prov = openprovenance.prov || (openprovenance.prov = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
