/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.model {
    /**
     * <p>Interface for PROV Usage association.
     * <p><a href="http://www.w3.org/TR/prov-dm/#concept-usage">PROV-DM Definition for Usage</a>: Usage is the beginning of utilizing an entity by an activity.
     * Before usage, the activity had not begun to utilize this entity and could not have been affected by the entity.
     * 
     * 
     * <p>The constructor method {@link ProvFactory#newUsed(QualifiedName, QualifiedName, QualifiedName)} can be used to create an instance of {@code Used}.
     * The following code snippet creates a new usage statement, with a role attribute, and current time.
     * The instance of usage is given an identifier {@code myId}.
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
     * &lt;complexType name="Usage"&gt;
     * &lt;complexContent&gt;
     * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     * &lt;sequence&gt;
     * &lt;element name="activity" type="{http://www.w3.org/ns/prov#}IDRef"/&gt;
     * &lt;element name="entity" type="{http://www.w3.org/ns/prov#}IDRef"/&gt;
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
     * @see <a href="http://www.w3.org/TR/prov-dm/#term-Usage">PROV-DM Usage</a>
     * @see <a href="http://www.w3.org/TR/prov-o/#Usage">PROV-O Usage</a>
     * @see <a href="http://www.w3.org/TR/prov-n/#expression-Usage">PROV-N Usage</a>
     * @see <a href="http://www.w3.org/TR/prov-xml/#term-Usage">PROV-XML Usage</a>
     * @see <a href="http://www.w3.org/Submission/2013/SUBM-prov-json-20130424/#term-Usage">PROV-JSON Usage</a>
     * 
     * @class
     */
    export interface Used extends org.openprovenance.prov.model.Identifiable, org.openprovenance.prov.model.HasLabel, org.openprovenance.prov.model.HasType, org.openprovenance.prov.model.HasTime, org.openprovenance.prov.model.HasRole, org.openprovenance.prov.model.HasLocation, org.openprovenance.prov.model.HasOther, org.openprovenance.prov.model.Influence {
        setActivity(aid: org.openprovenance.prov.model.QualifiedName);

        setEntity(eid: org.openprovenance.prov.model.QualifiedName);

        getEntity(): org.openprovenance.prov.model.QualifiedName;

        getActivity(): org.openprovenance.prov.model.QualifiedName;
    }
}

