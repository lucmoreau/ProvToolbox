package org.openprovenance.prov.summary;

import java.util.Map;
import java.util.Set;

import org.openprovenance.prov.scala.summary.Level0Mapper;

public class Level0MapperJson {
	String comment;
	Map<String,String> mapper;
	Set<String> ignore; 
	Set<String> properties;
	

	public String getComment() {
		return comment;
	}


	public void setComment(String comment) {
		this.comment = comment;
	}


	public Map<String, String> getMapper() {
		return mapper;
	}


	public void setMapper(Map<String, String> mapper) {
		this.mapper = mapper;
	}


	public Set<String> getIgnore() {
		return ignore;
	}


	public void setIgnore(Set<String> ignore) {
		this.ignore = ignore;
	}


	public Set<String> getProperties() {
		return properties;
	}


	public void setProperties(Set<String> properties) {
		this.properties = properties;
	}


	@Override
	public String toString() {
		return "Level0MapperJson [comment=" + comment + ", mapper=" + mapper + ", ignore=" + ignore + ", properties="
				+ properties + "]";
	}


}
