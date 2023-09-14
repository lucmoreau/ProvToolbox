package org.openprovenance.prov.bean.xml;
import java.util.List;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;


@JacksonXmlRootElement(localName="alternateOf")
public class AlternateOf implements Statement {
	public String id;
	public String alternate1;
	public String alternate2;
	public List<Attr> attr;
}
