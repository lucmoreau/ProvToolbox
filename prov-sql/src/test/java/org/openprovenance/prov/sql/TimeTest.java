package org.openprovenance.prov.sql;


import java.util.Date;

import javax.xml.datatype.XMLGregorianCalendar;

import org.openprovenance.prov.xml.ProvUtilities;

import junit.framework.TestCase;

public class TimeTest extends TestCase {
	private ProvFactory pFactory=ProvFactory.getFactory();
	
	public TimeTest (String testName) {
		super(testName);
	}
  
	

	public void testTime1() {
		XMLGregorianCalendar time=pFactory.newISOTime("2012-12-03T21:08:16.686Z");
		Date d=ProvUtilities.toDate(time);
		XMLGregorianCalendar time2=ProvUtilities.toXMLGregorianCalendar(d);
		assertEquals(time, time2);

	}
	
}
