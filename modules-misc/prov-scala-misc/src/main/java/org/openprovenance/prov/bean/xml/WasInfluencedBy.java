package org.openprovenance.prov.bean.xml;
import java.util.List;
import tools.jackson.dataformat.xml.annotation.JacksonXmlRootElement;


@JacksonXmlRootElement(localName="wasInfluencedBy")
public class WasInfluencedBy implements Statement {
	public String id;
	public String influencee;
	public String influencer;
	public List<Attr> attr;
}
