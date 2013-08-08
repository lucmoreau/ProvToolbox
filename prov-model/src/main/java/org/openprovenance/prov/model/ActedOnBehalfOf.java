package org.openprovenance.prov.model;


public interface ActedOnBehalfOf extends Identifiable, HasLabel, HasType, HasExtensibility, Influence {

    IDRef getDelegate();

    void setActivity(IDRef eid2);

    void setDelegate(IDRef delegate);

    void setResponsible(IDRef responsible);

    IDRef getResponsible();

    IDRef getActivity();

}
