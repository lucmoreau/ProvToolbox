package org.openprovenance.prov.core.jsonld11.serialization.deserial;


import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import org.openprovenance.prov.core.jsonld11.serialization.MisnamedBundle;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.vanilla.Bundle;
import tools.jackson.databind.ValueDeserializer;
import org.openprovenance.prov.model.ProvFactory;

import static org.openprovenance.prov.core.jsonld11.serialization.deserial.CustomThreadConfig.JSONLD_CONTEXT_KEY_NAMESPACE;
import static org.openprovenance.prov.core.jsonld11.serialization.deserial.CustomThreadConfig.getAttributes;

public class CustomBundleDeserializer extends ValueDeserializer<Bundle> {
    static final ProvFactory pf = org.openprovenance.prov.vanilla.ProvFactory.getFactory();

    @Override
    public Bundle deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) {
        Namespace ns= getAttributes().get().get(JSONLD_CONTEXT_KEY_NAMESPACE);
        MisnamedBundle mbun= deserializationContext.readValue(jsonParser, MisnamedBundle.class);
        mbun.getNamespace().setParent(ns);
        return (Bundle) mbun.toBundle(pf);
    }
}

