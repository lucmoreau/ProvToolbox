package org.openprovenance.prov.model;

public interface Entry {

    void setKey(TypedValue key);

    void setEntity(IDRef entity);

    TypedValue getKey();

    IDRef getEntity();

}
