package org.openprovenance.prov.model;

import java.util.List;

/**
 * <p>Interface for PROV Collection Membership
 * 
 * <p><a href="http://www.w3.org/TR/prov-dm/#concept-membership">PROV-DM Definition for Collection Membership</a>: 
 * Membership is the belonging of an entity to a collection.
 * 
 * <p>
 * <p><span class="strong">Relevant Factory Methods:</span>
 * <ul>
 * <li> {@link ProvFactory#newHadMember(QualifiedName, java.util.Collection)}
 * <li> {@link ProvFactory#newHadMember(QualifiedName, QualifiedName...)}
 * <li> {@link ObjectFactory#createHadMember()}
 * </ul>
 * 
 * <p><span class="strong">Schema Definition:</span>
 *
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Membership"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.w3.org/ns/prov#}AStatement"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="collection" type="{http://www.w3.org/ns/prov#}IDRef"/&gt;
 *         &lt;element name="entity" type="{http://www.w3.org/ns/prov#}IDRef" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 * <p> 
 * @see <a href="http://www.w3.org/TR/prov-dm/#term-membership">PROV-DM Membership</a>
 * @see <a href="http://www.w3.org/TR/prov-o/#hadMember">PROV-O Membship</a>
 * @see <a href="http://www.w3.org/TR/prov-n/#expression-collection-membership">PROV-N Membership</a>
 * @see <a href="http://www.w3.org/TR/prov-xml/#term-Membership">PROV-XML Membership</a>
 * @see <a href="http://www.w3.org/Submission/2013/SUBM-prov-json-20130424/#term-membership">PROV-JSON Membership</a>
 */

public interface HadMember extends Relation {

    /**
     * Get an identifier for the collection whose member is asserted
     * @return QualifiedName for the collection
     * @see <a href="http://www.w3.org/TR/prov-dm/#membership.collection">membership collection</a>
     */
    QualifiedName getCollection();

    /**
     * Get the list of identifiers of entities that are member of the collection.
     * @return a list of {@link QualifiedName}
     * 
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the entity property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEntity().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link QualifiedName }
     * @see <a href="http://www.w3.org/TR/prov-dm/#membership.entity">membership entity</a>
     * 
     * 
     */
    List<QualifiedName> getEntity();

    /**
     * Set an identifier for the collection whose member is asserted
     * @param collection QualifiedName for the collection
     * @see <a href="http://www.w3.org/TR/prov-dm/#membership.collection">membership collection</a>
     * 
     */
    void setCollection(QualifiedName collection);
    
}
