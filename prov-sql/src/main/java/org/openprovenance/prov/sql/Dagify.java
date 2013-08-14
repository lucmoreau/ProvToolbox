package org.openprovenance.prov.sql;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.xml.namespace.QName;

import org.openprovenance.prov.model.Identifiable;
import org.openprovenance.prov.model.Statement;




public class Dagify implements RecordAction {

    private EntityManager em;


    public Dagify(EntityManager em) {
        this.em=em;
    }
    
    Hashtable<String, org.openprovenance.prov.model.IDRef> table=new Hashtable<String, org.openprovenance.prov.model.IDRef>();
    
    public org.openprovenance.prov.model.IDRef uniquify(org.openprovenance.prov.model.IDRef ref) {
        if (ref==null) return null;
        QName q=ref.getRef();
        String uri=q.getNamespaceURI()+q.getLocalPart();
        org.openprovenance.prov.model.IDRef found=table.get(uri);
        if (found!=null) {
            return found;
        }
        Query qq=em.createQuery("SELECT e FROM IDRef e WHERE e.uri LIKE :uri");
        qq.setParameter("uri", uri);
        List<IDRef> ll=(List<IDRef>) qq.getResultList();
        //System.out.println("found ll " + ll);
        
        org.openprovenance.prov.model.IDRef newId=ref;
        if ((ll!=null) && (!(ll.isEmpty()))) {
            newId=ll.get(0);
        }
        table.put(uri, newId);
        return newId;
    }

   
    /*
    public IDRef createKey(QName id) {
	IDRef ref=new IDRef();
	ref.setRef(id);
  	return uniquify(ref);
      }

   */
    
    public IDRef createKey(Identifiable e) {
	IDRef ref=new IDRef();
	ref.setRef(e.getId());
  	return (IDRef)uniquify(ref);
      }




    public void run(Entity e) {
	e.setIdRef(createKey(e));
    }

    
    public void run(Activity a) {
	a.setIdRef(createKey(a));        
    }

  




    public void run(Agent ag) {
	ag.setIdRef(createKey(ag));                
    }

    public void run(WasGeneratedBy gen) {
	if (gen.getId()!=null) gen.setIdRef(createKey(gen));                
        gen.setEntity(uniquify(gen.getEntity()));
        gen.setActivity(uniquify(gen.getActivity()));
    }

    public void run(Used use) {
	if (use.getId()!=null) use.setIdRef(createKey(use));                
        use.setEntity(uniquify(use.getEntity()));
        use.setActivity(uniquify(use.getActivity()));      
    }

    public void run(WasInvalidatedBy inv) {
	if (inv.getId()!=null) inv.setIdRef(createKey(inv));                
        inv.setEntity(uniquify(inv.getEntity()));
        inv.setActivity(uniquify(inv.getActivity()));          
    }

    public void run(WasStartedBy start) {
	if (start.getId()!=null) start.setIdRef(createKey(start));                
        start.setActivity(uniquify(start.getActivity()));    
        start.setTrigger(uniquify(start.getTrigger()));
        start.setStarter(uniquify(start.getStarter()));                  
    }

    public void run(WasEndedBy end) {
	if (end.getId()!=null) end.setIdRef(createKey(end));                
        end.setActivity(uniquify(end.getActivity()));    
        end.setTrigger(uniquify(end.getTrigger()));
        end.setEnder(uniquify(end.getEnder()));          
      
    }

    public void run(WasInformedBy inf) {
	if (inf.getId()!=null) inf.setIdRef(createKey(inf));                
        inf.setInformant(uniquify(inf.getInformant()));    
        inf.setInformed(uniquify(inf.getInformed()));    
    }

    public void run(WasDerivedFrom der) {
	if (der.getId()!=null) der.setIdRef(createKey(der));                
        der.setGeneratedEntity(uniquify(der.getGeneratedEntity()));    
        der.setUsedEntity(uniquify(der.getUsedEntity()));    
        der.setActivity(uniquify(der.getActivity()));    
        der.setGeneration(uniquify(der.getGeneration()));    
        der.setUsage(uniquify(der.getUsage()));           
    }

    public void run(WasAssociatedWith assoc) {
	if (assoc.getId()!=null) assoc.setIdRef(createKey(assoc));                
        assoc.setActivity(uniquify(assoc.getActivity()));    
        assoc.setAgent(uniquify(assoc.getAgent()));    
        assoc.setPlan(uniquify(assoc.getPlan()));    
    }

    public void run(WasAttributedTo attr) {
	if (attr.getId()!=null) attr.setIdRef(createKey(attr));                
        attr.setAgent(uniquify(attr.getAgent()));    
        attr.setEntity(uniquify(attr.getEntity()));
    }

    public void run(ActedOnBehalfOf del) {
	if (del.getId()!=null) del.setIdRef(createKey(del));                
        del.setDelegate(uniquify(del.getDelegate()));    
        del.setResponsible(uniquify(del.getResponsible()));    
        del.setActivity(uniquify(del.getActivity()));            
    }

    public void run(WasInfluencedBy inf) {
	if (inf.getId()!=null) inf.setIdRef(createKey(inf));                
        inf.setInfluencee(uniquify(inf.getInfluencee()));
        inf.setInfluencer(uniquify(inf.getInfluencer()));
    }

    public void run(AlternateOf alt) {
        alt.setAlternate1(uniquify(alt.getAlternate1()));
        alt.setAlternate2(uniquify(alt.getAlternate2()));
    }

    public void run(MentionOf men) {
        men.setBundle(uniquify(men.getBundle()));
        men.setGeneralEntity(uniquify(men.getGeneralEntity()));
        men.setSpecificEntity(uniquify(men.getSpecificEntity()));       
    }

    public void run(SpecializationOf spec) {
        spec.setSpecificEntity(uniquify(spec.getSpecificEntity()));
        spec.setGeneralEntity(uniquify(spec.getGeneralEntity()));
    }

    public void run(HadMember mem) {
        List<org.openprovenance.prov.model.IDRef> ll=new LinkedList<org.openprovenance.prov.model.IDRef>();
        for (org.openprovenance.prov.model.IDRef er: mem.getEntity()) {
            ll.add(uniquify(er));
        }
        mem.getEntity().clear();
        mem.getEntity().addAll(ll);
        
        mem.setCollection(uniquify(mem.getCollection()));        
    }
    
    // should be in utility class
    
    public void forAllRecords(List<Statement> records, RecordAction action) {
        for (Statement o : records) {
            run(o, action);
        }
    }

    static public void run(Statement o, RecordAction action) {

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
    


}
