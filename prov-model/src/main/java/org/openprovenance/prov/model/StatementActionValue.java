package org.openprovenance.prov.model;


public interface StatementActionValue {

    Object doAction(Activity s);

    Object doAction(Used s);

    Object doAction(WasStartedBy s);

    Object doAction(Agent s);

    Object doAction(AlternateOf s);

    Object doAction(WasAssociatedWith s);

    Object doAction(WasAttributedTo s);

    Object doAction(WasInfluencedBy s);

    Object doAction(ActedOnBehalfOf s);

    Object doAction(WasDerivedFrom s);

    Object doAction(DictionaryMembership s);

    Object doAction(DerivedByRemovalFrom s);

    Object doAction(WasEndedBy s);

    Object doAction(Entity s);

    Object doAction(WasGeneratedBy s);

    Object doAction(WasInvalidatedBy s);

    Object doAction(HadMember s);

    Object doAction(MentionOf s);

    Object doAction(SpecializationOf s);

    Object doAction(DerivedByInsertionFrom s);

    Object doAction(WasInformedBy s);

    Object doAction(NamedBundle s, ProvUtilities provUtilities);

}
