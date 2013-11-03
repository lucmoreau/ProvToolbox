package org.openprovenance.prov.xml;

import java.util.Hashtable;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

import org.openprovenance.prov.model.ActedOnBehalfOf;
import org.openprovenance.prov.model.Activity;
import org.openprovenance.prov.model.Agent;
import org.openprovenance.prov.model.AlternateOf;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.HadMember;
import org.openprovenance.prov.model.IDRef;
import org.openprovenance.prov.model.Location;
import org.openprovenance.prov.model.MentionOf;
import org.openprovenance.prov.model.OtherAttribute;
import org.openprovenance.prov.model.RecordAction;
import org.openprovenance.prov.model.Role;
import org.openprovenance.prov.model.SpecializationOf;
import org.openprovenance.prov.model.Type;
import org.openprovenance.prov.model.Used;
import org.openprovenance.prov.model.Value;
import org.openprovenance.prov.model.WasAssociatedWith;
import org.openprovenance.prov.model.WasAttributedTo;
import org.openprovenance.prov.model.WasDerivedFrom;
import org.openprovenance.prov.model.WasEndedBy;
import org.openprovenance.prov.model.WasGeneratedBy;
import org.openprovenance.prov.model.WasInfluencedBy;
import org.openprovenance.prov.model.WasInformedBy;
import org.openprovenance.prov.model.WasInvalidatedBy;
import org.openprovenance.prov.model.WasStartedBy;

public class NamespaceGatherer implements RecordAction {
    

    /* mapping from prefixes to namespaces. */
    Hashtable<String, String> prefixes = new Hashtable<String, String>();
    /* mapping from namespaces to prefixes. */
    Hashtable<String, String> namespaces = new Hashtable<String, String>();
    
    
    public NamespaceGatherer() {
	prefixes.put("prov",NamespacePrefixMapper.PROV_NS);
	namespaces.put(NamespacePrefixMapper.PROV_NS,"prov");
    }
    
    public NamespaceGatherer(Hashtable<String, String> prefixes,
                             String defaultNamespace) {
	this.prefixes=prefixes;
	//todo create inverse map!
	this.defaultNamespace=defaultNamespace;
    }
    
    

    String defaultNamespace = null;

    public String getDefaultNamespace() {
	return defaultNamespace;
    }

    public Hashtable<String, String> getPrefixes() {
	return prefixes;
    }

    public void registerLocation(List<Location> locations) {
	for (Location loc : locations) {
	    register(loc);
	}
    }

    public void register(Location loc) {
	register(loc.getType());
	Object val = loc.getValue();
	if (val instanceof QName) {
	    register((QName) val);
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

    public void registerOther(List<OtherAttribute> others) {
	for (OtherAttribute other : others) {
	    register(other);
	}

    }

    public void register(OtherAttribute other) {
	register(other.getType());
	register(other.getElementName());
	Object val = other.getValue();
	if (val instanceof QName) {
	    register((QName) val);
	}
    }

    public void registerValue(Value val2) {
	Object val = val2.getValue();
	if (val instanceof QName) {
	    register((QName) val);
	}
    }

    final String stringForDefault="::";

    void register(IDRef name) {
	if (name!=null)
	register(name.getRef());
    }

    void register(QName name) {
	if (name==null) return;
	String namespace = name.getNamespaceURI();
	String prefix = name.getPrefix();
	if ((prefix == null) || (prefix.equals(XMLConstants.DEFAULT_NS_PREFIX))) {
	    if (defaultNamespace == null) {
		defaultNamespace = namespace;
		namespaces.put(namespace, stringForDefault);
	    } else {
		newPrefix(namespace);
	    }
	} else {
	    String old = prefixes.get(prefix);
	    if (old == null) {
		prefixes.put(prefix, namespace);
		namespaces.put(namespace,prefix);
	    } else {
		newPrefix(namespace);
	    }
	}
    }

    String xmlns = "_pre";
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
		namespaces.put(namespace, newPrefix);
	}
    }

    @Override
    public void run(HadMember mem) {
	register(mem.getCollection());
	for (IDRef i: mem.getEntity()) {
	    register(i);
	}
    }

    @Override
    public void run(SpecializationOf spec) {
	register(spec.getGeneralEntity());
	register(spec.getSpecificEntity());
    }

    @Override
    public void run(MentionOf men) {
	register(men.getBundle());
	register(men.getGeneralEntity());
	register(men.getSpecificEntity());
    }

    @Override
    public void run(AlternateOf alt) {
	register(alt.getAlternate1());
	register(alt.getAlternate2());
    }

    @Override
    public void run(WasInfluencedBy inf) {
	register(inf.getId());
	register(inf.getInfluencee());
	register(inf.getInfluencer());
	registerType(inf.getType());
	registerOther(inf.getOthers());
    }

    @Override
    public void run(ActedOnBehalfOf del) {
	register(del.getId());
	register(del.getDelegate());
	register(del.getResponsible());
	register(del.getActivity());
	registerType(del.getType());
	registerOther(del.getOthers());
    }

    @Override
    public void run(WasAttributedTo attr) {
	register(attr.getId());
	register(attr.getEntity());
	register(attr.getAgent());
	registerType(attr.getType());
	registerOther(attr.getOthers());	
    }

    @Override
    public void run(WasAssociatedWith assoc) {
	register(assoc.getId());
	register(assoc.getActivity());
	register(assoc.getAgent());
	register(assoc.getPlan());
	registerRole(assoc.getRole());
	registerType(assoc.getType());
	registerOther(assoc.getOthers());
    }

    @Override
    public void run(WasDerivedFrom der) {
	register(der.getId());
	register(der.getGeneratedEntity());
	register(der.getUsedEntity());
	register(der.getActivity());
	register(der.getGeneration());
	register(der.getUsage());
	registerType(der.getType());
	registerOther(der.getOthers());
    }

    @Override
    public void run(WasInformedBy inf) {
	register(inf.getId());
	register(inf.getInformed());
	register(inf.getInformant());
	registerType(inf.getType());
	registerOther(inf.getOthers());
    }

    @Override
    public void run(WasEndedBy end) {
	register(end.getId());
	register(end.getActivity());
	register(end.getEnder());
	register(end.getTrigger());
	registerLocation(end.getLocation());
	registerType(end.getType());
	registerRole(end.getRole());
	registerOther(end.getOthers());
    }

    @Override
    public void run(WasStartedBy start) {
	register(start.getId());
	register(start.getActivity());
	register(start.getStarter());
	register(start.getTrigger());
	registerLocation(start.getLocation());
	registerType(start.getType());
	registerRole(start.getRole());
	registerOther(start.getOthers());
    }

    @Override
    public void run(WasInvalidatedBy inv) {
	register(inv.getId());
	register(inv.getEntity());
	register(inv.getActivity());
	registerRole(inv.getRole());
	registerType(inv.getType());
	registerOther(inv.getOthers());
    }

    @Override
    public void run(Used use) {
	register(use.getId());
	register(use.getEntity());
	register(use.getActivity());
	registerRole(use.getRole());
	registerType(use.getType());
	registerOther(use.getOthers());
    }

    @Override
    public void run(WasGeneratedBy gen) {
	register(gen.getId());
	register(gen.getEntity());
	register(gen.getActivity());
	registerRole(gen.getRole());
	registerType(gen.getType());
	registerOther(gen.getOthers());
    }

    @Override
    public void run(Agent ag) {
	register(ag.getId());
	registerLocation(ag.getLocation());
	registerType(ag.getType());
	registerOther(ag.getOthers());
    }

    @Override
    public void run(Activity a) {
	register(a.getId());
	registerLocation(a.getLocation());
	registerType(a.getType());
	registerOther(a.getOthers());
    }

    @Override
    public void run(Entity e) {
	register(e.getId());
	registerLocation(e.getLocation());
	registerType(e.getType());
	registerValue(e.getValue());
	registerOther(e.getOthers());
    }

}
