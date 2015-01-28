package org.openprovenance.prov.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Set;


/** This class provides a set of indexes over information contained in
 * an Document, facilitating its navigation.  Its constructor takes an
 * Document builds an index for it.  Of course, for the index to be
 * maintained, one cannot access, say the list of edges, and mutate
 * it. Instead, one has to use the add methods provided.
 *<p>
 * Note that code is not thread-safe.

 TODO: index annotation, index edges

 */

public class IndexedDocument implements StatementAction {



    ProvUtilities u=new ProvUtilities();
    final ProvFactory pFactory;


    private HashMap<QualifiedName,Entity>   entityMap=new HashMap<QualifiedName, Entity>();
    private HashMap<QualifiedName,Activity> activityMap=new HashMap<QualifiedName, Activity>();
    private HashMap<QualifiedName,Agent>    agentMap=new HashMap<QualifiedName, Agent>();

    /* Collection of Used edges that have a given process as an
     * effect. */
    private HashMap<QualifiedName,Collection<Used>> activityUsedMap=new HashMap<QualifiedName, Collection<Used>>();

    /* Collection of Used edges that have a given entity as a
     * cause. */
    private HashMap<QualifiedName,Collection<Used>> entityUsedMap=new HashMap<QualifiedName, Collection<Used>>();
    private Collection<Used> anonUsed=new LinkedList<Used>();
    private HashMap<QualifiedName,Collection<Used>> namedUsedMap=new HashMap<QualifiedName, Collection<Used>>();


    /* Collection of WasGeneratedBy edges that have a given activity as a
     * cause. */
    private HashMap<QualifiedName,Collection<WasGeneratedBy>> activityWasGeneratedByMap=new HashMap<QualifiedName, Collection<WasGeneratedBy>>();

    /* Collection of WasGeneratedBy edges that have a given entity as an
     * effect. */
    private HashMap<QualifiedName,Collection<WasGeneratedBy>> entityWasGeneratedByMap=new HashMap<QualifiedName, Collection<WasGeneratedBy>>();
    private Collection<WasGeneratedBy> anonWasGeneratedBy=new LinkedList<WasGeneratedBy>();
    private HashMap<QualifiedName,Collection<WasGeneratedBy>> namedWasGeneratedByMap=new HashMap<QualifiedName, Collection<WasGeneratedBy>>();


    /* Collection of WasDerivedFrom edges that have a given entity as a cause. */
    private HashMap<QualifiedName,Collection<WasDerivedFrom>> entityCauseWasDerivedFromMap=new HashMap<QualifiedName, Collection<WasDerivedFrom>>();

    /* Collection of WasDerivedFrom edges that have a given entity as an
     * effect. */
    private HashMap<QualifiedName,Collection<WasDerivedFrom>> entityEffectWasDerivedFromMap=new HashMap<QualifiedName, Collection<WasDerivedFrom>>();
    private Collection<WasDerivedFrom> anonWasDerivedFrom=new LinkedList<WasDerivedFrom>();
    private HashMap<QualifiedName,Collection<WasDerivedFrom>> namedWasDerivedFromMap=new HashMap<QualifiedName, Collection<WasDerivedFrom>>();


    /* Collection of WasAssociatedWith edges that have a given activity as an
     * effect. */
    private HashMap<QualifiedName,Collection<WasAssociatedWith>> activityWasAssociatedWithMap=new HashMap<QualifiedName, Collection<WasAssociatedWith>>();

    /* Collection of WasAssociatedWith edges that have a given agent as a
     * cause. */
    private HashMap<QualifiedName,Collection<WasAssociatedWith>> agentWasAssociatedWithMap=new HashMap<QualifiedName, Collection<WasAssociatedWith>>();
    private Collection<WasAssociatedWith> anonWasAssociatedWith=new LinkedList<WasAssociatedWith>();
    private HashMap<QualifiedName,Collection<WasAssociatedWith>> namedWasAssociatedWithMap=new HashMap<QualifiedName, Collection<WasAssociatedWith>>();


    /* Collection of WasAttributedTo edges that have a given entiy as an
     * effect. */
    private HashMap<QualifiedName,Collection<WasAttributedTo>> entityWasAttributedToMap=new HashMap<QualifiedName, Collection<WasAttributedTo>>();

    /* Collection of WasAttributedTo edges that have a given agent as a
     * cause. */
    private HashMap<QualifiedName,Collection<WasAttributedTo>> agentWasAttributedToMap=new HashMap<QualifiedName, Collection<WasAttributedTo>>();
    private Collection<WasAttributedTo> anonWasAttributedTo=new LinkedList<WasAttributedTo>();
    private HashMap<QualifiedName,Collection<WasAttributedTo>> namedWasAttributedToMap=new HashMap<QualifiedName, Collection<WasAttributedTo>>();


    /* Collection of WasInformedBy edges that have a given activity as a cause. */
    private HashMap<QualifiedName,Collection<WasInformedBy>> activityCauseWasInformedByMap=new HashMap<QualifiedName, Collection<WasInformedBy>>();

    /* Collection of WasInformedBy edges that have a given activity as an
     * effect. */
    private HashMap<QualifiedName,Collection<WasInformedBy>> activityEffectWasInformedByMap=new HashMap<QualifiedName, Collection<WasInformedBy>>();
    private Collection<WasInformedBy> allWasInformedBy=new LinkedList<WasInformedBy>();
    private Namespace nss;
    private boolean flatten;


    /** Return all used edges for this graph. */
    public Collection<Used> getUsed() {
        return anonUsed;
    }
    /** Return all used edges with activity p as an effect. */
    public Collection<Used> getUsed(Activity p) {
        return activityUsedMap.get(p.getId());
    }

    /** Return all used edges with entity a as a cause. */
    public Collection<Used> getUsed(Entity p) {
        return entityUsedMap.get(p.getId());
    }

    /** Return all WasGeneratedBy edges for this graph. */
    public Collection<WasGeneratedBy> getWasGeneratedBy() {
        return anonWasGeneratedBy;
    }
    /** Return all WasGeneratedBy edges with activity p as an effect. */
    public Collection<WasGeneratedBy> getWasGeneratedBy(Activity p) {
        return activityWasGeneratedByMap.get(p.getId());
    }

    /** Return all WasGeneratedBy edges with entity a as a cause. */
    public Collection<WasGeneratedBy> getWasGeneratedBy(Entity p) {
        return entityWasGeneratedByMap.get(p.getId());
    }

    /** Return all WasDerivedFrom edges for this graph. */
    public Collection<WasDerivedFrom> getWasDerivedFrom() {
        return anonWasDerivedFrom;
    }
    /** Return all WasDerivedFrom edges with entity a as a cause. */
    public Collection<WasDerivedFrom> getWasDerivedFromWithCause(Entity a) {
        return entityCauseWasDerivedFromMap.get(a.getId());
    }

    /** Return all WasDerivedFrom edges with entity a as an effect . */
    public Collection<WasDerivedFrom> getWasDerivedFromWithEffect(Entity a) {
        return entityEffectWasDerivedFromMap.get(a.getId());
    }


    /** Return all WasInformedBy edges for this graph. */
    public Collection<WasInformedBy> getWasInformedBy() {
        return allWasInformedBy;
    }
    /** Return all WasInformedBy edges with activity p as a cause. */
    public Collection<WasInformedBy> getWasInformedByWithCause(Activity a) {
        return activityCauseWasInformedByMap.get(a.getId());
    }

    /** Return all WasInformedBy edges with activity a as an effect. */
    public Collection<WasInformedBy> getWasInformedByWithEffect(Activity a) {
        return activityEffectWasInformedByMap.get(a.getId());
    }

    /** Return all WasAssociatedWith edges for this graph. */
    public Collection<WasAssociatedWith> getWasAssociatedWith() {
        return anonWasAssociatedWith;
    }
    /** Return all WasAssociatedWith edges with activity p as an effect. */
    public Collection<WasAssociatedWith> getWasAssociatedWith(Activity p) {
        return activityWasAssociatedWithMap.get(p.getId());
    }

    /** Return all WasAssociatedWith edges with entity a as a cause. */
    public Collection<WasAssociatedWith> getWasAssociatedWith(Agent a) {
        return agentWasAssociatedWithMap.get(a.getId());
    
}




    public Entity add(Entity entity) {
        return add(entity.getId(),entity);
    }
    public Entity add(QualifiedName name, Entity entity) {
        Entity existing=entityMap.get(name);
        if (existing!=null) {
            mergeAttributes(existing,entity);
            return existing;
        } else {
            entityMap.put(name,entity);
            return entity;
        }
    }



    void mergeAttributes(Element existing, Element newElement) {
	Set<LangString> set=new HashSet<LangString>(newElement.getLabel());
	set.removeAll(existing.getLabel());
	existing.getLabel().addAll(set);
	
	Set<Location> set2=new HashSet<Location>(newElement.getLocation());
	set2.removeAll(existing.getLocation());
	existing.getLocation().addAll(set2);
	
	Set<Type> set3=new HashSet<Type>(newElement.getType());
	set3.removeAll(existing.getType());
	existing.getType().addAll(set3);
	
	Set<Other> set4=new HashSet<Other>(newElement.getOther());
	set4.removeAll(existing.getOther());
	existing.getOther().addAll(set4);	
    }
    
    void mergeAttributes(Influence existing, Influence newElement) {
	Set<LangString> set=new HashSet<LangString>(newElement.getLabel());
	set.removeAll(existing.getLabel());
	existing.getLabel().addAll(set);
	
	if (existing instanceof HasLocation) {
	    HasLocation existing2=(HasLocation) existing;
	    Set<Location> set2=new HashSet<Location>(((HasLocation)newElement).getLocation());
	    set2.removeAll(existing2.getLocation());
	    existing2.getLocation().addAll(set2);
	}
	
	Set<Type> set3=new HashSet<Type>(newElement.getType());
	set3.removeAll(existing.getType());
	existing.getType().addAll(set3);
	
	Set<Other> set4=new HashSet<Other>(newElement.getOther());
	set4.removeAll(existing.getOther());
	existing.getOther().addAll(set4);	
    }
    
    boolean sameEdge(Statement existing, Statement newElement, int count) {
	boolean ok=true;
	for (int i=1; i<=count; i++) {
	    QualifiedName qn1 = (QualifiedName)u.getter(existing,i);
	    QualifiedName qn2 = (QualifiedName)u.getter(newElement,i);
	    if (qn1==null) {
		if (qn2==null) {

		} else {
		    ok=false;
		    break;
		}
	    } else {
		if (qn2==null) {
		    ok=false;
		    break;
		} else {
		    if (!qn1.equals(qn2)) {
			ok=false;
			break;
		    }
		}
	    }
	}
	return ok;
    }
   
    public Agent add(Agent agent) {
        return add(agent.getId(),agent);
    }
    public Agent add(QualifiedName name, Agent agent) {
        Agent existing=agentMap.get(name);
        if (existing!=null) {
            mergeAttributes(existing,agent);
            return existing;
        } else {
            agentMap.put(name,agent);
            return agent;
        }
    }

    public Activity add(Activity activity) {
        return add(activity.getId(),activity);
    }

    public Activity add(QualifiedName name, Activity activity) {
        Activity existing=activityMap.get(name);
        if (existing!=null) {
            mergeAttributes(existing,activity);
            return existing;
        } else {
            activityMap.put(name,activity);
            return activity;
        }
    }

    public Activity getActivity(String name) {
        return activityMap.get(name);
    }
    public Entity getEntity(String name) {
        return entityMap.get(name);
    }
    public Agent getAgent(String name) {
        return agentMap.get(name);
    }
    public IndexedDocument(ProvFactory pFactory, Document doc) {
	this(pFactory,doc,true);
    }
            
    public IndexedDocument(ProvFactory pFactory, Document doc, boolean flatten) {
        this.pFactory=pFactory;
        this.nss=doc.getNamespace();
        this.flatten=flatten;
        
        u.forAllStatementOrBundle(doc.getStatementOrBundle(), this);

        /*

        if (graph.getDependencies()!=null) {
            if (getDependencies()==null) {
                setDependencies(of.createDependencies());
            }
            List<Edge> edges=u.getEdges(graph);

            for (Edge edge: edges) {
                if (edge instanceof Used) {
                    addUsed(pFactory.newUsed((Used) edge));  
                }
                if (edge instanceof WasGeneratedBy) {
                    addWasGeneratedBy(pFactory.newWasGeneratedBy((WasGeneratedBy) edge));  
                }
                if (edge instanceof WasDerivedFrom) {
                    addWasDerivedFrom(pFactory.newWasDerivedFrom((WasDerivedFrom) edge));  
                }
                if (edge instanceof WasAssociatedWith) {
                    addWasAssociatedWith(pFactory.newWasAssociatedWith((WasAssociatedWith) edge));  
                }
                if (edge instanceof WasInformedBy) {
                    addWasInformedBy(pFactory.newWasInformedBy((WasInformedBy) edge));  
                }
            }
        }
        */
    }



    /** Add a used edge to the graph. Update activityUsedMap and
        entityUsedMap accordingly.  Used edges with different attributes are considered distinct.
        */

    public Used add(Used used) {
	QualifiedName aid = used.getActivity();
	QualifiedName eid = used.getEntity();

	used = pFactory.newUsed(used); // clone
	
	QualifiedName id=used.getId();

	if (id == null) {

	    boolean found = false;
	    Collection<Used> ucoll = activityUsedMap.get(aid);
	    if (ucoll == null) {
		ucoll = new LinkedList<Used>();
		ucoll.add(used);
		activityUsedMap.put(aid, ucoll);
	    } else {
		for (Used u : ucoll) {
		    if (u.equals(used)) {
			found = true;
			used = u;
			break;
		    }
		}
		if (!found) {
		    ucoll.add(used);
		}
	    }

	    ucoll = entityUsedMap.get(eid);
	    if (ucoll == null) {
		ucoll = new LinkedList<Used>();
		ucoll.add(used);
		entityUsedMap.put(eid, ucoll);
	    } else {
		if (!found) {
		    // if we had not found it in the first table, then we
		    // have to add it here too
		    ucoll.add(used);
		}
	    }

	    if (!found) {
		anonUsed.add(used);
	    }
	} else {
	    Collection<Used> colu=namedUsedMap.get(id);
	    if (colu==null) {
		colu=new LinkedList<Used>();
		colu.add(used);
		namedUsedMap.put(id, colu);
	    } else {
		boolean found=false;
		for (Used u1: colu) {
		    if (sameEdge(u1,used,2)) {
			found=true;
			mergeAttributes(u1, used);
			break;			
		    }
		}
		if (!found) {
		    colu.add(used);
		}
	    }
	}
	return used;
    }

    public WasGeneratedBy add(WasGeneratedBy wgb) {
	QualifiedName aid = wgb.getActivity();
	QualifiedName eid = wgb.getEntity();

	wgb = pFactory.newWasGeneratedBy(wgb);

	QualifiedName id = wgb.getId();

	if (id == null) {

	    boolean found = false;
	    Collection<WasGeneratedBy> gcoll = activityWasGeneratedByMap.get(aid);
	    if (gcoll == null) {
		gcoll = new LinkedList<WasGeneratedBy>();
		gcoll.add(wgb);
		activityWasGeneratedByMap.put(aid, gcoll);
	    } else {

		for (WasGeneratedBy u : gcoll) {

		    if (u.equals(wgb)) {
			found = true;
			wgb = u;
			break;
		    }
		}
		if (!found) {
		    gcoll.add(wgb);
		}
	    }

	    gcoll = entityWasGeneratedByMap.get(eid);
	    if (gcoll == null) {
		gcoll = new LinkedList<WasGeneratedBy>();
		gcoll.add(wgb);
		entityWasGeneratedByMap.put(eid, gcoll);
	    } else {
		if (!found) {
		    // if we had not found it in the first table, then we
		    // have to add it here too
		    gcoll.add(wgb);
		}
	    }

	    if (!found) {
		anonWasGeneratedBy.add(wgb);
	    }
	} else {
	    Collection<WasGeneratedBy> colg=namedWasGeneratedByMap.get(id);
	    if (colg==null) {
		colg=new LinkedList<WasGeneratedBy>();
		colg.add(wgb);
		namedWasGeneratedByMap.put(id, colg);
	    } else {
		boolean found=false;
		for (WasGeneratedBy g1: colg) {
		    if (sameEdge(g1,wgb,2)) {
			found=true;
			mergeAttributes(g1, wgb);
			break;			
		    }
		}
		if (!found) {
		    colg.add(wgb);
		}
	    }
	}
	return wgb;
    }

  /** Add a wasDerivedFrom edge to the graph. Update activityWasDerivedFromMap and
      entityWasDerivedFromMap accordingly.  By doing so, aggregate all wasDerivedFrom
      edges (a1,r,a2) with different accounts in a single edge.
      Return the wasDerivedFrom edge itself (if it had not been encountered
      before), or the instance encountered before.*/

  public WasDerivedFrom add(WasDerivedFrom wdf) {
      QualifiedName e2=wdf.getGeneratedEntity();
      QualifiedName e1=wdf.getUsedEntity();
      
      wdf=pFactory.newWasDerivedFrom(wdf);
      
      QualifiedName id=wdf.getId();
      
      if (id==null) {
	  boolean found=false;
	  Collection<WasDerivedFrom> dcoll=entityCauseWasDerivedFromMap.get(e1);
	  if (dcoll==null) {
	      dcoll=new LinkedList<WasDerivedFrom>();
	      dcoll.add(wdf);
	      entityCauseWasDerivedFromMap.put(e1,dcoll);
	  } else {	      
	      for (WasDerivedFrom d: dcoll) {
		  if (d.equals(wdf)) {
		      found=true;
		      wdf=d;
		      break;
		  }
	      }
	      if (!found) {
		  dcoll.add(wdf);
	      }
	  }

	  dcoll=entityEffectWasDerivedFromMap.get(e2);
	  if (dcoll==null) {
	      dcoll=new LinkedList<WasDerivedFrom>();
	      dcoll.add(wdf);
	      entityEffectWasDerivedFromMap.put(e2,dcoll);
	  } else {
	      if (!found) {
		  // if we had not found it in the first table, then we
		  // have to add it here too
		  dcoll.add(wdf);
	      }
	  }
	  
	  if (!found) {
	      anonWasDerivedFrom.add(wdf);
	  }
      } else {
	  Collection<WasDerivedFrom> colg=namedWasDerivedFromMap.get(id);
	    if (colg==null) {
		colg=new LinkedList<WasDerivedFrom>();
		colg.add(wdf);
		namedWasDerivedFromMap.put(id, colg);
	    } else {
		boolean found=false;
		for (WasDerivedFrom d1: colg) {
		    if (sameEdge(d1,wdf,5)) {
			found=true;
			mergeAttributes(d1, wdf);
			break;			
		    }
		}
		if (!found) {
		    colg.add(wdf);
		}
	    }
      }
      return wdf;
  }

    /** Add a waw edge to the graph. Update activityWasAssociatedWithMap and
        agentWasAssociatedWithMap accordingly.  WasAssociatedWith edges with different attributes are considered distinct.
        */

    public WasAssociatedWith add(WasAssociatedWith waw) {
	QualifiedName aid = waw.getActivity();
	QualifiedName agid = waw.getAgent();

	waw = pFactory.newWasAssociatedWith(waw); // clone
	
	QualifiedName id=waw.getId();

	if (id == null) {

	    boolean found = false;
	    Collection<WasAssociatedWith> assoccoll = activityWasAssociatedWithMap.get(aid);
	    if (assoccoll == null) {
		assoccoll = new LinkedList<WasAssociatedWith>();
		assoccoll.add(waw);
		activityWasAssociatedWithMap.put(aid, assoccoll);
	    } else {
		for (WasAssociatedWith u : assoccoll) {
		    if (u.equals(waw)) {
			found = true;
			waw = u;
			break;
		    }
		}
		if (!found) {
		    assoccoll.add(waw);
		}
	    }

	    assoccoll = agentWasAssociatedWithMap.get(agid);
	    if (assoccoll == null) {
		assoccoll = new LinkedList<WasAssociatedWith>();
		assoccoll.add(waw);
		agentWasAssociatedWithMap.put(agid, assoccoll);
	    } else {
		if (!found) {
		    // if we had not found it in the first table, then we
		    // have to add it here too
		    assoccoll.add(waw);
		}
	    }

	    if (!found) {
		anonWasAssociatedWith.add(waw);
	    }
	} else {
	    Collection<WasAssociatedWith> assoccoll=namedWasAssociatedWithMap.get(id);
	    if (assoccoll==null) {
		assoccoll=new LinkedList<WasAssociatedWith>();
		assoccoll.add(waw);
		namedWasAssociatedWithMap.put(id, assoccoll);
	    } else {
		boolean found=false;
		for (WasAssociatedWith u1: assoccoll) {
		    if (sameEdge(u1,waw,3)) { // include plan
			found=true;
			mergeAttributes(u1, waw);
			break;			
		    }
		}
		if (!found) {
		    assoccoll.add(waw);
		}
	    }
	}
	return waw;
    }



    /** Add a waw edge to the graph. Update activityWasAttributedToMap and
        agentWasAttributedToMap accordingly.  WasAttributedTo edges with different attributes are considered distinct.
        */

    public WasAttributedTo add(WasAttributedTo wat) {
	QualifiedName aid = wat.getEntity();
	QualifiedName agid = wat.getAgent();

	wat = pFactory.newWasAttributedTo(wat); // clone
	
	QualifiedName id=wat.getId();

	if (id == null) {

	    boolean found = false;
	    Collection<WasAttributedTo> attrcoll = entityWasAttributedToMap.get(aid);
	    if (attrcoll == null) {
		attrcoll = new LinkedList<WasAttributedTo>();
		attrcoll.add(wat);
		entityWasAttributedToMap.put(aid, attrcoll);
	    } else {
		for (WasAttributedTo u : attrcoll) {
		    if (u.equals(wat)) {
			found = true;
			wat = u;
			break;
		    }
		}
		if (!found) {
		    attrcoll.add(wat);
		}
	    }

	    attrcoll = agentWasAttributedToMap.get(agid);
	    if (attrcoll == null) {
		attrcoll = new LinkedList<WasAttributedTo>();
		attrcoll.add(wat);
		agentWasAttributedToMap.put(agid, attrcoll);
	    } else {
		if (!found) {
		    // if we had not found it in the first table, then we
		    // have to add it here too
		    attrcoll.add(wat);
		}
	    }

	    if (!found) {
		anonWasAttributedTo.add(wat);
	    }
	} else {
	    Collection<WasAttributedTo> attrcoll=namedWasAttributedToMap.get(id);
	    if (attrcoll==null) {
		attrcoll=new LinkedList<WasAttributedTo>();
		attrcoll.add(wat);
		namedWasAttributedToMap.put(id, attrcoll);
	    } else {
		boolean found=false;
		for (WasAttributedTo u1: attrcoll) {
		    if (sameEdge(u1,wat,2)) {
			found=true;
			mergeAttributes(u1, wat);
			break;			
		    }
		}
		if (!found) {
		    attrcoll.add(wat);
		}
	    }
	}
	return wat;
    }




    boolean strict=false;

    @Override
    public void doAction(Activity s) {
	add(s);
	
    }
    @Override
    public void doAction(Used s) {
	add(s);
    }
    
    @Override
    public void doAction(WasStartedBy s) {
	throw new UnsupportedOperationException();	
    }
    @Override
    public void doAction(Agent s) {
	add(s);	
    }
    @Override
    public void doAction(AlternateOf s) {
	if (strict) throw new UnsupportedOperationException();		
    }
    @Override
    public void doAction(WasAssociatedWith s) {
	add(s);
    }
    @Override
    public void doAction(WasAttributedTo s) {
	add(s);
    }
    @Override
    public void doAction(WasInfluencedBy s) {
	throw new UnsupportedOperationException();		
    }
    @Override
    public void doAction(ActedOnBehalfOf s) {
	if (strict) throw new UnsupportedOperationException();		
    }
    @Override
    public void doAction(WasDerivedFrom s) {
	add(s);
    }
    @Override
    public void doAction(DictionaryMembership s) {
	throw new UnsupportedOperationException();	
    }
    @Override
    public void doAction(DerivedByRemovalFrom s) {
	throw new UnsupportedOperationException();	
    }
    @Override
    public void doAction(WasEndedBy s) {
	if (strict) throw new UnsupportedOperationException();	
    }
    @Override
    public void doAction(Entity s) {
	add(s);	
    }
    @Override
    public void doAction(WasGeneratedBy s) {
	add(s);
    }
    @Override
    public void doAction(WasInvalidatedBy s) {
	if (strict) throw new UnsupportedOperationException();	
    }
    @Override
    public void doAction(HadMember s) {
	if (strict) throw new UnsupportedOperationException();		
    }
    @Override
    public void doAction(MentionOf s) {
	throw new UnsupportedOperationException();			
    }
    @Override
    public void doAction(SpecializationOf s) {
	if (strict) throw new UnsupportedOperationException();			
	
    }
    @Override
    public void doAction(DerivedByInsertionFrom s) {
	throw new UnsupportedOperationException();
    }
    @Override
    public void doAction(WasInformedBy s) {
	if (strict) throw new UnsupportedOperationException();	
    }
    @Override
    public void doAction(Bundle bun, ProvUtilities provUtilities) {
	if (flatten) {
	    provUtilities.forAllStatement(bun.getStatement(), this);
	} else {
	    throw new UnsupportedOperationException("can't handle bundles without flattening");
	}
    }
    
    public Document toDocument() {
	Document res=pFactory.newDocument();
	res.getStatementOrBundle().addAll(entityMap.values());
	res.getStatementOrBundle().addAll(activityMap.values());
	res.getStatementOrBundle().addAll(agentMap.values());
	res.getStatementOrBundle().addAll(anonUsed);
	for (Collection<Used> c: namedUsedMap.values()) {
	    res.getStatementOrBundle().addAll(c);
	}
	res.getStatementOrBundle().addAll(anonWasGeneratedBy);
	for (Collection<WasGeneratedBy> c: namedWasGeneratedByMap.values()) {
	    res.getStatementOrBundle().addAll(c);
	}
	res.getStatementOrBundle().addAll(anonWasDerivedFrom);
	for (Collection<WasDerivedFrom> c: namedWasDerivedFromMap.values()) {
	    res.getStatementOrBundle().addAll(c);
	}
	res.getStatementOrBundle().addAll(anonWasAssociatedWith);
	for (Collection<WasAssociatedWith> c: namedWasAssociatedWithMap.values()) {
	    res.getStatementOrBundle().addAll(c);
	}
	res.getStatementOrBundle().addAll(anonWasAttributedTo);
	for (Collection<WasAttributedTo> c: namedWasAttributedToMap.values()) {
	    res.getStatementOrBundle().addAll(c);
	}

	res.setNamespace(Namespace.gatherNamespaces(res));
	return res;
    }

}


   //  /** Add a wasGeneratedBy edge to the graph. Update activityWasGeneratedByMap and
   //      entityWasGeneratedByMap accordingly.  By doing so, aggregate all wasGeneratedBy
   //      edges (a,r,p) with different accounts in a single edge.
   //      Return the wasGeneratedBy edge itself (if it had not been encountered
   //      before), or the instance encountered before.*/

   //  public WasGeneratedBy addWasGeneratedBy(WasGeneratedBy wasGeneratedBy) {
   //      ActivityRef pid=wasGeneratedBy.getCause();
   //      Activity p=(Activity)(pid.getRef());
   //      EntityRef aid=wasGeneratedBy.getEffect();
   //      Entity a=(Entity)(aid.getRef());
   //      Role r=wasGeneratedBy.getRole();
   //      Collection<AccountRef> accs=wasGeneratedBy.getAccount();

   //      WasGeneratedBy result=wasGeneratedBy;

   //      boolean found=false;
   //      Collection<WasGeneratedBy> gcoll=activityWasGeneratedByMap.get(p.getId());
   //      if (gcoll==null) {
   //          gcoll=new LinkedList();
   //          gcoll.add(wasGeneratedBy);
   //          activityWasGeneratedByMap.put(p.getId(),gcoll);
   //      } else {

   //          for (WasGeneratedBy u: gcoll) {
                
   //              if (aid.equals(u.getEffect())
   //                  &&
   //                  r.equals(u.getRole())) {
   //                  addNewAccounts(u.getAccount(),accs);
   //                  result=u;
   //                  found=true;
   //              }
   //          }
   //          if (!found) {
   //              gcoll.add(wasGeneratedBy);
   //          }
   //      }

   //      gcoll=entityWasGeneratedByMap.get(a.getId());
   //      if (gcoll==null) {
   //          gcoll=new LinkedList();
   //          gcoll.add(wasGeneratedBy);
   //          entityWasGeneratedByMap.put(a.getId(),gcoll);
   //      } else {
   //          if (!found) {
   //              // if we had not found it in the first table, then we
   //              // have to add it here too
   //              gcoll.add(wasGeneratedBy);
   //          }
   //      }

   //      if (!found) {
   //          allWasGeneratedBy.add(wasGeneratedBy);
   //          getDependencies().getUsedOrWasGeneratedByOrWasInformedBy().add(wasGeneratedBy);
   //      }
   //      return result;
   // }




   //  /** Add a wasControlledBy edge to the graph. Update activityWasAssociatedWithMap and
   //      agentWasAssociatedWithMap accordingly.  By doing so, aggregate all wasControlledBy
   //      edges (p,r,a) with different accounts in a single edge.
   //      Return the wasControlledBy edge itself (if it had not been encountered
   //      before), or the instance encountered before.*/

   //  public WasAssociatedWith addWasAssociatedWith(WasAssociatedWith wasControlledBy) {
   //      ActivityRef pid=wasControlledBy.getEffect();
   //      Activity p=(Activity)(pid.getRef());
   //      AgentRef aid=wasControlledBy.getCause();
   //      Agent a=(Agent)(aid.getRef());
   //      Role r=wasControlledBy.getRole();
   //      Collection<AccountRef> accs=wasControlledBy.getAccount();

   //      WasAssociatedWith result=wasControlledBy;

   //      boolean found=false;
   //      Collection<WasAssociatedWith> ccoll=activityWasAssociatedWithMap.get(p.getId());
   //      if (ccoll==null) {
   //          ccoll=new LinkedList();
   //          ccoll.add(wasControlledBy);
   //          activityWasAssociatedWithMap.put(p.getId(),ccoll);
   //      } else {

   //          for (WasAssociatedWith u: ccoll) {
                
   //              if (aid.equals(u.getCause())
   //                  &&
   //                  r.equals(u.getRole())) {
   //                  addNewAccounts(u.getAccount(),accs);
   //                  result=u;
   //                  found=true;
   //              }
   //          }
   //          if (!found) {
   //              ccoll.add(wasControlledBy);
   //          }
   //      }

   //      ccoll=agentWasAssociatedWithMap.get(a.getId());
   //      if (ccoll==null) {
   //          ccoll=new LinkedList();
   //          ccoll.add(wasControlledBy);
   //          agentWasAssociatedWithMap.put(p.getId(),ccoll);
   //      } else {
   //          if (!found) {
   //              // if we had not found it in the first table, then we
   //              // have to add it here too
   //              ccoll.add(wasControlledBy);
   //          }
   //      }

   //      if (!found) {
   //          allWasAssociatedWith.add(wasControlledBy);
   //          getDependencies().getUsedOrWasGeneratedByOrWasInformedBy().add(wasControlledBy);
   //      }
   //      return result;
   // }

   //  /** Add a wasTriggeredBy edge to the graph. Update activityWasInformedByMap and
   //      entityWasInformedByMap accordingly.  By doing so, aggregate all wasTriggeredBy
   //      edges (p1,r,p2) with different accounts in a single edge.
   //      Return the wasTriggeredBy edge itself (if it had not been encountered
   //      before), or the instance encountered before.*/

   //  public WasInformedBy addWasInformedBy(WasInformedBy wasTriggeredBy) {
   //      ActivityRef pid2=wasTriggeredBy.getEffect();
   //      Activity p2=(Activity)(pid2.getRef());
   //      ActivityRef pid1=wasTriggeredBy.getCause();
   //      Activity p1=(Activity)(pid1.getRef());
   //      Collection<AccountRef> accs=wasTriggeredBy.getAccount();

   //      WasInformedBy result=wasTriggeredBy;

   //      boolean found=false;
   //      Collection<WasInformedBy> dcoll=activityCauseWasInformedByMap.get(p1.getId());
   //      if (dcoll==null) {
   //          dcoll=new LinkedList();
   //          dcoll.add(wasTriggeredBy);
   //          activityCauseWasInformedByMap.put(p1.getId(),dcoll);
   //      } else {

   //          for (WasInformedBy d: dcoll) {
                
   //              if ( (pid1.equals(d.getCause())) && (pid2.equals(d.getEffect()))) {
   //                  addNewAccounts(d.getAccount(),accs);
   //                  result=d;
   //                  found=true;
   //              }
   //          }
   //          if (!found) {
   //              dcoll.add(wasTriggeredBy);
   //          }
   //      }

   //      dcoll=activityEffectWasInformedByMap.get(p2.getId());
   //      if (dcoll==null) {
   //          dcoll=new LinkedList();
   //          dcoll.add(wasTriggeredBy);
   //          activityEffectWasInformedByMap.put(p2.getId(),dcoll);
   //      } else {
   //          if (!found) {
   //              // if we had not found it in the first table, then we
   //              // have to add it here too
   //              dcoll.add(wasTriggeredBy);
   //          }
   //      }

   //      if (!found) {
   //          allWasInformedBy.add(wasTriggeredBy);
   //          getDependencies().getUsedOrWasGeneratedByOrWasInformedBy().add(wasTriggeredBy);
   //      }
   //      return result;
   // }
