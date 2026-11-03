package org.openprovenance.prov.scala.jsonld11.serialization.serial;

import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.ser.std.StdSerializer;
import org.openprovenance.prov.scala.immutable.TypedValue;
import tools.jackson.databind.SerializationContext;

import java.util.Set;


public class CustomAttributesSerializer extends StdSerializer<Object> {


    protected CustomAttributesSerializer() {
        super(Object.class);

    }

    protected CustomAttributesSerializer(Class<Object> t) {
        super(t);
    }

    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializationContext serializerProvider) {
        String newKey=(String)serializerProvider.getAttribute(CustomMapSerializer2.CONTEXT_KEY_FOR_MAP);

        Set<TypedValue> set=(Set<TypedValue>) o;
        if (!(set.isEmpty())) {
            jsonGenerator.writeName(newKey);
            jsonGenerator.writeStartArray();
            for (TypedValue a : set) {
                new CustomTypedValueSerializer().serialize(a, jsonGenerator, serializerProvider);
            }
            jsonGenerator.writeEndArray();
        }
    }


}