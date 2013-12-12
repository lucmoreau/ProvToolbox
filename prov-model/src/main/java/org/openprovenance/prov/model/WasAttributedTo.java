package org.openprovenance.prov.model;



/**
 * <p>Java interface for the PROV Attribution association.
 * 
 * <p><a href="http://www.w3.org/TR/prov-dm/#concept-attribution">PROV-DM Definition for Attribution</a>: 
 * Attribution is the ascribing of an entity to an agent.
 * 
 *  <p>
 *  
 * @author lavm
 *
 */
public interface WasAttributedTo  extends Identifiable,  HasLabel, HasType, HasOther, Influence {

    /** Set  the entity identifier.
     * 
     * @param entity {@link QualifiedName} of the entity
     */
    
    void setEntity(QualifiedName entity);

    /**  Set the identifier of the agent whom the entity is ascribed to.
     * 
     * @param agent {@link QualifiedName} of the agent
     */
    void setAgent(QualifiedName agent);

    /** Gets the entity identifier.
     * 
     * @return {@link QualifiedName} of the entity
     */
    QualifiedName getEntity();

    /** Get the identifier of the agent whom the entity is ascribed to, and therefore 
     * bears some responsibility for its existence
     * @return {@link QualifiedName} of the agent.
     */
    QualifiedName getAgent();

}
