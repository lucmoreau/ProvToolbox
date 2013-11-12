package org.openprovenance.prov.model;

public interface WasInvalidatedBy extends Identifiable,  HasLabel, HasTime, HasType, HasRole, HasLocation, HasOtherAttribute,  Influence{

    void setActivity(IDRef aid);

    void setEntity(IDRef eid);

    IDRef getEntity();

    IDRef getActivity();

}
