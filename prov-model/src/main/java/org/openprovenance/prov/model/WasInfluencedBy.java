package org.openprovenance.prov.model;

public interface WasInfluencedBy extends Identifiable,  HasLabel, HasType, HasExtensibility, HasOtherAttribute, Influence {

    void setInfluencee(IDRef influencee);

    void setInfluencer(IDRef influencer);

    IDRef getInfluencee();

    IDRef getInfluencer();

}
