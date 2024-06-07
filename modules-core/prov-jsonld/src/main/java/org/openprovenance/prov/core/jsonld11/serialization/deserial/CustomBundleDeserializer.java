package org.openprovenance.prov.core.jsonld11.serialization.deserial;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.openprovenance.prov.core.jsonld11.serialization.MisnamedBundle;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.vanilla.Bundle;
import org.openprovenance.prov.model.ProvFactory;

import java.io.IOException;

import static org.openprovenance.prov.core.jsonld11.serialization.deserial.CustomThreadConfig.JSONLD_CONTEXT_KEY_NAMESPACE;
import static org.openprovenance.prov.core.jsonld11.serialization.deserial.CustomThreadConfig.getAttributes;

public class CustomBundleDeserializer extends JsonDeserializer<Bundle> {
    static final ProvFactory pf = org.openprovenance.prov.vanilla.ProvFactory.getFactory();

    @Override
    public Bundle deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        Namespace ns= getAttributes().get().get(JSONLD_CONTEXT_KEY_NAMESPACE);
        MisnamedBundle mbun= jsonParser.readValueAs(MisnamedBundle.class);
        mbun.getNamespace().setParent(ns);
        return (Bundle) mbun.toBundle(pf);
    }
}

