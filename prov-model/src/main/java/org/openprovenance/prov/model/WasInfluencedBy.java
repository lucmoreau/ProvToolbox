package org.openprovenance.prov.model;

public interface WasInfluencedBy extends Identifiable,  HasLabel, HasType, HasOther, Influence {

    void setInfluencee(QualifiedName influencee);

    void setInfluencer(QualifiedName influencer);

    QualifiedName getInfluencee();

    QualifiedName getInfluencer();

}
