package org.openprovenance.prov.model;

public interface AlternateOf extends Relation0 {

    void setAlternate1(IDRef eid2);

    void setAlternate2(IDRef eid1);

    IDRef getAlternate1();

    IDRef getAlternate2();

}
