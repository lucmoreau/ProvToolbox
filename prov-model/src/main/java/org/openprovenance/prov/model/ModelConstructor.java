package org.openprovenance.prov.model;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

/** An interface for constructing concrete representations of the PROV data model */

public interface ModelConstructor {
 
    public ActedOnBehalfOf newActedOnBehalfOf(QName id, QName ag2, QName ag1, QName a, Collection<Attribute> attributes);
    public Activity newActivity(QName id, XMLGregorianCalendar startTime, XMLGregorianCalendar endTime, Collection<Attribute> attributes);
    public Agent newAgent(QName id, Collection<Attribute> attributes);
    public AlternateOf newAlternateOf(QName e2, QName e1);
    public DerivedByInsertionFrom newDerivedByInsertionFrom(QName id,
                                                            QName after,
                                                            QName before,
                                                            List<KeyQNamePair> keyEntitySet,
                                                            Collection<Attribute> attributes);
    public DerivedByRemovalFrom newDerivedByRemovalFrom(QName id,
                                                        QName after,
                                                        QName before,
                                                        List<Key> keys,
                                                        Collection<Attribute> attributes);
    public DictionaryMembership newDictionaryMembership(QName dict,
							List<KeyQNamePair> keyEntitySet);
    public Document newDocument(Hashtable<String, String> namespaces,
                                Collection<Statement> statements,
                                Collection<NamedBundle> bundles);
    public Entity newEntity(QName id, Collection<Attribute> attributes);
    public HadMember newHadMember(QName c, Collection<QName> e);
    public MentionOf newMentionOf(QName e2, QName e1, QName b);
    public NamedBundle newNamedBundle(QName id, 
                                      Hashtable<String,String> namespaces, 
                                      Collection<Statement> statements);
    public SpecializationOf newSpecializationOf(QName e2, QName e1);
    
    /** A factory method to create an instance of a usage {@link Used}
     * @param id an optional identifier for a usage
     * @param activity the identifier  of the <a href="http://www.w3.org/TR/prov-dm/#usage.activity">activity</a> that used an entity
     * @param entity an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#usage.entity">entity</a> being used
     * @param time an optional "usage time", the time at which the entity started to be used
     * @param attributes an optional set of attribute-value pairs representing additional information about this usage
     * @return an instance of {@link Used}
     */
    public Used newUsed(QName id, QName activity, QName entity, XMLGregorianCalendar time, Collection<Attribute> attributes);
    public WasAssociatedWith newWasAssociatedWith(QName id, QName a, QName ag, QName plan, Collection<Attribute> attributes);
    public WasAttributedTo newWasAttributedTo(QName id, QName e, QName ag,  Collection<Attribute> attributes);
 
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
    public WasDerivedFrom newWasDerivedFrom(QName id, QName e2, QName e1, QName activity, QName generation, QName usage,  Collection<Attribute> attributes);

    
    /** A factory method to create an instance of an end {@link WasEndedBy}
     * @param id
     * @param activity an identifier for the ended <a href="http://www.w3.org/TR/prov-dm/#end.activity">activity</a>
     * @param trigger an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#end.trigger">entity triggering</a> the activity ending
     * @param ender an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#end.ender">activity</a> that generated the (possibly unspecified) entity
     * @param time the optional <a href="http://www.w3.org/TR/prov-dm/#end.time">time</a>  at which the activity was ended
     * @param attributes an optional set of <a href="http://www.w3.org/TR/prov-dm/#end.attributes">attribute-value pairs</a> representing additional information about this activity end
     * @return an instance of {@link WasStartedBy}
     */

    public WasEndedBy newWasEndedBy(QName id, QName activity, QName trigger, QName ender, XMLGregorianCalendar time, Collection<Attribute> attributes);

    /** A factory method to create an instance of a generation {@link WasGeneratedBy}
     * @param id an optional identifier for a usage
     * @param entity an identifier for the created <a href="http://www.w3.org/TR/prov-dm/#generation.entity">entity</a>
     * @param activity an optional identifier  for the <a href="http://www.w3.org/TR/prov-dm/#generation.activity">activity</a> that creates the entity
     * @param time an optional "generation time", the time at which the entity was completely created
     * @param attributes an optional set of attribute-value pairs representing additional information about this generation
     * @return an instance of {@link WasGeneratedBy}
     */    


    public WasGeneratedBy newWasGeneratedBy(QName id, QName entity, QName activity, XMLGregorianCalendar time, Collection<Attribute> attributes);
    public WasInfluencedBy newWasInfluencedBy(QName id, QName a2, QName a1, Collection<Attribute> attributes);
    public WasInformedBy newWasInformedBy(QName id, QName a2, QName a1, Collection<Attribute> attributes);


    /** A factory method to create an instance of an invalidation {@link WasInvalidatedBy}
     * @param id an optional identifier for a usage
     * @param entity an identifier for the created <a href="http://www.w3.org/TR/prov-dm/#invalidation.entity">entity</a>
     * @param activity an optional identifier  for the <a href="http://www.w3.org/TR/prov-dm/#invalidation.activity">activity</a> that creates the entity
     * @param time an optional "invalidation time", the time at which the entity was completely created
     * @param attributes an optional set of attribute-value pairs representing additional information about this invalidation
     * @return an instance of {@link WasInvalidatedBy}
     */    

    public WasInvalidatedBy newWasInvalidatedBy(QName id, QName entity, QName activity, XMLGregorianCalendar time, Collection<Attribute> attributes);
    
    /** A factory method to create an instance of a start {@link WasStartedBy}
     * @param id
     * @param activity an identifier for the started <a href="http://www.w3.org/TR/prov-dm/#start.activity">activity</a>
     * @param trigger an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#start.trigger">entity triggering</a> the activity
     * @param starter an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#start.starter">activity</a> that generated the (possibly unspecified) entity
     * @param time the optional <a href="http://www.w3.org/TR/prov-dm/#start.time">time</a>  at which the activity was started
     * @param attributes an optional set of <a href="http://www.w3.org/TR/prov-dm/#start.attributes">attribute-value pairs</a> representing additional information about this activity start
     * @return an instance of {@link WasStartedBy}
     */
    public WasStartedBy newWasStartedBy(QName id, QName activity, QName trigger, QName starter, XMLGregorianCalendar time, Collection<Attribute> attributes);

    public void startBundle(QName bundleId, Hashtable<String, String> namespaces);
    public void startDocument(Hashtable<String, String> hashtable);

}
