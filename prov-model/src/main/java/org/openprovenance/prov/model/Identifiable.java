package org.openprovenance.prov.model;

/**
 * <p>Interface for what can be identified in PROV.
 * <p><a href="http://www.w3.org/TR/prov-dm/#dfn-identifier">PROV-DM Definition for Identifier</a>: An identifier is a qualified name.
 * <p>Entity, Activity, and Agent have a mandatory identifier. Two entities (resp. activities, agents) are equal if they have the same identifier.
 * <p>Generation, Usage, Communication, Start, End, Invalidation, Derivation, Attribution, Association, Delegation, Influence have an optional identifier. 
 * Two generations (resp. usages, communications, etc.) are equal if they have the same identifier.
 * 
 * <p><span class="strong">Relevant class</span>
 * <ul>
 * <li>{@link QualifiedName}
 * </ul>
 * <p>
 * 
 * @author lavm
 *
 */
public interface Identifiable {
    /**
     * Gets the value of the id property.  A null value means that the object has not been identified.  {@link Entity}, {@link Activity}, 
     * {@link Agent} have a non-null identifier.
     * 
     * @return
     *     possible object is
     *     {@link QualifiedName }
     *     
     */
    public QualifiedName getId();

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link QualifiedName }
     *     
     */
    public void setId(QualifiedName value);
} 
