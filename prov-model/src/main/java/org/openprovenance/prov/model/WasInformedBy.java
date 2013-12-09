package org.openprovenance.prov.model;

public interface WasInformedBy extends Identifiable,  HasLabel, HasType, HasOther, Influence {

    void setInformed(QualifiedName pid1);

    void setInformant(QualifiedName pid2);

    QualifiedName getInformed();

    QualifiedName getInformant();

}
