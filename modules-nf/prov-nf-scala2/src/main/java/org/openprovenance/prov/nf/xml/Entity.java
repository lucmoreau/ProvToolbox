package org.openprovenance.prov.nf.xml;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.openprovenance.prov.model.StatementOrBundle.Kind;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;


@JacksonXmlRootElement(localName="entity")
public class Entity implements Statement {
	public List<String> id;
	public List<Attr> attr;
	

	@Override
	public Kind getKind() {
		return Kind.PROV_ENTITY;
	}

	@Override
	public void normalize() {
		Collections.sort(id);
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
		return l1;
	}	
}
