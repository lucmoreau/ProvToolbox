package org.openprovenance.prov.core.jsonld11.serialization;

public interface Constants {
    String PROPERTY_AT_TYPE = "@type";
    String PROPERTY_AT_VALUE = "@value";

    String PROPERTY_STRING_VALUE = "@value";
    String PROPERTY_STRING_LANG = "@language";

    String PROPERTY_BLOCK_TYPE = "@type";

    String PROPERTY_PROV_TYPE = "prov:type";


    String PROPERTY_PROV_ACTIVITY = "Activity";
    String PROPERTY_PROV_AGENT = "Agent";
    String PROPERTY_PROV_ENTITY = "Entity";
    String PROPERTY_PROV_USED = "Usage";
    String PROPERTY_PROV_GENERATION = "Generation";
    String PROPERTY_PROV_ASSOCIATION = "Association";
    String PROPERTY_PROV_ATTRIBUTION = "Attribution";
    String PROPERTY_PROV_SPECIALIZATION = "provext:Specialization";
    String PROPERTY_PROV_ALTERNATE = "provext:Alternate";
    String PROPERTY_PROV_DERIVATION = "Derivation";
    String PROPERTY_PROV_COMMUNICATION = "Communication";
    String PROPERTY_PROV_INFLUENCE = "Influence";
    String PROPERTY_PROV_MEMBERSHIP = "provext:Membership";
    String PROPERTY_PROV_INVALIDATION = "Invalidation";
    String PROPERTY_PROV_START = "Start";
    String PROPERTY_PROV_END = "End";
    String PROPERTY_PROV_DELEGATION = "Delegation";
    String PROPERTY_PROV_BUNDLE = "Bundle";
    String PROPERTY_PROV_QUALIFIED_SPECIALIZATION = "provext:QualifiedSpecialization";
    String PROPERTY_PROV_QUALIFIED_ALTERNATE = "provext:QualifiedAlternate";
    String PROPERTY_PROV_QUALIFIED_MEMBERSHIP = "provext:QualifiedMembership";
    String OLD_JSONLD_CONTEXT_URL = "http://openprovenance.org/prov.jsonld";
    String JSONLD_CONTEXT_URL = "https://openprovenance.org/prov-jsonld/context.json";
}
