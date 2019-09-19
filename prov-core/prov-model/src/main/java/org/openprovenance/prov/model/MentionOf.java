package org.openprovenance.prov.model;



/**
 * <p>Interface for PROV MentionOf association.
 * <p><a href="http://www.w3.org/TR/prov-links/#concept-mention">PROV-Links Definition for Mention</a>: 
 * The mention of an entity in a bundle (containing a description of this entity) is another entity 
 * that is a specialization of the former and that presents at least the bundle as a further additional aspect.
 * 
 *
 * <p><span class="strong">Relevant Factory Methods:</span>
 * <ul>
 * <li> {@link ProvFactory#newMentionOf(QualifiedName, QualifiedName, QualifiedName)}
 * <li> {@link ObjectFactory#createMentionOf()}
 * </ul>
 * 
 * <p><span class="strong">Schema Definition:</span> 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Mention"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.w3.org/ns/prov#}AStatement"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="specificEntity" type="{http://www.w3.org/ns/prov#}IDRef"/&gt;
 *         &lt;element name="generalEntity" type="{http://www.w3.org/ns/prov#}IDRef"/&gt;
 *         &lt;element name="bundle" type="{http://www.w3.org/ns/prov#}IDRef"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 * <p> 
 * @see <a href="http://www.w3.org/TR/prov-links/#dfn-mentionof">PROV Data Model Mention</a>
 * @see <a href="http://www.w3.org/TR/prov-dm/#dfn-specializationof">PROV DM Specialization</a>
 * @see <a href="http://www.w3.org/TR/prov-links/#mentionOf">PROV-O mentionOf</a>
 * @see <a href="http://www.w3.org/TR/prov-links/#prod-mentionExpression">PROV-N Mention</a>
 * @see <a href="http://www.w3.org/TR/prov-links/#mention-xml">PROV-XML Mention</a>
 * 
 * @author lavm
 *
 */
public interface MentionOf extends Relation {

    /**
     * Get an identifier of a bundle that contains a description of "general entity" 
       and further constitutes one additional aspect presented by "specific entity".
     * @return a bundle's {@link QualifiedName}
     * @see <a href="http://www.w3.org/TR/prov-links/#mention.bundle">mention bundle</a>
     */
    QualifiedName getBundle();
    
    /**
     * Get an identifier for an entity that is described in the bundle.
     * @return an entity's {@link QualifiedName}
     * @see <a href="http://www.w3.org/TR/prov-links/#mention.generalEntity">mention generalEntity</a>
     */
    QualifiedName getGeneralEntity();

    
    /**
     * Get an identifier of the entity that is a mention of the general entity.
     * @return an entity's {@link QualifiedName}
     * @see <a href="http://www.w3.org/TR/prov-links/#mention.specificEntity">mention specificEntity</a>
     */
    QualifiedName getSpecificEntity();

    /**
     * Set a bundle's identifier
     * @param bundle a {@link QualifiedName} of a bundle
     * @see <a href="http://www.w3.org/TR/prov-links/#mention.bundle">mention bundle</a>
     */
    void setBundle(QualifiedName bundle);

    /**
     * Set the general entity's identifier
     * @param supra an entity's {@link QualifiedName}
     * @see <a href="http://www.w3.org/TR/prov-links/#mention.generalEntity">mention generalEntity</a>
     */
    void setGeneralEntity(QualifiedName supra);

    /**
     * Se the specific entity's identifier
     * @param infra an entity's {@link QualifiedName}
     * @see <a href="http://www.w3.org/TR/prov-links/#mention.specificEntity">mention specificEntity</a>
     */
    void setSpecificEntity(QualifiedName infra);

}
