package org.openprovenance.prov.bean.xml;
import java.util.List;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;


@JacksonXmlRootElement(localName="specializationOf")
public class SpecializationOf implements Statement {
	public String id;
	public String specificEntity;
	public String generalEntity;
	public List<Attr> attr;
}
