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
                 * This class provides a set of indexes over information contained in
                 * an Document, facilitating its navigation.  Its constructor takes an
                 * Document builds an index for it.  Of course, for the index to be
                 * maintained, one cannot access, say the list of edges, and mutate
                 * it. Instead, one has to use the add methods provided.
                 * <p>
                 * Note that code is not thread-safe.
                 *
                 * TODO: index annotation, index edges
                 * @param {org.openprovenance.prov.model.ProvFactory} pFactory
                 * @param {*} doc
                 * @param {boolean} flatten
                 * @class
                 */
                class IndexedDocument {
                    constructor(pFactory, doc, flatten) {
                        if (((pFactory != null && pFactory instanceof org.openprovenance.prov.model.ProvFactory) || pFactory === null) && ((doc != null && (doc.constructor != null && doc.constructor["__interfaces"] != null && doc.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Document") >= 0)) || doc === null) && ((typeof flatten === 'boolean') || flatten === null)) {
                            let __args = arguments;
                            if (this.pFactory === undefined) {
                                this.pFactory = null;
                            }
                            if (this.nss === undefined) {
                                this.nss = null;
                            }
                            if (this.flatten === undefined) {
                                this.flatten = false;
                            }
                            this.u = new org.openprovenance.prov.model.ProvUtilities();
                            this.entityMap = ({});
                            this.activityMap = ({});
                            this.agentMap = ({});
                            this.activityUsedMap = ({});
                            this.entityUsedMap = ({});
                            this.anonUsed = ([]);
                            this.namedUsedMap = ({});
                            this.activityWasGeneratedByMap = ({});
                            this.entityWasGeneratedByMap = ({});
                            this.anonWasGeneratedBy = ([]);
                            this.namedWasGeneratedByMap = ({});
                            this.entityCauseWasDerivedFromMap = ({});
                            this.entityEffectWasDerivedFromMap = ({});
                            this.anonWasDerivedFrom = ([]);
                            this.namedWasDerivedFromMap = ({});
                            this.activityWasAssociatedWithMap = ({});
                            this.agentWasAssociatedWithMap = ({});
                            this.anonWasAssociatedWith = ([]);
                            this.namedWasAssociatedWithMap = ({});
                            this.entityWasAttributedToMap = ({});
                            this.agentWasAttributedToMap = ({});
                            this.anonWasAttributedTo = ([]);
                            this.namedWasAttributedToMap = ({});
                            this.activityCauseWasInformedByMap = ({});
                            this.activityEffectWasInformedByMap = ({});
                            this.anonWasInformedBy = ([]);
                            this.namedWasInformedByMap = ({});
                            this.anonActedOnBehalfOf = ([]);
                            this.namedActedOnBehalfOfMap = ({});
                            this.responsibleActedOnBehalfOfMap = ({});
                            this.delegateActedOnBehalfOfMap = ({});
                            this.namedWasInvalidatedByMap = ({});
                            this.entityWasInvalidatedByMap = ({});
                            this.anonWasInvalidatedBy = ([]);
                            this.activityWasInvalidatedByMap = ({});
                            this.namedSpecializationOfMap = ({});
                            this.specificEntitySpecializationOfMap = ({});
                            this.anonSpecializationOf = ([]);
                            this.genericEntitySpecializationOfMap = ({});
                            this.anonAlternateOf = ([]);
                            this.namedAlternateOfMap = ({});
                            this.entityCauseAlternateOfMap = ({});
                            this.entityEffectAlternateOfMap = ({});
                            this.influenceeWasInfluencedByMap = ({});
                            this.influencerWasInfluencedByMap = ({});
                            this.anonWasInfluencedBy = ([]);
                            this.namedWasInfluencedByMap = ({});
                            this.activityWasStartedByMap = ({});
                            this.entityWasStartedByMap = ({});
                            this.anonWasStartedBy = ([]);
                            this.namedWasStartedByMap = ({});
                            this.anonWasEndedBy = ([]);
                            this.activityWasEndedByMap = ({});
                            this.namedWasEndedByMap = ({});
                            this.entityWasEndedByMap = ({});
                            this.anonHadMember = ([]);
                            this.collHadMemberMap = ({});
                            this.namedHadMemberMap = ({});
                            this.entityHadMemberMap = ({});
                            this.bundleMap = ({});
                            this.pFactory = pFactory;
                            this.flatten = flatten;
                            if (doc != null) {
                                this.nss = doc.getNamespace();
                                this.u.forAllStatementOrBundle(doc.getStatementOrBundle(), this);
                            }
                        }
                        else if (((pFactory != null && pFactory instanceof org.openprovenance.prov.model.ProvFactory) || pFactory === null) && ((doc != null && (doc.constructor != null && doc.constructor["__interfaces"] != null && doc.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Document") >= 0)) || doc === null) && flatten === undefined) {
                            let __args = arguments;
                            {
                                let __args = arguments;
                                let flatten = true;
                                if (this.pFactory === undefined) {
                                    this.pFactory = null;
                                }
                                if (this.nss === undefined) {
                                    this.nss = null;
                                }
                                if (this.flatten === undefined) {
                                    this.flatten = false;
                                }
                                this.u = new org.openprovenance.prov.model.ProvUtilities();
                                this.entityMap = ({});
                                this.activityMap = ({});
                                this.agentMap = ({});
                                this.activityUsedMap = ({});
                                this.entityUsedMap = ({});
                                this.anonUsed = ([]);
                                this.namedUsedMap = ({});
                                this.activityWasGeneratedByMap = ({});
                                this.entityWasGeneratedByMap = ({});
                                this.anonWasGeneratedBy = ([]);
                                this.namedWasGeneratedByMap = ({});
                                this.entityCauseWasDerivedFromMap = ({});
                                this.entityEffectWasDerivedFromMap = ({});
                                this.anonWasDerivedFrom = ([]);
                                this.namedWasDerivedFromMap = ({});
                                this.activityWasAssociatedWithMap = ({});
                                this.agentWasAssociatedWithMap = ({});
                                this.anonWasAssociatedWith = ([]);
                                this.namedWasAssociatedWithMap = ({});
                                this.entityWasAttributedToMap = ({});
                                this.agentWasAttributedToMap = ({});
                                this.anonWasAttributedTo = ([]);
                                this.namedWasAttributedToMap = ({});
                                this.activityCauseWasInformedByMap = ({});
                                this.activityEffectWasInformedByMap = ({});
                                this.anonWasInformedBy = ([]);
                                this.namedWasInformedByMap = ({});
                                this.anonActedOnBehalfOf = ([]);
                                this.namedActedOnBehalfOfMap = ({});
                                this.responsibleActedOnBehalfOfMap = ({});
                                this.delegateActedOnBehalfOfMap = ({});
                                this.namedWasInvalidatedByMap = ({});
                                this.entityWasInvalidatedByMap = ({});
                                this.anonWasInvalidatedBy = ([]);
                                this.activityWasInvalidatedByMap = ({});
                                this.namedSpecializationOfMap = ({});
                                this.specificEntitySpecializationOfMap = ({});
                                this.anonSpecializationOf = ([]);
                                this.genericEntitySpecializationOfMap = ({});
                                this.anonAlternateOf = ([]);
                                this.namedAlternateOfMap = ({});
                                this.entityCauseAlternateOfMap = ({});
                                this.entityEffectAlternateOfMap = ({});
                                this.influenceeWasInfluencedByMap = ({});
                                this.influencerWasInfluencedByMap = ({});
                                this.anonWasInfluencedBy = ([]);
                                this.namedWasInfluencedByMap = ({});
                                this.activityWasStartedByMap = ({});
                                this.entityWasStartedByMap = ({});
                                this.anonWasStartedBy = ([]);
                                this.namedWasStartedByMap = ({});
                                this.anonWasEndedBy = ([]);
                                this.activityWasEndedByMap = ({});
                                this.namedWasEndedByMap = ({});
                                this.entityWasEndedByMap = ({});
                                this.anonHadMember = ([]);
                                this.collHadMemberMap = ({});
                                this.namedHadMemberMap = ({});
                                this.entityHadMemberMap = ({});
                                this.bundleMap = ({});
                                this.pFactory = pFactory;
                                this.flatten = flatten;
                                if (doc != null) {
                                    this.nss = doc.getNamespace();
                                    this.u.forAllStatementOrBundle(doc.getStatementOrBundle(), this);
                                }
                            }
                            if (this.pFactory === undefined) {
                                this.pFactory = null;
                            }
                            if (this.nss === undefined) {
                                this.nss = null;
                            }
                            if (this.flatten === undefined) {
                                this.flatten = false;
                            }
                            this.u = new org.openprovenance.prov.model.ProvUtilities();
                            this.entityMap = ({});
                            this.activityMap = ({});
                            this.agentMap = ({});
                            this.activityUsedMap = ({});
                            this.entityUsedMap = ({});
                            this.anonUsed = ([]);
                            this.namedUsedMap = ({});
                            this.activityWasGeneratedByMap = ({});
                            this.entityWasGeneratedByMap = ({});
                            this.anonWasGeneratedBy = ([]);
                            this.namedWasGeneratedByMap = ({});
                            this.entityCauseWasDerivedFromMap = ({});
                            this.entityEffectWasDerivedFromMap = ({});
                            this.anonWasDerivedFrom = ([]);
                            this.namedWasDerivedFromMap = ({});
                            this.activityWasAssociatedWithMap = ({});
                            this.agentWasAssociatedWithMap = ({});
                            this.anonWasAssociatedWith = ([]);
                            this.namedWasAssociatedWithMap = ({});
                            this.entityWasAttributedToMap = ({});
                            this.agentWasAttributedToMap = ({});
                            this.anonWasAttributedTo = ([]);
                            this.namedWasAttributedToMap = ({});
                            this.activityCauseWasInformedByMap = ({});
                            this.activityEffectWasInformedByMap = ({});
                            this.anonWasInformedBy = ([]);
                            this.namedWasInformedByMap = ({});
                            this.anonActedOnBehalfOf = ([]);
                            this.namedActedOnBehalfOfMap = ({});
                            this.responsibleActedOnBehalfOfMap = ({});
                            this.delegateActedOnBehalfOfMap = ({});
                            this.namedWasInvalidatedByMap = ({});
                            this.entityWasInvalidatedByMap = ({});
                            this.anonWasInvalidatedBy = ([]);
                            this.activityWasInvalidatedByMap = ({});
                            this.namedSpecializationOfMap = ({});
                            this.specificEntitySpecializationOfMap = ({});
                            this.anonSpecializationOf = ([]);
                            this.genericEntitySpecializationOfMap = ({});
                            this.anonAlternateOf = ([]);
                            this.namedAlternateOfMap = ({});
                            this.entityCauseAlternateOfMap = ({});
                            this.entityEffectAlternateOfMap = ({});
                            this.influenceeWasInfluencedByMap = ({});
                            this.influencerWasInfluencedByMap = ({});
                            this.anonWasInfluencedBy = ([]);
                            this.namedWasInfluencedByMap = ({});
                            this.activityWasStartedByMap = ({});
                            this.entityWasStartedByMap = ({});
                            this.anonWasStartedBy = ([]);
                            this.namedWasStartedByMap = ({});
                            this.anonWasEndedBy = ([]);
                            this.activityWasEndedByMap = ({});
                            this.namedWasEndedByMap = ({});
                            this.entityWasEndedByMap = ({});
                            this.anonHadMember = ([]);
                            this.collHadMemberMap = ({});
                            this.namedHadMemberMap = ({});
                            this.entityHadMemberMap = ({});
                            this.bundleMap = ({});
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    getUsed$() {
                        return this.anonUsed;
                    }
                    getUsed$org_openprovenance_prov_model_Activity(p) {
                        return /* get */ ((m, k) => { if (m.entries == null)
                            m.entries = []; for (let i = 0; i < m.entries.length; i++)
                            if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                return m.entries[i].value;
                            } return null; })(this.activityUsedMap, p.getId());
                    }
                    /**
                     * Return all used edges with activity p as an effect.
                     * @param {*} p
                     * @return {*[]}
                     */
                    getUsed(p) {
                        if (((p != null && (p.constructor != null && p.constructor["__interfaces"] != null && p.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Activity") >= 0)) || p === null)) {
                            return this.getUsed$org_openprovenance_prov_model_Activity(p);
                        }
                        else if (((p != null && (p.constructor != null && p.constructor["__interfaces"] != null && p.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Entity") >= 0)) || p === null)) {
                            return this.getUsed$org_openprovenance_prov_model_Entity(p);
                        }
                        else if (p === undefined) {
                            return this.getUsed$();
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    getUsed$org_openprovenance_prov_model_Entity(p) {
                        return /* get */ ((m, k) => { if (m.entries == null)
                            m.entries = []; for (let i = 0; i < m.entries.length; i++)
                            if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                return m.entries[i].value;
                            } return null; })(this.entityUsedMap, p.getId());
                    }
                    getWasGeneratedBy$() {
                        return this.anonWasGeneratedBy;
                    }
                    getWasGeneratedBy$org_openprovenance_prov_model_Activity(p) {
                        return /* get */ ((m, k) => { if (m.entries == null)
                            m.entries = []; for (let i = 0; i < m.entries.length; i++)
                            if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                return m.entries[i].value;
                            } return null; })(this.activityWasGeneratedByMap, p.getId());
                    }
                    /**
                     * Return all WasGeneratedBy edges with activity p as an effect.
                     * @param {*} p
                     * @return {*[]}
                     */
                    getWasGeneratedBy(p) {
                        if (((p != null && (p.constructor != null && p.constructor["__interfaces"] != null && p.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Activity") >= 0)) || p === null)) {
                            return this.getWasGeneratedBy$org_openprovenance_prov_model_Activity(p);
                        }
                        else if (((p != null && (p.constructor != null && p.constructor["__interfaces"] != null && p.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Entity") >= 0)) || p === null)) {
                            return this.getWasGeneratedBy$org_openprovenance_prov_model_Entity(p);
                        }
                        else if (p === undefined) {
                            return this.getWasGeneratedBy$();
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    getWasGeneratedBy$org_openprovenance_prov_model_Entity(p) {
                        return /* get */ ((m, k) => { if (m.entries == null)
                            m.entries = []; for (let i = 0; i < m.entries.length; i++)
                            if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                return m.entries[i].value;
                            } return null; })(this.entityWasGeneratedByMap, p.getId());
                    }
                    /**
                     * Return all WasDerivedFrom edges for this graph.
                     * @return {*[]}
                     */
                    getWasDerivedFrom() {
                        return this.anonWasDerivedFrom;
                    }
                    /**
                     * Return all WasDerivedFrom edges with entity a as a cause.
                     * @param {*} a
                     * @return {*[]}
                     */
                    getWasDerivedFromWithCause(a) {
                        return /* get */ ((m, k) => { if (m.entries == null)
                            m.entries = []; for (let i = 0; i < m.entries.length; i++)
                            if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                return m.entries[i].value;
                            } return null; })(this.entityCauseWasDerivedFromMap, a.getId());
                    }
                    /**
                     * Return all WasDerivedFrom edges with entity a as an effect .
                     * @param {*} a
                     * @return {*[]}
                     */
                    getWasDerivedFromWithEffect(a) {
                        return /* get */ ((m, k) => { if (m.entries == null)
                            m.entries = []; for (let i = 0; i < m.entries.length; i++)
                            if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                return m.entries[i].value;
                            } return null; })(this.entityEffectWasDerivedFromMap, a.getId());
                    }
                    /**
                     * Return all WasInformedBy edges for this graph.
                     * @return {*[]}
                     */
                    getWasInformedBy() {
                        return this.anonWasInformedBy;
                    }
                    /**
                     * Return all WasInformedBy edges with activity p as a cause.
                     * @param {*} a
                     * @return {*[]}
                     */
                    getWasInformedByWithCause(a) {
                        return /* get */ ((m, k) => { if (m.entries == null)
                            m.entries = []; for (let i = 0; i < m.entries.length; i++)
                            if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                return m.entries[i].value;
                            } return null; })(this.activityCauseWasInformedByMap, a.getId());
                    }
                    /**
                     * Return all WasInformedBy edges with activity a as an effect.
                     * @param {*} a
                     * @return {*[]}
                     */
                    getWasInformedByWithEffect(a) {
                        return /* get */ ((m, k) => { if (m.entries == null)
                            m.entries = []; for (let i = 0; i < m.entries.length; i++)
                            if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                return m.entries[i].value;
                            } return null; })(this.activityEffectWasInformedByMap, a.getId());
                    }
                    getWasAssociatedWith$() {
                        return this.anonWasAssociatedWith;
                    }
                    getWasAssociatedWith$org_openprovenance_prov_model_Activity(p) {
                        return /* get */ ((m, k) => { if (m.entries == null)
                            m.entries = []; for (let i = 0; i < m.entries.length; i++)
                            if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                return m.entries[i].value;
                            } return null; })(this.activityWasAssociatedWithMap, p.getId());
                    }
                    /**
                     * Return all WasAssociatedWith edges with activity p as an effect.
                     * @param {*} p
                     * @return {*[]}
                     */
                    getWasAssociatedWith(p) {
                        if (((p != null && (p.constructor != null && p.constructor["__interfaces"] != null && p.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Activity") >= 0)) || p === null)) {
                            return this.getWasAssociatedWith$org_openprovenance_prov_model_Activity(p);
                        }
                        else if (((p != null && (p.constructor != null && p.constructor["__interfaces"] != null && p.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Agent") >= 0)) || p === null)) {
                            return this.getWasAssociatedWith$org_openprovenance_prov_model_Agent(p);
                        }
                        else if (p === undefined) {
                            return this.getWasAssociatedWith$();
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    getWasAssociatedWith$org_openprovenance_prov_model_Agent(a) {
                        return /* get */ ((m, k) => { if (m.entries == null)
                            m.entries = []; for (let i = 0; i < m.entries.length; i++)
                            if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                return m.entries[i].value;
                            } return null; })(this.agentWasAssociatedWithMap, a.getId());
                    }
                    add$org_openprovenance_prov_model_Entity(entity) {
                        return this.add$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_Entity(entity.getId(), entity);
                    }
                    add$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_Entity(name, entity) {
                        const existing = ((m, k) => { if (m.entries == null)
                            m.entries = []; for (let i = 0; i < m.entries.length; i++)
                            if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                return m.entries[i].value;
                            } return null; })(this.entityMap, name);
                        if (existing != null) {
                            this.mergeAttributes$org_openprovenance_prov_model_Element$org_openprovenance_prov_model_Element(existing, entity);
                            return existing;
                        }
                        else {
                            /* put */ ((m, k, v) => { if (m.entries == null)
                                m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                    m.entries[i].value = v;
                                    return;
                                } m.entries.push({ key: k, value: v, getKey: function () { return this.key; }, getValue: function () { return this.value; } }); })(this.entityMap, name, entity);
                            return entity;
                        }
                    }
                    mergeAttributes$org_openprovenance_prov_model_Element$org_openprovenance_prov_model_Element(existing, newElement) {
                        const set = (newElement.getLabel().slice(0));
                        /* removeAll */ ((a, r) => { let b = false; for (let i = 0; i < r.length; i++) {
                            let ndx = a.indexOf(r[i]);
                            if (ndx >= 0) {
                                a.splice(ndx, 1);
                                b = true;
                            }
                        } return b; })(set, existing.getLabel());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(existing.getLabel(), set);
                        const set2 = (newElement.getLocation().slice(0));
                        /* removeAll */ ((a, r) => { let b = false; for (let i = 0; i < r.length; i++) {
                            let ndx = a.indexOf(r[i]);
                            if (ndx >= 0) {
                                a.splice(ndx, 1);
                                b = true;
                            }
                        } return b; })(set2, existing.getLocation());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(existing.getLocation(), set2);
                        const set3 = (newElement.getType().slice(0));
                        /* removeAll */ ((a, r) => { let b = false; for (let i = 0; i < r.length; i++) {
                            let ndx = a.indexOf(r[i]);
                            if (ndx >= 0) {
                                a.splice(ndx, 1);
                                b = true;
                            }
                        } return b; })(set3, existing.getType());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(existing.getType(), set3);
                        const set4 = (newElement.getOther().slice(0));
                        /* removeAll */ ((a, r) => { let b = false; for (let i = 0; i < r.length; i++) {
                            let ndx = a.indexOf(r[i]);
                            if (ndx >= 0) {
                                a.splice(ndx, 1);
                                b = true;
                            }
                        } return b; })(set4, existing.getOther());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(existing.getOther(), set4);
                    }
                    mergeAttributes(existing, newElement) {
                        if (((existing != null && (existing.constructor != null && existing.constructor["__interfaces"] != null && existing.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Element") >= 0)) || existing === null) && ((newElement != null && (newElement.constructor != null && newElement.constructor["__interfaces"] != null && newElement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Element") >= 0)) || newElement === null)) {
                            return this.mergeAttributes$org_openprovenance_prov_model_Element$org_openprovenance_prov_model_Element(existing, newElement);
                        }
                        else if (((existing != null && (existing.constructor != null && existing.constructor["__interfaces"] != null && existing.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Influence") >= 0)) || existing === null) && ((newElement != null && (newElement.constructor != null && newElement.constructor["__interfaces"] != null && newElement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Influence") >= 0)) || newElement === null)) {
                            return this.mergeAttributes$org_openprovenance_prov_model_Influence$org_openprovenance_prov_model_Influence(existing, newElement);
                        }
                        else if (((existing != null) || existing === null) && ((newElement != null) || newElement === null)) {
                            return this.mergeAttributes$org_openprovenance_prov_model_Statement$org_openprovenance_prov_model_Statement(existing, newElement);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    mergeAttributes$org_openprovenance_prov_model_Influence$org_openprovenance_prov_model_Influence(existing, newElement) {
                        const set = (newElement.getLabel().slice(0));
                        /* removeAll */ ((a, r) => { let b = false; for (let i = 0; i < r.length; i++) {
                            let ndx = a.indexOf(r[i]);
                            if (ndx >= 0) {
                                a.splice(ndx, 1);
                                b = true;
                            }
                        } return b; })(set, existing.getLabel());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(existing.getLabel(), set);
                        if (existing != null && (existing.constructor != null && existing.constructor["__interfaces"] != null && existing.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HasLocation") >= 0)) {
                            const existing2 = existing;
                            const set2 = (newElement.getLocation().slice(0));
                            /* removeAll */ ((a, r) => { let b = false; for (let i = 0; i < r.length; i++) {
                                let ndx = a.indexOf(r[i]);
                                if (ndx >= 0) {
                                    a.splice(ndx, 1);
                                    b = true;
                                }
                            } return b; })(set2, existing2.getLocation());
                            /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(existing2.getLocation(), set2);
                        }
                        const set3 = (newElement.getType().slice(0));
                        /* removeAll */ ((a, r) => { let b = false; for (let i = 0; i < r.length; i++) {
                            let ndx = a.indexOf(r[i]);
                            if (ndx >= 0) {
                                a.splice(ndx, 1);
                                b = true;
                            }
                        } return b; })(set3, existing.getType());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(existing.getType(), set3);
                        const set4 = (newElement.getOther().slice(0));
                        /* removeAll */ ((a, r) => { let b = false; for (let i = 0; i < r.length; i++) {
                            let ndx = a.indexOf(r[i]);
                            if (ndx >= 0) {
                                a.splice(ndx, 1);
                                b = true;
                            }
                        } return b; })(set4, existing.getOther());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(existing.getOther(), set4);
                    }
                    mergeAttributes$org_openprovenance_prov_model_Statement$org_openprovenance_prov_model_Statement(existing, newElement) {
                        if (existing != null && (existing.constructor != null && existing.constructor["__interfaces"] != null && existing.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Element") >= 0)) {
                            this.mergeAttributes$org_openprovenance_prov_model_Element$org_openprovenance_prov_model_Element(existing, newElement);
                            return;
                        }
                        if (existing != null && (existing.constructor != null && existing.constructor["__interfaces"] != null && existing.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Influence") >= 0)) {
                            this.mergeAttributes$org_openprovenance_prov_model_Influence$org_openprovenance_prov_model_Influence(existing, newElement);
                            return;
                        }
                        throw Object.defineProperty(new Error(), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.UnsupportedOperationException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                    }
                    sameEdge(existing, newElement, count) {
                        let ok = true;
                        for (let i = 1; i <= count; i++) {
                            {
                                const qn1 = this.u.getter(existing, i);
                                const qn2 = this.u.getter(newElement, i);
                                if (qn1 == null) {
                                    if (qn2 == null) {
                                    }
                                    else {
                                        ok = false;
                                        break;
                                    }
                                }
                                else {
                                    if (qn2 == null) {
                                        ok = false;
                                        break;
                                    }
                                    else {
                                        if (!((o1, o2) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(qn1, qn2)) {
                                            ok = false;
                                            break;
                                        }
                                    }
                                }
                            }
                            ;
                        }
                        return ok;
                    }
                    add$org_openprovenance_prov_model_Agent(agent) {
                        return this.add$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_Agent(agent.getId(), agent);
                    }
                    add$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_Agent(name, agent) {
                        const existing = ((m, k) => { if (m.entries == null)
                            m.entries = []; for (let i = 0; i < m.entries.length; i++)
                            if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                return m.entries[i].value;
                            } return null; })(this.agentMap, name);
                        if (existing != null) {
                            this.mergeAttributes$org_openprovenance_prov_model_Element$org_openprovenance_prov_model_Element(existing, agent);
                            return existing;
                        }
                        else {
                            /* put */ ((m, k, v) => { if (m.entries == null)
                                m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                    m.entries[i].value = v;
                                    return;
                                } m.entries.push({ key: k, value: v, getKey: function () { return this.key; }, getValue: function () { return this.value; } }); })(this.agentMap, name, agent);
                            return agent;
                        }
                    }
                    add$org_openprovenance_prov_model_Activity(activity) {
                        return this.add$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_Activity(activity.getId(), activity);
                    }
                    add$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_Activity(name, activity) {
                        const existing = ((m, k) => { if (m.entries == null)
                            m.entries = []; for (let i = 0; i < m.entries.length; i++)
                            if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                return m.entries[i].value;
                            } return null; })(this.activityMap, name);
                        if (existing != null) {
                            this.mergeAttributes$org_openprovenance_prov_model_Element$org_openprovenance_prov_model_Element(existing, activity);
                            return existing;
                        }
                        else {
                            /* put */ ((m, k, v) => { if (m.entries == null)
                                m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                    m.entries[i].value = v;
                                    return;
                                } m.entries.push({ key: k, value: v, getKey: function () { return this.key; }, getValue: function () { return this.value; } }); })(this.activityMap, name, activity);
                            return activity;
                        }
                    }
                    getActivity(name) {
                        return /* get */ ((m, k) => { if (m.entries == null)
                            m.entries = []; for (let i = 0; i < m.entries.length; i++)
                            if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                return m.entries[i].value;
                            } return null; })(this.activityMap, name);
                    }
                    getEntity(name) {
                        return /* get */ ((m, k) => { if (m.entries == null)
                            m.entries = []; for (let i = 0; i < m.entries.length; i++)
                            if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                return m.entries[i].value;
                            } return null; })(this.entityMap, name);
                    }
                    getAgent(name) {
                        return /* get */ ((m, k) => { if (m.entries == null)
                            m.entries = []; for (let i = 0; i < m.entries.length; i++)
                            if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                return m.entries[i].value;
                            } return null; })(this.agentMap, name);
                    }
                    add$org_openprovenance_prov_model_WasInformedBy(wib) {
                        return (this.add(wib, 2, this.anonWasInformedBy, this.namedWasInformedByMap, this.activityEffectWasInformedByMap, this.activityCauseWasInformedByMap));
                    }
                    add$org_openprovenance_prov_model_Used(used) {
                        return (this.add(used, 3, this.anonUsed, this.namedUsedMap, this.activityUsedMap, this.entityUsedMap));
                    }
                    add$org_openprovenance_prov_model_WasGeneratedBy(wgb) {
                        return (this.add(wgb, 3, this.anonWasGeneratedBy, this.namedWasGeneratedByMap, this.entityWasGeneratedByMap, this.activityWasGeneratedByMap));
                    }
                    add$org_openprovenance_prov_model_WasDerivedFrom(wdf) {
                        return (this.add(wdf, 5, this.anonWasDerivedFrom, this.namedWasDerivedFromMap, this.entityEffectWasDerivedFromMap, this.entityCauseWasDerivedFromMap));
                    }
                    add$org_openprovenance_prov_model_WasAssociatedWith(waw) {
                        return (this.add(waw, 3, this.anonWasAssociatedWith, this.namedWasAssociatedWithMap, this.activityWasAssociatedWithMap, this.agentWasAssociatedWithMap));
                    }
                    add$org_openprovenance_prov_model_WasAttributedTo(wat) {
                        return (this.add(wat, 2, this.anonWasAttributedTo, this.namedWasAttributedToMap, this.entityWasAttributedToMap, this.agentWasAttributedToMap));
                    }
                    add$org_openprovenance_prov_model_ActedOnBehalfOf(act) {
                        return (this.add(act, 3, this.anonActedOnBehalfOf, this.namedActedOnBehalfOfMap, this.delegateActedOnBehalfOfMap, this.responsibleActedOnBehalfOfMap));
                    }
                    add$org_openprovenance_prov_model_WasInvalidatedBy(wib) {
                        return (this.add(wib, 3, this.anonWasInvalidatedBy, this.namedWasInvalidatedByMap, this.entityWasInvalidatedByMap, this.activityWasInvalidatedByMap));
                    }
                    add$org_openprovenance_prov_model_SpecializationOf(spec) {
                        return (this.add(spec, 2, this.anonSpecializationOf, this.namedSpecializationOfMap, this.specificEntitySpecializationOfMap, this.genericEntitySpecializationOfMap));
                    }
                    add$org_openprovenance_prov_model_AlternateOf(alt) {
                        return (this.add(alt, 2, this.anonAlternateOf, this.namedAlternateOfMap, this.entityEffectAlternateOfMap, this.entityCauseAlternateOfMap));
                    }
                    add$org_openprovenance_prov_model_WasInfluencedBy(winf) {
                        return (this.add(winf, 2, this.anonWasInfluencedBy, this.namedWasInfluencedByMap, this.influenceeWasInfluencedByMap, this.influencerWasInfluencedByMap));
                    }
                    add$org_openprovenance_prov_model_WasStartedBy(wsb) {
                        return (this.add(wsb, 4, this.anonWasStartedBy, this.namedWasStartedByMap, this.activityWasStartedByMap, this.entityWasStartedByMap));
                    }
                    add$org_openprovenance_prov_model_WasEndedBy(web) {
                        return (this.add(web, 4, this.anonWasEndedBy, this.namedWasEndedByMap, this.activityWasEndedByMap, this.entityWasEndedByMap));
                    }
                    add$org_openprovenance_prov_model_HadMember(hm) {
                        return (this.add(hm, 2, this.anonHadMember, this.namedHadMemberMap, this.collHadMemberMap, this.entityHadMemberMap));
                    }
                    add$org_openprovenance_prov_model_Relation$int$java_util_Collection$java_util_HashMap$java_util_HashMap$java_util_HashMap(statement, num, anonRelationCollection, namedRelationMap, effectRelationMap, causeRelationMap) {
                        const aid2 = this.u.getEffect(statement);
                        const aid1 = this.u.getCause(statement);
                        statement = (this.pFactory.newStatement(statement));
                        let id;
                        if (statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Identifiable") >= 0)) {
                            id = statement.getId();
                        }
                        else {
                            id = null;
                        }
                        if (id == null) {
                            let found = false;
                            let relationCollection = ((m, k) => { if (m.entries == null)
                                m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                    return m.entries[i].value;
                                } return null; })(effectRelationMap, aid2);
                            if (relationCollection == null) {
                                relationCollection = ([]);
                                /* add */ (relationCollection.push(statement) > 0);
                                /* put */ ((m, k, v) => { if (m.entries == null)
                                    m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                    if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                        m.entries[i].value = v;
                                        return;
                                    } m.entries.push({ key: k, value: v, getKey: function () { return this.key; }, getValue: function () { return this.value; } }); })(effectRelationMap, aid2, relationCollection);
                            }
                            else {
                                for (let index202 = 0; index202 < relationCollection.length; index202++) {
                                    let u = relationCollection[index202];
                                    {
                                        if (u.equals(statement)) {
                                            found = true;
                                            statement = u;
                                            break;
                                        }
                                    }
                                }
                                if (!found) {
                                    /* add */ (relationCollection.push(statement) > 0);
                                }
                            }
                            relationCollection = /* get */ ((m, k) => { if (m.entries == null)
                                m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                    return m.entries[i].value;
                                } return null; })(causeRelationMap, aid1);
                            if (relationCollection == null) {
                                relationCollection = ([]);
                                /* add */ (relationCollection.push(statement) > 0);
                                /* put */ ((m, k, v) => { if (m.entries == null)
                                    m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                    if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                        m.entries[i].value = v;
                                        return;
                                    } m.entries.push({ key: k, value: v, getKey: function () { return this.key; }, getValue: function () { return this.value; } }); })(causeRelationMap, aid1, relationCollection);
                            }
                            else {
                                if (!found) {
                                    /* add */ (relationCollection.push(statement) > 0);
                                }
                            }
                            if (!found) {
                                /* add */ (anonRelationCollection.push(statement) > 0);
                            }
                        }
                        else {
                            let relationCollection = ((m, k) => { if (m.entries == null)
                                m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                    return m.entries[i].value;
                                } return null; })(namedRelationMap, id);
                            if (relationCollection == null) {
                                relationCollection = ([]);
                                /* add */ (relationCollection.push(statement) > 0);
                                /* put */ ((m, k, v) => { if (m.entries == null)
                                    m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                    if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                        m.entries[i].value = v;
                                        return;
                                    } m.entries.push({ key: k, value: v, getKey: function () { return this.key; }, getValue: function () { return this.value; } }); })(namedRelationMap, id, relationCollection);
                            }
                            else {
                                let found = false;
                                for (let index203 = 0; index203 < relationCollection.length; index203++) {
                                    let u1 = relationCollection[index203];
                                    {
                                        if (this.sameEdge(u1, statement, num)) {
                                            found = true;
                                            this.mergeAttributes(u1, statement);
                                            break;
                                        }
                                    }
                                }
                                if (!found) {
                                    /* add */ (relationCollection.push(statement) > 0);
                                }
                            }
                        }
                        return statement;
                    }
                    /**
                     * Add an  edge to the graph. Update namedRelationMap, effectRelationMap and causeRelationMap, accordingly.
                     * Edges with different attributes are considered distinct.
                     * @param {*} statement
                     * @param {number} num
                     * @param {*[]} anonRelationCollection
                     * @param {*} namedRelationMap
                     * @param {*} effectRelationMap
                     * @param {*} causeRelationMap
                     * @return {*}
                     */
                    add(statement, num, anonRelationCollection, namedRelationMap, effectRelationMap, causeRelationMap) {
                        if (((statement != null) || statement === null) && ((typeof num === 'number') || num === null) && ((anonRelationCollection != null && (anonRelationCollection instanceof Array)) || anonRelationCollection === null) && ((namedRelationMap != null && (namedRelationMap instanceof Object)) || namedRelationMap === null) && ((effectRelationMap != null && (effectRelationMap instanceof Object)) || effectRelationMap === null) && ((causeRelationMap != null && (causeRelationMap instanceof Object)) || causeRelationMap === null)) {
                            return this.add$org_openprovenance_prov_model_Relation$int$java_util_Collection$java_util_HashMap$java_util_HashMap$java_util_HashMap(statement, num, anonRelationCollection, namedRelationMap, effectRelationMap, causeRelationMap);
                        }
                        else if (((statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || statement === null) && ((num != null && (num.constructor != null && num.constructor["__interfaces"] != null && num.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Entity") >= 0)) || num === null) && anonRelationCollection === undefined && namedRelationMap === undefined && effectRelationMap === undefined && causeRelationMap === undefined) {
                            return this.add$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_Entity(statement, num);
                        }
                        else if (((statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || statement === null) && ((num != null && (num.constructor != null && num.constructor["__interfaces"] != null && num.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Agent") >= 0)) || num === null) && anonRelationCollection === undefined && namedRelationMap === undefined && effectRelationMap === undefined && causeRelationMap === undefined) {
                            return this.add$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_Agent(statement, num);
                        }
                        else if (((statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || statement === null) && ((num != null && (num.constructor != null && num.constructor["__interfaces"] != null && num.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Activity") >= 0)) || num === null) && anonRelationCollection === undefined && namedRelationMap === undefined && effectRelationMap === undefined && causeRelationMap === undefined) {
                            return this.add$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_Activity(statement, num);
                        }
                        else if (((statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Entity") >= 0)) || statement === null) && num === undefined && anonRelationCollection === undefined && namedRelationMap === undefined && effectRelationMap === undefined && causeRelationMap === undefined) {
                            return this.add$org_openprovenance_prov_model_Entity(statement);
                        }
                        else if (((statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Agent") >= 0)) || statement === null) && num === undefined && anonRelationCollection === undefined && namedRelationMap === undefined && effectRelationMap === undefined && causeRelationMap === undefined) {
                            return this.add$org_openprovenance_prov_model_Agent(statement);
                        }
                        else if (((statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Activity") >= 0)) || statement === null) && num === undefined && anonRelationCollection === undefined && namedRelationMap === undefined && effectRelationMap === undefined && causeRelationMap === undefined) {
                            return this.add$org_openprovenance_prov_model_Activity(statement);
                        }
                        else if (((statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInformedBy") >= 0)) || statement === null) && num === undefined && anonRelationCollection === undefined && namedRelationMap === undefined && effectRelationMap === undefined && causeRelationMap === undefined) {
                            return this.add$org_openprovenance_prov_model_WasInformedBy(statement);
                        }
                        else if (((statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Used") >= 0)) || statement === null) && num === undefined && anonRelationCollection === undefined && namedRelationMap === undefined && effectRelationMap === undefined && causeRelationMap === undefined) {
                            return this.add$org_openprovenance_prov_model_Used(statement);
                        }
                        else if (((statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasGeneratedBy") >= 0)) || statement === null) && num === undefined && anonRelationCollection === undefined && namedRelationMap === undefined && effectRelationMap === undefined && causeRelationMap === undefined) {
                            return this.add$org_openprovenance_prov_model_WasGeneratedBy(statement);
                        }
                        else if (((statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasDerivedFrom") >= 0)) || statement === null) && num === undefined && anonRelationCollection === undefined && namedRelationMap === undefined && effectRelationMap === undefined && causeRelationMap === undefined) {
                            return this.add$org_openprovenance_prov_model_WasDerivedFrom(statement);
                        }
                        else if (((statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasAssociatedWith") >= 0)) || statement === null) && num === undefined && anonRelationCollection === undefined && namedRelationMap === undefined && effectRelationMap === undefined && causeRelationMap === undefined) {
                            return this.add$org_openprovenance_prov_model_WasAssociatedWith(statement);
                        }
                        else if (((statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasAttributedTo") >= 0)) || statement === null) && num === undefined && anonRelationCollection === undefined && namedRelationMap === undefined && effectRelationMap === undefined && causeRelationMap === undefined) {
                            return this.add$org_openprovenance_prov_model_WasAttributedTo(statement);
                        }
                        else if (((statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.ActedOnBehalfOf") >= 0)) || statement === null) && num === undefined && anonRelationCollection === undefined && namedRelationMap === undefined && effectRelationMap === undefined && causeRelationMap === undefined) {
                            return this.add$org_openprovenance_prov_model_ActedOnBehalfOf(statement);
                        }
                        else if (((statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInvalidatedBy") >= 0)) || statement === null) && num === undefined && anonRelationCollection === undefined && namedRelationMap === undefined && effectRelationMap === undefined && causeRelationMap === undefined) {
                            return this.add$org_openprovenance_prov_model_WasInvalidatedBy(statement);
                        }
                        else if (((statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.SpecializationOf") >= 0)) || statement === null) && num === undefined && anonRelationCollection === undefined && namedRelationMap === undefined && effectRelationMap === undefined && causeRelationMap === undefined) {
                            return this.add$org_openprovenance_prov_model_SpecializationOf(statement);
                        }
                        else if (((statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.AlternateOf") >= 0)) || statement === null) && num === undefined && anonRelationCollection === undefined && namedRelationMap === undefined && effectRelationMap === undefined && causeRelationMap === undefined) {
                            return this.add$org_openprovenance_prov_model_AlternateOf(statement);
                        }
                        else if (((statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInfluencedBy") >= 0)) || statement === null) && num === undefined && anonRelationCollection === undefined && namedRelationMap === undefined && effectRelationMap === undefined && causeRelationMap === undefined) {
                            return this.add$org_openprovenance_prov_model_WasInfluencedBy(statement);
                        }
                        else if (((statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasStartedBy") >= 0)) || statement === null) && num === undefined && anonRelationCollection === undefined && namedRelationMap === undefined && effectRelationMap === undefined && causeRelationMap === undefined) {
                            return this.add$org_openprovenance_prov_model_WasStartedBy(statement);
                        }
                        else if (((statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasEndedBy") >= 0)) || statement === null) && num === undefined && anonRelationCollection === undefined && namedRelationMap === undefined && effectRelationMap === undefined && causeRelationMap === undefined) {
                            return this.add$org_openprovenance_prov_model_WasEndedBy(statement);
                        }
                        else if (((statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HadMember") >= 0)) || statement === null) && num === undefined && anonRelationCollection === undefined && namedRelationMap === undefined && effectRelationMap === undefined && causeRelationMap === undefined) {
                            return this.add$org_openprovenance_prov_model_HadMember(statement);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    doAction$org_openprovenance_prov_model_Activity(s) {
                        this.add$org_openprovenance_prov_model_Activity(s);
                    }
                    doAction$org_openprovenance_prov_model_Used(s) {
                        this.add$org_openprovenance_prov_model_Used(s);
                    }
                    doAction$org_openprovenance_prov_model_WasStartedBy(s) {
                        this.add$org_openprovenance_prov_model_WasStartedBy(s);
                    }
                    doAction$org_openprovenance_prov_model_Agent(s) {
                        this.add$org_openprovenance_prov_model_Agent(s);
                    }
                    doAction$org_openprovenance_prov_model_AlternateOf(s) {
                        this.add$org_openprovenance_prov_model_AlternateOf(s);
                    }
                    doAction$org_openprovenance_prov_model_WasAssociatedWith(s) {
                        this.add$org_openprovenance_prov_model_WasAssociatedWith(s);
                    }
                    doAction$org_openprovenance_prov_model_WasAttributedTo(s) {
                        this.add$org_openprovenance_prov_model_WasAttributedTo(s);
                    }
                    doAction$org_openprovenance_prov_model_WasInfluencedBy(s) {
                        this.add$org_openprovenance_prov_model_WasInfluencedBy(s);
                    }
                    doAction$org_openprovenance_prov_model_ActedOnBehalfOf(s) {
                        this.add$org_openprovenance_prov_model_ActedOnBehalfOf(s);
                    }
                    doAction$org_openprovenance_prov_model_WasDerivedFrom(s) {
                        this.add$org_openprovenance_prov_model_WasDerivedFrom(s);
                    }
                    doAction$org_openprovenance_prov_model_WasEndedBy(s) {
                        this.add$org_openprovenance_prov_model_WasEndedBy(s);
                    }
                    doAction$org_openprovenance_prov_model_Entity(s) {
                        this.add$org_openprovenance_prov_model_Entity(s);
                    }
                    doAction$org_openprovenance_prov_model_WasGeneratedBy(s) {
                        this.add$org_openprovenance_prov_model_WasGeneratedBy(s);
                    }
                    doAction$org_openprovenance_prov_model_WasInvalidatedBy(s) {
                        this.add$org_openprovenance_prov_model_WasInvalidatedBy(s);
                    }
                    doAction$org_openprovenance_prov_model_HadMember(s) {
                        this.add$org_openprovenance_prov_model_HadMember(s);
                    }
                    doAction$org_openprovenance_prov_model_MentionOf(s) {
                        throw Object.defineProperty(new Error(), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.UnsupportedOperationException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                    }
                    doAction$org_openprovenance_prov_model_SpecializationOf(s) {
                        this.add$org_openprovenance_prov_model_SpecializationOf(s);
                    }
                    doAction$org_openprovenance_prov_model_extension_QualifiedSpecializationOf(s) {
                        this.add$org_openprovenance_prov_model_SpecializationOf(s);
                    }
                    doAction$org_openprovenance_prov_model_extension_QualifiedAlternateOf(s) {
                        this.add$org_openprovenance_prov_model_AlternateOf(s);
                    }
                    doAction$org_openprovenance_prov_model_extension_QualifiedHadMember(s) {
                        this.add$org_openprovenance_prov_model_HadMember(s);
                    }
                    doAction$org_openprovenance_prov_model_WasInformedBy(s) {
                        this.add$org_openprovenance_prov_model_WasInformedBy(s);
                    }
                    doAction$org_openprovenance_prov_model_DerivedByInsertionFrom(s) {
                        throw Object.defineProperty(new Error(), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.UnsupportedOperationException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                    }
                    doAction$org_openprovenance_prov_model_DictionaryMembership(s) {
                        throw Object.defineProperty(new Error(), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.UnsupportedOperationException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                    }
                    doAction$org_openprovenance_prov_model_DerivedByRemovalFrom(s) {
                        throw Object.defineProperty(new Error(), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.UnsupportedOperationException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                    }
                    doAction$org_openprovenance_prov_model_Bundle$org_openprovenance_prov_model_ProvUtilities(bun, provUtilities) {
                        if (this.flatten) {
                            provUtilities.forAllStatement(bun.getStatement(), this);
                        }
                        else {
                            let iDoc = ((m, k) => { if (m.entries == null)
                                m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                    return m.entries[i].value;
                                } return null; })(this.bundleMap, bun.getId());
                            if (iDoc == null) {
                                iDoc = new IndexedDocument(this.pFactory, null, this.flatten);
                                /* put */ ((m, k, v) => { if (m.entries == null)
                                    m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                    if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                        m.entries[i].value = v;
                                        return;
                                    } m.entries.push({ key: k, value: v, getKey: function () { return this.key; }, getValue: function () { return this.value; } }); })(this.bundleMap, bun.getId(), iDoc);
                            }
                            this.u.forAllStatement(bun.getStatement(), iDoc);
                        }
                    }
                    /**
                     *
                     * @param {*} bun
                     * @param {org.openprovenance.prov.model.ProvUtilities} provUtilities
                     */
                    doAction(bun, provUtilities) {
                        if (((bun != null && (bun.constructor != null && bun.constructor["__interfaces"] != null && bun.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Bundle") >= 0)) || bun === null) && ((provUtilities != null && provUtilities instanceof org.openprovenance.prov.model.ProvUtilities) || provUtilities === null)) {
                            return this.doAction$org_openprovenance_prov_model_Bundle$org_openprovenance_prov_model_ProvUtilities(bun, provUtilities);
                        }
                        else if (((bun != null && (bun.constructor != null && bun.constructor["__interfaces"] != null && bun.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Activity") >= 0)) || bun === null) && provUtilities === undefined) {
                            return this.doAction$org_openprovenance_prov_model_Activity(bun);
                        }
                        else if (((bun != null && (bun.constructor != null && bun.constructor["__interfaces"] != null && bun.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Used") >= 0)) || bun === null) && provUtilities === undefined) {
                            return this.doAction$org_openprovenance_prov_model_Used(bun);
                        }
                        else if (((bun != null && (bun.constructor != null && bun.constructor["__interfaces"] != null && bun.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasStartedBy") >= 0)) || bun === null) && provUtilities === undefined) {
                            return this.doAction$org_openprovenance_prov_model_WasStartedBy(bun);
                        }
                        else if (((bun != null && (bun.constructor != null && bun.constructor["__interfaces"] != null && bun.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Agent") >= 0)) || bun === null) && provUtilities === undefined) {
                            return this.doAction$org_openprovenance_prov_model_Agent(bun);
                        }
                        else if (((bun != null && (bun.constructor != null && bun.constructor["__interfaces"] != null && bun.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.AlternateOf") >= 0)) || bun === null) && provUtilities === undefined) {
                            return this.doAction$org_openprovenance_prov_model_AlternateOf(bun);
                        }
                        else if (((bun != null && (bun.constructor != null && bun.constructor["__interfaces"] != null && bun.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasAssociatedWith") >= 0)) || bun === null) && provUtilities === undefined) {
                            return this.doAction$org_openprovenance_prov_model_WasAssociatedWith(bun);
                        }
                        else if (((bun != null && (bun.constructor != null && bun.constructor["__interfaces"] != null && bun.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasAttributedTo") >= 0)) || bun === null) && provUtilities === undefined) {
                            return this.doAction$org_openprovenance_prov_model_WasAttributedTo(bun);
                        }
                        else if (((bun != null && (bun.constructor != null && bun.constructor["__interfaces"] != null && bun.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInfluencedBy") >= 0)) || bun === null) && provUtilities === undefined) {
                            return this.doAction$org_openprovenance_prov_model_WasInfluencedBy(bun);
                        }
                        else if (((bun != null && (bun.constructor != null && bun.constructor["__interfaces"] != null && bun.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.ActedOnBehalfOf") >= 0)) || bun === null) && provUtilities === undefined) {
                            return this.doAction$org_openprovenance_prov_model_ActedOnBehalfOf(bun);
                        }
                        else if (((bun != null && (bun.constructor != null && bun.constructor["__interfaces"] != null && bun.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasDerivedFrom") >= 0)) || bun === null) && provUtilities === undefined) {
                            return this.doAction$org_openprovenance_prov_model_WasDerivedFrom(bun);
                        }
                        else if (((bun != null && (bun.constructor != null && bun.constructor["__interfaces"] != null && bun.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasEndedBy") >= 0)) || bun === null) && provUtilities === undefined) {
                            return this.doAction$org_openprovenance_prov_model_WasEndedBy(bun);
                        }
                        else if (((bun != null && (bun.constructor != null && bun.constructor["__interfaces"] != null && bun.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Entity") >= 0)) || bun === null) && provUtilities === undefined) {
                            return this.doAction$org_openprovenance_prov_model_Entity(bun);
                        }
                        else if (((bun != null && (bun.constructor != null && bun.constructor["__interfaces"] != null && bun.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasGeneratedBy") >= 0)) || bun === null) && provUtilities === undefined) {
                            return this.doAction$org_openprovenance_prov_model_WasGeneratedBy(bun);
                        }
                        else if (((bun != null && (bun.constructor != null && bun.constructor["__interfaces"] != null && bun.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInvalidatedBy") >= 0)) || bun === null) && provUtilities === undefined) {
                            return this.doAction$org_openprovenance_prov_model_WasInvalidatedBy(bun);
                        }
                        else if (((bun != null && (bun.constructor != null && bun.constructor["__interfaces"] != null && bun.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.extension.QualifiedHadMember") >= 0)) || bun === null) && provUtilities === undefined) {
                            return this.doAction$org_openprovenance_prov_model_extension_QualifiedHadMember(bun);
                        }
                        else if (((bun != null && (bun.constructor != null && bun.constructor["__interfaces"] != null && bun.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HadMember") >= 0)) || bun === null) && provUtilities === undefined) {
                            return this.doAction$org_openprovenance_prov_model_HadMember(bun);
                        }
                        else if (((bun != null && (bun.constructor != null && bun.constructor["__interfaces"] != null && bun.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.MentionOf") >= 0)) || bun === null) && provUtilities === undefined) {
                            return this.doAction$org_openprovenance_prov_model_MentionOf(bun);
                        }
                        else if (((bun != null && (bun.constructor != null && bun.constructor["__interfaces"] != null && bun.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.extension.QualifiedSpecializationOf") >= 0)) || bun === null) && provUtilities === undefined) {
                            return this.doAction$org_openprovenance_prov_model_extension_QualifiedSpecializationOf(bun);
                        }
                        else if (((bun != null && (bun.constructor != null && bun.constructor["__interfaces"] != null && bun.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.SpecializationOf") >= 0)) || bun === null) && provUtilities === undefined) {
                            return this.doAction$org_openprovenance_prov_model_SpecializationOf(bun);
                        }
                        else if (((bun != null && (bun.constructor != null && bun.constructor["__interfaces"] != null && bun.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.extension.QualifiedAlternateOf") >= 0)) || bun === null) && provUtilities === undefined) {
                            return this.doAction$org_openprovenance_prov_model_extension_QualifiedAlternateOf(bun);
                        }
                        else if (((bun != null && (bun.constructor != null && bun.constructor["__interfaces"] != null && bun.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInformedBy") >= 0)) || bun === null) && provUtilities === undefined) {
                            return this.doAction$org_openprovenance_prov_model_WasInformedBy(bun);
                        }
                        else if (((bun != null && (bun.constructor != null && bun.constructor["__interfaces"] != null && bun.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.DerivedByInsertionFrom") >= 0)) || bun === null) && provUtilities === undefined) {
                            return this.doAction$org_openprovenance_prov_model_DerivedByInsertionFrom(bun);
                        }
                        else if (((bun != null && (bun.constructor != null && bun.constructor["__interfaces"] != null && bun.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.DictionaryMembership") >= 0)) || bun === null) && provUtilities === undefined) {
                            return this.doAction$org_openprovenance_prov_model_DictionaryMembership(bun);
                        }
                        else if (((bun != null && (bun.constructor != null && bun.constructor["__interfaces"] != null && bun.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.DerivedByRemovalFrom") >= 0)) || bun === null) && provUtilities === undefined) {
                            return this.doAction$org_openprovenance_prov_model_DerivedByRemovalFrom(bun);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    toDocument() {
                        const res = this.pFactory.newDocument$();
                        const statementOrBundle = res.getStatementOrBundle();
                        this.toContainer(statementOrBundle);
                        if (!this.flatten) {
                            {
                                let array205 = /* keySet */ ((m) => { let r = []; if (m.entries == null)
                                    m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                    r.push(m.entries[i].key); return r; })(this.bundleMap);
                                for (let index204 = 0; index204 < array205.length; index204++) {
                                    let bunId = array205[index204];
                                    {
                                        const idoc = ((m, k) => { if (m.entries == null)
                                            m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                            if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                                return m.entries[i].value;
                                            } return null; })(this.bundleMap, bunId);
                                        const bun = this.pFactory.newNamedBundle$org_openprovenance_prov_model_QualifiedName$java_util_Collection(bunId, null);
                                        const ll = ([]);
                                        idoc.toContainer(ll);
                                        for (let index206 = 0; index206 < ll.length; index206++) {
                                            let s = ll[index206];
                                            {
                                                /* add */ (bun.getStatement().push(s) > 0);
                                            }
                                        }
                                        bun.setNamespace(org.openprovenance.prov.model.Namespace.gatherNamespaces$org_openprovenance_prov_model_Bundle(bun));
                                        /* add */ (statementOrBundle.push(bun) > 0);
                                    }
                                }
                            }
                        }
                        res.setNamespace(org.openprovenance.prov.model.Namespace.gatherNamespaces$org_openprovenance_prov_model_Document(res));
                        return res;
                    }
                    /*private*/ toContainer(statementOrBundle) {
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, /* values */ ((m) => { let r = []; if (m.entries == null)
                            m.entries = []; for (let i = 0; i < m.entries.length; i++)
                            r.push(m.entries[i].value); return r; })(this.entityMap));
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, /* values */ ((m) => { let r = []; if (m.entries == null)
                            m.entries = []; for (let i = 0; i < m.entries.length; i++)
                            r.push(m.entries[i].value); return r; })(this.activityMap));
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, /* values */ ((m) => { let r = []; if (m.entries == null)
                            m.entries = []; for (let i = 0; i < m.entries.length; i++)
                            r.push(m.entries[i].value); return r; })(this.agentMap));
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, this.anonUsed);
                        {
                            let array208 = /* values */ ((m) => { let r = []; if (m.entries == null)
                                m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                r.push(m.entries[i].value); return r; })(this.namedUsedMap);
                            for (let index207 = 0; index207 < array208.length; index207++) {
                                let c = array208[index207];
                                {
                                    /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, c);
                                }
                            }
                        }
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, this.anonWasGeneratedBy);
                        {
                            let array210 = /* values */ ((m) => { let r = []; if (m.entries == null)
                                m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                r.push(m.entries[i].value); return r; })(this.namedWasGeneratedByMap);
                            for (let index209 = 0; index209 < array210.length; index209++) {
                                let c = array210[index209];
                                {
                                    /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, c);
                                }
                            }
                        }
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, this.anonWasDerivedFrom);
                        {
                            let array212 = /* values */ ((m) => { let r = []; if (m.entries == null)
                                m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                r.push(m.entries[i].value); return r; })(this.namedWasDerivedFromMap);
                            for (let index211 = 0; index211 < array212.length; index211++) {
                                let c = array212[index211];
                                {
                                    /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, c);
                                }
                            }
                        }
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, this.anonWasAssociatedWith);
                        {
                            let array214 = /* values */ ((m) => { let r = []; if (m.entries == null)
                                m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                r.push(m.entries[i].value); return r; })(this.namedWasAssociatedWithMap);
                            for (let index213 = 0; index213 < array214.length; index213++) {
                                let c = array214[index213];
                                {
                                    /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, c);
                                }
                            }
                        }
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, this.anonWasAttributedTo);
                        {
                            let array216 = /* values */ ((m) => { let r = []; if (m.entries == null)
                                m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                r.push(m.entries[i].value); return r; })(this.namedWasAttributedToMap);
                            for (let index215 = 0; index215 < array216.length; index215++) {
                                let c = array216[index215];
                                {
                                    /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, c);
                                }
                            }
                        }
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, this.anonWasInformedBy);
                        {
                            let array218 = /* values */ ((m) => { let r = []; if (m.entries == null)
                                m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                r.push(m.entries[i].value); return r; })(this.namedWasInformedByMap);
                            for (let index217 = 0; index217 < array218.length; index217++) {
                                let c = array218[index217];
                                {
                                    /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, c);
                                }
                            }
                        }
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, this.anonSpecializationOf);
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, this.anonAlternateOf);
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, this.anonHadMember);
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, this.anonWasInvalidatedBy);
                        {
                            let array220 = /* values */ ((m) => { let r = []; if (m.entries == null)
                                m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                r.push(m.entries[i].value); return r; })(this.namedWasInvalidatedByMap);
                            for (let index219 = 0; index219 < array220.length; index219++) {
                                let c = array220[index219];
                                {
                                    /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, c);
                                }
                            }
                        }
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, this.anonWasStartedBy);
                        {
                            let array222 = /* values */ ((m) => { let r = []; if (m.entries == null)
                                m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                r.push(m.entries[i].value); return r; })(this.namedWasStartedByMap);
                            for (let index221 = 0; index221 < array222.length; index221++) {
                                let c = array222[index221];
                                {
                                    /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, c);
                                }
                            }
                        }
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, this.anonWasEndedBy);
                        {
                            let array224 = /* values */ ((m) => { let r = []; if (m.entries == null)
                                m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                r.push(m.entries[i].value); return r; })(this.namedWasEndedByMap);
                            for (let index223 = 0; index223 < array224.length; index223++) {
                                let c = array224[index223];
                                {
                                    /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, c);
                                }
                            }
                        }
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, this.anonActedOnBehalfOf);
                        {
                            let array226 = /* values */ ((m) => { let r = []; if (m.entries == null)
                                m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                r.push(m.entries[i].value); return r; })(this.namedActedOnBehalfOfMap);
                            for (let index225 = 0; index225 < array226.length; index225++) {
                                let c = array226[index225];
                                {
                                    /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, c);
                                }
                            }
                        }
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, this.anonWasInfluencedBy);
                        {
                            let array228 = /* values */ ((m) => { let r = []; if (m.entries == null)
                                m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                r.push(m.entries[i].value); return r; })(this.namedWasInfluencedByMap);
                            for (let index227 = 0; index227 < array228.length; index227++) {
                                let c = array228[index227];
                                {
                                    /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, c);
                                }
                            }
                        }
                    }
                    /**
                     * This function allows a document to be merged with this IndexedDocument. If flatten is true, bundles include in the document will be flattend into this one.
                     *
                     *
                     * @param {*} doc the document to be merge into this
                     */
                    merge(doc) {
                        this.u.forAllStatementOrBundle(doc.getStatementOrBundle(), this);
                    }
                    traverseDerivations(from) {
                        const s = ([]);
                        /* push */ (s.push(from) > 0);
                        return this.traverseDerivations1(([]), ([]), s);
                    }
                    traverseDerivations1(last, seen, todo) {
                        let current = null;
                        while ((!( /* isEmpty */(todo.length == 0)))) {
                            {
                                current = /* pop */ todo.pop();
                                if ( /* contains */(seen.indexOf((current)) >= 0)) {
                                }
                                else {
                                    /* add */ ((s, e) => { if (s.indexOf(e) == -1) {
                                        s.push(e);
                                        return true;
                                    }
                                    else {
                                        return false;
                                    } })(seen, current);
                                    const successors = ((m, k) => { if (m.entries == null)
                                        m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                        if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                            return m.entries[i].value;
                                        } return null; })(this.entityCauseWasDerivedFromMap, current);
                                    if (successors == null || /* isEmpty */ (successors.length == 0)) {
                                    }
                                    else {
                                        for (let index229 = 0; index229 < successors.length; index229++) {
                                            let wdf = successors[index229];
                                            {
                                                const qn = wdf.getGeneratedEntity();
                                                /* add */ ((s, e) => { if (s.indexOf(e) == -1) {
                                                    s.push(e);
                                                    return true;
                                                }
                                                else {
                                                    return false;
                                                } })(last, qn);
                                                /* push */ (todo.push(qn) > 0);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        ;
                        return last;
                    }
                }
                model.IndexedDocument = IndexedDocument;
                IndexedDocument["__class"] = "org.openprovenance.prov.model.IndexedDocument";
                IndexedDocument["__interfaces"] = ["org.openprovenance.prov.model.StatementAction"];
            })(model = prov.model || (prov.model = {}));
        })(prov = openprovenance.prov || (openprovenance.prov = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
