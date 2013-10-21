package org.openprovenance.prov.model;


import javax.xml.datatype.XMLGregorianCalendar;

public interface Activity extends Identifiable,  HasLabel, HasType, HasExtensibility, HasLocation, Statement, Element  {



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
