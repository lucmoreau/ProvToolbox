package org.openprovenance.prov.core.json.serialization.deserial;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.openprovenance.prov.core.json.serialization.SortedBundle;
import org.openprovenance.prov.core.vanilla.Bundle;
import org.openprovenance.prov.model.ProvFactory;

import java.io.IOException;

public class CustomBundleDeserializer extends JsonDeserializer<Bundle> {
    ProvFactory pf = org.openprovenance.prov.core.vanilla.ProvFactory.getFactory();

    @Override
    public Bundle deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        SortedBundle sbun= jsonParser.readValueAs(SortedBundle.class);
        return (Bundle) sbun.toBundle(pf);
    }
}
