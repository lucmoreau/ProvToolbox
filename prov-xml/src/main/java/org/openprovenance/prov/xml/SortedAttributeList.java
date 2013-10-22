package org.openprovenance.prov.xml;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;

import org.openprovenance.prov.model.Identifiable;

public class SortedAttributeList<TYPE> extends AbstractList<TYPE> {

    int last_label=-1;
    int last_location=-1;
    int last_role=-1;
    int last_type=-1;
    int last_value=-1;

    private final ArrayList<TYPE> ll=new ArrayList<TYPE>();
  

    public SortedAttributeList() {
  	System.out.println("*** Constructor called");
      }
   
    @Override
    public TYPE get(int index) {
        return ll.get(index);
    }

    @Override
    public TYPE set(int index, TYPE element) {
	System.out.println("*** SortedAttributeList set " + index);

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
	System.out.println("*** SortedAttributeList add " + index + " " + element);
	ll.add(index,element);
    }

    
}
