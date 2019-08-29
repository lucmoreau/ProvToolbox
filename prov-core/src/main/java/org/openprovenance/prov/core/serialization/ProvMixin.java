package org.openprovenance.prov.core.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openprovenance.prov.core.*;

public class ProvMixin {
    public ProvMixin() {
    }

    public void addProvMixin(ObjectMapper mapper) {
        mapper.addMixIn(Activity.class,             org.openprovenance.prov.core.jsonld.JLD_Activity.class);
        mapper.addMixIn(HadMember.class,            org.openprovenance.prov.core.jsonld.JLD_HadMember.class);
        mapper.addMixIn(Agent.class,                org.openprovenance.prov.core.jsonld.JLD_Agent.class);
        mapper.addMixIn(AlternateOf.class,          org.openprovenance.prov.core.jsonld.JLD_AlternateOf.class);
        mapper.addMixIn(Entity.class,               org.openprovenance.prov.core.jsonld.JLD_Entity.class);
        mapper.addMixIn(SpecializationOf.class,     org.openprovenance.prov.core.jsonld.JLD_SpecializationOf.class);
        mapper.addMixIn(Used.class,                 org.openprovenance.prov.core.jsonld.JLD_Used.class);
    }
}