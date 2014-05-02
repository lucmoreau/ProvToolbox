package org.openprovenance.prov.template;

import java.util.Hashtable;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.model.TypedValue;

import static org.openprovenance.prov.template.Expand.APP_NS;

public class Bindings {
    
    final private Hashtable<QualifiedName, List<QualifiedName>> variables;
    final private Hashtable<QualifiedName, List<List<TypedValue>>> attributes;
    
    ProvFactory pf=new org.openprovenance.prov.xml.ProvFactory();

    public Bindings() {
	this(new Hashtable<QualifiedName, List<QualifiedName>>(), 
	     new Hashtable<QualifiedName, List<List<TypedValue>>>());
    }
    
    public Bindings(Hashtable<QualifiedName, List<QualifiedName>> variables,
                    Hashtable<QualifiedName, List<List<TypedValue>>> attributes) {
	this.variables=variables;
	this.attributes=attributes;
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
        
        for (Entry<QualifiedName, List<QualifiedName>> entry: variables.entrySet()) {
            Entity e=pf.newEntity(entry.getKey());
            int count=0;
            List<Attribute> attrs=new LinkedList<Attribute>();
            for (QualifiedName qn: entry.getValue()) {
                attrs.add(pf.newAttribute(APP_NS, "value"+count, "app", qn, pf.getName().XSD_QNAME));
                count++;
            }
            pf.setAttributes(e, attrs);
            ll.add(e);       
        }
        
        for (Entry<QualifiedName, List<List<TypedValue>>> entry: attributes.entrySet()) {
            Entity e=pf.newEntity(entry.getKey());
            int count1=0;
            List<Attribute> attrs=new LinkedList<Attribute>();
            for (List<TypedValue> vals: entry.getValue()) {
                int count2=0;
                for (TypedValue val: vals) {
                    attrs.add(pf.newAttribute(APP_NS, "value"+count1+","+count2, "app", val.getValue(), val.getType()));
                    count2++;
                }
                count1++;
            }
            pf.setAttributes(e, attrs);
            ll.add(e);       
        }
        
        result.getStatementOrBundle().addAll(ll);
        result.setNamespace(Namespace.gatherNamespaces(result));
        
        return result;
    }
    
	
}