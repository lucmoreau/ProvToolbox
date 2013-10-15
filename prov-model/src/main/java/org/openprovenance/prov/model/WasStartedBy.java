package org.openprovenance.prov.model;

public interface WasStartedBy extends Identifiable, HasLabel, HasTime, HasType,
	HasExtensibility, HasRole, HasLocation, Influence {

    IDRef getActivity();

    void setActivity(IDRef aid);

    IDRef getStarter();

    IDRef getTrigger();


    void setStarter(IDRef sid);

    void setTrigger(IDRef eid);

}
