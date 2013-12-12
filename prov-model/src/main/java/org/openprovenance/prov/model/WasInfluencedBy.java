package org.openprovenance.prov.model;

/**
 * <p>Java interface for the PROV Influence association.
 * 
 * <p><a href="http://www.w3.org/TR/prov-dm/#concept-influence">PROV-DM Definition for Influence</a>: 
 * Influence is the capacity of an entity, activity, or agent to have an effect on the character, 
 * development, or behavior of another by means of usage, start, end, generation, invalidation, 
 * communication, derivation, attribution, association, or delegation.
 *  
 *  <p>
 *  
 * @author lavm
 *
 */


public interface WasInfluencedBy extends Identifiable,  HasLabel, HasType, HasOther, Influence {

    void setInfluencee(QualifiedName influencee);

    void setInfluencer(QualifiedName influencer);

    QualifiedName getInfluencee();

    QualifiedName getInfluencer();

}
