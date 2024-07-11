package org.openprovenance.prov.core.jsonld11.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openprovenance.prov.core.jsonld11.JLD_Document;
import org.openprovenance.prov.vanilla.*;

public class ProvMixin {//extends org.openprovenance.prov.core.json.serialization.ProvMixin {

    public ProvMixin() {
    }

    public void addProvMixin(ObjectMapper mapper) {
        mapper.addMixIn(org.openprovenance.prov.vanilla.Document.class,             JLD_Document.class);
        mapper.addMixIn(ActedOnBehalfOf.class,      org.openprovenance.prov.core.jsonld11.JLD_ActedOnBehalfOf.class);
        mapper.addMixIn(Activity.class,             org.openprovenance.prov.core.jsonld11.JLD_Activity.class);
        mapper.addMixIn(HadMember.class,            org.openprovenance.prov.core.jsonld11.JLD_HadMember.class);
        mapper.addMixIn(Agent.class,                org.openprovenance.prov.core.jsonld11.JLD_Agent.class);
        mapper.addMixIn(AlternateOf.class,          org.openprovenance.prov.core.jsonld11.JLD_AlternateOf.class);
        mapper.addMixIn(Entity.class,               org.openprovenance.prov.core.jsonld11.JLD_Entity.class);
        mapper.addMixIn(SpecializationOf.class,     org.openprovenance.prov.core.jsonld11.JLD_SpecializationOf.class);
        mapper.addMixIn(Used.class,                 org.openprovenance.prov.core.jsonld11.JLD_Used.class);
        mapper.addMixIn(WasAssociatedWith.class,    org.openprovenance.prov.core.jsonld11.JLD_WasAssociatedWith.class);
        mapper.addMixIn(WasAttributedTo.class,      org.openprovenance.prov.core.jsonld11.JLD_WasAttributedTo.class);
        mapper.addMixIn(WasDerivedFrom.class,       org.openprovenance.prov.core.jsonld11.JLD_WasDerivedFrom.class);
        mapper.addMixIn(WasEndedBy.class,           org.openprovenance.prov.core.jsonld11.JLD_WasEndedBy.class);
        mapper.addMixIn(WasStartedBy.class,         org.openprovenance.prov.core.jsonld11.JLD_WasStartedBy.class);
        mapper.addMixIn(WasGeneratedBy.class,       org.openprovenance.prov.core.jsonld11.JLD_WasGeneratedBy.class);
        mapper.addMixIn(WasInvalidatedBy.class,     org.openprovenance.prov.core.jsonld11.JLD_WasInvalidatedBy.class);
        mapper.addMixIn(WasInformedBy.class,        org.openprovenance.prov.core.jsonld11.JLD_WasInformedBy.class);
        mapper.addMixIn(WasInfluencedBy.class,      org.openprovenance.prov.core.jsonld11.JLD_WasInfluencedBy.class);
        mapper.addMixIn(LangString.class,           org.openprovenance.prov.core.jsonld11.JLD_LangString.class);
        mapper.addMixIn(Role.class,                 org.openprovenance.prov.core.jsonld11.JLD_Attribute.class);
        mapper.addMixIn(Label.class,                org.openprovenance.prov.core.jsonld11.JLD_Attribute.class);
        mapper.addMixIn(Value.class,                org.openprovenance.prov.core.jsonld11.JLD_Attribute.class);
        mapper.addMixIn(Location.class,             org.openprovenance.prov.core.jsonld11.JLD_Attribute.class);
        mapper.addMixIn(Other.class,                org.openprovenance.prov.core.jsonld11.JLD_Attribute.class);
        mapper.addMixIn(Type.class,                 org.openprovenance.prov.core.jsonld11.JLD_Attribute.class);
        mapper.addMixIn(TypedValue.class,           org.openprovenance.prov.core.jsonld11.JLD_Attribute.class);
        mapper.addMixIn(Bundle.class,               org.openprovenance.prov.core.jsonld11.JLD_Bundle.class);
        mapper.addMixIn(MisnamedBundle.class,       org.openprovenance.prov.core.jsonld11.JLD_Bundle.class);
        mapper.addMixIn(QualifiedSpecializationOf.class,  org.openprovenance.prov.core.jsonld11.JLD_QualifiedSpecializationOf.class);
        mapper.addMixIn(QualifiedAlternateOf.class,       org.openprovenance.prov.core.jsonld11.JLD_QualifiedAlternateOf.class);
        mapper.addMixIn(QualifiedHadMember.class,         org.openprovenance.prov.core.jsonld11.JLD_QualifiedHadMember.class);

    }
}