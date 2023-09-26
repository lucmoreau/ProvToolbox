package org.openprovenance.prov.core.xml.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openprovenance.prov.core.xml.serialization.serial.WrapperBundle;
import org.openprovenance.prov.core.xml.serialization.serial.WrapperDocument;
import org.openprovenance.prov.vanilla.*;

public class ProvMixin {
    public ProvMixin() {
    }

    public void addProvMixin(ObjectMapper mapper) {
        mapper.addMixIn(org.openprovenance.prov.vanilla.Document.class,             org.openprovenance.prov.core.xml.Document.class);
        mapper.addMixIn(WrapperDocument.class,      org.openprovenance.prov.core.xml.Document.class);
//        /mapper.addMixIn(WrapperBundle.class,        org.openprovenance.prov.core.xml.XML_Bundle.class);
        mapper.addMixIn(ActedOnBehalfOf.class,      org.openprovenance.prov.core.xml.XML_ActedOnBehalfOf.class);
        mapper.addMixIn(Activity.class,             org.openprovenance.prov.core.xml.XML_Activity.class);
        mapper.addMixIn(HadMember.class,            org.openprovenance.prov.core.xml.XML_HadMember.class);
        mapper.addMixIn(Agent.class,                org.openprovenance.prov.core.xml.XML_Agent.class);
        mapper.addMixIn(AlternateOf.class,          org.openprovenance.prov.core.xml.XML_AlternateOf.class);
        mapper.addMixIn(Entity.class,               org.openprovenance.prov.core.xml.XML_Entity.class);
        mapper.addMixIn(SpecializationOf.class,     org.openprovenance.prov.core.xml.XML_SpecializationOf.class);
        mapper.addMixIn(Used.class,                 org.openprovenance.prov.core.xml.XML_Used.class);
        mapper.addMixIn(WasAssociatedWith.class,    org.openprovenance.prov.core.xml.XML_WasAssociatedWith.class);
        mapper.addMixIn(WasAttributedTo.class,      org.openprovenance.prov.core.xml.XML_WasAttributedTo.class);
        mapper.addMixIn(WasDerivedFrom.class,       org.openprovenance.prov.core.xml.XML_WasDerivedFrom.class);
        mapper.addMixIn(WasEndedBy.class,           org.openprovenance.prov.core.xml.XML_WasEndedBy.class);
        mapper.addMixIn(WasStartedBy.class,         org.openprovenance.prov.core.xml.XML_WasStartedBy.class);
        mapper.addMixIn(WasGeneratedBy.class,       org.openprovenance.prov.core.xml.XML_WasGeneratedBy.class);
        mapper.addMixIn(WasInvalidatedBy.class,     org.openprovenance.prov.core.xml.XML_WasInvalidatedBy.class);
        mapper.addMixIn(WasInformedBy.class,        org.openprovenance.prov.core.xml.XML_WasInformedBy.class);
        mapper.addMixIn(WasInfluencedBy.class,      org.openprovenance.prov.core.xml.XML_WasInfluencedBy.class);
      //  mapper.addMixIn(LangString.class,           org.openprovenance.prov.core.xml.XML_LangString.class);
        mapper.addMixIn(Role.class,                 org.openprovenance.prov.core.xml.XML_Attribute.class);
        mapper.addMixIn(Label.class,                org.openprovenance.prov.core.xml.XML_Attribute.class);
        mapper.addMixIn(Value.class,                org.openprovenance.prov.core.xml.XML_Attribute.class);
        mapper.addMixIn(Location.class,             org.openprovenance.prov.core.xml.XML_Attribute.class);
        mapper.addMixIn(Other.class,                org.openprovenance.prov.core.xml.XML_Attribute.class);
        mapper.addMixIn(Type.class,                 org.openprovenance.prov.core.xml.XML_Attribute.class);
        mapper.addMixIn(Bundle.class,               org.openprovenance.prov.core.xml.XML_Bundle.class);

        mapper.addMixIn(QualifiedSpecializationOf.class,  org.openprovenance.prov.core.xml.XML_QualifiedSpecializationOf.class);
        mapper.addMixIn(QualifiedAlternateOf.class,       org.openprovenance.prov.core.xml.XML_QualifiedAlternateOf.class);
        mapper.addMixIn(QualifiedHadMember.class,         org.openprovenance.prov.core.xml.XML_QualifiedHadMember.class);

    }
}