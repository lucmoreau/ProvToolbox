package org.openprovenance.prov.model;

import java.util.Hashtable;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

import org.openprovenance.prov.model.NamespacePrefixMapper;
import org.openprovenance.prov.model.ValueConverter;

public class NamespaceGatherer implements StatementAction {
    

    /* mapping from prefixes to namespaces. */
    Hashtable<String, String> prefixes = new Hashtable<String, String>();
    /* mapping from namespaces to prefixes. */
    Hashtable<String, String> namespaces = new Hashtable<String, String>();
    
    
    public NamespaceGatherer() {
	prefixes.put("prov",NamespacePrefixMapper.PROV_NS);
	namespaces.put(NamespacePrefixMapper.PROV_NS,"prov");
	defaultNamespace=null;
    }
    
    public NamespaceGatherer(Hashtable<String, String> prefixes,
                             String defaultNamespace) {
	this.prefixes=prefixes;
	//todo create inverse map!
	this.defaultNamespace=defaultNamespace;
    }
    
    

    String defaultNamespace;

    public String getDefaultNamespace() {
	return defaultNamespace;
    }

    public Hashtable<String, String> getPrefixes() {
	return prefixes;
    }

    public Hashtable<String, String> getNamespaces() {
	return namespaces;
    }
    public void registerLocation(List<Location> locations) {
	for (Location loc : locations) {
	    register(loc);
	}
    }

    public void register(Location loc) {
	if (loc!=null) {
	    register(loc.getType());
	    Object val = loc.getValue();
	    if (val instanceof QName) {
		register((QName) val);
	    }
	}
    }

    public void registerType(List<Type> types) {
	for (Type typ : types) {
	    register(typ);
	}

    }

    public void register(Type typ) {
	register(typ.getType());
	Object val = typ.getValue();
	if (val instanceof QName) {
	    register((QName) val);
	}
    }

    public void registerRole(List<Role> roles) {
	for (Role rol : roles) {
	    register(rol);
	}

    }

    public void register(Role rol) {
	register(rol.getType());
	Object val = rol.getValue();
	if (val instanceof QName) {
	    register((QName) val);
	}
    }

    public void registerOther(List<Other> others) {
	for (Other other : others) {
	    register(other);
	}

    }

    public void register(Other other) {
	register(other.getType());
	register(other.getElementName());
	Object val = other.getValue();
	if (val instanceof QName) {
	    register((QName) val);
	}
    }

    public void registerValue(Value val2) {
	if (val2!=null) {
	    register(val2.getType());
	    Object val = val2.getValue();
	    if (val instanceof QName) {
		register((QName) val);
	    }
	}
    }

    final String stringForDefault="::";

    void register(IDRef name) {
	if (name!=null)
	register(name.getRef());
    }
    
    //TODO: the code below is replicated in Namespace. Avoid duplication.

    void register(QName name) {
	if (name==null) return;
	String namespace = name.getNamespaceURI();
	String prefix = name.getPrefix();
	if ((prefix == null) || (prefix.equals(XMLConstants.DEFAULT_NS_PREFIX))) {
	    if (defaultNamespace == null) {
		defaultNamespace = namespace;
		//namespaces.put(namespace, stringForDefault);
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

    void newPrefix(String namespace) {
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
	    }
	    if (namespaces.get(namespace)==null) {
		// make sure we don't overwrite namespace
		namespaces.put(namespace, newPrefix);
	    }
	}
    }

    @Override
    public void doAction(HadMember mem) {
	register(mem.getCollection());
	for (IDRef i: mem.getEntity()) {
	    register(i);
	}
    }

    @Override
    public void doAction(SpecializationOf spec) {
	register(spec.getGeneralEntity());
	register(spec.getSpecificEntity());
    }

    @Override
    public void doAction(MentionOf men) {
	register(men.getBundle());
	register(men.getGeneralEntity());
	register(men.getSpecificEntity());
    }

    @Override
    public void doAction(AlternateOf alt) {
	register(alt.getAlternate1());
	register(alt.getAlternate2());
    }

    @Override
    public void doAction(WasInfluencedBy inf) {
	register(inf.getId());
	register(inf.getInfluencee());
	register(inf.getInfluencer());
	registerType(inf.getType());
	registerOther(inf.getOther());
    }

    @Override
    public void doAction(ActedOnBehalfOf del) {
	register(del.getId());
	register(del.getDelegate());
	register(del.getResponsible());
	register(del.getActivity());
	registerType(del.getType());
	registerOther(del.getOther());
    }

    @Override
    public void doAction(WasAttributedTo attr) {
	register(attr.getId());
	register(attr.getEntity());
	register(attr.getAgent());
	registerType(attr.getType());
	registerOther(attr.getOther());	
    }

    @Override
    public void doAction(WasAssociatedWith assoc) {
	register(assoc.getId());
	register(assoc.getActivity());
	register(assoc.getAgent());
	register(assoc.getPlan());
	registerRole(assoc.getRole());
	registerType(assoc.getType());
	registerOther(assoc.getOther());
    }

    @Override
    public void doAction(WasDerivedFrom der) {
	register(der.getId());
	register(der.getGeneratedEntity());
	register(der.getUsedEntity());
	register(der.getActivity());
	register(der.getGeneration());
	register(der.getUsage());
	registerType(der.getType());
	registerOther(der.getOther());
    }

    @Override
    public void doAction(WasInformedBy inf) {
	register(inf.getId());
	register(inf.getInformed());
	register(inf.getInformant());
	registerType(inf.getType());
	registerOther(inf.getOther());
    }

    @Override
    public void doAction(WasEndedBy end) {
	register(end.getId());
	register(end.getActivity());
	register(end.getEnder());
	register(end.getTrigger());
	registerLocation(end.getLocation());
	registerType(end.getType());
	registerRole(end.getRole());
	registerOther(end.getOther());
    }

    @Override
    public void doAction(WasStartedBy start) {
	register(start.getId());
	register(start.getActivity());
	register(start.getStarter());
	register(start.getTrigger());
	registerLocation(start.getLocation());
	registerType(start.getType());
	registerRole(start.getRole());
	registerOther(start.getOther());
    }

    @Override
    public void doAction(WasInvalidatedBy inv) {
	register(inv.getId());
	register(inv.getEntity());
	register(inv.getActivity());
	registerRole(inv.getRole());
	registerType(inv.getType());
	registerOther(inv.getOther());
    }

    @Override
    public void doAction(Used use) {
	register(use.getId());
	register(use.getEntity());
	register(use.getActivity());
	registerRole(use.getRole());
	registerType(use.getType());
	registerOther(use.getOther());
    }

    @Override
    public void doAction(WasGeneratedBy gen) {
	register(gen.getId());
	register(gen.getEntity());
	register(gen.getActivity());
	registerRole(gen.getRole());
	registerType(gen.getType());
	registerOther(gen.getOther());
    }

    @Override
    public void doAction(Agent ag) {
	register(ag.getId());
	registerLocation(ag.getLocation());
	registerType(ag.getType());
	registerOther(ag.getOther());
    }

    @Override
    public void doAction(Activity a) {
	register(a.getId());
	registerLocation(a.getLocation());
	registerType(a.getType());
	registerOther(a.getOther());
    }

    @Override
    public void doAction(Entity e) {
	register(e.getId());
	registerLocation(e.getLocation());
	registerType(e.getType());
	registerValue(e.getValue());
	registerOther(e.getOther());
    }

    @Override
    public void doAction(DictionaryMembership m) {
	register(m.getDictionary());
	registerEntry(m.getKeyEntityPair());	
    }

    @Override
    public void doAction(DerivedByRemovalFrom r) {
	register(r.getId());
	register(r.getNewDictionary());
	register(r.getOldDictionary());
	registerType(r.getType());
	registerOther(r.getOther());
	if (!r.getKey().isEmpty()) {
	    register(ValueConverter.QNAME_XSD_INT); // pick up an xsd qname, so that xsd is registered!
	}
	
    }

    @Override
    public void doAction(DerivedByInsertionFrom i) {
	register(i.getId());
	register(i.getNewDictionary());
	register(i.getOldDictionary());
	registerType(i.getType());
	registerOther(i.getOther());
	registerEntry(i.getKeyEntityPair());	
    }

    void registerEntry(List<Entry> keyEntityPairs) {
	for (Entry e: keyEntityPairs) {
	    register(e.getEntity());
	    //Object key=e.getKey();
	    register(ValueConverter.QNAME_XSD_INT); // pick up an xsd qname, so that xsd is registered!
	}	
    }

    @Override
    public void doAction(NamedBundle bu, ProvUtilities u) {
	//register(bu.getId());
	//for (Statement s2: bu.getStatement()) {
	//    u.doAction(s2, this);
	//}
    }


}
