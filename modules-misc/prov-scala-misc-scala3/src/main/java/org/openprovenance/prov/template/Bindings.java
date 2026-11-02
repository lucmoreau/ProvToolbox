package org.openprovenance.prov.template;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.Bundle;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.Other;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.ProvUtilities;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.model.TypedValue;

import static org.openprovenance.prov.template.ExpandUtil.TMPL_NS;
import static org.openprovenance.prov.template.ExpandUtil.TMPL_PREFIX;

public class Bindings {
    
    public static final String VALUE = "value_";
    public static final String VALUE2 = "2dvalue_";
    public static final String APP_VALUE = TMPL_NS+VALUE;
    public static final String VALUE_v2 = "binding_";
    public static final String APP_VALUE_v2 = TMPL_NS+VALUE_v2;
    public static final String APP_VALUE2 = TMPL_NS+VALUE2;
    
    final private Hashtable<QualifiedName, List<QualifiedName>> variables;
    final private Hashtable<QualifiedName, List<List<TypedValue>>> attributes;
    
    final ProvFactory pf;
    static ProvUtilities u= new ProvUtilities();

    public Bindings(ProvFactory pf) {
	this(new Hashtable<QualifiedName, List<QualifiedName>>(), 
	     new Hashtable<QualifiedName, List<List<TypedValue>>>(),
	     pf);
    }
    
    public Bindings(Hashtable<QualifiedName, List<QualifiedName>> variables,
                    Hashtable<QualifiedName, List<List<TypedValue>>> attributes,
                    ProvFactory pf) {
	this.variables=variables;
	this.attributes=attributes;
	this.pf=pf;
    }

    @Override
    public boolean equals (Object o) {
        if (!(o instanceof Bindings)) return false;
        Bindings b=(Bindings)o;
        return b.variables.equals(this.variables)
                && b.attributes.equals(this.attributes);
        
    }


    public Hashtable<QualifiedName, List<QualifiedName>> getVariables() {
	return variables;
    }



    public Hashtable<QualifiedName, List<List<TypedValue>>> getAttributes() {
	return attributes;
    }
    
    
    public void addVariable(QualifiedName name, QualifiedName val) {
	List<QualifiedName> v=variables.get(name);
	if (v==null) {
	    variables.put(name, new LinkedList<QualifiedName>());
	}
	variables.get(name).add(val);
    }
    
    public void addAttribute(QualifiedName name, List<TypedValue> values) {
        List<List<TypedValue>> v=attributes.get(name);
        if (v==null) {
            attributes.put(name, new LinkedList<List<TypedValue>>());
        }
        attributes.get(name).add(values);
    }
    

    @Override
    public String toString () {
	return "[" + getVariables() + " -- " + getAttributes() + "]";
    }


    public Document toDocument () {
        
        List<Statement> ll=new LinkedList<Statement>();
        
        add1DValues(ll,variables);  
        add2Dvalues(ll,attributes);
        
        Document dummy=pf.newDocument(null,ll,new LinkedList<Bundle>());
        Document result=pf.newDocument(pf.newNamespace(Namespace.gatherNamespaces(dummy)),ll,new LinkedList<Bundle>());
        		
        
        return result;
    }
    
    public Document toDocument_v2 () {
        
        List<Statement> ll=new LinkedList<Statement>();
        
        add1DValues_v2(ll,variables);  
        add2Dvalues_v2(ll,attributes);
        
        Document dummy=pf.newDocument(null,ll,new LinkedList<Bundle>());
        Document result=pf.newDocument(pf.newNamespace(Namespace.gatherNamespaces(dummy)),ll,new LinkedList<Bundle>());
        		
        
        return result;
    }
    public void add2Dvalues(List<Statement> ll, Hashtable<QualifiedName, List<List<TypedValue>>> attributes) {
        for (Entry<QualifiedName, List<List<TypedValue>>> entry: attributes.entrySet()) {
            int count1=0;
            List<Attribute> attrs=new LinkedList<Attribute>();
            for (List<TypedValue> vals: entry.getValue()) {
                int count2=0;
                for (TypedValue val: vals) {
                    attrs.add(pf.newAttribute(TMPL_NS, VALUE2+count1+"_"+count2, TMPL_PREFIX, val.getValue(), val.getType()));
                    count2++;
                }
                count1++;
            }
            Entity e=pf.newEntity(entry.getKey(),attrs);

            ll.add(e);       
        }
    }
    public void add2Dvalues_v2(List<Statement> ll, Hashtable<QualifiedName, List<List<TypedValue>>> attributes) {
        for (Entry<QualifiedName, List<List<TypedValue>>> entry: attributes.entrySet()) {
            int count1=0;
            List<Attribute> attrs=new LinkedList<Attribute>();
            for (List<TypedValue> vals: entry.getValue()) {
                //int count2=0;
                for (TypedValue val: vals) {
                    attrs.add(pf.newAttribute(TMPL_NS, VALUE_v2+count1, TMPL_PREFIX, val.getValue(), val.getType()));
                    //count2++;
                }
                count1++;
            }
            Entity e=pf.newEntity(entry.getKey(),attrs);

            ll.add(e);       
        }
    }

    public void add1DValues(List<Statement> ll, Hashtable<QualifiedName, List<QualifiedName>> variables) {
        for (Entry<QualifiedName, List<QualifiedName>> entry: variables.entrySet()) {
            int count=0;
            List<Attribute> attrs=new LinkedList<Attribute>();
            for (QualifiedName qn: entry.getValue()) {
                attrs.add(pf.newAttribute(TMPL_NS, VALUE+count, TMPL_PREFIX, qn, pf.getName().PROV_QUALIFIED_NAME));
                count++;
            }
            Entity e=pf.newEntity(entry.getKey(),attrs);
            ll.add(e);       
        }
    }
    public void add1DValues_v2(List<Statement> ll, Hashtable<QualifiedName, List<QualifiedName>> variables) {
        for (Entry<QualifiedName, List<QualifiedName>> entry: variables.entrySet()) {
            int count=0;
            List<Attribute> attrs=new LinkedList<Attribute>();
            for (QualifiedName qn: entry.getValue()) {
                attrs.add(pf.newAttribute(TMPL_NS, VALUE_v2+count, TMPL_PREFIX, qn, pf.getName().PROV_QUALIFIED_NAME));
                count++;
            }
            Entity e=pf.newEntity(entry.getKey(),attrs);
            ll.add(e);       
        }
    }
    
    
    public static Bindings fromDocument(Document doc,ProvFactory pf) {
        Bindings result=new Bindings(pf);
        
        List<Entity> entities=u.getEntity(doc);
        for (Entity entity: entities) {
            Hashtable<Integer,QualifiedName> map=new Hashtable<Integer, QualifiedName>();
            Hashtable<Integer, Hashtable<Integer,TypedValue>> map2=new Hashtable<Integer, Hashtable<Integer,TypedValue>>();
            for (Other attr: entity.getOther()) {
                String uri = attr.getElementName().getUri();
                if (uri.startsWith(APP_VALUE)) {
                    Integer i=Integer.valueOf(uri.substring(APP_VALUE.length()));
                    if (attr.getValue() instanceof QualifiedName) {
                        map.put(i, (QualifiedName) attr.getValue());
                    }
                } else {
                    if (uri.startsWith(APP_VALUE2)) {
                        String index=uri.substring(APP_VALUE2.length());
                        String [] nums=index.split("_");
                        Integer i=Integer.valueOf(nums[0]);
                        Integer j=Integer.valueOf(nums[1]);
                        
                        Hashtable<Integer, TypedValue> row=map2.get(i);
                        if (row==null) map2.put(i, new Hashtable<Integer, TypedValue>());
                        map2.get(i).put(j,attr);
                    }
                }
            }
            ArrayList<QualifiedName> ll=new ArrayList<QualifiedName>(); 
            int size=map.entrySet().size();
            if (size>0) {
                for (Entry<Integer, QualifiedName> entry: map.entrySet()) {
                    set(entry.getKey(),ll,entry.getValue());
                }
                result.getVariables().put(entity.getId(),ll);
            } 
            
            int size2=map2.entrySet().size();
            if (size2>0) {
                List<List<TypedValue>> allvalues= new LinkedList<List<TypedValue>>();

                for (Entry<Integer, Hashtable<Integer, TypedValue>> entry1: map2.entrySet()) {
                    List<TypedValue> values= new LinkedList<TypedValue>();
                    for (Entry<Integer, TypedValue> entry2: entry1.getValue().entrySet()) {
                        set(entry2.getKey(),values,entry2.getValue());
                    }
                    set(entry1.getKey(),allvalues,values);
                }
                result.getAttributes().put(entity.getId(),allvalues);

            } 
            
            
        }
        
        return result;
    }
    

    public static Bindings fromDocument_v2(Document doc,ProvFactory pf) {
        Bindings result=new Bindings(pf);
        
        List<Entity> entities=u.getEntity(doc);
        for (Entity entity: entities) {
            Hashtable<Integer,QualifiedName> map=new Hashtable<Integer, QualifiedName>();
            //Hashtable<Integer, Hashtable<Integer,TypedValue>> map2=new Hashtable<Integer, Hashtable<Integer,TypedValue>>();
            Hashtable<Integer, Set<TypedValue>> map_v2=new Hashtable<Integer, Set<TypedValue>>();
            for (Other attr: entity.getOther()) {
                String uri = attr.getElementName().getUri();
                if (uri.startsWith(APP_VALUE_v2)) {
                    String index=uri.substring(APP_VALUE_v2.length());
                    Integer i=Integer.valueOf(index);
                    
                    Set<TypedValue> row=map_v2.get(i);
                    if (row==null) map_v2.put(i, new HashSet<TypedValue>());
                    map_v2.get(i).add(attr);
                } else {
                	if (uri.startsWith(APP_VALUE)) {
                    	Integer i=Integer.valueOf(uri.substring(APP_VALUE.length()));
                    	
                        Set<TypedValue> row=map_v2.get(i);
                        if (row==null) map_v2.put(i, new HashSet<TypedValue>());
                        map_v2.get(i).add(attr);                    
    	            }
                }            
            }
            
            // reconstructing the map data structure for variables uniquely mapped to qualified names.
            boolean single_map=true;
            for (int i: map_v2.keySet()) {
            	single_map=single_map && (map_v2.get(i).size()==1);
            	if (single_map) {
            		for (TypedValue tv: map_v2.get(i)) { // only one element to iterate over
        				single_map=(tv.getValue() instanceof QualifiedName) ;
            		}
            	}
            }
            if (single_map) {
            	for (int i: map_v2.keySet()) {
            		for (TypedValue tv: map_v2.get(i)) { // only one element to iterate over
                    	map.put(i, (QualifiedName) tv.getValue());
            		}	
            	}
            }
            
            ArrayList<QualifiedName> ll=new ArrayList<QualifiedName>(); 
            int size=map.entrySet().size();
            if (size>0) {
                for (Entry<Integer, QualifiedName> entry: map.entrySet()) {
                    set(entry.getKey(),ll,entry.getValue());
                }
                result.getVariables().put(entity.getId(),ll);
            } 
            
            int size2=map_v2.entrySet().size();
            if (size2>0) {
                List<List<TypedValue>> allvalues= new LinkedList<List<TypedValue>>();

                for (Entry<Integer, Set<TypedValue>> entry1: map_v2.entrySet()) {
                    List<TypedValue> values= new LinkedList<TypedValue>();
                    //convert to 2dvalue
                    int count=0;
                    for (TypedValue tv: entry1.getValue()) {
                    	if (tv instanceof Other) {
                    		Other o=(Other)tv;
                    		String uri = o.getElementName().getUri();
                    		if (uri.startsWith(APP_VALUE_v2)) {
                                 Integer i=Integer.valueOf(uri.substring(APP_VALUE_v2.length()));
                                 values.add(pf.newAttribute(TMPL_NS, VALUE2+i+"_"+count, TMPL_PREFIX, o.getValue(), o.getType()));
                    		}
                    	}
                    	count++;
                    }
                    
                    set(entry1.getKey(),allvalues,values);
                }
                result.getAttributes().put(entity.getId(),allvalues);

            } 
            
            
        }
        
        return result;
    }
    
    public void addVariableBindingsAsAttributeBindings() {
    	Hashtable<QualifiedName, List<QualifiedName>> vb=getVariables();
    	for(Entry<QualifiedName,List<QualifiedName>> entry: vb.entrySet()) {
    		int count=0;
    		for (QualifiedName qn: entry.getValue()) {
    			List<TypedValue> ll=new LinkedList<TypedValue>();
    			ll.add(pf.newAttribute(TMPL_NS, VALUE2+count+"_"+0, TMPL_PREFIX, qn, pf.getName().PROV_QUALIFIED_NAME));
    			count++;
    			addAttribute(entry.getKey(),ll);
    		}
    	}
    	
    }
    
    static public <E> void set(int pos, List<E> ll, E val) {
        int size=ll.size();
        for (int i=size; i<=pos; i++) {
            ll.add(null);
        }
        ll.set(pos,val);
    }
	
}
