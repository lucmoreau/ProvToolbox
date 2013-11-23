package org.openprovenance.prov.model;

public interface Entry {

    void setKey(Key key);

    void setEntity(IDRef entity);

    Key getKey();

    IDRef getEntity();

}
