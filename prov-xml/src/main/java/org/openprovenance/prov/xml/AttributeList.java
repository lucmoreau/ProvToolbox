package org.openprovenance.prov.xml;

import java.util.AbstractList;
import java.util.ArrayList;

import org.openprovenance.prov.model.HasLocation;
import org.openprovenance.prov.model.Identifiable;

public class AttributeList<TYPE> extends AbstractList<TYPE> {


    private final ArrayList<TYPE> ll=new ArrayList<TYPE>();
  
    private Identifiable obj;

    AttributeList(Identifiable obj) {
	this.obj=obj;
    }

    @Override
    public TYPE get(int index) {
        return ll.get(index);
    }

    @Override
    public TYPE set(int index, TYPE element) {
        TYPE oldValue = ll.get(index);
        ll.set(index,element);
        return oldValue;
    }

    @Override
    public int size() {
        return ll.size();
    }

    @Override
    public Object[] toArray() {
        return (Object[]) ll.toArray();
    }
    
    public void add(int index,TYPE element){
	System.out.println("*** AttributeList add " + index);
	ll.add(index,element);
	if (element instanceof Location) {
	    Location attr=(Location) element;
	    if (obj instanceof HasLocation) {
		System.out.println("*** AttributeList adding location ");

		HasLocation obj2=(HasLocation) obj;
		obj2.getLocation().add(attr);
	    }
	}
	
    }

    
}
