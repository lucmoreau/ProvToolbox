/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var org;
(function (org) {
    var openprovenance;
    (function (openprovenance) {
        var prov;
        (function (prov) {
            var vanilla;
            (function (vanilla) {
                class ModelConstructor {
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
                    newActedOnBehalfOf(id, delegate, responsible, activity, attributes) {
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
                    newActivity(id, startTime, endTime, attributes) {
                        return new org.openprovenance.prov.vanilla.Activity(id, startTime, endTime, attributes);
                    }
                    /**
                     *
                     * @param {*} id
                     * @param {*[]} attributes
                     * @return {*}
                     */
                    newAgent(id, attributes) {
                        return new org.openprovenance.prov.vanilla.Agent(id, attributes);
                    }
                    /**
                     *
                     * @param {*} e1
                     * @param {*} e2
                     * @return {*}
                     */
                    newAlternateOf(e1, e2) {
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
                    newDerivedByInsertionFrom(id, after, before, keyEntitySet, attributes) {
                        throw Object.defineProperty(new Error(), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.UnsupportedOperationException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
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
                    newDerivedByRemovalFrom(id, after, before, keys, attributes) {
                        throw Object.defineProperty(new Error(), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.UnsupportedOperationException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                    }
                    /**
                     *
                     * @param {*} dict
                     * @param {*[]} keyEntitySet
                     * @return {*}
                     */
                    newDictionaryMembership(dict, keyEntitySet) {
                        throw Object.defineProperty(new Error(), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.UnsupportedOperationException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                    }
                    /**
                     * A factory method to create an instance of a {@link Document}
                     *
                     * @param {org.openprovenance.prov.model.Namespace} namespace  the prefix namespace mapping for the current document
                     * @param {*[]} statements a collection of statements
                     * @param {*[]} bundles    a collection of bundles
                     * @return {*} an instance of {@link Document}, with this prefix-namespace mapping, statements, and bundles
                     */
                    newDocument(namespace, statements, bundles) {
                        return new org.openprovenance.prov.vanilla.Document(namespace, statements, bundles);
                    }
                    /**
                     *
                     * @param {*} id
                     * @param {*[]} attributes
                     * @return {*}
                     */
                    newEntity(id, attributes) {
                        return new org.openprovenance.prov.vanilla.Entity(id, attributes);
                    }
                    /**
                     *
                     * @param {*} c
                     * @param {*[]} e
                     * @return {*}
                     */
                    newHadMember(c, e) {
                        const ll = ([]);
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(ll, e);
                        return new org.openprovenance.prov.vanilla.HadMember(c, ll);
                    }
                    /**
                     *
                     * @param {*} e2
                     * @param {*} e1
                     * @param {*} b
                     * @return {*}
                     */
                    newMentionOf(e2, e1, b) {
                        throw Object.defineProperty(new Error(), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.UnsupportedOperationException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                    }
                    /**
                     * A factory method to create an instance of a Bundle {@link Bundle}
                     *
                     * @param {*} id         an identifier for the bundle
                     * @param {org.openprovenance.prov.model.Namespace} namespace  a {@link Namespace} object mapping prefix to namespace URIs
                     * @param {*[]} statements a set of provenance descriptions
                     * @return {org.openprovenance.prov.vanilla.Bundle} {@link Bundle}
                     */
                    newNamedBundle(id, namespace, statements) {
                        return new org.openprovenance.prov.vanilla.Bundle(id, namespace, statements);
                    }
                    newQualifiedName$java_lang_String$java_lang_String$java_lang_String(namespace, local, prefix) {
                        return new org.openprovenance.prov.vanilla.QualifiedName(namespace, local, prefix);
                    }
                    newQualifiedName$java_lang_String$java_lang_String$java_lang_String$org_openprovenance_prov_model_ProvUtilities_BuildFlag(namespace, local, prefix, flag) {
                        throw Object.defineProperty(new Error(), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.UnsupportedOperationException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
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
                    newQualifiedName(namespace, local, prefix, flag) {
                        if (((typeof namespace === 'string') || namespace === null) && ((typeof local === 'string') || local === null) && ((typeof prefix === 'string') || prefix === null) && ((typeof flag === 'number') || flag === null)) {
                            return this.newQualifiedName$java_lang_String$java_lang_String$java_lang_String$org_openprovenance_prov_model_ProvUtilities_BuildFlag(namespace, local, prefix, flag);
                        }
                        else if (((typeof namespace === 'string') || namespace === null) && ((typeof local === 'string') || local === null) && ((typeof prefix === 'string') || prefix === null) && flag === undefined) {
                            return this.newQualifiedName$java_lang_String$java_lang_String$java_lang_String(namespace, local, prefix);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    /**
                     *
                     * @param {*} e2
                     * @param {*} e1
                     * @return {*}
                     */
                    newSpecializationOf(e2, e1) {
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
                    newUsed(id, activity, entity, time, attributes) {
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
                    newWasAssociatedWith(id, activity, agent, plan, attributes) {
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
                    newWasAttributedTo(id, entity, agent, attributes) {
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
                    newWasDerivedFrom(id, e2, e1, activity, generation, usage, attributes) {
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
                    newWasEndedBy(id, activity, trigger, ender, time, attributes) {
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
                    newWasGeneratedBy(id, entity, activity, time, attributes) {
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
                    newWasInfluencedBy(id, influencee, influencer, attributes) {
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
                    newWasInformedBy(id, informed, informant, attributes) {
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
                    newWasInvalidatedBy(id, entity, activity, time, attributes) {
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
                    newWasStartedBy(id, activity, trigger, starter, time, attributes) {
                        return new org.openprovenance.prov.vanilla.WasStartedBy(id, activity, trigger, starter, time, attributes);
                    }
                    /**
                     *
                     * @param {*} bundleId
                     * @param {org.openprovenance.prov.model.Namespace} namespace
                     */
                    startBundle(bundleId, namespace) {
                    }
                    /**
                     *
                     * @param {org.openprovenance.prov.model.Namespace} namespace
                     */
                    startDocument(namespace) {
                    }
                    /**
                     *
                     * @param {*} value
                     * @param {*} type
                     * @return {*}
                     */
                    newRole(value, type) {
                        return new org.openprovenance.prov.vanilla.Role(type, value);
                    }
                    /**
                     *
                     * @param {*} value
                     * @param {*} type
                     * @return {*}
                     */
                    newLocation(value, type) {
                        return new org.openprovenance.prov.vanilla.Location(type, value);
                    }
                    /**
                     *
                     * @param {*} value
                     * @param {*} type
                     * @return {*}
                     */
                    newType(value, type) {
                        return new org.openprovenance.prov.vanilla.Type(type, value);
                    }
                    /**
                     *
                     * @param {*} value
                     * @param {*} type
                     * @return {*}
                     */
                    newLabel(value, type) {
                        return new org.openprovenance.prov.vanilla.Label(type, value);
                    }
                    newInternationalizedString$java_lang_String$java_lang_String(s, lang) {
                        return new org.openprovenance.prov.vanilla.LangString(s, lang);
                    }
                    /**
                     *
                     * @param {string} s
                     * @param {string} lang
                     * @return {*}
                     */
                    newInternationalizedString(s, lang) {
                        if (((typeof s === 'string') || s === null) && ((typeof lang === 'string') || lang === null)) {
                            return this.newInternationalizedString$java_lang_String$java_lang_String(s, lang);
                        }
                        else if (((typeof s === 'string') || s === null) && lang === undefined) {
                            return this.newInternationalizedString$java_lang_String(s);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    /**
                     *
                     * @param {*} value
                     * @param {*} type
                     * @return {*}
                     */
                    newValue(value, type) {
                        return new org.openprovenance.prov.vanilla.Value(type, value);
                    }
                    /**
                     *
                     * @param {*} elementName
                     * @param {*} value
                     * @param {*} type
                     * @return {*}
                     */
                    newOther(elementName, value, type) {
                        return new org.openprovenance.prov.vanilla.Other(elementName, type, value);
                    }
                    newInternationalizedString$java_lang_String(s) {
                        return new org.openprovenance.prov.vanilla.LangString(s);
                    }
                    constructor() {
                    }
                }
                vanilla.ModelConstructor = ModelConstructor;
                ModelConstructor["__class"] = "org.openprovenance.prov.vanilla.ModelConstructor";
                ModelConstructor["__interfaces"] = ["org.openprovenance.prov.model.AtomConstructor", "org.openprovenance.prov.model.ModelConstructor"];
            })(vanilla = prov.vanilla || (prov.vanilla = {}));
        })(prov = openprovenance.prov || (openprovenance.prov = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
