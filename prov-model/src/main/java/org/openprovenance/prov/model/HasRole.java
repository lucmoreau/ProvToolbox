package org.openprovenance.prov.model;
import java.util.List;

/** 
 * <p>Interface for PROV objects that have a role.
 * <p><a href="http://www.w3.org/TR/prov-dm/#concept-role">PROV-DM Definition for Role</a>:  A role is the function of an entity 
 * or agent with respect to an activity, in the context of a usage, generation, invalidation, association, start, and end.
 * 
 * <p><span class="strong">Relevant class</span>
 * <ul>
 * <li>{@link Role}
 * </ul>
 */

public interface HasRole {
    
    /**
     * Gets the value of the role property.
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
     *    getRole().add(newItem);
     * </pre>
     * 
     * @return a list of objects of type
     * {@link Role }
     * 
     * 
     */

    public List<Role> getRole();
} 
