/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.model {
    /**
     * 
     * <p>Interface for PROV objects that have non-PROV attributes.
     * <p>
     * <p><span class="strong">Relevant class</span>
     * <ul>
     * <li>{@link Other}
     * </ul>
     * @class
     */
    export interface HasOther {
        /**
         * Gets the list of non-PROV attributes.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore, any modification made to the
         * returned list will be present inside the object.
         * This is why there is not a <CODE>set</CODE> method for the role property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         * getOther().add(newItem);
         * </pre>
         * 
         * @return {*[]} a list of objects of type
         * {@link Other}
         * 
         * 
         */
        getOther(): Array<org.openprovenance.prov.model.Other>;
    }
}

