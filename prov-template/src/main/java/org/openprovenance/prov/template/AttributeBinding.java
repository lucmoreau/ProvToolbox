package org.openprovenance.prov.template;

import java.util.List;
import java.util.Map.Entry;

import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.TypedValue;

public class AttributeBinding implements Binding {
    private QualifiedName name;
    private List<List<TypedValue>> values;

    @Override
    public QualifiedName getQualifiedName() {
	return name;
    }
    

    public List<List<TypedValue>> getValues() {
	return values;
    }
    
    public AttributeBinding(QualifiedName name, List<List<TypedValue>> values) {
	this.name=name;
	this.values=values;
    }

    public AttributeBinding(Entry<QualifiedName, List<List<TypedValue>>> entry) {
	this(entry.getKey(),entry.getValue());
    }

}
