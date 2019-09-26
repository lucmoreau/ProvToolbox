package org.openprovenance.prov.model;
import java.util.List;


/** 
 * <p>Interface for PROV objects that have non-PROV attributes.
 * <p>
 * <p><span class="strong">Relevant class</span>
 * <ul>
 * <li>{@link Other}
 * </ul>
 */
public interface HasOther {

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
     *    getOther().add(newItem);
     * </pre>
     * 
     * @return a list of objects of type
     * {@link Other }
     * 
     * 
     */

    public List<org.openprovenance.prov.model.Other> getOther();

} 
