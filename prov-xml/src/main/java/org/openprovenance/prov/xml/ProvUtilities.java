package org.openprovenance.prov.xml;

import java.util.List;
import java.util.Hashtable;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.model.QualifiedName;

import java.lang.reflect.Method;

/** Utilities for manipulating PROV Descriptions. */

public class ProvUtilities extends org.openprovenance.prov.model.ProvUtilities {

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
                org.openprovenance.prov.model.QualifiedName id1 = remoteEntity.getId();
                org.openprovenance.prov.model.QualifiedName id2 = remote.getId();
                if (ctxt.getGeneralEntity().equals(id1)
                        && ctxt.getBundle().equals(id2))
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
                org.openprovenance.prov.model.QualifiedName id1 = localEntity.getId();
                org.openprovenance.prov.model.QualifiedName id2 = remote.getId();
                if (ctxt.getSpecificEntity().equals(id1)
                        && ctxt.getBundle().equals(id2))
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

    public Method getterMethod(Statement o, int i)
            throws java.lang.NoSuchMethodException {
        String field = fields.get(o.getClass())[i];
        Method method = o.getClass().getDeclaredMethod("get" + field);
        return method;
    }

    static public String[] getTypes(Object o) {
        return fields.get(o.getClass());
    }

    public Method setterMethod(Statement o, int i, Object val)
            throws java.lang.NoSuchMethodException{
        String field = fields.get(o.getClass())[i];
        Method method = o.getClass()
                .getDeclaredMethod("set" + field, types.get(o.getClass())[i]);
        return method;
    }

    static {
        fields.put(Activity.class, new String[] { "Id", "StartTime", "EndTime", "Other" });

        fields.put(Used.class, new String[] { "Id", "Activity", "Entity",
                                             "Time", "Other" });
        fields.put(WasGeneratedBy.class, new String[] { "Id", "Entity",
                                                       "Activity", "Time",
                                                       "Other" });
        fields.put(WasInvalidatedBy.class, new String[] { "Id", "Entity",
                                                         "Activity", "Time",
                                                         "Other" });
        fields.put(WasStartedBy.class, new String[] { "Id", "Activity",
                                                     "Trigger", "Starter",
                                                     "Time", "Other" });
        // 0 , 1 , 2 , 3 , 4 , 5
        // length=6
        // firstTimeIndex=4
        // last index=5
        fields.put(WasEndedBy.class, new String[] { "Id", "Activity",
                                                   "Trigger", "Ender", "Time",
                                                   "Other" });
        fields.put(WasInformedBy.class, new String[] { "Id", "Informed", "Informant",
                                                      "Other" });
        fields.put(WasDerivedFrom.class, new String[] { "Id",
                                                       "GeneratedEntity",
                                                       "UsedEntity",
                                                       "Activity",
                                                       "Generation", "Usage",
                                                       "Other" });
        fields.put(WasInfluencedBy.class, new String[] { "Id", "Influencee",
                                                        "Influencer", "Others" });
        fields.put(WasAttributedTo.class, new String[] { "Id", "Entity",
                                                        "Agent", "Other" });
        fields.put(WasAssociatedWith.class, new String[] { "Id", "Activity",
                                                          "Agent", "Plan",
                                                          "Other" });
        fields.put(ActedOnBehalfOf.class, new String[] { "Id", "Delegate",
                                                        "Responsible",
                                                        "Activity", "Others" });
        fields.put(SpecializationOf.class, new String[] { "SpecificEntity",
                                                          "GeneralEntity" });
        
	// never use the accessor id for Mention, since it is not defined.
	// However, this allows iterations over this data structure to be performed
	//  like others.

        fields.put(MentionOf.class, new String[] { "Id", 
						   "SpecificEntity",
						   "GeneralEntity",
						   "Bundle" });
        

        types.put(Activity.class, new Class[] { QualifiedName.class, 
                                           XMLGregorianCalendar.class,
                                           XMLGregorianCalendar.class,
                                           Object.class });
        types.put(Used.class, new Class[] { QualifiedName.class, QualifiedName.class,
                                           QualifiedName.class,
                                           XMLGregorianCalendar.class,
                                           Object.class });
        types.put(WasGeneratedBy.class,
                  new Class[] { QualifiedName.class, QualifiedName.class,
                               QualifiedName.class, XMLGregorianCalendar.class,
                               Object.class });
        types.put(WasInvalidatedBy.class,
                  new Class[] { QualifiedName.class, QualifiedName.class,
                               QualifiedName.class, XMLGregorianCalendar.class,
                               Object.class });
        types.put(WasStartedBy.class, new Class[] { QualifiedName.class,
                                                   QualifiedName.class,
                                                   QualifiedName.class,
                                                   QualifiedName.class,
                                                   XMLGregorianCalendar.class,
                                                   Object.class });
        types.put(WasEndedBy.class, new Class[] { QualifiedName.class,
                                                 QualifiedName.class,
                                                 QualifiedName.class,
                                                 QualifiedName.class,
                                                 XMLGregorianCalendar.class,
                                                 Object.class });
        types.put(WasInformedBy.class, new Class[] { QualifiedName.class,
                                                    QualifiedName.class,
                                                    QualifiedName.class,
                                                    Object.class });
        types.put(WasDerivedFrom.class, new Class[] { QualifiedName.class,
                                                     QualifiedName.class,
                                                     QualifiedName.class,
                                                     QualifiedName.class,
                                                     QualifiedName.class,
                                                     QualifiedName.class,
                                                     Object.class });
        types.put(WasInfluencedBy.class, new Class[] { QualifiedName.class,
                                                      QualifiedName.class,
                                                      QualifiedName.class,
                                                      Object.class });
        types.put(WasAttributedTo.class, new Class[] { QualifiedName.class,
                                                      QualifiedName.class,
                                                      QualifiedName.class,
                                                      Object.class });
        types.put(WasAssociatedWith.class, new Class[] { QualifiedName.class,
                                                        QualifiedName.class,
                                                        QualifiedName.class,
                                                        QualifiedName.class,
                                                        Object.class });
        types.put(ActedOnBehalfOf.class, new Class[] { QualifiedName.class,
                                                      QualifiedName.class,
                                                      QualifiedName.class,
                                                      QualifiedName.class,
                                                      Object.class });
        types.put(SpecializationOf.class, new Class[] { QualifiedName.class,
                                                       QualifiedName.class });
        types.put(MentionOf.class, new Class[] { QualifiedName.class,
						 QualifiedName.class,
						 QualifiedName.class,
						 QualifiedName.class });
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



    public int getFirstTimeIndex(Statement o) {
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

 
}
