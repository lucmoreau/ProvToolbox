package org.openprovenance.prov.model;

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

        assertEquals("original time format, with offset, not preserved", yesTZtime.toXMLFormat(), date);
        assertEquals("transformed format is not expressed in default timezone", noTZtime.getTimezone(),pFactory.newTimeNow().getTimezone());
    }

    public void testDate2() {
        // now-date in the default timezone
        String date=pFactory.newTimeNow().toXMLFormat();


        XMLGregorianCalendar noTZtime = pFactory.newISOTime(date);
        XMLGregorianCalendar yesTZtime = pFactory.newISOTimeTZ(date);

        System.out.println("time zone (no):  " + noTZtime);
        System.out.println("time zone (yes): " + yesTZtime);

        assertEquals(noTZtime,yesTZtime);
    }

}
