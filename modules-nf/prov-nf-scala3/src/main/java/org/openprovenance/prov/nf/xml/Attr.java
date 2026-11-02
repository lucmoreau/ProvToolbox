package org.openprovenance.prov.nf.xml;

public class Attr implements Comparable<Attr>{
	public String element;
	public String value;
	public String type;
	@Override
	public int compareTo(Attr o) {
		int comp_e=this.element.compareTo(o.element);
		if (comp_e!=0) return comp_e;
		int comp_t=this.type.compareTo(o.type);
		if (comp_t!=0) return comp_t;
		int comp_v=this.value.compareTo(o.value);
		return comp_v;
	}
}
