package org.openprovenance.prov.core.jsonld11.serialization.serial;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.openprovenance.prov.vanilla.QualifiedName;
import org.openprovenance.prov.vanilla.TypedValue;

import java.io.IOException;

import static org.openprovenance.prov.core.jsonld11.serialization.Constants.PROPERTY_AT_ID;
import static org.openprovenance.prov.core.jsonld11.serialization.serial.CustomAttributeValueSerializer.prnt;
import static org.openprovenance.prov.core.jsonld11.serialization.serial.CustomKeySerializer.ID_COERCED_KEY;

// See https://www.baeldung.com/jackson-serialize-field-custom-criteria#2-custom-serializer

/**
 * A qualified-name value is written as a JSON-LD processor will read it as an IRI: a bare string under a key the
 * context coerces to {@code @id} (as the key serialiser just recorded), a node reference {@code {"@id": ...}} under
 * any other (https://github.com/openprov/prov-jsonld/issues/7).
 */
final public class CustomOverridingAttributeSerializer extends JsonSerializer<TypedValue> {

    private final JsonSerializer<TypedValue> defaultSerializer;

    public CustomOverridingAttributeSerializer(JsonSerializer<TypedValue> serializer) {
        defaultSerializer = serializer;
    }

    @Override
    final public void serialize(TypedValue o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        if (o.getValue() instanceof QualifiedName) {
            final String qn = prnt((QualifiedName) o.getValue());
            if (Boolean.TRUE.equals(serializerProvider.getAttribute(ID_COERCED_KEY))) {
                jsonGenerator.writeString(qn);
            } else {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField(PROPERTY_AT_ID, qn);
                jsonGenerator.writeEndObject();
            }
        } else {
            defaultSerializer.serialize(o,jsonGenerator,serializerProvider);
        }
    }
}
