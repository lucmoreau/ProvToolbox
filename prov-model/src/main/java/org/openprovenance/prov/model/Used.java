package org.openprovenance.prov.model;

/**
 * <p>Interface for PROV Usage association.
 * <p><a href="http://www.w3.org/TR/prov-dm/#concept-usage">PROV-DM Definition for Usage</a>: Usage is the beginning of utilizing an entity by an activity.
 *  Before usage, the activity had not begun to utilize this entity and could not have been affected by the entity.
 *  
 *
 * <p>The constructor method {@link ProvFactory#newUsed(QualifiedName, QualifiedName, QualifiedName)} can be used to create an instance of <tt>Used</tt>.
 * The following code snippet creates a new usage statement, with a role attribute, and current time.
 * The instance of usage is given an identifier <tt>myId</tt>.
 * <pre>
 * QualifiedName myId= ... ;  // some qualified name
 * QualifiedName activityId= ... ;  // some activity qualified name
 * QualifiedName entityId= ... ;    // some entity qualified name
 * Used myUsed=provFactory.newUsed(myId,activityId,entityId);
 * myUsed.getRole().add(provFactory.newRole("parameter"));
 * myUsed.setTime(provFactory.newTimeNow())
 * </pre>
 * 
 * <p><span class="strong">Relevant Factory Methods:</span>
 * <ul>
 * <li> {@link ProvFactory#newUsed(QualifiedName, QualifiedName, QualifiedName)}
 * <li> {@link ProvFactory#newUsed(QualifiedName, QualifiedName, QualifiedName, javax.xml.datatype.XMLGregorianCalendar, java.util.Collection)}
 * <li> {@link ObjectFactory#createUsed()}
 * </ul>
 * 
 * <p><span class="strong">Schema Definition:</span>
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Usage">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="activity" type="{http://www.w3.org/ns/prov#}IDRef"/>
 *         &lt;element name="entity" type="{http://www.w3.org/ns/prov#}IDRef"/>
 *         &lt;element name="time" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element ref="{http://www.w3.org/ns/prov#}label" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.w3.org/ns/prov#}location" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.w3.org/ns/prov#}role" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.w3.org/ns/prov#}type" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;any namespace='##other' maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute ref="{http://www.w3.org/ns/prov#}id"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * <p> 
 * @see <a href="http://www.w3.org/TR/prov-dm/#term-Usage">PROV-DM Usage</a>
 * @see <a href="http://www.w3.org/TR/prov-o/#Usage">PROV-O Usage</a>
 * @see <a href="http://www.w3.org/TR/prov-n/#expression-Usage">PROV-N Usage</a>
 * @see <a href="http://www.w3.org/TR/prov-xml/#term-Usage">PROV-XML Usage</a>
 * @see <a href="http://www.w3.org/Submission/2013/SUBM-prov-json-20130424/#term-Usage">PROV-JSON Usage</a>
 * 
 */

public interface Used extends Identifiable, HasLabel, HasType, HasTime, HasRole, HasLocation, HasOther, Influence {

    void setActivity(QualifiedName aid);

    void setEntity(QualifiedName eid);

    QualifiedName getEntity();

    QualifiedName getActivity();


}
