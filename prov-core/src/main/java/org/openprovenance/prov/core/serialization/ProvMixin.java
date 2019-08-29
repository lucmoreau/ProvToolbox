package org.openprovenance.prov.core.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openprovenance.prov.core.Activity;
import org.openprovenance.prov.core.HadMember;

public class ProvMixin {
    public ProvMixin() {
    }

    public void addProvMixin(ObjectMapper mapper) {
        mapper.addMixIn(Activity.class, org.openprovenance.prov.core.jsonld.JLD_Activity.class);
        mapper.addMixIn(HadMember.class, org.openprovenance.prov.core.jsonld.JLD_HadMember.class);
    }
}