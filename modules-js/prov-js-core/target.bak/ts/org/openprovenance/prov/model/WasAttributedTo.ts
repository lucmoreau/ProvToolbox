/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.model {
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
     * &lt;complexType name="Attribution"&gt;
     * &lt;complexContent&gt;
     * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     * &lt;sequence&gt;
     * &lt;element name="entity" type="{http://www.w3.org/ns/prov#}IDRef"/&gt;
     * &lt;element name="agent" type="{http://www.w3.org/ns/prov#}IDRef"/&gt;
     * &lt;element ref="{http://www.w3.org/ns/prov#}label" maxOccurs="unbounded" minOccurs="0"/&gt;
     * &lt;element ref="{http://www.w3.org/ns/prov#}type" maxOccurs="unbounded" minOccurs="0"/&gt;
     * &lt;any namespace='##other' maxOccurs="unbounded" minOccurs="0"/&gt;
     * &lt;/sequence&gt;
     * &lt;attribute ref="{http://www.w3.org/ns/prov#}id"/&gt;
     * &lt;/restriction&gt;
     * &lt;/complexContent&gt;
     * &lt;/complexType&gt;
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
     * @class
     */
    export interface WasAttributedTo extends org.openprovenance.prov.model.Identifiable, org.openprovenance.prov.model.HasLabel, org.openprovenance.prov.model.HasType, org.openprovenance.prov.model.HasOther, org.openprovenance.prov.model.Influence {
        /**
         * Set  the entity identifier.
         * 
         * @param {*} entity {@link QualifiedName} of the entity
         */
        setEntity(entity: org.openprovenance.prov.model.QualifiedName);

        /**
         * Set the identifier of the agent whom the entity is ascribed to.
         * 
         * @param {*} agent {@link QualifiedName} of the agent
         */
        setAgent(agent: org.openprovenance.prov.model.QualifiedName);

        /**
         * Gets the entity identifier.
         * 
         * @return {*} {@link QualifiedName} of the entity
         */
        getEntity(): org.openprovenance.prov.model.QualifiedName;

        /**
         * Get the identifier of the agent whom the entity is ascribed to, and therefore
         * bears some responsibility for its existence
         * @return {*} {@link QualifiedName} of the agent.
         */
        getAgent(): org.openprovenance.prov.model.QualifiedName;
    }
}

