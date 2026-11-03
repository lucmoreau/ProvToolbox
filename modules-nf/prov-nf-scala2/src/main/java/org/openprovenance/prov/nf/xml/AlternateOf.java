package org.openprovenance.prov.nf.xml;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.openprovenance.prov.model.StatementOrBundle.Kind;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;


@JacksonXmlRootElement(localName="alternateOf")
public class AlternateOf implements Statement {
	public List<String> id;
	public List<String> alternate1;
	public List<String> alternate2;
	public List<Attr> attr;
	
	@Override
	public Kind getKind() {
		return Kind.PROV_ALTERNATE;
	}
	
	@Override
	public void normalize() {
		Collections.sort(id);
		Collections.sort(alternate1);
		Collections.sort(alternate2);
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
		l1.add(alternate1);
		l1.add(alternate2);
		return l1;
	}	
}
