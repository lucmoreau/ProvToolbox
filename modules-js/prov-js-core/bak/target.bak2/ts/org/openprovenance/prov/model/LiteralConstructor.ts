/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.model {
    /**
     * Interface to convenience factory methods for PROV Literals.
     * 
     * @author lavm
     * @class
     */
    export interface LiteralConstructor {
        /**
         * Factory method for a <a href="http://www.w3.org/TR/xmlschema-2/#gYear">gYear</a>.
         * A {@code gYear} represents a gregorian calendar year.
         * @param {number} year value
         * @return {*} an instance of {@link XMLGregorianCalendar}
         * 
         */
        newGYear(year: number): any;

        /**
         * Factory method for a <a href="http://www.w3.org/TR/xmlschema-2/#gMonth">gMonth</a>.
         * A {@code gMonth} represents a gregorian month that recurs every year.
         * @param {number} month value
         * @return {*} an instance of {@link XMLGregorianCalendar}
         * 
         */
        newGMonth(month: number): any;

        /**
         * Factory method for a <a href="http://www.w3.org/TR/xmlschema-2/#gDay">gDay</a>.
         * A {@code gDay} represents a gregorian day that recurs, specifically a day of the month such as the 5th of the month.
         * @param {number} day value
         * @return {*} an instance of {@link XMLGregorianCalendar}
         * 
         */
        newGDay(day: number): any;

        /**
         * Factory method for a <a href="http://www.w3.org/TR/xmlschema-2/#gMonthDay">gMonthDay</a>.
         * A {@code gMonthDay} is a gregorian date that recurs, specifically a day of the year such as the third of May.
         * @param {number} month value
         * @param {number} day value
         * @return {*} an instance of {@link XMLGregorianCalendar}
         * 
         */
        newGMonthDay(month: number, day: number): any;

        /**
         * Factory method for a <a href="http://www.w3.org/TR/xmlschema-2/#dateTime">dateTime</a>.
         * @param {string} lexicalXSDDateTime
         * A string containing lexical representation of
         * xsd:datetime.
         * 
         * @return {*} an instance of {@link XMLGregorianCalendar}
         * 
         */
        newISOTime(lexicalXSDDateTime: string): any;

        /**
         * Factory method for a <a href="http://www.w3.org/TR/xmlschema-2/#duration">duration</a>.
         * @param {string} lexicalRepresentation <code>String</code> representation of a <code>Duration</code>.
         * 
         * @return {*} an instance of {@link javax.xml.datatype.Duration}
         * 
         */
        newDuration(lexicalRepresentation: string): any;

        base64Decoding(s: string): number[];

        hexDecoding(value: string): any;

        /**
         * Factory method for this moment's time expressed as <a href="http://www.w3.org/TR/xmlschema-2/#dateTime">dateTime</a>.
         * 
         * 
         * @return {*} an instance of {@link XMLGregorianCalendar}
         * 
         */
        newTimeNow(): any;
    }
}

