package org.openprovenance.prov.core.json.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openprovenance.prov.core.vanilla.*;

public class ProvMixin {
    public ProvMixin() {
    }

    public void addProvMixin(ObjectMapper mapper) {
        mapper.addMixIn(Document.class,             org.openprovenance.prov.core.json.Document.class);
        mapper.addMixIn(ActedOnBehalfOf.class,      org.openprovenance.prov.core.json.JSON_ActedOnBehalfOf.class);
        mapper.addMixIn(Activity.class,             org.openprovenance.prov.core.json.JSON_Activity.class);
        mapper.addMixIn(HadMember.class,            org.openprovenance.prov.core.json.JSON_HadMember.class);
        mapper.addMixIn(Agent.class,                org.openprovenance.prov.core.json.JSON_Agent.class);
        mapper.addMixIn(AlternateOf.class,          org.openprovenance.prov.core.json.JSON_AlternateOf.class);
        mapper.addMixIn(Entity.class,               org.openprovenance.prov.core.json.JSON_Entity.class);
        mapper.addMixIn(SpecializationOf.class,     org.openprovenance.prov.core.json.JSON_SpecializationOf.class);
        mapper.addMixIn(Used.class,                 org.openprovenance.prov.core.json.JSON_Used.class);
        mapper.addMixIn(WasAssociatedWith.class,    org.openprovenance.prov.core.json.JSON_WasAssociatedWith.class);
        mapper.addMixIn(WasAttributedTo.class,      org.openprovenance.prov.core.json.JSON_WasAttributedTo.class);
        mapper.addMixIn(WasDerivedFrom.class,       org.openprovenance.prov.core.json.JSON_WasDerivedFrom.class);
        mapper.addMixIn(WasEndedBy.class,           org.openprovenance.prov.core.json.JSON_WasEndedBy.class);
        mapper.addMixIn(WasStartedBy.class,         org.openprovenance.prov.core.json.JSON_WasStartedBy.class);
        mapper.addMixIn(WasGeneratedBy.class,       org.openprovenance.prov.core.json.JSON_WasGeneratedBy.class);
        mapper.addMixIn(WasInvalidatedBy.class,     org.openprovenance.prov.core.json.JSON_WasInvalidatedBy.class);
        mapper.addMixIn(WasInformedBy.class,        org.openprovenance.prov.core.json.JSON_WasInformedBy.class);
        mapper.addMixIn(WasInfluencedBy.class,      org.openprovenance.prov.core.json.JSON_WasInfluencedBy.class);
        mapper.addMixIn(LangString.class,           org.openprovenance.prov.core.json.JSON_LangString.class);
    }
}