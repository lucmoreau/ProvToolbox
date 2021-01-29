/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.model {
    /**
     * <p>Interface for PROV Specialization association.
     * <p><a href="http://www.w3.org/TR/prov-dm/#concept-specialization">PROV-DM Definition for Specialization</a>:
     * An entity that is a specialization ◊ of another shares all aspects of the latter, and additionally presents
     * more specific aspects of the same thing as the latter. In particular, the lifetime of the entity being specialized
     * contains that of any specialization.
     * 
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
     * &lt;complexType name="Specialization"&gt;
     * &lt;complexContent&gt;
     * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     * &lt;sequence&gt;
     * &lt;element name="specificEntity" type="{http://www.w3.org/ns/prov#}IDRef"/&gt;
     * &lt;element name="generalEntity" type="{http://www.w3.org/ns/prov#}IDRef"/&gt;
     * &lt;/sequence&gt;
     * &lt;/restriction&gt;
     * &lt;/complexContent&gt;
     * &lt;/complexType&gt;
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
     * @class
     */
    export interface SpecializationOf extends org.openprovenance.prov.model.Relation, org.openprovenance.prov.model.UnqualifiedRelation {
        /**
         * Mutator for the  entity that is a specialization of the general entity.
         * @param {*} specific the entity that is a specialization of the general one
         */
        setSpecificEntity(specific: org.openprovenance.prov.model.QualifiedName);

        /**
         * Mutator for the  entity that is being specialized.
         * @param {*} general the entity  that is being specialized.
         */
        setGeneralEntity(general: org.openprovenance.prov.model.QualifiedName);

        /**
         * Returns an identifier  of the entity that is being specialized.
         * 
         * @return {*} {@link QualifiedName} of the entity that is being specialized.
         */
        getGeneralEntity(): org.openprovenance.prov.model.QualifiedName;

        /**
         * Returns an identifier  of the entity that is a specialization of the general entity.
         * 
         * @return {*} {@link QualifiedName} of the entity that is a specialization of the general one.
         */
        getSpecificEntity(): org.openprovenance.prov.model.QualifiedName;
    }
}

