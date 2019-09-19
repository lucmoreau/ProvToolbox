package org.openprovenance.prov.model;


/** 
 * <p>Interface for PROV attribute location.
 * <p><a href="http://www.w3.org/TR/prov-dm/#concept-location">PROV-DM Definition for Location</a>:  A location can be an identifiable geographic place (ISO 19112), 
 * but it can also be a non-geographic place such as a directory, row, or column. As such, there are numerous ways in which location can be expressed, such as by a 
 * coordinate, address, landmark, and so forth. This document does not specify how to concretely express locations, but instead provide a mechanism to introduce locations, 
 * by means of a reserved attribute.
 * <p>The attribute prov:location is an optional attribute of Entity, Activity, Agent, Usage, Generation, Invalidation, Start, and End. 
 * The value associated with the attribute prov:location must be a PROV-DM Value, expected to denote a location.
 * <p>While the attribute prov:location is allowed for several PROV concepts, it may not make sense to use it in some cases. For example, an activity that describes the 
 * relocation of an entity will have start and end locations, as well as every place in between those points.
 *  
 * <p><span class="strong">Relevant class</span>
 * <ul>
 * <li>{@link HasLocation}
 * </ul>
 * <p>
 * @see  <a href="http://www.w3.org/TR/prov-dm/#term-attribute-location">PROV-DM Location Attribute</a>
 * @see  <a href="http://www.w3.org/TR/prov-o/#Location">PROV-O Location</a>
 * @see  <a href="http://www.w3.org/TR/prov-o/#atLocation">PROV-O atLocation</a>
 * @see  <a href="http://www.w3.org/TR/prov-n/#expression-attribute">PROV-N Attribute</a>
 */

public interface Location extends TypedValue, Attribute {

}
