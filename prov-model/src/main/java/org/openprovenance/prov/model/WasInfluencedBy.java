package org.openprovenance.prov.model;

public interface WasInfluencedBy extends Identifiable,  HasLabel, HasType, HasOther, Influence {

    void setInfluencee(IDRef influencee);

    void setInfluencer(IDRef influencer);

    IDRef getInfluencee();

    IDRef getInfluencer();

}
