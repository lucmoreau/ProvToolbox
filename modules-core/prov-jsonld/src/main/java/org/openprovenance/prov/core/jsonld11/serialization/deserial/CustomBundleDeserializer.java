package org.openprovenance.prov.core.jsonld11.serialization.deserial;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.openprovenance.prov.core.jsonld11.serialization.MisnamedBundle;
import org.openprovenance.prov.vanilla.Bundle;
import org.openprovenance.prov.model.ProvFactory;

import java.io.IOException;

public class CustomBundleDeserializer extends JsonDeserializer<Bundle> {
    ProvFactory pf = org.openprovenance.prov.vanilla.ProvFactory.getFactory();

    @Override
    public Bundle deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        MisnamedBundle mbun= jsonParser.readValueAs(MisnamedBundle.class);
        return (Bundle) mbun.toBundle(pf);
    }
}

