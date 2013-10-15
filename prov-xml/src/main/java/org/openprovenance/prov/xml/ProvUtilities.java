package org.openprovenance.prov.xml;

import java.util.List;
import java.util.Hashtable;
import javax.xml.bind.JAXBElement;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.openprovenance.prov.model.RecordAction;
import org.openprovenance.prov.model.RecordValue;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.model.IDRef;

import java.lang.reflect.Method;

/** Utilities for manipulating PROV Descriptions. */

public class ProvUtilities extends org.openprovenance.prov.model.ProvUtilities{

    private ProvFactory p = new ProvFactory();

    public MentionOf getMentionForRemoteEntity(NamedBundle local,
                                               Entity remoteEntity,
                                               NamedBundle remote) {        
        return getMentionForLocalEntity(local.getStatement(),
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
        return getMentionForLocalEntity(local.getStatement(),
                                        localEntity, remote);
    }

    MentionOf getMentionForLocalEntity(List<Statement> records,
                                       Entity localEntity, NamedBundle remote) {
        for (Statement o : records) {
            if (o instanceof MentionOf) {
                MentionOf ctxt = (MentionOf) o;
                QName id1 = localEntity.getId();
                QName id2 = remote.getId();
                if (ctxt.getSpecificEntity().getRef().equals(id1)
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
        fields.put(WasInformedBy.class, new String[] { "Id", "Informed", "Informant",
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
        fields.put(ActedOnBehalfOf.class, new String[] { "Id", "Delegate",
                                                        "Responsible",
                                                        "Activity", "Any" });
        fields.put(SpecializationOf.class, new String[] { "SpecificEntity",
                                                          "GeneralEntity" });
        
	// never use the accessor id for Mention, since it is not defined.
	// However, this allows iterations over this data structure to be performed
	//  like others.

        fields.put(MentionOf.class, new String[] { "Id", 
						   "SpecificEntity",
						   "GeneralEntity",
						   "Bundle" });
        

        types.put(Activity.class, new Class[] { QName.class, 
                                           XMLGregorianCalendar.class,
                                           XMLGregorianCalendar.class,
                                           Object.class });
        types.put(Used.class, new Class[] { QName.class, IDRef.class,
                                           IDRef.class,
                                           XMLGregorianCalendar.class,
                                           Object.class });
        types.put(WasGeneratedBy.class,
                  new Class[] { QName.class, IDRef.class,
                               IDRef.class, XMLGregorianCalendar.class,
                               Object.class });
        types.put(WasInvalidatedBy.class,
                  new Class[] { QName.class, IDRef.class,
                               IDRef.class, XMLGregorianCalendar.class,
                               Object.class });
        types.put(WasStartedBy.class, new Class[] { QName.class,
                                                   IDRef.class,
                                                   IDRef.class,
                                                   IDRef.class,
                                                   XMLGregorianCalendar.class,
                                                   Object.class });
        types.put(WasEndedBy.class, new Class[] { QName.class,
                                                 IDRef.class,
                                                 IDRef.class,
                                                 IDRef.class,
                                                 XMLGregorianCalendar.class,
                                                 Object.class });
        types.put(WasInformedBy.class, new Class[] { QName.class,
                                                    IDRef.class,
                                                    IDRef.class,
                                                    Object.class });
        types.put(WasDerivedFrom.class, new Class[] { QName.class,
                                                     IDRef.class,
                                                     IDRef.class,
                                                     IDRef.class,
                                                     IDRef.class,
                                                     IDRef.class,
                                                     Object.class });
        types.put(WasInfluencedBy.class, new Class[] { QName.class,
                                                      IDRef.class,
                                                      IDRef.class,
                                                      Object.class });
        types.put(WasAttributedTo.class, new Class[] { QName.class,
                                                      IDRef.class,
                                                      IDRef.class,
                                                      Object.class });
        types.put(WasAssociatedWith.class, new Class[] { QName.class,
                                                        IDRef.class,
                                                        IDRef.class,
                                                        IDRef.class,
                                                        Object.class });
        types.put(ActedOnBehalfOf.class, new Class[] { QName.class,
                                                      IDRef.class,
                                                      IDRef.class,
                                                      IDRef.class,
                                                      Object.class });
        types.put(SpecializationOf.class, new Class[] { IDRef.class,
                                                       IDRef.class });
        types.put(MentionOf.class, new Class[] { QName.class,
						 IDRef.class,
						 IDRef.class,
						 IDRef.class });
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
        System.out.println("addAttributes Unknown relation " + from);
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
                || (o instanceof HadMember);
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

        else if (o instanceof HadMember) {
            HadMember tmp = (HadMember) o;
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
