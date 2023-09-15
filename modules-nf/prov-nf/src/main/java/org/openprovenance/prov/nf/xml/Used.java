package org.openprovenance.prov.nf.xml;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


import org.openprovenance.prov.model.StatementOrBundle.Kind;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;


@JacksonXmlRootElement(localName="used")
public class Used implements Statement {
	public List<String> id;
	public List<String> entity;
	public List<String> activity;
	public List<String> time;
	public List<Attr> attr;

	@Override
	public LinkedList<List<String>> paramsAsListsofStrings() {
		LinkedList<List<String>> l1=new LinkedList<List<String>>();
		l1.add(id);
		l1.add(entity);
		l1.add(activity);
		l1.add(time);
		return l1;
	}
	@Override
	public Kind getKind() {
		return Kind.PROV_USAGE;
	}
	@Override
	public void normalize() {
		Collections.sort(id);
		Collections.sort(entity);
		Collections.sort(activity);
		Collections.sort(time);
		Collections.sort(attr);		
		
	}
	@Override
	public List<Attr> getAttr() {
		return attr;
	}
}
