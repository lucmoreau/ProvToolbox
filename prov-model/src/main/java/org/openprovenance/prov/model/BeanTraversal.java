package org.openprovenance.prov.model;

import java.util.List;
import java.util.LinkedList;


public class BeanTraversal {
    final private ModelConstructor c;
    final private ProvFactory pFactory;
    ProvUtilities u=new ProvUtilities();

    public BeanTraversal(ModelConstructor c, ProvFactory pFactory) {
	this.c=c;
	this.pFactory=pFactory;
    }

    public Document convert(Document doc) {

	List<NamedBundle> bRecords = new LinkedList<NamedBundle>();

	List<Statement> sRecords = new LinkedList<Statement>();
	
        c.startDocument(doc.getNamespace());

	for (Statement s : u.getStatement(doc)) {
	    if (s instanceof Entity) {
		sRecords.add(convert((Entity)s));
	    } else if (s instanceof Activity) {
		sRecords.add(convert((Activity)s));
	    } else if (s instanceof Agent) {
		sRecords.add(convert((Agent)s));
	    } else {
		sRecords.add(convertRelation((Relation0)s));
	    }

	}

	for (NamedBundle bu : u.getNamedBundle(doc)) {
	    NamedBundle o = convert(bu);
	    if (o != null)
		bRecords.add(o);

	}
	return c.newDocument(doc.getNamespace(), sRecords, bRecords);
    }

    
    public NamedBundle convert(NamedBundle b) {
	List<Statement> sRecords = new LinkedList<Statement>();
	QualifiedName bundleId=b.getId();
        c.startBundle(bundleId, b.getNamespace());

	for (Statement s : u.getStatement(b)) {
	    if (s instanceof Entity) {
		sRecords.add(convert((Entity)s));
	    } else if (s instanceof Activity) {
		sRecords.add(convert((Activity)s));
	    } else if (s instanceof Agent) {
		sRecords.add(convert((Agent)s));
	    } else {
		sRecords.add(convertRelation((Relation0)s));
	    }

	}
	return c.newNamedBundle(bundleId, b.getNamespace(), sRecords);
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

    public List<Attribute> convertLabelAttributes(HasLabel e, List<Attribute> acc) {
   	List<LangString> labels = e.getLabel();
   	for (LangString label : labels) {
   	    acc.add(pFactory.newAttribute(Attribute.AttributeKind.PROV_LABEL,label, pFactory.getName().XSD_STRING));
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
    
    public List<Attribute> convertLocationAttributes(HasLocation e, List<Attribute> acc) {
        List<Location> locations = e.getLocation();
        for (Location location : locations) {
            acc.add(pFactory.newAttribute(Attribute.AttributeKind.PROV_LOCATION,
                                          location.getValue(), 
                                          location.getType()));
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

    @SuppressWarnings("unchecked")
    public List<Attribute> convertAttributes(HasOther e, List<Attribute> acc) {
	@SuppressWarnings("rawtypes")
	List ll=e.getOther();
	acc.addAll((List<Attribute>)ll);
	return acc;
    }

       

    public Entity convert(Entity e) {
	List<Attribute> attrs=new LinkedList<Attribute>();	
	convertTypeAttributes(e,attrs);
	convertLabelAttributes(e,attrs);
	convertLocationAttributes(e,attrs);	
	convertValueAttributes(e,attrs);
	convertAttributes(e,attrs);
	return c.newEntity(e.getId(), attrs);
    }

    public Activity convert(Activity e) {
	List<Attribute> attrs=new LinkedList<Attribute>();	
	convertTypeAttributes(e,attrs);
	convertLabelAttributes(e,attrs);
	convertLocationAttributes(e,attrs);	
	convertAttributes(e,attrs);
	return c.newActivity(e.getId(), e.getStartTime(), e.getEndTime(), attrs);
    }
    

    public Agent convert(Agent e) {
	List<Attribute> attrs=new LinkedList<Attribute>();	
	convertTypeAttributes(e,attrs);
	convertLabelAttributes(e,attrs);
	convertLocationAttributes(e,attrs);	
	convertAttributes(e,attrs);
	return c.newAgent(e.getId(), attrs);
    }

    public Relation0 convertRelation(Relation0 o) {
	// no visitors, so ...
	if (o instanceof Used) {
	    return convert((Used) o);
	} else if (o instanceof WasInformedBy) {
	    return convert((WasInformedBy) o);
	} else if (o instanceof WasInfluencedBy) {
	    return convert((WasInfluencedBy) o);
	} else if (o instanceof WasGeneratedBy) {
	    return convert((WasGeneratedBy) o);
	} else if (o instanceof WasInvalidatedBy) {
	    return convert((WasInvalidatedBy) o);
	} else if (o instanceof WasStartedBy) {
	    return convert((WasStartedBy) o);
	} else if (o instanceof WasEndedBy) {
	    return convert((WasEndedBy) o);

	} else if (o instanceof WasDerivedFrom) {
	    return convert((WasDerivedFrom) o);

	} else if (o instanceof WasAttributedTo) {
	    return convert((WasAttributedTo) o);
	} else if (o instanceof WasAssociatedWith) {
	    return convert((WasAssociatedWith) o);
	} else if (o instanceof ActedOnBehalfOf) {
	    return convert((ActedOnBehalfOf) o);

	} else if (o instanceof AlternateOf) {
	    return convert((AlternateOf) o);
	} else if (o instanceof SpecializationOf) {
	    return convert((SpecializationOf) o);
	} else if (o instanceof MentionOf) {
	    return convert((MentionOf) o);
	} else if (o instanceof HadMember) {
	    return convert((HadMember) o);
	} else if (o instanceof DerivedByInsertionFrom) {
	    return convert((DerivedByInsertionFrom) o);
	} else if (o instanceof DerivedByRemovalFrom) {
	    return convert((DerivedByRemovalFrom) o);
	} else if (o instanceof DictionaryMembership) {
	    return convert((DictionaryMembership) o);

	} else {
	    throw new UnsupportedOperationException("Unknown relation type "
		    + o);
	}
    }

 

    public Used convert(Used use) {
	List<Attribute> attrs=new LinkedList<Attribute>();	
	convertTypeAttributes(use,attrs);
	convertLabelAttributes(use,attrs);
	convertLocationAttributes(use,attrs);	
 	convertRoleAttributes(use,attrs);
	convertAttributes(use,attrs);
	return c.newUsed(use.getId(), use.getActivity(), use.getEntity(), use.getTime(), attrs);
    }
    
    public WasGeneratedBy convert(WasGeneratedBy gen) {
 	List<Attribute> attrs=new LinkedList<Attribute>();	
 	convertTypeAttributes(gen,attrs);
 	convertLabelAttributes(gen,attrs);
 	convertLocationAttributes(gen,attrs);	
 	convertRoleAttributes(gen,attrs);
 	convertAttributes(gen,attrs);
 	return c.newWasGeneratedBy(gen.getId(), gen.getEntity(), gen.getActivity(), gen.getTime(), attrs);
     }

    
    public WasInvalidatedBy convert(WasInvalidatedBy inv) {
 	List<Attribute> attrs=new LinkedList<Attribute>();	
 	convertTypeAttributes(inv,attrs);
 	convertLabelAttributes(inv,attrs);
 	convertLocationAttributes(inv,attrs);	
 	convertRoleAttributes(inv,attrs);
 	convertAttributes(inv,attrs);
 	return c.newWasInvalidatedBy(inv.getId(), inv.getEntity(), inv.getActivity(), inv.getTime(), attrs);
     }

    public WasStartedBy convert(WasStartedBy start) {
 	List<Attribute> attrs=new LinkedList<Attribute>();	
 	convertTypeAttributes(start,attrs);
 	convertLabelAttributes(start,attrs);
 	convertLocationAttributes(start,attrs);	
 	convertRoleAttributes(start,attrs);
 	convertAttributes(start,attrs);
 	return c.newWasStartedBy(start.getId(), start.getActivity(), start.getTrigger(), start.getStarter(), start.getTime(), attrs);
     }

    public WasEndedBy convert(WasEndedBy end) {
        List<Attribute> attrs=new LinkedList<Attribute>();    
        convertTypeAttributes(end,attrs);
        convertLabelAttributes(end,attrs);
        convertLocationAttributes(end,attrs);   
        convertRoleAttributes(end,attrs);
        convertAttributes(end,attrs);
        return c.newWasEndedBy(end.getId(), end.getActivity(), end.getTrigger(), end.getEnder(), end.getTime(), attrs);
     }


   


    public WasInformedBy convert(WasInformedBy inf) {
        List<Attribute> attrs=new LinkedList<Attribute>();      
        convertTypeAttributes(inf,attrs);
        convertLabelAttributes(inf,attrs);
        convertAttributes(inf,attrs);
        return c.newWasInformedBy(inf.getId(), inf.getInformed(), inf.getInformant(), attrs);
    }

    public WasInfluencedBy convert(WasInfluencedBy infl) {
        List<Attribute> attrs=new LinkedList<Attribute>();      
        convertTypeAttributes(infl,attrs);
        convertLabelAttributes(infl,attrs);
        convertAttributes(infl,attrs);
        return c.newWasInfluencedBy(infl.getId(), infl.getInfluencee(), infl.getInfluencer(), attrs);
    }

    public WasDerivedFrom convert(WasDerivedFrom deriv) {
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

    public WasAssociatedWith convert(WasAssociatedWith assoc) {
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

    public WasAttributedTo convert(WasAttributedTo att) {
        List<Attribute> attrs=new LinkedList<Attribute>();      
        convertTypeAttributes(att,attrs);
        convertLabelAttributes(att,attrs);
        convertAttributes(att,attrs);
        return c.newWasAttributedTo(att.getId(), att.getEntity(), att.getAgent(), attrs);
    }

    public ActedOnBehalfOf convert(ActedOnBehalfOf del) {
        List<Attribute> attrs=new LinkedList<Attribute>();      
        convertTypeAttributes(del,attrs);
        convertLabelAttributes(del,attrs);
        convertAttributes(del,attrs);
        return c.newActedOnBehalfOf(del.getId(), del.getDelegate(), del.getResponsible(), del.getActivity(), attrs);
    }

    public AlternateOf convert(AlternateOf o) {
        return c.newAlternateOf(o.getAlternate1(), o.getAlternate2());
    }

    public SpecializationOf convert(SpecializationOf o) {
        return c.newSpecializationOf(o.getSpecificEntity(), o.getGeneralEntity());
    }

    public MentionOf convert(MentionOf o) {
        return c.newMentionOf(o.getSpecificEntity(),
                              o.getGeneralEntity(),
                              o.getBundle());
    }
    
    //TODO: only supporting one member in the relation
    // note: lots of test to support scruffy provenance
    public HadMember convert(HadMember o) {
        List<QualifiedName> qq=new LinkedList<QualifiedName>();
        if (o.getEntity()!=null) {
            for (QualifiedName eid:o.getEntity()) {
                qq.add(eid);
            }
        }
        return c.newHadMember(o.getCollection(), qq);
        
/*remember, scruffy hadmember in provn, has a null in get(0)!!
	return deprecated.convertHadMember(deprecated.convert((o.getCollection()==null) ? null: 	                                    
	                                      o.getCollection().getRef()),
	                          ((o.getEntity()==null || (o.getEntity().isEmpty())) ?
	                        	  deprecated.convert(null) :
	                        	  deprecated.convert((o.getEntity().get(0)==null) ? 
	                        		     null :
	                        	             o.getEntity().get(0).getRef()))); */
    }
    
    public Relation0 convert(DerivedByRemovalFrom o) {
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

    public Relation0 convert(DerivedByInsertionFrom o) {
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
    

    public Relation0 convert(DictionaryMembership o) {
	return c.newDictionaryMembership(o.getDictionary(),  o.getKeyEntityPair());
    }


    
	
}
