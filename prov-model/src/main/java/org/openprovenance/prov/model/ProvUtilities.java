package org.openprovenance.prov.model;

import java.util.Hashtable;
import java.util.List;
import java.util.LinkedList;


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
 

    public QualifiedName getEffect(Relation0 r) {
        if (r instanceof Used) {
            return ((Used) r).getActivity();
        }
        if (r instanceof WasStartedBy) {
            return ((WasStartedBy) r).getActivity();
        }
        if (r instanceof WasEndedBy) {
            return ((WasEndedBy) r).getActivity();
        }
        if (r instanceof WasGeneratedBy) {
            return ((WasGeneratedBy) r).getEntity();
        }
        if (r instanceof WasDerivedFrom) {
            return ((WasDerivedFrom) r).getGeneratedEntity();
        }
       
        if (r instanceof WasAssociatedWith) {
            return ((WasAssociatedWith) r).getActivity();
        }
        if (r instanceof WasInvalidatedBy) {
            return ((WasInvalidatedBy) r).getEntity();
        }

        if (r instanceof WasAttributedTo) {
            return ((WasAttributedTo) r).getEntity();
        }
        if (r instanceof AlternateOf) {
            return ((AlternateOf) r).getAlternate1();
        }
        if (r instanceof SpecializationOf) {
            return ((SpecializationOf) r).getSpecificEntity();
        }
        if (r instanceof HadMember) {
            return ((HadMember) r).getCollection();
        }
        if (r instanceof WasInformedBy) {
            return ((WasInformedBy) r).getInformed();
        }
        if (r instanceof MentionOf) {
            return ((MentionOf) r).getSpecificEntity();
        }
        if (r instanceof WasInfluencedBy) {
            return ((WasInfluencedBy) r).getInfluencee();
        }

        if (r instanceof ActedOnBehalfOf) {
            return ((ActedOnBehalfOf) r).getDelegate();
        }

        if (r instanceof DerivedByInsertionFrom) {
            return ((DerivedByInsertionFrom) r).getNewDictionary();
        }
        System.out.println("Unknown relation " + r);
        throw new UnsupportedOperationException();
    }
    

    public QualifiedName getCause(Relation0 r) {
        if (r instanceof Used) {
            return ((Used) r).getEntity();
        }
        if (r instanceof WasGeneratedBy) {
            return ((WasGeneratedBy) r).getActivity();
        }
        if (r instanceof WasInvalidatedBy) {
            return ((WasInvalidatedBy) r).getActivity();
        }
        if (r instanceof WasStartedBy) {
            return ((WasStartedBy) r).getTrigger();
        }
        if (r instanceof WasEndedBy) {
            return ((WasEndedBy) r).getTrigger();
        }
        if (r instanceof WasDerivedFrom) {
            return ((WasDerivedFrom) r).getUsedEntity();
        }

        if (r instanceof WasInfluencedBy) {
            return ((WasInfluencedBy) r).getInfluencer();
        }
          if (r instanceof WasAssociatedWith) {
            return ((WasAssociatedWith) r).getAgent();
        }
        if (r instanceof WasAttributedTo) {
            return ((WasAttributedTo) r).getAgent();
        }
        if (r instanceof AlternateOf) {
            return ((AlternateOf) r).getAlternate2();
        }
        if (r instanceof SpecializationOf) {
            return ((SpecializationOf) r).getGeneralEntity();
        }
        if (r instanceof HadMember) {
            return ((HadMember) r).getEntity().get(0);
        }
        if (r instanceof MentionOf) {
            return ((MentionOf) r).getGeneralEntity();
        }
        if (r instanceof WasInformedBy) {
            return ((WasInformedBy) r).getInformant();
        }
        if (r instanceof ActedOnBehalfOf) {
            return ((ActedOnBehalfOf) r).getResponsible();
        }
        if (r instanceof DerivedByInsertionFrom) {
            return ((DerivedByInsertionFrom) r).getOldDictionary();
        }
        System.out.println("Unknown relation " + r);
        throw new UnsupportedOperationException();
    }

    public List<QualifiedName> getOtherCauses(Relation0 r) {
        if (r instanceof WasAssociatedWith) {
            List<QualifiedName> res = new LinkedList<QualifiedName>();
            QualifiedName e = ((WasAssociatedWith) r).getPlan();
            if (e == null)
                return null;
            res.add(e);
            return res;
        }
        if (r instanceof WasStartedBy) {
            List<QualifiedName> res = new LinkedList<QualifiedName>();
            QualifiedName a = ((WasStartedBy) r).getStarter();
            if (a == null)
                return null;
            res.add(a);
            return res;
        }
        if (r instanceof MentionOf) {
            List<QualifiedName> res = new LinkedList<QualifiedName>();
            QualifiedName a = ((MentionOf) r).getBundle();
            if (a == null)
                return null;
            res.add(a);
            return res;
        }
        if (r instanceof HadMember) {
            List<QualifiedName> res = new LinkedList<QualifiedName>();
            List<QualifiedName> entities=((HadMember) r).getEntity();
            if ((entities==null) || (entities.size()<=1)) return null;
            boolean first=true;
            for (QualifiedName ee: entities) {
                if (!first) res.add(ee);
                first=false;
            }
            return res;
        }
        if (r instanceof WasEndedBy) {
            List<QualifiedName> res = new LinkedList<QualifiedName>();
            QualifiedName a = ((WasEndedBy) r).getEnder();
            if (a == null)
                return null;
            res.add(a);
            return res;
        }
        if (r instanceof ActedOnBehalfOf) {
            List<QualifiedName> res = new LinkedList<QualifiedName>();
            QualifiedName a = ((ActedOnBehalfOf) r).getActivity();
            if (a == null)
                return null;
            res.add(a);
            return res;
        }
        if (r instanceof DerivedByInsertionFrom) {
            List<QualifiedName> res = new LinkedList<QualifiedName>();
            DerivedByInsertionFrom dbif = ((DerivedByInsertionFrom) r);
            
            for (Entry entry : dbif.getKeyEntityPair()) {
                res.add(entry.getEntity());
            }
            return res;
        }
        return null;
    }

    public Hashtable<String, List<Attribute>> attributesWithNamespace(HasOther e,
                                                                      String namespace) {
	List ll=e.getOther();
        AttributeProcessor _attrs=new AttributeProcessor((List<Attribute>)ll); 
        return _attrs.attributesWithNamespace(namespace);
    }
    
    public void forAllStatementOrBundle(List<StatementOrBundle> records, StatementAction action) {
        for (StatementOrBundle o : records) {
            doAction(o, action);
        }
    }
    public void forAllStatement(List<Statement> records, StatementAction action) {
        for (Statement o : records) {
            doAction(o, action);
        }
    }

    public void doAction(StatementOrBundle s, StatementAction action) {
	switch (s.getKind()) {
	case PROV_ACTIVITY: 
	    action.doAction((Activity) s);
	    break;
	case PROV_AGENT:
	    action.doAction((Agent) s);
	    break;
	case PROV_ALTERNATE:
	    action.doAction((AlternateOf) s);
	    break;
	case PROV_ASSOCIATION:
	    action.doAction((WasAssociatedWith) s);
	    break;
	case PROV_ATTRIBUTION:
	    action.doAction((WasAttributedTo) s);
	    break;
	case PROV_BUNDLE: 
	    action.doAction((NamedBundle) s, this);
	    break;
	case PROV_COMMUNICATION:
	    action.doAction((WasInformedBy) s);
	    break;
	case PROV_DELEGATION:
	    action.doAction((ActedOnBehalfOf) s);
	    break;
	case PROV_DERIVATION:
	    action.doAction((WasDerivedFrom) s);
	    break;
	case PROV_DICTIONARY_INSERTION:
	    action.doAction((DerivedByInsertionFrom) s);
	    break;
	case PROV_DICTIONARY_MEMBERSHIP:
	    action.doAction((DictionaryMembership) s);
	    break;
	case PROV_DICTIONARY_REMOVAL:
	    action.doAction((DerivedByRemovalFrom) s);
	    break;
	case PROV_END:
	    action.doAction((WasEndedBy) s);
	    break;
	case PROV_ENTITY:
	    action.doAction((Entity) s);
	    break;
	case PROV_GENERATION:
	    action.doAction((WasGeneratedBy) s);
	    break;
	case PROV_INFLUENCE:
	    action.doAction((WasInfluencedBy) s);
	    break;
	case PROV_INVALIDATION:
	    action.doAction((WasInvalidatedBy) s);
	    break;
	case PROV_MEMBERSHIP:
	    action.doAction((HadMember) s);
	    break;
	case PROV_MENTION:
	    action.doAction((MentionOf) s);
	    break;
	case PROV_SPECIALIZATION:
	    action.doAction((SpecializationOf) s);
	    break;
	case PROV_START:
	    action.doAction((WasStartedBy) s);
	    break;
	case PROV_USAGE:
	    action.doAction((Used) s);
	    break;	
	}
    }

    

}
