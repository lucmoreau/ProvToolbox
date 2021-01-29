/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.model {
    /**
     * <p>Interface for PROV End association.
     * <p><a href="http://www.w3.org/TR/prov-dm/#concept-end">PROV-DM Definition for End</a>: End is when an activity is deemed to have been ended by an entity, known as trigger.
     * The activity no longer exists after its end. Any usage, generation, or invalidation involving an activity precedes the activity's end.
     * An end may refer to a trigger entity that terminated the activity, or to an activity, known as ender that generated the trigger.
     * 
     * 
     * <p>The constructor method {@link ProvFactory#newWasEndedBy(QualifiedName, QualifiedName, QualifiedName, QualifiedName)} can be used to create an instance of {@code End}.
     * The following code snippet creates a new end statement, with a role attribute, and current time.
     * The instance of end is given an identifier {@code myId}.
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
     * &lt;complexType name="End"&gt;
     * &lt;complexContent&gt;
     * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     * &lt;sequence&gt;
     * &lt;element name="activity" type="{http://www.w3.org/ns/prov#}IDRef"/&gt;
     * &lt;element name="trigger" type="{http://www.w3.org/ns/prov#}IDRef" minOccurs="0"/&gt;
     * &lt;element name="ender" type="{http://www.w3.org/ns/prov#}IDRef" minOccurs="0"/&gt;
     * &lt;element name="time" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     * &lt;element ref="{http://www.w3.org/ns/prov#}label" maxOccurs="unbounded" minOccurs="0"/&gt;
     * &lt;element ref="{http://www.w3.org/ns/prov#}location" maxOccurs="unbounded" minOccurs="0"/&gt;
     * &lt;element ref="{http://www.w3.org/ns/prov#}role" maxOccurs="unbounded" minOccurs="0"/&gt;
     * &lt;element ref="{http://www.w3.org/ns/prov#}type" maxOccurs="unbounded" minOccurs="0"/&gt;
     * &lt;any namespace='##other' maxOccurs="unbounded" minOccurs="0"/&gt;
     * &lt;/sequence&gt;
     * &lt;attribute ref="{http://www.w3.org/ns/prov#}id"/&gt;
     * &lt;/restriction&gt;
     * &lt;/complexContent&gt;
     * &lt;/complexType&gt;
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
     * @class
     */
    export interface WasEndedBy extends org.openprovenance.prov.model.Identifiable, org.openprovenance.prov.model.HasLabel, org.openprovenance.prov.model.HasTime, org.openprovenance.prov.model.HasType, org.openprovenance.prov.model.HasRole, org.openprovenance.prov.model.HasLocation, org.openprovenance.prov.model.HasOther, org.openprovenance.prov.model.Influence {
        setActivity(aid: org.openprovenance.prov.model.QualifiedName);

        setTrigger(eid: org.openprovenance.prov.model.QualifiedName);

        setEnder(sid: org.openprovenance.prov.model.QualifiedName);

        getActivity(): org.openprovenance.prov.model.QualifiedName;

        getTrigger(): org.openprovenance.prov.model.QualifiedName;

        getEnder(): org.openprovenance.prov.model.QualifiedName;
    }
}

