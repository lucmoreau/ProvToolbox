package org.openprovenance.prov.model;

public interface AlternateOf extends Relation0 {

    void setAlternate1(QualifiedName eid2);

    void setAlternate2(QualifiedName eid1);

    QualifiedName getAlternate1();

    QualifiedName getAlternate2();

}
