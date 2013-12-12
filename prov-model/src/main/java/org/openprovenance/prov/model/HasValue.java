package org.openprovenance.prov.model;


/** 
 * <p>Interface for PROV objects that have a value.
 * <p><a href="http://www.w3.org/TR/prov-dm/#concept-value-attribute">PROV-DM Definition for prov:value</a>:   
 * The attribute prov:value provides a value that is a direct representation of an entity as a PROV-DM Value.
 * <p><span class="strong">Relevant class</span>
 * <ul>
 * <li>{@link Value}
 * </ul>
 */

public interface HasValue {
    public Value getValue();
    public void setValue(Value value);
} 
