package org.openprovenance.prov.bean.xml;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;


@JacksonXmlRootElement(localName="used")
public class Used implements Statement {
	public String id;
	public String entity;
	public String activity;
	public String time;
	public List<Attr> attr;
}
