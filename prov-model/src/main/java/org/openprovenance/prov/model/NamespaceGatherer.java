package org.openprovenance.prov.model;

import java.util.Hashtable;
import java.util.List;

import javax.xml.XMLConstants;


public class NamespaceGatherer implements StatementAction {
    
    private Namespace ns=new Namespace();
    
    public NamespaceGatherer() {
	ns.addKnownNamespaces();
	ns.setDefaultNamespace(null);
    }
    
    public NamespaceGatherer(Hashtable<String, String> prefixes,
                             String defaultNamespace) {
	ns.getPrefixes().putAll(prefixes);
	//TODO create inverse map!
	ns.setDefaultNamespace(defaultNamespace);
    }
    
    

    public Namespace getNamespace() {
	return ns;
    }

    public void registerLocation(List<Location> locations) {
	for (Location loc : locations) {
	    register(loc);
	}
    }

    public void registerPotentialQualifiedName(Object o) {
	if (o instanceof QualifiedName) {
	    register((QualifiedName) o);
	}
    }
    
    public void register(Location loc) {
	if (loc!=null) {
	    register(loc.getType());
	    Object val = loc.getValue();
	    registerPotentialQualifiedName(val);
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
	registerPotentialQualifiedName(val);
    }

    public void registerRole(List<Role> roles) {
	for (Role rol : roles) {
	    register(rol);
	}

    }

    public void register(Role rol) {
	register(rol.getType());
	Object val = rol.getValue();
	registerPotentialQualifiedName(val);
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
	registerPotentialQualifiedName(val);
    }

    public void registerValue(Value val2) {
	if (val2!=null) {
	    register(val2.getType());
	    Object val = val2.getValue();
	    registerPotentialQualifiedName(val);
	}
    }


    void register(QualifiedName name) {
	if (name==null) return;
	String namespace = name.getNamespaceURI();
	String prefix = name.getPrefix();
	if ((prefix == null) || (prefix.equals(XMLConstants.DEFAULT_NS_PREFIX))) {
	    ns.registerDefault(namespace);
	} else {
	    ns.register(prefix, namespace);
	}
    }

    @Override
    public void doAction(HadMember mem) {
	register(mem.getCollection());
	for (QualifiedName i: mem.getEntity()) {
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
	for (Key k: r.getKey()) {
	    register(k);
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
	    Key key=e.getKey();
	    register(key);
	    //ns.register("xsd", NamespacePrefixMapper.XSD_NS);
	    //make sure xsd is registered!
	}	
    }

     void register(Key key) {
	 registerPotentialQualifiedName(key.getValue());
	 register(key.getType());	
    }

    @Override
    public void doAction(NamedBundle bu, ProvUtilities u) {
	//register(bu.getId());
	//for (Statement s2: bu.getStatement()) {
	//    u.doAction(s2, this);
	//}
    }


}
