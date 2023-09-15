package org.openprovenance.prov.model;

import java.util.List;

/**
 * Interface for a PROV Document.
 * <p><a href="http://www.w3.org/TR/prov-dm/#concept-bundle">PROV-N Definition for Document</a>: 
 * A document is a house-keeping construct of PROV-N capable of packaging up PROV-N expressions 
 * and namespace declarations. A document forms a self-contained package of provenance descriptions 
 * for the purpose of exchanging them. A document may be used to package up PROV-N expressions in 
 * response to a request for the provenance of something.
 * 
 *  
 * <p><span class="strong">Relevant Factory Methods:</span>
 * <ul>
 * <li> {@link ProvFactory#newDocument()}
 * <li> {@link ProvFactory#newDocument(Namespace, java.util.Collection, java.util.Collection)}
 * <li> {@link ProvFactory#newDocument(java.util.Collection, java.util.Collection, java.util.Collection, java.util.Collection)}
 * </ul>
 * 
 * 
 * 
 * <p><span class="strong">Schema Definition:</span>
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Document"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;choice maxOccurs="unbounded"&gt;
 *           &lt;group ref="{http://www.w3.org/ns/prov#}documentElements"/&gt;
 *           &lt;element name="bundleContent" type="{http://www.w3.org/ns/prov#}NamedBundle"/&gt;
 *         &lt;/choice&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>

 * @author lavm
 * 
 * <p> 
 * @see <a href="http://www.w3.org/TR/prov-n/#document">PROV-N Document</a>
 * @see <a href="http://www.w3.org/TR/prov-xml/#term-Document">PROV-XML Document</a>

 *
 */
public interface Document {
    
    /** Accessor for the {@link Namespace} object containing registered prefix/namespaces for the current document.#
     * 
     * @return an instance of {@link Namespace}
     */

    Namespace getNamespace();
    
    /**
     * Gets the value of the statementOrBundle property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the statementOrBundle property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStatementOrBundle().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Entity }
     * {@link Activity }
     * {@link WasGeneratedBy }
     * {@link Used }
     * {@link WasInformedBy }
     * {@link WasStartedBy }
     * {@link WasEndedBy }
     * {@link WasInvalidatedBy }
     * {@link WasDerivedFrom }
     * {@link Agent }
     * {@link WasAttributedTo }
     * {@link WasAssociatedWith }
     * {@link ActedOnBehalfOf }
     * {@link WasInfluencedBy }
     * {@link SpecializationOf }
     * {@link AlternateOf }
     * {@link HadMember }
     * {@link MentionOf }
     * {@link DictionaryMembership }
     * {@link DerivedByInsertionFrom }
     * {@link DerivedByRemovalFrom }
     * {@link Bundle }
     * @return a list of {@link StatementOrBundle}
     * 
     */  

    List<StatementOrBundle> getStatementOrBundle();
    
    /**
     * Sets the {@link Namespace} of this document
     * @param namespace an instance {@link Namespace}
     */
    
    void setNamespace(Namespace namespace);

}
