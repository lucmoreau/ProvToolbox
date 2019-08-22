package org.openprovenance.prov.core.serialization;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;

import java.io.IOException;

public class CustomKeyDeserializer extends KeyDeserializer {

    @Override
    public Object deserializeKey(String s, DeserializationContext deserializationContext) throws IOException {
        return new CustomQualifiedNameDeserializer().deserialize(s,deserializationContext);
    }
}
