package org.openprovenance.prov.model;


public interface WasAssociatedWith extends Identifiable,  HasLabel, HasType, HasRole, HasOther, Influence {

    void setActivity(QualifiedName eid2);

    void setAgent(QualifiedName eid1);

    void setPlan(QualifiedName eid);

    QualifiedName getActivity();

    QualifiedName getAgent();

    QualifiedName getPlan();

}
