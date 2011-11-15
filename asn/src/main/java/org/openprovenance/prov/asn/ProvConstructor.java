package org.openprovenance.prov.asn;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Hashtable;

import org.openprovenance.prov.xml.ProvFactory;
import org.openprovenance.prov.xml.Activity;
import org.openprovenance.prov.xml.Attributes;
import org.openprovenance.prov.xml.ActivityRef;
import org.openprovenance.prov.xml.Entity;
import org.openprovenance.prov.xml.EntityRef;
import org.openprovenance.prov.xml.Agent;
import org.openprovenance.prov.xml.Account;
import org.openprovenance.prov.xml.Container;
import org.openprovenance.prov.xml.Used;
import org.openprovenance.prov.xml.WasGeneratedBy;
import org.openprovenance.prov.xml.WasDerivedFrom;
import org.openprovenance.prov.xml.TypedLiteral;

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
    
    public ProvConstructor(ProvFactory pFactory) {
        this.pFactory=pFactory;
    }
            

    public Object convertActivity(Object id,Object recipe,Object startTime,Object endTime, Object aAttrs) {
	String s_id=(String)id;
	String s_recipe=(String)recipe;
	String s_startTime=(String)startTime;
	String s_endTime=(String)endTime;
	Attributes attrs=(Attributes)aAttrs;
        Activity a=pFactory.newActivity(s_id);
        activityTable.put(s_id,a);
	a.setAttributes(attrs);
	return a;
    }

    public String unwrap (String s) {
	return s.substring(1,s.length()-1);
    }
			   
    public Object convertEntity(Object id, Object eAttrs) {
	String s_id=(String)id;
	Attributes attrs=(Attributes)eAttrs;
	Entity e=pFactory.newEntity(s_id);
	entityTable.put(s_id,e);
	e.setAttributes(attrs);
	return e;
    }

    public Object convertContainer(List<Object> records) {    
	Collection<Account> accs=new LinkedList();
	Collection<Entity> es=new LinkedList();
	Collection<Agent> ags=new LinkedList();
	Collection<Activity> acs=new LinkedList();
	Collection<Object> lks=new LinkedList();
			
	for (Object o: records) {
            if (o instanceof Entity) { es.add((Entity)o); }
            else if (o instanceof Agent) { ags.add((Agent)o); }
            else if (o instanceof Activity) { acs.add((Activity)o); }
            else lks.add(o);
	}
	Container c=pFactory.newContainer(accs,
                                          acs,
                                          es,
                                          ags,
                                          lks);
	return c;
    }

    public Object convertAttributes(List<Object> attributeList) {
	Attributes attrs=pFactory.newAttributes();
        for (Object o: attributeList) {
            attrs.getAny().add(o);
	}
	return attrs;
    }
    
    public Object convertId(String id) {
	return id;
    }

    public Object convertAttribute(Object name, Object value) {
	String attr1=(String)name;
	if (value instanceof String) {
	    String val1=(String)(value);
	    return pFactory.newAttribute("http://todo.org/soon",
					 attr1.substring(0,attr1.indexOf(':')),
					 attr1.substring(attr1.indexOf(':')+1,attr1.length()),
					 unwrap(val1));
	} else {
	    QName attr1_QNAME = new QName("http://todo.org/soon2",
					  attr1.substring(0,attr1.indexOf(':')),
					  attr1.substring(attr1.indexOf(':')+1,attr1.length()));
 		
	    return new JAXBElement<TypedLiteral>(attr1_QNAME, TypedLiteral.class, null, (TypedLiteral)value);
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

    public Object convertString(String s) {
	return s;
    }


    public Object convertUsed(Object id2,Object id1, Object uAttrs, Object time) {
	String s_id2=(String)id2;
	String s_id1=(String)id1;
        Activity a2=activityTable.get(s_id2);
        ActivityRef a2r=pFactory.newActivityRef(a2);
        Entity e1=entityTable.get(s_id1);
        EntityRef e1r=pFactory.newEntityRef(e1);
        Used u=pFactory.newUsed(null,
                                a2r,
                                null,
                                e1r);
        Attributes attrs=(Attributes)uAttrs;
        u.setAttributes(attrs);
	return u;
    }
    
    public Object convertWasGeneratedBy(Object id2,Object id1, Object gAttrs, Object time) {
	String s_id2=(String)id2;
	String s_id1=(String)id1;
        Activity a1=activityTable.get(s_id1);
        ActivityRef a1r=pFactory.newActivityRef(a1);
        Entity e2=entityTable.get(s_id2);
        EntityRef e2r=pFactory.newEntityRef(e2);

        WasGeneratedBy g=pFactory.newWasGeneratedBy(null,
                                                    e2r,
                                                    null,
                                                    a1r);
        Attributes attrs=(Attributes)gAttrs;
        g.setAttributes(attrs);
	return g;
    }

    public Object convertWasDerivedFrom(Object id2,Object id1, Object pe, Object q2, Object q1) {
	String s_id2=(String)id2;
	String s_id1=(String)id1;
        Entity e2=entityTable.get(s_id2);
        EntityRef e2r=pFactory.newEntityRef(e2);
        Entity e1=entityTable.get(s_id1);
        EntityRef e1r=pFactory.newEntityRef(e1);
        WasDerivedFrom d=pFactory.newWasDerivedFrom(null,
                                                    e2r,
                                                    e1r);
	return d;
    }


    public Object convertQNAME(String qname) {
	return qname;
    }

    public Object convertIRI(String iri) {
	iri=unwrap(iri);
	return iri;
    }

    public Object convertTypedLiteral(String datatype, Object value) {
	String v2=(String)value;
	datatype=unwrap(datatype);
	return pFactory.newTypedLiteral(datatype,v2);
    }

}

