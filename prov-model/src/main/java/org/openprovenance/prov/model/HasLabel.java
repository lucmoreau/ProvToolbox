package org.openprovenance.prov.model;
import java.util.List;

/**
 * <p>Interface for PROV objects that have a role.
 * <p><a href="http://www.w3.org/TR/prov-dm/#concept-label">PROV-DM Definition for Label</a>:  The attribute prov:label 
 * provides a human-readable representation of an instance of a PROV-DM type or relation. 
 * 
 * <p><span class="strong">Relevant class</span>
 * <ul>
 * <li>{@link LangString}
 * <li>{@link Label}
 * </ul>
 * 
 * @author lavm
 *
 */
public interface HasLabel {
    /**
     * Gets the value of the label property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the object.
     * This is why there is not a <CODE>set</CODE> method for the label property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLabel().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LangString }
     * 
     * 
     */
    public abstract List<LangString> getLabel();
} 
