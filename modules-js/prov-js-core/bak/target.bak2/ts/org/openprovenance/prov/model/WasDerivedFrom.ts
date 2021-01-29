/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.model {
    /**
     * <p>Interface for PROV Derivation association.
     * <p><a href="http://www.w3.org/TR/prov-dm/#concept-derivation">PROV-DM Definition for Derivation</a>: A derivation is a transformation of an entity into another,
     * an update of an entity resulting in a new one, or the construction of a new entity based on a pre-existing entity.
     * 
     * 
     * <p>The constructor method {@link ProvFactory#newWasDerivedFrom(QualifiedName, QualifiedName, QualifiedName)} can be used to create an instance of {@code WasDerivedFrom}.
     * The following code snippet creates a new derivation statement, with a type attribute.
     * The instance of derivation is given an identifier {@code myId}.
     * <pre>
     * QualifiedName myId= ... ;  // some qualified name
     * QualifiedName effectId= ... ;  // some qualified name
     * QualifiedName causeId= ... ;   // some qualified name
     * WasDerivedFrom myWasDerivedFrom=provFactory.newWasDerivedFrom(myId,effectId,causeId)
     * myWasDerivedFrom.getType().add(provFactory.newType(Name.PROV_REVISION))
     * </pre>
     * 
     * <p><span class="strong">Relevant Factory Methods:</span>
     * <ul>
     * <li> {@link ProvFactory#newWasDerivedFrom(QualifiedName, QualifiedName, QualifiedName)}
     * <li> {@link ObjectFactory#createWasDerivedFrom()}
     * </ul>
     * 
     * <p><span class="strong">Schema Definition:</span>
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType name="Derivation"&gt;
     * &lt;complexContent&gt;
     * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     * &lt;sequence&gt;
     * &lt;element name="generatedEntity" type="{http://www.w3.org/ns/prov#}IDRef"/&gt;
     * &lt;element name="usedEntity" type="{http://www.w3.org/ns/prov#}IDRef"/&gt;
     * &lt;element name="activity" type="{http://www.w3.org/ns/prov#}IDRef" minOccurs="0"/&gt;
     * &lt;element name="generation" type="{http://www.w3.org/ns/prov#}IDRef" minOccurs="0"/&gt;
     * &lt;element name="usage" type="{http://www.w3.org/ns/prov#}IDRef" minOccurs="0"/&gt;
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
     * <p>
     * @see <a href="http://www.w3.org/TR/prov-dm/#term-Derivation">PROV-DM Derivation</a>
     * @see <a href="http://www.w3.org/TR/prov-o/#Derivation">PROV-O Derivation</a>
     * @see <a href="http://www.w3.org/TR/prov-n/#expression-Derivation">PROV-N Derivation</a>
     * @see <a href="http://www.w3.org/TR/prov-xml/#term-Derivation">PROV-XML Derivation</a>
     * @see <a href="http://www.w3.org/Submission/2013/SUBM-prov-json-20130424/#term-Derivation">PROV-JSON Derivation</a>
     * 
     * 
     * @class
     */
    export interface WasDerivedFrom extends org.openprovenance.prov.model.Identifiable, org.openprovenance.prov.model.HasLabel, org.openprovenance.prov.model.HasType, org.openprovenance.prov.model.HasOther, org.openprovenance.prov.model.Influence {
        setUsedEntity(aid2: org.openprovenance.prov.model.QualifiedName);

        setGeneratedEntity(aid1: org.openprovenance.prov.model.QualifiedName);

        setActivity(aid: org.openprovenance.prov.model.QualifiedName);

        setGeneration(did1: org.openprovenance.prov.model.QualifiedName);

        setUsage(did2: org.openprovenance.prov.model.QualifiedName);

        getGeneratedEntity(): org.openprovenance.prov.model.QualifiedName;

        getGeneration(): org.openprovenance.prov.model.QualifiedName;

        getUsedEntity(): org.openprovenance.prov.model.QualifiedName;

        getUsage(): org.openprovenance.prov.model.QualifiedName;

        getActivity(): org.openprovenance.prov.model.QualifiedName;
    }
}

