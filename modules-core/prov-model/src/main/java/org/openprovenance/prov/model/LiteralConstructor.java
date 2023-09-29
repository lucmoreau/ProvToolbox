package org.openprovenance.prov.model;

import javax.xml.datatype.XMLGregorianCalendar;


/**
 * Interface to convenience factory methods for PROV Literals.
 * 
 * @author lavm
 *
 */
public interface LiteralConstructor {
    
    /** Factory method for a <a href="http://www.w3.org/TR/xmlschema-2/#gYear">gYear</a>.
     * A {@code gYear} represents a gregorian calendar year.
     * @param year value 
     * @return an instance of {@link XMLGregorianCalendar}
     * 
     */

    Object newGYear(int year);
    
    /** Factory method for a <a href="http://www.w3.org/TR/xmlschema-2/#gMonth">gMonth</a>.
     * A {@code gMonth} represents a gregorian month that recurs every year.
     * @param month value 
     * @return an instance of {@link XMLGregorianCalendar}
     * 
     */


    Object newGMonth(int month);
    
    
    /** Factory method for a <a href="http://www.w3.org/TR/xmlschema-2/#gDay">gDay</a>.
     * A {@code gDay} represents a gregorian day that recurs, specifically a day of the month such as the 5th of the month.
     * @param day value 
     * @return an instance of {@link XMLGregorianCalendar}
     * 
     */
 
    Object newGDay(int day);
    
    
    /** Factory method for a <a href="http://www.w3.org/TR/xmlschema-2/#gMonthDay">gMonthDay</a>.
     * A {@code gMonthDay} is a gregorian date that recurs, specifically a day of the year such as the third of May. 
     * @param month value 
     * @param day value 
     * @return an instance of {@link XMLGregorianCalendar}
     * 
     */

    
    Object newGMonthDay(int month, int day);
    
    
    /** Factory method for a <a href="http://www.w3.org/TR/xmlschema-2/#dateTime">dateTime</a>.
     * @param lexicalXSDDateTime
     *     A string containing lexical representation of
     *     xsd:datetime.
     * 
     * @return an instance of {@link XMLGregorianCalendar} expressed in the system default timezone
     * 
     */

    Object newISOTime(String lexicalXSDDateTime);

    /** Factory method for a <a href="http://www.w3.org/TR/xmlschema-2/#dateTime">dateTime</a>.
     * @param lexicalXSDDateTime
     *     A string containing lexical representation of
     *     xsd:datetime.
     *
     * @return an instance of {@link XMLGregorianCalendar}
     *
     */

    Object newISOTimeUTC(String lexicalXSDDateTime);

    /** Factory method for a <a href="http://www.w3.org/TR/xmlschema-2/#dateTime">dateTime</a>.
     * @param lexicalXSDDateTime
     *     A string containing lexical representation of
     *     xsd:datetime.
     *
     * @return an instance of {@link XMLGregorianCalendar} normalized in UTC
     *
     */

    Object newISOTimeTZ(String lexicalXSDDateTime);

    /** Factory method for a <a href="http://www.w3.org/TR/xmlschema-2/#duration">duration</a>.
     * @param lexicalRepresentation <code>String</code> representation of a <code>Duration</code>.
     * 
     * @return an instance of {@link javax.xml.datatype.Duration}
     * 
     */

    
    Object newDuration(String lexicalRepresentation);

    byte [] base64Decoding(String s);

    Object hexDecoding(String value);
    
    /** Factory method for this moment's time expressed as <a href="http://www.w3.org/TR/xmlschema-2/#dateTime">dateTime</a>.
     * 
     * 
     * @return an instance of {@link XMLGregorianCalendar}
     * 
     */    
    Object newTimeNow();

}
