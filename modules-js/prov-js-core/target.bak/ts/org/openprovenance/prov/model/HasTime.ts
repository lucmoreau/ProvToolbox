/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.model {
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
     * @class
     */
    export interface HasTime {
        /**
         * Get time instant
         * @return {javax.xml.datatype.XMLGregorianCalendar} {@link XMLGregorianCalendar}
         */
        getTime(): javax.xml.datatype.XMLGregorianCalendar;

        /**
         * Set time instant
         * @param {javax.xml.datatype.XMLGregorianCalendar} time
         */
        setTime(time: javax.xml.datatype.XMLGregorianCalendar);
    }
}

