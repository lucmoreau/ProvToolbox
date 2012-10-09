package org.openprovenance.prov.notation;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Hashtable;
import java.net.URI;
import javax.xml.datatype.XMLGregorianCalendar;

import org.openprovenance.prov.xml.Attribute;
import org.openprovenance.prov.xml.Document;
import org.openprovenance.prov.xml.HadMember;
import org.openprovenance.prov.xml.HasLabel;
import org.openprovenance.prov.xml.HasLocation;
import org.openprovenance.prov.xml.HasRole;
import org.openprovenance.prov.xml.HasValue;
import org.openprovenance.prov.xml.InternationalizedString;
import org.openprovenance.prov.xml.ProvUtilities;
import org.openprovenance.prov.xml.Statement;
import org.openprovenance.prov.xml.URIWrapper;
import org.openprovenance.prov.xml.NamespacePrefixMapper;
import org.openprovenance.prov.xml.ProvFactory;
import org.openprovenance.prov.xml.Activity;
import org.openprovenance.prov.xml.ActivityRef;
import org.openprovenance.prov.xml.Entity;
import org.openprovenance.prov.xml.Agent;
import org.openprovenance.prov.xml.EntityRef;
import org.openprovenance.prov.xml.HasExtensibility;
import org.openprovenance.prov.xml.HasType;
import org.openprovenance.prov.xml.AgentRef;
import org.openprovenance.prov.xml.NamedBundle;
import org.openprovenance.prov.xml.Used;
import org.openprovenance.prov.xml.WasGeneratedBy;
import org.openprovenance.prov.xml.WasInvalidatedBy;
import org.openprovenance.prov.xml.WasStartedBy;
import org.openprovenance.prov.xml.WasInformedBy;
import org.openprovenance.prov.xml.ActedOnBehalfOf;
import org.openprovenance.prov.xml.WasEndedBy;
import org.openprovenance.prov.xml.WasDerivedFrom;
import org.openprovenance.prov.xml.WasAttributedTo;
import org.openprovenance.prov.xml.collection.DerivedByInsertionFrom;
import org.openprovenance.prov.xml.collection.DerivedByRemovalFrom;
import org.openprovenance.prov.xml.collection.CollectionMemberOf;
import org.openprovenance.prov.xml.collection.DictionaryMemberOf;
import org.openprovenance.prov.xml.collection.Entry;
import org.openprovenance.prov.xml.WasInfluencedBy;
import org.openprovenance.prov.xml.AlternateOf;
import org.openprovenance.prov.xml.MentionOf;
import org.openprovenance.prov.xml.SpecializationOf;
import org.openprovenance.prov.xml.WasAssociatedWith;
import javax.xml.namespace.QName;


public  class ProvConstructor implements TreeConstructor {
    private ProvFactory pFactory;
    private ProvUtilities u=new ProvUtilities();

    Hashtable<String,Entity>   entityTable   = new Hashtable<String,Entity>();
    Hashtable<String,Activity> activityTable = new Hashtable<String,Activity>();
    Hashtable<String,Agent>    agentTable    = new Hashtable<String,Agent>();

    Hashtable<String,String>  namespaceTable = new Hashtable<String,String>();
    
    public ProvConstructor(ProvFactory pFactory) {
        this.pFactory=pFactory;
        namespaceTable.put("xsd",NamespacePrefixMapper.XSD_NS);
        namespaceTable.put("xsi",NamespacePrefixMapper.XSI_NS);
    }
            

    public Object convertActivity(Object id,Object startTime,Object endTime, Object aAttrs) {
        String s_id=(String)id;
        String s_startTime=(String)startTime;
        String s_endTime=(String)endTime;
        @SuppressWarnings("unchecked")
        List<Object> attrs=(List<Object>)aAttrs;
        Activity a=pFactory.newActivity(s_id);
        activityTable.put(s_id,a);
        addAllAttributes(a,(List<Object>)attrs);

        if (s_endTime!=null) {
            a.setEndTime(pFactory.newISOTime(s_endTime));
        }

        if (s_startTime!=null) {
            a.setStartTime(pFactory.newISOTime(s_startTime));
        }

        return a;
    }


    public String unwrap (String s) {
        return s.substring(1,s.length()-1);
    }
               
    public Object convertEntity(Object id, Object eAttrs) {
        String s_id=(String)id;
        @SuppressWarnings("unchecked")
        List<Object> attrs=(List<Object>)eAttrs;
        Entity e=pFactory.newEntity(s_id);
        entityTable.put(s_id,e);
        addAllAttributes(e,(List<Object>)attrs);
        return e;
    }

    /* Recognize prov attributes and insert them in the appropriate fields.
       TODO: done for type, only*/
    
    public void addAllAttributes(HasExtensibility e, List<Object> attributes) {
	List<?> attrs=(List<?>) attributes;
	List<Attribute> attributes1=(List<Attribute>) attrs;
	
        for (Attribute o: attributes1) {
            QName q=o.getElementName();
            if ("type".equals(q.getLocalPart())) {
        	HasType eWithType=(HasType) e;
        	eWithType.getType().add(o.getValue());
            } else if ("location".equals(q.getLocalPart())) {
                HasLocation eWithLocation=(HasLocation) e;
                eWithLocation.getLocation().add(o.getValue());
            } else if ("role".equals(q.getLocalPart())) {
                HasRole eWithRole=(HasRole) e;
                eWithRole.getRole().add(o.getValue());
            } else if ("value".equals(q.getLocalPart())) {
                HasValue eWithValue=(HasValue) e;
                eWithValue.setValue(o.getValue());
            } else if ("label".equals(q.getLocalPart())) {
        	HasLabel eWithLabel=(HasLabel) e;
        	if (o.getValue() instanceof String) {
        	    eWithLabel.getLabel().add(pFactory.newInternationalizedString((String)o.getValue()));
        	} else {
        	    eWithLabel.getLabel().add((InternationalizedString)o.getValue());
        	}
            } else {
        	e.getAny().add(o);
            }
        } 
    }

    public Object convertAgent(Object id, Object eAttrs) {
        String s_id=(String)id;
        @SuppressWarnings("unchecked")
        List<Object> attrs=(List<Object>)eAttrs;
        Agent e=pFactory.newAgent(s_id);
        //entityTable.put(s_id,e);  
        agentTable.put(s_id,e);
        addAllAttributes(e,(List<Object>)attrs);
        return e;
    }

    public Object convertDocument(Object namespaces, List<Object> records, List<Object> bundles) {    
	Collection<Statement> stments=new LinkedList<Statement>();
        for (Object o: records) {
           stments.add((Statement)o); 
        }
        Document c=pFactory.newDocument();
        c.getEntityOrActivityOrWasGeneratedBy().addAll(stments);
        
        //System.out.println("Bundle namespaces " + namespaceTable);
        c.setNss(namespaceTable);

        if (bundles!=null) {
            List<NamedBundle> nbs=u.getNamedBundle(c);
            for (Object o: bundles) {
                nbs.add((NamedBundle) o);
            }
        }
        return c;
    }

    
    public Object convertNamedBundle(Object id, Object namespaces, List<Object> records) {    
        Collection<Entity> es=new LinkedList<Entity>();
        Collection<Agent> ags=new LinkedList<Agent>();
        Collection<Activity> acs=new LinkedList<Activity>();
        Collection<Statement> lks=new LinkedList<Statement>();
            
        if (records!=null) 
            for (Object o: records) {
                if (o instanceof Agent) { ags.add((Agent)o); }
                else if (o instanceof Entity) { es.add((Entity)o); }
                else if (o instanceof Activity) { acs.add((Activity)o); }
                else lks.add((Statement)o);
            }
        String s_id=(String)id;
        //System.out.println("NamedBundle name " + s_id);
        NamedBundle c=pFactory.newNamedBundle(s_id,
                                              acs,
                                              es,
                                              ags,
                                              lks);

        //System.out.println("Bundle namespaces " + namespaceTable);
        c.setNss(namespaceTable);
        return c;
    }

    public void startBundle(Object bundleId) {
    }
    
    public Object convertAttributes(List<Object> attributeList) {
        return attributeList;
    }
    
    public Object convertId(String id) {
        return id;
    }

    public String getPrefix(String namespace) {
        for (String s : namespaceTable.keySet()) {
            if (namespaceTable.get(s).equals(namespace)) {
                return s;
            }
        }
        return null;
    }

    public String getNamespace(String prefix) {
        if ((prefix==null) || ("".equals(prefix))) return namespaceTable.get("_");
        if (prefix.equals("prov")) return NamespacePrefixMapper.PROV_NS;
        if (prefix.equals("xsd")) return NamespacePrefixMapper.XSD_NS;
        return namespaceTable.get(prefix);
    }

    // TODO: I don't recognize the predefined attributes ...
    //
    public Object convertAttribute(Object name, Object value) {
        String attr1=(String)name;

        int index=attr1.indexOf(':');
        String prefix;
        String local;

        if (index==-1) {
            prefix="";
            local=attr1;
        } else {
            prefix=attr1.substring(0,index);
            local=attr1.substring(index+1,attr1.length());
        }
        
        /*

        if (false && value instanceof Integer) {
            String val1=value.toString();
            return pFactory.newAttribute(getNamespace(prefix),
                                         prefix,
                                         local,
                                         unwrap(val1));

        } else if (false && value instanceof String) {
            String val1=(String)(value);
            return pFactory.newAttribute(getNamespace(prefix),
                                         prefix,
                                         local,
                                         unwrap(val1));
        } else
        */
         {
            QName attr1_QNAME = new QName(getNamespace(prefix),
                                          local,
                                          prefix);
        
            //return new JAXBElement<TypedLiteral>(attr1_QNAME, TypedLiteral.class, null, (TypedLiteral)value);

            //return new JAXBElement<Object>(attr1_QNAME, Object.class, null, value);
            return pFactory.newAttribute(attr1_QNAME, value);
        }
    }

    public Object convertStart(String start) {
        return start;
    }

    public Object convertEnd(String end) {
        return end;
    }
    

    public Object convertString(String s) {
        s=unwrap(s);
        return s;
    }


    public Object convertString(String s, String lang) {
        s=unwrap(s);
        return pFactory.newInternationalizedString(s,lang);
    }

    public Object convertInt(int i) {
        return i;
    }


    public Object convertUsed(Object id, Object id2,Object id1, Object time, Object uAttrs) {
        String s_id=(String)id;
        String s_id2=(String)id2;
        String s_id1=(String)id1;
        ActivityRef a2r=(s_id2==null)? null: pFactory.newActivityRef(s_id2);
        EntityRef e1r=(s_id1==null)? null: pFactory.newEntityRef(s_id1);
        Used u=pFactory.newUsed(s_id,
                                a2r,
                                null,
                                e1r);
        List<?> attrs=(List<?>)uAttrs;
        addAllAttributes(u, (List<Object>)attrs);

        if (time!=null) {
	    if (time instanceof XMLGregorianCalendar) {
		u.setTime((XMLGregorianCalendar)time);
	    } else {
		u.setTime(pFactory.newISOTime((String)time));
	    }
        }
        return u;
    }

    
    public Object convertWasGeneratedBy(Object id, Object id2,Object id1, Object time, Object gAttrs) {
        String s_id=(String)id;
        String s_id2=(String)id2;
        String s_id1=(String)id1;
        ActivityRef a1r= (s_id1==null) ? null: pFactory.newActivityRef(s_id1);
        EntityRef e2r=pFactory.newEntityRef(s_id2);

        WasGeneratedBy g=pFactory.newWasGeneratedBy(s_id,
                                                    e2r,
                                                    null,
                                                    a1r);
        List<?> attrs=(List<?>)gAttrs;
        if (attrs!=null) addAllAttributes(g, (List<Object>)attrs);
        if (time!=null) {
	    if (time instanceof XMLGregorianCalendar) {
		g.setTime((XMLGregorianCalendar)time);
	    } else {
		g.setTime(pFactory.newISOTime((String)time));
	    }
        }
            
        return g;
    }

    public Object convertWasStartedBy(Object id, Object id2,Object id1, Object id3, Object time, Object gAttrs) {
        String s_id=(String)id;
        String s_id2=(String)id2;
        String s_id1=(String)id1;
        String s_id3=(String)id3;
        EntityRef e1r=(s_id1==null)? null: pFactory.newEntityRef(s_id1);  //id1 may be null
        ActivityRef a2r=(s_id2==null)? null: pFactory.newActivityRef(s_id2);

        WasStartedBy s=pFactory.newWasStartedBy(s_id,
                                                a2r,
                                                e1r);

        ActivityRef a3r=(s_id3==null)? null : pFactory.newActivityRef(s_id3);  //id3 may be null
        
        List<?> attrs=(List<?>)gAttrs;
        addAllAttributes(s, (List<Object>)attrs);
        if (time!=null) {
	    if (time instanceof XMLGregorianCalendar) {
		s.setTime((XMLGregorianCalendar)time);
	    } else {
		s.setTime(pFactory.newISOTime((String)time));
	    }

        }

        s.setStarter(a3r);
            
        return s;
    }

    public Object convertWasEndedBy(Object id, Object id2,Object id1, Object id3, Object time, Object gAttrs) {
        String s_id=(String)id;
        String s_id2=(String)id2;
        String s_id1=(String)id1;
        String s_id3=(String)id3;

        EntityRef e1r=(s_id1==null)? null: pFactory.newEntityRef(s_id1);  //id1 may be null
        ActivityRef a2r=(s_id2==null) ? null: pFactory.newActivityRef(s_id2);

        WasEndedBy s=pFactory.newWasEndedBy(s_id,
                                            a2r,
                                            e1r);
        ActivityRef a3r=(s_id3==null)? null: pFactory.newActivityRef(s_id3);  //id3 may be null
        
        List<?> attrs=(List<?>)gAttrs;
        addAllAttributes(s, (List<Object>)attrs);
        if (time!=null) {
	    if (time instanceof XMLGregorianCalendar) {
		s.setTime((XMLGregorianCalendar)time);
	    } else {
		s.setTime(pFactory.newISOTime((String)time));
	    }
        }

        s.setEnder(a3r);
            
        return s;
    }

    public Object convertWasInvalidatedBy(Object id, Object id2,Object id1, Object time, Object gAttrs) {
        String s_id=(String)id;
        String s_id2=(String)id2;
        String s_id1=(String)id1;
        ActivityRef a1r=(s_id1==null)? null: pFactory.newActivityRef(s_id1);
        EntityRef e2r=(s_id2==null) ? null : pFactory.newEntityRef(s_id2);

        WasInvalidatedBy g=pFactory.newWasInvalidatedBy(s_id,
                                                        e2r,
                                                        a1r);
        List<?> attrs=(List<?>)gAttrs;
        
        addAllAttributes(g, (List<Object>)attrs);
        if (time!=null) {
	    if (time instanceof XMLGregorianCalendar) {
		g.setTime((XMLGregorianCalendar)time);
	    } else {
		g.setTime(pFactory.newISOTime((String)time));
	    }
        }
            
        return g;
    }


    public Object convertWasInformedBy(Object id, Object id2, Object id1, Object aAttrs) {
        String s_id=(String)id;
        String s_id2=(String)id2;
        String s_id1=(String)id1;
        ActivityRef e1r=(s_id1==null) ? null : pFactory.newActivityRef(s_id1);

        ActivityRef a2r=(s_id2==null) ? null: pFactory.newActivityRef(s_id2);

        WasInformedBy s=pFactory.newWasInformedBy(s_id,
                                                  a2r,
                                                  e1r);
        List<?> attrs=(List<?>)aAttrs;
        addAllAttributes(s, (List<Object>)attrs);
        

        return s;
    }



    public Object convertWasAttributedTo(Object id, Object id2,Object id1, Object aAttrs) {
        String s_id=(String)id;
        String s_id2=(String)id2;
        String s_id1=(String)id1;
        AgentRef ag1r=(s_id1==null)? null: pFactory.newAgentRef(s_id1);
        EntityRef e2r=(s_id2==null)? null: pFactory.newEntityRef(s_id2);

        WasAttributedTo s=pFactory.newWasAttributedTo(s_id,
                                                      e2r,
                                                      ag1r);
        
        List<?> attrs=(List<?>)aAttrs;
        addAllAttributes(s, (List<Object>)attrs);
       
        return s;
    }

    public Object convertWasDerivedFrom(Object id, Object id2,Object id1, Object a, Object g2, Object u1, Object dAttrs) {
        String s_id=(String)id;
        String s_id2=(String)id2;
        String s_id1=(String)id1;
        EntityRef e2r=(s_id2==null)? null: pFactory.newEntityRef(s_id2);
        EntityRef e1r=(s_id1==null)? null: pFactory.newEntityRef(s_id1);
        WasDerivedFrom d=pFactory.newWasDerivedFrom(s_id,
                                                    e2r,
                                                    e1r);
        if (a!=null) d.setActivity(pFactory.newActivityRef((String)a));
        if (g2!=null) d.setGeneration(pFactory.newGenerationRef((String)g2));
        if (u1!=null) d.setUsage(pFactory.newUsageRef((String)u1));

        List<?> attrs=(List<?>)dAttrs;
        addAllAttributes(d, (List<Object>)attrs);
        return d;
    }

 
   

    public Object convertWasInfluencedBy(Object id, Object id2, Object id1, Object dAttrs) {
        String s_id=(String)id;
        String s_id2=(String)id2;
        String s_id1=(String)id1;

        WasInfluencedBy d=pFactory.newWasInfluencedBy(s_id,
                                                      s_id2,
                                                      s_id1);
        List<?> attrs=(List<?>)dAttrs;
        addAllAttributes(d, (List<Object>)attrs);
        
        return d;
    }


    public Object convertAlternateOf(Object id2,Object id1) {
        String s_id2=(String)id2;
        String s_id1=(String)id1;
        EntityRef e2r=(s_id2==null)? null: pFactory.newEntityRef(s_id2);
        EntityRef e1r=(s_id1==null)? null: pFactory.newEntityRef(s_id1);
        AlternateOf wco=pFactory.newAlternateOf(e2r,
                                                e1r);
        return wco;

    }

    public Object convertSpecializationOf(Object id2,Object id1) {
        String s_id2=(String)id2;
        String s_id1=(String)id1;
        EntityRef e2r=(s_id2==null)? null: pFactory.newEntityRef(s_id2);
        EntityRef e1r=(s_id1==null)? null: pFactory.newEntityRef(s_id1);
        SpecializationOf wco=pFactory.newSpecializationOf(e2r,
                                                          e1r);
        return wco;

    }


    public Object convertMentionOf(Object su, Object bu, Object ta) {

        String s_su=(String)su;
        String s_bu=(String)bu;
        String s_ta=(String)ta;
        

        MentionOf conOf=pFactory.newMentionOf(s_su,s_bu,s_ta);

        return conOf;
    }



    public Object convertWasAssociatedWith(Object id, Object id2,Object id1, Object pl, Object aAttrs) {
        String s_id=(String)id;
        String s_id2=(String)id2;
        String s_id1=(String)id1;
        String s_pl=(String)pl;
        ActivityRef e2r=(s_id2==null) ? null : pFactory.newActivityRef(s_id2);
        AgentRef e1r=(s_id1==null) ? null: pFactory.newAgentRef(s_id1);
        
        EntityRef e3r=(s_pl==null) ? null: pFactory.newEntityRef(s_pl);
        
        WasAssociatedWith waw=pFactory.newWasAssociatedWith(s_id,
                                                            e2r,
                                                            e1r);
        waw.setPlan(e3r);
        List<?> attrs=(List<?>)aAttrs;
        
        addAllAttributes(waw, (List<Object>)attrs);

        return waw;
    }

    public Object convertActedOnBehalfOf(Object id, Object id2,Object id1, Object a, Object aAttrs) {
        String s_id=(String)id;
        String s_id2=(String)id2;
        String s_id1=(String)id1;
        String s_id3=(String)a;
        AgentRef e2r=(s_id2==null) ? null : pFactory.newAgentRef(s_id2);
        AgentRef e1r=(s_id1==null) ? null : pFactory.newAgentRef(s_id1);

        ActivityRef e3r=(s_id3==null) ? null:pFactory.newActivityRef(s_id3); 
       
        ActedOnBehalfOf aobo=pFactory.newActedOnBehalfOf(s_id,
                                                         e2r,
                                                         e1r,
                                                         e3r);
        List<?> attrs=(List<?>)aAttrs;
        
        addAllAttributes(aobo, (List<Object>)attrs);

        return aobo;
    }
    

    @Override
    public Object convertHadMember(Object id2, Object id1) {
	String s_id2=(String)id2;
	String s_id1=(String)id1;
	EntityRef e2r=(s_id2==null)? null: pFactory.newEntityRef(s_id2);
	EntityRef e1r=(s_id1==null)? null: pFactory.newEntityRef(s_id1);
	HadMember hm=pFactory.newHadMember(e2r, new EntityRef[] {e1r});

	return hm;
    }
 
    

    public Object convertQualifiedName(String qname) {
        return qname;
    }

    public Object convertIRI(String iri) {
        if (iri==null) return null;
        iri=unwrap(iri);
        return URI.create(iri);
    }


    /* Uses the xsd:type to java:type mapping of JAXB */

    public Object convertToJava(String datatype, String value) {
        value=unwrap(value);
        //System.out.println("convertToJava: datatype " + datatype + "  " + value);
        if (datatype.equals("xsd:string"))  return value;

        if (datatype.equals("xsd:int"))     return Integer.parseInt(value);
        if (datatype.equals("xsd:long"))    return Long.parseLong(value);
        if (datatype.equals("xsd:short"))   return Short.parseShort(value);
        if (datatype.equals("xsd:double"))  return Double.parseDouble(value);
        if (datatype.equals("xsd:float"))   return Float.parseFloat(value);
        if (datatype.equals("xsd:decimal")) return new java.math.BigDecimal(value);
        if (datatype.equals("xsd:boolean")) return Boolean.parseBoolean(value);
        if (datatype.equals("xsd:byte"))    return Byte.parseByte(value);
        if (datatype.equals("xsd:unsignedInt"))   return Long.parseLong(value);
        if (datatype.equals("xsd:unsignedShort")) return Integer.parseInt(value);
        if (datatype.equals("xsd:unsignedByte"))  return Short.parseShort(value);
        if (datatype.equals("xsd:unsignedLong"))  return new java.math.BigInteger(value);
        if (datatype.equals("xsd:integer"))             return new java.math.BigInteger(value);
        if (datatype.equals("xsd:nonNegativeInteger"))  return new java.math.BigInteger(value);
        if (datatype.equals("xsd:nonPositiveInteger"))  return new java.math.BigInteger(value);
        if (datatype.equals("xsd:positiveInteger"))     return new java.math.BigInteger(value);

        if (datatype.equals("xsd:anyURI")) {
            URIWrapper u=new URIWrapper();
            u.setValue(URI.create(value));
            return u;
        }
        if (datatype.equals("xsd:QName")) {
            int index=value.indexOf(':');
            String prefix;
            String local;

            if (index==-1) {
                prefix="";
                local=value;
            } else {
                prefix=value.substring(0,index);
                local=value.substring(index+1,value.length());
            }
            return new QName(getNamespace(prefix), local, prefix);
        }
        if (datatype.equals("xsd:dateTime")) {
            return pFactory.newISOTime(value);  //TODO: use value!
        }


        if ((datatype.equals("rdf:XMLLiteral"))
            || (datatype.equals("xsd:normalizedString"))
            || (datatype.equals("xsd:token"))
            || (datatype.equals("xsd:language"))
            || (datatype.equals("xsd:Name"))
            || (datatype.equals("xsd:NCName"))
            || (datatype.equals("xsd:NMTOKEN"))
            || (datatype.equals("xsd:hexBinary"))
            || (datatype.equals("xsd:base64Binary"))) {

            throw new UnsupportedOperationException("KNOWN literal type but conversion not supported yet " + datatype);
        }

        throw new UnsupportedOperationException("UNKNOWN literal type " + datatype);
    }
    
    


    public Object convertTypedLiteral(String datatype, Object value) {
        if (value instanceof String) {
            Object val=convertToJava(datatype,(String)value);
            //pFactory.newTypedLiteral(val);
            return val;
        } else {
            return value;
        }
    }

    public Object convertNamespace(Object pre, Object iri) {
        String s_pre=(String)pre;
        String s_iri=(String)iri;
        s_iri=unwrap(s_iri);
        if (s_pre!=null) // should not occur
            namespaceTable.put(s_pre,s_iri);
        return null;
    }

    public Object convertDefaultNamespace(Object iri) {
        String s_iri=(String)iri;
        s_iri=unwrap(s_iri);
        namespaceTable.put("_",s_iri);
        return null;
    }
    
    public Object convertNamespaces(List<Object> namespaces) {
        pFactory.setNamespaces(namespaceTable);
        return namespaceTable;
    }

    public Object convertPrefix(String pre) {
        return pre;
    }


    /* Component 5 */

    public Object convertInsertion(Object id, Object id2, Object id1, Object map, Object dAttrs) {
        String s_id=(String)id;
        String s_id2=(String)id2;
        String s_id1=(String)id1;

        Entity e2=entityTable.get(s_id2);
        EntityRef e2r=pFactory.newEntityRef(e2);
        Entity e1=entityTable.get(s_id1);
        EntityRef e1r=pFactory.newEntityRef(e1);

        DerivedByInsertionFrom dbif=pFactory.newDerivedByInsertionFrom(s_id,
                                                                       e2r,
                                                                       e1r,
                                                                       null);
        List<?> attrs=(List<?>)dAttrs;
        dbif.getAny().addAll(attrs);

        List<?> entries=(List<?>)map;
        for (Object o: entries) {
            Entry entry=(Entry) o;
            dbif.getEntry().add(entry);
        }

        return dbif;
    }


    public Object convertRemoval(Object id, Object id2, Object id1, Object keyset, Object dAttrs) {

        String s_id=(String)id;
        String s_id2=(String)id2;
        String s_id1=(String)id1;

        Entity e2=entityTable.get(s_id2);
        EntityRef e2r=pFactory.newEntityRef(e2);
        Entity e1=entityTable.get(s_id1);
        EntityRef e1r=pFactory.newEntityRef(e1);

        DerivedByRemovalFrom dbrf=pFactory.newDerivedByRemovalFrom(s_id,
                                                                   e2r,
                                                                   e1r,
                                                                   null);
        List<?> attrs=(List<?>)dAttrs;
        dbrf.getAny().addAll(attrs);

        return dbrf;
    }


    public Object convertDictionaryMemberOf(Object id, Object id2, Object map, Object complete, Object dAttrs) {
        String s_id=(String)id;
        String s_id2=(String)id2;

        Entity e2=entityTable.get(s_id2);
        EntityRef e2r=pFactory.newEntityRef(e2);

        DictionaryMemberOf mo=pFactory.newDictionaryMemberOf(s_id,
                                                             e2r,
                                                             null);
        List<?> attrs=(List<?>)dAttrs;
        if (attrs!=null) mo.getAny().addAll(attrs);
        
        return mo;
    }

    public Object convertCollectionMemberOf(Object id, Object id2, Object map, Object complete, Object dAttrs) {
        String s_id=(String)id;
        String s_id2=(String)id2;

        Entity e2=entityTable.get(s_id2);
        EntityRef e2r=pFactory.newEntityRef(e2);

        CollectionMemberOf mo=pFactory.newCollectionMemberOf(s_id,
                                                             e2r,
                                                             null);
        List<?> attrs=(List<?>)dAttrs;
        if (attrs!=null) mo.getAny().addAll(attrs);
        
        return mo;
    }



    public Object convertEntry(Object o1, Object o2) {
        String s_id=(String)o2;

        Entity e=entityTable.get(s_id);
        EntityRef er=pFactory.newEntityRef(e);
    
        return pFactory.newEntry(o1,er);
    }


    public Object convertKeyEntitySet(List<Object> entries) {
        return entries;
    }

    public Object convertKeys(List<Object> keys) {
        return keys;
    }

    /* Component 6 */

    public Object convertExtension(Object name, Object id, Object args, Object dAttrs) {
        return null;
    }



}

