package org.openprovenance.prov.model;

/**
 * <p>Interface to denote a relation that carries some influence.
 * 
 * <p><a href="http://www.w3.org/TR/prov-dm/#concept-influence">PROV-DM Definition for Influence</a>: 
 * Influence is the capacity of an entity, activity, or agent to have an effect on the character, 
 * development, or behavior of another by means of usage, start, end, generation, invalidation, 
 * communication, derivation, attribution, association, or delegation.
 * 
 * <p>This interface is implemented by a collection of PROV statements, including  {@link WasInfluencedBy}.
 * 
 * @author lavm
 *
 */

public interface Influence extends Identifiable, HasOther, Relation0  {
    //    Ref getCause();
    //    Ref getEffect();
} 