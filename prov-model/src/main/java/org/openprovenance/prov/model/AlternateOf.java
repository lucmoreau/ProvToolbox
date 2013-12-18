package org.openprovenance.prov.model;

/**
 * <p>Interface for PROV Alternate association.
 * <p><a href="http://www.w3.org/TR/prov-dm/#concept-alternate">PROV-DM Definition for Alternate</a>: Two alternate 
 * entities present aspects of the same thing. These aspects may be the same or different, and the alternate entities 
 * may or may not overlap in time.
 * <p>Note that alternateOf is a necessarily very general relationship that, in reasoning, only states that the two 
 * alternate entities respectively fix some aspects of some common thing (possibly evolving over time), and so there 
 * is some relevant connection between the provenance of the alternates. In a specific application context, 
 * alternateOf, or a subtype of it, could allow more inferences.
 * 
 * <p><span class="strong">Relevant Factory Methods:</span>
 * <ul>
 * <li> {@link ProvFactory#newAlternateOf(QualifiedName, QualifiedName)}
 * <li> {@link ObjectFactory#createAlternateOf()}
 * </ul>
 * 
 * 
 * <p><span class="strong">Schema Definition:</span>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Alternate">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="alternate1" type="{http://www.w3.org/ns/prov#}IDRef"/>
 *         &lt;element name="alternate2" type="{http://www.w3.org/ns/prov#}IDRef"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 * <p> 
 * @see <a href="http://www.w3.org/TR/prov-dm/#term-alternate">PROV-DM Alternate</a>
 * @see <a href="http://www.w3.org/TR/prov-o/#alternateOf">PROV-O alternateOf</a>
 * @see <a href="http://www.w3.org/TR/prov-n/#expression-alternate">PROV-N Alternate</a>
 * @see <a href="http://www.w3.org/TR/prov-xml/#term-Alternate">PROV-XML Alternate</a>
 * @see <a href="http://www.w3.org/Submission/2013/SUBM-prov-json-20130424/#term-alternate">PROV-JSON Alternate</a>
 * 
 * @author lavm
 *
 */
public interface AlternateOf extends Relation0 {
    
    /** Sets the identifier for the first of the two entities.
     * 
     * @param entity a {@link QualifiedName} for second entity
     */
    void setAlternate1(QualifiedName entity);

    /** Sets the identifier for the second of the two entities.
     * 
     * @param entity a {@link QualifiedName} for second entity
     */
    void setAlternate2(QualifiedName entity);

    /** Returns the identifier for the first of the two entities.
     * 
     * @return a {@link QualifiedName}
     */
    QualifiedName getAlternate1();

    /** Returns the identifier for the second of the two entities.
     * 
     * @return a {@link QualifiedName}
     */
    QualifiedName getAlternate2();

}
