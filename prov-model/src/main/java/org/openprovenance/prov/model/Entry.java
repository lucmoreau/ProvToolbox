package org.openprovenance.prov.model;

public interface Entry {

    void setKey(Object key);

    void setEntity(IDRef entity);

    Object getKey();

    IDRef getEntity();

}
