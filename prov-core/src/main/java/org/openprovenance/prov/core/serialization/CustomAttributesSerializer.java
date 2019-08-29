package org.openprovenance.prov.core.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.openprovenance.prov.core.vanilla.TypedValue;

import java.io.IOException;
import java.util.Set;


public class CustomAttributesSerializer extends StdSerializer<Object> {


    protected CustomAttributesSerializer() {
        super(Object.class);

    }

    protected CustomAttributesSerializer(Class<Object> t) {
        super(t);
    }

    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        Set<TypedValue> set=(Set<TypedValue>) o;
        jsonGenerator.writeStartArray();
        for (TypedValue a: set) {
            new CustomTypedValueSerializer().serialize(a,jsonGenerator,serializerProvider);
        }
        jsonGenerator.writeEndArray();
    }


}