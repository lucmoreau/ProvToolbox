package org.openprovenance.prov.model;

import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.LinkedList;

import org.openprovenance.prov.model.StatementOrBundle.Kind;
import org.openprovenance.prov.model.exception.InvalidCaseException;
import javax.xml.datatype.XMLGregorianCalendar;


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

    public Hashtable<String, List<Other>> attributesWithNamespace(HasOther object,
                                                                  String namespace) {
	List<Other> ll=object.getOther();
	AttributeProcessor _attrs=new AttributeProcessor(ll); 
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

    public Object doAction(StatementOrBundle s, StatementActionValue action) {
  	switch (s.getKind()) {
  	case PROV_ACTIVITY: 
  	    return action.doAction((Activity) s);
  	case PROV_AGENT:
  	    return action.doAction((Agent) s);
  	case PROV_ALTERNATE:
  	    return action.doAction((AlternateOf) s);
  	case PROV_ASSOCIATION:
  	    return action.doAction((WasAssociatedWith) s);
  	case PROV_ATTRIBUTION:
  	    return action.doAction((WasAttributedTo) s);
  	case PROV_BUNDLE: 
  	    return action.doAction((NamedBundle) s, this);
  	case PROV_COMMUNICATION:
  	    return action.doAction((WasInformedBy) s);
  	case PROV_DELEGATION:
  	    return action.doAction((ActedOnBehalfOf) s);
  	case PROV_DERIVATION:
  	    return action.doAction((WasDerivedFrom) s);
  	case PROV_DICTIONARY_INSERTION:
  	    return action.doAction((DerivedByInsertionFrom) s);
  	case PROV_DICTIONARY_MEMBERSHIP:
  	    return action.doAction((DictionaryMembership) s);
  	case PROV_DICTIONARY_REMOVAL:
  	    return action.doAction((DerivedByRemovalFrom) s);
  	case PROV_END:
  	    return action.doAction((WasEndedBy) s);
  	case PROV_ENTITY:
  	    return action.doAction((Entity) s);
  	case PROV_GENERATION:
  	    return action.doAction((WasGeneratedBy) s);
  	case PROV_INFLUENCE:
  	    return action.doAction((WasInfluencedBy) s);
  	case PROV_INVALIDATION:
  	    return action.doAction((WasInvalidatedBy) s);
  	case PROV_MEMBERSHIP:
  	    return action.doAction((HadMember) s);
  	case PROV_MENTION:
  	    return action.doAction((MentionOf) s);
  	case PROV_SPECIALIZATION:
  	    return action.doAction((SpecializationOf) s);
  	case PROV_START:
  	    return action.doAction((WasStartedBy) s);
  	case PROV_USAGE:
  	    return action.doAction((Used) s);
  	default: 
  	    throw new InvalidCaseException("Statement Kind: " + s.getKind());
  	}
      }

    public static String unescape (String s) {
  	return s.replace("\\\"","\"");
      }
    
    public static String valueToNotationString(org.openprovenance.prov.model.Key key) {
 	return valueToNotationString(key.getValue(), key.getType());
     }

     
     public static String escape (String s) {
   	return s.replace("\"", "\\\"");
       }
     

     //TODO: move this code to ValueConverter
     //TODO: what else should be escaped?
     public static String valueToNotationString(Object val, org.openprovenance.prov.model.QualifiedName xsdType) {
  	if (val instanceof LangString) {
  	    LangString istring = (LangString) val;
  	    return "\"" + istring.getValue() + 
  		    ((istring.getLang()==null) ? "\"" : "\"@" + istring.getLang())
  		    + " %% " + Namespace.qualifiedNameToStringWithNamespace(xsdType);
  	} else if (val instanceof QualifiedName) {
  	    QualifiedName qn = (QualifiedName) val;	    
  	    return "'" + Namespace.qualifiedNameToStringWithNamespace(qn) + "'";
  	} else if (val instanceof String) {
 	    String s=(String)val;
 	    if (s.contains("\n")) {
 		// return "\"\"\"" + val + "\"\"\" %% " + qnameToString(xsdType);
 		return "\"\"\"" + escape(s) + "\"\"\"" ;
 	    } else {
 		return "\"" + escape(s) + "\" %% " + Namespace.qualifiedNameToStringWithNamespace(xsdType);
 	    }
  	} else {
 	    // We should never be here!
  	    return "\"" + val + "\" %% " + Namespace.qualifiedNameToStringWithNamespace(xsdType);
 	}
      }

     
     
     
     
   

     static public boolean hasType(org.openprovenance.prov.model.QualifiedName type, Collection<org.openprovenance.prov.model.Attribute> attributes) {
     	for (org.openprovenance.prov.model.Attribute attribute: attributes) {
     		switch (attribute.getKind()) {
     			case PROV_TYPE :
     				if (attribute.getValue().equals(type)) {
     					return true;
     				}
 					break;			
 				default :
 					break;
     			
     		}
     	}
     	return false;
     		
     }
 


     public Object getter(Statement s, int i)  {
         final Kind kind = s.getKind();
 	switch (kind) {
 	case PROV_ACTIVITY: {
 	    final org.openprovenance.prov.model.Activity a=(org.openprovenance.prov.model.Activity) s;
 	    switch (i) {
 	    case 0: return a.getId();
 	    case 1: return a.getStartTime();
 	    case 2: return a.getEndTime();
 	    case 3: return a.getOther();
 	    default: throw new ArrayIndexOutOfBoundsException("ProvUtilities.getter() for " + kind + " and index " + i);
 	    }
 	}
 	
 	case PROV_AGENT: {
 	    final org.openprovenance.prov.model.Agent a=(org.openprovenance.prov.model.Agent) s;
 	    switch (i) {
 	    case 0: return a.getId();
 	    case 1: return a.getOther();
 	    default: throw new ArrayIndexOutOfBoundsException("ProvUtilities.getter() for " + kind + " and index " + i);
 	    }
 	}
 	case PROV_ALTERNATE: {
 	    final org.openprovenance.prov.model.AlternateOf a=(org.openprovenance.prov.model.AlternateOf) s;
 	    switch (i) {
 	    case 0: return a.getAlternate1();
 	    case 1: return a.getAlternate2();
 	    default: throw new ArrayIndexOutOfBoundsException("ProvUtilities.getter() for " + kind + " and index " + i);
 	    }
 	}
 	case PROV_ASSOCIATION: {
 	    final org.openprovenance.prov.model.WasAssociatedWith a=(org.openprovenance.prov.model.WasAssociatedWith) s;
 	    switch (i) {
 	    case 0: return a.getId();
 	    case 1: return a.getActivity();
 	    case 2: return a.getAgent();
 	    case 3: return a.getPlan();
 	    case 4: return a.getOther();
 	    default: throw new ArrayIndexOutOfBoundsException("ProvUtilities.getter() for " + kind + " and index " + i);
 	    }
 	}
 	case PROV_ATTRIBUTION: {
 	    final org.openprovenance.prov.model.WasAttributedTo a=(org.openprovenance.prov.model.WasAttributedTo) s;
 	    switch (i) {
 	    case 0: return a.getId();
 	    case 1: return a.getEntity();
 	    case 2: return a.getAgent();
 	    case 3: return a.getOther();
 	    default: throw new ArrayIndexOutOfBoundsException("ProvUtilities.getter() for " + kind + " and index " + i);
 	    }
 	}
 	case PROV_BUNDLE:
 	    throw new InvalidCaseException("ProvUtilities.getter() for " + kind);
 	case PROV_COMMUNICATION: {
 	    final org.openprovenance.prov.model.WasInformedBy a=(org.openprovenance.prov.model.WasInformedBy) s;
 	    switch (i) {
 	    case 0: return a.getId();
 	    case 1: return a.getInformed();
 	    case 2: return a.getInformant();
 	    case 3: return a.getOther();
 	    default: throw new ArrayIndexOutOfBoundsException("ProvUtilities.getter() for " + kind + " and index " + i);
 	    }
 	}
 	case PROV_DELEGATION: {
 	    final org.openprovenance.prov.model.ActedOnBehalfOf a=(org.openprovenance.prov.model.ActedOnBehalfOf) s;
 	    switch (i) {
 	    case 0: return a.getId();
 	    case 1: return a.getDelegate();
 	    case 2: return a.getResponsible();
 	    case 3: return a.getActivity();
 	    case 4: return a.getOther();
 	    default: throw new ArrayIndexOutOfBoundsException("ProvUtilities.getter() for " + kind + " and index " + i);
 	    }
 	}
 	case PROV_DERIVATION: {
 	    final org.openprovenance.prov.model.WasDerivedFrom a=(org.openprovenance.prov.model.WasDerivedFrom) s;
 	    switch (i) {
 	    case 0: return a.getId();
 	    case 1: return a.getGeneratedEntity();
 	    case 2: return a.getUsedEntity();
 	    case 3: return a.getActivity();
 	    case 4: return a.getGeneration();
 	    case 5: return a.getUsage();
 	    case 6: return a.getOther();
 	    default: throw new ArrayIndexOutOfBoundsException("ProvUtilities.getter() for " + kind + " and index " + i);
 	    }
 	}
 	case PROV_DICTIONARY_INSERTION:
 	    throw new InvalidCaseException("ProvUtilities.getter() for " + kind);
 	case PROV_DICTIONARY_MEMBERSHIP:
 	    throw new InvalidCaseException("ProvUtilities.getter() for " + kind);
 	case PROV_DICTIONARY_REMOVAL:
 	    throw new InvalidCaseException("ProvUtilities.getter() for " + kind);
 	case PROV_END: {
 	    final org.openprovenance.prov.model.WasEndedBy a=(org.openprovenance.prov.model.WasEndedBy) s;
 	    switch (i) {
 	    case 0: return a.getId();
 	    case 1: return a.getActivity();
 	    case 2: return a.getTrigger();
 	    case 3: return a.getEnder();
 	    case 4: return a.getTime();
 	    case 5: return a.getOther();
 	    default: throw new ArrayIndexOutOfBoundsException("ProvUtilities.getter() for " + kind + " and index " + i);
 	    }
 	}
 	case PROV_ENTITY: {
 	    final org.openprovenance.prov.model.Entity a=(org.openprovenance.prov.model.Entity) s;
 	    switch (i) {
 	    case 0: return a.getId();
 	    case 1: return a.getOther();
 	    default: throw new ArrayIndexOutOfBoundsException("ProvUtilities.getter() for " + kind + " and index " + i);
 	    }
 	}
 	case PROV_GENERATION: {
 	    final org.openprovenance.prov.model.WasGeneratedBy a=(org.openprovenance.prov.model.WasGeneratedBy) s;
 	    switch (i) {
 	    case 0: return a.getId();
 	    case 1: return a.getEntity();
 	    case 2: return a.getActivity();
 	    case 3: return a.getTime();
 	    case 4: return a.getOther();
 	    default: throw new ArrayIndexOutOfBoundsException("ProvUtilities.getter() for " + kind + " and index " + i);
 	    }
 	}
 	case PROV_INFLUENCE: {
 	    final org.openprovenance.prov.model.WasInfluencedBy a=(org.openprovenance.prov.model.WasInfluencedBy) s;
 	    switch (i) {
 	    case 0: return a.getId();
 	    case 1: return a.getInfluencee();
 	    case 2: return a.getInfluencer();
 	    case 3: return a.getOther();
 	    default: throw new ArrayIndexOutOfBoundsException("ProvUtilities.getter() for " + kind + " and index " + i);
 	    }
 	}
 	case PROV_INVALIDATION: {
 	    final org.openprovenance.prov.model.WasInvalidatedBy a=(org.openprovenance.prov.model.WasInvalidatedBy) s;
 	    switch (i) {
 	    case 0: return a.getId();
 	    case 1: return a.getEntity();
 	    case 2: return a.getActivity();
 	    case 3: return a.getTime();
 	    case 4: return a.getOther();
 	    default: throw new ArrayIndexOutOfBoundsException("ProvUtilities.getter() for " + kind + " and index " + i);
 	    }
 	}
 	case PROV_MEMBERSHIP: {
 	    throw new InvalidCaseException("ProvUtilities.setter() for " + kind);
 	}
 	case PROV_MENTION: {
 	    final org.openprovenance.prov.model.MentionOf a=(org.openprovenance.prov.model.MentionOf) s;
 	    switch (i) {
 		//never use the accessor id for Mention, since it is
 		// not defined.  However, this allows iterations over
 		// this data structure to be performed like others.
 	    case 0: throw new ArrayIndexOutOfBoundsException("ProvUtilities.getter() for " + kind + " and index " + i);
 	    case 1: return a.getSpecificEntity();
 	    case 2: return a.getGeneralEntity();
 	    case 3: return a.getBundle();
 	    default: throw new ArrayIndexOutOfBoundsException("ProvUtilities.getter() for " + kind + " and index " + i);
 	    }
 	}
 	case PROV_SPECIALIZATION: {
 	    final org.openprovenance.prov.model.SpecializationOf a=(org.openprovenance.prov.model.SpecializationOf) s;
 	    switch (i) {
 	    case 0: return a.getSpecificEntity();
 	    case 1: return a.getGeneralEntity();
 	    default: throw new ArrayIndexOutOfBoundsException("ProvUtilities.getter() for " + kind + " and index " + i);
 	    }
 	}
 	case PROV_START: {
 	    final org.openprovenance.prov.model.WasStartedBy a=(org.openprovenance.prov.model.WasStartedBy) s;
 	    switch (i) {
 	    case 0: return a.getId();
 	    case 1: return a.getActivity();
 	    case 2: return a.getTrigger();
 	    case 3: return a.getStarter();
 	    case 4: return a.getTime();
 	    case 5: return a.getOther();
 	    default: throw new ArrayIndexOutOfBoundsException("ProvUtilities.getter() for " + kind + " and index " + i);
 	    }
 	}
 	case PROV_USAGE: {
 	    final org.openprovenance.prov.model.Used a=(org.openprovenance.prov.model.Used) s;
 	    switch (i) {
 	    case 0: return a.getId();
 	    case 1: return a.getActivity();
 	    case 2: return a.getEntity();
 	    case 3: return a.getTime();
 	    case 4: return a.getOther();
 	    default: throw new ArrayIndexOutOfBoundsException("ProvUtilities.getter() for " + kind + " and index " + i);
 	    }
 	}
 	default:
 	    throw new InvalidCaseException("ProvUtilities.getter() for " + kind);     
         }
     }

     public void setter(Statement s, int i, Object val)  {
         final Kind kind = s.getKind();
 	switch (kind) {
 	case PROV_ACTIVITY: {
 	    final org.openprovenance.prov.model.Activity a=(org.openprovenance.prov.model.Activity) s;
 	    switch (i) {
 	    case 0: a.setId((QualifiedName)val); return;
 	    case 1: a.setStartTime((XMLGregorianCalendar)val); return;
 	    case 2: a.setEndTime((XMLGregorianCalendar)val); return;
 	    default: throw new ArrayIndexOutOfBoundsException("ProvUtilities.setter() for " + kind + " and index " + i);
 	    }
 	}
 	
 	case PROV_AGENT: {
 	    final org.openprovenance.prov.model.Agent a=(org.openprovenance.prov.model.Agent) s;
 	    switch (i) {
 	    case 0: a.setId((QualifiedName)val); return;
 	    default: throw new ArrayIndexOutOfBoundsException("ProvUtilities.setter() for " + kind + " and index " + i);
 	    }
 	}
 	case PROV_ALTERNATE: {
 	    final org.openprovenance.prov.model.AlternateOf a=(org.openprovenance.prov.model.AlternateOf) s;
 	    switch (i) {
 	    case 0: a.setAlternate1((QualifiedName)val); return;
 	    case 1: a.setAlternate2((QualifiedName)val); return;
 	    default: throw new ArrayIndexOutOfBoundsException("ProvUtilities.setter() for " + kind + " and index " + i);
 	    }
 	}
 	case PROV_ASSOCIATION: {
 	    final org.openprovenance.prov.model.WasAssociatedWith a=(org.openprovenance.prov.model.WasAssociatedWith) s;
 	    switch (i) {
 	    case 0: a.setId((QualifiedName)val); return;
 	    case 1: a.setActivity((QualifiedName)val); return;
 	    case 2: a.setAgent((QualifiedName)val); return;
 	    case 3: a.setPlan((QualifiedName)val); return;
 	    default: throw new ArrayIndexOutOfBoundsException("ProvUtilities.setter() for " + kind + " and index " + i);
 	    }
 	}
 	case PROV_ATTRIBUTION: {
 	    final org.openprovenance.prov.model.WasAttributedTo a=(org.openprovenance.prov.model.WasAttributedTo) s;
 	    switch (i) {
 	    case 0: a.setId((QualifiedName)val); return;
 	    case 1: a.setEntity((QualifiedName)val); return;
 	    case 2: a.setAgent((QualifiedName)val); return;
 	    default: throw new ArrayIndexOutOfBoundsException("ProvUtilities.setter() for " + kind + " and index " + i);
 	    }
 	}
 	case PROV_BUNDLE:
 	    throw new InvalidCaseException("ProvUtilities.setter() for " + kind);
 	case PROV_COMMUNICATION: {
 	    final org.openprovenance.prov.model.WasInformedBy a=(org.openprovenance.prov.model.WasInformedBy) s;
 	    switch (i) {
 	    case 0: a.setId((QualifiedName)val); return;
 	    case 1: a.setInformed((QualifiedName)val); return;
 	    case 2: a.setInformant((QualifiedName)val); return;
 	    default: throw new ArrayIndexOutOfBoundsException("ProvUtilities.setter() for " + kind + " and index " + i);
 	    }
 	}
 	case PROV_DELEGATION: {
 	    final org.openprovenance.prov.model.ActedOnBehalfOf a=(org.openprovenance.prov.model.ActedOnBehalfOf) s;
 	    switch (i) {
 	    case 0: a.setId((QualifiedName)val); return;
 	    case 1: a.setDelegate((QualifiedName)val); return;
 	    case 2: a.setResponsible((QualifiedName)val); return;
 	    case 3: a.setActivity((QualifiedName)val); return;
 	    default: throw new ArrayIndexOutOfBoundsException("ProvUtilities.setter() for " + kind + " and index " + i);
 	    }
 	}
 	case PROV_DERIVATION: {
 	    final org.openprovenance.prov.model.WasDerivedFrom a=(org.openprovenance.prov.model.WasDerivedFrom) s;
 	    switch (i) {
 	    case 0: a.setId((QualifiedName)val); return;
 	    case 1: a.setGeneratedEntity((QualifiedName)val); return;
 	    case 2: a.setUsedEntity((QualifiedName)val); return;
 	    case 3: a.setActivity((QualifiedName)val); return;
 	    case 4: a.setGeneration((QualifiedName)val); return;
 	    case 5: a.setUsage((QualifiedName)val); return;
 	    default: throw new ArrayIndexOutOfBoundsException("ProvUtilities.setter() for " + kind + " and index " + i);
 	    }
 	}
 	case PROV_DICTIONARY_INSERTION:
 	    throw new InvalidCaseException("ProvUtilities.setter() for " + kind);
 	case PROV_DICTIONARY_MEMBERSHIP:
 	    throw new InvalidCaseException("ProvUtilities.setter() for " + kind);
 	case PROV_DICTIONARY_REMOVAL:
 	    throw new InvalidCaseException("ProvUtilities.setter() for " + kind);
 	case PROV_END: {
 	    final org.openprovenance.prov.model.WasEndedBy a=(org.openprovenance.prov.model.WasEndedBy) s;
 	    switch (i) {
 	    case 0: a.setId((QualifiedName)val); return;
 	    case 1: a.setActivity((QualifiedName)val); return;
 	    case 2: a.setTrigger((QualifiedName)val); return;
 	    case 3: a.setEnder((QualifiedName)val); return;
 	    case 4: a.setTime((XMLGregorianCalendar)val); return;
 	    default: throw new ArrayIndexOutOfBoundsException("ProvUtilities.setter() for " + kind + " and index " + i);
 	    }
 	}
 	case PROV_ENTITY: {
 	    final org.openprovenance.prov.model.Entity a=(org.openprovenance.prov.model.Entity) s;
 	    switch (i) {
 	    case 0: a.setId((QualifiedName)val); return;
 	    default: throw new ArrayIndexOutOfBoundsException("ProvUtilities.setter() for " + kind + " and index " + i);
 	    }
 	}
 	case PROV_GENERATION: {
 	    final org.openprovenance.prov.model.WasGeneratedBy a=(org.openprovenance.prov.model.WasGeneratedBy) s;
 	    switch (i) {
 	    case 0: a.setId((QualifiedName)val); return;
 	    case 1: a.setEntity((QualifiedName)val); return;
 	    case 2: a.setActivity((QualifiedName)val); return;
 	    case 3: a.setTime((XMLGregorianCalendar)val); return;
 	    default: throw new ArrayIndexOutOfBoundsException("ProvUtilities.setter() for " + kind + " and index " + i);
 	    }
 	}
 	case PROV_INFLUENCE: {
 	    final org.openprovenance.prov.model.WasInfluencedBy a=(org.openprovenance.prov.model.WasInfluencedBy) s;
 	    switch (i) {
 	    case 0: a.setId((QualifiedName)val); return;
 	    case 1: a.setInfluencee((QualifiedName)val); return;
 	    case 2: a.setInfluencer((QualifiedName)val); return;
 	    default: throw new ArrayIndexOutOfBoundsException("ProvUtilities.setter() for " + kind + " and index " + i);
 	    }
 	}
 	case PROV_INVALIDATION: {
 	    final org.openprovenance.prov.model.WasInvalidatedBy a=(org.openprovenance.prov.model.WasInvalidatedBy) s;
 	    switch (i) {
 	    case 0: a.setId((QualifiedName)val); return;
 	    case 1: a.setEntity((QualifiedName)val); return;
 	    case 2: a.setActivity((QualifiedName)val); return;
 	    case 3: a.setTime((XMLGregorianCalendar)val); return;
 	    default: throw new ArrayIndexOutOfBoundsException("ProvUtilities.setter() for " + kind + " and index " + i);
 	    }
 	}
 	case PROV_MEMBERSHIP: {
 	    throw new InvalidCaseException("ProvUtilities.setter() for " + kind);
 	}
 	case PROV_MENTION: {
 	    final org.openprovenance.prov.model.MentionOf a=(org.openprovenance.prov.model.MentionOf) s;
 	    switch (i) {
 		//never use the accessor id for Mention, since it is
 		// not defined.  However, this allows iterations over
 		// this data structure to be performed like others.
 	    case 0: throw new ArrayIndexOutOfBoundsException("ProvUtilities.setter() for " + kind + " and index " + i);
 	    case 1: a.setSpecificEntity((QualifiedName)val); return;
 	    case 2: a.setGeneralEntity((QualifiedName)val); return;
 	    case 3: a.setBundle((QualifiedName)val); return;
 	    default: throw new ArrayIndexOutOfBoundsException("ProvUtilities.setter() for " + kind + " and index " + i);
 	    }
 	}
 	case PROV_SPECIALIZATION: {
 	    final org.openprovenance.prov.model.SpecializationOf a=(org.openprovenance.prov.model.SpecializationOf) s;
 	    switch (i) {
 	    case 0: a.setSpecificEntity((QualifiedName)val); return;
 	    case 1: a.setGeneralEntity((QualifiedName)val); return;
 	    default: throw new ArrayIndexOutOfBoundsException("ProvUtilities.setter() for " + kind + " and index " + i);
 	    }
 	}
 	case PROV_START: {
 	    final org.openprovenance.prov.model.WasStartedBy a=(org.openprovenance.prov.model.WasStartedBy) s;
 	    switch (i) {
 	    case 0: a.setId((QualifiedName)val); return;
 	    case 1: a.setActivity((QualifiedName)val); return;
 	    case 2: a.setTrigger((QualifiedName)val); return;
 	    case 3: a.setStarter((QualifiedName)val); return;
 	    case 4: a.setTime((XMLGregorianCalendar)val); return;
 	    default: throw new ArrayIndexOutOfBoundsException("ProvUtilities.setter() for " + kind + " and index " + i);
 	    }
 	}
 	case PROV_USAGE: {
 	    final org.openprovenance.prov.model.Used a=(org.openprovenance.prov.model.Used) s;
 	    switch (i) {
 	    case 0: a.setId((QualifiedName)val); return;
 	    case 1: a.setActivity((QualifiedName)val); return;
 	    case 2: a.setEntity((QualifiedName)val); return;
 	    case 3: a.setTime((XMLGregorianCalendar)val); return;
 	    default: throw new ArrayIndexOutOfBoundsException("ProvUtilities.setter() for " + kind + " and index " + i);
 	    }
 	}
 	default:
 	    throw new InvalidCaseException("ProvUtilities.setter() for " + kind);     
         }
     }

     /**
      * Indicates whether object has no time field.
      * 
      */

     public boolean hasNoTime(Statement o) {	 
	 if (o instanceof HasTime) return false;
	 if (o instanceof Activity) return false;
	 return true;
		 /* OLD Definition
         return (o instanceof WasDerivedFrom) 
        	 || (o instanceof ActedOnBehalfOf)
                 || (o instanceof WasInformedBy)
                 || (o instanceof WasAttributedTo)
                 || (o instanceof WasAssociatedWith)
                 || (o instanceof WasInfluencedBy)
                 || (o instanceof SpecializationOf)
                 || (o instanceof AlternateOf)
                 || (o instanceof MentionOf)
                 || (o instanceof HadMember);
                 */
     } 
   

}
