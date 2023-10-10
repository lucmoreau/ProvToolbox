package org.openprovenance.prov.vanilla;

import org.openprovenance.prov.model.*;
import org.openprovenance.prov.model.Activity;
import org.openprovenance.prov.model.ActedOnBehalfOf;
import org.openprovenance.prov.model.Agent;
import org.openprovenance.prov.model.AlternateOf;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.HadMember;
import org.openprovenance.prov.model.LangString;
import org.openprovenance.prov.model.Location;
import org.openprovenance.prov.model.Other;
import org.openprovenance.prov.model.ProvUtilities;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.Role;
import org.openprovenance.prov.model.SpecializationOf;
import org.openprovenance.prov.model.Type;
import org.openprovenance.prov.model.Value;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

final public class ModelConstructor implements org.openprovenance.prov.model.ModelConstructor, AtomConstructor {
    /**
     * A factory method to create an instance of a delegation {@link ActedOnBehalfOf}
     *
     * @param id          identifier for the delegation association between delegate and responsible
     * @param delegate    identifier for the agent associated with an activity, acting on behalf of the responsible agent
     * @param responsible identifier for the agent, on behalf of which the delegate agent acted
     * @param activity    optional identifier of an activity for which the delegation association holds
     * @param attributes  optional set  of attributes representing additional information about this delegation association
     * @return an instance of {@link ActedOnBehalfOf}
     */
    @Override
    public ActedOnBehalfOf newActedOnBehalfOf(QualifiedName id, QualifiedName delegate, QualifiedName responsible, QualifiedName activity, Collection<Attribute> attributes) {
        return new org.openprovenance.prov.vanilla.ActedOnBehalfOf(id,delegate,responsible,activity,attributes);
    }

    @Override
    public Activity newActivity(QualifiedName id, XMLGregorianCalendar startTime, XMLGregorianCalendar endTime, Collection<Attribute> attributes) {
        return new org.openprovenance.prov.vanilla.Activity(id,startTime,endTime,attributes);
    }

    @Override
    public Agent newAgent(QualifiedName id, Collection<Attribute> attributes) {
        return new org.openprovenance.prov.vanilla.Agent(id,attributes);
    }

    @Override
    public AlternateOf newAlternateOf(QualifiedName e1, QualifiedName e2) {
        return new org.openprovenance.prov.vanilla.AlternateOf(e1,e2);
    }

    @Override
    public DerivedByInsertionFrom newDerivedByInsertionFrom(QualifiedName id, QualifiedName after, QualifiedName before, List<Entry> keyEntitySet, Collection<Attribute> attributes) {
        throw new UnsupportedOperationException();
    }

    @Override
    public DerivedByRemovalFrom newDerivedByRemovalFrom(QualifiedName id, QualifiedName after, QualifiedName before, List<Key> keys, Collection<Attribute> attributes) {
        throw new UnsupportedOperationException();
    }

    @Override
    public DictionaryMembership newDictionaryMembership(QualifiedName dict, List<Entry> keyEntitySet) {
        throw new UnsupportedOperationException();
    }

    /**
     * A factory method to create an instance of a {@link Document}
     *
     * @param namespace  the prefix namespace mapping for the current document
     * @param statements a collection of statements
     * @param bundles    a collection of bundles
     * @return an instance of {@link Document}, with this prefix-namespace mapping, statements, and bundles
     */
    @Override
    public org.openprovenance.prov.model.Document newDocument(Namespace namespace, Collection<Statement> statements, Collection<org.openprovenance.prov.model.Bundle> bundles) {
        return new Document(namespace,statements,bundles);
    }

    @Override
    public org.openprovenance.prov.model.Document newDocument(Namespace namespace, List<StatementOrBundle> statementsOrBundles) {
        return new Document(namespace,statementsOrBundles);
    }

    @Override
    public Entity newEntity(QualifiedName id, Collection<Attribute> attributes) {
        return new org.openprovenance.prov.vanilla.Entity(id,attributes);
    }

    @Override
    public HadMember newHadMember(QualifiedName c, Collection<QualifiedName> e) {
        List<org.openprovenance.prov.model.QualifiedName> ll=new LinkedList<>();
        ll.addAll(e);
        return new org.openprovenance.prov.vanilla.HadMember(c,ll);
    }

    @Override
    public MentionOf newMentionOf(QualifiedName e2, QualifiedName e1, QualifiedName b) {
        throw new UnsupportedOperationException();
    }

    /**
     * A factory method to create an instance of a Bundle {@link Bundle}
     *
     * @param id         an identifier for the bundle
     * @param namespace  a {@link Namespace} object mapping prefix to namespace URIs
     * @param statements a set of provenance descriptions
     * @return {@link Bundle}
     */
    @Override
    public Bundle newNamedBundle(QualifiedName id, Namespace namespace, Collection<Statement> statements) {
        return new Bundle(id,namespace,statements);
    }

    /**
     * A factory method for {@link QualifiedName}. A qualified name consists of a namespace, denoted by an optional prefix, and a local name.
     *
     * @param namespace a URI for the namespace
     * @param local     a local name
     * @param prefix    a string, which can be null.
     * @return an instance of {@link QualifiedName}
     */
    @Override
    public QualifiedName newQualifiedName(String namespace, String local, String prefix) {
        return new org.openprovenance.prov.vanilla.QualifiedName(namespace,local,prefix);
    }

    /**
     * A factory method for {@link QualifiedName}. A qualified name consists of a namespace, denoted by an optional prefix, and a local name.
     *
     * @param namespace a URI for the namespace
     * @param local     a local name
     * @param prefix    a string, which can be null
     * @param flag      build flag.
     * @return an instance of {@link QualifiedName}
     */
    @Override
    public QualifiedName newQualifiedName(String namespace, String local, String prefix, ProvUtilities.BuildFlag flag) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpecializationOf newSpecializationOf(QualifiedName e2, QualifiedName e1) {
        return new org.openprovenance.prov.vanilla.SpecializationOf(e2,e1);
    }

    /**
     * A factory method to create an instance of a Usage {@link org.openprovenance.prov.vanilla.Used}
     *
     * @param id         an optional identifier for a usage
     * @param activity   the identifier  of the <a href="http://www.w3.org/TR/prov-dm/#usage.activity">activity</a> that used an entity
     * @param entity     an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#usage.entity">entity</a> being used
     * @param time       an optional "usage time", the <a href="http://www.w3.org/TR/prov-dm/#usage.time">time</a> at which the entity started to be used
     * @param attributes an optional set of attribute-value pairs representing additional information about this usage
     * @return an instance of {@link org.openprovenance.prov.vanilla.Used}
     */
    @Override
    public org.openprovenance.prov.model.Used newUsed(QualifiedName id, QualifiedName activity, QualifiedName entity, XMLGregorianCalendar time, Collection<Attribute> attributes) {
        return new org.openprovenance.prov.vanilla.Used(id,activity,entity,time,attributes);
    }

    /**
     * A factory method to create an instance of an Association {@link WasAssociatedWith}
     *
     * @param id         an optional identifier for the association between an activity and an agent
     * @param activity   an identifier for the activity
     * @param agent      an optional identifier for the agent associated with the activity
     * @param plan       an optional identifier for the plan the agent relied on in the context of this activity
     * @param attributes an optional set of attribute-value pairs representing additional information about this association of this activity with this agent.
     * @return an instance of {@link WasAssociatedWith}
     */
    @Override
    public org.openprovenance.prov.model.WasAssociatedWith newWasAssociatedWith(QualifiedName id, QualifiedName activity, QualifiedName agent, QualifiedName plan, Collection<Attribute> attributes) {
        return new WasAssociatedWith(id,activity,agent,plan,attributes);
    }

    /**
     * A factory method to create an instance of an attribution {@link WasAttributedTo}
     *
     * @param id         an optional identifier for the relation
     * @param entity     an entity identifier
     * @param agent      the identifier of the agent whom the entity is ascribed to, and therefore bears some responsibility for its existence
     * @param attributes an optional set of attribute-value pairs representing additional information about this attribution.
     * @return an instance of {@link WasAttributedTo}
     */
    @Override
    public org.openprovenance.prov.model.WasAttributedTo newWasAttributedTo(QualifiedName id, QualifiedName entity, QualifiedName agent, Collection<Attribute> attributes) {
        return new WasAttributedTo(id,entity,agent,attributes);
    }

    /** A factory method to create an instance of a derivation {@link org.openprovenance.prov.model.WasDerivedFrom}
     * @param id an optional identifier for a derivation
     * @param e2 the identifier  of the <a href="http://www.w3.org/TR/prov-dm/#derivation.generatedEntity">entity generated</a> by the derivation
     * @param e1 the identifier  of the <a href="http://www.w3.org/TR/prov-dm/#derivation.usedEntity">entity used</a> by the derivation
     * @param activity an identifier for the <a href="http://www.w3.org/TR/prov-dm/#derivation.activity">activity</a> underpinning the derivation
     * @param generation an identifier for the <a href="http://www.w3.org/TR/prov-dm/#derivation.genertion">generation</a> associated with the derivation
     * @param usage an identifier for the <a href="http://www.w3.org/TR/prov-dm/#derivation.usage">usage</a> associated with the derivation
     * @param attributes an optional set of <a href="http://www.w3.org/TR/prov-dm/#end.attributes">attribute-value pairs</a> representing additional information about this derivation
     * @return an instance of {@link org.openprovenance.prov.model.WasDerivedFrom}
     */
    @Override
    public org.openprovenance.prov.model.WasDerivedFrom newWasDerivedFrom(QualifiedName id, QualifiedName e2, QualifiedName e1, QualifiedName activity, QualifiedName generation, QualifiedName usage, Collection<Attribute> attributes) {
        return new WasDerivedFrom(id,e2,e1,activity,generation,usage,attributes);
    }

    /**
     * A factory method to create an instance of an end {@link WasEndedBy}
     *
     * @param id an optional identifier for a end
     * @param activity   an identifier for the ended <a href="http://www.w3.org/TR/prov-dm/#end.activity">activity</a>
     * @param trigger    an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#end.trigger">entity triggering</a> the activity ending
     * @param ender      an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#end.ender">activity</a> that generated the (possibly unspecified) entity
     * @param time       the optional <a href="http://www.w3.org/TR/prov-dm/#end.time">time</a>  at which the activity was ended
     * @param attributes an optional set of <a href="http://www.w3.org/TR/prov-dm/#end.attributes">attribute-value pairs</a> representing additional information about this activity end
     * @return an instance of {@link WasStartedBy}
     */
    @Override
    public org.openprovenance.prov.model.WasEndedBy newWasEndedBy(QualifiedName id, QualifiedName activity, QualifiedName trigger, QualifiedName ender, XMLGregorianCalendar time, Collection<Attribute> attributes) {
        return new WasEndedBy(id,activity,trigger,ender,time,attributes);
    }

    /**
     * A factory method to create an instance of a generation {@link WasGeneratedBy}
     *
     * @param id         an optional identifier for a usage
     * @param entity     an identifier for the created <a href="http://www.w3.org/TR/prov-dm/#generation.entity">entity</a>
     * @param activity   an optional identifier  for the <a href="http://www.w3.org/TR/prov-dm/#generation.activity">activity</a> that creates the entity
     * @param time       an optional "generation time", the time at which the entity was completely created
     * @param attributes an optional set of attribute-value pairs representing additional information about this generation
     * @return an instance of {@link WasGeneratedBy}
     */
    @Override
    public org.openprovenance.prov.model.WasGeneratedBy newWasGeneratedBy(QualifiedName id, QualifiedName entity, QualifiedName activity, XMLGregorianCalendar time, Collection<Attribute> attributes) {
        return new WasGeneratedBy(id,entity,activity,time,attributes);
    }

    /**
     * A factory method to create an instance of an influence {@link WasInfluencedBy}
     *
     * @param id         optional identifier identifying the association
     * @param influencee an identifier for an entity, activity, or agent
     * @param influencer an identifier for an ancestor entity, activity, or agent that the former depends on
     * @param attributes an optional set of attribute-value pairs representing additional information about this association
     * @return an instance of {@link WasInfluencedBy}
     */
    @Override
    public org.openprovenance.prov.model.WasInfluencedBy newWasInfluencedBy(QualifiedName id, QualifiedName influencee, QualifiedName influencer, Collection<Attribute> attributes) {
        return new WasInfluencedBy(id,influencee,influencer,attributes);
    }

    /**
     * A factory method to create an instance of an communication {@link WasInformedBy}
     *
     * @param id         an optional identifier identifying the association;
     * @param informed   the identifier of the informed activity;
     * @param informant  the identifier of the informant activity;
     * @param attributes an optional set of attribute-value pairs representing additional information about this communication.
     * @return an instance of {@link WasInformedBy}
     */
    @Override
    public org.openprovenance.prov.model.WasInformedBy newWasInformedBy(QualifiedName id, QualifiedName informed, QualifiedName informant, Collection<Attribute> attributes) {
        return new WasInformedBy(id,informed,informant,attributes);
    }

    /**
     * A factory method to create an instance of an invalidation {@link WasInvalidatedBy}
     *
     * @param id         an optional identifier for a usage
     * @param entity     an identifier for the created <a href="http://www.w3.org/TR/prov-dm/#invalidation.entity">entity</a>
     * @param activity   an optional identifier  for the <a href="http://www.w3.org/TR/prov-dm/#invalidation.activity">activity</a> that creates the entity
     * @param time       an optional "invalidation time", the time at which the entity was completely created
     * @param attributes an optional set of attribute-value pairs representing additional information about this invalidation
     * @return an instance of {@link WasInvalidatedBy}
     */
    @Override
    public org.openprovenance.prov.model.WasInvalidatedBy newWasInvalidatedBy(QualifiedName id, QualifiedName entity, QualifiedName activity, XMLGregorianCalendar time, Collection<Attribute> attributes) {
        return new WasInvalidatedBy(id,entity,activity,time,attributes);
    }

    /**
     * A factory method to create an instance of a start {@link WasStartedBy}
     *
     * @param id an optional identifier for a start
     * @param activity   an identifier for the started <a href="http://www.w3.org/TR/prov-dm/#start.activity">activity</a>
     * @param trigger    an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#start.trigger">entity triggering</a> the activity
     * @param starter    an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#start.starter">activity</a> that generated the (possibly unspecified) entity
     * @param time       the optional <a href="http://www.w3.org/TR/prov-dm/#start.time">time</a>  at which the activity was started
     * @param attributes an optional set of <a href="http://www.w3.org/TR/prov-dm/#start.attributes">attribute-value pairs</a> representing additional information about this activity start
     * @return an instance of {@link WasStartedBy}
     */
    @Override
    public org.openprovenance.prov.model.WasStartedBy newWasStartedBy(QualifiedName id, QualifiedName activity, QualifiedName trigger, QualifiedName starter, XMLGregorianCalendar time, Collection<Attribute> attributes) {
        return new WasStartedBy(id,activity,trigger,starter,time,attributes);
    }

    @Override
    public void startBundle(QualifiedName bundleId, Namespace namespace) {

    }

    @Override
    public void startDocument(Namespace namespace) {

    }

    @Override
    public Role newRole(Object value, QualifiedName type) {
        return new org.openprovenance.prov.vanilla.Role(type,value);
    }

    @Override
    public Location newLocation(Object value, QualifiedName type) {
        return new org.openprovenance.prov.vanilla.Location(type,value);
    }

    @Override
    public Type newType(Object value, QualifiedName type) {
        return new org.openprovenance.prov.vanilla.Type(type,value);
    }

    @Override
    public org.openprovenance.prov.model.Label newLabel(Object value, QualifiedName type) {
        return new Label(type, value);
    }

    @Override
    public LangString newInternationalizedString(String s,String lang) {
        return new org.openprovenance.prov.vanilla.LangString(s,lang);
    }

    @Override
    public Value newValue(Object value, QualifiedName type) {
        return new org.openprovenance.prov.vanilla.Value(type,value);
    }

    @Override
    public Other newOther(QualifiedName elementName, Object value, QualifiedName type) {
        return new org.openprovenance.prov.vanilla.Other(elementName,type,value);
    }

    @Override
    public org.openprovenance.prov.model.LangString newInternationalizedString(String s) {
        return new org.openprovenance.prov.vanilla.LangString(s);
    }
}
