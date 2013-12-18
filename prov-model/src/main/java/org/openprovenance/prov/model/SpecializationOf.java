package org.openprovenance.prov.model;

/**
 * <p>Interface for PROV Specialization association.
 * <p><a href="http://www.w3.org/TR/prov-dm/#concept-specialization">PROV-DM Definition for Specialization</a>: 
 * An entity that is a specialization ◊ of another shares all aspects of the latter, and additionally presents 
 * more specific aspects of the same thing as the latter. In particular, the lifetime of the entity being specialized 
 * contains that of any specialization.
 * 

 * <p><span class="strong">Relevant Factory Methods:</span>
 * <ul>
 * <li> {@link ProvFactory#newSpecializationOf(QualifiedName, QualifiedName)}
 * <li> {@link ObjectFactory#createSpecializationOf()}
 * </ul>
 * 
 * <p><span class="strong">Schema Definition:</span>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Specialization">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="specificEntity" type="{http://www.w3.org/ns/prov#}IDRef"/>
 *         &lt;element name="generalEntity" type="{http://www.w3.org/ns/prov#}IDRef"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 * <p> 
 * @see <a href="http://www.w3.org/TR/prov-dm/#term-specialization">PROV-DM Specialization</a>
 * @see <a href="http://www.w3.org/TR/prov-o/#specializationOf">PROV-O specializationOf</a>
 * @see <a href="http://www.w3.org/TR/prov-n/#expression-specialization">PROV-N Specialization</a>
 * @see <a href="http://www.w3.org/TR/prov-xml/#term-Specialization">PROV-XML Specialization</a>
 * @see <a href="http://www.w3.org/Submission/2013/SUBM-prov-json-20130424/#term-specialization">PROV-JSON Specialization</a>
 * 
 * @author lavm
 *
 */

public interface SpecializationOf extends Relation0 {

    /** Mutator for the  entity that is a specialization of the general entity.
     * @param specific the entity that is a specialization of the general one*/
    void setSpecificEntity(QualifiedName specific);

    /** Mutator for the  entity that is being specialized.
     * @param general the entity  that is being specialized.
     **/
    void setGeneralEntity(QualifiedName general);

    /**  Returns an identifier  of the entity that is being specialized.
     * 
     * @return {@link QualifiedName} of the entity that is being specialized.
     */
    public QualifiedName getGeneralEntity();
    
    /**  Returns an identifier  of the entity that is a specialization of the general entity.
     * 
     * @return {@link QualifiedName} of the entity that is a specialization of the general one.
     */
    public QualifiedName getSpecificEntity();

}
