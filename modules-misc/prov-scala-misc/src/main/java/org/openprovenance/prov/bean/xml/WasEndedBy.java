package org.openprovenance.prov.bean.xml;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;


@JacksonXmlRootElement(localName="wasEndedBy")
public class WasEndedBy implements Statement {
	public String id;
	public String activity;
	public String trigger;
	public String ender;
	public String time;
	public List<Attr> attr;
}
