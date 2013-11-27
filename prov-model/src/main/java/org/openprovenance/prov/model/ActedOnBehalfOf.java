package org.openprovenance.prov.model;


public interface ActedOnBehalfOf extends Identifiable, HasLabel, HasType, HasOther, Influence {

    QualifiedName getDelegate();

    void setActivity(QualifiedName eid2);

    void setDelegate(QualifiedName delegate);

    void setResponsible(QualifiedName responsible);

    QualifiedName getResponsible();

    QualifiedName getActivity();

}
