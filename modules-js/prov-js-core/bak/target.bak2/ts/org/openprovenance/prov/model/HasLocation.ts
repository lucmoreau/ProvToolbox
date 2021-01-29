/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.model {
    /**
     * 
     * <p>Interface for PROV objects that have a location.
     * <p><a href="http://www.w3.org/TR/prov-dm/#concept-location">PROV-DM Definition for Location</a>:  A location can be an identifiable geographic place (ISO 19112),
     * but it can also be a non-geographic place such as a directory, row, or column.
     * 
     * <p><span class="strong">Relevant class</span>
     * <ul>
     * <li>{@link Location}
     * </ul>
     * 
     * @class
     */
    export interface HasLocation {
        /**
         * Gets the value of the location property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore, any modification made to the
         * returned list will be present inside the object.
         * This is why there is not a <CODE>set</CODE> method for the location property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         * getLocation().add(newItem);
         * </pre>
         * 
         * @return {*[]} a list of objects of type
         * {@link Location}
         * 
         * 
         */
        getLocation(): Array<org.openprovenance.prov.model.Location>;
    }
}

