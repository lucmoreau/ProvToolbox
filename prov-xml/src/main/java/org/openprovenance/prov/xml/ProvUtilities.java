package org.openprovenance.prov.xml;

import java.util.List;
import java.util.LinkedList;
import java.util.Hashtable;
import javax.xml.bind.JAXBElement;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import java.lang.reflect.Method;

/** Utilities for manipulating PROV Descriptions. */

public class ProvUtilities {

    private ProvFactory of = new ProvFactory();

    public List<Element> getElements(Bundle g) {
	List<Element> res = new LinkedList<Element>();
	res.addAll(g.getRecords().getEntity());
	res.addAll(g.getRecords().getActivity());
	res.addAll(g.getRecords().getAgent());
	return res;
    }

    public List<Relation0> getRelations(Bundle g) {
	List<Relation0> res = new LinkedList<Relation0>();
	Dependencies dep = g.getRecords().getDependencies();
	for (Object o : dep.getUsedOrWasGeneratedByOrWasStartedBy()) {
	    res.add((Relation0) o);
	}
	return res;
    }

    public QName getEffect(Relation0 r) {
	if (r instanceof Used) {
	    return ((Used) r).getActivity().getRef();
	}
	if (r instanceof WasStartedBy) {
	    return ((WasStartedBy) r).getActivity().getRef();
	}
	if (r instanceof WasEndedBy) {
	    return ((WasEndedBy) r).getActivity().getRef();
	}
	if (r instanceof WasGeneratedBy) {
	    return ((WasGeneratedBy) r).getEntity().getRef();
	}
	if (r instanceof WasDerivedFrom) {
	    return ((WasDerivedFrom) r).getGeneratedEntity().getRef();
	}
	if (r instanceof WasRevisionOf) {
	    return ((WasRevisionOf) r).getNewer().getRef();
	}
	if (r instanceof WasAssociatedWith) {
	    return ((WasAssociatedWith) r).getActivity().getRef();
	}
	if (r instanceof WasInvalidatedBy) {
	    return ((WasInvalidatedBy) r).getEntity().getRef();
	}

	if (r instanceof WasAttributedTo) {
	    return ((WasAttributedTo) r).getEntity().getRef();
	}
	if (r instanceof AlternateOf) {
	    return ((AlternateOf) r).getEntity2().getRef();
	}
	if (r instanceof SpecializationOf) {
	    return ((SpecializationOf) r).getSpecializedEntity().getRef();
	}
	if (r instanceof WasInformedBy) {
	    return ((WasInformedBy) r).getEffect().getRef();
	}
	if (r instanceof MentionOf) {
	    return ((MentionOf) r).getSpecializedEntity().getRef();
	}
	if (r instanceof WasInfluencedBy) {
	    return ((WasInfluencedBy) r).getInfluencee().getRef();
	}

	if (r instanceof ActedOnBehalfOf) {
	    return ((ActedOnBehalfOf) r).getSubordinate().getRef();
	}

	if (r instanceof DerivedByInsertionFrom) {
	    return ((DerivedByInsertionFrom) r).getAfter().getRef();
	}
	System.out.println("Unknow relation " + r);
	throw new UnsupportedOperationException();
    }

    public QName getCause(Relation0 r) {
	if (r instanceof Used) {
	    return ((Used) r).getEntity().getRef();
	}
	if (r instanceof WasGeneratedBy) {
	    ActivityRef ref = ((WasGeneratedBy) r).getActivity();
	    if (ref == null)
		return null;
	    return ref.getRef();
	}
	if (r instanceof WasInvalidatedBy) {
	    ActivityRef ref = ((WasInvalidatedBy) r).getActivity();
	    if (ref == null)
		return null;
	    return ref.getRef();
	}
	if (r instanceof WasStartedBy) {
	    EntityRef ref = ((WasStartedBy) r).getTrigger();
	    if (ref == null)
		return null;
	    return ref.getRef();
	}
	if (r instanceof WasEndedBy) {
	    EntityRef ref = ((WasEndedBy) r).getTrigger();
	    if (ref == null)
		return null;
	    return ref.getRef();
	}
	if (r instanceof WasDerivedFrom) {
	    return ((WasDerivedFrom) r).getUsedEntity().getRef();
	}

	if (r instanceof WasInfluencedBy) {
	    return ((WasInfluencedBy) r).getInfluencer().getRef();
	}
	if (r instanceof WasRevisionOf) {
	    return ((WasRevisionOf) r).getOlder().getRef();
	}
	if (r instanceof WasAssociatedWith) {
	    return ((WasAssociatedWith) r).getAgent().getRef();
	}
	if (r instanceof WasAttributedTo) {
	    return ((WasAttributedTo) r).getAgent().getRef();
	}
	if (r instanceof AlternateOf) {
	    return ((AlternateOf) r).getEntity1().getRef();
	}
	if (r instanceof SpecializationOf) {
	    return ((SpecializationOf) r).getGeneralEntity().getRef();
	}
	if (r instanceof MentionOf) {
	    return ((MentionOf) r).getGeneralEntity().getRef();
	}
	if (r instanceof WasInformedBy) {
	    return ((WasInformedBy) r).getCause().getRef();
	}
	if (r instanceof ActedOnBehalfOf) {
	    return ((ActedOnBehalfOf) r).getResponsible().getRef();
	}
	if (r instanceof DerivedByInsertionFrom) {
	    return ((DerivedByInsertionFrom) r).getBefore().getRef();
	}
	System.out.println("Unknown relation " + r);
	throw new UnsupportedOperationException();
    }

    public List<QName> getOtherCauses(Relation0 r) {
	if (r instanceof WasAssociatedWith) {
	    List<QName> res = new LinkedList<QName>();
	    EntityRef e = ((WasAssociatedWith) r).getPlan();
	    if (e == null)
		return null;
	    res.add(e.getRef());
	    return res;
	}
	if (r instanceof WasStartedBy) {
	    List<QName> res = new LinkedList<QName>();
	    ActivityRef a = ((WasStartedBy) r).getStarter();
	    if (a == null)
		return null;
	    res.add(a.getRef());
	    return res;
	}
	if (r instanceof MentionOf) {
	    List<QName> res = new LinkedList<QName>();
	    EntityRef a = ((MentionOf) r).getBundle();
	    if (a == null)
		return null;
	    res.add(a.getRef());
	    return res;
	}
	if (r instanceof WasEndedBy) {
	    List<QName> res = new LinkedList<QName>();
	    ActivityRef a = ((WasEndedBy) r).getEnder();
	    if (a == null)
		return null;
	    res.add(a.getRef());
	    return res;
	}
	if (r instanceof ActedOnBehalfOf) {
	    List<QName> res = new LinkedList<QName>();
	    ActivityRef a = ((ActedOnBehalfOf) r).getActivity();
	    if (a == null)
		return null;
	    res.add(a.getRef());
	    return res;
	}
	if (r instanceof DerivedByInsertionFrom) {
	    List<QName> res = new LinkedList<QName>();
	    DerivedByInsertionFrom dbif = ((DerivedByInsertionFrom) r);
	    for (Entry entry : dbif.getEntry()) {
		res.add(entry.getEntity().getRef());
	    }
	    return res;
	}
	return null;
    }

    public MentionOf getMentionForRemoteEntity(Bundle local,
	                                       Entity remoteEntity,
	                                       NamedBundle remote) {
	return getMentionForRemoteEntity(local.getRecords(), remoteEntity,
	                                 remote);
    }

    public MentionOf getMentionForRemoteEntity(NamedBundle local,
	                                       Entity remoteEntity,
	                                       NamedBundle remote) {
	return getMentionForRemoteEntity(local.getRecords(), remoteEntity,
	                                 remote);
    }

    MentionOf getMentionForRemoteEntity(Records local, Entity remoteEntity,
	                                NamedBundle remote) {
	Dependencies dep = local.getDependencies();
	for (Object o : dep.getUsedOrWasGeneratedByOrWasStartedBy()) {
	    if (o instanceof MentionOf) {
		MentionOf ctxt = (MentionOf) o;
		QName id1 = remoteEntity.getId();
		QName id2 = remote.getId();
		if (ctxt.getGeneralEntity().getRef().equals(id1)
		        && ctxt.getBundle().getRef().equals(id2))
		    return ctxt;
	    }
	}
	return null;
    }

    public MentionOf getMentionForLocalEntity(Bundle local, Entity localEntity,
	                                      NamedBundle remote) {
	return getMentionForLocalEntity(local.getRecords(), localEntity, remote);
    }

    public MentionOf getMentionForLocalEntity(NamedBundle local,
	                                      Entity localEntity,
	                                      NamedBundle remote) {
	return getMentionForLocalEntity(local.getRecords(), localEntity, remote);
    }

    MentionOf getMentionForLocalEntity(Records local, Entity localEntity,
	                               NamedBundle remote) {
	Dependencies dep = local.getDependencies();
	for (Object o : dep.getUsedOrWasGeneratedByOrWasStartedBy()) {
	    if (o instanceof MentionOf) {
		MentionOf ctxt = (MentionOf) o;
		QName id1 = localEntity.getId();
		QName id2 = remote.getId();
		if (ctxt.getSpecializedEntity().getRef().equals(id1)
		        && ctxt.getBundle().getRef().equals(id2))
		    return ctxt;
	    }
	}
	return null;
    }

    /** TODO: should \-unescape local part */
    public String toURI(QName qname) {
	return qname.getNamespaceURI() + qname.getLocalPart();
    }

    @SuppressWarnings("rawtypes")
    final static private Hashtable<Class, String[]> fields = new Hashtable<Class, String[]>();
    @SuppressWarnings("rawtypes")
    final static private Hashtable<Class, Class[]> types = new Hashtable<Class, Class[]>();
    @SuppressWarnings("rawtypes")
    static public Hashtable<Class, Class[]> getTypes() {
        return types;
    }

    public Object getter(Object o, int i)
	    throws java.lang.NoSuchMethodException,
	    java.lang.IllegalAccessException,
	    java.lang.reflect.InvocationTargetException {
	String field = fields.get(o.getClass())[i];
	Method method = o.getClass().getDeclaredMethod("get" + field);
	return method.invoke(o);
    }

    static public String[] getTypes(Object o) {
	return fields.get(o.getClass());
    }

    public Object setter(Object o, int i, Object val)
	    throws java.lang.NoSuchMethodException,
	    java.lang.IllegalAccessException,
	    java.lang.reflect.InvocationTargetException {
	String field = fields.get(o.getClass())[i];
	Method method = o.getClass()
	        .getDeclaredMethod("set" + field, types.get(o.getClass())[i]);
	return method.invoke(o, val);
    }

    static {
	fields.put(Used.class, new String[] { "Id", "Activity", "Entity",
	        "Time", "Any" });
	fields.put(WasGeneratedBy.class, new String[] { "Id", "Entity",
	        "Activity", "Time", "Any" });
	fields.put(WasInvalidatedBy.class, new String[] { "Id", "Entity",
	        "Activity", "Time", "Any" });
	fields.put(WasStartedBy.class, new String[] { "Id", "Activity",
	        "Trigger", "Starter", "Time", "Any" });
	// 0 , 1 , 2 , 3 , 4 , 5
	// length=6
	// firstTimeIndex=4
	// last index=5
	fields.put(WasEndedBy.class, new String[] { "Id", "Activity",
	        "Trigger", "Ender", "Time", "Any" });
	fields.put(WasInformedBy.class, new String[] { "Id", "Effect", "Cause",
	        "Any" });
	fields.put(WasDerivedFrom.class, new String[] { "Id",
	        "GeneratedEntity", "UsedEntity", "Activity", "Generation",
	        "Usage", "Any" });
	fields.put(WasInfluencedBy.class, new String[] { "Id", "Influencee",
	        "Influencer", "Any" });
	fields.put(WasAttributedTo.class, new String[] { "Id", "Entity",
	        "Agent", "Any" });
	fields.put(WasAssociatedWith.class, new String[] { "Id", "Activity",
	        "Agent", "Plan", "Any" });
	fields.put(ActedOnBehalfOf.class, new String[] { "Id", "Subordinate",
	        "Responsible", "Activity", "Any" });
	fields.put(SpecializationOf.class, new String[] { "SpecializedEntity",
	        "GeneralEntity" });

	types.put(Used.class, new Class[] { QName.class, ActivityRef.class,
	        EntityRef.class, XMLGregorianCalendar.class, Object.class });
	types.put(WasGeneratedBy.class, new Class[] { QName.class,
	        EntityRef.class, ActivityRef.class, XMLGregorianCalendar.class,
	        Object.class });
	types.put(WasInvalidatedBy.class, new Class[] { QName.class,
	        EntityRef.class, ActivityRef.class, XMLGregorianCalendar.class,
	        Object.class });
	types.put(WasStartedBy.class, new Class[] { QName.class,
	        ActivityRef.class, EntityRef.class, ActivityRef.class,
	        XMLGregorianCalendar.class, Object.class });
	types.put(WasEndedBy.class, new Class[] { QName.class,
	        ActivityRef.class, EntityRef.class, ActivityRef.class,
	        XMLGregorianCalendar.class, Object.class });
	types.put(WasInformedBy.class, new Class[] { QName.class,
	        ActivityRef.class, ActivityRef.class, Object.class });
	types.put(WasDerivedFrom.class, new Class[] { QName.class,
	        EntityRef.class, EntityRef.class, ActivityRef.class,
	        GenerationRef.class, UsageRef.class, Object.class });
	types.put(WasInfluencedBy.class, new Class[] { QName.class,
	        AnyRef.class, AnyRef.class, Object.class });
	types.put(WasAttributedTo.class, new Class[] { QName.class,
	        EntityRef.class, AgentRef.class, Object.class });
	types.put(WasAssociatedWith.class, new Class[] { QName.class,
	        ActivityRef.class, AgentRef.class, EntityRef.class,
	        Object.class });
	types.put(ActedOnBehalfOf.class,
	          new Class[] { QName.class, AgentRef.class, AgentRef.class,
	                  ActivityRef.class, Object.class });
	types.put(SpecializationOf.class, new Class[] { EntityRef.class, Entity.class });
    }

    @SuppressWarnings("unchecked")
    public <T> JAXBElement<T> newElement(T r) {
	if (r instanceof Used) {
	    return (JAXBElement<T>) of.newElement(of.newUsed((Used) r));
	}
	if (r instanceof WasStartedBy) {
	    return (JAXBElement<T>) of.newElement(of.newWasStartedBy((WasStartedBy) r));
	}
	if (r instanceof WasEndedBy) {
	    return (JAXBElement<T>) of.newElement(of.newWasEndedBy((WasEndedBy) r));
	}
	if (r instanceof WasGeneratedBy) {
	    return (JAXBElement<T>) of.newElement(of.newWasGeneratedBy((WasGeneratedBy) r));
	}
	if (r instanceof WasDerivedFrom) {
	    return (JAXBElement<T>) of.newElement(of.newWasDerivedFrom((WasDerivedFrom) r));
	}
	if (r instanceof WasAssociatedWith) {
	    return (JAXBElement<T>) of.newElement(of.newWasAssociatedWith((WasAssociatedWith) r));
	}
	if (r instanceof WasInvalidatedBy) {
	    return (JAXBElement<T>) of.newElement(of.newWasInvalidatedBy((WasInvalidatedBy) r));
	}
	if (r instanceof WasAttributedTo) {
	    return (JAXBElement<T>) of.newElement(of.newWasAttributedTo((WasAttributedTo) r));
	}
	if (r instanceof WasInformedBy) {
	    return (JAXBElement<T>) of.newElement(of.newWasInformedBy((WasInformedBy) r));
	}
	if (r instanceof WasInfluencedBy) {
	    return (JAXBElement<T>) of.newElement(of.newWasInfluencedBy((WasInfluencedBy) r));
	}
	if (r instanceof ActedOnBehalfOf) {
	    return (JAXBElement<T>) of.newElement(of.newActedOnBehalfOf((ActedOnBehalfOf) r));
	}
	System.out.println("newElement Unknow relation " + r);
	throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public <T> T addAttributes(T from, T to) {
	if (from instanceof Used) {
	    return (T) of.addAttributes((Used) from, (Used) to);
	}
	if (from instanceof WasStartedBy) {
	    return (T) of.addAttributes((WasStartedBy) from, (WasStartedBy) to);
	}
	if (from instanceof WasEndedBy) {
	    return (T) of.addAttributes((WasEndedBy) from, (WasEndedBy) to);
	}
	if (from instanceof WasGeneratedBy) {
	    return (T) of.addAttributes((WasGeneratedBy) from,
		                        (WasGeneratedBy) to);
	}
	if (from instanceof WasDerivedFrom) {
	    return (T) of.addAttributes((WasDerivedFrom) from,
		                        (WasDerivedFrom) to);
	}
	if (from instanceof WasAssociatedWith) {
	    return (T) of.addAttributes((WasAssociatedWith) from,
		                        (WasAssociatedWith) to);
	}
	if (from instanceof WasInvalidatedBy) {
	    return (T) of.addAttributes((WasInvalidatedBy) from,
		                        (WasInvalidatedBy) to);
	}

	if (from instanceof WasAttributedTo) {
	    return (T) of.addAttributes((WasAttributedTo) from,
		                        (WasAttributedTo) to);
	}
	/*
	 * if (from instanceof WasRevisionOf) { return (T)
	 * of.addAttributes((WasRevisionOf)from, (WasRevisionOf)to); } if (from
	 * instanceof AlternateOf) { return (T)
	 * of.addAttributes((AlternateOf)from, (AlternateOf)to); } if (from
	 * instanceof SpecializationOf) { return (T)
	 * of.addAttributes((SpecializationOf)from, (SpecializationOf)to); }
	 */
	if (from instanceof WasInformedBy) {
	    return (T) of.addAttributes((WasInformedBy) from,
		                        (WasInformedBy) to);
	}
	if (from instanceof WasInfluencedBy) {
	    return (T) of.addAttributes((WasInfluencedBy) from,
		                        (WasInfluencedBy) to);
	}

	if (from instanceof ActedOnBehalfOf) {
	    return (T) of.addAttributes((ActedOnBehalfOf) from,
		                        (ActedOnBehalfOf) to);
	}

	/*
	 * if (from instanceof DerivedByInsertionFrom) { return T
	 * of.addAttributes((DerivedByInsertionFrom)from,
	 * (DerivedByInsertionFrom)to); }
	 */
	System.out.println("addAttributes Unknow relation " + from);
	throw new UnsupportedOperationException();
    }

    /**
     * Indicates whether object has no time field.
     * 
     * @TODO: introduce an interface to do this.
     */

    public boolean hasNoTime(Object o) {
	return (o instanceof WasDerivedFrom) || (o instanceof ActedOnBehalfOf)
	        || (o instanceof WasInformedBy)
	        || (o instanceof WasAttributedTo)
	        || (o instanceof WasAssociatedWith)
	        || (o instanceof WasInfluencedBy)
	        || (o instanceof SpecializationOf)
	        || (o instanceof AlternateOf);
    }

    public int getFirstTimeIndex(Object o) {
	String[] types = getTypes(o);
	if (o instanceof Activity) {
	    return types.length - 3;
	} else if (hasNoTime(o)) {
	    return types.length - 1;
	} else {
	    return types.length - 2;
	}
    }

    public int getLastIndex(Object o) {
	String[] types = getTypes(o);
	return types.length - 1;
    }

    public void forAllRecords(Records recs, RecordAction action) {
	Dependencies deps = recs.getDependencies();
	List<Entity> entities = recs.getEntity();
	List<Activity> activities = recs.getActivity();
	List<Agent> agents = recs.getAgent();

	for (Entity e : entities) {
	    action.run(e);
	}

	for (Activity a : activities) {
	    action.run(a);
	}

	for (Agent ag : agents) {
	    action.run(ag);
	}

	for (Object o : deps.getUsedOrWasGeneratedByOrWasStartedBy()) {
	    if (o instanceof Used) {
		action.run((Used)o);
	    }

	    else if (o instanceof WasGeneratedBy) {
		WasGeneratedBy tmp = (WasGeneratedBy) o;
		action.run(tmp);
	    }

	    else if (o instanceof WasInvalidatedBy) {
		WasInvalidatedBy tmp = (WasInvalidatedBy) o;
		action.run(tmp);
	    }

	    else if (o instanceof WasStartedBy) {
		WasStartedBy tmp = (WasStartedBy) o;
		action.run(tmp);
	    }

	    else if (o instanceof WasEndedBy) {
		WasEndedBy tmp = (WasEndedBy) o;
		action.run(tmp);
	    }

	    else if (o instanceof WasInformedBy) {
		WasInformedBy tmp = (WasInformedBy) o;
		action.run(tmp);
	    }
	    
	    else if (o instanceof WasDerivedFrom) {
		WasDerivedFrom tmp = (WasDerivedFrom) o;
		action.run(tmp);
	    }


	    else if (o instanceof WasAssociatedWith) {
		WasAssociatedWith tmp = (WasAssociatedWith) o;
		action.run(tmp);
	    }

	    else if (o instanceof WasAttributedTo) {
		WasAttributedTo tmp = (WasAttributedTo) o;
		action.run(tmp);
	    }

	    else if (o instanceof ActedOnBehalfOf) {
		ActedOnBehalfOf tmp = (ActedOnBehalfOf) o;
		action.run(tmp);
	    }

	    else if (o instanceof WasInfluencedBy) {
		WasInfluencedBy tmp = (WasInfluencedBy) o;
		action.run(tmp);
	    }

	    else if (o instanceof AlternateOf) {
		AlternateOf tmp = (AlternateOf) o;
		action.run(tmp);
	    }

	    else if (o instanceof MentionOf) {
		MentionOf tmp = (MentionOf) o;
		action.run(tmp);
	    }
	    
	    else if (o instanceof SpecializationOf) {
		SpecializationOf tmp = (SpecializationOf) o;
		action.run(tmp);
	    }
	    
	}

    } 
}
