package org.openprovenance.prov.model;
import java.util.List;

/** 
 * <p>Interface for PROV objects that have a location.
 * <p><a href="http://www.w3.org/TR/prov-dm/#concept-location">PROV-DM Definition for Location</a>:  A location can be an identifiable geographic place (ISO 19112), 
 * but it can also be a non-geographic place such as a directory, row, or column. 
 * 
 * <p><span class="strong">Relevant class</span>
 * <ul>
 * <li>{@link Location}
 * </ul>
 * 
 */

public interface HasLocation {

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
     *    getLocation().add(newItem);
     * </pre>
     * 
     * @return a list of objects of type
     * {@link Location }
     * 
     * 
     */

    public List<Location> getLocation();

} 
