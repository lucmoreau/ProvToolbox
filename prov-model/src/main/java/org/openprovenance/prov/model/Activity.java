package org.openprovenance.prov.model;


import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>Interface for the PROV Activity complex type.
 * 
 * <p><a href="http://www.w3.org/TR/prov-dm/#concept-activity">PROV-DM Definition for Activity</a>: An activity is something that occurs over a period of time and acts upon or with entities; it may include consuming, processing, transforming, modifying, relocating, using, or generating entities.
 * 
 * 
 *  *
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
 * &lt;complexType name="Activity">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="startTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="endTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element ref="{http://www.w3.org/ns/prov#}label" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.w3.org/ns/prov#}location" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.w3.org/ns/prov#}type" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;any namespace='##other' maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute ref="{http://www.w3.org/ns/prov#}id"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * @see <a href="http://www.w3.org/TR/prov-dm/#term-activity">PROV-DM Activity</a>
 * @see <a href="http://www.w3.org/TR/prov-o/#Activity">PROV-O Activity</a>
 * @see <a href="http://www.w3.org/TR/prov-n/#expression-Activity">PROV-N Activity</a>
 * @see <a href="http://www.w3.org/TR/prov-xml/#term-Activity">PROV-XML Activity</a>
 * 
 */


public interface Activity extends Identifiable,  HasLabel, HasType, HasLocation, HasOther, Statement, Element  {



    /**
     * Gets the value of the startTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public abstract XMLGregorianCalendar getStartTime();

    /**
     * Sets the value of the startTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public abstract void setStartTime(XMLGregorianCalendar value);

    /**
     * Gets the value of the endTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public abstract XMLGregorianCalendar getEndTime();

    /**
     * Sets the value of the endTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public abstract void setEndTime(XMLGregorianCalendar value);



}
