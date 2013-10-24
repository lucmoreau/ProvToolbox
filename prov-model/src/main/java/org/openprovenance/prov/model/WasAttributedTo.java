package org.openprovenance.prov.model;

public interface WasAttributedTo  extends Identifiable,  HasLabel, HasType, HasExtensibility, HasOtherAttribute, Influence {

    void setEntity(IDRef eid);

    void setAgent(IDRef agid);

    IDRef getEntity();

    IDRef getAgent();

}
