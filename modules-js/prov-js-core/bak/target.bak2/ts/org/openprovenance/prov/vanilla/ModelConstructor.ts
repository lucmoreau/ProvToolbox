/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.vanilla {
    export class ModelConstructor implements org.openprovenance.prov.model.ModelConstructor, org.openprovenance.prov.model.AtomConstructor {
        /**
         * A factory method to create an instance of a delegation {@link ActedOnBehalfOf}
         * 
         * @param {*} id          identifier for the delegation association between delegate and responsible
         * @param {*} delegate    identifier for the agent associated with an activity, acting on behalf of the responsible agent
         * @param {*} responsible identifier for the agent, on behalf of which the delegate agent acted
         * @param {*} activity    optional identifier of an activity for which the delegation association holds
         * @param {*[]} attributes  optional set  of attributes representing additional information about this delegation association
         * @return {*} an instance of {@link ActedOnBehalfOf}
         */
        public newActedOnBehalfOf(id: org.openprovenance.prov.model.QualifiedName, delegate: org.openprovenance.prov.model.QualifiedName, responsible: org.openprovenance.prov.model.QualifiedName, activity: org.openprovenance.prov.model.QualifiedName, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.ActedOnBehalfOf {
            return new org.openprovenance.prov.vanilla.ActedOnBehalfOf(id, delegate, responsible, activity, attributes);
        }

        /**
         * 
         * @param {*} id
         * @param {javax.xml.datatype.XMLGregorianCalendar} startTime
         * @param {javax.xml.datatype.XMLGregorianCalendar} endTime
         * @param {*[]} attributes
         * @return {*}
         */
        public newActivity(id: org.openprovenance.prov.model.QualifiedName, startTime: javax.xml.datatype.XMLGregorianCalendar, endTime: javax.xml.datatype.XMLGregorianCalendar, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.Activity {
            return new org.openprovenance.prov.vanilla.Activity(id, startTime, endTime, attributes);
        }

        /**
         * 
         * @param {*} id
         * @param {*[]} attributes
         * @return {*}
         */
        public newAgent(id: org.openprovenance.prov.model.QualifiedName, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.Agent {
            return new org.openprovenance.prov.vanilla.Agent(id, attributes);
        }

        /**
         * 
         * @param {*} e1
         * @param {*} e2
         * @return {*}
         */
        public newAlternateOf(e1: org.openprovenance.prov.model.QualifiedName, e2: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.AlternateOf {
            return new org.openprovenance.prov.vanilla.AlternateOf(e1, e2);
        }

        /**
         * 
         * @param {*} id
         * @param {*} after
         * @param {*} before
         * @param {*[]} keyEntitySet
         * @param {*[]} attributes
         * @return {*}
         */
        public newDerivedByInsertionFrom(id: org.openprovenance.prov.model.QualifiedName, after: org.openprovenance.prov.model.QualifiedName, before: org.openprovenance.prov.model.QualifiedName, keyEntitySet: Array<org.openprovenance.prov.model.Entry>, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.DerivedByInsertionFrom {
            throw Object.defineProperty(new Error(), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.UnsupportedOperationException','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
        }

        /**
         * 
         * @param {*} id
         * @param {*} after
         * @param {*} before
         * @param {*[]} keys
         * @param {*[]} attributes
         * @return {*}
         */
        public newDerivedByRemovalFrom(id: org.openprovenance.prov.model.QualifiedName, after: org.openprovenance.prov.model.QualifiedName, before: org.openprovenance.prov.model.QualifiedName, keys: Array<org.openprovenance.prov.model.Key>, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.DerivedByRemovalFrom {
            throw Object.defineProperty(new Error(), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.UnsupportedOperationException','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
        }

        /**
         * 
         * @param {*} dict
         * @param {*[]} keyEntitySet
         * @return {*}
         */
        public newDictionaryMembership(dict: org.openprovenance.prov.model.QualifiedName, keyEntitySet: Array<org.openprovenance.prov.model.Entry>): org.openprovenance.prov.model.DictionaryMembership {
            throw Object.defineProperty(new Error(), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.UnsupportedOperationException','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
        }

        /**
         * A factory method to create an instance of a {@link Document}
         * 
         * @param {org.openprovenance.prov.model.Namespace} namespace  the prefix namespace mapping for the current document
         * @param {*[]} statements a collection of statements
         * @param {*[]} bundles    a collection of bundles
         * @return {*} an instance of {@link Document}, with this prefix-namespace mapping, statements, and bundles
         */
        public newDocument(namespace: org.openprovenance.prov.model.Namespace, statements: Array<org.openprovenance.prov.model.Statement>, bundles: Array<org.openprovenance.prov.model.Bundle>): org.openprovenance.prov.model.Document {
            return new org.openprovenance.prov.vanilla.Document(namespace, statements, bundles);
        }

        /**
         * 
         * @param {*} id
         * @param {*[]} attributes
         * @return {*}
         */
        public newEntity(id: org.openprovenance.prov.model.QualifiedName, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.Entity {
            return new org.openprovenance.prov.vanilla.Entity(id, attributes);
        }

        /**
         * 
         * @param {*} c
         * @param {*[]} e
         * @return {*}
         */
        public newHadMember(c: org.openprovenance.prov.model.QualifiedName, e: Array<org.openprovenance.prov.model.QualifiedName>): org.openprovenance.prov.model.HadMember {
            const ll: Array<org.openprovenance.prov.model.QualifiedName> = <any>([]);
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(ll, e);
            return new org.openprovenance.prov.vanilla.HadMember(c, ll);
        }

        /**
         * 
         * @param {*} e2
         * @param {*} e1
         * @param {*} b
         * @return {*}
         */
        public newMentionOf(e2: org.openprovenance.prov.model.QualifiedName, e1: org.openprovenance.prov.model.QualifiedName, b: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.MentionOf {
            throw Object.defineProperty(new Error(), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.UnsupportedOperationException','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
        }

        /**
         * A factory method to create an instance of a Bundle {@link Bundle}
         * 
         * @param {*} id         an identifier for the bundle
         * @param {org.openprovenance.prov.model.Namespace} namespace  a {@link Namespace} object mapping prefix to namespace URIs
         * @param {*[]} statements a set of provenance descriptions
         * @return {org.openprovenance.prov.vanilla.Bundle} {@link Bundle}
         */
        public newNamedBundle(id: org.openprovenance.prov.model.QualifiedName, namespace: org.openprovenance.prov.model.Namespace, statements: Array<org.openprovenance.prov.model.Statement>): org.openprovenance.prov.vanilla.Bundle {
            return new org.openprovenance.prov.vanilla.Bundle(id, namespace, statements);
        }

        public newQualifiedName$java_lang_String$java_lang_String$java_lang_String(namespace: string, local: string, prefix: string): org.openprovenance.prov.model.QualifiedName {
            return new org.openprovenance.prov.vanilla.QualifiedName(namespace, local, prefix);
        }

        public newQualifiedName$java_lang_String$java_lang_String$java_lang_String$org_openprovenance_prov_model_ProvUtilities_BuildFlag(namespace: string, local: string, prefix: string, flag: org.openprovenance.prov.model.ProvUtilities.BuildFlag): org.openprovenance.prov.model.QualifiedName {
            throw Object.defineProperty(new Error(), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.UnsupportedOperationException','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
        }

        /**
         * A factory method for {@link QualifiedName}. A qualified name consists of a namespace, denoted by an optional prefix, and a local name.
         * 
         * @param {string} namespace a URI for the namespace
         * @param {string} local     a local name
         * @param {string} prefix    a string, which can be null
         * @param {org.openprovenance.prov.model.ProvUtilities.BuildFlag} flag      build flag.
         * @return {*} an instance of {@link QualifiedName}
         */
        public newQualifiedName(namespace?: any, local?: any, prefix?: any, flag?: any): any {
            if (((typeof namespace === 'string') || namespace === null) && ((typeof local === 'string') || local === null) && ((typeof prefix === 'string') || prefix === null) && ((typeof flag === 'number') || flag === null)) {
                return <any>this.newQualifiedName$java_lang_String$java_lang_String$java_lang_String$org_openprovenance_prov_model_ProvUtilities_BuildFlag(namespace, local, prefix, flag);
            } else if (((typeof namespace === 'string') || namespace === null) && ((typeof local === 'string') || local === null) && ((typeof prefix === 'string') || prefix === null) && flag === undefined) {
                return <any>this.newQualifiedName$java_lang_String$java_lang_String$java_lang_String(namespace, local, prefix);
            } else throw new Error('invalid overload');
        }

        /**
         * 
         * @param {*} e2
         * @param {*} e1
         * @return {*}
         */
        public newSpecializationOf(e2: org.openprovenance.prov.model.QualifiedName, e1: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.SpecializationOf {
            return new org.openprovenance.prov.vanilla.SpecializationOf(e2, e1);
        }

        /**
         * A factory method to create an instance of a Usage {@link org.openprovenance.prov.vanilla.Used}
         * 
         * @param {*} id         an optional identifier for a usage
         * @param {*} activity   the identifier  of the <a href="http://www.w3.org/TR/prov-dm/#usage.activity">activity</a> that used an entity
         * @param {*} entity     an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#usage.entity">entity</a> being used
         * @param {javax.xml.datatype.XMLGregorianCalendar} time       an optional "usage time", the <a href="http://www.w3.org/TR/prov-dm/#usage.time">time</a> at which the entity started to be used
         * @param {*[]} attributes an optional set of attribute-value pairs representing additional information about this usage
         * @return {*} an instance of {@link org.openprovenance.prov.vanilla.Used}
         */
        public newUsed(id: org.openprovenance.prov.model.QualifiedName, activity: org.openprovenance.prov.model.QualifiedName, entity: org.openprovenance.prov.model.QualifiedName, time: javax.xml.datatype.XMLGregorianCalendar, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.Used {
            return new org.openprovenance.prov.vanilla.Used(id, activity, entity, time, attributes);
        }

        /**
         * A factory method to create an instance of an Association {@link WasAssociatedWith}
         * 
         * @param {*} id         an optional identifier for the association between an activity and an agent
         * @param {*} activity   an identifier for the activity
         * @param {*} agent      an optional identifier for the agent associated with the activity
         * @param {*} plan       an optional identifier for the plan the agent relied on in the context of this activity
         * @param {*[]} attributes an optional set of attribute-value pairs representing additional information about this association of this activity with this agent.
         * @return {*} an instance of {@link WasAssociatedWith}
         */
        public newWasAssociatedWith(id: org.openprovenance.prov.model.QualifiedName, activity: org.openprovenance.prov.model.QualifiedName, agent: org.openprovenance.prov.model.QualifiedName, plan: org.openprovenance.prov.model.QualifiedName, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.WasAssociatedWith {
            return new org.openprovenance.prov.vanilla.WasAssociatedWith(id, activity, agent, plan, attributes);
        }

        /**
         * A factory method to create an instance of an attribution {@link WasAttributedTo}
         * 
         * @param {*} id         an optional identifier for the relation
         * @param {*} entity     an entity identifier
         * @param {*} agent      the identifier of the agent whom the entity is ascribed to, and therefore bears some responsibility for its existence
         * @param {*[]} attributes an optional set of attribute-value pairs representing additional information about this attribution.
         * @return {*} an instance of {@link WasAttributedTo}
         */
        public newWasAttributedTo(id: org.openprovenance.prov.model.QualifiedName, entity: org.openprovenance.prov.model.QualifiedName, agent: org.openprovenance.prov.model.QualifiedName, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.WasAttributedTo {
            return new org.openprovenance.prov.vanilla.WasAttributedTo(id, entity, agent, attributes);
        }

        /**
         * A factory method to create an instance of a derivation {@link WasDerivedFrom}
         * 
         * @param {*} id         an optional identifier for a derivation
         * @param {*} e2         the identifier  of the <a href="http://www.w3.org/TR/prov-dm/#derivation.generatedEntity">entity generated</a> by the derivation
         * @param {*} e1         the identifier  of the <a href="http://www.w3.org/TR/prov-dm/#derivation.usedEntity">entity used</a> by the derivation
         * @param {*} activity
         * @param {*} generation
         * @param {*} usage
         * @param {*[]} attributes
         * @return {*} an instance of {@link WasDerivedFrom}
         */
        public newWasDerivedFrom(id: org.openprovenance.prov.model.QualifiedName, e2: org.openprovenance.prov.model.QualifiedName, e1: org.openprovenance.prov.model.QualifiedName, activity: org.openprovenance.prov.model.QualifiedName, generation: org.openprovenance.prov.model.QualifiedName, usage: org.openprovenance.prov.model.QualifiedName, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.WasDerivedFrom {
            return new org.openprovenance.prov.vanilla.WasDerivedFrom(id, e2, e1, activity, generation, usage, attributes);
        }

        /**
         * A factory method to create an instance of an end {@link WasEndedBy}
         * 
         * @param {*} id
         * @param {*} activity   an identifier for the ended <a href="http://www.w3.org/TR/prov-dm/#end.activity">activity</a>
         * @param {*} trigger    an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#end.trigger">entity triggering</a> the activity ending
         * @param {*} ender      an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#end.ender">activity</a> that generated the (possibly unspecified) entity
         * @param {javax.xml.datatype.XMLGregorianCalendar} time       the optional <a href="http://www.w3.org/TR/prov-dm/#end.time">time</a>  at which the activity was ended
         * @param {*[]} attributes an optional set of <a href="http://www.w3.org/TR/prov-dm/#end.attributes">attribute-value pairs</a> representing additional information about this activity end
         * @return {*} an instance of {@link WasStartedBy}
         */
        public newWasEndedBy(id: org.openprovenance.prov.model.QualifiedName, activity: org.openprovenance.prov.model.QualifiedName, trigger: org.openprovenance.prov.model.QualifiedName, ender: org.openprovenance.prov.model.QualifiedName, time: javax.xml.datatype.XMLGregorianCalendar, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.WasEndedBy {
            return new org.openprovenance.prov.vanilla.WasEndedBy(id, activity, trigger, ender, time, attributes);
        }

        /**
         * A factory method to create an instance of a generation {@link WasGeneratedBy}
         * 
         * @param {*} id         an optional identifier for a usage
         * @param {*} entity     an identifier for the created <a href="http://www.w3.org/TR/prov-dm/#generation.entity">entity</a>
         * @param {*} activity   an optional identifier  for the <a href="http://www.w3.org/TR/prov-dm/#generation.activity">activity</a> that creates the entity
         * @param {javax.xml.datatype.XMLGregorianCalendar} time       an optional "generation time", the time at which the entity was completely created
         * @param {*[]} attributes an optional set of attribute-value pairs representing additional information about this generation
         * @return {*} an instance of {@link WasGeneratedBy}
         */
        public newWasGeneratedBy(id: org.openprovenance.prov.model.QualifiedName, entity: org.openprovenance.prov.model.QualifiedName, activity: org.openprovenance.prov.model.QualifiedName, time: javax.xml.datatype.XMLGregorianCalendar, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.WasGeneratedBy {
            return new org.openprovenance.prov.vanilla.WasGeneratedBy(id, entity, activity, time, attributes);
        }

        /**
         * A factory method to create an instance of an influence {@link WasInfluencedBy}
         * 
         * @param {*} id         optional identifier identifying the association
         * @param {*} influencee an identifier for an entity, activity, or agent
         * @param {*} influencer an identifier for an ancestor entity, activity, or agent that the former depends on
         * @param {*[]} attributes an optional set of attribute-value pairs representing additional information about this association
         * @return {*} an instance of {@link WasInfluencedBy}
         */
        public newWasInfluencedBy(id: org.openprovenance.prov.model.QualifiedName, influencee: org.openprovenance.prov.model.QualifiedName, influencer: org.openprovenance.prov.model.QualifiedName, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.WasInfluencedBy {
            return new org.openprovenance.prov.vanilla.WasInfluencedBy(id, influencee, influencer, attributes);
        }

        /**
         * A factory method to create an instance of an communication {@link WasInformedBy}
         * 
         * @param {*} id         an optional identifier identifying the association;
         * @param {*} informed   the identifier of the informed activity;
         * @param {*} informant  the identifier of the informant activity;
         * @param {*[]} attributes an optional set of attribute-value pairs representing additional information about this communication.
         * @return {*} an instance of {@link WasInformedBy}
         */
        public newWasInformedBy(id: org.openprovenance.prov.model.QualifiedName, informed: org.openprovenance.prov.model.QualifiedName, informant: org.openprovenance.prov.model.QualifiedName, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.WasInformedBy {
            return new org.openprovenance.prov.vanilla.WasInformedBy(id, informed, informant, attributes);
        }

        /**
         * A factory method to create an instance of an invalidation {@link WasInvalidatedBy}
         * 
         * @param {*} id         an optional identifier for a usage
         * @param {*} entity     an identifier for the created <a href="http://www.w3.org/TR/prov-dm/#invalidation.entity">entity</a>
         * @param {*} activity   an optional identifier  for the <a href="http://www.w3.org/TR/prov-dm/#invalidation.activity">activity</a> that creates the entity
         * @param {javax.xml.datatype.XMLGregorianCalendar} time       an optional "invalidation time", the time at which the entity was completely created
         * @param {*[]} attributes an optional set of attribute-value pairs representing additional information about this invalidation
         * @return {*} an instance of {@link WasInvalidatedBy}
         */
        public newWasInvalidatedBy(id: org.openprovenance.prov.model.QualifiedName, entity: org.openprovenance.prov.model.QualifiedName, activity: org.openprovenance.prov.model.QualifiedName, time: javax.xml.datatype.XMLGregorianCalendar, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.WasInvalidatedBy {
            return new org.openprovenance.prov.vanilla.WasInvalidatedBy(id, entity, activity, time, attributes);
        }

        /**
         * A factory method to create an instance of a start {@link WasStartedBy}
         * 
         * @param {*} id
         * @param {*} activity   an identifier for the started <a href="http://www.w3.org/TR/prov-dm/#start.activity">activity</a>
         * @param {*} trigger    an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#start.trigger">entity triggering</a> the activity
         * @param {*} starter    an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#start.starter">activity</a> that generated the (possibly unspecified) entity
         * @param {javax.xml.datatype.XMLGregorianCalendar} time       the optional <a href="http://www.w3.org/TR/prov-dm/#start.time">time</a>  at which the activity was started
         * @param {*[]} attributes an optional set of <a href="http://www.w3.org/TR/prov-dm/#start.attributes">attribute-value pairs</a> representing additional information about this activity start
         * @return {*} an instance of {@link WasStartedBy}
         */
        public newWasStartedBy(id: org.openprovenance.prov.model.QualifiedName, activity: org.openprovenance.prov.model.QualifiedName, trigger: org.openprovenance.prov.model.QualifiedName, starter: org.openprovenance.prov.model.QualifiedName, time: javax.xml.datatype.XMLGregorianCalendar, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.WasStartedBy {
            return new org.openprovenance.prov.vanilla.WasStartedBy(id, activity, trigger, starter, time, attributes);
        }

        /**
         * 
         * @param {*} bundleId
         * @param {org.openprovenance.prov.model.Namespace} namespace
         */
        public startBundle(bundleId: org.openprovenance.prov.model.QualifiedName, namespace: org.openprovenance.prov.model.Namespace) {
        }

        /**
         * 
         * @param {org.openprovenance.prov.model.Namespace} namespace
         */
        public startDocument(namespace: org.openprovenance.prov.model.Namespace) {
        }

        /**
         * 
         * @param {*} value
         * @param {*} type
         * @return {*}
         */
        public newRole(value: any, type: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Role {
            return new org.openprovenance.prov.vanilla.Role(type, value);
        }

        /**
         * 
         * @param {*} value
         * @param {*} type
         * @return {*}
         */
        public newLocation(value: any, type: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Location {
            return new org.openprovenance.prov.vanilla.Location(type, value);
        }

        /**
         * 
         * @param {*} value
         * @param {*} type
         * @return {*}
         */
        public newType(value: any, type: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Type {
            return new org.openprovenance.prov.vanilla.Type(type, value);
        }

        /**
         * 
         * @param {*} value
         * @param {*} type
         * @return {*}
         */
        public newLabel(value: any, type: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Label {
            return new org.openprovenance.prov.vanilla.Label(type, value);
        }

        public newInternationalizedString$java_lang_String$java_lang_String(s: string, lang: string): org.openprovenance.prov.model.LangString {
            return new org.openprovenance.prov.vanilla.LangString(s, lang);
        }

        /**
         * 
         * @param {string} s
         * @param {string} lang
         * @return {*}
         */
        public newInternationalizedString(s?: any, lang?: any): any {
            if (((typeof s === 'string') || s === null) && ((typeof lang === 'string') || lang === null)) {
                return <any>this.newInternationalizedString$java_lang_String$java_lang_String(s, lang);
            } else if (((typeof s === 'string') || s === null) && lang === undefined) {
                return <any>this.newInternationalizedString$java_lang_String(s);
            } else throw new Error('invalid overload');
        }

        /**
         * 
         * @param {*} value
         * @param {*} type
         * @return {*}
         */
        public newValue(value: any, type: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Value {
            return new org.openprovenance.prov.vanilla.Value(type, value);
        }

        /**
         * 
         * @param {*} elementName
         * @param {*} value
         * @param {*} type
         * @return {*}
         */
        public newOther(elementName: org.openprovenance.prov.model.QualifiedName, value: any, type: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Other {
            return new org.openprovenance.prov.vanilla.Other(elementName, type, value);
        }

        public newInternationalizedString$java_lang_String(s: string): org.openprovenance.prov.model.LangString {
            return new org.openprovenance.prov.vanilla.LangString(s);
        }

        constructor() {
        }
    }
    ModelConstructor["__class"] = "org.openprovenance.prov.vanilla.ModelConstructor";
    ModelConstructor["__interfaces"] = ["org.openprovenance.prov.model.AtomConstructor","org.openprovenance.prov.model.ModelConstructor"];


}

