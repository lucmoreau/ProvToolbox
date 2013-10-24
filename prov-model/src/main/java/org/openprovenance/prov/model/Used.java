package org.openprovenance.prov.model;

public interface Used extends Identifiable, HasLabel, HasType, HasTime, HasExtensibility, HasRole, HasLocation, HasOtherAttribute, Influence {

    void setActivity(IDRef aid);

    void setEntity(IDRef eid);

    IDRef getEntity();

    IDRef getActivity();


}
