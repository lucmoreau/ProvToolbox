package org.openprovenance.prov.core.jsonld11;

import junit.framework.TestCase;
import org.openprovenance.prov.core.jsonld11.serialization.ProvDeserialiser;
import org.openprovenance.prov.vanilla.ProvFactory;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.io.StringReader;

public class DateTest extends TestCase {

    final ProvFactory pFactory=new ProvFactory();



    public void testDateJsonld() {
        String startDate = "2023-09-08T20:12:45.109-04:00";
        String endDate = "2023-10-15T20:35:06.793-02:00";
        XMLGregorianCalendar noTZstart= pFactory.newISOTime(startDate);
        XMLGregorianCalendar noTZend= pFactory.newISOTime(endDate);
        XMLGregorianCalendar yesTZstart= pFactory.newISOTimeTZ(startDate);
        XMLGregorianCalendar yesTZend= pFactory.newISOTimeTZ(endDate);

        String jsonld="{\n" +
                "  \"@context\" : [ {\n" +
                "    \"xsd\" : \"http://www.w3.org/2001/XMLSchema#\",\n" +
                "    \"ex\" : \"https://example.org/\",\n" +
                "    \"prov\" : \"http://www.w3.org/ns/prov#\"\n" +
                "  }, \"http://openprovenance.org/prov.jsonld\" ],\n" +
                "  \"@graph\" : [ {\n" +
                "    \"@type\" : \"prov:Activity\",\n" +
                "    \"@id\" : \"ex:a\",\n" +
                "    \"startTime\" : \"2023-09-08T20:12:45.109-04:00\",\n" +
                "    \"endTime\" : \"2023-10-15T20:35:06.793-02:00\"\n" +
                "  } ]\n" +
                "}";

        ProvDeserialiser deserial = new ProvDeserialiser();
        InputStream inputStream = new ByteArrayInputStream(jsonld.getBytes());
        org.openprovenance.prov.model.Document doc = deserial.deserialiseDocument(inputStream);
        assertNotNull(doc);
        assertEquals(1, doc.getStatementOrBundle().size());
        org.openprovenance.prov.model.Activity activity = (org.openprovenance.prov.model.Activity) doc.getStatementOrBundle().get(0);
        assertNotNull(activity);
        assertEquals(noTZstart, activity.getStartTime());
        assertEquals(noTZend, activity.getEndTime());
        assertNotSame(startDate, activity.getStartTime().toXMLFormat());
        assertNotSame(endDate, activity.getEndTime().toXMLFormat());

        // dates read by the json parser have been normalised
        assertEquals(0, activity.getStartTime().getTimezone());
        assertEquals(0, activity.getEndTime().getTimezone());
        // whereas the dates constructed with TZ method have their original TZ offset
        assertEquals(-4 * 60, yesTZstart.getTimezone());
        assertEquals(-2 * 60, yesTZend.getTimezone());

        System.out.println("activity.getStartTime().toXMLFormat() = " + activity.getStartTime().toXMLFormat());
        System.out.println("activity.getEndTime().toXMLFormat() = " + activity.getEndTime().toXMLFormat());
    }

}
