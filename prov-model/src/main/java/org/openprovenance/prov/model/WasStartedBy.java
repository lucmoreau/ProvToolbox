package org.openprovenance.prov.model;

/**
 * <p>Interface for PROV Start association.
 * <p><a href="http://www.w3.org/TR/prov-dm/#concept-start">PROV-DM Definition for Start</a>: Start is when an activity is deemed to have been started by an entity, 
 * known as trigger. The activity did not exist before its start. Any usage, generation, or invalidation involving an activity follows 
 * the activity's start. A start may refer to a trigger entity that set off the activity, or to an activity, known as starter, that generated the trigger.
 *  
 *
 * <p>The constructor method {@link ProvFactory#newWasStartedBy(QualifiedName, QualifiedName, QualifiedName, QualifiedName)} can be used to create an instance of <tt>Start</tt>.
 * The following code snippet creates a new start statement, with a role attribute, and current time.
 * The instance of start is given an identifier <tt>myId</tt>.
 * <pre>
 * QualifiedName myId= ... ;  // some qualified name
 * QualifiedName activityId= ... ;  // some activity qualified name
 * QualifiedName triggerId= ... ;    // some trigger qualified name
 * WasStartedBy myWasStartedBy=provFactory.newWasStartedBy(myId,activityId,triggerId,null);
 * myWasStartedBy.getRole().add(provFactory.newRole("trigger"));
 * myWasStartedBy.setTime(provFactory.newTimeNow())
 * </pre>
 * 
 * <p><span class="strong">Relevant Factory Methods:</span>
 * <ul>
 * <li> {@link ProvFactory#newWasStartedBy(QualifiedName, QualifiedName, QualifiedName, QualifiedName)}
 * <li> {@link ProvFactory#newWasStartedBy(QualifiedName, QualifiedName, QualifiedName, QualifiedName, javax.xml.datatype.XMLGregorianCalendar, java.util.Collection)}
 * <li> {@link ObjectFactory#createWasStartedBy()}
 * </ul>
 * 
 * <p><span class="strong">Schema Definition:</span>
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Start">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="activity" type="{http://www.w3.org/ns/prov#}IDRef"/>
 *         &lt;element name="trigger" type="{http://www.w3.org/ns/prov#}IDRef" minOccurs="0"/>
 *         &lt;element name="starter" type="{http://www.w3.org/ns/prov#}IDRef" minOccurs="0"/>
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
 * @see <a href="http://www.w3.org/TR/prov-dm/#term-Start">PROV-DM Start</a>
 * @see <a href="http://www.w3.org/TR/prov-o/#Start">PROV-O Start</a>
 * @see <a href="http://www.w3.org/TR/prov-n/#expression-Start">PROV-N Start</a>
 * @see <a href="http://www.w3.org/TR/prov-xml/#term-Start">PROV-XML Start</a>
 * @see <a href="http://www.w3.org/Submission/2013/SUBM-prov-json-20130424/#term-Start">PROV-JSON Start</a>
 * 
 * 
 */

public interface WasStartedBy extends Identifiable, HasLabel, HasTime, HasType,
	HasRole, HasLocation, HasOther, Influence {

    QualifiedName getActivity();

    void setActivity(QualifiedName aid);

    QualifiedName getStarter();

    QualifiedName getTrigger();


    void setStarter(QualifiedName sid);

    void setTrigger(QualifiedName eid);

}
