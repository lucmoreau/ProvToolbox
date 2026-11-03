package org.openprovenance.prov.summary;

import java.util.Map;
import java.util.Set;

public class SummaryDescriptionBean {
	@Override
	public String toString() {
		return "SummaryDescriptionBean [typeStrings=" + typeStrings + ", names=" + names + ", base=" + base
				+ ", prettyNames=" + prettyNames + ", prefixes=" + prefixes + ", nsBase=" + nsBase + "]";
	}
	
	public Map<Integer,String> typeStrings;
	public Map<String,Integer> names;
	public Map<Integer,Set<String>> base;
	public Map<Integer,String> prettyNames;
	public Map<String,String> prefixes;
	public String nsBase;
	
	public String getNsBase() {
		return nsBase;
	}
	public void setNsBase(String nsBase) {
		this.nsBase = nsBase;
	}
	public Map<Integer, String> getTypeStrings() {
		return typeStrings;
	}
	public void setTypeStrings(Map<Integer, String> typeStrings) {
		this.typeStrings = typeStrings;
	}
	public Map<String, Integer> getNames() {
		return names;
	}
	public void setNames(Map<String, Integer> names) {
		this.names = names;
	}
	public Map<Integer, Set<String>> getBase() {
		return base;
	}
	public void setBase(Map<Integer, Set<String>> base) {
		this.base = base;
	}
	public Map<Integer, String> getPrettyNames() {
		return prettyNames;
	}
	public void setPrettyNames(Map<Integer, String> prettyNames) {
		this.prettyNames = prettyNames;
	}
	public Map<String, String> getPrefixes() {
		return prefixes;
	}
	public void setPrefixes(Map<String, String> prefixes) {
		this.prefixes = prefixes;
	}
}
