package org.openprovenance.prov.model;

import java.util.List;


/**
 * <p>Interface for PROV Dictionary Insertion.
 * 
 * <p><a href="http://www.w3.org/TR/prov-dictionary/#term-dictionary-insertion">A PROV Dictionary Definition for Insertion</a>: 
 * An Insertion relation <CODE>prov:derivedByInsertionFrom(id; d2, d1, {(key_1, e_1), ..., (key_n, e_n)})</CODE> states that 
 * d2 is the dictionary following the insertion of key-entity pairs <CODE>(key_1, e_1), ..., (key_n, e_n)</CODE> into dictionary <CODE>d1</CODE>. 
 * In other words, the set of key-entity pairs <CODE>{(key_1, e_1), ...,(key_n, e_n)}</CODE> is to be seen as the difference between 
 * <CODE>d1</CODE> and <CODE>d2</CODE>.  
 * Note that this key-entity-set is considered to be complete. This means that we assume that no unknown keys were inserted in or 
 * removed from a dictionary derived by an insertion relation. 
 * 
 * <p>
 * <p><span class="strong">Relevant Factory Methods:</span>
 * <ul>
 * <li> {@link ProvFactory#newDerivedByInsertionFrom(QualifiedName, QualifiedName, QualifiedName, List, java.util.Collection)}
 * <li> {@link ObjectFactory#createDerivedByInsertionFrom()}
 * </ul>
 * 
 * <p><span class="strong">Schema Definition:</span>
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Insertion">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.w3.org/ns/prov#}AStatement">
 *       &lt;sequence>
 *         &lt;element name="newDictionary" type="{http://www.w3.org/ns/prov#}IDRef"/>
 *         &lt;element name="oldDictionary" type="{http://www.w3.org/ns/prov#}IDRef"/>
 *         &lt;element name="keyEntityPair" type="{http://www.w3.org/ns/prov#}KeyEntityPair" maxOccurs="unbounded"/>
 *         &lt;element ref="{http://www.w3.org/ns/prov#}label" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.w3.org/ns/prov#}type" maxOccurs="unbounded" minOccurs="0"/>
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
 * @see <a href="http://www.w3.org/TR/prov-dictionary/#term-dictionary-insertion">PROV-Dictionary Insertion</a>
 * @see <a href="http://www.w3.org/TR/prov-dictionary/#Insertion">PROV-O PROV-Dictionary Insertion</a>
 * @see <a href="http://www.w3.org/TR/prov-dictionary/#expression-dictionary-insertion">PROV-N PROV-Dictionary Insertion</a>
 * @see <a href="http://www.w3.org/TR/prov-dictionary/#term-Insertion">PROV-XML PROV-Dictionary Insertion</a>
 * 
 */

public interface DerivedByInsertionFrom extends Identifiable, HasOther, HasType, HasLabel, Influence {

    void setNewDictionary(QualifiedName after);

    void setOldDictionary(QualifiedName before);

    List<Entry> getKeyEntityPair();

    QualifiedName getNewDictionary();

    QualifiedName getOldDictionary();

}
