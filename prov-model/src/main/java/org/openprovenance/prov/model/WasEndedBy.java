package org.openprovenance.prov.model;

public interface WasEndedBy  extends Identifiable,  HasLabel, HasTime, HasType, HasExtensibility, HasRole, HasLocation, HasOtherAttribute, Influence {

    void setActivity(IDRef aid);

    void setTrigger(IDRef eid);

    void setEnder(IDRef sid);

    IDRef getActivity();

    IDRef getTrigger();

    IDRef getEnder();

}
