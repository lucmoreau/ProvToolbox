package org.openprovenance.prov.xml;
import java.util.List;

public interface HasExtensibility {
	@SuppressWarnings("rawtypes")
	public List getAny();
	public java.util.Hashtable<String,List<Object>> attributesWithNamespace(String namespace);
} 
