package org.openprovenance.prov.core.jsonld11.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openprovenance.prov.core.jsonld11.*;
import org.openprovenance.prov.core.vanilla.Document;
import org.openprovenance.prov.core.vanilla.*;

public class ProvMixin extends org.openprovenance.prov.core.jsonld.serialization.ProvMixin {

    public ProvMixin() {
    }

    public void addProvMixin(ObjectMapper mapper) {
        mapper.addMixIn(Document.class,             org.openprovenance.prov.core.jsonld11.Document.class);
        mapper.addMixIn(ActedOnBehalfOf.class,      JLD_ActedOnBehalfOf.class);
        mapper.addMixIn(Activity.class,             JLD_Activity.class);
        mapper.addMixIn(HadMember.class,            JLD_HadMember.class);
        mapper.addMixIn(Agent.class,                JLD_Agent.class);
        mapper.addMixIn(AlternateOf.class,          JLD_AlternateOf.class);
        mapper.addMixIn(Entity.class,               JLD_Entity.class);
        mapper.addMixIn(SpecializationOf.class,     JLD_SpecializationOf.class);
        mapper.addMixIn(Used.class,                 JLD_Used.class);
        mapper.addMixIn(WasAssociatedWith.class,    JLD_WasAssociatedWith.class);
        mapper.addMixIn(WasAttributedTo.class,      JLD_WasAttributedTo.class);
        mapper.addMixIn(WasDerivedFrom.class,       JLD_WasDerivedFrom.class);
        mapper.addMixIn(WasEndedBy.class,           JLD_WasEndedBy.class);
        mapper.addMixIn(WasStartedBy.class,         JLD_WasStartedBy.class);
        mapper.addMixIn(WasGeneratedBy.class,       JLD_WasGeneratedBy.class);
        mapper.addMixIn(WasInvalidatedBy.class,     JLD_WasInvalidatedBy.class);
        mapper.addMixIn(WasInformedBy.class,        JLD_WasInformedBy.class);
        mapper.addMixIn(WasInfluencedBy.class,      JLD_WasInfluencedBy.class);
        mapper.addMixIn(LangString.class,           JLD_LangString.class);
        mapper.addMixIn(Role.class,                 org.openprovenance.prov.core.jsonld11.JLD_Attribute.class);
        mapper.addMixIn(Label.class,                org.openprovenance.prov.core.jsonld11.JLD_Attribute.class);
        mapper.addMixIn(Value.class,                org.openprovenance.prov.core.jsonld11.JLD_Attribute.class);
        mapper.addMixIn(Location.class,             org.openprovenance.prov.core.jsonld11.JLD_Attribute.class);
        mapper.addMixIn(Other.class,                org.openprovenance.prov.core.jsonld11.JLD_Attribute.class);
        mapper.addMixIn(Type.class,                 org.openprovenance.prov.core.jsonld11.JLD_Attribute.class);
        mapper.addMixIn(Bundle.class,               org.openprovenance.prov.core.jsonld11.JLD_Bundle.class);
    }
}