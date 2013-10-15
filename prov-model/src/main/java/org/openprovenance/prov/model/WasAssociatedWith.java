package org.openprovenance.prov.model;


public interface WasAssociatedWith extends Identifiable,  HasLabel, HasType, HasExtensibility, HasRole, Influence {

    void setActivity(IDRef eid2);

    void setAgent(IDRef eid1);

    void setPlan(IDRef eid);

    IDRef getActivity();

    IDRef getAgent();

    IDRef getPlan();

}
