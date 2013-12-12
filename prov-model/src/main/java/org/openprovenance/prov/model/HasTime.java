package org.openprovenance.prov.model;

import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Interface for PROV objects that have time.
 * <p>In PROV, <a href="http://www.w3.org/TR/prov-dm/#dfn-time">time instants</a> 
 * are defined according to <a href="http://www.w3.org/TR/2012/REC-xmlschema11-2-20120405/#dateTime">xsd:dateTime</a>.
 * 
 * <p><span class="strong">Relevant class</span>
 * <ul>
 * <li>{@link XMLGregorianCalendar}
 * </ul>
 * 
 * 
 * @author lavm
 *
 */
public interface HasTime {
    
    /**
     * Get time instant
     * @return {@link XMLGregorianCalendar} 
     */
    public XMLGregorianCalendar getTime();
    
    /**
     * Set time instant
     * @param time 
     */
    public void setTime(XMLGregorianCalendar time);
} 