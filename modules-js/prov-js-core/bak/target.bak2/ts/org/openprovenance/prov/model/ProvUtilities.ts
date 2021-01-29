/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.model {
    /**
     * Utilities for manipulating PROV Descriptions.
     * @class
     */
    export class ProvUtilities {
        public getRelations$org_openprovenance_prov_model_Document(d: org.openprovenance.prov.model.Document): Array<org.openprovenance.prov.model.Relation> {
            return this.getObject<any>("org.openprovenance.prov.model.Relation", d.getStatementOrBundle());
        }

        public getRelations(d?: any): any {
            if (((d != null && (d.constructor != null && d.constructor["__interfaces"] != null && d.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Document") >= 0)) || d === null)) {
                return <any>this.getRelations$org_openprovenance_prov_model_Document(d);
            } else if (((d != null && (d.constructor != null && d.constructor["__interfaces"] != null && d.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Bundle") >= 0)) || d === null)) {
                return <any>this.getRelations$org_openprovenance_prov_model_Bundle(d);
            } else throw new Error('invalid overload');
        }

        public getEntity$org_openprovenance_prov_model_Document(d: org.openprovenance.prov.model.Document): Array<org.openprovenance.prov.model.Entity> {
            return this.getObject<any>("org.openprovenance.prov.model.Entity", d.getStatementOrBundle());
        }

        public getEntity(d?: any): any {
            if (((d != null && (d.constructor != null && d.constructor["__interfaces"] != null && d.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Document") >= 0)) || d === null)) {
                return <any>this.getEntity$org_openprovenance_prov_model_Document(d);
            } else if (((d != null && (d.constructor != null && d.constructor["__interfaces"] != null && d.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Bundle") >= 0)) || d === null)) {
                return <any>this.getEntity$org_openprovenance_prov_model_Bundle(d);
            } else throw new Error('invalid overload');
        }

        public getActivity$org_openprovenance_prov_model_Document(d: org.openprovenance.prov.model.Document): Array<org.openprovenance.prov.model.Activity> {
            return this.getObject<any>("org.openprovenance.prov.model.Activity", d.getStatementOrBundle());
        }

        public getActivity(d?: any): any {
            if (((d != null && (d.constructor != null && d.constructor["__interfaces"] != null && d.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Document") >= 0)) || d === null)) {
                return <any>this.getActivity$org_openprovenance_prov_model_Document(d);
            } else if (((d != null && (d.constructor != null && d.constructor["__interfaces"] != null && d.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Bundle") >= 0)) || d === null)) {
                return <any>this.getActivity$org_openprovenance_prov_model_Bundle(d);
            } else throw new Error('invalid overload');
        }

        public getAgent$org_openprovenance_prov_model_Document(d: org.openprovenance.prov.model.Document): Array<org.openprovenance.prov.model.Agent> {
            return this.getObject<any>("org.openprovenance.prov.model.Agent", d.getStatementOrBundle());
        }

        public getAgent(d?: any): any {
            if (((d != null && (d.constructor != null && d.constructor["__interfaces"] != null && d.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Document") >= 0)) || d === null)) {
                return <any>this.getAgent$org_openprovenance_prov_model_Document(d);
            } else if (((d != null && (d.constructor != null && d.constructor["__interfaces"] != null && d.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Bundle") >= 0)) || d === null)) {
                return <any>this.getAgent$org_openprovenance_prov_model_Bundle(d);
            } else throw new Error('invalid overload');
        }

        public getBundle(d: org.openprovenance.prov.model.Document): Array<org.openprovenance.prov.model.Bundle> {
            return this.getObject<any>("org.openprovenance.prov.model.Bundle", d.getStatementOrBundle());
        }

        public getRelations$org_openprovenance_prov_model_Bundle(d: org.openprovenance.prov.model.Bundle): Array<org.openprovenance.prov.model.Relation> {
            return this.getObject2<any>("org.openprovenance.prov.model.Relation", d.getStatement());
        }

        public getEntity$org_openprovenance_prov_model_Bundle(d: org.openprovenance.prov.model.Bundle): Array<org.openprovenance.prov.model.Entity> {
            return this.getObject2<any>("org.openprovenance.prov.model.Entity", d.getStatement());
        }

        public getActivity$org_openprovenance_prov_model_Bundle(d: org.openprovenance.prov.model.Bundle): Array<org.openprovenance.prov.model.Activity> {
            return this.getObject2<any>("org.openprovenance.prov.model.Activity", d.getStatement());
        }

        public getAgent$org_openprovenance_prov_model_Bundle(d: org.openprovenance.prov.model.Bundle): Array<org.openprovenance.prov.model.Agent> {
            return this.getObject2<any>("org.openprovenance.prov.model.Agent", d.getStatement());
        }

        public getNamedBundle(d: org.openprovenance.prov.model.Document): Array<org.openprovenance.prov.model.Bundle> {
            return this.getObject<any>("org.openprovenance.prov.model.Bundle", d.getStatementOrBundle());
        }

        public getStatement$org_openprovenance_prov_model_Document(d: org.openprovenance.prov.model.Document): Array<org.openprovenance.prov.model.Statement> {
            return this.getObject<any>("org.openprovenance.prov.model.Statement", d.getStatementOrBundle());
        }

        public getStatement(d?: any): any {
            if (((d != null && (d.constructor != null && d.constructor["__interfaces"] != null && d.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Document") >= 0)) || d === null)) {
                return <any>this.getStatement$org_openprovenance_prov_model_Document(d);
            } else if (((d != null && (d.constructor != null && d.constructor["__interfaces"] != null && d.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Bundle") >= 0)) || d === null)) {
                return <any>this.getStatement$org_openprovenance_prov_model_Bundle(d);
            } else throw new Error('invalid overload');
        }

        public getStatement$org_openprovenance_prov_model_Bundle(d: org.openprovenance.prov.model.Bundle): Array<org.openprovenance.prov.model.Statement> {
            const res: Array<any> = d.getStatement();
            return <Array<org.openprovenance.prov.model.Statement>><any>res;
        }

        public getObject<T>(cl: any, ll: Array<org.openprovenance.prov.model.StatementOrBundle>): Array<T> {
            const res: Array<T> = <any>([]);
            for(let index151=0; index151 < ll.length; index151++) {
                let o = ll[index151];
                {
                    if (/* isInstance */((c:any,o:any) => { if(typeof c === 'string') return (o.constructor && o.constructor["__interfaces"] && o.constructor["__interfaces"].indexOf(c) >= 0) || (o["__interfaces"] && o["__interfaces"].indexOf(c) >= 0); else if(typeof c === 'function') return (o instanceof c) || (o.constructor && o.constructor === c); })(cl, o)){
                        const o2: T = <T><any>o;
                        /* add */(res.push(o2)>0);
                    }
                }
            }
            return res;
        }

        public getObject2<T>(cl: any, ll: Array<org.openprovenance.prov.model.Statement>): Array<T> {
            const res: Array<T> = <any>([]);
            for(let index152=0; index152 < ll.length; index152++) {
                let o = ll[index152];
                {
                    if (/* isInstance */((c:any,o:any) => { if(typeof c === 'string') return (o.constructor && o.constructor["__interfaces"] && o.constructor["__interfaces"].indexOf(c) >= 0) || (o["__interfaces"] && o["__interfaces"].indexOf(c) >= 0); else if(typeof c === 'function') return (o instanceof c) || (o.constructor && o.constructor === c); })(cl, o)){
                        const o2: T = <T><any>o;
                        /* add */(res.push(o2)>0);
                    }
                }
            }
            return res;
        }

        public getEffect(r: org.openprovenance.prov.model.Relation): org.openprovenance.prov.model.QualifiedName {
            if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Used") >= 0)){
                return (<org.openprovenance.prov.model.Used><any>r).getActivity();
            }
            if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasStartedBy") >= 0)){
                return (<org.openprovenance.prov.model.WasStartedBy><any>r).getActivity();
            }
            if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasEndedBy") >= 0)){
                return (<org.openprovenance.prov.model.WasEndedBy><any>r).getActivity();
            }
            if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasGeneratedBy") >= 0)){
                return (<org.openprovenance.prov.model.WasGeneratedBy><any>r).getEntity();
            }
            if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasDerivedFrom") >= 0)){
                return (<org.openprovenance.prov.model.WasDerivedFrom><any>r).getGeneratedEntity();
            }
            if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasAssociatedWith") >= 0)){
                return (<org.openprovenance.prov.model.WasAssociatedWith><any>r).getActivity();
            }
            if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInvalidatedBy") >= 0)){
                return (<org.openprovenance.prov.model.WasInvalidatedBy><any>r).getEntity();
            }
            if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasAttributedTo") >= 0)){
                return (<org.openprovenance.prov.model.WasAttributedTo><any>r).getEntity();
            }
            if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.AlternateOf") >= 0)){
                return (<org.openprovenance.prov.model.AlternateOf><any>r).getAlternate1();
            }
            if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.SpecializationOf") >= 0)){
                return (<org.openprovenance.prov.model.SpecializationOf><any>r).getSpecificEntity();
            }
            if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HadMember") >= 0)){
                return (<org.openprovenance.prov.model.HadMember><any>r).getCollection();
            }
            if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInformedBy") >= 0)){
                return (<org.openprovenance.prov.model.WasInformedBy><any>r).getInformed();
            }
            if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.MentionOf") >= 0)){
                return (<org.openprovenance.prov.model.MentionOf><any>r).getSpecificEntity();
            }
            if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInfluencedBy") >= 0)){
                return (<org.openprovenance.prov.model.WasInfluencedBy><any>r).getInfluencee();
            }
            if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.ActedOnBehalfOf") >= 0)){
                return (<org.openprovenance.prov.model.ActedOnBehalfOf><any>r).getDelegate();
            }
            if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.DerivedByInsertionFrom") >= 0)){
                return (<org.openprovenance.prov.model.DerivedByInsertionFrom><any>r).getNewDictionary();
            }
            console.info("Unknown relation " + r);
            throw Object.defineProperty(new Error(), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.UnsupportedOperationException','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
        }

        public getCause(r: org.openprovenance.prov.model.Relation): org.openprovenance.prov.model.QualifiedName {
            if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Used") >= 0)){
                return (<org.openprovenance.prov.model.Used><any>r).getEntity();
            }
            if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasGeneratedBy") >= 0)){
                return (<org.openprovenance.prov.model.WasGeneratedBy><any>r).getActivity();
            }
            if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInvalidatedBy") >= 0)){
                return (<org.openprovenance.prov.model.WasInvalidatedBy><any>r).getActivity();
            }
            if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasStartedBy") >= 0)){
                return (<org.openprovenance.prov.model.WasStartedBy><any>r).getTrigger();
            }
            if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasEndedBy") >= 0)){
                return (<org.openprovenance.prov.model.WasEndedBy><any>r).getTrigger();
            }
            if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasDerivedFrom") >= 0)){
                return (<org.openprovenance.prov.model.WasDerivedFrom><any>r).getUsedEntity();
            }
            if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInfluencedBy") >= 0)){
                return (<org.openprovenance.prov.model.WasInfluencedBy><any>r).getInfluencer();
            }
            if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasAssociatedWith") >= 0)){
                return (<org.openprovenance.prov.model.WasAssociatedWith><any>r).getAgent();
            }
            if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasAttributedTo") >= 0)){
                return (<org.openprovenance.prov.model.WasAttributedTo><any>r).getAgent();
            }
            if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.AlternateOf") >= 0)){
                return (<org.openprovenance.prov.model.AlternateOf><any>r).getAlternate2();
            }
            if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.SpecializationOf") >= 0)){
                return (<org.openprovenance.prov.model.SpecializationOf><any>r).getGeneralEntity();
            }
            if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HadMember") >= 0)){
                return /* get */(<org.openprovenance.prov.model.HadMember><any>r).getEntity()[0];
            }
            if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.MentionOf") >= 0)){
                return (<org.openprovenance.prov.model.MentionOf><any>r).getGeneralEntity();
            }
            if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInformedBy") >= 0)){
                return (<org.openprovenance.prov.model.WasInformedBy><any>r).getInformant();
            }
            if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.ActedOnBehalfOf") >= 0)){
                return (<org.openprovenance.prov.model.ActedOnBehalfOf><any>r).getResponsible();
            }
            if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.DerivedByInsertionFrom") >= 0)){
                return (<org.openprovenance.prov.model.DerivedByInsertionFrom><any>r).getOldDictionary();
            }
            console.info("Unknown relation " + r);
            throw Object.defineProperty(new Error(), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.UnsupportedOperationException','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
        }

        public getOtherCauses(r: org.openprovenance.prov.model.Relation): Array<org.openprovenance.prov.model.QualifiedName> {
            if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasAssociatedWith") >= 0)){
                const res: Array<org.openprovenance.prov.model.QualifiedName> = <any>([]);
                const e: org.openprovenance.prov.model.QualifiedName = (<org.openprovenance.prov.model.WasAssociatedWith><any>r).getPlan();
                if (e == null)return null;
                /* add */(res.push(e)>0);
                return res;
            }
            if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasStartedBy") >= 0)){
                const res: Array<org.openprovenance.prov.model.QualifiedName> = <any>([]);
                const a: org.openprovenance.prov.model.QualifiedName = (<org.openprovenance.prov.model.WasStartedBy><any>r).getStarter();
                if (a == null)return null;
                /* add */(res.push(a)>0);
                return res;
            }
            if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.MentionOf") >= 0)){
                const res: Array<org.openprovenance.prov.model.QualifiedName> = <any>([]);
                const a: org.openprovenance.prov.model.QualifiedName = (<org.openprovenance.prov.model.MentionOf><any>r).getBundle();
                if (a == null)return null;
                /* add */(res.push(a)>0);
                return res;
            }
            if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HadMember") >= 0)){
                const res: Array<org.openprovenance.prov.model.QualifiedName> = <any>([]);
                const entities: Array<org.openprovenance.prov.model.QualifiedName> = (<org.openprovenance.prov.model.HadMember><any>r).getEntity();
                if ((entities == null) || (/* size */(<number>entities.length) <= 1))return null;
                let first: boolean = true;
                for(let index153=0; index153 < entities.length; index153++) {
                    let ee = entities[index153];
                    {
                        if (!first)/* add */(res.push(ee)>0);
                        first = false;
                    }
                }
                return res;
            }
            if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasEndedBy") >= 0)){
                const res: Array<org.openprovenance.prov.model.QualifiedName> = <any>([]);
                const a: org.openprovenance.prov.model.QualifiedName = (<org.openprovenance.prov.model.WasEndedBy><any>r).getEnder();
                if (a == null)return null;
                /* add */(res.push(a)>0);
                return res;
            }
            if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.ActedOnBehalfOf") >= 0)){
                const res: Array<org.openprovenance.prov.model.QualifiedName> = <any>([]);
                const a: org.openprovenance.prov.model.QualifiedName = (<org.openprovenance.prov.model.ActedOnBehalfOf><any>r).getActivity();
                if (a == null)return null;
                /* add */(res.push(a)>0);
                return res;
            }
            if (r != null && (r.constructor != null && r.constructor["__interfaces"] != null && r.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.DerivedByInsertionFrom") >= 0)){
                const res: Array<org.openprovenance.prov.model.QualifiedName> = <any>([]);
                const dbif: org.openprovenance.prov.model.DerivedByInsertionFrom = (<org.openprovenance.prov.model.DerivedByInsertionFrom><any>r);
                {
                    let array155 = dbif.getKeyEntityPair();
                    for(let index154=0; index154 < array155.length; index154++) {
                        let entry = array155[index154];
                        {
                            /* add */(res.push(entry.getEntity())>0);
                        }
                    }
                }
                return res;
            }
            return null;
        }

        public attributesWithNamespace(object: org.openprovenance.prov.model.HasOther, namespace: string): any {
            const ll: Array<org.openprovenance.prov.model.Other> = object.getOther();
            const _attrs: org.openprovenance.prov.model.AttributeProcessor = new org.openprovenance.prov.model.AttributeProcessor(ll);
            return _attrs.attributesWithNamespace(namespace);
        }

        public forAllStatementOrBundle(records: Array<org.openprovenance.prov.model.StatementOrBundle>, action: org.openprovenance.prov.model.StatementAction) {
            for(let index156=0; index156 < records.length; index156++) {
                let o = records[index156];
                {
                    this.doAction$org_openprovenance_prov_model_StatementOrBundle$org_openprovenance_prov_model_StatementAction(o, action);
                }
            }
        }

        public forAllStatement(records: Array<org.openprovenance.prov.model.Statement>, action: org.openprovenance.prov.model.StatementAction) {
            for(let index157=0; index157 < records.length; index157++) {
                let o = records[index157];
                {
                    this.doAction$org_openprovenance_prov_model_StatementOrBundle$org_openprovenance_prov_model_StatementAction(o, action);
                }
            }
        }

        public doAction$org_openprovenance_prov_model_StatementOrBundle$org_openprovenance_prov_model_StatementAction(s: org.openprovenance.prov.model.StatementOrBundle, action: org.openprovenance.prov.model.StatementAction) {
            switch((s.getKind())) {
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_ACTIVITY:
                action['doAction$org_openprovenance_prov_model_Activity'](<org.openprovenance.prov.model.Activity><any>s);
                break;
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_AGENT:
                action['doAction$org_openprovenance_prov_model_Agent'](<org.openprovenance.prov.model.Agent><any>s);
                break;
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_ALTERNATE:
                if (s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.extension.QualifiedAlternateOf") >= 0)){
                    action['doAction$org_openprovenance_prov_model_extension_QualifiedAlternateOf'](<org.openprovenance.prov.model.extension.QualifiedAlternateOf><any>s);
                } else {
                    action['doAction$org_openprovenance_prov_model_AlternateOf'](<org.openprovenance.prov.model.AlternateOf><any>s);
                }
                break;
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_ASSOCIATION:
                action['doAction$org_openprovenance_prov_model_WasAssociatedWith'](<org.openprovenance.prov.model.WasAssociatedWith><any>s);
                break;
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_ATTRIBUTION:
                action['doAction$org_openprovenance_prov_model_WasAttributedTo'](<org.openprovenance.prov.model.WasAttributedTo><any>s);
                break;
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_BUNDLE:
                action['doAction$org_openprovenance_prov_model_Bundle$org_openprovenance_prov_model_ProvUtilities'](<org.openprovenance.prov.model.Bundle><any>s, this);
                break;
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_COMMUNICATION:
                action['doAction$org_openprovenance_prov_model_WasInformedBy'](<org.openprovenance.prov.model.WasInformedBy><any>s);
                break;
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_DELEGATION:
                action['doAction$org_openprovenance_prov_model_ActedOnBehalfOf'](<org.openprovenance.prov.model.ActedOnBehalfOf><any>s);
                break;
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_DERIVATION:
                action['doAction$org_openprovenance_prov_model_WasDerivedFrom'](<org.openprovenance.prov.model.WasDerivedFrom><any>s);
                break;
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_DICTIONARY_INSERTION:
                action['doAction$org_openprovenance_prov_model_DerivedByInsertionFrom'](<org.openprovenance.prov.model.DerivedByInsertionFrom><any>s);
                break;
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_DICTIONARY_MEMBERSHIP:
                action['doAction$org_openprovenance_prov_model_DictionaryMembership'](<org.openprovenance.prov.model.DictionaryMembership><any>s);
                break;
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_DICTIONARY_REMOVAL:
                action['doAction$org_openprovenance_prov_model_DerivedByRemovalFrom'](<org.openprovenance.prov.model.DerivedByRemovalFrom><any>s);
                break;
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_END:
                action['doAction$org_openprovenance_prov_model_WasEndedBy'](<org.openprovenance.prov.model.WasEndedBy><any>s);
                break;
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_ENTITY:
                action['doAction$org_openprovenance_prov_model_Entity'](<org.openprovenance.prov.model.Entity><any>s);
                break;
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_GENERATION:
                action['doAction$org_openprovenance_prov_model_WasGeneratedBy'](<org.openprovenance.prov.model.WasGeneratedBy><any>s);
                break;
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_INFLUENCE:
                action['doAction$org_openprovenance_prov_model_WasInfluencedBy'](<org.openprovenance.prov.model.WasInfluencedBy><any>s);
                break;
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_INVALIDATION:
                action['doAction$org_openprovenance_prov_model_WasInvalidatedBy'](<org.openprovenance.prov.model.WasInvalidatedBy><any>s);
                break;
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_MEMBERSHIP:
                if (s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.extension.QualifiedHadMember") >= 0)){
                    action['doAction$org_openprovenance_prov_model_extension_QualifiedHadMember'](<org.openprovenance.prov.model.extension.QualifiedHadMember><any>s);
                } else {
                    action['doAction$org_openprovenance_prov_model_HadMember'](<org.openprovenance.prov.model.HadMember><any>s);
                }
                break;
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_MENTION:
                action['doAction$org_openprovenance_prov_model_MentionOf'](<org.openprovenance.prov.model.MentionOf><any>s);
                break;
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_SPECIALIZATION:
                if (s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.extension.QualifiedSpecializationOf") >= 0)){
                    action['doAction$org_openprovenance_prov_model_extension_QualifiedSpecializationOf'](<org.openprovenance.prov.model.extension.QualifiedSpecializationOf><any>s);
                } else {
                    action['doAction$org_openprovenance_prov_model_SpecializationOf'](<org.openprovenance.prov.model.SpecializationOf><any>s);
                }
                break;
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_START:
                action['doAction$org_openprovenance_prov_model_WasStartedBy'](<org.openprovenance.prov.model.WasStartedBy><any>s);
                break;
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_USAGE:
                action['doAction$org_openprovenance_prov_model_Used'](<org.openprovenance.prov.model.Used><any>s);
                break;
            }
        }

        public doAction(s?: any, action?: any) {
            if (((s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.StatementOrBundle") >= 0)) || s === null) && ((action != null && (action.constructor != null && action.constructor["__interfaces"] != null && action.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.StatementAction") >= 0)) || action === null)) {
                return <any>this.doAction$org_openprovenance_prov_model_StatementOrBundle$org_openprovenance_prov_model_StatementAction(s, action);
            } else if (((s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.StatementOrBundle") >= 0)) || s === null) && ((action != null && (action.constructor != null && action.constructor["__interfaces"] != null && action.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.StatementActionValue") >= 0)) || action === null)) {
                return <any>this.doAction$org_openprovenance_prov_model_StatementOrBundle$org_openprovenance_prov_model_StatementActionValue(s, action);
            } else throw new Error('invalid overload');
        }

        public doAction$org_openprovenance_prov_model_StatementOrBundle$org_openprovenance_prov_model_StatementActionValue(s: org.openprovenance.prov.model.StatementOrBundle, action: org.openprovenance.prov.model.StatementActionValue): any {
            switch((s.getKind())) {
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_ACTIVITY:
                return action['doAction$org_openprovenance_prov_model_Activity'](<org.openprovenance.prov.model.Activity><any>s);
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_AGENT:
                return action['doAction$org_openprovenance_prov_model_Agent'](<org.openprovenance.prov.model.Agent><any>s);
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_ALTERNATE:
                if (s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.extension.QualifiedAlternateOf") >= 0)){
                    return action['doAction$org_openprovenance_prov_model_extension_QualifiedAlternateOf'](<org.openprovenance.prov.model.extension.QualifiedAlternateOf><any>s);
                } else {
                    return action['doAction$org_openprovenance_prov_model_AlternateOf'](<org.openprovenance.prov.model.AlternateOf><any>s);
                }
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_ASSOCIATION:
                return action['doAction$org_openprovenance_prov_model_WasAssociatedWith'](<org.openprovenance.prov.model.WasAssociatedWith><any>s);
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_ATTRIBUTION:
                return action['doAction$org_openprovenance_prov_model_WasAttributedTo'](<org.openprovenance.prov.model.WasAttributedTo><any>s);
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_BUNDLE:
                return action['doAction$org_openprovenance_prov_model_Bundle$org_openprovenance_prov_model_ProvUtilities'](<org.openprovenance.prov.model.Bundle><any>s, this);
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_COMMUNICATION:
                return action['doAction$org_openprovenance_prov_model_WasInformedBy'](<org.openprovenance.prov.model.WasInformedBy><any>s);
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_DELEGATION:
                return action['doAction$org_openprovenance_prov_model_ActedOnBehalfOf'](<org.openprovenance.prov.model.ActedOnBehalfOf><any>s);
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_DERIVATION:
                return action['doAction$org_openprovenance_prov_model_WasDerivedFrom'](<org.openprovenance.prov.model.WasDerivedFrom><any>s);
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_DICTIONARY_INSERTION:
                return action['doAction$org_openprovenance_prov_model_DerivedByInsertionFrom'](<org.openprovenance.prov.model.DerivedByInsertionFrom><any>s);
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_DICTIONARY_MEMBERSHIP:
                return action['doAction$org_openprovenance_prov_model_DictionaryMembership'](<org.openprovenance.prov.model.DictionaryMembership><any>s);
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_DICTIONARY_REMOVAL:
                return action['doAction$org_openprovenance_prov_model_DerivedByRemovalFrom'](<org.openprovenance.prov.model.DerivedByRemovalFrom><any>s);
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_END:
                return action['doAction$org_openprovenance_prov_model_WasEndedBy'](<org.openprovenance.prov.model.WasEndedBy><any>s);
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_ENTITY:
                return action['doAction$org_openprovenance_prov_model_Entity'](<org.openprovenance.prov.model.Entity><any>s);
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_GENERATION:
                return action['doAction$org_openprovenance_prov_model_WasGeneratedBy'](<org.openprovenance.prov.model.WasGeneratedBy><any>s);
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_INFLUENCE:
                return action['doAction$org_openprovenance_prov_model_WasInfluencedBy'](<org.openprovenance.prov.model.WasInfluencedBy><any>s);
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_INVALIDATION:
                return action['doAction$org_openprovenance_prov_model_WasInvalidatedBy'](<org.openprovenance.prov.model.WasInvalidatedBy><any>s);
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_MEMBERSHIP:
                if (s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.extension.QualifiedHadMember") >= 0)){
                    return action['doAction$org_openprovenance_prov_model_extension_QualifiedHadMember'](<org.openprovenance.prov.model.extension.QualifiedHadMember><any>s);
                } else {
                    return action['doAction$org_openprovenance_prov_model_HadMember'](<org.openprovenance.prov.model.HadMember><any>s);
                }
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_MENTION:
                return action['doAction$org_openprovenance_prov_model_MentionOf'](<org.openprovenance.prov.model.MentionOf><any>s);
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_SPECIALIZATION:
                if (s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.extension.QualifiedSpecializationOf") >= 0)){
                    return action['doAction$org_openprovenance_prov_model_extension_QualifiedSpecializationOf'](<org.openprovenance.prov.model.extension.QualifiedSpecializationOf><any>s);
                } else {
                    return action['doAction$org_openprovenance_prov_model_SpecializationOf'](<org.openprovenance.prov.model.SpecializationOf><any>s);
                }
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_START:
                return action['doAction$org_openprovenance_prov_model_WasStartedBy'](<org.openprovenance.prov.model.WasStartedBy><any>s);
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_USAGE:
                return action['doAction$org_openprovenance_prov_model_Used'](<org.openprovenance.prov.model.Used><any>s);
            default:
                throw new org.openprovenance.prov.model.exception.InvalidCaseException("Statement Kind: " + s.getKind());
            }
        }

        public static unescape(s: string): string {
            return /* replace */s.split("\\\"").join("\"");
        }

        public static valueToNotationString$org_openprovenance_prov_model_Key(key: org.openprovenance.prov.model.Key): string {
            return ProvUtilities.valueToNotationString$java_lang_Object$org_openprovenance_prov_model_QualifiedName(key.getValue(), key.getType());
        }

        public static escape(s: string): string {
            return /* replace *//* replace */s.split("\\").join("\\\\").split("\"").join("\\\"");
        }

        public static internationalizedStringUri: string; public static internationalizedStringUri_$LI$(): string { if (ProvUtilities.internationalizedStringUri == null) { ProvUtilities.internationalizedStringUri = org.openprovenance.prov.model.NamespacePrefixMapper.PROV_NS + "InternationalizedString"; }  return ProvUtilities.internationalizedStringUri; }

        public static valueToNotationString$java_lang_Object$org_openprovenance_prov_model_QualifiedName(val: any, xsdType: org.openprovenance.prov.model.QualifiedName): string {
            if (val != null && (val.constructor != null && val.constructor["__interfaces"] != null && val.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.LangString") >= 0)){
                const istring: org.openprovenance.prov.model.LangString = <org.openprovenance.prov.model.LangString><any>val;
                return "\"" + ProvUtilities.escape(istring.getValue()) + ((istring.getLang() == null) ? "\"" : "\"@" + istring.getLang()) + (((xsdType == null) || (xsdType.getUri() === ProvUtilities.internationalizedStringUri_$LI$())) ? "" : " %% " + org.openprovenance.prov.model.Namespace.qualifiedNameToStringWithNamespace(xsdType));
            } else if (val != null && (val.constructor != null && val.constructor["__interfaces"] != null && val.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)){
                const qn: org.openprovenance.prov.model.QualifiedName = <org.openprovenance.prov.model.QualifiedName><any>val;
                return "\'" + org.openprovenance.prov.model.Namespace.qualifiedNameToStringWithNamespace(qn) + "\'";
            } else if (typeof val === 'string'){
                const s: string = <string>val;
                if (/* contains */(s.indexOf("\n") != -1)){
                    return "\"\"\"" + ProvUtilities.escape(s) + "\"\"\"";
                } else {
                    return "\"" + ProvUtilities.escape(s) + ((xsdType == null) ? "\"" : "\" %% " + org.openprovenance.prov.model.Namespace.qualifiedNameToStringWithNamespace(xsdType));
                }
            } else {
                return "\"" + val + "\" %% " + org.openprovenance.prov.model.Namespace.qualifiedNameToStringWithNamespace(xsdType);
            }
        }

        public static valueToNotationString(val?: any, xsdType?: any): any {
            if (((val != null) || val === null) && ((xsdType != null && (xsdType.constructor != null && xsdType.constructor["__interfaces"] != null && xsdType.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || xsdType === null)) {
                return <any>org.openprovenance.prov.model.ProvUtilities.valueToNotationString$java_lang_Object$org_openprovenance_prov_model_QualifiedName(val, xsdType);
            } else if (((val != null && (val.constructor != null && val.constructor["__interfaces"] != null && val.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Key") >= 0)) || val === null) && xsdType === undefined) {
                return <any>org.openprovenance.prov.model.ProvUtilities.valueToNotationString$org_openprovenance_prov_model_Key(val);
            } else throw new Error('invalid overload');
        }

        public static hasType(type: org.openprovenance.prov.model.QualifiedName, attributes: Array<org.openprovenance.prov.model.Attribute>): boolean {
            for(let index158=0; index158 < attributes.length; index158++) {
                let attribute = attributes[index158];
                {
                    switch((attribute.getKind())) {
                    case org.openprovenance.prov.model.Attribute.AttributeKind.PROV_TYPE:
                        if (/* equals */(<any>((o1: any, o2: any) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(attribute.getValue(),type))){
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

        public getter(s: org.openprovenance.prov.model.Statement, i: number): any {
            const kind: org.openprovenance.prov.model.StatementOrBundle.Kind = s.getKind();
            switch((kind)) {
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_ACTIVITY:
                {
                    const a: org.openprovenance.prov.model.Activity = <org.openprovenance.prov.model.Activity><any>s;
                    switch((i)) {
                    case 0:
                        return a.getId();
                    case 1:
                        return a.getStartTime();
                    case 2:
                        return a.getEndTime();
                    case 3:
                        return a.getOther();
                    default:
                        throw Object.defineProperty(new Error("ProvUtilities.getter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.Object','java.lang.ArrayIndexOutOfBoundsException','java.lang.RuntimeException','java.lang.Exception'] });
                    }
                };
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_AGENT:
                {
                    const a: org.openprovenance.prov.model.Agent = <org.openprovenance.prov.model.Agent><any>s;
                    switch((i)) {
                    case 0:
                        return a.getId();
                    case 1:
                        return a.getOther();
                    default:
                        throw Object.defineProperty(new Error("ProvUtilities.getter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.Object','java.lang.ArrayIndexOutOfBoundsException','java.lang.RuntimeException','java.lang.Exception'] });
                    }
                };
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_ALTERNATE:
                {
                    const a: org.openprovenance.prov.model.AlternateOf = <org.openprovenance.prov.model.AlternateOf><any>s;
                    switch((i)) {
                    case 0:
                        return a.getAlternate1();
                    case 1:
                        return a.getAlternate2();
                    default:
                        throw Object.defineProperty(new Error("ProvUtilities.getter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.Object','java.lang.ArrayIndexOutOfBoundsException','java.lang.RuntimeException','java.lang.Exception'] });
                    }
                };
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_ASSOCIATION:
                {
                    const a: org.openprovenance.prov.model.WasAssociatedWith = <org.openprovenance.prov.model.WasAssociatedWith><any>s;
                    switch((i)) {
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
                        throw Object.defineProperty(new Error("ProvUtilities.getter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.Object','java.lang.ArrayIndexOutOfBoundsException','java.lang.RuntimeException','java.lang.Exception'] });
                    }
                };
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_ATTRIBUTION:
                {
                    const a: org.openprovenance.prov.model.WasAttributedTo = <org.openprovenance.prov.model.WasAttributedTo><any>s;
                    switch((i)) {
                    case 0:
                        return a.getId();
                    case 1:
                        return a.getEntity();
                    case 2:
                        return a.getAgent();
                    case 3:
                        return a.getOther();
                    default:
                        throw Object.defineProperty(new Error("ProvUtilities.getter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.Object','java.lang.ArrayIndexOutOfBoundsException','java.lang.RuntimeException','java.lang.Exception'] });
                    }
                };
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_BUNDLE:
                throw new org.openprovenance.prov.model.exception.InvalidCaseException("ProvUtilities.getter() for " + kind);
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_COMMUNICATION:
                {
                    const a: org.openprovenance.prov.model.WasInformedBy = <org.openprovenance.prov.model.WasInformedBy><any>s;
                    switch((i)) {
                    case 0:
                        return a.getId();
                    case 1:
                        return a.getInformed();
                    case 2:
                        return a.getInformant();
                    case 3:
                        return a.getOther();
                    default:
                        throw Object.defineProperty(new Error("ProvUtilities.getter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.Object','java.lang.ArrayIndexOutOfBoundsException','java.lang.RuntimeException','java.lang.Exception'] });
                    }
                };
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_DELEGATION:
                {
                    const a: org.openprovenance.prov.model.ActedOnBehalfOf = <org.openprovenance.prov.model.ActedOnBehalfOf><any>s;
                    switch((i)) {
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
                        throw Object.defineProperty(new Error("ProvUtilities.getter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.Object','java.lang.ArrayIndexOutOfBoundsException','java.lang.RuntimeException','java.lang.Exception'] });
                    }
                };
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_DERIVATION:
                {
                    const a: org.openprovenance.prov.model.WasDerivedFrom = <org.openprovenance.prov.model.WasDerivedFrom><any>s;
                    switch((i)) {
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
                        throw Object.defineProperty(new Error("ProvUtilities.getter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.Object','java.lang.ArrayIndexOutOfBoundsException','java.lang.RuntimeException','java.lang.Exception'] });
                    }
                };
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_DICTIONARY_INSERTION:
                throw new org.openprovenance.prov.model.exception.InvalidCaseException("ProvUtilities.getter() for " + kind);
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_DICTIONARY_MEMBERSHIP:
                throw new org.openprovenance.prov.model.exception.InvalidCaseException("ProvUtilities.getter() for " + kind);
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_DICTIONARY_REMOVAL:
                throw new org.openprovenance.prov.model.exception.InvalidCaseException("ProvUtilities.getter() for " + kind);
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_END:
                {
                    const a: org.openprovenance.prov.model.WasEndedBy = <org.openprovenance.prov.model.WasEndedBy><any>s;
                    switch((i)) {
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
                        throw Object.defineProperty(new Error("ProvUtilities.getter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.Object','java.lang.ArrayIndexOutOfBoundsException','java.lang.RuntimeException','java.lang.Exception'] });
                    }
                };
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_ENTITY:
                {
                    const a: org.openprovenance.prov.model.Entity = <org.openprovenance.prov.model.Entity><any>s;
                    switch((i)) {
                    case 0:
                        return a.getId();
                    case 1:
                        return a.getOther();
                    default:
                        throw Object.defineProperty(new Error("ProvUtilities.getter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.Object','java.lang.ArrayIndexOutOfBoundsException','java.lang.RuntimeException','java.lang.Exception'] });
                    }
                };
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_GENERATION:
                {
                    const a: org.openprovenance.prov.model.WasGeneratedBy = <org.openprovenance.prov.model.WasGeneratedBy><any>s;
                    switch((i)) {
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
                        throw Object.defineProperty(new Error("ProvUtilities.getter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.Object','java.lang.ArrayIndexOutOfBoundsException','java.lang.RuntimeException','java.lang.Exception'] });
                    }
                };
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_INFLUENCE:
                {
                    const a: org.openprovenance.prov.model.WasInfluencedBy = <org.openprovenance.prov.model.WasInfluencedBy><any>s;
                    switch((i)) {
                    case 0:
                        return a.getId();
                    case 1:
                        return a.getInfluencee();
                    case 2:
                        return a.getInfluencer();
                    case 3:
                        return a.getOther();
                    default:
                        throw Object.defineProperty(new Error("ProvUtilities.getter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.Object','java.lang.ArrayIndexOutOfBoundsException','java.lang.RuntimeException','java.lang.Exception'] });
                    }
                };
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_INVALIDATION:
                {
                    const a: org.openprovenance.prov.model.WasInvalidatedBy = <org.openprovenance.prov.model.WasInvalidatedBy><any>s;
                    switch((i)) {
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
                        throw Object.defineProperty(new Error("ProvUtilities.getter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.Object','java.lang.ArrayIndexOutOfBoundsException','java.lang.RuntimeException','java.lang.Exception'] });
                    }
                };
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_MEMBERSHIP:
                {
                    const a: org.openprovenance.prov.model.HadMember = <org.openprovenance.prov.model.HadMember><any>s;
                    switch((i)) {
                    case 0:
                        return a.getCollection();
                    case 1:
                        return a.getEntity();
                    default:
                        throw Object.defineProperty(new Error("ProvUtilities.getter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.Object','java.lang.ArrayIndexOutOfBoundsException','java.lang.RuntimeException','java.lang.Exception'] });
                    }
                };
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_MENTION:
                {
                    const a: org.openprovenance.prov.model.MentionOf = <org.openprovenance.prov.model.MentionOf><any>s;
                    switch((i)) {
                    case 0:
                        throw Object.defineProperty(new Error("ProvUtilities.getter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.Object','java.lang.ArrayIndexOutOfBoundsException','java.lang.RuntimeException','java.lang.Exception'] });
                    case 1:
                        return a.getSpecificEntity();
                    case 2:
                        return a.getGeneralEntity();
                    case 3:
                        return a.getBundle();
                    default:
                        throw Object.defineProperty(new Error("ProvUtilities.getter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.Object','java.lang.ArrayIndexOutOfBoundsException','java.lang.RuntimeException','java.lang.Exception'] });
                    }
                };
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_SPECIALIZATION:
                {
                    const a: org.openprovenance.prov.model.SpecializationOf = <org.openprovenance.prov.model.SpecializationOf><any>s;
                    switch((i)) {
                    case 0:
                        return a.getSpecificEntity();
                    case 1:
                        return a.getGeneralEntity();
                    default:
                        throw Object.defineProperty(new Error("ProvUtilities.getter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.Object','java.lang.ArrayIndexOutOfBoundsException','java.lang.RuntimeException','java.lang.Exception'] });
                    }
                };
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_START:
                {
                    const a: org.openprovenance.prov.model.WasStartedBy = <org.openprovenance.prov.model.WasStartedBy><any>s;
                    switch((i)) {
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
                        throw Object.defineProperty(new Error("ProvUtilities.getter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.Object','java.lang.ArrayIndexOutOfBoundsException','java.lang.RuntimeException','java.lang.Exception'] });
                    }
                };
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_USAGE:
                {
                    const a: org.openprovenance.prov.model.Used = <org.openprovenance.prov.model.Used><any>s;
                    switch((i)) {
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
                        throw Object.defineProperty(new Error("ProvUtilities.getter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.Object','java.lang.ArrayIndexOutOfBoundsException','java.lang.RuntimeException','java.lang.Exception'] });
                    }
                };
            default:
                throw new org.openprovenance.prov.model.exception.InvalidCaseException("ProvUtilities.getter() for " + kind);
            }
        }

        public setter(s: org.openprovenance.prov.model.Statement, i: number, val: any) {
            const kind: org.openprovenance.prov.model.StatementOrBundle.Kind = s.getKind();
            switch((kind)) {
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_ACTIVITY:
                {
                    const a: org.openprovenance.prov.model.Activity = <org.openprovenance.prov.model.Activity><any>s;
                    switch((i)) {
                    case 0:
                        a.setId(<org.openprovenance.prov.model.QualifiedName><any>val);
                        return;
                    case 1:
                        a.setStartTime(<javax.xml.datatype.XMLGregorianCalendar>val);
                        return;
                    case 2:
                        a.setEndTime(<javax.xml.datatype.XMLGregorianCalendar>val);
                        return;
                    default:
                        throw Object.defineProperty(new Error("ProvUtilities.setter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.Object','java.lang.ArrayIndexOutOfBoundsException','java.lang.RuntimeException','java.lang.Exception'] });
                    }
                };
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_AGENT:
                {
                    const a: org.openprovenance.prov.model.Agent = <org.openprovenance.prov.model.Agent><any>s;
                    switch((i)) {
                    case 0:
                        a.setId(<org.openprovenance.prov.model.QualifiedName><any>val);
                        return;
                    default:
                        throw Object.defineProperty(new Error("ProvUtilities.setter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.Object','java.lang.ArrayIndexOutOfBoundsException','java.lang.RuntimeException','java.lang.Exception'] });
                    }
                };
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_ALTERNATE:
                {
                    const a: org.openprovenance.prov.model.AlternateOf = <org.openprovenance.prov.model.AlternateOf><any>s;
                    switch((i)) {
                    case 0:
                        a.setAlternate1(<org.openprovenance.prov.model.QualifiedName><any>val);
                        return;
                    case 1:
                        a.setAlternate2(<org.openprovenance.prov.model.QualifiedName><any>val);
                        return;
                    default:
                        throw Object.defineProperty(new Error("ProvUtilities.setter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.Object','java.lang.ArrayIndexOutOfBoundsException','java.lang.RuntimeException','java.lang.Exception'] });
                    }
                };
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_ASSOCIATION:
                {
                    const a: org.openprovenance.prov.model.WasAssociatedWith = <org.openprovenance.prov.model.WasAssociatedWith><any>s;
                    switch((i)) {
                    case 0:
                        a.setId(<org.openprovenance.prov.model.QualifiedName><any>val);
                        return;
                    case 1:
                        a.setActivity(<org.openprovenance.prov.model.QualifiedName><any>val);
                        return;
                    case 2:
                        a.setAgent(<org.openprovenance.prov.model.QualifiedName><any>val);
                        return;
                    case 3:
                        a.setPlan(<org.openprovenance.prov.model.QualifiedName><any>val);
                        return;
                    default:
                        throw Object.defineProperty(new Error("ProvUtilities.setter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.Object','java.lang.ArrayIndexOutOfBoundsException','java.lang.RuntimeException','java.lang.Exception'] });
                    }
                };
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_ATTRIBUTION:
                {
                    const a: org.openprovenance.prov.model.WasAttributedTo = <org.openprovenance.prov.model.WasAttributedTo><any>s;
                    switch((i)) {
                    case 0:
                        a.setId(<org.openprovenance.prov.model.QualifiedName><any>val);
                        return;
                    case 1:
                        a.setEntity(<org.openprovenance.prov.model.QualifiedName><any>val);
                        return;
                    case 2:
                        a.setAgent(<org.openprovenance.prov.model.QualifiedName><any>val);
                        return;
                    default:
                        throw Object.defineProperty(new Error("ProvUtilities.setter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.Object','java.lang.ArrayIndexOutOfBoundsException','java.lang.RuntimeException','java.lang.Exception'] });
                    }
                };
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_BUNDLE:
                throw new org.openprovenance.prov.model.exception.InvalidCaseException("ProvUtilities.setter() for " + kind);
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_COMMUNICATION:
                {
                    const a: org.openprovenance.prov.model.WasInformedBy = <org.openprovenance.prov.model.WasInformedBy><any>s;
                    switch((i)) {
                    case 0:
                        a.setId(<org.openprovenance.prov.model.QualifiedName><any>val);
                        return;
                    case 1:
                        a.setInformed(<org.openprovenance.prov.model.QualifiedName><any>val);
                        return;
                    case 2:
                        a.setInformant(<org.openprovenance.prov.model.QualifiedName><any>val);
                        return;
                    default:
                        throw Object.defineProperty(new Error("ProvUtilities.setter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.Object','java.lang.ArrayIndexOutOfBoundsException','java.lang.RuntimeException','java.lang.Exception'] });
                    }
                };
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_DELEGATION:
                {
                    const a: org.openprovenance.prov.model.ActedOnBehalfOf = <org.openprovenance.prov.model.ActedOnBehalfOf><any>s;
                    switch((i)) {
                    case 0:
                        a.setId(<org.openprovenance.prov.model.QualifiedName><any>val);
                        return;
                    case 1:
                        a.setDelegate(<org.openprovenance.prov.model.QualifiedName><any>val);
                        return;
                    case 2:
                        a.setResponsible(<org.openprovenance.prov.model.QualifiedName><any>val);
                        return;
                    case 3:
                        a.setActivity(<org.openprovenance.prov.model.QualifiedName><any>val);
                        return;
                    default:
                        throw Object.defineProperty(new Error("ProvUtilities.setter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.Object','java.lang.ArrayIndexOutOfBoundsException','java.lang.RuntimeException','java.lang.Exception'] });
                    }
                };
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_DERIVATION:
                {
                    const a: org.openprovenance.prov.model.WasDerivedFrom = <org.openprovenance.prov.model.WasDerivedFrom><any>s;
                    switch((i)) {
                    case 0:
                        a.setId(<org.openprovenance.prov.model.QualifiedName><any>val);
                        return;
                    case 1:
                        a.setGeneratedEntity(<org.openprovenance.prov.model.QualifiedName><any>val);
                        return;
                    case 2:
                        a.setUsedEntity(<org.openprovenance.prov.model.QualifiedName><any>val);
                        return;
                    case 3:
                        a.setActivity(<org.openprovenance.prov.model.QualifiedName><any>val);
                        return;
                    case 4:
                        a.setGeneration(<org.openprovenance.prov.model.QualifiedName><any>val);
                        return;
                    case 5:
                        a.setUsage(<org.openprovenance.prov.model.QualifiedName><any>val);
                        return;
                    default:
                        throw Object.defineProperty(new Error("ProvUtilities.setter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.Object','java.lang.ArrayIndexOutOfBoundsException','java.lang.RuntimeException','java.lang.Exception'] });
                    }
                };
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_DICTIONARY_INSERTION:
                throw new org.openprovenance.prov.model.exception.InvalidCaseException("ProvUtilities.setter() for " + kind);
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_DICTIONARY_MEMBERSHIP:
                throw new org.openprovenance.prov.model.exception.InvalidCaseException("ProvUtilities.setter() for " + kind);
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_DICTIONARY_REMOVAL:
                throw new org.openprovenance.prov.model.exception.InvalidCaseException("ProvUtilities.setter() for " + kind);
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_END:
                {
                    const a: org.openprovenance.prov.model.WasEndedBy = <org.openprovenance.prov.model.WasEndedBy><any>s;
                    switch((i)) {
                    case 0:
                        a.setId(<org.openprovenance.prov.model.QualifiedName><any>val);
                        return;
                    case 1:
                        a.setActivity(<org.openprovenance.prov.model.QualifiedName><any>val);
                        return;
                    case 2:
                        a.setTrigger(<org.openprovenance.prov.model.QualifiedName><any>val);
                        return;
                    case 3:
                        a.setEnder(<org.openprovenance.prov.model.QualifiedName><any>val);
                        return;
                    case 4:
                        a.setTime(<javax.xml.datatype.XMLGregorianCalendar>val);
                        return;
                    default:
                        throw Object.defineProperty(new Error("ProvUtilities.setter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.Object','java.lang.ArrayIndexOutOfBoundsException','java.lang.RuntimeException','java.lang.Exception'] });
                    }
                };
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_ENTITY:
                {
                    const a: org.openprovenance.prov.model.Entity = <org.openprovenance.prov.model.Entity><any>s;
                    switch((i)) {
                    case 0:
                        a.setId(<org.openprovenance.prov.model.QualifiedName><any>val);
                        return;
                    default:
                        throw Object.defineProperty(new Error("ProvUtilities.setter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.Object','java.lang.ArrayIndexOutOfBoundsException','java.lang.RuntimeException','java.lang.Exception'] });
                    }
                };
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_GENERATION:
                {
                    const a: org.openprovenance.prov.model.WasGeneratedBy = <org.openprovenance.prov.model.WasGeneratedBy><any>s;
                    switch((i)) {
                    case 0:
                        a.setId(<org.openprovenance.prov.model.QualifiedName><any>val);
                        return;
                    case 1:
                        a.setEntity(<org.openprovenance.prov.model.QualifiedName><any>val);
                        return;
                    case 2:
                        a.setActivity(<org.openprovenance.prov.model.QualifiedName><any>val);
                        return;
                    case 3:
                        a.setTime(<javax.xml.datatype.XMLGregorianCalendar>val);
                        return;
                    default:
                        throw Object.defineProperty(new Error("ProvUtilities.setter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.Object','java.lang.ArrayIndexOutOfBoundsException','java.lang.RuntimeException','java.lang.Exception'] });
                    }
                };
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_INFLUENCE:
                {
                    const a: org.openprovenance.prov.model.WasInfluencedBy = <org.openprovenance.prov.model.WasInfluencedBy><any>s;
                    switch((i)) {
                    case 0:
                        a.setId(<org.openprovenance.prov.model.QualifiedName><any>val);
                        return;
                    case 1:
                        a.setInfluencee(<org.openprovenance.prov.model.QualifiedName><any>val);
                        return;
                    case 2:
                        a.setInfluencer(<org.openprovenance.prov.model.QualifiedName><any>val);
                        return;
                    default:
                        throw Object.defineProperty(new Error("ProvUtilities.setter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.Object','java.lang.ArrayIndexOutOfBoundsException','java.lang.RuntimeException','java.lang.Exception'] });
                    }
                };
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_INVALIDATION:
                {
                    const a: org.openprovenance.prov.model.WasInvalidatedBy = <org.openprovenance.prov.model.WasInvalidatedBy><any>s;
                    switch((i)) {
                    case 0:
                        a.setId(<org.openprovenance.prov.model.QualifiedName><any>val);
                        return;
                    case 1:
                        a.setEntity(<org.openprovenance.prov.model.QualifiedName><any>val);
                        return;
                    case 2:
                        a.setActivity(<org.openprovenance.prov.model.QualifiedName><any>val);
                        return;
                    case 3:
                        a.setTime(<javax.xml.datatype.XMLGregorianCalendar>val);
                        return;
                    default:
                        throw Object.defineProperty(new Error("ProvUtilities.setter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.Object','java.lang.ArrayIndexOutOfBoundsException','java.lang.RuntimeException','java.lang.Exception'] });
                    }
                };
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_MEMBERSHIP:
                {
                    const a: org.openprovenance.prov.model.HadMember = <org.openprovenance.prov.model.HadMember><any>s;
                    switch((i)) {
                    case 0:
                        a.setCollection(<org.openprovenance.prov.model.QualifiedName><any>val);
                        return;
                    case 1:
                        /* remove */a.getEntity().splice(0, 1)[0];
                        /* add */(a.getEntity().push(<org.openprovenance.prov.model.QualifiedName><any>val)>0);
                        return;
                    default:
                        throw Object.defineProperty(new Error("ProvUtilities.setter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.Object','java.lang.ArrayIndexOutOfBoundsException','java.lang.RuntimeException','java.lang.Exception'] });
                    }
                };
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_MENTION:
                {
                    const a: org.openprovenance.prov.model.MentionOf = <org.openprovenance.prov.model.MentionOf><any>s;
                    switch((i)) {
                    case 0:
                        throw Object.defineProperty(new Error("ProvUtilities.setter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.Object','java.lang.ArrayIndexOutOfBoundsException','java.lang.RuntimeException','java.lang.Exception'] });
                    case 1:
                        a.setSpecificEntity(<org.openprovenance.prov.model.QualifiedName><any>val);
                        return;
                    case 2:
                        a.setGeneralEntity(<org.openprovenance.prov.model.QualifiedName><any>val);
                        return;
                    case 3:
                        a.setBundle(<org.openprovenance.prov.model.QualifiedName><any>val);
                        return;
                    default:
                        throw Object.defineProperty(new Error("ProvUtilities.setter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.Object','java.lang.ArrayIndexOutOfBoundsException','java.lang.RuntimeException','java.lang.Exception'] });
                    }
                };
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_SPECIALIZATION:
                {
                    const a: org.openprovenance.prov.model.SpecializationOf = <org.openprovenance.prov.model.SpecializationOf><any>s;
                    switch((i)) {
                    case 0:
                        a.setSpecificEntity(<org.openprovenance.prov.model.QualifiedName><any>val);
                        return;
                    case 1:
                        a.setGeneralEntity(<org.openprovenance.prov.model.QualifiedName><any>val);
                        return;
                    default:
                        throw Object.defineProperty(new Error("ProvUtilities.setter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.Object','java.lang.ArrayIndexOutOfBoundsException','java.lang.RuntimeException','java.lang.Exception'] });
                    }
                };
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_START:
                {
                    const a: org.openprovenance.prov.model.WasStartedBy = <org.openprovenance.prov.model.WasStartedBy><any>s;
                    switch((i)) {
                    case 0:
                        a.setId(<org.openprovenance.prov.model.QualifiedName><any>val);
                        return;
                    case 1:
                        a.setActivity(<org.openprovenance.prov.model.QualifiedName><any>val);
                        return;
                    case 2:
                        a.setTrigger(<org.openprovenance.prov.model.QualifiedName><any>val);
                        return;
                    case 3:
                        a.setStarter(<org.openprovenance.prov.model.QualifiedName><any>val);
                        return;
                    case 4:
                        a.setTime(<javax.xml.datatype.XMLGregorianCalendar>val);
                        return;
                    default:
                        throw Object.defineProperty(new Error("ProvUtilities.setter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.Object','java.lang.ArrayIndexOutOfBoundsException','java.lang.RuntimeException','java.lang.Exception'] });
                    }
                };
            case org.openprovenance.prov.model.StatementOrBundle.Kind.PROV_USAGE:
                {
                    const a: org.openprovenance.prov.model.Used = <org.openprovenance.prov.model.Used><any>s;
                    switch((i)) {
                    case 0:
                        a.setId(<org.openprovenance.prov.model.QualifiedName><any>val);
                        return;
                    case 1:
                        a.setActivity(<org.openprovenance.prov.model.QualifiedName><any>val);
                        return;
                    case 2:
                        a.setEntity(<org.openprovenance.prov.model.QualifiedName><any>val);
                        return;
                    case 3:
                        a.setTime(<javax.xml.datatype.XMLGregorianCalendar>val);
                        return;
                    default:
                        throw Object.defineProperty(new Error("ProvUtilities.setter() for " + kind + " and index " + i), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.Object','java.lang.ArrayIndexOutOfBoundsException','java.lang.RuntimeException','java.lang.Exception'] });
                    }
                };
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
        public hasNoTime(o: org.openprovenance.prov.model.Statement): boolean {
            if (o != null && (o.constructor != null && o.constructor["__interfaces"] != null && o.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HasTime") >= 0))return false;
            if (o != null && (o.constructor != null && o.constructor["__interfaces"] != null && o.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Activity") >= 0))return false;
            return true;
        }

        public static toXMLGregorianCalendar(date: Date): javax.xml.datatype.XMLGregorianCalendar {
            if (date == null)return null;
            const gCalendar: Date = new Date();
            /* setTime */gCalendar.setTime(date.getTime());
            let xmlCalendar: javax.xml.datatype.XMLGregorianCalendar = null;
            try {
                xmlCalendar = javax.xml.datatype.DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar);
            } catch(ex) {
                console.error(ex.message, ex);
                throw new org.openprovenance.prov.model.exception.UncheckedException(ex);
            }
            return xmlCalendar;
        }

        public static toDate(calendar: javax.xml.datatype.XMLGregorianCalendar): Date {
            if (calendar == null){
                return null;
            }
            return /* getTime */(new Date(calendar.toGregorianCalendar().getTime()));
        }

        /**
         * After reading/constructing a document, this method should be called to ensure that Namespaces are properly chained.
         * @param {*} document a {@link Document} to update
         */
        public updateNamespaces(document: org.openprovenance.prov.model.Document) {
            const rootNamespace: org.openprovenance.prov.model.Namespace = org.openprovenance.prov.model.Namespace.gatherNamespaces$org_openprovenance_prov_model_Document(document);
            document.setNamespace(rootNamespace);
            {
                let array160 = this.getBundle(document);
                for(let index159=0; index159 < array160.length; index159++) {
                    let bu = array160[index159];
                    {
                        let ns: org.openprovenance.prov.model.Namespace = bu.getNamespace();
                        if (ns != null){
                            ns.setParent(rootNamespace);
                        } else {
                            ns = new org.openprovenance.prov.model.Namespace();
                            ns.setParent(rootNamespace);
                            bu.setNamespace(ns);
                        }
                    }
                }
            }
        }
    }
    ProvUtilities["__class"] = "org.openprovenance.prov.model.ProvUtilities";


    export namespace ProvUtilities {

        export enum BuildFlag {
            NOCHEK, WARN, STRICT
        }
    }

}

