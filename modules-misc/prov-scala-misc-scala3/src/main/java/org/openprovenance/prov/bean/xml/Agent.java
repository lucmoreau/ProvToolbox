package org.openprovenance.prov.bean.xml;
import java.util.List;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;


@JacksonXmlRootElement(localName="agent")
public class Agent implements Statement {
	public String id;
	public List<Attr> attr;
}
