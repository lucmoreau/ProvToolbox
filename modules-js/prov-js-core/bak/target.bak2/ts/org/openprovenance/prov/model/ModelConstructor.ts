/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.model {
    /**
     * Interface for constructing concrete representations of the PROV data model
     * @class
     */
    export interface ModelConstructor {
        /**
         * A factory method to create an instance of a delegation {@link ActedOnBehalfOf}
         * @param {*} id identifier for the delegation association between delegate and responsible
         * @param {*} delegate identifier for the agent associated with an activity, acting on behalf of the responsible agent
         * @param {*} responsible identifier for the agent, on behalf of which the delegate agent acted
         * @param {*} activity optional identifier of an activity for which the delegation association holds
         * @param {*[]} attributes optional set  of attributes representing additional information about this delegation association
         * @return {*} an instance of {@link ActedOnBehalfOf}
         */
        newActedOnBehalfOf(id: org.openprovenance.prov.model.QualifiedName, delegate: org.openprovenance.prov.model.QualifiedName, responsible: org.openprovenance.prov.model.QualifiedName, activity: org.openprovenance.prov.model.QualifiedName, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.ActedOnBehalfOf;

        newActivity(id: org.openprovenance.prov.model.QualifiedName, startTime: javax.xml.datatype.XMLGregorianCalendar, endTime: javax.xml.datatype.XMLGregorianCalendar, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.Activity;

        newAgent(id: org.openprovenance.prov.model.QualifiedName, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.Agent;

        newAlternateOf(e1: org.openprovenance.prov.model.QualifiedName, e2: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.AlternateOf;

        newDerivedByInsertionFrom(id: org.openprovenance.prov.model.QualifiedName, after: org.openprovenance.prov.model.QualifiedName, before: org.openprovenance.prov.model.QualifiedName, keyEntitySet: Array<org.openprovenance.prov.model.Entry>, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.DerivedByInsertionFrom;

        newDerivedByRemovalFrom(id: org.openprovenance.prov.model.QualifiedName, after: org.openprovenance.prov.model.QualifiedName, before: org.openprovenance.prov.model.QualifiedName, keys: Array<org.openprovenance.prov.model.Key>, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.DerivedByRemovalFrom;

        newDictionaryMembership(dict: org.openprovenance.prov.model.QualifiedName, keyEntitySet: Array<org.openprovenance.prov.model.Entry>): org.openprovenance.prov.model.DictionaryMembership;

        /**
         * A factory method to create an instance of a {@link Document}
         * @param {org.openprovenance.prov.model.Namespace} namespace the prefix namespace mapping for the current document
         * @param {*[]} statements a collection of statements
         * @param {*[]} bundles a collection of bundles
         * @return {*} an instance of {@link Document}, with this prefix-namespace mapping, statements, and bundles
         */
        newDocument(namespace: org.openprovenance.prov.model.Namespace, statements: Array<org.openprovenance.prov.model.Statement>, bundles: Array<org.openprovenance.prov.model.Bundle>): org.openprovenance.prov.model.Document;

        newEntity(id: org.openprovenance.prov.model.QualifiedName, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.Entity;

        newHadMember(c: org.openprovenance.prov.model.QualifiedName, e: Array<org.openprovenance.prov.model.QualifiedName>): org.openprovenance.prov.model.HadMember;

        newMentionOf(e2: org.openprovenance.prov.model.QualifiedName, e1: org.openprovenance.prov.model.QualifiedName, b: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.MentionOf;

        /**
         * A factory method to create an instance of a Bundle {@link Bundle}
         * @param {*} id an identifier for the bundle
         * @param {org.openprovenance.prov.model.Namespace} namespace a {@link Namespace} object mapping prefix to namespace URIs
         * @param {*[]} statements a set of provenance descriptions
         * @return {*} {@link Bundle}
         */
        newNamedBundle(id: org.openprovenance.prov.model.QualifiedName, namespace: org.openprovenance.prov.model.Namespace, statements: Array<org.openprovenance.prov.model.Statement>): org.openprovenance.prov.model.Bundle;

        /**
         * A factory method for {@link QualifiedName}. A qualified name consists of a namespace, denoted by an optional prefix, and a local name.
         * @param {string} namespace a URI for the namespace
         * @param {string} local a local name
         * @param {string} prefix a string, which can be null
         * @param {org.openprovenance.prov.model.ProvUtilities.BuildFlag} flag build flag.
         * @return {*} an instance of {@link QualifiedName}
         */
        newQualifiedName(namespace?: any, local?: any, prefix?: any, flag?: any): any;

        newSpecializationOf(e2: org.openprovenance.prov.model.QualifiedName, e1: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.SpecializationOf;

        /**
         * A factory method to create an instance of a Usage {@link Used}
         * @param {*} id an optional identifier for a usage
         * @param {*} activity the identifier  of the <a href="http://www.w3.org/TR/prov-dm/#usage.activity">activity</a> that used an entity
         * @param {*} entity an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#usage.entity">entity</a> being used
         * @param {javax.xml.datatype.XMLGregorianCalendar} time an optional "usage time", the <a href="http://www.w3.org/TR/prov-dm/#usage.time">time</a> at which the entity started to be used
         * @param {*[]} attributes an optional set of attribute-value pairs representing additional information about this usage
         * @return {*} an instance of {@link Used}
         */
        newUsed(id: org.openprovenance.prov.model.QualifiedName, activity: org.openprovenance.prov.model.QualifiedName, entity: org.openprovenance.prov.model.QualifiedName, time: javax.xml.datatype.XMLGregorianCalendar, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.Used;

        /**
         * A factory method to create an instance of an Association {@link WasAssociatedWith}
         * @param {*} id an optional identifier for the association between an activity and an agent
         * @param {*} activity an identifier for the activity
         * @param {*} agent an optional identifier for the agent associated with the activity
         * @param {*} plan an optional identifier for the plan the agent relied on in the context of this activity
         * @param {*[]} attributes an optional set of attribute-value pairs representing additional information about this association of this activity with this agent.
         * @return {*} an instance of {@link WasAssociatedWith}
         */
        newWasAssociatedWith(id: org.openprovenance.prov.model.QualifiedName, activity: org.openprovenance.prov.model.QualifiedName, agent: org.openprovenance.prov.model.QualifiedName, plan: org.openprovenance.prov.model.QualifiedName, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.WasAssociatedWith;

        /**
         * A factory method to create an instance of an attribution {@link WasAttributedTo}
         * @param {*} id  an optional identifier for the relation
         * @param {*} entity an entity identifier
         * @param {*} agent  the identifier of the agent whom the entity is ascribed to, and therefore bears some responsibility for its existence
         * @param {*[]} attributes an optional set of attribute-value pairs representing additional information about this attribution.
         * @return {*} an instance of {@link WasAttributedTo}
         */
        newWasAttributedTo(id: org.openprovenance.prov.model.QualifiedName, entity: org.openprovenance.prov.model.QualifiedName, agent: org.openprovenance.prov.model.QualifiedName, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.WasAttributedTo;

        /**
         * A factory method to create an instance of a derivation {@link WasDerivedFrom}
         * @param {*} id an optional identifier for a derivation
         * @param {*} e2 the identifier  of the <a href="http://www.w3.org/TR/prov-dm/#derivation.generatedEntity">entity generated</a> by the derivation
         * @param {*} e1 the identifier  of the <a href="http://www.w3.org/TR/prov-dm/#derivation.usedEntity">entity used</a> by the derivation
         * @param {*} activity
         * @param {*} generation
         * @param {*} usage
         * @param {*[]} attributes
         * @return {*} an instance of {@link WasDerivedFrom}
         */
        newWasDerivedFrom(id: org.openprovenance.prov.model.QualifiedName, e2: org.openprovenance.prov.model.QualifiedName, e1: org.openprovenance.prov.model.QualifiedName, activity: org.openprovenance.prov.model.QualifiedName, generation: org.openprovenance.prov.model.QualifiedName, usage: org.openprovenance.prov.model.QualifiedName, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.WasDerivedFrom;

        /**
         * A factory method to create an instance of an end {@link WasEndedBy}
         * @param {*} id
         * @param {*} activity an identifier for the ended <a href="http://www.w3.org/TR/prov-dm/#end.activity">activity</a>
         * @param {*} trigger an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#end.trigger">entity triggering</a> the activity ending
         * @param {*} ender an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#end.ender">activity</a> that generated the (possibly unspecified) entity
         * @param {javax.xml.datatype.XMLGregorianCalendar} time the optional <a href="http://www.w3.org/TR/prov-dm/#end.time">time</a>  at which the activity was ended
         * @param {*[]} attributes an optional set of <a href="http://www.w3.org/TR/prov-dm/#end.attributes">attribute-value pairs</a> representing additional information about this activity end
         * @return {*} an instance of {@link WasStartedBy}
         */
        newWasEndedBy(id: org.openprovenance.prov.model.QualifiedName, activity: org.openprovenance.prov.model.QualifiedName, trigger: org.openprovenance.prov.model.QualifiedName, ender: org.openprovenance.prov.model.QualifiedName, time: javax.xml.datatype.XMLGregorianCalendar, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.WasEndedBy;

        /**
         * A factory method to create an instance of a generation {@link WasGeneratedBy}
         * @param {*} id an optional identifier for a usage
         * @param {*} entity an identifier for the created <a href="http://www.w3.org/TR/prov-dm/#generation.entity">entity</a>
         * @param {*} activity an optional identifier  for the <a href="http://www.w3.org/TR/prov-dm/#generation.activity">activity</a> that creates the entity
         * @param {javax.xml.datatype.XMLGregorianCalendar} time an optional "generation time", the time at which the entity was completely created
         * @param {*[]} attributes an optional set of attribute-value pairs representing additional information about this generation
         * @return {*} an instance of {@link WasGeneratedBy}
         */
        newWasGeneratedBy(id: org.openprovenance.prov.model.QualifiedName, entity: org.openprovenance.prov.model.QualifiedName, activity: org.openprovenance.prov.model.QualifiedName, time: javax.xml.datatype.XMLGregorianCalendar, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.WasGeneratedBy;

        /**
         * A factory method to create an instance of an influence {@link WasInfluencedBy}
         * @param {*} id optional identifier identifying the association
         * @param {*} influencee an identifier for an entity, activity, or agent
         * @param {*} influencer an identifier for an ancestor entity, activity, or agent that the former depends on
         * @param {*[]} attributes an optional set of attribute-value pairs representing additional information about this association
         * 
         * @return {*} an instance of {@link WasInfluencedBy}
         */
        newWasInfluencedBy(id: org.openprovenance.prov.model.QualifiedName, influencee: org.openprovenance.prov.model.QualifiedName, influencer: org.openprovenance.prov.model.QualifiedName, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.WasInfluencedBy;

        /**
         * A factory method to create an instance of an communication {@link WasInformedBy}
         * @param {*} id an optional identifier identifying the association;
         * @param {*} informed the identifier of the informed activity;
         * @param {*} informant the identifier of the informant activity;
         * @param {*[]} attributes an optional set of attribute-value pairs representing additional information about this communication.
         * @return {*} an instance of {@link WasInformedBy}
         */
        newWasInformedBy(id: org.openprovenance.prov.model.QualifiedName, informed: org.openprovenance.prov.model.QualifiedName, informant: org.openprovenance.prov.model.QualifiedName, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.WasInformedBy;

        /**
         * A factory method to create an instance of an invalidation {@link WasInvalidatedBy}
         * @param {*} id an optional identifier for a usage
         * @param {*} entity an identifier for the created <a href="http://www.w3.org/TR/prov-dm/#invalidation.entity">entity</a>
         * @param {*} activity an optional identifier  for the <a href="http://www.w3.org/TR/prov-dm/#invalidation.activity">activity</a> that creates the entity
         * @param {javax.xml.datatype.XMLGregorianCalendar} time an optional "invalidation time", the time at which the entity was completely created
         * @param {*[]} attributes an optional set of attribute-value pairs representing additional information about this invalidation
         * @return {*} an instance of {@link WasInvalidatedBy}
         */
        newWasInvalidatedBy(id: org.openprovenance.prov.model.QualifiedName, entity: org.openprovenance.prov.model.QualifiedName, activity: org.openprovenance.prov.model.QualifiedName, time: javax.xml.datatype.XMLGregorianCalendar, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.WasInvalidatedBy;

        /**
         * A factory method to create an instance of a start {@link WasStartedBy}
         * @param {*} id
         * @param {*} activity an identifier for the started <a href="http://www.w3.org/TR/prov-dm/#start.activity">activity</a>
         * @param {*} trigger an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#start.trigger">entity triggering</a> the activity
         * @param {*} starter an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#start.starter">activity</a> that generated the (possibly unspecified) entity
         * @param {javax.xml.datatype.XMLGregorianCalendar} time the optional <a href="http://www.w3.org/TR/prov-dm/#start.time">time</a>  at which the activity was started
         * @param {*[]} attributes an optional set of <a href="http://www.w3.org/TR/prov-dm/#start.attributes">attribute-value pairs</a> representing additional information about this activity start
         * @return {*} an instance of {@link WasStartedBy}
         */
        newWasStartedBy(id: org.openprovenance.prov.model.QualifiedName, activity: org.openprovenance.prov.model.QualifiedName, trigger: org.openprovenance.prov.model.QualifiedName, starter: org.openprovenance.prov.model.QualifiedName, time: javax.xml.datatype.XMLGregorianCalendar, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.WasStartedBy;

        startBundle(bundleId: org.openprovenance.prov.model.QualifiedName, namespace: org.openprovenance.prov.model.Namespace);

        startDocument(namespace: org.openprovenance.prov.model.Namespace);
    }
}

