package org.openprovenance.prov.bean.xml;
import java.util.List;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;


@JacksonXmlRootElement(localName="hadMember")
public class HadMember implements Statement {
	public String id;
	public String collection;
	public List<String> entity;
	public List<Attr> attr;
}
