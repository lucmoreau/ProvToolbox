package org.openprovenance.prov.template;

import java.util.List;
import java.util.Map.Entry;

import org.openprovenance.prov.model.QualifiedName;

public class VariableBinding implements Binding {

    private QualifiedName name;
    private List<QualifiedName> values;

    @Override
    public QualifiedName getQualifiedName() {
	return name;
    }
    
    public List<QualifiedName> getValues() {
	return values;
    }
    
    public VariableBinding(QualifiedName name, List<QualifiedName> values) {
	this.name=name;
	this.values=values;
    }

    public VariableBinding(Entry<QualifiedName, List<QualifiedName>> entry) {
	this(entry.getKey(),entry.getValue());
    }

}
