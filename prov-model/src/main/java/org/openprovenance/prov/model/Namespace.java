package org.openprovenance.prov.model;

import java.util.Hashtable;

import javax.xml.namespace.QName;


import org.openprovenance.prov.model.ProvUtilities;

public class Namespace {
    
    private static ThreadLocal<Namespace> threadNamespace =
	    new ThreadLocal<Namespace> () {
	      protected synchronized Namespace initialValue () {
		  return new Namespace();
	      }
    };
    

    public static Namespace getThreadNamespace() {
        return threadNamespace.get();
    }


    public static Namespace withThreadNamespace(Namespace ns) {
        return threadNamespace.get().set(ns);
    }

    private Namespace set(Namespace ns) {
	if (ns==null) return null;
	Hashtable<String, String> tmp1=new Hashtable<String, String>();
	tmp1.putAll(ns.prefixes);
	Hashtable<String, String> tmp2=new Hashtable<String, String>();
	tmp2.putAll(ns.namespaces);
	this.prefixes=tmp1;
	this.namespaces=tmp2;
	this.defaultNamespace=ns.defaultNamespace;
	return this;
    }


    Hashtable<String, String> prefixes=new Hashtable<String, String>();
    Hashtable<String, String> namespaces=new Hashtable<String, String>();
    String defaultNamespace=null;
	    
    public Namespace() {}
    
    public Namespace(Hashtable<String,String> pref) {
	for (java.util.Map.Entry<String,String> entry: pref.entrySet()) {
	    prefixes.put(entry.getKey(),entry.getValue());
	    namespaces.put(entry.getValue(),entry.getKey());
	}
    }
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
	u.forAllStatementOrBundle(doc.getStatementOrBundle(), 
	                          gatherer);
	
	Namespace ns=new Namespace();
	ns.prefixes=gatherer.getPrefixes();
	ns.defaultNamespace=gatherer.defaultNamespace;
	ns.namespaces=gatherer.getNamespaces();
	return ns;
    }

    public QName stringToQName(String id) {
	if (id == null)
	    return null;
	int index = id.indexOf(':');
	if (index == -1) {
	    return new QName(getDefaultNamespace(), id);
	}
	String prefix = id.substring(0, index);
	String local = id.substring(index + 1, id.length());
	if ("prov".equals(prefix)) {
	    return new QName(NamespacePrefixMapper.PROV_NS, local, prefix);
	} else if ("xsd".equals(prefix)) {
	    return new QName(NamespacePrefixMapper.XSD_NS, // + "#", // RDF ns ends
								 // in #, not
								 // XML ns.
			     local, prefix);
	} else {
	    return new QName(namespaces.get(prefix), local, prefix);
	}
    }

    
}