package org.openprovenance.prov.model;



/**
 * <p>Interface for the PROV Attribution association.
 * 
 * <p><a href="http://www.w3.org/TR/prov-dm/#concept-attribution">PROV-DM Definition for Attribution</a>: 
 * Attribution is the ascribing of an entity to an agent.
 * 
 * 
 * 
 * <p><span class="strong">Relevant Factory Methods:</span>
 * <ul>
 * <li> {@link ProvFactory#newWasAttributedTo(QualifiedName, QualifiedName, QualifiedName)}
 * <li> {@link ProvFactory#newWasAttributedTo(QualifiedName, QualifiedName, QualifiedName, java.util.Collection)}
 * <li> {@link ObjectFactory#createWasAttributedTo()}
 * </ul>
 * 
 * <p><span class="strong">Schema Definition:</span>
 *  
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * 
 * <pre>
 * &lt;complexType name="Attribution">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="entity" type="{http://www.w3.org/ns/prov#}IDRef"/>
 *         &lt;element name="agent" type="{http://www.w3.org/ns/prov#}IDRef"/>
 *         &lt;element ref="{http://www.w3.org/ns/prov#}label" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.w3.org/ns/prov#}type" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;any namespace='##other' maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute ref="{http://www.w3.org/ns/prov#}id"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 *
 * <p> 
 * @see <a href="http://www.w3.org/TR/prov-dm/#term-attribution">PROV-DM Attribution</a>
 * @see <a href="http://www.w3.org/TR/prov-o/#Attribution">PROV-O Attribution</a>
 * @see <a href="http://www.w3.org/TR/prov-n/#expression-attribution">PROV-N Attribution</a>
 * @see <a href="http://www.w3.org/TR/prov-xml/#term-Attribution">PROV-XML Attribution</a>
 * @see <a href="http://www.w3.org/Submission/2013/SUBM-prov-json-20130424/#term-attribution">PROV-JSON Attribution</a>
 *   
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
