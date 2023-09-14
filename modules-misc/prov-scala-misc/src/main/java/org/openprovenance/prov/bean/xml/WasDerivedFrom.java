package org.openprovenance.prov.bean.xml;
import java.util.List;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;


@JacksonXmlRootElement(localName="wasDerivedFrom")
public class WasDerivedFrom implements Statement {
	public String id;
	public String generatedEntity;
	public String usedEntity;
	public String activity;
	public String generation;
	public String usage;
	public List<Attr> attr;
}
