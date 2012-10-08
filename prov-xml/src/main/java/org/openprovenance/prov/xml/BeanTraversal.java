package org.openprovenance.prov.xml;

import java.util.List;
import java.util.LinkedList;
import javax.xml.namespace.QName;
import javax.xml.datatype.XMLGregorianCalendar;


public class BeanTraversal {
    private BeanConstructor c;
    
    ProvUtilities u=new ProvUtilities();

    public BeanTraversal(BeanConstructor c) {
	this.c = c;
    }

    public Object convert(Document b) {
	List<Object> lnkRecords = new LinkedList<Object>();
	List<Object> aRecords = new LinkedList<Object>();
	List<Object> eRecords = new LinkedList<Object>();
	List<Object> agRecords = new LinkedList<Object>();
	List<Object> bRecords = new LinkedList<Object>();
	for (Entity e : u.getEntity(b)) {
	    eRecords.add(convert(e));
	}
	for (Activity a : u.getActivity(b)) {
	    aRecords.add(convert(a));
	}
	for (Agent ag : u.getAgent(b)) {
	    agRecords.add(convert(ag));
	}
	for (Object lnk : u.getRelations(b)) {
	    Object o = convertRelation(lnk);
	    if (o != null)
		lnkRecords.add(o);
	}
	
	for (NamedBundle bu : u.getNamedBundle(b)) {
	    Object o = convert(bu);
	    if (o != null)
		bRecords.add(o);

	}
	return c.convertBundle(b.getNss(), aRecords, eRecords, agRecords,
	                       lnkRecords, bRecords);
    }

    public Object convert(NamedBundle b) {
	List<Object> lnkRecords = new LinkedList<Object>();
	List<Object> aRecords = new LinkedList<Object>();
	List<Object> eRecords = new LinkedList<Object>();
	List<Object> agRecords = new LinkedList<Object>();

	for (Entity e : u.getEntity(b)) {
	    eRecords.add(convert(e));
	}
	for (Activity a : u.getActivity(b)) {
	    aRecords.add(convert(a));
	}
	for (Agent ag : u.getAgent(b)) {
	    agRecords.add(convert(ag));
	}
	for (Object lnk : u.getRelations(b)) {
	    Object o = convertRelation(lnk);
	    if (o != null)
		lnkRecords.add(o);
	}
	return c.convertNamedBundle(c.convert(b.getId()), b.getNss(), aRecords,
	                            eRecords, agRecords, lnkRecords);
    }

   // public Object convertAttribute(Element a) {
//	return c.convertAttribute(a.getNodeName(), c.convertAttributeValue(a));
    //}

    public Attribute convertAttribute(Attribute a) {
	 return a;
	/*
	if (a instanceof Attribute) {
	    // TODO: what is it here
	    return a;
	} else {
	    throw new UnsupportedOperationException();
	}
	
	if (a instanceof JAXBElement) {
	    JAXBElement<?> je = (JAXBElement<?>) a;
	    QName q = je.getName();
	    Object o = je.getValue();
	    String value;
	    value = o.toString();

	    return c.convertAttribute(c.convert(q), value);
	}
	if (a instanceof Element) {
	    return convertAttribute((Element) a);
	} else {
	    return c.convertAttribute(a.toString(), a.toString());
	}*/
    }

    public List<Object> convertTypeAttributes(HasType e) {
	List<Object> attrs = new LinkedList<Object>();
	for (Object a : e.getType()) {
	    attrs.add(convertTypedLiteral(a));
	}
	return attrs;
    }

    public List<Object> convertLabelAttribute(HasLabel e) {
	List<InternationalizedString> labels = e.getLabel();
	List<Object> res = new LinkedList<Object>();
	for (InternationalizedString label : labels) {
	    res.add(convertTypedLiteral(label));
	}
	return res;
    }
    public List<Object> convertLocationAttribute(HasLocation e) {
        List<Object> locations = e.getLocation();
        List<Object> res = new LinkedList<Object>();
        for (Object location : locations) {
            res.add(convertTypedLiteral(location));
        }
        return res;
    }

    public List<Object> convertRoleAttribute(HasRole e) {
        List<Object> roles = e.getRole();
        List<Object> res = new LinkedList<Object>();
        for (Object role : roles) {
            res.add(convertTypedLiteral(role));
        }
        return res;
    }
    
    public Object convertValueAttribute(HasValue e) {
        Object value = e.getValue();
        if (value==null) return null;
        return convertTypedLiteral(value);
       
    }

    public List<Attribute> convertAttributes(HasExtensibility e) {
	List<Attribute> attrs = new LinkedList<Attribute>();
	for (Attribute a : e.getAny()) {
	    attrs.add(convertAttribute(a));
	}
	return attrs;
    }

    public String quoteWrap(Object b) {
	return "\"" + b + "\"";
    }

    public Object convertTypedLiteral(Object a) {
	if (a instanceof QName) {
	    QName q = (QName) a;
	    return c.convertTypedLiteral("xsd:QName", quoteWrap(c.convert(q)));
	}
	if (a instanceof URIWrapper) {
	    URIWrapper u = (URIWrapper) a;
	    return c.convertTypedLiteral("xsd:anyURI", quoteWrap(u));
	}
	if (a instanceof Boolean) {
	    Boolean b = (Boolean) a;
	    return c.convertTypedLiteral("xsd:boolean", quoteWrap(b));
	}
	if (a instanceof String) {
	    String b = (String) a;
	    return c.convertTypedLiteral("xsd:string", quoteWrap(b));
	}
	if (a instanceof InternationalizedString) {
	    //String b = ((InternationalizedString) a).getValue();
	    //String l = ((InternationalizedString) a).getLang();
	    
	    return c.convertTypedLiteral("xsd:string", a);
	}
	if (a instanceof Double) {
	    Double b = (Double) a;
	    return c.convertTypedLiteral("xsd:double", quoteWrap(b));
	}
	if (a instanceof Integer) {
	    Integer b = (Integer) a;
	    return c.convertTypedLiteral("xsd:int", quoteWrap(b));
	} 
	if (a instanceof XMLGregorianCalendar) {
	    XMLGregorianCalendar ti=(XMLGregorianCalendar) a;
	    return c.convertTypedLiteral("xsd:dateTime", quoteWrap(ti));
	} else {
	    throw new UnsupportedOperationException("Unknown typedLiteral " + a
		    + "(" + a.getClass() + ")");
	}
    }

    public Object convert(Entity e) {
	List<Object> tAttrs = convertTypeAttributes(e);
	List<Attribute> otherAttrs = convertAttributes(e);
	List<Object> lAttrs = convertLabelAttribute(e);
	List<Object> locAttrs = convertLocationAttribute(e);
	Object value=convertValueAttribute(e);

	return c.convertEntity(c.convert(e.getId()), tAttrs, lAttrs, locAttrs, value, otherAttrs);
    }

    public Object convert(Activity e) {
	List<Object> tAttrs = convertTypeAttributes(e);
	List<Attribute> otherAttrs = convertAttributes(e);
	List<Object> lAttrs = convertLabelAttribute(e);
	List<Object> locAttrs = convertLocationAttribute(e);


	return c.convertActivity(c.convert(e.getId()), tAttrs, lAttrs, locAttrs,
	                         otherAttrs, e.getStartTime(), e.getEndTime());
    }

    public Object convert(Agent e) {
	List<Object> tAttrs = convertTypeAttributes(e);
	List<Attribute> otherAttrs = convertAttributes(e);
	List<Object> lAttrs = convertLabelAttribute(e);
	List<Object> locAttrs = convertLocationAttribute(e);


	return c.convertAgent(c.convert(e.getId()), tAttrs, lAttrs, locAttrs, otherAttrs);
    }

    public Object convertRelation(Object o) {
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

	} else {
	    throw new UnsupportedOperationException("Unknown relation type "
		    + o);
	}
    }

    public Object convert(Used o) {
        List<Object> tAttrs = convertTypeAttributes((HasType) o);
        List<Object> labAttrs = convertLabelAttribute(o);
        List<Object> locAttrs = convertLocationAttribute(o);
        List<Object> roleAttrs = convertRoleAttribute(o);
        List<Attribute> otherAttrs = convertAttributes((HasExtensibility) o);
	ActivityRef a;
	return c.convertUsed(c.convert(o.getId()), tAttrs, 
	                     labAttrs,
	                     locAttrs, roleAttrs,
	                     otherAttrs,
	                     ((a = o.getActivity()) == null) ? null : c
                                     .convert(a.getRef()),
	                     c.convert(o.getEntity().getRef()), o.getTime());
    }

    public Object convert(WasStartedBy o) {
	List<Object> tAttrs = convertTypeAttributes((HasType) o);
	List<Object> labAttrs = convertLabelAttribute(o);
	List<Attribute> otherAttrs = convertAttributes((HasExtensibility) o);
	List<Object> locAttrs = convertLocationAttribute(o);
	List<Object> roleAttrs = convertRoleAttribute(o);
	
	EntityRef e;
	ActivityRef a;
	ActivityRef s;
	return c.convertWasStartedBy(c.convert(o.getId()),
	                             tAttrs,
	                             labAttrs,
	                             locAttrs,
	                             roleAttrs,
	                             otherAttrs,
	                             ((a = o.getActivity()) == null) ? null : c
	                                     .convert(a.getRef()),
	                             ((e = o.getTrigger()) == null) ? null : c
	                                     .convert(e.getRef()),
	                             ((s = o.getStarter()) == null) ? null : c
	                                     .convert(s.getRef()), o.getTime());
    }

    public Object convert(WasEndedBy o) {
	List<Object> tAttrs = convertTypeAttributes((HasType) o);
	List<Object> labAttrs = convertLabelAttribute(o);
	List<Attribute> otherAttrs = convertAttributes((HasExtensibility) o);
	List<Object> locAttrs = convertLocationAttribute(o);
	List<Object> roleAttrs = convertRoleAttribute(o);
	
	EntityRef e;
	ActivityRef a;
	ActivityRef s;
	return c.convertWasEndedBy(c.convert(o.getId()),
	                           tAttrs,
	                           labAttrs,
	                           locAttrs,
	                           roleAttrs,
	                           otherAttrs,
	                           ((a = o.getActivity()) == null) ? null : c
	                                   .convert(a.getRef()),
	                           ((e = o.getTrigger()) == null) ? null : c
	                                   .convert(e.getRef()),
	                           ((s = o.getEnder()) == null) ? null : c
	                                   .convert(s.getRef()), o.getTime());
    }

    public Object convert(WasGeneratedBy o) {
	List<Object> tAttrs = convertTypeAttributes((HasType) o);
	List<Object> labAttrs = convertLabelAttribute(o);
	List<Attribute> otherAttrs = convertAttributes((HasExtensibility) o);
	List<Object> locAttrs = convertLocationAttribute(o);
	List<Object> roleAttrs = convertRoleAttribute(o);

	ActivityRef a;
	return c.convertWasGeneratedBy(c.convert(o.getId()), tAttrs, labAttrs,
	                               locAttrs,
	                               roleAttrs,
	                               otherAttrs, c.convert(o.getEntity()
	                                       .getRef()),
	                               ((a = o.getActivity()) == null) ? null
	                                       : c.convert(a.getRef()), o
	                                       .getTime());
    }

    public Object convert(WasInvalidatedBy o) {
	List<Object> tAttrs = convertTypeAttributes((HasType) o);
	List<Object> labAttrs = convertLabelAttribute(o);
	List<Attribute> otherAttrs = convertAttributes((HasExtensibility) o);
	List<Object> locAttrs = convertLocationAttribute(o);
	List<Object> roleAttrs = convertRoleAttribute(o);
	
	ActivityRef a;
	return c.convertWasInvalidatedBy(c.convert(o.getId()), tAttrs,labAttrs,
	                                 locAttrs,
	                                 roleAttrs,
	                                 otherAttrs, c.convert(o.getEntity()
	                                         .getRef()),
	                                 ((a = o.getActivity()) == null) ? null
	                                         : c.convert(a.getRef()), o
	                                         .getTime());
    }

    public Object convert(WasInformedBy o) {
	List<Object> tAttrs = convertTypeAttributes((HasType) o);
	List<Attribute> otherAttrs = convertAttributes((HasExtensibility) o);
	return c.convertWasInformedBy(c.convert(o.getId()), tAttrs, otherAttrs,
	                              c.convert(o.getEffect().getRef()),
	                              c.convert(o.getCause().getRef()));
    }

    public Object convert(WasInfluencedBy o) {
	List<Object> tAttrs = convertTypeAttributes((HasType) o);
	List<Attribute> otherAttrs = convertAttributes((HasExtensibility) o);
	return c.convertWasInfluencedBy(c.convert(o.getId()), tAttrs,
	                                otherAttrs,
	                                c.convert(o.getInfluencee().getRef()),
	                                c.convert(o.getInfluencer().getRef()));
    }

    public Object convert(WasDerivedFrom o) {
	List<Object> tAttrs = convertTypeAttributes((HasType) o);
	List<Attribute> otherAttrs = convertAttributes((HasExtensibility) o);
	return c.convertWasDerivedFrom(c.convert(o.getId()), tAttrs,
	                               otherAttrs, c.convert(o
	                                       .getGeneratedEntity().getRef()),
	                               c.convert(o.getUsedEntity().getRef()));
    }

    public Object convert(WasAssociatedWith o) {
	List<Object> tAttrs = convertTypeAttributes((HasType) o);
	List<Attribute> otherAttrs = convertAttributes((HasExtensibility) o);
	return c.convertWasAssociatedWith(c.convert(o.getId()),
	                                  tAttrs,
	                                  otherAttrs,
	                                  c.convert(o.getActivity().getRef()),
	                                  (o.getAgent() == null) ? null : c
	                                          .convert(o.getAgent()
	                                                  .getRef()),
	                                  (o.getPlan() == null) ? null
	                                          : c.convert(o.getPlan()
	                                                  .getRef()));
    }

    public Object convert(WasAttributedTo o) {
	List<Object> tAttrs = convertTypeAttributes((HasType) o);
	List<Attribute> otherAttrs = convertAttributes((HasExtensibility) o);
	return c.convertWasAttributedTo(c.convert(o.getId()),
	                                tAttrs,
	                                otherAttrs,
	                                c.convert(o.getEntity().getRef()),
	                                (o.getAgent() == null) ? null : c
	                                        .convert(o.getAgent().getRef()));
    }

    public Object convert(ActedOnBehalfOf o) {
	List<Object> tAttrs = convertTypeAttributes((HasType) o);
	List<Attribute> otherAttrs = convertAttributes((HasExtensibility) o);
	return c.convertActedOnBehalfOf(c.convert(o.getId()),
	                                tAttrs,
	                                otherAttrs,
	                                c.convert(o.getSubordinate().getRef()),
	                                c.convert(o.getResponsible().getRef()),
	                                (o.getActivity() == null) ? null : c
	                                        .convert(o.getActivity()
	                                                .getRef()));
    }

    public Object convert(AlternateOf o) {

	return c.convertAlternateOf(c.convert(o.getEntity2().getRef()),
	                            c.convert(o.getEntity1().getRef()));
    }

    public Object convert(SpecializationOf o) {
	return c.convertSpecializationOf(c.convert(o.getSpecializedEntity()
	        .getRef()), c.convert(o.getGeneralEntity().getRef()));
    }

    public Object convert(MentionOf o) {
	return c.convertMentionOf(c.convert(o.getSpecializedEntity().getRef()),
	                          c.convert(o.getGeneralEntity().getRef()),
	                          c.convert(o.getBundle().getRef()));
    }

	
}
