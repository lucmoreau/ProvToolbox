package org.openprovenance.prov.model;

/** 
 * <p>Interface for PROV attribute role.
 * <p><a href="http://www.w3.org/TR/prov-dm/#concept-role">PROV-DM Definition for Role</a>:  A role is the function of an entity 
 * or agent with respect to an activity, in the context of a usage, generation, invalidation, association, start, and end.
 * 
 * <p><span class="strong">Relevant class</span>
 * <ul>
 * <li>{@link HasRole}
 * </ul>
 * <p>
 * @see  <a href="http://www.w3.org/TR/prov-dm/#term-attribute-role">PROV-DM Role Attribute</a>
 * @see  <a href="http://www.w3.org/TR/prov-o/#Role">PROV-O Role</a>
 * @see  <a href="http://www.w3.org/TR/prov-o/#hadRole">PROV-O hadRole</a>
 * @see  <a href="http://www.w3.org/TR/prov-n/#expression-attribute">PROV-N Attribute</a>
 */

public interface Role extends TypedValue, Attribute {

}
