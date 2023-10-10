package org.openprovenance.prov.model;
import java.util.Collection;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;


/** Interface for constructing concrete representations of the PROV data model */

public interface ModelConstructor {

    /** A factory method to create an instance of a delegation {@link ActedOnBehalfOf}
     * @param id identifier for the delegation association between delegate and responsible
     * @param delegate identifier for the agent associated with an activity, acting on behalf of the responsible agent
     * @param responsible identifier for the agent, on behalf of which the delegate agent acted
     * @param activity optional identifier of an activity for which the delegation association holds
     * @param attributes optional set  of attributes representing additional information about this delegation association
     * @return an instance of {@link ActedOnBehalfOf}
     */
    ActedOnBehalfOf newActedOnBehalfOf(QualifiedName id, QualifiedName delegate, QualifiedName responsible, QualifiedName activity, Collection<Attribute> attributes);
    Activity newActivity(QualifiedName id, XMLGregorianCalendar startTime, XMLGregorianCalendar endTime, Collection<Attribute> attributes);
    Agent newAgent(QualifiedName id, Collection<Attribute> attributes);
    AlternateOf newAlternateOf(QualifiedName e1, QualifiedName e2);
    DerivedByInsertionFrom newDerivedByInsertionFrom(QualifiedName id,
                                                     QualifiedName after,
                                                     QualifiedName before,
                                                     List<Entry> keyEntitySet,
                                                     Collection<Attribute> attributes);
    DerivedByRemovalFrom newDerivedByRemovalFrom(QualifiedName id,
                                                 QualifiedName after,
                                                 QualifiedName before,
                                                 List<Key> keys,
                                                 Collection<Attribute> attributes);
    DictionaryMembership newDictionaryMembership(QualifiedName dict, List<Entry> keyEntitySet);

    /** A factory method to create an instance of a {@link Document}
     * @param namespace the prefix namespace mapping for the current document
     * @param statements a collection of statements
     * @param bundles a collection of bundles
     * @return an instance of {@link Document}, with this prefix-namespace mapping, statements, and bundles
     */
    Document newDocument(Namespace namespace,
                         Collection<Statement> statements,
                         Collection<Bundle> bundles);
    /** A factory method to create an instance of a {@link Document}
     * @param namespace the prefix namespace mapping for the current document
     * @param statementsOrBundles a list of statements or bundles
     * @return an instance of {@link Document}, with this prefix-namespace mapping, statements, and bundles
     */
    Document newDocument(Namespace namespace, List<StatementOrBundle> statementsOrBundles);

    Entity newEntity(QualifiedName id, Collection<Attribute> attributes);
    HadMember newHadMember(QualifiedName c, Collection<QualifiedName> e);
    MentionOf newMentionOf(QualifiedName e2, QualifiedName e1, QualifiedName b);

    /**
     * A factory method to create an instance of a Bundle {@link Bundle}
     * @param id an identifier for the bundle
     * @param namespace a {@link Namespace} object mapping prefix to namespace URIs
     * @param statements a set of provenance descriptions 
     * @return {@link Bundle}
     */
    Bundle newNamedBundle(QualifiedName id,
                          Namespace namespace,
                          Collection<Statement> statements);
    /** A factory method for {@link QualifiedName}. A qualified name consists of a namespace, denoted by an optional prefix, and a local name.
     * @param namespace a URI for the namespace
     * @param local a local name 
     * @param prefix a string, which can be null.
     * @return an instance of {@link QualifiedName}
     */
    QualifiedName newQualifiedName(String namespace, String local, String prefix);

    /** A factory method for {@link QualifiedName}. A qualified name consists of a namespace, denoted by an optional prefix, and a local name.
     * @param namespace a URI for the namespace
     * @param local a local name 
     * @param prefix a string, which can be null
     * @param flag build flag.
     * @return an instance of {@link QualifiedName}
     */
    QualifiedName newQualifiedName(String namespace, String local, String prefix, ProvUtilities.BuildFlag flag);

    SpecializationOf newSpecializationOf(QualifiedName e2, QualifiedName e1);

    /** A factory method to create an instance of a Usage {@link Used}
     * @param id an optional identifier for a usage
     * @param activity the identifier  of the <a href="http://www.w3.org/TR/prov-dm/#usage.activity">activity</a> that used an entity
     * @param entity an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#usage.entity">entity</a> being used
     * @param time an optional "usage time", the <a href="http://www.w3.org/TR/prov-dm/#usage.time">time</a> at which the entity started to be used
     * @param attributes an optional set of attribute-value pairs representing additional information about this usage
     * @return an instance of {@link Used}
     */
    Used newUsed(QualifiedName id, QualifiedName activity, QualifiedName entity, XMLGregorianCalendar time, Collection<Attribute> attributes);


    /** A factory method to create an instance of an Association {@link WasAssociatedWith}
     * @param id an optional identifier for the association between an activity and an agent
     * @param activity an identifier for the activity
     * @param agent an optional identifier for the agent associated with the activity
     * @param plan an optional identifier for the plan the agent relied on in the context of this activity
     * @param attributes an optional set of attribute-value pairs representing additional information about this association of this activity with this agent.
     * @return an instance of {@link WasAssociatedWith}
     */
    WasAssociatedWith newWasAssociatedWith(QualifiedName id, QualifiedName activity, QualifiedName agent, QualifiedName plan, Collection<Attribute> attributes);

    /** A factory method to create an instance of an attribution {@link WasAttributedTo}
     * @param id  an optional identifier for the relation
     * @param entity an entity identifier
     * @param agent  the identifier of the agent whom the entity is ascribed to, and therefore bears some responsibility for its existence
     * @param attributes an optional set of attribute-value pairs representing additional information about this attribution.
     * @return an instance of {@link WasAttributedTo}
     */
    WasAttributedTo newWasAttributedTo(QualifiedName id, QualifiedName entity, QualifiedName agent,  Collection<Attribute> attributes);


    /** A factory method to create an instance of a derivation {@link WasDerivedFrom}
     * @param id an optional identifier for a derivation
     * @param e2 the identifier  of the <a href="http://www.w3.org/TR/prov-dm/#derivation.generatedEntity">entity generated</a> by the derivation 
     * @param e1 the identifier  of the <a href="http://www.w3.org/TR/prov-dm/#derivation.usedEntity">entity used</a> by the derivation
     * @param activity an identifier for the <a href="http://www.w3.org/TR/prov-dm/#derivation.activity">activity</a> underpinning the derivation
     * @param generation an identifier for the <a href="http://www.w3.org/TR/prov-dm/#derivation.genertion">generation</a> associated with the derivation
     * @param usage an identifier for the <a href="http://www.w3.org/TR/prov-dm/#derivation.usage">usage</a> associated with the derivation
     * @param attributes an optional set of <a href="http://www.w3.org/TR/prov-dm/#end.attributes">attribute-value pairs</a> representing additional information about this derivation
     * @return an instance of {@link WasDerivedFrom}
     */
    WasDerivedFrom newWasDerivedFrom(QualifiedName id, QualifiedName e2, QualifiedName e1, QualifiedName activity, QualifiedName generation, QualifiedName usage,  Collection<Attribute> attributes);

    /** A factory method to create an instance of an end {@link WasEndedBy}
     * @param id an optional identifier for a end
     * @param activity an identifier for the ended <a href="http://www.w3.org/TR/prov-dm/#end.activity">activity</a>
     * @param trigger an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#end.trigger">entity triggering</a> the activity ending
     * @param ender an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#end.ender">activity</a> that generated the (possibly unspecified) entity
     * @param time the optional <a href="http://www.w3.org/TR/prov-dm/#end.time">time</a>  at which the activity was ended
     * @param attributes an optional set of <a href="http://www.w3.org/TR/prov-dm/#end.attributes">attribute-value pairs</a> representing additional information about this activity start
     * @return an instance of {@link WasStartedBy}
     */

    WasEndedBy newWasEndedBy(QualifiedName id, QualifiedName activity, QualifiedName trigger, QualifiedName ender, XMLGregorianCalendar time, Collection<Attribute> attributes);

    /** A factory method to create an instance of a generation {@link WasGeneratedBy}
     * @param id an optional identifier for a usage
     * @param entity an identifier for the created <a href="http://www.w3.org/TR/prov-dm/#generation.entity">entity</a>
     * @param activity an optional identifier  for the <a href="http://www.w3.org/TR/prov-dm/#generation.activity">activity</a> that creates the entity
     * @param time an optional "generation time", the time at which the entity was completely created
     * @param attributes an optional set of attribute-value pairs representing additional information about this generation
     * @return an instance of {@link WasGeneratedBy}
     */


    WasGeneratedBy newWasGeneratedBy(QualifiedName id, QualifiedName entity, QualifiedName activity, XMLGregorianCalendar time, Collection<Attribute> attributes);

    /** A factory method to create an instance of an influence {@link WasInfluencedBy}
     * @param id optional identifier identifying the association
     * @param influencee an identifier for an entity, activity, or agent
     * @param influencer an identifier for an ancestor entity, activity, or agent that the former depends on
     * @param attributes an optional set of attribute-value pairs representing additional information about this association
     *
     * @return an instance of {@link WasInfluencedBy}
     */
    WasInfluencedBy newWasInfluencedBy(QualifiedName id, QualifiedName influencee, QualifiedName influencer, Collection<Attribute> attributes);


    /** A factory method to create an instance of an communication {@link WasInformedBy}
     * @param id an optional identifier identifying the association;
     * @param informed the identifier of the informed activity;
     * @param informant the identifier of the informant activity;
     * @param attributes an optional set of attribute-value pairs representing additional information about this communication.
     * @return an instance of {@link WasInformedBy}
     */
    WasInformedBy newWasInformedBy(QualifiedName id, QualifiedName informed, QualifiedName informant, Collection<Attribute> attributes);

    /** A factory method to create an instance of an invalidation {@link WasInvalidatedBy}
     * @param id an optional identifier for a usage
     * @param entity an identifier for the created <a href="http://www.w3.org/TR/prov-dm/#invalidation.entity">entity</a>
     * @param activity an optional identifier  for the <a href="http://www.w3.org/TR/prov-dm/#invalidation.activity">activity</a> that creates the entity
     * @param time an optional "invalidation time", the time at which the entity was completely created
     * @param attributes an optional set of attribute-value pairs representing additional information about this invalidation
     * @return an instance of {@link WasInvalidatedBy}
     */

    WasInvalidatedBy newWasInvalidatedBy(QualifiedName id, QualifiedName entity, QualifiedName activity, XMLGregorianCalendar time, Collection<Attribute> attributes);

    /** A factory method to create an instance of a start {@link WasStartedBy}
     * @param id an optional identifier for a start
     * @param activity an identifier for the started <a href="http://www.w3.org/TR/prov-dm/#start.activity">activity</a>
     * @param trigger an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#start.trigger">entity triggering</a> the activity
     * @param starter an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#start.starter">activity</a> that generated the (possibly unspecified) entity
     * @param time the optional <a href="http://www.w3.org/TR/prov-dm/#start.time">time</a>  at which the activity was started
     * @param attributes an optional set of <a href="http://www.w3.org/TR/prov-dm/#start.attributes">attribute-value pairs</a> representing additional information about this activity start
     * @return an instance of {@link WasStartedBy}
     */
    WasStartedBy newWasStartedBy(QualifiedName id, QualifiedName activity, QualifiedName trigger, QualifiedName starter, XMLGregorianCalendar time, Collection<Attribute> attributes);
    void startBundle(QualifiedName bundleId, Namespace namespace);

    void startDocument(Namespace namespace);


}
