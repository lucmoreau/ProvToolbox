package org.openprovenance.prov.nf.xml;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.openprovenance.prov.model.StatementOrBundle.Kind;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;


@JacksonXmlRootElement(localName="wasInfluencedBy")
public class WasInfluencedBy implements Statement {
	public List<String> id;
	public List<String> influencee;
	public List<String> influencer;
	public List<Attr> attr;

	@Override
	public Kind getKind() {
		return Kind.PROV_INFLUENCE;
	}
	
	@Override
	public void normalize() {
		Collections.sort(id);
		Collections.sort(influencee);
		Collections.sort(influencer);
		Collections.sort(attr);				
	}
	
	@Override
	public List<Attr> getAttr() {
		return attr;
	}
	
	@Override
	public LinkedList<List<String>> paramsAsListsofStrings() {
		LinkedList<List<String>> l1=new LinkedList<List<String>>();
		l1.add(id);
		l1.add(influencee);
		l1.add(influencer);
		return l1;
	}	
}
