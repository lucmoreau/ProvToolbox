package org.openprovenance.prov.asn;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Hashtable;

import org.openprovenance.prov.xml.ProvFactory;
import org.openprovenance.prov.xml.Activity;
import org.openprovenance.prov.xml.ActivityRef;
import org.openprovenance.prov.xml.Entity;
import org.openprovenance.prov.xml.EntityRef;
import org.openprovenance.prov.xml.Agent;
import org.openprovenance.prov.xml.Account;
import org.openprovenance.prov.xml.Container;
import org.openprovenance.prov.xml.Used;
import org.openprovenance.prov.xml.WasGeneratedBy;
import org.openprovenance.prov.xml.WasDerivedFrom;

import  org.antlr.runtime.CommonTokenStream;
import  org.antlr.runtime.ANTLRFileStream;
import  org.antlr.runtime.CharStream;
import  org.antlr.runtime.Token;
import  org.antlr.runtime.tree.Tree;
import  org.antlr.runtime.tree.CommonTree;
import  org.antlr.runtime.tree.CommonTreeAdaptor;
import  org.antlr.runtime.tree.TreeAdaptor;


public  class ProvConstructor {
    ProvFactory pFactory;

    Hashtable<String,Entity>   entityTable   = new Hashtable<String,Entity>();
    Hashtable<String,Activity> activityTable = new Hashtable<String,Activity>();
    Hashtable<String,Agent>    agentTable    = new Hashtable<String,Agent>();
    
    public ProvConstructor(ProvFactory pFactory) {
        this.pFactory=pFactory;
    }
            

    public String getTokenString(Tree t) {
        if (t==null) return null;
        return ((CommonTree)t).getToken().getText();
    }
    public void walkToken(String token) {
        System.out.print(token);
    }
    public String convertToken(String token) {
        System.out.print(token);
        return token;
    }
    
    public void walk(Tree ast) {
        switch(ast.getType()) {
		case ASNParser.ACTIVITY:
			System.out.print("ACTIVITY ");
			walk(ast.getChild(0));
			walk(ast.getChild(1));
			walk(ast.getChild(2));
			walk(ast.getChild(3));
			walk(ast.getChild(4));
			System.out.print(";\n");
			break;
		case ASNParser.ENTITY:
			System.out.print("entity ");
			walk(ast.getChild(0));
			walk(ast.getChild(1));
			System.out.print(";\n");
			break;
		case ASNParser.CONTAINER:
			System.out.print("container ");
			for (int i=0; i< ast.getChildCount(); i++) {
			    walk(ast.getChild(i));
			}
			System.out.print(";\n");
			break;
		case ASNParser.ATTRIBUTES:
			System.out.print("ATTRIBUTES ");
			for (int i=0; i< ast.getChildCount(); i++) {
			    walk(ast.getChild(i));
			}
			break;
		case ASNParser.ID:
			System.out.print("ID ");
			walkToken(getTokenString(ast.getChild(0)));
			System.out.print(" ");
			break;
		case ASNParser.ATTRIBUTE:
			System.out.print("ATTRIBUTE ");
			walkToken(getTokenString(ast.getChild(0)));
			System.out.print(" ");
			break;
		case ASNParser.START:
			System.out.print("START ");
			walkToken(getTokenString(ast.getChild(0)));
			System.out.print(" ");
			break;
		case ASNParser.END:
			System.out.print("END ");
			walkToken(getTokenString(ast.getChild(0)));
			System.out.print(" ");
			break;
		case ASNParser.A:
            if (ast.getChildCount()==0) {
                walkToken(null);
            } else {
                walk(ast.getChild(0));
            }
			System.out.print(" ");
			break;
		case ASNParser.STRING:
			System.out.print("STRING ");
			walk(ast.getChild(0));
			System.out.print(" ");
			break;
		case ASNParser.USED:
			System.out.print("USED ");
			walk(ast.getChild(0));
			walk(ast.getChild(1));
			walk(ast.getChild(2));
			walk(ast.getChild(3));
			System.out.print(";\n");
			break;
		case ASNParser.WGB:
			System.out.print("WGB ");
			walk(ast.getChild(0));
			walk(ast.getChild(1));
			walk(ast.getChild(2));
			walk(ast.getChild(3));
			System.out.print(";\n");
			break;
		case ASNParser.WDF:
			System.out.print("WDF ");
			walk(ast.getChild(0));
			walk(ast.getChild(1));
			walk(ast.getChild(2));
			walk(ast.getChild(3));
			walk(ast.getChild(4));
			System.out.print(";\n");
			break;


            // ...handle every other possible node type in the AST...
        }
    }

    public Object convert(Tree ast) {
        switch(ast.getType()) {
		case ASNParser.ACTIVITY:
			System.out.print("ACTIVITY ");
			String id=(String)convert(ast.getChild(0));
			convert(ast.getChild(1));
			convert(ast.getChild(2));
			convert(ast.getChild(3));
			convert(ast.getChild(4));
            Activity a=pFactory.newActivity(id);
            activityTable.put(id,a);
			System.out.print(";\n");
			return a;
		case ASNParser.ENTITY:
			System.out.print("entity ");
			id=(String)convert(ast.getChild(0));
			convert(ast.getChild(1));
            Entity e=pFactory.newEntity(id);
            entityTable.put(id,e);
			System.out.print(";\n");
			return e;
		case ASNParser.CONTAINER:
			System.out.print("container ");
            Collection<Account> accs=new LinkedList();
            Collection<Entity> es=new LinkedList();
            Collection<Agent> ags=new LinkedList();
            Collection<Activity> acs=new LinkedList();
            Collection<Object> lks=new LinkedList();

			for (int i=0; i< ast.getChildCount(); i++) {
			    Object o=convert(ast.getChild(i));
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
			System.out.print(";\n");
			return c;
		case ASNParser.ATTRIBUTES:
			System.out.print("ATTRIBUTES ");
			for (int i=0; i< ast.getChildCount(); i++) {
			    convert(ast.getChild(i));
			}
			return null;
		case ASNParser.ID:
			return convertToken(getTokenString(ast.getChild(0)));
		case ASNParser.ATTRIBUTE:
			System.out.print("ATTRIBUTE ");
			convertToken(getTokenString(ast.getChild(0)));
			System.out.print(" ");
			return null;
		case ASNParser.START:
			System.out.print("START ");
			convertToken(getTokenString(ast.getChild(0)));
			System.out.print(" ");
			return null;
		case ASNParser.END:
			System.out.print("END ");
			convertToken(getTokenString(ast.getChild(0)));
			System.out.print(" ");
			return null;
		case ASNParser.A:
            if (ast.getChildCount()==0) {
                convertToken(null);
            } else {
                convert(ast.getChild(0));
            }
			System.out.print(" ");
			return null;
		case ASNParser.STRING:
			System.out.print("STRING ");
			convert(ast.getChild(0));
			System.out.print(" ");
			return null;
		case ASNParser.USED:
			System.out.print("USED ");
			String id2=(String)convert(ast.getChild(0));
			String id1=(String)(convert(ast.getChild(1)));
            Activity a2=activityTable.get(id2);
            ActivityRef a2r=pFactory.newActivityRef(a2);
            Entity e1=entityTable.get(id1);
            EntityRef e1r=pFactory.newEntityRef(e1);
			convert(ast.getChild(2));
			convert(ast.getChild(3));
            Used u=pFactory.newUsed(null,
                                    a2r,
                                    null,
                                    e1r);
			System.out.print(";\n");
			return u;
		case ASNParser.WGB:
			System.out.print("WGB ");
			id2=(String)convert(ast.getChild(0));
			id1=(String)(convert(ast.getChild(1)));
            Activity a1=activityTable.get(id1);
            ActivityRef a1r=pFactory.newActivityRef(a1);
            Entity e2=entityTable.get(id2);
            EntityRef e2r=pFactory.newEntityRef(e2);

			convert(ast.getChild(2));
			convert(ast.getChild(3));
            WasGeneratedBy g=pFactory.newWasGeneratedBy(null,
                                                        e2r,
                                                        null,
                                                        a1r);

			System.out.print(";\n");
			return g;
		case ASNParser.WDF:
			System.out.print("WDF ");
			convert(ast.getChild(0));
			convert(ast.getChild(1));
			convert(ast.getChild(2));
			convert(ast.getChild(3));
			convert(ast.getChild(4));
			System.out.print(";\n");
			return null;


            // ...handle every other possible node type in the AST...
        }
        return null;
    }



}

