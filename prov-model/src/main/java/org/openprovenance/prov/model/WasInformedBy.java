package org.openprovenance.prov.model;

public interface WasInformedBy extends Identifiable,  HasLabel, HasType, HasExtensibility, Influence{

    void setInformed(IDRef pid1);

    void setInformant(IDRef pid2);

    IDRef getInformed();

    IDRef getInformant();

}
