package org.openprovenance.prov.bean.xml;
import java.util.List;
import tools.jackson.dataformat.xml.annotation.JacksonXmlRootElement;


@JacksonXmlRootElement(localName="wasAssociatedWith")
public class WasAssociatedWith implements Statement {
	public String id;
	public String activity;
	public String agent;
	public String plan;
	public List<Attr> attr;

}
