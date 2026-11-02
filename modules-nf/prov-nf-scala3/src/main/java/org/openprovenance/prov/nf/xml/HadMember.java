package org.openprovenance.prov.nf.xml;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.openprovenance.prov.model.StatementOrBundle.Kind;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;


@JacksonXmlRootElement(localName="hadMember")
public class HadMember implements Statement {
	public List<String> id;
	public List<String> collection;
	public List<String> entity;
	public List<Attr> attr;
	

	@Override
	public Kind getKind() {
		return Kind.PROV_MEMBERSHIP;
	}
	
	@Override
	public void normalize() {
		Collections.sort(id);
		Collections.sort(collection);
		Collections.sort(entity);
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
		l1.add(collection);
		l1.add(entity);
		return l1;
	}	
}
