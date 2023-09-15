package org.openprovenance.prov.nf.xml;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.model.StatementOrBundle.Kind;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;


@JacksonXmlRootElement(localName="actedOnBehalfOf")
public class ActedOnBehalfOf implements Statement {
	public List<String> id;
	public List<String> delegate;
	public List<String> responsible;
	public List<String> activity;
	public List<Attr> attr;

	@Override
	public Kind getKind() {
		return StatementOrBundle.Kind.PROV_DELEGATION;
	}
	@Override
	public LinkedList<List<String>> paramsAsListsofStrings() {
		LinkedList<List<String>> l1=new LinkedList<List<String>>();
		l1.add(id);
		l1.add(delegate);
		l1.add(responsible);
		l1.add(activity);
		return l1;
	}
	
	@Override
	public void normalize() {
		Collections.sort(id);
		Collections.sort(delegate);
		Collections.sort(responsible);
		Collections.sort(activity);
		Collections.sort(attr);
		
	}
	@Override
	public List<Attr> getAttr() {
		return attr;
	}
}
