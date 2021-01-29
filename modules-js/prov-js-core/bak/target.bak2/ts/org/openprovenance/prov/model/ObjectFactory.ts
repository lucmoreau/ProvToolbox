/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.model {
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
     * @class
     */
    export interface ObjectFactory {
        createActedOnBehalfOf(): org.openprovenance.prov.model.ActedOnBehalfOf;

        createActivity(): org.openprovenance.prov.model.Activity;

        createAgent(): org.openprovenance.prov.model.Agent;

        createAlternateOf(): org.openprovenance.prov.model.AlternateOf;

        createDerivedByInsertionFrom(): org.openprovenance.prov.model.DerivedByInsertionFrom;

        createDerivedByRemovalFrom(): org.openprovenance.prov.model.DerivedByRemovalFrom;

        createDictionaryMembership(): org.openprovenance.prov.model.DictionaryMembership;

        createDocument(): org.openprovenance.prov.model.Document;

        createEntity(): org.openprovenance.prov.model.Entity;

        createEntry(): org.openprovenance.prov.model.Entry;

        createHadMember(): org.openprovenance.prov.model.HadMember;

        createInternationalizedString(): org.openprovenance.prov.model.LangString;

        createKey(): org.openprovenance.prov.model.Key;

        createLocation(): org.openprovenance.prov.model.Location;

        createMentionOf(): org.openprovenance.prov.model.MentionOf;

        createNamedBundle(): org.openprovenance.prov.model.Bundle;

        createOther(): org.openprovenance.prov.model.Other;

        createRole(): org.openprovenance.prov.model.Role;

        createSpecializationOf(): org.openprovenance.prov.model.SpecializationOf;

        createType(): org.openprovenance.prov.model.Type;

        createTypedValue(): org.openprovenance.prov.model.TypedValue;

        /**
         * Create an instance of {@link Used}
         * 
         * @return {*}
         */
        createUsed(): org.openprovenance.prov.model.Used;

        createValue(): org.openprovenance.prov.model.Value;

        createWasAssociatedWith(): org.openprovenance.prov.model.WasAssociatedWith;

        createWasAttributedTo(): org.openprovenance.prov.model.WasAttributedTo;

        createWasDerivedFrom(): org.openprovenance.prov.model.WasDerivedFrom;

        createWasEndedBy(): org.openprovenance.prov.model.WasEndedBy;

        createWasGeneratedBy(): org.openprovenance.prov.model.WasGeneratedBy;

        createWasInfluencedBy(): org.openprovenance.prov.model.WasInfluencedBy;

        createWasInformedBy(): org.openprovenance.prov.model.WasInformedBy;

        createWasInvalidatedBy(): org.openprovenance.prov.model.WasInvalidatedBy;

        createWasStartedBy(): org.openprovenance.prov.model.WasStartedBy;

        createQualifiedSpecializationOf(): org.openprovenance.prov.model.extension.QualifiedSpecializationOf;

        createQualifiedAlternateOf(): org.openprovenance.prov.model.extension.QualifiedAlternateOf;

        createQualifiedHadMember(): org.openprovenance.prov.model.extension.QualifiedHadMember;
    }
}

