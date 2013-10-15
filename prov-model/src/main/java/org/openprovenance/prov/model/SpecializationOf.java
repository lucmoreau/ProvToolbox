package org.openprovenance.prov.model;

public interface SpecializationOf extends Relation0 {

    void setSpecificEntity(IDRef eid2);

    void setGeneralEntity(IDRef eid1);

    IDRef getGeneralEntity();

    IDRef getSpecificEntity();

}
