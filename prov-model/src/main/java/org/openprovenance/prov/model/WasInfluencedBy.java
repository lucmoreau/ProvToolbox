package org.openprovenance.prov.model;

/**
 * <p>Interface for the PROV Influence association.
 * 
 * <p><a href="http://www.w3.org/TR/prov-dm/#concept-influence">PROV-DM Definition for Influence</a>: 
 * Influence is the capacity of an entity, activity, or agent to have an effect on the character, 
 * development, or behavior of another by means of usage, start, end, generation, invalidation, 
 * communication, derivation, attribution, association, or delegation.
 *  
 * <p>
 * 
 * <p><span class="strong">Relevant Factory Methods:</span>
 * <ul>
 * <li> {@link ProvFactory#newWasInfluencedBy(QualifiedName, QualifiedName, QualifiedName)}
 * <li> {@link ProvFactory#newWasInfluencedBy(QualifiedName, QualifiedName, QualifiedName, java.util.Collection)}
 * <li> {@link ObjectFactory#createWasInfluencedBy()}
 * </ul>
 * 
 * <p><span class="strong">Schema Definition:</span>
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 *   
 * <pre>
 * &lt;complexType name="Influence"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="influencee" type="{http://www.w3.org/ns/prov#}IDRef"/&gt;
 *         &lt;element name="influencer" type="{http://www.w3.org/ns/prov#}IDRef"/&gt;
 *         &lt;element ref="{http://www.w3.org/ns/prov#}label" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.w3.org/ns/prov#}type" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;any namespace='##other' maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute ref="{http://www.w3.org/ns/prov#}id"/&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 * <p> 
 * @see <a href="http://www.w3.org/TR/prov-dm/#term-influence">PROV-DM Influence</a>
 * @see <a href="http://www.w3.org/TR/prov-o/#Influence">PROV-O Influence</a>
 * @see <a href="http://www.w3.org/TR/prov-n/#expression-influence">PROV-N Influence</a>
 * @see <a href="http://www.w3.org/TR/prov-xml/#term-Influence">PROV-XML Influence</a>
 * @see <a href="http://www.w3.org/Submission/2013/SUBM-prov-json-20130424/#term-influence">PROV-JSON Influence</a>
 *
 * @author lavm
 *
 */


public interface WasInfluencedBy extends Identifiable,  HasLabel, HasType, HasOther, Influence {

    void setInfluencee(QualifiedName influencee);

    void setInfluencer(QualifiedName influencer);

    QualifiedName getInfluencee();

    QualifiedName getInfluencer();

}
