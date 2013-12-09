package org.openprovenance.prov.model;

public interface SpecializationOf extends Relation0 {

    void setSpecificEntity(QualifiedName eid2);

    void setGeneralEntity(QualifiedName eid1);

    QualifiedName getGeneralEntity();

    QualifiedName getSpecificEntity();

}
