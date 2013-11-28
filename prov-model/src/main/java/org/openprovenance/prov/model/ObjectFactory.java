package org.openprovenance.prov.model;

import javax.xml.bind.JAXBElement;

public interface ObjectFactory {

    ActedOnBehalfOf createActedOnBehalfOf();

    JAXBElement<ActedOnBehalfOf> createActedOnBehalfOf(ActedOnBehalfOf u);

    Activity createActivity();

    JAXBElement<Activity> createActivity(Activity u);

    Agent createAgent();

    JAXBElement<Agent> createAgent(Agent u);

    AlternateOf createAlternateOf();

    DerivedByInsertionFrom createDerivedByInsertionFrom();

    DerivedByRemovalFrom createDerivedByRemovalFrom();

    DictionaryMembership createDictionaryMembership();

    Document createDocument();

    Entity createEntity();

    JAXBElement<Entity> createEntity(Entity u);

    Entry createEntry();

    HadMember createHadMember();   
    

    InternationalizedString createInternationalizedString();

    Key createKey();

    Location createLocation();

    MentionOf createMentionOf();

    JAXBElement<MentionOf> createMentionOf(MentionOf u);

    NamedBundle createNamedBundle();

    Other createOther();

    Role createRole();

    SpecializationOf createSpecializationOf();

    Type createType();

    TypedValue createTypedValue();
    
    /**
     * Create an instance of {@link Used }
     * 
     */
    Used createUsed();
    
    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Used }{@code >}}
     * 
     */
    JAXBElement<Used> createUsed(Used u);

    Value createValue();

    WasAssociatedWith createWasAssociatedWith();

    JAXBElement<WasAssociatedWith> createWasAssociatedWith(WasAssociatedWith u);

    WasAttributedTo createWasAttributedTo();

    JAXBElement<WasAttributedTo> createWasAttributedTo(WasAttributedTo u);

    WasDerivedFrom createWasDerivedFrom();

    JAXBElement<WasDerivedFrom> createWasDerivedFrom(WasDerivedFrom u);

    WasEndedBy createWasEndedBy();

    JAXBElement<WasEndedBy> createWasEndedBy(WasEndedBy u);

    WasGeneratedBy createWasGeneratedBy();

    JAXBElement<WasGeneratedBy> createWasGeneratedBy(WasGeneratedBy u);

    WasInfluencedBy createWasInfluencedBy();

    JAXBElement<WasInfluencedBy> createWasInfluencedBy(WasInfluencedBy u);

    WasInformedBy createWasInformedBy();

    JAXBElement<WasInformedBy> createWasInformedBy(WasInformedBy u);

    WasInvalidatedBy createWasInvalidatedBy();

    JAXBElement<WasInvalidatedBy> createWasInvalidatedBy(WasInvalidatedBy u);

    WasStartedBy createWasStartedBy();

    JAXBElement<WasStartedBy> createWasStartedBy(WasStartedBy u);
    
    


    
   
}
