package org.openprovenance.prov.model;

public interface WasAttributedTo  extends Identifiable,  HasLabel, HasType, HasOther, Influence {

    void setEntity(QualifiedName eid);

    void setAgent(QualifiedName agid);

    QualifiedName getEntity();

    QualifiedName getAgent();

}
