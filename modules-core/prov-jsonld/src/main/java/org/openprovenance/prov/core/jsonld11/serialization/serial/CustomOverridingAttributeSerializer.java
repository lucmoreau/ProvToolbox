package org.openprovenance.prov.core.jsonld11.serialization.serial;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.openprovenance.prov.vanilla.QualifiedName;
import org.openprovenance.prov.vanilla.TypedValue;

import java.awt.*;
import java.io.IOException;

import static org.openprovenance.prov.core.jsonld11.serialization.serial.CustomAttributeValueSerializer.prnt;

// See https://www.baeldung.com/jackson-serialize-field-custom-criteria#2-custom-serializer

public class CustomOverridingAttributeSerializer extends JsonSerializer<TypedValue> {

    private final JsonSerializer<TypedValue> defaultSerializer;

    public CustomOverridingAttributeSerializer(JsonSerializer<TypedValue> serializer) {
        defaultSerializer = serializer;
    }

    @Override
    public void serialize(TypedValue o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        if (o.getValue() instanceof QualifiedName) {
            jsonGenerator.writeString(prnt((QualifiedName) o.getValue()));
        } else {
            defaultSerializer.serialize(o,jsonGenerator,serializerProvider);
        }
    }
}
