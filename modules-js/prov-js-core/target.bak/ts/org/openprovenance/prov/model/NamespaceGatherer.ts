/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.model {
    /**
     * Utility class to traverse a document, register all namespaces occurring in {@link QualifiedName}s
     * and attributes as well as associated prefixes, and create a {@link Namespace} datastructure.
     * 
     * @author lavm
     * @param {*} prefixes
     * @param {string} defaultNamespace
     * @class
     */
    export class NamespaceGatherer implements org.openprovenance.prov.model.StatementAction {
        static pu: org.openprovenance.prov.model.ProvUtilities; public static pu_$LI$(): org.openprovenance.prov.model.ProvUtilities { if (NamespaceGatherer.pu == null) { NamespaceGatherer.pu = new org.openprovenance.prov.model.ProvUtilities(); }  return NamespaceGatherer.pu; }

        /*private*/ ns: org.openprovenance.prov.model.Namespace;

        public constructor(prefixes?: any, defaultNamespace?: any) {
            if (((prefixes != null && (prefixes instanceof Object)) || prefixes === null) && ((typeof defaultNamespace === 'string') || defaultNamespace === null)) {
                let __args = arguments;
                this.ns = new org.openprovenance.prov.model.Namespace();
                this.ns.getPrefixes().putAll(prefixes);
                this.ns.setDefaultNamespace(defaultNamespace);
            } else if (prefixes === undefined && defaultNamespace === undefined) {
                let __args = arguments;
                this.ns = new org.openprovenance.prov.model.Namespace();
                this.ns.addKnownNamespaces();
                this.ns.setDefaultNamespace(null);
            } else throw new Error('invalid overload');
        }

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
        public static accumulateAllNamespaces(document: org.openprovenance.prov.model.Document): org.openprovenance.prov.model.Namespace {
            const res: org.openprovenance.prov.model.Namespace = new org.openprovenance.prov.model.Namespace(document.getNamespace());
            {
                let array167 = NamespaceGatherer.pu_$LI$().getNamedBundle(document);
                for(let index166=0; index166 < array167.length; index166++) {
                    let b = array167[index166];
                    {
                        const ns: org.openprovenance.prov.model.Namespace = b.getNamespace();
                        if (ns != null)res.extendWith(ns);
                    }
                }
            }
            return res;
        }

        public getNamespace(): org.openprovenance.prov.model.Namespace {
            return this.ns;
        }

        public registerLocation(locations: Array<org.openprovenance.prov.model.Location>) {
            for(let index168=0; index168 < locations.length; index168++) {
                let loc = locations[index168];
                {
                    this.register$org_openprovenance_prov_model_Location(loc);
                }
            }
        }

        public registerPotentialQualifiedName(o: any) {
            if (o != null && (o.constructor != null && o.constructor["__interfaces"] != null && o.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)){
                this.register$org_openprovenance_prov_model_QualifiedName(<org.openprovenance.prov.model.QualifiedName><any>o);
            }
        }

        public register$org_openprovenance_prov_model_Location(loc: org.openprovenance.prov.model.Location) {
            if (loc != null){
                this.register$org_openprovenance_prov_model_QualifiedName(loc.getType());
                const val: any = loc.getValue();
                this.registerPotentialQualifiedName(val);
            }
        }

        public register(loc?: any) {
            if (((loc != null && (loc.constructor != null && loc.constructor["__interfaces"] != null && loc.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Location") >= 0)) || loc === null)) {
                return <any>this.register$org_openprovenance_prov_model_Location(loc);
            } else if (((loc != null && (loc.constructor != null && loc.constructor["__interfaces"] != null && loc.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Type") >= 0)) || loc === null)) {
                return <any>this.register$org_openprovenance_prov_model_Type(loc);
            } else if (((loc != null && (loc.constructor != null && loc.constructor["__interfaces"] != null && loc.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Role") >= 0)) || loc === null)) {
                return <any>this.register$org_openprovenance_prov_model_Role(loc);
            } else if (((loc != null && (loc.constructor != null && loc.constructor["__interfaces"] != null && loc.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Other") >= 0)) || loc === null)) {
                return <any>this.register$org_openprovenance_prov_model_Other(loc);
            } else if (((loc != null && (loc.constructor != null && loc.constructor["__interfaces"] != null && loc.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || loc === null)) {
                return <any>this.register$org_openprovenance_prov_model_QualifiedName(loc);
            } else if (((loc != null && (loc.constructor != null && loc.constructor["__interfaces"] != null && loc.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Key") >= 0)) || loc === null)) {
                return <any>this.register$org_openprovenance_prov_model_Key(loc);
            } else throw new Error('invalid overload');
        }

        public registerType(types: Array<org.openprovenance.prov.model.Type>) {
            for(let index169=0; index169 < types.length; index169++) {
                let typ = types[index169];
                {
                    this.register$org_openprovenance_prov_model_Type(typ);
                }
            }
        }

        public register$org_openprovenance_prov_model_Type(typ: org.openprovenance.prov.model.Type) {
            this.register$org_openprovenance_prov_model_QualifiedName(typ.getType());
            const val: any = typ.getValue();
            this.registerPotentialQualifiedName(val);
        }

        public registerRole(roles: Array<org.openprovenance.prov.model.Role>) {
            for(let index170=0; index170 < roles.length; index170++) {
                let rol = roles[index170];
                {
                    this.register$org_openprovenance_prov_model_Role(rol);
                }
            }
        }

        public register$org_openprovenance_prov_model_Role(rol: org.openprovenance.prov.model.Role) {
            this.register$org_openprovenance_prov_model_QualifiedName(rol.getType());
            const val: any = rol.getValue();
            this.registerPotentialQualifiedName(val);
        }

        public registerOther(others: Array<org.openprovenance.prov.model.Other>) {
            for(let index171=0; index171 < others.length; index171++) {
                let other = others[index171];
                {
                    this.register$org_openprovenance_prov_model_Other(other);
                }
            }
        }

        public register$org_openprovenance_prov_model_Other(other: org.openprovenance.prov.model.Other) {
            this.register$org_openprovenance_prov_model_QualifiedName(other.getType());
            this.register$org_openprovenance_prov_model_QualifiedName(other.getElementName());
            const val: any = other.getValue();
            this.registerPotentialQualifiedName(val);
        }

        public registerValue(val2: org.openprovenance.prov.model.Value) {
            if (val2 != null){
                this.register$org_openprovenance_prov_model_QualifiedName(val2.getType());
                const val: any = val2.getValue();
                this.registerPotentialQualifiedName(val);
            }
        }

        register$org_openprovenance_prov_model_QualifiedName(name: org.openprovenance.prov.model.QualifiedName) {
            if (name == null)return;
            const namespace: string = name.getNamespaceURI();
            const prefix: string = name.getPrefix();
            if ((prefix == null) || (prefix === javax.xml.XMLConstants.DEFAULT_NS_PREFIX)){
                this.ns.registerDefault(namespace);
            } else {
                this.ns.register(prefix, namespace);
            }
        }

        public doAction$org_openprovenance_prov_model_HadMember(mem: org.openprovenance.prov.model.HadMember) {
            this.register$org_openprovenance_prov_model_QualifiedName(mem.getCollection());
            {
                let array173 = mem.getEntity();
                for(let index172=0; index172 < array173.length; index172++) {
                    let i = array173[index172];
                    {
                        this.register$org_openprovenance_prov_model_QualifiedName(i);
                    }
                }
            }
        }

        public doAction$org_openprovenance_prov_model_extension_QualifiedHadMember(mem: org.openprovenance.prov.model.extension.QualifiedHadMember) {
            this.register$org_openprovenance_prov_model_QualifiedName(mem.getId());
            this.register$org_openprovenance_prov_model_QualifiedName(mem.getCollection());
            {
                let array175 = mem.getEntity();
                for(let index174=0; index174 < array175.length; index174++) {
                    let i = array175[index174];
                    {
                        this.register$org_openprovenance_prov_model_QualifiedName(i);
                    }
                }
            }
            this.registerType(mem.getType());
            this.registerOther(mem.getOther());
        }

        public doAction$org_openprovenance_prov_model_SpecializationOf(spec: org.openprovenance.prov.model.SpecializationOf) {
            this.register$org_openprovenance_prov_model_QualifiedName(spec.getGeneralEntity());
            this.register$org_openprovenance_prov_model_QualifiedName(spec.getSpecificEntity());
        }

        public doAction$org_openprovenance_prov_model_extension_QualifiedSpecializationOf(spec: org.openprovenance.prov.model.extension.QualifiedSpecializationOf) {
            this.register$org_openprovenance_prov_model_QualifiedName(spec.getId());
            this.register$org_openprovenance_prov_model_QualifiedName(spec.getGeneralEntity());
            this.register$org_openprovenance_prov_model_QualifiedName(spec.getSpecificEntity());
            this.registerType(spec.getType());
            this.registerOther(spec.getOther());
        }

        public doAction$org_openprovenance_prov_model_extension_QualifiedAlternateOf(alt: org.openprovenance.prov.model.extension.QualifiedAlternateOf) {
            this.register$org_openprovenance_prov_model_QualifiedName(alt.getId());
            this.register$org_openprovenance_prov_model_QualifiedName(alt.getAlternate1());
            this.register$org_openprovenance_prov_model_QualifiedName(alt.getAlternate2());
            this.registerType(alt.getType());
            this.registerOther(alt.getOther());
        }

        public doAction$org_openprovenance_prov_model_MentionOf(men: org.openprovenance.prov.model.MentionOf) {
            this.register$org_openprovenance_prov_model_QualifiedName(men.getBundle());
            this.register$org_openprovenance_prov_model_QualifiedName(men.getGeneralEntity());
            this.register$org_openprovenance_prov_model_QualifiedName(men.getSpecificEntity());
        }

        public doAction$org_openprovenance_prov_model_AlternateOf(alt: org.openprovenance.prov.model.AlternateOf) {
            this.register$org_openprovenance_prov_model_QualifiedName(alt.getAlternate1());
            this.register$org_openprovenance_prov_model_QualifiedName(alt.getAlternate2());
        }

        public doAction$org_openprovenance_prov_model_WasInfluencedBy(inf: org.openprovenance.prov.model.WasInfluencedBy) {
            this.register$org_openprovenance_prov_model_QualifiedName(inf.getId());
            this.register$org_openprovenance_prov_model_QualifiedName(inf.getInfluencee());
            this.register$org_openprovenance_prov_model_QualifiedName(inf.getInfluencer());
            this.registerType(inf.getType());
            this.registerOther(inf.getOther());
        }

        public doAction$org_openprovenance_prov_model_ActedOnBehalfOf(del: org.openprovenance.prov.model.ActedOnBehalfOf) {
            this.register$org_openprovenance_prov_model_QualifiedName(del.getId());
            this.register$org_openprovenance_prov_model_QualifiedName(del.getDelegate());
            this.register$org_openprovenance_prov_model_QualifiedName(del.getResponsible());
            this.register$org_openprovenance_prov_model_QualifiedName(del.getActivity());
            this.registerType(del.getType());
            this.registerOther(del.getOther());
        }

        public doAction$org_openprovenance_prov_model_WasAttributedTo(attr: org.openprovenance.prov.model.WasAttributedTo) {
            this.register$org_openprovenance_prov_model_QualifiedName(attr.getId());
            this.register$org_openprovenance_prov_model_QualifiedName(attr.getEntity());
            this.register$org_openprovenance_prov_model_QualifiedName(attr.getAgent());
            this.registerType(attr.getType());
            this.registerOther(attr.getOther());
        }

        public doAction$org_openprovenance_prov_model_WasAssociatedWith(assoc: org.openprovenance.prov.model.WasAssociatedWith) {
            this.register$org_openprovenance_prov_model_QualifiedName(assoc.getId());
            this.register$org_openprovenance_prov_model_QualifiedName(assoc.getActivity());
            this.register$org_openprovenance_prov_model_QualifiedName(assoc.getAgent());
            this.register$org_openprovenance_prov_model_QualifiedName(assoc.getPlan());
            this.registerRole(assoc.getRole());
            this.registerType(assoc.getType());
            this.registerOther(assoc.getOther());
        }

        public doAction$org_openprovenance_prov_model_WasDerivedFrom(der: org.openprovenance.prov.model.WasDerivedFrom) {
            this.register$org_openprovenance_prov_model_QualifiedName(der.getId());
            this.register$org_openprovenance_prov_model_QualifiedName(der.getGeneratedEntity());
            this.register$org_openprovenance_prov_model_QualifiedName(der.getUsedEntity());
            this.register$org_openprovenance_prov_model_QualifiedName(der.getActivity());
            this.register$org_openprovenance_prov_model_QualifiedName(der.getGeneration());
            this.register$org_openprovenance_prov_model_QualifiedName(der.getUsage());
            this.registerType(der.getType());
            this.registerOther(der.getOther());
        }

        public doAction$org_openprovenance_prov_model_WasInformedBy(inf: org.openprovenance.prov.model.WasInformedBy) {
            this.register$org_openprovenance_prov_model_QualifiedName(inf.getId());
            this.register$org_openprovenance_prov_model_QualifiedName(inf.getInformed());
            this.register$org_openprovenance_prov_model_QualifiedName(inf.getInformant());
            this.registerType(inf.getType());
            this.registerOther(inf.getOther());
        }

        public doAction$org_openprovenance_prov_model_WasEndedBy(end: org.openprovenance.prov.model.WasEndedBy) {
            this.register$org_openprovenance_prov_model_QualifiedName(end.getId());
            this.register$org_openprovenance_prov_model_QualifiedName(end.getActivity());
            this.register$org_openprovenance_prov_model_QualifiedName(end.getEnder());
            this.register$org_openprovenance_prov_model_QualifiedName(end.getTrigger());
            this.registerLocation(end.getLocation());
            this.registerType(end.getType());
            this.registerRole(end.getRole());
            this.registerOther(end.getOther());
        }

        public doAction$org_openprovenance_prov_model_WasStartedBy(start: org.openprovenance.prov.model.WasStartedBy) {
            this.register$org_openprovenance_prov_model_QualifiedName(start.getId());
            this.register$org_openprovenance_prov_model_QualifiedName(start.getActivity());
            this.register$org_openprovenance_prov_model_QualifiedName(start.getStarter());
            this.register$org_openprovenance_prov_model_QualifiedName(start.getTrigger());
            this.registerLocation(start.getLocation());
            this.registerType(start.getType());
            this.registerRole(start.getRole());
            this.registerOther(start.getOther());
        }

        public doAction$org_openprovenance_prov_model_WasInvalidatedBy(inv: org.openprovenance.prov.model.WasInvalidatedBy) {
            this.register$org_openprovenance_prov_model_QualifiedName(inv.getId());
            this.register$org_openprovenance_prov_model_QualifiedName(inv.getEntity());
            this.register$org_openprovenance_prov_model_QualifiedName(inv.getActivity());
            this.registerLocation(inv.getLocation());
            this.registerRole(inv.getRole());
            this.registerType(inv.getType());
            this.registerOther(inv.getOther());
        }

        public doAction$org_openprovenance_prov_model_Used(use: org.openprovenance.prov.model.Used) {
            this.register$org_openprovenance_prov_model_QualifiedName(use.getId());
            this.register$org_openprovenance_prov_model_QualifiedName(use.getEntity());
            this.register$org_openprovenance_prov_model_QualifiedName(use.getActivity());
            this.registerLocation(use.getLocation());
            this.registerRole(use.getRole());
            this.registerType(use.getType());
            this.registerOther(use.getOther());
        }

        public doAction$org_openprovenance_prov_model_WasGeneratedBy(gen: org.openprovenance.prov.model.WasGeneratedBy) {
            this.register$org_openprovenance_prov_model_QualifiedName(gen.getId());
            this.register$org_openprovenance_prov_model_QualifiedName(gen.getEntity());
            this.register$org_openprovenance_prov_model_QualifiedName(gen.getActivity());
            this.registerLocation(gen.getLocation());
            this.registerRole(gen.getRole());
            this.registerType(gen.getType());
            this.registerOther(gen.getOther());
        }

        public doAction$org_openprovenance_prov_model_Agent(ag: org.openprovenance.prov.model.Agent) {
            this.register$org_openprovenance_prov_model_QualifiedName(ag.getId());
            this.registerLocation(ag.getLocation());
            this.registerType(ag.getType());
            this.registerOther(ag.getOther());
        }

        public doAction$org_openprovenance_prov_model_Activity(a: org.openprovenance.prov.model.Activity) {
            this.register$org_openprovenance_prov_model_QualifiedName(a.getId());
            this.registerLocation(a.getLocation());
            this.registerType(a.getType());
            this.registerOther(a.getOther());
        }

        public doAction$org_openprovenance_prov_model_Entity(e: org.openprovenance.prov.model.Entity) {
            this.register$org_openprovenance_prov_model_QualifiedName(e.getId());
            this.registerLocation(e.getLocation());
            this.registerType(e.getType());
            this.registerValue(e.getValue());
            this.registerOther(e.getOther());
        }

        public doAction$org_openprovenance_prov_model_DictionaryMembership(m: org.openprovenance.prov.model.DictionaryMembership) {
            this.register$org_openprovenance_prov_model_QualifiedName(m.getDictionary());
            this.registerEntry(m.getKeyEntityPair());
        }

        public doAction$org_openprovenance_prov_model_DerivedByRemovalFrom(r: org.openprovenance.prov.model.DerivedByRemovalFrom) {
            this.register$org_openprovenance_prov_model_QualifiedName(r.getId());
            this.register$org_openprovenance_prov_model_QualifiedName(r.getNewDictionary());
            this.register$org_openprovenance_prov_model_QualifiedName(r.getOldDictionary());
            this.registerType(r.getType());
            this.registerOther(r.getOther());
            {
                let array177 = r.getKey();
                for(let index176=0; index176 < array177.length; index176++) {
                    let k = array177[index176];
                    {
                        this.register$org_openprovenance_prov_model_Key(k);
                    }
                }
            }
        }

        public doAction$org_openprovenance_prov_model_DerivedByInsertionFrom(i: org.openprovenance.prov.model.DerivedByInsertionFrom) {
            this.register$org_openprovenance_prov_model_QualifiedName(i.getId());
            this.register$org_openprovenance_prov_model_QualifiedName(i.getNewDictionary());
            this.register$org_openprovenance_prov_model_QualifiedName(i.getOldDictionary());
            this.registerType(i.getType());
            this.registerOther(i.getOther());
            this.registerEntry(i.getKeyEntityPair());
        }

        registerEntry(keyEntityPairs: Array<org.openprovenance.prov.model.Entry>) {
            for(let index178=0; index178 < keyEntityPairs.length; index178++) {
                let e = keyEntityPairs[index178];
                {
                    this.register$org_openprovenance_prov_model_QualifiedName(e.getEntity());
                    const key: org.openprovenance.prov.model.Key = e.getKey();
                    this.register$org_openprovenance_prov_model_Key(key);
                }
            }
        }

        register$org_openprovenance_prov_model_Key(key: org.openprovenance.prov.model.Key) {
            this.registerPotentialQualifiedName(key.getValue());
            this.register$org_openprovenance_prov_model_QualifiedName(key.getType());
        }

        public doAction$org_openprovenance_prov_model_Bundle$org_openprovenance_prov_model_ProvUtilities(bu: org.openprovenance.prov.model.Bundle, u: org.openprovenance.prov.model.ProvUtilities) {
            this.register$org_openprovenance_prov_model_QualifiedName(bu.getId());
            {
                let array180 = bu.getStatement();
                for(let index179=0; index179 < array180.length; index179++) {
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
        public doAction(bu?: any, u?: any) {
            if (((bu != null && (bu.constructor != null && bu.constructor["__interfaces"] != null && bu.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Bundle") >= 0)) || bu === null) && ((u != null && u instanceof <any>org.openprovenance.prov.model.ProvUtilities) || u === null)) {
                return <any>this.doAction$org_openprovenance_prov_model_Bundle$org_openprovenance_prov_model_ProvUtilities(bu, u);
            } else if (((bu != null && (bu.constructor != null && bu.constructor["__interfaces"] != null && bu.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.extension.QualifiedHadMember") >= 0)) || bu === null) && u === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_extension_QualifiedHadMember(bu);
            } else if (((bu != null && (bu.constructor != null && bu.constructor["__interfaces"] != null && bu.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HadMember") >= 0)) || bu === null) && u === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_HadMember(bu);
            } else if (((bu != null && (bu.constructor != null && bu.constructor["__interfaces"] != null && bu.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.extension.QualifiedSpecializationOf") >= 0)) || bu === null) && u === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_extension_QualifiedSpecializationOf(bu);
            } else if (((bu != null && (bu.constructor != null && bu.constructor["__interfaces"] != null && bu.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.SpecializationOf") >= 0)) || bu === null) && u === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_SpecializationOf(bu);
            } else if (((bu != null && (bu.constructor != null && bu.constructor["__interfaces"] != null && bu.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.extension.QualifiedAlternateOf") >= 0)) || bu === null) && u === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_extension_QualifiedAlternateOf(bu);
            } else if (((bu != null && (bu.constructor != null && bu.constructor["__interfaces"] != null && bu.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.MentionOf") >= 0)) || bu === null) && u === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_MentionOf(bu);
            } else if (((bu != null && (bu.constructor != null && bu.constructor["__interfaces"] != null && bu.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.AlternateOf") >= 0)) || bu === null) && u === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_AlternateOf(bu);
            } else if (((bu != null && (bu.constructor != null && bu.constructor["__interfaces"] != null && bu.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInfluencedBy") >= 0)) || bu === null) && u === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_WasInfluencedBy(bu);
            } else if (((bu != null && (bu.constructor != null && bu.constructor["__interfaces"] != null && bu.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.ActedOnBehalfOf") >= 0)) || bu === null) && u === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_ActedOnBehalfOf(bu);
            } else if (((bu != null && (bu.constructor != null && bu.constructor["__interfaces"] != null && bu.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasAttributedTo") >= 0)) || bu === null) && u === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_WasAttributedTo(bu);
            } else if (((bu != null && (bu.constructor != null && bu.constructor["__interfaces"] != null && bu.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasAssociatedWith") >= 0)) || bu === null) && u === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_WasAssociatedWith(bu);
            } else if (((bu != null && (bu.constructor != null && bu.constructor["__interfaces"] != null && bu.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasDerivedFrom") >= 0)) || bu === null) && u === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_WasDerivedFrom(bu);
            } else if (((bu != null && (bu.constructor != null && bu.constructor["__interfaces"] != null && bu.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInformedBy") >= 0)) || bu === null) && u === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_WasInformedBy(bu);
            } else if (((bu != null && (bu.constructor != null && bu.constructor["__interfaces"] != null && bu.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasEndedBy") >= 0)) || bu === null) && u === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_WasEndedBy(bu);
            } else if (((bu != null && (bu.constructor != null && bu.constructor["__interfaces"] != null && bu.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasStartedBy") >= 0)) || bu === null) && u === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_WasStartedBy(bu);
            } else if (((bu != null && (bu.constructor != null && bu.constructor["__interfaces"] != null && bu.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInvalidatedBy") >= 0)) || bu === null) && u === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_WasInvalidatedBy(bu);
            } else if (((bu != null && (bu.constructor != null && bu.constructor["__interfaces"] != null && bu.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Used") >= 0)) || bu === null) && u === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_Used(bu);
            } else if (((bu != null && (bu.constructor != null && bu.constructor["__interfaces"] != null && bu.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasGeneratedBy") >= 0)) || bu === null) && u === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_WasGeneratedBy(bu);
            } else if (((bu != null && (bu.constructor != null && bu.constructor["__interfaces"] != null && bu.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Agent") >= 0)) || bu === null) && u === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_Agent(bu);
            } else if (((bu != null && (bu.constructor != null && bu.constructor["__interfaces"] != null && bu.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Activity") >= 0)) || bu === null) && u === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_Activity(bu);
            } else if (((bu != null && (bu.constructor != null && bu.constructor["__interfaces"] != null && bu.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Entity") >= 0)) || bu === null) && u === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_Entity(bu);
            } else if (((bu != null && (bu.constructor != null && bu.constructor["__interfaces"] != null && bu.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.DictionaryMembership") >= 0)) || bu === null) && u === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_DictionaryMembership(bu);
            } else if (((bu != null && (bu.constructor != null && bu.constructor["__interfaces"] != null && bu.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.DerivedByRemovalFrom") >= 0)) || bu === null) && u === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_DerivedByRemovalFrom(bu);
            } else if (((bu != null && (bu.constructor != null && bu.constructor["__interfaces"] != null && bu.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.DerivedByInsertionFrom") >= 0)) || bu === null) && u === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_DerivedByInsertionFrom(bu);
            } else throw new Error('invalid overload');
        }
    }
    NamespaceGatherer["__class"] = "org.openprovenance.prov.model.NamespaceGatherer";
    NamespaceGatherer["__interfaces"] = ["org.openprovenance.prov.model.StatementAction"];


}

