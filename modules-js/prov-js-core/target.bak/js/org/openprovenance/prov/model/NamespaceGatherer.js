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
                 * Utility class to traverse a document, register all namespaces occurring in {@link QualifiedName}s
                 * and attributes as well as associated prefixes, and create a {@link Namespace} datastructure.
                 *
                 * @author lavm
                 * @param {*} prefixes
                 * @param {string} defaultNamespace
                 * @class
                 */
                class NamespaceGatherer {
                    constructor(prefixes, defaultNamespace) {
                        if (((prefixes != null && (prefixes instanceof Object)) || prefixes === null) && ((typeof defaultNamespace === 'string') || defaultNamespace === null)) {
                            let __args = arguments;
                            this.ns = new org.openprovenance.prov.model.Namespace();
                            this.ns.getPrefixes().putAll(prefixes);
                            this.ns.setDefaultNamespace(defaultNamespace);
                        }
                        else if (prefixes === undefined && defaultNamespace === undefined) {
                            let __args = arguments;
                            this.ns = new org.openprovenance.prov.model.Namespace();
                            this.ns.addKnownNamespaces();
                            this.ns.setDefaultNamespace(null);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    static pu_$LI$() { if (NamespaceGatherer.pu == null) {
                        NamespaceGatherer.pu = new org.openprovenance.prov.model.ProvUtilities();
                    } return NamespaceGatherer.pu; }
                    /**
                     * Accumulate all namespace declarations in a single {@link Namespace} instance.
                     * This includes the Document-level {@link Namespace} but also all Bundle-level {@link Namespace}s.
                     *
                     * <p>This method is particular useful before serialization to XML since JAXB doesn't offer us the
                     * means to generate prefix declaration in inner Elements. Hence, all namespaces need to be declared
                     * at the root of the xml document.
                     *
                     * @param {*} document Document from which Namespaces are accumulated
                     * @return {org.openprovenance.prov.model.Namespace} a new instance of {@link Namespace}
                     */
                    static accumulateAllNamespaces(document) {
                        const res = new org.openprovenance.prov.model.Namespace(document.getNamespace());
                        {
                            let array167 = NamespaceGatherer.pu_$LI$().getNamedBundle(document);
                            for (let index166 = 0; index166 < array167.length; index166++) {
                                let b = array167[index166];
                                {
                                    const ns = b.getNamespace();
                                    if (ns != null)
                                        res.extendWith(ns);
                                }
                            }
                        }
                        return res;
                    }
                    getNamespace() {
                        return this.ns;
                    }
                    registerLocation(locations) {
                        for (let index168 = 0; index168 < locations.length; index168++) {
                            let loc = locations[index168];
                            {
                                this.register$org_openprovenance_prov_model_Location(loc);
                            }
                        }
                    }
                    registerPotentialQualifiedName(o) {
                        if (o != null && (o.constructor != null && o.constructor["__interfaces"] != null && o.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) {
                            this.register$org_openprovenance_prov_model_QualifiedName(o);
                        }
                    }
                    register$org_openprovenance_prov_model_Location(loc) {
                        if (loc != null) {
                            this.register$org_openprovenance_prov_model_QualifiedName(loc.getType());
                            const val = loc.getValue();
                            this.registerPotentialQualifiedName(val);
                        }
                    }
                    register(loc) {
                        if (((loc != null && (loc.constructor != null && loc.constructor["__interfaces"] != null && loc.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Location") >= 0)) || loc === null)) {
                            return this.register$org_openprovenance_prov_model_Location(loc);
                        }
                        else if (((loc != null && (loc.constructor != null && loc.constructor["__interfaces"] != null && loc.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Type") >= 0)) || loc === null)) {
                            return this.register$org_openprovenance_prov_model_Type(loc);
                        }
                        else if (((loc != null && (loc.constructor != null && loc.constructor["__interfaces"] != null && loc.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Role") >= 0)) || loc === null)) {
                            return this.register$org_openprovenance_prov_model_Role(loc);
                        }
                        else if (((loc != null && (loc.constructor != null && loc.constructor["__interfaces"] != null && loc.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Other") >= 0)) || loc === null)) {
                            return this.register$org_openprovenance_prov_model_Other(loc);
                        }
                        else if (((loc != null && (loc.constructor != null && loc.constructor["__interfaces"] != null && loc.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || loc === null)) {
                            return this.register$org_openprovenance_prov_model_QualifiedName(loc);
                        }
                        else if (((loc != null && (loc.constructor != null && loc.constructor["__interfaces"] != null && loc.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Key") >= 0)) || loc === null)) {
                            return this.register$org_openprovenance_prov_model_Key(loc);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    registerType(types) {
                        for (let index169 = 0; index169 < types.length; index169++) {
                            let typ = types[index169];
                            {
                                this.register$org_openprovenance_prov_model_Type(typ);
                            }
                        }
                    }
                    register$org_openprovenance_prov_model_Type(typ) {
                        this.register$org_openprovenance_prov_model_QualifiedName(typ.getType());
                        const val = typ.getValue();
                        this.registerPotentialQualifiedName(val);
                    }
                    registerRole(roles) {
                        for (let index170 = 0; index170 < roles.length; index170++) {
                            let rol = roles[index170];
                            {
                                this.register$org_openprovenance_prov_model_Role(rol);
                            }
                        }
                    }
                    register$org_openprovenance_prov_model_Role(rol) {
                        this.register$org_openprovenance_prov_model_QualifiedName(rol.getType());
                        const val = rol.getValue();
                        this.registerPotentialQualifiedName(val);
                    }
                    registerOther(others) {
                        for (let index171 = 0; index171 < others.length; index171++) {
                            let other = others[index171];
                            {
                                this.register$org_openprovenance_prov_model_Other(other);
                            }
                        }
                    }
                    register$org_openprovenance_prov_model_Other(other) {
                        this.register$org_openprovenance_prov_model_QualifiedName(other.getType());
                        this.register$org_openprovenance_prov_model_QualifiedName(other.getElementName());
                        const val = other.getValue();
                        this.registerPotentialQualifiedName(val);
                    }
                    registerValue(val2) {
                        if (val2 != null) {
                            this.register$org_openprovenance_prov_model_QualifiedName(val2.getType());
                            const val = val2.getValue();
                            this.registerPotentialQualifiedName(val);
                        }
                    }
                    register$org_openprovenance_prov_model_QualifiedName(name) {
                        if (name == null)
                            return;
                        const namespace = name.getNamespaceURI();
                        const prefix = name.getPrefix();
                        if ((prefix == null) || (prefix === javax.xml.XMLConstants.DEFAULT_NS_PREFIX)) {
                            this.ns.registerDefault(namespace);
                        }
                        else {
                            this.ns.register(prefix, namespace);
                        }
                    }
                    doAction$org_openprovenance_prov_model_HadMember(mem) {
                        this.register$org_openprovenance_prov_model_QualifiedName(mem.getCollection());
                        {
                            let array173 = mem.getEntity();
                            for (let index172 = 0; index172 < array173.length; index172++) {
                                let i = array173[index172];
                                {
                                    this.register$org_openprovenance_prov_model_QualifiedName(i);
                                }
                            }
                        }
                    }
                    doAction$org_openprovenance_prov_model_extension_QualifiedHadMember(mem) {
                        this.register$org_openprovenance_prov_model_QualifiedName(mem.getId());
                        this.register$org_openprovenance_prov_model_QualifiedName(mem.getCollection());
                        {
                            let array175 = mem.getEntity();
                            for (let index174 = 0; index174 < array175.length; index174++) {
                                let i = array175[index174];
                                {
                                    this.register$org_openprovenance_prov_model_QualifiedName(i);
                                }
                            }
                        }
                        this.registerType(mem.getType());
                        this.registerOther(mem.getOther());
                    }
                    doAction$org_openprovenance_prov_model_SpecializationOf(spec) {
                        this.register$org_openprovenance_prov_model_QualifiedName(spec.getGeneralEntity());
                        this.register$org_openprovenance_prov_model_QualifiedName(spec.getSpecificEntity());
                    }
                    doAction$org_openprovenance_prov_model_extension_QualifiedSpecializationOf(spec) {
                        this.register$org_openprovenance_prov_model_QualifiedName(spec.getId());
                        this.register$org_openprovenance_prov_model_QualifiedName(spec.getGeneralEntity());
                        this.register$org_openprovenance_prov_model_QualifiedName(spec.getSpecificEntity());
                        this.registerType(spec.getType());
                        this.registerOther(spec.getOther());
                    }
                    doAction$org_openprovenance_prov_model_extension_QualifiedAlternateOf(alt) {
                        this.register$org_openprovenance_prov_model_QualifiedName(alt.getId());
                        this.register$org_openprovenance_prov_model_QualifiedName(alt.getAlternate1());
                        this.register$org_openprovenance_prov_model_QualifiedName(alt.getAlternate2());
                        this.registerType(alt.getType());
                        this.registerOther(alt.getOther());
                    }
                    doAction$org_openprovenance_prov_model_MentionOf(men) {
                        this.register$org_openprovenance_prov_model_QualifiedName(men.getBundle());
                        this.register$org_openprovenance_prov_model_QualifiedName(men.getGeneralEntity());
                        this.register$org_openprovenance_prov_model_QualifiedName(men.getSpecificEntity());
                    }
                    doAction$org_openprovenance_prov_model_AlternateOf(alt) {
                        this.register$org_openprovenance_prov_model_QualifiedName(alt.getAlternate1());
                        this.register$org_openprovenance_prov_model_QualifiedName(alt.getAlternate2());
                    }
                    doAction$org_openprovenance_prov_model_WasInfluencedBy(inf) {
                        this.register$org_openprovenance_prov_model_QualifiedName(inf.getId());
                        this.register$org_openprovenance_prov_model_QualifiedName(inf.getInfluencee());
                        this.register$org_openprovenance_prov_model_QualifiedName(inf.getInfluencer());
                        this.registerType(inf.getType());
                        this.registerOther(inf.getOther());
                    }
                    doAction$org_openprovenance_prov_model_ActedOnBehalfOf(del) {
                        this.register$org_openprovenance_prov_model_QualifiedName(del.getId());
                        this.register$org_openprovenance_prov_model_QualifiedName(del.getDelegate());
                        this.register$org_openprovenance_prov_model_QualifiedName(del.getResponsible());
                        this.register$org_openprovenance_prov_model_QualifiedName(del.getActivity());
                        this.registerType(del.getType());
                        this.registerOther(del.getOther());
                    }
                    doAction$org_openprovenance_prov_model_WasAttributedTo(attr) {
                        this.register$org_openprovenance_prov_model_QualifiedName(attr.getId());
                        this.register$org_openprovenance_prov_model_QualifiedName(attr.getEntity());
                        this.register$org_openprovenance_prov_model_QualifiedName(attr.getAgent());
                        this.registerType(attr.getType());
                        this.registerOther(attr.getOther());
                    }
                    doAction$org_openprovenance_prov_model_WasAssociatedWith(assoc) {
                        this.register$org_openprovenance_prov_model_QualifiedName(assoc.getId());
                        this.register$org_openprovenance_prov_model_QualifiedName(assoc.getActivity());
                        this.register$org_openprovenance_prov_model_QualifiedName(assoc.getAgent());
                        this.register$org_openprovenance_prov_model_QualifiedName(assoc.getPlan());
                        this.registerRole(assoc.getRole());
                        this.registerType(assoc.getType());
                        this.registerOther(assoc.getOther());
                    }
                    doAction$org_openprovenance_prov_model_WasDerivedFrom(der) {
                        this.register$org_openprovenance_prov_model_QualifiedName(der.getId());
                        this.register$org_openprovenance_prov_model_QualifiedName(der.getGeneratedEntity());
                        this.register$org_openprovenance_prov_model_QualifiedName(der.getUsedEntity());
                        this.register$org_openprovenance_prov_model_QualifiedName(der.getActivity());
                        this.register$org_openprovenance_prov_model_QualifiedName(der.getGeneration());
                        this.register$org_openprovenance_prov_model_QualifiedName(der.getUsage());
                        this.registerType(der.getType());
                        this.registerOther(der.getOther());
                    }
                    doAction$org_openprovenance_prov_model_WasInformedBy(inf) {
                        this.register$org_openprovenance_prov_model_QualifiedName(inf.getId());
                        this.register$org_openprovenance_prov_model_QualifiedName(inf.getInformed());
                        this.register$org_openprovenance_prov_model_QualifiedName(inf.getInformant());
                        this.registerType(inf.getType());
                        this.registerOther(inf.getOther());
                    }
                    doAction$org_openprovenance_prov_model_WasEndedBy(end) {
                        this.register$org_openprovenance_prov_model_QualifiedName(end.getId());
                        this.register$org_openprovenance_prov_model_QualifiedName(end.getActivity());
                        this.register$org_openprovenance_prov_model_QualifiedName(end.getEnder());
                        this.register$org_openprovenance_prov_model_QualifiedName(end.getTrigger());
                        this.registerLocation(end.getLocation());
                        this.registerType(end.getType());
                        this.registerRole(end.getRole());
                        this.registerOther(end.getOther());
                    }
                    doAction$org_openprovenance_prov_model_WasStartedBy(start) {
                        this.register$org_openprovenance_prov_model_QualifiedName(start.getId());
                        this.register$org_openprovenance_prov_model_QualifiedName(start.getActivity());
                        this.register$org_openprovenance_prov_model_QualifiedName(start.getStarter());
                        this.register$org_openprovenance_prov_model_QualifiedName(start.getTrigger());
                        this.registerLocation(start.getLocation());
                        this.registerType(start.getType());
                        this.registerRole(start.getRole());
                        this.registerOther(start.getOther());
                    }
                    doAction$org_openprovenance_prov_model_WasInvalidatedBy(inv) {
                        this.register$org_openprovenance_prov_model_QualifiedName(inv.getId());
                        this.register$org_openprovenance_prov_model_QualifiedName(inv.getEntity());
                        this.register$org_openprovenance_prov_model_QualifiedName(inv.getActivity());
                        this.registerLocation(inv.getLocation());
                        this.registerRole(inv.getRole());
                        this.registerType(inv.getType());
                        this.registerOther(inv.getOther());
                    }
                    doAction$org_openprovenance_prov_model_Used(use) {
                        this.register$org_openprovenance_prov_model_QualifiedName(use.getId());
                        this.register$org_openprovenance_prov_model_QualifiedName(use.getEntity());
                        this.register$org_openprovenance_prov_model_QualifiedName(use.getActivity());
                        this.registerLocation(use.getLocation());
                        this.registerRole(use.getRole());
                        this.registerType(use.getType());
                        this.registerOther(use.getOther());
                    }
                    doAction$org_openprovenance_prov_model_WasGeneratedBy(gen) {
                        this.register$org_openprovenance_prov_model_QualifiedName(gen.getId());
                        this.register$org_openprovenance_prov_model_QualifiedName(gen.getEntity());
                        this.register$org_openprovenance_prov_model_QualifiedName(gen.getActivity());
                        this.registerLocation(gen.getLocation());
                        this.registerRole(gen.getRole());
                        this.registerType(gen.getType());
                        this.registerOther(gen.getOther());
                    }
                    doAction$org_openprovenance_prov_model_Agent(ag) {
                        this.register$org_openprovenance_prov_model_QualifiedName(ag.getId());
                        this.registerLocation(ag.getLocation());
                        this.registerType(ag.getType());
                        this.registerOther(ag.getOther());
                    }
                    doAction$org_openprovenance_prov_model_Activity(a) {
                        this.register$org_openprovenance_prov_model_QualifiedName(a.getId());
                        this.registerLocation(a.getLocation());
                        this.registerType(a.getType());
                        this.registerOther(a.getOther());
                    }
                    doAction$org_openprovenance_prov_model_Entity(e) {
                        this.register$org_openprovenance_prov_model_QualifiedName(e.getId());
                        this.registerLocation(e.getLocation());
                        this.registerType(e.getType());
                        this.registerValue(e.getValue());
                        this.registerOther(e.getOther());
                    }
                    doAction$org_openprovenance_prov_model_DictionaryMembership(m) {
                        this.register$org_openprovenance_prov_model_QualifiedName(m.getDictionary());
                        this.registerEntry(m.getKeyEntityPair());
                    }
                    doAction$org_openprovenance_prov_model_DerivedByRemovalFrom(r) {
                        this.register$org_openprovenance_prov_model_QualifiedName(r.getId());
                        this.register$org_openprovenance_prov_model_QualifiedName(r.getNewDictionary());
                        this.register$org_openprovenance_prov_model_QualifiedName(r.getOldDictionary());
                        this.registerType(r.getType());
                        this.registerOther(r.getOther());
                        {
                            let array177 = r.getKey();
                            for (let index176 = 0; index176 < array177.length; index176++) {
                                let k = array177[index176];
                                {
                                    this.register$org_openprovenance_prov_model_Key(k);
                                }
                            }
                        }
                    }
                    doAction$org_openprovenance_prov_model_DerivedByInsertionFrom(i) {
                        this.register$org_openprovenance_prov_model_QualifiedName(i.getId());
                        this.register$org_openprovenance_prov_model_QualifiedName(i.getNewDictionary());
                        this.register$org_openprovenance_prov_model_QualifiedName(i.getOldDictionary());
                        this.registerType(i.getType());
                        this.registerOther(i.getOther());
                        this.registerEntry(i.getKeyEntityPair());
                    }
                    registerEntry(keyEntityPairs) {
                        for (let index178 = 0; index178 < keyEntityPairs.length; index178++) {
                            let e = keyEntityPairs[index178];
                            {
                                this.register$org_openprovenance_prov_model_QualifiedName(e.getEntity());
                                const key = e.getKey();
                                this.register$org_openprovenance_prov_model_Key(key);
                            }
                        }
                    }
                    register$org_openprovenance_prov_model_Key(key) {
                        this.registerPotentialQualifiedName(key.getValue());
                        this.register$org_openprovenance_prov_model_QualifiedName(key.getType());
                    }
                    doAction$org_openprovenance_prov_model_Bundle$org_openprovenance_prov_model_ProvUtilities(bu, u) {
                        this.register$org_openprovenance_prov_model_QualifiedName(bu.getId());
                        {
                            let array180 = bu.getStatement();
                            for (let index179 = 0; index179 < array180.length; index179++) {
                                let s2 = array180[index179];
                                {
                                    u.doAction$org_openprovenance_prov_model_StatementOrBundle$org_openprovenance_prov_model_StatementAction(s2, this);
                                }
                            }
                        }
                    }
                    /**
                     *
                     * @param {*} bu
                     * @param {org.openprovenance.prov.model.ProvUtilities} u
                     */
                    doAction(bu, u) {
                        if (((bu != null && (bu.constructor != null && bu.constructor["__interfaces"] != null && bu.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Bundle") >= 0)) || bu === null) && ((u != null && u instanceof org.openprovenance.prov.model.ProvUtilities) || u === null)) {
                            return this.doAction$org_openprovenance_prov_model_Bundle$org_openprovenance_prov_model_ProvUtilities(bu, u);
                        }
                        else if (((bu != null && (bu.constructor != null && bu.constructor["__interfaces"] != null && bu.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.extension.QualifiedHadMember") >= 0)) || bu === null) && u === undefined) {
                            return this.doAction$org_openprovenance_prov_model_extension_QualifiedHadMember(bu);
                        }
                        else if (((bu != null && (bu.constructor != null && bu.constructor["__interfaces"] != null && bu.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HadMember") >= 0)) || bu === null) && u === undefined) {
                            return this.doAction$org_openprovenance_prov_model_HadMember(bu);
                        }
                        else if (((bu != null && (bu.constructor != null && bu.constructor["__interfaces"] != null && bu.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.extension.QualifiedSpecializationOf") >= 0)) || bu === null) && u === undefined) {
                            return this.doAction$org_openprovenance_prov_model_extension_QualifiedSpecializationOf(bu);
                        }
                        else if (((bu != null && (bu.constructor != null && bu.constructor["__interfaces"] != null && bu.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.SpecializationOf") >= 0)) || bu === null) && u === undefined) {
                            return this.doAction$org_openprovenance_prov_model_SpecializationOf(bu);
                        }
                        else if (((bu != null && (bu.constructor != null && bu.constructor["__interfaces"] != null && bu.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.extension.QualifiedAlternateOf") >= 0)) || bu === null) && u === undefined) {
                            return this.doAction$org_openprovenance_prov_model_extension_QualifiedAlternateOf(bu);
                        }
                        else if (((bu != null && (bu.constructor != null && bu.constructor["__interfaces"] != null && bu.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.MentionOf") >= 0)) || bu === null) && u === undefined) {
                            return this.doAction$org_openprovenance_prov_model_MentionOf(bu);
                        }
                        else if (((bu != null && (bu.constructor != null && bu.constructor["__interfaces"] != null && bu.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.AlternateOf") >= 0)) || bu === null) && u === undefined) {
                            return this.doAction$org_openprovenance_prov_model_AlternateOf(bu);
                        }
                        else if (((bu != null && (bu.constructor != null && bu.constructor["__interfaces"] != null && bu.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInfluencedBy") >= 0)) || bu === null) && u === undefined) {
                            return this.doAction$org_openprovenance_prov_model_WasInfluencedBy(bu);
                        }
                        else if (((bu != null && (bu.constructor != null && bu.constructor["__interfaces"] != null && bu.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.ActedOnBehalfOf") >= 0)) || bu === null) && u === undefined) {
                            return this.doAction$org_openprovenance_prov_model_ActedOnBehalfOf(bu);
                        }
                        else if (((bu != null && (bu.constructor != null && bu.constructor["__interfaces"] != null && bu.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasAttributedTo") >= 0)) || bu === null) && u === undefined) {
                            return this.doAction$org_openprovenance_prov_model_WasAttributedTo(bu);
                        }
                        else if (((bu != null && (bu.constructor != null && bu.constructor["__interfaces"] != null && bu.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasAssociatedWith") >= 0)) || bu === null) && u === undefined) {
                            return this.doAction$org_openprovenance_prov_model_WasAssociatedWith(bu);
                        }
                        else if (((bu != null && (bu.constructor != null && bu.constructor["__interfaces"] != null && bu.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasDerivedFrom") >= 0)) || bu === null) && u === undefined) {
                            return this.doAction$org_openprovenance_prov_model_WasDerivedFrom(bu);
                        }
                        else if (((bu != null && (bu.constructor != null && bu.constructor["__interfaces"] != null && bu.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInformedBy") >= 0)) || bu === null) && u === undefined) {
                            return this.doAction$org_openprovenance_prov_model_WasInformedBy(bu);
                        }
                        else if (((bu != null && (bu.constructor != null && bu.constructor["__interfaces"] != null && bu.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasEndedBy") >= 0)) || bu === null) && u === undefined) {
                            return this.doAction$org_openprovenance_prov_model_WasEndedBy(bu);
                        }
                        else if (((bu != null && (bu.constructor != null && bu.constructor["__interfaces"] != null && bu.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasStartedBy") >= 0)) || bu === null) && u === undefined) {
                            return this.doAction$org_openprovenance_prov_model_WasStartedBy(bu);
                        }
                        else if (((bu != null && (bu.constructor != null && bu.constructor["__interfaces"] != null && bu.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInvalidatedBy") >= 0)) || bu === null) && u === undefined) {
                            return this.doAction$org_openprovenance_prov_model_WasInvalidatedBy(bu);
                        }
                        else if (((bu != null && (bu.constructor != null && bu.constructor["__interfaces"] != null && bu.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Used") >= 0)) || bu === null) && u === undefined) {
                            return this.doAction$org_openprovenance_prov_model_Used(bu);
                        }
                        else if (((bu != null && (bu.constructor != null && bu.constructor["__interfaces"] != null && bu.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasGeneratedBy") >= 0)) || bu === null) && u === undefined) {
                            return this.doAction$org_openprovenance_prov_model_WasGeneratedBy(bu);
                        }
                        else if (((bu != null && (bu.constructor != null && bu.constructor["__interfaces"] != null && bu.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Agent") >= 0)) || bu === null) && u === undefined) {
                            return this.doAction$org_openprovenance_prov_model_Agent(bu);
                        }
                        else if (((bu != null && (bu.constructor != null && bu.constructor["__interfaces"] != null && bu.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Activity") >= 0)) || bu === null) && u === undefined) {
                            return this.doAction$org_openprovenance_prov_model_Activity(bu);
                        }
                        else if (((bu != null && (bu.constructor != null && bu.constructor["__interfaces"] != null && bu.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Entity") >= 0)) || bu === null) && u === undefined) {
                            return this.doAction$org_openprovenance_prov_model_Entity(bu);
                        }
                        else if (((bu != null && (bu.constructor != null && bu.constructor["__interfaces"] != null && bu.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.DictionaryMembership") >= 0)) || bu === null) && u === undefined) {
                            return this.doAction$org_openprovenance_prov_model_DictionaryMembership(bu);
                        }
                        else if (((bu != null && (bu.constructor != null && bu.constructor["__interfaces"] != null && bu.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.DerivedByRemovalFrom") >= 0)) || bu === null) && u === undefined) {
                            return this.doAction$org_openprovenance_prov_model_DerivedByRemovalFrom(bu);
                        }
                        else if (((bu != null && (bu.constructor != null && bu.constructor["__interfaces"] != null && bu.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.DerivedByInsertionFrom") >= 0)) || bu === null) && u === undefined) {
                            return this.doAction$org_openprovenance_prov_model_DerivedByInsertionFrom(bu);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                }
                model.NamespaceGatherer = NamespaceGatherer;
                NamespaceGatherer["__class"] = "org.openprovenance.prov.model.NamespaceGatherer";
                NamespaceGatherer["__interfaces"] = ["org.openprovenance.prov.model.StatementAction"];
            })(model = prov.model || (prov.model = {}));
        })(prov = openprovenance.prov || (openprovenance.prov = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
