package org.openprovenance.prov.model;



/** 
 * <p>Interface for non-PROV attributes.
 *
 * <p><span class="strong">Relevant class</span>
 * <ul>
 * <li>{@link HasOther}
 * </ul>
 */
public interface Other extends TypedValue, Attribute {
    
    /**
     * Get the element name
     * @return {@link QualifiedName} with namespace URI different than prov
     */
    public QualifiedName getElementName();
    
    /**
     * Set the elementName
     * @param elementName is a {@link QualifiedName} whose namespace URI differs from prov
     */
    
    public void setElementName(QualifiedName elementName) ;

}
