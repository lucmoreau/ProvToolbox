/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.model {
    /**
     * <p>Interface for the PROV Entity complex type.
     * 
     * <p><a href="http://www.w3.org/TR/prov-dm/#concept-entity">PROV-DM Definition for Entity</a>: An entity is a physical, digital, conceptual, or other kind of thing with some fixed aspects; entities may be real or imaginary.
     * 
     * 
     * <p>The constructor method {@link ProvFactory#newEntity(QualifiedName)} can be used to create an instance of {@code Entity}.
     * The following code snippet creates a new entity statement, with a location attribute
     * "London".  The entity is given an identifier {@code myId}.
     * <pre>
     * QualifiedName myId= ... ;  // some qualified name
     * Entity myEntity=provFactory.newEntity(myId)
     * myEntity.getLocation().add(provFactory.newLocation("London"))
     * </pre>
     * 
     * <p><span class="strong">Relevant Factory Methods:</span>
     * <ul>
     * <li> {@link ProvFactory#newEntity(QualifiedName)}
     * <li> {@link ProvFactory#newEntity(QualifiedName, java.util.Collection)}
     * <li> {@link ObjectFactory#createEntity()}
     * </ul>
     * 
     * <p><span class="strong">Schema Definition:</span>
     * <p>The following schema fragment specifies the expected content contained within this type.
     * <pre>
     * &lt;complexType name="Entity"&gt;
     * &lt;complexContent&gt;
     * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     * &lt;sequence&gt;
     * &lt;element ref="{http://www.w3.org/ns/prov#}label" maxOccurs="unbounded" minOccurs="0"/&gt;
     * &lt;element ref="{http://www.w3.org/ns/prov#}location" maxOccurs="unbounded" minOccurs="0"/&gt;
     * &lt;element ref="{http://www.w3.org/ns/prov#}type" maxOccurs="unbounded" minOccurs="0"/&gt;
     * &lt;element ref="{http://www.w3.org/ns/prov#}value" minOccurs="0"/&gt;
     * &lt;any processContents='skip' namespace='##other' maxOccurs="unbounded" minOccurs="0"/&gt;
     * &lt;/sequence&gt;
     * &lt;attribute ref="{http://www.w3.org/ns/prov#}id"/&gt;
     * &lt;/restriction&gt;
     * &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * <p>
     * @see <a href="http://www.w3.org/TR/prov-dm/#term-entity">PROV-DM Entity</a>
     * @see <a href="http://www.w3.org/TR/prov-o/#Entity">PROV-O Entity</a>
     * @see <a href="http://www.w3.org/TR/prov-n/#expression-Entity">PROV-N Entity</a>
     * @see <a href="http://www.w3.org/TR/prov-xml/#term-Entity">PROV-XML Entity</a>
     * @see <a href="http://www.w3.org/Submission/2013/SUBM-prov-json-20130424/#term-entity">PROV-JSON Entity</a>
     * 
     * @class
     */
    export interface Entity extends org.openprovenance.prov.model.Identifiable, org.openprovenance.prov.model.HasLabel, org.openprovenance.prov.model.HasType, org.openprovenance.prov.model.HasLocation, org.openprovenance.prov.model.Statement, org.openprovenance.prov.model.HasValue, org.openprovenance.prov.model.HasOther, org.openprovenance.prov.model.Element {    }
}

