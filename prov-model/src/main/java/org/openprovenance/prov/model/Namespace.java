package org.openprovenance.prov.model;

import java.util.Hashtable;

import javax.xml.XMLConstants;


import org.openprovenance.prov.model.ProvUtilities;
import org.openprovenance.prov.model.exception.QualifiedNameException;

/** A class to manipulate Namespaces when creating, serializing and converting prov documents. 
 * @author Luc Moreau 
 * */

public class Namespace implements QualifiedNameExport {
    
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
    
    public void extendWith(Namespace ns) {
	if (ns==null) return;
	if (ns.getDefaultNamespace()!=null) {
	    registerDefault(ns.getDefaultNamespace());
	}
	for (String prefix: ns.prefixes.keySet()) {
	    register(prefix, ns.prefixes.get(prefix));
	}
    }


    private Hashtable<String, String> prefixes=new Hashtable<String, String>();
    private Hashtable<String, String> namespaces=new Hashtable<String, String>();
    private String defaultNamespace=null;
    
    private Namespace parent=null;
    
    public void setParent(Namespace parent) {
	this.parent=parent;
    }
    public Namespace getParent() {
	return parent;
    }

    public void addKnownNamespaces() {
	getPrefixes().put("prov",NamespacePrefixMapper.PROV_NS);
	getNamespaces().put(NamespacePrefixMapper.PROV_NS,"prov");
	getPrefixes().put("xsd",NamespacePrefixMapper.XSD_NS);
	getNamespaces().put(NamespacePrefixMapper.XSD_NS,"xsd");
    }
	    
    public Namespace() {}
    
    public Namespace(Hashtable<String,String> pref) {
	for (java.util.Map.Entry<String,String> entry: pref.entrySet()) {
	    prefixes.put(entry.getKey(),entry.getValue());
	    namespaces.put(entry.getValue(),entry.getKey());
	}
    }
    
    public Namespace(Namespace other) {
	this.defaultNamespace=other.defaultNamespace;
	this.prefixes=new Hashtable<String, String>();
	prefixes.putAll(other.prefixes);
	this.namespaces=new Hashtable<String,String>();
	namespaces.putAll(other.namespaces);
    }
    public String getDefaultNamespace () {
	return defaultNamespace;
    }
    
    public void setDefaultNamespace(String defaultNamespace) {
	this.defaultNamespace=defaultNamespace;
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
    public void registerDefault(String namespace) {
	register(null,namespace);
    }

    public void register(String prefix, String namespace) {
 	if ((prefix == null) || (prefix.equals(XMLConstants.DEFAULT_NS_PREFIX))) {
 	    if (defaultNamespace == null) {
 		defaultNamespace = namespace;
 	    } else {
 		newPrefix(namespace);
 	    }
 	} else {
 	    String old = prefixes.get(prefix);
 	    if (old == null) {
 		prefixes.put(prefix, namespace);
 		if (namespaces.get(namespace)==null) {
 		    // make sure we don't overwrite an existing namespace
 		    namespaces.put(namespace,prefix);
 		}
 	    } else {
 		newPrefix(namespace);
 	    }
 	}
     }

     static final public String xmlns = "pre_";
     int prefixCount = 0;

     public void newPrefix(String namespace) {
 	boolean success = false;
 	while (!success) {
 	    String old=namespaces.get(namespace);
 	    if (old!=null) return;
 	    int count = prefixCount++;
 	    String newPrefix = xmlns + count; //gensym a new prefix
 	    String oldPrefix = prefixes.get(newPrefix); // check that it hasn't been used yet
 	    if (oldPrefix == null) {
 		prefixes.put(newPrefix, namespace);
 		success = true;
 	    
 		if (namespaces.get(namespace)==null) {
 		    // make sure we don't overwrite namespace
 		    namespaces.put(namespace, newPrefix);
 		}
 	    }
 	}
     }

    
     public void unregister(String prefix, String namespace) {
	 String val=prefixes.get(prefix);
	 if (val!=null){
	     if (val.equals(namespace)) {
		 prefixes.remove(prefix);
		 namespaces.remove(namespace);
	     }
	 }
     }
     
     public void unregisterDeafult(String namespace) {
	 String val=getDefaultNamespace();
	 if (val!=null){
	     if (val.equals(namespace)) {
		 setDefaultNamespace(null);
	     }
	 }
     }
	    
    static ProvUtilities u=new ProvUtilities();
    
    /* A Utility to find all namespaces (and associated prefixes). Prefixes are adopted first-come. 
     * If a prefix is bound to another namespace, then a new prefix is generated for that namespace. */
    
    static public Namespace gatherNamespaces(Document doc) {
   	NamespaceGatherer gatherer=new NamespaceGatherer();	
   	u.forAllStatementOrBundle(doc.getStatementOrBundle(), 
   	                          gatherer);
   	
   	Namespace ns=gatherer.getNamespace();
   	return ns;
    }
    
    static public Namespace gatherNamespaces(NamedBundle bundle) {
   	NamespaceGatherer gatherer=new NamespaceGatherer();	
   	u.forAllStatement(bundle.getStatement(), gatherer);
   	Namespace ns=gatherer.getNamespace();
   	return ns;
    }
    
   
    public QualifiedName stringToQualifiedName(String id, ProvFactory pFactory) {
	if (id == null)
	    return null;
	int index = id.indexOf(':');
	if (index == -1) {
	    String tmp = getDefaultNamespace();
	    if (tmp == null && parent != null) tmp = parent.getDefaultNamespace();
	    if (tmp==null) throw new NullPointerException("Namespace.stringToQualifiedName(: Null namespace for "+id);
	    return pFactory.newQualifiedName(tmp, id, null);
	}
	String prefix = id.substring(0, index);
	String local = id.substring(index + 1, id.length());
	
	//TODO: why have special cases here, prov and xsd are now declared prefixes in namespaces
	if ("prov".equals(prefix)) {
	    return pFactory.newQualifiedName(NamespacePrefixMapper.PROV_NS, local, prefix);
	} else if ("xsd".equals(prefix)) {
	    return pFactory.newQualifiedName(NamespacePrefixMapper.XSD_NS, // + "#", // RDF ns ends
								 // in #, not
								 // XML ns.
			     local, prefix);
	} else {
	    String tmp=prefixes.get(prefix);
	    if (tmp==null) {
		if (parent!=null) {
		    return parent.stringToQualifiedName(id, pFactory);
		} else {
		    throw new QualifiedNameException("Namespace.stringToQualifiedName(): Null namespace for " + id + " namespace " + this);
		}
	    }
	    return pFactory.newQualifiedName(tmp, local, prefix);
	}
    }


     
    static public String qualifiedNameToStringWithNamespace(QualifiedName name) {
 	Namespace ns=Namespace.getThreadNamespace();
 	return ns.qualifiedNameToString(name);
     }
     
    /*
     * (non-Javadoc)
     * @see org.openprovenance.prov.model.QualifiedNameExport#qualifiedNameToString(org.openprovenance.prov.model.QualifiedName)
     */
    public String qualifiedNameToString(QualifiedName name) {
	return qualifiedNameToString(name,null);
    }
   
    /**
     * @param name the QualifiedName to convert to string
     * @param child argument used just for the purpose of debugging when throwing an exception
     * @return a string representation of the QualifiedName
     */
  
    public String qualifiedNameToString(QualifiedName name, Namespace child) {
 	if ((getDefaultNamespace()!=null) 
 		&& (getDefaultNamespace().equals(name.getNamespaceURI()))) {
 	    return name.getLocalPart();
 	} else {
 	    String pref=getNamespaces().get(name.getNamespaceURI());
 	    if (pref!=null)  {
 		return pref + ":" + name.getLocalPart();
 	    } else {
 		if (parent!=null) {
 		    return parent.qualifiedNameToString(name,this);
 		}
 		else 
 		    throw new QualifiedNameException("unknown qualified name " + name 
 		                                     + " with namespace " + toString()
 		                                     + ((child==null)? "" : (" and " + child)));
 	    }
 	}
     }

    public String toString() {
	return "[Namespace (" + defaultNamespace + ") " + prefixes + "]";
    }





    
}
