package org.openprovenance.prov.model.builder;

import org.openprovenance.prov.model.ModelConstructor;
import org.openprovenance.prov.model.ProvFactory;

public class AgentBuilder extends GenericBuilder<AgentBuilder>{

    public AgentBuilder(Builder builder, ModelConstructor mc, ProvFactory pf) {
        super(builder,mc,pf);

    }
    public Builder build() {
        parent.statements.add(mc.newEntity(id,attrs));
        return parent;
    }


    public AgentBuilder knownAsLocal() {
        parent.knownAs.put(id.getLocalPart(),id);
        return this;
    }

}