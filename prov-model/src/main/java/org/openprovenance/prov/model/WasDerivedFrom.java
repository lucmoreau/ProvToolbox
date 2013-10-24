package org.openprovenance.prov.model;

public interface WasDerivedFrom  extends Identifiable,  HasLabel, HasType, HasExtensibility, HasOtherAttribute, Influence {

    void setUsedEntity(IDRef aid2);

    void setGeneratedEntity(IDRef aid1);

    void setActivity(IDRef aid);

    void setGeneration(IDRef did1);

    void setUsage(IDRef did2);

    IDRef getGeneratedEntity();

    IDRef getGeneration();

    IDRef getUsedEntity();

    IDRef getUsage();

    Ref getActivity();

}
