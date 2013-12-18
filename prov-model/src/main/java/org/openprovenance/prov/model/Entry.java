package org.openprovenance.prov.model;

/**
 * Interface for Key-Entity entry in a PROV Dictionary.
 * <p>Key-entity pairs are used to identify the members of a dictionary.
 * 
 * 
 * <p><span class="strong">Relevant Factory Methods:</span>
 * <ul>
 * <li> {@link ProvFactory#newEntry(Key,QualifiedName)}
 * <li> {@link ObjectFactory#createEntry()}
 * </ul>
 * 
 * <p><span class="strong">Schema Definition:</span>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="KeyEntityPair">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="key" type="{http://www.w3.org/ns/prov#}TypedValue"/>
 *         &lt;element name="entity" type="{http://www.w3.org/ns/prov#}IDRef"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * @see <a href="http://www.w3.org/TR/prov-dictionary/#KeyEntityPair">Key-Entity Pair in PROV-O (see PROV-DICTIONARY)</a>
 * @see <a href="http://www.w3.org/TR/prov-dictionary/#term-KeyEntityPair">Key-Entity Pair in PROV-XML (see PROV-DICTIONARY)</a>
 * @see <a href="http://www.w3.org/TR/prov-dictionary/#prod-keyEntityPair">Key-Entity Pair in PROV-N (see PROV-DICTIONARY)</a>
 * 
 * @author lavm
 *
 */
public interface Entry {

    void setKey(Key key);

    void setEntity(QualifiedName entity);


    Key getKey();

    QualifiedName getEntity();

}
