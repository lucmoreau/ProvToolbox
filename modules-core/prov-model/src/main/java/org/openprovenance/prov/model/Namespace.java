package org.openprovenance.prov.model;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

import org.openprovenance.prov.model.ProvUtilities;
import org.openprovenance.prov.model.ProvUtilities.BuildFlag;
import org.openprovenance.prov.model.exception.QualifiedNameException;

/** A class to manipulate Namespaces when creating, serializing and converting prov documents. 
 * @author Luc Moreau 
 * */

public class Namespace  {

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
        this.parent=ns.parent;
        return this;
    }

    /**
     * Extends this Namespace with all the prefix/namespace registration of the Namespace received as argument. 
     * @param ns the {@link Namespace} whose prefix/namespace registration are added to this {@link Namespace}.
     */
    public void extendWith(Namespace ns) {
        if (ns==null) return;
        if (ns.getDefaultNamespace()!=null) {
            registerDefault(ns.getDefaultNamespace());
        }
        for (String prefix: ns.prefixes.keySet()) {
            register(prefix, ns.prefixes.get(prefix));
        }
    }


    protected Map<String, String> prefixes=new Hashtable<String, String>();
    protected Map<String, String> namespaces=new Hashtable<String, String>();
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

    public Namespace(Map<String,String> pref) {
        for (java.util.Map.Entry<String,String> entry: pref.entrySet()) {
            prefixes.put(entry.getKey(),entry.getValue());
            namespaces.put(entry.getValue(),entry.getKey());
        }
    }

    public Namespace(Namespace other) {
        this.defaultNamespace=other.defaultNamespace;
        this.prefixes= new HashMap<>();
        prefixes.putAll(other.prefixes);
        this.namespaces= new HashMap<>();
        namespaces.putAll(other.namespaces);
    }
    public String getDefaultNamespace () {
        return defaultNamespace;
    }

    public void setDefaultNamespace(String defaultNamespace) {
        this.defaultNamespace=defaultNamespace;
    }

    public Map<String, String> getPrefixes() {
        return prefixes;
    }
    public Map<String, String> getNamespaces() {
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

    static public Namespace gatherNamespaces(Bundle bundle) {
        NamespaceGatherer gatherer=new NamespaceGatherer();	
        u.forAllStatement(bundle.getStatement(), gatherer);
        gatherer.register(bundle.getId());
        Namespace ns=gatherer.getNamespace();
        return ns;
    }

    static public Namespace gatherNamespaces(Bundle bundle, ProvFactory pFactory) {
        NamespaceGatherer gatherer=new NamespaceGatherer();	
        u.forAllStatement(bundle.getStatement(), gatherer);
        gatherer.register(bundle.getId());
        Namespace ns=gatherer.getNamespace();
        Namespace ns2=pFactory.newNamespace(ns);
        return ns2;
    }

    public QualifiedName stringToQualifiedName(String id, ProvFactory pFactory) {
        return stringToQualifiedName(id,pFactory,true);
    }

    static QualifiedNameUtils qnU = new QualifiedNameUtils();

    public QualifiedName stringToQualifiedName(String id, ProvFactory pFactory, boolean isEscaped) {
        if (id == null)
            return null;
        final int index = id.indexOf(':');
        if (index == -1) {
            String tmp = getDefaultNamespace();
            if (tmp == null) {
                if (parent != null) {
                    tmp = parent.getDefaultNamespace();
                    if (tmp ==null) throw new NullPointerException("Namespace.stringToQualifiedName(): Null namespace for parent "+id);
                } else {
                    throw new NullPointerException("Namespace.stringToQualifiedName(): Null namespace for "+id);
                }
            }
            return pFactory.newQualifiedName(tmp, id, null);
        }
        final String prefix = id.substring(0, index);

        String tmp=prefixes.get(prefix);
        if (tmp==null) {
            if (parent!=null) {
                return parent.stringToQualifiedName(id, pFactory,isEscaped);
            } else {
                throw new QualifiedNameException("Namespace.stringToQualifiedName(): Null namespace for " + id + " namespace " + this);
            }
        }
        final String local = id.substring(index + 1);
        if (isEscaped) {
            return pFactory.newQualifiedName(tmp, local, prefix);
        } else {
            return pFactory.newQualifiedName(tmp, qnU.escapeProvLocalName(local)
                    //local.replace("=","\\=")
                    , prefix);
        }



    }

    /**
     * Creates a qualified name, with given prefix and local name. The prefix needs to have been pre-registered. 
     * Prefix is allowed to be null: in that case, the intended namespace is the default namespace 
     * (see {@link Namespace#getDefaultNamespace()}) which must have been pre-registered.
     * @param prefix the prefix for the {@link QualifiedName}
     * @param local the local name for the {@link QualifiedName}
     * @param pFactory the factory method used to construct the {@link QualifiedName}
     * @return a {@link QualifiedName}
     * @throws QualifiedNameException if prefix is not pre-registered.
     * @throws NullPointerException if prefix is null and defaultnamespace has not been registered.
     */

    public QualifiedName qualifiedName(String prefix, String local, ProvFactory pFactory) {

        if (prefix == null) {
            String tmp = getDefaultNamespace();
            if (tmp == null && parent != null) tmp = parent.getDefaultNamespace();
            if (tmp==null) throw new NullPointerException("Namespace.stringToQualifiedName(: Null namespace for "+ local);
            return pFactory.newQualifiedName(tmp, local, null);
        }

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
                    return parent.qualifiedName(prefix, local, pFactory);
                } else {
                    throw new QualifiedNameException("Namespace.stringToQualifiedName(): Null namespace for " + prefix + ":"  + local + " namespace " + this);
                }
            }
            return pFactory.newQualifiedName(tmp, local, prefix);
        }
    }



    static public String qualifiedNameToStringWithNamespace(QualifiedName name) {
        Namespace ns=Namespace.getThreadNamespace();
        return ns.qualifiedNameToString(name);
    }


    public String qualifiedNameToString(QName name) {
        return qualifiedNameToString(name,null);
    }



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
    /**
     * @param name the QName to convert to string
     * @param child argument used just for the purpose of debugging when throwing an exception
     * @return a string representation of the QualifiedName
     */

    public String qualifiedNameToString(QName name, Namespace child) {
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
        return "[Namespace (" + defaultNamespace + ") " + prefixes + ", parent: " + parent + "]";
    }





}
