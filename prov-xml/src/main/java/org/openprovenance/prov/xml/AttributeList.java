package org.openprovenance.prov.xml;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;

import org.openprovenance.prov.model.Identifiable;

public class AttributeList<TYPE> extends AbstractList<TYPE> {


    private final ArrayList<TYPE> ll=new ArrayList<TYPE>();
  
    private Identifiable obj;

    AttributeList(Identifiable obj) {
  	System.out.println("*** Constructor called");
  	this.obj=obj;
      }
    AttributeList(Identifiable obj, Collection<TYPE> col) {
  	this(obj);
  	ll.addAll(col);
      }

    @Override
    public TYPE get(int index) {
        return ll.get(index);
    }

    @Override
    public TYPE set(int index, TYPE element) {
	System.out.println("*** AttributeList set " + index);

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
	System.out.println("*** AttributeList add " + index + " " + element);
	ll.add(index,element);
	HasAllAttributes obj2=(HasAllAttributes)obj;
	obj2.getAllAttributes().add((org.openprovenance.prov.model.Attribute)element);	
    }

    
}
