package org.openprovenance.prov.model;

public interface WasGeneratedBy extends Identifiable,  HasLabel, HasTime, HasType, HasExtensibility, HasRole, HasLocation, Influence {

    void setActivity(IDRef pid);

    void setEntity(IDRef aid);

    IDRef getEntity();

    IDRef getActivity();

}
