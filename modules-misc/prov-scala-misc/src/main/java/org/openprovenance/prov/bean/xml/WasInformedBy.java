package org.openprovenance.prov.bean.xml;
import java.util.List;
import tools.jackson.dataformat.xml.annotation.JacksonXmlRootElement;


@JacksonXmlRootElement(localName="wasInformedBy")
public class WasInformedBy implements Statement {
	public String id;
	public String informed;
	public String informant;
	public List<Attr> attr;
}
