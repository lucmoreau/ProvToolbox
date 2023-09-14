package org.openprovenance.prov.bean.xml;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;


@JacksonXmlRootElement(localName="wasStartedBy")
public class WasStartedBy implements Statement {
	public String id;
	public String activity;
	public String trigger;
	public String starter;
	public String time;
	public List<Attr> attr;
}
