package org.openprovenance.prov.vanilla;

import org.openprovenance.prov.model.Key;
import org.openprovenance.prov.model.QualifiedName;

public class Entry implements org.openprovenance.prov.model.Entry {

    private Key key;
    private QualifiedName entity;

    public Entry(Key key, QualifiedName entity) {
        this.key=key;
        this.entity=entity;
    }

    @Override
    public void setKey(Key key) {
        this.key=key;
    }

    @Override
    public void setEntity(QualifiedName entity) {
        this.entity=entity;
    }

    @Override
    public Key getKey() {
        return key;
    }

    @Override
    public QualifiedName getEntity() {
        return entity;
    }
}
