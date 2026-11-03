package org.openprovenance.prov.bean.xml;
import java.util.List;
import tools.jackson.dataformat.xml.annotation.JacksonXmlRootElement;


@JacksonXmlRootElement(localName="wasAttributedTo")
public class WasAttributedTo implements Statement {
	public String id;
	public String entity;
	public String agent;
	public List<Attr> attr;
}
