package org.openprovenance.prov.model.builder;

import org.openprovenance.prov.model.ModelConstructor;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;

public class WasInfluencedByBuilder extends GenericBuilder<WasInfluencedByBuilder>{
    protected QualifiedName influencee;
    protected QualifiedName influencer;

    public WasInfluencedByBuilder(Builder builder, ModelConstructor mc, ProvFactory pf) {
        super(builder,mc,pf);
    }

    public WasInfluencedByBuilder influencee(QualifiedName influencee) {
        this.influencee =influencee;
        return this;
    }
    public WasInfluencedByBuilder influencee(String prefix, String local) {
        this.influencee =qn(prefix,local);
        return this;
    }
    public WasInfluencedByBuilder influencee(String knownAs) {
        QualifiedName qn=parent.knownAs.get(knownAs);
        if (qn==null) throw new NullPointerException("informed cannot find knownAs " + knownAs + " in " + parent.knownAs.keySet());
        this.influencee =qn;
        return this;
    }

    public WasInfluencedByBuilder influencer(QualifiedName influencer) {
        this.influencer=influencer;
        return this;
    }
    public WasInfluencedByBuilder influencer(String prefix, String local) {
        this.influencer=qn(prefix,local);
        return this;
    }
    public WasInfluencedByBuilder influencer(String knownAs) {
        QualifiedName qn=parent.knownAs.get(knownAs);
        if (qn==null) throw new NullPointerException("informant: cannot find knownAs " + knownAs + " in " + parent.knownAs.keySet());
        this.influencer=qn;
        return this;
    }

    public Builder build() {
        parent.statements.add(mc.newWasInfluencedBy(id, influencee, influencer, attrs));
        return parent;
    }



}
