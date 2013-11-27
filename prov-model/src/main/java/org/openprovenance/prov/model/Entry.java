package org.openprovenance.prov.model;

public interface Entry {

    void setKey(Key key);

    void setEntity(QualifiedName entity);

    Key getKey();

    QualifiedName getEntity();

}
