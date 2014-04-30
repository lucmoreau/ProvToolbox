package org.openprovenance.prov.template;

import java.util.LinkedList;
import java.util.List;
import org.openprovenance.prov.model.QualifiedName;

public class Groupings /* implements Iterable<Binding> */ {
    final private List<List<QualifiedName>> variables;

    
    public Groupings() {
	variables=new LinkedList<List<QualifiedName>>();
    }
    
    public List<QualifiedName> get(int group) {
	return variables.get(group);
    }
    
    public int size() {
	return variables.size();
    }
   
    public void addVariable(QualifiedName name) {
	List<QualifiedName> ll=new LinkedList<QualifiedName>();
	ll.add(name);
	variables.add(ll);
    }

    
    public void addVariable(int group, QualifiedName name) {
	List<QualifiedName> v=variables.get(group);
	v.add(name);
    }
    
    
 
    
    @Override
    public String toString () {
	return "" + variables;
    }

	
}