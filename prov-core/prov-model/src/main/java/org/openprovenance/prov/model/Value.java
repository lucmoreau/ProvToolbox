package org.openprovenance.prov.model;
/** 
 * <p>Interface for PROV attribute value.
 * <p><a href="http://www.w3.org/TR/prov-dm/#concept-value-attribute">PROV-DM Definition for Value</a>:  
 * The attribute prov:value provides a value that is a direct representation of an entity as a PROV-DM Value.
 * <p><span class="strong">Relevant class</span>
 * <ul>
 * <li>{@link HasValue}
 * </ul>
 * <p>
 * @see  <a href="http://www.w3.org/TR/prov-dm/#concept-value-attribute">PROV-DM Value Attribute</a>
 * @see  <a href="http://www.w3.org/TR/prov-o/#value">PROV-O value</a>
 * @see  <a href="http://www.w3.org/TR/prov-n/#expression-attribute">PROV-N Attribute</a>
 */
public interface Value extends TypedValue, Attribute {

}
