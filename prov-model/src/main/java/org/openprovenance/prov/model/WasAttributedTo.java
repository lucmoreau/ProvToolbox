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

    void setEntity(QualifiedName eid);

    void setAgent(QualifiedName agid);

    QualifiedName getEntity();

    QualifiedName getAgent();

}
