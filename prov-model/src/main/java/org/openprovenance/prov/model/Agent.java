package org.openprovenance.prov.model;

/**
 * <p>Interface for the PROV Agent complex type.
 * 
 * <p><a href="http://www.w3.org/TR/prov-dm/#concept-agent">PROV-DM Definition for Agent</a>: An agent is something 
 * that bears some form of responsibility for an activity taking place, for the existence of an entity, or for another agent's activity.
 *
 *
 * <p><span class="strong">Relevant Factory Methods:</span>
 * <ul>
 * <li> {@link ProvFactory#newAgent(QualifiedName)}
 * <li> {@link ProvFactory#newAgent(QualifiedName, java.util.Collection)}
 * <li> {@link ObjectFactory#createAgent()}
 * </ul>
 * 
 * <p>The following schema fragment specifies the expected content contained within this type.
 * 
 * <pre>
 * &lt;complexType name="Agent">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.w3.org/ns/prov#}label" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.w3.org/ns/prov#}location" maxOccurs="unbounded" minOccurs="0"/>
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
 * @see <a href="http://www.w3.org/TR/prov-dm/#term-agent">PROV-DM Agent</a>
 * @see <a href="http://www.w3.org/TR/prov-o/#Agent">PROV-O Agent</a>
 * @see <a href="http://www.w3.org/TR/prov-n/#expression-Agent">PROV-N Agent</a>
 * @see <a href="http://www.w3.org/TR/prov-xml/#term-Agent">PROV-XML Agent</a>
 * 
 */

public interface Agent extends Identifiable,  HasLabel, HasType, HasLocation, HasOther, Statement, Element  {

}
