package org.openprovenance.prov.model;

public interface WasInvalidatedBy extends Identifiable,  HasLabel, HasTime, HasType, HasExtensibility, HasRole, HasLocation, Influence{

    void setActivity(IDRef aid);

    void setEntity(IDRef eid);

    IDRef getEntity();

    IDRef getActivity();

}
