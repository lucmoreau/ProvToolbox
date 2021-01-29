/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.model {
    /**
     * <p>Interface for PROV Dictionary Membership
     * 
     * <p><a href="http://www.w3.org/TR/prov-dictionary/#term-dictionary-membership">A PROV Dictionary Definition for Membership</a>:
     * Similar to the collection membership relation, the dictionary membership allows stating the members of a Dictionary.
     * However, it provides additional structure. Note that dictionary membership implies collection membership, but not vice versa.
     * 
     * <p>
     * <p><span class="strong">Relevant Factory Methods:</span>
     * <ul>
     * <li> {@link ProvFactory#newDictionaryMembership(QualifiedName, List)}
     * <li> {@link ObjectFactory#createDictionaryMembership()}
     * </ul>
     * 
     * <p><span class="strong">Schema Definition:</span>
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType name="DictionaryMembership"&gt;
     * &lt;complexContent&gt;
     * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     * &lt;sequence&gt;
     * &lt;element name="dictionary" type="{http://www.w3.org/ns/prov#}IDRef"/&gt;
     * &lt;element name="keyEntityPair" type="{http://www.w3.org/ns/prov#}KeyEntityPair" maxOccurs="unbounded"/&gt;
     * &lt;/sequence&gt;
     * &lt;/restriction&gt;
     * &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     * 
     * <p>
     * @see <a href="http://www.w3.org/TR/prov-dictionary/#term-dictionary-membership">PROV-Dictionary Membership</a>
     * @see <a href="http://www.w3.org/TR/prov-dictionary/#hadDictionaryMember">PROV-O PROV-Dictionary Membership</a>
     * @see <a href="http://www.w3.org/TR/prov-dictionary/#expression-dictionary-membership">PROV-N PROV-Dictionary Membership</a>
     * @see <a href="http://www.w3.org/TR/prov-dictionary/#term-DictionaryMembership">PROV-XML PROV-Dictionary Membership</a>
     * 
     * 
     * @class
     */
    export interface DictionaryMembership extends org.openprovenance.prov.model.Relation {
        /**
         * Get an identifier for the dictionary whose members are asserted.
         * @return {*} {@link QualifiedName} of the dictionary
         * @see <a href="http://www.w3.org/TR/prov-dictionary/#membership-d.dictionary">membership dictionary</a>
         */
        getDictionary(): org.openprovenance.prov.model.QualifiedName;

        /**
         * A list of entries (key-entity pairs) that are members of the dictionary. Note that while the conceptual
         * models allows for one key-entity pair to be asserted at the time, {@link DictionaryMembership} allows for multiple pairs
         * to be asserted.
         * @return {*[]} a list of {@link Entry}
         * @see <a href="http://www.w3.org/TR/prov-dictionary/#membership-d.entity">membership entity</a>
         * @see <a href="http://www.w3.org/TR/prov-dictionary/#membership-d.key">membership key</a>
         */
        getKeyEntityPair(): Array<org.openprovenance.prov.model.Entry>;

        /**
         * Set an identifier for the dictionary whose members are asserted.
         * @param {*} dictionary a {@link QualifiedName} of the dictionary
         * @see <a href="http://www.w3.org/TR/prov-dictionary/#membership-d.dictionary">membership dictionary</a>
         */
        setDictionary(dictionary: org.openprovenance.prov.model.QualifiedName);
    }
}

