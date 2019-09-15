package org.openprovenance.prov.core.jsonld.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openprovenance.prov.core.vanilla.*;

public class ProvMixin {
    public ProvMixin() {
    }

    public void addProvMixin(ObjectMapper mapper) {
        mapper.addMixIn(Document.class,             org.openprovenance.prov.core.jsonld.Document.class);
        mapper.addMixIn(ActedOnBehalfOf.class,      org.openprovenance.prov.core.jsonld.JLD_ActedOnBehalfOf.class);
        mapper.addMixIn(Activity.class,             org.openprovenance.prov.core.jsonld.JLD_Activity.class);
        mapper.addMixIn(HadMember.class,            org.openprovenance.prov.core.jsonld.JLD_HadMember.class);
        mapper.addMixIn(Agent.class,                org.openprovenance.prov.core.jsonld.JLD_Agent.class);
        mapper.addMixIn(AlternateOf.class,          org.openprovenance.prov.core.jsonld.JLD_AlternateOf.class);
        mapper.addMixIn(Entity.class,               org.openprovenance.prov.core.jsonld.JLD_Entity.class);
        mapper.addMixIn(SpecializationOf.class,     org.openprovenance.prov.core.jsonld.JLD_SpecializationOf.class);
        mapper.addMixIn(Used.class,                 org.openprovenance.prov.core.jsonld.JLD_Used.class);
        mapper.addMixIn(WasAssociatedWith.class,    org.openprovenance.prov.core.jsonld.JLD_WasAssociatedWith.class);
        mapper.addMixIn(WasAttributedTo.class,      org.openprovenance.prov.core.jsonld.JLD_WasAttributedTo.class);
        mapper.addMixIn(WasDerivedFrom.class,       org.openprovenance.prov.core.jsonld.JLD_WasDerivedFrom.class);
        mapper.addMixIn(WasEndedBy.class,           org.openprovenance.prov.core.jsonld.JLD_WasEndedBy.class);
        mapper.addMixIn(WasStartedBy.class,         org.openprovenance.prov.core.jsonld.JLD_WasStartedBy.class);
        mapper.addMixIn(WasGeneratedBy.class,       org.openprovenance.prov.core.jsonld.JLD_WasGeneratedBy.class);
        mapper.addMixIn(WasInvalidatedBy.class,     org.openprovenance.prov.core.jsonld.JLD_WasInvalidatedBy.class);
        mapper.addMixIn(WasInformedBy.class,        org.openprovenance.prov.core.jsonld.JLD_WasInformedBy.class);
        mapper.addMixIn(WasInfluencedBy.class,      org.openprovenance.prov.core.jsonld.JLD_WasInfluencedBy.class);
        mapper.addMixIn(LangString.class,           org.openprovenance.prov.core.jsonld.JLD_LangString.class);
        mapper.addMixIn(Role.class,                 org.openprovenance.prov.core.jsonld.JLD_Attribute.class);
        mapper.addMixIn(Label.class,                org.openprovenance.prov.core.jsonld.JLD_Attribute.class);
        mapper.addMixIn(Value.class,                org.openprovenance.prov.core.jsonld.JLD_Attribute.class);
        mapper.addMixIn(Location.class,             org.openprovenance.prov.core.jsonld.JLD_Attribute.class);
        mapper.addMixIn(Other.class,                org.openprovenance.prov.core.jsonld.JLD_Attribute.class);
        mapper.addMixIn(Type.class,                 org.openprovenance.prov.core.jsonld.JLD_Attribute.class);
        mapper.addMixIn(Bundle.class,               org.openprovenance.prov.core.jsonld.JLD_Bundle.class);
        mapper.addMixIn(QualifiedSpecializationOf.class,  org.openprovenance.prov.core.jsonld.JLD_QualifiedSpecializationOf.class);
        mapper.addMixIn(QualifiedAlternateOf.class,       org.openprovenance.prov.core.jsonld.JLD_QualifiedAlternateOf.class);
        mapper.addMixIn(QualifiedHadMember.class,         org.openprovenance.prov.core.jsonld.JLD_QualifiedHadMember.class);

    }
}