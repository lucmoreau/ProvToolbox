package org.openprovenance.prov.bean.xml;
import java.util.List;
import tools.jackson.dataformat.xml.annotation.JacksonXmlRootElement;


@JacksonXmlRootElement(localName="entity")
public class Entity implements Statement {
	public String id;
	public List<Attr> attr;
}
