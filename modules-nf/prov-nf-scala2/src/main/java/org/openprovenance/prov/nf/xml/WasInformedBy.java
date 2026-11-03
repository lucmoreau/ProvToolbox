package org.openprovenance.prov.nf.xml;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.openprovenance.prov.model.StatementOrBundle.Kind;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;


@JacksonXmlRootElement(localName="wasInformedBy")
public class WasInformedBy implements Statement {
	public List<String> id;
	public List<String> informed;
	public List<String> informant;
	public List<Attr> attr;

	@Override
	public Kind getKind() {
		return Kind.PROV_COMMUNICATION;
	}
	
	@Override
	public void normalize() {
		Collections.sort(id);
		Collections.sort(informed);
		Collections.sort(informant);
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
		l1.add(informed);
		l1.add(informant);
		return l1;
	}	
}
