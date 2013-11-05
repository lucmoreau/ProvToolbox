package org.openprovenance.prov.xml;

import java.util.AbstractList;
import java.util.ArrayList;

/** SortedAttributeList is a list of attributes, maintained sorted, as per prov-xml schema: 
 * prov:location, prov:role, prov:type, prov:value, and others.
 * Adding an element to the list with .add(element) or .add(index,element) adds element as 
 * the last attribute of its kind in the list. Furthermore, the SortedAttributeList ensures that 
 * there is a single prov:value attribute.
 * 
 * 
 * @author lavm
 *
 * @param <TYPE>
 */
public class SortedAttributeList<TYPE> extends AbstractList<TYPE> {

    int last_key=-1;
    int last_location=-1;
    int last_role=-1;
    int last_type=-1;
    int last_value=-1;
    int first_value=-1;
    int last_other=-1;

    private final ArrayList<TYPE> ll=new ArrayList<TYPE>();
  

    public SortedAttributeList() {
    }
   
    @Override
    public TYPE get(int index) {
        return ll.get(index);
    }

    @Override
    public TYPE set(int index, TYPE element) {
	//System.out.println("*** SortedAttributeList set " + index);

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
	//System.out.println("*** SortedAttributeList add " + index + " " + element);
	if (element instanceof org.openprovenance.prov.model.Key) {
	    last_key++;
	    ll.add(last_key,element);
	    last_location++;
	    last_role++;
	    last_type++;
	    last_value++; first_value++;
	    last_other++;
	} else if (element instanceof org.openprovenance.prov.model.Location) {
	    last_location++;
	    ll.add(last_location,element);
	    last_role++;
	    last_type++;
	    last_value++; first_value++;
	    last_other++;
	} else if (element instanceof org.openprovenance.prov.model.Role) {
	    last_role++;
	    ll.add(last_role,element);
	    last_type++;
	    last_value++; first_value++;
	    last_other++;
	} else if (element instanceof org.openprovenance.prov.model.Type) {
	    last_type++;
	    ll.add(last_type,element);
	    last_value++; first_value++;
	    last_other++;
	} else if (element instanceof org.openprovenance.prov.model.Value) {
	    if (last_value==first_value) { // there was no value at that point!
		last_value++;
		ll.add(last_value,element);
		last_other++;
	    } else {
		ll.set(last_value, element); // overwrite an existing value
	    }
	} else {
	    // it's not a prov attribute, let's add it at the end
	    last_other++;
	    ll.add(last_other,element); 
	}
    }

    
}
