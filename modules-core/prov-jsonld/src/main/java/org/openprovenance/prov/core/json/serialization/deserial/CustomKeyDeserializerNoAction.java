package org.openprovenance.prov.core.json.serialization.deserial;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import org.openprovenance.prov.model.QualifiedName;

import java.io.IOException;

public class CustomKeyDeserializerNoAction extends KeyDeserializer {
    final private CustomDeferredQualifiedNameDeserializer customDeferredQualifiedNameDeserializer = new CustomDeferredQualifiedNameDeserializer();

    @Override
    public Object deserializeKey(String s, DeserializationContext deserializationContext) throws IOException {
        return customDeferredQualifiedNameDeserializer.deserialize(s, deserializationContext);
    }
}
