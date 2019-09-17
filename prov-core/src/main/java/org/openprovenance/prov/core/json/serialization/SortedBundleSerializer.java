package org.openprovenance.prov.core.json.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.openprovenance.prov.core.vanilla.Bundle;

import java.io.IOException;

public class SortedBundleSerializer extends StdSerializer<Bundle> {

    SortedBundleSerializer() {
        super(Bundle.class);
    }

    protected SortedBundleSerializer(Class<Bundle> t) {
        super(t);
    }

    @Override
    public void serialize(Bundle bundle, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeObject(new SortedBundle(bundle));
    }
}
