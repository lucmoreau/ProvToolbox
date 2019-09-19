package org.openprovenance.prov.core.json.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.openprovenance.prov.vanilla.Bundle;
import org.openprovenance.prov.vanilla.ProvFactory;

import java.io.IOException;

public class SortedBundleDeserializer extends StdDeserializer<Bundle> {

    static ProvFactory pf=new ProvFactory();

    SortedBundleDeserializer() {
        super(Bundle.class);
    }

    @Override
    public Bundle deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        SortedBundle sbun=jsonParser.readValueAs(SortedBundle.class);
        return (Bundle) sbun.toBundle(pf);
    }

    protected SortedBundleDeserializer(Class<Bundle> t) {
        super(t);
    }

}

