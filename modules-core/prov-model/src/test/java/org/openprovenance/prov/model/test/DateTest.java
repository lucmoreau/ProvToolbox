package org.openprovenance.prov.model.test;

import junit.framework.TestCase;
import org.openprovenance.prov.vanilla.ProvFactory;

import javax.xml.datatype.XMLGregorianCalendar;

public class DateTest extends TestCase {

    final ProvFactory pFactory=new ProvFactory();



    public void testDate1() {
        String date="2023-09-22T14:19:04.760-05:00";


        XMLGregorianCalendar noTZtime = pFactory.newISOTime(date);
        XMLGregorianCalendar yesTZtime = pFactory.newISOTimeTZ(date);

        System.out.println("time zone (no):  " + noTZtime);
        System.out.println("time zone (yes): " + yesTZtime);
        System.out.println("time now: " + pFactory.newTimeNow());

        assertEquals("original time format, with offset, not preserved", yesTZtime.toXMLFormat(), date);
        // this test does not work, the result depends on the timezone of the date and where the test in run
        // assertEquals("transformed format is not expressed in default timezone", noTZtime.getTimezone(),pFactory.newTimeNow().getTimezone());
    }

    public void testDate2() {
        // now-date in the default timezone
        String date=pFactory.newTimeNow().toXMLFormat();


        XMLGregorianCalendar noTZtime = pFactory.newISOTime(date);
        XMLGregorianCalendar yesTZtime = pFactory.newISOTimeTZ(date);

    //    System.out.println("time zone (no):  " + noTZtime);
    //    System.out.println("time zone (yes): " + yesTZtime);

        assertEquals(noTZtime,yesTZtime);
    }

    public void testDate3() {
        String date1 = "2023-09-22T14:19:04.760-05:00";
        String date2 = "2023-09-22T15:19:04.760-04:00";


        XMLGregorianCalendar noTZtime1 = pFactory.newISOTime(date1);
        XMLGregorianCalendar yesTZtime1 = pFactory.newISOTimeTZ(date1);
        XMLGregorianCalendar noTZtime2 = pFactory.newISOTime(date2);
        XMLGregorianCalendar yesTZtime2 = pFactory.newISOTimeTZ(date2);

        assertEquals(noTZtime1,yesTZtime1);
        assertEquals(noTZtime2,yesTZtime2);
        assertEquals(noTZtime1,noTZtime2);

        assertNotSame(noTZtime1.toXMLFormat(),yesTZtime1.toXMLFormat());
        assertNotSame(noTZtime2.toXMLFormat(),yesTZtime2.toXMLFormat());

        //noTZtime1 and noTZtime2 lexical representations are equal, since they have been normalised
        assertEquals(noTZtime1.toXMLFormat(),noTZtime2.toXMLFormat());

        //yesTZtime1 and yesTZtime2 lexical representations are not equal, since they have not been normalised
        assertNotSame(yesTZtime1.toXMLFormat(),yesTZtime2.toXMLFormat());

        //yesTZtime1 and yesTZtime2 lexical representations are  equal, after being normalised
        assertEquals(yesTZtime1.normalize().toXMLFormat(),yesTZtime2.normalize().toXMLFormat());

    }

    public void testDate4() {
        String date1 = "2023-09-22T14:19:04.760"; // no timezone offset specified, so it's the default timezone of the system
        String date2 = "2023-09-22T14:19:04.760Z";
        String date3 = "2023-09-22T14:19:04.760+00:00";


        XMLGregorianCalendar noTZtime1 = pFactory.newISOTime(date1);
        XMLGregorianCalendar yesTZtime1 = pFactory.newISOTimeTZ(date1);
        XMLGregorianCalendar normalizedTZtime1 = pFactory.newISOTimeUTC(date1);
        XMLGregorianCalendar noTZtime2 = pFactory.newISOTime(date2);
        XMLGregorianCalendar yesTZtime2 = pFactory.newISOTimeTZ(date2);
        XMLGregorianCalendar noTZtime3 = pFactory.newISOTime(date3);
        XMLGregorianCalendar yesTZtime3 = pFactory.newISOTimeTZ(date3);

        assertEquals(noTZtime1,yesTZtime1);
        assertEquals(noTZtime2,yesTZtime2);
        assertEquals(noTZtime3,yesTZtime3);

        assertEquals(noTZtime3,noTZtime2);

    //    System.out.println("noTZtime1.toXMLFormat() = " + noTZtime1.toXMLFormat());
   //     System.out.println("noTZtime2.toXMLFormat() = " + noTZtime2.toXMLFormat());
        assertNotSame(noTZtime1.toXMLFormat(),yesTZtime1.toXMLFormat());
        assertNotSame(noTZtime2.toXMLFormat(),yesTZtime2.toXMLFormat());
   //     System.out.println("normalizedTZtime1.toXMLFormat() = " + normalizedTZtime1.toXMLFormat());


        //noTZtime1 and noTZtime3 lexical representations are equal
        assertEquals(noTZtime2.toXMLFormat(),noTZtime3.toXMLFormat());

        //yesTZtime1 and yesTZtime2 lexical representations are also equal
        assertEquals(noTZtime2.toXMLFormat(),noTZtime3.toXMLFormat());


    }

    // arbitrary precision from fractional seconds is not supported by XMLGregorianCalendar
    public void testDate5() {
        String date1 = "2023-09-08T14:12:45.10931231236545213876";
        String date2 = "2023-09-08T14:12:45.109321321312321432523";
        XMLGregorianCalendar time1 = pFactory.newISOTime(date1);
        XMLGregorianCalendar time2 = pFactory.newISOTime(date2);
        assertEquals(time1, time2);
        System.out.println("time1 = " + time1);
        System.out.println("time2 = " + time2);
    }

}
