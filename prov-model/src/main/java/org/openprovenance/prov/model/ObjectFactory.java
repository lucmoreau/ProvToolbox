package org.openprovenance.prov.model;

import javax.xml.bind.JAXBElement;

public interface ObjectFactory {

    ActedOnBehalfOf createActedOnBehalfOf();

    Activity createActivity();

    IDRef createIDRef();

    Agent createAgent();

    AlternateOf createAlternateOf();

    Location createLocation();

    Role createRole();

    Value createValue();

    DictionaryMembership createDictionaryMembership();

    DerivedByInsertionFrom createDerivedByInsertionFrom();

    DerivedByRemovalFrom createDerivedByRemovalFrom();

    Document createDocument();

    JAXBElement<ActedOnBehalfOf> createActedOnBehalfOf(ActedOnBehalfOf u);

    JAXBElement<Activity> createActivity(Activity u);

    Type createType();
    
    

    JAXBElement<Agent> createAgent(Agent u);

    JAXBElement<Entity> createEntity(Entity u);

    JAXBElement<MentionOf> createMentionOf(MentionOf u);

    JAXBElement<Used> createUsed(Used u);

    JAXBElement<WasAssociatedWith> createWasAssociatedWith(WasAssociatedWith u);

    JAXBElement<WasAttributedTo> createWasAttributedTo(WasAttributedTo u);

    JAXBElement<WasDerivedFrom> createWasDerivedFrom(WasDerivedFrom u);

    JAXBElement<WasEndedBy> createWasEndedBy(WasEndedBy u);

    JAXBElement<WasGeneratedBy> createWasGeneratedBy(WasGeneratedBy u);

    JAXBElement<WasInfluencedBy> createWasInfluencedBy(WasInfluencedBy u);

    JAXBElement<WasInformedBy> createWasInformedBy(WasInformedBy u);

    JAXBElement<WasInvalidatedBy> createWasInvalidatedBy(WasInvalidatedBy u);

    JAXBElement<WasStartedBy> createWasStartedBy(WasStartedBy u);

    Entity createEntity();

    Entry createEntry();

    HadMember createHadMember();

    InternationalizedString createInternationalizedString();

    MentionOf createMentionOf();

    NamedBundle createNamedBundle();

    SpecializationOf createSpecializationOf();

    Used createUsed();

    WasAssociatedWith createWasAssociatedWith();

    WasAttributedTo createWasAttributedTo();

    WasDerivedFrom createWasDerivedFrom();

    WasEndedBy createWasEndedBy();

    WasGeneratedBy createWasGeneratedBy();

    WasInfluencedBy createWasInfluencedBy();

    WasInformedBy createWasInformedBy();

    WasInvalidatedBy createWasInvalidatedBy();

    WasStartedBy createWasStartedBy();
    
    


    
   
}
