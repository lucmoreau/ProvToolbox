package org.openprovenance.prov.model;



/**
 * <p>Interface for the PROV Association association.
 * 
 * <p><a href="http://www.w3.org/TR/prov-dm/#concept-association">PROV-DM Definition for Association</a>: 
 * An activity association is an assignment of responsibility to an agent for an activity, indicating that
 * the agent had a role in the activity. It further allows for a plan to be specified, which is the plan intended 
 * by the agent to achieve some goals in the context of this activity.
 *  <p>
 *  
 * 

 * <p><span class="strong">Relevant Factory Methods:</span>
 * <ul>
 * <li> {@link ProvFactory#newWasAssociatedWith(QualifiedName, QualifiedName, QualifiedName)}
 * <li> {@link ProvFactory#newWasAssociatedWith(QualifiedName, QualifiedName, QualifiedName, QualifiedName, java.util.Collection)}
 * <li> {@link ObjectFactory#createWasAssociatedWith()}
 * </ul>
 * 
 * <p><span class="strong">Schema Definition:</span>
 *  
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Association">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="activity" type="{http://www.w3.org/ns/prov#}IDRef"/>
 *         &lt;element name="agent" type="{http://www.w3.org/ns/prov#}IDRef" minOccurs="0"/>
 *         &lt;element name="plan" type="{http://www.w3.org/ns/prov#}IDRef" minOccurs="0"/>
 *         &lt;element ref="{http://www.w3.org/ns/prov#}label" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.w3.org/ns/prov#}role" maxOccurs="unbounded" minOccurs="0"/>
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
 * @see <a href="http://www.w3.org/TR/prov-dm/#term-Association">PROV-DM Association</a>
 * @see <a href="http://www.w3.org/TR/prov-o/#Association">PROV-O Association</a>
 * @see <a href="http://www.w3.org/TR/prov-n/#expression-Association">PROV-N Association</a>
 * @see <a href="http://www.w3.org/TR/prov-xml/#term-Association">PROV-XML Association</a>
 * @see <a href="http://www.w3.org/Submission/2013/SUBM-prov-json-20130424/#term-Association">PROV-JSON Association</a>
 *  
 *  
 * @author lavm
 *
 */

public interface WasAssociatedWith extends Identifiable,  HasLabel, HasType, HasRole, HasOther, Influence {

    void setActivity(QualifiedName eid2);

    void setAgent(QualifiedName eid1);

    void setPlan(QualifiedName eid);

    QualifiedName getActivity();

    QualifiedName getAgent();

    QualifiedName getPlan();

}
