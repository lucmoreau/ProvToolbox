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

    private ProvFactory of=new ProvFactory();

    public List<Element> getElements(Bundle g) {
        List<Element> res=new LinkedList();
        res.addAll(g.getRecords().getEntity());
        res.addAll(g.getRecords().getActivity());
        res.addAll(g.getRecords().getAgent());
        return res;
    }

    public List<Relation0> getRelations(Bundle g) {
        List<Relation0> res=new LinkedList();
        Dependencies dep=g.getRecords().getDependencies();
        for (Object o:dep.getUsedOrWasGeneratedByOrWasStartedBy()) {
            res.add((Relation0)o);
        }
        return res;
    }

    public QName getEffect(Relation0 r) {
        if (r instanceof Used) {
            return ((Used)r).getActivity().getRef();
        }
        if (r instanceof WasStartedBy) {
            return ((WasStartedBy)r).getActivity().getRef();
        }
        if (r instanceof WasEndedBy) {
            return ((WasEndedBy)r).getActivity().getRef();
        }
        if (r instanceof WasGeneratedBy) {
            return ((WasGeneratedBy)r).getEntity().getRef();
        }
        if (r instanceof WasDerivedFrom) {
            return ((WasDerivedFrom)r).getGeneratedEntity().getRef();
        }
        if (r instanceof WasRevisionOf) {
            return ((WasRevisionOf)r).getNewer().getRef();
        }
        if (r instanceof WasAssociatedWith) {
            return ((WasAssociatedWith)r).getActivity().getRef();
        }
        if (r instanceof WasInvalidatedBy) {
            return ((WasInvalidatedBy)r).getEntity().getRef();
        }

        if (r instanceof WasAttributedTo) {
            return ((WasAttributedTo)r).getEntity().getRef();
        }
        if (r instanceof AlternateOf) {
            return ((AlternateOf)r).getEntity2().getRef();
        }
        if (r instanceof SpecializationOf) {
            return ((SpecializationOf)r).getSpecializedEntity().getRef();
        }
        if (r instanceof WasInformedBy) {
            return ((WasInformedBy)r).getEffect().getRef();
        }
        if (r instanceof WasInfluencedBy) {
            return ((WasInfluencedBy)r).getInfluencee().getRef();
        }

        if (r instanceof ActedOnBehalfOf) {
            return ((ActedOnBehalfOf)r).getSubordinate().getRef();
        }

        if (r instanceof DerivedByInsertionFrom) {
            return ((DerivedByInsertionFrom)r).getAfter().getRef();
        }
        System.out.println("Unknow relation " + r);
        throw new UnsupportedOperationException();
    }
        
    public QName getCause(Relation0 r) {
        if (r instanceof Used) {
            return ((Used)r).getEntity().getRef();
        }
        if (r instanceof WasGeneratedBy) {
	    ActivityRef ref=((WasGeneratedBy)r).getActivity();
	    if (ref==null) return null;
            return ref.getRef();
        }
        if (r instanceof WasInvalidatedBy) {
	    ActivityRef ref=((WasInvalidatedBy)r).getActivity();
	    if (ref==null) return null;
            return ref.getRef();
        }
        if (r instanceof WasStartedBy) {
	    EntityRef ref=((WasStartedBy)r).getTrigger();
	    if (ref==null) return null;
            return ref.getRef();
        }
        if (r instanceof WasEndedBy) {
	    EntityRef ref=((WasEndedBy)r).getTrigger();
	    if (ref==null) return null;
            return ref.getRef();
        }
        if (r instanceof WasDerivedFrom) {
            return ((WasDerivedFrom)r).getUsedEntity().getRef();
        }

        if (r instanceof WasInfluencedBy) {
            return ((WasInfluencedBy)r).getInfluencer().getRef();
        }
        if (r instanceof WasRevisionOf) {
            return ((WasRevisionOf)r).getOlder().getRef();
        }
        if (r instanceof WasAssociatedWith) {
            return ((WasAssociatedWith)r).getAgent().getRef();
        }
        if (r instanceof WasAttributedTo) {
            return ((WasAttributedTo)r).getAgent().getRef();
        }
        if (r instanceof AlternateOf) {
            return ((AlternateOf)r).getEntity1().getRef();
        }
        if (r instanceof SpecializationOf) {
            return ((SpecializationOf)r).getGeneralEntity().getRef();
        }
        if (r instanceof WasInformedBy) {
            return ((WasInformedBy)r).getCause().getRef();
        }
        if (r instanceof ActedOnBehalfOf) {
            return ((ActedOnBehalfOf)r).getResponsible().getRef();
        }
        if (r instanceof DerivedByInsertionFrom) {
            return ((DerivedByInsertionFrom)r).getBefore().getRef();
        }
	System.out.println("Unknown relation " + r);
        throw new UnsupportedOperationException();
    }

    
    public List<QName> getOtherCauses(Relation0 r) {
        if (r instanceof WasAssociatedWith) {
            List<QName> res=new LinkedList<QName>();
            EntityRef e=((WasAssociatedWith)r).getPlan();
            if (e==null) return null;
            res.add(e.getRef());
            return res;
        }
        if (r instanceof WasStartedBy) {
            List<QName> res=new LinkedList<QName>();
            ActivityRef a=((WasStartedBy)r).getStarter();
            if (a==null) return null;
            res.add(a.getRef());
            return res;
        }
        if (r instanceof WasEndedBy) {
            List<QName> res=new LinkedList<QName>();
            ActivityRef a=((WasEndedBy)r).getEnder();
            if (a==null) return null;
            res.add(a.getRef());
            return res;
        }
        if (r instanceof ActedOnBehalfOf) {
            List<QName> res=new LinkedList<QName>();
            ActivityRef a=((ActedOnBehalfOf)r).getActivity();
            if (a==null) return null;
            res.add(a.getRef());
            return res;
        }
        if (r instanceof DerivedByInsertionFrom) {
            List<QName> res=new LinkedList<QName>();
            DerivedByInsertionFrom dbif=((DerivedByInsertionFrom)r);
            for (Entry entry: dbif.getEntry()) {
                res.add(entry.getEntity().getRef());
            }
            return res;
        }
	return null;
    }

    public MentionOf getMentionForRemoteEntity(Bundle local, Entity remoteEntity, NamedBundle remote) {
        return getMentionForRemoteEntity(local.getRecords(),remoteEntity,remote);
    }

    public MentionOf getMentionForRemoteEntity(NamedBundle local, Entity remoteEntity, NamedBundle remote) {
        return getMentionForRemoteEntity(local.getRecords(),remoteEntity,remote);
    }

    MentionOf getMentionForRemoteEntity(Records local, Entity remoteEntity, NamedBundle remote) {
        Dependencies dep=local.getDependencies();
        for (Object o:dep.getUsedOrWasGeneratedByOrWasStartedBy()) {
            if (o instanceof MentionOf) {
                MentionOf ctxt=(MentionOf) o;
                QName id1=remoteEntity.getId();
                QName id2=remote.getId();
                if (ctxt.getGeneralEntity().getRef().equals(id1)
                    &&
                    ctxt.getBundle().getRef().equals(id2))
                    return ctxt;
            }
        }
        return null;
    }

    public MentionOf getMentionForLocalEntity(Bundle local, Entity localEntity, NamedBundle remote) {
        return getMentionForLocalEntity(local.getRecords(),localEntity,remote);
    }

    public MentionOf getMentionForLocalEntity(NamedBundle local, Entity localEntity, NamedBundle remote) {
        return getMentionForLocalEntity(local.getRecords(),localEntity,remote);
    }

    MentionOf getMentionForLocalEntity(Records local, Entity localEntity, NamedBundle remote) {
        Dependencies dep=local.getDependencies();
        for (Object o:dep.getUsedOrWasGeneratedByOrWasStartedBy()) {
            if (o instanceof MentionOf) {
                MentionOf ctxt=(MentionOf) o;
                QName id1=localEntity.getId();
                QName id2=remote.getId();
                if (ctxt.getSpecializedEntity().getRef().equals(id1)
                    &&
                    ctxt.getBundle().getRef().equals(id2))
                    return ctxt;
            }
        }
        return null;
    }

    /** TODO: should \-unescape local part */
    public String toURI(QName qname) {
	return qname.getNamespaceURI() + qname.getLocalPart();
    }

    final static private Hashtable<Class,String[]> fields=new  Hashtable<Class,String[]> ();
    final static private Hashtable<Class,Class[]> types=new  Hashtable<Class,Class[]> ();

    public Object getter(Object o, int i) throws java.lang.NoSuchMethodException, java.lang.IllegalAccessException, java.lang.reflect.InvocationTargetException{
	String field=fields.get(o.getClass())[i];
	Method method=o.getClass().getDeclaredMethod("get"+field);
	return method.invoke(o);
    }
    static public String[] getTypes(Object o) {
	return fields.get(o.getClass());
    }

    public Object setter(Object o, int i, Object val) throws java.lang.NoSuchMethodException, java.lang.IllegalAccessException, java.lang.reflect.InvocationTargetException{
	String field=fields.get(o.getClass())[i];
	Method method=o.getClass().getDeclaredMethod("set"+field,types.get(o.getClass())[i]);
	return method.invoke(o,val);
    }
	

    static {
	fields.put(Used.class,              new String[] {"Id","Activity","Entity","Time","Any"});
	fields.put(WasGeneratedBy.class,    new String[] {"Id","Entity","Activity","Time","Any"});
	fields.put(WasInvalidatedBy.class,  new String[] {"Id","Entity","Activity","Time","Any"});
	fields.put(WasStartedBy.class,      new String[] {"Id","Activity","Trigger","Starter","Time","Any"});
	fields.put(WasEndedBy.class,        new String[] {"Id","Activity","Trigger","Ender","Time","Any"});
	fields.put(WasInformedBy.class,     new String[] {"Id","Effect","Cause","Any"});
	fields.put(WasDerivedFrom.class,    new String[] {"Id","GeneratedEntity", "UsedEntity","Activity","Generation","Usage","Any"});
	fields.put(WasInfluencedBy.class,   new String[] {"Id","Influencee","Influencer","Any"});
	fields.put(WasAttributedTo.class,   new String[] {"Id","Entity","Agent","Any"});
	fields.put(WasAssociatedWith.class, new String[] {"Id","Activity","Agent", "Plan", "Any"});
	fields.put(ActedOnBehalfOf.class,   new String[] {"Id","Subordinate","Responsible", "Activity", "Any"});



	types.put(Used.class,              new Class[] {QName.class,ActivityRef.class,EntityRef.class,XMLGregorianCalendar.class,Object.class});
	types.put(WasGeneratedBy.class,    new Class[] {QName.class,EntityRef.class,ActivityRef.class,XMLGregorianCalendar.class,Object.class});
	types.put(WasInvalidatedBy.class,  new Class[] {QName.class,EntityRef.class,ActivityRef.class,XMLGregorianCalendar.class,Object.class});
	types.put(WasStartedBy.class,      new Class[] {QName.class,ActivityRef.class,EntityRef.class,ActivityRef.class,XMLGregorianCalendar.class,Object.class});
	types.put(WasEndedBy.class,        new Class[] {QName.class,ActivityRef.class,EntityRef.class,ActivityRef.class,XMLGregorianCalendar.class,Object.class});
	types.put(WasInformedBy.class,     new Class[] {QName.class,ActivityRef.class,ActivityRef.class,Object.class});
	types.put(WasDerivedFrom.class,    new Class[] {QName.class,EntityRef.class, EntityRef.class,ActivityRef.class,WasGeneratedBy.class,Used.class,Object.class});
	types.put(WasInfluencedBy.class,   new Class[] {QName.class,AnyRef.class,AnyRef.class,Object.class});
	types.put(WasAttributedTo.class,   new Class[] {QName.class,EntityRef.class,AgentRef.class,Object.class});
	types.put(WasAssociatedWith.class, new Class[] {QName.class,ActivityRef.class,AgentRef.class, EntityRef.class, Object.class});
	types.put(ActedOnBehalfOf.class,   new Class[] {QName.class,AgentRef.class,AgentRef.class,ActivityRef.class, Object.class});

    }

    public <T> JAXBElement<T> newElement(T r) {
        if (r instanceof Used) {
            return (JAXBElement<T>)of.newElement((Used)r);
        }
        if (r instanceof WasStartedBy) {
            return (JAXBElement<T>)of.newElement((WasStartedBy)r);
        }
        if (r instanceof WasEndedBy) {
            return (JAXBElement<T>)of.newElement((WasEndedBy)r);
        }
        if (r instanceof WasGeneratedBy) {
            return (JAXBElement<T>)of.newElement((WasGeneratedBy)r);
        }
        if (r instanceof WasDerivedFrom) {
            return (JAXBElement<T>)of.newElement((WasDerivedFrom)r);
        }
        if (r instanceof WasAssociatedWith) {
            return (JAXBElement<T>)of.newElement((WasAssociatedWith)r);
        }
        if (r instanceof WasInvalidatedBy) {
            return (JAXBElement<T>)of.newElement((WasInvalidatedBy)r);
        }

        if (r instanceof WasAttributedTo) {
            return (JAXBElement<T>)of.newElement((WasAttributedTo)r);
        }
	/*
	  if (r instanceof WasRevisionOf) {
	  return (JAXBElement<T>)of.newElement((WasRevisionOf)r);
	  }
	  if (r instanceof AlternateOf) {
	  return (JAXBElement<T>)of.newElement((AlternateOf)r);
	  }
	  if (r instanceof SpecializationOf) {
	  return (JAXBElement<T>)of.newElement((SpecializationOf)r);
	  }
	*/
        if (r instanceof WasInformedBy) {
            return (JAXBElement<T>)of.newElement((WasInformedBy)r);
        }
        if (r instanceof WasInfluencedBy) {
            return (JAXBElement<T>)of.newElement((WasInfluencedBy)r);
        }

        if (r instanceof ActedOnBehalfOf) {
            return (JAXBElement<T>)of.newElement((ActedOnBehalfOf)r);
        }

	/*
	  if (r instanceof DerivedByInsertionFrom) {
	  return (JAXBElement<T>)of.newElement((DerivedByInsertionFrom)r);
	  }
	*/
        System.out.println("newElement Unknow relation " + r);
        throw new UnsupportedOperationException();
    }

    /** Indicates whether object has no time field.
	@TODO: introduce an interface to do this. */

    public boolean hasNoTime(Object o) {
	return (o instanceof WasDerivedFrom)
	    || (o instanceof ActedOnBehalfOf)
	    || (o instanceof WasInformedBy)
	    || (o instanceof WasAttributedTo)
	    || (o instanceof WasAssociatedWith)
	    || (o instanceof WasInfluencedBy)
	    || (o instanceof SpecializationOf)
	    || (o instanceof AlternateOf);
    }


    public int getFirstTimeIndex(Object o) {
	String [] types=getTypes(o);
	if (o instanceof Activity) {
	    return types.length-3;
	} else if (hasNoTime(o)) {
	    return types.length-1;
	} else {
	    return types.length-2;
	}
    }

    public int getLastIndex(Object o) {
	String [] types=getTypes(o);
	return types.length-1;
    }


}

