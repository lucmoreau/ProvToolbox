package org.openprovenance.prov.model;

import java.util.List;
import java.util.LinkedList;

import org.openprovenance.prov.model.extension.QualifiedSpecializationOf;


/** Generic Traversal of a PROV model bean.
 * Makes use of the "visitor" in {@link ProvUtilities#doAction(StatementOrBundle, StatementActionValue)}
 * 
 * @author lavm
 *
 */
public class BeanTraversal implements StatementActionValue {
    final private ModelConstructor c;
    final private ProvFactory pFactory;
    ProvUtilities u=new ProvUtilities();

    public BeanTraversal(ModelConstructor c, ProvFactory pFactory) {
	this.c=c;
	this.pFactory=pFactory;
    }
   

    public ActedOnBehalfOf doAction(ActedOnBehalfOf del) {
        List<Attribute> attrs=new LinkedList<Attribute>();      
        convertTypeAttributes(del,attrs);
        convertLabelAttributes(del,attrs);
        convertAttributes(del,attrs);
        return c.newActedOnBehalfOf(del.getId(), del.getDelegate(), del.getResponsible(), del.getActivity(), attrs);
    }

    
    public Activity doAction(Activity e) {
	List<Attribute> attrs=new LinkedList<Attribute>();	
	convertTypeAttributes(e,attrs);
	convertLabelAttributes(e,attrs);
	convertLocationAttributes(e,attrs);	
	convertAttributes(e,attrs);
	return c.newActivity(e.getId(), e.getStartTime(), e.getEndTime(), attrs);
    }

   
    public Agent doAction(Agent e) {
	List<Attribute> attrs=new LinkedList<Attribute>();	
	convertTypeAttributes(e,attrs);
	convertLabelAttributes(e,attrs);
	convertLocationAttributes(e,attrs);	
	convertAttributes(e,attrs);
	return c.newAgent(e.getId(), attrs);
    }

    public AlternateOf doAction(AlternateOf o) {
        return c.newAlternateOf(o.getAlternate1(), o.getAlternate2());
    }
    
    public Relation doAction(DerivedByInsertionFrom o) {
    	List<Attribute> attrs=new LinkedList<Attribute>();      
        convertTypeAttributes(o,attrs);
        convertLabelAttributes(o,attrs);
        convertAttributes(o,attrs);
    	
	return c.newDerivedByInsertionFrom(o.getId(), 
	                                   o.getNewDictionary(), 
	                                   o.getOldDictionary(), 
	                                   o.getKeyEntityPair(), 
	                                   attrs);

	
    }
    
    public Relation doAction(DerivedByRemovalFrom o) {
    	List<Attribute> attrs=new LinkedList<Attribute>();      
        convertTypeAttributes(o,attrs);
        convertLabelAttributes(o,attrs);
        convertAttributes(o,attrs);
    	
	
	return c.newDerivedByRemovalFrom(o.getId(), 
	                                 o.getNewDictionary(), 
	                                 o.getOldDictionary(), 
	                                 o.getKey(), 
	                                 attrs);
	                                 
    }


    public Relation doAction(DictionaryMembership o) {
	return c.newDictionaryMembership(o.getDictionary(),  o.getKeyEntityPair());
    }

    public Document doAction(Document doc) {

	List<Bundle> bRecords = new LinkedList<Bundle>();

	List<Statement> sRecords = new LinkedList<Statement>();
	
	Namespace docNamespace=doc.getNamespace();
        Namespace.withThreadNamespace(docNamespace);

        c.startDocument(doc.getNamespace());

	for (Statement s : u.getStatement(doc)) {
	    sRecords.add((Statement) u.doAction(s, this));
	}

	for (Bundle bu : u.getNamedBundle(doc)) {
	    Namespace.withThreadNamespace(new Namespace(docNamespace));
	    Bundle o = doAction(bu,u);
	    if (o != null)
		bRecords.add(o);

	}
	return c.newDocument(doc.getNamespace(), sRecords, bRecords);
    }

       

    public Entity doAction(Entity e) {
	List<Attribute> attrs=new LinkedList<Attribute>();	
	convertTypeAttributes(e,attrs);
	convertLabelAttributes(e,attrs);
	convertLocationAttributes(e,attrs);	
	convertValueAttributes(e,attrs);
	convertAttributes(e,attrs);
	return c.newEntity(e.getId(), attrs);
    }

    //TODO: only supporting one member in the relation
    // note: lots of test to support scruffy provenance
    public HadMember doAction(HadMember o) {
        List<QualifiedName> qq=new LinkedList<QualifiedName>();
        if (o.getEntity()!=null) {
            for (QualifiedName eid:o.getEntity()) {
                qq.add(eid);
            }
        }
        return c.newHadMember(o.getCollection(), qq);
        
       /*remember, scruffy hadmember in provn, has a null in get(0)!!
	return deprecated.convertHadMember(deprecated.doAction((o.getCollection()==null) ? null: 	                                    
	                                      o.getCollection().getRef()),
	                          ((o.getEntity()==null || (o.getEntity().isEmpty())) ?
	                        	  deprecated.doAction(null) :
	                        	  deprecated.doAction((o.getEntity().get(0)==null) ? 
	                        		     null :
	                        	             o.getEntity().get(0).getRef()))); */
    }
    

    public MentionOf doAction(MentionOf o) {
        return c.newMentionOf(o.getSpecificEntity(),
                              o.getGeneralEntity(),
                              o.getBundle());
    }

    public Bundle doAction(Bundle b, ProvUtilities u) {
	List<Statement> sRecords = new LinkedList<Statement>();
	QualifiedName bundleId=b.getId();
	
	     
	Namespace old=Namespace.getThreadNamespace();
	Namespace bundleNamespace;
	if (b.getNamespace()!=null) {
	    bundleNamespace=new Namespace(b.getNamespace());
	} else {
	    bundleNamespace=new Namespace();
	}
	bundleNamespace.setParent(new Namespace(old)); //ensure to make a copy of old, since setting might otherwise create a loop
	Namespace.withThreadNamespace(bundleNamespace);


        c.startBundle(bundleId, b.getNamespace());

        
	for (Statement s : u.getStatement(b)) {
	    sRecords.add((Statement) u.doAction(s, this));

	}
	return c.newNamedBundle(bundleId, b.getNamespace(), sRecords);
    }

 

    public SpecializationOf doAction(SpecializationOf o) {
        return c.newSpecializationOf(o.getSpecificEntity(), o.getGeneralEntity());
    }
    

    public SpecializationOf doAction(QualifiedSpecializationOf o) {
        List<Attribute> attrs=new LinkedList<Attribute>();  
        convertTypeAttributes(o,attrs);
        convertLabelAttributes(o,attrs);
        convertAttributes(o,attrs);
        ModelConstructorExtension c2=(ModelConstructorExtension)c;
        return c2.newQualifiedSpecializationOf(o.getId(), o.getSpecificEntity(), o.getGeneralEntity(), attrs);
    }
    
    public Used doAction(Used use) {
	List<Attribute> attrs=new LinkedList<Attribute>();	
	convertTypeAttributes(use,attrs);
	convertLabelAttributes(use,attrs);
	convertLocationAttributes(use,attrs);	
 	convertRoleAttributes(use,attrs);
	convertAttributes(use,attrs);
	return c.newUsed(use.getId(), use.getActivity(), use.getEntity(), use.getTime(), attrs);
    }

    
    public WasAssociatedWith doAction(WasAssociatedWith assoc) {
        List<Attribute> attrs=new LinkedList<Attribute>();      
        convertTypeAttributes(assoc,attrs);
        convertLabelAttributes(assoc,attrs);
        convertRoleAttributes(assoc,attrs);
        convertAttributes(assoc,attrs);
 	return c.newWasAssociatedWith(assoc.getId(),
 	                              assoc.getActivity(), 
 	                              assoc.getAgent(), 
 	                              assoc.getPlan(), 
 	                              attrs);
    }

    public WasAttributedTo doAction(WasAttributedTo att) {
        List<Attribute> attrs=new LinkedList<Attribute>();      
        convertTypeAttributes(att,attrs);
        convertLabelAttributes(att,attrs);
        convertAttributes(att,attrs);
        return c.newWasAttributedTo(att.getId(), att.getEntity(), att.getAgent(), attrs);
    }

    public WasDerivedFrom doAction(WasDerivedFrom deriv) {
	List<Attribute> attrs=new LinkedList<Attribute>();	
 	convertTypeAttributes(deriv,attrs);
 	convertLabelAttributes(deriv,attrs);
 	convertAttributes(deriv,attrs);
 	return c.newWasDerivedFrom(deriv.getId(), 
 	                           deriv.getGeneratedEntity(), 
 	                           deriv.getUsedEntity(), 
 	                           deriv.getActivity(), 
 	                           deriv.getGeneration(), 
 	                           deriv.getUsage(), 
 	                           attrs);
    }


   


    public WasEndedBy doAction(WasEndedBy end) {
        List<Attribute> attrs=new LinkedList<Attribute>();    
        convertTypeAttributes(end,attrs);
        convertLabelAttributes(end,attrs);
        convertLocationAttributes(end,attrs);   
        convertRoleAttributes(end,attrs);
        convertAttributes(end,attrs);
        return c.newWasEndedBy(end.getId(), end.getActivity(), end.getTrigger(), end.getEnder(), end.getTime(), attrs);
     }

    public WasGeneratedBy doAction(WasGeneratedBy gen) {
 	List<Attribute> attrs=new LinkedList<Attribute>();	
 	convertTypeAttributes(gen,attrs);
 	convertLabelAttributes(gen,attrs);
 	convertLocationAttributes(gen,attrs);	
 	convertRoleAttributes(gen,attrs);
 	convertAttributes(gen,attrs);
 	return c.newWasGeneratedBy(gen.getId(), gen.getEntity(), gen.getActivity(), gen.getTime(), attrs);
     }

    public WasInfluencedBy doAction(WasInfluencedBy infl) {
        List<Attribute> attrs=new LinkedList<Attribute>();      
        convertTypeAttributes(infl,attrs);
        convertLabelAttributes(infl,attrs);
        convertAttributes(infl,attrs);
        return c.newWasInfluencedBy(infl.getId(), infl.getInfluencee(), infl.getInfluencer(), attrs);
    }

    public WasInformedBy doAction(WasInformedBy inf) {
        List<Attribute> attrs=new LinkedList<Attribute>();      
        convertTypeAttributes(inf,attrs);
        convertLabelAttributes(inf,attrs);
        convertAttributes(inf,attrs);
        return c.newWasInformedBy(inf.getId(), inf.getInformed(), inf.getInformant(), attrs);
    }

    public WasInvalidatedBy doAction(WasInvalidatedBy inv) {
 	List<Attribute> attrs=new LinkedList<Attribute>();	
 	convertTypeAttributes(inv,attrs);
 	convertLabelAttributes(inv,attrs);
 	convertLocationAttributes(inv,attrs);	
 	convertRoleAttributes(inv,attrs);
 	convertAttributes(inv,attrs);
 	return c.newWasInvalidatedBy(inv.getId(), inv.getEntity(), inv.getActivity(), inv.getTime(), attrs);
     }

    public WasStartedBy doAction(WasStartedBy start) {
 	List<Attribute> attrs=new LinkedList<Attribute>();	
 	convertTypeAttributes(start,attrs);
 	convertLabelAttributes(start,attrs);
 	convertLocationAttributes(start,attrs);	
 	convertRoleAttributes(start,attrs);
 	convertAttributes(start,attrs);
 	return c.newWasStartedBy(start.getId(), start.getActivity(), start.getTrigger(), start.getStarter(), start.getTime(), attrs);
     }

    @SuppressWarnings("unchecked")
    public List<Attribute> convertAttributes(HasOther e, List<Attribute> acc) {
	@SuppressWarnings("rawtypes")
	List ll=e.getOther();
	acc.addAll((List<Attribute>)ll);
	return acc;
    }

    public List<Attribute> convertLabelAttributes(HasLabel e, List<Attribute> acc) {
   	List<LangString> labels = e.getLabel();
   	for (LangString label : labels) {
   	    acc.add(pFactory.newAttribute(Attribute.AttributeKind.PROV_LABEL,label, pFactory.getName().XSD_STRING));
   	}
   	return acc;
    }

    public List<Attribute> convertLocationAttributes(HasLocation e, List<Attribute> acc) {
        List<Location> locations = e.getLocation();
        for (Location location : locations) {
            acc.add(pFactory.newAttribute(Attribute.AttributeKind.PROV_LOCATION,
                                          location.getValue(), 
                                          location.getType()));
        }
        return acc;
    }
    

    public List<Attribute> convertRoleAttributes(HasRole e, List<Attribute> acc) {
   	List<Role> roles = e.getRole();
   	for (Role role : roles) {
   	    acc.add(pFactory.newAttribute(Attribute.AttributeKind.PROV_ROLE,
   	                                  role.getValue(), 
   	                                  role.getType()));
   	}
   	return acc;
    }

    public List<Attribute> convertTypeAttributes(HasType e, List<Attribute> acc) {
	List<Type> types=e.getType();
	for (Type type : types) {
	    acc.add(pFactory.newAttribute(Attribute.AttributeKind.PROV_TYPE, 
	                                  type.getValue(),
	                                  //type.getValueAsObject(vconv), 
	                                  type.getType()));
	}
	return acc;
    }
    

    public Object convertValueAttributes(HasValue e, List<Attribute> acc) {
        Value value = e.getValue();
        if (value==null) return acc;
        acc.add(pFactory.newAttribute(Attribute.AttributeKind.PROV_VALUE,
                                      value.getValue(), 
                                      value.getType()));
        return acc;     
    }

    
	
}
