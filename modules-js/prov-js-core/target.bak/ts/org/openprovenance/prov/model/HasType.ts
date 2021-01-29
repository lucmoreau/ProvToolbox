/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.model {
    /**
     * 
     * <p>Interface for PROV objects that have a type.
     * <p><a href="http://www.w3.org/TR/prov-dm/#term-attribute-type">PROV-DM Definition for prov:type</a>:
     * The attribute prov:type provides further typing information for any construct with an optional set of attribute-value pairs.
     * 
     * <p><span class="strong">Relevant class</span>
     * <ul>
     * <li>{@link Type}
     * </ul>
     * @class
     */
    export interface HasType {
        /**
         * Gets the value of the <CODE>prov:type</CODE> property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the type property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         * getType().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Type}
         * 
         * @return {*[]} a List of Type values
         * 
         */
        getType(): Array<org.openprovenance.prov.model.Type>;
    }
}

