package org.openprovenance.prov.xml;

import java.util.Hashtable;

import org.openprovenance.prov.model.NamedBundle;
import org.openprovenance.prov.model.Statement;

import org.openprovenance.prov.model.Document;

public class Namespace {
    

    Hashtable<String, String> prefixes=new Hashtable<String, String>();
    Hashtable<String, String> namespaces=new Hashtable<String, String>();
    String defaultNamespace=null;
	    
    
    public String getDefaultNamespace () {
	return defaultNamespace;
    }
    
    public Hashtable<String, String> getPrefixes() {
	return prefixes;
    }
    public Hashtable<String, String> getNamespaces() {
	return namespaces;
    }
    
    
    public boolean check(String prefix, String namespace) {
	String knownAs=prefixes.get(prefix);
	return namespace==knownAs;
    }
    
    static ProvUtilities u=new ProvUtilities();
    
    /* A Utility to find all namespaces (and associated prefixes). Prefixes are adopted first-come. 
     * If a prefix is bound to another namespace, then a new prefix is generated for that namespace. */
    
    static public Namespace gatherNamespaces(Document doc) {
	NamespaceGatherer gatherer=new NamespaceGatherer();	
	for (Statement s: u.getStatement(doc)) {
	    u.run(s, gatherer);
	}
	for (NamedBundle bu: u.getBundle(doc)) {
	    gatherer.register(bu.getId());
	    for (Statement s2: u.getStatement(bu)) {
		u.run(s2,gatherer);
	    }
	}
	Namespace ns=new Namespace();
	ns.prefixes=gatherer.getPrefixes();
	ns.defaultNamespace=gatherer.defaultNamespace;
	ns.namespaces=gatherer.getNamespaces();
	return ns;
    }
    
    
}