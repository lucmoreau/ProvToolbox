package org.openprovenance.prov.template;

import java.util.Hashtable;
import java.util.List;

import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.TypedValue;

public class Bindings {
    
    final private Hashtable<QualifiedName, List<TypedValue>> variables;
    final private Hashtable<QualifiedName, List<List<TypedValue>>> parameters;
    
    

    public Bindings(Hashtable<QualifiedName, List<TypedValue>> variables,
                   Hashtable<QualifiedName, List<List<TypedValue>>> parameters) {
	this.variables=variables;
	this.parameters=parameters;
    }



    public Hashtable<QualifiedName, List<TypedValue>> getVariables() {
	return variables;
    }



    public Hashtable<QualifiedName, List<List<TypedValue>>> getParameters() {
	return parameters;
    }

}
