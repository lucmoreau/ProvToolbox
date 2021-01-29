/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.model {
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
     * &lt;complexType name="Agent"&gt;
     * &lt;complexContent&gt;
     * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     * &lt;sequence&gt;
     * &lt;element ref="{http://www.w3.org/ns/prov#}label" maxOccurs="unbounded" minOccurs="0"/&gt;
     * &lt;element ref="{http://www.w3.org/ns/prov#}location" maxOccurs="unbounded" minOccurs="0"/&gt;
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
     * @see <a href="http://www.w3.org/TR/prov-dm/#term-agent">PROV-DM Agent</a>
     * @see <a href="http://www.w3.org/TR/prov-o/#Agent">PROV-O Agent</a>
     * @see <a href="http://www.w3.org/TR/prov-n/#expression-Agent">PROV-N Agent</a>
     * @see <a href="http://www.w3.org/TR/prov-xml/#term-Agent">PROV-XML Agent</a>
     * 
     * @class
     */
    export interface Agent extends org.openprovenance.prov.model.Identifiable, org.openprovenance.prov.model.HasLabel, org.openprovenance.prov.model.HasType, org.openprovenance.prov.model.HasLocation, org.openprovenance.prov.model.HasOther, org.openprovenance.prov.model.Statement, org.openprovenance.prov.model.Element {    }
}

