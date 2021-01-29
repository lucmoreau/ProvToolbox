/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.model {
    /**
     * <p>Interface for the PROV Activity complex type.
     * 
     * <p><a href="http://www.w3.org/TR/prov-dm/#concept-activity">PROV-DM Definition for Activity</a>: An activity is something that occurs over a period of time and acts upon or with entities; it may include consuming, processing, transforming, modifying, relocating, using, or generating entities.
     * 
     * 
     * *
     * <p><span class="strong">Relevant Factory Methods:</span>
     * <ul>
     * <li> {@link ProvFactory#newActivity(QualifiedName)}
     * <li> {@link ProvFactory#newActivity(org.openprovenance.prov.model.QualifiedName, javax.xml.datatype.XMLGregorianCalendar, javax.xml.datatype.XMLGregorianCalendar, java.util.Collection)}
     * <li> {@link ObjectFactory#createActivity()}
     * </ul>
     * 
     * <p>The following schema fragment specifies the expected content contained within this type.
     * 
     * <pre>
     * &lt;complexType name="Activity"&gt;
     * &lt;complexContent&gt;
     * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     * &lt;sequence&gt;
     * &lt;element name="startTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     * &lt;element name="endTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
     * &lt;element ref="{http://www.w3.org/ns/prov#}label" maxOccurs="unbounded" minOccurs="0"/&gt;
     * &lt;element ref="{http://www.w3.org/ns/prov#}location" maxOccurs="unbounded" minOccurs="0"/&gt;
     * &lt;element ref="{http://www.w3.org/ns/prov#}type" maxOccurs="unbounded" minOccurs="0"/&gt;
     * &lt;any namespace='##other' maxOccurs="unbounded" minOccurs="0"/&gt;
     * &lt;/sequence&gt;
     * &lt;attribute ref="{http://www.w3.org/ns/prov#}id"/&gt;
     * &lt;/restriction&gt;
     * &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * @see <a href="http://www.w3.org/TR/prov-dm/#term-activity">PROV-DM Activity</a>
     * @see <a href="http://www.w3.org/TR/prov-o/#Activity">PROV-O Activity</a>
     * @see <a href="http://www.w3.org/TR/prov-n/#expression-Activity">PROV-N Activity</a>
     * @see <a href="http://www.w3.org/TR/prov-xml/#term-Activity">PROV-XML Activity</a>
     * 
     * @class
     */
    export interface Activity extends org.openprovenance.prov.model.Identifiable, org.openprovenance.prov.model.HasLabel, org.openprovenance.prov.model.HasType, org.openprovenance.prov.model.HasLocation, org.openprovenance.prov.model.HasOther, org.openprovenance.prov.model.Statement, org.openprovenance.prov.model.Element {
        /**
         * Gets the value of the startTime property.
         * 
         * @return
         * possible object is
         * {@link XMLGregorianCalendar}
         * 
         * @return {javax.xml.datatype.XMLGregorianCalendar}
         */
        getStartTime(): javax.xml.datatype.XMLGregorianCalendar;

        /**
         * Sets the value of the startTime property.
         * 
         * @param {javax.xml.datatype.XMLGregorianCalendar} value
         * allowed object is
         * {@link XMLGregorianCalendar}
         * 
         */
        setStartTime(value: javax.xml.datatype.XMLGregorianCalendar);

        /**
         * Gets the value of the endTime property.
         * 
         * @return
         * possible object is
         * {@link XMLGregorianCalendar}
         * 
         * @return {javax.xml.datatype.XMLGregorianCalendar}
         */
        getEndTime(): javax.xml.datatype.XMLGregorianCalendar;

        /**
         * Sets the value of the endTime property.
         * 
         * @param {javax.xml.datatype.XMLGregorianCalendar} value
         * allowed object is
         * {@link XMLGregorianCalendar}
         * 
         */
        setEndTime(value: javax.xml.datatype.XMLGregorianCalendar);
    }
}

