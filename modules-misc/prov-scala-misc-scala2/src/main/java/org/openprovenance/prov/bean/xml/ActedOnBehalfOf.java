package org.openprovenance.prov.bean.xml;
import java.util.List;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;


@JacksonXmlRootElement(localName="actedOnBehalfOf")
public class ActedOnBehalfOf implements Statement {
	public String id;
	public String delegate;
	public String responsible;
	public String activity;
	public List<Attr> attr;
}
