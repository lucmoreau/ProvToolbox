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
    
    void gather(QName name) {
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
	// TODO Auto-generated method stub

    }

    @Override
    public void run(SpecializationOf spec) {
	// TODO Auto-generated method stub

    }

    @Override
    public void run(MentionOf men) {
	// TODO Auto-generated method stub

    }

    @Override
    public void run(AlternateOf alt) {
	// TODO Auto-generated method stub

    }

    @Override
    public void run(WasInfluencedBy inf) {
	// TODO Auto-generated method stub

    }

    @Override
    public void run(ActedOnBehalfOf del) {
	// TODO Auto-generated method stub

    }

    @Override
    public void run(WasAttributedTo attr) {
	// TODO Auto-generated method stub

    }

    @Override
    public void run(WasAssociatedWith assoc) {
	// TODO Auto-generated method stub

    }

    @Override
    public void run(WasDerivedFrom der) {
	// TODO Auto-generated method stub

    }

    @Override
    public void run(WasInformedBy inf) {
	// TODO Auto-generated method stub

    }

    @Override
    public void run(WasEndedBy end) {
	// TODO Auto-generated method stub

    }

    @Override
    public void run(WasStartedBy start) {
	// TODO Auto-generated method stub

    }

    @Override
    public void run(WasInvalidatedBy inv) {
	// TODO Auto-generated method stub

    }

    @Override
    public void run(Used use) {
	// TODO Auto-generated method stub

    }

    @Override
    public void run(WasGeneratedBy gen) {
	// TODO Auto-generated method stub

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
