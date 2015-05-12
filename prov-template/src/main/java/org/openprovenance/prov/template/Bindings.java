package org.openprovenance.prov.template;

import java.util.Hashtable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.Other;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.ProvUtilities;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.model.TypedValue;

import static org.openprovenance.prov.template.Expand.TMPL_NS;
import static org.openprovenance.prov.template.Expand.TMPL_PREFIX;

public class Bindings {
    
    public static final String VALUE = "value_";
    public static final String VALUE2 = "2dvalue_";
    public static final String APP_VALUE = TMPL_NS+VALUE;
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
        Document result=pf.newDocument();
        
        List<StatementOrBundle> ll=new LinkedList<StatementOrBundle>();
        
        add1DValues(ll,variables);  
        add2Dvalues(ll,attributes);
        
        result.getStatementOrBundle().addAll(ll);
        result.setNamespace(pf.newNamespace(Namespace.gatherNamespaces(result)));
        
        return result;
    }

    public void add2Dvalues(List<StatementOrBundle> ll, Hashtable<QualifiedName, List<List<TypedValue>>> attributes) {
        for (Entry<QualifiedName, List<List<TypedValue>>> entry: attributes.entrySet()) {
            Entity e=pf.newEntity(entry.getKey());
            int count1=0;
            List<Other> attrs=new LinkedList<Other>();
            for (List<TypedValue> vals: entry.getValue()) {
                int count2=0;
                for (TypedValue val: vals) {
                    attrs.add(pf.newOther(TMPL_NS, VALUE2+count1+"_"+count2, TMPL_PREFIX, val.getValue(), val.getType()));
                    count2++;
                }
                count1++;
            }
            e.getOther().addAll(attrs);
            ll.add(e);       
        }
    }

    public void add1DValues(List<StatementOrBundle> ll, Hashtable<QualifiedName, List<QualifiedName>> variables) {
        for (Entry<QualifiedName, List<QualifiedName>> entry: variables.entrySet()) {
            Entity e=pf.newEntity(entry.getKey());
            int count=0;
            List<Attribute> attrs=new LinkedList<Attribute>();
            for (QualifiedName qn: entry.getValue()) {
                attrs.add(pf.newAttribute(TMPL_NS, VALUE+count, TMPL_PREFIX, qn, pf.getName().PROV_QUALIFIED_NAME));
                count++;
            }
            pf.setAttributes(e, attrs);
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
    
    static public <E> void set(int pos, List<E> ll, E val) {
        int size=ll.size();
        for (int i=size; i<=pos; i++) {
            ll.add(null);
        }
        ll.set(pos,val);
    }
	
}