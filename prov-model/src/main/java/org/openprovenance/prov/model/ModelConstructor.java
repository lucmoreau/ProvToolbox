package org.openprovenance.prov.model;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

/** An interface for constructing concrete representations of the PROV data model */

public interface ModelConstructor {
    
    public Entity newEntity(QName id, Collection<Attribute> attributes);
    public Activity newActivity(QName id, XMLGregorianCalendar startTime, XMLGregorianCalendar endTime, Collection<Attribute> attributes);
    public Agent newAgent(QName id, Collection<Attribute> attributes);
    public Used newUsed(QName id, QName activity, QName entity, XMLGregorianCalendar time, Collection<Attribute> attributes);
    public WasGeneratedBy newWasGeneratedBy(QName id, QName entity, QName activity, XMLGregorianCalendar time, Collection<Attribute> attributes);
    public WasInvalidatedBy newWasInvalidatedBy(QName id, QName entity, QName activity, XMLGregorianCalendar time, Collection<Attribute> attributes);
    public WasStartedBy newWasStartedBy(QName id, QName activity, QName trigger, QName starter, XMLGregorianCalendar time, Collection<Attribute> attributes);
    public WasEndedBy newWasEndedBy(QName id, QName activity, QName trigger, QName ender, XMLGregorianCalendar time, Collection<Attribute> attributes);
    public WasDerivedFrom newWasDerivedFrom(QName id, QName e2, QName e1, QName activity, QName generation, QName usage,  Collection<Attribute> attributes);
    public WasAssociatedWith newWasAssociatedWith(QName id, QName a, QName ag, QName plan, Collection<Attribute> attributes);
    public WasAttributedTo newWasAttributedTo(QName id, QName e, QName ag,  Collection<Attribute> attributes);
    public ActedOnBehalfOf newActedOnBehalfOf(QName id, QName ag2, QName ag1, QName a, Collection<Attribute> attributes);
    public WasInformedBy newWasInformedBy(QName id, QName a2, QName a1, Collection<Attribute> attributes);
    public WasInfluencedBy newWasInfluencedBy(QName id, QName a2, QName a1, Collection<Attribute> attributes);
    public AlternateOf newAlternateOf(QName e2, QName e1);
    public SpecializationOf newSpecializationOf(QName e2, QName e1);
    public MentionOf newMentionOf(QName e2, QName e1, QName b);

    public HadMember newHadMember(QName c, Collection<QName> e);

    public Document newDocument(Hashtable<String, String> namespaces,
                                Collection<Statement> statements,
                                Collection<NamedBundle> bundles);
    public NamedBundle newNamedBundle(QName id, 
                                      Hashtable<String,String> namespaces, 
                                      Collection<Statement> statements);
    public void startDocument(Hashtable<String, String> hashtable);
    public void startBundle(QName bundleId, Hashtable<String, String> namespaces);
    
    public DerivedByInsertionFrom newDerivedByInsertionFrom(QName id,
                                                            QName after,
                                                            QName before,
                                                            List<KeyQNamePair> keyEntitySet,
                                                            Collection<Attribute> attributes);

    public DerivedByRemovalFrom newDerivedByRemovalFrom(QName id,
                                                        QName after,
                                                        QName before,
                                                        List<Object> keys,
                                                        Collection<Attribute> attributes);
    public DictionaryMembership newDictionaryMembership(QName dict,
							List<KeyQNamePair> keyEntitySet);

}
