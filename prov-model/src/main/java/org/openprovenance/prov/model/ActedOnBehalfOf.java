package org.openprovenance.prov.model;

/**
 * <p>Interface for the PROV Delegation association.
 * 
 * <p><a href="http://www.w3.org/TR/prov-dm/#concept-delegation">PROV-DM Definition for Delegation</a>: 
 * Delegation is the assignment of authority and responsibility to an agent (by itself or by another agent)
 *  to carry out a specific activity as a delegate or representative, while the agent it acts on behalf of 
 *  retains some responsibility for the outcome of the delegated work.
 *  
 *  <p>
 * <p><span class="strong">Relevant Factory Methods:</span>
 * <ul>
 * <li> {@link ProvFactory#newActedOnBehalfOf(QualifiedName, QualifiedName, QualifiedName)}
 * <li> {@link ProvFactory#newActedOnBehalfOf(QualifiedName, QualifiedName, QualifiedName, QualifiedName, java.util.Collection)}
 * <li> {@link ObjectFactory#createActedOnBehalfOf()}
 * </ul>
 * 
 * <p><span class="strong">Schema Definition:</span>
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Delegation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="delegate" type="{http://www.w3.org/ns/prov#}IDRef"/>
 *         &lt;element name="responsible" type="{http://www.w3.org/ns/prov#}IDRef"/>
 *         &lt;element name="activity" type="{http://www.w3.org/ns/prov#}IDRef"/>
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
 * @see <a href="http://www.w3.org/TR/prov-dm/#term-delegation">PROV-DM Delegation</a>
 * @see <a href="http://www.w3.org/TR/prov-o/#Delegation">PROV-O Delegation</a>
 * @see <a href="http://www.w3.org/TR/prov-n/#expression-delegation">PROV-N Delegation</a>
 * @see <a href="http://www.w3.org/TR/prov-xml/#term-Delegation">PROV-XML Delegation</a>
 * @see <a href="http://www.w3.org/Submission/2013/SUBM-prov-json-20130424/#term-delegation">PROV-JSON Delegation</a>
 
 * @author lavm
 *
 */


public interface ActedOnBehalfOf extends Identifiable, HasLabel, HasType, HasOther, Influence {

    QualifiedName getDelegate();

    void setActivity(QualifiedName eid2);

    void setDelegate(QualifiedName delegate);

    void setResponsible(QualifiedName responsible);

    QualifiedName getResponsible();

    QualifiedName getActivity();

}
