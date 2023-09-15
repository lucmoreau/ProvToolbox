package org.openprovenance.prov.nf.xml;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.model.StatementOrBundle.Kind;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;


@JacksonXmlRootElement(localName="activity")
public class Activity implements Statement {
	public List<String> id;
	public List<String> startTime;
	public List<String> endTime;
	public List<Attr> attr;

	
	@Override
	public Kind getKind() {
		return StatementOrBundle.Kind.PROV_ACTIVITY;
	}
	
	@Override
	public void normalize() {
		Collections.sort(id);
		Collections.sort(startTime);
		Collections.sort(endTime);
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
		l1.add(startTime);
		l1.add(endTime);
		return l1;
	}
}
