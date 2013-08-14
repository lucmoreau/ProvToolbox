package org.openprovenance.prov.model;

import java.util.Hashtable;
import java.util.List;
import java.util.LinkedList;

import javax.xml.namespace.QName;


/** Utilities for manipulating PROV Descriptions. */

public class ProvUtilities {


    /*
     * public List<Element> getElements(Bundle g) { List<Element> res = new
     * LinkedList<Element>(); res.addAll(g.getRecords().getEntity());
     * res.addAll(g.getRecords().getActivity());
     * res.addAll(g.getRecords().getAgent()); return res; }
     */

    public List<Relation0> getRelations(Document d) {
        return getObject(Relation0.class,
                         d.getStatementOrBundle());
    }

    public List<Entity> getEntity(Document d) {
        return getObject(Entity.class, d.getStatementOrBundle());
    }

    public List<Activity> getActivity(Document d) {
        return getObject(Activity.class,
                         d.getStatementOrBundle());
    }

    public List<Agent> getAgent(Document d) {
        return getObject(Agent.class, d.getStatementOrBundle());
    }

    public List<NamedBundle> getBundle(Document d) {
        return getObject(NamedBundle.class, d.getStatementOrBundle());
    }

    public List<Relation0> getRelations(NamedBundle d) {
        return getObject2(Relation0.class,
                         d.getStatement());
    }

    public List<Entity> getEntity(NamedBundle d) {
        return getObject2(Entity.class, d.getStatement());
    }

    public List<Activity> getActivity(NamedBundle d) {
        return getObject2(Activity.class,
                         d.getStatement());
    }

    public List<Agent> getAgent(NamedBundle d) {
        return getObject2(Agent.class, d.getStatement());
    }

    public List<NamedBundle> getNamedBundle(Document d) {
        return getObject(NamedBundle.class,
                         d.getStatementOrBundle());
    }

    public List<Statement> getStatement(Document d) {
	return getObject(Statement.class,
	                 d.getStatementOrBundle());
    }

    @SuppressWarnings("unchecked")
    public List<Statement> getStatement(NamedBundle d) {
        List<?> res = d.getStatement();
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
            return ((AlternateOf) r).getAlternate1().getRef();
        }
        if (r instanceof SpecializationOf) {
            return ((SpecializationOf) r).getSpecificEntity().getRef();
        }
        if (r instanceof HadMember) {
            return ((HadMember) r).getCollection().getRef();
        }
        if (r instanceof WasInformedBy) {
            return ((WasInformedBy) r).getInformed().getRef();
        }
        if (r instanceof MentionOf) {
            return ((MentionOf) r).getSpecificEntity().getRef();
        }
        if (r instanceof WasInfluencedBy) {
            return ((WasInfluencedBy) r).getInfluencee().getRef();
        }

        if (r instanceof ActedOnBehalfOf) {
            return ((ActedOnBehalfOf) r).getDelegate().getRef();
        }

        if (r instanceof DerivedByInsertionFrom) {
            return ((DerivedByInsertionFrom) r).getNewDictionary().getRef();
        }
        System.out.println("Unknown relation " + r);
        throw new UnsupportedOperationException();
    }

    public QName getCause(Relation0 r) {
        if (r instanceof Used) {
            return ((Used) r).getEntity().getRef();
        }
        if (r instanceof WasGeneratedBy) {
            IDRef ref = ((WasGeneratedBy) r).getActivity();
            if (ref == null)
                return null;
            return ref.getRef();
        }
        if (r instanceof WasInvalidatedBy) {
            IDRef ref = ((WasInvalidatedBy) r).getActivity();
            if (ref == null)
                return null;
            return ref.getRef();
        }
        if (r instanceof WasStartedBy) {
            IDRef ref = ((WasStartedBy) r).getTrigger();
            if (ref == null)
                return null;
            return ref.getRef();
        }
        if (r instanceof WasEndedBy) {
            IDRef ref = ((WasEndedBy) r).getTrigger();
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
          if (r instanceof WasAssociatedWith) {
            return ((WasAssociatedWith) r).getAgent().getRef();
        }
        if (r instanceof WasAttributedTo) {
            return ((WasAttributedTo) r).getAgent().getRef();
        }
        if (r instanceof AlternateOf) {
            return ((AlternateOf) r).getAlternate2().getRef();
        }
        if (r instanceof SpecializationOf) {
            return ((SpecializationOf) r).getGeneralEntity().getRef();
        }
        if (r instanceof HadMember) {
            return ((HadMember) r).getEntity().get(0).getRef();
        }
        if (r instanceof MentionOf) {
            return ((MentionOf) r).getGeneralEntity().getRef();
        }
        if (r instanceof WasInformedBy) {
            return ((WasInformedBy) r).getInformant().getRef();
        }
        if (r instanceof ActedOnBehalfOf) {
            return ((ActedOnBehalfOf) r).getResponsible().getRef();
        }
        if (r instanceof DerivedByInsertionFrom) {
            return ((DerivedByInsertionFrom) r).getOldDictionary().getRef();
        }
        System.out.println("Unknown relation " + r);
        throw new UnsupportedOperationException();
    }

    public List<QName> getOtherCauses(Relation0 r) {
        if (r instanceof WasAssociatedWith) {
            List<QName> res = new LinkedList<QName>();
            IDRef e = ((WasAssociatedWith) r).getPlan();
            if (e == null)
                return null;
            res.add(e.getRef());
            return res;
        }
        if (r instanceof WasStartedBy) {
            List<QName> res = new LinkedList<QName>();
            IDRef a = ((WasStartedBy) r).getStarter();
            if (a == null)
                return null;
            res.add(a.getRef());
            return res;
        }
        if (r instanceof MentionOf) {
            List<QName> res = new LinkedList<QName>();
            IDRef a = ((MentionOf) r).getBundle();
            if (a == null)
                return null;
            res.add(a.getRef());
            return res;
        }
        if (r instanceof HadMember) {
            List<QName> res = new LinkedList<QName>();
            List<IDRef> entities=((HadMember) r).getEntity();
            if ((entities==null) || (entities.size()<=1)) return null;
            boolean first=true;
            for (IDRef ee: entities) {
                if (!first) res.add(ee.getRef());
                first=false;
            }
            return res;
        }
        if (r instanceof WasEndedBy) {
            List<QName> res = new LinkedList<QName>();
            IDRef a = ((WasEndedBy) r).getEnder();
            if (a == null)
                return null;
            res.add(a.getRef());
            return res;
        }
        if (r instanceof ActedOnBehalfOf) {
            List<QName> res = new LinkedList<QName>();
            IDRef a = ((ActedOnBehalfOf) r).getActivity();
            if (a == null)
                return null;
            res.add(a.getRef());
            return res;
        }
        if (r instanceof DerivedByInsertionFrom) {
            List<QName> res = new LinkedList<QName>();
            DerivedByInsertionFrom dbif = ((DerivedByInsertionFrom) r);
            
            for (Entry entry : dbif.getKeyEntityPair()) {
                res.add(entry.getEntity().getRef());
            }
            return res;
        }
        return null;
    }

    public Hashtable<String, List<Attribute>> attributesWithNamespace(HasExtensibility e,
                                                                      String namespace) {
        AttributeProcessor _attrs=new AttributeProcessor(e.getAny()); 
        return _attrs.attributesWithNamespace(namespace);
    }

}
