package org.openprovenance.prov.asn;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Hashtable;
import java.net.URI;

import org.openprovenance.prov.xml.URIWrapper;
import org.openprovenance.prov.xml.ProvFactory;
import org.openprovenance.prov.xml.Activity;
import org.openprovenance.prov.xml.ActivityRef;
import org.openprovenance.prov.xml.Entity;
import org.openprovenance.prov.xml.Agent;
import org.openprovenance.prov.xml.EntityRef;
import org.openprovenance.prov.xml.AgentRef;
import org.openprovenance.prov.xml.Agent;
import org.openprovenance.prov.xml.Account;
import org.openprovenance.prov.xml.Container;
import org.openprovenance.prov.xml.Used;
import org.openprovenance.prov.xml.HadPlan;
import org.openprovenance.prov.xml.WasGeneratedBy;
import org.openprovenance.prov.xml.WasDerivedFrom;
import org.openprovenance.prov.xml.WasComplementOf;
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
        a.getAny().addAll(attrs);

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
        e.getAny().addAll(attrs);
        return e;
    }

    public Object convertAgent(Object id, Object eAttrs) {
        String s_id=(String)id;
        List attrs=(List)eAttrs;
        Agent e=pFactory.newAgent(s_id);
        //entityTable.put(s_id,e);  
        agentTable.put(s_id,e);
        e.getAny().addAll(attrs);
        return e;
    }

    public Object convertContainer(Object namespaces, List<Object> records) {    
        Collection<Account> accs=new LinkedList();
        Collection<Entity> es=new LinkedList();
        Collection<Agent> ags=new LinkedList();
        Collection<Activity> acs=new LinkedList();
        Collection<Object> lks=new LinkedList();
            
        for (Object o: records) {
            if (o instanceof Agent) { ags.add((Agent)o); }
            else if (o instanceof Entity) { es.add((Entity)o); }
            else if (o instanceof Activity) { acs.add((Activity)o); }
            else lks.add(o);
        }
        Container c=pFactory.newContainer(accs,
                                          acs,
                                          es,
                                          ags,
                                          lks);
        System.out.println("Container namespaces " + namespaceTable);
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
        if (prefix.equals("prov")) return "http://openprovenance.org/prov-xml#";
        if (prefix.equals("xsd")) return "http://www.w3.org/2001/XMLSchema";
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
    
    public Object convertA(Object a) {
        return a;
    }

    public Object convertG(Object a) {
        return a;
    }

    public Object convertU(Object a) {
        return a;
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
        Activity a1=activityTable.get(s_id1);
        ActivityRef a1r=pFactory.newActivityRef(a1);
        Entity e2=entityTable.get(s_id2);
        EntityRef e2r=pFactory.newEntityRef(e2);

        WasGeneratedBy g=pFactory.newWasGeneratedBy(s_id,
                                                    e2r,
                                                    null,
                                                    a1r);
        List attrs=(List)gAttrs;
        g.getAny().addAll(attrs);
        if (time!=null) {
            g.setTime(pFactory.newISOTime((String)time));
        }
            
        return g;
    }

    public Object convertWasDerivedFrom(Object id2,Object id1, Object a, Object g2, Object u1, Object dAttrs) {
        String s_id2=(String)id2;
        String s_id1=(String)id1;
        Entity e2=entityTable.get(s_id2);
        EntityRef e2r=pFactory.newEntityRef(e2);
        Entity e1=entityTable.get(s_id1);
        EntityRef e1r=pFactory.newEntityRef(e1);
        WasDerivedFrom d=pFactory.newWasDerivedFrom((QName)null,
                                                    e2r,
                                                    e1r);
        if (a!=null) d.setActivity(pFactory.newActivityRef((String)a));
        if (g2!=null) d.setGeneration(pFactory.newDependencyRef((String)g2));
        if (u1!=null) d.setUsage(pFactory.newDependencyRef((String)u1));

        List attrs=(List)dAttrs;
        d.getAny().addAll(attrs);


        return d;
    }

    public Object convertWasComplementOf(Object id, Object id2,Object id1, Object aAttrs) {
        String s_id=(String)id;
        String s_id2=(String)id2;
        String s_id1=(String)id1;
        Entity e2=entityTable.get(s_id2);
        EntityRef e2r=pFactory.newEntityRef(e2);
        Entity e1=entityTable.get(s_id1);
        EntityRef e1r=pFactory.newEntityRef(e1);
        WasComplementOf wco=pFactory.newWasComplementOf(s_id,
                                                        e2r,
                                                        e1r);
        List attrs=(List)aAttrs;
        wco.getAny().addAll(attrs);

        return wco;

    }


    public Object convertWasAssociatedWith(Object id, Object id2,Object id1, Object aAttrs) {
        String s_id=(String)id;
        String s_id2=(String)id2;
        String s_id1=(String)id1;
        Activity e2=activityTable.get(s_id2);
        ActivityRef e2r=pFactory.newActivityRef(e2);
        Agent e1=agentTable.get(s_id1);
        AgentRef e1r=pFactory.newAgentRef(e1);
        WasAssociatedWith waw=pFactory.newWasAssociatedWith(s_id,
                                                            e2r,
                                                            e1r);
        List attrs=(List)aAttrs;
        waw.getAny().addAll(attrs);

        return waw;
    }

    public Object convertHadPlan(Object id, Object id2,Object id1, Object aAttrs) {
        String s_id=(String)id;
        String s_id2=(String)id2;
        String s_id1=(String)id1;
        Activity a2=activityTable.get(s_id2);
        ActivityRef a2r=pFactory.newActivityRef(a2);
        Entity e1=entityTable.get(s_id1);

	EntityRef e1r=pFactory.newEntityRef(e1);
	HadPlan hp=pFactory.newHadPlan(s_id,
				       a2r,
				       e1r);
        List attrs=(List)aAttrs;
        hp.getAny().addAll(attrs);
        return hp;
    }



    public Object convertQNAME(String qname) {
        return qname;
    }

    public Object convertIRI(String iri) {
        iri=unwrap(iri);
        return URI.create(iri);
    }


    public Object convertToJava(String datatype, String value) {
	value=unwrap(value);
	//System.out.println("convertToJava: datatype " + datatype + "  " + value);
	if (datatype.equals("xsd:string")) return value;
	if (datatype.equals("xsd:int")) return Integer.parseInt(value);
	if (datatype.equals("xsd:double")) return Double.parseDouble(value);
	if (datatype.equals("xsd:boolean")) return Boolean.parseBoolean(value);
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

	throw new UnsupportedOperationException("Unknown literal type " + datatype);
    //return value;
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

 

}

