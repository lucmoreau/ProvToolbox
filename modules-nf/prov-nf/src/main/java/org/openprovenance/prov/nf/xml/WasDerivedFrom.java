package org.openprovenance.prov.nf.xml;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.openprovenance.prov.model.StatementOrBundle.Kind;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;


@JacksonXmlRootElement(localName="wasDerivedFrom")
public class WasDerivedFrom implements Statement {
	public List<String> id=new LinkedList<String>();
	public List<String> generatedEntity=new LinkedList<String>();
	public List<String> usedEntity=new LinkedList<String>();
	public List<String> activity=new LinkedList<String>();
	public List<String> generation=new LinkedList<String>();
	public List<String> usage=new LinkedList<String>();
	public List<Attr> attr=new LinkedList<Attr>();
	

	@Override
	public Kind getKind() {
		return Kind.PROV_DERIVATION;
	}
	
	@Override
	public void normalize() {
		Collections.sort(id);
		Collections.sort(generatedEntity);
		Collections.sort(usedEntity);
		Collections.sort(activity);
		Collections.sort(generation);
		Collections.sort(usage);
		Collections.sort(attr);				
	}
	
	/*
	@Override
	public String toString() {
		return "WasDerivedFrom [id=" + id + ", generatedEntity=" + generatedEntity + ", usedEntity=" + usedEntity
				+ ", activity=" + activity + ", generation=" + generation + ", usage=" + usage + ", attr=" + attr + "]";
	}
	*/
	@Override
	public List<Attr> getAttr() {
		return attr;
	}
	
	@Override
	public LinkedList<List<String>> paramsAsListsofStrings() {
		LinkedList<List<String>> l1=new LinkedList<List<String>>();
		l1.add(id);
		l1.add(generatedEntity);
		l1.add(usedEntity);
		l1.add(activity);
		l1.add(generation);
		l1.add(usage);
		return l1;
	}	
	
}
