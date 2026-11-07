package org.openprovenance.prov.bean.xml;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;
import tools.jackson.dataformat.xml.annotation.JacksonXmlRootElement;


@JacksonXmlRootElement(localName="activity")
public class Activity implements Statement {
	public String id;
	public String startTime;
	public String endTime;
	public List<Attr> attr;
}
