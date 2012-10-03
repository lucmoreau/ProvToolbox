package org.openprovenance.prov.xml;

import java.util.List;
import java.util.LinkedList;
import java.util.Hashtable;
import javax.xml.bind.JAXBElement;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.openprovenance.prov.xml.collection.DerivedByInsertionFrom;
import org.openprovenance.prov.xml.collection.Entry;

import java.lang.reflect.Method;

/** Utilities for manipulating PROV Descriptions. */

public class ProvUtilities {

    private ProvFactory p = new ProvFactory();

    /*
     * public List<Element> getElements(Bundle g) { List<Element> res = new
     * LinkedList<Element>(); res.addAll(g.getRecords().getEntity());
     * res.addAll(g.getRecords().getActivity());
     * res.addAll(g.getRecords().getAgent()); return res; }
     */

    public List<Relation0> getRelations(Document d) {
        return getObject(Relation0.class,
                         d.getEntityOrActivityOrWasGeneratedBy());
    }

    public List<Entity> getEntity(Document d) {
        return getObject(Entity.class, d.getEntityOrActivityOrWasGeneratedBy());
    }

    public List<Activity> getActivity(Document d) {
        return getObject(Activity.class,
                         d.getEntityOrActivityOrWasGeneratedBy());
    }

    public List<Agent> getAgent(Document d) {
        return getObject(Agent.class, d.getEntityOrActivityOrWasGeneratedBy());
    }

    public List<Relation0> getRelations(NamedBundle d) {
        return getObject2(Relation0.class,
                         d.getEntityOrActivityOrWasGeneratedBy());
    }

    public List<Entity> getEntity(NamedBundle d) {
        return getObject2(Entity.class, d.getEntityOrActivityOrWasGeneratedBy());
    }

    public List<Activity> getActivity(NamedBundle d) {
        return getObject2(Activity.class,
                         d.getEntityOrActivityOrWasGeneratedBy());
    }

    public List<Agent> getAgent(NamedBundle d) {
        return getObject2(Agent.class, d.getEntityOrActivityOrWasGeneratedBy());
    }

    public List<NamedBundle> getNamedBundle(Document d) {
        return getObject(NamedBundle.class,
                         d.getEntityOrActivityOrWasGeneratedBy());
    }

    public List<Statement> getStatement(Document d) {
	return getObject(Statement.class,
	                 d.getEntityOrActivityOrWasGeneratedBy());
    }

    @SuppressWarnings("unchecked")
    public List<Statement> getStatement(NamedBundle d) {
        List<?> res = d.getEntityOrActivityOrWasGeneratedBy();
        return (List<Statement>) res;
    }

    public <T> List<T> getObject(Class<T> cl, List<StatementOrBundle> ll) {
        List<T> res = new LinkedList<T>();
        for (Object o : ll) {
            if (cl.isInstance(o)) {
                @SuppressWarnings("unchecked")
                T o2 = (T) o;
                res.add(o2);
            }
        }
        return res;
    }
    public <T> List<T> getObject2(Class<T> cl, List<Statement> ll) {
        List<T> res = new LinkedList<T>();
        for (Object o : ll) {
            if (cl.isInstance(o)) {
                @SuppressWarnings("unchecked")
                T o2 = (T) o;
                res.add(o2);
            }
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
        if (r instanceof Membership) {
            return ((Membership) r).getCollection().getRef();
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
        if (r instanceof Membership) {
            return ((Membership) r).getEntity().get(0).getRef();
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
        if (r instanceof Membership) {
            List<QName> res = new LinkedList<QName>();
            List<EntityRef> entities=((Membership) r).getEntity();
            if ((entities==null) || (entities.size()<=1)) return null;
            boolean first=true;
            for (EntityRef ee: entities) {
                if (!first) res.add(ee.getRef());
                first=false;
            }
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

    public MentionOf getMentionForRemoteEntity(NamedBundle local,
                                               Entity remoteEntity,
                                               NamedBundle remote) {
        return getMentionForLocalEntity(local.getEntityOrActivityOrWasGeneratedBy(),
                                         remoteEntity, remote);
    }

    MentionOf getMentionForRemoteEntity(List<Object> objects,
                                        Entity remoteEntity, NamedBundle remote) {
        for (Object o : objects) {
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

    /*
     * public MentionOf getMentionForLocalEntity(Bundle local, Entity
     * localEntity, NamedBundle remote) { return
     * getMentionForLocalEntity(local.getRecords(), localEntity, remote); }
     */

    public MentionOf getMentionForLocalEntity(NamedBundle local,
                                              Entity localEntity,
                                              NamedBundle remote) {
        return getMentionForLocalEntity(local.getEntityOrActivityOrWasGeneratedBy(),
                                        localEntity, remote);
    }

    MentionOf getMentionForLocalEntity(List<Statement> records,
                                       Entity localEntity, NamedBundle remote) {
        for (Statement o : records) {
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
        fields.put(Activity.class, new String[] { "Id", "StartTime", "EndTime", "Any" });

        fields.put(Used.class, new String[] { "Id", "Activity", "Entity",
                                             "Time", "Any" });
        fields.put(WasGeneratedBy.class, new String[] { "Id", "Entity",
                                                       "Activity", "Time",
                                                       "Any" });
        fields.put(WasInvalidatedBy.class, new String[] { "Id", "Entity",
                                                         "Activity", "Time",
                                                         "Any" });
        fields.put(WasStartedBy.class, new String[] { "Id", "Activity",
                                                     "Trigger", "Starter",
                                                     "Time", "Any" });
        // 0 , 1 , 2 , 3 , 4 , 5
        // length=6
        // firstTimeIndex=4
        // last index=5
        fields.put(WasEndedBy.class, new String[] { "Id", "Activity",
                                                   "Trigger", "Ender", "Time",
                                                   "Any" });
        fields.put(WasInformedBy.class, new String[] { "Id", "Effect", "Cause",
                                                      "Any" });
        fields.put(WasDerivedFrom.class, new String[] { "Id",
                                                       "GeneratedEntity",
                                                       "UsedEntity",
                                                       "Activity",
                                                       "Generation", "Usage",
                                                       "Any" });
        fields.put(WasInfluencedBy.class, new String[] { "Id", "Influencee",
                                                        "Influencer", "Any" });
        fields.put(WasAttributedTo.class, new String[] { "Id", "Entity",
                                                        "Agent", "Any" });
        fields.put(WasAssociatedWith.class, new String[] { "Id", "Activity",
                                                          "Agent", "Plan",
                                                          "Any" });
        fields.put(ActedOnBehalfOf.class, new String[] { "Id", "Subordinate",
                                                        "Responsible",
                                                        "Activity", "Any" });
        fields.put(SpecializationOf.class, new String[] { "SpecializedEntity",
                                                          "GeneralEntity" });
        
	// never use the accessor id for Mention, since it is not defined.
	// However, this allows iterations over this data structure to be performed
	//  like others.

        fields.put(MentionOf.class, new String[] { "Id", 
						   "SpecializedEntity",
						   "GeneralEntity",
						   "Bundle" });
        

        types.put(Activity.class, new Class[] { QName.class, 
                                           XMLGregorianCalendar.class,
                                           XMLGregorianCalendar.class,
                                           Object.class });
        types.put(Used.class, new Class[] { QName.class, ActivityRef.class,
                                           EntityRef.class,
                                           XMLGregorianCalendar.class,
                                           Object.class });
        types.put(WasGeneratedBy.class,
                  new Class[] { QName.class, EntityRef.class,
                               ActivityRef.class, XMLGregorianCalendar.class,
                               Object.class });
        types.put(WasInvalidatedBy.class,
                  new Class[] { QName.class, EntityRef.class,
                               ActivityRef.class, XMLGregorianCalendar.class,
                               Object.class });
        types.put(WasStartedBy.class, new Class[] { QName.class,
                                                   ActivityRef.class,
                                                   EntityRef.class,
                                                   ActivityRef.class,
                                                   XMLGregorianCalendar.class,
                                                   Object.class });
        types.put(WasEndedBy.class, new Class[] { QName.class,
                                                 ActivityRef.class,
                                                 EntityRef.class,
                                                 ActivityRef.class,
                                                 XMLGregorianCalendar.class,
                                                 Object.class });
        types.put(WasInformedBy.class, new Class[] { QName.class,
                                                    ActivityRef.class,
                                                    ActivityRef.class,
                                                    Object.class });
        types.put(WasDerivedFrom.class, new Class[] { QName.class,
                                                     EntityRef.class,
                                                     EntityRef.class,
                                                     ActivityRef.class,
                                                     GenerationRef.class,
                                                     UsageRef.class,
                                                     Object.class });
        types.put(WasInfluencedBy.class, new Class[] { QName.class,
                                                      AnyRef.class,
                                                      AnyRef.class,
                                                      Object.class });
        types.put(WasAttributedTo.class, new Class[] { QName.class,
                                                      EntityRef.class,
                                                      AgentRef.class,
                                                      Object.class });
        types.put(WasAssociatedWith.class, new Class[] { QName.class,
                                                        ActivityRef.class,
                                                        AgentRef.class,
                                                        EntityRef.class,
                                                        Object.class });
        types.put(ActedOnBehalfOf.class, new Class[] { QName.class,
                                                      AgentRef.class,
                                                      AgentRef.class,
                                                      ActivityRef.class,
                                                      Object.class });
        types.put(SpecializationOf.class, new Class[] { EntityRef.class,
                                                       EntityRef.class });
        types.put(MentionOf.class, new Class[] { QName.class,
						 EntityRef.class,
						 EntityRef.class,
						 EntityRef.class });
    }

    @SuppressWarnings("unchecked")
    public <T> JAXBElement<T> newElement(T r) {
        if (r instanceof Activity) {
            return (JAXBElement<T>) p.newElement(p.newActivity((Activity) r));
        }
        if (r instanceof Used) {
            return (JAXBElement<T>) p.newElement(p.newUsed((Used) r));
        }
        if (r instanceof WasStartedBy) {
            return (JAXBElement<T>) p.newElement(p
                    .newWasStartedBy((WasStartedBy) r));
        }
        if (r instanceof WasEndedBy) {
            return (JAXBElement<T>) p.newElement(p
                    .newWasEndedBy((WasEndedBy) r));
        }
        if (r instanceof WasGeneratedBy) {
            return (JAXBElement<T>) p.newElement(p
                    .newWasGeneratedBy((WasGeneratedBy) r));
        }
        if (r instanceof WasDerivedFrom) {
            return (JAXBElement<T>) p.newElement(p
                    .newWasDerivedFrom((WasDerivedFrom) r));
        }
        if (r instanceof WasAssociatedWith) {
            return (JAXBElement<T>) p.newElement(p
                    .newWasAssociatedWith((WasAssociatedWith) r));
        }
        if (r instanceof WasInvalidatedBy) {
            return (JAXBElement<T>) p.newElement(p
                    .newWasInvalidatedBy((WasInvalidatedBy) r));
        }
        if (r instanceof WasAttributedTo) {
            return (JAXBElement<T>) p.newElement(p
                    .newWasAttributedTo((WasAttributedTo) r));
        }
        if (r instanceof WasInformedBy) {
            return (JAXBElement<T>) p.newElement(p
                    .newWasInformedBy((WasInformedBy) r));
        }
        if (r instanceof WasInfluencedBy) {
            return (JAXBElement<T>) p.newElement(p
                    .newWasInfluencedBy((WasInfluencedBy) r));
        }
        if (r instanceof MentionOf) {
            return (JAXBElement<T>) p.newElement(p.newMentionOf((MentionOf) r));
        }
        if (r instanceof ActedOnBehalfOf) {
            return (JAXBElement<T>) p.newElement(p
                    .newActedOnBehalfOf((ActedOnBehalfOf) r));
        }

        
        System.out.println("newElement Unknow relation " + r);
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public <T> T addAttributes(T from, T to) {
        if (from instanceof Used) {
            return (T) p.addAttributes((Used) from, (Used) to);
        }
        if (from instanceof WasStartedBy) {
            return (T) p.addAttributes((WasStartedBy) from, (WasStartedBy) to);
        }
        if (from instanceof WasEndedBy) {
            return (T) p.addAttributes((WasEndedBy) from, (WasEndedBy) to);
        }
        if (from instanceof WasGeneratedBy) {
            return (T) p.addAttributes((WasGeneratedBy) from,
                                        (WasGeneratedBy) to);
        }
        if (from instanceof WasDerivedFrom) {
            return (T) p.addAttributes((WasDerivedFrom) from,
                                        (WasDerivedFrom) to);
        }
        if (from instanceof WasAssociatedWith) {
            return (T) p.addAttributes((WasAssociatedWith) from,
                                        (WasAssociatedWith) to);
        }
        if (from instanceof WasInvalidatedBy) {
            return (T) p.addAttributes((WasInvalidatedBy) from,
                                        (WasInvalidatedBy) to);
        }

        if (from instanceof WasAttributedTo) {
            return (T) p.addAttributes((WasAttributedTo) from,
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
            return (T) p.addAttributes((WasInformedBy) from,
                                        (WasInformedBy) to);
        }
        if (from instanceof WasInfluencedBy) {
            return (T) p.addAttributes((WasInfluencedBy) from,
                                        (WasInfluencedBy) to);
        }

        if (from instanceof ActedOnBehalfOf) {
            return (T) p.addAttributes((ActedOnBehalfOf) from,
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
                || (o instanceof AlternateOf)
                || (o instanceof MentionOf)
                || (o instanceof Membership);
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

    public void forAllRecords(List<Statement> records, RecordAction action) {
        for (Statement o : records) {
            run(o, action);
        }
    }

    public void run(Statement o, RecordAction action) {

        if (o instanceof Entity) {
            action.run((Entity) o);
        } else if (o instanceof Activity) {
            action.run((Activity) o);
        } else if (o instanceof Agent) {
            action.run((Agent) o);
        } else if (o instanceof Used) {
            action.run((Used) o);
        } else if (o instanceof WasGeneratedBy) {
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

        else if (o instanceof Membership) {
            Membership tmp = (Membership) o;
            action.run(tmp);
        }

    }
    
    public Object run(Statement o, RecordValue action) {
        if (o instanceof Entity) {
            return action.run((Entity) o);
        } else if (o instanceof Activity) {
            return action.run((Activity) o);
        } else if (o instanceof Agent) {
            return action.run((Agent) o);
        } else if (o instanceof Used) {
            return action.run((Used) o);
        } else if (o instanceof WasGeneratedBy) {
            WasGeneratedBy tmp = (WasGeneratedBy) o;
            return action.run(tmp);
        }

        else if (o instanceof WasInvalidatedBy) {
            WasInvalidatedBy tmp = (WasInvalidatedBy) o;
            return action.run(tmp);
        }

        else if (o instanceof WasStartedBy) {
            WasStartedBy tmp = (WasStartedBy) o;
            return action.run(tmp);
        }

        else if (o instanceof WasEndedBy) {
            WasEndedBy tmp = (WasEndedBy) o;
            return action.run(tmp);
        }

        else if (o instanceof WasInformedBy) {
            WasInformedBy tmp = (WasInformedBy) o;
            return action.run(tmp);
        }

        else if (o instanceof WasDerivedFrom) {
            WasDerivedFrom tmp = (WasDerivedFrom) o;
            return action.run(tmp);
        }

        else if (o instanceof WasAssociatedWith) {
            WasAssociatedWith tmp = (WasAssociatedWith) o;
            return action.run(tmp);
        }

        else if (o instanceof WasAttributedTo) {
            WasAttributedTo tmp = (WasAttributedTo) o;
            return action.run(tmp);
        }

        else if (o instanceof ActedOnBehalfOf) {
            ActedOnBehalfOf tmp = (ActedOnBehalfOf) o;
            return action.run(tmp);
        }

        else if (o instanceof WasInfluencedBy) {
            WasInfluencedBy tmp = (WasInfluencedBy) o;
            return action.run(tmp);
        }

        else if (o instanceof AlternateOf) {
            AlternateOf tmp = (AlternateOf) o;
            return action.run(tmp);
        }

        else if (o instanceof MentionOf) {
            MentionOf tmp = (MentionOf) o;
            return action.run(tmp);
        }

        else if (o instanceof SpecializationOf) {
            SpecializationOf tmp = (SpecializationOf) o;
            return action.run(tmp);
        }
        else throw new UnsupportedOperationException();

    }

}
