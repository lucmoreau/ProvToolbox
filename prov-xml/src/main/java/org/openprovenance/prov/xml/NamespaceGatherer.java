package org.openprovenance.prov.xml;

import java.util.Hashtable;
import java.util.List;

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
    
    public NamespaceGatherer() {}
    
    public NamespaceGatherer(Hashtable<String, String> prefixes,
                             String defaultNamespace) {
	this.prefixes=prefixes;
	//todo create inverse map!
	this.defaultNamespace=defaultNamespace;
    }
    
    

    /* mapping from prefixes to namespaces. */
    Hashtable<String, String> prefixes = new Hashtable<String, String>();
    /* mapping from namespaces to prefixes. */
    Hashtable<String, String> namespaces = new Hashtable<String, String>();
    
    String defaultNamespace = null;

    public String getDefaultNamespace() {
	return defaultNamespace;
    }

    public Hashtable<String, String> getPrefixes() {
	return prefixes;
    }

    private void gatherLocation(List<Location> locations) {
	for (Location loc : locations) {
	    gather(loc.getType());
	    Object val = loc.getValue();
	    if (val instanceof QName) {
		gather((QName) val);
	    }
	}

    }

    private void gatherType(List<Type> types) {
	for (Type typ : types) {
	    gather(typ.getType());
	    Object val = typ.getValue();
	    if (val instanceof QName) {
		gather((QName) val);
	    }
	}

    }

    private void gatherRole(List<Role> roles) {
	for (Role rol : roles) {
	    gather(rol.getType());
	    Object val = rol.getValue();
	    if (val instanceof QName) {
		gather((QName) val);
	    }
	}

    }

    private void gatherOther(List<OtherAttribute> others) {
	for (OtherAttribute other : others) {
	    gather(other.getType());
	    gather(other.getElementName());
	    Object val = other.getValue();
	    if (val instanceof QName) {
		gather((QName) val);
	    }
	}

    }

    private void gatherValue(Value val2) {
	Object val = val2.getValue();
	if (val instanceof QName) {
	    gather((QName) val);
	}
    }

    final String stringForDefault="::";

    void gather(IDRef name) {
	if (name!=null)
	gather(name.getRef());
    }

    void gather(QName name) {
	if (name==null) return;
	String namespace = name.getNamespaceURI();
	String prefix = name.getPrefix();
	if (prefix == null) {
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
	gather(mem.getCollection());
	for (IDRef i: mem.getEntity()) {
	    gather(i);
	}
    }

    @Override
    public void run(SpecializationOf spec) {
	gather(spec.getGeneralEntity());
	gather(spec.getSpecificEntity());
    }

    @Override
    public void run(MentionOf men) {
	gather(men.getBundle());
	gather(men.getGeneralEntity());
	gather(men.getSpecificEntity());
    }

    @Override
    public void run(AlternateOf alt) {
	gather(alt.getAlternate1());
	gather(alt.getAlternate2());
    }

    @Override
    public void run(WasInfluencedBy inf) {
	gather(inf.getId());
	gather(inf.getInfluencee());
	gather(inf.getInfluencer());
	gatherType(inf.getType());
	gatherOther(inf.getOthers());
    }

    @Override
    public void run(ActedOnBehalfOf del) {
	gather(del.getId());
	gather(del.getDelegate());
	gather(del.getResponsible());
	gather(del.getActivity());
	gatherType(del.getType());
	gatherOther(del.getOthers());
    }

    @Override
    public void run(WasAttributedTo attr) {
	gather(attr.getId());
	gather(attr.getEntity());
	gather(attr.getAgent());
	gatherType(attr.getType());
	gatherOther(attr.getOthers());	
    }

    @Override
    public void run(WasAssociatedWith assoc) {
	gather(assoc.getId());
	gather(assoc.getActivity());
	gather(assoc.getAgent());
	gather(assoc.getPlan());
	gatherRole(assoc.getRole());
	gatherType(assoc.getType());
	gatherOther(assoc.getOthers());
    }

    @Override
    public void run(WasDerivedFrom der) {
	gather(der.getId());
	gather(der.getGeneratedEntity());
	gather(der.getUsedEntity());
	gather(der.getActivity());
	gather(der.getGeneration());
	gather(der.getUsage());
	gatherType(der.getType());
	gatherOther(der.getOthers());
    }

    @Override
    public void run(WasInformedBy inf) {
	gather(inf.getId());
	gather(inf.getInformed());
	gather(inf.getInformant());
	gatherType(inf.getType());
	gatherOther(inf.getOthers());
    }

    @Override
    public void run(WasEndedBy end) {
	gather(end.getId());
	gather(end.getActivity());
	gather(end.getEnder());
	gather(end.getTrigger());
	gatherLocation(end.getLocation());
	gatherType(end.getType());
	gatherRole(end.getRole());
	gatherOther(end.getOthers());
    }

    @Override
    public void run(WasStartedBy start) {
	gather(start.getId());
	gather(start.getActivity());
	gather(start.getStarter());
	gather(start.getTrigger());
	gatherLocation(start.getLocation());
	gatherType(start.getType());
	gatherRole(start.getRole());
	gatherOther(start.getOthers());
    }

    @Override
    public void run(WasInvalidatedBy inv) {
	gather(inv.getId());
	gather(inv.getEntity());
	gather(inv.getActivity());
	gatherRole(inv.getRole());
	gatherType(inv.getType());
	gatherOther(inv.getOthers());
    }

    @Override
    public void run(Used use) {
	gather(use.getId());
	gather(use.getEntity());
	gather(use.getActivity());
	gatherRole(use.getRole());
	gatherType(use.getType());
	gatherOther(use.getOthers());
    }

    @Override
    public void run(WasGeneratedBy gen) {
	gather(gen.getId());
	gather(gen.getEntity());
	gather(gen.getActivity());
	gatherRole(gen.getRole());
	gatherType(gen.getType());
	gatherOther(gen.getOthers());
    }

    @Override
    public void run(Agent ag) {
	gather(ag.getId());
	gatherLocation(ag.getLocation());
	gatherType(ag.getType());
	gatherOther(ag.getOthers());
    }

    @Override
    public void run(Activity a) {
	gather(a.getId());
	gatherLocation(a.getLocation());
	gatherType(a.getType());
	gatherOther(a.getOthers());
    }

    @Override
    public void run(Entity e) {
	gather(e.getId());
	gatherLocation(e.getLocation());
	gatherType(e.getType());
	gatherValue(e.getValue());
	gatherOther(e.getOthers());
    }

}
