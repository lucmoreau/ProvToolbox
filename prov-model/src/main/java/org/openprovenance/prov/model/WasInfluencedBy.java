package org.openprovenance.prov.model;

import java.util.List;

public interface WasInfluencedBy extends Identifiable,  HasLabel, HasType, HasExtensibility, Influence {

    void setInfluencee(IDRef influencee);

    void setInfluencer(IDRef influencer);

    IDRef getInfluencee();

    IDRef getInfluencer();

}
