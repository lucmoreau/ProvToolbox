package org.openprovenance.prov.core.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openprovenance.prov.core.Activity;
import org.openprovenance.prov.core.AlternateOf;
import org.openprovenance.prov.core.HadMember;
import org.openprovenance.prov.core.Agent;

public class ProvMixin {
    public ProvMixin() {
    }

    public void addProvMixin(ObjectMapper mapper) {
        mapper.addMixIn(Activity.class, org.openprovenance.prov.core.jsonld.JLD_Activity.class);
        mapper.addMixIn(HadMember.class, org.openprovenance.prov.core.jsonld.JLD_HadMember.class);
        mapper.addMixIn(Agent.class, org.openprovenance.prov.core.jsonld.JLD_Agent.class);
        mapper.addMixIn(AlternateOf.class, org.openprovenance.prov.core.jsonld.JLD_AlternateOf.class);
    }
}