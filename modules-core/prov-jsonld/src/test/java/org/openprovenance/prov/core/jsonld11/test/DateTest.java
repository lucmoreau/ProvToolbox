package org.openprovenance.prov.core.jsonld11.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;
import org.openprovenance.prov.core.jsonld11.serialization.ProvDeserialiser;
import org.openprovenance.prov.model.DateTimeOption;
import org.openprovenance.prov.vanilla.ProvFactory;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.TimeZone;

public class DateTest extends TestCase {

    final ProvFactory pFactory=new ProvFactory();
    String startDate = "2023-09-08T20:12:45.109-04:00";
    String endDate = "2023-10-15T20:35:06.793-02:00";
    String jsonld="{\n" +
            "  \"@context\" : [ {\n" +
            "    \"xsd\" : \"http://www.w3.org/2001/XMLSchema#\",\n" +
            "    \"ex\" : \"https://example.org/\",\n" +
            "    \"prov\" : \"http://www.w3.org/ns/prov#\"\n" +
            "  }, \"https://openprovenance.org/prov-jsonld/context.jsonld\" ],\n" +
            "  \"@graph\" : [ {\n" +
            "    \"@type\" : \"Activity\",\n" +
            "    \"@id\" : \"ex:a\",\n" +
            "    \"startTime\" : \"2023-09-08T20:12:45.109-04:00\",\n" +
            "    \"endTime\" : \"2023-10-15T20:35:06.793-02:00\"\n" +
            "  } ]\n" +
            "}";


    XMLGregorianCalendar noTZstart= pFactory.newISOTime(startDate);
    XMLGregorianCalendar noTZend= pFactory.newISOTime(endDate);
    XMLGregorianCalendar yesTZstart= pFactory.newISOTimeTZ(startDate);
    XMLGregorianCalendar yesTZend= pFactory.newISOTimeTZ(endDate);

    public void testDateJsonldUTC() {
     //   System.out.println(jsonld);

        ProvDeserialiser deserial = new ProvDeserialiser(new ObjectMapper(), DateTimeOption.UTC);
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

   //     System.out.println("UTC: activity.getStartTime().toXMLFormat() = " + activity.getStartTime().toXMLFormat());
  //      System.out.println("UTC: activity.getEndTime().toXMLFormat() = " + activity.getEndTime().toXMLFormat());
    }
    public void testDateJsonldPreserve() {

        ProvDeserialiser deserial = new ProvDeserialiser(new ObjectMapper(), DateTimeOption.PRESERVE);
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
        assertEquals(-4 * 60, activity.getStartTime().getTimezone());
        assertEquals(-2 * 60, activity.getEndTime().getTimezone());
        // whereas the dates constructed with TZ method have their original TZ offset
        assertEquals(-4 * 60, yesTZstart.getTimezone());
        assertEquals(-2 * 60, yesTZend.getTimezone());

    //    System.out.println("PRESERVE: activity.getStartTime().toXMLFormat() = " + activity.getStartTime().toXMLFormat());
    //    System.out.println("PRESERVE: activity.getEndTime().toXMLFormat() = " + activity.getEndTime().toXMLFormat());
    }

    public void testDateJsonldTZ() {

        TimeZone tz = TimeZone.getTimeZone("Japan");


        ProvDeserialiser deserial = new ProvDeserialiser(new ObjectMapper(), DateTimeOption.TIMEZONE, tz);
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
        assertEquals(9 * 60, activity.getStartTime().getTimezone());
        assertEquals(9 * 60, activity.getEndTime().getTimezone());
        // whereas the dates constructed with TZ method have their original TZ offset
        assertEquals(-4 * 60, yesTZstart.getTimezone());
        assertEquals(-2 * 60, yesTZend.getTimezone());

   //     System.out.println("TZ: activity.getStartTime().toXMLFormat() = " + activity.getStartTime().toXMLFormat());
  //      System.out.println("TZ: activity.getEndTime().toXMLFormat() = " + activity.getEndTime().toXMLFormat());
    }

}
