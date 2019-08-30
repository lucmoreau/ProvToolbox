package org.openprovenance.prov.core.xml.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openprovenance.prov.core.vanilla.*;
import org.openprovenance.prov.core.vanilla.Document;
import org.openprovenance.prov.core.xml.*;

public class ProvMixin {
    public ProvMixin() {
    }

    public void addProvMixin(ObjectMapper mapper) {
        mapper.addMixIn(Document.class,             org.openprovenance.prov.core.xml.Document.class);
        mapper.addMixIn(ActedOnBehalfOf.class,      XML_ActedOnBehalfOf.class);
        mapper.addMixIn(Activity.class,             XML_Activity.class);
        mapper.addMixIn(HadMember.class,            XML_HadMember.class);
        mapper.addMixIn(Agent.class,                XML_Agent.class);
        mapper.addMixIn(AlternateOf.class,          XML_AlternateOf.class);
        mapper.addMixIn(Entity.class,               XML_Entity.class);
        mapper.addMixIn(SpecializationOf.class,     XML_SpecializationOf.class);
        mapper.addMixIn(Used.class,                 XML_Used.class);
        mapper.addMixIn(WasAssociatedWith.class,    XML_WasAssociatedWith.class);
        mapper.addMixIn(WasAttributedTo.class,      XML_WasAttributedTo.class);
        mapper.addMixIn(WasDerivedFrom.class,       XML_WasDerivedFrom.class);
        mapper.addMixIn(WasEndedBy.class,           XML_WasEndedBy.class);
        mapper.addMixIn(WasStartedBy.class,         XML_WasStartedBy.class);
        mapper.addMixIn(WasGeneratedBy.class,       XML_WasGeneratedBy.class);
        mapper.addMixIn(WasInvalidatedBy.class,     XML_WasInvalidatedBy.class);
        mapper.addMixIn(WasInformedBy.class,        XML_WasInformedBy.class);
        mapper.addMixIn(WasInfluencedBy.class,      XML_WasInfluencedBy.class);
        mapper.addMixIn(LangString.class,           XML_LangString.class);
    }
}