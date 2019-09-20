package org.openprovenance.prov.core.json.serialization.serial;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.openprovenance.prov.core.json.serialization.SortedBundle;
import org.openprovenance.prov.vanilla.Bundle;

import java.io.IOException;

public class CustomBundleSerializer extends JsonSerializer<Bundle> {
    @Override
    public void serialize(Bundle bun, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeObject(new SortedBundle(bun));
    }
}
