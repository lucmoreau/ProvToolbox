package org.openprovenance.prov.model;

/**
 * <p>Interface for PROV Invalidation association.
 * <p><a href="http://www.w3.org/TR/prov-dm/#concept-invalidation">PROV-DM Definition for Invalidation</a>: Invalidation is the start of the destruction, cessation, or 
 * expiry of an existing entity by an activity. The entity is no longer available for use (or further invalidation) after invalidation. 
 * Any generation or usage of an entity precedes its invalidation.
 *  
 *
 * <p>The constructor method {@link ProvFactory#newWasInvalidatedBy(QualifiedName, QualifiedName, QualifiedName)} can be used to create an instance of <tt>WasInvalidatedBy</tt>.
 * The following code snippet creates a new invalidation statement, with a role attribute, and current time.
 * The instance of invalidation is given an identifier <tt>myId</tt>.
 * <pre>
 * QualifiedName myId= ... ;  // some qualified name
 * QualifiedName entityId= ... ;    // some entity qualified name
 * QualifiedName activityId= ... ;  // some activity qualified name
 * WasInvalidatedBy myWasInvalidatedBy=provFactory.newWasInvalidatedBy(myId,entityId,activityId);
 * myWasInvalidatedBy.getRole().add(provFactory.newRole("parameter"));
 * myWasInvalidatedBy.setTime(provFactory.newTimeNow())
 * </pre>
 * 
 * <p><span class="strong">Relevant Factory Methods:</span>
 * <ul>
 * <li> {@link ProvFactory#newWasInvalidatedBy(QualifiedName, QualifiedName, QualifiedName)}
 * <li> {@link ProvFactory#newWasInvalidatedBy(QualifiedName, QualifiedName, QualifiedName, javax.xml.datatype.XMLGregorianCalendar, java.util.Collection)}
 * <li> {@link ObjectFactory#createWasInvalidatedBy()}
 * </ul>
 * 
 * <p><span class="strong">Schema Definition:</span>
 * 
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Invalidation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="entity" type="{http://www.w3.org/ns/prov#}IDRef"/>
 *         &lt;element name="activity" type="{http://www.w3.org/ns/prov#}IDRef" minOccurs="0"/>
 *         &lt;element name="time" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element ref="{http://www.w3.org/ns/prov#}label" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.w3.org/ns/prov#}type" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.w3.org/ns/prov#}location" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.w3.org/ns/prov#}role" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;any namespace='##other' maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute ref="{http://www.w3.org/ns/prov#}id"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 * <p> 
 * @see <a href="http://www.w3.org/TR/prov-dm/#term-Invalidation">PROV-DM Invalidation</a>
 * @see <a href="http://www.w3.org/TR/prov-o/#Invalidation">PROV-O Invalidation</a>
 * @see <a href="http://www.w3.org/TR/prov-n/#expression-Invalidation">PROV-N Invalidation</a>
 * @see <a href="http://www.w3.org/TR/prov-xml/#term-Invalidation">PROV-XML Invalidation</a>
 * @see <a href="http://www.w3.org/Submission/2013/SUBM-prov-json-20130424/#term-Invalidation">PROV-JSON Invalidation</a>
 * 
 * 
 */
public interface WasInvalidatedBy extends Identifiable,  HasLabel, HasTime, HasType, HasRole, HasLocation, HasOther,  Influence{

    void setActivity(QualifiedName aid);

    void setEntity(QualifiedName eid);

    QualifiedName getEntity();

    QualifiedName getActivity();

}
