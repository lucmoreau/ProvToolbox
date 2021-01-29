/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.model {
    /**
     * <p>Interface for the PROV Association association.
     * 
     * <p><a href="http://www.w3.org/TR/prov-dm/#concept-association">PROV-DM Definition for Association</a>:
     * An activity association is an assignment of responsibility to an agent for an activity, indicating that
     * the agent had a role in the activity. It further allows for a plan to be specified, which is the plan intended
     * by the agent to achieve some goals in the context of this activity.
     * <p>
     * 
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
     * &lt;complexType name="Association"&gt;
     * &lt;complexContent&gt;
     * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     * &lt;sequence&gt;
     * &lt;element name="activity" type="{http://www.w3.org/ns/prov#}IDRef"/&gt;
     * &lt;element name="agent" type="{http://www.w3.org/ns/prov#}IDRef" minOccurs="0"/&gt;
     * &lt;element name="plan" type="{http://www.w3.org/ns/prov#}IDRef" minOccurs="0"/&gt;
     * &lt;element ref="{http://www.w3.org/ns/prov#}label" maxOccurs="unbounded" minOccurs="0"/&gt;
     * &lt;element ref="{http://www.w3.org/ns/prov#}role" maxOccurs="unbounded" minOccurs="0"/&gt;
     * &lt;element ref="{http://www.w3.org/ns/prov#}type" maxOccurs="unbounded" minOccurs="0"/&gt;
     * &lt;any namespace='##other' maxOccurs="unbounded" minOccurs="0"/&gt;
     * &lt;/sequence&gt;
     * &lt;attribute ref="{http://www.w3.org/ns/prov#}id"/&gt;
     * &lt;/restriction&gt;
     * &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
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
     * @class
     */
    export interface WasAssociatedWith extends org.openprovenance.prov.model.Identifiable, org.openprovenance.prov.model.HasLabel, org.openprovenance.prov.model.HasType, org.openprovenance.prov.model.HasRole, org.openprovenance.prov.model.HasOther, org.openprovenance.prov.model.Influence {
        setActivity(eid2: org.openprovenance.prov.model.QualifiedName);

        setAgent(eid1: org.openprovenance.prov.model.QualifiedName);

        setPlan(eid: org.openprovenance.prov.model.QualifiedName);

        getActivity(): org.openprovenance.prov.model.QualifiedName;

        getAgent(): org.openprovenance.prov.model.QualifiedName;

        getPlan(): org.openprovenance.prov.model.QualifiedName;
    }
}

