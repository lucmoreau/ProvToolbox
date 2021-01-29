/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.model {
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
    export class IndexedDocument implements org.openprovenance.prov.model.StatementAction {
        u: org.openprovenance.prov.model.ProvUtilities;

        pFactory: org.openprovenance.prov.model.ProvFactory;

        /*private*/ entityMap: any;

        /*private*/ activityMap: any;

        /*private*/ agentMap: any;

        /*private*/ activityUsedMap: any;

        /*private*/ entityUsedMap: any;

        /*private*/ anonUsed: Array<org.openprovenance.prov.model.Used>;

        /*private*/ namedUsedMap: any;

        /*private*/ activityWasGeneratedByMap: any;

        /*private*/ entityWasGeneratedByMap: any;

        /*private*/ anonWasGeneratedBy: Array<org.openprovenance.prov.model.WasGeneratedBy>;

        /*private*/ namedWasGeneratedByMap: any;

        /*private*/ entityCauseWasDerivedFromMap: any;

        /*private*/ entityEffectWasDerivedFromMap: any;

        /*private*/ anonWasDerivedFrom: Array<org.openprovenance.prov.model.WasDerivedFrom>;

        /*private*/ namedWasDerivedFromMap: any;

        /*private*/ activityWasAssociatedWithMap: any;

        /*private*/ agentWasAssociatedWithMap: any;

        /*private*/ anonWasAssociatedWith: Array<org.openprovenance.prov.model.WasAssociatedWith>;

        /*private*/ namedWasAssociatedWithMap: any;

        /*private*/ entityWasAttributedToMap: any;

        /*private*/ agentWasAttributedToMap: any;

        /*private*/ anonWasAttributedTo: Array<org.openprovenance.prov.model.WasAttributedTo>;

        /*private*/ namedWasAttributedToMap: any;

        /*private*/ activityCauseWasInformedByMap: any;

        /*private*/ activityEffectWasInformedByMap: any;

        /*private*/ anonWasInformedBy: Array<org.openprovenance.prov.model.WasInformedBy>;

        /*private*/ namedWasInformedByMap: any;

        /*private*/ nss: org.openprovenance.prov.model.Namespace;

        /*private*/ flatten: boolean;

        /*private*/ anonActedOnBehalfOf: Array<org.openprovenance.prov.model.ActedOnBehalfOf>;

        /*private*/ namedActedOnBehalfOfMap: any;

        /*private*/ responsibleActedOnBehalfOfMap: any;

        /*private*/ delegateActedOnBehalfOfMap: any;

        /*private*/ namedWasInvalidatedByMap: any;

        /*private*/ entityWasInvalidatedByMap: any;

        /*private*/ anonWasInvalidatedBy: Array<org.openprovenance.prov.model.WasInvalidatedBy>;

        /*private*/ activityWasInvalidatedByMap: any;

        /*private*/ namedSpecializationOfMap: any;

        /*private*/ specificEntitySpecializationOfMap: any;

        /*private*/ anonSpecializationOf: Array<org.openprovenance.prov.model.SpecializationOf>;

        /*private*/ genericEntitySpecializationOfMap: any;

        /*private*/ anonAlternateOf: Array<org.openprovenance.prov.model.AlternateOf>;

        /*private*/ namedAlternateOfMap: any;

        /*private*/ entityCauseAlternateOfMap: any;

        /*private*/ entityEffectAlternateOfMap: any;

        /*private*/ influenceeWasInfluencedByMap: any;

        /*private*/ influencerWasInfluencedByMap: any;

        /*private*/ anonWasInfluencedBy: Array<org.openprovenance.prov.model.WasInfluencedBy>;

        /*private*/ namedWasInfluencedByMap: any;

        /*private*/ activityWasStartedByMap: any;

        /*private*/ entityWasStartedByMap: any;

        /*private*/ anonWasStartedBy: Array<org.openprovenance.prov.model.WasStartedBy>;

        /*private*/ namedWasStartedByMap: any;

        /*private*/ anonWasEndedBy: Array<org.openprovenance.prov.model.WasEndedBy>;

        /*private*/ activityWasEndedByMap: any;

        /*private*/ namedWasEndedByMap: any;

        /*private*/ entityWasEndedByMap: any;

        /*private*/ anonHadMember: Array<org.openprovenance.prov.model.HadMember>;

        /*private*/ collHadMemberMap: any;

        /*private*/ namedHadMemberMap: any;

        /*private*/ entityHadMemberMap: any;

        public getUsed$(): Array<org.openprovenance.prov.model.Used> {
            return this.anonUsed;
        }

        public getUsed$org_openprovenance_prov_model_Activity(p: org.openprovenance.prov.model.Activity): Array<org.openprovenance.prov.model.Used> {
            return /* get */((m,k) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { return m.entries[i].value; } return null; })(<any>this.activityUsedMap, p.getId());
        }

        /**
         * Return all used edges with activity p as an effect.
         * @param {*} p
         * @return {*[]}
         */
        public getUsed(p?: any): any {
            if (((p != null && (p.constructor != null && p.constructor["__interfaces"] != null && p.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Activity") >= 0)) || p === null)) {
                return <any>this.getUsed$org_openprovenance_prov_model_Activity(p);
            } else if (((p != null && (p.constructor != null && p.constructor["__interfaces"] != null && p.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Entity") >= 0)) || p === null)) {
                return <any>this.getUsed$org_openprovenance_prov_model_Entity(p);
            } else if (p === undefined) {
                return <any>this.getUsed$();
            } else throw new Error('invalid overload');
        }

        public getUsed$org_openprovenance_prov_model_Entity(p: org.openprovenance.prov.model.Entity): Array<org.openprovenance.prov.model.Used> {
            return /* get */((m,k) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { return m.entries[i].value; } return null; })(<any>this.entityUsedMap, p.getId());
        }

        public getWasGeneratedBy$(): Array<org.openprovenance.prov.model.WasGeneratedBy> {
            return this.anonWasGeneratedBy;
        }

        public getWasGeneratedBy$org_openprovenance_prov_model_Activity(p: org.openprovenance.prov.model.Activity): Array<org.openprovenance.prov.model.WasGeneratedBy> {
            return /* get */((m,k) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { return m.entries[i].value; } return null; })(<any>this.activityWasGeneratedByMap, p.getId());
        }

        /**
         * Return all WasGeneratedBy edges with activity p as an effect.
         * @param {*} p
         * @return {*[]}
         */
        public getWasGeneratedBy(p?: any): any {
            if (((p != null && (p.constructor != null && p.constructor["__interfaces"] != null && p.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Activity") >= 0)) || p === null)) {
                return <any>this.getWasGeneratedBy$org_openprovenance_prov_model_Activity(p);
            } else if (((p != null && (p.constructor != null && p.constructor["__interfaces"] != null && p.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Entity") >= 0)) || p === null)) {
                return <any>this.getWasGeneratedBy$org_openprovenance_prov_model_Entity(p);
            } else if (p === undefined) {
                return <any>this.getWasGeneratedBy$();
            } else throw new Error('invalid overload');
        }

        public getWasGeneratedBy$org_openprovenance_prov_model_Entity(p: org.openprovenance.prov.model.Entity): Array<org.openprovenance.prov.model.WasGeneratedBy> {
            return /* get */((m,k) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { return m.entries[i].value; } return null; })(<any>this.entityWasGeneratedByMap, p.getId());
        }

        /**
         * Return all WasDerivedFrom edges for this graph.
         * @return {*[]}
         */
        public getWasDerivedFrom(): Array<org.openprovenance.prov.model.WasDerivedFrom> {
            return this.anonWasDerivedFrom;
        }

        /**
         * Return all WasDerivedFrom edges with entity a as a cause.
         * @param {*} a
         * @return {*[]}
         */
        public getWasDerivedFromWithCause(a: org.openprovenance.prov.model.Entity): Array<org.openprovenance.prov.model.WasDerivedFrom> {
            return /* get */((m,k) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { return m.entries[i].value; } return null; })(<any>this.entityCauseWasDerivedFromMap, a.getId());
        }

        /**
         * Return all WasDerivedFrom edges with entity a as an effect .
         * @param {*} a
         * @return {*[]}
         */
        public getWasDerivedFromWithEffect(a: org.openprovenance.prov.model.Entity): Array<org.openprovenance.prov.model.WasDerivedFrom> {
            return /* get */((m,k) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { return m.entries[i].value; } return null; })(<any>this.entityEffectWasDerivedFromMap, a.getId());
        }

        /**
         * Return all WasInformedBy edges for this graph.
         * @return {*[]}
         */
        public getWasInformedBy(): Array<org.openprovenance.prov.model.WasInformedBy> {
            return this.anonWasInformedBy;
        }

        /**
         * Return all WasInformedBy edges with activity p as a cause.
         * @param {*} a
         * @return {*[]}
         */
        public getWasInformedByWithCause(a: org.openprovenance.prov.model.Activity): Array<org.openprovenance.prov.model.WasInformedBy> {
            return /* get */((m,k) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { return m.entries[i].value; } return null; })(<any>this.activityCauseWasInformedByMap, a.getId());
        }

        /**
         * Return all WasInformedBy edges with activity a as an effect.
         * @param {*} a
         * @return {*[]}
         */
        public getWasInformedByWithEffect(a: org.openprovenance.prov.model.Activity): Array<org.openprovenance.prov.model.WasInformedBy> {
            return /* get */((m,k) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { return m.entries[i].value; } return null; })(<any>this.activityEffectWasInformedByMap, a.getId());
        }

        public getWasAssociatedWith$(): Array<org.openprovenance.prov.model.WasAssociatedWith> {
            return this.anonWasAssociatedWith;
        }

        public getWasAssociatedWith$org_openprovenance_prov_model_Activity(p: org.openprovenance.prov.model.Activity): Array<org.openprovenance.prov.model.WasAssociatedWith> {
            return /* get */((m,k) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { return m.entries[i].value; } return null; })(<any>this.activityWasAssociatedWithMap, p.getId());
        }

        /**
         * Return all WasAssociatedWith edges with activity p as an effect.
         * @param {*} p
         * @return {*[]}
         */
        public getWasAssociatedWith(p?: any): any {
            if (((p != null && (p.constructor != null && p.constructor["__interfaces"] != null && p.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Activity") >= 0)) || p === null)) {
                return <any>this.getWasAssociatedWith$org_openprovenance_prov_model_Activity(p);
            } else if (((p != null && (p.constructor != null && p.constructor["__interfaces"] != null && p.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Agent") >= 0)) || p === null)) {
                return <any>this.getWasAssociatedWith$org_openprovenance_prov_model_Agent(p);
            } else if (p === undefined) {
                return <any>this.getWasAssociatedWith$();
            } else throw new Error('invalid overload');
        }

        public getWasAssociatedWith$org_openprovenance_prov_model_Agent(a: org.openprovenance.prov.model.Agent): Array<org.openprovenance.prov.model.WasAssociatedWith> {
            return /* get */((m,k) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { return m.entries[i].value; } return null; })(<any>this.agentWasAssociatedWithMap, a.getId());
        }

        public add$org_openprovenance_prov_model_Entity(entity: org.openprovenance.prov.model.Entity): org.openprovenance.prov.model.Entity {
            return this.add$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_Entity(entity.getId(), entity);
        }

        public add$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_Entity(name: org.openprovenance.prov.model.QualifiedName, entity: org.openprovenance.prov.model.Entity): org.openprovenance.prov.model.Entity {
            const existing: org.openprovenance.prov.model.Entity = /* get */((m,k) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { return m.entries[i].value; } return null; })(<any>this.entityMap, name);
            if (existing != null){
                this.mergeAttributes$org_openprovenance_prov_model_Element$org_openprovenance_prov_model_Element(existing, entity);
                return existing;
            } else {
                /* put */((m,k,v) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { m.entries[i].value=v; return; } m.entries.push({key:k,value:v,getKey: function() { return this.key }, getValue: function() { return this.value }}); })(<any>this.entityMap, name, entity);
                return entity;
            }
        }

        public mergeAttributes$org_openprovenance_prov_model_Element$org_openprovenance_prov_model_Element(existing: org.openprovenance.prov.model.Element, newElement: org.openprovenance.prov.model.Element) {
            const set: Array<org.openprovenance.prov.model.LangString> = <any>(newElement.getLabel().slice(0));
            /* removeAll */((a, r) => { let b=false; for(let i=0;i<r.length;i++) { let ndx=a.indexOf(r[i]); if(ndx>=0) { a.splice(ndx, 1); b=true; } } return b; })(set,existing.getLabel());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(existing.getLabel(), set);
            const set2: Array<org.openprovenance.prov.model.Location> = <any>(newElement.getLocation().slice(0));
            /* removeAll */((a, r) => { let b=false; for(let i=0;i<r.length;i++) { let ndx=a.indexOf(r[i]); if(ndx>=0) { a.splice(ndx, 1); b=true; } } return b; })(set2,existing.getLocation());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(existing.getLocation(), set2);
            const set3: Array<org.openprovenance.prov.model.Type> = <any>(newElement.getType().slice(0));
            /* removeAll */((a, r) => { let b=false; for(let i=0;i<r.length;i++) { let ndx=a.indexOf(r[i]); if(ndx>=0) { a.splice(ndx, 1); b=true; } } return b; })(set3,existing.getType());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(existing.getType(), set3);
            const set4: Array<org.openprovenance.prov.model.Other> = <any>(newElement.getOther().slice(0));
            /* removeAll */((a, r) => { let b=false; for(let i=0;i<r.length;i++) { let ndx=a.indexOf(r[i]); if(ndx>=0) { a.splice(ndx, 1); b=true; } } return b; })(set4,existing.getOther());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(existing.getOther(), set4);
        }

        public mergeAttributes(existing?: any, newElement?: any) {
            if (((existing != null && (existing.constructor != null && existing.constructor["__interfaces"] != null && existing.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Element") >= 0)) || existing === null) && ((newElement != null && (newElement.constructor != null && newElement.constructor["__interfaces"] != null && newElement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Element") >= 0)) || newElement === null)) {
                return <any>this.mergeAttributes$org_openprovenance_prov_model_Element$org_openprovenance_prov_model_Element(existing, newElement);
            } else if (((existing != null && (existing.constructor != null && existing.constructor["__interfaces"] != null && existing.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Influence") >= 0)) || existing === null) && ((newElement != null && (newElement.constructor != null && newElement.constructor["__interfaces"] != null && newElement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Influence") >= 0)) || newElement === null)) {
                return <any>this.mergeAttributes$org_openprovenance_prov_model_Influence$org_openprovenance_prov_model_Influence(existing, newElement);
            } else if (((existing != null) || existing === null) && ((newElement != null) || newElement === null)) {
                return <any>this.mergeAttributes$org_openprovenance_prov_model_Statement$org_openprovenance_prov_model_Statement(existing, newElement);
            } else throw new Error('invalid overload');
        }

        mergeAttributes$org_openprovenance_prov_model_Influence$org_openprovenance_prov_model_Influence(existing: org.openprovenance.prov.model.Influence, newElement: org.openprovenance.prov.model.Influence) {
            const set: Array<org.openprovenance.prov.model.LangString> = <any>(newElement.getLabel().slice(0));
            /* removeAll */((a, r) => { let b=false; for(let i=0;i<r.length;i++) { let ndx=a.indexOf(r[i]); if(ndx>=0) { a.splice(ndx, 1); b=true; } } return b; })(set,existing.getLabel());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(existing.getLabel(), set);
            if (existing != null && (existing.constructor != null && existing.constructor["__interfaces"] != null && existing.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HasLocation") >= 0)){
                const existing2: org.openprovenance.prov.model.HasLocation = <org.openprovenance.prov.model.HasLocation><any>existing;
                const set2: Array<org.openprovenance.prov.model.Location> = <any>((<org.openprovenance.prov.model.HasLocation><any>newElement).getLocation().slice(0));
                /* removeAll */((a, r) => { let b=false; for(let i=0;i<r.length;i++) { let ndx=a.indexOf(r[i]); if(ndx>=0) { a.splice(ndx, 1); b=true; } } return b; })(set2,existing2.getLocation());
                /* addAll */((l1, l2) => l1.push.apply(l1, l2))(existing2.getLocation(), set2);
            }
            const set3: Array<org.openprovenance.prov.model.Type> = <any>(newElement.getType().slice(0));
            /* removeAll */((a, r) => { let b=false; for(let i=0;i<r.length;i++) { let ndx=a.indexOf(r[i]); if(ndx>=0) { a.splice(ndx, 1); b=true; } } return b; })(set3,existing.getType());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(existing.getType(), set3);
            const set4: Array<org.openprovenance.prov.model.Other> = <any>(newElement.getOther().slice(0));
            /* removeAll */((a, r) => { let b=false; for(let i=0;i<r.length;i++) { let ndx=a.indexOf(r[i]); if(ndx>=0) { a.splice(ndx, 1); b=true; } } return b; })(set4,existing.getOther());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(existing.getOther(), set4);
        }

        mergeAttributes$org_openprovenance_prov_model_Statement$org_openprovenance_prov_model_Statement<T extends org.openprovenance.prov.model.Statement>(existing: T, newElement: T) {
            if (existing != null && (existing.constructor != null && existing.constructor["__interfaces"] != null && existing.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Element") >= 0)){
                this.mergeAttributes$org_openprovenance_prov_model_Element$org_openprovenance_prov_model_Element(<any>existing, <any>newElement);
                return;
            }
            if (existing != null && (existing.constructor != null && existing.constructor["__interfaces"] != null && existing.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Influence") >= 0)){
                this.mergeAttributes$org_openprovenance_prov_model_Influence$org_openprovenance_prov_model_Influence(<any>existing, <any>newElement);
                return;
            }
            throw Object.defineProperty(new Error(), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.UnsupportedOperationException','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
        }

        sameEdge(existing: org.openprovenance.prov.model.Statement, newElement: org.openprovenance.prov.model.Statement, count: number): boolean {
            let ok: boolean = true;
            for(let i: number = 1; i <= count; i++) {{
                const qn1: any = this.u.getter(existing, i);
                const qn2: any = this.u.getter(newElement, i);
                if (qn1 == null){
                    if (qn2 == null){
                    } else {
                        ok = false;
                        break;
                    }
                } else {
                    if (qn2 == null){
                        ok = false;
                        break;
                    } else {
                        if (!/* equals */(<any>((o1: any, o2: any) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(qn1,qn2))){
                            ok = false;
                            break;
                        }
                    }
                }
            };}
            return ok;
        }

        public add$org_openprovenance_prov_model_Agent(agent: org.openprovenance.prov.model.Agent): org.openprovenance.prov.model.Agent {
            return this.add$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_Agent(agent.getId(), agent);
        }

        public add$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_Agent(name: org.openprovenance.prov.model.QualifiedName, agent: org.openprovenance.prov.model.Agent): org.openprovenance.prov.model.Agent {
            const existing: org.openprovenance.prov.model.Agent = /* get */((m,k) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { return m.entries[i].value; } return null; })(<any>this.agentMap, name);
            if (existing != null){
                this.mergeAttributes$org_openprovenance_prov_model_Element$org_openprovenance_prov_model_Element(existing, agent);
                return existing;
            } else {
                /* put */((m,k,v) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { m.entries[i].value=v; return; } m.entries.push({key:k,value:v,getKey: function() { return this.key }, getValue: function() { return this.value }}); })(<any>this.agentMap, name, agent);
                return agent;
            }
        }

        public add$org_openprovenance_prov_model_Activity(activity: org.openprovenance.prov.model.Activity): org.openprovenance.prov.model.Activity {
            return this.add$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_Activity(activity.getId(), activity);
        }

        public add$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_Activity(name: org.openprovenance.prov.model.QualifiedName, activity: org.openprovenance.prov.model.Activity): org.openprovenance.prov.model.Activity {
            const existing: org.openprovenance.prov.model.Activity = /* get */((m,k) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { return m.entries[i].value; } return null; })(<any>this.activityMap, name);
            if (existing != null){
                this.mergeAttributes$org_openprovenance_prov_model_Element$org_openprovenance_prov_model_Element(existing, activity);
                return existing;
            } else {
                /* put */((m,k,v) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { m.entries[i].value=v; return; } m.entries.push({key:k,value:v,getKey: function() { return this.key }, getValue: function() { return this.value }}); })(<any>this.activityMap, name, activity);
                return activity;
            }
        }

        public getActivity(name: string): org.openprovenance.prov.model.Activity {
            return /* get */((m,k) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { return m.entries[i].value; } return null; })(<any>this.activityMap, name);
        }

        public getEntity(name: string): org.openprovenance.prov.model.Entity {
            return /* get */((m,k) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { return m.entries[i].value; } return null; })(<any>this.entityMap, name);
        }

        public getAgent(name: string): org.openprovenance.prov.model.Agent {
            return /* get */((m,k) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { return m.entries[i].value; } return null; })(<any>this.agentMap, name);
        }

        public constructor(pFactory?: any, doc?: any, flatten?: any) {
            if (((pFactory != null && pFactory instanceof <any>org.openprovenance.prov.model.ProvFactory) || pFactory === null) && ((doc != null && (doc.constructor != null && doc.constructor["__interfaces"] != null && doc.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Document") >= 0)) || doc === null) && ((typeof flatten === 'boolean') || flatten === null)) {
                let __args = arguments;
                if (this.pFactory === undefined) { this.pFactory = null; } 
                if (this.nss === undefined) { this.nss = null; } 
                if (this.flatten === undefined) { this.flatten = false; } 
                this.u = new org.openprovenance.prov.model.ProvUtilities();
                this.entityMap = <any>({});
                this.activityMap = <any>({});
                this.agentMap = <any>({});
                this.activityUsedMap = <any>({});
                this.entityUsedMap = <any>({});
                this.anonUsed = <any>([]);
                this.namedUsedMap = <any>({});
                this.activityWasGeneratedByMap = <any>({});
                this.entityWasGeneratedByMap = <any>({});
                this.anonWasGeneratedBy = <any>([]);
                this.namedWasGeneratedByMap = <any>({});
                this.entityCauseWasDerivedFromMap = <any>({});
                this.entityEffectWasDerivedFromMap = <any>({});
                this.anonWasDerivedFrom = <any>([]);
                this.namedWasDerivedFromMap = <any>({});
                this.activityWasAssociatedWithMap = <any>({});
                this.agentWasAssociatedWithMap = <any>({});
                this.anonWasAssociatedWith = <any>([]);
                this.namedWasAssociatedWithMap = <any>({});
                this.entityWasAttributedToMap = <any>({});
                this.agentWasAttributedToMap = <any>({});
                this.anonWasAttributedTo = <any>([]);
                this.namedWasAttributedToMap = <any>({});
                this.activityCauseWasInformedByMap = <any>({});
                this.activityEffectWasInformedByMap = <any>({});
                this.anonWasInformedBy = <any>([]);
                this.namedWasInformedByMap = <any>({});
                this.anonActedOnBehalfOf = <any>([]);
                this.namedActedOnBehalfOfMap = <any>({});
                this.responsibleActedOnBehalfOfMap = <any>({});
                this.delegateActedOnBehalfOfMap = <any>({});
                this.namedWasInvalidatedByMap = <any>({});
                this.entityWasInvalidatedByMap = <any>({});
                this.anonWasInvalidatedBy = <any>([]);
                this.activityWasInvalidatedByMap = <any>({});
                this.namedSpecializationOfMap = <any>({});
                this.specificEntitySpecializationOfMap = <any>({});
                this.anonSpecializationOf = <any>([]);
                this.genericEntitySpecializationOfMap = <any>({});
                this.anonAlternateOf = <any>([]);
                this.namedAlternateOfMap = <any>({});
                this.entityCauseAlternateOfMap = <any>({});
                this.entityEffectAlternateOfMap = <any>({});
                this.influenceeWasInfluencedByMap = <any>({});
                this.influencerWasInfluencedByMap = <any>({});
                this.anonWasInfluencedBy = <any>([]);
                this.namedWasInfluencedByMap = <any>({});
                this.activityWasStartedByMap = <any>({});
                this.entityWasStartedByMap = <any>({});
                this.anonWasStartedBy = <any>([]);
                this.namedWasStartedByMap = <any>({});
                this.anonWasEndedBy = <any>([]);
                this.activityWasEndedByMap = <any>({});
                this.namedWasEndedByMap = <any>({});
                this.entityWasEndedByMap = <any>({});
                this.anonHadMember = <any>([]);
                this.collHadMemberMap = <any>({});
                this.namedHadMemberMap = <any>({});
                this.entityHadMemberMap = <any>({});
                this.bundleMap = <any>({});
                this.pFactory = pFactory;
                this.flatten = flatten;
                if (doc != null){
                    this.nss = doc.getNamespace();
                    this.u.forAllStatementOrBundle(doc.getStatementOrBundle(), this);
                }
            } else if (((pFactory != null && pFactory instanceof <any>org.openprovenance.prov.model.ProvFactory) || pFactory === null) && ((doc != null && (doc.constructor != null && doc.constructor["__interfaces"] != null && doc.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Document") >= 0)) || doc === null) && flatten === undefined) {
                let __args = arguments;
                {
                    let __args = arguments;
                    let flatten: any = true;
                    if (this.pFactory === undefined) { this.pFactory = null; } 
                    if (this.nss === undefined) { this.nss = null; } 
                    if (this.flatten === undefined) { this.flatten = false; } 
                    this.u = new org.openprovenance.prov.model.ProvUtilities();
                    this.entityMap = <any>({});
                    this.activityMap = <any>({});
                    this.agentMap = <any>({});
                    this.activityUsedMap = <any>({});
                    this.entityUsedMap = <any>({});
                    this.anonUsed = <any>([]);
                    this.namedUsedMap = <any>({});
                    this.activityWasGeneratedByMap = <any>({});
                    this.entityWasGeneratedByMap = <any>({});
                    this.anonWasGeneratedBy = <any>([]);
                    this.namedWasGeneratedByMap = <any>({});
                    this.entityCauseWasDerivedFromMap = <any>({});
                    this.entityEffectWasDerivedFromMap = <any>({});
                    this.anonWasDerivedFrom = <any>([]);
                    this.namedWasDerivedFromMap = <any>({});
                    this.activityWasAssociatedWithMap = <any>({});
                    this.agentWasAssociatedWithMap = <any>({});
                    this.anonWasAssociatedWith = <any>([]);
                    this.namedWasAssociatedWithMap = <any>({});
                    this.entityWasAttributedToMap = <any>({});
                    this.agentWasAttributedToMap = <any>({});
                    this.anonWasAttributedTo = <any>([]);
                    this.namedWasAttributedToMap = <any>({});
                    this.activityCauseWasInformedByMap = <any>({});
                    this.activityEffectWasInformedByMap = <any>({});
                    this.anonWasInformedBy = <any>([]);
                    this.namedWasInformedByMap = <any>({});
                    this.anonActedOnBehalfOf = <any>([]);
                    this.namedActedOnBehalfOfMap = <any>({});
                    this.responsibleActedOnBehalfOfMap = <any>({});
                    this.delegateActedOnBehalfOfMap = <any>({});
                    this.namedWasInvalidatedByMap = <any>({});
                    this.entityWasInvalidatedByMap = <any>({});
                    this.anonWasInvalidatedBy = <any>([]);
                    this.activityWasInvalidatedByMap = <any>({});
                    this.namedSpecializationOfMap = <any>({});
                    this.specificEntitySpecializationOfMap = <any>({});
                    this.anonSpecializationOf = <any>([]);
                    this.genericEntitySpecializationOfMap = <any>({});
                    this.anonAlternateOf = <any>([]);
                    this.namedAlternateOfMap = <any>({});
                    this.entityCauseAlternateOfMap = <any>({});
                    this.entityEffectAlternateOfMap = <any>({});
                    this.influenceeWasInfluencedByMap = <any>({});
                    this.influencerWasInfluencedByMap = <any>({});
                    this.anonWasInfluencedBy = <any>([]);
                    this.namedWasInfluencedByMap = <any>({});
                    this.activityWasStartedByMap = <any>({});
                    this.entityWasStartedByMap = <any>({});
                    this.anonWasStartedBy = <any>([]);
                    this.namedWasStartedByMap = <any>({});
                    this.anonWasEndedBy = <any>([]);
                    this.activityWasEndedByMap = <any>({});
                    this.namedWasEndedByMap = <any>({});
                    this.entityWasEndedByMap = <any>({});
                    this.anonHadMember = <any>([]);
                    this.collHadMemberMap = <any>({});
                    this.namedHadMemberMap = <any>({});
                    this.entityHadMemberMap = <any>({});
                    this.bundleMap = <any>({});
                    this.pFactory = pFactory;
                    this.flatten = flatten;
                    if (doc != null){
                        this.nss = doc.getNamespace();
                        this.u.forAllStatementOrBundle(doc.getStatementOrBundle(), this);
                    }
                }
                if (this.pFactory === undefined) { this.pFactory = null; } 
                if (this.nss === undefined) { this.nss = null; } 
                if (this.flatten === undefined) { this.flatten = false; } 
                this.u = new org.openprovenance.prov.model.ProvUtilities();
                this.entityMap = <any>({});
                this.activityMap = <any>({});
                this.agentMap = <any>({});
                this.activityUsedMap = <any>({});
                this.entityUsedMap = <any>({});
                this.anonUsed = <any>([]);
                this.namedUsedMap = <any>({});
                this.activityWasGeneratedByMap = <any>({});
                this.entityWasGeneratedByMap = <any>({});
                this.anonWasGeneratedBy = <any>([]);
                this.namedWasGeneratedByMap = <any>({});
                this.entityCauseWasDerivedFromMap = <any>({});
                this.entityEffectWasDerivedFromMap = <any>({});
                this.anonWasDerivedFrom = <any>([]);
                this.namedWasDerivedFromMap = <any>({});
                this.activityWasAssociatedWithMap = <any>({});
                this.agentWasAssociatedWithMap = <any>({});
                this.anonWasAssociatedWith = <any>([]);
                this.namedWasAssociatedWithMap = <any>({});
                this.entityWasAttributedToMap = <any>({});
                this.agentWasAttributedToMap = <any>({});
                this.anonWasAttributedTo = <any>([]);
                this.namedWasAttributedToMap = <any>({});
                this.activityCauseWasInformedByMap = <any>({});
                this.activityEffectWasInformedByMap = <any>({});
                this.anonWasInformedBy = <any>([]);
                this.namedWasInformedByMap = <any>({});
                this.anonActedOnBehalfOf = <any>([]);
                this.namedActedOnBehalfOfMap = <any>({});
                this.responsibleActedOnBehalfOfMap = <any>({});
                this.delegateActedOnBehalfOfMap = <any>({});
                this.namedWasInvalidatedByMap = <any>({});
                this.entityWasInvalidatedByMap = <any>({});
                this.anonWasInvalidatedBy = <any>([]);
                this.activityWasInvalidatedByMap = <any>({});
                this.namedSpecializationOfMap = <any>({});
                this.specificEntitySpecializationOfMap = <any>({});
                this.anonSpecializationOf = <any>([]);
                this.genericEntitySpecializationOfMap = <any>({});
                this.anonAlternateOf = <any>([]);
                this.namedAlternateOfMap = <any>({});
                this.entityCauseAlternateOfMap = <any>({});
                this.entityEffectAlternateOfMap = <any>({});
                this.influenceeWasInfluencedByMap = <any>({});
                this.influencerWasInfluencedByMap = <any>({});
                this.anonWasInfluencedBy = <any>([]);
                this.namedWasInfluencedByMap = <any>({});
                this.activityWasStartedByMap = <any>({});
                this.entityWasStartedByMap = <any>({});
                this.anonWasStartedBy = <any>([]);
                this.namedWasStartedByMap = <any>({});
                this.anonWasEndedBy = <any>([]);
                this.activityWasEndedByMap = <any>({});
                this.namedWasEndedByMap = <any>({});
                this.entityWasEndedByMap = <any>({});
                this.anonHadMember = <any>([]);
                this.collHadMemberMap = <any>({});
                this.namedHadMemberMap = <any>({});
                this.entityHadMemberMap = <any>({});
                this.bundleMap = <any>({});
            } else throw new Error('invalid overload');
        }

        public add$org_openprovenance_prov_model_WasInformedBy(wib: org.openprovenance.prov.model.WasInformedBy): org.openprovenance.prov.model.WasInformedBy {
            return <any>(this.add(wib, 2, this.anonWasInformedBy, this.namedWasInformedByMap, this.activityEffectWasInformedByMap, this.activityCauseWasInformedByMap));
        }

        public add$org_openprovenance_prov_model_Used(used: org.openprovenance.prov.model.Used): org.openprovenance.prov.model.Used {
            return <any>(this.add(used, 3, this.anonUsed, this.namedUsedMap, this.activityUsedMap, this.entityUsedMap));
        }

        public add$org_openprovenance_prov_model_WasGeneratedBy(wgb: org.openprovenance.prov.model.WasGeneratedBy): org.openprovenance.prov.model.WasGeneratedBy {
            return <any>(this.add(wgb, 3, this.anonWasGeneratedBy, this.namedWasGeneratedByMap, this.entityWasGeneratedByMap, this.activityWasGeneratedByMap));
        }

        public add$org_openprovenance_prov_model_WasDerivedFrom(wdf: org.openprovenance.prov.model.WasDerivedFrom): org.openprovenance.prov.model.WasDerivedFrom {
            return <any>(this.add(wdf, 5, this.anonWasDerivedFrom, this.namedWasDerivedFromMap, this.entityEffectWasDerivedFromMap, this.entityCauseWasDerivedFromMap));
        }

        public add$org_openprovenance_prov_model_WasAssociatedWith(waw: org.openprovenance.prov.model.WasAssociatedWith): org.openprovenance.prov.model.WasAssociatedWith {
            return <any>(this.add(waw, 3, this.anonWasAssociatedWith, this.namedWasAssociatedWithMap, this.activityWasAssociatedWithMap, this.agentWasAssociatedWithMap));
        }

        public add$org_openprovenance_prov_model_WasAttributedTo(wat: org.openprovenance.prov.model.WasAttributedTo): org.openprovenance.prov.model.WasAttributedTo {
            return <any>(this.add(wat, 2, this.anonWasAttributedTo, this.namedWasAttributedToMap, this.entityWasAttributedToMap, this.agentWasAttributedToMap));
        }

        public add$org_openprovenance_prov_model_ActedOnBehalfOf(act: org.openprovenance.prov.model.ActedOnBehalfOf): org.openprovenance.prov.model.ActedOnBehalfOf {
            return <any>(this.add(act, 3, this.anonActedOnBehalfOf, this.namedActedOnBehalfOfMap, this.delegateActedOnBehalfOfMap, this.responsibleActedOnBehalfOfMap));
        }

        public add$org_openprovenance_prov_model_WasInvalidatedBy(wib: org.openprovenance.prov.model.WasInvalidatedBy): org.openprovenance.prov.model.WasInvalidatedBy {
            return <any>(this.add(wib, 3, this.anonWasInvalidatedBy, this.namedWasInvalidatedByMap, this.entityWasInvalidatedByMap, this.activityWasInvalidatedByMap));
        }

        public add$org_openprovenance_prov_model_SpecializationOf(spec: org.openprovenance.prov.model.SpecializationOf): org.openprovenance.prov.model.SpecializationOf {
            return <any>(this.add(spec, 2, this.anonSpecializationOf, this.namedSpecializationOfMap, this.specificEntitySpecializationOfMap, this.genericEntitySpecializationOfMap));
        }

        public add$org_openprovenance_prov_model_AlternateOf(alt: org.openprovenance.prov.model.AlternateOf): org.openprovenance.prov.model.AlternateOf {
            return <any>(this.add(alt, 2, this.anonAlternateOf, this.namedAlternateOfMap, this.entityEffectAlternateOfMap, this.entityCauseAlternateOfMap));
        }

        public add$org_openprovenance_prov_model_WasInfluencedBy(winf: org.openprovenance.prov.model.WasInfluencedBy): org.openprovenance.prov.model.WasInfluencedBy {
            return <any>(this.add(winf, 2, this.anonWasInfluencedBy, this.namedWasInfluencedByMap, this.influenceeWasInfluencedByMap, this.influencerWasInfluencedByMap));
        }

        public add$org_openprovenance_prov_model_WasStartedBy(wsb: org.openprovenance.prov.model.WasStartedBy): org.openprovenance.prov.model.WasStartedBy {
            return <any>(this.add(wsb, 4, this.anonWasStartedBy, this.namedWasStartedByMap, this.activityWasStartedByMap, this.entityWasStartedByMap));
        }

        public add$org_openprovenance_prov_model_WasEndedBy(web: org.openprovenance.prov.model.WasEndedBy): org.openprovenance.prov.model.WasEndedBy {
            return <any>(this.add(web, 4, this.anonWasEndedBy, this.namedWasEndedByMap, this.activityWasEndedByMap, this.entityWasEndedByMap));
        }

        public add$org_openprovenance_prov_model_HadMember(hm: org.openprovenance.prov.model.HadMember): org.openprovenance.prov.model.HadMember {
            return <any>(this.add(hm, 2, this.anonHadMember, this.namedHadMemberMap, this.collHadMemberMap, this.entityHadMemberMap));
        }

        public add$org_openprovenance_prov_model_Relation$int$java_util_Collection$java_util_HashMap$java_util_HashMap$java_util_HashMap<T extends org.openprovenance.prov.model.Relation>(statement: T, num: number, anonRelationCollection: Array<T>, namedRelationMap: any, effectRelationMap: any, causeRelationMap: any): T {
            const aid2: org.openprovenance.prov.model.QualifiedName = this.u.getEffect(statement);
            const aid1: org.openprovenance.prov.model.QualifiedName = this.u.getCause(statement);
            statement = <any>(this.pFactory.newStatement<any>(statement));
            let id: org.openprovenance.prov.model.QualifiedName;
            if (statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Identifiable") >= 0)){
                id = (<any>statement).getId();
            } else {
                id = null;
            }
            if (id == null){
                let found: boolean = false;
                let relationCollection: Array<T> = /* get */((m,k) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { return m.entries[i].value; } return null; })(<any>effectRelationMap, aid2);
                if (relationCollection == null){
                    relationCollection = <any>([]);
                    /* add */(relationCollection.push(statement)>0);
                    /* put */((m,k,v) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { m.entries[i].value=v; return; } m.entries.push({key:k,value:v,getKey: function() { return this.key }, getValue: function() { return this.value }}); })(<any>effectRelationMap, aid2, relationCollection);
                } else {
                    for(let index202=0; index202 < relationCollection.length; index202++) {
                        let u = relationCollection[index202];
                        {
                            if (u.equals(statement)){
                                found = true;
                                statement = u;
                                break;
                            }
                        }
                    }
                    if (!found){
                        /* add */(relationCollection.push(statement)>0);
                    }
                }
                relationCollection = /* get */((m,k) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { return m.entries[i].value; } return null; })(<any>causeRelationMap, aid1);
                if (relationCollection == null){
                    relationCollection = <any>([]);
                    /* add */(relationCollection.push(statement)>0);
                    /* put */((m,k,v) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { m.entries[i].value=v; return; } m.entries.push({key:k,value:v,getKey: function() { return this.key }, getValue: function() { return this.value }}); })(<any>causeRelationMap, aid1, relationCollection);
                } else {
                    if (!found){
                        /* add */(relationCollection.push(statement)>0);
                    }
                }
                if (!found){
                    /* add */(anonRelationCollection.push(statement)>0);
                }
            } else {
                let relationCollection: Array<T> = /* get */((m,k) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { return m.entries[i].value; } return null; })(<any>namedRelationMap, id);
                if (relationCollection == null){
                    relationCollection = <any>([]);
                    /* add */(relationCollection.push(statement)>0);
                    /* put */((m,k,v) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { m.entries[i].value=v; return; } m.entries.push({key:k,value:v,getKey: function() { return this.key }, getValue: function() { return this.value }}); })(<any>namedRelationMap, id, relationCollection);
                } else {
                    let found: boolean = false;
                    for(let index203=0; index203 < relationCollection.length; index203++) {
                        let u1 = relationCollection[index203];
                        {
                            if (this.sameEdge(u1, statement, num)){
                                found = true;
                                this.mergeAttributes(u1, statement);
                                break;
                            }
                        }
                    }
                    if (!found){
                        /* add */(relationCollection.push(statement)>0);
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
        public add<T extends org.openprovenance.prov.model.Relation>(statement?: any, num?: any, anonRelationCollection?: any, namedRelationMap?: any, effectRelationMap?: any, causeRelationMap?: any): any {
            if (((statement != null) || statement === null) && ((typeof num === 'number') || num === null) && ((anonRelationCollection != null && (anonRelationCollection instanceof Array)) || anonRelationCollection === null) && ((namedRelationMap != null && (namedRelationMap instanceof Object)) || namedRelationMap === null) && ((effectRelationMap != null && (effectRelationMap instanceof Object)) || effectRelationMap === null) && ((causeRelationMap != null && (causeRelationMap instanceof Object)) || causeRelationMap === null)) {
                return <any>this.add$org_openprovenance_prov_model_Relation$int$java_util_Collection$java_util_HashMap$java_util_HashMap$java_util_HashMap(statement, num, anonRelationCollection, namedRelationMap, effectRelationMap, causeRelationMap);
            } else if (((statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || statement === null) && ((num != null && (num.constructor != null && num.constructor["__interfaces"] != null && num.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Entity") >= 0)) || num === null) && anonRelationCollection === undefined && namedRelationMap === undefined && effectRelationMap === undefined && causeRelationMap === undefined) {
                return <any>this.add$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_Entity(statement, num);
            } else if (((statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || statement === null) && ((num != null && (num.constructor != null && num.constructor["__interfaces"] != null && num.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Agent") >= 0)) || num === null) && anonRelationCollection === undefined && namedRelationMap === undefined && effectRelationMap === undefined && causeRelationMap === undefined) {
                return <any>this.add$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_Agent(statement, num);
            } else if (((statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || statement === null) && ((num != null && (num.constructor != null && num.constructor["__interfaces"] != null && num.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Activity") >= 0)) || num === null) && anonRelationCollection === undefined && namedRelationMap === undefined && effectRelationMap === undefined && causeRelationMap === undefined) {
                return <any>this.add$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_Activity(statement, num);
            } else if (((statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Entity") >= 0)) || statement === null) && num === undefined && anonRelationCollection === undefined && namedRelationMap === undefined && effectRelationMap === undefined && causeRelationMap === undefined) {
                return <any>this.add$org_openprovenance_prov_model_Entity(statement);
            } else if (((statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Agent") >= 0)) || statement === null) && num === undefined && anonRelationCollection === undefined && namedRelationMap === undefined && effectRelationMap === undefined && causeRelationMap === undefined) {
                return <any>this.add$org_openprovenance_prov_model_Agent(statement);
            } else if (((statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Activity") >= 0)) || statement === null) && num === undefined && anonRelationCollection === undefined && namedRelationMap === undefined && effectRelationMap === undefined && causeRelationMap === undefined) {
                return <any>this.add$org_openprovenance_prov_model_Activity(statement);
            } else if (((statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInformedBy") >= 0)) || statement === null) && num === undefined && anonRelationCollection === undefined && namedRelationMap === undefined && effectRelationMap === undefined && causeRelationMap === undefined) {
                return <any>this.add$org_openprovenance_prov_model_WasInformedBy(statement);
            } else if (((statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Used") >= 0)) || statement === null) && num === undefined && anonRelationCollection === undefined && namedRelationMap === undefined && effectRelationMap === undefined && causeRelationMap === undefined) {
                return <any>this.add$org_openprovenance_prov_model_Used(statement);
            } else if (((statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasGeneratedBy") >= 0)) || statement === null) && num === undefined && anonRelationCollection === undefined && namedRelationMap === undefined && effectRelationMap === undefined && causeRelationMap === undefined) {
                return <any>this.add$org_openprovenance_prov_model_WasGeneratedBy(statement);
            } else if (((statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasDerivedFrom") >= 0)) || statement === null) && num === undefined && anonRelationCollection === undefined && namedRelationMap === undefined && effectRelationMap === undefined && causeRelationMap === undefined) {
                return <any>this.add$org_openprovenance_prov_model_WasDerivedFrom(statement);
            } else if (((statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasAssociatedWith") >= 0)) || statement === null) && num === undefined && anonRelationCollection === undefined && namedRelationMap === undefined && effectRelationMap === undefined && causeRelationMap === undefined) {
                return <any>this.add$org_openprovenance_prov_model_WasAssociatedWith(statement);
            } else if (((statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasAttributedTo") >= 0)) || statement === null) && num === undefined && anonRelationCollection === undefined && namedRelationMap === undefined && effectRelationMap === undefined && causeRelationMap === undefined) {
                return <any>this.add$org_openprovenance_prov_model_WasAttributedTo(statement);
            } else if (((statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.ActedOnBehalfOf") >= 0)) || statement === null) && num === undefined && anonRelationCollection === undefined && namedRelationMap === undefined && effectRelationMap === undefined && causeRelationMap === undefined) {
                return <any>this.add$org_openprovenance_prov_model_ActedOnBehalfOf(statement);
            } else if (((statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInvalidatedBy") >= 0)) || statement === null) && num === undefined && anonRelationCollection === undefined && namedRelationMap === undefined && effectRelationMap === undefined && causeRelationMap === undefined) {
                return <any>this.add$org_openprovenance_prov_model_WasInvalidatedBy(statement);
            } else if (((statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.SpecializationOf") >= 0)) || statement === null) && num === undefined && anonRelationCollection === undefined && namedRelationMap === undefined && effectRelationMap === undefined && causeRelationMap === undefined) {
                return <any>this.add$org_openprovenance_prov_model_SpecializationOf(statement);
            } else if (((statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.AlternateOf") >= 0)) || statement === null) && num === undefined && anonRelationCollection === undefined && namedRelationMap === undefined && effectRelationMap === undefined && causeRelationMap === undefined) {
                return <any>this.add$org_openprovenance_prov_model_AlternateOf(statement);
            } else if (((statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInfluencedBy") >= 0)) || statement === null) && num === undefined && anonRelationCollection === undefined && namedRelationMap === undefined && effectRelationMap === undefined && causeRelationMap === undefined) {
                return <any>this.add$org_openprovenance_prov_model_WasInfluencedBy(statement);
            } else if (((statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasStartedBy") >= 0)) || statement === null) && num === undefined && anonRelationCollection === undefined && namedRelationMap === undefined && effectRelationMap === undefined && causeRelationMap === undefined) {
                return <any>this.add$org_openprovenance_prov_model_WasStartedBy(statement);
            } else if (((statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasEndedBy") >= 0)) || statement === null) && num === undefined && anonRelationCollection === undefined && namedRelationMap === undefined && effectRelationMap === undefined && causeRelationMap === undefined) {
                return <any>this.add$org_openprovenance_prov_model_WasEndedBy(statement);
            } else if (((statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HadMember") >= 0)) || statement === null) && num === undefined && anonRelationCollection === undefined && namedRelationMap === undefined && effectRelationMap === undefined && causeRelationMap === undefined) {
                return <any>this.add$org_openprovenance_prov_model_HadMember(statement);
            } else throw new Error('invalid overload');
        }

        public doAction$org_openprovenance_prov_model_Activity(s: org.openprovenance.prov.model.Activity) {
            this.add$org_openprovenance_prov_model_Activity(s);
        }

        public doAction$org_openprovenance_prov_model_Used(s: org.openprovenance.prov.model.Used) {
            this.add$org_openprovenance_prov_model_Used(s);
        }

        public doAction$org_openprovenance_prov_model_WasStartedBy(s: org.openprovenance.prov.model.WasStartedBy) {
            this.add$org_openprovenance_prov_model_WasStartedBy(s);
        }

        public doAction$org_openprovenance_prov_model_Agent(s: org.openprovenance.prov.model.Agent) {
            this.add$org_openprovenance_prov_model_Agent(s);
        }

        public doAction$org_openprovenance_prov_model_AlternateOf(s: org.openprovenance.prov.model.AlternateOf) {
            this.add$org_openprovenance_prov_model_AlternateOf(s);
        }

        public doAction$org_openprovenance_prov_model_WasAssociatedWith(s: org.openprovenance.prov.model.WasAssociatedWith) {
            this.add$org_openprovenance_prov_model_WasAssociatedWith(s);
        }

        public doAction$org_openprovenance_prov_model_WasAttributedTo(s: org.openprovenance.prov.model.WasAttributedTo) {
            this.add$org_openprovenance_prov_model_WasAttributedTo(s);
        }

        public doAction$org_openprovenance_prov_model_WasInfluencedBy(s: org.openprovenance.prov.model.WasInfluencedBy) {
            this.add$org_openprovenance_prov_model_WasInfluencedBy(s);
        }

        public doAction$org_openprovenance_prov_model_ActedOnBehalfOf(s: org.openprovenance.prov.model.ActedOnBehalfOf) {
            this.add$org_openprovenance_prov_model_ActedOnBehalfOf(s);
        }

        public doAction$org_openprovenance_prov_model_WasDerivedFrom(s: org.openprovenance.prov.model.WasDerivedFrom) {
            this.add$org_openprovenance_prov_model_WasDerivedFrom(s);
        }

        public doAction$org_openprovenance_prov_model_WasEndedBy(s: org.openprovenance.prov.model.WasEndedBy) {
            this.add$org_openprovenance_prov_model_WasEndedBy(s);
        }

        public doAction$org_openprovenance_prov_model_Entity(s: org.openprovenance.prov.model.Entity) {
            this.add$org_openprovenance_prov_model_Entity(s);
        }

        public doAction$org_openprovenance_prov_model_WasGeneratedBy(s: org.openprovenance.prov.model.WasGeneratedBy) {
            this.add$org_openprovenance_prov_model_WasGeneratedBy(s);
        }

        public doAction$org_openprovenance_prov_model_WasInvalidatedBy(s: org.openprovenance.prov.model.WasInvalidatedBy) {
            this.add$org_openprovenance_prov_model_WasInvalidatedBy(s);
        }

        public doAction$org_openprovenance_prov_model_HadMember(s: org.openprovenance.prov.model.HadMember) {
            this.add$org_openprovenance_prov_model_HadMember(s);
        }

        public doAction$org_openprovenance_prov_model_MentionOf(s: org.openprovenance.prov.model.MentionOf) {
            throw Object.defineProperty(new Error(), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.UnsupportedOperationException','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
        }

        public doAction$org_openprovenance_prov_model_SpecializationOf(s: org.openprovenance.prov.model.SpecializationOf) {
            this.add$org_openprovenance_prov_model_SpecializationOf(s);
        }

        public doAction$org_openprovenance_prov_model_extension_QualifiedSpecializationOf(s: org.openprovenance.prov.model.extension.QualifiedSpecializationOf) {
            this.add$org_openprovenance_prov_model_SpecializationOf(s);
        }

        public doAction$org_openprovenance_prov_model_extension_QualifiedAlternateOf(s: org.openprovenance.prov.model.extension.QualifiedAlternateOf) {
            this.add$org_openprovenance_prov_model_AlternateOf(s);
        }

        public doAction$org_openprovenance_prov_model_extension_QualifiedHadMember(s: org.openprovenance.prov.model.extension.QualifiedHadMember) {
            this.add$org_openprovenance_prov_model_HadMember(s);
        }

        public doAction$org_openprovenance_prov_model_WasInformedBy(s: org.openprovenance.prov.model.WasInformedBy) {
            this.add$org_openprovenance_prov_model_WasInformedBy(s);
        }

        public doAction$org_openprovenance_prov_model_DerivedByInsertionFrom(s: org.openprovenance.prov.model.DerivedByInsertionFrom) {
            throw Object.defineProperty(new Error(), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.UnsupportedOperationException','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
        }

        public doAction$org_openprovenance_prov_model_DictionaryMembership(s: org.openprovenance.prov.model.DictionaryMembership) {
            throw Object.defineProperty(new Error(), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.UnsupportedOperationException','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
        }

        public doAction$org_openprovenance_prov_model_DerivedByRemovalFrom(s: org.openprovenance.prov.model.DerivedByRemovalFrom) {
            throw Object.defineProperty(new Error(), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.UnsupportedOperationException','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
        }

        bundleMap: any;

        public doAction$org_openprovenance_prov_model_Bundle$org_openprovenance_prov_model_ProvUtilities(bun: org.openprovenance.prov.model.Bundle, provUtilities: org.openprovenance.prov.model.ProvUtilities) {
            if (this.flatten){
                provUtilities.forAllStatement(bun.getStatement(), this);
            } else {
                let iDoc: IndexedDocument = /* get */((m,k) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { return m.entries[i].value; } return null; })(<any>this.bundleMap, bun.getId());
                if (iDoc == null){
                    iDoc = new IndexedDocument(this.pFactory, null, this.flatten);
                    /* put */((m,k,v) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { m.entries[i].value=v; return; } m.entries.push({key:k,value:v,getKey: function() { return this.key }, getValue: function() { return this.value }}); })(<any>this.bundleMap, bun.getId(), iDoc);
                }
                this.u.forAllStatement(bun.getStatement(), iDoc);
            }
        }

        /**
         * 
         * @param {*} bun
         * @param {org.openprovenance.prov.model.ProvUtilities} provUtilities
         */
        public doAction(bun?: any, provUtilities?: any) {
            if (((bun != null && (bun.constructor != null && bun.constructor["__interfaces"] != null && bun.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Bundle") >= 0)) || bun === null) && ((provUtilities != null && provUtilities instanceof <any>org.openprovenance.prov.model.ProvUtilities) || provUtilities === null)) {
                return <any>this.doAction$org_openprovenance_prov_model_Bundle$org_openprovenance_prov_model_ProvUtilities(bun, provUtilities);
            } else if (((bun != null && (bun.constructor != null && bun.constructor["__interfaces"] != null && bun.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Activity") >= 0)) || bun === null) && provUtilities === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_Activity(bun);
            } else if (((bun != null && (bun.constructor != null && bun.constructor["__interfaces"] != null && bun.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Used") >= 0)) || bun === null) && provUtilities === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_Used(bun);
            } else if (((bun != null && (bun.constructor != null && bun.constructor["__interfaces"] != null && bun.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasStartedBy") >= 0)) || bun === null) && provUtilities === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_WasStartedBy(bun);
            } else if (((bun != null && (bun.constructor != null && bun.constructor["__interfaces"] != null && bun.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Agent") >= 0)) || bun === null) && provUtilities === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_Agent(bun);
            } else if (((bun != null && (bun.constructor != null && bun.constructor["__interfaces"] != null && bun.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.AlternateOf") >= 0)) || bun === null) && provUtilities === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_AlternateOf(bun);
            } else if (((bun != null && (bun.constructor != null && bun.constructor["__interfaces"] != null && bun.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasAssociatedWith") >= 0)) || bun === null) && provUtilities === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_WasAssociatedWith(bun);
            } else if (((bun != null && (bun.constructor != null && bun.constructor["__interfaces"] != null && bun.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasAttributedTo") >= 0)) || bun === null) && provUtilities === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_WasAttributedTo(bun);
            } else if (((bun != null && (bun.constructor != null && bun.constructor["__interfaces"] != null && bun.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInfluencedBy") >= 0)) || bun === null) && provUtilities === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_WasInfluencedBy(bun);
            } else if (((bun != null && (bun.constructor != null && bun.constructor["__interfaces"] != null && bun.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.ActedOnBehalfOf") >= 0)) || bun === null) && provUtilities === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_ActedOnBehalfOf(bun);
            } else if (((bun != null && (bun.constructor != null && bun.constructor["__interfaces"] != null && bun.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasDerivedFrom") >= 0)) || bun === null) && provUtilities === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_WasDerivedFrom(bun);
            } else if (((bun != null && (bun.constructor != null && bun.constructor["__interfaces"] != null && bun.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasEndedBy") >= 0)) || bun === null) && provUtilities === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_WasEndedBy(bun);
            } else if (((bun != null && (bun.constructor != null && bun.constructor["__interfaces"] != null && bun.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Entity") >= 0)) || bun === null) && provUtilities === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_Entity(bun);
            } else if (((bun != null && (bun.constructor != null && bun.constructor["__interfaces"] != null && bun.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasGeneratedBy") >= 0)) || bun === null) && provUtilities === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_WasGeneratedBy(bun);
            } else if (((bun != null && (bun.constructor != null && bun.constructor["__interfaces"] != null && bun.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInvalidatedBy") >= 0)) || bun === null) && provUtilities === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_WasInvalidatedBy(bun);
            } else if (((bun != null && (bun.constructor != null && bun.constructor["__interfaces"] != null && bun.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.extension.QualifiedHadMember") >= 0)) || bun === null) && provUtilities === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_extension_QualifiedHadMember(bun);
            } else if (((bun != null && (bun.constructor != null && bun.constructor["__interfaces"] != null && bun.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HadMember") >= 0)) || bun === null) && provUtilities === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_HadMember(bun);
            } else if (((bun != null && (bun.constructor != null && bun.constructor["__interfaces"] != null && bun.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.MentionOf") >= 0)) || bun === null) && provUtilities === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_MentionOf(bun);
            } else if (((bun != null && (bun.constructor != null && bun.constructor["__interfaces"] != null && bun.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.extension.QualifiedSpecializationOf") >= 0)) || bun === null) && provUtilities === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_extension_QualifiedSpecializationOf(bun);
            } else if (((bun != null && (bun.constructor != null && bun.constructor["__interfaces"] != null && bun.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.SpecializationOf") >= 0)) || bun === null) && provUtilities === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_SpecializationOf(bun);
            } else if (((bun != null && (bun.constructor != null && bun.constructor["__interfaces"] != null && bun.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.extension.QualifiedAlternateOf") >= 0)) || bun === null) && provUtilities === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_extension_QualifiedAlternateOf(bun);
            } else if (((bun != null && (bun.constructor != null && bun.constructor["__interfaces"] != null && bun.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInformedBy") >= 0)) || bun === null) && provUtilities === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_WasInformedBy(bun);
            } else if (((bun != null && (bun.constructor != null && bun.constructor["__interfaces"] != null && bun.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.DerivedByInsertionFrom") >= 0)) || bun === null) && provUtilities === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_DerivedByInsertionFrom(bun);
            } else if (((bun != null && (bun.constructor != null && bun.constructor["__interfaces"] != null && bun.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.DictionaryMembership") >= 0)) || bun === null) && provUtilities === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_DictionaryMembership(bun);
            } else if (((bun != null && (bun.constructor != null && bun.constructor["__interfaces"] != null && bun.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.DerivedByRemovalFrom") >= 0)) || bun === null) && provUtilities === undefined) {
                return <any>this.doAction$org_openprovenance_prov_model_DerivedByRemovalFrom(bun);
            } else throw new Error('invalid overload');
        }

        public toDocument(): org.openprovenance.prov.model.Document {
            const res: org.openprovenance.prov.model.Document = this.pFactory.newDocument$();
            const statementOrBundle: Array<org.openprovenance.prov.model.StatementOrBundle> = res.getStatementOrBundle();
            this.toContainer(statementOrBundle);
            if (!this.flatten){
                {
                    let array205 = /* keySet */((m) => { let r=[]; if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) r.push(m.entries[i].key); return r; })(<any>this.bundleMap);
                    for(let index204=0; index204 < array205.length; index204++) {
                        let bunId = array205[index204];
                        {
                            const idoc: IndexedDocument = /* get */((m,k) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { return m.entries[i].value; } return null; })(<any>this.bundleMap, bunId);
                            const bun: org.openprovenance.prov.model.Bundle = this.pFactory.newNamedBundle$org_openprovenance_prov_model_QualifiedName$java_util_Collection(bunId, null);
                            const ll: Array<org.openprovenance.prov.model.StatementOrBundle> = <any>([]);
                            idoc.toContainer(ll);
                            for(let index206=0; index206 < ll.length; index206++) {
                                let s = ll[index206];
                                {
                                    /* add */(bun.getStatement().push(<org.openprovenance.prov.model.Statement><any>s)>0);
                                }
                            }
                            bun.setNamespace(org.openprovenance.prov.model.Namespace.gatherNamespaces$org_openprovenance_prov_model_Bundle(bun));
                            /* add */(statementOrBundle.push(bun)>0);
                        }
                    }
                }
            }
            res.setNamespace(org.openprovenance.prov.model.Namespace.gatherNamespaces$org_openprovenance_prov_model_Document(res));
            return res;
        }

        /*private*/ toContainer(statementOrBundle: Array<org.openprovenance.prov.model.StatementOrBundle>) {
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, /* values */((m) => { let r=[]; if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) r.push(m.entries[i].value); return r; })(<any>this.entityMap));
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, /* values */((m) => { let r=[]; if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) r.push(m.entries[i].value); return r; })(<any>this.activityMap));
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, /* values */((m) => { let r=[]; if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) r.push(m.entries[i].value); return r; })(<any>this.agentMap));
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, this.anonUsed);
            {
                let array208 = /* values */((m) => { let r=[]; if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) r.push(m.entries[i].value); return r; })(<any>this.namedUsedMap);
                for(let index207=0; index207 < array208.length; index207++) {
                    let c = array208[index207];
                    {
                        /* addAll */((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, c);
                    }
                }
            }
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, this.anonWasGeneratedBy);
            {
                let array210 = /* values */((m) => { let r=[]; if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) r.push(m.entries[i].value); return r; })(<any>this.namedWasGeneratedByMap);
                for(let index209=0; index209 < array210.length; index209++) {
                    let c = array210[index209];
                    {
                        /* addAll */((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, c);
                    }
                }
            }
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, this.anonWasDerivedFrom);
            {
                let array212 = /* values */((m) => { let r=[]; if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) r.push(m.entries[i].value); return r; })(<any>this.namedWasDerivedFromMap);
                for(let index211=0; index211 < array212.length; index211++) {
                    let c = array212[index211];
                    {
                        /* addAll */((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, c);
                    }
                }
            }
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, this.anonWasAssociatedWith);
            {
                let array214 = /* values */((m) => { let r=[]; if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) r.push(m.entries[i].value); return r; })(<any>this.namedWasAssociatedWithMap);
                for(let index213=0; index213 < array214.length; index213++) {
                    let c = array214[index213];
                    {
                        /* addAll */((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, c);
                    }
                }
            }
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, this.anonWasAttributedTo);
            {
                let array216 = /* values */((m) => { let r=[]; if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) r.push(m.entries[i].value); return r; })(<any>this.namedWasAttributedToMap);
                for(let index215=0; index215 < array216.length; index215++) {
                    let c = array216[index215];
                    {
                        /* addAll */((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, c);
                    }
                }
            }
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, this.anonWasInformedBy);
            {
                let array218 = /* values */((m) => { let r=[]; if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) r.push(m.entries[i].value); return r; })(<any>this.namedWasInformedByMap);
                for(let index217=0; index217 < array218.length; index217++) {
                    let c = array218[index217];
                    {
                        /* addAll */((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, c);
                    }
                }
            }
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, this.anonSpecializationOf);
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, this.anonAlternateOf);
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, this.anonHadMember);
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, this.anonWasInvalidatedBy);
            {
                let array220 = /* values */((m) => { let r=[]; if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) r.push(m.entries[i].value); return r; })(<any>this.namedWasInvalidatedByMap);
                for(let index219=0; index219 < array220.length; index219++) {
                    let c = array220[index219];
                    {
                        /* addAll */((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, c);
                    }
                }
            }
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, this.anonWasStartedBy);
            {
                let array222 = /* values */((m) => { let r=[]; if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) r.push(m.entries[i].value); return r; })(<any>this.namedWasStartedByMap);
                for(let index221=0; index221 < array222.length; index221++) {
                    let c = array222[index221];
                    {
                        /* addAll */((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, c);
                    }
                }
            }
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, this.anonWasEndedBy);
            {
                let array224 = /* values */((m) => { let r=[]; if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) r.push(m.entries[i].value); return r; })(<any>this.namedWasEndedByMap);
                for(let index223=0; index223 < array224.length; index223++) {
                    let c = array224[index223];
                    {
                        /* addAll */((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, c);
                    }
                }
            }
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, this.anonActedOnBehalfOf);
            {
                let array226 = /* values */((m) => { let r=[]; if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) r.push(m.entries[i].value); return r; })(<any>this.namedActedOnBehalfOfMap);
                for(let index225=0; index225 < array226.length; index225++) {
                    let c = array226[index225];
                    {
                        /* addAll */((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, c);
                    }
                }
            }
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, this.anonWasInfluencedBy);
            {
                let array228 = /* values */((m) => { let r=[]; if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) r.push(m.entries[i].value); return r; })(<any>this.namedWasInfluencedByMap);
                for(let index227=0; index227 < array228.length; index227++) {
                    let c = array228[index227];
                    {
                        /* addAll */((l1, l2) => l1.push.apply(l1, l2))(statementOrBundle, c);
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
        public merge(doc: org.openprovenance.prov.model.Document) {
            this.u.forAllStatementOrBundle(doc.getStatementOrBundle(), this);
        }

        public traverseDerivations(from: org.openprovenance.prov.model.QualifiedName): Array<org.openprovenance.prov.model.QualifiedName> {
            const s: Array<org.openprovenance.prov.model.QualifiedName> = <any>([]);
            /* push */(s.push(from)>0);
            return this.traverseDerivations1(<any>([]), <any>([]), s);
        }

        public traverseDerivations1(last: Array<org.openprovenance.prov.model.QualifiedName>, seen: Array<org.openprovenance.prov.model.QualifiedName>, todo: Array<org.openprovenance.prov.model.QualifiedName>): Array<org.openprovenance.prov.model.QualifiedName> {
            let current: org.openprovenance.prov.model.QualifiedName = null;
            while((!(/* isEmpty */(todo.length == 0)))) {{
                current = /* pop */todo.pop();
                if (/* contains */(seen.indexOf(<any>(current)) >= 0)){
                } else {
                    /* add */((s, e) => { if(s.indexOf(e)==-1) { s.push(e); return true; } else { return false; } })(seen, current);
                    const successors: Array<org.openprovenance.prov.model.WasDerivedFrom> = /* get */((m,k) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { return m.entries[i].value; } return null; })(<any>this.entityCauseWasDerivedFromMap, current);
                    if (successors == null || /* isEmpty */(successors.length == 0)){
                    } else {
                        for(let index229=0; index229 < successors.length; index229++) {
                            let wdf = successors[index229];
                            {
                                const qn: org.openprovenance.prov.model.QualifiedName = wdf.getGeneratedEntity();
                                /* add */((s, e) => { if(s.indexOf(e)==-1) { s.push(e); return true; } else { return false; } })(last, qn);
                                /* push */(todo.push(qn)>0);
                            }
                        }
                    }
                }
            }};
            return last;
        }
    }
    IndexedDocument["__class"] = "org.openprovenance.prov.model.IndexedDocument";
    IndexedDocument["__interfaces"] = ["org.openprovenance.prov.model.StatementAction"];


}

