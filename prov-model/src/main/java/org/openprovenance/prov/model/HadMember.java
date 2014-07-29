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
 * &lt;complexType name="Membership">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.w3.org/ns/prov#}AStatement">
 *       &lt;sequence>
 *         &lt;element name="collection" type="{http://www.w3.org/ns/prov#}IDRef"/>
 *         &lt;element name="entity" type="{http://www.w3.org/ns/prov#}IDRef" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
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

public interface HadMember extends Relation0 {

    void setCollection(QualifiedName collection);

    List<QualifiedName> getEntity();

    QualifiedName getCollection();
    
}
