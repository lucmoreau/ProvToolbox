package org.openprovenance.prov.notation;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Hashtable;
import java.net.URI;

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
import org.openprovenance.prov.xml.NoteRef;
import org.openprovenance.prov.xml.Agent;
import org.openprovenance.prov.xml.Bundle;
import org.openprovenance.prov.xml.NamedBundle;
import org.openprovenance.prov.xml.Used;
import org.openprovenance.prov.xml.WasGeneratedBy;
import org.openprovenance.prov.xml.WasInvalidatedBy;
import org.openprovenance.prov.xml.WasStartedBy;
import org.openprovenance.prov.xml.WasStartedByActivity;
import org.openprovenance.prov.xml.WasInformedBy;
import org.openprovenance.prov.xml.ActedOnBehalfOf;
import org.openprovenance.prov.xml.WasEndedBy;
import org.openprovenance.prov.xml.WasDerivedFrom;
import org.openprovenance.prov.xml.WasAttributedTo;
import org.openprovenance.prov.xml.WasRevisionOf;
import org.openprovenance.prov.xml.WasQuotedFrom;
import org.openprovenance.prov.xml.DerivedByInsertionFrom;
import org.openprovenance.prov.xml.DerivedByRemovalFrom;
import org.openprovenance.prov.xml.MemberOf;
import org.openprovenance.prov.xml.Entry;
import org.openprovenance.prov.xml.HadOriginalSource;
import org.openprovenance.prov.xml.TracedTo;
import org.openprovenance.prov.xml.AlternateOf;
import org.openprovenance.prov.xml.Note;
import org.openprovenance.prov.xml.HasAnnotation;
import org.openprovenance.prov.xml.SpecializationOf;
import org.openprovenance.prov.xml.WasAssociatedWith;
import org.openprovenance.prov.xml.NamespacePrefixMapper;

import  org.antlr.runtime.CommonTokenStream;
import  org.antlr.runtime.ANTLRFileStream;
import  org.antlr.runtime.CharStream;
import  org.antlr.runtime.Token;
import  org.antlr.runtime.tree.Tree;
import  org.antlr.runtime.tree.CommonTree;
import  org.antlr.runtime.tree.CommonTreeAdaptor;
import  org.antlr.runtime.tree.TreeAdaptor;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;


public  class ProvConstructor implements TreeConstructor {
    private ProvFactory pFactory;

    Hashtable<String,Entity>   entityTable   = new Hashtable<String,Entity>();
    Hashtable<String,Activity> activityTable = new Hashtable<String,Activity>();
    Hashtable<String,Agent>    agentTable    = new Hashtable<String,Agent>();
    Hashtable<String,Note>     noteTable     = new Hashtable<String,Note>();

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
        List attrs=(List)aAttrs;
        Activity a=pFactory.newActivity(s_id);
        activityTable.put(s_id,a);
        addAllAttributes(a,(List)attrs);

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
        List attrs=(List)eAttrs;
        Entity e=pFactory.newEntity(s_id);
        entityTable.put(s_id,e);
        addAllAttributes(e,(List)attrs);
        return e;
    }

    /* Recognize prov attributes and insert them in the appropriate fields.
     TODO: done for type, only*/
    
    public void addAllAttributes(HasExtensibility e, List attributes) {
        for (Object o: attributes) {
            if (o instanceof JAXBElement) {
                JAXBElement je=(JAXBElement) o;
                QName q=je.getName();
                if ("type".equals(q.getLocalPart())) {
                    HasType eWithType=(HasType) e;
                    eWithType.getType().add(je.getValue());
                } else {
                    e.getAny().add(o);
                }
            } else {
                e.getAny().add(o);
            }
        }
    }

    public Object convertAgent(Object id, Object eAttrs) {
        String s_id=(String)id;
        List attrs=(List)eAttrs;
        Agent e=pFactory.newAgent(s_id);
        //entityTable.put(s_id,e);  
        agentTable.put(s_id,e);
        addAllAttributes(e,(List)attrs);
        return e;
    }

    public Object convertBundle(Object namespaces, List<Object> records, List<Object> bundles) {    
        Collection<Entity> es=new LinkedList();
        Collection<Agent> ags=new LinkedList();
        Collection<Activity> acs=new LinkedList();
        Collection<Note> ns=new LinkedList();
        Collection<Object> lks=new LinkedList();
            
        for (Object o: records) {
            if (o instanceof Agent) { ags.add((Agent)o); }
            else if (o instanceof Entity) { es.add((Entity)o); }
            else if (o instanceof Activity) { acs.add((Activity)o); }
            else if (o instanceof Note) { ns.add((Note)o); }
            else lks.add(o);
        }
        Bundle c=pFactory.newBundle(      acs,
                                          es,
                                          ags,
                                          ns,
                                          lks);
        System.out.println("Bundle namespaces " + namespaceTable);
        c.setNss(namespaceTable);
        return c;
    }

    public Object convertNamedBundle(Object id, Object namespaces, List<Object> records) {    
        Collection<Entity> es=new LinkedList();
        Collection<Agent> ags=new LinkedList();
        Collection<Activity> acs=new LinkedList();
        Collection<Note> ns=new LinkedList();
        Collection<Object> lks=new LinkedList();
            
	if (records!=null) 
	    for (Object o: records) {
		if (o instanceof Agent) { ags.add((Agent)o); }
		else if (o instanceof Entity) { es.add((Entity)o); }
		else if (o instanceof Activity) { acs.add((Activity)o); }
		else if (o instanceof Note) { ns.add((Note)o); }
		else lks.add(o);
	    }
        String s_id=(String)id;

        NamedBundle c=pFactory.newNamedBundle(s_id,
					      acs,
					      es,
					      ags,
					      ns,
					      lks);

        System.out.println("Bundle namespaces " + namespaceTable);
        c.setNss(namespaceTable);
        return c;
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
        } else {
            QName attr1_QNAME = new QName(getNamespace(prefix),
                                          local,
                                          prefix);
        
            //return new JAXBElement<TypedLiteral>(attr1_QNAME, TypedLiteral.class, null, (TypedLiteral)value);

            return new JAXBElement<Object>(attr1_QNAME, Object.class, null, value);
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

    public Object convertInt(int i) {
        return i;
    }


    public Object convertUsed(Object id, Object id2,Object id1, Object time, Object uAttrs) {
        String s_id=(String)id;
        String s_id2=(String)id2;
        String s_id1=(String)id1;
        Activity a2=activityTable.get(s_id2);
        ActivityRef a2r=pFactory.newActivityRef(a2);
        Entity e1=entityTable.get(s_id1);
        EntityRef e1r=pFactory.newEntityRef(e1);
        Used u=pFactory.newUsed(s_id,
                                a2r,
                                null,
                                e1r);
        List attrs=(List)uAttrs;
        u.getAny().addAll(attrs);

        if (time!=null) {
            u.setTime(pFactory.newISOTime((String)time));
        }
        return u;
    }

    
    public Object convertWasGeneratedBy(Object id, Object id2,Object id1, Object time, Object gAttrs) {
        String s_id=(String)id;
        String s_id2=(String)id2;
        String s_id1=(String)id1;
        Activity a1=(s_id1==null)? null: activityTable.get(s_id1);  //id1 may be null
        ActivityRef a1r=null;
        if (a1!=null) a1r=pFactory.newActivityRef(a1);
        Entity e2=entityTable.get(s_id2);
        EntityRef e2r=pFactory.newEntityRef(e2);

        WasGeneratedBy g=pFactory.newWasGeneratedBy(s_id,
                                                    e2r,
                                                    null,
                                                    a1r);
        List attrs=(List)gAttrs;
        if (attrs!=null) g.getAny().addAll(attrs);
        if (time!=null) {
            g.setTime(pFactory.newISOTime((String)time));
        }
            
        return g;
    }

    public Object convertWasStartedBy(Object id, Object id2,Object id1, Object time, Object gAttrs) {
        String s_id=(String)id;
        String s_id2=(String)id2;
        String s_id1=(String)id1;
        Entity e1=(s_id1==null)? null: entityTable.get(s_id1);  //id1 may be null
        EntityRef e1r=null;
        if (e1!=null) e1r=pFactory.newEntityRef(e1);
        Activity a2=activityTable.get(s_id2);
        ActivityRef a2r=pFactory.newActivityRef(a2);

        WasStartedBy s=pFactory.newWasStartedBy(s_id,
                                                a2r,
                                                e1r);
        List attrs=(List)gAttrs;
        s.getAny().addAll(attrs);
        if (time!=null) {
            s.setTime(pFactory.newISOTime((String)time));
        }
            
        return s;
    }

    public Object convertWasEndedBy(Object id, Object id2,Object id1, Object time, Object gAttrs) {
        String s_id=(String)id;
        String s_id2=(String)id2;
        String s_id1=(String)id1;
        Entity e1=(s_id1==null)? null: entityTable.get(s_id1);  //id1 may be null
        EntityRef e1r=null;
        if (e1!=null) e1r=pFactory.newEntityRef(e1);
        Activity a2=activityTable.get(s_id2);
        ActivityRef a2r=pFactory.newActivityRef(a2);

        WasEndedBy s=pFactory.newWasEndedBy(s_id,
                                            a2r,
                                            e1r);
        List attrs=(List)gAttrs;
        s.getAny().addAll(attrs);
        if (time!=null) {
            s.setTime(pFactory.newISOTime((String)time));
        }
            
        return s;
    }

    public Object convertWasInvalidatedBy(Object id, Object id2,Object id1, Object time, Object gAttrs) {
        String s_id=(String)id;
        String s_id2=(String)id2;
        String s_id1=(String)id1;
        Activity a1=(s_id1==null)? null: activityTable.get(s_id1);  //id1 may be null
        ActivityRef a1r=null;
        if (a1!=null) a1r=pFactory.newActivityRef(a1);
        Entity e2=entityTable.get(s_id2);
        EntityRef e2r=pFactory.newEntityRef(e2);

        WasInvalidatedBy g=pFactory.newWasInvalidatedBy(s_id,
                                                        e2r,
                                                        a1r);
        List attrs=(List)gAttrs;
        if (attrs!=null) g.getAny().addAll(attrs);
        if (time!=null) {
            g.setTime(pFactory.newISOTime((String)time));
        }
            
        return g;
    }


    public Object convertWasInformedBy(Object id, Object id2, Object id1, Object aAttrs) {
        String s_id=(String)id;
        String s_id2=(String)id2;
        String s_id1=(String)id1;
        Activity e1=activityTable.get(s_id1); 
        ActivityRef e1r=pFactory.newActivityRef(e1);
        Activity a2=activityTable.get(s_id2);
        ActivityRef a2r=pFactory.newActivityRef(a2);

        WasInformedBy s=pFactory.newWasInformedBy(s_id,
                                                  a2r,
                                                  e1r);
        List attrs=(List)aAttrs;
        s.getAny().addAll(attrs);

        return s;
    }

    public Object convertWasStartedByActivity(Object id, Object id2, Object id1, Object aAttrs) {
        String s_id=(String)id;
        String s_id2=(String)id2;
        String s_id1=(String)id1;
        Activity e1=activityTable.get(s_id1); 
        ActivityRef e1r=pFactory.newActivityRef(e1);
        Activity a2=activityTable.get(s_id2);
        ActivityRef a2r=pFactory.newActivityRef(a2);

        WasStartedByActivity s=pFactory.newWasStartedByActivity(s_id,
                                                                a2r,
                                                                e1r);
        List attrs=(List)aAttrs;
        s.getAny().addAll(attrs);

        return s;
    }



    public Object convertWasAttributedTo(Object id, Object id2,Object id1, Object gAttrs) {
        String s_id=(String)id;
        String s_id2=(String)id2;
        String s_id1=(String)id1;
        Agent ag1=agentTable.get(s_id1);
        AgentRef ag1r=pFactory.newAgentRef(ag1);

        Entity e2=entityTable.get(s_id2);
        EntityRef e2r=pFactory.newEntityRef(e2);

        WasAttributedTo s=pFactory.newWasAttributedTo(s_id,
                                                      e2r,
                                                      ag1r);
        return s;
    }

    public Object convertWasDerivedFrom(Object id, Object id2,Object id1, Object a, Object g2, Object u1, Object dAttrs) {
        String s_id=(String)id;
        String s_id2=(String)id2;
        String s_id1=(String)id1;
        Entity e2=entityTable.get(s_id2);
        EntityRef e2r=pFactory.newEntityRef(e2);
        Entity e1=entityTable.get(s_id1);
        EntityRef e1r=pFactory.newEntityRef(e1);
        WasDerivedFrom d=pFactory.newWasDerivedFrom(s_id,
                                                    e2r,
                                                    e1r);
        if (a!=null) d.setActivity(pFactory.newActivityRef((String)a));
        if (g2!=null) d.setGeneration(pFactory.newDependencyRef((String)g2));
        if (u1!=null) d.setUsage(pFactory.newDependencyRef((String)u1));

        List attrs=(List)dAttrs;
        d.getAny().addAll(attrs);


        return d;
    }

    public Object convertWasRevisionOf(Object id, Object id2,Object id1, Object a, Object g2, Object u1, Object dAttrs) {
        String s_id=(String)id;
        String s_id2=(String)id2;
        String s_id1=(String)id1;

        Entity e2=entityTable.get(s_id2);
        EntityRef e2r=pFactory.newEntityRef(e2);
        Entity e1=entityTable.get(s_id1);
        EntityRef e1r=pFactory.newEntityRef(e1);

        WasRevisionOf d=pFactory.newWasRevisionOf(s_id,
                                                  e2r,
                                                  e1r);

        if (a!=null) d.setActivity(pFactory.newActivityRef((String)a));
        if (g2!=null) d.setGeneration(pFactory.newDependencyRef((String)g2));
        if (u1!=null) d.setUsage(pFactory.newDependencyRef((String)u1));
        List attrs=(List)dAttrs;
        d.getAny().addAll(attrs);

        return d;
    }

    public Object convertWasQuotedFrom(Object id, Object id2,Object id1, Object ag2, Object ag1, Object dAttrs) {
        String s_id=(String)id;
        String s_id2=(String)id2;
        String s_id1=(String)id1;
        String s_ag2=(String)ag2;
        String s_ag1=(String)ag1;
        Entity e2=entityTable.get(s_id2);
        EntityRef e2r=pFactory.newEntityRef(e2);
        Entity e1=entityTable.get(s_id1);
        EntityRef e1r=pFactory.newEntityRef(e1);

        AgentRef agr2=(s_ag2==null)? null : pFactory.newAgentRef(s_ag2);
        AgentRef agr1=(s_ag1==null)? null : pFactory.newAgentRef(s_ag1);

        WasQuotedFrom d=pFactory.newWasQuotedFrom(s_id,
                                                  e2r,
                                                  e1r,
                                                  agr2,
                                                  agr1);
        List attrs=(List)dAttrs;
        d.getAny().addAll(attrs);
        return d;
    }

    public Object convertHadOriginalSource(Object id, Object id2,Object id1, Object dAttrs) {
        String s_id=(String)id;
        String s_id2=(String)id2;
        String s_id1=(String)id1;
        Entity e2=entityTable.get(s_id2);
        EntityRef e2r=pFactory.newEntityRef(e2);
        Entity e1=entityTable.get(s_id1);
        EntityRef e1r=pFactory.newEntityRef(e1);

        HadOriginalSource d=pFactory.newHadOriginalSource(s_id,
                                                          e2r,
                                                          e1r);
        List attrs=(List)dAttrs;
        d.getAny().addAll(attrs);
        return d;
    }
    public Object convertTracedTo(Object id, Object id2, Object id1, Object dAttrs) {
        String s_id=(String)id;
        String s_id2=(String)id2;
        String s_id1=(String)id1;
        Entity e2=entityTable.get(s_id2);
        EntityRef e2r=pFactory.newEntityRef(e2);
        Entity e1=entityTable.get(s_id1);
        EntityRef e1r=pFactory.newEntityRef(e1);

        TracedTo d=pFactory.newTracedTo(s_id,
                                        e2r,
                                        e1r);
        List attrs=(List)dAttrs;
        d.getAny().addAll(attrs);
        return d;
    }


    public Object convertAlternateOf(Object id2,Object id1) {
        String s_id2=(String)id2;
        String s_id1=(String)id1;
        Entity e2=entityTable.get(s_id2);
        EntityRef e2r=pFactory.newEntityRef(e2);
        Entity e1=entityTable.get(s_id1);
        EntityRef e1r=pFactory.newEntityRef(e1);
        AlternateOf wco=pFactory.newAlternateOf(e2r,
                                                e1r);
        return wco;

    }

    public Object convertSpecializationOf(Object id2,Object id1) {
        String s_id2=(String)id2;
        String s_id1=(String)id1;
        Entity e2=entityTable.get(s_id2);
        EntityRef e2r=pFactory.newEntityRef(e2);
        Entity e1=entityTable.get(s_id1);
        EntityRef e1r=pFactory.newEntityRef(e1);
        SpecializationOf wco=pFactory.newSpecializationOf(e2r,
                                                          e1r);
        return wco;

    }


    public Object convertWasAssociatedWith(Object id, Object id2,Object id1, Object pl, Object aAttrs) {
        String s_id=(String)id;
        String s_id2=(String)id2;
        String s_id1=(String)id1;
        String s_pl=(String)pl;
        Activity e2=activityTable.get(s_id2);
        ActivityRef e2r=pFactory.newActivityRef(e2);
        Agent e1=null;
        AgentRef e1r=null;
        if (s_id1!=null) {
            e1=agentTable.get(s_id1);
            e1r=pFactory.newAgentRef(e1);
        }
        Entity e3=null;
        EntityRef e3r=null;
        if (s_pl!=null) {
            e3=entityTable.get(s_pl);
            e3r=pFactory.newEntityRef(e3);
        }
        WasAssociatedWith waw=pFactory.newWasAssociatedWith(s_id,
                                                            e2r,
                                                            e1r);
        waw.setPlan(e3r);
        List attrs=(List)aAttrs;
        waw.getAny().addAll(attrs);

        return waw;
    }

    public Object convertActedOnBehalfOf(Object id, Object id2,Object id1, Object a, Object aAttrs) {
        String s_id=(String)id;
        String s_id2=(String)id2;
        String s_id1=(String)id1;
        String s_a=(String)a;
        Agent e2=agentTable.get(s_id2);
        AgentRef e2r=pFactory.newAgentRef(e2);
        Agent e1=agentTable.get(s_id1);
        AgentRef e1r=pFactory.newAgentRef(e1);

        Activity e3=null;
        ActivityRef e3r=null;
        if (a!=null) {
            e3=activityTable.get(s_a);
            e3r=pFactory.newActivityRef(e3);
        }
        ActedOnBehalfOf aobo=pFactory.newActedOnBehalfOf(s_id,
                                                         e2r,
                                                         e1r,
                                                         e3r);
        List attrs=(List)aAttrs;
        aobo.getAny().addAll(attrs);

        return aobo;
    }

    public Object convertQNAME(String qname) {
        return qname;
    }

    public Object convertIRI(String iri) {
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



        if ((datatype.equals("xsd:dateTime"))
            || (datatype.equals("rdf:XMLLiteral"))
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
        Object val=convertToJava(datatype,(String)value);
        //pFactory.newTypedLiteral(val);
        return val;
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
        List attrs=(List)dAttrs;
        dbif.getAny().addAll(attrs);

        List entries=(List)map;
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
    List attrs=(List)dAttrs;
        dbrf.getAny().addAll(attrs);

    return dbrf;
    }


    public Object convertMemberOf(Object id, Object id2, Object map, Object complete, Object dAttrs) {
        String s_id=(String)id;
        String s_id2=(String)id2;

        Entity e2=entityTable.get(s_id2);
        EntityRef e2r=pFactory.newEntityRef(e2);

    MemberOf mo=pFactory.newMemberOf(s_id,
                       e2r,
                       null);
    List attrs=(List)dAttrs;
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

    public Object convertNote(Object id, Object attrs) {
        String s_id=(String)id;
        List nAttrs=(List)attrs;
        Note n=pFactory.newNote(s_id);
        noteTable.put(s_id,n);
        n.getAny().addAll(nAttrs);
        return n;
    }

    public Object convertHasAnnotation(Object something, Object note) {
        String s_id2=(String)something;
        String s_id1=(String)note;

        HasAnnotation han=pFactory.newHasAnnotation(s_id2,
                                                    s_id1);
        return han;
    }
 

}

