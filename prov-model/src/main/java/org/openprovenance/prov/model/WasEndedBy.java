package org.openprovenance.prov.model;

/**
 * <p>Interface for PROV End association.
 * <p><a href="http://www.w3.org/TR/prov-dm/#concept-end">PROV-DM Definition for End</a>: End is when an activity is deemed to have been ended by an entity, known as trigger. 
 * The activity no longer exists after its end. Any usage, generation, or invalidation involving an activity precedes the activity's end. 
 * An end may refer to a trigger entity that terminated the activity, or to an activity, known as ender that generated the trigger.
 *  
 *
 * <p>The constructor method {@link ProvFactory#newWasEndedBy(QualifiedName, QualifiedName, QualifiedName, QualifiedName)} can be used to create an instance of <tt>End</tt>.
 * The following code snippet creates a new end statement, with a role attribute, and current time.
 * The instance of end is given an identifier <tt>myId</tt>.
 * <pre>
 * QualifiedName myId= ... ;  // some qualified name
 * QualifiedName activityId= ... ;  // some activity qualified name
 * QualifiedName triggerId= ... ;    // some trigger qualified name
 * WasEndedBy myWasEndedBy=provFactory.newWasEndedBy(myId,activityId,triggerId,null);
 * myWasEndedBy.getRole().add(provFactory.newRole("trigger"));
 * myWasEndedBy.setTime(provFactory.newTimeNow())
 * </pre>
 * 
 * <p><span class="strong">Relevant Factory Methods:</span>
 * <ul>
 * <li> {@link ProvFactory#newWasEndedBy(QualifiedName, QualifiedName, QualifiedName, QualifiedName)}
 * <li> {@link ProvFactory#newWasEndedBy(QualifiedName, QualifiedName, QualifiedName, QualifiedName, javax.xml.datatype.XMLGregorianCalendar, java.util.Collection)}
 * <li> {@link ObjectFactory#createWasEndedBy()}
 * </ul>
 * 
 * <p><span class="strong">Schema Definition:</span>
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="End">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="activity" type="{http://www.w3.org/ns/prov#}IDRef"/>
 *         &lt;element name="trigger" type="{http://www.w3.org/ns/prov#}IDRef" minOccurs="0"/>
 *         &lt;element name="ender" type="{http://www.w3.org/ns/prov#}IDRef" minOccurs="0"/>
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
 * @see <a href="http://www.w3.org/TR/prov-dm/#term-End">PROV-DM End</a>
 * @see <a href="http://www.w3.org/TR/prov-o/#End">PROV-O End</a>
 * @see <a href="http://www.w3.org/TR/prov-n/#expression-End">PROV-N End</a>
 * @see <a href="http://www.w3.org/TR/prov-xml/#term-End">PROV-XML End</a>
 * @see <a href="http://www.w3.org/Submission/2013/SUBM-prov-json-20130424/#term-End">PROV-JSON End</a>
 * 
 * 
 */

public interface WasEndedBy  extends Identifiable,  HasLabel, HasTime, HasType, HasRole, HasLocation, HasOther, Influence {

    void setActivity(QualifiedName aid);

    void setTrigger(QualifiedName eid);

    void setEnder(QualifiedName sid);

    QualifiedName getActivity();

    QualifiedName getTrigger();

    QualifiedName getEnder();

}
