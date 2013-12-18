package org.openprovenance.prov.model;


/**
 * This interface specifies core functionality similar to the JAXB ObjectFactory.
 * It contains a factory method for each 
 * Java element interface 
 * in the org.openprovenance.prov.model package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new org.openprovenance.prov.model.instances of the Java representation.
 * for XML content. These factory methods create empty instances, in which
 * no field has been initialized yet.
 * 
 * <p>Note that this interface differs from the ObjectFactory in prov-xml since
 * it returns instance of classes defined in package org.openprovenance.prov.model
 * 
 */

public interface ObjectFactory {

    ActedOnBehalfOf createActedOnBehalfOf();


    Activity createActivity();
     Agent createAgent();


    AlternateOf createAlternateOf();

    DerivedByInsertionFrom createDerivedByInsertionFrom();

    DerivedByRemovalFrom createDerivedByRemovalFrom();

    DictionaryMembership createDictionaryMembership();

    Document createDocument();

    Entity createEntity();


    Entry createEntry();

    HadMember createHadMember();   
    

    LangString createInternationalizedString();

    Key createKey();

    Location createLocation();

    MentionOf createMentionOf();


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

    Value createValue();

    WasAssociatedWith createWasAssociatedWith();
    
    WasAttributedTo createWasAttributedTo();

    WasDerivedFrom createWasDerivedFrom();

    WasEndedBy createWasEndedBy();

    WasGeneratedBy createWasGeneratedBy();
   
    WasInfluencedBy createWasInfluencedBy();

    WasInformedBy createWasInformedBy();

    WasInvalidatedBy createWasInvalidatedBy();

    WasStartedBy createWasStartedBy();

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Used }{@code >}}
     * 
     */
    
    /*
     * 
     *     
     JAXBElement<MentionOf> createMentionOf(MentionOf u);

    JAXBElement<Used> createUsed(Used u);
    JAXBElement<Entity> createEntity(Entity u);

    //JAXBElement<WasAssociatedWith> createWasAssociatedWith(WasAssociatedWith u);

   
    JAXBElement<ActedOnBehalfOf> createActedOnBehalfOf(ActedOnBehalfOf u);

    JAXBElement<Activity> createActivity(Activity u);
    JAXBElement<Agent> createAgent(Agent u);


    JAXBElement<WasAttributedTo> createWasAttributedTo(WasAttributedTo u);


    JAXBElement<WasDerivedFrom> createWasDerivedFrom(WasDerivedFrom u);

    JAXBElement<WasEndedBy> createWasEndedBy(WasEndedBy u);

    JAXBElement<WasGeneratedBy> createWasGeneratedBy(WasGeneratedBy u);

    JAXBElement<WasInfluencedBy> createWasInfluencedBy(WasInfluencedBy u);

    JAXBElement<WasInformedBy> createWasInformedBy(WasInformedBy u);


    JAXBElement<WasInvalidatedBy> createWasInvalidatedBy(WasInvalidatedBy u);


    JAXBElement<WasStartedBy> createWasStartedBy(WasStartedBy u);
    
    
*/

    
   
}
