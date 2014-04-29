package org.openprovenance.prov.template;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.TypedValue;

public class Groupings implements Iterable<Binding> {
    final private List<List<QualifiedName>> variables;

    
    public Groupings() {
	variables=new LinkedList<List<QualifiedName>>();
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
    
    public List<QualifiedName> get(List<Integer> index) {
	List<QualifiedName> result=new LinkedList<QualifiedName>();
	int count=0;
	for (int i: index) {
	    result.add(variables.get(count).get(i));
	    count++;
	}
	return result;
    }
    
    public List<List<Integer>> enumerateAll(List<Integer> index) {
	//TODO: here
	return null;
    }
    
    class GroupingIterator implements Iterator<Binding> {
	

	@Override
	public boolean hasNext() {
	    if (first) {
		boolean val=iterator1.hasNext();
		if (val) return val;
	    }
	    return iterator2.hasNext();
	}
	 

	@Override
	public Binding next() {
	    if (first) {
		boolean val=iterator1.hasNext();
		if (val) return new VariableBinding(iterator1.next());
	    } 
	    return new AttributeBinding(iterator2.next());    
	}
	

	@Override
	public void remove() {
	    throw new UnsupportedOperationException();	    
	}
	
	private boolean first;
	private Iterator<Entry<QualifiedName, List<QualifiedName>>> iterator1;
	private Iterator<Entry<QualifiedName, List<List<TypedValue>>>> iterator2;

	public GroupingIterator(Groupings b) {
	    first=true;
	   // iterator1=variables.iterator();
	   // iterator2=b.getAttributes().entrySet().iterator();
	}
	
    }
    @Override
    public String toString () {
	return "" + variables;
    }


    @Override
    public Iterator<Binding> iterator() {
	return new GroupingIterator(this);
    }
    
	
}