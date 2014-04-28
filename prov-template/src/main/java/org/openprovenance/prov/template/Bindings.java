package org.openprovenance.prov.template;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.TypedValue;

public class Bindings {
    
    final private Hashtable<QualifiedName, List<QualifiedName>> variables;
    final private Hashtable<QualifiedName, List<List<TypedValue>>> attributes;
    
    
    public Bindings() {
	this(new Hashtable<QualifiedName, List<QualifiedName>>(), 
	     new Hashtable<QualifiedName, List<List<TypedValue>>>());
    }
    
    public Bindings(Hashtable<QualifiedName, List<QualifiedName>> variables,
                    Hashtable<QualifiedName, List<List<TypedValue>>> attributes) {
	this.variables=variables;
	this.attributes=attributes;
    }



    public Hashtable<QualifiedName, List<QualifiedName>> getVariables() {
	return variables;
    }



    public Hashtable<QualifiedName, List<List<TypedValue>>> getAttributes() {
	return attributes;
    }
    
    
    public void addVariable(QualifiedName name, QualifiedName val) {
	List<QualifiedName> v=variables.get(name);
	if (v==null) {
	    variables.put(name, new LinkedList<QualifiedName>());
	}
	variables.get(name).add(val);
    }
    

    @Override
    public String toString () {
	return "[" + getVariables() + " -- " + getAttributes() + "]";
    }


    
	
}