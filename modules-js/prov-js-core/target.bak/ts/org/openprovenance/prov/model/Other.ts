/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.model {
    /**
     * 
     * <p>Interface for non-PROV attributes.
     * <p>
     * <p><span class="strong">Relevant class</span>
     * <ul>
     * <li>{@link HasOther}
     * </ul>
     * @class
     */
    export interface Other extends org.openprovenance.prov.model.TypedValue, org.openprovenance.prov.model.Attribute {
        /**
         * Get the element name
         * @return {*} {@link QualifiedName} with namespace URI different than prov
         */
        getElementName(): org.openprovenance.prov.model.QualifiedName;

        /**
         * Set the elementName
         * @param {*} elementName is a {@link QualifiedName} whose namespace URI differs from prov
         */
        setElementName(elementName: org.openprovenance.prov.model.QualifiedName);
    }
}

