package org.openprovenance.prov.vanilla;


import org.openprovenance.prov.model.QualifiedName;

public class Key extends TypedValue implements org.openprovenance.prov.model.Key {

    public Key(Object o, QualifiedName type) {
        super(type, o);
    }
}
