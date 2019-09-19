package org.openprovenance.prov.xml;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.Identifiable;

public class AttributeList<TYPE> extends AbstractList<TYPE> {


    private final ArrayList<TYPE> ll=new ArrayList<TYPE>();
  
    private Identifiable obj;

    public AttributeList(Identifiable obj) {
  	//System.out.println("*** Constructor called");
  	this.obj=obj;
      }
    public AttributeList(Identifiable obj, Collection<TYPE> col) {
  	this(obj);
  	ll.addAll(col);
      }

    @Override
    public TYPE get(int index) {
        return ll.get(index);
    }

    @Override
    public TYPE set(int index, TYPE element) {
	//System.out.println("*** AttributeList set " + index);

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
	//System.out.println("*** AttributeList add " + index + " " + element);
	ll.add(index,element);
	HasAllAttributes obj2=(HasAllAttributes)obj;
	obj2.getAllAttributes().add((org.openprovenance.prov.model.Attribute)element);	
    }
    
    final static public <TYPE> AttributeList<TYPE> populateKnownAttributes(Identifiable object,
									   List<Attribute> all,
									   Class<TYPE> cl) {
	List<TYPE> some = new ArrayList<TYPE>();
	if (all != null) {
	    for (Attribute attr : all) {
		if (cl.isInstance(attr)) {
		    some.add((TYPE) attr);
		}
	    }
	}
	return new AttributeList<TYPE>(object, some);
    }
   
    
   
}
