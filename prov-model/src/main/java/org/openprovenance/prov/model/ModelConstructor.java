package org.openprovenance.prov.model;
import java.util.Collection;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;


/** An interface for constructing concrete representations of the PROV data model */

public interface ModelConstructor {
 
    public ActedOnBehalfOf newActedOnBehalfOf(QualifiedName id, QualifiedName ag2, QualifiedName ag1, QualifiedName a, Collection<Attribute> attributes);
    public Activity newActivity(QualifiedName id, XMLGregorianCalendar startTime, XMLGregorianCalendar endTime, Collection<Attribute> attributes);
    public Agent newAgent(QualifiedName id, Collection<Attribute> attributes);
    public AlternateOf newAlternateOf(QualifiedName e2, QualifiedName e1);
    public DerivedByInsertionFrom newDerivedByInsertionFrom(QualifiedName id,
                                                            QualifiedName after,
                                                            QualifiedName before,
                                                            List<Entry> keyEntitySet,
                                                            Collection<Attribute> attributes);
    public DerivedByRemovalFrom newDerivedByRemovalFrom(QualifiedName id,
                                                        QualifiedName after,
                                                        QualifiedName before,
                                                        List<Key> keys,
                                                        Collection<Attribute> attributes);
    public DictionaryMembership newDictionaryMembership(QualifiedName dict,
							List<Entry> keyEntitySet);
    public Document newDocument(Namespace namespace,
                                Collection<Statement> statements, 
                                Collection<NamedBundle> bundles);
    public Entity newEntity(QualifiedName id, Collection<Attribute> attributes);
    public HadMember newHadMember(QualifiedName c, Collection<QualifiedName> e);
    public MentionOf newMentionOf(QualifiedName e2, QualifiedName e1, QualifiedName b);
    public NamedBundle newNamedBundle(QualifiedName id, 
                                      Namespace namespace, 
                                      Collection<Statement> statements);
    public SpecializationOf newSpecializationOf(QualifiedName e2, QualifiedName e1);
    
    /** A factory method to create an instance of a usage {@link Used}
     * @param id an optional identifier for a usage
     * @param activity the identifier  of the <a href="http://www.w3.org/TR/prov-dm/#usage.activity">activity</a> that used an entity
     * @param entity an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#usage.entity">entity</a> being used
     * @param time an optional "usage time", the time at which the entity started to be used
     * @param attributes an optional set of attribute-value pairs representing additional information about this usage
     * @return an instance of {@link Used}
     */
    public Used newUsed(QualifiedName id, QualifiedName activity, QualifiedName entity, XMLGregorianCalendar time, Collection<Attribute> attributes);
    public WasAssociatedWith newWasAssociatedWith(QualifiedName id, QualifiedName a, QualifiedName ag, QualifiedName plan, Collection<Attribute> attributes);
    public WasAttributedTo newWasAttributedTo(QualifiedName id, QualifiedName e, QualifiedName ag,  Collection<Attribute> attributes);
 
    /** A factory method to create an instance of a derivation {@link WasDerivedFrom}
     * @param id an optional identifier for a derivation
     * @param e2 the identifier  of the <a href="http://www.w3.org/TR/prov-dm/#derivation.generatedEntity">entity generated</a> by the derivation 
     * @param e1 the identifier  of the <a href="http://www.w3.org/TR/prov-dm/#derivation.usedEntity">entity used</a> by the derivation
     * @param activity
     * @param generation
     * @param usage
     * @param attributes
     * @return an instance of {@link WasDerivedFrom}
     */
    public WasDerivedFrom newWasDerivedFrom(QualifiedName id, QualifiedName e2, QualifiedName e1, QualifiedName activity, QualifiedName generation, QualifiedName usage,  Collection<Attribute> attributes);

    
    /** A factory method to create an instance of an end {@link WasEndedBy}
     * @param id
     * @param activity an identifier for the ended <a href="http://www.w3.org/TR/prov-dm/#end.activity">activity</a>
     * @param trigger an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#end.trigger">entity triggering</a> the activity ending
     * @param ender an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#end.ender">activity</a> that generated the (possibly unspecified) entity
     * @param time the optional <a href="http://www.w3.org/TR/prov-dm/#end.time">time</a>  at which the activity was ended
     * @param attributes an optional set of <a href="http://www.w3.org/TR/prov-dm/#end.attributes">attribute-value pairs</a> representing additional information about this activity end
     * @return an instance of {@link WasStartedBy}
     */

    public WasEndedBy newWasEndedBy(QualifiedName id, QualifiedName activity, QualifiedName trigger, QualifiedName ender, XMLGregorianCalendar time, Collection<Attribute> attributes);

    /** A factory method to create an instance of a generation {@link WasGeneratedBy}
     * @param id an optional identifier for a usage
     * @param entity an identifier for the created <a href="http://www.w3.org/TR/prov-dm/#generation.entity">entity</a>
     * @param activity an optional identifier  for the <a href="http://www.w3.org/TR/prov-dm/#generation.activity">activity</a> that creates the entity
     * @param time an optional "generation time", the time at which the entity was completely created
     * @param attributes an optional set of attribute-value pairs representing additional information about this generation
     * @return an instance of {@link WasGeneratedBy}
     */    


    public WasGeneratedBy newWasGeneratedBy(QualifiedName id, QualifiedName entity, QualifiedName activity, XMLGregorianCalendar time, Collection<Attribute> attributes);
    public WasInfluencedBy newWasInfluencedBy(QualifiedName id, QualifiedName a2, QualifiedName a1, Collection<Attribute> attributes);
    public WasInformedBy newWasInformedBy(QualifiedName id, QualifiedName a2, QualifiedName a1, Collection<Attribute> attributes);


    /** A factory method to create an instance of an invalidation {@link WasInvalidatedBy}
     * @param id an optional identifier for a usage
     * @param entity an identifier for the created <a href="http://www.w3.org/TR/prov-dm/#invalidation.entity">entity</a>
     * @param activity an optional identifier  for the <a href="http://www.w3.org/TR/prov-dm/#invalidation.activity">activity</a> that creates the entity
     * @param time an optional "invalidation time", the time at which the entity was completely created
     * @param attributes an optional set of attribute-value pairs representing additional information about this invalidation
     * @return an instance of {@link WasInvalidatedBy}
     */    

    public WasInvalidatedBy newWasInvalidatedBy(QualifiedName id, QualifiedName entity, QualifiedName activity, XMLGregorianCalendar time, Collection<Attribute> attributes);
    
    /** A factory method to create an instance of a start {@link WasStartedBy}
     * @param id
     * @param activity an identifier for the started <a href="http://www.w3.org/TR/prov-dm/#start.activity">activity</a>
     * @param trigger an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#start.trigger">entity triggering</a> the activity
     * @param starter an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#start.starter">activity</a> that generated the (possibly unspecified) entity
     * @param time the optional <a href="http://www.w3.org/TR/prov-dm/#start.time">time</a>  at which the activity was started
     * @param attributes an optional set of <a href="http://www.w3.org/TR/prov-dm/#start.attributes">attribute-value pairs</a> representing additional information about this activity start
     * @return an instance of {@link WasStartedBy}
     */
    public WasStartedBy newWasStartedBy(QualifiedName id, QualifiedName activity, QualifiedName trigger, QualifiedName starter, XMLGregorianCalendar time, Collection<Attribute> attributes);

    public void startBundle(QualifiedName bundleId, Namespace namespace);
    public void startDocument(Namespace namespace);
    
    
    public QualifiedName newQualifiedName(String namespace, String local, String prefix);


}
