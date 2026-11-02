package org.openprovenance.prov.nf.xml;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.openprovenance.prov.model.StatementOrBundle;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface Statement extends Comparable<Statement> {
	public void normalize();
	
	@JsonIgnore
	public StatementOrBundle.Kind	getKind();
	
	public LinkedList<List<String>> paramsAsListsofStrings();
	
	default <T extends Comparable<T>> int compareLists (List<T> l1, List<T> l2) {
		int len1 = l1.size();
		int len2 = l2.size();
		int len=Math.min(len1,len2);
		for (int i=0; i<len; i++) {
			int comp=l1.get(i).compareTo(l2.get(i));
			if (comp!=0) return comp;	
		}
		return Integer.compare(len2, len1);
	}
	
	default <T extends Comparable<T>> int compareListsOfLists (LinkedList<List<T>> l1, LinkedList<List<T>> l2) {
		while (true) {
			if (l1.isEmpty()) {
				if (l2.isEmpty()) {
					return 0;
				} else {
					return -1;
				}

			} else {
				if (l2.isEmpty()) {
					return 1;
				} else {
					List<T> v1=l1.removeFirst();
					List<T> v2=l2.removeFirst();
					int comp=compareLists(v1,v2);
					if (comp==0) {
						// iterate with l1 and l2
					} else {
						return comp;
					}
				}
			}
		}
	}
	
	
	public List<Attr> getAttr();
	
	@Override
	default public int compareTo(Statement o) {
		int comp=this.getKind().compareTo(o.getKind());
		if (comp!=0) {
			return comp;
		} else {
			LinkedList<List<String>> l1 = this.paramsAsListsofStrings();
			LinkedList<List<String>> l2 = o.paramsAsListsofStrings();
			int comp_l=compareListsOfLists(l1,l2);
			if (comp_l!=0) {
				return comp_l;
			} else {
				return compareLists(this.getAttr(),o.getAttr());
			}
		}
	}
	
	default <T extends Comparable<T>> void doSort(List<T> l) {
		if ((l!=null) && (!l.isEmpty())) {
			System.out.println(l);
			Collections.sort(l);
		}
	}
}
