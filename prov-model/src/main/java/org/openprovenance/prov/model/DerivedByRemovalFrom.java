package org.openprovenance.prov.model;

import java.util.List;


/**
 * <p>Interface for PROV Dictionary Removal.
 * 
 * <p><a href="http://www.w3.org/TR/prov-dictionary/#term-dictionary-removal">A PROV Dictionary Definition for Removal</a>: 
 * A Removal relation <CODE>prov:derivedByRemovalFrom(id; d2,d1, {key_1, ..., key_n})</CODE> states that 
 * <CODE>d2</CODE> is the dictionary following the removal of the set of pairs corresponding to keys 
 * <CODE>key_1...key_n</CODE> from <CODE>d1</CODE>. In other words, the key-set <CODE>{key_1,...,key_n}</CODE> 
 * is to be seen as the difference in keys and corresponding entities between <CODE>d1</CODE> and <CODE>d2</CODE>. 
 * Note that this key-set is considered to be complete. This means that we assume that no unknown keys were 
 * inserted in or removed from a dictionary derived by a removal relation. 
 * 
 * <p>
 * <p><span class="strong">Relevant Factory Methods:</span>
 * <ul>
 * <li> {@link ProvFactory#newDerivedByRemovalFrom(QualifiedName, QualifiedName, QualifiedName, List, java.util.Collection)}
 * <li> {@link ObjectFactory#createDerivedByRemovalFrom()}
 * </ul>
 * 
 * <p><span class="strong">Schema Definition:</span>
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Removal">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.w3.org/ns/prov#}AStatement">
 *       &lt;sequence>
 *         &lt;element name="newDictionary" type="{http://www.w3.org/ns/prov#}IDRef"/>
 *         &lt;element name="oldDictionary" type="{http://www.w3.org/ns/prov#}IDRef"/>
 *         &lt;element name="key" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" maxOccurs="unbounded"/>
 *         &lt;element ref="{http://www.w3.org/ns/prov#}label" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.w3.org/ns/prov#}type" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.w3.org/ns/prov#}others" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;any processContents='lax' namespace='##other' maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute ref="{http://www.w3.org/ns/prov#}id"/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 * <p> 
 * @see <a href="http://www.w3.org/TR/prov-dictionary/#term-dictionary-removal">PROV-Dictionary Removal</a>
 * @see <a href="http://www.w3.org/TR/prov-dictionary/#Removal">PROV-O PROV-Dictionary Removal</a>
 * @see <a href="http://www.w3.org/TR/prov-dictionary/#expression-dictionary-removal">PROV-N PROV-Dictionary Removal</a>
 * @see <a href="http://www.w3.org/TR/prov-dictionary/#term-Removal">PROV-XML PROV-Dictionary Removal</a>
 * 
 */

public interface DerivedByRemovalFrom extends Identifiable, HasType, HasLabel, Influence {

    /**
     * Get the set of deleted keys.
     * @return A list of keys
     * @see <a href="http://www.w3.org/TR/prov-dictionary/#removal.key-set">Removal Key-Set</a>
     *
     */
    List<Key> getKey();

    /**
     * Get an identifier for the dictionary after the deletion
     * @return a QualifiedName
     * @see <a href="http://www.w3.org/TR/prov-dictionary/#removal.after">Removal After</a>
     */
    QualifiedName getNewDictionary();
    
    /**
     * Get an identifier for the dictionary before the deletion
     * @return a QualifiedName
     * @see <a href="http://www.w3.org/TR/prov-dictionary/#removal.before">Removal Before</a>
     */
    QualifiedName getOldDictionary();
    
    /**
     * Set an identifier for the dictionary after the deletion.
     * @param after QualifiedName of the dictionary after the deletion
     * @see <a href="http://www.w3.org/TR/prov-dictionary/#removal.after">Removal After</a>
     */

    void setNewDictionary(QualifiedName after);

    /**
     * Set an identifier for the dictionary before the deletion.
     * @param before QualifiedName of the dictionary before the deletion
     * @see <a href="http://www.w3.org/TR/prov-dictionary/#removal.before">Removal Before</a>
     */
    void setOldDictionary(QualifiedName before);

}
