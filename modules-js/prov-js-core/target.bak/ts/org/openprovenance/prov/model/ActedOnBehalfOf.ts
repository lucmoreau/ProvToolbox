/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.model {
    /**
     * <p>Interface for the PROV Delegation association.
     * 
     * <p><a href="http://www.w3.org/TR/prov-dm/#concept-delegation">PROV-DM Definition for Delegation</a>:
     * Delegation is the assignment of authority and responsibility to an agent (by itself or by another agent)
     * to carry out a specific activity as a delegate or representative, while the agent it acts on behalf of
     * retains some responsibility for the outcome of the delegated work.
     * 
     * <p>
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
     * &lt;complexType name="Delegation"&gt;
     * &lt;complexContent&gt;
     * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     * &lt;sequence&gt;
     * &lt;element name="delegate" type="{http://www.w3.org/ns/prov#}IDRef"/&gt;
     * &lt;element name="responsible" type="{http://www.w3.org/ns/prov#}IDRef"/&gt;
     * &lt;element name="activity" type="{http://www.w3.org/ns/prov#}IDRef"/&gt;
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
     * 
     * <p>
     * @see <a href="http://www.w3.org/TR/prov-dm/#term-delegation">PROV-DM Delegation</a>
     * @see <a href="http://www.w3.org/TR/prov-o/#Delegation">PROV-O Delegation</a>
     * @see <a href="http://www.w3.org/TR/prov-n/#expression-delegation">PROV-N Delegation</a>
     * @see <a href="http://www.w3.org/TR/prov-xml/#term-Delegation">PROV-XML Delegation</a>
     * @see <a href="http://www.w3.org/Submission/2013/SUBM-prov-json-20130424/#term-delegation">PROV-JSON Delegation</a>
     * 
     * @author lavm
     * @class
     */
    export interface ActedOnBehalfOf extends org.openprovenance.prov.model.Identifiable, org.openprovenance.prov.model.HasLabel, org.openprovenance.prov.model.HasType, org.openprovenance.prov.model.HasOther, org.openprovenance.prov.model.Influence {
        /**
         * Get the activity identifier. This is the activity for which the delegation link holds.
         * @return {*} the activity QualifiedName
         * @see <a href="http://www.w3.org/TR/prov-dm/#delegation.activity">delegation activity</a>
         */
        getActivity(): org.openprovenance.prov.model.QualifiedName;

        /**
         * Get the delegate identifier. The delegate is the agent associated with an activity, acting on behalf of the responsible agent.
         * @return {*} a QualifiedName
         * @see <a href="http://www.w3.org/TR/prov-dm/#delegation.delegate">delegation delegate</a>
         */
        getDelegate(): org.openprovenance.prov.model.QualifiedName;

        /**
         * Get the responsible identifier. The responsible is the agent, on behalf of which the delegate agent acted.
         * @return {*} a QualifiedName
         * @see <a href="http://www.w3.org/TR/prov-dm/#delegation.responsible">delegation responsible</a>
         */
        getResponsible(): org.openprovenance.prov.model.QualifiedName;

        /**
         * Set the activity identifier.
         * @param {*} activity QualifiedName of the activity
         * @see <a href="http://www.w3.org/TR/prov-dm/#delegation.activity">delegation activity</a>
         */
        setActivity(activity: org.openprovenance.prov.model.QualifiedName);

        /**
         * Set the delegate identifier.
         * @param {*} delegate QualifiedName of the delegate agent.
         * @see <a href="http://www.w3.org/TR/prov-dm/#delegation.delegate">delegation delegate</a>
         */
        setDelegate(delegate: org.openprovenance.prov.model.QualifiedName);

        /**
         * Set the responsible identifier
         * @param {*} responsible QualifiedName of the responsible agent.
         * @see <a href="http://www.w3.org/TR/prov-dm/#delegation.responsible">delegation responsible</a>
         */
        setResponsible(responsible: org.openprovenance.prov.model.QualifiedName);
    }
}

