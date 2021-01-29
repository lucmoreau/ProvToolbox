/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.model {
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
     * &lt;complexType name="Removal"&gt;
     * &lt;complexContent&gt;
     * &lt;extension base="{http://www.w3.org/ns/prov#}AStatement"&gt;
     * &lt;sequence&gt;
     * &lt;element name="newDictionary" type="{http://www.w3.org/ns/prov#}IDRef"/&gt;
     * &lt;element name="oldDictionary" type="{http://www.w3.org/ns/prov#}IDRef"/&gt;
     * &lt;element name="key" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" maxOccurs="unbounded"/&gt;
     * &lt;element ref="{http://www.w3.org/ns/prov#}label" maxOccurs="unbounded" minOccurs="0"/&gt;
     * &lt;element ref="{http://www.w3.org/ns/prov#}type" maxOccurs="unbounded" minOccurs="0"/&gt;
     * &lt;element ref="{http://www.w3.org/ns/prov#}others" maxOccurs="unbounded" minOccurs="0"/&gt;
     * &lt;any processContents='lax' namespace='##other' maxOccurs="unbounded" minOccurs="0"/&gt;
     * &lt;/sequence&gt;
     * &lt;attribute ref="{http://www.w3.org/ns/prov#}id"/&gt;
     * &lt;/extension&gt;
     * &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     * <p>
     * @see <a href="http://www.w3.org/TR/prov-dictionary/#term-dictionary-removal">PROV-Dictionary Removal</a>
     * @see <a href="http://www.w3.org/TR/prov-dictionary/#Removal">PROV-O PROV-Dictionary Removal</a>
     * @see <a href="http://www.w3.org/TR/prov-dictionary/#expression-dictionary-removal">PROV-N PROV-Dictionary Removal</a>
     * @see <a href="http://www.w3.org/TR/prov-dictionary/#term-Removal">PROV-XML PROV-Dictionary Removal</a>
     * 
     * @class
     */
    export interface DerivedByRemovalFrom extends org.openprovenance.prov.model.Identifiable, org.openprovenance.prov.model.HasType, org.openprovenance.prov.model.HasLabel, org.openprovenance.prov.model.Influence {
        /**
         * Get the set of deleted keys.
         * @return {*[]} A list of keys
         * @see <a href="http://www.w3.org/TR/prov-dictionary/#removal.key-set">Removal Key-Set</a>
         */
        getKey(): Array<org.openprovenance.prov.model.Key>;

        /**
         * Get an identifier for the dictionary after the deletion
         * @return {*} a QualifiedName
         * @see <a href="http://www.w3.org/TR/prov-dictionary/#removal.after">Removal After</a>
         */
        getNewDictionary(): org.openprovenance.prov.model.QualifiedName;

        /**
         * Get an identifier for the dictionary before the deletion
         * @return {*} a QualifiedName
         * @see <a href="http://www.w3.org/TR/prov-dictionary/#removal.before">Removal Before</a>
         */
        getOldDictionary(): org.openprovenance.prov.model.QualifiedName;

        /**
         * Set an identifier for the dictionary after the deletion.
         * @param {*} after QualifiedName of the dictionary after the deletion
         * @see <a href="http://www.w3.org/TR/prov-dictionary/#removal.after">Removal After</a>
         */
        setNewDictionary(after: org.openprovenance.prov.model.QualifiedName);

        /**
         * Set an identifier for the dictionary before the deletion.
         * @param {*} before QualifiedName of the dictionary before the deletion
         * @see <a href="http://www.w3.org/TR/prov-dictionary/#removal.before">Removal Before</a>
         */
        setOldDictionary(before: org.openprovenance.prov.model.QualifiedName);
    }
}

