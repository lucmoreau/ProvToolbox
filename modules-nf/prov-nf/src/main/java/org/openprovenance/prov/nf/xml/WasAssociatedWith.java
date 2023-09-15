package org.openprovenance.prov.nf.xml;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.openprovenance.prov.model.StatementOrBundle.Kind;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;


@JacksonXmlRootElement(localName="wasAssociatedWith")
public class WasAssociatedWith implements Statement {
	public List<String> id;
	public List<String> activity;
	public List<String> agent;
	public List<String> plan;
	public List<Attr> attr;
	

	@Override
	public Kind getKind() {
		return Kind.PROV_ASSOCIATION;
	}
	
	@Override
	public void normalize() {
		Collections.sort(id);
		Collections.sort(activity);
		Collections.sort(agent);
		Collections.sort(plan);
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
		l1.add(activity);
		l1.add(agent);
		l1.add(plan);
		return l1;
	}	

}
