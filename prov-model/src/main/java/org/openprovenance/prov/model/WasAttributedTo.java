package org.openprovenance.prov.model;

import java.util.List;

public interface WasAttributedTo  extends Identifiable,  HasLabel, HasType, HasExtensibility, Influence {

    void setEntity(IDRef eid);

    void setAgent(IDRef agid);

    IDRef getEntity();

    IDRef getAgent();

}
