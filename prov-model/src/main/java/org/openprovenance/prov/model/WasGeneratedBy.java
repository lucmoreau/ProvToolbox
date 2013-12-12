package org.openprovenance.prov.model;


/**
 * <p>Interface for PROV Generation association.
 * <p><a href="http://www.w3.org/TR/prov-dm/#concept-generation">PROV-DM Definition for Generation</a>: Generation is the completion of production of a 
 * new entity by an activity. This entity did not exist before generation and becomes available for usage after this generation.
 *  
 *
 * <p>The constructor method {@link ProvFactory#newWasGeneratedBy(QualifiedName, QualifiedName, QualifiedName)} can be used to create an instance of <tt>WasGeneratedBy</tt>.
 * The following code snippet creates a new generation statement, with a role attribute, and current time.
 * The instance of generation is given an identifier <tt>myId</tt>.
 * <pre>
 * QualifiedName myId= ... ;  // some qualified name
 * QualifiedName entityId= ... ;    // some entity qualified name
 * QualifiedName activityId= ... ;  // some activity qualified name
 * WasGeneratedBy myWasGeneratedBy=provFactory.newWasGeneratedBy(myId,entityId,activityId);
 * myWasGeneratedBy.getRole().add(provFactory.newRole("parameter"));
 * myWasGeneratedBy.setTime(provFactory.newTimeNow())
 * </pre>
 * 
 * <p><span class="strong">Relevant Factory Methods:</span>
 * <ul>
 * <li> {@link ProvFactory#newWasGeneratedBy(QualifiedName, QualifiedName, QualifiedName)}
 * <li> {@link ProvFactory#newWasGeneratedBy(QualifiedName, QualifiedName, QualifiedName, javax.xml.datatype.XMLGregorianCalendar, java.util.Collection)}
 * <li> {@link ObjectFactory#createWasGeneratedBy()}
 * </ul>
 * 
 * <p><span class="strong">Schema Definition:</span>
 * 
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Generation">
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
 * @see <a href="http://www.w3.org/TR/prov-dm/#term-Generation">PROV-DM Generation</a>
 * @see <a href="http://www.w3.org/TR/prov-o/#Generation">PROV-O Generation</a>
 * @see <a href="http://www.w3.org/TR/prov-n/#expression-Generation">PROV-N Generation</a>
 * @see <a href="http://www.w3.org/TR/prov-xml/#term-Generation">PROV-XML Generation</a>
 * @see <a href="http://www.w3.org/Submission/2013/SUBM-prov-json-20130424/#term-Generation">PROV-JSON Generation</a>
 * 
 * 
 */
public interface WasGeneratedBy extends Identifiable,  HasLabel, HasTime, HasType, HasRole, HasLocation, HasOther, Influence {

    void setActivity(QualifiedName pid);

    void setEntity(QualifiedName aid);

    QualifiedName getEntity();

    QualifiedName getActivity();

}
